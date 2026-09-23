package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.BidTemplate;
import com.huabo.contract.vo.BidTemplateQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 标书模板表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface BidTemplateMapper extends BaseMapper<BidTemplate> {

    /**
     * 分页查询标书模板列表
     * 
     * @param param 查询参数
     * @return 标书模板列表
     */
    List<BidTemplate> selectBidTemplateList(@Param("param") BidTemplateQueryParam param);

    /**
     * 根据模板编号查询标书模板
     * 
     * @param templateNo 模板编号
     * @return 标书模板
     */
    BidTemplate selectByTemplateNo(@Param("templateNo") String templateNo);

    /**
     * 根据模板类型查询标书模板列表
     * 
     * @param templateType 模板类型
     * @return 标书模板列表
     */
    List<BidTemplate> selectByTemplateType(@Param("templateType") Integer templateType);

    /**
     * 检查模板编号是否存在
     * 
     * @param templateNo 模板编号
     * @param excludeId 排除的ID
     * @return 存在返回true，不存在返回false
     */
    boolean existsTemplateNo(@Param("templateNo") String templateNo, @Param("excludeId") Long excludeId);

    /**
     * 获取启用的模板列表
     * 
     * @return 启用的模板列表
     */
    List<BidTemplate> selectEnabledTemplates();

    /**
     * 获取默认模板列表
     * 
     * @return 默认模板列表
     */
    List<BidTemplate> selectDefaultTemplates();

    /**
     * 获取常用模板列表
     * 
     * @param minUsageCount 最小使用次数
     * @return 常用模板列表
     */
    List<BidTemplate> selectPopularTemplates(@Param("minUsageCount") Integer minUsageCount);

    /**
     * 获取新模板列表
     * 
     * @param days 天数
     * @return 新模板列表
     */
    List<BidTemplate> selectNewTemplates(@Param("days") Integer days);

    /**
     * 获取长期未使用的模板列表
     * 
     * @param days 天数
     * @return 长期未使用的模板列表
     */
    List<BidTemplate> selectLongTimeUnusedTemplates(@Param("days") Integer days);

    /**
     * 根据适用行业查询模板列表
     * 
     * @param industry 适用行业
     * @return 模板列表
     */
    List<BidTemplate> selectByApplicableIndustry(@Param("industry") String industry);

    /**
     * 根据适用项目类型查询模板列表
     * 
     * @param projectType 适用项目类型
     * @return 模板列表
     */
    List<BidTemplate> selectByApplicableProjectType(@Param("projectType") String projectType);

    /**
     * 批量更新启用状态
     * 
     * @param ids 模板ID列表
     * @param isEnabled 是否启用
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateEnabledStatus(@Param("ids") List<Long> ids, 
                                @Param("isEnabled") Integer isEnabled, 
                                @Param("updateBy") Long updateBy);

    /**
     * 批量更新默认状态
     * 
     * @param ids 模板ID列表
     * @param isDefault 是否默认
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateDefaultStatus(@Param("ids") List<Long> ids, 
                                @Param("isDefault") Integer isDefault, 
                                @Param("updateBy") Long updateBy);

    /**
     * 更新使用次数
     * 
     * @param id 模板ID
     * @return 更新数量
     */
    int updateUsageCount(@Param("id") Long id);

    /**
     * 统计标书模板数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectBidTemplateStatistics(@Param("param") BidTemplateQueryParam param);

    /**
     * 获取模板类型分布统计
     * 
     * @param param 查询参数
     * @return 模板类型分布
     */
    List<Map<String, Object>> selectTemplateTypeDistribution(@Param("param") BidTemplateQueryParam param);

    /**
     * 获取适用行业分布统计
     * 
     * @param param 查询参数
     * @return 适用行业分布
     */
    List<Map<String, Object>> selectApplicableIndustryDistribution(@Param("param") BidTemplateQueryParam param);

    /**
     * 获取文件类型分布统计
     * 
     * @param param 查询参数
     * @return 文件类型分布
     */
    List<Map<String, Object>> selectFileTypeDistribution(@Param("param") BidTemplateQueryParam param);

    /**
     * 获取使用频率统计
     * 
     * @param param 查询参数
     * @return 使用频率统计
     */
    List<Map<String, Object>> selectUsageFrequencyStatistics(@Param("param") BidTemplateQueryParam param);

    /**
     * 模糊搜索标书模板
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 标书模板列表
     */
    List<BidTemplate> searchBidTemplates(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取推荐模板列表
     * 
     * @param templateType 模板类型
     * @param industry 行业
     * @param projectType 项目类型
     * @param limit 限制数量
     * @return 推荐模板列表
     */
    List<BidTemplate> selectRecommendedTemplates(@Param("templateType") Integer templateType,
                                                @Param("industry") String industry,
                                                @Param("projectType") String projectType,
                                                @Param("limit") Integer limit);

    /**
     * 获取相似模板列表
     * 
     * @param templateId 模板ID
     * @param limit 限制数量
     * @return 相似模板列表
     */
    List<BidTemplate> selectSimilarTemplates(@Param("templateId") Long templateId, @Param("limit") Integer limit);

    /**
     * 清除默认状态（设置所有模板为非默认）
     * 
     * @param templateType 模板类型
     * @return 更新数量
     */
    int clearDefaultStatus(@Param("templateType") Integer templateType);

    /**
     * 获取版本历史列表
     * 
     * @param templateNo 模板编号
     * @return 版本历史列表
     */
    List<BidTemplate> selectVersionHistory(@Param("templateNo") String templateNo);

    /**
     * 获取最新版本模板
     * 
     * @param templateNo 模板编号
     * @return 最新版本模板
     */
    BidTemplate selectLatestVersion(@Param("templateNo") String templateNo);
}
