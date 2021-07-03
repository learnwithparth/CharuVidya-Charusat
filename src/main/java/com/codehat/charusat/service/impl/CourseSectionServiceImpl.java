package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseSectionRepository;
import com.codehat.charusat.service.CourseSectionService;

import java.util.Optional;

import com.codehat.charusat.service.UserService;
import com.codehat.charusat.service.dto.CourseSectionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseSection}.
 */
@Service
@Transactional
public class CourseSectionServiceImpl implements CourseSectionService {

    private final Logger log = LoggerFactory.getLogger(CourseSectionServiceImpl.class);

    private final CourseSectionRepository courseSectionRepository;

    private final UserService userService;

    private final CourseServiceImpl courseService;

    public CourseSectionServiceImpl(
        CourseSectionRepository courseSectionRepository,
        UserService userService,
        CourseServiceImpl courseService
    ) {
        this.courseSectionRepository = courseSectionRepository;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public CourseSection save(CourseSection courseSection) {
        log.debug("Request to save CourseSection : {}", courseSection);
        return courseSectionRepository.save(courseSection);
    }

    @Override
    public Optional<CourseSection> partialUpdate(CourseSection courseSection) {
        log.debug("Request to partially update CourseSection : {}", courseSection);

        return courseSectionRepository
            .findById(courseSection.getId())
            .map(
                existingCourseSection -> {
                    if (courseSection.getSectionTitle() != null) {
                        existingCourseSection.setSectionTitle(courseSection.getSectionTitle());
                    }
                    if (courseSection.getSectionDescription() != null) {
                        existingCourseSection.setSectionDescription(courseSection.getSectionDescription());
                    }
                    if (courseSection.getSectionOrder() != null) {
                        existingCourseSection.setSectionOrder(courseSection.getSectionOrder());
                    }
                    if (courseSection.getIsDraft() != null) {
                        existingCourseSection.setIsDraft(courseSection.getIsDraft());
                    }
                    if (courseSection.getIsApproved() != null) {
                        existingCourseSection.setIsApproved(courseSection.getIsApproved());
                    }

                    return existingCourseSection;
                }
            )
            .map(courseSectionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseSection> findAll(Pageable pageable) {
        log.debug("Request to get all CourseSections");
        return courseSectionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseSection> findOne(Long id) {
        log.debug("Request to get CourseSection : {}", id);
        return courseSectionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseSection : {}", id);
        courseSectionRepository.deleteById(id);
    }

    /**
     * CUSTOM
     * */
    @Override
    public Page<CourseSection> findCourseSectionByCourse(Long courseId, Pageable pageable) {
        log.debug("Request to get CourseSection by CourseId : {}", courseId);
        Optional<User> user = userService.getUserWithAuthorities();
        if(user.isPresent()){
            if(user.get().getAuthorities().toString().contains("ROLE_ADMIN")){
                return courseSectionRepository.findCourseSectionByCourse_Id(courseId, pageable);
            } else if(user.get().getAuthorities().toString().contains("ROLE_FACULTY")) {
                return courseSectionRepository.findCourseSectionByCourse_User_IdAndCourse_Id(user.get().getId(), courseId, pageable);
            } else if(user.get().getAuthorities().toString().contains("ROLE_STUDENT")){
                return courseSectionRepository.findCourseSectionByCourse_IdAndCourseEnrolledUsersListsContaining(courseId, user.get(), pageable);
            } else{
                return null;
            }
        } else{
            return null;
        }
    }

    @Override
    public CourseSection save(Long courseId, CourseSectionDTO courseSectionDTO) {
        System.out.println("Hello");
        Optional<Course> course = courseService.findOne(courseId);
        Optional<User> user = userService.getUserWithAuthorities();
        if(course.isPresent() && course.get().getUser().equals(user.get())){
            CourseSection courseSection = new CourseSection(courseSectionDTO);
            courseSection.course(course.get());
            courseSection.sectionOrder(
                courseSectionRepository.findCourseSectionByCourse_Id(courseId).size() + 1
            );
            courseSection.isApproved(false);
            return courseSectionRepository.save(courseSection);

        } else{
            return null;
        }
    }
}
