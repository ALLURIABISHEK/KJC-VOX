import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DepartmentService } from '../services/department.service';

@Component({
  selector: 'app-manage-department',
  templateUrl: './manage-department.component.html',
  styleUrls: ['./manage-department.component.css']
})
export class ManageDepartmentComponent implements OnInit {

  departments: any[] = [];

  constructor(
    private departmentService: DepartmentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDepartments();
  }

  // Load all departments from backend
  loadDepartments(): void {
    this.departmentService.getAllDepartments().subscribe({
      next: (data) => {
        this.departments = data;
      },
      error: () => {
        alert('❌ Failed to load departments');
      }
    });
  }

  // Navigate to fresh add form
  addDepartment(): void {
    this.router.navigate(['/add-department']);
  }

  // Navigate to edit with query param
  editDepartment(className: string): void {
    this.router.navigate(['/add-department'], {
      queryParams: { className }
    });
  }

  // Delete a department
  deleteDepartment(className: string): void {
    if (confirm(`Are you sure you want to delete department: ${className}?`)) {
      this.departmentService.deleteDepartment(className).subscribe({
        next: () => {
          alert('✅ Department deleted!');
          this.loadDepartments();
        },
        error: () => {
          alert('❌ Failed to delete department');
        }
      });
    }
  }

  // Show first 2 subject names only, rest as +N more
  formatSubjects(subjects: any[]): string {
    if (!subjects || subjects.length === 0) return '—';

    const displayed = subjects.slice(0, 2).map(s => s.name).join(', ');
    const remaining = subjects.length - 2;

    return remaining > 0 ? `${displayed}, +${remaining} more` : displayed;
  }
}
