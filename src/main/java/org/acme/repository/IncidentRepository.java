package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.Incident;
import java.util.UUID;

@ApplicationScoped
public class IncidentRepository extends BaseRepository<Incident> {

}
