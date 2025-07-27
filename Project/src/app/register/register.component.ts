import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name: string = '';
  email: string = '';
  code: string = '';
  menuOpen: boolean = false; // Added for the menu toggle functionality

  constructor(private router: Router, private authService: AuthService) {}

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  sendCode() {
    if (!this.name || !this.email) {
      alert('Please enter both name and email');
      return;
    }

    fetch('http://localhost:8080/api/send-code', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: this.name, email: this.email })
    })
      .then(res => res.json())
      .then(data => {
        alert(data.message);
      })
      .catch(err => {
        console.error("Error sending code:", err);
        alert('Error sending code. See console for details.');
      });
  }

  submitForm() {
    if (!this.name || !this.email || !this.code) {
      alert('All fields are required.');
      return;
    }

    fetch('http://localhost:8080/api/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: this.name, email: this.email, code: this.code })
    })
      .then(res => res.json())
      .then(data => {
        alert(data.message);
        if (data.success) {
          this.authService.clearUser();
          this.router.navigate(['/login']);
        }
      })
      .catch(err => {
        console.error("Registration failed:", err);
        alert("Registration failed. See console for details.");
      });
  }
}