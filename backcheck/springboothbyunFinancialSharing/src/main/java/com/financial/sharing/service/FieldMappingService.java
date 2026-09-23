package com.financial.sharing.service;

import com.financial.sharing.dto.FieldMappingBatchParam;
import com.financial.sharing.dto.FieldMappingImportParam;
import com.financial.sharing.dto.FieldMappingParam;
import com.financial.sharing.dto.FieldMappingSaveParam;
import com.hbfk.util.JsonBean;

/**
 * 字段映射服务接口
 */
public interface FieldMappingService {

    /**
     * 查询字段映射列表
     */
    JsonBean getFieldMappingList(FieldMappingParam param);

    /**
     * 获取字段映射详情
     */
    JsonBean getFieldMapping(Long mappingId, Long tenantId);

    /**
     * 保存字段映射
     */
    Long saveFieldMapping(FieldMappingSaveParam param);

    /**
     * 更新字段映射
     */
    void updateFieldMapping(Long mappingId, FieldMappingSaveParam param);

    /**
     * 删除字段映射
     */
    void deleteFieldMapping(Long mappingId, Long tenantId);

    /**
     * 根据数据源ID查询字段映射
     */
    JsonBean getFieldMappingByDataSourceId(Long dataSourceId, Long tenantId);

    /**
     * 批量操作字段映射
     */
    JsonBean batchOperationFieldMapping(FieldMappingBatchParam param);

    /**
     * 导入字段映射
     */
    JsonBean importFieldMapping(FieldMappingImportParam param);

    /**
     * 导出字段映射
     */
    JsonBean exportFieldMapping(Long dataSourceId, Long tenantId);

    /**
     * 复制字段映射
     */
    void copyFieldMapping(Long sourceDataSourceId, Long targetDataSourceId, Long tenantId, Long createUser);

    /**
     * 更新排序
     */
    void updateSortOrder(Long mappingId, Integer sortOrder, Long updateUser);
}