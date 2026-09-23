-- ============================================
-- 印鉴类型表结构升级脚本
-- 添加 SEAL_LEVEL 和 SCOPE 字段
-- 执行时间: 2026-02-02
-- ============================================

-- 1. 添加印鉴级别字段 (达梦数据库语法)
ALTER TABLE TC_SEAL_TYPE ADD SEAL_LEVEL VARCHAR(10);
COMMENT ON COLUMN TC_SEAL_TYPE.SEAL_LEVEL IS '印鉴级别: 1-一级印鉴, 2-二级印鉴, 3-三级印鉴';

-- 2. 添加适用范围字段
ALTER TABLE TC_SEAL_TYPE ADD SCOPE VARCHAR(100);
COMMENT ON COLUMN TC_SEAL_TYPE.SCOPE IS '适用范围';

-- 3. 更新现有数据，设置默认值
UPDATE TC_SEAL_TYPE SET SEAL_LEVEL = '1' WHERE SEAL_LEVEL IS NULL;
UPDATE TC_SEAL_TYPE SET SCOPE = '通用' WHERE SCOPE IS NULL;

-- 4. 根据类型编码设置合理的印鉴级别
UPDATE TC_SEAL_TYPE SET SEAL_LEVEL = '1', SCOPE = '全公司' WHERE TYPE_CODE = 'OFFICIAL_SEAL';
UPDATE TC_SEAL_TYPE SET SEAL_LEVEL = '1', SCOPE = '法务部门' WHERE TYPE_CODE = 'LEGAL_SEAL';
UPDATE TC_SEAL_TYPE SET SEAL_LEVEL = '2', SCOPE = '财务部门' WHERE TYPE_CODE = 'FINANCE_SEAL';
UPDATE TC_SEAL_TYPE SET SEAL_LEVEL = '2', SCOPE = '合同业务' WHERE TYPE_CODE = 'CONTRACT_SEAL';
UPDATE TC_SEAL_TYPE SET SEAL_LEVEL = '3', SCOPE = '发票业务' WHERE TYPE_CODE = 'INVOICE_SEAL';
UPDATE TC_SEAL_TYPE SET SEAL_LEVEL = '3', SCOPE = '人事部门' WHERE TYPE_CODE = 'HR_SEAL';

COMMIT;

-- ============================================
-- Oracle 语法版本 (如果使用Oracle数据库)
-- ============================================
-- ALTER TABLE TC_SEAL_TYPE ADD (
--     SEAL_LEVEL VARCHAR2(10),
--     SCOPE VARCHAR2(100)
-- );
-- COMMENT ON COLUMN TC_SEAL_TYPE.SEAL_LEVEL IS '印鉴级别: 1-一级印鉴, 2-二级印鉴, 3-三级印鉴';
-- COMMENT ON COLUMN TC_SEAL_TYPE.SCOPE IS '适用范围';

-- ============================================
-- MySQL 语法版本 (如果使用MySQL数据库)
-- ============================================
-- ALTER TABLE TC_SEAL_TYPE 
--     ADD COLUMN SEAL_LEVEL VARCHAR(10) COMMENT '印鉴级别: 1-一级印鉴, 2-二级印鉴, 3-三级印鉴',
--     ADD COLUMN SCOPE VARCHAR(100) COMMENT '适用范围';

