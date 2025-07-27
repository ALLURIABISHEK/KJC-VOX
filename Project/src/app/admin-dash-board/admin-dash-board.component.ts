import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-admin-dash-board',
  templateUrl: './admin-dash-board.component.html',
  styleUrls: ['./admin-dash-board.component.css']
})
export class AdminDashBoardComponent implements OnInit {
  currentPage = '';
  adminName = 'Admin'; // Default fallback value
  dropdownOpen = false; // 🔹 Controls the logout dropdown

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentPage = this.router.url.replace('/', '');
      }
    });

   
  }

  
  goBack() {
    this.router.navigate(['/']);
  }

  navigateTo(page: string) {
    this.router.navigate(['/' + page]);
  }

  // 🔹 Toggle dropdown menu
  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  // 🔹 Logout and redirect to login page
  logout() {
    // Optionally clear localStorage/sessionStorage here
    this.router.navigate(['/login']);
  }
}