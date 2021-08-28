package com.codehat.charusat.web.rest;

import com.codehat.charusat.domain.CourseCategory;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseCategoryRepository;
import com.codehat.charusat.service.CourseCategoryService;
import com.codehat.charusat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.codehat.charusat.domain.CourseCategory}.
 */
@RestController
@RequestMapping("/api")
public class CourseCategoryResource {

    private final Logger log = LoggerFactory.getLogger(CourseCategoryResource.class);

    private static final String ENTITY_NAME = "courseCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseCategoryService courseCategoryService;

    private final CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryResource(CourseCategoryService courseCategoryService, CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryService = courseCategoryService;
        this.courseCategoryRepository = courseCategoryRepository;
    }

    /**
     * {@code POST  /course-categories} : Create a new courseCategory.
     *
     * @param courseCategory the courseCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseCategory, or with status {@code 400 (Bad Request)} if the courseCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-categories")
    public ResponseEntity<CourseCategory> createCourseCategory(@Valid @RequestBody CourseCategory courseCategory)
        throws URISyntaxException {
        log.debug("REST request to save CourseCategory : {}", courseCategory);
        if (courseCategory.getId() != null) {
            throw new BadRequestAlertException("A new courseCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseCategory result = courseCategoryService.save(courseCategory);
        return ResponseEntity
            .created(new URI("/api/course-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-categories/:id} : Updates an existing courseCategory.
     *
     * @param id the id of the courseCategory to save.
     * @param courseCategory the courseCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseCategory,
     * or with status {@code 400 (Bad Request)} if the courseCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-categories/{id}")
    public ResponseEntity<CourseCategory> updateCourseCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseCategory courseCategory
    ) throws URISyntaxException {
        log.debug("REST request to update CourseCategory : {}, {}", id, courseCategory);
        if (courseCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseCategory result = courseCategoryService.save(courseCategory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-categories/:id} : Partial updates given fields of an existing courseCategory, field will ignore if it is null
     *
     * @param id the id of the courseCategory to save.
     * @param courseCategory the courseCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseCategory,
     * or with status {@code 400 (Bad Request)} if the courseCategory is not valid,
     * or with status {@code 404 (Not Found)} if the courseCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-categories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CourseCategory> partialUpdateCourseCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseCategory courseCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseCategory partially : {}, {}", id, courseCategory);
        if (courseCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseCategory> result = courseCategoryService.partialUpdate(courseCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /course-categories} : get all the courseCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseCategories in body.
     */
    @GetMapping("/course-categories")
    public ResponseEntity<List<CourseCategory>> getAllCourseCategories(Pageable pageable) {
        log.debug("REST request to get a page of CourseCategories");
        Page<CourseCategory> page = courseCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/course-category-list")
    public ResponseEntity<List<CourseCategory>> getAllCourseCategories() {
        log.debug("REST request to get a list of CourseCategories");
        return ResponseEntity.ok().body(courseCategoryService.findAll());
    }

    /**
     * {@code GET  /course-categories/:id} : get the "id" courseCategory.
     *
     * @param id the id of the courseCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-categories/{id}")
    public ResponseEntity<CourseCategory> getCourseCategory(@PathVariable Long id) {
        log.debug("REST request to get CourseCategory : {}", id);
        Optional<CourseCategory> courseCategory = courseCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseCategory);
    }

    @GetMapping("/course-category/parent-categories")
    public ResponseEntity<List<CourseCategory>> getParentCourseCategories() {
        log.debug("REST request ot get course category by isParent");
        List<CourseCategory> list = courseCategoryService.listParentCategory();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/course-category/sub-categories/{id}")
    public ResponseEntity<List<CourseCategory>> getSubCourseCategories(@PathVariable Long id) {
        log.debug("REST request ot get course category by parentId");
        return ResponseEntity.ok().body(courseCategoryService.listByParentId(id));
    }

    /**
     * {@code DELETE  /course-categories/:id} : delete the "id" courseCategory.
     *
     * @param id the id of the courseCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-categories/{id}")
    public ResponseEntity<Void> deleteCourseCategory(@PathVariable Long id) {
        log.debug("REST request to delete CourseCategory : {}", id);
        courseCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/course-category/{parentId}/sub-category/get-course-count")
    public ResponseEntity<Map<Long, Integer>> getCourseCountBySubCategory(@PathVariable Long parentId) {
        return courseCategoryService.getCourseCountBySubCategory(parentId);
    }

    @GetMapping("/course-category/get-course-count")
    public ResponseEntity<Map<Long, Integer>> getCourseCountByParentCategory() {
        return courseCategoryService.getCourseCountByParentCategory();
    }

    @GetMapping("/course-category/sub-categories")
    public ResponseEntity<List<CourseCategory>> getSubCourseCategories() {
        return courseCategoryService.getCourseSubCategories();
    }

    @GetMapping("/course-category/{courseCategoryId}/get-reviewers")
    public ResponseEntity<Set<User>> getReviewerByCategory(@PathVariable Long courseCategoryId) throws Exception {
        return courseCategoryService.getReviewerByCourseCategoryId(courseCategoryId);
    }

    @PostMapping("/course-category/{courseCategoryId}/set-reviewers")
    public ResponseEntity setReviewersByCategory(@PathVariable Long courseCategoryId, @RequestBody Set<User> reviewers) throws Exception {
        return courseCategoryService.setReviewerInSubCategories(courseCategoryId, reviewers);
    }
}
