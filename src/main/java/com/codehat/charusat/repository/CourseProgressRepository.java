package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseProgress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {
    @Query("select courseProgress from CourseProgress courseProgress where courseProgress.user.login = ?#{principal.username}")
    List<CourseProgress> findByUserIsCurrentUser();

    Optional<CourseProgress> findByCourseSessionAndUser(CourseSession courseSession, User user);

    @Modifying
    @Query(
        value = "delete from CourseProgress courseProgress where  courseProgress.courseSession in (" +
        "select courseSession.id from CourseSession courseSession where courseSession.courseSection.id in (" +
        "select courseSection.id from CourseSection courseSection where courseSection.course.id in (" +
        "select course.id from Course course where course.id = :courseId)))"
    )
    void deleteCourseProgressByCourseId(@Param("courseId") Long courseId);
}
