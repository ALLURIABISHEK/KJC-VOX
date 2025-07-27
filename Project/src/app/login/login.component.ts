import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  email: string = '';
  password: string = '';
  isSubmitting: boolean = false;
  isMobileMenuOpen: boolean = false;
  errorMessage: string = '';

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService
  ) {}

ngOnInit() {
  this.errorMessage = '';

  const currentUser = this.authService.getCurrentUser();
  if (currentUser) {
    if (currentUser.isVerified) {
      this.router.navigate(['/user-dash-board']);
    } else {
      this.router.navigate(['/verification-page']);
    }
  }
}


  toggleMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen = false;
  }

 submitLoginForm(): void {
  // Clear previous error messages
  this.errorMessage = '';

  if (this.isSubmitting) {
    return;
  }

  // Trim whitespace from inputs
  this.email = this.email.trim();
  this.password = this.password.trim();

  // Basic validation
  if (!this.email) {
    this.errorMessage = 'Please enter your email';
    return;
  }

  if (!this.password) {
    this.errorMessage = 'Please enter your password';
    return;
  }

  // Email format validation
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(this.email)) {
    this.errorMessage = 'Please enter a valid email address';
    return;
  }

  if (
    !this.email.toLowerCase().includes('kristujayanti.com') &&
    !this.email.toLowerCase().includes('@gmail.com')
  ) {
    this.errorMessage = 'Please use your KJC or Gmail (faculty) email address';
    return;
  }

  this.isSubmitting = true;

  const loginData = {
    email: this.email,
    password: this.password
  };

  const httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    })
  };

  console.log('Sending login request:', { email: loginData.email, password: '***' });

  this.http.post<any>('http://localhost:8080/api/login', loginData, httpOptions)
    .subscribe({
      next: (response) => {
        console.log('Login response:', response);
        this.isSubmitting = false;

        if (response.success) {
          this.authService.setCurrentUser({
            name: response.name,
            email: response.email || this.email,
            isVerified: response.isVerified,
            class: response.className,
            semester: response.semester,
            rollnumber: response.rollnumber,
            department: response.department,
            course: response.course,
            role: response.role
          });

          // 🔁 Updated role/email-based redirect
          const email = (response.email || this.email).toLowerCase();

          if (email === 'admin@kristujayanti.com') {
            this.router.navigate(['/admin-dash-board']);
          } else if (email.endsWith('@gmail.com')) {
            this.router.navigate(['/faculty-dashboard']);
          } else {
            if (response.isVerified) {
              this.router.navigate(['/user-dash-board']);
            } else {
              this.router.navigate(['/verification-page']);
            }
          }

        } else {
          this.errorMessage = response.message || 'Login failed. Please try again.';
        }
      },
      error: (err) => {
        console.error('Login error:', err);
        this.isSubmitting = false;

        // Handle different error types
        if (err.error?.message) {
          this.errorMessage = err.error.message;
        } else if (err.status === 400) {
          this.errorMessage = 'Invalid email or password format';
        } else if (err.status === 401) {
          this.errorMessage = 'Invalid credentials. Please check your email and password.';
        } else if (err.status === 404) {
          this.errorMessage = 'User not found. Please check your email or register first.';
        } else if (err.status === 0) {
          this.errorMessage = 'Cannot connect to server. Please check if the server is running.';
        } else if (err.status >= 500) {
          this.errorMessage = 'Server error. Please try again later.';
        } else {
          this.errorMessage = 'An unexpected error occurred. Please try again.';
        }
      }
    });
} // ✅ This closes submitLoginForm()

clearErrorMessage(): void {
  this.errorMessage = '';
}
}
