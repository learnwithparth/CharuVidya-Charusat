package com.codehat.charusat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehat.charusat.IntegrationTest;
import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.repository.CourseSectionRepository;
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
 * Integration tests for the {@link CourseSectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseSectionResourceIT {

    private static final String DEFAULT_SECTION_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SECTION_ORDER = 1;
    private static final Integer UPDATED_SECTION_ORDER = 2;

    private static final Boolean DEFAULT_IS_DRAFT = false;
    private static final Boolean UPDATED_IS_DRAFT = true;

    private static final Boolean DEFAULT_IS_APPROVED = false;
    private static final Boolean UPDATED_IS_APPROVED = true;

    private static final String ENTITY_API_URL = "/api/course-sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseSectionMockMvc;

    private CourseSection courseSection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseSection createEntity(EntityManager em) {
        CourseSection courseSection = new CourseSection()
            .sectionTitle(DEFAULT_SECTION_TITLE)
            .sectionDescription(DEFAULT_SECTION_DESCRIPTION)
            .sectionOrder(DEFAULT_SECTION_ORDER)
            .isDraft(DEFAULT_IS_DRAFT)
            .isApproved(DEFAULT_IS_APPROVED);
        return courseSection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseSection createUpdatedEntity(EntityManager em) {
        CourseSection courseSection = new CourseSection()
            .sectionTitle(UPDATED_SECTION_TITLE)
            .sectionDescription(UPDATED_SECTION_DESCRIPTION)
            .sectionOrder(UPDATED_SECTION_ORDER)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED);
        return courseSection;
    }

    @BeforeEach
    public void initTest() {
        courseSection = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseSection() throws Exception {
        int databaseSizeBeforeCreate = courseSectionRepository.findAll().size();
        // Create the CourseSection
        restCourseSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSection)))
            .andExpect(status().isCreated());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeCreate + 1);
        CourseSection testCourseSection = courseSectionList.get(courseSectionList.size() - 1);
        assertThat(testCourseSection.getSectionTitle()).isEqualTo(DEFAULT_SECTION_TITLE);
        assertThat(testCourseSection.getSectionDescription()).isEqualTo(DEFAULT_SECTION_DESCRIPTION);
        assertThat(testCourseSection.getSectionOrder()).isEqualTo(DEFAULT_SECTION_ORDER);
        assertThat(testCourseSection.getIsDraft()).isEqualTo(DEFAULT_IS_DRAFT);
        assertThat(testCourseSection.getIsApproved()).isEqualTo(DEFAULT_IS_APPROVED);
    }

    @Test
    @Transactional
    void createCourseSectionWithExistingId() throws Exception {
        // Create the CourseSection with an existing ID
        courseSection.setId(1L);

        int databaseSizeBeforeCreate = courseSectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSection)))
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectionTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSectionRepository.findAll().size();
        // set the field null
        courseSection.setSectionTitle(null);

        // Create the CourseSection, which fails.

        restCourseSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSection)))
            .andExpect(status().isBadRequest());

        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSectionOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSectionRepository.findAll().size();
        // set the field null
        courseSection.setSectionOrder(null);

        // Create the CourseSection, which fails.

        restCourseSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSection)))
            .andExpect(status().isBadRequest());

        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDraftIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSectionRepository.findAll().size();
        // set the field null
        courseSection.setIsDraft(null);

        // Create the CourseSection, which fails.

        restCourseSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSection)))
            .andExpect(status().isBadRequest());

        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsApprovedIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseSectionRepository.findAll().size();
        // set the field null
        courseSection.setIsApproved(null);

        // Create the CourseSection, which fails.

        restCourseSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSection)))
            .andExpect(status().isBadRequest());

        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseSections() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        // Get all the courseSectionList
        restCourseSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionTitle").value(hasItem(DEFAULT_SECTION_TITLE)))
            .andExpect(jsonPath("$.[*].sectionDescription").value(hasItem(DEFAULT_SECTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sectionOrder").value(hasItem(DEFAULT_SECTION_ORDER)))
            .andExpect(jsonPath("$.[*].isDraft").value(hasItem(DEFAULT_IS_DRAFT.booleanValue())))
            .andExpect(jsonPath("$.[*].isApproved").value(hasItem(DEFAULT_IS_APPROVED.booleanValue())));
    }

    @Test
    @Transactional
    void getCourseSection() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        // Get the courseSection
        restCourseSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, courseSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseSection.getId().intValue()))
            .andExpect(jsonPath("$.sectionTitle").value(DEFAULT_SECTION_TITLE))
            .andExpect(jsonPath("$.sectionDescription").value(DEFAULT_SECTION_DESCRIPTION))
            .andExpect(jsonPath("$.sectionOrder").value(DEFAULT_SECTION_ORDER))
            .andExpect(jsonPath("$.isDraft").value(DEFAULT_IS_DRAFT.booleanValue()))
            .andExpect(jsonPath("$.isApproved").value(DEFAULT_IS_APPROVED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCourseSection() throws Exception {
        // Get the courseSection
        restCourseSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseSection() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();

        // Update the courseSection
        CourseSection updatedCourseSection = courseSectionRepository.findById(courseSection.getId()).get();
        // Disconnect from session so that the updates on updatedCourseSection are not directly saved in db
        em.detach(updatedCourseSection);
        updatedCourseSection
            .sectionTitle(UPDATED_SECTION_TITLE)
            .sectionDescription(UPDATED_SECTION_DESCRIPTION)
            .sectionOrder(UPDATED_SECTION_ORDER)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED);

        restCourseSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseSection))
            )
            .andExpect(status().isOk());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
        CourseSection testCourseSection = courseSectionList.get(courseSectionList.size() - 1);
        assertThat(testCourseSection.getSectionTitle()).isEqualTo(UPDATED_SECTION_TITLE);
        assertThat(testCourseSection.getSectionDescription()).isEqualTo(UPDATED_SECTION_DESCRIPTION);
        assertThat(testCourseSection.getSectionOrder()).isEqualTo(UPDATED_SECTION_ORDER);
        assertThat(testCourseSection.getIsDraft()).isEqualTo(UPDATED_IS_DRAFT);
        assertThat(testCourseSection.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
    }

    @Test
    @Transactional
    void putNonExistingCourseSection() throws Exception {
        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();
        courseSection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseSection() throws Exception {
        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();
        courseSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseSection() throws Exception {
        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();
        courseSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseSection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseSectionWithPatch() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();

        // Update the courseSection using partial update
        CourseSection partialUpdatedCourseSection = new CourseSection();
        partialUpdatedCourseSection.setId(courseSection.getId());

        partialUpdatedCourseSection
            .sectionDescription(UPDATED_SECTION_DESCRIPTION)
            .sectionOrder(UPDATED_SECTION_ORDER)
            .isApproved(UPDATED_IS_APPROVED);

        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseSection))
            )
            .andExpect(status().isOk());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
        CourseSection testCourseSection = courseSectionList.get(courseSectionList.size() - 1);
        assertThat(testCourseSection.getSectionTitle()).isEqualTo(DEFAULT_SECTION_TITLE);
        assertThat(testCourseSection.getSectionDescription()).isEqualTo(UPDATED_SECTION_DESCRIPTION);
        assertThat(testCourseSection.getSectionOrder()).isEqualTo(UPDATED_SECTION_ORDER);
        assertThat(testCourseSection.getIsDraft()).isEqualTo(DEFAULT_IS_DRAFT);
        assertThat(testCourseSection.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
    }

    @Test
    @Transactional
    void fullUpdateCourseSectionWithPatch() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();

        // Update the courseSection using partial update
        CourseSection partialUpdatedCourseSection = new CourseSection();
        partialUpdatedCourseSection.setId(courseSection.getId());

        partialUpdatedCourseSection
            .sectionTitle(UPDATED_SECTION_TITLE)
            .sectionDescription(UPDATED_SECTION_DESCRIPTION)
            .sectionOrder(UPDATED_SECTION_ORDER)
            .isDraft(UPDATED_IS_DRAFT)
            .isApproved(UPDATED_IS_APPROVED);

        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseSection))
            )
            .andExpect(status().isOk());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
        CourseSection testCourseSection = courseSectionList.get(courseSectionList.size() - 1);
        assertThat(testCourseSection.getSectionTitle()).isEqualTo(UPDATED_SECTION_TITLE);
        assertThat(testCourseSection.getSectionDescription()).isEqualTo(UPDATED_SECTION_DESCRIPTION);
        assertThat(testCourseSection.getSectionOrder()).isEqualTo(UPDATED_SECTION_ORDER);
        assertThat(testCourseSection.getIsDraft()).isEqualTo(UPDATED_IS_DRAFT);
        assertThat(testCourseSection.getIsApproved()).isEqualTo(UPDATED_IS_APPROVED);
    }

    @Test
    @Transactional
    void patchNonExistingCourseSection() throws Exception {
        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();
        courseSection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseSection() throws Exception {
        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();
        courseSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseSection() throws Exception {
        int databaseSizeBeforeUpdate = courseSectionRepository.findAll().size();
        courseSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseSection))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseSection in the database
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseSection() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        int databaseSizeBeforeDelete = courseSectionRepository.findAll().size();

        // Delete the courseSection
        restCourseSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseSection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseSection> courseSectionList = courseSectionRepository.findAll();
        assertThat(courseSectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
