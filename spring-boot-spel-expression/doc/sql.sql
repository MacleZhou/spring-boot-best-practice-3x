CREATE TABLE `resb_certificate_question` (
                                             `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                             `question_no` varchar(128) NOT NULL COMMENT '问题编号',
                                             `classify` varchar(32) NOT NULL COMMENT '题库分类, A B C 等',
                                             `type` varchar(16) NOT NULL COMMENT 'single- 单选 multi-多选 judge-判断 pic-图片',
                                             `title` varchar(256) NOT NULL COMMENT '问题描述',
                                             `item_type` tinyint(2) NOT NULL COMMENT '选项类型 1-固定选项 2-表达式 3-列举全部正确选项 4-取部分正确答案 5-固定答案',
                                             `item_right_count_limit` tinyint(2) NOT NULL COMMENT '正确选项的最大数量',
                                             `item_count_limit` tinyint(2) NOT NULL COMMENT '选项的最大数量',
                                             `right_item_no` varchar(64) DEFAULT NULL COMMENT '固定正确答案',
                                             `extra_error_items` varchar(512) DEFAULT NULL COMMENT '补充的固定错误选项，逗号隔开',
                                             `index_name` varchar(256) DEFAULT NULL COMMENT '指标名称',
                                             `index_field` varchar(64) DEFAULT NULL COMMENT '指标字段名',
                                             `index_field_type` varchar(50) DEFAULT NULL COMMENT '指标数据类型',
                                             `request_url` varchar(256) DEFAULT '0' COMMENT '指标获取地址',
                                             `request_method` varchar(256) DEFAULT '0' COMMENT '指标获取方法',
                                             `request_param` varchar(512) DEFAULT NULL COMMENT '指标获取参数',
                                             `request_cache_group` varchar(64) DEFAULT NULL COMMENT '缓存分组标识',
                                             `index_type` tinyint(2) NOT NULL COMMENT '1: 统计指标 2:组织分组指标',
                                             `index_predicate` varchar(256) DEFAULT NULL COMMENT '校验条件',
                                             `index_parse` varchar(256) DEFAULT NULL COMMENT '结果解析',
                                             `index_decorate` varchar(256) DEFAULT NULL COMMENT '结果值包装转化',
                                             `valid_state` tinyint(2) NOT NULL COMMENT '是否有效（0：否  1：是）',
                                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='认证题库';

CREATE TABLE `resb_certificate_item` (
                                         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                         `question_no` varchar(128) NOT NULL COMMENT '问题编号',
                                         `item_no` varchar(128) NOT NULL COMMENT '选项编号',
                                         `item_title` varchar(128) DEFAULT NULL COMMENT '选项文案',
                                         `order_num` tinyint(2) NOT NULL COMMENT '顺序',
                                         `valid_state` tinyint(2) NOT NULL COMMENT '是否有效（0：否  1：是）',
                                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='认证选项';

insert into resb_certificate_question (question_no, classify, type, title, item_type, item_right_count_limit, item_count_limit, index_type, valid_state) values ('25', 'A,B,C,D,E,F,G,H,I,J,K,L', 'single', '以下对于该小区本月入住率说法正确的是?', 2)