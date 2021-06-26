package com.codehat.charusat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehat.charusat.IntegrationTest;
import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.repository.CourseProgressRepository;
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
 * Integration tests for the {@link CourseProgressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseProgressResourceIT {

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final Instant DEFAULT_WATCH_SECONDS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_WATCH_SECONDS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/course-progresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseProgressRepository courseProgressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseProgressMockMvc;

    private CourseProgress courseProgress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseProgress createEntity(EntityManager em) {
        CourseProgress courseProgress = new CourseProgress().completed(DEFAULT_COMPLETED).watchSeconds(DEFAULT_WATCH_SECONDS);
        return courseProgress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseProgress createUpdatedEntity(EntityManager em) {
        CourseProgress courseProgress = new CourseProgress().completed(UPDATED_COMPLETED).watchSeconds(UPDATED_WATCH_SECONDS);
        return courseProgress;
    }

    @BeforeEach
    public void initTest() {
        courseProgress = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseProgress() throws Exception {
        int databaseSizeBeforeCreate = courseProgressRepository.findAll().size();
        // Create the CourseProgress
        restCourseProgressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isCreated());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeCreate + 1);
        CourseProgress testCourseProgress = courseProgressList.get(courseProgressList.size() - 1);
        assertThat(testCourseProgress.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testCourseProgress.getWatchSeconds()).isEqualTo(DEFAULT_WATCH_SECONDS);
    }

    @Test
    @Transactional
    void createCourseProgressWithExistingId() throws Exception {
        // Create the CourseProgress with an existing ID
        courseProgress.setId(1L);

        int databaseSizeBeforeCreate = courseProgressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseProgressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseProgressRepository.findAll().size();
        // set the field null
        courseProgress.setCompleted(null);

        // Create the CourseProgress, which fails.

        restCourseProgressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isBadRequest());

        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWatchSecondsIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseProgressRepository.findAll().size();
        // set the field null
        courseProgress.setWatchSeconds(null);

        // Create the CourseProgress, which fails.

        restCourseProgressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isBadRequest());

        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseProgresses() throws Exception {
        // Initialize the database
        courseProgressRepository.saveAndFlush(courseProgress);

        // Get all the courseProgressList
        restCourseProgressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseProgress.getId().intValue())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())))
            .andExpect(jsonPath("$.[*].watchSeconds").value(hasItem(DEFAULT_WATCH_SECONDS.toString())));
    }

    @Test
    @Transactional
    void getCourseProgress() throws Exception {
        // Initialize the database
        courseProgressRepository.saveAndFlush(courseProgress);

        // Get the courseProgress
        restCourseProgressMockMvc
            .perform(get(ENTITY_API_URL_ID, courseProgress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseProgress.getId().intValue()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()))
            .andExpect(jsonPath("$.watchSeconds").value(DEFAULT_WATCH_SECONDS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCourseProgress() throws Exception {
        // Get the courseProgress
        restCourseProgressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseProgress() throws Exception {
        // Initialize the database
        courseProgressRepository.saveAndFlush(courseProgress);

        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();

        // Update the courseProgress
        CourseProgress updatedCourseProgress = courseProgressRepository.findById(courseProgress.getId()).get();
        // Disconnect from session so that the updates on updatedCourseProgress are not directly saved in db
        em.detach(updatedCourseProgress);
        updatedCourseProgress.completed(UPDATED_COMPLETED).watchSeconds(UPDATED_WATCH_SECONDS);

        restCourseProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseProgress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseProgress))
            )
            .andExpect(status().isOk());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
        CourseProgress testCourseProgress = courseProgressList.get(courseProgressList.size() - 1);
        assertThat(testCourseProgress.getCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testCourseProgress.getWatchSeconds()).isEqualTo(UPDATED_WATCH_SECONDS);
    }

    @Test
    @Transactional
    void putNonExistingCourseProgress() throws Exception {
        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();
        courseProgress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseProgress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseProgress() throws Exception {
        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();
        courseProgress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseProgress() throws Exception {
        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();
        courseProgress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseProgressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseProgress)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseProgressWithPatch() throws Exception {
        // Initialize the database
        courseProgressRepository.saveAndFlush(courseProgress);

        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();

        // Update the courseProgress using partial update
        CourseProgress partialUpdatedCourseProgress = new CourseProgress();
        partialUpdatedCourseProgress.setId(courseProgress.getId());

        partialUpdatedCourseProgress.watchSeconds(UPDATED_WATCH_SECONDS);

        restCourseProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseProgress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseProgress))
            )
            .andExpect(status().isOk());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
        CourseProgress testCourseProgress = courseProgressList.get(courseProgressList.size() - 1);
        assertThat(testCourseProgress.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
        assertThat(testCourseProgress.getWatchSeconds()).isEqualTo(UPDATED_WATCH_SECONDS);
    }

    @Test
    @Transactional
    void fullUpdateCourseProgressWithPatch() throws Exception {
        // Initialize the database
        courseProgressRepository.saveAndFlush(courseProgress);

        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();

        // Update the courseProgress using partial update
        CourseProgress partialUpdatedCourseProgress = new CourseProgress();
        partialUpdatedCourseProgress.setId(courseProgress.getId());

        partialUpdatedCourseProgress.completed(UPDATED_COMPLETED).watchSeconds(UPDATED_WATCH_SECONDS);

        restCourseProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseProgress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseProgress))
            )
            .andExpect(status().isOk());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
        CourseProgress testCourseProgress = courseProgressList.get(courseProgressList.size() - 1);
        assertThat(testCourseProgress.getCompleted()).isEqualTo(UPDATED_COMPLETED);
        assertThat(testCourseProgress.getWatchSeconds()).isEqualTo(UPDATED_WATCH_SECONDS);
    }

    @Test
    @Transactional
    void patchNonExistingCourseProgress() throws Exception {
        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();
        courseProgress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseProgress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseProgress() throws Exception {
        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();
        courseProgress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseProgress() throws Exception {
        int databaseSizeBeforeUpdate = courseProgressRepository.findAll().size();
        courseProgress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseProgressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseProgress))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseProgress in the database
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseProgress() throws Exception {
        // Initialize the database
        courseProgressRepository.saveAndFlush(courseProgress);

        int databaseSizeBeforeDelete = courseProgressRepository.findAll().size();

        // Delete the courseProgress
        restCourseProgressMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseProgress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseProgress> courseProgressList = courseProgressRepository.findAll();
        assertThat(courseProgressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
