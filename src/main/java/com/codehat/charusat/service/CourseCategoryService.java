package com.codehat.charusat.service;

import com.codehat.charusat.domain.CourseCategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CourseCategory}.
 */
public interface CourseCategoryService {
    /**
     * Save a courseCategory.
     *
     * @param courseCategory the entity to save.
     * @return the persisted entity.
     */
    CourseCategory save(CourseCategory courseCategory);

    /**
     * Partially updates a courseCategory.
     *
     * @param courseCategory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CourseCategory> partialUpdate(CourseCategory courseCategory);

    /**
     * Get all the courseCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseCategory> findAll(Pageable pageable);

    /**
     * Get the "id" courseCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseCategory> findOne(Long id);

    /**
     * Delete the "id" courseCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
