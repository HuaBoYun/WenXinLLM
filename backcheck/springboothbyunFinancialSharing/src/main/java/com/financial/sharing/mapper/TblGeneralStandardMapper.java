package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblGeneralStandard;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 通用标准Mapper
 */
public interface TblGeneralStandardMapper extends BaseMapper<TblGeneralStandard> {

    /**
     * 根据标准编码查询
     */
    TblGeneralStandard selectByStandardCode(@Param("standardCode") String standardCode);

    /**
     * 根据标准类型查询
     */
    List<TblGeneralStandard> selectByStandardType(@Param("standardType") String standardType);

    /**
     * 根据标准名称模糊查询
     */
    List<TblGeneralStandard> selectByStandardNameLike(@Param("standardName") String standardName);

    /**
     * 根据是否启用查询
     */
    List<TblGeneralStandard> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 根据适用范围查询
     */
    List<TblGeneralStandard> selectByApplicableScope(@Param("applicableScope") String applicableScope);
}
