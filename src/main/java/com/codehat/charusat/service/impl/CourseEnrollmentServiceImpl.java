package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.CourseEnrollment;
import com.codehat.charusat.repository.CourseEnrollmentRepository;
import com.codehat.charusat.service.CourseEnrollmentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseEnrollment}.
 */
@Service
@Transactional
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    private final Logger log = LoggerFactory.getLogger(CourseEnrollmentServiceImpl.class);

    private final CourseEnrollmentRepository courseEnrollmentRepository;

    public CourseEnrollmentServiceImpl(CourseEnrollmentRepository courseEnrollmentRepository) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
    }

    @Override
    public CourseEnrollment save(CourseEnrollment courseEnrollment) {
        log.debug("Request to save CourseEnrollment : {}", courseEnrollment);
        return courseEnrollmentRepository.save(courseEnrollment);
    }

    @Override
    public Optional<CourseEnrollment> partialUpdate(CourseEnrollment courseEnrollment) {
        log.debug("Request to partially update CourseEnrollment : {}", courseEnrollment);

        return courseEnrollmentRepository
            .findById(courseEnrollment.getId())
            .map(
                existingCourseEnrollment -> {
                    if (courseEnrollment.getEnrollmentDate() != null) {
                        existingCourseEnrollment.setEnrollmentDate(courseEnrollment.getEnrollmentDate());
                    }
                    if (courseEnrollment.getLastAccessedDate() != null) {
                        existingCourseEnrollment.setLastAccessedDate(courseEnrollment.getLastAccessedDate());
                    }

                    return existingCourseEnrollment;
                }
            )
            .map(courseEnrollmentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseEnrollment> findAll(Pageable pageable) {
        log.debug("Request to get all CourseEnrollments");
        return courseEnrollmentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseEnrollment> findOne(Long id) {
        log.debug("Request to get CourseEnrollment : {}", id);
        return courseEnrollmentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseEnrollment : {}", id);
        courseEnrollmentRepository.deleteById(id);
    }
}
