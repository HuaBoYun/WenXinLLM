-- =====================================================
-- 华博云数据修复验证脚本
-- 用于验证外键约束修复效果
-- =====================================================

-- 1. 基础数据完整性检查
-- =====================================================

-- 1.1 检查结算记录和责任中心的关联完整性
SELECT
    '外键约束验证' as check_type,
    COUNT(*) as total_settlements,
    SUM(CASE WHEN r_from.CENTER_ID IS NULL THEN 1 ELSE 0 END) as missing_from_centers,
    SUM(CASE WHEN r_to.CENTER_ID IS NULL THEN 1 ELSE 0 END) as missing_to_centers,
    SUM(CASE WHEN r_from.CENTER_ID IS NULL OR r_to.CENTER_ID IS NULL THEN 1 ELSE 0 END) as total_issues,
    ROUND(
        (SUM(CASE WHEN r_from.CENTER_ID IS NOT NULL AND r_to.CENTER_ID IS NOT NULL THEN 1 ELSE 0 END) * 100.0) /
        NULLIF(COUNT(*), 0), 2
    ) as integrity_percentage
FROM T_INTERNAL_SETTLEMENT s
LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID
LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID;

-- 1.2 检查是否还存在问题记录
SELECT
    CASE
        WHEN COUNT(*) = 0 THEN '✅ 修复成功，无问题记录'
        ELSE '❌ 仍有 ' || COUNT(*) || ' 条问题记录'
    END as status_message
FROM T_INTERNAL_SETTLEMENT s
LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID
LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID
WHERE r_from.CENTER_ID IS NULL OR r_to.CENTER_ID IS NULL;

-- 2. 新创建的责任中心验证
-- =====================================================

-- 2.1 统计新创建的责任中心数量
SELECT
    '新创建责任中心统计' as check_type,
    COUNT(*) as new_centers_count,
    MIN(r.CREATE_TIME) as earliest_creation,
    MAX(r.CREATE_TIME) as latest_creation,
    COUNT(DISTINCT r.BOOK_ID) as affected_books,
    COUNT(DISTINCT r.TENANT_ID) as affected_tenants
FROM REDACTED.T_RESPONSIBILITY_CENTER r
WHERE r.CENTER_ID IN (
    SELECT FROM_CENTER_ID FROM T_INTERNAL_SETTLEMENT
    UNION
    SELECT TO_CENTER_ID FROM T_INTERNAL_SETTLEMENT
)
AND r.CREATE_TIME >= SYSDATE - 1/24; -- 最近24小时创建的

-- 2.2 查看新创建的责任中心详情
SELECT
    r.CENTER_ID,
    r.CENTER_CODE,
    r.CENTER_NAME,
    r.CENTER_TYPE,
    r.BOOK_ID,
    r.TENANT_ID,
    r.IS_ENABLED,
    r.CREATE_TIME,
    (SELECT COUNT(*) FROM T_INTERNAL_SETTLEMENT s WHERE s.FROM_CENTER_ID = r.CENTER_ID) as used_as_from_count,
    (SELECT COUNT(*) FROM T_INTERNAL_SETTLEMENT s WHERE s.TO_CENTER_ID = r.CENTER_ID) as used_as_to_count
FROM REDACTED.T_RESPONSIBILITY_CENTER r
WHERE r.CENTER_ID IN (
    SELECT FROM_CENTER_ID FROM T_INTERNAL_SETTLEMENT
    UNION
    SELECT TO_CENTER_ID FROM T_INTERNAL_SETTLEMENT
)
AND r.CREATE_TIME >= SYSDATE - 1/24
ORDER BY r.CENTER_ID;

-- 3. 业务数据一致性检查
-- =====================================================

-- 3.1 检查结算记录的业务完整性
SELECT
    s.SETTLEMENT_STATUS,
    COUNT(*) as record_count,
    SUM(s.SETTLEMENT_AMOUNT) as total_amount,
    AVG(s.SETTLEMENT_AMOUNT) as avg_amount,
    MIN(s.CREATE_TIME) as earliest_record,
    MAX(s.CREATE_TIME) as latest_record
FROM T_INTERNAL_SETTLEMENT s
JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID
JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID
GROUP BY s.SETTLEMENT_STATUS
ORDER BY s.SETTLEMENT_STATUS;

-- 3.2 检查账簿级别的数据完整性
SELECT
    s.BOOK_ID,
    COUNT(*) as settlement_count,
    SUM(s.SETTLEMENT_AMOUNT) as total_amount,
    COUNT(DISTINCT s.FROM_CENTER_ID) as unique_from_centers,
    COUNT(DISTINCT s.TO_CENTER_ID) as unique_to_centers
FROM T_INTERNAL_SETTLEMENT s
JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID
JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID
GROUP BY s.BOOK_ID
ORDER BY s.BOOK_ID;

-- 4. 成本中心与责任中心一致性验证
-- =====================================================

-- 4.1 检查新创建的责任中心对应的成本中心是否存在
SELECT
    '成本中心-责任中心映射验证' as check_type,
    COUNT(*) as total_mapped_centers,
    SUM(CASE WHEN c.CENTER_ID IS NOT NULL THEN 1 ELSE 0 END) as existing_cost_centers,
    SUM(CASE WHEN c.CENTER_ID IS NULL THEN 1 ELSE 0 END) as missing_cost_centers
FROM REDACTED.T_RESPONSIBILITY_CENTER r
LEFT JOIN T_COST_CENTER c ON r.CENTER_ID = c.CENTER_ID
WHERE r.CENTER_ID IN (
    SELECT FROM_CENTER_ID FROM T_INTERNAL_SETTLEMENT
    UNION
    SELECT TO_CENTER_ID FROM T_INTERNAL_SETTLEMENT
)
AND r.CREATE_TIME >= SYSDATE - 1/24;

-- 4.2 检查数据字段一致性
SELECT
    '字段一致性验证' as check_type,
    COUNT(*) as total_centers,
    SUM(CASE WHEN r.CENTER_CODE = c.CENTER_CODE THEN 1 ELSE 0 END) as code_match,
    SUM(CASE WHEN r.CENTER_NAME = c.CENTER_NAME THEN 1 ELSE 0 END) as name_match,
    SUM(CASE WHEN r.BOOK_ID = c.BOOK_ID THEN 1 ELSE 0 END) as book_match,
    SUM(CASE WHEN r.TENANT_ID = c.TENANT_ID THEN 1 ELSE 0 END) as tenant_match
FROM REDACTED.T_RESPONSIBILITY_CENTER r
JOIN T_COST_CENTER c ON r.CENTER_ID = c.CENTER_ID
WHERE r.CENTER_ID IN (
    SELECT FROM_CENTER_ID FROM T_INTERNAL_SETTLEMENT
    UNION
    SELECT TO_CENTER_ID FROM T_INTERNAL_SETTLEMENT
)
AND r.CREATE_TIME >= SYSDATE - 1/24;

-- 5. 性能和索引检查
-- =====================================================

-- 5.1 检查相关表的记录数量和大小
SELECT
    '表统计信息' as table_stats,
    (SELECT COUNT(*) FROM T_INTERNAL_SETTLEMENT) as settlement_count,
    (SELECT COUNT(*) FROM REDACTED.T_RESPONSIBILITY_CENTER) as responsibility_center_count,
    (SELECT COUNT(*) FROM T_COST_CENTER) as cost_center_count,
    (SELECT COUNT(*) FROM REDACTED.T_RESPONSIBILITY_CENTER WHERE CREATE_TIME >= SYSDATE - 1/24) as new_centers_today
FROM DUAL;

-- 5.2 检查外键约束状态（如果Oracle数据库支持）
SELECT
    c.CONSTRAINT_NAME,
    c.CONSTRAINT_TYPE,
    c.TABLE_NAME,
    c.STATUS,
    c.VALIDATED,
    c.RELY
FROM ALL_CONSTRAINTS c
WHERE c.TABLE_NAME = 'T_INTERNAL_SETTLEMENT'
  AND c.CONSTRAINT_TYPE = 'R'
  AND c.CONSTRAINT_NAME LIKE 'FK_INTERNAL_SETTLEMENT_%';

-- 6. 综合评估报告
-- =====================================================

-- 6.1 生成修复效果评估
WITH integrity_check AS (
    SELECT
        COUNT(*) as total_settlements,
        SUM(CASE WHEN r_from.CENTER_ID IS NULL OR r_to.CENTER_ID IS NULL THEN 1 ELSE 0 END) as problematic_count
    FROM T_INTERNAL_SETTLEMENT s
    LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID
    LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID
),
new_centers AS (
    SELECT COUNT(*) as new_centers_count
    FROM REDACTED.T_RESPONSIBILITY_CENTER
    WHERE CREATE_TIME >= SYSDATE - 1/24
)
SELECT
    '修复效果综合评估' as assessment_type,
    integrity_check.total_settlements,
    integrity_check.problematic_count,
    CASE
        WHEN integrity_check.problematic_count = 0 THEN '✅ 完全修复'
        WHEN integrity_check.problematic_count > 0 THEN '❌ 仍有问题'
        ELSE '⚠️ 需要检查'
    END as repair_status,
    new_centers.new_centers_count,
    CASE
        WHEN integrity_check.total_settlements > 0 THEN
            ROUND((integrity_check.total_settlements - integrity_check.problematic_count) * 100.0 / integrity_check.total_settlements, 2)
        ELSE 100
    END as success_percentage,
    SYSDATE as check_time
FROM integrity_check, new_centers;

-- 6.2 问题清单（如果存在）
SELECT
    '剩余问题清单' as issue_list,
    s.SETTLEMENT_ID,
    s.SETTLEMENT_NO,
    s.FROM_CENTER_ID,
    s.TO_CENTER_ID,
    s.BOOK_ID,
    CASE
        WHEN r_from.CENTER_ID IS NULL AND r_to.CENTER_ID IS NULL THEN 'FROM 和 TO 中心都不存在'
        WHEN r_from.CENTER_ID IS NULL THEN 'FROM 中心不存在'
        WHEN r_to.CENTER_ID IS NULL THEN 'TO 中心不存在'
        ELSE '其他问题'
    END as issue_description,
    s.CREATE_TIME
FROM T_INTERNAL_SETTLEMENT s
LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID
LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID
WHERE r_from.CENTER_ID IS NULL OR r_to.CENTER_ID IS NULL
ORDER BY s.CREATE_TIME DESC;

-- =====================================================
-- 验证脚本执行完成
-- =====================================================

-- 验证结论判断标准：
-- 1. total_issues = 0: 修复成功
-- 2. success_percentage = 100: 修复成功
-- 3. repair_status = '✅ 完全修复': 修复成功
-- 4. 剩余问题清单为空: 修复成功

-- 如果所有检查都通过，可以：
-- 1. 重启应用服务
-- 2. 执行业务功能测试
-- 3. 清理备份表（建议保留7天）