package com.codehat.charusat.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CourseCategory.
 */
@Entity
@Table(name = "course_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "course_category_title", length = 255, nullable = false)
    private String courseCategoryTitle;

    @NotNull
    @Size(max = 255)
    @Column(name = "logo", length = 255, nullable = false)
    private String logo;

    @Column(name = "is_parent", nullable = false)
    @NotNull
    private Boolean isParent;

    @NotNull
    @Column(name = "parent_id", nullable = false)
    private Integer parentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseCategory id(Long id) {
        this.id = id;
        return this;
    }

    public String getCourseCategoryTitle() {
        return this.courseCategoryTitle;
    }

    public CourseCategory courseCategoryTitle(String courseCategoryTitle) {
        this.courseCategoryTitle = courseCategoryTitle;
        return this;
    }

    public void setCourseCategoryTitle(String courseCategoryTitle) {
        this.courseCategoryTitle = courseCategoryTitle;
    }

    public String getLogo() {
        return this.logo;
    }

    public CourseCategory logo(String logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getIsParent() {
        return this.isParent;
    }

    public CourseCategory isParent(Boolean isParent) {
        this.isParent = isParent;
        return this;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public CourseCategory parentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseCategory)) {
            return false;
        }
        return id != null && id.equals(((CourseCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseCategory{" +
            "id=" + getId() +
            ", courseCategoryTitle='" + getCourseCategoryTitle() + "'" +
            ", logo='" + getLogo() + "'" +
            ", isParent=" + getIsParent() +
            ", parentId=" + getParentId() +
            "}";
    }
}
