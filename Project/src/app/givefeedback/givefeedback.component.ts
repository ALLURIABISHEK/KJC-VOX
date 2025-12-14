import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';
import { HttpClient } from '@angular/common/http';

interface ValidationResult {
  approved: boolean;
  suggestions?: string[];
  message?: string;
}

@Component({
  selector: 'app-givefeedback',
  templateUrl: './givefeedback.component.html',
  styleUrls: ['./givefeedback.component.css']
})
export class FeedbackFormComponent implements OnInit {
  feedback = {
    punctual: '',
    clarity: 0,
    engaging: '',
    pace: '',
    satisfaction: 0,
    comments: ''
  };

  subjectDetails = {
    subject: '',
    courseCode: '',
    faculty: ''
  };

  student: any = {};

  // AI Validation properties
  aiValidationStarted = false;
  validationStep = 0; // 0=not started, 1=sending, 2=analyzing, 3=result
  validationResult: ValidationResult | null = null;

  constructor(
    private route: ActivatedRoute,
    private auth: AuthService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Read subject details from query params
    this.route.queryParams.subscribe(params => {
      this.subjectDetails.subject = params['subject'];
      this.subjectDetails.courseCode = params['courseCode'];
      this.subjectDetails.faculty = params['faculty'];
    });

    this.student = this.auth.getCurrentUser();
  }

  isFormValid(): boolean {
    return !!(
      this.feedback.punctual &&
      this.feedback.engaging &&
      this.feedback.pace &&
      this.feedback.clarity > 0 &&
      this.feedback.satisfaction > 0 &&
      this.feedback.comments.trim()
    );
  }

  async validateWithAI() {
    if (!this.isFormValid()) {
      alert('Please complete all fields before validation');
      return;
    }

    this.aiValidationStarted = true;
    this.validationStep = 0;
    this.validationResult = null;

    try {
      // Step 1: Sending
      this.validationStep = 1;
      await this.delay(800);

      // Step 2: Analyzing
      this.validationStep = 2;
      
      // Call backend AI validation endpoint
      const response = await this.http.post<ValidationResult>(
        'http://localhost:8080/api/validate-feedback',
        { comments: this.feedback.comments }
      ).toPromise();

      await this.delay(1000);

      // Step 3: Result
      this.validationStep = 3;
      this.validationResult = response!;

    } catch (error) {
      console.error('AI Validation Error:', error);
      alert('Error during AI validation. Please try again.');
      this.aiValidationStarted = false;
      this.validationStep = 0;
    }
  }

  applySuggestion(suggestion: string) {
    this.feedback.comments = suggestion;
    // Automatically re-validate after applying suggestion
    setTimeout(() => this.validateWithAI(), 300);
  }

  async onSubmit() {
    if (!this.validationResult?.approved) {
      alert('Please ensure your feedback is validated and approved by AI before submitting.');
      return;
    }

    console.log("🧑‍🎓 Student info from AuthService:", this.student);

    const payload = {
      ...this.feedback,
      subject: this.subjectDetails.subject,
      courseCode: this.subjectDetails.courseCode,
      faculty: this.subjectDetails.faculty,
      studentName: this.student.name,
      studentEmail: this.student.email,
      studentRoll: this.student.rollnumber,
      class: this.student.class,
      semester: this.student.semester,
      department: this.student.department,
      timestamp: new Date()
    };

    console.log("📦 Final payload to send:", payload);

    this.http.post('http://localhost:8080/api/submit-feedback', payload).subscribe({
      next: res => {
        alert("✅ Feedback submitted successfully!");
        this.router.navigate(['/usersubjects']);
      },
      error: err => {
        console.error("❌ Error submitting feedback:", err);
        alert("Error submitting feedback.");
      }
    });
  }

  private delay(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}