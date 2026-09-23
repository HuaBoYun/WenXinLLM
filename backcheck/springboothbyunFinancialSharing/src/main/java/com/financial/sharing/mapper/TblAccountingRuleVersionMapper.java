package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblAccountingRuleVersion;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 会计规则版本Mapper
 */
public interface TblAccountingRuleVersionMapper extends BaseMapper<TblAccountingRuleVersion> {

    /**
     * 根据规则ID查询
     */
    List<TblAccountingRuleVersion> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据版本号查询
     */
    List<TblAccountingRuleVersion> selectByVersionNumber(@Param("versionNumber") String versionNumber);

    /**
     * 根据规则ID查询最新版本
     */
    TblAccountingRuleVersion selectLatestByRuleId(@Param("ruleId") String ruleId);
}
