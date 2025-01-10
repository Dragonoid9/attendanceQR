--liquibase formatted sql


-- changeset Sadip_Khadka:1_create_role_table
-- preconditions:
--    onFail: MARK_RAN
--    - sqlCheck expectedResult=0:
--          SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'roles';
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL
);

-- changeset Sadip_Khadka:2_create_user_role_mapping_table
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