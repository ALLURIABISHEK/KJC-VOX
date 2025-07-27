import { Component, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-role-selection', // Replace with your component name
  templateUrl: './role-selection.component.html',
  styleUrls: ['./role-selection.component.css'] // or .scss if using SCSS
})
export class RoleselectionComponent implements AfterViewInit {

  ngAfterViewInit(): void {
    const hamburger = document.getElementById('hamburger') as HTMLElement | null;
    const mobileMenu = document.getElementById('mobileMenu') as HTMLElement | null;

    if (hamburger && mobileMenu) {
      hamburger.addEventListener('click', () => {
        mobileMenu.classList.toggle('hidden');
      });
    }
  }
}