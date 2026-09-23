-- ============================================
-- 修复 TC_SEAL_TYPE 表缺失列的 ALTER 脚本
-- 保留原有表，只添加缺失的列
-- 兼容达梦数据库 DM8
-- 创建日期: 2025-12-25
-- ============================================

-- 检查表是否存在
-- SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME = 'TC_SEAL_TYPE';

-- 添加缺失的列（如果不存在）
-- 注意：根据实际表结构，TC_SEAL_TYPE 表已有的列：
-- TYPE_ID, TYPE_NAME, TYPE_CODE, IS_ACTIVE, REMARK, CREATE_TIME

-- 可能缺失的列（根据XML映射需要）：
-- SORT_ORDER - 排序字段
-- IS_ENABLED - 是否启用（注意：表中实际使用 IS_ACTIVE）

-- 方案1：如果表中确实需要 SORT_ORDER 列
ALTER TABLE TC_SEAL_TYPE ADD SORT_ORDER INTEGER DEFAULT 0;

-- 添加列注释
COMMENT ON COLUMN TC_SEAL_TYPE.SORT_ORDER IS '排序顺序(数字越小越靠前)';

-- 更新现有的排序值
UPDATE TC_SEAL_TYPE SET SORT_ORDER = TYPE_ID WHERE SORT_ORDER IS NULL;

COMMIT;

-- ============================================
-- 说明：
-- 1. 此脚本会保留原有表的所有数据
-- 2. 只添加缺失的 SORT_ORDER 列
-- 3. 如果列已存在，ALTER 语句会报错，可以忽略
-- 4. IS_ENABLED 和 IS_ACTIVE 是同一个字段的不同命名，
--    统一使用 IS_ACTIVE（1=启用，0=禁用）
-- ============================================

-- 验证结果
-- SELECT * FROM TC_SEAL_TYPE;
