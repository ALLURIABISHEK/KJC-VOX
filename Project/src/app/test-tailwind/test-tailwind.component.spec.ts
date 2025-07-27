import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestTailwindComponent } from './test-tailwind.component';

describe('TestTailwindComponent', () => {
  let component: TestTailwindComponent;
  let fixture: ComponentFixture<TestTailwindComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestTailwindComponent]
    });
    fixture = TestBed.createComponent(TestTailwindComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
