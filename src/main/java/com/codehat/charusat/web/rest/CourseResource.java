package com.codehat.charusat.web.rest;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseRepository;
import com.codehat.charusat.service.CourseService;
import com.codehat.charusat.service.dto.CourseDTO;
import com.codehat.charusat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.codehat.charusat.domain.Course}.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseService courseService;

    private final CourseRepository courseRepository;

    public CourseResource(CourseService courseService, CourseRepository courseRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param course the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Course result = courseService.save(course);
        return ResponseEntity
            .created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courses/:id} : Updates an existing course.
     *
     * @param id the id of the course to save.
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Course course
    ) throws URISyntaxException {
        log.debug("REST request to update Course : {}, {}", id, course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, course.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Course result = courseService.save(course);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, course.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /courses/:id} : Partial updates given fields of an existing course, field will ignore if it is null
     *
     * @param id the id of the course to save.
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 404 (Not Found)} if the course is not found,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/courses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Course> partialUpdateCourse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Course course
    ) throws URISyntaxException {
        log.debug("REST request to partial update Course partially : {}, {}", id, course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, course.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Course> result = courseService.partialUpdate(course);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, course.getId().toString())
        );
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses(Pageable pageable) {
        log.debug("REST request to get a page of Courses");
        Page<Course> page = courseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * CUSTOM
     * {@code GET  /courses/category/{categoryId}} : get all the courses related to a particular category.
     *
     * @param categoryId the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses/category/{categoryId}")
    public ResponseEntity<List<Course>> getCourseByCategory(@PathVariable Long categoryId) {
        log.debug("REST request to get Course by categoryId : {}", categoryId);
        List<Course> list = courseService.getByCategoryId(categoryId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * CUSTOM
     * {@code POST  /courses/enroll} : Enroll in a particular course.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @PostMapping("/courses/enroll")
    public ResponseEntity enrollInCourse(@RequestBody Course course) {
        log.debug("REST request to enroll in Course : {}", course);
        return courseService.enrollInCourse(course);
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the course to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the course, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        Optional<Course> course = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(course);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the course to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * CUSTOM
     * */
    @PostMapping("/instructor-course")
    public ResponseEntity<Course> createCourseInstructor(@Valid @RequestBody CourseDTO courseDTO) throws URISyntaxException {
        log.debug("REST request to save Course : {}", courseDTO);
        Course result = courseService.save(courseDTO);
        return ResponseEntity
            .created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/course/{courseId}/approve")
    public ResponseEntity<Course> approveCourse(@PathVariable Long courseId) throws URISyntaxException {
        log.debug("REST request to approve CourseId : {}", courseId);
        Course result = courseService.approveCourse(courseId);
        return ResponseEntity
            .created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
