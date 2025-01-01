--liquibase formatted sql

-- changeset Sadip_Khadka:1_create_user_table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255),
                       UNIQUE (username)
);

-- changeset Sadip_Khadka:2_create_role_table
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL
);

-- changeset Sadip_Khadka:3_create_user_role_mapping_table
CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role_id BIGINT NOT NULL,
                           PRIMARY KEY (user_id, role_id),
                           CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                           CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- changeset Sadip_Khadka:3_create_auth_token_table
CREATE TABLE auth_token (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            token VARCHAR(255) NOT NULL UNIQUE,
                            expiry_date TIMESTAMP NOT NULL,
                            user_id BIGINT NOT NULL,
                            CONSTRAINT fk_auth_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);