create table t_topic
(
    id varchar(32) not null comment '主键ID'primary key,
    stars int not null comment '点赞数',
    title varchar(50) null comment '标题',
    create_time datetime not null comment '创建时间'
)comment '主题表';

create table t_blog
(
    id varchar(32) not null comment '主键ID'primary key,
    deleted tinyint(1) default 0 not null comment '是否删除',
    create_by varchar(32) not null comment '创建人',
    create_time datetime not null comment '创建时间',
    update_by varchar(32) not null comment '更新人',
    update_time datetime not null comment '更新时间',
    title varchar(50) not null comment '标题',
    content varchar(256) null comment '内容',
    url varchar(128) null comment '博客链接',
    star int not null comment '点赞数',
    publish_time datetime null comment '发布时间',
    score decimal(18, 2) not null comment '评分',
    status int not null comment '状态',
    `order` decimal(18, 2) not null comment '排序',
    is_top tinyint(1) not null comment '是否置顶',
    top tinyint(1) not null comment '是否置顶'
)comment '博客表';