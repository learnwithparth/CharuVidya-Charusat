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
    @Query(
        value = "select course_category from CourseCategory course_category where course_category.parentId in (" +
        "select course_category.parentId from CourseCategory course_category where course_category.id in (" +
        "select course.courseCategory.id from Course course" +
        ")" +
        ")"
    )
    List<CourseCategory> findParentCategory();

    @Query(value = "SELECT * from course_category where parent_id = ?1 and is_parent=false", nativeQuery = true)
    List<CourseCategory> findByParentId(Long id);

    @Query(value = "select * from course_category where id in ()", nativeQuery = true)
    List<CourseCategory> find();
}
