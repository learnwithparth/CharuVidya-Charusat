package com.codehat.charusat.service;

import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.service.dto.CourseSectionDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link CourseSection}.
 */
public interface CourseSectionService {
    /**
     * Save a courseSection.
     *
     * @param courseSection the entity to save.
     * @return the persisted entity.
     */
    CourseSection save(CourseSection courseSection);

    /**
     * Partially updates a courseSection.
     *
     * @param courseSection the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CourseSection> partialUpdate(CourseSection courseSection);

    /**
     * Get all the courseSections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseSection> findAll(Pageable pageable);

    /**
     * Get the "id" courseSection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseSection> findOne(Long id);

    /**
     * Delete the "id" courseSection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * CUSTOM:
     * Get the CourseSection based on course.
     * */
    Page<CourseSection> findCourseSectionByCourse(Long courseId, Pageable pageable) throws Exception;

    CourseSection save(Long courseId, CourseSectionDTO courseSectionDTO);

    ResponseEntity<Map<CourseSection, List<CourseSession>>> findAllCourseSectionAndSessionByCourse(Long courseId);

    CourseSection approveCourseSection(Long id) throws Exception;
}
