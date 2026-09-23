-- ================================================================================
-- 印鉴安全管理相关表 - 达梦数据库 DM8 兼容
-- 创建日期: 2025-12-25
-- 说明: 包含印鉴操作日志表和印鉴安全配置表
-- ================================================================================

-- ================================================================================
-- 1. 印鉴操作日志表 (TBL_SEAL_OPERATION_LOG)
-- 用途: 记录所有印鉴的锁定、解锁、配置修改等操作
-- ================================================================================
CREATE TABLE TBL_SEAL_OPERATION_LOG (
    LOG_ID BIGINT IDENTITY(1,1) PRIMARY KEY,
    SEAL_ID VARCHAR(32) NOT NULL,                   -- 印鉴ID
    SEAL_CODE VARCHAR(50),                        -- 印鉴编码
    SEAL_NAME VARCHAR(100),                       -- 印鉴名称
    OPERATION_TYPE VARCHAR(20) NOT NULL,          -- 操作类型(LOCK/UNLOCK/CONFIG/VIEW/CREATE/UPDATE/DELETE)
    OPERATION_DESC VARCHAR(500),                  -- 操作描述
    OPERATION_REASON VARCHAR(500),                -- 操作原因
    OPERATOR_ID VARCHAR(32) NOT NULL,             -- 操作人ID
    OPERATOR_NAME VARCHAR(50) NOT NULL,            -- 操作人姓名
    OPERATION_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 操作时间
    IP_ADDRESS VARCHAR(50),                       -- 操作IP地址
    OPERATION_RESULT VARCHAR(20),                 -- 操作结果(SUCCESS/FAIL)
    ERROR_MESSAGE VARCHAR(500),                    -- 错误信息
    ORG_ID VARCHAR(32),                            -- 组织ID
    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IDX_OPERATION_LOG_SEAL ON TBL_SEAL_OPERATION_LOG(SEAL_ID);
CREATE INDEX IDX_OPERATION_LOG_TYPE ON TBL_SEAL_OPERATION_LOG(OPERATION_TYPE);
CREATE INDEX IDX_OPERATION_LOG_TIME ON TBL_SEAL_OPERATION_LOG(OPERATION_TIME);
CREATE INDEX IDX_OPERATION_LOG_OPERATOR ON TBL_SEAL_OPERATION_LOG(OPERATOR_ID);

-- 添加注释
COMMENT ON TABLE TBL_SEAL_OPERATION_LOG IS '印鉴操作日志表';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.LOG_ID IS '日志ID';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.SEAL_ID IS '印鉴ID';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.SEAL_CODE IS '印鉴编码';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.SEAL_NAME IS '印鉴名称';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.OPERATION_TYPE IS '操作类型(LOCK-锁定,UNLOCK-解锁,CONFIG-配置修改,VIEW-查看,CREATE-创建,UPDATE-更新,DELETE-删除)';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.OPERATION_DESC IS '操作描述';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.OPERATION_REASON IS '操作原因';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.OPERATOR_ID IS '操作人ID';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.OPERATOR_NAME IS '操作人姓名';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.OPERATION_TIME IS '操作时间';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.IP_ADDRESS IS '操作IP地址';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.OPERATION_RESULT IS '操作结果(SUCCESS-成功,FAIL-失败)';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.ERROR_MESSAGE IS '错误信息';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.ORG_ID IS '组织ID';
COMMENT ON COLUMN TBL_SEAL_OPERATION_LOG.CREATE_TIME IS '创建时间';

-- ================================================================================
-- 2. 印鉴安全配置表 (TBL_SEAL_SECURITY_CONFIG)
-- 用途: 存储印鉴的安全配置信息,包括安全级别、授权用户、使用时间限制等
-- ================================================================================
CREATE TABLE TBL_SEAL_SECURITY_CONFIG (
    CONFIG_ID VARCHAR(32) NOT NULL PRIMARY KEY,
    SEAL_ID VARCHAR(32) NOT NULL UNIQUE,            -- 印鉴ID
    SECURITY_LEVEL INTEGER DEFAULT 1,              -- 安全级别(1-低,2-中,3-高)
    ALLOWED_USERS TEXT,                            -- 授权用户ID列表(JSON数组字符串)
    ALLOWED_DEPTS TEXT,                            -- 授权部门ID列表(JSON数组字符串)
    USAGE_START_TIME VARCHAR(10),                  -- 使用开始时间(HH:mm)
    USAGE_END_TIME VARCHAR(10),                    -- 使用结束时间(HH:mm)
    MAX_DAILY_USAGE INTEGER DEFAULT 0,             -- 每日最大使用次数(0-无限制)
    REQUIRE_APPROVAL INTEGER DEFAULT 0,            -- 是否需要审批(1-是,0-否)
    APPROVAL_USERS TEXT,                           -- 审批人ID列表(JSON数组字符串)
    IS_ENABLED INTEGER DEFAULT 1,                  -- 配置是否启用(1-启用,0-禁用)
    REMARKS VARCHAR(500),                          -- 备注
    ORG_ID VARCHAR(32),                             -- 组织ID
    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CREATE_USER VARCHAR(50),
    UPDATE_USER VARCHAR(50)
);

-- 创建索引
CREATE INDEX IDX_SECURITY_CONFIG_SEAL ON TBL_SEAL_SECURITY_CONFIG(SEAL_ID);
CREATE INDEX IDX_SECURITY_CONFIG_LEVEL ON TBL_SEAL_SECURITY_CONFIG(SECURITY_LEVEL);
CREATE INDEX IDX_SECURITY_CONFIG_ENABLED ON TBL_SEAL_SECURITY_CONFIG(IS_ENABLED);

-- 添加注释
COMMENT ON TABLE TBL_SEAL_SECURITY_CONFIG IS '印鉴安全配置表';
COMMENT ON COLUMN TBL_SEAL_SECURITY_CONFIG.CONFIG_ID IS '配置ID';
COMMENT ON COLUMN TBL_SEAL_SECURITY_CONFIG.SEAL_ID IS '印鉴ID';
COMMENT ON COLUMN TBL_SEAL_SECURITY_CONFIG.SECURITY_LEVEL IS '安全级别(1-低,2-中,3-高)';
COMMENT ON COLUMN TBL_SEAL_SECURITY_CONFIG.ALLOWED_USERS IS '授权用户ID列表(JSON数组)';
COMMENT ON COLUMN TBL_SEAL_SECURITY_CONFIG.ALLOWED_DEPTS IS '授权部门ID列表(JSON数组)';
COMMENT ON COLUMN TBL_SECURITY_CONFIG.USAGE_START_TIME IS '使用开始时间';
COMMENT ON COLUMN TBL_SEAL_SECURITY_CONFIG.USAGE_END_TIME IS '使用结束时间';
COMMENT ON TBL_SEAL_SECURITY_CONFIG.MAX_DAILY_USAGE IS '每日最大使用次数(0-无限制)';
COMMENT ON TBL_SEAL_SECURITY_CONFIG.REQUIRE_APPROVAL IS '是否需要审批';
COMMENT ON TBL_SEAL_SECURITY_CONFIG.APPROVAL_USERS IS '审批人ID列表(JSON数组)';
COMMENT ON TBL_SEAL_SECURITY_CONFIG.IS_ENABLED IS '配置是否启用';
COMMENT ON TBL_SEAL_SECURITY_CONFIG.REMARKS IS '备注';
COMMENT ON TBL_SEAL_SECURITY_CONFIG.ORG_ID IS '组织ID';

-- ================================================================================
-- 3. 插入测试数据
-- ================================================================================

-- 3.1 插入操作日志测试数据
INSERT INTO TBL_SEAL_OPERATION_LOG (
    SEAL_ID, SEAL_CODE, SEAL_NAME, OPERATION_TYPE, OPERATION_DESC,
    OPERATION_REASON, OPERATOR_ID, OPERATOR_NAME, OPERATION_RESULT, IP_ADDRESS
) VALUES
('1001', 'YZGZ2024001', '华博云科技有限公司公章', 'LOCK', '批量锁定印鉴', '安全检查', '1001', '张伟', 'SUCCESS', '192.0.2.200'),
('1001', 'YZGZ2024001', '华博云科技有限公司公章', 'UNLOCK', '批量解锁印鉴', '检查完成', '1001', '张伟', 'SUCCESS', '192.0.2.200'),
('1002', 'YZGZ2024002', '华博云北京分公司公章', 'CONFIG', '修改安全配置', '提升安全级别', '1002', '李娜', 'SUCCESS', '192.0.2.200'),
('1003', 'YZGZ2024003', '华博云上海分公司公章', 'VIEW', '查看印鉴详情', '业务需要', '1003', '王强', 'SUCCESS', '192.0.2.200'),
('1004', 'YZGZ2024004', '华博云广州分公司公章', 'UPDATE', '更新印鉴信息', '信息变更', '1004', '刘洋', 'SUCCESS', '192.0.2.200'),
('1005', 'YZGZ2024005', '华博云深圳分公司公章', 'LOCK', '锁定印鉴', '异常使用', '1001', '张伟', 'SUCCESS', '192.0.2.200'),
('1006', 'YZCW2024001', '华博云财务专用章', 'CONFIG', '设置使用时间限制', '工作时间控制', '1002', '李娜', 'SUCCESS', '192.0.2.200'),
('1007', 'YZCW2024002', '华博云发票专用章', 'LOCK', '批量锁定', '定期审计', '1001', '张伟', 'SUCCESS', '192.0.2.200'),
('1008', 'YZCW2024003', '华博云税务专用章', 'UNLOCK', '解锁使用', '审计完成', '1001', '张伟', 'SUCCESS', '192.0.2.200'),
('1009', 'YZCW2024004', '华博云银行预留印鉴', 'CONFIG', '添加授权用户', '权限调整', '1002', '李娜', 'SUCCESS', '192.0.2.200');

COMMIT;

-- 3.2 插入安全配置测试数据
INSERT INTO TBL_SEAL_SECURITY_CONFIG (
    CONFIG_ID, SEAL_ID, SECURITY_LEVEL, ALLOWED_USERS, USAGE_START_TIME, USAGE_END_TIME, MAX_DAILY_USAGE, REQUIRE_APPROVAL, REMARKS, CREATE_USER
) VALUES
('CONFIG_001', '1001', 3, '["1001","1002","1003"]', '08:00', '18:00', 10, 1, '公章高安全配置,需要审批', 'system'),
('CONFIG_002', '1002', 2, '["1002","1004"]', '09:00', '17:00', 5, 0, '公章中安全配置,财务人员使用', 'system'),
('CONFIG_003', '1003', 1, '["1001","1002","1003","1004","1005"]', '00:00', '23:59', 0, 0, '公章低安全配置,全员可用', 'system'),
('CONFIG_004', '1006', 3, '["1002","1006"]', '08:30', '17:30', 20, 1, '财务章高安全配置', 'system'),
('CONFIG_005', '1007', 2, '["1002","1006","1007"]', '09:00', '18:00', 15, 0, '发票章中安全配置', 'system');

COMMIT;

-- ================================================================================
-- 4. 验证插入结果
-- ================================================================================
SELECT '印鉴操作日志表的行数: ' || COUNT(*) as result FROM TBL_SEAL_OPERATION_LOG;
SELECT '印鉴安全配置表的行数: ' || COUNT(*) as result FROM TBL_SEAL_SECURITY_CONFIG;

-- 按操作类型统计日志
SELECT
    OPERATION_TYPE as 操作类型,
    COUNT(*) as 数量,
    SUM(CASE WHEN OPERATION_RESULT = 'SUCCESS' THEN 1 ELSE 0 END) as 成功数,
    SUM(CASE WHEN OPERATION_RESULT = 'FAIL' THEN 1 ELSE 0 END) as 失败数
FROM TBL_SEAL_OPERATION_LOG
GROUP BY OPERATION_TYPE
ORDER BY OPERATION_TYPE;

-- 查看安全配置
SELECT
    SEAL_ID as 印鉴ID,
    SECURITY_LEVEL as 安全级别,
    ALLOWED_USERS as 授权用户,
    USAGE_START_TIME as 开始时间,
    USAGE_END_TIME as 结束时间,
    MAX_DAILY_USAGE as 每日最大使用次数,
    REQUIRE_APPROVAL as 是否需要审批
FROM TBL_SEAL_SECURITY_CONFIG
ORDER BY SECURITY_LEVEL DESC;

-- 说明:
-- 1. TBL_SEAL_OPERATION_LOG表记录所有印鉴操作日志,包括锁定、解锁、配置修改等
-- 2. TBL_SEAL_SECURITY_CONFIG表存储印鉴的安全配置,包括安全级别、授权用户、时间限制等
-- 3. 测试数据包含10条操作日志和5条安全配置
-- 4. 支持按印鉴ID、操作类型、操作时间等字段查询
-- 5. 操作类型: LOCK(锁定)、UNLOCK(解锁)、CONFIG(配置修改)、VIEW(查看)、CREATE(创建)、UPDATE(更新)、DELETE(删除)
-- 6. 安全级别: 1(低)、2(中)、3(高)
-- 7. 所有SQL兼容达梦数据库DM8
