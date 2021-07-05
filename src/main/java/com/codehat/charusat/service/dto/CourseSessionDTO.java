package com.codehat.charusat.service.dto;

import com.codehat.charusat.domain.CourseSection;

public class CourseSessionDTO {

    String sessionTitle;
    String sessionDescription;
    String sessionVideo;
    String sessionResource;
    Boolean isPreview;
    Boolean isDraft;

    //CourseSection courseSection;

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public String getSessionVideo() {
        return sessionVideo;
    }

    public void setSessionVideo(String sessionVideo) {
        this.sessionVideo = sessionVideo;
    }

    public String getSessionResource() {
        return sessionResource;
    }

    public void setSessionResource(String sessionResource) {
        this.sessionResource = sessionResource;
    }

    public Boolean getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(Boolean preview) {
        isPreview = preview;
    }

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean draft) {
        isDraft = draft;
    }
    //    public CourseSection getCourseSection() {
    //        return courseSection;
    //    }

    //    public void setCourseSection(CourseSection courseSection) {
    //        this.courseSection = courseSection;
    //    }
}
