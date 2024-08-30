CREATE TABLE Role (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    role ENUM('ADMIN', 'USER') NOT NULL
);

INSERT INTO `Role`( role ) VALUES ('admin');
INSERT INTO `Role`( role ) VALUES ('user');