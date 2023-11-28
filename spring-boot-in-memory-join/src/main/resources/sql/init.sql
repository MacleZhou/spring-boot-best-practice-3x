CREATE DATABASE IF NOT EXISTS `spring6-study`;

USE `spring6-study`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
                         `id` int(11) NOT NULL,
                         `name` varchar(45) DEFAULT NULL,
                         `create_date` datetime DEFAULT NULL,
                         PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
                              `id` int(11) NOT NULL,
                              `userId` int(11) DEFAULT NULL,
                              `userId` int(11) DEFAULT NULL,
                              `name` varchar(45) DEFAULT NULL,
                              `create_date` datetime DEFAULT NULL,
                              PRIMARY KEY (`id`)
);

