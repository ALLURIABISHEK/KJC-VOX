import { Component, OnInit, AfterViewInit } from '@angular/core';
import { AuthService } from '../core/services/auth.service';
import { FeedbackService } from '../services/feedback.service';
import { Chart, ChartConfiguration, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-faculty-dashboard',
  templateUrl: './faculty-dashboard.component.html',
  styleUrls: ['./faculty-dashboard.component.css']
})
export class FacultyDashboardComponent implements OnInit, AfterViewInit {
  facultyName: string = 'Faculty';
  overallPerformanceChart: Chart | null = null;

  // Overall performance metrics
  overallPunctuality: number = 0;
  overallClarity: number = 0;
  overallEngagement: number = 0;
  overallSatisfaction: number = 0;
  totalFeedbackCount: number = 0;

  // Subjects being taught
  subjects: string[] = ['Java', 'Python', 'Cloud Computing', 'Data Structures'];

  constructor(
    private authService: AuthService,
    private feedbackService: FeedbackService
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser?.name) {
      this.facultyName = currentUser.name;
    }

    // Fetch overall performance data
    this.fetchOverallPerformance();
  }

  ngAfterViewInit(): void {
    // Chart will be created after data is loaded
  }

  fetchOverallPerformance(): void {
    // Get all subjects and aggregate the feedback
    // For now, using sample data - replace with actual API call
    
    // Simulated aggregated data from all subjects
    this.overallPunctuality = 75;
    this.overallClarity = 82;
    this.overallEngagement = 68;
    this.overallSatisfaction = 75;
    this.totalFeedbackCount = 156;

    setTimeout(() => this.createPerformanceChart(), 100);
  }

  createPerformanceChart(): void {
    const canvas = document.getElementById('overallPerformanceChart') as HTMLCanvasElement;
    if (!canvas) return;

    if (this.overallPerformanceChart) {
      this.overallPerformanceChart.destroy();
    }

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    // Create a radar/polar chart for overall performance
    this.overallPerformanceChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['Punctuality', 'Clarity', 'Engagement', 'Satisfaction'],
        datasets: [{
          label: 'Overall Performance',
          data: [
            this.overallPunctuality,
            this.overallClarity,
            this.overallEngagement,
            this.overallSatisfaction
          ],
          backgroundColor: [
            '#3b82f6', // Blue
            '#10b981', // Green
            '#f59e0b', // Amber
            '#8b5cf6'  // Purple
          ],
          borderWidth: 3,
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
              font: {
                size: 12,
                weight: 'bold'
              },
              padding: 15,
              usePointStyle: true
            }
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                const label = context.label || '';
                const value = context.parsed || 0;
                return `${label}: ${value}%`;
              }
            }
          }
        }
      }
    });
  }

  getPerformanceColor(value: number): string {
    if (value >= 76) return 'text-green-600';
    if (value >= 41) return 'text-yellow-600';
    return 'text-red-600';
  }

  getPerformanceLabel(value: number): string {
    if (value >= 76) return 'Excellent';
    if (value >= 41) return 'Good';
    return 'Needs Improvement';
  }

  ngOnDestroy(): void {
    if (this.overallPerformanceChart) {
      this.overallPerformanceChart.destroy();
    }
  }
}