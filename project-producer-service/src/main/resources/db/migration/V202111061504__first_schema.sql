CREATE TABLE `profile`
(
    `id`              VARCHAR(40)  NOT NULL,
    `user_cif`        VARCHAR(50)  NULL,
    `full_name`       VARCHAR(100)  NOT NULL,
    `phone_number`    VARCHAR(20)   NULL,
    `date_of_birth`   date          NULL,
    `national_number` VARCHAR(50)  NOT NULL,
    `email`           VARCHAR(100)  NULL,
    `profile_picture` TEXT          NULL,
    `created_at`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at`      datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_profile PRIMARY KEY (`id`)
);

CREATE TABLE `merchant`
(
    `id`       VARCHAR(40) NOT NULL,
    `logo`     TEXT         NULL,
    `web_site` VARCHAR(255) NULL,
    `slogan`   VARCHAR(255) NULL,
    CONSTRAINT pk_merchant PRIMARY KEY (`id`)
);

CREATE TABLE `user`
(
    `id`         VARCHAR(255) NOT NULL,
    `username`   VARCHAR(100)  NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `status`     INT          NOT NULL,
    `profile_id` VARCHAR(50)  NULL,
    `merchant_id` VARCHAR(50)  NULL,
    `role`       INT          NOT NULL,
    `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_user PRIMARY KEY (`id`)
);

ALTER TABLE `user`
    ADD CONSTRAINT uc_user_username UNIQUE (`username`);

CREATE INDEX username_index ON `user` (`username`);

ALTER TABLE `user`
    ADD CONSTRAINT FK_USER_ON_PROFILE FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`);

ALTER TABLE `profile`
    ADD CONSTRAINT uc_profile_national_number UNIQUE (`national_number`);


ALTER TABLE `user`
    ADD CONSTRAINT FK_USER_ON_MERCHANT FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`id`);

INSERT INTO `user` (`id`, `username`, `password`, `status`, `role`, `created_at`, `updated_at`)
VALUE ('0e7a462b-301f-4b8d-89af-9ddf4c6892bc', 'admin', '$2a$10$/d9EkiIE9ku.MoDh21sdY.Eks5izJ.0K02SjzhQUCr92ngFTOpNDq', 0, 1, '2021-12-06 13:26:12', '2021-12-06 13:26:12')