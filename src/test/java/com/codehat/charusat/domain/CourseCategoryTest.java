package com.codehat.charusat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codehat.charusat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseCategory.class);
        CourseCategory courseCategory1 = new CourseCategory();
        courseCategory1.setId(1L);
        CourseCategory courseCategory2 = new CourseCategory();
        courseCategory2.setId(courseCategory1.getId());
        assertThat(courseCategory1).isEqualTo(courseCategory2);
        courseCategory2.setId(2L);
        assertThat(courseCategory1).isNotEqualTo(courseCategory2);
        courseCategory1.setId(null);
        assertThat(courseCategory1).isNotEqualTo(courseCategory2);
    }
}
