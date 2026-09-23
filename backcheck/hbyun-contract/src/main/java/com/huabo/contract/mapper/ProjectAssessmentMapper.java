package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.ProjectAssessment;
import com.huabo.contract.vo.AssessmentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 项目考核记录Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface ProjectAssessmentMapper extends BaseMapper<ProjectAssessment> {

    /**
     * 分页查询考核记录
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<ProjectAssessment> selectAssessmentPage(Page<ProjectAssessment> page, @Param("param") AssessmentQueryParam param);

    /**
     * 根据项目ID和期间查询考核记录
     *
     * @param projectId 项目ID
     * @param assessmentPeriod 考核期间
     * @return 考核记录
     */
    @Select("SELECT * FROM project_assessment WHERE project_id = #{projectId} AND assessment_period = #{assessmentPeriod}")
    ProjectAssessment selectByProjectIdAndPeriod(@Param("projectId") String projectId, 
                                               @Param("assessmentPeriod") String assessmentPeriod);

    /**
     * 查询项目考核历史记录
     *
     * @param projectId 项目ID
     * @return 考核记录列表
     */
    @Select("SELECT * FROM project_assessment WHERE project_id = #{projectId} ORDER BY assessment_date DESC")
    List<ProjectAssessment> selectHistoryByProjectId(@Param("projectId") String projectId);

    /**
     * 查询考核统计概览
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param deptId 部门ID
     * @return 统计数据
     */
    Map<String, Object> selectAssessmentStatistics(@Param("startDate") String startDate, 
                                                  @Param("endDate") String endDate, 
                                                  @Param("deptId") Long deptId);

    /**
     * 查询项目考核排名
     *
     * @param assessmentPeriod 考核期间
     * @param assessmentType 考核类型
     * @param limit 返回数量
     * @return 排名列表
     */
    List<Map<String, Object>> selectAssessmentRanking(@Param("assessmentPeriod") String assessmentPeriod, 
                                                     @Param("assessmentType") Integer assessmentType, 
                                                     @Param("limit") Integer limit);

    /**
     * 查询考核等级分布
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 等级分布
     */
    List<Map<String, Object>> selectLevelDistribution(@Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate);

    /**
     * 查询考核趋势数据
     *
     * @param projectId 项目ID
     * @param months 月份数
     * @return 趋势数据
     */
    List<Map<String, Object>> selectAssessmentTrend(@Param("projectId") String projectId, 
                                                   @Param("months") Integer months);

    /**
     * 检查考核记录是否存在
     *
     * @param projectId 项目ID
     * @param assessmentPeriod 考核期间
     * @param assessmentType 考核类型
     * @param excludeId 排除的ID
     * @return 数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM project_assessment " +
            "WHERE project_id = #{projectId} " +
            "AND assessment_period = #{assessmentPeriod} " +
            "AND assessment_type = #{assessmentType} " +
            "<if test='excludeId != null'> AND id != #{excludeId} </if>" +
            "</script>")
    int countExistingAssessment(@Param("projectId") String projectId, 
                              @Param("assessmentPeriod") String assessmentPeriod, 
                              @Param("assessmentType") Integer assessmentType, 
                              @Param("excludeId") Long excludeId);
}
