package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.domain.CourseSession;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {
    /**
     * CUSTOM
     * */
    Page<CourseSession> findAllByCourseSection_Id(Long courseSectionId, Pageable pageable);

    Page<CourseSession> findAllByCourseSection_IdAndIsApproved(Long courseSectionId, Boolean value, Pageable pageable);

    Page<CourseSession> findAllByCourseSection_IdAndIsApproved(Long courseSectionId, boolean value, Pageable pageable);

    List<CourseSession> findAllByCourseSection_Id(Long courseSectionId);

    @Modifying
    @Query(
        value = "delete from CourseSession courseSession where courseSession.courseSection.id in (" +
        "select courseSection.id from CourseSection courseSection where courseSection.course.id in (" +
        "select course.id from Course course where course.id = :courseId" +
        ")" +
        ")"
    )
    void deleteCourseSessionByCourseId(@Param("courseId") Long courseId);

    @Query(value = "delete from CourseSession courseSession where courseSession.courseSection.id = :courseSectionId")
    @Modifying
    void deleteCourseSessionByCourseSectionId(@Param("courseSectionId") Long courseSectionId);

    Integer countCourseSessionByCourseSectionAndIsApproved(CourseSection courseSection, Boolean approvalValue);
}
