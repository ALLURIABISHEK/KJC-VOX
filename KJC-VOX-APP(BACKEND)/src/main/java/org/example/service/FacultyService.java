package org.example.service;

import org.example.model.Faculty;
import org.example.repository.FacultyRepository;
import org.example.repository.RemovedFacultyRepository;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class FacultyService {
    private final FacultyRepository facultyRepository = new FacultyRepository();
    private final RemovedFacultyRepository removedFacultyRepository = new RemovedFacultyRepository(); // ✅ add this


    public Faculty addFaculty(Faculty faculty) {
        if (faculty.getFacultyId() == null || faculty.getFacultyId().trim().isEmpty()) {
            throw new IllegalArgumentException("Faculty ID cannot be null or empty.");
        }

        return facultyRepository.save(faculty);
    }

    public List<Faculty> getAllFaculty(String sortBy, String sortOrder, int page, int limit) {
        try {
            List<Faculty> allFaculty = facultyRepository.findAll();

            if (sortBy != null && !sortBy.isEmpty()) {
                allFaculty = sortFaculty(allFaculty, sortBy, sortOrder);
            }

            int startIndex = (page - 1) * limit;
            int endIndex = Math.min(startIndex + limit, allFaculty.size());

            if (startIndex >= allFaculty.size()) {
                return new ArrayList<>();
            }

            return allFaculty.subList(startIndex, endIndex);
        } catch (Exception e) {
            System.err.println("Service error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // This method should use MongoDB _id
    public Faculty getFacultyById(String id) {
        try {
            return facultyRepository.findById(id);
        } catch (IllegalArgumentException e) {
            // Re-throw validation errors to controller
            throw e;
        } catch (Exception e) {
            System.err.println("Service error: " + e.getMessage());
            return null;
        }
    }

    // This method uses custom facultyId
    public Faculty getFacultyByFacultyId(String facultyId) {
        try {
            return facultyRepository.findByFacultyId(facultyId);
        } catch (Exception e) {
            System.err.println("Service error: " + e.getMessage());
            return null;
        }
    }

    // Delete using MongoDB _id
    public boolean deleteFaculty(String id) {
        try {
            System.out.println("Service - deleteFaculty called with ID: '" + id + "'");
            return facultyRepository.delete(id);
        } catch (IllegalArgumentException e) {
            // Re-throw validation errors to controller
            System.err.println("Service validation error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Service error: " + e.getMessage());
            return false;
        }
    }
    private final RemovedFacultyRepository removedRepo = new RemovedFacultyRepository();

    public boolean deleteFacultyByEmail(String email) {
        Faculty faculty = facultyRepository.getFacultyByEmail(email);
        if (faculty != null) {
            removedRepo.saveRemovedFaculty(faculty); // store before deleting
            return facultyRepository.deleteByEmail(email);
        }
        return false;
    }

    public Faculty updateFacultyByEmail(String email, Faculty newData) {
        return facultyRepository.updateByEmail(email, newData);
    }

    public List<Faculty> searchFaculty(String fullName, String email, String departmentType,
                                       String fromDate, String toDate) {
        try {
            return facultyRepository.search(fullName, email, departmentType, fromDate, toDate);
        } catch (Exception e) {
            System.err.println("Service error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Map<String, Object> getDepartmentStats() {
        try {
            List<Faculty> allFaculty = facultyRepository.findAll();

            Map<String, Long> departmentCounts = allFaculty.stream()
                    .collect(Collectors.groupingBy(Faculty::getDepartmentType, Collectors.counting()));

            int totalFaculty = allFaculty.size();
            int totalDepartments = departmentCounts.size();
            int newHires = calculateNewHires(allFaculty);
            int recentRemovals = 0;

            List<Map<String, Object>> departmentStats = new ArrayList<>();
            for (Map.Entry<String, Long> entry : departmentCounts.entrySet()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("departmentName", "Computer Science [" + entry.getKey() + "]");
                stat.put("count", entry.getValue());
                stat.put("percentage", totalFaculty > 0 ? Math.round((entry.getValue() * 100.0) / totalFaculty) : 0);
                departmentStats.add(stat);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("totalFaculty", totalFaculty);
            response.put("totalDepartments", totalDepartments);
            response.put("newHires", newHires);
            response.put("recentRemovals", recentRemovals);
            response.put("departmentStats", departmentStats);

            return response;
        } catch (Exception e) {
            System.err.println("Service error: " + e.getMessage());
            return new HashMap<>();
        }
    }
    public Faculty getFacultyByEmail(String email) {
        return facultyRepository.getFacultyByEmail(email);
    }
    public long getRemovedFacultyCount() {
        return removedFacultyRepository.getRemovedFacultyCount();
    }


    private List<Faculty> sortFaculty(List<Faculty> facultyList, String sortBy, String sortOrder) {
        Comparator<Faculty> comparator;

        switch (sortBy.toLowerCase()) {
            case "facultyid":
                comparator = Comparator.comparing(Faculty::getFacultyId, String.CASE_INSENSITIVE_ORDER);
                break;
            case "fullname":
            case "name":
                comparator = Comparator.comparing(Faculty::getFullName, String.CASE_INSENSITIVE_ORDER);
                break;
            case "joiningdate":
            case "date":
                comparator = Comparator.comparing(f -> {
                    try {
                        return LocalDate.parse(f.getJoiningDate());
                    } catch (Exception e) {
                        return LocalDate.MIN;
                    }
                });
                break;
            case "departmenttype":
            case "department":
                comparator = Comparator.comparing(Faculty::getDepartmentType);
                break;
            case "email":
                comparator = Comparator.comparing(Faculty::getEmail, String.CASE_INSENSITIVE_ORDER);
                break;
            default:
                comparator = Comparator.comparing(Faculty::getFullName, String.CASE_INSENSITIVE_ORDER);
        }

        if ("desc".equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }

        return facultyList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private int calculateNewHires(List<Faculty> allFaculty) {
        LocalDate thirtyDaysAgo = LocalDate.now().minus(30, ChronoUnit.DAYS);

        return (int) allFaculty.stream()
                .filter(faculty -> {
                    try {
                        LocalDate joiningDate = LocalDate.parse(faculty.getJoiningDate());
                        return !joiningDate.isBefore(thirtyDaysAgo);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();
    }
}