package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.domain.UserCourseProgress;
import com.codehat.charusat.repository.CourseProgressRepository;
import com.codehat.charusat.repository.CourseRepository;
import com.codehat.charusat.repository.UserCourseProgressRepository;
import com.codehat.charusat.service.UserCourseProgressService;
import com.codehat.charusat.service.UserService;
import com.codehat.charusat.web.rest.UserCourseProgressResource;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCourseProgressServiceImpl implements UserCourseProgressService {

    private final Logger log = LoggerFactory.getLogger(UserCourseProgressServiceImpl.class);
    private final UserCourseProgressRepository userCourseProgressRepository;
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final CourseProgressRepository courseProgressRepository;

    public UserCourseProgressServiceImpl(
        UserCourseProgressRepository userCourseProgressRepository,
        UserService userService,
        CourseRepository courseRepository,
        CourseProgressRepository courseProgressRepository
    ) {
        this.userCourseProgressRepository = userCourseProgressRepository;
        this.userService = userService;
        this.courseRepository = courseRepository;
        this.courseProgressRepository = courseProgressRepository;
    }

    @Override
    public UserCourseProgress save(UserCourseProgress userCourseProgress) {
        UserCourseProgress userCourseProgress1 = new UserCourseProgress(
            userCourseProgress.getUser(),
            userCourseProgress.getCourse(),
            userCourseProgress.getCourseProgress()
        );
        return userCourseProgressRepository.save(userCourseProgress1);
    }

    @Override
    public UserCourseProgress addUsingUserAndCourse(UserCourseProgress ucp) {
        Optional<Course> course = courseRepository.findById(ucp.getCourse().getId());
        Optional<CourseProgress> courseProgress = courseProgressRepository.findById(ucp.getCourseProgress().getId());
        Optional<User> user = userService.getUserWithAuthorities();
        System.out.println(ucp.toString());
        System.out.println(courseProgress.toString());
        if (course.isPresent() && courseProgress.isPresent() && user.isPresent()) {
            Optional<UserCourseProgress> userCourseProgress = userCourseProgressRepository.findByUserAndCourse(user.get(), course.get());
            UserCourseProgress userCourseProgress1;
            if (userCourseProgress.isPresent()) {
                userCourseProgress1 = userCourseProgress.get();
                userCourseProgress1.setCourseProgress(courseProgress.get());
            } else {
                userCourseProgress1 = new UserCourseProgress(user.get(), course.get(), courseProgress.get());
            }
            userCourseProgressRepository.save(userCourseProgress1);
        }
        return null; //invalid user/course/courseProgress
    }

    @Override
    public ResponseEntity<CourseProgress> getCourseProgressUsingUserAndCourse(UserCourseProgress ucp) {
        try {
            Optional<Course> course = courseRepository.findById(ucp.getCourse().getId());
            Optional<User> user = userService.getUserWithAuthorities();
            if (user.isPresent()) {
                if (course.isPresent()) {
                    Optional<UserCourseProgress> userCourseProgress = userCourseProgressRepository.findByUserAndCourse(
                        user.get(),
                        course.get()
                    );
                    if (userCourseProgress.isPresent()) {
                        return ResponseEntity.ok().body(userCourseProgress.get().getCourseProgress());
                    } else throw new Exception("No such course progress exist");
                } else throw new Exception("No such course exist");
            } else throw new Exception("No such user exist");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public Optional<UserCourseProgress> findOne(Long id) {
        return userCourseProgressRepository.findById(id);
    }

    @Override
    public List<UserCourseProgress> findAll() {
        return userCourseProgressRepository.findAll();
    }

    @Override
    public boolean delete(Long id) {
        if (!findOne(id).isPresent()) return false;
        userCourseProgressRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<UserCourseProgress> findAll(Pageable pageable) {
        return userCourseProgressRepository.findAll(pageable);
    }
}
