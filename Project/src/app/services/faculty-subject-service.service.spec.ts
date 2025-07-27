import { TestBed } from '@angular/core/testing';

import { FacultySubjectService } from './faculty-subject-service.service';

describe('FacultySubjectServiceService', () => {
  let service: FacultySubjectService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FacultySubjectService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
