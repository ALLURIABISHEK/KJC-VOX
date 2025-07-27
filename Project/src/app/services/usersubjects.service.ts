import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SubjectData {
  name: string;
  courseCode: string;
  assignedFaculty: string;
}

@Injectable({
  providedIn: 'root'
})
export class MySubjectsService {
private apiUrl = 'http://localhost:8080/api/my-subjects';

  constructor(private http: HttpClient) {}

 getSubjectsForClass(faculty: string, className: string, semester: number): Observable<SubjectData[]> {
  return this.http.get<SubjectData[]>(
    `${this.apiUrl}?faculty=${encodeURIComponent(faculty)}&className=${encodeURIComponent(className)}&semester=${semester}`
  );
}
  
}
