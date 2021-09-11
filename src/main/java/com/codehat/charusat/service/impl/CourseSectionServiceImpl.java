package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseSectionRepository;
import com.codehat.charusat.repository.CourseSessionRepository;
import com.codehat.charusat.service.CourseSectionService;
import com.codehat.charusat.service.UserService;
import com.codehat.charusat.service.dto.CourseSectionDTO;
import java.util.*;
import javax.swing.text.html.Option;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    private final UserService userService;

    private final CourseServiceImpl courseService;

    private final CourseSessionRepository courseSessionRepository;

    public CourseSectionServiceImpl(
        CourseSectionRepository courseSectionRepository,
        UserService userService,
        CourseServiceImpl courseService,
        CourseSessionRepository courseSessionRepository
    ) {
        this.courseSectionRepository = courseSectionRepository;
        this.userService = userService;
        this.courseService = courseService;
        this.courseSessionRepository = courseSessionRepository;
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
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete CourseSection : {}", id);
        courseSessionRepository.deleteCourseSessionByCourseSectionId(id);
        courseSectionRepository.deleteById(id);
    }

    /**
     * CUSTOM
     * */
    @Override
    public Page<CourseSection> findCourseSectionByCourse(Long courseId, Pageable pageable) {
        log.debug("Request to get CourseSection by CourseId : {}", courseId);
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            if (
                user.get().getAuthorities().toString().contains("ROLE_ADMIN") ||
                user.get().getAuthorities().toString().contains("ROLE_REVIEWER")
            ) {
                return courseSectionRepository.findCourseSectionByCourse_Id(courseId, pageable);
            } else if (user.get().getAuthorities().toString().contains("ROLE_FACULTY")) {
                return courseSectionRepository.findCourseSectionByCourse_User_IdAndCourse_Id(user.get().getId(), courseId, pageable);
            } else if (user.get().getAuthorities().toString().contains("ROLE_STUDENT")) {
                //                return courseSectionRepository.findCourseSectionByCourse_IdAndCourseEnrolledUsersListsContaining(
                //                    courseId,
                //                    user.get(),
                //                    pageable
                //                );
                return courseSectionRepository.findCourseSectionByCourse_IdAndIsApproved(courseId, true, pageable);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public CourseSection save(Long courseId, CourseSectionDTO courseSectionDTO) {
        Optional<Course> course = courseService.findOne(courseId);
        Optional<User> user = userService.getUserWithAuthorities();
        if (course.isPresent() && course.get().getUser().equals(user.get())) {
            CourseSection courseSection = new CourseSection(courseSectionDTO);
            courseSection.course(course.get());
            courseSection.isDraft(false);
            courseSection.sectionOrder(courseSectionRepository.findCourseSectionByCourse_Id(courseId).size() + 1);
            courseSection.isApproved(false);
            return courseSectionRepository.save(courseSection);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<Map<CourseSection, List<CourseSession>>> findAllCourseSectionAndSessionByCourse(Long courseId) {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            Optional<Course> course = courseService.findOne(courseId);
            if (course.isPresent()) {
                Map<CourseSection, List<CourseSession>> map = new HashMap<>();
                List<CourseSection> courseSections = courseSectionRepository.findCourseSectionByCourse_Id(courseId);
                List<CourseSession> courseSessions;
                for (CourseSection courseSection : courseSections) {
                    courseSessions = courseSessionRepository.findAllByCourseSection_Id(courseSection.getId());
                    map.put(courseSection, courseSessions);
                }
                return ResponseEntity.ok(map);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public CourseSection approveCourseSection(Long id) throws Exception {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            if (
                user.get().getAuthorities().toString().contains("ROLE_ADMIN") ||
                user.get().getAuthorities().toString().contains("ROLE_REVIEWER")
            ) {
                Optional<CourseSection> courseSection = courseSectionRepository.findById(id);
                if (courseSection.isPresent()) {
                    courseSection.get().setIsApproved(true);
                    return courseSectionRepository.save(courseSection.get());
                } else {
                    throw new Exception("No such course-section");
                }
            } else {
                throw new Exception("User is not authorised");
            }
        } else {
            throw new Exception("User not found!");
        }
    }
}
