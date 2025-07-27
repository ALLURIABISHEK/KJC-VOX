import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacultyfeedbackComponent } from './facultyfeedback.component';

describe('FacultyfeedbackComponent', () => {
  let component: FacultyfeedbackComponent;
  let fixture: ComponentFixture<FacultyfeedbackComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FacultyfeedbackComponent]
    });
    fixture = TestBed.createComponent(FacultyfeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
