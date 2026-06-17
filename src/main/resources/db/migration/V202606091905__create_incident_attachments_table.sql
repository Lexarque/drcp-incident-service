CREATE TABLE IF NOT EXISTS incident_attachments (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    incident_id     UUID            NOT NULL REFERENCES incidents(id) ON DELETE CASCADE,
    file_name       TEXT,
    file_url        TEXT            NOT NULL,
    content_type    VARCHAR(100),
    file_size       BIGINT,
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_incident_attachment_incident
    ON incident_attachments(incident_id);