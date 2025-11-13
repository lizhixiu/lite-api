/*
 Navicat Premium Dump SQL

 Source Server         : xc-mac
 Source Server Type    : MySQL
 Source Server Version : 50733 (5.7.33-0ubuntu0.18.04.1)
 Source Host           : localhost:3306
 Source Schema         : lite_boot

 Target Server Type    : MySQL
 Target Server Version : 50733 (5.7.33-0ubuntu0.18.04.1)
 File Encoding         : 65001

 Date: 11/11/2025 01:30:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lite_config
-- ----------------------------
DROP TABLE IF EXISTS `lite_config`;
CREATE TABLE `lite_config` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `pid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父节点ID',
  `type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置类型编码',
  `type_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置类型名称',
  `code_txt` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置编码',
  `name_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置名称',
  `value_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置值',
  `rec_sort_num` decimal(5,0) DEFAULT NULL COMMENT '排序号',
  `rec_remarks_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `rec_valid_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否有效',
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `rec_sys_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否系统保留',
  `rec_create_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_update_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `rec_update_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `rec_update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置';

-- ----------------------------
-- Records of lite_config
-- ----------------------------
BEGIN;
INSERT INTO `lite_config` (`id`, `pid`, `type_code`, `type_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('2548ccd2e2434ea88af446133c2f40bd', '-1', 'CLASS', '目录', 'SYS', '系统配置', NULL, 5, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:52:30', '1', '轩晨', '2025-11-09 13:30:14');
INSERT INTO `lite_config` (`id`, `pid`, `type_code`, `type_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('77f79671841b4f708197ca434aed718f', '2548ccd2e2434ea88af446133c2f40bd', 'CONFIG', '配置', 'super-password', '通用密码', 'xclite', 5, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 01:52:53', '1', '轩晨', '2025-11-09 13:21:09');
INSERT INTO `lite_config` (`id`, `pid`, `type_code`, `type_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('b6fc37ed456f4cd2b3e812f4d85dd443', '2548ccd2e2434ea88af446133c2f40bd', 'CONFIG', '配置', 'verify-code.enable', '是否验证“验证码”', 'false', 10, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:53:10', '1', '轩晨', '2025-11-09 13:21:18');
COMMIT;

-- ----------------------------
-- Table structure for lite_dept
-- ----------------------------
DROP TABLE IF EXISTS `lite_dept`;
CREATE TABLE `lite_dept` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `pid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父节点id',
  `org_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业ID',
  `org_ref` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业名称',
  `code_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门编码',
  `name_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门名称',
  `short_name_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门简称',
  `type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门类型编码',
  `type_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门类型名称',
  `rec_sort_num` decimal(5,0) DEFAULT NULL COMMENT '排序号',
  `rec_remarks_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `rec_valid_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否有效',
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `rec_sys_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否系统保留',
  `rec_create_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_update_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `rec_update_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `rec_update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- ----------------------------
-- Records of lite_dept
-- ----------------------------
BEGIN;
INSERT INTO `lite_dept` (`id`, `pid`, `org_id`, `org_ref`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('1394e5ae74904c4595c1b7d22f2bf635', '1', '1', 'XcLite科技有限公司', 'LDBZ', '领导班子', '公司领导', '01', '内部部门', 5, NULL, '1', '0', '0', '1', '轩晨', '2025-11-04 23:26:53', '1', '轩晨', '2025-11-10 00:35:52');
INSERT INTO `lite_dept` (`id`, `pid`, `org_id`, `org_ref`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('21c69bcba3e6485f886f4f764d9ee8c1', '1', '1', 'XcLite科技有限公司', 'CWZCB', '财务资产部', '财务部', '01', '内部部门', 20, NULL, '1', '0', '0', '1', '轩晨', '2025-11-04 23:27:29', '1', '轩晨', '2025-11-10 00:36:43');
INSERT INTO `lite_dept` (`id`, `pid`, `org_id`, `org_ref`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('29c33c18c60d44fb9d75f36cff76f1a1', '36509c401438490f8fde8be375b7d1fc', '36509c401438490f8fde8be375b7d1fc', '华东分公司', 'YFB', '研发部', NULL, '01', '内部部门', 25, NULL, '1', '0', '0', '1', '轩晨', '2025-11-04 23:55:15', '1', '轩晨', '2025-11-10 00:36:52');
INSERT INTO `lite_dept` (`id`, `pid`, `org_id`, `org_ref`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('5353a532e6a64f54a168a80d9943d757', '36509c401438490f8fde8be375b7d1fc', '36509c401438490f8fde8be375b7d1fc', '华东分公司', 'XSB', '销售部', NULL, '01', '内部部门', 30, NULL, '1', '0', '0', '1', '轩晨', '2025-11-04 23:55:32', '1', '轩晨', '2025-11-10 00:37:01');
INSERT INTO `lite_dept` (`id`, `pid`, `org_id`, `org_ref`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('6c24296ebccf4d5781ba897c8cf25c9d', '1', '1', 'XcLite科技有限公司', 'GHBGS', '工会办公室', '办公室', '01', '内部部门', 10, NULL, '1', '0', '0', '1', '轩晨', '2025-11-04 23:27:03', '1', '轩晨', '2025-11-10 00:36:08');
INSERT INTO `lite_dept` (`id`, `pid`, `org_id`, `org_ref`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('f765001fb63d47b686358a741b0b7aeb', '1', '1', 'XcLite科技有限公司', 'ZHGLB', '综合管理部', '综合部', '01', '内部部门', 15, NULL, '1', '0', '0', '1', '轩晨', '2025-11-04 23:27:10', '1', '轩晨', '2025-11-10 00:36:30');
COMMIT;

-- ----------------------------
-- Table structure for lite_nav
-- ----------------------------
DROP TABLE IF EXISTS `lite_nav`;
CREATE TABLE `lite_nav` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单id',
  `code_txt` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航编码',
  `pid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父节点id',
  `name_txt` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航名称',
  `alias_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航别名',
  `icon_txt` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航图标',
  `component_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件',
  `url_txt` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航url',
  `type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航类型编码',
  `type_ref` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航类型',
  `link_type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航链接类型编码',
  `link_type_ref` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航链接类型',
  `tips_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '导航提示说明',
  `open_mode_code` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '打开方式，0：iframe  1：新标签页',
  `show_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否显示：1显示，0不显示',
  `keep_alive_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否缓存：1缓存，0不缓存',
  `rec_sort_num` decimal(5,0) DEFAULT NULL COMMENT '排序号',
  `rec_remarks_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `rec_valid_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否有效',
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `rec_sys_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否系统保留',
  `rec_create_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_update_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `rec_update_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `rec_update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品导航';

-- ----------------------------
-- Records of lite_nav
-- ----------------------------
BEGIN;
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('17480b684295458aa42c541c9a852320', 'liteUserTvList', '3963312abab54402897e6f3c73123401', '用户管理', NULL, 'el-icon-user', 'lite/user/liteUser/liteUserTvList', '/lite/user/liteUser/liteUserTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 20, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:06:04', '1', '轩晨', '2025-11-09 23:26:49');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('23dea935ca114381b3dcf946fe2c4adb', 'liteLogOperTvList', '8a51738ce9954e1c971ac4a545ae80fc', '操作日志', NULL, 'el-icon-notebook', 'lite/log/liteLogOper/liteLogOperTvList', '/lite/log/liteLogOper/liteLogOperTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 26, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:06:24', '1', '轩晨', '2025-11-11 00:58:01');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('28f049ac35f84bbcb86e85caa96d4fbc', 'config', 'cec8e274d178493f90610aa9c8a41e01', '配置管理', NULL, 'el-icon-setting', NULL, '/config', 'CLASS', '目录', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 12, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:04:49', '1', '轩晨', '2025-11-09 23:26:04');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('3963312abab54402897e6f3c73123401', 'org', 'cec8e274d178493f90610aa9c8a41e01', '组织机构', NULL, 'el-icon-office-building', NULL, '/org', 'CLASS', '目录', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 10, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:04:17', '1', '轩晨', '2025-11-09 23:24:34');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('49fcb1b839b34c4a98f83a798664436e', 'liteParaClass', '28f049ac35f84bbcb86e85caa96d4fbc', '系统参数分类', NULL, 'el-icon-finished', 'lite/para/liteParaClass/liteParaClassTvList', '/lite/para/liteParaClass/liteParaClassTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 32, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:06:44', '1', '轩晨', '2025-11-09 23:28:23');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('62e60d86ecf6472eaa151847986d1818', 'litePostTvList', '3963312abab54402897e6f3c73123401', '岗位管理', NULL, 'el-icon-ship', 'lite/post/litePost/litePostTvList', '/lite/post/litePost/litePostTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 19, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:05:56', '1', '轩晨', '2025-11-09 23:26:39');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('8a51738ce9954e1c971ac4a545ae80fc', 'log', 'cec8e274d178493f90610aa9c8a41e01', '日志管理', NULL, 'el-icon-clock', NULL, '/log', 'CLASS', '目录', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 11, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:04:36', '1', '轩晨', '2025-11-09 23:25:35');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('9749b4c381164a548b9e44f5da8da4cb', 'liteDeptTvList', '3963312abab54402897e6f3c73123401', '部门管理', NULL, 'sc-icon-organization', 'lite/dept/liteDept/liteDeptTvList', '/lite/dept/liteDept/liteDeptTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 18, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 02:05:47', '1', '轩晨', '2025-11-09 23:26:31');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('996bc6f303de4bb8b3a4632a55bfc64c', 'litePara', '28f049ac35f84bbcb86e85caa96d4fbc', '系统参数', NULL, 'el-icon-expand', 'lite/para/litePara/liteParaTvList', '/lite/para/litePara/liteParaTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 33, NULL, '1', '0', '1', '1', '轩晨', '2025-11-09 17:50:22', '1', '轩晨', '2025-11-09 23:28:32');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('ae9451ec9e9349f6a031b1269badb672', 'liteLogLoginTvList', '8a51738ce9954e1c971ac4a545ae80fc', '登录日志', NULL, 'el-icon-aim', 'lite/log/liteLogLogin/liteLogLoginTvList', '/lite/log/liteLogLogin/liteLogLoginTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 25, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:06:15', '1', '轩晨', '2025-11-11 00:56:34');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('bd99d88dab414980a60b31edf8580ce8', 'liteNavTvList', '28f049ac35f84bbcb86e85caa96d4fbc', '导航管理', NULL, 'el-icon-menu', 'lite/nav/liteNav/liteNavTvList', '/lite/nav/liteNav/liteNavTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 31, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:06:35', '1', '轩晨', '2025-11-09 23:28:11');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('cec8e274d178493f90610aa9c8a41e01', 'SYS', '-1', '基础模块', NULL, 'el-icon-bowl', NULL, NULL, 'MODULE', '模块', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 5, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:02:35', '1', '轩晨', '2025-11-09 23:29:13');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('e9152bdf604742d7bc30274b16c035df', 'liteConfigTvList', '28f049ac35f84bbcb86e85caa96d4fbc', '配置中心', NULL, 'el-icon-set-up', 'lite/config/liteConfig/liteConfigTvList', '/lite/config/liteConfig/liteConfigTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 35, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:06:51', '1', '轩晨', '2025-11-09 23:28:42');
INSERT INTO `lite_nav` (`id`, `code_txt`, `pid`, `name_txt`, `alias_txt`, `icon_txt`, `component_txt`, `url_txt`, `type_code`, `type_ref`, `link_type_code`, `link_type_ref`, `tips_txt`, `open_mode_code`, `show_flag`, `keep_alive_flag`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('fed1fa4f704045a6a1cdc710ca0f0897', 'liteOrgTvList', '3963312abab54402897e6f3c73123401', '机构管理', NULL, 'el-icon-school', 'lite/org/liteOrg/liteOrgTvList', '/lite/org/liteOrg/liteOrgTvList', 'NAV', '导航', 'SYSTEM', '系统默认', NULL, NULL, '1', '0', 17, NULL, '1', '0', '1', '1', '轩晨', '2025-11-03 02:05:36', '1', '轩晨', '2025-11-09 23:26:21');
COMMIT;

-- ----------------------------
-- Table structure for lite_org
-- ----------------------------
DROP TABLE IF EXISTS `lite_org`;
CREATE TABLE `lite_org` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `pid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父节点id',
  `code_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业编码',
  `name_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业名称',
  `short_name_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业简称',
  `credit_code_txt` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业统一社会信用代码',
  `type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业类型编码',
  `type_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业类型名称',
  `rec_sort_num` decimal(5,0) DEFAULT NULL COMMENT '排序号',
  `rec_remarks_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `rec_valid_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否有效',
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `rec_sys_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否系统保留',
  `rec_create_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_update_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `rec_update_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `rec_update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织机构表';

-- ----------------------------
-- Records of lite_org
-- ----------------------------
BEGIN;
INSERT INTO `lite_org` (`id`, `pid`, `code_txt`, `name_txt`, `short_name_txt`, `credit_code_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('1', '-1', 'XcLite', 'XcLite科技有限公司', 'XcLite', '91350100M000100000', '01', '集团公司', 5, NULL, '1', '0', '0', '1', '轩晨', '2025-11-02 23:15:22', '1', '轩晨', '2025-11-10 00:00:57');
INSERT INTO `lite_org` (`id`, `pid`, `code_txt`, `name_txt`, `short_name_txt`, `credit_code_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('36509c401438490f8fde8be375b7d1fc', '1', 'EastChina', '华东分公司', '华东分公司', '91350100M000100001', '03', '分公司', 10, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:03:03', '1', '轩晨', '2025-11-10 00:01:04');
COMMIT;

-- ----------------------------
-- Table structure for lite_para
-- ----------------------------
DROP TABLE IF EXISTS `lite_para`;
CREATE TABLE `lite_para` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `pid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父节点ID',
  `class_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参数分类ID',
  `class_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类编码',
  `class_ref` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类名称',
  `code_txt` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参数编码',
  `name_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参数名称',
  `value_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参数值',
  `rec_sort_num` decimal(5,0) DEFAULT NULL COMMENT '排序号',
  `rec_remarks_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `rec_valid_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否有效',
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `rec_sys_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否系统保留',
  `rec_create_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_update_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `rec_update_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `rec_update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参数表';

-- ----------------------------
-- Records of lite_para
-- ----------------------------
BEGIN;
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('0a57ea0b8506418aa8462548d42e2887', NULL, 'f87704e926704c35bbb24a0110385fec', 'SYS_ORG_TYPE', '企业类型', '02', '子公司', NULL, 2, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:58:10', '1', '轩晨', '2025-11-10 00:00:19');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('34d6e1993814443d8bb33822ee90c57d', NULL, '3f86ddb5ecf2495390d129cb486116be', 'SYS_LINK_TYPE', '链接类型', 'SYSTEM', '内部链接', NULL, 1, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:18:21', '1', '轩晨', '2025-11-09 23:16:54');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('5ba465582a444f98baf157fcce1adc6a', NULL, '7c6a4fb06423426fac5af222171afb6c', 'SYS_CONFIG_TYPE', '系统配置类型', 'CLASS', '目录', NULL, 11, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:46:46', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('681b20b01c554aaeb85335ba150cf643', NULL, '88194651740b452390aa8490cd2e81b8', 'SYS_PARA_TYPE', '系统参数类型', 'LIST', '列表参数', NULL, 6, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:45:30', '1', '轩晨', '2025-11-03 01:45:06');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('6eb63905451748f2a200f0999579c1d6', NULL, 'bed2ac211c884ca69502b999700d4bc4', 'SYS_USER_GENDER_TYPE', '性别类型', 'HIDDEN', '隐藏', NULL, 3, NULL, '1', '0', NULL, '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:33:50', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:33:22');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('710ccd98077b4955a0799c085c133d93', NULL, '8d9517c596c84bc1a65c8f56033adf38', 'SYS_POST_TYPE', '岗位类型', '02', '中层', NULL, 18, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-10 00:39:16', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('77bae63e012a49c399b75512af49c123', NULL, 'f87704e926704c35bbb24a0110385fec', 'SYS_ORG_TYPE', '企业类型', '01', '集团公司', NULL, 1, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:57:50', '1', '轩晨', '2025-11-10 00:00:10');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('828eba3bf351434ba72d674067876aa1', NULL, 'f87704e926704c35bbb24a0110385fec', 'SYS_ORG_TYPE', '企业类型', '03', '分公司', NULL, 3, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:58:21', '1', '轩晨', '2025-11-10 00:00:31');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('8d95a83c910d49c38405045e8e0ccba1', NULL, '8d9517c596c84bc1a65c8f56033adf38', 'SYS_POST_TYPE', '岗位类型', '03', '底层', NULL, 19, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-10 00:39:23', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('8fdc39ee6fa64d3d9bd600b98c2d0946', NULL, 'e5cae8014f934f9ba9eb44d2a030761d', 'SYS_USER_TYPE', '用户类型', 'USER', '普通用户', NULL, 2, NULL, '1', '0', NULL, '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:32:10', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:31:59');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('9388c0be076d4452b72fcb85d79e848a', NULL, '7c6a4fb06423426fac5af222171afb6c', 'SYS_CONFIG_TYPE', '系统配置类型', 'CONFIG', '配置', NULL, 12, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:47:00', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('93eb4467eb774c66b4a601bc8e442b85', NULL, '1b8a9fe59e4c4f8a993f12885b9cc0f1', 'SYS_NAV_TYPE', '导航类型', 'NAV', '导航', NULL, 3, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:13:47', '1', '轩晨', '2025-11-09 23:13:29');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('9b21600d645c462f8f1e2da6ae6be3fe', NULL, '2c9aa95a761347d0a68f7ff385df666b', 'SYS_NAV_OPEN_MODE', '打开方式', 'SYSTEM', '系统默认', NULL, 1, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:21:56', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('9bb3d1c6feae47a69d205f9eeaf9c675', NULL, 'dc647e42bafe42e8b96c819b046af190', 'SYS_USER_EFFECT_TYPE', '用户生效类型', 'TEMPORARY', '临时有效', NULL, 25, NULL, '1', '0', NULL, '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:37:10', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('a062c984fde040878d7703e3effb4300', NULL, '8d9517c596c84bc1a65c8f56033adf38', 'SYS_POST_TYPE', '岗位类型', '01', '高层', NULL, 17, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-10 00:39:09', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('a3dcb83135fa41a0af48facb51f8bbd9', NULL, '1b8a9fe59e4c4f8a993f12885b9cc0f1', 'SYS_NAV_TYPE', '导航类型', 'CLASS', '目录', NULL, 2, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:13:38', '1', '轩晨', '2025-11-09 23:13:29');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('c22cdb0a365e4a81ba00e5f4319a2459', NULL, '90d18e8b76b1441fbcdf7911363405e8', 'SYS_DEPT_TYPE', '部门类型', '02', '外部机构', NULL, 2, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-10 00:35:28', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('dcdb3b3aab8f4872b16751e22cc49627', NULL, 'bed2ac211c884ca69502b999700d4bc4', 'SYS_USER_GENDER_TYPE', '性别类型', 'WOMEN', '女', NULL, 2, NULL, '1', '0', NULL, '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:32:39', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:33:22');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('e1608d686dbd4a59863dd49db3837666', NULL, '1b8a9fe59e4c4f8a993f12885b9cc0f1', 'SYS_NAV_TYPE', '导航类型', 'MODULE', '模块', NULL, 1, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:10:32', '1', '轩晨', '2025-11-09 23:13:29');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('eaa841e3835248079252df6db787df82', NULL, '3f86ddb5ecf2495390d129cb486116be', 'SYS_LINK_TYPE', '链接类型', 'EXT', '外部链接', NULL, 2, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-09 23:16:47', '1', '轩晨', '2025-11-09 23:16:54');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('ed8474c021a2403c9347556852c8c8e9', NULL, 'bed2ac211c884ca69502b999700d4bc4', 'SYS_USER_GENDER_TYPE', '性别类型', 'MEN', '男', NULL, 1, NULL, '1', '0', NULL, '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:32:28', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:33:18');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('edfd9321530949f38d43db4b9084f6c1', NULL, 'dc647e42bafe42e8b96c819b046af190', 'SYS_USER_EFFECT_TYPE', '用户生效类型', 'PERMANENT', '长期有效', NULL, 24, NULL, '1', '0', NULL, '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:36:50', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('f6ef5169565946cbb6533c5c02a0778d', NULL, '88194651740b452390aa8490cd2e81b8', 'SYS_PARA_TYPE', '系统参数类型', 'CLASS', '目录', NULL, 5, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:39:45', '1', '轩晨', '2025-11-03 01:45:06');
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('f8f325006a154b70bf39457a496859be', NULL, '90d18e8b76b1441fbcdf7911363405e8', 'SYS_DEPT_TYPE', '部门类型', '01', '内部部门', NULL, 1, NULL, '1', '0', NULL, '1', '轩晨', '2025-11-10 00:35:19', NULL, NULL, NULL);
INSERT INTO `lite_para` (`id`, `pid`, `class_id`, `class_code`, `class_ref`, `code_txt`, `name_txt`, `value_txt`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('f99c17026cb9407aad1bb383164c1295', NULL, 'e5cae8014f934f9ba9eb44d2a030761d', 'SYS_USER_TYPE', '用户类型', 'SYSTEM', '系统用户', NULL, 1, NULL, '1', '0', NULL, '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:31:53', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:31:59');
COMMIT;

-- ----------------------------
-- Table structure for lite_para_class
-- ----------------------------
DROP TABLE IF EXISTS `lite_para_class`;
CREATE TABLE `lite_para_class` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `pid` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父节点ID',
  `code_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类编码',
  `name_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类名称',
  `type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型编码',
  `type_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型名称',
  `rec_sort_num` decimal(5,0) DEFAULT NULL COMMENT '排序号',
  `rec_remarks_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `rec_valid_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否有效',
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `rec_sys_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否系统保留',
  `rec_create_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_update_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `rec_update_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `rec_update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参数分类';

-- ----------------------------
-- Records of lite_para_class
-- ----------------------------
BEGIN;
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('1b8a9fe59e4c4f8a993f12885b9cc0f1', '90f77e38576c453ea4c2d327317c6f8b', 'SYS_NAV_TYPE', '导航类型', 'LIST', '列表参数', 1, NULL, '1', '0', '0', '1', '轩晨', '2025-11-09 19:04:00', '1', '轩晨', '2025-11-09 19:06:03');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('2c9aa95a761347d0a68f7ff385df666b', '90f77e38576c453ea4c2d327317c6f8b', 'SYS_NAV_OPEN_MODE', '打开方式', 'LIST', '列表参数', 3, NULL, '1', '0', '0', '1', '轩晨', '2025-11-09 19:04:53', '1', '轩晨', '2025-11-09 19:06:13');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('3f86ddb5ecf2495390d129cb486116be', '90f77e38576c453ea4c2d327317c6f8b', 'SYS_LINK_TYPE', '链接类型', 'LIST', '列表参数', 2, NULL, '1', '0', '0', '1', '轩晨', '2025-11-09 19:04:31', '1', '轩晨', '2025-11-09 19:06:09');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('4854b83501b84951b5bf1c4da82243f7', '-1', 'SYS', '基础平台', 'CLASS', '目录', 5, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:27:23', '1', '轩晨', '2025-11-09 18:43:38');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('7c6a4fb06423426fac5af222171afb6c', '85e9f88166a448adb95075b2010ee5b3', 'SYS_CONFIG_TYPE', '系统配置类型', 'LIST', '列表参数', 16, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:28:57', '1', '轩晨', '2025-11-09 18:43:27');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('85e9f88166a448adb95075b2010ee5b3', '4854b83501b84951b5bf1c4da82243f7', 'CONFIG', '配置管理', 'CLASS', '目录', 10, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:27:55', '1', '轩晨', '2025-11-09 18:43:34');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('88194651740b452390aa8490cd2e81b8', '85e9f88166a448adb95075b2010ee5b3', 'SYS_PARA_TYPE', '系统参数类型', 'LIST', '列表参数', 15, NULL, '1', '0', '0', '1', '轩晨', '2025-11-03 01:28:25', '1', '轩晨', '2025-11-09 18:43:20');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('8d9517c596c84bc1a65c8f56033adf38', 'fb37efc3dcca45c8a52c604abf27c2b4', 'SYS_POST_TYPE', '岗位类型', 'LIST', '列表参数', 3, NULL, '1', '0', '1', '1', '轩晨', '2025-11-10 00:38:46', NULL, NULL, NULL);
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('90d18e8b76b1441fbcdf7911363405e8', 'fb37efc3dcca45c8a52c604abf27c2b4', 'SYS_DEPT_TYPE', '部门类型', 'LIST', '列表参数', 2, NULL, '1', '0', '1', '1', '轩晨', '2025-11-10 00:34:53', NULL, NULL, NULL);
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('90f77e38576c453ea4c2d327317c6f8b', '4854b83501b84951b5bf1c4da82243f7', 'NAV', '导航管理', 'CLASS', '目录', 1, NULL, '1', '0', '0', '1', '轩晨', '2025-11-09 19:05:50', '1', '轩晨', '2025-11-09 19:06:24');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('bed2ac211c884ca69502b999700d4bc4', 'fb37efc3dcca45c8a52c604abf27c2b4', 'SYS_USER_GENDER_TYPE', '性别类型', 'LIST', '列表参数', 5, NULL, '1', '0', '1', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:29:50', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:29:57');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('dc647e42bafe42e8b96c819b046af190', 'fb37efc3dcca45c8a52c604abf27c2b4', 'SYS_USER_EFFECT_TYPE', '用户生效类型', 'LIST', '列表参数', 6, NULL, '1', '0', '1', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:31:04', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:29:57');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('e5cae8014f934f9ba9eb44d2a030761d', 'fb37efc3dcca45c8a52c604abf27c2b4', 'SYS_USER_TYPE', '用户类型', 'LIST', '列表参数', 4, NULL, '1', '0', '1', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:30:08', '77716bb912df48e59be90214bb9607b4', 'admin', '2025-11-11 00:29:57');
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('f87704e926704c35bbb24a0110385fec', 'fb37efc3dcca45c8a52c604abf27c2b4', 'SYS_ORG_TYPE', '企业类型', 'LIST', '列表参数', 1, NULL, '1', '0', '1', '1', '轩晨', '2025-11-09 23:57:25', NULL, NULL, NULL);
INSERT INTO `lite_para_class` (`id`, `pid`, `code_txt`, `name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('fb37efc3dcca45c8a52c604abf27c2b4', '4854b83501b84951b5bf1c4da82243f7', 'ORG', '组织机构管理', 'CLASS', '目录', 2, NULL, '1', '0', '1', '1', '轩晨', '2025-11-09 23:56:47', '1', '轩晨', '2025-11-09 19:06:24');
COMMIT;

-- ----------------------------
-- Table structure for lite_post
-- ----------------------------
DROP TABLE IF EXISTS `lite_post`;
CREATE TABLE `lite_post` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `row_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关联ID',
  `code_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位编码',
  `name_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位名称',
  `short_name_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位简称',
  `type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位类型编码',
  `type_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '岗位类型名称',
  `rec_sort_num` decimal(5,0) DEFAULT NULL COMMENT '排序号',
  `rec_remarks_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `rec_valid_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否有效',
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `rec_sys_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否系统保留',
  `rec_create_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_update_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `rec_update_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `rec_update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位表';

-- ----------------------------
-- Records of lite_post
-- ----------------------------
BEGIN;
INSERT INTO `lite_post` (`id`, `row_id`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('2227d1ea7571497b880c867fe9ce62c7', '1', 'ZJL', '总经理', NULL, '01', '高层', 25, NULL, '1', '0', '0', '1', '轩晨', '2025-11-05 00:06:40', '1', '轩晨', '2025-11-10 00:46:12');
INSERT INTO `lite_post` (`id`, `row_id`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('43ec3f64d81f42d196659daf04f0dee5', '1394e5ae74904c4595c1b7d22f2bf635', 'FZJL', '副总经理', NULL, '01', '高层', 15, NULL, '1', '0', '0', '1', '轩晨', '2025-11-05 00:06:02', '1', '轩晨', '2025-11-10 00:46:55');
INSERT INTO `lite_post` (`id`, `row_id`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('5cd98075ed064c35809f653a2fdf03e2', '1394e5ae74904c4595c1b7d22f2bf635', 'ZJL', '总经理', NULL, '01', '高层', 10, NULL, '1', '0', '0', '1', '轩晨', '2025-11-05 00:05:55', '1', '轩晨', '2025-11-10 00:46:45');
INSERT INTO `lite_post` (`id`, `row_id`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('937fad4a18214e3183dbf750a6a021a5', '1', 'DSH', '董事长', NULL, '01', '高层', 20, NULL, '1', '0', '0', '1', '轩晨', '2025-11-05 00:06:33', '1', '轩晨', '2025-11-10 00:44:57');
INSERT INTO `lite_post` (`id`, `row_id`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('c9a432da00f0485fbe7f8bf41acdc495', '1394e5ae74904c4595c1b7d22f2bf635', 'DWSJ', '党委书记', NULL, '01', '高层', 5, NULL, '1', '0', '0', '1', '轩晨', '2025-11-05 00:05:47', '1', '轩晨', '2025-11-10 00:46:37');
INSERT INTO `lite_post` (`id`, `row_id`, `code_txt`, `name_txt`, `short_name_txt`, `type_code`, `type_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('e35a29c30dc242128308f941dbf4a35b', '1', 'FZLJ', '副总经理', NULL, '01', '高层', 30, NULL, '1', '0', '0', '1', '轩晨', '2025-11-05 00:06:48', '1', '轩晨', '2025-11-10 00:46:20');
COMMIT;

-- ----------------------------
-- Table structure for lite_user
-- ----------------------------
DROP TABLE IF EXISTS `lite_user`;
CREATE TABLE `lite_user` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `login_code_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录账号',
  `login_pwd_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `login_last_dt` datetime DEFAULT NULL COMMENT '最后登录时间',
  `login_last_ip_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '最后登录IP',
  `update_pwd_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否已修改密码标识',
  `effect_type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '生效类型',
  `effect_dt` datetime DEFAULT NULL COMMENT '生效时间',
  `invalid_dt` datetime DEFAULT NULL COMMENT '注销时间',
  `type_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户类型',
  `name_txt` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `code_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户编码',
  `nickname_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `email_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `avatar_url_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `gender_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `gender_ref` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `cnid_txt` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证',
  `qq_txt` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'QQ',
  `wechat_txt` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信',
  `first_letters_txt` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名称首字母',
  `full_letters_txt` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名称全拼',
  `birthday_dt` datetime DEFAULT NULL COMMENT '出生日期',
  `hide_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否隐藏',
  `dept_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门ID',
  `dept_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门',
  `org_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业ID',
  `org_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业',
  `rec_sort_num` decimal(5,0) DEFAULT NULL COMMENT '排序号',
  `rec_remarks_txt` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `rec_valid_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否有效',
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `rec_sys_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否系统保留',
  `rec_create_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_update_id` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人ID',
  `rec_update_ref` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `rec_update_dt` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- Records of lite_user
-- ----------------------------
BEGIN;
INSERT INTO `lite_user` (`id`, `login_code_txt`, `login_pwd_txt`, `login_last_dt`, `login_last_ip_txt`, `update_pwd_flag`, `effect_type_code`, `effect_dt`, `invalid_dt`, `type_code`, `name_txt`, `code_txt`, `nickname_txt`, `email_txt`, `phone_txt`, `avatar_url_txt`, `gender_code`, `gender_ref`, `cnid_txt`, `qq_txt`, `wechat_txt`, `first_letters_txt`, `full_letters_txt`, `birthday_dt`, `hide_flag`, `dept_id`, `dept_ref`, `org_id`, `org_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('1', 'xc', '21232f297a57a5a743894a0e4a801fc3', '2025-11-11 00:43:18', '127.0.0.1', '0', 'PERMANENT', NULL, NULL, 'SYSTEM', '轩晨', 'xc', '轩晨', NULL, NULL, NULL, 'MEN', '男', NULL, NULL, NULL, NULL, NULL, '1989-11-11 00:00:00', '0', NULL, NULL, '1', 'xclite', 5, NULL, '1', '0', '0', '1', '轩晨', '2025-11-05 00:20:08', '1', '轩晨', '2025-11-11 00:45:29');
INSERT INTO `lite_user` (`id`, `login_code_txt`, `login_pwd_txt`, `login_last_dt`, `login_last_ip_txt`, `update_pwd_flag`, `effect_type_code`, `effect_dt`, `invalid_dt`, `type_code`, `name_txt`, `code_txt`, `nickname_txt`, `email_txt`, `phone_txt`, `avatar_url_txt`, `gender_code`, `gender_ref`, `cnid_txt`, `qq_txt`, `wechat_txt`, `first_letters_txt`, `full_letters_txt`, `birthday_dt`, `hide_flag`, `dept_id`, `dept_ref`, `org_id`, `org_ref`, `rec_sort_num`, `rec_remarks_txt`, `rec_valid_flag`, `rec_del_flag`, `rec_sys_flag`, `rec_create_id`, `rec_create_ref`, `rec_create_dt`, `rec_update_id`, `rec_update_ref`, `rec_update_dt`) VALUES ('77716bb912df48e59be90214bb9607b4', 'admin', '21232f297a57a5a743894a0e4a801fc3', '2025-11-11 01:20:52', '127.0.0.1', '0', 'PERMANENT', NULL, NULL, 'SYSTEM', '系统管理员', 'admin', '管理员', NULL, NULL, NULL, 'HIDDEN', '隐藏', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, '1', 'xclite', 10, NULL, '1', '0', '0', '1', '轩晨', '2025-11-05 00:20:17', '1', '轩晨', '2025-11-11 00:48:04');
COMMIT;


-- ----------------------------
-- Table structure for lite_log_login
-- ----------------------------
DROP TABLE IF EXISTS `lite_log_login`;
CREATE TABLE `lite_log_login` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `login_code_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录账号',
  `fail_pwd_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '失败密码',
  `fail_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否删除',
  `login_type_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录类型（成功、失败）',
  `login_browser_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '浏览器',
  `login_os_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作系统',
  `login_address_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地理位置',
  `login_ip_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip地址',
  `login_token_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'token',
  `rec_create_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
  `rec_create_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_org_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- ----------------------------
-- Table structure for lite_log_oper
-- ----------------------------
DROP TABLE IF EXISTS `lite_log_oper`;
CREATE TABLE `lite_log_oper` (
 `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
 `api_name_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接口名',
 `rec_del_flag` varchar(4) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '是否删除',
 `api_path_txt` varchar(4096) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路径',
 `api_method_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '方法',
 `cost_time_num` decimal(11,0) DEFAULT NULL COMMENT '耗时',
 `user_agent_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户代理',
 `user_ip_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户IP',
 `rec_create_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人ID',
 `login_token_txt` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'token',
 `rec_create_ref` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
 `rec_create_dt` datetime DEFAULT NULL COMMENT '创建时间',
 `rec_org_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业ID',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

SET FOREIGN_KEY_CHECKS = 1;


