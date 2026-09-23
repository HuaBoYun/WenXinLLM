package com.huabo.finance.service.algorithm.impl;

import com.huabo.finance.mapper.GatherFinanceDataMapper;
import com.huabo.finance.service.algorithm.TransformAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 底层科目计算算法
 * 
 * 算法说明:
 * 1. 底层科目: 没有下级科目的科目
 * 2. 通过判断是否存在下级科目来确定IGRADE字段
 * 3. IGRADE=1表示底层科目, IGRADE=0表示非底层科目
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Component
public class CalculateBottomAccountAlgorithm implements TransformAlgorithm {

    @Autowired
    private GatherFinanceDataMapper gatherFinanceDataMapper;

    @Override
    public String getAlgorithmCode() {
        return "CALC_BOTTOM";
    }

    @Override
    public String getAlgorithmName() {
        return "计算底层科目";
    }

    @Override
    public void execute(Map<String, Object> params, String taskId) throws Exception {
        log.info("开始执行底层科目计算算法, taskId={}, params={}", taskId, params);

        // 获取参数
        String targetTable = (String) params.get("targetTable");
        String accIdField = (String) params.get("accIdField"); // 科目编码字段
        String parentIdField = (String) params.get("parentIdField"); // 上级科目字段
        String igradeField = (String) params.get("igradeField"); // 底层标识字段

        // 构建SQL - 使用子查询判断是否存在下级科目
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(targetTable).append(" T1 SET ");
        sql.append(igradeField).append(" = CASE ");
        sql.append("WHEN EXISTS (");
        sql.append("  SELECT 1 FROM ").append(targetTable).append(" T2 ");
        sql.append("  WHERE T2.").append(parentIdField).append(" = T1.").append(accIdField);
        
        // 添加过滤条件(如果有)
        if (params.containsKey("orgId") && params.containsKey("year")) {
            sql.append("  AND T2.ORGID = '").append(params.get("orgId")).append("' ");
            sql.append("  AND T2.YEAR = '").append(params.get("year")).append("'");
        }
        
        sql.append(") THEN 0 "); // 存在下级科目,非底层
        sql.append("ELSE 1 END"); // 不存在下级科目,是底层

        // 添加WHERE条件
        if (params.containsKey("orgId") && params.containsKey("year")) {
            sql.append(" WHERE T1.ORGID = '").append(params.get("orgId")).append("' ");
            sql.append("AND T1.YEAR = '").append(params.get("year")).append("'");
        }

        log.info("底层科目计算SQL: {}", sql.toString());

        // 执行SQL
        gatherFinanceDataMapper.executeUpdateSql(sql.toString());

        log.info("底层科目计算算法执行完成, taskId={}", taskId);
    }

    @Override
    public boolean validateParams(Map<String, Object> params) {
        // 验证必填参数
        if (params == null || params.isEmpty()) {
            log.error("算法参数为空");
            return false;
        }

        String[] requiredFields = {"targetTable", "accIdField", "parentIdField", "igradeField"};
        for (String field : requiredFields) {
            if (!params.containsKey(field) || params.get(field) == null || params.get(field).toString().trim().isEmpty()) {
                log.error("缺少必填参数: {}", field);
                return false;
            }
        }

        return true;
    }
}

