/*
 Navicat Premium Data Transfer

 Source Server         : chihuobao
 Source Server Type    : MySQL
 Source Server Version : 50527
 Source Host           : localhost:3306
 Source Schema         : takeout

 Target Server Type    : MySQL
 Target Server Version : 50527
 File Encoding         : 65001

 Date: 20/03/2026 13:44:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sort` int(11) NULL DEFAULT 0,
  `status` tinyint(4) NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop`(`shop_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 1, '热菜', 1, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `category` VALUES (2, 1, '凉菜', 2, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `category` VALUES (3, 1, '汤品', 3, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `category` VALUES (4, 2, '肉类', 1, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `category` VALUES (5, 2, '蔬菜类', 2, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `category` VALUES (6, 2, '主食类', 3, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `product_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` decimal(10, 2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total_fee` decimal(10, 2) NOT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order`(`order_id`) USING BTREE,
  INDEX `idx_product`(`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES (1, 1, 4, '凉拌木耳', NULL, 15.00, 1, 15.00, '2026-03-20 13:42:25', '2026-03-20 13:42:25');
INSERT INTO `order_detail` VALUES (2, 1, 5, '西红柿鸡蛋汤', NULL, 10.00, 1, 10.00, '2026-03-20 13:42:25', '2026-03-20 13:42:25');
INSERT INTO `order_detail` VALUES (3, 1, 6, '紫菜蛋花汤', NULL, 8.00, 1, 8.00, '2026-03-20 13:42:25', '2026-03-20 13:42:25');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `shop_id` int(11) NOT NULL,
  `total_amount` decimal(10, 2) NOT NULL,
  `pay_amount` decimal(10, 2) NOT NULL,
  `delivery_fee` decimal(10, 2) NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT 1,
  `receiver_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `receiver_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `receiver_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pay_time` datetime NULL DEFAULT NULL,
  `delivery_time` datetime NULL DEFAULT NULL,
  `finish_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no`) USING BTREE,
  INDEX `idx_user`(`user_id`) USING BTREE,
  INDEX `idx_shop_status`(`shop_id`, `status`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'ORDMMYH2L62SXQ2', 1, 1, 33.00, 33.00, 0.00, '多加香菜', 4, '测试用户', '15612341234', '武工大教辅宿舍', '2026-03-20 13:42:30', '2026-03-20 05:42:46', '2026-03-20 13:42:59', '2026-03-20 13:42:25', '2026-03-20 13:42:59');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shop_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `stock` int(11) NULL DEFAULT 999,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sales` int(11) NULL DEFAULT 0,
  `status` tinyint(4) NULL DEFAULT 1,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop_category`(`shop_id`, `category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 1, 1, '鱼香肉丝', 28.00, 999, '经典川菜，酸甜咸鲜，口感丰富', 'https://example.com/food1.jpg', 120, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (2, 1, 1, '宫保鸡丁', 32.00, 999, '鸡肉滑嫩，花生酥脆，微辣酸甜', 'https://example.com/food2.jpg', 98, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (3, 1, 2, '拍黄瓜', 12.00, 999, '清爽解腻，酸辣开胃', 'https://example.com/food3.jpg', 156, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (4, 1, 2, '凉拌木耳', 15.00, 999, '口感爽脆，营养丰富', 'https://example.com/food4.jpg', 89, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (5, 1, 3, '西红柿鸡蛋汤', 10.00, 999, '家常味道，暖心暖胃', 'https://example.com/food5.jpg', 78, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (6, 1, 3, '紫菜蛋花汤', 8.00, 999, '鲜香可口，做法简单', '/files/download/1773985227582-product_23_皮蛋瘦肉粥.jpg', 105, -1, '2026-03-20 13:38:47', '2026-03-20 13:41:22');
INSERT INTO `product` VALUES (7, 2, 4, '肥牛卷', 18.00, 999, '精选肥牛，涮煮后鲜嫩多汁', 'https://example.com/food7.jpg', 200, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (8, 2, 4, '羔羊肉卷', 16.00, 999, '内蒙羔羊肉，无膻味，口感佳', 'https://example.com/food8.jpg', 186, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (9, 2, 5, '娃娃菜', 5.00, 999, '鲜嫩爽口，吸满汤汁超入味', 'https://example.com/food9.jpg', 230, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (10, 2, 5, '金针菇', 6.00, 999, '口感脆嫩，营养丰富', 'https://example.com/food10.jpg', 198, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (11, 2, 6, '方便面', 4.00, 999, '经典泡面，吸汁入味', 'https://example.com/food11.jpg', 210, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `product` VALUES (12, 2, 6, '红薯粉', 5.00, 999, 'Q弹劲道，不易断', 'https://example.com/food12.jpg', 178, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cover_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `min_price` decimal(10, 2) NULL DEFAULT 0.00,
  `delivery_fee` decimal(10, 2) NULL DEFAULT 0.00,
  `rating` decimal(2, 1) NULL DEFAULT 5.0,
  `status` tinyint(4) NOT NULL DEFAULT 1,
  `owner_id` int(11) NOT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_owner`(`owner_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of shop
-- ----------------------------
INSERT INTO `shop` VALUES (1, '美味家常菜馆', '北京市朝阳区建国路88号', '13800138000', 'https://example.com/shop1.jpg', '主打各类家常菜，口味正宗，价格亲民', 20.00, 5.00, 4.8, 1, 1, '2026-03-20 13:38:47', '2026-03-20 13:38:47');
INSERT INTO `shop` VALUES (2, '鲜香麻辣烫', '上海市浦东新区张江路100号', '13900139000', 'https://example.com/shop2.jpg', '川味麻辣烫，食材新鲜，汤底任选', 15.00, 3.00, 4.7, 1, 2, '2026-03-20 13:38:47', '2026-03-20 13:38:47');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'testuser', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', NULL, 'USER', '2026-03-19 15:35:56');
INSERT INTO `user` VALUES (2, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', NULL, 'ADMIN', '2026-03-19 15:35:56');
INSERT INTO `user` VALUES (3, 'shop_owner1', '123456', '莫丝', 'https://example.com/avatar1.jpg', 'shop_owner', '2026-03-20 13:38:47');
INSERT INTO `user` VALUES (4, 'shop_owner2', '123456', '奥尔拉', 'https://example.com/avatar2.jpg', 'shop_owner', '2026-03-20 13:38:47');

SET FOREIGN_KEY_CHECKS = 1;
