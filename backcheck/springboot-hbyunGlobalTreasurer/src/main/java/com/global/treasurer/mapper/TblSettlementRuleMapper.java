package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblSettlementRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface TblSettlementRuleMapper extends BaseMapper<TblSettlementRule> {
    List<TblSettlementRule> selectRulePage(Map<String, Object> params);
    int countRuleList(Map<String, Object> params);
    List<TblSettlementRule> selectApplicableRules(Map<String, Object> params);
    Map<String, Object> selectRuleExecutionStats(@Param("orgId") Long orgId);
    List<Map<String, Object>> selectRuleConflicts(@Param("orgId") Long orgId);
}
