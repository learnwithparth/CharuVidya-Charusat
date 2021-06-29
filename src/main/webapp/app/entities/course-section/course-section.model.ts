import { ICourse } from 'app/entities/course/course.model';

export interface ICourseSection {
  id?: number;
  sectionTitle?: string;
  sectionDescription?: string | null;
  sectionOrder?: number;
  isDraft?: boolean;
  isApproved?: boolean;
  course?: ICourse | null;
}

export class CourseSection implements ICourseSection {
  constructor(
    public id?: number,
    public sectionTitle?: string,
    public sectionDescription?: string | null,
    public sectionOrder?: number,
    public isDraft?: boolean,
    public isApproved?: boolean,
    public course?: ICourse | null
  ) {
    this.isDraft = this.isDraft ?? false;
    this.isApproved = this.isApproved ?? false;
  }
}

export function getCourseSectionIdentifier(courseSection: ICourseSection): number | undefined {
  return courseSection.id;
}
