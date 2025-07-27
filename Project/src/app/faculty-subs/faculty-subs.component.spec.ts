import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacultySubsComponent } from './faculty-subs.component';

describe('FacultySubsComponent', () => {
  let component: FacultySubsComponent;
  let fixture: ComponentFixture<FacultySubsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FacultySubsComponent]
    });
    fixture = TestBed.createComponent(FacultySubsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
