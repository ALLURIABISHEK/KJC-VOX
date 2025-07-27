import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// ✅ If you're defining Department inline in component, skip this.
// Otherwise, if you're using external model file:
// import { Department } from '../models/Department';

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {
  private apiUrl = 'http://localhost:8080/api/departments'; // Your backend endpoint

  constructor(private http: HttpClient) {}

  addDepartment(department: any): Observable<any> {
    return this.http.post(this.apiUrl, department);
  }

  getAllDepartments() {
  return this.http.get<any[]>('http://localhost:8080/api/departments');
}

getDepartmentByClassName(className: string) {
  return this.http.get<any>(`http://localhost:8080/api/departments/class?className=${className}`);
}

deleteDepartment(className: string) {
  return this.http.delete(`http://localhost:8080/api/departments?className=${className}`);
}

}


