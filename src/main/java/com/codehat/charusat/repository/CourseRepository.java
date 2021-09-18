package com.codehat.charusat.repository;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
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

    @Query(
        value = "select course from Course course where course.courseCategory.id = :id and course.isApproved = true order by course.courseTitle"
    )
    List<Course> findByCategoryId(@Param("id") Long id);

    List<Course> findCourseByEnrolledUsersListsContaining(User user);

    Page<Course> findAllByIsApproved(Boolean value, Pageable pageable);

    @Query(value = "SELECT * FROM Codehat_Charusat.course order by course_updated_on desc limit 10", nativeQuery = true)
    List<Course> coursesOrderedByUpdatedDate();

    @Query(value = "select count(*) from rel_course__enrolled_users_list", nativeQuery = true)
    Integer findTotalEnrollment();
}
