-- 存放接口信息
create table if not exists api_service.`interface_info`
(
    `id` varchar(256) not null primary key,
    `name` varchar(256) not null comment '接口名称',
    `description` varchar(256) not null comment '接口描述',
    `request_url` varchar(256) not null comment '接口请求地址',
    `request_header` text not null comment '请求头',
    `response_header` text not null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` int not null comment '请求人类型',
    `userId` bigint not null comment '用户名',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '存放接口信息';

insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('984', 'IG', '张煜祺', 'www.burton-zieme.name', '廖智渊', '陶天翊', 0, 0, 1);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('440324878', '3t', '杜鑫鹏', 'www.dewitt-harber.com', '钱展鹏', '侯旭尧', 0, 0, 784521838);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('237718', 'A8', '秦立辉', 'www.everett-dibbert.co', '陈修杰', '孙彬', 0, 0, 2131237);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('41', 'di4', '李晓博', 'www.brianna-schimmel.io', '何子涵', '马懿轩', 0, 0, 173);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('21588', 'Oo', '萧鹏涛', 'www.corey-vandervort.io', '许哲瀚', '邓语堂', 0, 0, 88416458);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('828', 'w5LQ', '黄钰轩', 'www.marlon-rice.name', '于雪松', '袁志强', 0, 0, 3932378);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('336', 'LHK', '于子默', 'www.lore-dach.biz', '龙楷瑞', '程炫明', 0, 0, 25);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('227', 'E9i', '陆泽洋', 'www.kara-luettgen.info', '杜天宇', '阎峻熙', 0, 0, 1862666249);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('5414298', 'YEe', '武雨泽', 'www.dean-schuppe.io', '罗果', '韩伟泽', 0, 0, 3361037);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('103527', '9gzR', '王立轩', 'www.bernetta-bins.info', '熊锦程', '胡越彬', 0, 0, 612);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('236508996', 'd1tf', '赖天宇', 'www.sid-heidenreich.biz', '郭昊然', '戴烨霖', 0, 0, 3309081107);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('1711', 'U5', '史雪松', 'www.edwardo-sanford.com', '莫烨伟', '刘伟祺', 0, 0, 7);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('4760', 'Ae4', '梁绍齐', 'www.edmond-moore.co', '朱君浩', '林弘文', 0, 0, 81010);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('485', 'iV2TL', '陈绍齐', 'www.natalie-dach.name', '李弘文', '史智渊', 0, 0, 674428);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('36701781', 'eCjl', '田烨伟', 'www.coy-lebsack.org', '赖胤祥', '汪思', 0, 0, 97487);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('276415', 'Oi', '黎文博', 'www.liberty-waters.org', '任明哲', '钟炫明', 0, 0, 874365);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('650066', 'h3', '尹泽洋', 'www.apolonia-dickinson.info', '刘明杰', '杜雨泽', 0, 0, 772878650);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('97268591', 'QWYF', '潘熠彤', 'www.darby-daugherty.name', '钱烨霖', '董明', 0, 0, 58693930);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('5954344016', 'F7', '贾风华', 'www.kenton-oreilly.com', '陆鑫鹏', '姚鸿煊', 0, 0, 8);
insert into api_service.`interface_info` (`id`, `name`, `description`, `request_url`, `request_header`, `response_header`, `status`, `method`, `userId`) values ('30758999', 'eq', '朱鹏煊', 'www.leida-heaney.org', '田乐驹', '孟瑞霖', 0, 0, 217336984);