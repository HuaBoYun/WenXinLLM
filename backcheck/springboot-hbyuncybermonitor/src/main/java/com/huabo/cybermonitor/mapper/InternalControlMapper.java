package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.InternalControl;
import com.huabo.cybermonitor.vo.InternalControlQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 内控管理Mapper接口
 *
 * @author huabo
 * @since 2024-12-12
 */
@Mapper
public interface InternalControlMapper extends BaseMapper<InternalControl> {

    /**
     * 分页查询内控管理列表
     *
     * @param page 分页参数
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<InternalControl> selectInternalControlPage(Page<InternalControl> page, @Param("queryVo") InternalControlQueryVo queryVo);

    /**
     * 查询内控管理列表
     *
     * @param queryVo 查询条件
     * @return 内控管理列表
     */
    List<InternalControl> selectInternalControlList(@Param("queryVo") InternalControlQueryVo queryVo);

    /**
     * 根据企业ID查询内控统计
     *
     * @param enterpriseId 企业ID
     * @return 统计结果
     */
    Map<String, Object> selectControlStatisticsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询内控制度体系统计
     *
     * @param enterpriseId 企业ID
     * @return 制度体系统计
     */
    Map<String, Object> selectControlSystemStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询内控执行状态统计
     *
     * @param enterpriseId 企业ID
     * @return 执行状态统计
     */
    List<Map<String, Object>> selectExecutionStatusStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询内控评估结果统计
     *
     * @param enterpriseId 企业ID
     * @return 评估结果统计
     */
    Map<String, Object> selectAssessmentResultStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询风险等级分布
     *
     * @param enterpriseId 企业ID
     * @return 风险等级分布
     */
    List<Map<String, Object>> selectRiskLevelDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询内控类型分布
     *
     * @param enterpriseId 企业ID
     * @return 内控类型分布
     */
    List<Map<String, Object>> selectControlTypeDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询部门内控分布
     *
     * @param enterpriseId 企业ID
     * @return 部门内控分布
     */
    List<Map<String, Object>> selectControlDistributionByDepartment(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询内控有效性趋势
     *
     * @param enterpriseId 企业ID
     * @param months 月份数
     * @return 有效性趋势
     */
    List<Map<String, Object>> selectEffectivenessTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 查询需要执行的内控制度
     *
     * @param enterpriseId 企业ID
     * @param days 天数
     * @return 需要执行的制度列表
     */
    List<InternalControl> selectControlsToExecute(@Param("enterpriseId") String enterpriseId, @Param("days") Integer days);

    /**
     * 查询需要评估的内控制度
     *
     * @param enterpriseId 企业ID
     * @param days 天数
     * @return 需要评估的制度列表
     */
    List<InternalControl> selectControlsToAssess(@Param("enterpriseId") String enterpriseId, @Param("days") Integer days);

    /**
     * 查询需要改进的内控制度
     *
     * @param enterpriseId 企业ID
     * @return 需要改进的制度列表
     */
    List<InternalControl> selectControlsToImprove(@Param("enterpriseId") String enterpriseId);

    /**
     * 批量更新内控状态
     *
     * @param controlIds 内控ID列表
     * @param status 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateControlStatus(@Param("controlIds") List<String> controlIds, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 批量执行内控制度
     *
     * @param controlIds 内控ID列表
     * @param executionTime 执行时间
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchExecuteControls(@Param("controlIds") List<String> controlIds, @Param("executionTime") String executionTime, @Param("updateBy") String updateBy);

    /**
     * 查询内控改进统计
     *
     * @param enterpriseId 企业ID
     * @return 改进统计
     */
    Map<String, Object> selectImprovementStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询内控监督统计
     *
     * @param enterpriseId 企业ID
     * @return 监督统计
     */
    Map<String, Object> selectSupervisionStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询内控培训统计
     *
     * @param enterpriseId 企业ID
     * @return 培训统计
     */
    Map<String, Object> selectTrainingStatistics(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询内控制度关联关系
     *
     * @param enterpriseId 企业ID
     * @return 关联关系
     */
    List<Map<String, Object>> selectControlRelationships(@Param("enterpriseId") String enterpriseId);
}
