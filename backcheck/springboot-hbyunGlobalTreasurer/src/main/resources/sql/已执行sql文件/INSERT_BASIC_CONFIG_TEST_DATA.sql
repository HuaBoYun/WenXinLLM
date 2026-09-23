-- ====================================
-- 基础配置测试数据
-- 包含：业务系统注册、数据映射配置、电票账户配置
-- ====================================

-- 1. 业务系统注册测试数据
-- ====================================
INSERT INTO TC_BUSINESS_SYSTEM (BUSINESS_SYSTEM_ID, SYSTEM_CODE, SYSTEM_NAME, SYSTEM_TYPE, SYSTEM_VERSION, SYSTEM_DESC, API_URL, AUTH_TYPE, AUTH_CONFIG, API_KEY, SECRET_KEY, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER, REMARK)
VALUES
(1, 'FIN001', '财务管理系统', 'FINANCE', 'V2.0', '企业核心财务系统', 'http://finance.example.com/api', 'OAUTH2', '{"tokenUrl": "/oauth/token"}', 'key_fin_001', 'secret_fin_001', 1, SYSDATE, SYSDATE, 'admin', 'admin', '核心财务系统'),
(2, 'BANK001', '银行直连系统', 'BANK', 'V1.5', '各大银行直连接口', 'http://bank.example.com/api', 'API_KEY', '{"timeout": 30000}', 'key_bank_001', 'secret_bank_001', 1, SYSDATE, SYSDATE, 'admin', 'admin', '工商银行、建设银行等'),
(3, 'ERP001', 'ERP系统集成', 'ERP', 'V3.0', '企业资源计划系统', 'http://erp.example.com/api', 'JWT', '{"algorithm": "HS256"}', 'key_erp_001', 'secret_erp_001', 1, SYSDATE, SYSDATE, 'admin', 'admin', 'SAP/用友/金蝶'),
(4, 'CRM001', '客户关系管理', 'CRM', 'V2.1', 'CRM系统集成', 'http://crm.example.com/api', 'BASIC_AUTH', '{"encoding": "UTF-8"}', 'key_crm_001', 'secret_crm_001', 1, SYSDATE, SYSDATE, 'admin', 'admin', '客户管理系统'),
(5, 'TAX001', '税务对接系统', 'THIRD_PARTY', 'V1.0', '国家税务总局接口', 'http://tax.example.com/api', 'API_KEY', '{"retryTimes": 3}', 'key_tax_001', 'secret_tax_001', 1, SYSDATE, SYSDATE, 'admin', 'admin', '税务系统');

-- 2. 数据映射配置测试数据
-- ====================================
INSERT INTO TC_DATA_MAPPING (DATA_MAPPING_ID, MAPPING_CODE, MAPPING_NAME, SOURCE_SYSTEM, TARGET_SYSTEM, MAPPING_TYPE, SOURCE_FIELD, TARGET_FIELD, MAPPING_RULE, TRANSFORM_RULE, DESCRIPTION, STATUS, PRIORITY, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER, REMARK)
VALUES
(1, 'MAP001', '财务系统到ERP映射', 'FINANCE', 'ERP', 'FIELD', 'order_amount', 'amount', '直接映射', NULL, '订单金额字段映射', 1, 1, SYSDATE, SYSDATE, 'admin', 'admin', '基础字段映射'),
(2, 'MAP002', '银行到财务映射', 'BANK', 'FINANCE', 'FUNCTION', 'transaction_date', 'create_time', '日期格式转换', "DATE_FORMAT(transaction_date, 'YYYY-MM-DD')", '交易日期转换', 1, 2, SYSDATE, SYSDATE, 'admin', 'admin', '日期函数转换'),
(3, 'MAP003', 'CRM到财务映射', 'CRM', 'FINANCE', 'VALUE', 'customer_type', 'account_type', '客户类型映射', '{"VIP": "A", "普通": "B"}', '客户类型值映射', 1, 3, SYSDATE, SYSDATE, 'admin', 'admin', '值映射'),
(4, 'MAP004', 'ERP到银行映射', 'ERP', 'BANK', 'FIELD', 'payment_account', 'account_number', '付款账号映射', NULL, '付款账号直接映射', 1, 4, SYSDATE, SYSDATE, 'admin', 'admin', '账号字段'),
(5, 'MAP005', '税务到财务映射', 'THIRD_PARTY', 'FINANCE', 'FUNCTION', 'tax_amount', 'tax_fee', '税额计算', 'tax_amount * 0.13', '增值税计算', 1, 5, SYSDATE, SYSDATE, 'admin', 'admin', '税额计算');

-- 3. 电票账户配置测试数据
-- ====================================
INSERT INTO TC_ETICKET_ACCOUNT (ETICKET_ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_NAME, ETICKET_TYPE, ACCOUNT_TYPE, BANK_ID, BANK_NAME, BANK_ACCOUNT_NUMBER, ACCOUNT_BALANCE, CREDIT_LIMIT, OPEN_DATE, EXPIRE_DATE, ACCOUNT_STATUS, CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER, REMARK)
VALUES
(1, 'ETK001', '企业电票主账户', 'ECDS', 'ACCEPTANCE', 'ICBC', '工商银行', '6222020200001234567', 1000000.00, 5000000.00, TO_DATE('2024-01-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), 'NORMAL', '张三', '13800138000', 'zhangsan@example.com', 1, SYSDATE, SYSDATE, 'admin', 'admin', 'ECDS系统主账户'),
(2, 'ETK002', '建行电票账户', 'BECP', 'DISCOUNT', 'CCB', '建设银行', '6227000220002345678', 2000000.00, 8000000.00, TO_DATE('2024-02-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), 'NORMAL', '李四', '13800138000', 'lisi@example.com', 1, SYSDATE, SYSDATE, 'admin', 'admin', 'BECP贴现账户'),
(3, 'ETK003', '农行电票账户', 'BANK_ETICKET', 'REDISCOUNT', 'ABC', '农业银行', '6228480402567890123', 1500000.00, 6000000.00, TO_DATE('2024-03-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), 'NORMAL', '王五', '13800138000', 'wangwu@example.com', 1, SYSDATE, SYSDATE, 'admin', 'admin', '银行电票转贴现'),
(4, 'ETK004', '中行质押账户', 'ECDS', 'PLEDGE', 'BOC', '中国银行', '6216615500001234567', 3000000.00, 10000000.00, TO_DATE('2024-04-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), 'NORMAL', '赵六', '13800138000', 'zhaoliu@example.com', 1, SYSDATE, SYSDATE, 'admin', 'admin', '质押账户'),
(5, 'ETK005', '招行托管账户', 'BECP', 'CUSTODY', 'CMB', '招商银行', '6214830200007654321', 5000000.00, 15000000.00, TO_DATE('2024-05-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), 'NORMAL', '孙七', '13800138000', 'sunqi@example.com', 1, SYSDATE, SYSDATE, 'admin', 'admin', '资金托管账户');

COMMIT;

-- 查询验证语句
-- SELECT * FROM TC_BUSINESS_SYSTEM;
-- SELECT * FROM TC_DATA_MAPPING;
-- SELECT * FROM TC_ETICKET_ACCOUNT;
