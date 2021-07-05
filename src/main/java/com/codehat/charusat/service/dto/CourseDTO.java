package com.codehat.charusat.service.dto;

import com.codehat.charusat.domain.CourseCategory;
import com.codehat.charusat.domain.CourseLevel;

public class CourseDTO {

    String courseTitle;
    String courseDescription;
    String courseObjectives;
    String courseSubTitle;
    String logo;
    CourseLevel courseLevel;
    CourseCategory courseCategory;

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
}
