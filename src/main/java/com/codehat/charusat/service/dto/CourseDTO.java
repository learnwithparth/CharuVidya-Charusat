package com.codehat.charusat.service.dto;

import com.codehat.charusat.domain.*;
import java.time.LocalDate;
import java.util.Random;

public class CourseDTO {

    Long id;
    String courseTitle;
    String courseDescription;
    String courseObjectives;
    String courseSubTitle;
    String previewVideourl;
    Integer courseLength;
    String courseRootDir;
    Boolean isDraft;
    Boolean isApproved;
    Double amount;
    LocalDate courseApprovalDate;
    String logo;
    CourseLevel courseLevel;
    CourseCategory courseCategory;
    User user;
    User reviewer;
    Boolean enrolled;
    LocalDate courseCreatedOn;
    LocalDate courseUpdatedOn;
    Integer minStudents;
    Integer maxStudents;
    CourseReviewStatus courseReviewStatus;

    public CourseDTO() {}

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.courseTitle = course.getCourseTitle();
        this.courseDescription = course.getCourseDescription();
        this.courseObjectives = course.getCourseObjectives();
        this.courseTitle = course.getCourseSubTitle();
        this.logo = course.getLogo();
        this.courseLevel = course.getCourseLevel();
        this.courseCategory = course.getCourseCategory();
        this.courseCreatedOn = course.getCourseCreatedOn();
        this.courseUpdatedOn = course.getCourseUpdatedOn();
        this.minStudents = course.getMinStudents();
        this.maxStudents = course.getMaxStudents();
        this.previewVideourl = course.getPreviewVideourl();
        this.user = course.getUser();
        this.reviewer = course.getReviewer();
        this.courseLength = course.getCourseLength();
        this.courseRootDir = course.getCourseRootDir();
        this.courseApprovalDate = course.getCourseApprovalDate();
        this.amount = course.getAmount();
        this.isApproved = course.getIsApproved();
        this.isDraft = course.getIsDraft();
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseObjectives() {
        return courseObjectives;
    }

    public void setCourseObjectives(String courseObjectives) {
        this.courseObjectives = courseObjectives;
    }

    public String getCourseSubTitle() {
        return courseSubTitle;
    }

    public void setCourseSubTitle(String courseSubTitle) {
        this.courseSubTitle = courseSubTitle;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }

    public CourseCategory getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategory courseCategory) {
        this.courseCategory = courseCategory;
    }

    public Boolean getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Boolean enrolled) {
        this.enrolled = enrolled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCourseCreatedOn() {
        return courseCreatedOn;
    }

    public void setCourseCreatedOn(LocalDate courseCreatedOn) {
        this.courseCreatedOn = courseCreatedOn;
    }

    public LocalDate getCourseUpdatedOn() {
        return courseUpdatedOn;
    }

    public void setCourseUpdatedOn(LocalDate courseUpdatedOn) {
        this.courseUpdatedOn = courseUpdatedOn;
    }

    public Integer getMinStudents() {
        return minStudents;
    }

    public void setMinStudents(Integer minStudents) {
        this.minStudents = minStudents;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public String getPreviewVideourl() {
        return previewVideourl;
    }

    public void setPreviewVideourl(String previewVideourl) {
        this.previewVideourl = previewVideourl;
    }

    public Integer getCourseLength() {
        return courseLength;
    }

    public void setCourseLength(Integer courseLength) {
        this.courseLength = courseLength;
    }

    public String getCourseRootDir() {
        return courseRootDir;
    }

    public void setCourseRootDir(String courseRootDir) {
        this.courseRootDir = courseRootDir;
    }

    public Boolean getDraft() {
        return isDraft;
    }

    public void setDraft(Boolean draft) {
        isDraft = draft;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getCourseApprovalDate() {
        return courseApprovalDate;
    }

    public void setCourseApprovalDate(LocalDate courseApprovalDate) {
        this.courseApprovalDate = courseApprovalDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public CourseReviewStatus getCourseReviewStatus() {
        return courseReviewStatus;
    }

    public void setCourseReviewStatus(CourseReviewStatus courseReviewStatus) {
        this.courseReviewStatus = courseReviewStatus;
    }
}
