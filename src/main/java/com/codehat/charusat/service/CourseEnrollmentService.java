package com.codehat.charusat.service;

import com.codehat.charusat.domain.CourseEnrollment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CourseEnrollment}.
 */
public interface CourseEnrollmentService {
    /**
     * Save a courseEnrollment.
     *
     * @param courseEnrollment the entity to save.
     * @return the persisted entity.
     */
    CourseEnrollment save(CourseEnrollment courseEnrollment);

    /**
     * Partially updates a courseEnrollment.
     *
     * @param courseEnrollment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CourseEnrollment> partialUpdate(CourseEnrollment courseEnrollment);

    /**
     * Get all the courseEnrollments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseEnrollment> findAll(Pageable pageable);

    /**
     * Get the "id" courseEnrollment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseEnrollment> findOne(Long id);

    /**
     * Delete the "id" courseEnrollment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
