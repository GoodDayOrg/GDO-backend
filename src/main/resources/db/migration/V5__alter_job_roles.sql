ALTER TABLE job_roles
    DROP COLUMN status,
    ADD COLUMN description TEXT,
    ADD COLUMN responsibilities TEXT,
    ADD COLUMN sharepointUrl VARCHAR(255),
    ADD COLUMN statusId INT,
    ADD COLUMN numberOfOpenPositions INT,
    ADD CONSTRAINT fk_statusId FOREIGN KEY (statusId) REFERENCES status(statusId)
;
