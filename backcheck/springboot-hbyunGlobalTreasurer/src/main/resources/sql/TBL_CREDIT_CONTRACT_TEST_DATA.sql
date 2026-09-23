-- ============================================================
-- 授信合同测试数据插入脚本（达梦数据库版本）
-- 表名: TBL_CREDIT_CONTRACT
-- 说明: 根据实体类 TblCreditContract 生成的测试数据
-- 注意: CONTRACT_ID 是自增列，不需要手动指定
-- 创建时间: 2026-02-04
-- ============================================================

-- 清空现有测试数据（可选，谨慎使用）
-- DELETE FROM TBL_CREDIT_CONTRACT WHERE CONTRACT_NO LIKE 'HT2026%';

-- 插入授信合同测试数据（10条）
INSERT INTO TBL_CREDIT_CONTRACT (
    CONTRACT_NO, CREDIT_APPLICATION_ID, BANK_CODE, BANK_NAME,
    CREDIT_LIMIT, CURRENCY_CODE, CREDIT_PERIOD, START_DATE, END_DATE,
    INTEREST_RATE, GUARANTEE_METHOD, CONTRACT_STATUS, SIGNING_DATE,
    EFFECTIVE_DATE, TERMINATION_DATE, USED_AMOUNT, AVAILABLE_AMOUNT,
    CONTRACT_FILE, SIGNED_BY, SIGNED_BY_NAME, SIGNED_AT,
    COMPANY_ID, COMPANY_NAME, DELETE_FLAG, REMARK
) VALUES
-- 合同1: 流动资金贷款合同（生效中）
(
    'HT202601001', 1, 'ICBC', '中国工商银行股份有限公司',
    10000000.00, 'CNY', 36, 
    TO_TIMESTAMP('2026-01-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2029-01-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    4.35, '信用担保', 'EFFECTIVE', 
    TO_TIMESTAMP('2026-01-10 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-01-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    NULL, 3000000.00, 7000000.00,
    '/files/contracts/HT202601001.pdf', 1001, '张三',
    TO_TIMESTAMP('2026-01-10 14:30:00', 'YYYY-MM-DD HH24:MI:SS'),
    1, '华博云科技有限公司', 0, '流动资金贷款合同，用于日常经营周转'
),
-- 合同2: 固定资产贷款合同（生效中）
(
    'HT202601002', 2, 'CCB', '中国建设银行股份有限公司',
    50000000.00, 'CNY', 60,
    TO_TIMESTAMP('2026-01-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2031-01-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    4.75, '抵押担保', 'EFFECTIVE',
    TO_TIMESTAMP('2026-01-18 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-01-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    NULL, 20000000.00, 30000000.00,
    '/files/contracts/HT202601002.pdf', 1002, '李四',
    TO_TIMESTAMP('2026-01-18 10:15:00', 'YYYY-MM-DD HH24:MI:SS'),
    1, '华博云科技有限公司', 0, '固定资产贷款合同，用于购置生产设备'
),
-- 合同3: 银行承兑汇票合同（已签署）
(
    'HT202601003', NULL, 'ABC', '中国农业银行股份有限公司',
    5000000.00, 'CNY', 12,
    TO_TIMESTAMP('2026-02-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2027-02-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    3.85, '保证金担保', 'SIGNED',
    TO_TIMESTAMP('2026-01-28 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    NULL, NULL, 0.00, 5000000.00,
    '/files/contracts/HT202601003.pdf', 1003, '王五',
    TO_TIMESTAMP('2026-01-28 16:45:00', 'YYYY-MM-DD HH24:MI:SS'),
    2, '华东分公司', 0, '银行承兑汇票授信合同'
),
-- 合同4: 信用证合同（草稿）
(
    'HT202602001', NULL, 'BOC', '中国银行股份有限公司',
    8000000.00, 'USD', 24,
    TO_TIMESTAMP('2026-03-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2028-03-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    5.20, '信用担保', 'DRAFT',
    NULL, NULL, NULL, 0.00, 8000000.00,
    NULL, NULL, NULL, NULL,
    3, '华南分公司', 0, '信用证授信合同草稿'
),
-- 合同5: 综合授信合同（生效中）
(
    'HT202602002', NULL, 'CMB', '招商银行股份有限公司',
    30000000.00, 'CNY', 48,
    TO_TIMESTAMP('2026-02-10 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2030-02-10 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    4.50, '综合担保', 'EFFECTIVE',
    TO_TIMESTAMP('2026-02-08 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-02-10 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    NULL, 10000000.00, 20000000.00,
    '/files/contracts/HT202602002.pdf', 1004, '赵六',
    TO_TIMESTAMP('2026-02-08 09:30:00', 'YYYY-MM-DD HH24:MI:SS'),
    1, '华博云科技有限公司', 0, '综合授信合同，包含多种融资方式'
),
-- 合同6: 短期流动资金贷款（生效中）
(
    'HT202602003', NULL, 'PSBC', '中国邮政储蓄银行',
    3000000.00, 'CNY', 6,
    TO_TIMESTAMP('2026-02-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-08-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    4.10, '信用担保', 'EFFECTIVE',
    TO_TIMESTAMP('2026-02-12 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-02-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    NULL, 1000000.00, 2000000.00,
    '/files/contracts/HT202602003.pdf', 1005, '孙七',
    TO_TIMESTAMP('2026-02-12 15:20:00', 'YYYY-MM-DD HH24:MI:SS'),
    2, '华东分公司', 0, '短期流动资金贷款，6个月期限'
),
-- 合同7: 项目贷款合同（已签署）
(
    'HT202602004', NULL, 'CITIC', '中信银行股份有限公司',
    80000000.00, 'CNY', 84,
    TO_TIMESTAMP('2026-03-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2033-03-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    5.00, '项目抵押', 'SIGNED',
    TO_TIMESTAMP('2026-02-25 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    NULL, NULL, 0.00, 80000000.00,
    '/files/contracts/HT202602004.pdf', 1006, '周八',
    TO_TIMESTAMP('2026-02-25 11:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    1, '华博云科技有限公司', 0, '大型项目贷款，7年期'
),
-- 合同8: 贸易融资合同（生效中）
(
    'HT202602005', NULL, 'SPDB', '上海浦东发展银行',
    15000000.00, 'CNY', 12,
    TO_TIMESTAMP('2026-02-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2027-02-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    4.20, '货物质押', 'EFFECTIVE',
    TO_TIMESTAMP('2026-02-18 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-02-20 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    NULL, 5000000.00, 10000000.00,
    '/files/contracts/HT202602005.pdf', 1007, '吴九',
    TO_TIMESTAMP('2026-02-18 14:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    3, '华南分公司', 0, '贸易融资合同，用于进出口业务'
),
-- 合同9: 供应链金融合同（已终止）
(
    'HT202601004', NULL, 'CEB', '中国光大银行',
    12000000.00, 'CNY', 18,
    TO_TIMESTAMP('2025-06-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-12-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    4.60, '应收账款质押', 'TERMINATED',
    TO_TIMESTAMP('2025-05-28 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2025-06-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-01-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    8000000.00, 0.00,
    '/files/contracts/HT202601004.pdf', 1008, '郑十',
    TO_TIMESTAMP('2025-05-28 13:45:00', 'YYYY-MM-DD HH24:MI:SS'),
    2, '华东分公司', 0, '供应链金融合同，已提前终止'
),
-- 合同10: 中长期贷款合同（生效中）
(
    'HT202602006', NULL, 'HXB', '华夏银行股份有限公司',
    25000000.00, 'CNY', 72,
    TO_TIMESTAMP('2026-02-25 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2032-02-25 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    4.90, '混合担保', 'EFFECTIVE',
    TO_TIMESTAMP('2026-02-22 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2026-02-25 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    NULL, 8000000.00, 17000000.00,
    '/files/contracts/HT202602006.pdf', 1009, '钱十一',
    TO_TIMESTAMP('2026-02-22 16:30:00', 'YYYY-MM-DD HH24:MI:SS'),
    1, '华博云科技有限公司', 0, '中长期贷款合同，6年期'
);

-- 提交事务
COMMIT;

-- 查询验证
SELECT
    CONTRACT_ID,
    CONTRACT_NO,
    BANK_NAME,
    CREDIT_LIMIT,
    CURRENCY_CODE,
    CONTRACT_STATUS,
    COMPANY_NAME,
    SIGNING_DATE
FROM TBL_CREDIT_CONTRACT
ORDER BY CONTRACT_ID;

-- 统计各状态合同数量
SELECT
    CONTRACT_STATUS,
    COUNT(*) AS COUNT
FROM TBL_CREDIT_CONTRACT
GROUP BY CONTRACT_STATUS;

