import { ICourse } from 'app/entities/course/course.model';
import { IUser } from 'app/entities/user/user.model';

export interface ICourseVisitModel {
  id?: number;
  course?: ICourse;
  user?: IUser;
  lastVisitedDate?: Date;
  courseVisitedCount?: number;
}

export class CourseVisitModel implements ICourseVisitModel {
  constructor(
    public id?: number,
    public course?: ICourse,
    public user?: IUser,
    public lastVisitedDate?: Date,
    public courseVisitedCount?: number
  ) {}
}
