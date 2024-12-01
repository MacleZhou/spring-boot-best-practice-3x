create table `audit-log`
(
    id             bigint        not null comment '主键' primary key,
    `request_id`    varchar(50)          not null comment '请求ID',
    `data_change` varchar(1000) null comment '变更项',
    `before_value` varchar(1000) null comment '对象变更前json',
    `after_value`  varchar(1000) null comment '对象变更后json',
    `table_name`   varchar(50)   null comment '变更表名',
    `create_time`  datetime      null comment '变更时间',
    `user_id`      bigint        null comment '操作人员'
  --   可以适当冗余相关用户名，机构ID，机构名称等。
)
    charset = utf8mb4;

create table goods
(
    id          bigint         not null
        primary key,
    name        varchar(100)   not null,
    price       decimal(18, 2) not null,
    description varchar(255)   null,
    brand       varchar(100)   not null,
    image_url   varchar(255)   not null,
    create_time datetime       not null,
    update_time datetime       not null
)
    charset = utf8mb4;