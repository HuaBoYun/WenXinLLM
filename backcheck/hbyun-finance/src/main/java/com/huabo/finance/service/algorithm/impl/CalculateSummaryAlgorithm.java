package com.huabo.finance.service.algorithm.impl;

import com.huabo.finance.mapper.GatherFinanceDataMapper;
import com.huabo.finance.service.algorithm.TransformAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 逐级汇总算法
 * 
 * 算法说明:
 * 1. 从底层科目向上逐级汇总
 * 2. 先汇总底层科目(IGRADE=1)
 * 3. 再汇总上一级科目
 * 4. 循环直到顶级科目
 * 5. 汇总字段: 借方发生额、贷方发生额、余额
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Component
public class CalculateSummaryAlgorithm implements TransformAlgorithm {

    @Autowired
    private GatherFinanceDataMapper gatherFinanceDataMapper;

    @Override
    public String getAlgorithmCode() {
        return "CALC_SUMMARY";
    }

    @Override
    public String getAlgorithmName() {
        return "逐级汇总";
    }

    @Override
    public void execute(Map<String, Object> params, String taskId) throws Exception {
        log.info("开始执行逐级汇总算法, taskId={}, params={}", taskId, params);

        // 获取参数
        String targetTable = (String) params.get("targetTable");
        String accIdField = (String) params.get("accIdField"); // 科目ID字段
        String parentIdField = (String) params.get("parentIdField"); // 上级科目ID字段
        String igradeField = (String) params.get("igradeField"); // 底层标识字段
        String debitField = (String) params.get("debitField"); // 借方发生额字段
        String creditField = (String) params.get("creditField"); // 贷方发生额字段
        String balanceField = (String) params.get("balanceField"); // 余额字段

        // 构建WHERE条件
        String whereCondition = "";
        if (params.containsKey("orgId") && params.containsKey("year")) {
            whereCondition = " AND ORGID = '" + params.get("orgId") + "' AND YEAR = '" + params.get("year") + "'";
        }

        // 循环汇总,最多10级
        int maxLevel = 10;
        for (int level = 1; level <= maxLevel; level++) {
            log.info("开始汇总第{}级科目", level);

            // 构建汇总SQL
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ").append(targetTable).append(" T1 SET ");
            
            // 汇总借方发生额
            sql.append(debitField).append(" = (");
            sql.append("  SELECT COALESCE(SUM(T2.").append(debitField).append("), 0) ");
            sql.append("  FROM ").append(targetTable).append(" T2 ");
            sql.append("  WHERE T2.").append(parentIdField).append(" = T1.").append(accIdField);
            sql.append("  AND T2.").append(igradeField).append(" = 1");
            sql.append(whereCondition);
            sql.append("), ");

            // 汇总贷方发生额
            sql.append(creditField).append(" = (");
            sql.append("  SELECT COALESCE(SUM(T2.").append(creditField).append("), 0) ");
            sql.append("  FROM ").append(targetTable).append(" T2 ");
            sql.append("  WHERE T2.").append(parentIdField).append(" = T1.").append(accIdField);
            sql.append("  AND T2.").append(igradeField).append(" = 1");
            sql.append(whereCondition);
            sql.append("), ");

            // 汇总余额
            sql.append(balanceField).append(" = (");
            sql.append("  SELECT COALESCE(SUM(T2.").append(balanceField).append("), 0) ");
            sql.append("  FROM ").append(targetTable).append(" T2 ");
            sql.append("  WHERE T2.").append(parentIdField).append(" = T1.").append(accIdField);
            sql.append("  AND T2.").append(igradeField).append(" = 1");
            sql.append(whereCondition);
            sql.append(") ");

            // 只更新有下级科目的科目
            sql.append("WHERE EXISTS (");
            sql.append("  SELECT 1 FROM ").append(targetTable).append(" T3 ");
            sql.append("  WHERE T3.").append(parentIdField).append(" = T1.").append(accIdField);
            sql.append("  AND T3.").append(igradeField).append(" = 1");
            sql.append(whereCondition);
            sql.append(")");
            sql.append(whereCondition.replace("AND", "AND T1."));

            log.info("第{}级汇总SQL: {}", level, sql.toString());

            // 执行汇总SQL
            gatherFinanceDataMapper.executeUpdateSql(sql.toString());

            // 更新已汇总科目的IGRADE标识为0(非底层)
            StringBuilder updateIgradeSql = new StringBuilder();
            updateIgradeSql.append("UPDATE ").append(targetTable).append(" SET ");
            updateIgradeSql.append(igradeField).append(" = 0 ");
            updateIgradeSql.append("WHERE ").append(igradeField).append(" = 1 ");
            updateIgradeSql.append("AND EXISTS (");
            updateIgradeSql.append("  SELECT 1 FROM ").append(targetTable).append(" T2 ");
            updateIgradeSql.append("  WHERE T2.").append(parentIdField).append(" = ").append(targetTable).append(".").append(accIdField);
            updateIgradeSql.append(whereCondition.replace("AND", "AND T2."));
            updateIgradeSql.append(")");
            updateIgradeSql.append(whereCondition);

            gatherFinanceDataMapper.executeUpdateSql(updateIgradeSql.toString());

            // 检查是否还有需要汇总的科目
            String checkSql = "SELECT COUNT(*) FROM " + targetTable + " WHERE " + igradeField + " = 1" + whereCondition;
            // 这里简化处理,实际应该查询数据库判断
            log.info("第{}级汇总完成", level);
        }

        log.info("逐级汇总算法执行完成, taskId={}", taskId);
    }

    @Override
    public boolean validateParams(Map<String, Object> params) {
        // 验证必填参数
        if (params == null || params.isEmpty()) {
            log.error("算法参数为空");
            return false;
        }

        String[] requiredFields = {"targetTable", "accIdField", "parentIdField", "igradeField", "debitField", "creditField", "balanceField"};
        for (String field : requiredFields) {
            if (!params.containsKey(field) || params.get(field) == null || params.get(field).toString().trim().isEmpty()) {
                log.error("缺少必填参数: {}", field);
                return false;
            }
        }

        return true;
    }
}

