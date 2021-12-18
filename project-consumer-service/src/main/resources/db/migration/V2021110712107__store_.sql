CREATE TABLE `store`
(
    `id`            VARCHAR(50) NOT NULL,
    `name`          VARCHAR(250) NOT NULL,
    `code`          VARCHAR(100) NOT NULL,
    `merchant_id`   VARCHAR(50) NOT NULL,
    `business_hour` VARCHAR(1000) NOT NULL,
    `city_id`       VARCHAR(50) NULL,
    `district_id`   VARCHAR(50) NULL,
    `commune_id`    VARCHAR(50) NULL,
    `address`       VARCHAR(250) NOT NULL,
    `description`   VARCHAR(500) NULL,
    `status`        INT          NOT NULL,
    `logo`          MEDIUMBLOB   NULL,
    `gps`           VARCHAR(200) NULL,
    `imported_m1`   BIT NULL,
    `created_at`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_store PRIMARY KEY (`id`)
);

ALTER TABLE store
    ADD CONSTRAINT uc_store_code UNIQUE (`code`);

CREATE INDEX idx_name ON store (`name`);

ALTER TABLE store
    ADD CONSTRAINT FK_STORE_ON_CITY FOREIGN KEY (`city_id`) REFERENCES city (`id`);

ALTER TABLE store
    ADD CONSTRAINT FK_STORE_ON_COMMUNE FOREIGN KEY (`commune_id`) REFERENCES commune (`id`);

ALTER TABLE store
    ADD CONSTRAINT FK_STORE_ON_DISTRICT FOREIGN KEY (`district_id`) REFERENCES district (`id`);

CREATE TABLE `store_category`
(
    `store_id`    VARCHAR(50) NOT NULL,
    `category_id` VARCHAR(50) NOT NULL
);

ALTER TABLE `store_category`
    ADD CONSTRAINT FK_STORE_CATEGORY_ON_STORE FOREIGN KEY (`store_id`) REFERENCES store (`id`);
ALTER TABLE `store_category`
    ADD CONSTRAINT FK_STORE_CATEGORY_ON_CATEGORY FOREIGN KEY (`category_id`) REFERENCES `category` (`id`);
