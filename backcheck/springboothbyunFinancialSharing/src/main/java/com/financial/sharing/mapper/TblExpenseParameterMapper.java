package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblExpenseParameter;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 费用参数Mapper
 */
public interface TblExpenseParameterMapper extends BaseMapper<TblExpenseParameter> {

    /**
     * 根据参数编码查询
     */
    TblExpenseParameter selectByParameterCode(@Param("parameterCode") String parameterCode);

    /**
     * 根据参数类型查询
     */
    List<TblExpenseParameter> selectByParameterType(@Param("parameterType") String parameterType);

    /**
     * 根据数据类型查询
     */
    List<TblExpenseParameter> selectByDataType(@Param("dataType") String dataType);

    /**
     * 根据启用状态查询
     */
    List<TblExpenseParameter> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 根据必填状态查询
     */
    List<TblExpenseParameter> selectByIsRequired(@Param("isRequired") Integer isRequired);
}
