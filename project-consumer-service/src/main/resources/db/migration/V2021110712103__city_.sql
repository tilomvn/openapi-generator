CREATE TABLE `city`
(
    `id`            VARCHAR(255) NOT NULL,
    `name`          VARCHAR(250) NOT NULL,
    `created_at`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_city PRIMARY KEY (`id`)
);

ALTER TABLE `city` ADD CONSTRAINT uc_name UNIQUE (`name`);

