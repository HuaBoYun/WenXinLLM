package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.CostModelEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 成本模型Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
public interface CostModelMapper extends BaseMapper<CostModelEntity> {

    /**
     * 分页查询成本模型列表
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param modelCode 模型编码
     * @param modelName 模型名称
     * @param modelType 模型类型
     * @param industry 适用行业
     * @param isEnabled 启用状态
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 模型列表
     */
    List<Map<String, Object>> selectModelListWithPagination(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("modelCode") String modelCode,
            @Param("modelName") String modelName,
            @Param("modelType") String modelType,
            @Param("industry") String industry,
            @Param("isEnabled") Integer isEnabled,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询总记录数
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param modelCode 模型编码
     * @param modelName 模型名称
     * @param modelType 模型类型
     * @param industry 适用行业
     * @param isEnabled 启用状态
     * @return 总记录数
     */
    int countModelList(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("modelCode") String modelCode,
            @Param("modelName") String modelName,
            @Param("modelType") String modelType,
            @Param("industry") String industry,
            @Param("isEnabled") Integer isEnabled
    );

    /**
     * 根据模型编码查询模型
     *
     * @param modelCode 模型编码
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 模型信息
     */
    Map<String, Object> selectByModelCode(
            @Param("modelCode") String modelCode,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 批量更新模型状态
     *
     * @param modelIds 模型ID列表
     * @param isEnabled 启用状态
     * @return 更新数量
     */
    int batchUpdateStatus(
            @Param("modelIds") List<String> modelIds,
            @Param("isEnabled") Integer isEnabled
    );

    /**
     * 检查模型编码是否存在
     *
     * @param modelCode 模型编码
     * @param modelId 模型ID（更新时排除自己）
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 数量
     */
    int checkModelCodeExists(
            @Param("modelCode") String modelCode,
            @Param("modelId") String modelId,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 获取模型选项列表
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 模型选项列表
     */
    List<Map<String, Object>> selectModelOptions(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );
}

