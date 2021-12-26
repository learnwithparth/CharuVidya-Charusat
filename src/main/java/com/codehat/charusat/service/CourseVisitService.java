package com.codehat.charusat.service;

import com.codehat.charusat.domain.CourseVisit;
import com.codehat.charusat.domain.User;
import java.util.List;

public interface CourseVisitService {
    CourseVisit updateCourseCount(Long courseId, User user) throws Exception;

    List<CourseVisit> findAll();
}
