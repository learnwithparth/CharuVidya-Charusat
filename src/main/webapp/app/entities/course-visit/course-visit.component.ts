import { Component, OnInit } from '@angular/core';
import { ICourseVisitModel } from 'app/entities/course-visit/course-visit.model';
import { CourseVisitService } from 'app/entities/course-visit/course-visit.service';

@Component({
  selector: 'jhi-course-visit',
  templateUrl: './course-visit.component.html',
  styleUrls: ['./course-visit.component.scss'],
})
export class CourseVisitComponent implements OnInit {
  courseVisitCounts: ICourseVisitModel[] | null = null;
  constructor(private courseVisitService: CourseVisitService) {}

  ngOnInit(): void {
    this.courseVisitService.getCourseVisits().subscribe(res => {
      this.courseVisitCounts = res.body;
    });
  }
}
