package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetModelVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算模型版本Mapper接口
 * 
 * @description 预算模型版本数据访问层
 * @author AI Agent
 * @date 2026-01-30
 */
@Mapper
public interface BudgetModelVersionMapper extends BaseMapper<BudgetModelVersion> {

    /**
     * 根据模型ID查询版本列表
     * 
     * @param modelId 模型ID
     * @param companyId 公司ID
     * @return 版本列表
     */
    List<BudgetModelVersion> selectByModelId(@Param("modelId") String modelId,
                                              @Param("companyId") String companyId);

    /**
     * 根据版本号查询
     * 
     * @param versionNo 版本号
     * @param modelId 模型ID
     * @param companyId 公司ID
     * @return 版本信息
     */
    BudgetModelVersion selectByVersionNo(@Param("versionNo") String versionNo,
                                          @Param("modelId") String modelId,
                                          @Param("companyId") String companyId);

    /**
     * 查询当前版本
     * 
     * @param modelId 模型ID
     * @param companyId 公司ID
     * @return 当前版本信息
     */
    BudgetModelVersion selectCurrentVersion(@Param("modelId") String modelId,
                                             @Param("companyId") String companyId);

    /**
     * 查询已发布版本列表
     * 
     * @param modelId 模型ID
     * @param companyId 公司ID
     * @return 已发布版本列表
     */
    List<BudgetModelVersion> selectPublishedVersions(@Param("modelId") String modelId,
                                                      @Param("companyId") String companyId);

    /**
     * 查询版本历史（分页）
     * 
     * @param modelId 模型ID
     * @param companyId 公司ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 版本历史列表
     */
    List<Map<String, Object>> selectVersionHistory(@Param("modelId") String modelId,
                                                     @Param("companyId") String companyId,
                                                     @Param("offset") Integer offset,
                                                     @Param("limit") Integer limit);

    /**
     * 统计版本数量
     * 
     * @param modelId 模型ID
     * @param companyId 公司ID
     * @return 版本数量
     */
    int countByModelId(@Param("modelId") String modelId,
                       @Param("companyId") String companyId);

    /**
     * 更新当前版本标记
     * 
     * @param modelId 模型ID
     * @param versionId 版本ID
     * @param companyId 公司ID
     * @return 影响行数
     */
    int updateCurrentVersion(@Param("modelId") String modelId,
                             @Param("versionId") String versionId,
                             @Param("companyId") String companyId);

    /**
     * 清除当前版本标记
     * 
     * @param modelId 模型ID
     * @param companyId 公司ID
     * @return 影响行数
     */
    int clearCurrentVersion(@Param("modelId") String modelId,
                            @Param("companyId") String companyId);

    /**
     * 批量删除版本
     * 
     * @param ids ID列表
     * @param companyId 公司ID
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<String> ids, @Param("companyId") String companyId);

    /**
     * 查询最新版本号
     * 
     * @param modelId 模型ID
     * @param companyId 公司ID
     * @return 最新版本号
     */
    String selectLatestVersionNo(@Param("modelId") String modelId,
                                  @Param("companyId") String companyId);
}

