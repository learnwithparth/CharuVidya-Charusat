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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

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
    public ResponseEntity<List<UserCourseProgress>> getAllUserCourseProgress(Pageable pageable) {
        log.debug("REST request to get a page of UserCourseProgress");
        Page<UserCourseProgress> page = userCourseProgressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
