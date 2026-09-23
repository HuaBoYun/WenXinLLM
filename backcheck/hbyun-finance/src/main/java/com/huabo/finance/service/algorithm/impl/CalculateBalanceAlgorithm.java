package com.huabo.finance.service.algorithm.impl;

import com.huabo.finance.mapper.GatherFinanceDataMapper;
import com.huabo.finance.service.algorithm.TransformAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 科目余额计算算法(借贷方向判断)
 * 
 * 算法说明:
 * 1. 根据借贷方向(DC)和余额方向判断最终余额
 * 2. 借方科目: 借方余额 = 借方发生额 - 贷方发生额
 * 3. 贷方科目: 贷方余额 = 贷方发生额 - 借方发生额
 * 4. 判断余额方向: 正数-与科目方向一致, 负数-与科目方向相反, 零-平
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Component
public class CalculateBalanceAlgorithm implements TransformAlgorithm {

    @Autowired
    private GatherFinanceDataMapper gatherFinanceDataMapper;

    @Override
    public String getAlgorithmCode() {
        return "CALC_BALANCE";
    }

    @Override
    public String getAlgorithmName() {
        return "计算科目余额(借贷方向判断)";
    }

    @Override
    public void execute(Map<String, Object> params, String taskId) throws Exception {
        log.info("开始执行科目余额计算算法, taskId={}, params={}", taskId, params);

        // 获取参数
        String targetTable = (String) params.get("targetTable");
        String dcField = (String) params.get("dcField"); // 借贷方向字段
        String debitField = (String) params.get("debitField"); // 借方发生额字段
        String creditField = (String) params.get("creditField"); // 贷方发生额字段
        String balanceField = (String) params.get("balanceField"); // 余额字段
        String balanceDirectionField = (String) params.get("balanceDirectionField"); // 余额方向字段

        // 构建SQL - 使用CASE WHEN实现借贷方向判断
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(targetTable).append(" SET ");
        
        // 计算余额
        sql.append(balanceField).append(" = CASE ");
        sql.append("WHEN ").append(dcField).append(" = '借' THEN ").append(debitField).append(" - ").append(creditField).append(" ");
        sql.append("WHEN ").append(dcField).append(" = '贷' THEN ").append(creditField).append(" - ").append(debitField).append(" ");
        sql.append("ELSE 0 END, ");

        // 计算余额方向
        sql.append(balanceDirectionField).append(" = CASE ");
        sql.append("WHEN (").append(dcField).append(" = '借' AND ").append(debitField).append(" - ").append(creditField).append(" > 0) THEN '借' ");
        sql.append("WHEN (").append(dcField).append(" = '借' AND ").append(debitField).append(" - ").append(creditField).append(" < 0) THEN '贷' ");
        sql.append("WHEN (").append(dcField).append(" = '贷' AND ").append(creditField).append(" - ").append(debitField).append(" > 0) THEN '贷' ");
        sql.append("WHEN (").append(dcField).append(" = '贷' AND ").append(creditField).append(" - ").append(debitField).append(" < 0) THEN '借' ");
        sql.append("ELSE '平' END ");

        // 添加任务ID过滤条件(如果有)
        if (params.containsKey("orgId") && params.containsKey("year")) {
            sql.append("WHERE ORGID = '").append(params.get("orgId")).append("' ");
            sql.append("AND YEAR = '").append(params.get("year")).append("'");
        }

        log.info("科目余额计算SQL: {}", sql.toString());

        // 执行SQL
        gatherFinanceDataMapper.executeUpdateSql(sql.toString());

        log.info("科目余额计算算法执行完成, taskId={}", taskId);
    }

    @Override
    public boolean validateParams(Map<String, Object> params) {
        // 验证必填参数
        if (params == null || params.isEmpty()) {
            log.error("算法参数为空");
            return false;
        }

        String[] requiredFields = {"targetTable", "dcField", "debitField", "creditField", "balanceField", "balanceDirectionField"};
        for (String field : requiredFields) {
            if (!params.containsKey(field) || params.get(field) == null || params.get(field).toString().trim().isEmpty()) {
                log.error("缺少必填参数: {}", field);
                return false;
            }
        }

        return true;
    }
}

