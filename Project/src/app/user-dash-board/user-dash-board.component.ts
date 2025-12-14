import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-user-dash-board',
  templateUrl: './user-dash-board.component.html',
  styleUrls: ['./user-dash-board.component.css']
})
export class UserDashBoardComponent implements OnInit {
  userName: string = '';
  notices: any[] = [];
  user: any;

  constructor(
    private authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getCurrentUser();

    if (this.user && this.user.name) {
      this.userName = this.user.name;
      this.loadNotices(); // Load notices after user is fetched
    } else {
      console.warn('User not found. Redirecting to login...');
      this.router.navigate(['/login']);
    }
  }

  goToMySubjects(): void {
    this.router.navigate(['/usersubjects']);
  }
  

  showLogout = false;

toggleLogout() {
  this.showLogout = !this.showLogout;
}

logout() {
  // Add your logout logic here
  this.showLogout = false;
  // Example: this.router.navigate(['/login']);
}

  loadNotices(): void {
    const noticeFor = 'student'; // You can change this based on role if needed

    this.http.get<any[]>(`http://localhost:8080/api/notices?for=${noticeFor}`).subscribe({
      next: (data) => {
        this.notices = data;
        console.log('📢 Notices fetched:', data);
      },
      error: (err) => {
        console.error('❌ Error loading notices:', err);
      }
    });
  }
}
