import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, NavigationEnd } from '@angular/router';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

interface DashboardStats {
  totalStudents: number;
  totalFaculty: number;
  totalDepartments: number;
  totalSubjects: number;
  totalNotices: number;
  totalFeedbacks: number;
}

interface StudentDistribution {
  className: string;
  count: number;
}

interface FacultyDistribution {
  departmentType: string;
  count: number;
}

interface SemesterDistribution {
  semester: string;
  count: number;
}

interface DepartmentOverview {
  departmentName: string;
  studentCount: number;
  facultyCount: number;
  subjectCount: number;
}

@Component({
  selector: 'app-admin-dash-board',
  templateUrl: './admin-dash-board.component.html',
  styleUrls: ['./admin-dash-board.component.css']
})
export class AdminDashBoardComponent implements OnInit {
  currentPage = '';
  adminName = 'Admin';
  dropdownOpen = false;
  
  dashboardStats: DashboardStats | null = null;
  
  private apiUrl = 'http://localhost:8080/api/dashboard';

  private charts: { [key: string]: Chart } = {};

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentPage = this.router.url.replace('/', '');
      }
    });

    this.loadDashboardData();
  }

  loadDashboardData() {
    // Load all dashboard stats
    this.http.get<DashboardStats>(`${this.apiUrl}/stats`).subscribe({
      next: (data) => {
        this.dashboardStats = data;
      },
      error: (err) => console.error('Error loading dashboard stats:', err)
    });

    // Load Student Distribution
    this.http.get<StudentDistribution[]>(`${this.apiUrl}/student-distribution`).subscribe({
      next: (data) => this.renderStudentDistributionChart(data),
      error: (err) => console.error('Error loading student distribution:', err)
    });

    // Load Faculty Distribution
    this.http.get<FacultyDistribution[]>(`${this.apiUrl}/faculty-distribution`).subscribe({
      next: (data) => this.renderFacultyDistributionChart(data),
      error: (err) => console.error('Error loading faculty distribution:', err)
    });

    // Load Semester Distribution
    this.http.get<SemesterDistribution[]>(`${this.apiUrl}/semester-distribution`).subscribe({
      next: (data) => this.renderSemesterDistributionChart(data),
      error: (err) => console.error('Error loading semester distribution:', err)
    });

    // Load Department Overview
    this.http.get<DepartmentOverview[]>(`${this.apiUrl}/department-overview`).subscribe({
      next: (data) => this.renderDepartmentOverviewChart(data),
      error: (err) => console.error('Error loading department overview:', err)
    });
  }

  renderStudentDistributionChart(data: StudentDistribution[]) {
    const ctx = document.getElementById('studentDistributionChart') as HTMLCanvasElement;
    if (!ctx) return;

    if (this.charts['studentDistribution']) {
      this.charts['studentDistribution'].destroy();
    }

    this.charts['studentDistribution'] = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: data.map(d => d.className),
        datasets: [{
          data: data.map(d => d.count),
          backgroundColor: [
            '#3b82f6',
            '#10b981',
            '#f59e0b',
            '#ef4444',
            '#8b5cf6',
            '#ec4899'
          ],
          borderWidth: 2,
          borderColor: '#ffffff'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              padding: 15,
              font: { size: 12 }
            }
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                const label = context.label || '';
                const value = context.parsed || 0;
                const total = context.dataset.data.reduce((a: number, b: any) => a + b, 0);
                const percentage = ((value / total) * 100).toFixed(1);
                return `${label}: ${value} (${percentage}%)`;
              }
            }
          }
        }
      }
    });
  }

  renderFacultyDistributionChart(data: FacultyDistribution[]) {
    const ctx = document.getElementById('facultyDistributionChart') as HTMLCanvasElement;
    if (!ctx) return;

    if (this.charts['facultyDistribution']) {
      this.charts['facultyDistribution'].destroy();
    }

    this.charts['facultyDistribution'] = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: data.map(d => d.departmentType),
        datasets: [{
          data: data.map(d => d.count),
          backgroundColor: ['#06b6d4', '#f97316', '#8b5cf6'],
          borderWidth: 2,
          borderColor: '#ffffff'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              padding: 15,
              font: { size: 12 }
            }
          }
        }
      }
    });
  }

  renderSemesterDistributionChart(data: SemesterDistribution[]) {
    const ctx = document.getElementById('semesterDistributionChart') as HTMLCanvasElement;
    if (!ctx) return;

    if (this.charts['semesterDistribution']) {
      this.charts['semesterDistribution'].destroy();
    }

    this.charts['semesterDistribution'] = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: data.map(d => d.semester),
        datasets: [{
          label: 'Number of Students',
          data: data.map(d => d.count),
          backgroundColor: '#8b5cf6',
          borderColor: '#7c3aed',
          borderWidth: 2,
          borderRadius: 8
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: { display: false }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: { stepSize: 20 }
          }
        }
      }
    });
  }

  renderDepartmentOverviewChart(data: DepartmentOverview[]) {
    const ctx = document.getElementById('departmentOverviewChart') as HTMLCanvasElement;
    if (!ctx) return;

    if (this.charts['departmentOverview']) {
      this.charts['departmentOverview'].destroy();
    }

    this.charts['departmentOverview'] = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: data.map(d => d.departmentName),
        datasets: [
          {
            label: 'Students',
            data: data.map(d => d.studentCount),
            backgroundColor: '#3b82f6',
            borderRadius: 6
          },
          {
            label: 'Faculty',
            data: data.map(d => d.facultyCount),
            backgroundColor: '#10b981',
            borderRadius: 6
          },
          {
            label: 'Subjects',
            data: data.map(d => d.subjectCount),
            backgroundColor: '#f59e0b',
            borderRadius: 6
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              padding: 15,
              font: { size: 12 }
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: { stepSize: 10 }
          }
        }
      }
    });
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  logout() {
    localStorage.clear();
    sessionStorage.clear();
    this.router.navigate(['/login']);
  }

  goBack() {
    this.router.navigate(['/']);
  }

  navigateTo(page: string) {
    this.router.navigate(['/' + page]);
  }
}