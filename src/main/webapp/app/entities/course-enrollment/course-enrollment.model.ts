import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ICourse } from 'app/entities/course/course.model';

export interface ICourseEnrollment {
  id?: number;
  enrollementDate?: dayjs.Dayjs;
  lastAccessedDate?: dayjs.Dayjs;
  user?: IUser | null;
  course?: ICourse | null;
}

export class CourseEnrollment implements ICourseEnrollment {
  constructor(
    public id?: number,
    public enrollementDate?: dayjs.Dayjs,
    public lastAccessedDate?: dayjs.Dayjs,
    public user?: IUser | null,
    public course?: ICourse | null
  ) {}
}

export function getCourseEnrollmentIdentifier(courseEnrollment: ICourseEnrollment): number | undefined {
  return courseEnrollment.id;
}
