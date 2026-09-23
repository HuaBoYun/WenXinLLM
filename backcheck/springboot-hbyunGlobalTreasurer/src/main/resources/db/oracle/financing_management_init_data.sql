-- ============================================================
-- 融资管理模块测试数据插入脚本
-- 数据库: 达梦数据库 (DM Database)
-- 版本: v1.0
-- 创建日期: 2026-01-14
-- 说明: 本脚本为融资管理模块提供规范的测试数据
-- ============================================================

-- ============================================================
-- 第一部分: 金融机构测试数据
-- ============================================================

INSERT INTO TBL_FINANCIAL_INSTITUTION (
    INSTITUTION_ID, INSTITUTION_CODE, INSTITUTION_NAME, INSTITUTION_TYPE,
    LEGAL_REPRESENTATIVE, REGISTERED_CAPITAL, REGISTERED_ADDRESS,
    CREDIT_RATING, CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL,
    STATUS, CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'BANK001', '中国工商银行股份有限公司', '商业银行',
    '陈四清', 3564087000000, '北京市西城区复兴门内大街55号',
    'AAA', '张经理', '010-66108888', 'dev@example.com',
    1, 1, '系统管理员', CURRENT_TIMESTAMP
),
(
    2, 'BANK002', '中国建设银行股份有限公司', '商业银行',
    '田国立', 2500000000000, '北京市西城区金融大街25号',
    'AAA', '李经理', '010-67598888', 'dev@example.com',
    1, 1, '系统管理员', CURRENT_TIMESTAMP
),
(
    3, 'BANK003', '中国农业银行股份有限公司', '商业银行',
    '周慕冰', 3247940000000, '北京市东城区建国门内大街69号',
    'AAA', '王经理', '010-85106688', 'dev@example.com',
    1, 1, '系统管理员', CURRENT_TIMESTAMP
),
(
    4, 'BANK004', '中国银行股份有限公司', '商业银行',
    '刘连舸', 2953870000000, '北京市复兴门内大街1号',
    'AAA', '赵经理', '010-66596688', 'dev@example.com',
    1, 1, '系统管理员', CURRENT_TIMESTAMP
),
(
    5, 'LEASE001', '远东宏信有限公司', '租赁公司',
    '孔繁伟', 20000000000, '上海市浦东新区世纪大道1168号',
    'AA+', '孙经理', '021-58787888', 'dev@example.com',
    1, 1, '系统管理员', CURRENT_TIMESTAMP
),
(
    6, 'LEASE002', '中航国际租赁有限公司', '租赁公司',
    '赵宏伟', 13800138000, '北京市朝阳区建国路乙98号',
    'AA', '周经理', '010-59828888', 'dev@example.com',
    1, 1, '系统管理员', CURRENT_TIMESTAMP
);

-- ============================================================
-- 第二部分: 担保业务测试数据
-- ============================================================

-- 2.1 担保申请测试数据
INSERT INTO TBL_GUARANTEE_APPLICATION (
    APPLICATION_ID, APPLICATION_NO, GUARANTEE_TYPE, GUARANTEE_AMOUNT,
    CURRENCY_CODE, GUARANTEE_PERIOD, PERIOD_UNIT, GUARANTEE_PURPOSE,
    GUARANTOR_ID, GUARANTOR_NAME, BENEFCIARY_ID, BENEFCIARY_NAME,
    GUARANTEE_MODE, APPLICATION_STATUS, APPLICATION_DATE,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'GA202601001', '履约保证', 5000000.00,
    'CNY', 24, '月', '工程项目履约担保',
    100, '华博集团', 1, '中国工商银行股份有限公司',
    '连带责任保证', 'APPROVED', '2026-01-01',
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'GA202601002', '付款保证', 3000000.00,
    'CNY', 12, '月', '贸易付款担保',
    100, '华博集团', 2, '中国建设银行股份有限公司',
    '一般保证', 'PENDING', '2026-01-05',
    1, '华博云科技有限公司',
    1, '李四', CURRENT_TIMESTAMP
),
(
    3, 'GA202601003', '投标保证', 1000000.00,
    'CNY', 6, '月', '工程项目投标担保',
    100, '华博集团', 3, '中国农业银行股份有限公司',
    '连带责任保证', 'APPROVED', '2026-01-10',
    1, '华博云科技有限公司',
    1, '王五', CURRENT_TIMESTAMP
);

-- 2.2 担保合同测试数据
INSERT INTO TBL_GUARANTEE_CONTRACT (
    CONTRACT_ID, CONTRACT_NO, APPLICATION_ID, GUARANTEE_TYPE,
    GUARANTEE_AMOUNT, CURRENCY_CODE, GUARANTEE_PERIOD, PERIOD_UNIT,
    GUARANTEE_START_DATE, GUARANTEE_END_DATE, GUARANTEE_RATE, GUARANTEE_FEE,
    PAYMENT_METHOD, GUARANTOR_ID, GUARANTOR_NAME, BENEFCIARY_ID, BENEFCIARY_NAME,
    GUARANTEE_MODE, CONTRACT_STATUS, SIGNING_DATE,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'GC202601001', 1, '履约保证',
    5000000.00, 'CNY', 24, '月',
    '2026-01-15', '2028-01-15', 0.0250, 125000.00,
    '一次性支付', 100, '华博集团', 1, '中国工商银行股份有限公司',
    '连带责任保证', 'ACTIVE', '2026-01-15',
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'GC202601003', 3, '投标保证',
    1000000.00, 'CNY', 6, '月',
    '2026-01-20', '2026-07-20', 0.0150, 15000.00,
    '一次性支付', 100, '华博集团', 3, '中国农业银行股份有限公司',
    '连带责任保证', 'ACTIVE', '2026-01-20',
    1, '华博云科技有限公司',
    1, '王五', CURRENT_TIMESTAMP
);

-- 2.3 担保物测试数据
INSERT INTO TBL_COLLATERAL (
    COLLATERAL_ID, COLLATERAL_NO, COLLATERAL_TYPE, COLLATERAL_NAME,
    COLLATERAL_VALUE, CURRENCY_CODE, EVALUATION_VALUE, EVALUATION_DATE,
    EVALUATION_AGENCY, COLLATERAL_STATUS, OWNER_ID, OWNER_NAME,
    LOCATION, CONTRACT_ID, COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'COL202601001', '房产', '北京市朝阳区房产',
    8000000.00, 'CNY', 8500000.00, '2025-12-01',
    '中联资产评估有限公司', 'ACTIVE', 100, '华博集团',
    '北京市朝阳区XX路XX号', 1, 1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'COL202601002', '设备', '生产设备XX型号',
    3000000.00, 'CNY', 2800000.00, '2025-12-10',
    '北京中企华资产评估有限责任公司', 'ACTIVE', 100, '华博集团',
    '北京市大兴区XX工业园', 1, 1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    3, 'COL202601003', '车辆', '奔驰S400轿车',
    1200000.00, 'CNY', 1000000.00, '2025-11-15',
    '中联资产评估有限公司', 'ACTIVE', 100, '华博集团',
    '北京市海淀区XX停车场', 2, 1, '华博云科技有限公司',
    1, '王五', CURRENT_TIMESTAMP
);

-- ============================================================
-- 第三部分: 授信业务测试数据
-- ============================================================

-- 3.1 授信申请测试数据
INSERT INTO TBL_CREDIT_APPLICATION (
    APPLICATION_ID, APPLICATION_NO, CREDIT_TYPE, APPLIED_AMOUNT,
    CURRENCY_CODE, CREDIT_PURPOSE, CREDIT_PERIOD, PERIOD_UNIT,
    INTEREST_RATE, FINANCIAL_INSTITUTION_ID, FINANCIAL_INSTITUTION_NAME,
    APPLICATION_STATUS, APPLICATION_DATE, APPROVAL_DATE, APPROVAL_AMOUNT,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'CA202601001', '流动资金贷款', 10000000.00,
    'CNY', '日常经营周转', 36, '月',
    4.35, 1, '中国工商银行股份有限公司',
    'APPROVED', '2026-01-01', '2026-01-10', 10000000.00,
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'CA202601002', '固定资产贷款', 50000000.00,
    'CNY', '设备购置', 60, '月',
    4.75, 2, '中国建设银行股份有限公司',
    'APPROVED', '2026-01-05', '2026-01-12', 50000000.00,
    1, '华博云科技有限公司',
    1, '李四', CURRENT_TIMESTAMP
),
(
    3, 'CA202601003', '综合授信', 80000000.00,
    'CNY', '综合授信额度', 12, '月',
    4.50, 3, '中国农业银行股份有限公司',
    'PENDING', '2026-01-10', NULL, NULL,
    1, '华博云科技有限公司',
    1, '王五', CURRENT_TIMESTAMP
);

-- 3.2 授信合同测试数据
INSERT INTO TBL_CREDIT_CONTRACT (
    CONTRACT_ID, CONTRACT_NO, APPLICATION_ID, CREDIT_TYPE,
    CREDIT_LIMIT, CURRENCY_CODE, USED_AMOUNT, AVAILABLE_AMOUNT,
    INTEREST_RATE, CREDIT_PERIOD, PERIOD_UNIT, START_DATE, END_DATE,
    FINANCIAL_INSTITUTION_ID, FINANCIAL_INSTITUTION_NAME,
    CONTRACT_STATUS, SIGNING_DATE,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'CC202601001', 1, '流动资金贷款',
    10000000.00, 'CNY', 3000000.00, 7000000.00,
    4.35, 36, '月', '2026-01-15', '2029-01-15',
    1, '中国工商银行股份有限公司',
    'ACTIVE', '2026-01-15',
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'CC202601002', 2, '固定资产贷款',
    50000000.00, 'CNY', 20000000.00, 30000000.00,
    4.75, 60, '月', '2026-01-18', '2031-01-18',
    2, '中国建设银行股份有限公司',
    'ACTIVE', '2026-01-18',
    1, '华博云科技有限公司',
    1, '李四', CURRENT_TIMESTAMP
);

-- 3.3 授信额度测试数据
INSERT INTO TBL_CREDIT_LIMIT (
    LIMIT_ID, CONTRACT_ID, LIMIT_TYPE, TOTAL_LIMIT,
    USED_LIMIT, AVAILABLE_LIMIT, CURRENCY_CODE,
    LIMIT_STATUS, EFFECTIVE_DATE, EXPIRY_DATE,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 1, '流动资金', 10000000.00,
    3000000.00, 7000000.00, 'CNY',
    'ACTIVE', '2026-01-15', '2029-01-15',
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 2, '固定资产', 50000000.00,
    20000000.00, 30000000.00, 'CNY',
    'ACTIVE', '2026-01-18', '2031-01-18',
    1, '华博云科技有限公司',
    1, '李四', CURRENT_TIMESTAMP
);

-- ============================================================
-- 第四部分: 银行贷款测试数据
-- ============================================================

INSERT INTO TBL_BANK_LOAN (
    LOAN_ID, LOAN_NO, CONTRACT_ID, LOAN_TYPE,
    LOAN_AMOUNT, CURRENCY_CODE, INTEREST_RATE,
    LOAN_PERIOD, PERIOD_UNIT, START_DATE, END_DATE, PAYMENT_METHOD,
    FINANCIAL_INSTITUTION_ID, FINANCIAL_INSTITUTION_NAME,
    LOAN_STATUS, DRAWDOWN_DATE,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'BL202601001', 1, '流动资金贷款',
    3000000.00, 'CNY', 4.35,
    36, '月', '2026-01-20', '2029-01-20', '等额本息',
    1, '中国工商银行股份有限公司',
    'ACTIVE', '2026-01-20',
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'BL202601002', 2, '固定资产贷款',
    20000000.00, 'CNY', 4.75,
    60, '月', '2026-01-25', '2031-01-25', '等额本金',
    2, '中国建设银行股份有限公司',
    'ACTIVE', '2026-01-25',
    1, '华博云科技有限公司',
    1, '李四', CURRENT_TIMESTAMP
);

-- ============================================================
-- 第五部分: 票据贴现测试数据
-- ============================================================

INSERT INTO TBL_BILL_DISCOUNT (
    DISCOUNT_ID, DISCOUNT_NO, BILL_TYPE, BILL_NO,
    BILL_AMOUNT, CURRENCY_CODE, DISCOUNT_RATE, DISCOUNT_AMOUNT,
    DISCOUNT_PERIOD, BILL_ISSUE_DATE, BILL_MATURITY_DATE, DISCOUNT_DATE,
    ACCEPTOR_NAME, DRAWER_NAME, PAYEE_NAME,
    FINANCIAL_INSTITUTION_ID, FINANCIAL_INSTITUTION_NAME,
    DISCOUNT_STATUS,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'BD202601001', '银行承兑汇票', 'HB202601001',
    5000000.00, 'CNY', 3.50, 43750.00,
    90, '2026-01-01', '2026-04-01', '2026-01-10',
    '中国工商银行股份有限公司', '华博云科技有限公司', 'XX贸易有限公司',
    1, '中国工商银行股份有限公司',
    'ACTIVE',
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'BD202601002', '商业承兑汇票', 'HB202601002',
    3000000.00, 'CNY', 4.20, 31500.00,
    60, '2026-01-05', '2026-03-06', '2026-01-15',
    'XX集团有限公司', 'XX贸易有限公司', '华博云科技有限公司',
    2, '中国建设银行股份有限公司',
    'ACTIVE',
    1, '华博云科技有限公司',
    1, '李四', CURRENT_TIMESTAMP
);

-- ============================================================
-- 第六部分: 银行承兑测试数据
-- ============================================================

INSERT INTO TBL_BILL_ACCEPTANCE (
    ACCEPTANCE_ID, ACCEPTANCE_NO, BILL_TYPE, BILL_NO,
    BILL_AMOUNT, CURRENCY_CODE, ACCEPTANCE_PERIOD, PERIOD_UNIT,
    ISSUE_DATE, MATURITY_DATE, ACCEPTANCE_RATE, ACCEPTANCE_FEE,
    DRAWER_NAME, PAYEE_NAME, ACCEPTOR_NAME,
    FINANCIAL_INSTITUTION_ID, FINANCIAL_INSTITUTION_NAME,
    ACCEPTANCE_STATUS,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'BA202601001', '银行承兑汇票', 'HB202601003',
    8000000.00, 'CNY', 6, '月',
    '2026-01-10', '2026-07-10', 0.50, 20000.00,
    '华博云科技有限公司', 'XX供应商有限公司', '中国工商银行股份有限公司',
    1, '中国工商银行股份有限公司',
    'ACTIVE',
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'BA202601002', '银行承兑汇票', 'HB202601004',
    6000000.00, 'CNY', 3, '月',
    '2026-01-15', '2026-04-15', 0.45, 6750.00,
    '华博云科技有限公司', 'XX材料有限公司', '中国建设银行股份有限公司',
    2, '中国建设银行股份有限公司',
    'ACTIVE',
    1, '华博云科技有限公司',
    1, '李四', CURRENT_TIMESTAMP
);

-- ============================================================
-- 第七部分: 融资租赁测试数据
-- ============================================================

INSERT INTO TBL_FINANCIAL_LEASE (
    LEASE_ID, LEASE_NO, PLAN_ID, LEASE_TYPE,
    ASSET_NAME, ASSET_VALUE, CURRENCY_CODE,
    LEASE_AMOUNT, LEASE_PERIOD, PERIOD_UNIT,
    LEASE_RATE, START_DATE, END_DATE, PAYMENT_METHOD,
    LEASING_COMPANY_ID, LEASING_COMPANY_NAME,
    LEASE_STATUS, SIGNING_DATE,
    COMPANY_ID, COMPANY_NAME,
    CREATED_BY, CREATED_BY_NAME, CREATED_TIME
) VALUES
(
    1, 'FL202601001', NULL, '融资租赁',
    '数控机床设备XX型', 5000000.00, 'CNY',
    5500000.00, 36, '月',
    6.80, '2026-01-20', '2029-01-20', '等额本息',
    5, '远东宏信有限公司',
    'ACTIVE', '2026-01-20',
    1, '华博云科技有限公司',
    1, '张三', CURRENT_TIMESTAMP
),
(
    2, 'FL202601002', NULL, '融资租赁',
    '办公设备XX套', 800000.00, 'CNY',
    850000.00, 24, '月',
    6.50, '2026-01-25', '2028-01-25', '等额本金',
    6, '中航国际租赁有限公司',
    'ACTIVE', '2026-01-25',
    1, '华博云科技有限公司',
    1, '李四', CURRENT_TIMESTAMP
);

-- ============================================================
-- 脚本结束
-- ============================================================

-- 验证插入的数据
SELECT '金融机构表' AS TABLE_NAME, COUNT(*) AS RECORD_COUNT FROM TBL_FINANCIAL_INSTITUTION
UNION ALL
SELECT '担保申请表', COUNT(*) FROM TBL_GUARANTEE_APPLICATION
UNION ALL
SELECT '担保合同表', COUNT(*) FROM TBL_GUARANTEE_CONTRACT
UNION ALL
SELECT '担保物表', COUNT(*) FROM TBL_COLLATERAL
UNION ALL
SELECT '授信申请表', COUNT(*) FROM TBL_CREDIT_APPLICATION
UNION ALL
SELECT '授信合同表', COUNT(*) FROM TBL_CREDIT_CONTRACT
UNION ALL
SELECT '授信额度表', COUNT(*) FROM TBL_CREDIT_LIMIT
UNION ALL
SELECT '银行贷款表', COUNT(*) FROM TBL_BANK_LOAN
UNION ALL
SELECT '票据贴现表', COUNT(*) FROM TBL_BILL_DISCOUNT
UNION ALL
SELECT '银行承兑表', COUNT(*) FROM TBL_BILL_ACCEPTANCE
UNION ALL
SELECT '融资租赁表', COUNT(*) FROM TBL_FINANCIAL_LEASE;
