package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseRepository;
import com.codehat.charusat.security.AuthoritiesConstants;
import com.codehat.charusat.service.CourseService;
import com.codehat.charusat.service.MailService;
import com.codehat.charusat.service.UserService;
import com.codehat.charusat.service.dto.CourseDTO;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    private final UserService userService;

    private final MailService mailService;

    public CourseServiceImpl(CourseRepository courseRepository, UserService userService, MailService mailService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    @Override
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);

        /**
         * Setting the default values that needs to set during the course creation.
         * */
        course.setCourseCreatedOn(LocalDate.now());
        course.setIsApproved(false);
        course.setCourseUpdatedOn(LocalDate.now());
        course.setUser(userService.getUserWithAuthorities().get());
        /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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

                    /**
                     * Not needed to update this attribute.
                     * */
                    if (course.getCourseCreatedOn() != null) {
                        existingCourse.setCourseCreatedOn(course.getCourseCreatedOn());
                    }

                    /**
                     * Updated the courseUpdatedOn back to current time.
                     * */
                    existingCourse.setCourseUpdatedOn(LocalDate.now());
                    /*if (course.getCourseUpdatedOn() != null) {
                        existingCourse.setCourseUpdatedOn(course.getCourseUpdatedOn());
                    }*/
                    if (course.getCourseRootDir() != null) {
                        existingCourse.setCourseRootDir(course.getCourseRootDir());
                    }
                    if (course.getAmount() != null) {
                        existingCourse.setAmount(course.getAmount());
                    }
                    if (course.getIsDraft() != null) {
                        existingCourse.setIsDraft(course.getIsDraft());
                    }
                    /**
                     * Changed the isApproved back to false;
                     * */
                    existingCourse.setIsApproved(false);
                    /*if (course.getIsApproved() != null) {
                        existingCourse.setIsApproved(course.getIsApproved());
                    }*/

                    /**
                     * Changed the approval date back to null;
                     * */
                    existingCourse.setCourseApprovalDate(null);
                    /*if (course.getCourseApprovalDate() != null) {
                        existingCourse.setCourseApprovalDate(course.getCourseApprovalDate());
                    }*/

                    return existingCourse;
                }
            )
            .map(courseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");

        /**
         * CUSTOM
         * Get different courses according to the role of the user.
         * */
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            String authority = user.get().getAuthorities().toString();
            if (authority.contains(AuthoritiesConstants.ADMIN)) {
                return courseRepository.findAll(pageable);
            } else if (authority.contains(AuthoritiesConstants.FACULTY)) {
                return courseRepository.findCourseByUserEqualsOrEnrolledUsersListsContaining(user.get(), user.get(), pageable);
            } else if (authority.contains(AuthoritiesConstants.STUDENT)) {
                //                return courseRepository.findCourseByEnrolledUsersListsContaining(user.get(), pageable);
                return courseRepository.findAll(pageable);
            } else {
                return null;
            }
        } else {
            return null;
        }
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

    /**
     * CUSTOM
     * */
    @Override
    public List<Course> getByCategoryId(Long id) {
        List<Course> list = courseRepository.findByCategoryId(id);
        return list;
    }

    @Override
    public ResponseEntity enrollInCourse(Course course) {
        try {
            courseRepository
                .findById(course.getId())
                .map(
                    existingCourse -> {
                        Set<User> alreadyEnrolledUsers = existingCourse.getEnrolledUsersLists();
                        if (userService.getUserWithAuthorities().isPresent()) {
                            alreadyEnrolledUsers.add(userService.getUserWithAuthorities().get());
                        }
                        return existingCourse;
                    }
                )
                .map(courseRepository::save);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public Course save(CourseDTO courseDTO) {
        log.debug("Request to save Course : {}", courseDTO);

        /**
         * Setting the default values that needs to set during the course creation.
         * */
        Course course = new Course(courseDTO);
        course.setCourseCreatedOn(LocalDate.now());
        course.setCourseUpdatedOn(LocalDate.now());
        course.setIsApproved(true);
        course.setIsDraft(false);
        course.setAmount(0.0);
        course.setUser(userService.getUserWithAuthorities().get());
        /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        return courseRepository.save(course);
    }

    @Override
    public Course approveCourse(Long courseId) {
        log.debug("Request to approve CourseId : {}", courseId);

        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            String authority = user.get().getAuthorities().toString();
            if (authority.contains("ROLE_ADMIN") || authority.contains("ROLE_REVIEWER")) {
                Optional<Course> course = courseRepository.findById(courseId);
                if (course.isPresent()) {
                    course.get().setIsApproved(true);
                    mailService.sendCourseApprovalMail(course.get());
                    return courseRepository.save(course.get());
                } else {
                    log.warn("Course not present");
                    return null;
                }
            } else {
                log.warn("You are not authorized");
                return null;
            }
        } else {
            log.warn("User not present");
            return null;
        }
    }
}
