import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedbackFormComponent } from './givefeedback.component';

describe('GivefeedbackComponent', () => {
  let component: FeedbackFormComponent;
  let fixture: ComponentFixture<FeedbackFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FeedbackFormComponent]
    });
    fixture = TestBed.createComponent(FeedbackFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
