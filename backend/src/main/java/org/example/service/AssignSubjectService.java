package org.example.service;

import org.example.model.AssignSubject;
import org.example.repository.AssignSubjectRepository;

public class AssignSubjectService {
    private final AssignSubjectRepository repository;

    public AssignSubjectService(AssignSubjectRepository repository) {
        this.repository = repository;
    }
    public AssignSubject getAssignedSubjects(String className, int semester, String facultyName) {
        return repository.findAssignedSubjects(className, semester, facultyName);
    }

    public boolean assignSubjects(AssignSubject assignSubject) {
        for (String subject : assignSubject.getSubjects()) {
            if (repository.isDuplicate(assignSubject.getFaculty(), assignSubject.getClassName(), assignSubject.getSemester(), subject)) {
                return false; // Duplicate assignment exists
            }
        }

        repository.save(assignSubject);
        return true;
    }
}
