/*
 Navicat Premium Data Transfer

 Source Server         : 192.0.2.12
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 192.0.2.12:3306
 Source Schema         : hbgrctest

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 09/05/2024 22:56:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_gbi_batch
-- ----------------------------
DROP TABLE IF EXISTS `tbl_gbi_batch`;
CREATE TABLE `tbl_gbi_batch` (
                                 `ID` varchar(255) NOT NULL COMMENT '主键',
                                 `APP_ID` varchar(255) NOT NULL COMMENT 'appid',
                                 `BATCH_NO` varchar(255) DEFAULT NULL COMMENT '批次编号，目前暂时和id一样，之后可支持批次多文件',
                                 `ATTNAME` varchar(255) DEFAULT NULL COMMENT '原文件名称',
                                 `ATTPATH` varchar(255) DEFAULT NULL COMMENT '文件ftp地址',
                                 `ATTSIZE` int(32) DEFAULT NULL COMMENT '文件大小',
                                 `USER_ID` decimal(10,0) DEFAULT NULL COMMENT '用户id',
                                 `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
                                 `CREATE_COMPANY` varchar(255) NOT NULL COMMENT '企业',
                                 `CREATE_DEPT` varchar(255) NOT NULL COMMENT '部门',
                                 PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_gbi_columns
-- ----------------------------
DROP TABLE IF EXISTS `tbl_gbi_columns`;
CREATE TABLE `tbl_gbi_columns` (
                                   `ID` varchar(255) NOT NULL COMMENT 'PK',
                                   `APP_ID` varchar(255) NOT NULL COMMENT 'appid',
                                   `BATCH_ID` varchar(255) DEFAULT NULL COMMENT '批次id',
                                   `BATCH_NO` varchar(255) DEFAULT NULL COMMENT '批次编号',
                                   `COLUMN_NAME` varchar(255) DEFAULT NULL COMMENT '字段名称',
                                   `COLUMN_COMMENT` varchar(255) NOT NULL COMMENT '字段描述',
                                   `DATA_BASE` varchar(255) NOT NULL,
                                   `DATA_SOURCE_ID` varchar(0) NOT NULL,
                                   `DEMO` varchar(255) NOT NULL COMMENT '样例',
                                   `INDEXED` int(1) DEFAULT NULL COMMENT '是否索引',
                                   `LABELS` varchar(255) NOT NULL,
                                   `PARENT_COLUMN` varchar(255) NOT NULL COMMENT '父级字段id',
                                   `CELL_TYPE` varchar(255) DEFAULT NULL COMMENT '页面展示字段类型',
                                   `USER_ID` decimal(32,0) DEFAULT NULL COMMENT '用户id',
                                   `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
                                   `CREATE_COMPANY` varchar(255) NOT NULL COMMENT '企业',
                                   `CREATE_DEPT` varchar(255) NOT NULL COMMENT '部门',
                                   `UPDATE_TIME` datetime NOT NULL COMMENT '修改时间',
                                   `SORT` int(8) NOT NULL COMMENT '排序',
                                   `DELETED` int(1) NOT NULL COMMENT '是否删除',
                                   `TABLE_ID` varchar(255) DEFAULT NULL COMMENT '表id',
                                   `MAPPING_COLUMN` varchar(255) DEFAULT NULL COMMENT '数据库真实字段名称',
                                   `INDEX_NAME` varchar(255) NOT NULL COMMENT '索引名称',
                                   `FIELD_TYPE` varchar(255) DEFAULT NULL COMMENT '字段类型',
                                   PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tbl_gbi_table
-- ----------------------------
DROP TABLE IF EXISTS `tbl_gbi_table`;
CREATE TABLE `tbl_gbi_table` (
                                 `ID` varchar(255) NOT NULL COMMENT 'PK',
                                 `APP_ID` varchar(255) NOT NULL COMMENT 'appid',
                                 `BATCH_ID` varchar(255) DEFAULT NULL COMMENT '批次id',
                                 `BATCH_NO` varchar(255) DEFAULT NULL COMMENT '批次编号',
                                 `CUSTOM_TABLE` varchar(255) DEFAULT NULL COMMENT '客户定义表名称',
                                 `DESCRIPTION` varchar(255) NOT NULL COMMENT '描述',
                                 `TABLE_NAME` varchar(255) DEFAULT NULL COMMENT '真实表名称',
                                 `USER_ID` decimal(32,0) DEFAULT NULL COMMENT '用户id',
                                 `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
                                 `CREATE_COMPANY` varchar(255) NOT NULL COMMENT '企业',
                                 `CREATE_DEPT` varchar(255) NOT NULL COMMENT '部门',
                                 `UPDATE_TIME` datetime NOT NULL COMMENT '修改时间',
                                 `SORT` int(8) NOT NULL COMMENT '排序',
                                 PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
