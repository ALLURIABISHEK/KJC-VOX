import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getFeedbackSummary(subject: string, faculty: string): Observable<any> {
    const params = new HttpParams()
      .set('subject', subject)
      .set('faculty', faculty);

    return this.http.get(`${this.baseUrl}/feedback-summary`, { params });
  }
}
