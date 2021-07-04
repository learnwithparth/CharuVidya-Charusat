import { Component, OnInit } from '@angular/core';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { InstructorCourseSectionService } from 'app/entities/instructor-pages/instructor-coursesection/instructor-coursesection.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CourseSectionDeleteDialogComponent } from 'app/entities/course-section/delete/course-section-delete-dialog.component';

@Component({
  selector: 'jhi-user-course-sections',
  templateUrl: './user-course-sections.component.html',
  styleUrls: ['./user-course-sections.component.scss'],
})
export class UserCourseSectionsComponent implements OnInit {
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

  private loadSections(): void {
    if (this.courseId !== null) {
      this.courseSectionService.query(this.courseId).subscribe(res => {
        this.courseSections = res.body;
      });
    }
  }
}
