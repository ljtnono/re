/*
 Navicat Premium Data Transfer

 Source Server         : localhost-3306
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : re

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 25/10/2020 18:23:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_article
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '博客文章id',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '博客文章标题',
  `summary` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '博客文章简介信息',
  `markdown` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '博客文章markdown内容信息',
  `create_time` datetime(0) NOT NULL COMMENT '博客文章创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '博客文章修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除  0正常',
  `tag_id` int NULL DEFAULT NULL COMMENT '博客标签id, 没有则不属于任何标签',
  `status` tinyint(1) NOT NULL COMMENT '文章状态 0 正常 1 推荐列表 2 置顶',
  `user_id` int NULL DEFAULT NULL COMMENT '所属用户id，如果为null，表示匿名用户',
  `image_id` int NULL DEFAULT NULL COMMENT '封面图的id, 如果为null，表示默认封面',
  `view` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `favorite` int NOT NULL DEFAULT 0 COMMENT '喜欢数',
  `is_draft` tinyint(1) NOT NULL COMMENT '是否是草稿 0 不是 1 是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_blog_article_sys_user_1`(`user_id`) USING BTREE,
  INDEX `fk_blog_article_blog_article_tag_1`(`tag_id`) USING BTREE,
  INDEX `fk_blog_article_rs_image_1`(`image_id`) USING BTREE,
  CONSTRAINT `fk_blog_article_blog_article_tag_1` FOREIGN KEY (`tag_id`) REFERENCES `blog_article_tag` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_blog_article_rs_image_1` FOREIGN KEY (`image_id`) REFERENCES `rs_image` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_blog_article_sys_user_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '博客文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_article
-- ----------------------------

-- ----------------------------
-- Table structure for blog_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '博客文章标签id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签名',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '博客标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for blog_comment
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '博客评论id',
  `article_id` int NOT NULL COMMENT '评论所属文章id',
  `text` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `user_id` int NULL DEFAULT NULL COMMENT '评论的用户id，如果为null表示匿名评论',
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户评论的ip地址',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_blog_comment_blog_article_1`(`article_id`) USING BTREE,
  CONSTRAINT `fk_blog_comment_blog_article_1` FOREIGN KEY (`article_id`) REFERENCES `blog_article` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '博客评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of blog_comment
-- ----------------------------

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户消息表id',
  `user_id` int NOT NULL COMMENT '用户id',
  `message` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  `status` tinyint(1) NOT NULL COMMENT '消息状态 0 未读 1 已读',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 已删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_sys_user_message_sys_user_1`(`user_id`) USING BTREE,
  CONSTRAINT `fk_sys_user_message_sys_user_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for rs_image
-- ----------------------------
DROP TABLE IF EXISTS `rs_image`;
CREATE TABLE `rs_image`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图片表id',
  `image_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片id（UUID 32位）',
  `origin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原图片名（包含扩展名）',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片的url地址',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  `size` bigint NOT NULL COMMENT '图片大小',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rs_image
-- ----------------------------

-- ----------------------------
-- Table structure for rs_link
-- ----------------------------
DROP TABLE IF EXISTS `rs_link`;
CREATE TABLE `rs_link`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '链接id',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '链接所指向的url',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  `type` tinyint(1) NOT NULL COMMENT '链接类型 1 友情链接 2 技术官网 ',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of rs_link
-- ----------------------------

-- ----------------------------
-- Table structure for skill
-- ----------------------------
DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '技能表id',
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '技能名',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '技能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of skill
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '系统配置表id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置名',
  `key` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置键',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '系统日志表id',
  `event_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志事件类型 比如登陆事件  login，编写博客事件 blog_edit',
  `user_id` int NOT NULL COMMENT '用户id',
  `event_result` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件结果，成功为success，失败为fail',
  `event_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件描述',
  `event_time` datetime(0) NOT NULL COMMENT '事件执行时间',
  `token` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'token',
  `ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '事件执行ip地址',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_sys_log_sys_user_1`(`user_id`) USING BTREE,
  CONSTRAINT `fk_sys_log_sys_user_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '权限表id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名',
  `type` tinyint(1) NOT NULL COMMENT '权限类型 0 菜单 1 具体权限或按钮',
  `parent_id` int NOT NULL COMMENT '父权限id，顶层父菜单为-1',
  `expression` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限表达式',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6085 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1000, '博客管理', 0, -1, 'blog', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1001, '博客管理', 0, 1000, 'blog:blog', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1002, '查看博客', 1, 1001, 'blog:blog:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1003, '新增博客', 1, 1001, 'blog:blog:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1004, '修改博客', 1, 1001, 'blog:blog:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1005, '删除博客', 1, 1001, 'blog:blog:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1020, '标签管理', 0, 1000, 'blog:tag', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1021, '查看标签', 1, 1020, 'blog:tag:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1022, '新增标签', 1, 1020, 'blog:tag:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1023, '修改标签', 1, 1020, 'blog:tag:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1024, '删除标签', 1, 1020, 'blog:tag:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1040, '评论管理', 0, 1000, 'blog:comment', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1041, '查看评论', 1, 1040, 'blog:comment:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1042, '导出评论', 1, 1040, 'blog:comment:export', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (1043, '删除评论', 1, 1040, 'blog:comment:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2000, '资源管理', 0, -1, 'rs', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2001, '图片管理', 0, 2000, 'rs:image', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2002, '查看图片', 1, 2001, 'rs:image:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2003, '上传图片', 1, 2001, 'rs:image:upload', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2004, '下载图片', 1, 2001, 'rs:image:download', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2005, '删除图片', 1, 2001, 'rs:image:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2006, '更新图片', 1, 2001, 'rs:image:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2020, '链接管理', 0, 2000, 'rs:link', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2021, '查看链接', 1, 2020, 'rs:link:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2022, '新增链接', 1, 2020, 'rs:link:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2023, '更新链接', 1, 2020, 'rs:link:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (2024, '删除链接', 1, 2020, 'rs:link:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (3000, '时间轴管理', 0, -1, 'timeline', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (3001, '查看时间轴', 1, 3000, 'timeline:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (3002, '删除时间轴', 1, 3000, 'timeline:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (3003, '修改时间轴', 1, 3000, 'timeline:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (4000, '技能管理', 0, -1, 'skill', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (4001, '查看技能', 1, 4000, 'skill:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (4002, '新增技能', 1, 4000, 'skill:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (4003, '删除技能', 1, 4000, 'skill:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (4004, '修改技能', 1, 4000, 'skill:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5000, '消息管理', 0, -1, 'message', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5001, '消息管理', 0, 5000, 'message:message', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5002, '查看消息', 1, 5001, 'message:message:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5003, '新增消息', 1, 5001, 'message:message:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5004, '删除消息', 1, 5001, 'message:message:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5005, '修改消息', 1, 5001, 'message:message:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5020, '回收站管理', 0, 5000, 'message:recycle', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5021, '查看回收站', 1, 5020, 'message:recycle:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5022, '删除回收站消息', 1, 5020, 'message:recycle:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (5023, '恢复回收站消息', 1, 5020, 'message:recycle:recover', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6000, '系统管理', 0, -1, 'system', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6001, '用户管理', 0, 6000, 'system:user', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6002, '查看用户', 1, 6001, 'system:user:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6003, '新增用户', 1, 6001, 'system:user:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6004, '删除用户', 1, 6001, 'system:user:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6005, '修改用户', 1, 6001, 'system:user:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6020, '角色管理', 0, 6000, 'system:role', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6021, '查看角色', 1, 6020, 'system:role:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6022, '新增角色', 1, 6020, 'system:role:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6023, '删除角色', 1, 6020, 'system:role:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6024, '修改角色', 1, 6020, 'system:role:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6040, '权限管理', 0, 6000, 'system:permission', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6041, '查看权限', 1, 6040, 'system:permission:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6042, '新增权限', 1, 6040, 'system:permission:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6043, '删除权限', 1, 6040, 'system:permission:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6044, '修改权限', 1, 6040, 'system:permission:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6060, '日志管理', 0, 6000, 'system:log', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6061, '查看日志', 1, 6060, 'system:log:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6062, '新增日志', 1, 6060, 'system:log:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6063, '删除日志', 1, 6060, 'system:log:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6064, '导出日志', 1, 6060, 'system:log:export', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6080, '网站配置', 0, 6000, 'system:config', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6081, '查看配置', 1, 6080, 'system:config:view', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6082, '新增配置', 1, 6080, 'system:config:add', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6083, '删除配置', 1, 6080, 'system:config:delete', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);
INSERT INTO `sys_permission` VALUES (6084, '修改配置', 1, 6080, 'system:config:update', '2020-08-24 00:23:16', '2020-08-24 00:23:16', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色表id',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '超级管理员', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0);
INSERT INTO `sys_role` VALUES (2, 'test', '测试人员', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0);
INSERT INTO `sys_role` VALUES (3, 'tourist', '游客', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色权限表id',
  `role_id` int NOT NULL COMMENT '角色id',
  `permission_id` int NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_sys_role_permission_sys_role_1`(`role_id`) USING BTREE,
  INDEX `fk_sys_role_permission_sys_permission_1`(`permission_id`) USING BTREE,
  CONSTRAINT `fk_sys_role_permission_sys_permission_1` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_role_permission_sys_role_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 1000);
INSERT INTO `sys_role_permission` VALUES (2, 1, 1001);
INSERT INTO `sys_role_permission` VALUES (3, 1, 1002);
INSERT INTO `sys_role_permission` VALUES (4, 1, 1003);
INSERT INTO `sys_role_permission` VALUES (5, 1, 1004);
INSERT INTO `sys_role_permission` VALUES (6, 1, 1005);
INSERT INTO `sys_role_permission` VALUES (7, 1, 1020);
INSERT INTO `sys_role_permission` VALUES (8, 1, 1021);
INSERT INTO `sys_role_permission` VALUES (9, 1, 1022);
INSERT INTO `sys_role_permission` VALUES (10, 1, 1023);
INSERT INTO `sys_role_permission` VALUES (11, 1, 1024);
INSERT INTO `sys_role_permission` VALUES (12, 1, 1040);
INSERT INTO `sys_role_permission` VALUES (13, 1, 1041);
INSERT INTO `sys_role_permission` VALUES (14, 1, 1042);
INSERT INTO `sys_role_permission` VALUES (15, 1, 1043);
INSERT INTO `sys_role_permission` VALUES (16, 1, 2000);
INSERT INTO `sys_role_permission` VALUES (17, 1, 2001);
INSERT INTO `sys_role_permission` VALUES (18, 1, 2002);
INSERT INTO `sys_role_permission` VALUES (19, 1, 2003);
INSERT INTO `sys_role_permission` VALUES (20, 1, 2004);
INSERT INTO `sys_role_permission` VALUES (21, 1, 2005);
INSERT INTO `sys_role_permission` VALUES (22, 1, 2006);
INSERT INTO `sys_role_permission` VALUES (23, 1, 2020);
INSERT INTO `sys_role_permission` VALUES (24, 1, 2021);
INSERT INTO `sys_role_permission` VALUES (25, 1, 2022);
INSERT INTO `sys_role_permission` VALUES (26, 1, 2023);
INSERT INTO `sys_role_permission` VALUES (27, 1, 2024);
INSERT INTO `sys_role_permission` VALUES (28, 1, 3000);
INSERT INTO `sys_role_permission` VALUES (29, 1, 3001);
INSERT INTO `sys_role_permission` VALUES (30, 1, 3002);
INSERT INTO `sys_role_permission` VALUES (31, 1, 3003);
INSERT INTO `sys_role_permission` VALUES (32, 1, 4000);
INSERT INTO `sys_role_permission` VALUES (33, 1, 4001);
INSERT INTO `sys_role_permission` VALUES (34, 1, 4002);
INSERT INTO `sys_role_permission` VALUES (35, 1, 4003);
INSERT INTO `sys_role_permission` VALUES (36, 1, 4004);
INSERT INTO `sys_role_permission` VALUES (37, 1, 5000);
INSERT INTO `sys_role_permission` VALUES (38, 1, 5001);
INSERT INTO `sys_role_permission` VALUES (39, 1, 5002);
INSERT INTO `sys_role_permission` VALUES (40, 1, 5003);
INSERT INTO `sys_role_permission` VALUES (41, 1, 5004);
INSERT INTO `sys_role_permission` VALUES (42, 1, 5005);
INSERT INTO `sys_role_permission` VALUES (43, 1, 5020);
INSERT INTO `sys_role_permission` VALUES (44, 1, 5021);
INSERT INTO `sys_role_permission` VALUES (45, 1, 5022);
INSERT INTO `sys_role_permission` VALUES (46, 1, 5023);
INSERT INTO `sys_role_permission` VALUES (47, 1, 6000);
INSERT INTO `sys_role_permission` VALUES (48, 1, 6001);
INSERT INTO `sys_role_permission` VALUES (49, 1, 6002);
INSERT INTO `sys_role_permission` VALUES (50, 1, 6003);
INSERT INTO `sys_role_permission` VALUES (51, 1, 6004);
INSERT INTO `sys_role_permission` VALUES (52, 1, 6005);
INSERT INTO `sys_role_permission` VALUES (53, 1, 6020);
INSERT INTO `sys_role_permission` VALUES (54, 1, 6021);
INSERT INTO `sys_role_permission` VALUES (55, 1, 6022);
INSERT INTO `sys_role_permission` VALUES (56, 1, 6023);
INSERT INTO `sys_role_permission` VALUES (57, 1, 6024);
INSERT INTO `sys_role_permission` VALUES (58, 1, 6040);
INSERT INTO `sys_role_permission` VALUES (59, 1, 6041);
INSERT INTO `sys_role_permission` VALUES (60, 1, 6042);
INSERT INTO `sys_role_permission` VALUES (61, 1, 6043);
INSERT INTO `sys_role_permission` VALUES (62, 1, 6044);
INSERT INTO `sys_role_permission` VALUES (63, 1, 6060);
INSERT INTO `sys_role_permission` VALUES (64, 1, 6061);
INSERT INTO `sys_role_permission` VALUES (65, 1, 6062);
INSERT INTO `sys_role_permission` VALUES (66, 1, 6063);
INSERT INTO `sys_role_permission` VALUES (67, 1, 6064);
INSERT INTO `sys_role_permission` VALUES (68, 1, 6080);
INSERT INTO `sys_role_permission` VALUES (69, 1, 6081);
INSERT INTO `sys_role_permission` VALUES (70, 1, 6082);
INSERT INTO `sys_role_permission` VALUES (71, 1, 6083);
INSERT INTO `sys_role_permission` VALUES (72, 1, 6084);

-- ----------------------------
-- Table structure for sys_timeline
-- ----------------------------
DROP TABLE IF EXISTS `sys_timeline`;
CREATE TABLE `sys_timeline`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '时间轴id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '修改时间',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间轴内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '时间轴' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_timeline
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户邮箱',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '80cea81e681679a81634e2b1846e6cb8', '15337106753', '935188400@qq.com', '2020-08-24 00:42:24', '2020-08-24 00:42:24', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户角色表id',
  `user_id` int NOT NULL COMMENT '用户id',
  `role_id` int NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_sys_user_role_sys_user_1`(`user_id`) USING BTREE,
  INDEX `fk_sys_user_role_sys_role_1`(`role_id`) USING BTREE,
  CONSTRAINT `fk_sys_user_role_sys_role_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_user_role_sys_user_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for user_skill
-- ----------------------------
DROP TABLE IF EXISTS `user_skill`;
CREATE TABLE `user_skill`  (
  `id` int NOT NULL COMMENT '用户技能表id',
  `user_id` int NOT NULL COMMENT '用户id',
  `skill_id` int NOT NULL COMMENT '技能id',
  `score` int NOT NULL COMMENT '技能得分',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `skill_id`(`skill_id`) USING BTREE,
  CONSTRAINT `user_skill_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_skill_ibfk_2` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户技能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_skill
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
