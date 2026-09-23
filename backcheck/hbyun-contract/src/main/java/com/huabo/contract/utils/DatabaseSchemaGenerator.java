package com.huabo.contract.utils;

import com.huabo.contract.config.DatabaseCompatibilityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库表结构生成器
 * 根据不同数据库类型生成兼容的建表脚本
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Component
public class DatabaseSchemaGenerator {

    @Autowired
    private DatabaseCompatibilityUtils compatibilityUtils;

    /**
     * 生成标书模板表的建表脚本
     *
     * @return 建表脚本
     */
    public String generateBidTemplateTableSql() {
        Map<String, String> columns = new LinkedHashMap<>();
        
        columns.put("id", "BIGINT_AUTO PRIMARY KEY");
        columns.put("template_no", "VARCHAR(50) NOT NULL");
        columns.put("template_name", "VARCHAR(200) NOT NULL");
        columns.put("template_type", "TINYINT NOT NULL DEFAULT 1");
        columns.put("template_category", "VARCHAR(100)");
        columns.put("template_version", "VARCHAR(20)");
        columns.put("template_description", "TEXT");
        columns.put("template_content", "TEXT");
        columns.put("template_structure", "TEXT");
        columns.put("required_sections", "TEXT");
        columns.put("optional_sections", "TEXT");
        columns.put("template_format", "VARCHAR(20)");
        columns.put("file_size", "BIGINT DEFAULT 0");
        columns.put("file_path", "VARCHAR(500)");
        columns.put("preview_path", "VARCHAR(500)");
        columns.put("template_status", "TINYINT NOT NULL DEFAULT 1");
        columns.put("is_default", "TINYINT NOT NULL DEFAULT 0");
        columns.put("is_enabled", "TINYINT NOT NULL DEFAULT 1");
        columns.put("usage_count", "INT DEFAULT 0");
        columns.put("download_count", "INT DEFAULT 0");
        columns.put("rating_score", "DECIMAL(3,2) DEFAULT 0.00");
        columns.put("rating_count", "INT DEFAULT 0");
        columns.put("applicable_projects", "TEXT");
        columns.put("industry_scope", "VARCHAR(200)");
        columns.put("complexity_level", "TINYINT DEFAULT 1");
        columns.put("estimated_completion_time", "INT DEFAULT 0");
        columns.put("template_tags", "VARCHAR(500)");
        columns.put("keywords", "VARCHAR(500)");
        columns.put("author_id", "BIGINT");
        columns.put("author_name", "VARCHAR(100)");
        columns.put("reviewer_id", "BIGINT");
        columns.put("reviewer_name", "VARCHAR(100)");
        columns.put("review_date", "DATETIME");
        columns.put("review_comments", "TEXT");
        columns.put("approval_status", "TINYINT DEFAULT 1");
        columns.put("approver_id", "BIGINT");
        columns.put("approver_name", "VARCHAR(100)");
        columns.put("approval_date", "DATETIME");
        columns.put("approval_comments", "TEXT");
        columns.put("last_used_date", "DATETIME");
        columns.put("maintenance_date", "DATETIME");
        columns.put("expiry_date", "DATETIME");
        columns.put("backup_path", "VARCHAR(500)");
        columns.put("change_log", "TEXT");
        columns.put("usage_instructions", "TEXT");
        columns.put("related_templates", "VARCHAR(500)");
        columns.put("remarks", "TEXT");
        columns.put("create_time", "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");
        columns.put("update_time", "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");
        columns.put("create_by", "BIGINT NOT NULL");
        columns.put("update_by", "BIGINT NOT NULL");
        columns.put("del_flag", "TINYINT NOT NULL DEFAULT 0");
        columns.put("version", "INT NOT NULL DEFAULT 1");

        return compatibilityUtils.getCreateTableSql("bid_template", columns);
    }

    /**
     * 生成保证金管理表的建表脚本
     *
     * @return 建表脚本
     */
    public String generateDepositManagementTableSql() {
        Map<String, String> columns = new LinkedHashMap<>();
        
        columns.put("id", "BIGINT_AUTO PRIMARY KEY");
        columns.put("deposit_no", "VARCHAR(50) NOT NULL");
        columns.put("project_id", "BIGINT NOT NULL");
        columns.put("project_name", "VARCHAR(200)");
        columns.put("bidding_project_id", "BIGINT");
        columns.put("deposit_type", "TINYINT NOT NULL DEFAULT 1");
        columns.put("deposit_amount", "DECIMAL(15,2) NOT NULL DEFAULT 0.00");
        columns.put("paid_amount", "DECIMAL(15,2) DEFAULT 0.00");
        columns.put("refund_amount", "DECIMAL(15,2) DEFAULT 0.00");
        columns.put("forfeited_amount", "DECIMAL(15,2) DEFAULT 0.00");
        columns.put("deposit_ratio", "DECIMAL(5,2) DEFAULT 0.00");
        columns.put("payment_method", "TINYINT DEFAULT 1");
        columns.put("payment_account", "VARCHAR(100)");
        columns.put("payment_bank", "VARCHAR(200)");
        columns.put("payment_date", "DATETIME");
        columns.put("payment_voucher", "VARCHAR(200)");
        columns.put("refund_date", "DATETIME");
        columns.put("refund_account", "VARCHAR(100)");
        columns.put("refund_bank", "VARCHAR(200)");
        columns.put("refund_voucher", "VARCHAR(200)");
        columns.put("deposit_status", "TINYINT NOT NULL DEFAULT 1");
        columns.put("risk_level", "TINYINT DEFAULT 1");
        columns.put("guarantor_id", "BIGINT");
        columns.put("guarantor_name", "VARCHAR(100)");
        columns.put("guarantee_type", "TINYINT DEFAULT 1");
        columns.put("guarantee_amount", "DECIMAL(15,2) DEFAULT 0.00");
        columns.put("guarantee_start_date", "DATETIME");
        columns.put("guarantee_end_date", "DATETIME");
        columns.put("guarantee_document", "VARCHAR(500)");
        columns.put("manager_id", "BIGINT");
        columns.put("manager_name", "VARCHAR(100)");
        columns.put("contact_person", "VARCHAR(100)");
        columns.put("contact_phone", "VARCHAR(20)");
        columns.put("contact_email", "VARCHAR(100)");
        columns.put("remarks", "TEXT");
        columns.put("create_time", "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");
        columns.put("update_time", "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");
        columns.put("create_by", "BIGINT NOT NULL");
        columns.put("update_by", "BIGINT NOT NULL");
        columns.put("del_flag", "TINYINT NOT NULL DEFAULT 0");
        columns.put("version", "INT NOT NULL DEFAULT 1");

        return compatibilityUtils.getCreateTableSql("deposit_management", columns);
    }

    /**
     * 生成所有新增表的建表脚本
     *
     * @return 建表脚本列表
     */
    public List<String> generateAllTableSql() {
        List<String> sqlList = new ArrayList<>();
        
        // 标书模板表
        sqlList.add(generateBidTemplateTableSql());
        
        // 保证金管理表
        sqlList.add(generateDepositManagementTableSql());
        
        return sqlList;
    }

    /**
     * 生成索引创建脚本
     *
     * @return 索引创建脚本列表
     */
    public List<String> generateIndexSql() {
        List<String> sqlList = new ArrayList<>();
        
        // 标书模板表索引
        sqlList.add(compatibilityUtils.getCreateIndexSql("bid_template", "uk_template_no", 
            new String[]{"template_no"}, true));
        sqlList.add(compatibilityUtils.getCreateIndexSql("bid_template", "idx_template_name", 
            new String[]{"template_name"}, false));
        sqlList.add(compatibilityUtils.getCreateIndexSql("bid_template", "idx_template_type", 
            new String[]{"template_type"}, false));
        sqlList.add(compatibilityUtils.getCreateIndexSql("bid_template", "idx_template_status", 
            new String[]{"template_status"}, false));
        sqlList.add(compatibilityUtils.getCreateIndexSql("bid_template", "idx_is_enabled", 
            new String[]{"is_enabled"}, false));
        sqlList.add(compatibilityUtils.getCreateIndexSql("bid_template", "idx_create_time", 
            new String[]{"create_time"}, false));
        
        // 保证金管理表索引
        sqlList.add(compatibilityUtils.getCreateIndexSql("deposit_management", "uk_deposit_no", 
            new String[]{"deposit_no"}, true));
        sqlList.add(compatibilityUtils.getCreateIndexSql("deposit_management", "idx_project_id", 
            new String[]{"project_id"}, false));
        sqlList.add(compatibilityUtils.getCreateIndexSql("deposit_management", "idx_deposit_type", 
            new String[]{"deposit_type"}, false));
        sqlList.add(compatibilityUtils.getCreateIndexSql("deposit_management", "idx_deposit_status", 
            new String[]{"deposit_status"}, false));
        sqlList.add(compatibilityUtils.getCreateIndexSql("deposit_management", "idx_payment_date", 
            new String[]{"payment_date"}, false));
        sqlList.add(compatibilityUtils.getCreateIndexSql("deposit_management", "idx_manager_id", 
            new String[]{"manager_id"}, false));
        
        return sqlList;
    }

    /**
     * 生成序列创建脚本（仅达梦数据库需要）
     *
     * @return 序列创建脚本列表
     */
    public List<String> generateSequenceSql() {
        List<String> sqlList = new ArrayList<>();
        
        if (compatibilityUtils.isDaMeng()) {
            // 为达梦数据库创建序列
            sqlList.add(compatibilityUtils.getCreateSequenceSql("seq_bid_template", 1, 1));
            sqlList.add(compatibilityUtils.getCreateSequenceSql("seq_deposit_management", 1, 1));
        }
        
        return sqlList;
    }

    /**
     * 生成完整的数据库初始化脚本
     *
     * @return 完整的初始化脚本
     */
    public String generateFullInitScript() {
        StringBuilder script = new StringBuilder();
        
        script.append("-- ").append(compatibilityUtils.getDatabaseProductName()).append("数据库初始化脚本\n");
        script.append("-- 生成时间：").append(new java.util.Date()).append("\n\n");
        
        // 建表脚本
        script.append("-- 创建表\n");
        List<String> tableSqlList = generateAllTableSql();
        for (String sql : tableSqlList) {
            script.append(sql).append(";\n\n");
        }
        
        // 索引脚本
        script.append("-- 创建索引\n");
        List<String> indexSqlList = generateIndexSql();
        for (String sql : indexSqlList) {
            script.append(sql).append(";\n");
        }
        script.append("\n");
        
        // 序列脚本（仅达梦数据库）
        List<String> sequenceSqlList = generateSequenceSql();
        if (!sequenceSqlList.isEmpty()) {
            script.append("-- 创建序列\n");
            for (String sql : sequenceSqlList) {
                script.append(sql).append(";\n");
            }
            script.append("\n");
        }
        
        script.append("-- 初始化脚本生成完成\n");
        
        return script.toString();
    }

    /**
     * 打印数据库初始化脚本到日志
     */
    public void printInitScript() {
        String script = generateFullInitScript();
        log.info("数据库初始化脚本：\n{}", script);
    }

    /**
     * 验证数据库兼容性
     *
     * @return 验证结果
     */
    public boolean validateDatabaseCompatibility() {
        try {
            // 检查数据库类型
            if (compatibilityUtils.getCurrentDatabaseType() == DatabaseCompatibilityConfig.DatabaseType.UNKNOWN) {
                log.error("未知的数据库类型，无法进行兼容性处理");
                return false;
            }
            
            // 生成测试脚本
            String testScript = generateFullInitScript();
            if (testScript == null || testScript.trim().isEmpty()) {
                log.error("生成的初始化脚本为空");
                return false;
            }
            
            log.info("数据库兼容性验证通过，当前数据库：{}", compatibilityUtils.getDatabaseProductName());
            return true;
        } catch (Exception e) {
            log.error("数据库兼容性验证失败", e);
            return false;
        }
    }
}
