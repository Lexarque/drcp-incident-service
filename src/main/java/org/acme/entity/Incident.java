package org.acme.entity;

import jakarta.persistence.*;
import org.acme.shared.entity.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "incidents")
public class Incident extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false, length = 20)
    private String status = "OPEN";

    @Column(name = "created_by", columnDefinition = "TEXT")
    private String createdBy;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<IncidentAttachment> attachments;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<IncidentStatusHistory> statusHistories;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public java.util.List<IncidentAttachment> getAttachments() { return attachments; }
    public void setAttachments(java.util.List<IncidentAttachment> attachments) { this.attachments = attachments; }

    public java.util.List<IncidentStatusHistory> getStatusHistories() { return statusHistories; }
    public void setStatusHistories(java.util.List<IncidentStatusHistory> statusHistories) { this.statusHistories = statusHistories; }
}