package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblProxyDelegationLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 代理操作日志Mapper
 */
public interface TblProxyDelegationLogMapper extends BaseMapper<TblProxyDelegationLog> {

    /**
     * 根据委托ID查询日志
     */
    List<TblProxyDelegationLog> selectByDelegationId(@Param("delegationId") String delegationId);

    /**
     * 根据操作人查询日志
     */
    List<TblProxyDelegationLog> selectByOperator(@Param("operator") String operator);

    /**
     * 根据业务类型查询
     */
    List<TblProxyDelegationLog> selectByBusinessType(@Param("businessType") String businessType);

    /**
     * 根据业务ID查询
     */
    List<TblProxyDelegationLog> selectByBusinessId(@Param("businessId") String businessId);

    /**
     * 根据委托ID和业务类型查询
     */
    List<TblProxyDelegationLog> selectByDelegationIdAndBusinessType(@Param("delegationId") String delegationId, @Param("businessType") String businessType);
}
