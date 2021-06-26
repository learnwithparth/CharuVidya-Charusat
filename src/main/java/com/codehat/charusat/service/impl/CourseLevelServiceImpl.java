package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.CourseLevel;
import com.codehat.charusat.repository.CourseLevelRepository;
import com.codehat.charusat.service.CourseLevelService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseLevel}.
 */
@Service
@Transactional
public class CourseLevelServiceImpl implements CourseLevelService {

    private final Logger log = LoggerFactory.getLogger(CourseLevelServiceImpl.class);

    private final CourseLevelRepository courseLevelRepository;

    public CourseLevelServiceImpl(CourseLevelRepository courseLevelRepository) {
        this.courseLevelRepository = courseLevelRepository;
    }

    @Override
    public CourseLevel save(CourseLevel courseLevel) {
        log.debug("Request to save CourseLevel : {}", courseLevel);
        return courseLevelRepository.save(courseLevel);
    }

    @Override
    public Optional<CourseLevel> partialUpdate(CourseLevel courseLevel) {
        log.debug("Request to partially update CourseLevel : {}", courseLevel);

        return courseLevelRepository
            .findById(courseLevel.getId())
            .map(
                existingCourseLevel -> {
                    if (courseLevel.getLevel() != null) {
                        existingCourseLevel.setLevel(courseLevel.getLevel());
                    }
                    if (courseLevel.getDescription() != null) {
                        existingCourseLevel.setDescription(courseLevel.getDescription());
                    }

                    return existingCourseLevel;
                }
            )
            .map(courseLevelRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseLevel> findAll(Pageable pageable) {
        log.debug("Request to get all CourseLevels");
        return courseLevelRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseLevel> findOne(Long id) {
        log.debug("Request to get CourseLevel : {}", id);
        return courseLevelRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseLevel : {}", id);
        courseLevelRepository.deleteById(id);
    }
}
