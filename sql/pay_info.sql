/*
 Navicat Premium Data Transfer

 Source Server         : RDS
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : rm-8vbol3k62a4owhicq1o.mysql.zhangbei.rds.aliyuncs.com:3306
 Source Schema         : pay

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 04/03/2022 23:06:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_info
-- ----------------------------
DROP TABLE IF EXISTS `pay_info`;
CREATE TABLE `pay_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `order_no` bigint NOT NULL COMMENT '订单号',
  `map_order_no` bigint NOT NULL COMMENT '映射的订单号',
  `pay_platform` int DEFAULT NULL COMMENT '支付平台:1-支付宝,2-微信',
  `platform_number` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '支付流水号',
  `platform_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '支付状态',
  `pay_amount` decimal(20,2) NOT NULL COMMENT '支付金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uqe_map_order_no` (`map_order_no`) USING BTREE,
  UNIQUE KEY `uqe_platform_number` (`platform_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb3;

SET FOREIGN_KEY_CHECKS = 1;
