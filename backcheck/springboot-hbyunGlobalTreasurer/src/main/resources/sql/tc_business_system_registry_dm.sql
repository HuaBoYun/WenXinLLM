-- =============================================================
-- 业务系统注册表 - 达梦数据库建表脚本
-- 表名: TBL_BUSINESS_SYSTEM_REGISTRY
-- 描述: 用于注册和管理外部业务系统的配置信息
-- 数据库: 达梦数据库 DM8
-- 编码: UTF-8
-- =============================================================

-- 删除已存在的表(如果存在)
DROP TABLE IF EXISTS TBL_BUSINESS_SYSTEM_REGISTRY;

-- =============================================================
-- 创建表结构
-- =============================================================
CREATE TABLE TBL_BUSINESS_SYSTEM_REGISTRY (
    ID BIGINT NOT NULL,
    SYSTEM_NAME VARCHAR(200) NOT NULL,
    SYSTEM_CODE VARCHAR(100) NOT NULL,
    SYSTEM_TYPE VARCHAR(50) NOT NULL,
    API_URL VARCHAR(500),
    AUTH_TYPE VARCHAR(50),
    AUTH_CONFIG CLOB,
    DESCRIPTION VARCHAR(500),
    SYSTEM_DESC VARCHAR(500),
    STATUS VARCHAR(10) DEFAULT '1',
    CONNECTION_STATUS VARCHAR(20) DEFAULT 'OFFLINE',
    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CREATE_USER VARCHAR(100),
    UPDATE_USER VARCHAR(100),
    CONSTRAINT PK_BUSINESS_SYSTEM_REGISTRY PRIMARY KEY (ID)
);

-- =============================================================
-- 字段注释
-- =============================================================
COMMENT ON TABLE TBL_BUSINESS_SYSTEM_REGISTRY IS '业务系统注册表';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.ID IS '主键ID';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.SYSTEM_NAME IS '系统名称';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.SYSTEM_CODE IS '系统编码(唯一标识)';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.SYSTEM_TYPE IS '系统类型(ERP/银行系统/税务系统等)';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.API_URL IS 'API接口地址';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.AUTH_TYPE IS '认证方式(API_KEY/OAUTH2/BASIC_AUTH等)';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.AUTH_CONFIG IS '认证配置(JSON格式)';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.DESCRIPTION IS '系统描述';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.SYSTEM_DESC IS '系统详细描述';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.STATUS IS '状态(1-启用,0-禁用)';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.CONNECTION_STATUS IS '连接状态(ONLINE-在线,OFFLINE-离线,ERROR-错误)';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.UPDATE_TIME IS '更新时间';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.CREATE_USER IS '创建人';
COMMENT ON COLUMN TBL_BUSINESS_SYSTEM_REGISTRY.UPDATE_USER IS '更新人';

-- =============================================================
-- 创建索引
-- =============================================================

-- 系统编码唯一索引
CREATE UNIQUE INDEX IDX_SYSTEM_CODE ON TBL_BUSINESS_SYSTEM_REGISTRY(SYSTEM_CODE);

-- 系统名称索引(用于模糊查询)
CREATE INDEX IDX_SYSTEM_NAME ON TBL_BUSINESS_SYSTEM_REGISTRY(SYSTEM_NAME);

-- 系统类型索引(用于按类型筛选)
CREATE INDEX IDX_SYSTEM_TYPE ON TBL_BUSINESS_SYSTEM_REGISTRY(SYSTEM_TYPE);

-- 连接状态索引(用于状态查询)
CREATE INDEX IDX_CONNECTION_STATUS ON TBL_BUSINESS_SYSTEM_REGISTRY(CONNECTION_STATUS);

-- 状态索引(用于启用/禁用筛选)
CREATE INDEX IDX_STATUS ON TBL_BUSINESS_SYSTEM_REGISTRY(STATUS);

-- 创建时间索引(用于排序和查询)
CREATE INDEX IDX_CREATE_TIME ON TBL_BUSINESS_SYSTEM_REGISTRY(CREATE_TIME DESC);

-- =============================================================
-- 插入测试数据
-- =============================================================

-- 测试数据1: ERP系统
INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY (
    ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG,
    DESCRIPTION, SYSTEM_DESC, STATUS, CONNECTION_STATUS,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES (
    1,
    'SAP ERP系统',
    'SAP_ERP_001',
    'ERP',
    'https://sap-api.company.com/api/v1',
    'OAUTH2',
    '{"clientId": "sap_client_001", "clientSecret": "REDACTED", "tokenUrl": "https://sap-api.company.com/oauth/token"}',
    '公司SAP ERP系统,用于财务管理',
    'SAP S/4HANA系统,包含财务、采购、销售模块',
    '1',
    'ONLINE',
    TIMESTAMP '2024-12-20 10:00:00',
    TIMESTAMP '2024-12-24 10:30:00',
    'admin',
    'admin'
);

-- 测试数据2: 银行系统
INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY (
    ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG,
    DESCRIPTION, SYSTEM_DESC, STATUS, CONNECTION_STATUS,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES (
    2,
    '工商银行银企直联',
    'ICB_BANK_001',
    '银行系统',
    'https://ebank.icbc.com.cn/api',
    'API_KEY',
    '{"apiKey": "icbc_key_***", "accountNo": "1234567890"}',
    '工商银行银企直联系统,用于资金查询和转账',
    '工商银行B2B系统,支持账户查询、转账、对账等功能',
    '1',
    'ONLINE',
    TIMESTAMP '2024-12-21 14:20:00',
    TIMESTAMP '2024-12-24 11:00:00',
    'admin',
    'zhangsan'
);

-- 测试数据3: 税务系统
INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY (
    ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG,
    DESCRIPTION, SYSTEM_DESC, STATUS, CONNECTION_STATUS,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES (
    3,
    '国家税务总局电子税务局',
    'TAX_SYSTEM_001',
    '税务系统',
    'https://etax.chinatax.gov.cn/api',
    'CERTIFICATE',
    '{"certId": "tax_cert_***", "certPath": "/path/to/cert"}',
    '国家税务总局电子税务局接口',
    '全国统一电子税务局,支持发票开具、申报、查询等功能',
    '1',
    'OFFLINE',
    TIMESTAMP '2024-12-22 09:15:00',
    TIMESTAMP '2024-12-22 09:15:00',
    'admin',
    'admin'
);

-- 测试数据4: 财务共享平台
INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY (
    ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG,
    DESCRIPTION, SYSTEM_DESC, STATUS, CONNECTION_STATUS,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES (
    4,
    '华博云财务共享平台',
    'HBYUN_FINANCE_001',
    '财务共享',
    'https://finance.hbyun.com/api',
    'BASIC_AUTH',
    '{"username": "finance_api", "password": "REDACTED"}',
    '华博云内部财务共享平台',
    '财务共享服务中心,提供费用报销、付款管理、资金计划等功能',
    '1',
    'ONLINE',
    TIMESTAMP '2024-12-23 16:45:00',
    TIMESTAMP '2024-12-24 12:00:00',
    'admin',
    'lisi'
);

-- 测试数据5: 司库管理系统
INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY (
    ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG,
    DESCRIPTION, SYSTEM_DESC, STATUS, CONNECTION_STATUS,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES (
    5,
    '全球司库管理系统',
    'GLOBAL_TREASURY_001',
    '司库系统',
    'https://treasury.hbyun.com/api',
    'OAUTH2',
    '{"clientId": "treasury_client", "clientSecret": "REDACTED", "scope": "read write"}',
    '全球司库管理系统',
    '企业级司库管理平台,提供资金监控、流动性管理、风险管控等功能',
    '1',
    'ONLINE',
    TIMESTAMP '2024-12-24 08:00:00',
    TIMESTAMP '2024-12-24 13:30:00',
    'admin',
    'admin'
);

-- 测试数据6: CRM系统
INSERT INTO TBL_BUSINESS_SYSTEM_REGISTRY (
    ID, SYSTEM_NAME, SYSTEM_CODE, SYSTEM_TYPE, API_URL, AUTH_TYPE, AUTH_CONFIG,
    DESCRIPTION, SYSTEM_DESC, STATUS, CONNECTION_STATUS,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES (
    6,
    '销售易CRM系统',
    'CRM_XS_001',
    'CRM',
    'https://crm.xiaoshouyi.com/api',
    'API_KEY',
    '{"apiKey": "crm_key_***", "companyId": "12345"}',
    '销售易CRM客户关系管理系统',
    '销售易CRM系统,用于客户信息管理和销售流程管理',
    '0',
    'OFFLINE',
    TIMESTAMP '2024-12-24 10:00:00',
    TIMESTAMP '2024-12-24 14:00:00',
    'admin',
    'wangwu'
);

-- =============================================================
-- 提交事务
-- =============================================================
COMMIT;

-- =============================================================
-- 查询验证语句
-- =============================================================
-- 查看表结构
-- SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'TBL_BUSINESS_SYSTEM_REGISTRY' ORDER BY COLUMN_ID;

-- 查看索引
-- SELECT INDEX_NAME, INDEX_TYPE FROM USER_INDEXES WHERE TABLE_NAME = 'TBL_BUSINESS_SYSTEM_REGISTRY';

-- 查看数据
-- SELECT COUNT(*) AS TOTAL_RECORDS FROM TBL_BUSINESS_SYSTEM_REGISTRY;
-- SELECT * FROM TBL_BUSINESS_SYSTEM_REGISTRY ORDER BY CREATE_TIME DESC;
