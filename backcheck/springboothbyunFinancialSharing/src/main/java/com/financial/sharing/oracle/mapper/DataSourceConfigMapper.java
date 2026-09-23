package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.DataSourceConfigEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据源配置Mapper
 */
public interface DataSourceConfigMapper extends BaseMapper<DataSourceConfigEntity> {

    /**
     * 分页查询数据源配置
     */
    IPage<DataSourceConfigEntity> selectDataSourceConfigPage(Page<DataSourceConfigEntity> page, @Param("param") Map<String, Object> param);

    /**
     * 根据配置代码查询
     */
    DataSourceConfigEntity selectByConfigCode(@Param("configCode") String configCode, @Param("tenantId") Long tenantId);

    /**
     * 批量更新状态
     */
    int batchUpdateStatus(@Param("configIds") List<Long> configIds, @Param("isEnabled") Boolean isEnabled, @Param("updateUser") Long updateUser);

    /**
     * 批量删除
     */
    int batchDelete(@Param("configIds") List<Long> configIds, @Param("tenantId") Long tenantId);

    /**
     * 查询启用的数据源
     */
    List<DataSourceConfigEntity> selectEnabledDataSources(@Param("tenantId") Long tenantId);

    /**
     * 根据数据源类型查询
     */
    List<DataSourceConfigEntity> selectByDataSourceType(@Param("dataSourceType") String dataSourceType, @Param("tenantId") Long tenantId);

    /**
     * 统计数据源数量
     */
    Map<String, Object> countDataSources(@Param("tenantId") Long tenantId);
}