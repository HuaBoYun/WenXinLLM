package com.global.treasurer.service;

import com.global.treasurer.entity.TcPartnerArchive;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 伙伴档案服务接口
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
public interface TcPartnerArchiveService {

    /**
     * 分页查询伙伴档案列表
     * 
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TcPartnerArchive> list(Integer pageNum, Integer pageSize, Map<String, Object> params);

    /**
     * 根据ID查询伙伴档案
     * 
     * @param id 档案ID
     * @return 伙伴档案
     */
    TcPartnerArchive getById(String id);

    /**
     * 根据伙伴编码查询伙伴档案
     * 
     * @param partnerCode 伙伴编码
     * @return 伙伴档案
     */
    TcPartnerArchive getByPartnerCode(String partnerCode);

    /**
     * 新增伙伴档案
     * 
     * @param partnerArchive 伙伴档案
     * @return 操作结果
     */
    boolean save(TcPartnerArchive partnerArchive);

    /**
     * 更新伙伴档案
     * 
     * @param partnerArchive 伙伴档案
     * @return 操作结果
     */
    boolean update(TcPartnerArchive partnerArchive);

    /**
     * 删除伙伴档案
     * 
     * @param id 档案ID
     * @return 操作结果
     */
    boolean delete(String id);

    /**
     * 批量删除伙伴档案
     * 
     * @param ids 档案ID列表
     * @return 操作结果
     */
    boolean batchDelete(List<String> ids);

    /**
     * 根据伙伴类型ID查询伙伴列表
     * 
     * @param partnerTypeId 伙伴类型ID
     * @return 伙伴列表
     */
    List<TcPartnerArchive> getByPartnerTypeId(String partnerTypeId);

    /**
     * 检查伙伴编码是否存在
     * 
     * @param partnerCode 伙伴编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkPartnerCodeExists(String partnerCode, String excludeId);

    /**
     * 检查统一社会信用代码是否存在
     * 
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkCreditCodeExists(String unifiedSocialCreditCode, String excludeId);

    /**
     * 根据合作状态查询伙伴列表
     * 
     * @param cooperationStatus 合作状态
     * @return 伙伴列表
     */
    List<TcPartnerArchive> getByCooperationStatus(String cooperationStatus);

    /**
     * 根据风险等级查询伙伴列表
     * 
     * @param riskLevel 风险等级
     * @return 伙伴列表
     */
    List<TcPartnerArchive> getByRiskLevel(String riskLevel);

    /**
     * 根据信用等级查询伙伴列表
     * 
     * @param creditRating 信用等级
     * @return 伙伴列表
     */
    List<TcPartnerArchive> getByCreditRating(String creditRating);

    /**
     * 批量更新合作状态
     * 
     * @param ids 伙伴ID列表
     * @param cooperationStatus 合作状态
     * @param updateUser 更新人
     * @return 操作结果
     */
    boolean batchUpdateCooperationStatus(List<String> ids, String cooperationStatus, String updateUser);

    /**
     * 批量更新风险等级
     * 
     * @param ids 伙伴ID列表
     * @param riskLevel 风险等级
     * @param updateUser 更新人
     * @return 操作结果
     */
    boolean batchUpdateRiskLevel(List<String> ids, String riskLevel, String updateUser);

    /**
     * 查询伙伴统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getPartnerStatistics();

    /**
     * 根据行业分类查询伙伴列表
     * 
     * @param industryClassification 行业分类
     * @return 伙伴列表
     */
    List<TcPartnerArchive> getByIndustryClassification(String industryClassification);

    /**
     * 根据企业规模查询伙伴列表
     * 
     * @param enterpriseScale 企业规模
     * @return 伙伴列表
     */
    List<TcPartnerArchive> getByEnterpriseScale(String enterpriseScale);

    /**
     * 查询即将到期的合作伙伴
     * 
     * @param days 天数
     * @return 伙伴列表
     */
    List<TcPartnerArchive> getExpiringPartners(Integer days);

    /**
     * 模糊查询伙伴信息
     * 
     * @param keyword 关键词
     * @return 伙伴列表
     */
    List<TcPartnerArchive> searchByKeyword(String keyword);

    /**
     * 导入伙伴档案
     * 
     * @param partnerList 伙伴列表
     * @return 导入结果
     */
    Map<String, Object> importPartners(List<TcPartnerArchive> partnerList);

    /**
     * 导出伙伴档案
     * 
     * @param params 查询参数
     * @return 导出数据
     */
    List<TcPartnerArchive> exportPartners(Map<String, Object> params);

    /**
     * 验证伙伴档案信息
     * 
     * @param partnerArchive 伙伴档案
     * @return 验证结果
     */
    Map<String, Object> validatePartnerInfo(TcPartnerArchive partnerArchive);

    /**
     * 同步伙伴信息
     * 
     * @param partnerId 伙伴ID
     * @return 同步结果
     */
    Map<String, Object> syncPartnerInfo(String partnerId);
}
