-- =============================================
-- 数据源配置测试数据
-- 表名: TC_DATA_SOURCE_CONFIG
-- 生成时间: 2025-01-21
-- 说明: 包含10条不同类型的数据源配置测试数据
-- =============================================

-- 清理现有测试数据(可选)
-- DELETE FROM TC_DATA_SOURCE_CONFIG WHERE SOURCE_CODE LIKE 'TEST_%';

-- 1. MySQL数据库 - 生产环境
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_MYSQL_001', '生产环境MySQL主库', 'MYSQL', 'DATABASE',
    '{"url":"jdbc:mysql://192.0.2.200:3306/production","port":3306,"host":"192.0.2.200","database":"production","username":"prod_user","password":"REDACTED","driver":"com.mysql.cj.jdbc.Driver","params":"useSSL=false&serverTimezone=Asia/Shanghai"}',
    NULL, 'EVERY_5_MIN', SYSDATE, 'SUCCESS', 0, 5, 30, 3,
    '1', '1', '生产环境核心数据库', SYSDATE, SYSDATE, 'admin', 'admin', 0
);

-- 2. Oracle数据库 - 生产环境
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_ORACLE_001', '生产环境Oracle财务库', 'ORACLE', 'DATABASE',
    '{"url":"jdbc:oracle:thin:@192.0.2.200:1521:ORCL","port":1521,"host":"192.0.2.200","sid":"ORCL","username":"finance_user","password":"REDACTED","driver":"oracle.jdbc.OracleDriver","params":"connectTimeout=30000"}',
    NULL, 'EVERY_10_MIN', SYSDATE - 1/24, 'SUCCESS', 0, 5, 60, 3,
    '1', '1', '财务系统Oracle数据库', SYSDATE, SYSDATE, 'admin', 'admin', 0
);

-- 3. 达梦数据库 - 测试环境
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_DM_001', '测试环境达梦数据库', 'DAMENG', 'DATABASE',
    '{"url":"jdbc:dm://192.0.2.200:5236","port":5236,"host":"192.0.2.200","database":"TEST_DB","username":"test_user","password":"REDACTED","driver":"dm.jdbc.driver.DmDriver"}',
    NULL, 'MANUAL', SYSDATE - 2/24, 'SUCCESS', 0, 3, 45, 2,
    '1', '1', '达梦数据库测试实例', SYSDATE, SYSDATE, 'tester', 'admin', 0
);

-- 4. PostgreSQL数据库 - 开发环境
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_POSTGRESQL_001', '开发环境PostgreSQL', 'POSTGRESQL', 'DATABASE',
    '{"url":"jdbc:postgresql://192.0.2.200:5432/dev_db","port":5432,"host":"192.0.2.200","database":"dev_db","username":"dev_user","password":"REDACTED","driver":"org.postgresql.Driver","schema":"public"}',
    NULL, 'HOURLY', SYSDATE - 1/48, 'SUCCESS', 0, 5, 30, 3,
    '1', '1', '开发环境PostgreSQL数据库', SYSDATE, SYSDATE, 'developer', 'admin', 0
);

-- 5. Redis缓存 - 生产环境
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_REDIS_001', '生产环境Redis集群', 'REDIS', 'CACHE',
    '{"host":"192.0.2.200","port":6379,"password":"REDACTED","database":0,"cluster":"true","nodes":"192.0.2.200:6379,192.0.2.200:6379,192.0.2.200:6379","maxActive":50,"maxIdle":20,"minIdle":5}',
    NULL, 'EVERY_1_MIN', SYSDATE - 1/1440, 'SUCCESS', 0, 5, 10, 3,
    '1', '1', 'Redis缓存集群', SYSDATE, SYSDATE, 'admin', 'admin', 0
);

-- 6. REST API接口 - 外部数据服务
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_API_001', '税务查询API接口', 'API', 'API',
    NULL,
    '{"baseUrl":"https://api.tax.example.com","version":"v2","authType":"Bearer","token":"REDACTED","headers":{"Content-Type":"application/json"},"timeout":30,"retryCount":3,"endpoints":{"query":"/tax/query","report":"/tax/report"}}',
    'DAILY', SYSDATE - 1, 'SUCCESS', 0, 5, 60, 3,
    '1', '1', '外部税务数据查询接口', SYSDATE, SYSDATE, 'admin', 'admin', 0
);

-- 7. MySQL数据库 - 测试环境(有同步错误)
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_MYSQL_002', '测试环境MySQL从库', 'MYSQL', 'DATABASE',
    '{"url":"jdbc:mysql://192.0.2.200:3306/test_db","port":3306,"host":"192.0.2.200","database":"test_db","username":"test_user","password":"REDACTED","driver":"com.mysql.cj.jdbc.Driver"}',
    NULL, 'EVERY_5_MIN', SYSDATE - 1/12, 'ERROR', 3, 5, 30, 3,
    '1', '1', '测试环境MySQL从库(网络不稳定)', SYSDATE, SYSDATE, 'tester', 'admin', 0
);

-- 8. 文件数据源 - Excel文件
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_FILE_001', '财务报表Excel文件源', 'FILE', 'FILE',
    '{"filePath":"/data/files/financial_reports","pattern":"*.xlsx","encoding":"UTF-8","separator":",","hasHeader":true,"sheetName":"Sheet1","skipRows":0}',
    NULL, 'WEEKLY', SYSDATE - 7, 'SUCCESS', 0, 3, 60, 1,
    '1', '1', '财务报表Excel文件导入', SYSDATE, SYSDATE, 'finance', 'admin', 0
);

-- 9. Oracle数据库 - 开发环境(未激活)
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_ORACLE_002', '开发环境Oracle备份库', 'ORACLE', 'DATABASE',
    '{"url":"jdbc:oracle:thin:@192.0.2.200:1521:DEVDB","port":1521,"host":"192.0.2.200","sid":"DEVDB","username":"dev_backup","password":"REDACTED","driver":"oracle.jdbc.OracleDriver"}',
    NULL, 'MANUAL', NULL, 'UNTESTED', 0, 5, 45, 2,
    '0', '0', '开发环境备份库(待激活)', SYSDATE, SYSDATE, 'developer', 'admin', 0
);

-- 10. REST API接口 - 第三方支付接口
INSERT INTO TC_DATA_SOURCE_CONFIG (
    ID, SOURCE_CODE, SOURCE_NAME, SOURCE_TYPE, DATA_TYPE,
    CONNECTION_CONFIG, API_CONFIG, SYNC_FREQUENCY, LAST_SYNC_TIME,
    SYNC_STATUS, ERROR_COUNT, MAX_ERROR_COUNT, TIMEOUT_SECONDS,
    RETRY_COUNT, IS_ACTIVE, STATUS, REMARK, CREATE_TIME,
    UPDATE_TIME, CREATE_USER, UPDATE_USER, VERSION_NO
) VALUES (
    SYS_GUID(), 'TEST_API_002', '第三方支付回调API', 'API', 'API',
    NULL,
    '{"baseUrl":"https://pay.example.com/api","version":"v1","authType":"API_KEY","apiKey":"******","apiSecret":"REDACTED","headers":{"Content-Type":"application/json"},"timeout":30,"retryCount":3,"endpoints":{"callback":"/payment/callback","query":"/payment/query","refund":"/payment/refund"}}',
    'REALTIME', SYSDATE - 1/288, 'SUCCESS', 1, 10, 20, 5,
    '1', '1', '第三方支付平台回调接口', SYSDATE, SYSDATE, 'admin', 'admin', 0
);

-- =============================================
-- 数据说明
-- =============================================
-- 1. 数据源类型包括: MYSQL, ORACLE, DAMENG, POSTGRESQL, REDIS, API, FILE
-- 2. 数据类型包括: DATABASE, CACHE, API, FILE
-- 3. 同步频率包括: EVERY_1_MIN, EVERY_5_MIN, EVERY_10_MIN, HOURLY, DAILY, WEEKLY, MANUAL, REALTIME
-- 4. 同步状态包括: SUCCESS, ERROR, UNTESTED
-- 5. IS_ACTIVE: 1-激活, 0-未激活
-- 6. STATUS: 1-启用, 0-禁用

-- =============================================
-- 查询验证SQL
-- =============================================
-- 查询所有数据源配置
-- SELECT * FROM TC_DATA_SOURCE_CONFIG ORDER BY CREATE_TIME DESC;

-- 按数据源类型统计
-- SELECT SOURCE_TYPE, COUNT(*) AS COUNT FROM TC_DATA_SOURCE_CONFIG GROUP BY SOURCE_TYPE;

-- 按同步状态统计
-- SELECT SYNC_STATUS, COUNT(*) AS COUNT FROM TC_DATA_SOURCE_CONFIG GROUP BY SYNC_STATUS;

-- 查询激活的数据源
-- SELECT * FROM TC_DATA_SOURCE_CONFIG WHERE IS_ACTIVE = '1' AND STATUS = '1';

-- 查询有错误的数据源
-- SELECT * FROM TC_DATA_SOURCE_CONFIG WHERE ERROR_COUNT > 0;
