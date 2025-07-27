import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'], // Optional if needed
})
export class HomeComponent implements OnInit {
  mobileNavOpen = false;
  currentSlide = 0;
  features = [
    {
      icon: '🔒',
      title: 'Anonymous Feedback',
      desc: 'Share honest thoughts and experiences without revealing your identity.',
    },
    {
      icon: '📊',
      title: 'Multi-Role Access',
      desc: 'Separate dashboards for students, faculty, and admins with tailored tools.',
    },
    {
      icon: '🛡️',
      title: 'Secure Platform',
      desc: 'Built with industry-standard security to protect data and privacy.',
    },
    {
      icon: '🔗',
      title: 'Easy Integration',
      desc: 'Easily integrate with existing institutional systems and workflows.',
    },
  ];

  toggleMenu() {
    this.mobileNavOpen = !this.mobileNavOpen;
  }

  closeMenu() {
    this.mobileNavOpen = false;
  }

  ngOnInit(): void {
    setInterval(() => {
      this.currentSlide = (this.currentSlide + 1) % this.features.length;
    }, 2000); // Auto-slide every 2 seconds
  }
}