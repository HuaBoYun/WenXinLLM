package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblMonitorRule;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblMonitorRuleMapper extends BaseMapper<TblMonitorRule> {

    @SelectProvider(type=TblMonitorRuleSqlConfig.class,method="findTblMonitorRuleByUser")
    IPage<TblMonitorRule> findTblMonitorRuleByUser(IPage<TblMonitorRule> page, BigDecimal staffid);

    @Select("SELECT * FROM TBL_MONITOR_RULE WHERE RULEID = #{ruleid}")
    TblMonitorRule findRuleid(BigDecimal ruleid);

    @SelectProvider(type=TblMonitorRuleSqlConfig.class,method="findAll")
    IPage<TblMonitorRule> findAll(String solutionid, IPage<TblMonitorRule> page);

}
