package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblAuditRuleLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 规则执行日志表Mapper
 */
public interface TblAuditRuleLogMapper extends BaseMapper<TblAuditRuleLog> {

    /**
     * 根据规则ID查询日志列表
     */
    List<TblAuditRuleLog> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据业务类型和业务ID查询日志列表
     */
    List<TblAuditRuleLog> selectByBusiness(@Param("businessType") String businessType, @Param("businessId") String businessId);

    /**
     * 根据执行结果查询日志列表
     */
    List<TblAuditRuleLog> selectByExecutionResult(@Param("executionResult") String executionResult);

    /**
     * 根据执行人ID查询日志列表
     */
    List<TblAuditRuleLog> selectByExecutionUser(@Param("executionUser") String executionUser);

    /**
     * 根据规则ID和执行结果查询日志列表
     */
    List<TblAuditRuleLog> selectByRuleIdAndResult(@Param("ruleId") String ruleId, @Param("executionResult") String executionResult);

    /**
     * 根据业务类型查询日志列表，按执行时间降序
     */
    List<TblAuditRuleLog> selectByBusinessTypeOrderByTime(@Param("businessType") String businessType);
}
