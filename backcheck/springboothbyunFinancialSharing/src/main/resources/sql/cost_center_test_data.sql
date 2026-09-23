-- 成本中心测试数据脚本
-- 适配达梦数据库语法

-- 删除现有测试数据（如果存在）
DELETE FROM T_COST_CENTER WHERE CENTER_ID IN (1, 2);

-- 插入成本中心测试数据
INSERT INTO T_COST_CENTER (
    CENTER_ID, CENTER_CODE, CENTER_NAME, CENTER_TYPE, PARENT_CENTER_ID,
    CENTER_LEVEL, IS_LEAF, MANAGER_ID, COST_ALLOCATION_METHOD, IS_ENABLED,
    BOOK_ID, TENANT_ID, VERSION, IS_DELETED, CREATE_TIME, UPDATE_TIME,
    CREATOR, UPDATER
) VALUES
(
    1,
    'CC001',
    '生产部成本中心',
    1,
    NULL,
    1,
    1,
    '1001',
    1,
    1,
    '1',
    '1000',
    1,
    0,
    SYSDATE,
    SYSDATE,
    'admin',
    'admin'
),
(
    2,
    'CC002',
    '管理部成本中心',
    1,
    NULL,
    1,
    1,
    '1002',
    1,
    1,
    '1',
    '1000',
    1,
    0,
    SYSDATE,
    SYSDATE,
    'admin',
    'admin'
);

-- 提交事务
COMMIT;

-- 验证插入结果
SELECT CENTER_ID, CENTER_CODE, CENTER_NAME, IS_ENABLED, IS_DELETED
FROM T_COST_CENTER
WHERE CENTER_ID IN (1, 2);