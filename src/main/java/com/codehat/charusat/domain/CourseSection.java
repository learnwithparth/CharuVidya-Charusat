package com.codehat.charusat.domain;

import com.codehat.charusat.service.dto.CourseSectionDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

/**
 * A CourseSection.
 */
@Entity
@Table(name = "course_section")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseSection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "section_title", length = 255, nullable = false)
    private String sectionTitle;

    @Size(max = 255)
    @Column(name = "section_description", length = 255)
    private String sectionDescription;

    @NotNull
    @Column(name = "section_order", nullable = false)
    private Integer sectionOrder;

    @NotNull
    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft;

    @NotNull
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "courseLevel", "courseCategory", "user", "reviewer", "enrolledUsersLists" }, allowSetters = true)
    private Course course;

    public CourseSection() {}

    public CourseSection(CourseSectionDTO courseSectionDTO) {
        this.sectionTitle = courseSectionDTO.getSectionTitle();
        this.sectionDescription = courseSectionDTO.getSectionDescription();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseSection id(Long id) {
        this.id = id;
        return this;
    }

    public String getSectionTitle() {
        return this.sectionTitle;
    }

    public CourseSection sectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
        return this;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSectionDescription() {
        return this.sectionDescription;
    }

    public CourseSection sectionDescription(String sectionDescription) {
        this.sectionDescription = sectionDescription;
        return this;
    }

    public void setSectionDescription(String sectionDescription) {
        this.sectionDescription = sectionDescription;
    }

    public Integer getSectionOrder() {
        return this.sectionOrder;
    }

    public CourseSection sectionOrder(Integer sectionOrder) {
        this.sectionOrder = sectionOrder;
        return this;
    }

    public void setSectionOrder(Integer sectionOrder) {
        this.sectionOrder = sectionOrder;
    }

    public Boolean getIsDraft() {
        return this.isDraft;
    }

    public CourseSection isDraft(Boolean isDraft) {
        this.isDraft = isDraft;
        return this;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getIsApproved() {
        return this.isApproved;
    }

    public CourseSection isApproved(Boolean isApproved) {
        this.isApproved = isApproved;
        return this;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Course getCourse() {
        return this.course;
    }

    public CourseSection course(Course course) {
        this.setCourse(course);
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseSection)) {
            return false;
        }
        return id != null && id.equals(((CourseSection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseSection{" +
            "id=" + getId() +
            ", sectionTitle='" + getSectionTitle() + "'" +
            ", sectionDescription='" + getSectionDescription() + "'" +
            ", sectionOrder=" + getSectionOrder() +
            ", isDraft='" + getIsDraft() + "'" +
            ", isApproved='" + getIsApproved() + "'" +
            "}";
    }
}
