package com.codehat.charusat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codehat.charusat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseProgressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseProgress.class);
        CourseProgress courseProgress1 = new CourseProgress();
        courseProgress1.setId(1L);
        CourseProgress courseProgress2 = new CourseProgress();
        courseProgress2.setId(courseProgress1.getId());
        assertThat(courseProgress1).isEqualTo(courseProgress2);
        courseProgress2.setId(2L);
        assertThat(courseProgress1).isNotEqualTo(courseProgress2);
        courseProgress1.setId(null);
        assertThat(courseProgress1).isNotEqualTo(courseProgress2);
    }
}
