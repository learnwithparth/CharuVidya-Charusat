import { TestBed } from '@angular/core/testing';

import { UserCourseProgressService } from './user-course-progress.service';

describe('UserCourseProgressService', () => {
  let service: UserCourseProgressService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserCourseProgressService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
