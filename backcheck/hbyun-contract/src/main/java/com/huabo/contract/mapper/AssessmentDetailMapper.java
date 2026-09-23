package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.AssessmentDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 考核明细Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface AssessmentDetailMapper extends BaseMapper<AssessmentDetail> {

    /**
     * 根据考核ID查询明细列表
     *
     * @param assessmentId 考核ID
     * @return 明细列表
     */
    @Select("SELECT ad.*, ai.indicator_code, ai.indicator_name, ai.indicator_type, ai.unit " +
            "FROM assessment_detail ad " +
            "LEFT JOIN assessment_indicator ai ON ad.indicator_id = ai.id " +
            "WHERE ad.assessment_id = #{assessmentId} " +
            "ORDER BY ai.sort_order ASC, ad.id ASC")
    List<AssessmentDetail> selectDetailsByAssessmentId(@Param("assessmentId") Long assessmentId);

    /**
     * 根据考核ID删除明细
     *
     * @param assessmentId 考核ID
     * @return 删除数量
     */
    @Select("DELETE FROM assessment_detail WHERE assessment_id = #{assessmentId}")
    int deleteByAssessmentId(@Param("assessmentId") Long assessmentId);

    /**
     * 查询指标统计分析数据
     *
     * @param assessmentPeriod 考核期间
     * @param indicatorId 指标ID
     * @return 统计数据
     */
    Map<String, Object> selectIndicatorStatistics(@Param("assessmentPeriod") String assessmentPeriod, 
                                                 @Param("indicatorId") Long indicatorId);

    /**
     * 查询所有指标的统计分析数据
     *
     * @param assessmentPeriod 考核期间
     * @return 统计数据列表
     */
    List<Map<String, Object>> selectAllIndicatorStatistics(@Param("assessmentPeriod") String assessmentPeriod);

    /**
     * 批量插入考核明细
     *
     * @param details 明细列表
     * @return 插入数量
     */
    int batchInsert(@Param("details") List<AssessmentDetail> details);

    /**
     * 查询指标得分分布
     *
     * @param indicatorId 指标ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分布数据
     */
    List<Map<String, Object>> selectScoreDistribution(@Param("indicatorId") Long indicatorId, 
                                                     @Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate);

    /**
     * 查询项目各指标得分
     *
     * @param projectId 项目ID
     * @param assessmentPeriod 考核期间
     * @return 指标得分列表
     */
    @Select("SELECT ad.*, ai.indicator_code, ai.indicator_name " +
            "FROM assessment_detail ad " +
            "LEFT JOIN assessment_indicator ai ON ad.indicator_id = ai.id " +
            "LEFT JOIN project_assessment pa ON ad.assessment_id = pa.id " +
            "WHERE pa.project_id = #{projectId} AND pa.assessment_period = #{assessmentPeriod} " +
            "ORDER BY ai.sort_order ASC")
    List<AssessmentDetail> selectProjectIndicatorScores(@Param("projectId") String projectId, 
                                                       @Param("assessmentPeriod") String assessmentPeriod);
}
