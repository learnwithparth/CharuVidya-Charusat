import { Component, OnInit } from '@angular/core';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CourseSectionService } from 'app/entities/course-section/service/course-section.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { CourseSectionDeleteDialogComponent } from 'app/entities/course-section/delete/course-section-delete-dialog.component';
import { combineLatest } from 'rxjs';
import { InstructorCourseSectionService } from 'app/entities/instructor-pages/instructor-coursesection/instructor-coursesection.service';

@Component({
  selector: 'jhi-instructor-coursesection',
  templateUrl: './instructor-coursesection.component.html',
  styleUrls: ['./instructor-coursesection.component.scss'],
})
export class InstructorCoursesectionComponent implements OnInit {
  courseSections?: ICourseSection[] | null;
  isLoading = false;
  courseId!: string | null;

  constructor(
    protected courseSectionService: InstructorCourseSectionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    const hasCourseId: boolean = this.activatedRoute.snapshot.paramMap.has('courseId');
    if (hasCourseId) {
      this.courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    }
    this.loadSections();
  }

  trackId(index: number, item: ICourseSection): number {
    return item.id!;
  }

  delete(courseSection: ICourseSection): void {
    const modalRef = this.modalService.open(CourseSectionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.courseSection = courseSection;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadSections();
      }
    });
  }

  loadPage(): void {
    this.isLoading = true;
    this.loadSections();
    this.isLoading = false;
  }

  addSection(): void {
    if (this.courseId !== null) {
      this.router.navigate([`course/${this.courseId}/section/new`]);
    }
  }

  private loadSections(): void {
    if (this.courseId !== null) {
      this.courseSectionService.query(this.courseId).subscribe(res => {
        this.courseSections = res.body;
      });
    }
  }
}
