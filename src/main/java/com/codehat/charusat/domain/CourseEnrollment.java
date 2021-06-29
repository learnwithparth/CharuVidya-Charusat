package com.codehat.charusat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CourseEnrollment.
 */
@Entity
@Table(name = "course_enrollment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseEnrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "enrollement_date", nullable = false)
    private LocalDate enrollmentDate;

    @NotNull
    @Column(name = "last_accessed_date", nullable = false)
    private LocalDate lastAccessedDate;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "courseLevel", "courseCategory", "user", "reviewer", "enrolledUsersLists" }, allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseEnrollment id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getEnrollmentDate() {
        return this.enrollmentDate;
    }

    public CourseEnrollment enrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
        return this;
    }

    public void setEnrollmentDate(LocalDate enrollementDate) {
        this.enrollmentDate = enrollementDate;
    }

    public LocalDate getLastAccessedDate() {
        return this.lastAccessedDate;
    }

    public CourseEnrollment lastAccessedDate(LocalDate lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
        return this;
    }

    public void setLastAccessedDate(LocalDate lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
    }

    public User getUser() {
        return this.user;
    }

    public CourseEnrollment user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return this.course;
    }

    public CourseEnrollment course(Course course) {
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
        if (!(o instanceof CourseEnrollment)) {
            return false;
        }
        return id != null && id.equals(((CourseEnrollment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseEnrollment{" +
            "id=" + getId() +
            ", enrollementDate='" + getEnrollmentDate() + "'" +
            ", lastAccessedDate='" + getLastAccessedDate() + "'" +
            "}";
    }
}
