--liquibase formatted sql

-- changeset Sadip_Khadka:1_create_user_table runBefore="2_create_role_table"
-- preconditions:
--    onFail: MARK_RAN
--    - sqlCheck expectedResult=0:
--          SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'users';
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255),
                       UNIQUE (username)
);

-- changeset Sadip_Khadka:2_create_role_table
-- preconditions:
--    onFail: MARK_RAN
--    - sqlCheck expectedResult=0:
--          SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'roles';
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL
);

-- changeset Sadip_Khadka:3_create_user_role_mapping_table
-- preconditions:
--    onFail: MARK_RAN
--    - sqlCheck expectedResult=0:
--          SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'user_role';
CREATE TABLE user_role (
                           user_id BIGINT NOT NULL,
                           role_id BIGINT NOT NULL,
                           PRIMARY KEY (user_id, role_id),
                           CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                           CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- changeset Sadip_Khadka:4_create_auth_token_table
-- preconditions:
--    onFail: MARK_RAN
--    - sqlCheck expectedResult=0:
--          SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'auth_token';
CREATE TABLE auth_token (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            access_token VARCHAR(255) NOT NULL UNIQUE,
                            refresh_token VARCHAR(255) NOT NULL UNIQUE,
                            issued_date TIMESTAMP NOT NULL,
                            is_active BOOLEAN NOT NULL DEFAULT FALSE,
                            user_id BIGINT NOT NULL,
                            CONSTRAINT fk_auth_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
