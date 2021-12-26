package com.codehat.charusat.domain;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.*;

@Entity
public class CourseVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Course course;

    private Instant lastVisitedDate;

    private Long courseVisitedCount;

    public CourseVisit() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Instant getLastVisitedDate() {
        return lastVisitedDate;
    }

    public void setLastVisitedDate(Instant lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }

    public Long getCourseVisitedCount() {
        return courseVisitedCount;
    }

    public void setCourseVisitedCount(Long courseVisitedCount) {
        this.courseVisitedCount = courseVisitedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseVisit)) return false;
        CourseVisit that = (CourseVisit) o;
        return (
            getId().equals(that.getId()) &&
            getUser().equals(that.getUser()) &&
            getCourse().equals(that.getCourse()) &&
            getLastVisitedDate().equals(that.getLastVisitedDate()) &&
            getCourseVisitedCount().equals(that.getCourseVisitedCount())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getCourse(), getLastVisitedDate(), getCourseVisitedCount());
    }
}
