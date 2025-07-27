import { Component, OnInit } from '@angular/core';
import { DepartmentService } from '../services/department.service';
import { ActivatedRoute, Router } from '@angular/router';

interface Subject {
  name: string;
  courseCode: string;
}

interface Department {
  departmentName: string;
  departmentType: string;
  className: string;
  semester: number;
  subjects: Subject[];
}

@Component({
  selector: 'app-add-department',
  templateUrl: './add-deptartment.component.html',
  styleUrls: ['./add-deptartment.component.css']
})
export class AddDepartmentComponent implements OnInit {
  departmentName = 'Department of Computer Science';
  departmentType = 'PG';
  className = '';
  semester = 1;

  subjects: Subject[] = Array.from({ length: 8 }, () => ({ name: '', courseCode: '' }));

  isEditMode = false;

  constructor(
    private departmentService: DepartmentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const classNameParam = params['className'];
      if (classNameParam) {
        this.isEditMode = true;
        this.loadDepartmentData(classNameParam);
      }
    });
  }

  loadDepartmentData(className: string): void {
    this.departmentService.getDepartmentByClassName(className).subscribe({
      next: (data) => {
        this.departmentName = data.departmentName;
        this.departmentType = data.departmentType;
        this.className = data.className;
        this.semester = data.semester;
        this.subjects = data.subjects || Array.from({ length: 8 }, () => ({ name: '', courseCode: '' }));
        if (this.subjects.length < 8) {
          const remaining = 8 - this.subjects.length;
          this.subjects.push(...Array.from({ length: remaining }, () => ({ name: '', courseCode: '' })));
        }
      },
      error: () => {
        alert('❌ Failed to load department details.');
        this.router.navigate(['/manage-department']); // Optional fallback
      }
    });
  }

  onSubmit(): void {
    const department: Department = {
      departmentName: this.departmentName,
      departmentType: this.departmentType,
      className: this.className,
      semester: this.semester,
      subjects: this.subjects
    };

    this.departmentService.addDepartment(department).subscribe({
      next: () => {
        alert(this.isEditMode ? '✅ Department updated successfully!' : '✅ Department added successfully!');
        this.router.navigate(['/manage-department']);
      },
      error: (error) => {
        console.error('❌ Error:', error);
        alert('Something went wrong. Try again.');
      }
    });
  }
}
