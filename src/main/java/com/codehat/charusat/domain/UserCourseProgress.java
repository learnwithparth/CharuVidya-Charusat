package com.codehat.charusat.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "user_course_progress", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "course_id" }) })
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class UserCourseProgress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Course course;

    @NotNull
    @OneToOne
    private CourseProgress courseProgress;

    public UserCourseProgress() {}

    public UserCourseProgress(User user, Course course, CourseProgress courseProgress) {
        this.user = user;
        this.course = course;
        this.courseProgress = courseProgress;
    }

    public Long getId() {
        return id;
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

    public CourseProgress getCourseProgress() {
        return courseProgress;
    }

    public void setCourseProgress(CourseProgress courseProgress) {
        this.courseProgress = courseProgress;
    }

    @Override
    public String toString() {
        return "UserCourseProgress{" + "id=" + id + ", user=" + user + ", course=" + course + ", courseProgress=" + courseProgress + '}';
    }
}
