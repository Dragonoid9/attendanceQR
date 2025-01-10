--liquibase formatted sql

-- changeset Sadip_Khadka:1_create_attendance_table runAfter="1_create_user_table"
-- preconditions:
--    onFail: HALT
--    - tableExists tableName=users
--    - tableExists tableName=roles
--    - tableExists tableName=user_role
--    - tableExists tableName=auth_token
Create table attendance (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NOT NULL,  -- Assuming you want to link attendance to a user
                            checkin DATETIME NOT NULL,  -- Track check-in time
                            checkout DATETIME,         -- Track check-out time (can be NULL if not checked out yet)
                            date DATE NOT NULL,        -- Date of attendance (this is the specific day)
                            work_type VARCHAR(255) NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES users(id) -- Assuming you have a 'users' table where the 'user_id' is from
);