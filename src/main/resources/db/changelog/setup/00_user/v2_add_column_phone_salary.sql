--liquibase formatted sql

-- changeset Sadip_Khadka:2_add_columns_to_user_table
-- preconditions:
--    onFail: MARK_RAN
--    - sqlCheck expectedResult=1:
--          SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'users';

ALTER TABLE users
                    ADD COLUMN phone_number VARCHAR (255) NOT NULL,
                    ADD COLUMN address VARCHAR (255) NOT NULL,
                    ADD COLUMN salary DOUBLE NOT NULL,
                    ADD COLUMN department VARCHAR (255) NOT NULL,
                    ADD COLUMN date_of_birth DATE,
                    ADD COLUMN hire_date DATE,
                    ADD COLUMN status VARCHAR (255),
                    ADD COLUMN profile_picture_url VARCHAR (255);
