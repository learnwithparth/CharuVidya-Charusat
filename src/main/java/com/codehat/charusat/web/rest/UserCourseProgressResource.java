package com.codehat.charusat.web.rest;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.domain.UserCourseProgress;
import com.codehat.charusat.service.UserCourseProgressService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserCourseProgressResource {

    private final Logger log = LoggerFactory.getLogger(UserCourseProgressResource.class);
    private static final String ENTITY_NAME = "userCourseProgress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCourseProgressService userCourseProgressService;

    public UserCourseProgressResource(UserCourseProgressService userCourseProgressService) {
        this.userCourseProgressService = userCourseProgressService;
    }

    @GetMapping("/user-course-progress") //accessible by only admin
    public List<UserCourseProgress> getAllUserCourseProgress() {
        return userCourseProgressService.findAll();
    }

    @GetMapping("/user-course-progress/{id}") //accessible by only admin
    public UserCourseProgress getUserCourseProgressById(@PathVariable Long id) {
        Optional<UserCourseProgress> userCourseProgressOptional = userCourseProgressService.findOne(id);
        return userCourseProgressOptional.isPresent() ? userCourseProgressOptional.get() : null;
    }

    @PostMapping("/user-course-progress")
    public void addNewUserCourseProgress(@RequestBody UserCourseProgress userCourseProgress) throws Exception {
        userCourseProgressService.addUsingUserAndCourse(userCourseProgress);
    }

    @PostMapping("/user-course-progress/getCourseProgress")
    public ResponseEntity<CourseProgress> getCourseProgressByCourseAndUser(@RequestBody UserCourseProgress userCourseProgress)
        throws Exception {
        return userCourseProgressService.getCourseProgressUsingUserAndCourse(userCourseProgress);
    }

    @DeleteMapping("/user-course-progress/{id}") //accessible by only admin
    public boolean deleteUserCourseProgreeById(@PathVariable Long id) {
        return userCourseProgressService.delete(id);
    }
}
