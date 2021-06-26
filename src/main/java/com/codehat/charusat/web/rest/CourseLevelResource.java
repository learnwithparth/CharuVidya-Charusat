package com.codehat.charusat.web.rest;

import com.codehat.charusat.domain.CourseLevel;
import com.codehat.charusat.repository.CourseLevelRepository;
import com.codehat.charusat.service.CourseLevelService;
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
 * REST controller for managing {@link com.codehat.charusat.domain.CourseLevel}.
 */
@RestController
@RequestMapping("/api")
public class CourseLevelResource {

    private final Logger log = LoggerFactory.getLogger(CourseLevelResource.class);

    private static final String ENTITY_NAME = "courseLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseLevelService courseLevelService;

    private final CourseLevelRepository courseLevelRepository;

    public CourseLevelResource(CourseLevelService courseLevelService, CourseLevelRepository courseLevelRepository) {
        this.courseLevelService = courseLevelService;
        this.courseLevelRepository = courseLevelRepository;
    }

    /**
     * {@code POST  /course-levels} : Create a new courseLevel.
     *
     * @param courseLevel the courseLevel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseLevel, or with status {@code 400 (Bad Request)} if the courseLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-levels")
    public ResponseEntity<CourseLevel> createCourseLevel(@Valid @RequestBody CourseLevel courseLevel) throws URISyntaxException {
        log.debug("REST request to save CourseLevel : {}", courseLevel);
        if (courseLevel.getId() != null) {
            throw new BadRequestAlertException("A new courseLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseLevel result = courseLevelService.save(courseLevel);
        return ResponseEntity
            .created(new URI("/api/course-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-levels/:id} : Updates an existing courseLevel.
     *
     * @param id the id of the courseLevel to save.
     * @param courseLevel the courseLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseLevel,
     * or with status {@code 400 (Bad Request)} if the courseLevel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-levels/{id}")
    public ResponseEntity<CourseLevel> updateCourseLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseLevel courseLevel
    ) throws URISyntaxException {
        log.debug("REST request to update CourseLevel : {}, {}", id, courseLevel);
        if (courseLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseLevel result = courseLevelService.save(courseLevel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseLevel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-levels/:id} : Partial updates given fields of an existing courseLevel, field will ignore if it is null
     *
     * @param id the id of the courseLevel to save.
     * @param courseLevel the courseLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseLevel,
     * or with status {@code 400 (Bad Request)} if the courseLevel is not valid,
     * or with status {@code 404 (Not Found)} if the courseLevel is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-levels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CourseLevel> partialUpdateCourseLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseLevel courseLevel
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseLevel partially : {}, {}", id, courseLevel);
        if (courseLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseLevel> result = courseLevelService.partialUpdate(courseLevel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseLevel.getId().toString())
        );
    }

    /**
     * {@code GET  /course-levels} : get all the courseLevels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseLevels in body.
     */
    @GetMapping("/course-levels")
    public ResponseEntity<List<CourseLevel>> getAllCourseLevels(Pageable pageable) {
        log.debug("REST request to get a page of CourseLevels");
        Page<CourseLevel> page = courseLevelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-levels/:id} : get the "id" courseLevel.
     *
     * @param id the id of the courseLevel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseLevel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-levels/{id}")
    public ResponseEntity<CourseLevel> getCourseLevel(@PathVariable Long id) {
        log.debug("REST request to get CourseLevel : {}", id);
        Optional<CourseLevel> courseLevel = courseLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseLevel);
    }

    /**
     * {@code DELETE  /course-levels/:id} : delete the "id" courseLevel.
     *
     * @param id the id of the courseLevel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-levels/{id}")
    public ResponseEntity<Void> deleteCourseLevel(@PathVariable Long id) {
        log.debug("REST request to delete CourseLevel : {}", id);
        courseLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
