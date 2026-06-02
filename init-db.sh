#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE drcp_incident_svc;
    GRANT ALL PRIVILEGES ON DATABASE drcp_incident_svc TO $POSTGRES_USER;
EOSQL