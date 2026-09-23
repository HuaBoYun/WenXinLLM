package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblAccountingRuleLibrary;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 会计规则库Mapper
 */
public interface TblAccountingRuleLibraryMapper extends BaseMapper<TblAccountingRuleLibrary> {

    /**
     * 根据库编码查询
     */
    TblAccountingRuleLibrary selectByLibraryCode(@Param("libraryCode") String libraryCode);

    /**
     * 根据库名称查询
     */
    List<TblAccountingRuleLibrary> selectByLibraryName(@Param("libraryName") String libraryName);

    /**
     * 根据库类型查询
     */
    List<TblAccountingRuleLibrary> selectByLibraryType(@Param("libraryType") String libraryType);

    /**
     * 根据是否启用查询
     */
    List<TblAccountingRuleLibrary> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);
}
