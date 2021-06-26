package com.codehat.charusat.web.rest;

import com.codehat.charusat.domain.CourseEnrollment;
import com.codehat.charusat.repository.CourseEnrollmentRepository;
import com.codehat.charusat.service.CourseEnrollmentService;
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
 * REST controller for managing {@link com.codehat.charusat.domain.CourseEnrollment}.
 */
@RestController
@RequestMapping("/api")
public class CourseEnrollmentResource {

    private final Logger log = LoggerFactory.getLogger(CourseEnrollmentResource.class);

    private static final String ENTITY_NAME = "courseEnrollment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseEnrollmentService courseEnrollmentService;

    private final CourseEnrollmentRepository courseEnrollmentRepository;

    public CourseEnrollmentResource(
        CourseEnrollmentService courseEnrollmentService,
        CourseEnrollmentRepository courseEnrollmentRepository
    ) {
        this.courseEnrollmentService = courseEnrollmentService;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
    }

    /**
     * {@code POST  /course-enrollments} : Create a new courseEnrollment.
     *
     * @param courseEnrollment the courseEnrollment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseEnrollment, or with status {@code 400 (Bad Request)} if the courseEnrollment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-enrollments")
    public ResponseEntity<CourseEnrollment> createCourseEnrollment(@Valid @RequestBody CourseEnrollment courseEnrollment)
        throws URISyntaxException {
        log.debug("REST request to save CourseEnrollment : {}", courseEnrollment);
        if (courseEnrollment.getId() != null) {
            throw new BadRequestAlertException("A new courseEnrollment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseEnrollment result = courseEnrollmentService.save(courseEnrollment);
        return ResponseEntity
            .created(new URI("/api/course-enrollments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-enrollments/:id} : Updates an existing courseEnrollment.
     *
     * @param id the id of the courseEnrollment to save.
     * @param courseEnrollment the courseEnrollment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseEnrollment,
     * or with status {@code 400 (Bad Request)} if the courseEnrollment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseEnrollment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-enrollments/{id}")
    public ResponseEntity<CourseEnrollment> updateCourseEnrollment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseEnrollment courseEnrollment
    ) throws URISyntaxException {
        log.debug("REST request to update CourseEnrollment : {}, {}", id, courseEnrollment);
        if (courseEnrollment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseEnrollment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseEnrollmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseEnrollment result = courseEnrollmentService.save(courseEnrollment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseEnrollment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-enrollments/:id} : Partial updates given fields of an existing courseEnrollment, field will ignore if it is null
     *
     * @param id the id of the courseEnrollment to save.
     * @param courseEnrollment the courseEnrollment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseEnrollment,
     * or with status {@code 400 (Bad Request)} if the courseEnrollment is not valid,
     * or with status {@code 404 (Not Found)} if the courseEnrollment is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseEnrollment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-enrollments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CourseEnrollment> partialUpdateCourseEnrollment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseEnrollment courseEnrollment
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseEnrollment partially : {}, {}", id, courseEnrollment);
        if (courseEnrollment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseEnrollment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseEnrollmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseEnrollment> result = courseEnrollmentService.partialUpdate(courseEnrollment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseEnrollment.getId().toString())
        );
    }

    /**
     * {@code GET  /course-enrollments} : get all the courseEnrollments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseEnrollments in body.
     */
    @GetMapping("/course-enrollments")
    public ResponseEntity<List<CourseEnrollment>> getAllCourseEnrollments(Pageable pageable) {
        log.debug("REST request to get a page of CourseEnrollments");
        Page<CourseEnrollment> page = courseEnrollmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-enrollments/:id} : get the "id" courseEnrollment.
     *
     * @param id the id of the courseEnrollment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseEnrollment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-enrollments/{id}")
    public ResponseEntity<CourseEnrollment> getCourseEnrollment(@PathVariable Long id) {
        log.debug("REST request to get CourseEnrollment : {}", id);
        Optional<CourseEnrollment> courseEnrollment = courseEnrollmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseEnrollment);
    }

    /**
     * {@code DELETE  /course-enrollments/:id} : delete the "id" courseEnrollment.
     *
     * @param id the id of the courseEnrollment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-enrollments/{id}")
    public ResponseEntity<Void> deleteCourseEnrollment(@PathVariable Long id) {
        log.debug("REST request to delete CourseEnrollment : {}", id);
        courseEnrollmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
