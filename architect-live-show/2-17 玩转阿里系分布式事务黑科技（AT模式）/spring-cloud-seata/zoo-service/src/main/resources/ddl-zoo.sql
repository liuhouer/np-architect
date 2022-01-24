CREATE TABLE `zoo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL COMMENT '名称',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `available` int(1) DEFAULT NULL COMMENT '当前是否可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


INSERT INTO `seata-zoo`.`zoo` (`id`, `name`, `type`, `available`) VALUES ('1', 'Elephant', '1', '1');