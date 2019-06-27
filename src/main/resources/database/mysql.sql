DROP TABLE IF EXISTS sys_user;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

INSERT INTO sys_user VALUES(REPLACE(UUID(),'-',''),'AAA','123456');
INSERT INTO sys_user VALUES(REPLACE(UUID(),'-',''),'BBB','123456');
INSERT INTO sys_user VALUES(REPLACE(UUID(),'-',''),'CCC','123456');