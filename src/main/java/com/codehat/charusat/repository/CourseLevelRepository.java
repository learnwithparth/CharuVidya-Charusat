package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseLevelRepository extends JpaRepository<CourseLevel, Long> {}
