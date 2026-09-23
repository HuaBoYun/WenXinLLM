-- ============================================================
-- 基础配置页面测试数据
-- 包含: 业务系统注册、数据映射配置、电票账户配置
-- 数据库: 达梦数据库 (DM8)
-- 日期: 2025-01-21
-- ============================================================

-- 1. 业务系统注册测试数据
INSERT INTO TC_BUSINESS_SYSTEM (ID, SYSTEM_CODE, SYSTEM_NAME, SYSTEM_TYPE, SYSTEM_VERSION, SYSTEM_DESC, CONNECTION_URL, AUTH_TYPE, AUTH_CONFIG, API_KEY, SECRET_KEY, STATUS, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER)
VALUES
(1, 'ERP001', 'ERP财务系统', 'FINANCE', 'V2.0', '企业核心ERP系统', 'http://erp.company.com/api', 'API_KEY', '{"timeout": 30000}', 'key_erp_001', 'secret_erp_001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(2, 'BANK001', '工商银行系统', 'BANK', 'V1.5', '工商银行对接系统', 'https://bank.icbc.com.cn/gateway', 'OAUTH2', '{"clientId": "icbc_client", "scope": "read"}', 'key_bank_001', 'secret_bank_001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(3, 'TAX001', '税务系统', 'THIRD_PARTY', 'V3.0', '国家税务接口系统', 'http://tax.gov.cn/api', 'SIGNATURE', '{"algorithm": "RSA"}', 'key_tax_001', 'secret_tax_001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(4, 'OA001', 'OA办公系统', 'ERP', 'V1.0', '协同办公系统', 'http://oa.company.com', 'TOKEN', '{"tokenHeader": "X-Auth-Token"}', 'key_oa_001', 'secret_oa_001', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin');

-- 2. 数据映射配置测试数据
INSERT INTO TC_DATA_MAPPING (ID, SYSTEM_ID, MAPPING_NAME, SOURCE_FIELD, TARGET_FIELD, FIELD_TYPE, CONVERSION_RULE, DEFAULT_VALUE, IS_REQUIRED, VALIDATION_RULE, ERROR_STRATEGY, SORT_ORDER, STATUS, REMARK, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER)
VALUES
(1, 1, '订单号映射', 'orderNo', 'ORDER_ID', 'STRING', 'TRIM', NULL, '1', 'NOT_NULL', 'SKIP', 1, 1, 'ERP订单号映射到财务系统', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(2, 1, '金额映射', 'amount', 'AMOUNT', 'DECIMAL', 'ROUND(2)', '0.00', '1', 'GREATER_THAN_ZERO', 'LOG', 2, 1, '订单金额映射', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(3, 2, '银行流水号', 'bankFlowNo', 'BANK_FLOW_ID', 'STRING', 'UPPER', NULL, '1', 'NOT_NULL', 'SKIP', 3, 1, '银行流水号映射', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(4, 2, '交易日期', 'tradeDate', 'TRADE_DATE', 'DATE', 'TO_DATE(yyyy-MM-dd)', NULL, '1', 'NOT_NULL', 'SKIP', 4, 1, '交易日期格式转换', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(5, 3, '发票代码', 'invoiceCode', 'INVOICE_CODE', 'STRING', 'TRIM', NULL, '1', 'LENGTH(12)', 'SKIP', 5, 1, '税务发票代码映射', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin');

-- 3. 电票账户配置测试数据
INSERT INTO TC_ETICKET_ACCOUNT (ID, ACCOUNT_NO, BANK_CODE, BANK_NAME, CUSTOMER_NO, ETICKET_STATUS, BUSINESS_SCOPE, CURRENCY_CODES, DAILY_LIMIT, MONTHLY_LIMIT, INTERFACE_URL, INTERFACE_PORT, CERT_PATH, CERT_PASSWORD, OPERATOR_INFO, TIMEOUT_SETTING, RETRY_COUNT, LAST_CONNECT_TIME, CONNECTION_STATUS, STATUS, REMARK, CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER)
VALUES
(1, 'ECDS_ACC_001', 'ICBC', '工商银行', 'CUST_001', 'NORMAL', 'ACCEPTANCE,DISCOUNT', 'CNY', 1000000.00, 30000000.00, 'https://ecds.icbc.com.cn/api', '8443', '/certs/icds_client.p12', 'pwd123', '张三|admin|oper123', '30000', 3, CURRENT_TIMESTAMP, 'SUCCESS', 1, '工商银行电票账户1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(2, 'ECDS_ACC_002', 'CCB', '建设银行', 'CUST_002', 'NORMAL', 'ACCEPTANCE,REDISCOUNT', 'CNY', 2000000.00, 50000000.00, 'https://ecds.ccb.com/api', '8443', '/certs/ccb_client.p12', 'pwd456', '李四|admin|oper456', '30000', 3, CURRENT_TIMESTAMP, 'SUCCESS', 1, '建设银行电票账户2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(3, 'ECDS_ACC_003', 'ABC', '农业银行', 'CUST_003', 'NORMAL', 'DISCOUNT,PLEDGE', 'CNY', 1500000.00, 40000000.00, 'https://ecds.abchina.com/api', '8443', '/certs/abc_client.p12', 'pwd789', '王五|admin|oper789', '30000', 3, CURRENT_TIMESTAMP, 'SUCCESS', 1, '农业银行电票账户3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin'),
(4, 'ECDS_ACC_004', 'BOC', '中国银行', 'CUST_004', 'FROZEN', 'ACCEPTANCE', 'CNY', 800000.00, 20000000.00, 'https://ecds.boc.com/api', '8443', '/certs/boc_client.p12', 'pwd000', '赵六|admin|oper000', '30000', 3, CURRENT_TIMESTAMP, 'FAILED', 0, '中国银行电票账户4(冻结)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin');

-- 提交事务
COMMIT;

-- 查询验证
SELECT '业务系统数据:' AS 类型, COUNT(*) AS 记录数 FROM TC_BUSINESS_SYSTEM
UNION ALL
SELECT '数据映射数据:', COUNT(*) FROM TC_DATA_MAPPING
UNION ALL
SELECT '电票账户数据:', COUNT(*) FROM TC_ETICKET_ACCOUNT;
