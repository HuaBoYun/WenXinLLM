package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblExpenseParameterOption;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 参数选项Mapper
 */
public interface TblExpenseParameterOptionMapper extends BaseMapper<TblExpenseParameterOption> {

    /**
     * 根据参数ID查询选项列表
     */
    List<TblExpenseParameterOption> selectByParameterId(@Param("parameterId") String parameterId);

    /**
     * 根据启用状态查询
     */
    List<TblExpenseParameterOption> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 根据参数ID和启用状态查询
     */
    List<TblExpenseParameterOption> selectByParameterIdAndEnabled(@Param("parameterId") String parameterId, @Param("isEnabled") Integer isEnabled);
}
