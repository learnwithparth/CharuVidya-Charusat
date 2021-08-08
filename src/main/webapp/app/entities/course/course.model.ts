import * as dayjs from 'dayjs';
import { ICourseLevel } from 'app/entities/course-level/course-level.model';
import { ICourseCategory } from 'app/entities/course-category/course-category.model';
import { IUser } from 'app/entities/user/user.model';

export interface ICourse {
  id?: number;
  courseTitle?: string;
  courseDescription?: string;
  courseObjectives?: string | null;
  courseSubTitle?: string;
  previewVideourl?: string;
  courseLength?: number | null;
  minStudents?: number;
  maxStudents?: number;
  logo?: string;
  courseCreatedOn?: dayjs.Dayjs;
  courseUpdatedOn?: dayjs.Dayjs;
  courseRootDir?: string | null;
  amount?: number | null;
  isDraft?: boolean;
  isApproved?: boolean;
  courseApprovalDate?: dayjs.Dayjs | null;
  courseLevel?: ICourseLevel | null;
  courseCategory?: ICourseCategory | null;
  user?: IUser | null;
  reviewer?: IUser | null;
  enrolled?: boolean | null;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public courseTitle?: string,
    public courseDescription?: string,
    public courseObjectives?: string | null,
    public courseSubTitle?: string,
    public previewVideourl?: string,
    public courseLength?: number | null,
    public minStudents?: number,
    public maxStudents?: number,
    public logo?: string,
    public courseCreatedOn?: dayjs.Dayjs,
    public courseUpdatedOn?: dayjs.Dayjs,
    public courseRootDir?: string | null,
    public amount?: number | null,
    public isDraft?: boolean,
    public isApproved?: boolean,
    public courseApprovalDate?: dayjs.Dayjs | null,
    public courseLevel?: ICourseLevel | null,
    public courseCategory?: ICourseCategory | null,
    public user?: IUser | null,
    public reviewer?: IUser | null,
    public enrolled?: boolean
  ) {
    this.isDraft = this.isDraft ?? false;
    this.isApproved = this.isApproved ?? false;
  }
}

export function getCourseIdentifier(course: ICourse): number | undefined {
  return course.id;
}
