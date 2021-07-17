package com.codehat.charusat.service.dto;

import com.codehat.charusat.domain.CourseSection;
import org.springframework.web.multipart.MultipartFile;

public class CourseSessionDTO {

    String sessionTitle;
    String sessionDescription;
    MultipartFile sessionVideo;
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

    public MultipartFile getSessionVideo() {
        return sessionVideo;
    }

    //    public void setSessionVideo(MultipartFile sessionVideo) {
    //        this.sessionVideo = sessionVideo;
    //    }

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
}
