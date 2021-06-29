package com.codehat.charusat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CourseSession.
 */
@Entity
@Table(name = "course_session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "session_title", length = 255, nullable = false)
    private String sessionTitle;

    @Size(max = 255)
    @Column(name = "session_description", length = 255)
    private String sessionDescription;

    @NotNull
    @Size(max = 300)
    @Column(name = "session_video", length = 300, nullable = false)
    private String sessionVideo;

    @NotNull
    @Column(name = "session_duration", nullable = false)
    private Instant sessionDuration;

    @NotNull
    @Column(name = "session_order", nullable = false)
    private Integer sessionOrder;

    @Size(max = 300)
    @Column(name = "session_resource", length = 300)
    private String sessionResource;

    @NotNull
    @Size(max = 300)
    @Column(name = "session_location", length = 300, nullable = false)
    private String sessionLocation;

    @NotNull
    @Column(name = "is_preview", nullable = false)
    private Boolean isPreview;

    @NotNull
    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft;

    @NotNull
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @NotNull
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished;

    @ManyToOne
    @JsonIgnoreProperties(value = { "course" }, allowSetters = true)
    private CourseSection courseSection;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseSession id(Long id) {
        this.id = id;
        return this;
    }

    public String getSessionTitle() {
        return this.sessionTitle;
    }

    public CourseSession sessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
        return this;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public String getSessionDescription() {
        return this.sessionDescription;
    }

    public CourseSession sessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
        return this;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public String getSessionVideo() {
        return this.sessionVideo;
    }

    public CourseSession sessionVideo(String sessionVideo) {
        this.sessionVideo = sessionVideo;
        return this;
    }

    public void setSessionVideo(String sessionVideo) {
        this.sessionVideo = sessionVideo;
    }

    public Instant getSessionDuration() {
        return this.sessionDuration;
    }

    public CourseSession sessionDuration(Instant sessionDuration) {
        this.sessionDuration = sessionDuration;
        return this;
    }

    public void setSessionDuration(Instant sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public Integer getSessionOrder() {
        return this.sessionOrder;
    }

    public CourseSession sessionOrder(Integer sessionOrder) {
        this.sessionOrder = sessionOrder;
        return this;
    }

    public void setSessionOrder(Integer sessionOrder) {
        this.sessionOrder = sessionOrder;
    }

    public String getSessionResource() {
        return this.sessionResource;
    }

    public CourseSession sessionResource(String sessionResource) {
        this.sessionResource = sessionResource;
        return this;
    }

    public void setSessionResource(String sessionResource) {
        this.sessionResource = sessionResource;
    }

    public String getSessionLocation() {
        return this.sessionLocation;
    }

    public CourseSession sessionLocation(String sessionLocation) {
        this.sessionLocation = sessionLocation;
        return this;
    }

    public void setSessionLocation(String sessionLocation) {
        this.sessionLocation = sessionLocation;
    }

    public Boolean getIsPreview() {
        return this.isPreview;
    }

    public CourseSession isPreview(Boolean isPreview) {
        this.isPreview = isPreview;
        return this;
    }

    public void setIsPreview(Boolean isPreview) {
        this.isPreview = isPreview;
    }

    public Boolean getIsDraft() {
        return this.isDraft;
    }

    public CourseSession isDraft(Boolean isDraft) {
        this.isDraft = isDraft;
        return this;
    }

    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getIsApproved() {
        return this.isApproved;
    }

    public CourseSession isApproved(Boolean isApproved) {
        this.isApproved = isApproved;
        return this;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Boolean getIsPublished() {
        return this.isPublished;
    }

    public CourseSession isPublished(Boolean isPublished) {
        this.isPublished = isPublished;
        return this;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public CourseSection getCourseSection() {
        return this.courseSection;
    }

    public CourseSession courseSection(CourseSection courseSection) {
        this.setCourseSection(courseSection);
        return this;
    }

    public void setCourseSection(CourseSection courseSection) {
        this.courseSection = courseSection;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseSession)) {
            return false;
        }
        return id != null && id.equals(((CourseSession) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseSession{" +
            "id=" + getId() +
            ", sessionTitle='" + getSessionTitle() + "'" +
            ", sessionDescription='" + getSessionDescription() + "'" +
            ", sessionVideo='" + getSessionVideo() + "'" +
            ", sessionDuration='" + getSessionDuration() + "'" +
            ", sessionOrder=" + getSessionOrder() +
            ", sessionResource='" + getSessionResource() + "'" +
            ", sessionLocation='" + getSessionLocation() + "'" +
            ", isPreview='" + getIsPreview() + "'" +
            ", isDraft='" + getIsDraft() + "'" +
            ", isApproved='" + getIsApproved() + "'" +
            ", isPublished='" + getIsPublished() + "'" +
            "}";
    }
}
