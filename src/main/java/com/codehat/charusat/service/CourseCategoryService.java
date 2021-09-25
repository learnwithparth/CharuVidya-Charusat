package com.codehat.charusat.service;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseCategory;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.service.dto.AdminUserDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

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

    List<CourseCategory> findAll();

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

    /**
     *
     * @return list of categories where is_parent is 1
     */
    List<CourseCategory> listParentCategory();

    /**
     *
     * @param id
     * @return list of sub-categories of a parent category
     */
    List<CourseCategory> listByParentId(Long id);

    ResponseEntity<Map<Long, Integer>> getCourseCountBySubCategory(Long parentId);

    ResponseEntity<Map<Long, Integer>> getCourseCountByParentCategory();

    ResponseEntity<List<CourseCategory>> getCourseSubCategories();

    ResponseEntity setReviewerInSubCategories(List<CourseCategory> reviewerCategories, AdminUserDTO user) throws Exception;

    ResponseEntity<Set<User>> getReviewerByCourseCategoryId(Long courseCategoryId) throws Exception;

    ResponseEntity setReviewerInSubCategories(Long courseCategoryId, Set<User> reviewers) throws Exception;

    Map<String, List<Course>> getCoursesBySubCategories();

    Map<String, List<CourseCategory>> getCourseSubCategoriesByParentCategories();
}
