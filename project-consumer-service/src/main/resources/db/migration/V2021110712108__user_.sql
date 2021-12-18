CREATE TABLE `user`
(
    `id`         VARCHAR(50) NOT NULL,
    `status`     INT NOT NULL,
    `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_user PRIMARY KEY (`id`)
);

