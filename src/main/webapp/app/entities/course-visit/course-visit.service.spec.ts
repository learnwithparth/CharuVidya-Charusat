import { TestBed } from '@angular/core/testing';

import { CourseVisitService } from './course-visit.service';

describe('CourseVisitService', () => {
  let service: CourseVisitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseVisitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
