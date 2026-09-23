package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblGeneralStandardLevel;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 标准级别配置Mapper
 */
public interface TblGeneralStandardLevelMapper extends BaseMapper<TblGeneralStandardLevel> {

    /**
     * 根据标准ID查询
     */
    List<TblGeneralStandardLevel> selectByStandardId(@Param("standardId") String standardId);

    /**
     * 根据级别编码查询
     */
    TblGeneralStandardLevel selectByLevelCode(@Param("levelCode") String levelCode);

    /**
     * 根据标准ID查询，按排序号排序
     */
    List<TblGeneralStandardLevel> selectByStandardIdOrderBySort(@Param("standardId") String standardId);

    /**
     * 根据标准ID和级别编码查询
     */
    TblGeneralStandardLevel selectByStandardIdAndLevelCode(@Param("standardId") String standardId, @Param("levelCode") String levelCode);

    /**
     * 根据标准ID查询最大排序号
     */
    Integer selectMaxSortOrderByStandardId(@Param("standardId") String standardId);
}
