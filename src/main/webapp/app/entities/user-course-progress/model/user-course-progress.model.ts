import { IUser } from 'app/entities/user/user.model';
import { ICourse } from 'app/entities/course/course.model';
import { ICourseProgress } from 'app/entities/course-progress/course-progress.model';

export interface IUserCourseProgress {
  id?: number;
  user?: IUser | null;
  course?: ICourse | null;
  courseProgress?: ICourseProgress | null;
}

export class UserCourseProgress implements IUserCourseProgress {
  constructor(public id?: number, public user?: IUser | null, public course?: ICourse, public courseProgress?: ICourseProgress | null) {}
}

export function getUserCourseProgressIdentifier(userCourseProgress: IUserCourseProgress): number | undefined {
  return userCourseProgress.id;
}
