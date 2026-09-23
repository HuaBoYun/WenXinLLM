package com.huabo.finance.service.algorithm.impl;

import com.huabo.finance.mapper.GatherFinanceDataMapper;
import com.huabo.finance.service.algorithm.TransformAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 上级科目编码计算算法
 * 
 * 算法说明:
 * 1. 根据科目编码规则计算上级科目编码
 * 2. 例如: 1001001 的上级是 1001, 1001 的上级是 1
 * 3. 从右向左逐级截取非零部分
 * 4. 根据上级科目编码查找上级科目ID
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Component
public class CalculateParentCodeAlgorithm implements TransformAlgorithm {

    @Autowired
    private GatherFinanceDataMapper gatherFinanceDataMapper;

    @Override
    public String getAlgorithmCode() {
        return "CALC_PARENT";
    }

    @Override
    public String getAlgorithmName() {
        return "计算上级科目编码";
    }

    @Override
    public void execute(Map<String, Object> params, String taskId) throws Exception {
        log.info("开始执行上级科目编码计算算法, taskId={}, params={}", taskId, params);

        // 获取参数
        String targetTable = (String) params.get("targetTable");
        String accCodeField = (String) params.get("accCodeField"); // 科目编码字段
        String accIdField = (String) params.get("accIdField"); // 科目ID字段
        String parentIdField = (String) params.get("parentIdField"); // 上级科目ID字段

        // 构建SQL - 使用子查询根据编码规则查找上级科目
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(targetTable).append(" T1 SET ");
        sql.append(parentIdField).append(" = (");
        sql.append("  SELECT T2.").append(accIdField);
        sql.append("  FROM ").append(targetTable).append(" T2 ");
        sql.append("  WHERE T2.").append(accCodeField).append(" = (");
        
        // 计算上级科目编码 - 从右向左截取非零部分
        // 使用RTRIM去除右侧的0,然后去掉最后一位非零字符
        sql.append("    CASE ");
        sql.append("      WHEN LENGTH(RTRIM(T1.").append(accCodeField).append(", '0')) > 1 THEN ");
        sql.append("        SUBSTR(RTRIM(T1.").append(accCodeField).append(", '0'), 1, LENGTH(RTRIM(T1.").append(accCodeField).append(", '0')) - 1) ");
        sql.append("      ELSE NULL ");
        sql.append("    END");
        sql.append("  )");

        // 添加过滤条件(如果有)
        if (params.containsKey("orgId") && params.containsKey("year")) {
            sql.append("  AND T2.ORGID = '").append(params.get("orgId")).append("' ");
            sql.append("  AND T2.YEAR = '").append(params.get("year")).append("'");
        }
        
        sql.append(")");

        // 添加WHERE条件
        if (params.containsKey("orgId") && params.containsKey("year")) {
            sql.append(" WHERE T1.ORGID = '").append(params.get("orgId")).append("' ");
            sql.append("AND T1.YEAR = '").append(params.get("year")).append("'");
        }

        log.info("上级科目编码计算SQL: {}", sql.toString());

        // 执行SQL
        gatherFinanceDataMapper.executeUpdateSql(sql.toString());

        log.info("上级科目编码计算算法执行完成, taskId={}", taskId);
    }

    @Override
    public boolean validateParams(Map<String, Object> params) {
        // 验证必填参数
        if (params == null || params.isEmpty()) {
            log.error("算法参数为空");
            return false;
        }

        String[] requiredFields = {"targetTable", "accCodeField", "accIdField", "parentIdField"};
        for (String field : requiredFields) {
            if (!params.containsKey(field) || params.get(field) == null || params.get(field).toString().trim().isEmpty()) {
                log.error("缺少必填参数: {}", field);
                return false;
            }
        }

        return true;
    }
}

