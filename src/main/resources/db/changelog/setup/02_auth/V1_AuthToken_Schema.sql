--liquibase formatted sql

-- changeset Sadip_Khadka:1_create_auth_token_table
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