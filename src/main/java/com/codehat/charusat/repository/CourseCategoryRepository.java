package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseCategory;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    List<CourseCategory> findByIsParent(int i);

    @Query(value = "SELECT * from course_category where parent_id = ?1 and is_parent=0", nativeQuery = true)
    List<CourseCategory> findByParentId(Long id);
}
