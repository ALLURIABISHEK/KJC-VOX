import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-notice',
  templateUrl: './notice.component.html',
})
export class NoticeComponent {
  notice = {
    title: '',
    date: '',
    noticeFor: '',
    noticeType: '',
    description: ''
  };

  constructor(private http: HttpClient) {}

  submitNotice() {
    if (!this.notice.title || !this.notice.date || !this.notice.noticeFor || !this.notice.noticeType || !this.notice.description) {
      alert("Please fill in all fields.");
      return;
    }

    this.http.post('http://localhost:8080/api/notices', this.notice)
      .subscribe({
        next: res => {
          alert("Notice submitted successfully!");
          this.notice = { title: '', date: '', noticeFor: '', noticeType: '', description: '' };
        },
        error: err => {
          console.error(err);
          alert("Failed to submit notice.");
        }
      });
  }
}
