package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseProgress;
import java.util.List;
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
}
