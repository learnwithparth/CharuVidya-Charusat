package com.codehat.charusat.repository;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseReviewStatus;
import com.codehat.charusat.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface CourseReviewStatusRepository extends JpaRepository<CourseReviewStatus, Long> {
    List<CourseReviewStatus> findCourseReviewStatusByReviewer(User user);

    void deleteCourseReviewStatusByCourseId(Long id);
}
