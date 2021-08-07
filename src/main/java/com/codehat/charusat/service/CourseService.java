package com.codehat.charusat.service;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.service.dto.CourseDTO;
import io.swagger.models.Response;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * Service Interface for managing {@link Course}.
 */
public interface CourseService {
    /**
     * Save a course.
     *
     * @param course the entity to save.
     * @return the persisted entity.
     */
    Course save(Course course);

    /**
     * Partially updates a course.
     *
     * @param course the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Course> partialUpdate(Course course);

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Course> findAll(Pageable pageable);

    /**
     * Get the "id" course.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Course> findOne(Long id);

    /**
     * Delete the "id" course.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * CUSTOM
     * */
    List<CourseDTO> getByCategoryId(Long id) throws Exception;

    ResponseEntity enrollInCourse(Course course);

    /**
     * Overloaded method.
     * Save a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    Course save(CourseDTO courseDTO);

    Course approveCourse(Long courseId);

    Page<Course> findAllCoursesByFilter(String filter, Pageable pageable);

    ResponseEntity<Integer> getStudentEnrolledCountByCourse(Long courseId);

    List<Course> getEnrolledCourses();
}
