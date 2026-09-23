package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.FieldMappingEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字段映射Mapper
 */
public interface FieldMappingMapper extends BaseMapper<FieldMappingEntity> {

    /**
     * 分页查询字段映射
     */
    IPage<FieldMappingEntity> selectFieldMappingPage(Page<FieldMappingEntity> page, @Param("param") Map<String, Object> param);

    /**
     * 根据数据源ID查询字段映射
     */
    List<FieldMappingEntity> selectByDataSourceId(@Param("dataSourceId") Long dataSourceId, @Param("tenantId") Long tenantId);

    /**
     * 根据目标表查询字段映射
     */
    List<FieldMappingEntity> selectByTargetTable(@Param("targetTable") String targetTable, @Param("tenantId") Long tenantId);

    /**
     * 批量保存字段映射
     */
    int batchInsert(@Param("mappings") List<FieldMappingEntity> mappings);

    /**
     * 批量更新状态
     */
    int batchUpdateStatus(@Param("mappingIds") List<Long> mappingIds, @Param("isEnabled") Boolean isEnabled, @Param("updateUser") Long updateUser);

    /**
     * 批量删除
     */
    int batchDelete(@Param("mappingIds") List<Long> mappingIds, @Param("tenantId") Long tenantId);

    /**
     * 根据数据源ID删除
     */
    int deleteByDataSourceId(@Param("dataSourceId") Long dataSourceId, @Param("tenantId") Long tenantId);

    /**
     * 查询最大排序号
     */
    Integer selectMaxSortOrder(@Param("dataSourceId") Long dataSourceId, @Param("tenantId") Long tenantId);

    /**
     * 更新排序
     */
    int updateSortOrder(@Param("mappingId") Long mappingId, @Param("sortOrder") Integer sortOrder, @Param("updateUser") Long updateUser);

    /**
     * 复制字段映射
     */
    int copyFieldMappings(@Param("sourceDataSourceId") Long sourceDataSourceId,
                         @Param("targetDataSourceId") Long targetDataSourceId,
                         @Param("tenantId") Long tenantId,
                         @Param("createUser") Long createUser);
}