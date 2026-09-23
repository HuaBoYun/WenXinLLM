package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblProxyDelegationHistory;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 委托历史记录Mapper
 */
public interface TblProxyDelegationHistoryMapper extends BaseMapper<TblProxyDelegationHistory> {

    /**
     * 根据委托ID查询历史记录
     */
    List<TblProxyDelegationHistory> selectByDelegationId(@Param("delegationId") String delegationId);

    /**
     * 根据操作类型查询
     */
    List<TblProxyDelegationHistory> selectByOperationType(@Param("operationType") String operationType);

    /**
     * 根据操作人查询
     */
    List<TblProxyDelegationHistory> selectByOperator(@Param("operator") String operator);

    /**
     * 根据委托ID和操作类型查询
     */
    List<TblProxyDelegationHistory> selectByDelegationIdAndOperationType(@Param("delegationId") String delegationId, @Param("operationType") String operationType);
}
