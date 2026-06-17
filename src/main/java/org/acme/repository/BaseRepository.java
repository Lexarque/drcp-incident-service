package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.UUID;

public class BaseRepository<T> implements PanacheRepositoryBase<T, UUID> {
}
