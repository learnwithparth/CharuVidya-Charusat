package com.codehat.charusat.repository;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.domain.UserCourseProgress;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {
    Optional<UserCourseProgress> findByUserAndCourse(User user, Course course);

    @Modifying
    @Query(
        value = "delete from UserCourseProgress userCourseProgress where userCourseProgress.courseProgress.id in(" +
        "select courseProgress.id from CourseProgress courseProgress where courseProgress.courseSession in (" +
        "            select courseSession.id from CourseSession courseSession where courseSession.courseSection.id in (" +
        "            select courseSection.id from CourseSection courseSection where courseSection.course.id in (" +
        "            select course.id from Course course where course.id = :courseId))))"
    )
    void deleteUserCourseProgressByCourseId(@Param("courseId") Long courseId);

    void deleteUserCourseProgressByCourseProgressId(Long courseProgressId);
}
