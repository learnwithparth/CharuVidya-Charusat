package com.codehat.charusat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehat.charusat.IntegrationTest;
import com.codehat.charusat.domain.Course;
import com.codehat.charusat.repository.CourseRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CourseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseResourceIT {

    private static final String DEFAULT_COURSE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_OBJECTIVES = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_OBJECTIVES = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_SUB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_SUB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIEW_VIDEOURL = "AAAAAAAAAA";
    private static final String UPDATED_PREVIEW_VIDEOURL = "BBBBBBBBBB";

    private static final Integer DEFAULT_COURSE_LENGTH = 1;
    private static final Integer UPDATED_COURSE_LENGTH = 2;

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_COURSE_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COURSE_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_COURSE_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COURSE_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COURSE_ROOT_DIR = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ROOT_DIR = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Boolean DEFAULT_IS_DRAFT = false;
    private static final Boolean UPDATED_IS_DRAFT = true;

    private static final Boolean DEFAULT_IS_APPROVED = false;
    private static final Boolean UPDATED_IS_APPROVED = true;

    private static final LocalDate DEFAULT_COURSE_APPROVAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COURSE_APPROVAL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .courseTitle(DEFAULT_COURSE_TITLE)
            .courseDescription(DEFAULT_COURSE_DESCRIPTION)
            .courseObjectives(DEFAULT_COURSE_OBJECTIVES)
            .courseSubTitle(DEFAULT_COURSE_SUB_TITLE)
            .previewVideourl(DEFAULT_PREVIEW_VIDEOURL)
            .courseLength(DEFAULT_COURSE_LENGTH)
            .logo(DEFAULT_LOGO)
            .courseCreatedOn(DEFAULT_COURSE_CREATED_ON)
            .courseUpdatedOn(DEFAULT_COURSE_UPDATED_ON)
            .courseRootDir(DEFAULT_COURSE_ROOT_DIR)
            .amount(DEFAULT_AMOUNT)
            .isDraft(DEFAULT_IS_DRAFT)
            .isApproved(DEFAULT_IS_APPROVED)
            .courseApprovalDate(DEFAULT_COURSE_APPROVAL_DATE);
        return course;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .courseTitle(UPDATED_COURSE_TITLE)
            .courseDescription(UPDATED_COURSE_DESCRIPTION)
            .courseObjectives(UPDATED_COURSE_OBJECTIVES)
            .courseSubTitle(UPDATED_COURSE_SUB_TITLE)
            .previewVideourl(UPDATED_PREVIEW_VIDEOURL)
            .courseLength(UPDATED_COURSE_LENGTH)
            .logo(UPDATED_LOGO)
            .courseCreatedOn(UPDATED_COURSE_CREATED_ON)
            .courseUpdatedOn(UPDATED_COURSE_UPDATED_ON)
            .courseRootDir(UPDATED_COURSE_ROOT_DIR)
            .amount(UPDATED_AMOUNT)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED)
            .courseApprovalDate(UPDATED_COURSE_APPROVAL_DATE);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();
        // Create the Course
        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCourseTitle()).isEqualTo(DEFAULT_COURSE_TITLE);
        assertThat(testCourse.getCourseDescription()).isEqualTo(DEFAULT_COURSE_DESCRIPTION);
        assertThat(testCourse.getCourseObjectives()).isEqualTo(DEFAULT_COURSE_OBJECTIVES);
        assertThat(testCourse.getCourseSubTitle()).isEqualTo(DEFAULT_COURSE_SUB_TITLE);
        assertThat(testCourse.getPreviewVideourl()).isEqualTo(DEFAULT_PREVIEW_VIDEOURL);
        assertThat(testCourse.getCourseLength()).isEqualTo(DEFAULT_COURSE_LENGTH);
        assertThat(testCourse.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCourse.getCourseCreatedOn()).isEqualTo(DEFAULT_COURSE_CREATED_ON);
        assertThat(testCourse.getCourseUpdatedOn()).isEqualTo(DEFAULT_COURSE_UPDATED_ON);
        assertThat(testCourse.getCourseRootDir()).isEqualTo(DEFAULT_COURSE_ROOT_DIR);
        assertThat(testCourse.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testCourse.getIsDraft()).isEqualTo(DEFAULT_IS_DRAFT);
        assertThat(testCourse.getIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
        assertThat(testCourse.getCourseApprovalDate()).isEqualTo(DEFAULT_COURSE_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void createCourseWithExistingId() throws Exception {
        // Create the Course with an existing ID
        course.setId(1L);

        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCourseTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCourseTitle(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCourseDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCourseDescription(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCourseSubTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCourseSubTitle(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPreviewVideourlIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setPreviewVideourl(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLogoIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setLogo(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCourseCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCourseCreatedOn(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCourseUpdatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCourseUpdatedOn(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDraftIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setIsDraft(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsApprovedIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setIsApproved(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseTitle").value(hasItem(DEFAULT_COURSE_TITLE)))
            .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(DEFAULT_COURSE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].courseObjectives").value(hasItem(DEFAULT_COURSE_OBJECTIVES)))
            .andExpect(jsonPath("$.[*].courseSubTitle").value(hasItem(DEFAULT_COURSE_SUB_TITLE)))
            .andExpect(jsonPath("$.[*].previewVideourl").value(hasItem(DEFAULT_PREVIEW_VIDEOURL)))
            .andExpect(jsonPath("$.[*].courseLength").value(hasItem(DEFAULT_COURSE_LENGTH)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].courseCreatedOn").value(hasItem(DEFAULT_COURSE_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].courseUpdatedOn").value(hasItem(DEFAULT_COURSE_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].courseRootDir").value(hasItem(DEFAULT_COURSE_ROOT_DIR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].isDraft").value(hasItem(DEFAULT_IS_DRAFT.booleanValue())))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].courseApprovalDate").value(hasItem(DEFAULT_COURSE_APPROVAL_DATE.toString())));
    }

    @Test
    @Transactional
    void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc
            .perform(get(ENTITY_API_URL_ID, course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.courseTitle").value(DEFAULT_COURSE_TITLE))
            .andExpect(jsonPath("$.courseDescription").value(DEFAULT_COURSE_DESCRIPTION))
            .andExpect(jsonPath("$.courseObjectives").value(DEFAULT_COURSE_OBJECTIVES))
            .andExpect(jsonPath("$.courseSubTitle").value(DEFAULT_COURSE_SUB_TITLE))
            .andExpect(jsonPath("$.previewVideourl").value(DEFAULT_PREVIEW_VIDEOURL))
            .andExpect(jsonPath("$.courseLength").value(DEFAULT_COURSE_LENGTH))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.courseCreatedOn").value(DEFAULT_COURSE_CREATED_ON.toString()))
            .andExpect(jsonPath("$.courseUpdatedOn").value(DEFAULT_COURSE_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.courseRootDir").value(DEFAULT_COURSE_ROOT_DIR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.isDraft").value(DEFAULT_IS_DRAFT.booleanValue()))
            .andExpect(jsonPath("$.isApproved").value(DEFAULT_IS_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.courseApprovalDate").value(DEFAULT_COURSE_APPROVAL_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .courseTitle(UPDATED_COURSE_TITLE)
            .courseDescription(UPDATED_COURSE_DESCRIPTION)
            .courseObjectives(UPDATED_COURSE_OBJECTIVES)
            .courseSubTitle(UPDATED_COURSE_SUB_TITLE)
            .previewVideourl(UPDATED_PREVIEW_VIDEOURL)
            .courseLength(UPDATED_COURSE_LENGTH)
            .logo(UPDATED_LOGO)
            .courseCreatedOn(UPDATED_COURSE_CREATED_ON)
            .courseUpdatedOn(UPDATED_COURSE_UPDATED_ON)
            .courseRootDir(UPDATED_COURSE_ROOT_DIR)
            .amount(UPDATED_AMOUNT)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED)
            .courseApprovalDate(UPDATED_COURSE_APPROVAL_DATE);

        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCourseTitle()).isEqualTo(UPDATED_COURSE_TITLE);
        assertThat(testCourse.getCourseDescription()).isEqualTo(UPDATED_COURSE_DESCRIPTION);
        assertThat(testCourse.getCourseObjectives()).isEqualTo(UPDATED_COURSE_OBJECTIVES);
        assertThat(testCourse.getCourseSubTitle()).isEqualTo(UPDATED_COURSE_SUB_TITLE);
        assertThat(testCourse.getPreviewVideourl()).isEqualTo(UPDATED_PREVIEW_VIDEOURL);
        assertThat(testCourse.getCourseLength()).isEqualTo(UPDATED_COURSE_LENGTH);
        assertThat(testCourse.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCourse.getCourseCreatedOn()).isEqualTo(UPDATED_COURSE_CREATED_ON);
        assertThat(testCourse.getCourseUpdatedOn()).isEqualTo(UPDATED_COURSE_UPDATED_ON);
        assertThat(testCourse.getCourseRootDir()).isEqualTo(UPDATED_COURSE_ROOT_DIR);
        assertThat(testCourse.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCourse.getIsDraft()).isEqualTo(UPDATED_IS_DRAFT);
        assertThat(testCourse.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testCourse.getCourseApprovalDate()).isEqualTo(UPDATED_COURSE_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, course.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse
            .courseTitle(UPDATED_COURSE_TITLE)
            .courseDescription(UPDATED_COURSE_DESCRIPTION)
            .courseObjectives(UPDATED_COURSE_OBJECTIVES)
            .courseSubTitle(UPDATED_COURSE_SUB_TITLE)
            .previewVideourl(UPDATED_PREVIEW_VIDEOURL)
            .courseLength(UPDATED_COURSE_LENGTH)
            .amount(UPDATED_AMOUNT)
            .isApproved(UPDATED_IS_APPROVED);

        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCourseTitle()).isEqualTo(UPDATED_COURSE_TITLE);
        assertThat(testCourse.getCourseDescription()).isEqualTo(UPDATED_COURSE_DESCRIPTION);
        assertThat(testCourse.getCourseObjectives()).isEqualTo(UPDATED_COURSE_OBJECTIVES);
        assertThat(testCourse.getCourseSubTitle()).isEqualTo(UPDATED_COURSE_SUB_TITLE);
        assertThat(testCourse.getPreviewVideourl()).isEqualTo(UPDATED_PREVIEW_VIDEOURL);
        assertThat(testCourse.getCourseLength()).isEqualTo(UPDATED_COURSE_LENGTH);
        assertThat(testCourse.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCourse.getCourseCreatedOn()).isEqualTo(DEFAULT_COURSE_CREATED_ON);
        assertThat(testCourse.getCourseUpdatedOn()).isEqualTo(DEFAULT_COURSE_UPDATED_ON);
        assertThat(testCourse.getCourseRootDir()).isEqualTo(DEFAULT_COURSE_ROOT_DIR);
        assertThat(testCourse.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCourse.getIsDraft()).isEqualTo(DEFAULT_IS_DRAFT);
        assertThat(testCourse.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testCourse.getCourseApprovalDate()).isEqualTo(DEFAULT_COURSE_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse
            .courseTitle(UPDATED_COURSE_TITLE)
            .courseDescription(UPDATED_COURSE_DESCRIPTION)
            .courseObjectives(UPDATED_COURSE_OBJECTIVES)
            .courseSubTitle(UPDATED_COURSE_SUB_TITLE)
            .previewVideourl(UPDATED_PREVIEW_VIDEOURL)
            .courseLength(UPDATED_COURSE_LENGTH)
            .logo(UPDATED_LOGO)
            .courseCreatedOn(UPDATED_COURSE_CREATED_ON)
            .courseUpdatedOn(UPDATED_COURSE_UPDATED_ON)
            .courseRootDir(UPDATED_COURSE_ROOT_DIR)
            .amount(UPDATED_AMOUNT)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED)
            .courseApprovalDate(UPDATED_COURSE_APPROVAL_DATE);

        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCourseTitle()).isEqualTo(UPDATED_COURSE_TITLE);
        assertThat(testCourse.getCourseDescription()).isEqualTo(UPDATED_COURSE_DESCRIPTION);
        assertThat(testCourse.getCourseObjectives()).isEqualTo(UPDATED_COURSE_OBJECTIVES);
        assertThat(testCourse.getCourseSubTitle()).isEqualTo(UPDATED_COURSE_SUB_TITLE);
        assertThat(testCourse.getPreviewVideourl()).isEqualTo(UPDATED_PREVIEW_VIDEOURL);
        assertThat(testCourse.getCourseLength()).isEqualTo(UPDATED_COURSE_LENGTH);
        assertThat(testCourse.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCourse.getCourseCreatedOn()).isEqualTo(UPDATED_COURSE_CREATED_ON);
        assertThat(testCourse.getCourseUpdatedOn()).isEqualTo(UPDATED_COURSE_UPDATED_ON);
        assertThat(testCourse.getCourseRootDir()).isEqualTo(UPDATED_COURSE_ROOT_DIR);
        assertThat(testCourse.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testCourse.getIsDraft()).isEqualTo(UPDATED_IS_DRAFT);
        assertThat(testCourse.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testCourse.getCourseApprovalDate()).isEqualTo(UPDATED_COURSE_APPROVAL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, course.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc
            .perform(delete(ENTITY_API_URL_ID, course.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
