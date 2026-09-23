package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblProxyDelegationPermission;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 委托权限Mapper
 */
public interface TblProxyDelegationPermissionMapper extends BaseMapper<TblProxyDelegationPermission> {

    /**
     * 根据委托ID查询权限列表
     */
    List<TblProxyDelegationPermission> selectByDelegationId(@Param("delegationId") String delegationId);

    /**
     * 根据权限类型查询
     */
    List<TblProxyDelegationPermission> selectByPermissionType(@Param("permissionType") String permissionType);

    /**
     * 根据权限值查询
     */
    List<TblProxyDelegationPermission> selectByPermissionValue(@Param("permissionValue") String permissionValue);

    /**
     * 根据委托ID和权限类型查询
     */
    List<TblProxyDelegationPermission> selectByDelegationIdAndType(@Param("delegationId") String delegationId, @Param("permissionType") String permissionType);
}
