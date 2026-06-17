CREATE TABLE IF NOT EXISTS incident_status_histories (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    incident_id     UUID            NOT NULL REFERENCES incidents(id) ON DELETE CASCADE,
    old_status      VARCHAR(20),
    new_status      VARCHAR(20)     NOT NULL,
    changed_by      VARCHAR(100),
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_incident_status_history_incident
    ON incident_status_histories(incident_id);