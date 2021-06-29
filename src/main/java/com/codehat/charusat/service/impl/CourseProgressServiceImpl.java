package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.repository.CourseProgressRepository;
import com.codehat.charusat.service.CourseProgressService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseProgress}.
 */
@Service
@Transactional
public class CourseProgressServiceImpl implements CourseProgressService {

    private final Logger log = LoggerFactory.getLogger(CourseProgressServiceImpl.class);

    private final CourseProgressRepository courseProgressRepository;

    public CourseProgressServiceImpl(CourseProgressRepository courseProgressRepository) {
        this.courseProgressRepository = courseProgressRepository;
    }

    @Override
    public CourseProgress save(CourseProgress courseProgress) {
        log.debug("Request to save CourseProgress : {}", courseProgress);
        return courseProgressRepository.save(courseProgress);
    }

    @Override
    public Optional<CourseProgress> partialUpdate(CourseProgress courseProgress) {
        log.debug("Request to partially update CourseProgress : {}", courseProgress);

        return courseProgressRepository
            .findById(courseProgress.getId())
            .map(
                existingCourseProgress -> {
                    if (courseProgress.getCompleted() != null) {
                        existingCourseProgress.setCompleted(courseProgress.getCompleted());
                    }
                    if (courseProgress.getWatchSeconds() != null) {
                        existingCourseProgress.setWatchSeconds(courseProgress.getWatchSeconds());
                    }

                    return existingCourseProgress;
                }
            )
            .map(courseProgressRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseProgress> findAll(Pageable pageable) {
        log.debug("Request to get all CourseProgresses");
        return courseProgressRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseProgress> findOne(Long id) {
        log.debug("Request to get CourseProgress : {}", id);
        return courseProgressRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseProgress : {}", id);
        courseProgressRepository.deleteById(id);
    }
}
