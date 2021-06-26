package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.CourseCategory;
import com.codehat.charusat.repository.CourseCategoryRepository;
import com.codehat.charusat.service.CourseCategoryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseCategory}.
 */
@Service
@Transactional
public class CourseCategoryServiceImpl implements CourseCategoryService {

    private final Logger log = LoggerFactory.getLogger(CourseCategoryServiceImpl.class);

    private final CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryServiceImpl(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Override
    public CourseCategory save(CourseCategory courseCategory) {
        log.debug("Request to save CourseCategory : {}", courseCategory);
        return courseCategoryRepository.save(courseCategory);
    }

    @Override
    public Optional<CourseCategory> partialUpdate(CourseCategory courseCategory) {
        log.debug("Request to partially update CourseCategory : {}", courseCategory);

        return courseCategoryRepository
            .findById(courseCategory.getId())
            .map(
                existingCourseCategory -> {
                    if (courseCategory.getCourseCategoryTitle() != null) {
                        existingCourseCategory.setCourseCategoryTitle(courseCategory.getCourseCategoryTitle());
                    }
                    if (courseCategory.getLogo() != null) {
                        existingCourseCategory.setLogo(courseCategory.getLogo());
                    }
                    if (courseCategory.getIsParent() != null) {
                        existingCourseCategory.setIsParent(courseCategory.getIsParent());
                    }
                    if (courseCategory.getParentId() != null) {
                        existingCourseCategory.setParentId(courseCategory.getParentId());
                    }

                    return existingCourseCategory;
                }
            )
            .map(courseCategoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseCategory> findAll(Pageable pageable) {
        log.debug("Request to get all CourseCategories");
        return courseCategoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseCategory> findOne(Long id) {
        log.debug("Request to get CourseCategory : {}", id);
        return courseCategoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseCategory : {}", id);
        courseCategoryRepository.deleteById(id);
    }
}
