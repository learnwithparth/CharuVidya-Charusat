package com.codehat.charusat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehat.charusat.IntegrationTest;
import com.codehat.charusat.domain.CourseEnrollment;
import com.codehat.charusat.repository.CourseEnrollmentRepository;
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
 * Integration tests for the {@link CourseEnrollmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseEnrollmentResourceIT {

    private static final LocalDate DEFAULT_ENROLLEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENROLLEMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_ACCESSED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_ACCESSED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/course-enrollments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseEnrollmentMockMvc;

    private CourseEnrollment courseEnrollment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseEnrollment createEntity(EntityManager em) {
        CourseEnrollment courseEnrollment = new CourseEnrollment()
            .enrollmentDate(DEFAULT_ENROLLEMENT_DATE)
            .lastAccessedDate(DEFAULT_LAST_ACCESSED_DATE);
        return courseEnrollment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseEnrollment createUpdatedEntity(EntityManager em) {
        CourseEnrollment courseEnrollment = new CourseEnrollment()
            .enrollmentDate(UPDATED_ENROLLEMENT_DATE)
            .lastAccessedDate(UPDATED_LAST_ACCESSED_DATE);
        return courseEnrollment;
    }

    @BeforeEach
    public void initTest() {
        courseEnrollment = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseEnrollment() throws Exception {
        int databaseSizeBeforeCreate = courseEnrollmentRepository.findAll().size();
        // Create the CourseEnrollment
        restCourseEnrollmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isCreated());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeCreate + 1);
        CourseEnrollment testCourseEnrollment = courseEnrollmentList.get(courseEnrollmentList.size() - 1);
        assertThat(testCourseEnrollment.getEnrollmentDate()).isEqualTo(DEFAULT_ENROLLEMENT_DATE);
        assertThat(testCourseEnrollment.getLastAccessedDate()).isEqualTo(DEFAULT_LAST_ACCESSED_DATE);
    }

    @Test
    @Transactional
    void createCourseEnrollmentWithExistingId() throws Exception {
        // Create the CourseEnrollment with an existing ID
        courseEnrollment.setId(1L);

        int databaseSizeBeforeCreate = courseEnrollmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseEnrollmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnrollementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseEnrollmentRepository.findAll().size();
        // set the field null
        courseEnrollment.setEnrollmentDate(null);

        // Create the CourseEnrollment, which fails.

        restCourseEnrollmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isBadRequest());

        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastAccessedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseEnrollmentRepository.findAll().size();
        // set the field null
        courseEnrollment.setLastAccessedDate(null);

        // Create the CourseEnrollment, which fails.

        restCourseEnrollmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isBadRequest());

        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseEnrollments() throws Exception {
        // Initialize the database
        courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        // Get all the courseEnrollmentList
        restCourseEnrollmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseEnrollment.getId().intValue())))
            .andExpect(jsonPath("$.[*].enrollementDate").value(hasItem(DEFAULT_ENROLLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastAccessedDate").value(hasItem(DEFAULT_LAST_ACCESSED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCourseEnrollment() throws Exception {
        // Initialize the database
        courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        // Get the courseEnrollment
        restCourseEnrollmentMockMvc
            .perform(get(ENTITY_API_URL_ID, courseEnrollment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseEnrollment.getId().intValue()))
            .andExpect(jsonPath("$.enrollementDate").value(DEFAULT_ENROLLEMENT_DATE.toString()))
            .andExpect(jsonPath("$.lastAccessedDate").value(DEFAULT_LAST_ACCESSED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCourseEnrollment() throws Exception {
        // Get the courseEnrollment
        restCourseEnrollmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseEnrollment() throws Exception {
        // Initialize the database
        courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();

        // Update the courseEnrollment
        CourseEnrollment updatedCourseEnrollment = courseEnrollmentRepository.findById(courseEnrollment.getId()).get();
        // Disconnect from session so that the updates on updatedCourseEnrollment are not directly saved in db
        em.detach(updatedCourseEnrollment);
        updatedCourseEnrollment.enrollmentDate(UPDATED_ENROLLEMENT_DATE).lastAccessedDate(UPDATED_LAST_ACCESSED_DATE);

        restCourseEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseEnrollment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
        CourseEnrollment testCourseEnrollment = courseEnrollmentList.get(courseEnrollmentList.size() - 1);
        assertThat(testCourseEnrollment.getEnrollmentDate()).isEqualTo(UPDATED_ENROLLEMENT_DATE);
        assertThat(testCourseEnrollment.getLastAccessedDate()).isEqualTo(UPDATED_LAST_ACCESSED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCourseEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();
        courseEnrollment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseEnrollment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();
        courseEnrollment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();
        courseEnrollment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseEnrollmentWithPatch() throws Exception {
        // Initialize the database
        courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();

        // Update the courseEnrollment using partial update
        CourseEnrollment partialUpdatedCourseEnrollment = new CourseEnrollment();
        partialUpdatedCourseEnrollment.setId(courseEnrollment.getId());

        partialUpdatedCourseEnrollment.enrollmentDate(UPDATED_ENROLLEMENT_DATE).lastAccessedDate(UPDATED_LAST_ACCESSED_DATE);

        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
        CourseEnrollment testCourseEnrollment = courseEnrollmentList.get(courseEnrollmentList.size() - 1);
        assertThat(testCourseEnrollment.getEnrollmentDate()).isEqualTo(UPDATED_ENROLLEMENT_DATE);
        assertThat(testCourseEnrollment.getLastAccessedDate()).isEqualTo(UPDATED_LAST_ACCESSED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCourseEnrollmentWithPatch() throws Exception {
        // Initialize the database
        courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();

        // Update the courseEnrollment using partial update
        CourseEnrollment partialUpdatedCourseEnrollment = new CourseEnrollment();
        partialUpdatedCourseEnrollment.setId(courseEnrollment.getId());

        partialUpdatedCourseEnrollment.enrollmentDate(UPDATED_ENROLLEMENT_DATE).lastAccessedDate(UPDATED_LAST_ACCESSED_DATE);

        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
        CourseEnrollment testCourseEnrollment = courseEnrollmentList.get(courseEnrollmentList.size() - 1);
        assertThat(testCourseEnrollment.getEnrollmentDate()).isEqualTo(UPDATED_ENROLLEMENT_DATE);
        assertThat(testCourseEnrollment.getLastAccessedDate()).isEqualTo(UPDATED_LAST_ACCESSED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCourseEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();
        courseEnrollment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();
        courseEnrollment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = courseEnrollmentRepository.findAll().size();
        courseEnrollment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseEnrollment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseEnrollment in the database
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseEnrollment() throws Exception {
        // Initialize the database
        courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        int databaseSizeBeforeDelete = courseEnrollmentRepository.findAll().size();

        // Delete the courseEnrollment
        restCourseEnrollmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseEnrollment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.findAll();
        assertThat(courseEnrollmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
