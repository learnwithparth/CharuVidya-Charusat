package com.codehat.charusat.service;

import com.codehat.charusat.domain.CourseSession;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CourseSession}.
 */
public interface CourseSessionService {
    /**
     * Save a courseSession.
     *
     * @param courseSession the entity to save.
     * @return the persisted entity.
     */
    CourseSession save(CourseSession courseSession);

    /**
     * Partially updates a courseSession.
     *
     * @param courseSession the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CourseSession> partialUpdate(CourseSession courseSession);

    /**
     * Get all the courseSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseSession> findAll(Pageable pageable);

    /**
     * Get the "id" courseSession.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseSession> findOne(Long id);

    /**
     * Delete the "id" courseSession.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    /**
     * Custom
     * */
    Page<CourseSession> findCourseSessionByCourseSection(Long courseSectionId, Pageable pageable);
}
