import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface TestSubResponse {
  courseCode: string;
  name: string;
  assignedFaculty: string;
}

@Injectable({ providedIn: 'root' })
export class FacultySubjectService {
  private baseUrl = 'http://localhost:8080/api/test-sub-my-subjects';

  constructor(private http: HttpClient) {}

  getAssignedSubjectsForFaculty(faculty: string, className: string, semester: number): Observable<TestSubResponse[]> {
    const params = new HttpParams()
      .set('faculty', faculty.trim())
      .set('className', className.trim())
      .set('semester', semester.toString());

    console.log('🚀 TestSubFacultyService: Making request with params:', {
      faculty: faculty.trim(),
      className: className.trim(),
      semester: semester
    });

    return this.http.get<TestSubResponse[]>(this.baseUrl, { params });
  }
}