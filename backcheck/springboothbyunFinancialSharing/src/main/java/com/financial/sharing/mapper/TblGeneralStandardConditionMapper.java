package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblGeneralStandardCondition;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 适用条件Mapper
 */
public interface TblGeneralStandardConditionMapper extends BaseMapper<TblGeneralStandardCondition> {

    /**
     * 根据标准ID查询
     */
    List<TblGeneralStandardCondition> selectByStandardId(@Param("standardId") String standardId);

    /**
     * 根据标准ID查询，按优先级排序
     */
    List<TblGeneralStandardCondition> selectByStandardIdOrderByPriority(@Param("standardId") String standardId);

    /**
     * 根据条件表达式模糊查询
     */
    List<TblGeneralStandardCondition> selectByConditionExpressionLike(@Param("conditionExpression") String conditionExpression);

    /**
     * 根据标准ID和优先级查询
     */
    List<TblGeneralStandardCondition> selectByStandardIdAndPriority(@Param("standardId") String standardId, @Param("priority") Integer priority);

    /**
     * 根据标准ID查询最大优先级
     */
    Integer selectMaxPriorityByStandardId(@Param("standardId") String standardId);
}
