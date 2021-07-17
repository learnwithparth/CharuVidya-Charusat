package com.codehat.charusat.web.rest;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.repository.CourseSessionRepository;
import com.codehat.charusat.service.CourseSessionService;
import com.codehat.charusat.service.dto.CourseSectionDTO;
import com.codehat.charusat.service.dto.CourseSessionDTO;
import com.codehat.charusat.web.rest.errors.BadRequestAlertException;
import io.github.techgnious.exception.VideoException;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.codehat.charusat.domain.CourseSession}.
 */
@RestController
@RequestMapping("/api")
public class CourseSessionResource {

    private final Logger log = LoggerFactory.getLogger(CourseSessionResource.class);

    private static final String ENTITY_NAME = "courseSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseSessionService courseSessionService;

    private final CourseSessionRepository courseSessionRepository;

    public CourseSessionResource(CourseSessionService courseSessionService, CourseSessionRepository courseSessionRepository) {
        this.courseSessionService = courseSessionService;
        this.courseSessionRepository = courseSessionRepository;
    }

    /**
     * {@code POST  /course-sessions} : Create a new courseSession.
     *
     * @param courseSession the courseSession to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseSession, or with status {@code 400 (Bad Request)} if the courseSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-sessions")
    public ResponseEntity<CourseSession> createCourseSession(@Valid @RequestBody CourseSession courseSession) throws URISyntaxException {
        log.debug("REST request to save CourseSession : {}", courseSession);
        if (courseSession.getId() != null) {
            throw new BadRequestAlertException("A new courseSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseSession result = courseSessionService.save(courseSession);
        return ResponseEntity
            .created(new URI("/api/course-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-sessions/:id} : Updates an existing courseSession.
     *
     * @param id the id of the courseSession to save.
     * @param courseSession the courseSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseSession,
     * or with status {@code 400 (Bad Request)} if the courseSession is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-sessions/{id}")
    public ResponseEntity<CourseSession> updateCourseSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseSession courseSession
    ) throws URISyntaxException {
        log.debug("REST request to update CourseSession : {}, {}", id, courseSession);
        if (courseSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseSession result = courseSessionService.save(courseSession);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseSession.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-sessions/:id} : Partial updates given fields of an existing courseSession, field will ignore if it is null
     *
     * @param id the id of the courseSession to save.
     * @param courseSession the courseSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseSession,
     * or with status {@code 400 (Bad Request)} if the courseSession is not valid,
     * or with status {@code 404 (Not Found)} if the courseSession is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-sessions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CourseSession> partialUpdateCourseSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseSession courseSession
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseSession partially : {}, {}", id, courseSession);
        if (courseSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseSession> result = courseSessionService.partialUpdate(courseSession);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseSession.getId().toString())
        );
    }

    /**
     * {@code GET  /course-sessions} : get all the courseSessions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseSessions in body.
     */
    @GetMapping("/course-sessions")
    public ResponseEntity<List<CourseSession>> getAllCourseSessions(Pageable pageable) {
        log.debug("REST request to get a page of CourseSessions");
        Page<CourseSession> page = courseSessionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-sessions/:id} : get the "id" courseSession.
     *
     * @param id the id of the courseSession to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseSession, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-sessions/{id}")
    public ResponseEntity<CourseSession> getCourseSession(@PathVariable Long id) {
        log.debug("REST request to get CourseSession : {}", id);
        Optional<CourseSession> courseSession = courseSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseSession);
    }

    /**
     * {@code DELETE  /course-sessions/:id} : delete the "id" courseSession.
     *
     * @param id the id of the courseSession to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-sessions/{id}")
    public ResponseEntity<Void> deleteCourseSession(@PathVariable Long id) {
        log.debug("REST request to delete CourseSession : {}", id);
        courseSessionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * CUSTOM
     * */
    @GetMapping("course/{courseId}/course-section/{courseSectionId}/course-sessions")
    public ResponseEntity<List<CourseSession>> getAllCourseSessionByCourse(
        @PathVariable Long courseId,
        @PathVariable Long courseSectionId,
        Pageable pageable
    ) {
        log.debug("REST request to get CourseSession by CourseSection: {}", courseSectionId);
        Page<CourseSession> page = courseSessionService.findCourseSessionByCourseSection(courseId, courseSectionId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("course/{courseId}/course-section/{courseSectionId}/course-sessions/{sessionId}")
    public ResponseEntity<List<CourseSession>> getCourseSessionByCourse(
        @PathVariable Long courseId,
        @PathVariable Long courseSectionId,
        @PathVariable Long sessionId,
        Pageable pageable
    ) {
        log.debug("REST request to get CourseSession by CourseSection: {}", courseSectionId);
        Page<CourseSession> page = courseSessionService.findCourseSessionByCourseSection(courseId, courseSectionId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("course/{courseId}/course-section/{courseSectionId}/course-session")
    public ResponseEntity<CourseSession> createCourseSession(
        @RequestBody CourseSessionDTO courseSessionDTO,
        @PathVariable Long courseId,
        @PathVariable Long courseSectionId
    ) throws URISyntaxException, IOException, VideoException {
        log.debug("REST request to save CourseSession : {}", courseSessionDTO);
        CourseSession result = courseSessionService.save(courseId, courseSectionId, courseSessionDTO);
        return ResponseEntity
            .created(new URI("/api/course-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("course/{courseId}/course-section/{courseSectionId}/course-session/{courseSessionId}/publish")
    public ResponseEntity<CourseSession> publishCourseSession(
        @PathVariable Long courseId,
        @PathVariable Long courseSectionId,
        @PathVariable Long courseSessionId,
        @RequestParam Boolean value
    ) throws URISyntaxException {
        log.debug("REST request to publish CourseSession : {}", courseSessionId);
        CourseSession result = courseSessionService.publish(courseId, courseSectionId, courseSessionId, value);
        return ResponseEntity
            .created(new URI("/api/course-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/test")
    public void test(@RequestBody MultipartFile file) throws Exception {
        courseSessionService.compressAndUpload(file);
    }
}
