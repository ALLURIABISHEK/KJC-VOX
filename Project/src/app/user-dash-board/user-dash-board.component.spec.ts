import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserDashBoardComponent } from './user-dash-board.component';

describe('UserDashBoardComponent', () => {
  let component: UserDashBoardComponent;
  let fixture: ComponentFixture<UserDashBoardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserDashBoardComponent]
    });
    fixture = TestBed.createComponent(UserDashBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
