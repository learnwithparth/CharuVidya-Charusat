package com.codehat.charusat.web.rest;

import static com.amazonaws.services.cognitoidp.model.AttributeDataType.DateTime;

import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.repository.CourseProgressRepository;
import com.codehat.charusat.repository.CourseSessionRepository;
import com.codehat.charusat.service.CourseProgressService;
import com.codehat.charusat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;
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
 * REST controller for managing {@link com.codehat.charusat.domain.CourseProgress}.
 */
@RestController
@RequestMapping("/api")
public class CourseProgressResource {

    private final Logger log = LoggerFactory.getLogger(CourseProgressResource.class);

    private static final String ENTITY_NAME = "courseProgress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseProgressService courseProgressService;

    private final CourseProgressRepository courseProgressRepository;

    private final CourseSessionRepository courseSessionRepository;

    public CourseProgressResource(
        CourseProgressService courseProgressService,
        CourseProgressRepository courseProgressRepository,
        CourseSessionRepository courseSessionRepository
    ) {
        this.courseProgressService = courseProgressService;
        this.courseProgressRepository = courseProgressRepository;
        this.courseSessionRepository = courseSessionRepository;
    }

    /**
     * {@code POST  /course-progresses} : Create a new courseProgress.
     *
     * @param courseProgress the courseProgress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseProgress, or with status {@code 400 (Bad Request)} if the courseProgress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-progresses")
    public ResponseEntity<CourseProgress> createCourseProgress(@Valid @RequestBody CourseProgress courseProgress)
        throws URISyntaxException {
        log.debug("REST request to save CourseProgress : {}", courseProgress);
        if (courseProgress.getId() != null) {
            throw new BadRequestAlertException("A new courseProgress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseProgress result = courseProgressService.save(courseProgress);
        return ResponseEntity
            .created(new URI("/api/course-progresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-progresses/:id} : Updates an existing courseProgress.
     *
     * @param id the id of the courseProgress to save.
     * @param courseProgress the courseProgress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseProgress,
     * or with status {@code 400 (Bad Request)} if the courseProgress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseProgress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-progresses/{id}")
    public ResponseEntity<CourseProgress> updateCourseProgress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseProgress courseProgress
    ) throws URISyntaxException {
        log.debug("REST request to update CourseProgress : {}, {}", id, courseProgress);
        if (courseProgress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseProgress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseProgressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseProgress result = courseProgressService.save(courseProgress);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseProgress.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-progresses/:id} : Partial updates given fields of an existing courseProgress, field will ignore if it is null
     *
     * @param id the id of the courseProgress to save.
     * @param courseProgress the courseProgress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseProgress,
     * or with status {@code 400 (Bad Request)} if the courseProgress is not valid,
     * or with status {@code 404 (Not Found)} if the courseProgress is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseProgress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-progresses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CourseProgress> partialUpdateCourseProgress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseProgress courseProgress
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseProgress partially : {}, {}", id, courseProgress);
        if (courseProgress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseProgress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseProgressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseProgress> result = courseProgressService.partialUpdate(courseProgress);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseProgress.getId().toString())
        );
    }

    /**
     * {@code GET  /course-progresses} : get all the courseProgresses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseProgresses in body.
     */
    @GetMapping("/course-progresses")
    public ResponseEntity<List<CourseProgress>> getAllCourseProgresses(Pageable pageable) {
        log.debug("REST request to get a page of CourseProgresses");
        Page<CourseProgress> page = courseProgressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-progresses/:id} : get the "id" courseProgress.
     *
     * @param id the id of the courseProgress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseProgress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-progresses/{id}")
    public ResponseEntity<CourseProgress> getCourseProgress(@PathVariable Long id) {
        log.debug("REST request to get CourseProgress : {}", id);
        Optional<CourseProgress> courseProgress = courseProgressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseProgress);
    }

    /**
     * {@code DELETE  /course-progresses/:id} : delete the "id" courseProgress.
     *
     * @param id the id of the courseProgress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-progresses/{id}")
    public ResponseEntity<Void> deleteCourseProgress(@PathVariable Long id) {
        log.debug("REST request to delete CourseProgress : {}", id);
        courseProgressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/course-progresses/currentUserWatchTime/{sessionId}")
    public ResponseEntity<CourseProgress> getUserCourseProgress(@PathVariable Long sessionId) throws URISyntaxException {
        Optional<CourseSession> session = courseSessionRepository.findById(sessionId);
        if (session.isPresent()) return ResponseEntity
            .ok()
            .body(courseProgressService.getUserProgress(session.get())); else throw new BadRequestAlertException(
            "Invalid session",
            ENTITY_NAME,
            "idnotfound"
        );
    }

    @PostMapping("/course-progresses/updateUserWatchTime")
    public ResponseEntity<Boolean> updateCourseProgressWatchTime(@RequestBody CourseProgress courseProgress) {
        log.debug("Update watch time called", courseProgress.getId());
        courseProgressService.updateTime(courseProgress);
        return ResponseEntity.ok().body(true);
    }
}
