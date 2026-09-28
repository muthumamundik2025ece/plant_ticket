CREATE TABLE regions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_regions PRIMARY KEY (id),
    CONSTRAINT uk_regions_code UNIQUE (code),
    CONSTRAINT uk_regions_name UNIQUE (name)
);

CREATE TABLE farmers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone VARCHAR(30),
    region_id BIGINT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_farmers PRIMARY KEY (id),
    CONSTRAINT uk_farmers_email UNIQUE (email),
    CONSTRAINT fk_farmers_region FOREIGN KEY (region_id) REFERENCES regions (id)
);
CREATE INDEX idx_farmers_region_id ON farmers (region_id);

CREATE TABLE officers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employee_code VARCHAR(40) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone VARCHAR(30),
    region_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_officers PRIMARY KEY (id),
    CONSTRAINT uk_officers_email UNIQUE (email),
    CONSTRAINT uk_officers_employee_code UNIQUE (employee_code),
    CONSTRAINT fk_officers_region FOREIGN KEY (region_id) REFERENCES regions (id)
);
CREATE INDEX idx_officers_region_id ON officers (region_id);

CREATE TABLE crops (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    variety VARCHAR(100),
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_crops PRIMARY KEY (id),
    CONSTRAINT uk_crops_name_variety UNIQUE (name, variety)
);

CREATE TABLE tickets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_number VARCHAR(36) NOT NULL,
    farmer_id BIGINT NOT NULL,
    crop_id BIGINT NOT NULL,
    region_id BIGINT NOT NULL,
    symptoms VARCHAR(1000) NOT NULL,
    description VARCHAR(5000) NOT NULL,
    photo_reference VARCHAR(2048),
    assigned_officer_id BIGINT,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    resolved_at TIMESTAMP(6),
    resolution_time_minutes BIGINT,
    escalation_level INTEGER NOT NULL DEFAULT 0,
    escalated_at TIMESTAMP(6),
    escalation_reason VARCHAR(1000),
    CONSTRAINT pk_tickets PRIMARY KEY (id),
    CONSTRAINT uk_tickets_ticket_number UNIQUE (ticket_number),
    CONSTRAINT fk_tickets_farmer FOREIGN KEY (farmer_id) REFERENCES farmers (id),
    CONSTRAINT fk_tickets_crop FOREIGN KEY (crop_id) REFERENCES crops (id),
    CONSTRAINT fk_tickets_region FOREIGN KEY (region_id) REFERENCES regions (id),
    CONSTRAINT fk_tickets_officer FOREIGN KEY (assigned_officer_id) REFERENCES officers (id),
    CONSTRAINT chk_tickets_resolution_time CHECK (resolution_time_minutes IS NULL OR resolution_time_minutes >= 0),
    CONSTRAINT chk_tickets_escalation_level CHECK (escalation_level >= 0)
);
CREATE INDEX idx_tickets_farmer_id ON tickets (farmer_id);
CREATE INDEX idx_tickets_crop_id ON tickets (crop_id);
CREATE INDEX idx_tickets_region_status ON tickets (region_id, status);
CREATE INDEX idx_tickets_officer_status ON tickets (assigned_officer_id, status);
CREATE INDEX idx_tickets_created_at ON tickets (created_at);
CREATE INDEX idx_tickets_escalated_at ON tickets (escalated_at);

CREATE TABLE ticket_responses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    farmer_id BIGINT,
    officer_id BIGINT,
    message VARCHAR(5000) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_ticket_responses PRIMARY KEY (id),
    CONSTRAINT fk_responses_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id),
    CONSTRAINT fk_responses_farmer FOREIGN KEY (farmer_id) REFERENCES farmers (id),
    CONSTRAINT fk_responses_officer FOREIGN KEY (officer_id) REFERENCES officers (id),
    CONSTRAINT chk_responses_single_author CHECK ((farmer_id IS NOT NULL AND officer_id IS NULL) OR (farmer_id IS NULL AND officer_id IS NOT NULL))
);
CREATE INDEX idx_responses_ticket_created ON ticket_responses (ticket_id, created_at);

CREATE TABLE ticket_status_history (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    previous_status VARCHAR(32),
    new_status VARCHAR(32) NOT NULL,
    changed_by_farmer_id BIGINT,
    changed_by_officer_id BIGINT,
    note VARCHAR(1000),
    changed_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_ticket_status_history PRIMARY KEY (id),
    CONSTRAINT fk_status_history_ticket FOREIGN KEY (ticket_id) REFERENCES tickets (id),
    CONSTRAINT fk_status_history_farmer FOREIGN KEY (changed_by_farmer_id) REFERENCES farmers (id),
    CONSTRAINT fk_status_history_officer FOREIGN KEY (changed_by_officer_id) REFERENCES officers (id),
    CONSTRAINT chk_status_history_single_actor CHECK (changed_by_farmer_id IS NULL OR changed_by_officer_id IS NULL)
);
CREATE INDEX idx_status_history_ticket_changed ON ticket_status_history (ticket_id, changed_at);