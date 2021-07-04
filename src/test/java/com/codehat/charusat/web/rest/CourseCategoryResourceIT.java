package com.codehat.charusat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.codehat.charusat.IntegrationTest;
import com.codehat.charusat.domain.CourseCategory;
import com.codehat.charusat.repository.CourseCategoryRepository;
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
 * Integration tests for the {@link CourseCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseCategoryResourceIT {

    private static final String DEFAULT_COURSE_CATEGORY_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_CATEGORY_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PARENT = true;
    private static final Boolean UPDATED_IS_PARENT = false;

    private static final Integer DEFAULT_PARENT_ID = 1;
    private static final Integer UPDATED_PARENT_ID = 2;

    private static final String ENTITY_API_URL = "/api/course-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseCategoryMockMvc;

    private CourseCategory courseCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseCategory createEntity(EntityManager em) {
        CourseCategory courseCategory = new CourseCategory()
            .courseCategoryTitle(DEFAULT_COURSE_CATEGORY_TITLE)
            .logo(DEFAULT_LOGO)
            .isParent(DEFAULT_IS_PARENT)
            .parentId(DEFAULT_PARENT_ID);
        return courseCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseCategory createUpdatedEntity(EntityManager em) {
        CourseCategory courseCategory = new CourseCategory()
            .courseCategoryTitle(UPDATED_COURSE_CATEGORY_TITLE)
            .logo(UPDATED_LOGO)
            .isParent(UPDATED_IS_PARENT)
            .parentId(UPDATED_PARENT_ID);
        return courseCategory;
    }

    @BeforeEach
    public void initTest() {
        courseCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseCategory() throws Exception {
        int databaseSizeBeforeCreate = courseCategoryRepository.findAll().size();
        // Create the CourseCategory
        restCourseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isCreated());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        CourseCategory testCourseCategory = courseCategoryList.get(courseCategoryList.size() - 1);
        assertThat(testCourseCategory.getCourseCategoryTitle()).isEqualTo(DEFAULT_COURSE_CATEGORY_TITLE);
        assertThat(testCourseCategory.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCourseCategory.getIsParent()).isEqualTo(DEFAULT_IS_PARENT);
        assertThat(testCourseCategory.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    @Transactional
    void createCourseCategoryWithExistingId() throws Exception {
        // Create the CourseCategory with an existing ID
        courseCategory.setId(1L);

        int databaseSizeBeforeCreate = courseCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCourseCategoryTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseCategoryRepository.findAll().size();
        // set the field null
        courseCategory.setCourseCategoryTitle(null);

        // Create the CourseCategory, which fails.

        restCourseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLogoIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseCategoryRepository.findAll().size();
        // set the field null
        courseCategory.setLogo(null);

        // Create the CourseCategory, which fails.

        restCourseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsParentIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseCategoryRepository.findAll().size();
        // set the field null
        courseCategory.setIsParent(null);

        // Create the CourseCategory, which fails.

        restCourseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseCategoryRepository.findAll().size();
        // set the field null
        courseCategory.setParentId(null);

        // Create the CourseCategory, which fails.

        restCourseCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseCategories() throws Exception {
        // Initialize the database
        courseCategoryRepository.saveAndFlush(courseCategory);

        // Get all the courseCategoryList
        restCourseCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseCategoryTitle").value(hasItem(DEFAULT_COURSE_CATEGORY_TITLE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].isParent").value(hasItem(DEFAULT_IS_PARENT)))
            .andExpect(jsonPath("$.[*].parentId").value(hasItem(DEFAULT_PARENT_ID)));
    }

    @Test
    @Transactional
    void getCourseCategory() throws Exception {
        // Initialize the database
        courseCategoryRepository.saveAndFlush(courseCategory);

        // Get the courseCategory
        restCourseCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, courseCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseCategory.getId().intValue()))
            .andExpect(jsonPath("$.courseCategoryTitle").value(DEFAULT_COURSE_CATEGORY_TITLE))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.isParent").value(DEFAULT_IS_PARENT))
            .andExpect(jsonPath("$.parentId").value(DEFAULT_PARENT_ID));
    }

    @Test
    @Transactional
    void getNonExistingCourseCategory() throws Exception {
        // Get the courseCategory
        restCourseCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseCategory() throws Exception {
        // Initialize the database
        courseCategoryRepository.saveAndFlush(courseCategory);

        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();

        // Update the courseCategory
        CourseCategory updatedCourseCategory = courseCategoryRepository.findById(courseCategory.getId()).get();
        // Disconnect from session so that the updates on updatedCourseCategory are not directly saved in db
        em.detach(updatedCourseCategory);
        updatedCourseCategory
            .courseCategoryTitle(UPDATED_COURSE_CATEGORY_TITLE)
            .logo(UPDATED_LOGO)
            .isParent(UPDATED_IS_PARENT)
            .parentId(UPDATED_PARENT_ID);

        restCourseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseCategory))
            )
            .andExpect(status().isOk());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
        CourseCategory testCourseCategory = courseCategoryList.get(courseCategoryList.size() - 1);
        assertThat(testCourseCategory.getCourseCategoryTitle()).isEqualTo(UPDATED_COURSE_CATEGORY_TITLE);
        assertThat(testCourseCategory.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCourseCategory.getIsParent()).isEqualTo(UPDATED_IS_PARENT);
        assertThat(testCourseCategory.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void putNonExistingCourseCategory() throws Exception {
        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();
        courseCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseCategory() throws Exception {
        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();
        courseCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseCategory() throws Exception {
        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();
        courseCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseCategoryWithPatch() throws Exception {
        // Initialize the database
        courseCategoryRepository.saveAndFlush(courseCategory);

        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();

        // Update the courseCategory using partial update
        CourseCategory partialUpdatedCourseCategory = new CourseCategory();
        partialUpdatedCourseCategory.setId(courseCategory.getId());

        restCourseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseCategory))
            )
            .andExpect(status().isOk());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
        CourseCategory testCourseCategory = courseCategoryList.get(courseCategoryList.size() - 1);
        assertThat(testCourseCategory.getCourseCategoryTitle()).isEqualTo(DEFAULT_COURSE_CATEGORY_TITLE);
        assertThat(testCourseCategory.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCourseCategory.getIsParent()).isEqualTo(DEFAULT_IS_PARENT);
        assertThat(testCourseCategory.getParentId()).isEqualTo(DEFAULT_PARENT_ID);
    }

    @Test
    @Transactional
    void fullUpdateCourseCategoryWithPatch() throws Exception {
        // Initialize the database
        courseCategoryRepository.saveAndFlush(courseCategory);

        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();

        // Update the courseCategory using partial update
        CourseCategory partialUpdatedCourseCategory = new CourseCategory();
        partialUpdatedCourseCategory.setId(courseCategory.getId());

        partialUpdatedCourseCategory
            .courseCategoryTitle(UPDATED_COURSE_CATEGORY_TITLE)
            .logo(UPDATED_LOGO)
            .isParent(UPDATED_IS_PARENT)
            .parentId(UPDATED_PARENT_ID);

        restCourseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseCategory))
            )
            .andExpect(status().isOk());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
        CourseCategory testCourseCategory = courseCategoryList.get(courseCategoryList.size() - 1);
        assertThat(testCourseCategory.getCourseCategoryTitle()).isEqualTo(UPDATED_COURSE_CATEGORY_TITLE);
        assertThat(testCourseCategory.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCourseCategory.getIsParent()).isEqualTo(UPDATED_IS_PARENT);
        assertThat(testCourseCategory.getParentId()).isEqualTo(UPDATED_PARENT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingCourseCategory() throws Exception {
        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();
        courseCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseCategory() throws Exception {
        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();
        courseCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseCategory() throws Exception {
        int databaseSizeBeforeUpdate = courseCategoryRepository.findAll().size();
        courseCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseCategory in the database
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseCategory() throws Exception {
        // Initialize the database
        courseCategoryRepository.saveAndFlush(courseCategory);

        int databaseSizeBeforeDelete = courseCategoryRepository.findAll().size();

        // Delete the courseCategory
        restCourseCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseCategory> courseCategoryList = courseCategoryRepository.findAll();
        assertThat(courseCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
