package com.codehat.charusat.service.impl;

import static com.codehat.charusat.config.Constants.*;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.codehat.charusat.domain.Course;
import com.codehat.charusat.domain.CourseSection;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseSectionRepository;
import com.codehat.charusat.repository.CourseSessionRepository;
import com.codehat.charusat.service.CourseSessionService;
import com.codehat.charusat.service.UserService;
import com.codehat.charusat.service.dto.CourseSessionDTO;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    private final CourseSectionRepository courseSectionRepository;

    public CourseSessionServiceImpl(
        CourseSessionRepository courseSessionRepository,
        CourseServiceImpl courseService,
        UserService userService,
        CourseSectionServiceImpl courseSectionService,
        CourseSectionRepository courseSectionRepository
    ) {
        this.courseSessionRepository = courseSessionRepository;
        this.courseService = courseService;
        this.userService = userService;
        this.courseSectionService = courseSectionService;
        this.courseSectionRepository = courseSectionRepository;
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
        if (user.isPresent()) {
            if (user.get().getAuthorities().toString().contains("ROLE_ADMIN")) {
                Optional<Course> course = courseService.findOne(courseId);
                if (course.isPresent()) {
                    Optional<CourseSection> courseSection = courseSectionService.findOne(courseSectionId);
                    if (courseSection.isPresent() && courseSection.get().getCourse().equals(course.get())) {
                        return courseSessionRepository.findAllByCourseSection_IdAndIsApproved(courseSectionId, true, pageable);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else if (user.get().getAuthorities().toString().contains("ROLE_FACULTY")) {
                Optional<Course> course = courseService.findOne(courseId);
                if (course.isPresent()) {
                    if (course.get().getUser().getId().equals(user.get().getId())) {
                        Optional<CourseSection> courseSection = courseSectionService.findOne(courseSectionId);
                        if (courseSection.isPresent() && courseSection.get().getCourse().equals(course.get())) {
                            return courseSessionRepository.findAllByCourseSection_Id(courseSectionId, pageable);
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else if (user.get().getAuthorities().toString().contains("ROLE_STUDENT")) {
                Optional<Course> course = courseService.findOne(courseId);
                if (course.isPresent()) {
                    /*if (course.get().getEnrolledUsersLists().contains(user.get())) {
                        return courseSessionRepository.findAllByCourseSection_Id(courseSectionId, pageable);
                    } else {
                        return null;
                    }*/
                    Optional<CourseSection> courseSection = courseSectionService.findOne(courseSectionId);
                    if (courseSection.isPresent() && courseSection.get().getCourse().equals(course.get())) {
                        return courseSessionRepository.findAllByCourseSection_IdAndIsApproved(courseSectionId, true, pageable);
                    } else {
                        return null;
                    }
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
    public CourseSession save(Long courseId, Long courseSectionId, CourseSessionDTO courseSessionDTO) throws IOException, VideoException {
        System.out.println(courseSessionDTO.getSessionVideo());
        Optional<User> user = userService.getUserWithAuthorities();
        //        Checking if user is present
        if (user.isPresent()) {
            Optional<Course> course = courseService.findOne(courseId);
            //            Checking if course is present and the current user is the owner of that course.
            if (course.isPresent() && course.get().getUser().equals(user.get())) {
                Optional<CourseSection> courseSection = courseSectionService.findOne(courseSectionId);
                //                Checking if courseSection is present and if the courseSection is part of the course.
                if (courseSection.isPresent() && courseSection.get().getCourse().equals(course.get())) {
                    CourseSession courseSession = new CourseSession(courseSessionDTO);
                    System.out.println(courseSessionDTO.getSessionVideo());
                    //courseSession.setSessionVideo(compressAndUpload(courseSessionDTO.getSessionVideo()));
                    courseSession.setSessionVideo(courseSessionDTO.getSessionVideo());
                    courseSession.setCourseSection(courseSection.get());
                    courseSession.isApproved(false);
                    courseSession.setSessionDuration(Instant.now());
                    courseSession.setSessionLocation("");
                    courseSession.isPublished(false);
                    if (courseSessionDTO.getIsDraft() == null) {
                        courseSession.setIsDraft(false);
                    }
                    if (courseSessionDTO.getIsPreview() == null) {
                        courseSession.setIsPreview(false);
                    }
                    courseSession.sessionOrder(courseSessionRepository.findAllByCourseSection_Id(courseSectionId).size() + 1);
                    return courseSessionRepository.save(courseSession);
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

    public String compressAndUpload(MultipartFile sessionVideo) throws IOException, VideoException {
        log.info("Compression started");
        IVCompressor compressor = new IVCompressor();
        byte[] bytes = compressor.reduceVideoSize(sessionVideo.getBytes(), VideoFormats.MP4, ResizeResolution.R480P);
        log.info("Compression completed");

        log.info("Uploading the video to S3");
        String fileRandom = String.valueOf(Math.abs(new Random().nextInt()));
        System.out.println(fileRandom);
        String userLogin = userService.getUserWithAuthorities().get().getLogin();
        File file = new File(OBJECT_PATH + userLogin + "_" + fileRandom + ".mp4");
        System.out.println(file.getName());
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(bytes);
        fout.close();

        //        s3client.putObject(S3_BUCKET_NAME, userLogin + "/" + file.getName(), file);
        s3client.putObject(
            new PutObjectRequest(S3_BUCKET_NAME, userLogin + "/" + file.getName(), file).withCannedAcl(CannedAccessControlList.PublicRead)
        );
        log.info("Uploading completed");

        file.delete();
        return S3_BUCKET_LINK + userLogin + "/" + file.getName();
    }

    @Override
    public CourseSession publish(Long courseId, Long courseSectionId, Long courseSessionId, Boolean value) {
        Optional<User> user = userService.getUserWithAuthorities();
        //        Checking is user is present.
        if (user.isPresent()) {
            Optional<Course> course = courseService.findOne(courseId);
            //            Checking if course is present and the current user is the owner of that course.
            if (course.isPresent() && course.get().getUser().equals(user.get())) {
                Optional<CourseSection> courseSection = courseSectionService.findOne(courseSectionId);
                //                Checking if courseSection is present and if the courseSection is part of the course.
                if (courseSection.isPresent() && courseSection.get().getCourse().equals(course.get())) {
                    Optional<CourseSession> courseSession = findOne(courseSessionId);
                    //                    Checking is courseSession is present and if this session is of particular courseSection
                    if (courseSession.isPresent() && courseSession.get().getCourseSection().equals(courseSection.get())) {
                        courseSession.get().isPublished(value);
                        return courseSessionRepository.save(courseSession.get());
                    } else {
                        return null;
                    }
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
    public Long getVideoLength(String videoId) throws IOException {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {})
            .setApplicationName("video-test")
            .build();
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

    @Override
    public ResponseEntity<CourseSession> approveCourseSession(CourseSession courseSessionDTO) throws Exception {
        Optional<CourseSession> courseSession = courseSessionRepository.findById(courseSessionDTO.getId());
        if (courseSession.isPresent() && !courseSession.get().getIsDraft()) {
            courseSession.get().setIsApproved(true);
            courseSessionRepository.save(courseSession.get());

            Integer approvedCourseSessionCountByCourseSection = courseSessionRepository.countCourseSessionByCourseSectionAndIsApproved(
                courseSession.get().getCourseSection(),
                true
            );
            Integer totalCourseSessionCountByCourseSection = courseSessionRepository
                .findAllByCourseSection_Id(courseSession.get().getCourseSection().getId())
                .size();

            if (approvedCourseSessionCountByCourseSection.equals(totalCourseSessionCountByCourseSection)) {
                System.out.println("Sessions" + approvedCourseSessionCountByCourseSection + " " + totalCourseSessionCountByCourseSection);
                courseSectionService.approveCourseSection(courseSession.get().getCourseSection().getId());

                Integer approvedCourseSectionByCourse = courseSectionRepository.countCourseSectionByCourseAndIsApproved(
                    courseSession.get().getCourseSection().getCourse(),
                    true
                );
                Integer totalCourseSectionByCourse = courseSectionRepository
                    .findCourseSectionByCourse_Id(courseSession.get().getCourseSection().getCourse().getId())
                    .size();

                if (approvedCourseSectionByCourse.equals(totalCourseSectionByCourse)) {
                    System.out.println("Sections " + approvedCourseSectionByCourse + " " + totalCourseSectionByCourse);
                    courseService.approveCourse(courseSession.get().getCourseSection().getCourse().getId());
                }
            }
        } else {
            throw new Exception("No such course-session found");
        }
        return ResponseEntity.accepted().body(courseSession.get());
    }
}
