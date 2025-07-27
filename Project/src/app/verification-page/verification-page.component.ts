import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

interface StudentData {
  name: string;
  rollnumber: string;
  email: string;
  department: string;
  course: string;
  semester: string;
  class: string;
}

interface VerificationResponse {
  success: boolean;
  message?: string;
}

@Component({
  selector: 'app-verification-page',
  templateUrl: './verification-page.component.html',
  styleUrls: ['./verification-page.component.css']
})
export class VerificationPageComponent implements OnInit {
  studentData?: StudentData;
  isConfirmed = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.fetchStudentData();
  }

  toggleNav(): void {
    const navMenu = document.getElementById('navMenu');
    if (navMenu) {
      navMenu.classList.toggle('active');
    }
  }

  fetchStudentData(): void {
    const email = this.authService.getEmail();
    if (!email) {
      this.redirectToLogin();
      return;
    }

    this.http.get<StudentData>(
      `http://localhost:8080/api/verify-details?email=${encodeURIComponent(email)}`
    ).subscribe({
      next: (response) => {
        this.studentData = response;
      },
      error: (error) => {
        console.error("Error loading student details:", error);
        alert("Failed to load student details");
        this.redirectToLogin();
      }
    });
  }

  handleVerification(): void {
    if (!this.isConfirmed) {
      alert("Please confirm the information is correct");
      return;
    }

    const email = this.authService.getEmail();
    if (!email) {
      this.redirectToLogin();
      return;
    }

    this.http.post<VerificationResponse>(
      'http://localhost:8080/api/finalize-verification',
      { email }
    ).subscribe({
      next: (response) => {
        if (response.success) {
          // ✅ Fetch full academic info after verification
          this.http.get<StudentData>(
            `http://localhost:8080/api/student-details?email=${encodeURIComponent(email)}`
          ).subscribe({
            next: (studentInfo) => {
             this.authService.setCurrentUser({
  ...this.authService.getCurrentUser(),
  isVerified: true,
  class: studentInfo.class,
  semester: studentInfo.semester,
  course: studentInfo.course,
  department: studentInfo.department,
  rollnumber: studentInfo.rollnumber    // ✅ Important!
});

              this.router.navigate(['/user-dash-board']);
            },
            error: (err) => {
              console.error("Error fetching academic info:", err);
              alert("Verified, but failed to load academic info");
              this.router.navigate(['/user-dash-board']);
            }
          });
        } else {
          alert("Verification failed: " + (response.message || 'Unknown error'));
        }
      },
      error: (error) => {
        console.error("Verification error:", error);
        alert("Failed to complete verification");
      }
    });
  }

  private redirectToLogin(): void {
    this.authService.clearUser();
    this.router.navigate(['/login']);
  }
}
