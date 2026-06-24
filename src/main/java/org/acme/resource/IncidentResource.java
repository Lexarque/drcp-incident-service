package org.acme.resource;

import org.acme.dto.*;
import org.acme.service.IncidentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.shared.dto.ApiResponse;

import java.util.List;

@Path("/incidents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IncidentResource {

    @Inject
    IncidentService incidentService;

    @GET
    @RolesAllowed({"RESPONDER", "ADMIN", "VICTIM"})
    public Response getAllIncidents() {
        List<IncidentDto> incidents = incidentService.getAllIncidents();
        return Response.ok()
                .entity(ApiResponse.of(200, "Incidents retrieved successfully", incidents))
                .build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"RESPONDER", "ADMIN", "VICTIM"})
    public Response getIncidentById(@PathParam("id") String id) {
        IncidentDto incident = incidentService.getIncidentById(id);
        return Response.ok()
                .entity(ApiResponse.of(200, "Incident retrieved successfully", incident))
                .build();
    }

    @POST
    @RolesAllowed("VICTIM")
    public Response createIncident(@Valid CreateIncidentDto dto) {
        IncidentDto created = incidentService.createIncident(dto);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.of(201, "Incident created successfully", created))
                .build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"RESPONDER", "ADMIN"})
    public Response updateIncident(@PathParam("id") String id, @Valid UpdateIncidentDto dto) {
        IncidentDto updated = incidentService.updateIncident(id, dto);
        return Response.ok()
                .entity(ApiResponse.of(200, "Incident updated successfully", updated))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deleteIncident(@PathParam("id") String id) {
        incidentService.deleteIncident(id);
        return Response.ok()
                .entity(ApiResponse.of(200, "Incident deleted successfully"))
                .build();
    }

    // Attachments – image attachment support
    @POST
    @Path("/{incidentId}/attachments")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("VICTIM")
    public Response addAttachment(@PathParam("incidentId") String incidentId, @Valid AttachmentDto dto) {
        AttachmentDto created = incidentService.addAttachment(incidentId, dto);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.of(201, "Attachment added successfully", created))
                .build();
    }

    // Real-time status tracking: retrieve status history
    @GET
    @Path("/{incidentId}/status-history")
    @RolesAllowed({"RESPONDER", "ADMIN"})
    public Response getStatusHistory(@PathParam("incidentId") String incidentId) {
        List<StatusHistoryDto> history = incidentService.getStatusHistory(incidentId);
        return Response.ok()
                .entity(ApiResponse.of(200, "Status history retrieved", history))
                .build();
    }
}