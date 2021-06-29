import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ICourseSession } from 'app/entities/course-session/course-session.model';

export interface ICourseProgress {
  id?: number;
  completed?: boolean;
  watchSeconds?: dayjs.Dayjs;
  user?: IUser | null;
  courseSession?: ICourseSession | null;
}

export class CourseProgress implements ICourseProgress {
  constructor(
    public id?: number,
    public completed?: boolean,
    public watchSeconds?: dayjs.Dayjs,
    public user?: IUser | null,
    public courseSession?: ICourseSession | null
  ) {
    this.completed = this.completed ?? false;
  }
}

export function getCourseProgressIdentifier(courseProgress: ICourseProgress): number | undefined {
  return courseProgress.id;
}
