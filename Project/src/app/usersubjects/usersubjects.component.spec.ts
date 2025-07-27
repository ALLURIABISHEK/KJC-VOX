import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UsersubjectsComponent } from './usersubjects.component'; // ✅ Correct import

describe('UsersubjectsComponent', () => {
  let component: UsersubjectsComponent; // ✅ Correct class name
  let fixture: ComponentFixture<UsersubjectsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UsersubjectsComponent] // ✅ Correct class name
    });
    fixture = TestBed.createComponent(UsersubjectsComponent); // ✅ Correct class
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
