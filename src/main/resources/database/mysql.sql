# 创建数据库
CREATE DATABASE IF NOT EXISTS boot DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

# 创建表
USE boot;
DROP TABLE IF EXISTS sys_user;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

INSERT INTO sys_user(`username`,`password`) VALUES('AAA','123456');
INSERT INTO sys_user(`username`,`password`) VALUES('BBB','123456');
INSERT INTO sys_user(`username`,`password`) VALUES('CCC','123456');