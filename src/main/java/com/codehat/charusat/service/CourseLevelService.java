package com.codehat.charusat.service;

import com.codehat.charusat.domain.CourseLevel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CourseLevel}.
 */
public interface CourseLevelService {
    /**
     * Save a courseLevel.
     *
     * @param courseLevel the entity to save.
     * @return the persisted entity.
     */
    CourseLevel save(CourseLevel courseLevel);

    /**
     * Partially updates a courseLevel.
     *
     * @param courseLevel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CourseLevel> partialUpdate(CourseLevel courseLevel);

    /**
     * Get all the courseLevels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseLevel> findAll(Pageable pageable);

    /**
     * Get the "id" courseLevel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseLevel> findOne(Long id);

    /**
     * Delete the "id" courseLevel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
