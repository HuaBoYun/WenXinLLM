-- ===================================================================
-- 数据源配置表测试数据
-- 达梦数据库(DM8)
-- 创建时间: 2026-01-30
-- 说明: 插入10条测试数据（使用表结构中存在的字段）
-- ===================================================================

-- 先清空旧数据
DELETE FROM TC_DATA_SOURCE_CONFIG;
COMMIT;

-- 插入测试数据1: 生产环境MySQL主数据库
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, USERNAME, ENVIRONMENT, CONNECTION_STATUS,
    CONNECTION_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130001', 'MYSQL_MAIN_PROD', '生产环境MySQL主数据库', 'MYSQL', 'DATABASE',
    'jdbc:mysql://192.0.2.200:3306/hbyun_treasury?useUnicode=true&characterEncoding=utf8', 'treasury_admin', 'PROD', 'NORMAL',
    '{"host":"192.0.2.200","port":3306,"database":"hbyun_treasury"}', '1', '1', '1', '财资系统生产环境MySQL主数据库', '主数据库，每日全量备份',
    'DAILY', 30, 3, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据2: 备份环境达梦数据库
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, USERNAME, ENVIRONMENT, CONNECTION_STATUS,
    CONNECTION_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130002', 'DM_BACKUP_PROD', '生产环境达梦备份数据库', 'DAMENG', 'DATABASE',
    'jdbc:dm://192.0.2.200:5236/HBYUN_BACKUP', 'backup_user', 'PROD', 'NORMAL',
    '{"host":"192.0.2.200","port":5236,"database":"HBYUN_BACKUP"}', '1', '1', '1', '财资系统达梦备份数据库', '用于数据备份和恢复',
    'WEEKLY', 60, 5, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据3: 银行接口API
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, ENVIRONMENT, CONNECTION_STATUS,
    API_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130003', 'BANK_API_ICBC', '工商银行接口API', 'API', 'API',
    'https://api.icbc.com.cn/treasury/v1', 'PROD', 'ERROR',
    '{"apiKey":"xxx","secret":"REDACTED","version":"1.0"}', '0', '0', '1', '工商银行银企直联接口', '暂时不可用，等待银行修复',
    'REALTIME', 10, 1, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据4: Redis缓存服务
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, USERNAME, ENVIRONMENT, CONNECTION_STATUS,
    CONNECTION_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130004', 'REDIS_CACHE_MAIN', 'Redis缓存主服务', 'REDIS', 'CACHE',
    'redis://192.0.2.200:6379/0', 'cache_user', 'PROD', 'NORMAL',
    '{"host":"192.0.2.200","port":6379,"database":0}', '1', '1', '1', 'Redis缓存服务，用于缓存热点数据', '主缓存节点',
    'HOURLY', 5, 2, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据5: 测试环境PostgreSQL
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, USERNAME, ENVIRONMENT, CONNECTION_STATUS,
    CONNECTION_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130005', 'POSTGRESQL_TEST', '测试环境PostgreSQL数据库', 'POSTGRESQL', 'DATABASE',
    'jdbc:postgresql://192.0.2.200:5432/hbyun_test', 'test_user', 'TEST', 'NORMAL',
    '{"host":"192.0.2.200","port":5432,"database":"hbyun_test"}', '1', '1', '1', '测试环境PostgreSQL数据库', '用于开发和测试',
    'DAILY', 30, 3, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据6: ERP系统Oracle数据库
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, USERNAME, ENVIRONMENT, CONNECTION_STATUS,
    CONNECTION_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130006', 'ORACLE_ERP_PROD', 'ERP系统Oracle数据库', 'ORACLE', 'DATABASE',
    'jdbc:oracle:thin:@192.0.2.200:1521:ERPPROD', 'erp_user', 'PROD', 'UNTESTED',
    '{"host":"192.0.2.200","port":1521,"sid":"ERPPROD"}', '1', '1', '1', 'ERP系统Oracle数据库连接', 'ERP系统数据同步源',
    'WEEKLY', 60, 3, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据7: 文件数据源接口
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, USERNAME, ENVIRONMENT, CONNECTION_STATUS,
    CONNECTION_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130007', 'FILE_INTERFACE_BANK', '银行对账文件接口', 'FILE', 'FILE',
    '/data/interface/bank/statement/', 'file_user', 'PROD', 'NORMAL',
    '{"path":"/data/interface/bank/statement/","pattern":"*.txt"}', '1', '1', '1', '银行对账文件接口，用于处理银行对账单文件', '每日凌晨2点自动处理',
    'DAILY', 120, 3, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据8: 开发环境MySQL
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, USERNAME, ENVIRONMENT, CONNECTION_STATUS,
    CONNECTION_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130008', 'MYSQL_DEV_LOCAL', '开发环境本地MySQL', 'MYSQL', 'DATABASE',
    'jdbc:mysql://localhost:3306/hbyun_dev?useUnicode=true&characterEncoding=utf8', 'root', 'DEV', 'NORMAL',
    '{"host":"localhost","port":3306,"database":"hbyun_dev"}', '1', '1', '1', '开发环境本地MySQL数据库', '开发人员本地测试环境',
    'REALTIME', 10, 1, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据9: 数据仓库大数据平台
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, USERNAME, ENVIRONMENT, CONNECTION_STATUS,
    CONNECTION_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130009', 'HIVE_DATA_WAREHOUSE', '数据仓库Hive集群', 'HIVE', 'DATABASE',
    'jdbc:hive2://192.0.2.200:10000/hbyun_warehouse', 'hive_user', 'PROD', 'NORMAL',
    '{"host":"192.0.2.200","port":10000,"database":"hbyun_warehouse"}', '1', '1', '1', '数据仓库Hive集群连接', '用于大数据分析和报表',
    'MONTHLY', 300, 5, 'system', SYSDATE, SYSDATE
);

-- 插入测试数据10: 第三方支付接口
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_URL, ENVIRONMENT, CONNECTION_STATUS,
    API_CONFIG, IS_ENABLED, IS_ACTIVE, STATUS, DESCRIPTION, REMARK,
    SYNC_FREQUENCY, TIMEOUT_SECONDS, RETRY_COUNT,
    CREATE_USER, CREATE_TIME, UPDATE_TIME
) VALUES (
    'DS20250130010', 'ALIPAY_API', '支付宝支付接口', 'API', 'API',
    'https://openapi.alipay.com/gateway.do', 'PROD', 'NORMAL',
    '{"appId":"xxx","privateKey":"xxx","publicKey":"xxx","format":"JSON"}', '1', '1', '1', '支付宝支付接口，用于线上支付对账', '每日自动对账',
    'DAILY', 15, 3, 'system', SYSDATE, SYSDATE
);

COMMIT;

-- 验证插入结果
SELECT
    ID AS "主键ID",
    SOURCE_CODE AS "数据源编码",
    SOURCE_NAME AS "数据源名称",
    SOURCE_TYPE AS "数据源类型",
    DATA_TYPE AS "数据类型",
    ENVIRONMENT AS "环境",
    CONNECTION_STATUS AS "连接状态",
    IS_ENABLED AS "是否启用",
    IS_ACTIVE AS "是否激活",
    STATUS AS "状态",
    CREATE_TIME AS "创建时间"
FROM TC_DATA_SOURCE_CONFIG
ORDER BY CREATE_TIME DESC;

-- 统计插入的数据量
SELECT '数据源配置表测试数据插入完成，共插入 ' || COUNT(*) || ' 条记录' AS MESSAGE
FROM TC_DATA_SOURCE_CONFIG;
