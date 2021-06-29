package com.codehat.charusat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codehat.charusat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseSectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseSection.class);
        CourseSection courseSection1 = new CourseSection();
        courseSection1.setId(1L);
        CourseSection courseSection2 = new CourseSection();
        courseSection2.setId(courseSection1.getId());
        assertThat(courseSection1).isEqualTo(courseSection2);
        courseSection2.setId(2L);
        assertThat(courseSection1).isNotEqualTo(courseSection2);
        courseSection1.setId(null);
        assertThat(courseSection1).isNotEqualTo(courseSection2);
    }
}
