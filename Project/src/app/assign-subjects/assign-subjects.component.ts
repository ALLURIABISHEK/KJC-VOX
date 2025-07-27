import { Component, OnInit } from '@angular/core';
import { FacultyService, Faculty } from '../services/faculty.service';
import { DepartmentService } from '../services/department.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-assign-subjects',
  templateUrl: './assign-subjects.component.html',
  styleUrls: ['./assign-subjects.component.css']
})
export class AssignSubjectsComponent implements OnInit {

  departmentName = 'Department of Computer Science (PG)';
  departmentType = 'PG';

  className = '';
  semester = 1;

  facultyList: Faculty[] = [];
  selectedFaculty = '';

  subjectList: string[] = [];
  selectedSubjects: string[] = [];

  constructor(
    private facultyService: FacultyService,
    private departmentService: DepartmentService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadFaculty();
  }

  loadFaculty() {
    this.facultyService.getAllFaculty().subscribe(data => {
      this.facultyList = data;
    });
  }

  onClassChange() {
    if (this.className) {
      this.departmentService.getDepartmentByClassName(this.className).subscribe(data => {
        this.subjectList = data?.subjects?.map((s: any) => s.name) || [];
        this.selectedSubjects = []; // clear previous selection
      });
    }
  }

  toggleSubject(subject: string) {
    const index = this.selectedSubjects.indexOf(subject);
    if (index > -1) {
      this.selectedSubjects.splice(index, 1);
    } else {
      this.selectedSubjects.push(subject);
    }
  }

  submitAssignment() {
    const payload = {
      departmentName: this.departmentName,
      departmentType: this.departmentType,
      className: this.className,
      semester: this.semester,
      faculty: this.selectedFaculty,
      subjects: this.selectedSubjects
    };

    this.http.post('http://localhost:8080/api/assign-subjects', payload).subscribe({
      next: () => {
        alert('Subjects assigned successfully!');
        this.selectedSubjects = [];
      },
      error: err => {
        if (err.status === 409) {
          alert('Duplicate assignment detected!');
        } else {
          alert('Failed to assign subjects');
        }
      }
    });
  }
}
