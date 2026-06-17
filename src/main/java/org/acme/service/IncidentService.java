package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.*;
import org.acme.entity.Incident;
import org.acme.entity.IncidentAttachment;
import org.acme.entity.IncidentStatusHistory;
import org.acme.repository.IncidentRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class IncidentService extends SharedService {

    @Inject
    IncidentRepository incidentRepository;

    @Inject
    JsonWebToken jwt;

    public List<IncidentDto> getAllIncidents() {
        return incidentRepository.listAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public IncidentDto getIncidentById(String id) {
        Incident incident = findIncidentOrThrow(id);
        return toDto(incident);
    }

    public List<AttachmentDto> getAttachments(String incidentId) {
        Incident incident = findIncidentOrThrow(incidentId);
        return incident.getAttachments().stream()
                .map(this::toAttachmentDto)
                .collect(Collectors.toList());
    }

    public List<StatusHistoryDto> getStatusHistory(String incidentId) {
        Incident incident = findIncidentOrThrow(incidentId);
        return incident.getStatusHistories().stream()
                .map(this::toStatusHistoryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public IncidentDto createIncident(CreateIncidentDto dto) {
        String userId = jwt.getSubject();

        Incident incident = new Incident();
        incident.setTitle(dto.title());
        incident.setDescription(dto.description());
        incident.setLatitude(dto.latitude());
        incident.setLongitude(dto.longitude());
        incident.setAddress(dto.address());
        incident.setStatus("OPEN");
        incident.setCreatedBy(userId);
        incidentRepository.persist(incident);

        addStatusHistory(incident, null, "OPEN", userId);

        return toDto(incident);
    }

    @Transactional
    public IncidentDto updateIncident(String id, UpdateIncidentDto dto) {
        Incident incident = findIncidentOrThrow(id);
        boolean statusChanged = false;
        String oldStatus = incident.getStatus();

        if (dto.title() != null) incident.setTitle(dto.title());
        if (dto.description() != null) incident.setDescription(dto.description());
        if (dto.latitude() != null) incident.setLatitude(dto.latitude());
        if (dto.longitude() != null) incident.setLongitude(dto.longitude());
        if (dto.address() != null) incident.setAddress(dto.address());
        if (dto.status() != null && !dto.status().equals(incident.getStatus())) {
            incident.setStatus(dto.status());
            statusChanged = true;
        }
        incidentRepository.persist(incident);

        if (statusChanged) {
            addStatusHistory(incident, oldStatus, dto.status(), null);
        }

        return toDto(incident);
    }

    @Transactional
    public void deleteIncident(String id) {
        Incident incident = findIncidentOrThrow(id);
        incidentRepository.delete(incident);
    }

    @Transactional
    public AttachmentDto addAttachment(String incidentId, AttachmentDto dto) {
        Incident incident = findIncidentOrThrow(incidentId);

        IncidentAttachment attachment = new IncidentAttachment();
        attachment.setIncident(incident);
        attachment.setFileName(dto.getFileName());
        attachment.setFileUrl(dto.getFileUrl());
        attachment.setContentType(dto.getContentType());
        attachment.setFileSize(dto.getFileSize());
        IncidentAttachment.persist(attachment);

        return toAttachmentDto(attachment);
    }

    // Helper methods
    private Incident findIncidentOrThrow(String id) {
        UUID uuid = UUID.fromString(id);
        Incident incident = incidentRepository.findById(uuid);
        if (incident == null) {
            throw new RuntimeException("Incident not found"); // better: custom exception + mapper
        }
        return incident;
    }

    private void addStatusHistory(Incident incident, String oldStatus, String newStatus, String changedBy) {
        IncidentStatusHistory history = new IncidentStatusHistory();
        history.setIncident(incident);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy(changedBy);
        IncidentStatusHistory.persist(history); // using Panache static method
    }

    // DTO mappers
    private IncidentDto toDto(Incident i) {
        IncidentDto dto = new IncidentDto();
        dto.setId(i.getId().toString());
        dto.setTitle(i.getTitle());
        dto.setDescription(i.getDescription());
        dto.setLatitude(i.getLatitude());
        dto.setLongitude(i.getLongitude());
        dto.setAddress(i.getAddress());
        dto.setStatus(i.getStatus());
        List<AttachmentDto> attachmentDtos = IncidentAttachment
                .find("incident.id", i.getId())
                .list()
                .stream()
                .map(a -> toAttachmentDto((IncidentAttachment) a))
                .toList();
        dto.setAttachments(attachmentDtos);
        dto.setCreatedBy(i.getCreatedBy());
        dto.setCreatedAt(i.getCreatedAt());
        dto.setUpdatedAt(i.getUpdatedAt());
        return dto;
    }

    private AttachmentDto toAttachmentDto(IncidentAttachment a) {
        AttachmentDto dto = new AttachmentDto();
        dto.setId(a.getId().toString());
        dto.setFileName(a.getFileName());
        dto.setFileUrl(a.getFileUrl());
        dto.setContentType(a.getContentType());
        dto.setFileSize(a.getFileSize());
        dto.setUploadedAt(a.getCreatedAt());
        return dto;
    }

    private StatusHistoryDto toStatusHistoryDto(IncidentStatusHistory h) {
        StatusHistoryDto dto = new StatusHistoryDto();
        dto.setId(h.getId().toString());
        dto.setOldStatus(h.getOldStatus());
        dto.setNewStatus(h.getNewStatus());
        dto.setChangedBy(h.getChangedBy());
        dto.setChangedAt(h.getUpdatedAt());
        return dto;
    }
}