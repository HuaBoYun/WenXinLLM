package com.huabo.finance.service.algorithm.impl;

import com.huabo.finance.mapper.GatherFinanceDataMapper;
import com.huabo.finance.service.algorithm.TransformAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 辅助账余额计算算法
 * 
 * 算法说明:
 * 1. 计算辅助账余额(按辅助账类型分组汇总)
 * 2. 从辅助账明细表汇总到辅助账余额表
 * 3. 汇总字段: 借方发生额、贷方发生额、余额
 * 4. 分组字段: 科目编码、辅助账类型、辅助账编码
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Component
public class CalculateAuxBalanceAlgorithm implements TransformAlgorithm {

    @Autowired
    private GatherFinanceDataMapper gatherFinanceDataMapper;

    @Override
    public String getAlgorithmCode() {
        return "CALC_AUX_BALANCE";
    }

    @Override
    public String getAlgorithmName() {
        return "计算辅助账余额";
    }

    @Override
    public void execute(Map<String, Object> params, String taskId) throws Exception {
        log.info("开始执行辅助账余额计算算法, taskId={}, params={}", taskId, params);

        // 获取参数
        String sourceTable = (String) params.get("sourceTable"); // 辅助账明细表
        String targetTable = (String) params.get("targetTable"); // 辅助账余额表
        String accIdField = (String) params.get("accIdField"); // 科目编码字段
        String auxTypeField = (String) params.get("auxTypeField"); // 辅助账类型字段
        String auxCodeField = (String) params.get("auxCodeField"); // 辅助账编码字段
        String debitField = (String) params.get("debitField"); // 借方金额字段
        String creditField = (String) params.get("creditField"); // 贷方金额字段

        // 构建WHERE条件
        String whereCondition = "";
        if (params.containsKey("orgId") && params.containsKey("year")) {
            whereCondition = " WHERE ORGID = '" + params.get("orgId") + "' AND YEAR = '" + params.get("year") + "'";
        }

        // 先清空目标表
        String deleteSql = "DELETE FROM " + targetTable + whereCondition;
        log.info("清空辅助账余额表SQL: {}", deleteSql);
        gatherFinanceDataMapper.executeDeleteSql(deleteSql);

        // 构建汇总插入SQL
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(targetTable).append(" (");
        sql.append(accIdField).append(", ");
        sql.append(auxTypeField).append(", ");
        sql.append(auxCodeField).append(", ");
        sql.append(debitField).append(", ");
        sql.append(creditField).append(", ");
        sql.append("BALANCE");
        
        // 添加组织和年度字段(如果有)
        if (params.containsKey("orgId") && params.containsKey("year")) {
            sql.append(", ORGID, YEAR");
        }
        
        sql.append(") ");
        sql.append("SELECT ");
        sql.append(accIdField).append(", ");
        sql.append(auxTypeField).append(", ");
        sql.append(auxCodeField).append(", ");
        sql.append("COALESCE(SUM(").append(debitField).append("), 0) AS ").append(debitField).append(", ");
        sql.append("COALESCE(SUM(").append(creditField).append("), 0) AS ").append(creditField).append(", ");
        sql.append("COALESCE(SUM(").append(debitField).append("), 0) - COALESCE(SUM(").append(creditField).append("), 0) AS BALANCE");
        
        // 添加组织和年度字段(如果有)
        if (params.containsKey("orgId") && params.containsKey("year")) {
            sql.append(", ORGID, YEAR");
        }
        
        sql.append(" FROM ").append(sourceTable);
        sql.append(whereCondition);
        sql.append(" GROUP BY ");
        sql.append(accIdField).append(", ");
        sql.append(auxTypeField).append(", ");
        sql.append(auxCodeField);
        
        // 添加组织和年度分组(如果有)
        if (params.containsKey("orgId") && params.containsKey("year")) {
            sql.append(", ORGID, YEAR");
        }

        log.info("辅助账余额汇总SQL: {}", sql.toString());

        // 执行汇总SQL
        gatherFinanceDataMapper.executeInsertSql(sql.toString());

        log.info("辅助账余额计算算法执行完成, taskId={}", taskId);
    }

    @Override
    public boolean validateParams(Map<String, Object> params) {
        // 验证必填参数
        if (params == null || params.isEmpty()) {
            log.error("算法参数为空");
            return false;
        }

        String[] requiredFields = {"sourceTable", "targetTable", "accIdField", "auxTypeField", "auxCodeField", "debitField", "creditField"};
        for (String field : requiredFields) {
            if (!params.containsKey(field) || params.get(field) == null || params.get(field).toString().trim().isEmpty()) {
                log.error("缺少必填参数: {}", field);
                return false;
            }
        }

        return true;
    }
}

