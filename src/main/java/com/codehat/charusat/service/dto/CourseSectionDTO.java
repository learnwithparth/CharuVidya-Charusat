package com.codehat.charusat.service.dto;

import com.codehat.charusat.domain.Course;

public class CourseSectionDTO {

    String sectionTitle;
    String sectionDescription;
    Boolean isDraft;

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSectionDescription() {
        return sectionDescription;
    }

    public void setSectionDescription(String sectionDescription) {
        this.sectionDescription = sectionDescription;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean draft) {
        isDraft = draft;
    }
}
