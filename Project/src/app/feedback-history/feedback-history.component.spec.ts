import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedbackHistoryComponent } from './feedback-history.component';

describe('FeedbackHistoryComponent', () => {
  let component: FeedbackHistoryComponent;
  let fixture: ComponentFixture<FeedbackHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FeedbackHistoryComponent]
    });
    fixture = TestBed.createComponent(FeedbackHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
