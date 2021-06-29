package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {

    /**
     * CUSTOM
     * */
    Page<CourseSession> findAllByCourseSection_Id(Long courseSectionId, Pageable pageable);
}
