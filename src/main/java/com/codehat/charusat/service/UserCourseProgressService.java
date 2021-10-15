package com.codehat.charusat.service;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.domain.UserCourseProgress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserCourseProgressService {
    UserCourseProgress save(UserCourseProgress userCourseProgress);

    UserCourseProgress addUsingUserAndCourse(UserCourseProgress userCourseProgress);

    Optional<UserCourseProgress> findOne(Long id);

    List<UserCourseProgress> findAll();

    ResponseEntity<CourseProgress> getCourseProgressUsingUserAndCourse(UserCourseProgress userCourseProgress);

    boolean delete(Long id);

    Page<UserCourseProgress> findAll(Pageable pageable);
}
