import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../core/services/auth.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router'; // ✅ Add this at top (already done?)


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

  constructor(
    private route: ActivatedRoute,
    private auth: AuthService,
    private http: HttpClient,
    private router: Router   // ✅ Inject Router

  ) {}

  ngOnInit(): void {
    // Read subject details from query params
    this.route.queryParams.subscribe(params => {
      this.subjectDetails.subject = params['subject'];
      this.subjectDetails.courseCode = params['courseCode'];
      this.subjectDetails.faculty = params['faculty'];
    });

this.student = this.auth.getCurrentUser(); // includes name, roll, etc.
  }

onSubmit() {
  console.log("🧑‍🎓 Student info from AuthService:", this.student); // ✅ Debug log

  const payload = {
    ...this.feedback,
    subject: this.subjectDetails.subject,
    courseCode: this.subjectDetails.courseCode,
    faculty: this.subjectDetails.faculty,
    studentName: this.student.name,
    studentEmail: this.student.email,
    studentRoll: this.student.rollnumber,  // using rollnumber from frontend
    class: this.student.class,
    semester: this.student.semester,
    department: this.student.department,
    timestamp: new Date()
  };

  console.log("📦 Final payload to send:", payload); // ✅ Debug log

  this.http.post('http://localhost:8080/api/submit-feedback', payload).subscribe({
    next: res => {
      alert("✅ Feedback submitted successfully!");
            this.router.navigate(['/usersubjects']); // ✅ Redirect to usersubjects

    },
    error: err => {
      console.error("❌ Error submitting feedback:", err);
      alert("Error submitting feedback.");
    }
  });
}

}
