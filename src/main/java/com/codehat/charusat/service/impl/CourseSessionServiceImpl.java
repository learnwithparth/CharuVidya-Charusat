package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseSessionRepository;
import com.codehat.charusat.service.CourseSessionService;
import com.codehat.charusat.service.UserService;
import com.codehat.charusat.service.dto.CourseSessionDTO;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.codehat.charusat.config.Constants.YOUTUBE_API_KEY;

/**
 * Service Implementation for managing {@link CourseSession}.
 */
@Service
@Transactional
public class CourseSessionServiceImpl implements CourseSessionService {

    private final Logger log = LoggerFactory.getLogger(CourseSessionServiceImpl.class);

    private final CourseSessionRepository courseSessionRepository;

    private final CourseServiceImpl courseService;

    private final UserService userService;

    private final CourseSectionServiceImpl courseSectionService;

    public CourseSessionServiceImpl(
        CourseSessionRepository courseSessionRepository,
        CourseServiceImpl courseService,
        UserService userService,
        CourseSectionServiceImpl courseSectionService
    ) {
        this.courseSessionRepository = courseSessionRepository;
        this.courseService = courseService;
        this.userService = userService;
        this.courseSectionService = courseSectionService;
    }

    @Override
    public CourseSession save(CourseSession courseSession) {
        log.debug("Request to save CourseSession : {}", courseSession);
        return courseSessionRepository.save(courseSession);
    }

    @Override
    public Optional<CourseSession> partialUpdate(CourseSession courseSession) {
        log.debug("Request to partially update CourseSession : {}", courseSession);

        return courseSessionRepository
            .findById(courseSession.getId())
            .map(
                existingCourseSession -> {
                    if (courseSession.getSessionTitle() != null) {
                        existingCourseSession.setSessionTitle(courseSession.getSessionTitle());
                    }
                    if (courseSession.getSessionDescription() != null) {
                        existingCourseSession.setSessionDescription(courseSession.getSessionDescription());
                    }
                    if (courseSession.getSessionVideo() != null) {
                        existingCourseSession.setSessionVideo(courseSession.getSessionVideo());
                    }
                    if (courseSession.getSessionDuration() != null) {
                        existingCourseSession.setSessionDuration(courseSession.getSessionDuration());
                    }
                    if (courseSession.getSessionOrder() != null) {
                        existingCourseSession.setSessionOrder(courseSession.getSessionOrder());
                    }
                    if (courseSession.getSessionResource() != null) {
                        existingCourseSession.setSessionResource(courseSession.getSessionResource());
                    }
                    if (courseSession.getSessionLocation() != null) {
                        existingCourseSession.setSessionLocation(courseSession.getSessionLocation());
                    }
                    if (courseSession.getIsPreview() != null) {
                        existingCourseSession.setIsPreview(courseSession.getIsPreview());
                    }
                    if (courseSession.getIsDraft() != null) {
                        existingCourseSession.setIsDraft(courseSession.getIsDraft());
                    }
                    if (courseSession.getIsApproved() != null) {
                        existingCourseSession.setIsApproved(courseSession.getIsApproved());
                    }
                    if (courseSession.getIsPublished() != null) {
                        existingCourseSession.setIsPublished(courseSession.getIsPublished());
                    }

                    return existingCourseSession;
                }
            )
            .map(courseSessionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseSession> findAll(Pageable pageable) {
        log.debug("Request to get all CourseSessions");
        return courseSessionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseSession> findOne(Long id) {
        log.debug("Request to get CourseSession : {}", id);
        return courseSessionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseSession : {}", id);
        courseSessionRepository.deleteById(id);
    }

    @Override
    public Page<CourseSession> findCourseSessionByCourseSection(Long courseId, Long courseSectionId, Pageable pageable) {
        log.debug("Request to get CourseSession by CourseSection: {}", courseSectionId);
        Optional<User> user = userService.getUserWithAuthorities();
        if(user.isPresent()){
            if(user.get().getAuthorities().toString().contains("ROLE_ADMIN")){
                return courseSessionRepository.findAllByCourseSection_Id(courseSectionId, pageable);
            } else if(user.get().getAuthorities().toString().contains("ROLE_FACULTY")){
                Optional<Course> course = courseService.findOne(courseId);
                if(course.isPresent()){
                    if(course.get().getUser().getId().equals(user.get().getId())){
                        return courseSessionRepository.findAllByCourseSection_Id(courseSectionId, pageable);
                    } else{
                        return null;
                    }
                } else{
                    return null;
                }
            } else if(user.get().getAuthorities().toString().contains("ROLE_STUDENT")){
                Optional<Course> course = courseService.findOne(courseId);
                if(course.isPresent()){
                    if(course.get().getEnrolledUsersLists().contains(user.get())){
                        return courseSessionRepository.findAllByCourseSection_Id(courseSectionId, pageable);
                    } else{
                        return null;
                    }
                } else{
                    return null;
                }
            } else{
                return null;
            }
        } else{
            return null;
        }
    }

    @Override
    public CourseSession save(Long courseId, Long courseSectionId, CourseSessionDTO courseSessionDTO) throws IOException {
        Optional<User> user = userService.getUserWithAuthorities();
//        Checking if user is present
        if(user.isPresent()){
            Optional<Course> course = courseService.findOne(courseId);
//            Checking if course is present and the current user is the owner of that course.
            if(course.isPresent() && course.get().getUser().equals(user.get())){
                Optional<CourseSection> courseSection = courseSectionService.findOne(courseSectionId);
//                Checking if courseSection is present and if the courseSection is part of the course.
                if(courseSection.isPresent() && courseSection.get().getCourse().equals(course.get())){
                    CourseSession courseSession = new CourseSession(courseSessionDTO);
                    courseSession.isPublished(false);
                    courseSession.sessionOrder(courseSessionRepository.findAllByCourseSection_Id(courseSectionId).size() + 1);
                    String videoId = null;
                    if(courseSession.getSessionVideo().contains("https://www.youtube.com/watch?v=")){
                        videoId = courseSession.getSessionVideo().split("https://www.youtube.com/watch\\?v=")[1];
                    } else if(courseSession.getSessionVideo().contains("https://youtu.be/")){
                        videoId = courseSession.getSessionVideo().split("https://youtu.be/")[1];
                    }
//                    courseSession.setSessionDuration(getVideoLength(videoId));
                    return courseSessionRepository.save(courseSession);
                } else{
                    return null;
                }
            } else{
                return null;
            }
        } else{
            return null;
        }
    }

    @Override
    public CourseSession publish(Long courseId, Long courseSectionId, Long courseSessionId, Boolean value) {
        Optional<User> user = userService.getUserWithAuthorities();
//        Checking is user is present.
        if(user.isPresent()){
            Optional<Course> course = courseService.findOne(courseId);
//            Checking if course is present and the current user is the owner of that course.
            if(course.isPresent() && course.get().getUser().equals(user.get())){
                Optional<CourseSection> courseSection = courseSectionService.findOne(courseSectionId);
//                Checking if courseSection is present and if the courseSection is part of the course.
                if(courseSection.isPresent() && courseSection.get().getCourse().equals(course.get())){
                    Optional<CourseSession> courseSession = findOne(courseSessionId);
//                    Checking is courseSession is present and if this session is of particular courseSection
                    if(courseSession.isPresent() && courseSession.get().getCourseSection().equals(courseSection.get())){
                        courseSession.get().isPublished(value);
                        return courseSessionRepository.save(courseSession.get());
                    } else{
                        return null;
                    }
                } else{
                    return null;
                }
            } else{
                return null;
            }
        } else{
            return null;
        }
    }

    @Override
    public Long getVideoLength(String videoId) throws IOException {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
            request -> {
            }).setApplicationName("video-test").build();
        YouTube.Videos.List videoRequest = youtube.videos().list(Collections.singletonList("snippet, statistics, contentDetails"));
        videoRequest.setId(Collections.singletonList(videoId));
        videoRequest.setKey(YOUTUBE_API_KEY);
        VideoListResponse listResponse = videoRequest.execute();
        List<Video> videoList = listResponse.getItems();
        Video targetVideo = videoList.iterator().next();
        System.out.println(targetVideo.getContentDetails().getDuration());
        String duration = targetVideo.getContentDetails().getDuration();
        return Duration.parse(duration).getSeconds();

    }
}
