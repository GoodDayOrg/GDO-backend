CREATE TABLE status (
    statusId INT AUTO_INCREMENT PRIMARY KEY,
    statusName ENUM('open', 'closed') NOT NULL
);
