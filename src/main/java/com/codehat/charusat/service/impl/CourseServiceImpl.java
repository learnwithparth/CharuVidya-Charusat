package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.repository.CourseRepository;
import com.codehat.charusat.service.CourseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> partialUpdate(Course course) {
        log.debug("Request to partially update Course : {}", course);

        return courseRepository
            .findById(course.getId())
            .map(
                existingCourse -> {
                    if (course.getCourseTitle() != null) {
                        existingCourse.setCourseTitle(course.getCourseTitle());
                    }
                    if (course.getCourseDescription() != null) {
                        existingCourse.setCourseDescription(course.getCourseDescription());
                    }
                    if (course.getCourseObjectives() != null) {
                        existingCourse.setCourseObjectives(course.getCourseObjectives());
                    }
                    if (course.getCourseSubTitle() != null) {
                        existingCourse.setCourseSubTitle(course.getCourseSubTitle());
                    }
                    if (course.getPreviewVideourl() != null) {
                        existingCourse.setPreviewVideourl(course.getPreviewVideourl());
                    }
                    if (course.getCourseLength() != null) {
                        existingCourse.setCourseLength(course.getCourseLength());
                    }
                    if (course.getLogo() != null) {
                        existingCourse.setLogo(course.getLogo());
                    }
                    if (course.getCourseCreatedOn() != null) {
                        existingCourse.setCourseCreatedOn(course.getCourseCreatedOn());
                    }
                    if (course.getCourseUpdatedOn() != null) {
                        existingCourse.setCourseUpdatedOn(course.getCourseUpdatedOn());
                    }
                    if (course.getCourseRootDir() != null) {
                        existingCourse.setCourseRootDir(course.getCourseRootDir());
                    }
                    if (course.getAmount() != null) {
                        existingCourse.setAmount(course.getAmount());
                    }
                    if (course.getIsDraft() != null) {
                        existingCourse.setIsDraft(course.getIsDraft());
                    }
                    if (course.getIsApproved() != null) {
                        existingCourse.setIsApproved(course.getIsApproved());
                    }
                    if (course.getCourseApprovalDate() != null) {
                        existingCourse.setCourseApprovalDate(course.getCourseApprovalDate());
                    }

                    return existingCourse;
                }
            )
            .map(courseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        return courseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        courseRepository.deleteById(id);
    }
}
