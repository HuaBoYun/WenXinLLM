-- 插入测试数据 - 业务系统注册表
-- 兼容达梦数据库

-- 删除已存在的测试数据
DELETE FROM TC_BUSINESS_SYSTEM WHERE SYSTEM_CODE LIKE 'TEST_%';

-- 插入测试数据
INSERT INTO TC_BUSINESS_SYSTEM (
    ID, SYSTEM_CODE, SYSTEM_NAME, SYSTEM_TYPE, SYSTEM_DESC,
    INTERFACE_URL, AUTH_TYPE, AUTH_CONFIG, DATA_FORMAT,
    CONNECTION_STATUS, STATUS, REMARK,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES
(
    '1000000001', 'TEST_FINANCE_001', '华博云财务系统', 'FINANCE',
    '企业核心财务管理平台,支持总账、应收应付、固定资产管理',
    'http://finance.hbyun.com/api', 'OAUTH2', '{"tokenUrl":"http://finance.hbyun.com/oauth/token"}', 'JSON',
    'ONLINE', '1', '系统运行正常',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '1000000002', 'TEST_BANK_ICBC', '工商银行直连系统', 'BANK',
    '工商银行银企直连平台,提供账户查询、转账、代发薪等服务',
    'https://icbc.hbyun.com:8443/gateway', 'API_KEY', '{"appId":"HBICBC001","appSecret":"REDACTED"}', 'XML',
    'ONLINE', '1', '银企直连',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '1000000003', 'TEST_ERP_SAP', 'SAP ERP系统', 'ERP',
    'SAP S/4HANA企业资源规划系统,集成财务、采购、销售、库存等模块',
    'http://sap.hbyun.com:8000/api/v1', 'JWT', '{"issuer":"http://sap.hbyun.com","audience":"api"}', 'JSON',
    'ONLINE', '1', 'S/4HANA 2020',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '1000000004', 'TEST_CRM_SFDC', 'Salesforce CRM系统', 'CRM',
    'Salesforce客户关系管理系统,管理客户、商机、合同等信息',
    'https://salesforce.hbyun.com/services/data/v54.0', 'OAUTH2', '{"clientId":"HB_SFDC_001"}', 'JSON',
    'OFFLINE', '1', '维护中',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '1000000005', 'TEST_TAX', '税务申报系统', 'THIRD_PARTY',
    '国家税务总局金税三期系统接口,支持增值税、企业所得税等税种申报',
    'https://tax.chinatax.gov.cn/api', 'BASIC_AUTH', '{"username":"HB_TAX_001"}', 'JSON',
    'ONLINE', '1', '电子税务局',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '1000000006', 'TEST_BANK_CCB', '建设银行现金管理系统', 'BANK',
    '建设银行现金管理平台,提供资金归集、池化、调拨等功能',
    'https://ccb.hbyun.com:9443/cashmgmt', 'API_KEY', '{"appId":"HBCCB002"}', 'JSON',
    'ERROR', '0', '连接异常,正在排查',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '1000000007', 'TEST_PAYMENT_ALIPAY', '支付宝支付平台', 'THIRD_PARTY',
    '支付宝开放平台,支持在线支付、扫码支付、APP支付等',
    'https://openapi.alipay.com/gateway.do', 'API_KEY', '{"appId":"2021000012345678"}', 'JSON',
    'ONLINE', '1', '蚂蚁集团',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '1000000008', 'TEST_PAYMENT_WX', '微信支付平台', 'THIRD_PARTY',
    '微信支付商户平台,支持公众号支付、小程序支付、H5支付等',
    'https://api.mch.weixin.qq.com/pay', 'API_KEY', '{"mchId":"1234567890"}', 'XML',
    'ONLINE', '1', '腾讯财付通',
    SYSDATE, SYSDATE, 'admin', 'admin'
);

COMMIT;

-- ========================================
-- 插入测试数据 - 数据映射配置表
-- ========================================

-- 删除已存在的测试数据
DELETE FROM TC_DATA_MAPPING WHERE MAPPING_NAME LIKE '测试_%';

-- 插入测试数据
INSERT INTO TC_DATA_MAPPING (
    ID, MAPPING_CODE, MAPPING_NAME, SOURCE_SYSTEM, TARGET_SYSTEM,
    MAPPING_TYPE, SOURCE_FIELDS, TARGET_FIELDS, MAPPING_RULES,
    TRANSFORM_FUNCTION, DESCRIPTION, STATUS,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES
(
    '2000000001', 'MAP_FIN_2_ERP_001', '测试_财务凭证映射到ERP', 'FINANCE', 'ERP',
    'FIELD', 'voucher_no,voucher_date,amount', 'voucher_id,posting_date,amount',
    '{"ruleType":"direct","description":"直接映射"}',
    'formatAmount', '财务系统凭证数据映射到ERP总账模块', '1',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '2000000002', 'MAP_BANK_2_FIN_001', '测试_银行流水映射到财务', 'BANK', 'FINANCE',
    'FIELD', 'trans_date,trans_amount,trans_type,balance', 'transaction_date,transaction_amount,transaction_type,account_balance',
    '{"ruleType":"direct","description":"直接映射,需格式化日期"}',
    'formatDate', '银行流水数据映射到财务系统现金日记账', '1',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '2000000003', 'MAP_CUST_2_CRM_001', '测试_客户信息映射到CRM', 'ERP', 'CRM',
    'FIELD', 'customer_code,customer_name,contact,tel', 'account_id,name,phone_number,email',
    '{"ruleType":"valueMapping","mappings":{"customer_type":{"VIP":"platinum","普通":"standard"}}}',
    'mergeContactInfo', 'ERP客户主数据同步到Salesforce CRM', '1',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '2000000004', 'MAP_ORDER_2_FIN_001', '测试_订单金额汇总映射', 'THIRD_PARTY', 'FINANCE',
    'FUNCTION', 'order_id,order_date,customer_id,order_items', 'period,customer_code,total_amount',
    '{"ruleType":"aggregation","function":"SUM","groupBy":"customer_code"}',
    'calculateOrderTotal', '电商平台订单数据按客户汇总后映射到财务应收', '1',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '2000000005', 'MAP_INV_2_TAX_001', '测试_发票信息映射到税务', 'FINANCE', 'THIRD_PARTY',
    'FIELD', 'invoice_code,invoice_no,amount,tax_amount', 'invoice_code,invoice_number,total_amount,tax_amount',
    '{"ruleType":"direct","validation":"required"}',
    'validateInvoice', '进项发票信息映射到税务申报系统', '1',
    SYSDATE, SYSDATE, 'admin', 'admin'
);

COMMIT;

-- ========================================
-- 插入测试数据 - 电票账户配置表
-- ========================================

-- 删除已存在的测试数据
DELETE FROM TC_ETICKET_ACCOUNT WHERE ACCOUNT_NUMBER LIKE 'TEST_%';

-- 插入测试数据
INSERT INTO TC_ETICKET_ACCOUNT (
    ETICKET_ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_NAME, ETICKET_SYSTEM, ACCOUNT_TYPE,
    BANK_NAME, BANK_CODE, BANK_ACCOUNT_NUMBER, BRANCH_NAME,
    ACCOUNT_BALANCE, CREDIT_LIMIT, SYNC_STATUS, ACCOUNT_STATUS,
    OPEN_DATE, EXPIRY_DATE, DESCRIPTION, REMARK,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES
(
    '3000000001', 'TEST_ETK_001', '测试_工商银行电票账户', 'ECDS', 'ACCEPTANCE',
    '工商银行', 'ICBC', '6222020200001234567', '北京分行营业部',
    5000000.00, 10000000.00, 'SUCCESS', 'NORMAL',
    TO_DATE('2023-01-15', 'YYYY-MM-DD'), TO_DATE('2028-01-15', 'YYYY-MM-DD'),
    '用于接收银行承兑汇票', 'ECDS系统已开通',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '3000000002', 'TEST_ETK_002', '测试_建设银行电票账户', 'ECDS', 'DISCOUNT',
    '建设银行', 'CCB', '6217000012345678901', '上海浦东分行',
    3000000.00, 8000000.00, 'SUCCESS', 'NORMAL',
    TO_DATE('2023-03-20', 'YYYY-MM-DD'), TO_DATE('2028-03-20', 'YYYY-MM-DD'),
    '用于票据贴现业务', '贴现额度充足',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '3000000003', 'TEST_ETK_003', '测试_招商银行电票账户', 'BECP', 'ACCEPTANCE',
    '招商银行', 'CMB', '6214830123456789', '深圳分行',
    8000000.00, 15000000.00, 'SUCCESS', 'NORMAL',
    TO_DATE('2023-06-10', 'YYYY-MM-DD'), TO_DATE('2028-06-10', 'YYYY-MM-DD'),
    '商业承兑汇票专用账户', 'BECP系统',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '3000000004', 'TEST_ETK_004', '测试_农业银行保证金账户', 'ECDS', 'MARGIN',
    '农业银行', 'ABC', '6228480034567890123', '广州天河支行',
    2000000.00, 5000000.00, 'PENDING', 'NORMAL',
    TO_DATE('2023-08-05', 'YYYY-MM-DD'), TO_DATE('2028-08-05', 'YYYY-MM-DD'),
    '电票保证金账户', '待激活',
    SYSDATE, SYSDATE, 'admin', 'admin'
),
(
    '3000000005', 'TEST_ETK_005', '测试_中国银行质押账户', 'BANK_ETICKET', 'PLEDGE',
    '中国银行', 'BOC', '6216610023456789012', '杭州西湖支行',
    4500000.00, 12000000.00, 'SUCCESS', 'FROZEN',
    TO_DATE('2023-09-01', 'YYYY-MM-DD'), TO_DATE('2028-09-01', 'YYYY-MM-DD'),
    '票据质押账户', '账户冻结中',
    SYSDATE, SYSDATE, 'admin', 'admin'
);

COMMIT;

-- 查询验证
SELECT
    ID,
    SYSTEM_CODE,
    SYSTEM_NAME,
    SYSTEM_TYPE,
    INTERFACE_URL,
    AUTH_TYPE,
    CONNECTION_STATUS,
    STATUS,
    CREATE_TIME
FROM TC_BUSINESS_SYSTEM
ORDER BY CREATE_TIME DESC;

SELECT
    ID,
    MAPPING_CODE,
    MAPPING_NAME,
    SOURCE_SYSTEM,
    TARGET_SYSTEM,
    MAPPING_TYPE,
    STATUS,
    CREATE_TIME
FROM TC_DATA_MAPPING
ORDER BY CREATE_TIME DESC;

SELECT
    ETICKET_ACCOUNT_ID,
    ACCOUNT_NUMBER,
    ACCOUNT_NAME,
    ETICKET_SYSTEM,
    ACCOUNT_TYPE,
    BANK_NAME,
    ACCOUNT_BALANCE,
    ACCOUNT_STATUS,
    CREATE_TIME
FROM TC_ETICKET_ACCOUNT
ORDER BY CREATE_TIME DESC;
