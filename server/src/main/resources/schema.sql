CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(64) NOT NULL,
  `email` varchar(45) NOT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);

CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`)
);
 
CREATE TABLE `user_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `user_fk_idx` (`user_id`),
  KEY `role_fk_idx` (`role_id`),
  CONSTRAINT `role_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
);

INSERT INTO `roles` (`name`) VALUES ('ROLE_USER');
INSERT INTO `roles` (`name`) VALUES ('ROLE_MODERATOR');
INSERT INTO `roles` (`name`) VALUES ('ROLE_ADMIN');

-- INSERT INTO `users` (`username`, `password`, `email`, `enabled`, `role_id`) VALUES ('user', '$2a$12$4wCH8QP5njv3Q8u.sLcgoeimJWoDZ.rFueyML75Ms5zPNVG4ige5e', 'email_1@user.com', '1', '1');
-- INSERT INTO `users` (`username`, `password`, `email`, `enabled`, `role_id`) VALUES ('moderator', '$2a$12$4wCH8QP5njv3Q8u.sLcgoeimJWoDZ.rFueyML75Ms5zPNVG4ige5e', 'email_2@moderator.com', '1', '2');
-- INSERT INTO `users` (`username`, `password`, `email`, `enabled`, `role_id`) VALUES ('admin', '$2a$12$4wCH8QP5njv3Q8u.sLcgoeimJWoDZ.rFueyML75Ms5zPNVG4ige5e', 'email_2@admin.com', '1', '3');

INSERT INTO `users` (`username`, `password`, `email`, `enabled`) VALUES ('user', '$2a$12$4wCH8QP5njv3Q8u.sLcgoeimJWoDZ.rFueyML75Ms5zPNVG4ige5e', 'email_1@user.com', '1');
INSERT INTO `users` (`username`, `password`, `email`, `enabled`) VALUES ('moderator', '$2a$12$4wCH8QP5njv3Q8u.sLcgoeimJWoDZ.rFueyML75Ms5zPNVG4ige5e', 'email_2@moderator.com', '1');
INSERT INTO `users` (`username`, `password`, `email`, `enabled`) VALUES ('admin', '$2a$12$4wCH8QP5njv3Q8u.sLcgoeimJWoDZ.rFueyML75Ms5zPNVG4ige5e', 'email_2@admin.com', '1');

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES (2, 2);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES (3, 3);

