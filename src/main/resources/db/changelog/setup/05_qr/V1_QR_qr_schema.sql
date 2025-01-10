--liquibase formatted sql

-- changeset Sadip_Khadka:1_qr_schema

Create TABLE qr (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                token Varchar(255) not null ,
                date DATE NOT NULL,
                qr_url TEXT
);