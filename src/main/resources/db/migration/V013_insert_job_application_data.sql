INSERT INTO application_status (statusApplicationName)
VALUES
    ('in progress'),
    ('hired'),
    ('rejected');

 INSERT INTO job_application (Email, jobRoleId, statusApplicationId)
 VALUES ('admin',3,1), ('admin',5,3), ('admin',6,3);