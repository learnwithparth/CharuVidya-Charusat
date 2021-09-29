package com.codehat.charusat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CourseProgress.
 */
@Entity
@Table(name = "course_progress")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @NotNull
    @Column(name = "watch_seconds", nullable = false)
    private Long watchSeconds;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "courseSection" }, allowSetters = true)
    private CourseSession courseSession;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseProgress id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public CourseProgress completed(Boolean completed) {
        this.completed = completed;
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getWatchSeconds() {
        return this.watchSeconds;
    }

    public CourseProgress watchSeconds(Long watchSeconds) {
        this.watchSeconds = watchSeconds;
        return this;
    }

    public void setWatchSeconds(Long watchSeconds) {
        this.watchSeconds = watchSeconds;
    }

    public User getUser() {
        return this.user;
    }

    public CourseProgress user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CourseSession getCourseSession() {
        return this.courseSession;
    }

    public CourseProgress courseSession(CourseSession courseSession) {
        this.setCourseSession(courseSession);
        return this;
    }

    public void setCourseSession(CourseSession courseSession) {
        this.courseSession = courseSession;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseProgress)) {
            return false;
        }
        return id != null && id.equals(((CourseProgress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseProgress{" +
            "id=" + getId() +
            ", completed='" + getCompleted() + "'" +
            ", watchSeconds='" + getWatchSeconds() + "'" +
            "}";
    }
}
