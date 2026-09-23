package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算模型Mapper接口
 * 
 * @description 预算模型数据访问层
 * @author AI Assistant
 * @date 2025-12-31
 */
@Mapper
public interface BudgetModelMapper extends BaseMapper<BudgetModel> {

    /**
     * 根据模型编码查询
     * 
     * @param modelCode 模型编码
     * @param companyId 公司ID
     * @return 模型信息
     */
    BudgetModel selectByCode(@Param("modelCode") String modelCode,
                              @Param("companyId") String companyId);

    /**
     * 分页查询模型列表
     * 
     * @param params 查询参数
     * @return 模型列表
     */
    List<BudgetModel> selectPageList(@Param("params") Map<String, Object> params);

    /**
     * 统计模型数量
     * 
     * @param params 查询参数
     * @return 数量
     */
    int countByParams(@Param("params") Map<String, Object> params);

    /**
     * 查询模型统计数据
     * 
     * @param companyId 公司ID
     * @return 统计数据
     */
    Map<String, Object> selectModelStats(@Param("companyId") String companyId);

    /**
     * 查询模型版本列表
     * 
     * @param modelId 模型ID
     * @return 版本列表
     */
    List<Map<String, Object>> selectModelVersions(@Param("modelId") String modelId);

    /**
     * 复制模型
     * 
     * @param sourceModelId 源模型ID
     * @param targetModel 目标模型
     * @return 影响行数
     */
    int copyModel(@Param("sourceModelId") String sourceModelId,
                  @Param("targetModel") BudgetModel targetModel);

    /**
     * 批量删除模型
     * 
     * @param ids ID列表
     * @param companyId 公司ID
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<String> ids, @Param("companyId") String companyId);
}

