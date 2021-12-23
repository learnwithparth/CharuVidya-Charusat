package com.codehat.charusat.repository;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseVisit;
import com.codehat.charusat.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseVisitRepository extends JpaRepository<CourseVisit, Long> {
    Optional<CourseVisit> findCourseVisitByCourseAndUser(Course course, User user);
}
