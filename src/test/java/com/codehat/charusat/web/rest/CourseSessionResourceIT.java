package com.codehat.charusat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehat.charusat.IntegrationTest;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.repository.CourseSessionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CourseSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseSessionResourceIT {

    private static final String DEFAULT_SESSION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SESSION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SESSION_VIDEO = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_VIDEO = "BBBBBBBBBB";

    private static final Instant DEFAULT_SESSION_DURATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SESSION_DURATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_SESSION_ORDER = 1;
    private static final Integer UPDATED_SESSION_ORDER = 2;

    private static final String DEFAULT_SESSION_RESOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_RESOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_SESSION_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_LOCATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PREVIEW = false;
    private static final Boolean UPDATED_IS_PREVIEW = true;

    private static final Boolean DEFAULT_IS_DRAFT = false;
    private static final Boolean UPDATED_IS_DRAFT = true;

    private static final Boolean DEFAULT_IS_APPROVED = false;
    private static final Boolean UPDATED_IS_APPROVED = true;

    private static final Boolean DEFAULT_IS_PUBLISHED = false;
    private static final Boolean UPDATED_IS_PUBLISHED = true;

    private static final String ENTITY_API_URL = "/api/course-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseSessionRepository courseSessionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseSessionMockMvc;

    private CourseSession courseSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseSession createEntity(EntityManager em) {
        CourseSession courseSession = new CourseSession()
            .sessionTitle(DEFAULT_SESSION_TITLE)
            .sessionDescription(DEFAULT_SESSION_DESCRIPTION)
            .sessionVideo(DEFAULT_SESSION_VIDEO)
            .sessionDuration(DEFAULT_SESSION_DURATION)
            .sessionOrder(DEFAULT_SESSION_ORDER)
            .sessionResource(DEFAULT_SESSION_RESOURCE)
            .sessionLocation(DEFAULT_SESSION_LOCATION)
            .isPreview(DEFAULT_IS_PREVIEW)
            .isDraft(DEFAULT_IS_DRAFT)
            .isApproved(DEFAULT_IS_APPROVED)
            .isPublished(DEFAULT_IS_PUBLISHED);
        return courseSession;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseSession createUpdatedEntity(EntityManager em) {
        CourseSession courseSession = new CourseSession()
            .sessionTitle(UPDATED_SESSION_TITLE)
            .sessionDescription(UPDATED_SESSION_DESCRIPTION)
            .sessionVideo(UPDATED_SESSION_VIDEO)
            .sessionDuration(UPDATED_SESSION_DURATION)
            .sessionOrder(UPDATED_SESSION_ORDER)
            .sessionResource(UPDATED_SESSION_RESOURCE)
            .sessionLocation(UPDATED_SESSION_LOCATION)
            .isPreview(UPDATED_IS_PREVIEW)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED)
            .isPublished(UPDATED_IS_PUBLISHED);
        return courseSession;
    }

    @BeforeEach
    public void initTest() {
        courseSession = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseSession() throws Exception {
        int databaseSizeBeforeCreate = courseSessionRepository.findAll().size();
        // Create the CourseSession
        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isCreated());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeCreate + 1);
        CourseSession testCourseSession = courseSessionList.get(courseSessionList.size() - 1);
        assertThat(testCourseSession.getSessionTitle()).isEqualTo(DEFAULT_SESSION_TITLE);
        assertThat(testCourseSession.getSessionDescription()).isEqualTo(DEFAULT_SESSION_DESCRIPTION);
        assertThat(testCourseSession.getSessionVideo()).isEqualTo(DEFAULT_SESSION_VIDEO);
        assertThat(testCourseSession.getSessionDuration()).isEqualTo(DEFAULT_SESSION_DURATION);
        assertThat(testCourseSession.getSessionOrder()).isEqualTo(DEFAULT_SESSION_ORDER);
        assertThat(testCourseSession.getSessionResource()).isEqualTo(DEFAULT_SESSION_RESOURCE);
        assertThat(testCourseSession.getSessionLocation()).isEqualTo(DEFAULT_SESSION_LOCATION);
        assertThat(testCourseSession.getIsPreview()).isEqualTo(DEFAULT_IS_PREVIEW);
        assertThat(testCourseSession.getIsDraft()).isEqualTo(DEFAULT_IS_DRAFT);
        assertThat(testCourseSession.getIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
        assertThat(testCourseSession.getIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void createCourseSessionWithExistingId() throws Exception {
        // Create the CourseSession with an existing ID
        courseSession.setId(1L);

        int databaseSizeBeforeCreate = courseSessionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSessionTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setSessionTitle(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSessionVideoIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setSessionVideo(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSessionDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setSessionDuration(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSessionOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setSessionOrder(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSessionLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setSessionLocation(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPreviewIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setIsPreview(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDraftIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setIsDraft(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsApprovedIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setIsApproved(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPublishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSessionRepository.findAll().size();
        // set the field null
        courseSession.setIsPublished(null);

        // Create the CourseSession, which fails.

        restCourseSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isBadRequest());

        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseSessions() throws Exception {
        // Initialize the database
        courseSessionRepository.saveAndFlush(courseSession);

        // Get all the courseSessionList
        restCourseSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionTitle").value(hasItem(DEFAULT_SESSION_TITLE)))
            .andExpect(jsonPath("$.[*].sessionDescription").value(hasItem(DEFAULT_SESSION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sessionVideo").value(hasItem(DEFAULT_SESSION_VIDEO)))
            .andExpect(jsonPath("$.[*].sessionDuration").value(hasItem(DEFAULT_SESSION_DURATION.toString())))
            .andExpect(jsonPath("$.[*].sessionOrder").value(hasItem(DEFAULT_SESSION_ORDER)))
            .andExpect(jsonPath("$.[*].sessionResource").value(hasItem(DEFAULT_SESSION_RESOURCE)))
            .andExpect(jsonPath("$.[*].sessionLocation").value(hasItem(DEFAULT_SESSION_LOCATION)))
            .andExpect(jsonPath("$.[*].isPreview").value(hasItem(DEFAULT_IS_PREVIEW.booleanValue())))
            .andExpect(jsonPath("$.[*].isDraft").value(hasItem(DEFAULT_IS_DRAFT.booleanValue())))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED.booleanValue())))
            .andExpect(jsonPath("$.[*].isPublished").value(hasItem(DEFAULT_IS_PUBLISHED.booleanValue())));
    }

    @Test
    @Transactional
    void getCourseSession() throws Exception {
        // Initialize the database
        courseSessionRepository.saveAndFlush(courseSession);

        // Get the courseSession
        restCourseSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, courseSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseSession.getId().intValue()))
            .andExpect(jsonPath("$.sessionTitle").value(DEFAULT_SESSION_TITLE))
            .andExpect(jsonPath("$.sessionDescription").value(DEFAULT_SESSION_DESCRIPTION))
            .andExpect(jsonPath("$.sessionVideo").value(DEFAULT_SESSION_VIDEO))
            .andExpect(jsonPath("$.sessionDuration").value(DEFAULT_SESSION_DURATION.toString()))
            .andExpect(jsonPath("$.sessionOrder").value(DEFAULT_SESSION_ORDER))
            .andExpect(jsonPath("$.sessionResource").value(DEFAULT_SESSION_RESOURCE))
            .andExpect(jsonPath("$.sessionLocation").value(DEFAULT_SESSION_LOCATION))
            .andExpect(jsonPath("$.isPreview").value(DEFAULT_IS_PREVIEW.booleanValue()))
            .andExpect(jsonPath("$.isDraft").value(DEFAULT_IS_DRAFT.booleanValue()))
            .andExpect(jsonPath("$.isApproved").value(DEFAULT_IS_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.isPublished").value(DEFAULT_IS_PUBLISHED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCourseSession() throws Exception {
        // Get the courseSession
        restCourseSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseSession() throws Exception {
        // Initialize the database
        courseSessionRepository.saveAndFlush(courseSession);

        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();

        // Update the courseSession
        CourseSession updatedCourseSession = courseSessionRepository.findById(courseSession.getId()).get();
        // Disconnect from session so that the updates on updatedCourseSession are not directly saved in db
        em.detach(updatedCourseSession);
        updatedCourseSession
            .sessionTitle(UPDATED_SESSION_TITLE)
            .sessionDescription(UPDATED_SESSION_DESCRIPTION)
            .sessionVideo(UPDATED_SESSION_VIDEO)
            .sessionDuration(UPDATED_SESSION_DURATION)
            .sessionOrder(UPDATED_SESSION_ORDER)
            .sessionResource(UPDATED_SESSION_RESOURCE)
            .sessionLocation(UPDATED_SESSION_LOCATION)
            .isPreview(UPDATED_IS_PREVIEW)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED)
            .isPublished(UPDATED_IS_PUBLISHED);

        restCourseSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseSession))
            )
            .andExpect(status().isOk());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
        CourseSession testCourseSession = courseSessionList.get(courseSessionList.size() - 1);
        assertThat(testCourseSession.getSessionTitle()).isEqualTo(UPDATED_SESSION_TITLE);
        assertThat(testCourseSession.getSessionDescription()).isEqualTo(UPDATED_SESSION_DESCRIPTION);
        assertThat(testCourseSession.getSessionVideo()).isEqualTo(UPDATED_SESSION_VIDEO);
        assertThat(testCourseSession.getSessionDuration()).isEqualTo(UPDATED_SESSION_DURATION);
        assertThat(testCourseSession.getSessionOrder()).isEqualTo(UPDATED_SESSION_ORDER);
        assertThat(testCourseSession.getSessionResource()).isEqualTo(UPDATED_SESSION_RESOURCE);
        assertThat(testCourseSession.getSessionLocation()).isEqualTo(UPDATED_SESSION_LOCATION);
        assertThat(testCourseSession.getIsPreview()).isEqualTo(UPDATED_IS_PREVIEW);
        assertThat(testCourseSession.getIsDraft()).isEqualTo(UPDATED_IS_DRAFT);
        assertThat(testCourseSession.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testCourseSession.getIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void putNonExistingCourseSession() throws Exception {
        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();
        courseSession.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseSession() throws Exception {
        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();
        courseSession.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseSession() throws Exception {
        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();
        courseSession.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseSessionWithPatch() throws Exception {
        // Initialize the database
        courseSessionRepository.saveAndFlush(courseSession);

        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();

        // Update the courseSession using partial update
        CourseSession partialUpdatedCourseSession = new CourseSession();
        partialUpdatedCourseSession.setId(courseSession.getId());

        partialUpdatedCourseSession
            .sessionDescription(UPDATED_SESSION_DESCRIPTION)
            .sessionResource(UPDATED_SESSION_RESOURCE)
            .sessionLocation(UPDATED_SESSION_LOCATION)
            .isPreview(UPDATED_IS_PREVIEW)
            .isDraft(UPDATED_IS_DRAFT);

        restCourseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseSession))
            )
            .andExpect(status().isOk());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
        CourseSession testCourseSession = courseSessionList.get(courseSessionList.size() - 1);
        assertThat(testCourseSession.getSessionTitle()).isEqualTo(DEFAULT_SESSION_TITLE);
        assertThat(testCourseSession.getSessionDescription()).isEqualTo(UPDATED_SESSION_DESCRIPTION);
        assertThat(testCourseSession.getSessionVideo()).isEqualTo(DEFAULT_SESSION_VIDEO);
        assertThat(testCourseSession.getSessionDuration()).isEqualTo(DEFAULT_SESSION_DURATION);
        assertThat(testCourseSession.getSessionOrder()).isEqualTo(DEFAULT_SESSION_ORDER);
        assertThat(testCourseSession.getSessionResource()).isEqualTo(UPDATED_SESSION_RESOURCE);
        assertThat(testCourseSession.getSessionLocation()).isEqualTo(UPDATED_SESSION_LOCATION);
        assertThat(testCourseSession.getIsPreview()).isEqualTo(UPDATED_IS_PREVIEW);
        assertThat(testCourseSession.getIsDraft()).isEqualTo(UPDATED_IS_DRAFT);
        assertThat(testCourseSession.getIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
        assertThat(testCourseSession.getIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void fullUpdateCourseSessionWithPatch() throws Exception {
        // Initialize the database
        courseSessionRepository.saveAndFlush(courseSession);

        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();

        // Update the courseSession using partial update
        CourseSession partialUpdatedCourseSession = new CourseSession();
        partialUpdatedCourseSession.setId(courseSession.getId());

        partialUpdatedCourseSession
            .sessionTitle(UPDATED_SESSION_TITLE)
            .sessionDescription(UPDATED_SESSION_DESCRIPTION)
            .sessionVideo(UPDATED_SESSION_VIDEO)
            .sessionDuration(UPDATED_SESSION_DURATION)
            .sessionOrder(UPDATED_SESSION_ORDER)
            .sessionResource(UPDATED_SESSION_RESOURCE)
            .sessionLocation(UPDATED_SESSION_LOCATION)
            .isPreview(UPDATED_IS_PREVIEW)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED)
            .isPublished(UPDATED_IS_PUBLISHED);

        restCourseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseSession))
            )
            .andExpect(status().isOk());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
        CourseSession testCourseSession = courseSessionList.get(courseSessionList.size() - 1);
        assertThat(testCourseSession.getSessionTitle()).isEqualTo(UPDATED_SESSION_TITLE);
        assertThat(testCourseSession.getSessionDescription()).isEqualTo(UPDATED_SESSION_DESCRIPTION);
        assertThat(testCourseSession.getSessionVideo()).isEqualTo(UPDATED_SESSION_VIDEO);
        assertThat(testCourseSession.getSessionDuration()).isEqualTo(UPDATED_SESSION_DURATION);
        assertThat(testCourseSession.getSessionOrder()).isEqualTo(UPDATED_SESSION_ORDER);
        assertThat(testCourseSession.getSessionResource()).isEqualTo(UPDATED_SESSION_RESOURCE);
        assertThat(testCourseSession.getSessionLocation()).isEqualTo(UPDATED_SESSION_LOCATION);
        assertThat(testCourseSession.getIsPreview()).isEqualTo(UPDATED_IS_PREVIEW);
        assertThat(testCourseSession.getIsDraft()).isEqualTo(UPDATED_IS_DRAFT);
        assertThat(testCourseSession.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
        assertThat(testCourseSession.getIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
    }

    @Test
    @Transactional
    void patchNonExistingCourseSession() throws Exception {
        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();
        courseSession.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseSession() throws Exception {
        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();
        courseSession.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseSession() throws Exception {
        int databaseSizeBeforeUpdate = courseSessionRepository.findAll().size();
        courseSession.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSessionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseSession))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseSession in the database
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseSession() throws Exception {
        // Initialize the database
        courseSessionRepository.saveAndFlush(courseSession);

        int databaseSizeBeforeDelete = courseSessionRepository.findAll().size();

        // Delete the courseSession
        restCourseSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseSession> courseSessionList = courseSessionRepository.findAll();
        assertThat(courseSessionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
