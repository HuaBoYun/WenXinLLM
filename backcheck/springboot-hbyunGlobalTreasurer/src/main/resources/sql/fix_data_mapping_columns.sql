-- =============================================
-- 修复数据映射配置表 - 添加缺失的列
-- 表名: TBL_DATA_MAPPING_CONFIG
-- 说明: 为现有表添加DESCRIPTION列
-- 日期: 2024-12-24
-- =============================================

-- 添加DESCRIPTION列(如果不存在)
ALTER TABLE TBL_DATA_MAPPING_CONFIG ADD DESCRIPTION VARCHAR(1000);

-- 提交
COMMIT;

-- 验证列是否添加成功
SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'TBL_DATA_MAPPING_CONFIG' AND COLUMN_NAME = 'DESCRIPTION';
