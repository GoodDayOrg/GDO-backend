CREATE TABLE job_application (
    Email VARCHAR(64) NOT NULL,
    jobRoleId INT NOT NULL,
    statusApplicationId INT,
    PRIMARY KEY (Email, jobRoleId),
    FOREIGN KEY (statusApplicationId) REFERENCES application_status(statusApplicationId)
);