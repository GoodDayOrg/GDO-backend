CREATE TABLE `User` (
    Email varchar(64) NOT NULL,
    Password varchar(64) NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (Email),
    FOREIGN KEY (role_id) REFERENCES `Role`(id)
);

INSERT INTO User( Email, Password, role_id) VALUES ('admin@example.com', '$2a$10$M87XjY69RKVJ8w135phR8u.ERXInZhxfDT9OOIfa7nNQB/r5tXLKW', 1);
INSERT INTO User( Email, Password, role_id) VALUES ('user@example.com', '$2a$10$ZE5vKwdg23oTWcSLDaCKHuk8BnJ3MzHhBdmUcEvBVgP1z7KuiqB1q', 2);