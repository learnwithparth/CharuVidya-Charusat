package com.codehat.charusat.service.dto;

import com.codehat.charusat.domain.CourseCategory;
import java.util.List;

public class CategoryReviewerDTO {

    AdminUserDTO user;
    List<CourseCategory> reviewerCategories;

    public AdminUserDTO getUser() {
        return user;
    }

    public void setUser(AdminUserDTO user) {
        this.user = user;
    }

    public List<CourseCategory> getReviewerCategories() {
        return reviewerCategories;
    }

    public void setReviewerCategories(List<CourseCategory> reviewerCategories) {
        this.reviewerCategories = reviewerCategories;
    }
}
