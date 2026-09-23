package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblExpenseParameterPermission;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 参数权限Mapper
 */
public interface TblExpenseParameterPermissionMapper extends BaseMapper<TblExpenseParameterPermission> {

    /**
     * 根据参数ID查询权限列表
     */
    List<TblExpenseParameterPermission> selectByParameterId(@Param("parameterId") String parameterId);

    /**
     * 根据权限类型查询
     */
    List<TblExpenseParameterPermission> selectByPermissionType(@Param("permissionType") String permissionType);

    /**
     * 根据权限目标查询
     */
    List<TblExpenseParameterPermission> selectByPermissionTarget(@Param("permissionTarget") String permissionTarget);

    /**
     * 根据参数ID和权限类型查询
     */
    List<TblExpenseParameterPermission> selectByParameterIdAndType(@Param("parameterId") String parameterId, @Param("permissionType") String permissionType);
}
