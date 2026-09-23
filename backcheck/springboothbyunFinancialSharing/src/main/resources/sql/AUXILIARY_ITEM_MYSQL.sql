-- 辅助核算项表 - MySQL版本
-- 创建时间：2024-12-19
-- 说明：辅助核算项基础信息表，支持多级树形结构

-- 删除表（如果存在）
DROP TABLE IF EXISTS `t_auxiliary_item`;

-- 创建辅助核算项表
CREATE TABLE `t_auxiliary_item` (
    `auxiliary_id` BIGINT NOT NULL COMMENT '主键ID',
    `auxiliary_code` VARCHAR(50) NOT NULL COMMENT '核算项编码',
    `auxiliary_name` VARCHAR(200) NOT NULL COMMENT '核算项名称',
    `auxiliary_type` VARCHAR(20) NOT NULL COMMENT '核算类型',
    `parent_id` BIGINT DEFAULT NULL COMMENT '上级ID',
    `is_leaf` TINYINT(1) DEFAULT 1 COMMENT '是否末级：0否 1是',
    `is_enabled` TINYINT(1) DEFAULT 1 COMMENT '启用状态：0禁用 1启用',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `book_id` BIGINT NOT NULL COMMENT '账簿ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    `version` INT DEFAULT 0 COMMENT '版本号（乐观锁）',
    `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '删除标识：0否 1是',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `creator` BIGINT DEFAULT NULL COMMENT '创建人',
    `updater` BIGINT DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`auxiliary_id`),
    KEY `idx_auxiliary_code` (`auxiliary_code`, `auxiliary_type`, `tenant_id`, `book_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_auxiliary_type` (`auxiliary_type`, `tenant_id`, `book_id`),
    KEY `idx_tenant_book` (`tenant_id`, `book_id`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='辅助核算项表';

-- 插入测试数据
INSERT INTO `t_auxiliary_item` (
    `auxiliary_id`, `auxiliary_code`, `auxiliary_name`, `auxiliary_type`,
    `parent_id`, `is_leaf`, `is_enabled`, `sort_order`,
    `book_id`, `tenant_id`, `creator`, `updater`
) VALUES
-- 部门类型
(1, 'DEPT_ROOT', '部门根节点', 'DEPT', NULL, 0, 1, 0, 1, 1, 1, 1),
(2, 'DEPT001', '财务部', 'DEPT', 1, 1, 1, 1, 1, 1, 1, 1),
(3, 'DEPT002', '技术部', 'DEPT', 1, 1, 1, 2, 1, 1, 1, 1),
(4, 'DEPT003', '人事部', 'DEPT', 1, 1, 1, 3, 1, 1, 1, 1),

-- 人员类型
(10, 'PERSON_ROOT', '人员根节点', 'PERSON', NULL, 0, 1, 0, 1, 1, 1, 1),
(11, 'PERSON001', '张三', 'PERSON', 10, 1, 1, 1, 1, 1, 1, 1),
(12, 'PERSON002', '李四', 'PERSON', 10, 1, 1, 2, 1, 1, 1, 1),
(13, 'PERSON003', '王五', 'PERSON', 10, 1, 0, 3, 1, 1, 1, 1),

-- 项目类型
(20, 'PROJECT_ROOT', '项目根节点', 'PROJECT', NULL, 0, 1, 0, 1, 1, 1, 1),
(21, 'PROJ001', '财务系统项目', 'PROJECT', 20, 1, 1, 1, 1, 1, 1, 1),
(22, 'PROJ002', 'ERP升级项目', 'PROJECT', 20, 1, 1, 2, 1, 1, 1, 1),

-- 客户类型
(30, 'CUSTOMER_ROOT', '客户根节点', 'CUSTOMER', NULL, 0, 1, 0, 1, 1, 1, 1),
(31, 'CUST001', '华博云客户A', 'CUSTOMER', 30, 1, 1, 1, 1, 1, 1, 1),
(32, 'CUST002', '华博云客户B', 'CUSTOMER', 30, 1, 1, 2, 1, 1, 1, 1),

-- 供应商类型
(40, 'SUPPLIER_ROOT', '供应商根节点', 'SUPPLIER', NULL, 0, 1, 0, 1, 1, 1, 1),
(41, 'SUPP001', '供应商A', 'SUPPLIER', 40, 1, 1, 1, 1, 1, 1, 1),
(42, 'SUPP002', '供应商B', 'SUPPLIER', 40, 1, 1, 2, 1, 1, 1, 1),

-- 产品类型
(50, 'PRODUCT_ROOT', '产品根节点', 'PRODUCT', NULL, 0, 1, 0, 1, 1, 1, 1),
(51, 'PROD001', '财务软件', 'PRODUCT', 50, 1, 1, 1, 1, 1, 1, 1),
(52, 'PROD002', 'ERP系统', 'PRODUCT', 50, 1, 1, 2, 1, 1, 1, 1),

-- 地区类型
(60, 'AREA_ROOT', '地区根节点', 'AREA', NULL, 0, 1, 0, 1, 1, 1, 1),
(61, 'AREA001', '华北地区', 'AREA', 60, 0, 1, 1, 1, 1, 1, 1),
(62, 'AREA002', '华南地区', 'AREA', 60, 0, 1, 2, 1, 1, 1, 1),
(63, 'AREA003', '北京', 'AREA', 61, 1, 1, 1, 1, 1, 1, 1),
(64, 'AREA004', '天津', 'AREA', 61, 1, 1, 2, 1, 1, 1, 1),
(65, 'AREA005', '广东', 'AREA', 62, 1, 1, 1, 1, 1, 1, 1),

-- 其他类型
(70, 'OTHER_ROOT', '其他根节点', 'OTHER', NULL, 0, 1, 0, 1, 1, 1, 1),
(71, 'OTHER001', '其他项目A', 'OTHER', 70, 1, 1, 1, 1, 1, 1, 1);