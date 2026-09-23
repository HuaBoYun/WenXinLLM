-- ============================================================
-- 企业纠纷接口配置补全
-- 执行时间：2026-05-13
-- 说明：
--   1. TBL_CJBDI_DATA_CATEGORY 表新增 LOGIN_NAME / LOGIN_PASSWORD 字段
--      用于存储需要账号密码认证的第三方接口凭证（如企业纠纷 apiCode=24）
--   2. 更新 categoryId=18（企业纠纷）的完整配置
-- ============================================================

-- Step 1: 新增字段（达梦数据库兼容语法）
-- 若字段已存在请注释掉对应 ALTER 语句
ALTER TABLE TBL_CJBDI_DATA_CATEGORY
    ADD LOGIN_NAME VARCHAR(200) DEFAULT NULL;

ALTER TABLE TBL_CJBDI_DATA_CATEGORY
    ADD LOGIN_PASSWORD VARCHAR(200) DEFAULT NULL;

-- Step 2: 更新企业纠纷（categoryId=18，apiCode=24）的完整配置
UPDATE TBL_CJBDI_DATA_CATEGORY
SET
    TICKET        = ' ',
    AES_KEY       = ' ',
    API_URL       = 'http://192.0.2.15/qysstest/qyss/enterlit/caseQueryApi',
    LOGIN_NAME    = '',
    LOGIN_PASSWORD = ''
WHERE CATEGORY_ID = 18;

COMMIT;
