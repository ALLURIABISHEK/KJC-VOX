import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Feedback {
  _id: string;
  subject: string;
  courseCode: string;
  faculty: string;
  studentName: string;
  studentEmail: string;
  studentRoll: string;
  punctual: string;
  clarity: number;
  engaging: string;
  pace: string;
  satisfaction: number;
  comments: string;
  submittedAt?: string;
}

@Component({
  selector: 'app-feedback-history',
  templateUrl: './feedback-history.component.html',
  styleUrls: ['./feedback-history.component.css']
})
export class FeedbackHistoryComponent implements OnInit {
  feedbackList: Feedback[] = [];
  loading: boolean = true;
  error: string = '';
  studentEmail: string = '24mcab07@kristujayanti.com'; // Get this from your auth service
  expandedFeedback: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadFeedbackHistory();
  }

  loadFeedbackHistory(): void {
    this.loading = true;
    this.error = '';
    
    this.http.get<Feedback[]>(`http://localhost:8080/api/feedback-history/history/${this.studentEmail}`)
      .subscribe({
        next: (data) => {
          this.feedbackList = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Error loading feedback:', err);
          this.error = 'Failed to load feedback history. Please try again.';
          this.loading = false;
        }
      });
  }

  toggleExpand(feedbackId: string): void {
    this.expandedFeedback = this.expandedFeedback === feedbackId ? null : feedbackId;
  }

  getStarArray(rating: number): number[] {
    return Array(5).fill(0).map((_, i) => i < rating ? 1 : 0);
  }

  getPaceColor(pace: string): string {
    switch(pace.toLowerCase()) {
      case 'slow': return 'bg-blue-100 text-blue-700 border-blue-300';
      case 'normal': return 'bg-green-100 text-green-700 border-green-300';
      case 'fast': return 'bg-orange-100 text-orange-700 border-orange-300';
      default: return 'bg-gray-100 text-gray-700 border-gray-300';
    }
  }
}
