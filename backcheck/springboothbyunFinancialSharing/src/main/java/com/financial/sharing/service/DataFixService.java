package com.financial.sharing.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 数据修复服务
 * 用于修复外键约束违反等问题
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DataFixService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 修复缺失的责任中心数据
     */
    public void fixMissingResponsibilityCenters() {
        log.info("开始修复缺失的责任中心数据...");

        try {
            // 1. 查询问题数据
            List<Map<String, Object>> problemData = queryProblemData();
            log.info("发现 {} 条有问题的结算记录", problemData.size());

            if (problemData.isEmpty()) {
                log.info("未发现数据问题，无需修复");
                return;
            }

            // 2. 查询需要创建的责任中心
            List<Map<String, Object>> missingCenters = queryMissingCenters();
            log.info("需要创建 {} 个责任中心记录", missingCenters.size());

            // 3. 创建缺失的责任中心记录
            int createdCount = createMissingResponsibilityCenters(missingCenters);
            log.info("成功创建 {} 个责任中心记录", createdCount);

            // 4. 验证修复结果
            List<Map<String, Object>> remainingIssues = queryRemainingIssues();
            log.info("修复后剩余问题记录数: {}", remainingIssues.size());

            if (remainingIssues.isEmpty()) {
                log.info("✅ 数据修复成功！所有外键约束问题已解决");
            } else {
                log.warn("❌ 仍有 {} 条问题记录未解决", remainingIssues.size());
                for (Map<String, Object> issue : remainingIssues) {
                    log.warn("问题记录: {}", issue);
                }
            }

        } catch (Exception e) {
            log.error("数据修复过程中发生错误", e);
            throw new RuntimeException("数据修复失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查询问题数据
     */
    private List<Map<String, Object>> queryProblemData() {
        String sql = "SELECT " +
            "s.SETTLEMENT_ID, " +
            "s.FROM_CENTER_ID, " +
            "s.TO_CENTER_ID, " +
            "s.BOOK_ID, " +
            "s.TENANT_ID, " +
            "s.SETTLEMENT_NO, " +
            "s.SETTLEMENT_AMOUNT, " +
            "s.CREATE_TIME, " +
            "CASE " +
            "    WHEN r_from.CENTER_ID IS NULL THEN 'FROM中心不存在' " +
            "    ELSE 'FROM中心存在' " +
            "END as from_status, " +
            "CASE " +
            "    WHEN r_to.CENTER_ID IS NULL THEN 'TO中心不存在' " +
            "    ELSE 'TO中心存在' " +
            "END as to_status " +
            "FROM T_INTERNAL_SETTLEMENT s " +
            "LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID " +
            "LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID " +
            "WHERE r_from.CENTER_ID IS NULL OR r_to.CENTER_ID IS NULL " +
            "ORDER BY s.CREATE_TIME DESC";

        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 查询需要创建的责任中心
     */
    private List<Map<String, Object>> queryMissingCenters() {
        String sql = "SELECT " +
            "c.CENTER_ID, " +
            "c.CENTER_CODE, " +
            "c.CENTER_NAME, " +
            "c.CENTER_TYPE, " +
            "c.PARENT_CENTER_ID, " +
            "c.CENTER_LEVEL, " +
            "c.IS_LEAF, " +
            "c.MANAGER_ID, " +
            "c.IS_ENABLED, " +
            "c.BOOK_ID, " +
            "c.TENANT_ID " +
            "FROM T_COST_CENTER c " +
            "WHERE c.CENTER_ID IN ( " +
            "    SELECT DISTINCT FROM_CENTER_ID " +
            "    FROM T_INTERNAL_SETTLEMENT " +
            "    WHERE FROM_CENTER_ID NOT IN (SELECT CENTER_ID FROM REDACTED.T_RESPONSIBILITY_CENTER) " +
            "    UNION " +
            "    SELECT DISTINCT TO_CENTER_ID " +
            "    FROM T_INTERNAL_SETTLEMENT " +
            "    WHERE TO_CENTER_ID NOT IN (SELECT CENTER_ID FROM REDACTED.T_RESPONSIBILITY_CENTER) " +
            ") " +
            "AND c.IS_DELETED = 0 AND c.IS_ENABLED = 1 " +
            "ORDER BY c.CENTER_ID";

        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 创建缺失的责任中心记录
     */
    private int createMissingResponsibilityCenters(List<Map<String, Object>> missingCenters) {
        if (missingCenters.isEmpty()) {
            return 0;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO REDACTED.T_RESPONSIBILITY_CENTER (");
        sql.append("CENTER_ID, CENTER_CODE, CENTER_NAME, CENTER_TYPE, ");
        sql.append("PARENT_CENTER_ID, CENTER_LEVEL, IS_LEAF, MANAGER_ID, ");
        sql.append("IS_ENABLED, BOOK_ID, TENANT_ID, VERSION, IS_DELETED, ");
        sql.append("CREATE_TIME, UPDATE_TIME, CREATOR, UPDATER");
        sql.append(") VALUES (");

        for (int i = 0; i < missingCenters.size(); i++) {
            if (i > 0) sql.append(", ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE, 1, 1)");
        }
        sql.append(")");

        Object[] params = new Object[missingCenters.size() * 12];
        int index = 0;

        for (Map<String, Object> center : missingCenters) {
            params[index++] = center.get("CENTER_ID");
            params[index++] = center.get("CENTER_CODE");
            params[index++] = center.get("CENTER_NAME");
            params[index++] = center.get("CENTER_TYPE");
            params[index++] = center.get("PARENT_CENTER_ID");
            params[index++] = center.get("CENTER_LEVEL");
            params[index++] = center.get("IS_LEAF");
            params[index++] = center.get("MANAGER_ID");
            params[index++] = center.get("IS_ENABLED");
            params[index++] = center.get("BOOK_ID");
            params[index++] = center.get("TENANT_ID");
            params[index++] = 1; // VERSION
            params[index++] = 0; // IS_DELETED
        }

        return jdbcTemplate.update(sql.toString(), params);
    }

    /**
     * 查询剩余问题
     */
    private List<Map<String, Object>> queryRemainingIssues() {
        String sql = "SELECT " +
            "s.SETTLEMENT_ID, " +
            "s.FROM_CENTER_ID, " +
            "s.TO_CENTER_ID, " +
            "s.BOOK_ID, " +
            "s.SETTLEMENT_NO " +
            "FROM T_INTERNAL_SETTLEMENT s " +
            "LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID " +
            "LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID " +
            "WHERE r_from.CENTER_ID IS NULL OR r_to.CENTER_ID IS NULL";

        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 检查外键约束状态
     */
    public boolean checkForeignKeyConstraints() {
        String sql = "SELECT COUNT(*) as issue_count " +
            "FROM T_INTERNAL_SETTLEMENT s " +
            "LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_from ON s.FROM_CENTER_ID = r_from.CENTER_ID " +
            "LEFT JOIN REDACTED.T_RESPONSIBILITY_CENTER r_to ON s.TO_CENTER_ID = r_to.CENTER_ID " +
            "WHERE r_from.CENTER_ID IS NULL OR r_to.CENTER_ID IS NULL";

        Integer issueCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return issueCount != null && issueCount == 0;
    }
}