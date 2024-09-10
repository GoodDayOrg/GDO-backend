INSERT INTO application_status (statusApplicationName)
VALUES
    ('in progress'),
    ('hired'),
    ('rejected');

 INSERT INTO job_application (Email, jobRoleId, statusApplicationId)
 VALUES ('admin@example.com',3,1), ('admin@example.com',5,3), ('admin@example.com',6,3);