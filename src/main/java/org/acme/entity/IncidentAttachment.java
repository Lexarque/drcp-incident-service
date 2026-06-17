package org.acme.entity;

import jakarta.persistence.*;
import org.acme.shared.entity.BaseEntity;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "incident_attachments")
public class IncidentAttachment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    @Column(name = "file_name", columnDefinition = "TEXT")
    private String fileName;

    @Column(name = "file_url", columnDefinition = "TEXT", nullable = false)
    private String fileUrl;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    public Incident getIncident() { return incident; }
    public void setIncident(Incident incident) { this.incident = incident; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
}