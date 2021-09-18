import { TestBed } from '@angular/core/testing';

import { AssignCategoryReviewerService } from './assign-category-reviewer.service';

describe('AssignCategoryReviewerService', () => {
  let service: AssignCategoryReviewerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AssignCategoryReviewerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
