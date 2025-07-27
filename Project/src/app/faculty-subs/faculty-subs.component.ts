import { Component, OnInit } from '@angular/core';
import { FacultySubjectService, TestSubResponse } from '../services/faculty-subject-service.service';
import { AuthService } from '../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-faculty-subs',
  templateUrl: './faculty-subs.component.html',
  styleUrls: ['./faculty-subs.component.css']
})
export class FacultySubsComponent implements OnInit {
  className: string = '1MCA-B';
  semester: number = 1;
  assignedSubjects: TestSubResponse[] = [];
  facultyName: string = '';
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private facultySubjectService: FacultySubjectService,  // Fixed service name
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    console.log('🔎 Loaded user from AuthService:', currentUser);

    if (currentUser && currentUser.name) {
      this.facultyName = currentUser.name;
      this.fetchAssignedSubjects();
    } else {
      console.warn('❌ No faculty logged in or name missing!');
      this.errorMessage = 'Faculty information not found. Please log in again.';
    }
  }

  fetchAssignedSubjects(): void {
    console.log('📤 Fetching subjects for faculty:', this.facultyName, 'class:', this.className, 'semester:', this.semester);

    if (!this.facultyName || !this.className || !this.semester) {
      console.warn('❌ Missing required parameters for fetch');
      this.errorMessage = 'Missing required parameters.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.facultySubjectService
      .getAssignedSubjectsForFaculty(this.facultyName, this.className, this.semester)
      .subscribe({
        next: (data: TestSubResponse[]) => {  // Added type annotation
          console.log('✅ Data received from FacultySubjectService:', data);
          this.assignedSubjects = Array.isArray(data) ? data : [];
          this.isLoading = false;
          
          if (this.assignedSubjects.length === 0) {
            this.errorMessage = 'No subjects assigned to you for the selected class and semester.';
          }
        },
        error: (err: any) => {  // Added type annotation
          console.error('❌ Error fetching assigned subjects:', err);
          this.isLoading = false;
          this.errorMessage = 'Error fetching subjects. Please try again.';
          this.assignedSubjects = [];
        }
      });
  }

  viewFeedback(subject: TestSubResponse): void {
    console.log('🔍 Viewing feedback for subject:', subject.name);
    sessionStorage.setItem('selectedSubject', subject.name);
    this.router.navigate(['/facultyfeedback']);
  }
}