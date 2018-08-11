-- 创建数据库
drop database if exists `seckill`;
create database seckill;

-- 使用数据库
use seckill;

-- 创建秒杀库存表
create table `stock` (
    `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
    `name` varchar(120) NOT NULL COMMENT '商品名称',
    `number` int NOT NULL COMMENT '库存数量',
    `start_time` timestamp NOT NULL COMMENT '秒杀开始时间',
    `end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '每条库存信息的创建时间',
    primary key (seckill_id),
    key idx_start_time(start_time),
    key idx_end_time(end_time),
    key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 初始化数据
insert into stock(name, number, start_time, end_time)
values
    ('2000元秒杀iphone7', 10, '2018-11-11 00:00:00', '2018-11-11 01:00:00'),
    ('1000元秒杀ipad', 100, '2018-11-11 00:00:00', '2018-11-11 01:00:00'),
    ('100元秒杀金士顿4G内存条', 100, '2018-11-11 02:00:00', '2018-11-11 03:00:00'),
    ('200元秒杀东芝256G固态', 100, '2018-11-11 02:00:00', '2018-11-11 03:00:00'),
    ('1000元秒杀AMD R5处理器', 100, '2018-11-11 02:00:00', '2018-11-11 03:00:00'),
    ('10元秒杀一排酸奶', 1000, '2018-11-11 00:00:00', '2018-11-12 00:00:00'),
    ('5元秒杀薯片', 1000, '2018-11-11 06:00:00', '2018-11-11 08:00:00'),
    ('10000元秒杀外星人笔记本电脑', 10, '2018-11-11 07:00:00', '2018-11-11 08:00:00'),
    ('Java核心技术卷I 原书第十版', 10, '2018-07-22 15:15:00', '2018-07-23 08:00:00');

insert into stock(name, number, start_time, end_time)
values ('Java核心技术卷I 原书第十版', 10, '2018-07-22 15:15:00', '2018-07-23 08:00:00');

-- 秒杀成功明细表
-- 用户登录认证的相关信息
create table `success_killed` (
  `seckill_id` bigint NOT NULL COMMENT '秒杀商品的id',
  `user_phone` bigint NOT NULL COMMENT '用户手机号',
  `state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标示 -1:无效 0:成功 1:已付款 2:已发货',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '明细条目创建时间',
  primary key (seckill_id, user_phone),
  key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';