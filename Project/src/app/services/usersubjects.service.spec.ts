import { TestBed } from '@angular/core/testing';

import { UsersubjectsService } from './usersubjects.service';

describe('UsersubjectsService', () => {
  let service: UsersubjectsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsersubjectsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
