import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-assign-reviewer',
  templateUrl: './assign-reviewer.component.html',
})
export class AssignReviewerComponent implements OnInit {
  editForm: any;

  ngOnInit(): void {
    console.warn('in ngoninit');
  }

  save(value: any): void {
    console.warn('in ngoninit');
  }

  previousState(): void {
    window.history.back();
  }
}
