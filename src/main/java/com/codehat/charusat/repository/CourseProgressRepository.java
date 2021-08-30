package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
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
}
