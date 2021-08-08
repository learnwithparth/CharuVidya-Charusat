package com.codehat.charusat.repository;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseSection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    /**
     * CUSTOM
     * */
    Page<CourseSection> findCourseSectionByCourse_Id(Long courseId, Pageable pageable);

    List<CourseSection> findCourseSectionByCourse_Id(Long courseId);

    Page<CourseSection> findCourseSectionByCourse_User_IdAndCourse_Id(Long userId, Long courseId, Pageable pageable);

    Page<CourseSection> findCourseSectionByCourse_IdAndCourseEnrolledUsersListsContaining(Long courseId, User user, Pageable pageable);

    @Modifying
    @Query(value = "delete from CourseSection courseSection where courseSection.course.id = :courseId")
    void deleteCourseSectionByCourseId(@Param("courseId") Long courseId);

    Integer countCourseSectionByCourseAndIsApproved(Course course, Boolean approvalValue);
}
