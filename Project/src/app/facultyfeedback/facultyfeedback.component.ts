import { Component, OnInit } from '@angular/core';
import { FeedbackService } from '../services/feedback.service';
import { AuthService } from '../core/services/auth.service';



@Component({
  selector: 'app-facultyfeedback',
  templateUrl: './facultyfeedback.component.html',
  styleUrls: ['./facultyfeedback.component.css']
})
export class FeedbackSummaryComponent implements OnInit {
  feedbackStats: { title: string; value: string }[] = [];
  comments: string[] = [];
  selectedMonth: string = '';
  months: string[] = ['January', 'February', 'March']; // optional

  constructor(
    private feedbackService: FeedbackService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const subject = sessionStorage.getItem('selectedSubject') || 'java';
    const faculty = this.authService.getCurrentUser()?.name || 'Faculty';

    this.fetchFeedbackSummary(subject, faculty);
  }

  fetchFeedbackSummary(subject: string, faculty: string): void {
    this.feedbackService.getFeedbackSummary(subject, faculty).subscribe({
      next: (data) => {
        this.feedbackStats = [
          { title: 'Punctuality', value: data.punctuality },
          { title: 'Clarity of Explanation', value: data.clarity },
          { title: 'Engagement', value: data.engagement },
          { title: 'Pace - Normal', value: `${data.pace.normal} responses` },
          { title: 'Pace - Fast', value: `${data.pace.fast} responses` },
          { title: 'Pace - Slow', value: `${data.pace.slow} responses` }
        ];
        this.comments = data.comments;
      },
      error: (err) => {
        console.error('Failed to fetch feedback summary:', err);
      }
    });
  }
}
