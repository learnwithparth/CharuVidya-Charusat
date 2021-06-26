package com.codehat.charusat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codehat.charusat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseSession.class);
        CourseSession courseSession1 = new CourseSession();
        courseSession1.setId(1L);
        CourseSession courseSession2 = new CourseSession();
        courseSession2.setId(courseSession1.getId());
        assertThat(courseSession1).isEqualTo(courseSession2);
        courseSession2.setId(2L);
        assertThat(courseSession1).isNotEqualTo(courseSession2);
        courseSession1.setId(null);
        assertThat(courseSession1).isNotEqualTo(courseSession2);
    }
}
