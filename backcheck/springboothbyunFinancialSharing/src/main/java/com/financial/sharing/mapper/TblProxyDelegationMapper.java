package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblProxyDelegation;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 代理委托Mapper
 */
public interface TblProxyDelegationMapper extends BaseMapper<TblProxyDelegation> {

    /**
     * 根据委托编码查询
     */
    TblProxyDelegation selectByDelegationCode(@Param("delegationCode") String delegationCode);

    /**
     * 根据委托类型查询
     */
    List<TblProxyDelegation> selectByDelegationType(@Param("delegationType") String delegationType);

    /**
     * 根据委托人ID查询
     */
    List<TblProxyDelegation> selectByDelegatorId(@Param("delegatorId") String delegatorId);

    /**
     * 根据代理人ID查询
     */
    List<TblProxyDelegation> selectByProxyId(@Param("proxyId") String proxyId);

    /**
     * 根据启用状态查询
     */
    List<TblProxyDelegation> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 查询有效的委托关系（根据委托人和当前日期）
     */
    List<TblProxyDelegation> selectValidDelegationByDelegator(@Param("delegatorId") String delegatorId, @Param("currentDate") String currentDate);
}
