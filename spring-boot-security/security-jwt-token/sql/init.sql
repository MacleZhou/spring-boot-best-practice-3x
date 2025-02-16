CREATE TABLE `sys_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键'，
    `user_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '用户名',
    `nick_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
    `password`  VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '密码',
    `status`    CHAR(1) DEFAULT '0' COMMENT '账号状态，0-正常，1-停用',
    `email`     VARCHAR(64) DEFAULT NULL COMMENT '邮箱',
    `phonenumber` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
    `sex`       CHAR(1) DEFAULT NULL COMMENT '性别，0-Male，1-Female, 2-Unknown',
    `avatar`     VARCHAR(128) DEFAULT NULL COMMENT '头像',
    `user_type`       CHAR(1) NOT NULL DEFAULT '1' COMMENT '用户类型，0-Admin，1-Normal user',
    `create_by`     BIGINT(20) DEFAULT NULl COMMENT 'create',
    `update_by`     BIGINT(20) DEFAULT NULl COMMENT 'create',
    `create_time`   DATETIME DEFAULT NULL COMMENT 'create time',
    `update_time`   DATETIME DEFAULT NULL COMMENT 'create time',
    `del_flag`       INT(11) DEFAULT 0 COMMENT '删除标记，0-Normal，1-Deleted'
);