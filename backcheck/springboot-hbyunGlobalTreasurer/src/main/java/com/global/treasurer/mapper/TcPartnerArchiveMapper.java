package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcPartnerArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 伙伴档案Mapper接口
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
@Mapper
public interface TcPartnerArchiveMapper extends BaseMapper<TcPartnerArchive> {

    /**
     * 根据伙伴类型ID查询伙伴列表
     * 
     * @param partnerTypeId 伙伴类型ID
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByPartnerTypeId(@Param("partnerTypeId") String partnerTypeId);

    /**
     * 根据伙伴编码查询伙伴信息
     * 
     * @param partnerCode 伙伴编码
     * @return 伙伴信息
     */
    TcPartnerArchive selectByPartnerCode(@Param("partnerCode") String partnerCode);

    /**
     * 检查伙伴编码是否存在
     * 
     * @param partnerCode 伙伴编码
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    int checkPartnerCodeExists(@Param("partnerCode") String partnerCode, @Param("excludeId") String excludeId);

    /**
     * 检查统一社会信用代码是否存在
     * 
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    int checkCreditCodeExists(@Param("unifiedSocialCreditCode") String unifiedSocialCreditCode, @Param("excludeId") String excludeId);

    /**
     * 根据合作状态查询伙伴列表
     * 
     * @param cooperationStatus 合作状态
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByCooperationStatus(@Param("cooperationStatus") String cooperationStatus);

    /**
     * 根据风险等级查询伙伴列表
     * 
     * @param riskLevel 风险等级
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByRiskLevel(@Param("riskLevel") String riskLevel);

    /**
     * 根据信用等级查询伙伴列表
     * 
     * @param creditRating 信用等级
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByCreditRating(@Param("creditRating") String creditRating);

    /**
     * 多条件查询伙伴列表
     * 
     * @param params 查询参数
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByMultipleConditions(@Param("params") Map<String, Object> params);

    /**
     * 批量更新合作状态
     * 
     * @param ids 伙伴ID列表
     * @param cooperationStatus 合作状态
     * @param updateUser 更新人
     * @return 更新数量
     */
    int batchUpdateCooperationStatus(@Param("ids") List<String> ids, @Param("cooperationStatus") String cooperationStatus, @Param("updateUser") String updateUser);

    /**
     * 批量更新风险等级
     * 
     * @param ids 伙伴ID列表
     * @param riskLevel 风险等级
     * @param updateUser 更新人
     * @return 更新数量
     */
    int batchUpdateRiskLevel(@Param("ids") List<String> ids, @Param("riskLevel") String riskLevel, @Param("updateUser") String updateUser);

    /**
     * 查询伙伴统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> selectPartnerStatistics();

    /**
     * 根据行业分类查询伙伴列表
     * 
     * @param industryClassification 行业分类
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByIndustryClassification(@Param("industryClassification") String industryClassification);

    /**
     * 根据企业规模查询伙伴列表
     * 
     * @param enterpriseScale 企业规模
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByEnterpriseScale(@Param("enterpriseScale") String enterpriseScale);

    /**
     * 查询即将到期的合作伙伴
     * 
     * @param days 天数
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectExpiringPartners(@Param("days") Integer days);

    /**
     * 模糊查询伙伴信息
     *
     * @param keyword 关键词
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByKeyword(@Param("keyword") String keyword);

    /**
     * 根据参数查询伙伴列表
     *
     * @param params 查询参数
     * @return 伙伴列表
     */
    List<TcPartnerArchive> selectByParams(@Param("params") Map<String, Object> params);

    /**
     * 搜索伙伴信息（关键词）
     *
     * @param keyword 关键词
     * @return 伙伴列表
     */
    List<TcPartnerArchive> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 根据ID更新（忽略乐观锁，只根据ID更新）
     *
     * @param partnerArchive 伙伴档案信息
     * @return 更新数量
     */
    int updateByIdIgnoreVersion(TcPartnerArchive partnerArchive);

    /**
     * 根据ID删除（忽略乐观锁，只根据ID删除）
     *
     * @param id 伙伴档案ID
     * @return 删除数量
     */
    int deleteByIdIgnoreVersion(String id);
}
