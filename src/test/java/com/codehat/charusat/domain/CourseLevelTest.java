package com.codehat.charusat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codehat.charusat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseLevel.class);
        CourseLevel courseLevel1 = new CourseLevel();
        courseLevel1.setId(1L);
        CourseLevel courseLevel2 = new CourseLevel();
        courseLevel2.setId(courseLevel1.getId());
        assertThat(courseLevel1).isEqualTo(courseLevel2);
        courseLevel2.setId(2L);
        assertThat(courseLevel1).isNotEqualTo(courseLevel2);
        courseLevel1.setId(null);
        assertThat(courseLevel1).isNotEqualTo(courseLevel2);
    }
}
