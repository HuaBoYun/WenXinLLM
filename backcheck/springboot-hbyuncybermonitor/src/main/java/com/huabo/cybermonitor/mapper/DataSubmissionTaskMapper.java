package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.DataSubmissionTask;
import com.huabo.cybermonitor.vo.DataSubmissionTaskQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据报送任务Mapper接口
 *
 * @author huabo
 * @since 2024-12-12
 */
@Mapper
public interface DataSubmissionTaskMapper extends BaseMapper<DataSubmissionTask> {

    /**
     * 分页查询数据报送任务列表
     *
     * @param page 分页参数
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<DataSubmissionTask> selectDataSubmissionTaskPage(Page<DataSubmissionTask> page, @Param("queryVo") DataSubmissionTaskQueryVo queryVo);

    /**
     * 查询数据报送任务列表
     *
     * @param queryVo 查询条件
     * @return 数据报送任务列表
     */
    List<DataSubmissionTask> selectDataSubmissionTaskList(@Param("queryVo") DataSubmissionTaskQueryVo queryVo);

    /**
     * 根据企业ID查询报送任务统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> selectTaskStatisticsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询即将到期的报送任务
     *
     * @param enterpriseId 企业ID
     * @param days 天数
     * @return 即将到期的任务列表
     */
    List<DataSubmissionTask> selectUpcomingTasks(@Param("enterpriseId") String enterpriseId, @Param("days") Integer days);

    /**
     * 查询逾期的报送任务
     *
     * @param enterpriseId 企业ID
     * @return 逾期任务列表
     */
    List<DataSubmissionTask> selectOverdueTasks(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据任务状态统计数量
     *
     * @param enterpriseId 企业ID
     * @return 状态统计
     */
    List<Map<String, Object>> selectTaskCountByStatus(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据优先级统计数量
     *
     * @param enterpriseId 企业ID
     * @return 优先级统计
     */
    List<Map<String, Object>> selectTaskCountByPriority(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据报送类型统计数量
     *
     * @param enterpriseId 企业ID
     * @return 报送类型统计
     */
    List<Map<String, Object>> selectTaskCountBySubmissionType(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询任务完成率趋势
     *
     * @param enterpriseId 企业ID
     * @param months 月份数
     * @return 完成率趋势
     */
    List<Map<String, Object>> selectTaskCompletionTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 查询任务质量评分分布
     *
     * @param enterpriseId 企业ID
     * @return 质量评分分布
     */
    List<Map<String, Object>> selectQualityScoreDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 批量更新任务状态
     *
     * @param taskIds 任务ID列表
     * @param status 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateTaskStatus(@Param("taskIds") List<String> taskIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量删除任务
     *
     * @param taskIds 任务ID列表
     * @param updateBy 更新人
     * @return 删除数量
     */
    int batchDeleteTasks(@Param("taskIds") List<String> taskIds, @Param("updateBy") String updateBy);

    /**
     * 查询任务执行进度统计
     *
     * @param enterpriseId 企业ID
     * @return 进度统计
     */
    Map<String, Object> selectTaskProgressStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询部门任务分布
     *
     * @param enterpriseId 企业ID
     * @return 部门任务分布
     */
    List<Map<String, Object>> selectTaskDistributionByDepartment(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询负责人任务分布
     *
     * @param enterpriseId 企业ID
     * @return 负责人任务分布
     */
    List<Map<String, Object>> selectTaskDistributionByPerson(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询监管部门任务分布
     *
     * @param enterpriseId 企业ID
     * @return 监管部门任务分布
     */
    List<Map<String, Object>> selectTaskDistributionByRegulator(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询任务反馈处理统计
     *
     * @param enterpriseId 企业ID
     * @return 反馈处理统计
     */
    Map<String, Object> selectFeedbackProcessingStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询任务时效性分析
     *
     * @param enterpriseId 企业ID
     * @return 时效性分析
     */
    Map<String, Object> selectTaskTimelinessAnalysis(@Param("enterpriseId") String enterpriseId);
}
