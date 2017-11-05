DECLARE @ROLE_ID BIGINT
INSERT INTO roles (description, name) VALUES ('Admin', 'ROLE_ADMIN');
INSERT INTO roles (description, name) VALUES ('User', 'ROLE_USER');
INSERT INTO roles (description, name) VALUES ('Guest', 'ROLE_Guest');

SELECT @ROLE_ID = id
FROM roles
WHERE name = 'ROLE_ADMIN';

INSERT INTO users (email, first_name, last_name, nickname, password, application_user_role_id) VALUES ('admin@test.com', 'Tom', 'Parris', 'Captain Atom', '$2a$04$bKiIraV96Ptqadq5mvjQxuwZoqOCXIwUbhMPUn3R/yN7OePNxDjV.', @ROLE_ID);

SELECT @ROLE_ID = id
FROM roles
WHERE name = 'ROLE_USER';

INSERT INTO users (email, first_name, last_name, nickname, password, application_user_role_id) VALUES ('user@test.com', 'B Elanna', 'Torres', 'Bell', '$2a$04$bKiIraV96Ptqadq5mvjQxuwZoqOCXIwUbhMPUn3R/yN7OePNxDjV.', @ROLE_ID);