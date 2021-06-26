package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.repository.CourseSessionRepository;
import com.codehat.charusat.service.CourseSessionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseSession}.
 */
@Service
@Transactional
public class CourseSessionServiceImpl implements CourseSessionService {

    private final Logger log = LoggerFactory.getLogger(CourseSessionServiceImpl.class);

    private final CourseSessionRepository courseSessionRepository;

    public CourseSessionServiceImpl(CourseSessionRepository courseSessionRepository) {
        this.courseSessionRepository = courseSessionRepository;
    }

    @Override
    public CourseSession save(CourseSession courseSession) {
        log.debug("Request to save CourseSession : {}", courseSession);
        return courseSessionRepository.save(courseSession);
    }

    @Override
    public Optional<CourseSession> partialUpdate(CourseSession courseSession) {
        log.debug("Request to partially update CourseSession : {}", courseSession);

        return courseSessionRepository
            .findById(courseSession.getId())
            .map(
                existingCourseSession -> {
                    if (courseSession.getSessionTitle() != null) {
                        existingCourseSession.setSessionTitle(courseSession.getSessionTitle());
                    }
                    if (courseSession.getSessionDescription() != null) {
                        existingCourseSession.setSessionDescription(courseSession.getSessionDescription());
                    }
                    if (courseSession.getSessionVideo() != null) {
                        existingCourseSession.setSessionVideo(courseSession.getSessionVideo());
                    }
                    if (courseSession.getSessionDuration() != null) {
                        existingCourseSession.setSessionDuration(courseSession.getSessionDuration());
                    }
                    if (courseSession.getSessionOrder() != null) {
                        existingCourseSession.setSessionOrder(courseSession.getSessionOrder());
                    }
                    if (courseSession.getSessionResource() != null) {
                        existingCourseSession.setSessionResource(courseSession.getSessionResource());
                    }
                    if (courseSession.getSessionLocation() != null) {
                        existingCourseSession.setSessionLocation(courseSession.getSessionLocation());
                    }
                    if (courseSession.getIsPreview() != null) {
                        existingCourseSession.setIsPreview(courseSession.getIsPreview());
                    }
                    if (courseSession.getIsDraft() != null) {
                        existingCourseSession.setIsDraft(courseSession.getIsDraft());
                    }
                    if (courseSession.getIsApproved() != null) {
                        existingCourseSession.setIsApproved(courseSession.getIsApproved());
                    }
                    if (courseSession.getIsPublished() != null) {
                        existingCourseSession.setIsPublished(courseSession.getIsPublished());
                    }

                    return existingCourseSession;
                }
            )
            .map(courseSessionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseSession> findAll(Pageable pageable) {
        log.debug("Request to get all CourseSessions");
        return courseSessionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseSession> findOne(Long id) {
        log.debug("Request to get CourseSession : {}", id);
        return courseSessionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseSession : {}", id);
        courseSessionRepository.deleteById(id);
    }
}
