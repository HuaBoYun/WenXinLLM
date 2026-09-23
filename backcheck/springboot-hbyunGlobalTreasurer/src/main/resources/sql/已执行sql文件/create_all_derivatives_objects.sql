-- =====================================================================
-- 衍生品持仓监控 - 完整初始化脚本 (达梦数据库)
-- 生成日期: 2026-01-21
-- 说明: 一键创建所有表 + 插入测试数据
-- =====================================================================

-- 步骤1: 创建所有表
@src/main/resources/sql/create_derivatives_tables_dm.sql

-- 步骤2: 插入测试数据
@src/main/resources/sql/insert_position_monitoring_correct.sql

-- 步骤3: 验证数据
SELECT '========== 初始化完成 ==========' AS INFO;
SELECT '远期交易: ' || COUNT(*) || ' 条' AS INFO FROM TBL_FORWARD_TRANSACTION WHERE DEL_FLAG = '0';
SELECT '期权交易: ' || COUNT(*) || ' 条' AS INFO FROM TBL_OPTION_TRANSACTION WHERE DEL_FLAG = '0';
SELECT '期货交易: ' || COUNT(*) || ' 条' AS INFO FROM TBL_FUTURES_TRANSACTION WHERE DEL_FLAG = '0';
SELECT '掉期交易: ' || COUNT(*) || ' 条' AS INFO FROM TBL_SWAP_TRANSACTION WHERE DEL_FLAG = '0';
SELECT '预警记录: ' || COUNT(*) || ' 条' AS INFO FROM TBL_DERIVATIVES_ALERT WHERE DEL_FLAG = '0';
SELECT '市场数据: ' || COUNT(*) || ' 条' AS INFO FROM TBL_DERIVATIVES_MARKET_DATA;

SELECT '========== 总持仓数据 ==========' AS INFO;
SELECT 'TOTAL_POSITIONS: ' || COUNT(*) || ' 条' AS TOTAL_RECORDS FROM (
    SELECT 'FORWARD' FROM TBL_FORWARD_TRANSACTION WHERE DEL_FLAG = '0' AND STATUS IN ('ACTIVE', 'PENDING') AND ORG_ID = 1
    UNION ALL
    SELECT 'OPTION' FROM TBL_OPTION_TRANSACTION WHERE DEL_FLAG = '0' AND STATUS IN ('ACTIVE', 'PENDING') AND ORG_ID = 1
    UNION ALL
    SELECT 'FUTURES' FROM TBL_FUTURES_TRANSACTION WHERE DEL_FLAG = '0' AND STATUS IN ('ACTIVE', 'PENDING') AND ORG_ID = 1
    UNION ALL
    SELECT 'SWAP' FROM TBL_SWAP_TRANSACTION WHERE DEL_FLAG = '0' AND STATUS IN ('ACTIVE', 'PENDING') AND ORG_ID = 1
);

-- 完成
SELECT '========== 初始化成功! ==========' AS INFO;
SELECT '请重启后端服务并访问前端页面验证数据' AS INFO;
