CREATE TABLE `category`
(
    `id`            VARCHAR(255)  NOT NULL,
    `names`         VARCHAR(200)  NOT NULL,
    `code`          VARCHAR(100)  NOT NULL,
    `created_at`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at`    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_category PRIMARY KEY (`id`)
);

ALTER TABLE `category` ADD CONSTRAINT uc_category_code UNIQUE (`code`);

CREATE INDEX idx_code ON `category` (`code`);