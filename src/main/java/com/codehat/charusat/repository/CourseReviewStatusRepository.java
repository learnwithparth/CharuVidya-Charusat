package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface CourseReviewStatusRepository extends JpaRepository<CourseReviewStatus, Long> {}
