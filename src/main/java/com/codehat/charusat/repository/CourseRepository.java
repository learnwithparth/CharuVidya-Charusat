package com.codehat.charusat.repository;

import com.codehat.charusat.domain.Course;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select course from Course course where course.user.login = ?#{principal.username}")
    List<Course> findByUserIsCurrentUser();

    @Query("select course from Course course where course.reviewer.login = ?#{principal.username}")
    List<Course> findByReviewerIsCurrentUser();

    @Query(value = "SELECT * from course where course_category_id = ?1", nativeQuery = true)
    List<Course> findByCategoryId(Long id);
}
