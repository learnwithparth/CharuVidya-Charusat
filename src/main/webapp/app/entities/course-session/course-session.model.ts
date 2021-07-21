import * as dayjs from 'dayjs';
import { ICourseSection } from 'app/entities/course-section/course-section.model';

export interface ICourseSession {
  id?: number;
  sessionTitle?: string;
  sessionDescription?: string | null;
  sessionVideo?: string;
  sessionDuration?: dayjs.Dayjs;
  sessionOrder?: number;
  sessionResource?: string | null;
  sessionLocation?: string;
  isPreview?: boolean;
  isDraft?: boolean;
  isApproved?: boolean;
  isPublished?: boolean;
  quizLink?: string;
  courseSection?: ICourseSection | null;
}

export class CourseSession implements ICourseSession {
  constructor(
    public id?: number,
    public sessionTitle?: string,
    public sessionDescription?: string | null,
    public sessionVideo?: string,
    public sessionDuration?: dayjs.Dayjs,
    public sessionOrder?: number,
    public sessionResource?: string | null,
    public sessionLocation?: string,
    public isPreview?: boolean,
    public isDraft?: boolean,
    public isApproved?: boolean,
    public isPublished?: boolean,
    public courseSection?: ICourseSection | null
  ) {
    this.isPreview = this.isPreview ?? false;
    this.isDraft = this.isDraft ?? false;
    this.isApproved = this.isApproved ?? false;
    this.isPublished = this.isPublished ?? false;
  }
}

export function getCourseSessionIdentifier(courseSession: ICourseSession): number | undefined {
  return courseSession.id;
}
