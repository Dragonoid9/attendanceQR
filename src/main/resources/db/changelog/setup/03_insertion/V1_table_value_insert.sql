--liquibase formatted sql

-- changeset Sadip_Khadka:1
-- Insert roles into the roles table
INSERT INTO roles (name) VALUES
                             ('ROLE_SUPER ADMIN'),
                             ('Employee'),
                             ('Intern');


-- changeset Sadip_Khadka:2
-- Insert the user 'Dragon' into the users table
INSERT INTO users (username, email, password) VALUES
    ('Dragon', 'dragon@gmail.com', '$2a$10$yaF7PLpUQYxoao4XEg1tDeGlozqThffVXADANzWV/f9ZEAUn2Xo4q');



-- changeset Sadip_Khadka:3
-- Assign the 'ROLE_SUPER ADMIN' role to 'Dragon'
-- Fetch the IDs dynamically to avoid hardcoding
INSERT INTO user_role (user_id, role_id)
SELECT
    u.id AS user_id,
    r.id AS role_id
FROM
    users u,
    roles r
WHERE
    u.username = 'Dragon'
  AND r.name = 'ROLE_SUPER ADMIN';
