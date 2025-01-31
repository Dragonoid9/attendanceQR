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


