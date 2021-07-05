package com.codehat.charusat.web.rest;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.repository.CourseSectionRepository;
import com.codehat.charusat.service.CourseSectionService;
import com.codehat.charusat.service.dto.CourseSectionDTO;
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
 * REST controller for managing {@link com.codehat.charusat.domain.CourseSection}.
 */
@RestController
@RequestMapping("/api")
public class CourseSectionResource {

    private final Logger log = LoggerFactory.getLogger(CourseSectionResource.class);

    private static final String ENTITY_NAME = "courseSection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseSectionService courseSectionService;

    private final CourseSectionRepository courseSectionRepository;

    public CourseSectionResource(CourseSectionService courseSectionService, CourseSectionRepository courseSectionRepository) {
        this.courseSectionService = courseSectionService;
        this.courseSectionRepository = courseSectionRepository;
    }

    /**
     * {@code POST  /course-sections} : Create a new courseSection.
     *
     * @param courseSection the courseSection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseSection, or with status {@code 400 (Bad Request)} if the courseSection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-sections")
    public ResponseEntity<CourseSection> createCourseSection(@Valid @RequestBody CourseSection courseSection) throws URISyntaxException {
        log.debug("REST request to save CourseSection : {}", courseSection);
        if (courseSection.getId() != null) {
            throw new BadRequestAlertException("A new courseSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseSection result = courseSectionService.save(courseSection);
        return ResponseEntity
            .created(new URI("/api/course-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-sections/:id} : Updates an existing courseSection.
     *
     * @param id the id of the courseSection to save.
     * @param courseSection the courseSection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseSection,
     * or with status {@code 400 (Bad Request)} if the courseSection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseSection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-sections/{id}")
    public ResponseEntity<CourseSection> updateCourseSection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseSection courseSection
    ) throws URISyntaxException {
        log.debug("REST request to update CourseSection : {}, {}", id, courseSection);
        if (courseSection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseSection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseSection result = courseSectionService.save(courseSection);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseSection.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-sections/:id} : Partial updates given fields of an existing courseSection, field will ignore if it is null
     *
     * @param id the id of the courseSection to save.
     * @param courseSection the courseSection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseSection,
     * or with status {@code 400 (Bad Request)} if the courseSection is not valid,
     * or with status {@code 404 (Not Found)} if the courseSection is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseSection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-sections/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CourseSection> partialUpdateCourseSection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseSection courseSection
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseSection partially : {}, {}", id, courseSection);
        if (courseSection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseSection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseSection> result = courseSectionService.partialUpdate(courseSection);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseSection.getId().toString())
        );
    }

    /**
     * {@code GET  /course-sections} : get all the courseSections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseSections in body.
     */
    @GetMapping("/course-sections")
    public ResponseEntity<List<CourseSection>> getAllCourseSections(Pageable pageable) {
        log.debug("REST request to get a page of CourseSections");
        Page<CourseSection> page = courseSectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-sections/:id} : get the "id" courseSection.
     *
     * @param id the id of the courseSection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseSection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-sections/{id}")
    public ResponseEntity<CourseSection> getCourseSection(@PathVariable Long id) {
        log.debug("REST request to get CourseSection : {}", id);
        Optional<CourseSection> courseSection = courseSectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseSection);
    }

    /**
     * {@code DELETE  /course-sections/:id} : delete the "id" courseSection.
     *
     * @param id the id of the courseSection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-sections/{id}")
    public ResponseEntity<Void> deleteCourseSection(@PathVariable Long id) {
        log.debug("REST request to delete CourseSection : {}", id);
        courseSectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/course/{courseId}/course-sections")
    public ResponseEntity<CourseSection> createCourseSection(
        @PathVariable Long courseId,
        @RequestBody CourseSectionDTO courseSectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CourseSection : {}", courseSectionDTO);
        CourseSection result = courseSectionService.save(courseId, courseSectionDTO);
        return ResponseEntity
            .created(new URI("/api/course-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * CUSTOM
     *
     * @param courseId the course of the courseSection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseSection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course/{courseId}/course-sections")
    public ResponseEntity<List<CourseSection>> getAllCourseSectionByCourse(
        @PathVariable Long courseId,
        Pageable pageable
        ){
        log.debug("REST request to get Course-Section based on CourseId: {}", courseId);
        Page<CourseSection> page = courseSectionService.findCourseSectionByCourse(courseId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
