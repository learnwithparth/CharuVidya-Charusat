import { ICourse } from 'app/entities/course/course.model.ts';

export interface ICourseLevel {
  id?: number;
  level?: string | null;
  description?: string | null;
  levels?: ICourse[] | null;
}

export class CourseLevel implements ICourseLevel {
  constructor(public id?: number, public level?: string | null, public description?: string | null, public levels?: ICourse[] | null) {}
}

export function getCourseLevelIdentifier(courseLevel: ICourseLevel): number | undefined {
  return courseLevel.id;
}
