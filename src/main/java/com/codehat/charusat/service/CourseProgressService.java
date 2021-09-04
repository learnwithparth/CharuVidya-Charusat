package com.codehat.charusat.service;

import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.domain.CourseSession;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CourseProgress}.
 */
public interface CourseProgressService {
    /**
     * Save a courseProgress.
     *
     * @param courseProgress the entity to save.
     * @return the persisted entity.
     */
    CourseProgress save(CourseProgress courseProgress);

    /**
     * Partially updates a courseProgress.
     *
     * @param courseProgress the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CourseProgress> partialUpdate(CourseProgress courseProgress);

    /**
     * Get all the courseProgresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseProgress> findAll(Pageable pageable);

    /**
     * Get the "id" courseProgress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseProgress> findOne(Long id);

    /**
     * Delete the "id" courseProgress.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean updateTime(CourseProgress courseProgress);

    CourseProgress getUserProgress(CourseSession courseSession);
}
