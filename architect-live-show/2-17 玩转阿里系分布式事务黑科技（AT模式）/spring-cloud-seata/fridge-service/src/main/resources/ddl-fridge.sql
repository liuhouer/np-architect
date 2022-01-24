CREATE TABLE `fridge` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `animal_id` bigint(11) DEFAULT NULL COMMENT '冰箱里动物ID',
  `door_opened` int(1) DEFAULT '0' COMMENT '门关没关',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


INSERT INTO `seata-fridge`.`fridge` (`id`, `animal_id`, `door_opened`) VALUES ('1', null,'0');
