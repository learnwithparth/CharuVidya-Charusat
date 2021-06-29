package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.repository.CourseSectionRepository;
import com.codehat.charusat.service.CourseSectionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseSection}.
 */
@Service
@Transactional
public class CourseSectionServiceImpl implements CourseSectionService {

    private final Logger log = LoggerFactory.getLogger(CourseSectionServiceImpl.class);

    private final CourseSectionRepository courseSectionRepository;

    public CourseSectionServiceImpl(CourseSectionRepository courseSectionRepository) {
        this.courseSectionRepository = courseSectionRepository;
    }

    @Override
    public CourseSection save(CourseSection courseSection) {
        log.debug("Request to save CourseSection : {}", courseSection);
        return courseSectionRepository.save(courseSection);
    }

    @Override
    public Optional<CourseSection> partialUpdate(CourseSection courseSection) {
        log.debug("Request to partially update CourseSection : {}", courseSection);

        return courseSectionRepository
            .findById(courseSection.getId())
            .map(
                existingCourseSection -> {
                    if (courseSection.getSectionTitle() != null) {
                        existingCourseSection.setSectionTitle(courseSection.getSectionTitle());
                    }
                    if (courseSection.getSectionDescription() != null) {
                        existingCourseSection.setSectionDescription(courseSection.getSectionDescription());
                    }
                    if (courseSection.getSectionOrder() != null) {
                        existingCourseSection.setSectionOrder(courseSection.getSectionOrder());
                    }
                    if (courseSection.getIsDraft() != null) {
                        existingCourseSection.setIsDraft(courseSection.getIsDraft());
                    }
                    if (courseSection.getIsApproved() != null) {
                        existingCourseSection.setIsApproved(courseSection.getIsApproved());
                    }

                    return existingCourseSection;
                }
            )
            .map(courseSectionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseSection> findAll(Pageable pageable) {
        log.debug("Request to get all CourseSections");
        return courseSectionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseSection> findOne(Long id) {
        log.debug("Request to get CourseSection : {}", id);
        return courseSectionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseSection : {}", id);
        courseSectionRepository.deleteById(id);
    }

    /**
     * CUSTOM
     * */
    @Override
    public Page<CourseSection> findCourseSectionByCourse(Long courseId, Pageable pageable) {
        log.debug("Request to get CourseSection by CourseId : {}", courseId);
        return courseSectionRepository.findCourseSectionByCourse_Id(courseId, pageable);
    }
}
