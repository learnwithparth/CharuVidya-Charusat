package com.codehat.charusat.service.impl;

import com.codehat.charusat.domain.CourseProgress;
import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.domain.User;
import com.codehat.charusat.repository.CourseProgressRepository;
import com.codehat.charusat.repository.CourseSessionRepository;
import com.codehat.charusat.service.CourseProgressService;
import com.codehat.charusat.service.UserService;
import io.undertow.util.BadRequestException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseProgress}.
 */
@Service
@Transactional
public class CourseProgressServiceImpl implements CourseProgressService {

    private final Logger log = LoggerFactory.getLogger(CourseProgressServiceImpl.class);

    private final CourseProgressRepository courseProgressRepository;
    private final CourseSessionRepository courseSessionRepository;
    private final UserService userService;

    public CourseProgressServiceImpl(
        CourseProgressRepository courseProgressRepository,
        UserService userService,
        CourseSessionRepository courseSessionRepository
    ) {
        this.courseProgressRepository = courseProgressRepository;
        this.userService = userService;
        this.courseSessionRepository = courseSessionRepository;
    }

    @Override
    public CourseProgress save(CourseProgress courseProgress) {
        log.debug("Request to save CourseProgress : {}", courseProgress);
        return courseProgressRepository.save(courseProgress);
    }

    @Override
    public Optional<CourseProgress> partialUpdate(CourseProgress courseProgress) {
        log.debug("Request to partially update CourseProgress : {}", courseProgress);

        return courseProgressRepository
            .findById(courseProgress.getId())
            .map(
                existingCourseProgress -> {
                    if (courseProgress.getCompleted() != null) {
                        existingCourseProgress.setCompleted(courseProgress.getCompleted());
                    }
                    if (courseProgress.getWatchSeconds() != null) {
                        existingCourseProgress.setWatchSeconds(courseProgress.getWatchSeconds());
                    }

                    return existingCourseProgress;
                }
            )
            .map(courseProgressRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseProgress> findAll(Pageable pageable) {
        log.debug("Request to get all CourseProgresses");
        return courseProgressRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseProgress> findOne(Long id) {
        log.debug("Request to get CourseProgress : {}", id);
        return courseProgressRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseProgress : {}", id);
        courseProgressRepository.deleteById(id);
    }

    @Override
    public CourseProgress getUserProgress(CourseSession courseSession) {
        User user = userService.getUserWithAuthorities().get();
        Optional<CourseProgress> courseProgress = courseProgressRepository.findByCourseSessionAndUser(courseSession, user);
        if (courseProgress.isPresent()) {
            return courseProgress.get();
        } else {
            CourseProgress newCourseProgress = new CourseProgress();
            newCourseProgress.setUser(user);
            newCourseProgress.setCourseSession(courseSession);
            newCourseProgress.setWatchSeconds(0L);
            newCourseProgress.setCompleted(false);
            return courseProgressRepository.save(newCourseProgress);
        }
    }

    @Override
    public boolean updateTime(CourseProgress courseProgress) {
        //        if(courseProgress.getId()!=null){
        courseProgressRepository.findAll().stream().peek(courseProgress1 -> System.out.println(courseProgress1.getWatchSeconds()));
        CourseSession courseSession = courseProgress.getCourseSession();
        if (!this.courseSessionRepository.findById(courseSession.getId()).isPresent()) return false; //no such session
        User user = userService.getUserWithAuthorities().get();
        Optional<CourseProgress> existing = courseProgressRepository.findByCourseSessionAndUser(courseSession, user);
        if (existing.isPresent()) {
            CourseProgress existingCourseProgress = existing.get();
            existingCourseProgress.setWatchSeconds(courseProgress.getWatchSeconds());
            courseProgressRepository.save(existingCourseProgress);
            courseProgressRepository.findAll().stream().peek(courseProgress1 -> System.out.println(courseProgress1.getWatchSeconds()));
            return true;
        } else {
            courseProgress.setUser(userService.getUserWithAuthorities().get());
            courseProgressRepository.save(courseProgress);
            courseProgressRepository.findAll().stream().peek(courseProgress1 -> System.out.println(courseProgress1.getWatchSeconds()));
            return false;
        }
    }
}
