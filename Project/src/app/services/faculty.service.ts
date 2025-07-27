import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Faculty {
  _id?: string;
  id?: string;
  facultyId?: string;
  idAsString?: string;
  fullName: string;
  email: string;
  department: string;
  joiningDate: string;
  departmentType: string;
}

@Injectable({
  providedIn: 'root'
})
export class FacultyService {
  private apiUrl = 'http://localhost:8080/api/faculty';

  constructor(private http: HttpClient) {}

  addFaculty(faculty: Partial<Faculty>): Observable<Faculty> {
    console.log('Adding faculty:', faculty);
    return this.http.post<Faculty>(this.apiUrl, faculty);
  }

  getAllFaculty(): Observable<Faculty[]> {
    console.log('Getting all faculty');
    return this.http.get<Faculty[]>(this.apiUrl);
  }

  getFacultyById(mongoId: string): Observable<Faculty> {
    console.log('Getting faculty by MongoDB ID:', mongoId);
    return this.http.get<Faculty>(`${this.apiUrl}/${mongoId}`);
  }

  updateFaculty(mongoId: string, faculty: Partial<Faculty>): Observable<Faculty> {
    console.log('Updating faculty with MongoDB ID:', mongoId);
    return this.http.put<Faculty>(`${this.apiUrl}/${mongoId}`, faculty);
  }

  deleteFaculty(mongoId: string): Observable<any> {
    console.log('Deleting faculty with MongoDB ID:', mongoId);
    return this.http.delete(`${this.apiUrl}/${mongoId}`);
  }

  deleteFacultyByEmail(email: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/by-email/${email}`);
  }

  updateFacultyByEmail(email: string, data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${email}`, data);
  }

  searchFaculty(params: any): Observable<Faculty[]> {
    return this.http.get<Faculty[]>(`${this.apiUrl}/search`, { params });
  }

  getDepartmentStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/stats`);
  }

getRemovedFacultyCount() {
  return this.http.get<{ removedCount: number }>('http://localhost:8080/api/faculty/removed/count');
}

}
