package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.BidTemplate;
import com.huabo.contract.vo.BidTemplateQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 标书模板表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface BidTemplateService extends IService<BidTemplate> {

    /**
     * 分页查询标书模板列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<BidTemplate> getBidTemplateList(BidTemplateQueryParam param);

    /**
     * 保存标书模板（新增或修改）
     * 
     * @param bidTemplate 标书模板
     * @return 保存结果
     */
    boolean saveBidTemplate(BidTemplate bidTemplate);

    /**
     * 根据ID获取标书模板详情
     * 
     * @param id 主键ID
     * @return 标书模板详情
     */
    BidTemplate getBidTemplateById(Long id);

    /**
     * 根据模板编号获取标书模板
     * 
     * @param templateNo 模板编号
     * @return 标书模板
     */
    BidTemplate getBidTemplateByTemplateNo(String templateNo);

    /**
     * 删除标书模板
     * 
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteBidTemplate(Long id);

    /**
     * 批量删除标书模板
     * 
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteBidTemplate(List<Long> ids);

    /**
     * 检查模板编号是否存在
     * 
     * @param templateNo 模板编号
     * @param excludeId 排除的ID（用于修改时排除自身）
     * @return 是否存在
     */
    boolean existsTemplateNo(String templateNo, Long excludeId);

    /**
     * 根据模板类型获取模板列表
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<BidTemplate> getBidTemplatesByType(Integer templateType);

    /**
     * 更新模板启用状态
     * 
     * @param id 模板ID
     * @param enabled 是否启用
     * @return 更新结果
     */
    boolean updateTemplateStatus(Long id, Integer enabled);

    /**
     * 复制标书模板
     * 
     * @param sourceId 源模板ID
     * @return 新模板
     */
    BidTemplate copyBidTemplate(Long sourceId);

    /**
     * 获取模板统计信息
     * 
     * @return 统计数据
     */
    Map<String, Object> getTemplateStatistics();

    /**
     * 生成模板编号
     * 
     * @return 模板编号
     */
    String generateTemplateNo();

    /**
     * 更新模板使用次数
     * 
     * @param id 模板ID
     * @return 更新结果
     */
    boolean updateUsageCount(Long id);

    /**
     * 更新模板下载次数
     * 
     * @param id 模板ID
     * @return 更新结果
     */
    boolean updateDownloadCount(Long id);

    /**
     * 获取热门模板列表
     * 
     * @param limit 限制数量
     * @return 热门模板列表
     */
    List<BidTemplate> getPopularTemplates(Integer limit);

    /**
     * 获取最新模板列表
     * 
     * @param limit 限制数量
     * @return 最新模板列表
     */
    List<BidTemplate> getLatestTemplates(Integer limit);

    /**
     * 搜索标书模板
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 模板列表
     */
    List<BidTemplate> searchBidTemplates(String keyword, Integer limit);

    /**
     * 获取推荐模板列表
     * 
     * @param templateType 模板类型
     * @param limit 限制数量
     * @return 推荐模板列表
     */
    List<BidTemplate> getRecommendedTemplates(Integer templateType, Integer limit);

    /**
     * 更新模板评分
     * 
     * @param id 模板ID
     * @param rating 评分
     * @return 更新结果
     */
    boolean updateTemplateRating(Long id, Double rating);

    /**
     * 获取模板类型分布统计
     * 
     * @return 类型分布统计
     */
    Map<String, Object> getTemplateTypeDistribution();

    /**
     * 获取模板使用情况统计
     * 
     * @return 使用情况统计
     */
    Map<String, Object> getTemplateUsageStatistics();

    /**
     * 检查模板是否可以删除
     * 
     * @param id 模板ID
     * @return 是否可以删除
     */
    boolean canDeleteTemplate(Long id);

    /**
     * 获取模板版本历史
     * 
     * @param templateNo 模板编号
     * @return 版本历史列表
     */
    List<BidTemplate> getTemplateVersionHistory(String templateNo);

    /**
     * 设置默认模板
     * 
     * @param id 模板ID
     * @param templateType 模板类型
     * @return 设置结果
     */
    boolean setDefaultTemplate(Long id, Integer templateType);

    /**
     * 获取默认模板
     * 
     * @param templateType 模板类型
     * @return 默认模板
     */
    BidTemplate getDefaultTemplate(Integer templateType);

    /**
     * 导出模板
     * 
     * @param id 模板ID
     * @return 导出结果
     */
    Map<String, Object> exportTemplate(Long id);

    /**
     * 导入模板
     * 
     * @param templateData 模板数据
     * @return 导入结果
     */
    boolean importTemplate(Map<String, Object> templateData);

    /**
     * 验证模板内容
     * 
     * @param bidTemplate 标书模板
     * @return 验证结果
     */
    Map<String, Object> validateTemplate(BidTemplate bidTemplate);

    /**
     * 获取模板预览信息
     * 
     * @param id 模板ID
     * @return 预览信息
     */
    Map<String, Object> getTemplatePreview(Long id);

    /**
     * 更新模板最后使用时间
     * 
     * @param id 模板ID
     * @return 更新结果
     */
    boolean updateLastUsedDate(Long id);
}
