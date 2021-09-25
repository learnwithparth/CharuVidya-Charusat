package com.codehat.charusat.service.impl;

import com.codehat.charusat.config.Constants;
import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseReviewStatus;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.*;
import com.codehat.charusat.security.AuthoritiesConstants;
import com.codehat.charusat.service.CourseService;
import com.codehat.charusat.service.MailService;
import com.codehat.charusat.service.UserService;
import com.codehat.charusat.service.dto.CourseDTO;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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

    private final CourseReviewStatusRepository courseReviewStatusRepository;

    private final UserRepository userRepository;

    public CourseServiceImpl(
        CourseRepository courseRepository,
        UserService userService,
        MailService mailService,
        CourseSectionRepository courseSectionRepository,
        CourseSessionRepository courseSessionRepository,
        CourseReviewStatusRepository courseReviewStatusRepository,
        UserRepository userRepository
    ) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.courseSectionRepository = courseSectionRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.courseReviewStatusRepository = courseReviewStatusRepository;
        this.userRepository = userRepository;
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
                return courseRepository.findAllByIsApproved(true, pageable);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            String authority = user.get().getAuthorities().toString();
            if (authority.contains(AuthoritiesConstants.ADMIN)) {
                return courseRepository.findAll();
            } else if (authority.contains(AuthoritiesConstants.FACULTY)) {
                return courseRepository.findCourseByUserEqualsOrEnrolledUsersListsContaining(user.get(), user.get());
            } else if (authority.contains(AuthoritiesConstants.STUDENT)) {
                //                return courseRepository.findCourseByEnrolledUsersListsContaining(user.get(), pageable);
                return courseRepository.findAllByIsApproved(true);
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
                courseDTO.setMinStudents(course.getMinStudents() + getStudentEnrolledCountByCourse(course.getId()).getBody());
                list.add(courseDTO);
            }
            return list;
        } else {
            throw new Exception("User ot found");
        }
    }

    @Override
    public ResponseEntity enrollInCourse(String courseId) {
        AtomicBoolean flag = new AtomicBoolean(false);
        try {
            courseRepository
                .findById(Long.parseLong(courseId))
                .map(
                    existingCourse -> {
                        Set<User> alreadyEnrolledUsers = existingCourse.getEnrolledUsersLists();
                        if (
                            userService.getUserWithAuthorities().isPresent() &&
                            !alreadyEnrolledUsers.contains(userService.getUserWithAuthorities().get())
                        ) {
                            alreadyEnrolledUsers.add(userService.getUserWithAuthorities().get());
                            flag.set(true);
                        }
                        return existingCourse;
                    }
                )
                .map(courseRepository::save);
            if (flag.get()) return ResponseEntity.accepted().build(); else {
                return ResponseEntity.badRequest().body("Already enrolled");
            }
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

        CourseReviewStatus courseReviewStatus = new CourseReviewStatus();
        courseReviewStatus.setStatus(Constants.DRAFTING);
        courseReviewStatus.setStatusUpdatedOn(LocalDate.now());
        courseReviewStatus.setCourse(course);
        CourseReviewStatus crs = courseReviewStatusRepository.save(courseReviewStatus);
        course.setCourseReviewStatus(crs);
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
                    CourseReviewStatus crs = course.get().getCourseReviewStatus();
                    crs.setStatus(Constants.APPROVED);
                    crs.setStatusUpdatedOn(LocalDate.now());
                    courseReviewStatusRepository.save(crs);
                    course.get().setCourseReviewStatus(crs);
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
    public ResponseEntity<List<CourseDTO>> getTop10LatestCourses() {
        List<Course> result = courseRepository.coursesOrderedByUpdatedDate();
        List<CourseDTO> courses = new ArrayList<>();
        CourseDTO temp;
        for (Course course : result) {
            temp = new CourseDTO(course);
            temp.setMinStudents(temp.getMinStudents() + getStudentEnrolledCountByCourse(temp.getId()).getBody());
            courses.add(temp);
        }
        return ResponseEntity.ok().body(courses);
    }

    @Override
    public ResponseEntity<Map<String, String>> getOverview() {
        Map<String, String> map = new HashMap<>();

        Integer data = courseRepository.findAll().size();
        map.put("totalCourses", data.toString());

        data = courseRepository.findTotalEnrollment();
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            data += course.getMinStudents();
        }
        map.put("totalEnrollments", data.toString());

        data = userService.getTotalUsersByAuthority(AuthoritiesConstants.FACULTY);
        map.put("totalInstructors", data.toString());

        return ResponseEntity.ok().body(map);
    }

    @Override
    public ResponseEntity<Set<User>> getEnrolledUsersByCourseId(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            Set<User> users = course.get().getEnrolledUsersLists();
            return ResponseEntity.ok().body(users);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity assignReviewerToCourse(Long courseId, Long userId) {
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<User> user = userRepository.findById(userId);
        if (course.isPresent() && user.isPresent()) {
            CourseReviewStatus crs = course.get().getCourseReviewStatus();
            crs.setStatus(Constants.REVIEWER_ASSIGNED);
            crs.setStatusUpdatedOn(LocalDate.now());
            crs.setReviewer(user.get());
            course.get().setCourseReviewStatus(crs);
            courseRepository.save(course.get());
            courseReviewStatusRepository.save(crs);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<Course>> coursesForReview() {
        Optional<User> user = userService.getUserWithAuthorities();
        List<Course> courseList = new ArrayList<>();
        if (user.isPresent()) {
            List<CourseReviewStatus> list = courseReviewStatusRepository.findCourseReviewStatusByReviewer(user.get());
            for (CourseReviewStatus crs : list) {
                courseList.add(crs.getCourse());
            }
            return ResponseEntity.ok().body(courseList);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity receivedForApproval(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            course.get().isDraft(false);
            course.get().isApproved(false);
            CourseReviewStatus crs = course.get().getCourseReviewStatus();
            crs.setStatus(Constants.APPROVAL_PENDING);
            crs.setStatusUpdatedOn(LocalDate.now());
            courseReviewStatusRepository.save(crs);
            course.get().setCourseReviewStatus(crs);
            courseRepository.save(course.get());
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
