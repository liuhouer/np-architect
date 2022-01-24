/*
 Navicat Premium Data Transfer

 Source Server         : 天天吃货 - 生产
 Source Server Type    : MariaDB
 Source Server Version : 100316
 Source Host           : 39.107.75.2:6022
 Source Schema         : itzixi-pay

 Target Server Type    : MariaDB
 Target Server Version : 100316
 File Encoding         : 65001

 Date: 01/12/2019 21:55:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` varchar(64) NOT NULL COMMENT '订单主键',
  `merchant_order_id` varchar(64) NOT NULL COMMENT '商户订单号',
  `merchant_user_id` varchar(64) NOT NULL COMMENT '商户方的发起用户的用户主键id',
  `amount` int(11) NOT NULL COMMENT '实际支付总金额（包含商户所支付的订单费邮费总额）',
  `pay_method` int(11) NOT NULL COMMENT '支付方式',
  `pay_status` int(11) NOT NULL COMMENT '支付状态 10：未支付 20：已支付 30：支付失败 40：已退款',
  `come_from` varchar(128) NOT NULL COMMENT '从哪一端来的，比如从天天吃货这门实战过来的',
  `return_url` varchar(255) NOT NULL COMMENT '支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址',
  `is_delete` int(11) NOT NULL COMMENT '逻辑删除状态;1: 删除 0:未删除',
  `created_time` datetime NOT NULL COMMENT '创建时间（成交时间）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表;';

SET FOREIGN_KEY_CHECKS = 1;
