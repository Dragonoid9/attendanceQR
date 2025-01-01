--liquibase formatted sql

-- changeset Sadip_Khadka:1_add_email_column_to_users_table
-- preconditions:
--    onFail:MARK_RAN
--    onError:HALT
--    - tableExists tableName=users

ALTER TABLE users
    ADD COLUMN email VARCHAR(255) NOT NULL UNIQUE;