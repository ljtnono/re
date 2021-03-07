/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.8
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : 192.168.1.8:8003
 Source Schema         : re

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 07/03/2021 11:08:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_article
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '博客文章id',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '博客文章标题',
  `summary` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '博客文章简介信息',
  `markdown_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '博客文章markdown内容信息',
  `create_time` datetime NOT NULL COMMENT '博客文章创建时间',
  `modify_time` datetime NOT NULL COMMENT '博客文章修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除  0正常',
  `type_id` int DEFAULT NULL COMMENT '博客分类id',
  `user_id` int DEFAULT NULL COMMENT '所属用户id，如果为-1，表示匿名用户',
  `cover_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '封面图路径',
  `view` int NOT NULL DEFAULT '0' COMMENT '浏览量',
  `favorite` int NOT NULL DEFAULT '0' COMMENT '喜欢数',
  `is_draft` tinyint(1) NOT NULL COMMENT '是否是草稿 0 不是 1 是',
  `is_recommend` tinyint(1) NOT NULL COMMENT '是否推荐 0 是 1 不是',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_blog_article_sys_user_1` (`user_id`) USING BTREE,
  KEY `fk_blog_article_blog_article_tag_1` (`type_id`) USING BTREE,
  KEY `fk_blog_article_rs_image_1` (`cover_url`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='博客文章表';

-- ----------------------------
-- Records of blog_article
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for blog_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag` (
  `id` int NOT NULL COMMENT '博客标签关联表id，自增',
  `article_id` int NOT NULL COMMENT '文章id',
  `tag_id` int NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章标签关联表';

-- ----------------------------
-- Records of blog_article_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for blog_comment
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '博客评论id',
  `article_id` int NOT NULL COMMENT '评论所属文章id',
  `text` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '评论内容',
  `user_id` int DEFAULT NULL COMMENT '评论的用户id，如果为null表示匿名评论',
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户评论的ip地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_blog_comment_blog_article_1` (`article_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='博客评论表';

-- ----------------------------
-- Records of blog_comment
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag` (
  `id` int NOT NULL COMMENT '标签id，自增',
  `name` varchar(20) NOT NULL COMMENT '标签名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客标签';

-- ----------------------------
-- Records of blog_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for blog_type
-- ----------------------------
DROP TABLE IF EXISTS `blog_type`;
CREATE TABLE `blog_type` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '博客分类id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名，不超过20个字符',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 0 正常 1 已删除',
  `view` int DEFAULT NULL COMMENT '类型总浏览量',
  `favorite` int DEFAULT NULL COMMENT '类型总喜欢数',
  `is_recommend` tinyint(1) NOT NULL COMMENT '是否推荐类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='博客标签表';

-- ----------------------------
-- Records of blog_type
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for git_repository
-- ----------------------------
DROP TABLE IF EXISTS `git_repository`;
CREATE TABLE `git_repository` (
  `id` int NOT NULL COMMENT '主键id，自增',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '仓库名',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '仓库地址',
  `star_num` int DEFAULT NULL COMMENT '仓库star数量',
  `fork_num` int DEFAULT NULL COMMENT '仓库fork数量',
  `watch_num` int DEFAULT NULL COMMENT '仓库watch数量',
  `language` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '主要语言',
  `platform` tinyint(1) DEFAULT NULL COMMENT '平台 1 github 2 gitee',
  `is_recommend` tinyint(1) DEFAULT NULL COMMENT '是否推荐 0 是 1 不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='git仓库';

-- ----------------------------
-- Records of git_repository
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for rs_book
-- ----------------------------
DROP TABLE IF EXISTS `rs_book`;
CREATE TABLE `rs_book` (
  `id` int NOT NULL COMMENT '主键id，自增',
  `name` varchar(255) DEFAULT NULL COMMENT '书名',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `publish_date` datetime DEFAULT NULL COMMENT '出版日期',
  `category` varchar(255) DEFAULT NULL COMMENT '分类',
  `download_url` varchar(255) DEFAULT NULL COMMENT '下载地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='书籍';

-- ----------------------------
-- Records of rs_book
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for rs_image
-- ----------------------------
DROP TABLE IF EXISTS `rs_image`;
CREATE TABLE `rs_image` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图片表id',
  `image_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片id（UUID 32位）',
  `origin_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原图片名（包含扩展名）',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图片的url地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `size` bigint DEFAULT NULL COMMENT '图片大小',
  `save_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图片的存储路径',
  `md5` varchar(255) DEFAULT NULL COMMENT '图片md5值',
  `suffix` varchar(255) DEFAULT NULL COMMENT '图片后缀名',
  `type` tinyint(1) DEFAULT NULL COMMENT '图片类型 1 博客类型 2 全局配置',
  `is_deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除 0 正常 1 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `save_path` (`save_path`),
  KEY `md5` (`md5`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='图片表';

-- ----------------------------
-- Records of rs_image
-- ----------------------------
BEGIN;
INSERT INTO `rs_image` VALUES (4, 'f92643e2a50141c0aac3e6a21846711e', '毕业证书.jpg', 'http://localhost:8001/re/static/images/f92643e2a50141c0aac3e6a21846711e.jpg', '2021-01-28 01:07:19', '2021-01-28 01:07:19', 1042686, 'E:\\re\\re\\project\\static\\images\\f92643e2a50141c0aac3e6a21846711e.jpg', '3f75f730965ba4bc3c544593cd15bf2e', 'jpg', 2, 0);
INSERT INTO `rs_image` VALUES (5, 'f184b437913b4c3fa23390d131c8cab9', 'Java_logo.jpeg', 'http://localhost:8001/re/static/images/f184b437913b4c3fa23390d131c8cab9.jpeg', '2021-02-21 23:38:05', '2021-02-21 23:38:05', 12621, '/Users/lingjiatong/code/re/re/project/static/images/f184b437913b4c3fa23390d131c8cab9.jpeg', '6acead3ea6ac47d6cce4462744930d78', 'jpeg', 2, 0);
INSERT INTO `rs_image` VALUES (6, 'bc25cc41e71f455ebec98eebd26aab27', 'mybatis-plus.jpeg', 'http://localhost:8001/re/static/images/bc25cc41e71f455ebec98eebd26aab27.jpeg', '2021-02-21 23:39:16', '2021-02-21 23:39:16', 16909, '/Users/lingjiatong/code/re/re/project/static/images/bc25cc41e71f455ebec98eebd26aab27.jpeg', '4fe1725c20fad7cdce2fe861261de95c', 'jpeg', 2, 0);
COMMIT;

-- ----------------------------
-- Table structure for rs_link
-- ----------------------------
DROP TABLE IF EXISTS `rs_link`;
CREATE TABLE `rs_link` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '链接id',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '链接所指向的url',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '链接标题',
  `article_id` int DEFAULT NULL COMMENT '引用博客id（只有当是博客链接时存在此字段）',
  `is_friend_link` tinyint(1) DEFAULT NULL COMMENT '是否是友链 0 是 1 不是',
  `is_invalid_link` tinyint(1) DEFAULT NULL COMMENT '是否是无效链接 0 是 1 不是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '连接tab栏图标',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='链接表';

-- ----------------------------
-- Records of rs_link
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for rs_music
-- ----------------------------
DROP TABLE IF EXISTS `rs_music`;
CREATE TABLE `rs_music` (
  `id` int NOT NULL COMMENT '主键，自增',
  `url` varchar(255) DEFAULT NULL COMMENT '访问地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='音乐表';

-- ----------------------------
-- Records of rs_music
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '系统配置表id',
  `description` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置名',
  `key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置键',
  `value` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_config` VALUES (1, '用户头像图片路径', 'DEFAULT_AVATAR_URL', 'https://ftp.ljtnono.cn/re/images/avatar.png');
INSERT INTO `sys_config` VALUES (2, '后端登陆页背景图片', 'ADMIN_BACKGROUND_URL', 'https://www.baidu.com');
INSERT INTO `sys_config` VALUES (3, '后端菜单栏上面的icon', 'NAV_MENU_ICON_URL', '1');
INSERT INTO `sys_config` VALUES (4, '后端菜单栏上面缩小后的icon', 'NAV_MENU_ICON_MINI_URL', '1');
INSERT INTO `sys_config` VALUES (5, '版权信息', 'COPYRIGHT', '本站所有标注为原创的文章，转载请标明出处。\n本站所有转载的文章，一般都会在文章中注明原文出处。\n所有转载的文章皆来源于互联网，若因此对原作者造成侵权，烦请原作者告知，我会立刻删除相关内容。');
INSERT INTO `sys_config` VALUES (6, '网站备案号', 'WEBSITE_RECORD_NUMBER', '1');
INSERT INTO `sys_config` VALUES (7, '免责声明', 'DISCLAIMER', '1、本网站属于个人非赢利性质的网站，所有转载的文章都以遵循原作者的版权声明注明了文章来源。\n2、如果原文没有版权声明，按照目前互联网开放的原则，本网站将在不通知作者的情况下转载文章。\n3、如果原文明确注明“禁止转载”，本网站将不会转载。\n4、如果本网站转载的文章不符合作者的版权声明或者作者不想让本网站转载您的文章，请邮件告知，本站将会在第一时间删除相关信息！\n5、本网站转载文章仅为留作备份和知识点分享的目的。\n6、本网站将尽力确保所提供信息的准确性及可靠性，但不保证信息的正确性和完整性，且不对因信息的不正确或遗漏导致的任何损失或损害承担相关责任。\n7、本网站所发布、转载的文章，其版权均归原作者所有。如其他自媒体、网站或个人从本网站下载使用，请在转载有关文章时务必尊重该文章的著作权，保留本网站注明的“原文来源”或者自行去原文处复制版权声明，并自负版权等法律责任。\n8、本网站的所有原创文章皆可以任意转载，但转载时务必请注明出处。\n9、尊重原创，知识共享！');
INSERT INTO `sys_config` VALUES (8, '博主网名', 'AUTHOR_NICK_NAME', '最后的疼爱');
INSERT INTO `sys_config` VALUES (9, '博主简介', '1', '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `id` int NOT NULL COMMENT '主键id，自增',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '定时任务名',
  `cron` varchar(255) DEFAULT NULL COMMENT 'cron表达式',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `effective_status` tinyint(1) NOT NULL COMMENT '启用状态0 启用 1 不启用',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 0 正常 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id，自增',
  `user_id` int DEFAULT NULL COMMENT '日志操作用户id',
  `type` tinyint(1) DEFAULT NULL COMMENT '日志类型 1 用户日志 2 系统日志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `op_name` varchar(255) NOT NULL COMMENT '操作名',
  `op_detail` varchar(255) NOT NULL COMMENT '操作详情',
  `result` tinyint(1) DEFAULT NULL COMMENT '操作结果 1 成功  2 失败',
  `ip` varchar(255) DEFAULT NULL COMMENT '操作者ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户消息表id',
  `user_id` int NOT NULL COMMENT '用户id',
  `message` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '消息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `status` tinyint(1) NOT NULL COMMENT '消息状态 0 未读 1 已读',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 已删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_sys_user_message_sys_user_1` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户消息表';

-- ----------------------------
-- Records of sys_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '权限表id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名',
  `type` tinyint(1) NOT NULL COMMENT '权限类型 0 菜单 1 具体权限或按钮',
  `parent_id` int NOT NULL COMMENT '父权限id，顶层父菜单为-1',
  `expression` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限表达式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6085 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
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
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色表id',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '超级管理员', '拥有所有权限', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0);
INSERT INTO `sys_role` VALUES (2, '测试', '拥有除了系统管理之外的权限', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0);
INSERT INTO `sys_role` VALUES (3, '游客', '只拥有各模块查看权限', '2020-08-24 00:30:21', '2020-08-24 00:30:21', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色权限表id',
  `role_id` int NOT NULL COMMENT '角色id',
  `permission_id` int NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_sys_role_permission_sys_role_1` (`role_id`) USING BTREE,
  KEY `fk_sys_role_permission_sys_permission_1` (`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色权限表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
BEGIN;
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
COMMIT;

-- ----------------------------
-- Table structure for sys_timeline
-- ----------------------------
DROP TABLE IF EXISTS `sys_timeline`;
CREATE TABLE `sys_timeline` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '时间轴id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '时间轴内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='时间轴';

-- ----------------------------
-- Records of sys_timeline
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号码',
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户邮箱',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  `is_deleted` tinyint(1) NOT NULL COMMENT '是否删除 1 删除 0 正常',
  `avatar_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户头像访问url',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username_deleted` (`username`,`is_deleted`) USING BTREE COMMENT '不能同时存在两个同名用户',
  KEY `username_phone_email` (`username`,`phone`,`email`) USING BTREE COMMENT '模糊查询索引'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'lingjiatong', '80cea81e681679a81634e2b1846e6cb8', '16333333333', '935188400@qq.com', '2020-08-24 00:42:24', '2021-01-02 15:13:21', 0, 'https://ftp.ljtnono.cn/re/images/avatar.png');
INSERT INTO `sys_user` VALUES (2, 'ljtnono', '80cea81e681679a81634e2b1846e6cb8', '15337106753', '935188400@qq.com', '2020-11-20 23:25:33', '2021-01-24 15:05:12', 1, 'https://ftp.ljtnono.cn/re/images/avatar.png');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户角色表id',
  `user_id` int NOT NULL COMMENT '用户id',
  `role_id` int NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_sys_user_role_sys_user_1` (`user_id`) USING BTREE,
  KEY `fk_sys_user_role_sys_role_1` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 2);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
