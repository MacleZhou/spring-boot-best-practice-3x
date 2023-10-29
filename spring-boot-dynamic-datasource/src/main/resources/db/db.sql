CREATE DATABASE IF NOT EXISTS test1 DEFAULT CHARACTER SET utf8;

CREATE TABLE `databasesource`  (
                                   `datasource_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源的id',
                                   `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接信息',
                                   `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
                                   `pass_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
                                   `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '暂留字段',
                                   `databasetype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库类型'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `test1`.`databasesource`(`datasource_id`, `url`, `user_name`, `pass_word`, `code`, `databasetype`) VALUES ('dbtest2', 'jdbc:mysql://localhost:3306/test2?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull', 'root', 'NBfe@2099', NULL, 'mysql');
INSERT INTO `test1`.`databasesource`(`datasource_id`, `url`, `user_name`, `pass_word`, `code`, `databasetype`) VALUES ('dbtest3', 'jdbc:mysql://localhost:3306/test3?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull', 'root', 'NBfe@2099', NULL, 'mysql');

commit;

CREATE DATABASE IF NOT EXISTS test2 DEFAULT CHARACTER SET utf8;
CREATE TABLE `user`  (
                         `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                         `age` int(3) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
INSERT INTO `test2`.`user`(`user_name`, `age`) VALUES ('数据库2-小明', 20);
INSERT INTO `test2`.`user`(`user_name`, `age`) VALUES ('数据库2-小方', 17);


CREATE DATABASE IF NOT EXISTS test3 DEFAULT CHARACTER SET utf8;

CREATE TABLE `user`  (
                         `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                         `age` int(3) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `test3`.`user`(`user_name`, `age`) VALUES ('数据库3-啊强', 11);
INSERT INTO `test3`.`user`(`user_name`, `age`) VALUES ('数据库3-啊木', 12);