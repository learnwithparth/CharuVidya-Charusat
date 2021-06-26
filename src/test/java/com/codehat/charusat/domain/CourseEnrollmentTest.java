package com.codehat.charusat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codehat.charusat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseEnrollmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseEnrollment.class);
        CourseEnrollment courseEnrollment1 = new CourseEnrollment();
        courseEnrollment1.setId(1L);
        CourseEnrollment courseEnrollment2 = new CourseEnrollment();
        courseEnrollment2.setId(courseEnrollment1.getId());
        assertThat(courseEnrollment1).isEqualTo(courseEnrollment2);
        courseEnrollment2.setId(2L);
        assertThat(courseEnrollment1).isNotEqualTo(courseEnrollment2);
        courseEnrollment1.setId(null);
        assertThat(courseEnrollment1).isNotEqualTo(courseEnrollment2);
    }
}
