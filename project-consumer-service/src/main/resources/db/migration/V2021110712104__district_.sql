CREATE TABLE `district`
(
    `id`         VARCHAR(50) NOT NULL,
    `name`       VARCHAR(250) NOT NULL,
    `city_id`    VARCHAR(50) NULL,
    `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_district PRIMARY KEY (`id`)
);

ALTER TABLE `district`
    ADD CONSTRAINT FK_DISTRICT_ON_CITY FOREIGN KEY (`city_id`) REFERENCES `city` (`id`);

