package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseVisit;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseVisitRepository;
import com.codehat.charusat.service.CourseService;
import com.codehat.charusat.service.CourseVisitService;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CourseVisitServiceImpl implements CourseVisitService {

    private final CourseVisitRepository courseVisitRepository;

    private final CourseService courseService;

    CourseVisitServiceImpl(CourseVisitRepository courseVisitRepository, CourseService courseService) {
        this.courseVisitRepository = courseVisitRepository;
        this.courseService = courseService;
    }

    @Override
    public CourseVisit updateCourseCount(Long courseId, User user) throws Exception {
        Optional<Course> course = courseService.findOne(courseId);
        if (course.isPresent()) {
            Optional<CourseVisit> courseVisitInDB = courseVisitRepository.findCourseVisitByCourseAndUser(course.get(), user);
            if (courseVisitInDB.isEmpty()) {
                CourseVisit newCourseVisit = new CourseVisit();
                newCourseVisit.setLastVisitedDate(Instant.now());
                newCourseVisit.setUser(user);
                newCourseVisit.setCourse(course.get());
                newCourseVisit.setCourseVisitedCount(1L);
                return courseVisitRepository.save(newCourseVisit);
            } else {
                courseVisitInDB.get().setLastVisitedDate(Instant.now());
                courseVisitInDB.get().setCourseVisitedCount(courseVisitInDB.get().getCourseVisitedCount() + 1);
                return courseVisitRepository.save(courseVisitInDB.get());
            }
        } else {
            throw new Exception("Course does not exists");
        }
    }
}
