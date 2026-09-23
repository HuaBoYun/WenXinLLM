package com.financial.sharing.enterpriseReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.enterpriseReport.entity.TblFormGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表单组Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface FormGroupMapper extends BaseMapper<TblFormGroup> {

    /**
     * 查询表单组列表(关联目录名称)
     * 
     * @param tenantId 租户ID
     * @param directoryId 目录ID
     * @param groupName 表单组名称
     * @param status 状态
     * @return 表单组列表
     */
    List<TblFormGroup> selectFormGroupList(@Param("tenantId") String tenantId,
                                           @Param("directoryId") String directoryId,
                                           @Param("groupName") String groupName,
                                           @Param("status") String status);

    /**
     * 检查表单组编码是否存在
     * 
     * @param groupCode 表单组编码
     * @param tenantId 租户ID
     * @param excludeGroupId 排除的表单组ID
     * @return 数量
     */
    int checkGroupCodeExists(@Param("groupCode") String groupCode,
                            @Param("tenantId") String tenantId,
                            @Param("excludeGroupId") String excludeGroupId);
}

