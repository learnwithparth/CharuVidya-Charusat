package com.codehat.charusat.repository;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
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
}
