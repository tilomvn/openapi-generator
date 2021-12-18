CREATE TABLE `user_assignment`
(
    `id`         VARCHAR(50) NOT NULL,
    `store_id`   VARCHAR(50) NULL,
    `user_id`    VARCHAR(50) NULL,
    `role`       INT          NOT NULL,
    `created_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    `updated_at` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT pk_user_assignment PRIMARY KEY (`id`)
);

ALTER TABLE user_assignment
    ADD CONSTRAINT FK_USER_ASSIGNMENT_ON_STORE FOREIGN KEY (`store_id`) REFERENCES store (`id`);

ALTER TABLE user_assignment
    ADD CONSTRAINT FK_USER_ASSIGNMENT_ON_USER FOREIGN KEY (`user_id`) REFERENCES user (`id`);

