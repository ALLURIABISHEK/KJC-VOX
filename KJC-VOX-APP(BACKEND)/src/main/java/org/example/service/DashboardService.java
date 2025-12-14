package org.example.service;

import org.example.dao.DashboardDao;
import org.example.model.*;

import java.util.List;

public class DashboardService {

    private DashboardDao dashboardDao;

    public DashboardService() {
        this.dashboardDao = new DashboardDao();
    }

    /**
     * Get all dashboard statistics
     */
    public DashboardStats getDashboardStats() {
        long totalStudents = dashboardDao.getTotalStudents();
        long totalFaculty = dashboardDao.getTotalFaculty();
        long totalDepartments = dashboardDao.getTotalDepartments();
        long totalSubjects = dashboardDao.getTotalSubjects();
        long totalNotices = dashboardDao.getTotalNotices();
        long totalFeedbacks = dashboardDao.getTotalFeedbacks();

        return new DashboardStats(
                totalStudents,
                totalFaculty,
                totalDepartments,
                totalSubjects,
                totalNotices,
                totalFeedbacks
        );
    }
    /**
     * Get student distribution by class
     */
    public List<StudentDistribution> getStudentDistribution() {
        return dashboardDao.getStudentDistributionByClass();
    }

    /**
     * Get faculty distribution by department type
     */
    public List<FacultyDistribution> getFacultyDistribution() {
        return dashboardDao.getFacultyDistributionByType();
    }

    /**
     * Get semester distribution
     */
    public List<SemesterDistribution> getSemesterDistribution() {
        return dashboardDao.getSemesterDistribution();
    }

    /**
     * Get department overview
     */
    public List<DepartmentOverview> getDepartmentOverview() {
        return dashboardDao.getDepartmentOverview();
    }
}