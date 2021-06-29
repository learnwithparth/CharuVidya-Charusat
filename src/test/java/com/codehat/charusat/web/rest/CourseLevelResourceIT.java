package com.codehat.charusat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehat.charusat.IntegrationTest;
import com.codehat.charusat.domain.CourseLevel;
import com.codehat.charusat.repository.CourseLevelRepository;
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
 * Integration tests for the {@link CourseLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseLevelResourceIT {

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/course-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseLevelRepository courseLevelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseLevelMockMvc;

    private CourseLevel courseLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseLevel createEntity(EntityManager em) {
        CourseLevel courseLevel = new CourseLevel().level(DEFAULT_LEVEL).description(DEFAULT_DESCRIPTION);
        return courseLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseLevel createUpdatedEntity(EntityManager em) {
        CourseLevel courseLevel = new CourseLevel().level(UPDATED_LEVEL).description(UPDATED_DESCRIPTION);
        return courseLevel;
    }

    @BeforeEach
    public void initTest() {
        courseLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseLevel() throws Exception {
        int databaseSizeBeforeCreate = courseLevelRepository.findAll().size();
        // Create the CourseLevel
        restCourseLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseLevel)))
            .andExpect(status().isCreated());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeCreate + 1);
        CourseLevel testCourseLevel = courseLevelList.get(courseLevelList.size() - 1);
        assertThat(testCourseLevel.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCourseLevel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCourseLevelWithExistingId() throws Exception {
        // Create the CourseLevel with an existing ID
        courseLevel.setId(1L);

        int databaseSizeBeforeCreate = courseLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseLevelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseLevel)))
            .andExpect(status().isBadRequest());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCourseLevels() throws Exception {
        // Initialize the database
        courseLevelRepository.saveAndFlush(courseLevel);

        // Get all the courseLevelList
        restCourseLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCourseLevel() throws Exception {
        // Initialize the database
        courseLevelRepository.saveAndFlush(courseLevel);

        // Get the courseLevel
        restCourseLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, courseLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseLevel.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCourseLevel() throws Exception {
        // Get the courseLevel
        restCourseLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseLevel() throws Exception {
        // Initialize the database
        courseLevelRepository.saveAndFlush(courseLevel);

        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();

        // Update the courseLevel
        CourseLevel updatedCourseLevel = courseLevelRepository.findById(courseLevel.getId()).get();
        // Disconnect from session so that the updates on updatedCourseLevel are not directly saved in db
        em.detach(updatedCourseLevel);
        updatedCourseLevel.level(UPDATED_LEVEL).description(UPDATED_DESCRIPTION);

        restCourseLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseLevel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseLevel))
            )
            .andExpect(status().isOk());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
        CourseLevel testCourseLevel = courseLevelList.get(courseLevelList.size() - 1);
        assertThat(testCourseLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testCourseLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCourseLevel() throws Exception {
        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();
        courseLevel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseLevel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseLevel() throws Exception {
        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();
        courseLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseLevel() throws Exception {
        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();
        courseLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseLevelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseLevel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseLevelWithPatch() throws Exception {
        // Initialize the database
        courseLevelRepository.saveAndFlush(courseLevel);

        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();

        // Update the courseLevel using partial update
        CourseLevel partialUpdatedCourseLevel = new CourseLevel();
        partialUpdatedCourseLevel.setId(courseLevel.getId());

        partialUpdatedCourseLevel.description(UPDATED_DESCRIPTION);

        restCourseLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseLevel))
            )
            .andExpect(status().isOk());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
        CourseLevel testCourseLevel = courseLevelList.get(courseLevelList.size() - 1);
        assertThat(testCourseLevel.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCourseLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCourseLevelWithPatch() throws Exception {
        // Initialize the database
        courseLevelRepository.saveAndFlush(courseLevel);

        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();

        // Update the courseLevel using partial update
        CourseLevel partialUpdatedCourseLevel = new CourseLevel();
        partialUpdatedCourseLevel.setId(courseLevel.getId());

        partialUpdatedCourseLevel.level(UPDATED_LEVEL).description(UPDATED_DESCRIPTION);

        restCourseLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseLevel))
            )
            .andExpect(status().isOk());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
        CourseLevel testCourseLevel = courseLevelList.get(courseLevelList.size() - 1);
        assertThat(testCourseLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testCourseLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCourseLevel() throws Exception {
        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();
        courseLevel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseLevel() throws Exception {
        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();
        courseLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseLevel() throws Exception {
        int databaseSizeBeforeUpdate = courseLevelRepository.findAll().size();
        courseLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseLevelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseLevel in the database
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseLevel() throws Exception {
        // Initialize the database
        courseLevelRepository.saveAndFlush(courseLevel);

        int databaseSizeBeforeDelete = courseLevelRepository.findAll().size();

        // Delete the courseLevel
        restCourseLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseLevel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseLevel> courseLevelList = courseLevelRepository.findAll();
        assertThat(courseLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
