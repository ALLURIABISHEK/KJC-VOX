import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../core/services/auth.service';
import { MySubjectsService, SubjectData } from '../services/usersubjects.service';


interface StudentDetails {
  class: string;
  semester: number | string;
  name: string;
  email: string;
  rollnumber: string;
  department: string;
  course: string;
}

@Component({
  selector: 'app-usersubjects',
  templateUrl: './usersubjects.component.html',
  styleUrls: ['./usersubjects.component.css']
})
export class UsersubjectsComponent implements OnInit {
  subjects: SubjectData[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private subjectService: MySubjectsService
  ) {}

  ngOnInit(): void {
    const email = this.authService.getEmail();

    if (!email) {
      this.errorMessage = 'User email is missing.';
      this.isLoading = false;
      return;
    }

    // STEP 1: Fetch student academic info
    this.http.get<StudentDetails>(`http://localhost:8080/api/student-details?email=${email}`)
      .subscribe({
        next: (studentInfo) => {
          const className = studentInfo.class;
          const semesterRaw = studentInfo.semester;

          if (!className || !semesterRaw) {
            this.errorMessage = 'Academic info missing (class or semester).';
            this.isLoading = false;
            return;
          }

          // Convert Roman numerals to integer if needed
          const romanToNumber: any = {
            I: 1, II: 2, III: 3, IV: 4,
            V: 5, VI: 6, VII: 7, VIII: 8
          };

          const semester = typeof semesterRaw === 'string'
            ? (romanToNumber[semesterRaw.toUpperCase()] || 1)
            : semesterRaw;

          // ✅ STEP 2: Use service instead of direct HttpClient
     this.subjectService.getSubjectsForClass('', className, semester)
  .subscribe({
    next: (subjects) => {
      console.log("📦 Received subjects from service:", subjects);
      this.subjects = subjects;
      this.isLoading = false;
    },
              error: (err) => {
                console.error('Failed to fetch subjects from service:', err);
                this.errorMessage = 'Could not load subjects.';
                this.isLoading = false;
              }
            });
        },
        error: (err) => {
          console.error("Error fetching student academic info:", err);
          this.errorMessage = "Failed to load academic info.";
          this.isLoading = false;
        }
      });
  }
}
