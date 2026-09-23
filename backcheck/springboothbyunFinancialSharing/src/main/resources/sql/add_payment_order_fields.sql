-- ====================================================================
-- 付款单表升级脚本 - 添加收款账户和开户行字段
-- 作者: 星光问心AI
-- 日期: 2026-01-13
-- 说明: 为付款单表添加收款账户和收款开户行字段
-- ====================================================================

-- 检查并添加收款账户字段
ALTER TABLE TBL_PAYMENT_ORDER ADD COLUMN IF NOT EXISTS RECEIVER_ACCOUNT VARCHAR(100) COMMENT '收款账户';

-- 检查并添加收款开户行字段
ALTER TABLE TBL_PAYMENT_ORDER ADD COLUMN IF NOT EXISTS RECEIVER_BANK VARCHAR(200) COMMENT '收款开户行';

-- 为新字段添加注释（达梦数据库语法）
COMMENT ON COLUMN TBL_PAYMENT_ORDER.RECEIVER_ACCOUNT IS '收款账户';
COMMENT ON COLUMN TBL_PAYMENT_ORDER.RECEIVER_BANK IS '收款开户行';

SELECT '=== 付款单表字段升级完成 ===' AS '';
SELECT '已添加字段: RECEIVER_ACCOUNT (收款账户)' AS '';
SELECT '已添加字段: RECEIVER_BANK (收款开户行)' AS '';
