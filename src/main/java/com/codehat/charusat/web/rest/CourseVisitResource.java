package com.codehat.charusat.web.rest;

import com.codehat.charusat.domain.CourseVisit;
import com.codehat.charusat.service.CourseVisitService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class CourseVisitResource {

    private final Logger log = LoggerFactory.getLogger(CourseVisitResource.class);

    private final CourseVisitService courseVisitService;

    public CourseVisitResource(CourseVisitService courseVisitService) {
        this.courseVisitService = courseVisitService;
    }

    @GetMapping("/course-visits")
    public List<CourseVisit> getCourseVisits() {
        log.debug("REST request to get all CourseVisits");
        return courseVisitService.findAll();
    }
}
