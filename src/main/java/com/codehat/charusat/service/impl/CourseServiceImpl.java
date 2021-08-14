package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseRepository;
import com.codehat.charusat.repository.CourseSectionRepository;
import com.codehat.charusat.repository.CourseSessionRepository;
import com.codehat.charusat.security.AuthoritiesConstants;
import com.codehat.charusat.service.CourseService;
import com.codehat.charusat.service.MailService;
import com.codehat.charusat.service.UserService;
import com.codehat.charusat.service.dto.CourseDTO;
import java.time.LocalDate;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

    private final CourseSectionRepository courseSectionRepository;

    private final CourseSessionRepository courseSessionRepository;

    public CourseServiceImpl(
        CourseRepository courseRepository,
        UserService userService,
        MailService mailService,
        CourseSectionRepository courseSectionRepository,
        CourseSessionRepository courseSessionRepository
    ) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSessionRepository = courseSessionRepository;
    }

    @Override
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);

        /**
         * Setting the default values that needs to set during the course creation.
         * */
        course.setCourseCreatedOn(LocalDate.now());
        course.setIsApproved(false);
        course.setIsDraft(true);
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
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);

        /**
         * CUSTOM
         * */
        courseSessionRepository.deleteCourseSessionByCourseId(id);
        courseSectionRepository.deleteCourseSectionByCourseId(id);
        courseRepository.deleteById(id);
    }

    /**
     * CUSTOM
     * */
    @Override
    public List<CourseDTO> getByCategoryId(Long id) throws Exception {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            List<CourseDTO> list = new ArrayList<>();
            List<Course> courses = courseRepository.findByCategoryId(id);
            CourseDTO courseDTO;
            for (Course course : courses) {
                courseDTO = new CourseDTO(course);
                if (course.getEnrolledUsersLists().contains(user.get())) {
                    courseDTO.setEnrolled(true);
                } else {
                    courseDTO.setEnrolled(false);
                }
                list.add(courseDTO);
            }
            return list;
        } else {
            throw new Exception("User ot found");
        }
    }

    @Override
    public ResponseEntity enrollInCourse(String courseId) {
        try {
            courseRepository
                .findById(Long.parseLong(courseId))
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
        course.setIsApproved(false);
        course.setIsDraft(true);
        course.setAmount(0.0);
        course.setMaxStudents(0);
        course.setMinStudents(0);
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

    @Override
    public Page<Course> findAllCoursesByFilter(String filter, Pageable pageable) {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            String authority = user.get().getAuthorities().toString();
            if (authority.contains(AuthoritiesConstants.ADMIN)) {
                if (filter.contains("not-approved")) {
                    return courseRepository.findAllByIsApproved(false, pageable);
                } else if (filter.contains("approved")) {
                    return courseRepository.findAllByIsApproved(true, pageable);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<Integer> getStudentEnrolledCountByCourse(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            Integer count = course.get().getEnrolledUsersLists().size();
            return ResponseEntity.ok(count);
        } else {
            log.error("Course not found");
            return ResponseEntity.noContent().build();
        }
    }

    @Override
    public List<Course> getEnrolledCourses() throws Exception {
        Optional<User> user = userService.getUserWithAuthorities();
        List<Course> courses;
        if (user.isPresent()) {
            courses = courseRepository.findCourseByEnrolledUsersListsContaining(user.get());
            return courses;
        } else {
            throw new Exception("User not found");
        }
    }

    @Override
    public ResponseEntity<List<Course>> getTop10LatestCourses() {
        List<Course> courses = courseRepository.coursesOrderedByUpdatedDate().subList(0, 9);
        return ResponseEntity.ok().body(courses);
    }

    @Override
    public ResponseEntity receivedForApproval(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            course.get().isDraft(false);
            course.get().isApproved(false);
            courseRepository.save(course.get());
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
