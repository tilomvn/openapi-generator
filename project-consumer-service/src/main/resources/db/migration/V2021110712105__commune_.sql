CREATE TABLE `commune`
(
    `id`          VARCHAR(50) NOT NULL,
    `name`        VARCHAR(250) NOT NULL,
    `district_id` VARCHAR(50) NULL,
    `created_at`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_commune PRIMARY KEY (`id`)
);

ALTER TABLE `commune`
    ADD CONSTRAINT FK_COMMUNE_ON_DISTRICT FOREIGN KEY (`district_id`) REFERENCES `district` (`id`);
