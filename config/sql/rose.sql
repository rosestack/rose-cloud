-- -------------------------------------------------------------
-- TablePlus 6.1.2(568)
--
-- https://tableplus.com/
--
-- Database: chensoul
-- Generation Time: 2025-03-28 17:37:09.7900
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

create database if not exists chensoul;

use chensoul;

DROP TABLE IF EXISTS `sys_announce`;
CREATE TABLE `sys_announce` (
  `id` bigint NOT NULL COMMENT '主键',
  `title` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
  `content` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '内容',
  `type` smallint DEFAULT NULL COMMENT '类型',
  `release_time` datetime DEFAULT NULL COMMENT '发布时间',
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户ID',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公告表';

DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编码',
  `name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用 ';

DROP TABLE IF EXISTS `sys_client`;
CREATE TABLE `sys_client` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '编码',
  `client_secret` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密钥',
  `resource_ids` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源ServerID',
  `scope` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作用域',
  `authorized_grant_types` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权方式',
  `web_server_redirect_uri` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调地址',
  `authorities` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限列表',
  `access_token_validity` int DEFAULT NULL COMMENT '请求令牌有效时间',
  `refresh_token_validity` int DEFAULT NULL COMMENT '刷新令牌有效时间',
  `additional_information` text COLLATE utf8mb4_general_ci COMMENT '扩展信息',
  `autoapprove` int DEFAULT NULL COMMENT '是否自动放行',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户端 ';

DROP TABLE IF EXISTS `sys_credential`;
CREATE TABLE `sys_credential` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
  `authority` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `activate_token` datetime DEFAULT NULL COMMENT '激活 token',
  `reset_token` datetime DEFAULT NULL COMMENT ' 重置 token',
  `extra` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 额外信息',
  `enabled` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `version` smallint DEFAULT '0' COMMENT '是否删除',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户凭证';

DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` bigint DEFAULT '0' COMMENT '父主键',
  `trace_id` varchar(2000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '祖级列表',
  `name` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门名',
  `full_name` varchar(45) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门全称',
  `sort` int DEFAULT NULL COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户ID',
  `version` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门表';

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `trace_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `server_ip` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remote_ip` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remote_location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_agent` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `request_uri` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `request_method` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `response_result` text COLLATE utf8mb4_general_ci,
  `cost_time` bigint NOT NULL,
  `success` smallint DEFAULT '1' COMMENT '是否成功',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日志表';

DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `id` bigint NOT NULL COMMENT '主键',
  `code` varchar(12) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位编号',
  `name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位名称',
  `type` smallint DEFAULT NULL COMMENT '岗位类型',
  `sort` int DEFAULT NULL COMMENT '岗位排序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位描述',
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户ID',
  `version` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位表';

DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint DEFAULT NULL COMMENT '客户端ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父级ID',
  `name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `icon` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `url` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'URL',
  `open_mode` int DEFAULT NULL COMMENT '打开方式 0默认单页打开，1打开新页面，2iframe打开',
  `permission` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识码',
  `type` int DEFAULT NULL COMMENT '类型 0：目录   1：菜单   2：uri',
  `sort` int DEFAULT NULL COMMENT '排序',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资源 ';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名',
  `code` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色编码',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色 ';

DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `resource_id` bigint DEFAULT NULL COMMENT '资源ID',
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色资源表';

DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `domain` varchar(512) DEFAULT NULL COMMENT '域名',
  `contact_name` varchar(512) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(32) DEFAULT NULL COMMENT '联系人手机号',
  `address` varchar(257) DEFAULT NULL COMMENT '联系人地址',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='租户表';

DROP TABLE IF EXISTS `sys_tenant_app`;
CREATE TABLE `sys_tenant_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint DEFAULT NULL COMMENT '应用ID',
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户ID',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租户应用表';

DROP TABLE IF EXISTS `sys_tenant_user`;
CREATE TABLE `sys_tenant_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `nickname` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 昵称',
  `email` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 邮箱',
  `avatar` varchar(512) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 头像',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `post_id` bigint DEFAULT NULL COMMENT '岗位ID',
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租户用户表';

DROP TABLE IF EXISTS `sys_tenant_user_role`;
CREATE TABLE `sys_tenant_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `tenant_id` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租户用户角色表';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '手机号',
  `extra` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上次登录IP',
  `authority` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上次登录IP',
  `last_login_ip` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '上次登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  `status` smallint DEFAULT '1' COMMENT '是否可用 0不可用，1可用',
  `version` smallint DEFAULT '0' COMMENT '是否删除',
  `is_deleted` smallint DEFAULT '0' COMMENT '是否删除',
  `created_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户 ';

INSERT INTO `sys_app` (`id`, `code`, `name`, `remark`, `status`, `is_deleted`, `created_by`, `created_time`, `updated_by`, `updated_time`) VALUES
(1, 'rose', '内部业务', NULL, 1, 0, NULL, '2025-03-28 09:24:38', NULL, '2025-03-28 09:24:38');

INSERT INTO `sys_client` (`id`, `client_id`, `client_secret`, `resource_ids`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `status`, `is_deleted`, `created_by`, `created_time`, `updated_by`, `updated_time`) VALUES
(1, 'rose', 'rose', NULL, 'server', 'password,authorization_code,client_credentials,refresh_token', NULL, NULL, 600, 43200, NULL, 1, 1, 0, NULL, '2025-03-28 09:24:38', NULL, '2025-03-28 09:24:38');

INSERT INTO `sys_role` (`id`, `name`, `code`, `remark`, `status`, `is_deleted`, `tenant_id`, `created_by`, `created_time`, `updated_by`, `updated_time`) VALUES
(1, '超级管理员', 'super_admin', NULL, 1, 0, '000000', NULL, '2025-03-28 09:24:38', NULL, '2025-03-28 09:24:38');

INSERT INTO `sys_user` (`id`, `name`, `phone`, `extra`, `authority`, `last_login_ip`, `last_login_time`, `status`, `version`, `is_deleted`, `created_by`, `created_time`, `updated_by`, `updated_time`) VALUES
(1, 'admin', '1380000000', NULL, 'SYS_ADMIN', '1', NULL, 1, 0, 0, NULL, '2025-03-28 09:24:38', NULL, '2025-03-28 09:24:38');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
