package com.codehat.charusat.domain;

import com.codehat.charusat.service.dto.CourseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 600)
    @Column(name = "course_title", length = 600, nullable = false)
    private String courseTitle;

    @NotNull
    @Size(max = 255)
    @Column(name = "course_description", length = 255, nullable = false)
    private String courseDescription;

    @Size(max = 255)
    @Column(name = "course_objectives", length = 255)
    private String courseObjectives;

    @NotNull
    @Size(max = 120)
    @Column(name = "course_sub_title", length = 120, nullable = false)
    private String courseSubTitle;

    @NotNull
    @Size(max = 255)
    @Column(name = "preview_videourl", length = 255, nullable = false)
    private String previewVideourl;

    @Column(name = "course_length")
    private Integer courseLength;

    @NotNull
    @Size(max = 255)
    @Column(name = "logo", length = 255, nullable = false)
    private String logo;

    @NotNull
    @Column(name = "course_created_on", nullable = false)
    private LocalDate courseCreatedOn;

    @NotNull
    @Column(name = "course_updated_on", nullable = false)
    private LocalDate courseUpdatedOn;

    @Size(max = 255)
    @Column(name = "course_root_dir", length = 255)
    private String courseRootDir;

    @Column(name = "amount")
    private Double amount;

    @NotNull
    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft;

    @NotNull
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @Column(name = "course_approval_date")
    private LocalDate courseApprovalDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "levels" }, allowSetters = true)
    private CourseLevel courseLevel;

    @ManyToOne
    private CourseCategory courseCategory;

    @ManyToOne
    private User user;

    @ManyToOne
    private User reviewer;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_course__enrolled_users_list",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "enrolled_users_list_id")
    )
    private Set<User> enrolledUsersLists = new HashSet<>();

    public Course(){

    }

    public Course(CourseDTO courseDTO){
        this.courseTitle = courseDTO.getCourseTitle();
        this.courseDescription = courseDTO.getCourseDescription();
        this.courseObjectives = courseDTO.getCourseObjectives();
        this.courseSubTitle = courseDTO.getCourseSubTitle();
        this.previewVideourl = courseDTO.getPreviewVideourl();
        this.logo = courseDTO.getLogo();
        this.courseRootDir = null;
        this.isDraft = courseDTO.getIsDraft();
        System.out.println(courseDTO.getIsDraft() + " " + this.isDraft);
        this.courseLevel = courseDTO.getCourseLevel();
        this.courseCategory = courseDTO.getCourseCategory();
    }


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course id(Long id) {
        this.id = id;
        return this;
    }

    public String getCourseTitle() {
        return this.courseTitle;
    }

    public Course courseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
        return this;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return this.courseDescription;
    }

    public Course courseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
        return this;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseObjectives() {
        return this.courseObjectives;
    }

    public Course courseObjectives(String courseObjectives) {
        this.courseObjectives = courseObjectives;
        return this;
    }

    public void setCourseObjectives(String courseObjectives) {
        this.courseObjectives = courseObjectives;
    }

    public String getCourseSubTitle() {
        return this.courseSubTitle;
    }

    public Course courseSubTitle(String courseSubTitle) {
        this.courseSubTitle = courseSubTitle;
        return this;
    }

    public void setCourseSubTitle(String courseSubTitle) {
        this.courseSubTitle = courseSubTitle;
    }

    public String getPreviewVideourl() {
        return this.previewVideourl;
    }

    public Course previewVideourl(String previewVideourl) {
        this.previewVideourl = previewVideourl;
        return this;
    }

    public void setPreviewVideourl(String previewVideourl) {
        this.previewVideourl = previewVideourl;
    }

    public Integer getCourseLength() {
        return this.courseLength;
    }

    public Course courseLength(Integer courseLength) {
        this.courseLength = courseLength;
        return this;
    }

    public void setCourseLength(Integer courseLength) {
        this.courseLength = courseLength;
    }

    public String getLogo() {
        return this.logo;
    }

    public Course logo(String logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public LocalDate getCourseCreatedOn() {
        return this.courseCreatedOn;
    }

    public Course courseCreatedOn(LocalDate courseCreatedOn) {
        this.courseCreatedOn = courseCreatedOn;
        return this;
    }

    public void setCourseCreatedOn(LocalDate courseCreatedOn) {
        this.courseCreatedOn = courseCreatedOn;
    }

    public LocalDate getCourseUpdatedOn() {
        return this.courseUpdatedOn;
    }

    public Course courseUpdatedOn(LocalDate courseUpdatedOn) {
        this.courseUpdatedOn = courseUpdatedOn;
        return this;
    }

    public void setCourseUpdatedOn(LocalDate courseUpdatedOn) {
        this.courseUpdatedOn = courseUpdatedOn;
    }

    public String getCourseRootDir() {
        return this.courseRootDir;
    }

    public Course courseRootDir(String courseRootDir) {
        this.courseRootDir = courseRootDir;
        return this;
    }

    public void setCourseRootDir(String courseRootDir) {
        this.courseRootDir = courseRootDir;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Course amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getIsDraft() {
        return this.isDraft;
    }

    public Course isDraft(Boolean isDraft) {
        this.isDraft = isDraft;
        return this;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getIsApproved() {
        return this.isApproved;
    }

    public Course isApproved(Boolean isApproved) {
        this.isApproved = isApproved;
        return this;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public LocalDate getCourseApprovalDate() {
        return this.courseApprovalDate;
    }

    public Course courseApprovalDate(LocalDate courseApprovalDate) {
        this.courseApprovalDate = courseApprovalDate;
        return this;
    }

    public void setCourseApprovalDate(LocalDate courseApprovalDate) {
        this.courseApprovalDate = courseApprovalDate;
    }

    public CourseLevel getCourseLevel() {
        return this.courseLevel;
    }

    public Course courseLevel(CourseLevel courseLevel) {
        this.setCourseLevel(courseLevel);
        return this;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }

    public CourseCategory getCourseCategory() {
        return this.courseCategory;
    }

    public Course courseCategory(CourseCategory courseCategory) {
        this.setCourseCategory(courseCategory);
        return this;
    }

    public void setCourseCategory(CourseCategory courseCategory) {
        this.courseCategory = courseCategory;
    }

    public User getUser() {
        return this.user;
    }

    public Course user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getReviewer() {
        return this.reviewer;
    }

    public Course reviewer(User user) {
        this.setReviewer(user);
        return this;
    }

    public void setReviewer(User user) {
        this.reviewer = user;
    }

    public Set<User> getEnrolledUsersLists() {
        return enrolledUsersLists;
    }

    public void setEnrolledUsersLists(Set<User> enrolledUsersLists) {
        this.enrolledUsersLists = enrolledUsersLists;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "Course{" +
            "id=" + id +
            ", courseTitle='" + courseTitle + '\'' +
            ", courseDescription='" + courseDescription + '\'' +
            ", courseObjectives='" + courseObjectives + '\'' +
            ", courseSubTitle='" + courseSubTitle + '\'' +
            ", previewVideourl='" + previewVideourl + '\'' +
            ", courseLength=" + courseLength +
            ", logo='" + logo + '\'' +
            ", courseCreatedOn=" + courseCreatedOn +
            ", courseUpdatedOn=" + courseUpdatedOn +
            ", courseRootDir='" + courseRootDir + '\'' +
            ", amount=" + amount +
            ", isDraft=" + isDraft +
            ", isApproved=" + isApproved +
            ", courseApprovalDate=" + courseApprovalDate +
            ", courseLevel=" + courseLevel +
            ", courseCategory=" + courseCategory +
            ", user=" + user +
            ", reviewer=" + reviewer +
            '}';
    }
}
