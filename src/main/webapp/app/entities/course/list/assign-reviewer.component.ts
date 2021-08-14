import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-assign-reviewer',
  templateUrl: './assign-reviewer.component.html',
})
export class AssignReviewerComponent implements OnInit {
  editForm: any;

  ngOnInit(): void {}

  save(value: any): void {}

  previousState(): void {
    window.history.back();
  }
}
