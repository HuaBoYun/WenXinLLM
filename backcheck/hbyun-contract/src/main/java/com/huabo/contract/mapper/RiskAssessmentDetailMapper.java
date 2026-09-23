package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.RiskAssessmentDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 风险评估明细表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface RiskAssessmentDetailMapper extends BaseMapper<RiskAssessmentDetail> {

    /**
     * 根据评估ID查询明细列表
     * 
     * @param assessmentId 评估ID
     * @return 明细列表
     */
    List<RiskAssessmentDetail> selectByAssessmentId(@Param("assessmentId") Long assessmentId);

    /**
     * 根据评估ID删除明细
     * 
     * @param assessmentId 评估ID
     * @return 删除数量
     */
    int deleteByAssessmentId(@Param("assessmentId") Long assessmentId);

    /**
     * 批量插入明细
     * 
     * @param detailList 明细列表
     * @return 插入数量
     */
    int batchInsert(@Param("detailList") List<RiskAssessmentDetail> detailList);

    /**
     * 计算评估总分
     * 
     * @param assessmentId 评估ID
     * @return 总分
     */
    BigDecimal calculateTotalScore(@Param("assessmentId") Long assessmentId);

    /**
     * 获取最高风险等级
     * 
     * @param assessmentId 评估ID
     * @return 最高风险等级
     */
    Integer getMaxRiskLevel(@Param("assessmentId") Long assessmentId);

    /**
     * 根据风险类别查询明细
     * 
     * @param assessmentId 评估ID
     * @param riskCategory 风险类别
     * @return 明细列表
     */
    List<RiskAssessmentDetail> selectByRiskCategory(@Param("assessmentId") Long assessmentId, 
                                                   @Param("riskCategory") Integer riskCategory);

    /**
     * 获取高风险项目明细
     * 
     * @param assessmentId 评估ID
     * @param minRiskScore 最小风险得分
     * @return 高风险项目明细
     */
    List<RiskAssessmentDetail> selectHighRiskItems(@Param("assessmentId") Long assessmentId, 
                                                  @Param("minRiskScore") BigDecimal minRiskScore);

    /**
     * 统计风险类别分布
     * 
     * @param assessmentId 评估ID
     * @return 风险类别分布
     */
    List<Map<String, Object>> selectRiskCategoryDistribution(@Param("assessmentId") Long assessmentId);

    /**
     * 统计风险影响分布
     * 
     * @param assessmentId 评估ID
     * @return 风险影响分布
     */
    List<Map<String, Object>> selectRiskImpactDistribution(@Param("assessmentId") Long assessmentId);

    /**
     * 获取需要缓解措施的风险项目
     * 
     * @param assessmentId 评估ID
     * @return 需要缓解措施的风险项目
     */
    List<RiskAssessmentDetail> selectItemsNeedMitigation(@Param("assessmentId") Long assessmentId);

    /**
     * 更新风险得分
     * 
     * @param id 明细ID
     * @param riskScore 风险得分
     * @return 更新数量
     */
    int updateRiskScore(@Param("id") Long id, @Param("riskScore") BigDecimal riskScore);

    /**
     * 批量更新风险得分
     *
     * @param assessmentId 评估ID
     * @return 更新数量
     */
    int batchUpdateRiskScore(@Param("assessmentId") Long assessmentId);

    /**
     * 根据评估ID查询高风险明细
     *
     * @param assessmentId 评估ID
     * @return 高风险明细列表
     */
    List<RiskAssessmentDetail> selectHighRiskByAssessmentId(@Param("assessmentId") Long assessmentId);

    /**
     * 根据相对方ID查询风险类别分布
     *
     * @param counterpartId 相对方ID
     * @return 风险类别分布
     */
    List<Map<String, Object>> selectRiskCategoriesByCounterpart(@Param("counterpartId") Long counterpartId);
}
