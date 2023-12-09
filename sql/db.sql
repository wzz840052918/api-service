-- 存放接口信息
create table if not exists api_service.`interface_info`
(
    `id` bigint not null primary key,
    `name` varchar(256) not null comment '接口名称',
    `description` varchar(256) not null comment '接口描述',
    `url` varchar(256) not null comment '接口请求地址',
    `host` varchar(32) not null comment 'host',
    `port` varchar(32) comment '端口号',
    `path` varchar(256) not null comment '路径',
    `request_header` text not null comment '请求头',
    `response_header` text not null comment '响应头',
    `request_params` text comment  '请求参数',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(32) not null comment '请求类型',
    `userId` bigint comment '用户名',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '存放接口信息';


-- 用户和调用接口关系表
create table if not exists api_service.`user_interface_info`
(
    `id` bigint not null auto_increment primary key,
    `user_id` bigint not null comment '调用用户 id',
    `interface_info_id` bigint not null comment '接口id',
    `total_num` int default 0 not null comment '总调用次数',
    `left_num` int default 0 not null comment '接口剩余调用次数',
    `status` int default 0 not null comment '0-正常，1-禁用',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户和调用接口关系表';