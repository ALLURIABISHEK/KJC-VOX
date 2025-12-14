import { Component, OnInit, AfterViewInit } from '@angular/core';
import { FeedbackService } from '../services/feedback.service';
import { AuthService } from '../core/services/auth.service';
import { Chart, ChartConfiguration, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-facultyfeedback',
  templateUrl: './facultyfeedback.component.html',
  styleUrls: ['./facultyfeedback.component.css']
})
export class FeedbackSummaryComponent implements OnInit, AfterViewInit {
  feedbackStats: { title: string; value: string }[] = [];
  comments: string[] = [];
  selectedMonth: string = '';
  months: string[] = ['January', 'February', 'March'];
  subjectName: string = ''; // Add this line

  // Parsed data for visualizations
  punctualityPercent: number = 0;
  clarityRating: number = 0;
  engagementPercent: number = 0;
  paceNormal: number = 0;
  paceFast: number = 0;
  paceSlow: number = 0;
  satisfactionScore: number = 0;

  // Chart instances
  paceChart: Chart | null = null;
  punctualityDonutChart: Chart | null = null;

  constructor(
    private feedbackService: FeedbackService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const subject = sessionStorage.getItem('selectedSubject') || 'java';
    const faculty = this.authService.getCurrentUser()?.name || 'Faculty';
    
    // Capitalize subject name for display
    this.subjectName = this.capitalizeSubject(subject);
    
    this.fetchFeedbackSummary(subject, faculty);
  }

  capitalizeSubject(subject: string): string {
    // Handle common cases like "java" -> "Java", "python" -> "Python", "cloud computing" -> "Cloud Computing"
    return subject
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }

  ngAfterViewInit(): void {
    // Charts will be created after data is loaded
  }

  fetchFeedbackSummary(subject: string, faculty: string): void {
    this.feedbackService.getFeedbackSummary(subject, faculty).subscribe({
      next: (data) => {
        // Parse punctuality (e.g., "50%" -> 50)
        this.punctualityPercent = parseInt(data.punctuality) || 0;
        
        // Parse clarity (e.g., "4.0/5" -> 4.0)
        const clarityMatch = data.clarity.match(/(\d+\.?\d*)/);
        this.clarityRating = clarityMatch ? parseFloat(clarityMatch[1]) : 0;
        
        // Parse engagement (e.g., "25%" -> 25)
        this.engagementPercent = parseInt(data.engagement) || 0;
        
        // Parse pace
        this.paceNormal = data.pace.normal || 0;
        this.paceFast = data.pace.fast || 0;
        this.paceSlow = data.pace.slow || 0;
        
        // Calculate overall satisfaction
        const clarityPercent = (this.clarityRating / 5) * 100;
        this.satisfactionScore = Math.round(
          (this.punctualityPercent + clarityPercent + this.engagementPercent) / 3
        );

        this.feedbackStats = [
          { title: 'Punctuality', value: data.punctuality },
          { title: 'Clarity of Explanation', value: data.clarity },
          { title: 'Engagement', value: data.engagement },
          { title: 'Pace - Normal', value: `${data.pace.normal} responses` },
          { title: 'Pace - Fast', value: `${data.pace.fast} responses` },
          { title: 'Pace - Slow', value: `${data.pace.slow} responses` }
        ];
        this.comments = data.comments;

        // Create charts after data is loaded
        setTimeout(() => this.createCharts(), 100);
      },
      error: (err) => {
        console.error('Failed to fetch feedback summary:', err);
      }
    });
  }

  createCharts(): void {
    this.createPaceChart();
    this.createPunctualityDonutChart();
  }

  createPaceChart(): void {
    const canvas = document.getElementById('paceChart') as HTMLCanvasElement;
    if (!canvas) return;

    if (this.paceChart) {
      this.paceChart.destroy();
    }

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    this.paceChart = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: ['Normal', 'Fast', 'Slow'],
        datasets: [{
          data: [this.paceNormal, this.paceFast, this.paceSlow],
          backgroundColor: ['#10b981', '#ef4444', '#3b82f6'],
          borderWidth: 2,
          borderColor: '#fff'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              font: { size: 14 },
              padding: 15
            }
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                const label = context.label || '';
                const value = context.parsed || 0;
                const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
                const percentage = ((value / total) * 100).toFixed(1);
                return `${label}: ${value} (${percentage}%)`;
              }
            }
          }
        }
      }
    });
  }

  createPunctualityDonutChart(): void {
    const canvas = document.getElementById('punctualityDonut') as HTMLCanvasElement;
    if (!canvas) return;

    if (this.punctualityDonutChart) {
      this.punctualityDonutChart.destroy();
    }

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    this.punctualityDonutChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['Punctual', 'Not Punctual'],
        datasets: [{
          data: [this.punctualityPercent, 100 - this.punctualityPercent],
          backgroundColor: [
            this.getColor(this.punctualityPercent),
            '#e5e7eb'
          ],
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        cutout: '70%',
        plugins: {
          legend: {
            display: false
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                return `${context.label}: ${context.parsed}%`;
              }
            }
          }
        }
      }
    });
  }

  getColor(value: number): string {
    if (value >= 76) return '#10b981'; // Green
    if (value >= 41) return '#fbbf24'; // Yellow
    return '#ef4444'; // Red
  }

  getColorClass(value: number): string {
    if (value >= 76) return 'text-green-600';
    if (value >= 41) return 'text-yellow-600';
    return 'text-red-600';
  }

  getStars(rating: number): number[] {
    return Array(5).fill(0).map((_, i) => i < Math.floor(rating) ? 1 : 0);
  }

  ngOnDestroy(): void {
    if (this.paceChart) this.paceChart.destroy();
    if (this.punctualityDonutChart) this.punctualityDonutChart.destroy();
  }
}