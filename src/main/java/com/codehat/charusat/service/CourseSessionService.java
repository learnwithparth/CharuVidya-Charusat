package com.codehat.charusat.service;

import com.codehat.charusat.domain.CourseSession;
import com.codehat.charusat.service.dto.CourseSessionDTO;
import io.github.techgnious.exception.VideoException;
import java.io.IOException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link CourseSession}.
 */
public interface CourseSessionService {
    /**
     * Save a courseSession.
     *
     * @param courseSession the entity to save.
     * @return the persisted entity.
     */
    CourseSession save(CourseSession courseSession);

    /**
     * Partially updates a courseSession.
     *
     * @param courseSession the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CourseSession> partialUpdate(CourseSession courseSession);

    /**
     * Get all the courseSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseSession> findAll(Pageable pageable);

    /**
     * Get the "id" courseSession.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseSession> findOne(Long id);

    /**
     * Delete the "id" courseSession.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Custom
     * */
    Page<CourseSession> findCourseSessionByCourseSection(Long courseId, Long courseSectionId, Pageable pageable);

    CourseSession save(Long courseId, Long courseSectionId, CourseSessionDTO courseSessionDTO) throws IOException, VideoException;

    Long getVideoLength(String videoLink) throws IOException;

    CourseSession publish(Long courseId, Long courseSectionId, Long courseSessionId, Boolean value);

    String compressAndUpload(MultipartFile file) throws Exception;
}
