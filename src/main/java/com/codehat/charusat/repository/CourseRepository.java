package com.codehat.charusat.repository;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
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

    /**
     * CUSTOM
     * */
    Page<Course> findCourseByUserEqualsOrEnrolledUsersListsContaining(User author, User user, Pageable pageable);

    @Query(value = "select course from Course course where course.courseCategory.id = :id order by course.courseTitle")
    List<Course> findByCategoryId(@Param("id") Long id);

    Page<Course> findCourseByEnrolledUsersListsContaining(User user, Pageable pageable);

    Page<Course> findAllByIsApproved(Boolean value, Pageable pageable);
}
