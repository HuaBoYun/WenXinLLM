-- ============================================================
-- 为业务系统注册表添加状态字段(简单版本)
-- 数据库: 达梦数据库 (DM8)
-- 说明: 直接添加字段,如果已存在会报错但不影响原表和数据
-- 作者: 华博云开发团队
-- 日期: 2024-12-24
-- ============================================================

-- 方式1: 直接添加STATUS字段(如果已存在会报错但跳过)
ALTER TABLE TBL_BUSINESS_SYSTEM_REGISTRY ADD STATUS VARCHAR2(10);

-- 添加字段注释
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.STATUS IS '状态(0-禁用,1-启用)';

-- 为现有数据设置默认值
UPDATE TBL_BUSINESS_SYSTEM_REGISTRY SET STATUS = '1' WHERE STATUS IS NULL;
COMMIT;

-- ============================================================

-- 方式2: 直接添加CONNECTION_STATUS字段(如果已存在会报错但跳过)
ALTER TABLE TBL_BUSINESS_SYSTEM_REGISTRY ADD CONNECTION_STATUS VARCHAR2(20);

-- 添加字段注释
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.CONNECTION_STATUS IS '连接状态(OFFLINE-离线,ONLINE-在线,ERROR-错误)';

-- 为现有数据设置默认值
UPDATE TBL_BUSINESS_SYSTEM_REGISTRY SET CONNECTION_STATUS = 'OFFLINE' WHERE CONNECTION_STATUS IS NULL;
COMMIT;

-- ============================================================
-- 注意事项:
-- 1. 如果字段已存在,ALTER TABLE语句会报错,但不影响原表和数据
-- 2. 可以忽略错误信息,继续执行后面的语句
-- 3. 或者只执行需要添加的字段对应的SQL
-- ============================================================
