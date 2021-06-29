package com.codehat.charusat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CourseLevel.
 */
@Entity
@Table(name = "course_level")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    @Column(name = "level", length = 20)
    private String level;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @OneToMany(mappedBy = "courseLevel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courseLevel", "courseCategory", "user", "reviewer", "enrolledUsersLists" }, allowSetters = true)
    private Set<Course> levels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseLevel id(Long id) {
        this.id = id;
        return this;
    }

    public String getLevel() {
        return this.level;
    }

    public CourseLevel level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return this.description;
    }

    public CourseLevel description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Course> getLevels() {
        return this.levels;
    }

    public CourseLevel levels(Set<Course> courses) {
        this.setLevels(courses);
        return this;
    }

    public CourseLevel addLevel(Course course) {
        this.levels.add(course);
        course.setCourseLevel(this);
        return this;
    }

    public CourseLevel removeLevel(Course course) {
        this.levels.remove(course);
        course.setCourseLevel(null);
        return this;
    }

    public void setLevels(Set<Course> courses) {
        if (this.levels != null) {
            this.levels.forEach(i -> i.setCourseLevel(null));
        }
        if (courses != null) {
            courses.forEach(i -> i.setCourseLevel(this));
        }
        this.levels = courses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseLevel)) {
            return false;
        }
        return id != null && id.equals(((CourseLevel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseLevel{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
