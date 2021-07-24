package com.codehat.charusat.repository;

import com.codehat.charusat.domain.CourseCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
    @Query(
        value = "select course_category from CourseCategory course_category where course_category.isParent = true and course_category.parentId in (" +
        "select course_category.parentId from CourseCategory course_category where course_category.id in (" +
        "select course.courseCategory.id from Course course" +
        ")" +
        ") order by course_category.courseCategoryTitle"
    )
    List<CourseCategory> findParentCategory();

    @Query(
        value = "SELECT course_category from CourseCategory course_category where course_category.parentId = :id and course_category.id in (" +
        "select course.courseCategory.id from Course course" +
        ") order by course_category.courseCategoryTitle"
    )
    List<CourseCategory> findByParentId(@Param("id") Integer id);

    @Query(value = "select * from course_category where id in ()", nativeQuery = true)
    List<CourseCategory> find();

    @Query(value = "select category from CourseCategory category order by category.courseCategoryTitle")
    List<CourseCategory> findAllCategories();
}
