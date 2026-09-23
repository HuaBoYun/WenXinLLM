package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.EnterpriseDashboard;
import com.huabo.cybermonitor.vo.EnterpriseDashboardQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 企业管理驾驶舱 Mapper 接口
 *
 * @author huabo
 * @since 2024-12-12
 */
@Mapper
public interface EnterpriseDashboardMapper extends BaseMapper<EnterpriseDashboard> {

    /**
     * 分页查询企业管理驾驶舱数据
     *
     * @param page 分页参数
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<EnterpriseDashboard> selectEnterpriseDashboardPage(Page<EnterpriseDashboard> page, @Param("queryVo") EnterpriseDashboardQueryVo queryVo);

    /**
     * 查询企业管理驾驶舱列表
     *
     * @param queryVo 查询条件
     * @return 企业管理驾驶舱列表
     */
    List<EnterpriseDashboard> selectEnterpriseDashboardList(@Param("queryVo") EnterpriseDashboardQueryVo queryVo);

    /**
     * 根据企业ID获取最新的驾驶舱数据
     *
     * @param enterpriseId 企业ID
     * @return 企业管理驾驶舱数据
     */
    EnterpriseDashboard selectLatestByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业概览统计数据
     *
     * @param enterpriseId 企业ID
     * @return 概览统计数据
     */
    Map<String, Object> selectEnterpriseOverview(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业关键指标数据
     *
     * @param enterpriseId 企业ID
     * @return 关键指标数据
     */
    Map<String, Object> selectKeyIndicators(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业业务状态监控数据
     *
     * @param enterpriseId 企业ID
     * @return 业务状态监控数据
     */
    Map<String, Object> selectBusinessStatus(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业风险监控数据
     *
     * @param enterpriseId 企业ID
     * @return 风险监控数据
     */
    Map<String, Object> selectRiskMonitoring(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业数据报送状态
     *
     * @param enterpriseId 企业ID
     * @return 数据报送状态
     */
    Map<String, Object> selectDataSubmissionStatus(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业财务指标趋势数据
     *
     * @param enterpriseId 企业ID
     * @param months 月份数
     * @return 财务指标趋势数据
     */
    List<Map<String, Object>> selectFinancialTrend(@Param("enterpriseId") String enterpriseId, @Param("months") Integer months);

    /**
     * 获取企业业务分布数据
     *
     * @param enterpriseId 企业ID
     * @return 业务分布数据
     */
    List<Map<String, Object>> selectBusinessDistribution(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业经营计划执行进度
     *
     * @param enterpriseId 企业ID
     * @return 经营计划执行进度
     */
    List<Map<String, Object>> selectOperatingPlanExecution(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业预算执行进度
     *
     * @param enterpriseId 企业ID
     * @return 预算执行进度
     */
    List<Map<String, Object>> selectBudgetExecution(@Param("enterpriseId") String enterpriseId);

    /**
     * 刷新企业驾驶舱数据
     *
     * @param enterpriseId 企业ID
     * @return 影响行数
     */
    int refreshDashboardData(@Param("enterpriseId") String enterpriseId);

    /**
     * 获取企业驾驶舱配置
     *
     * @param enterpriseId 企业ID
     * @return 配置数据
     */
    Map<String, Object> selectDashboardConfig(@Param("enterpriseId") String enterpriseId);

    /**
     * 保存企业驾驶舱配置
     *
     * @param enterpriseId 企业ID
     * @param configData 配置数据
     * @return 影响行数
     */
    int saveDashboardConfig(@Param("enterpriseId") String enterpriseId, @Param("configData") Map<String, Object> configData);

    /**
     * 统计企业驾驶舱数据
     *
     * @param queryVo 查询条件
     * @return 统计结果
     */
    Map<String, Object> selectStatistics(@Param("queryVo") EnterpriseDashboardQueryVo queryVo);

    /**
     * 批量删除企业管理驾驶舱数据
     *
     * @param dashboardIds 驾驶舱ID列表
     * @return 影响行数
     */
    int deleteBatchByIds(@Param("dashboardIds") List<String> dashboardIds);

    /**
     * 根据企业ID删除驾驶舱数据
     *
     * @param enterpriseId 企业ID
     * @return 影响行数
     */
    int deleteByEnterpriseId(@Param("enterpriseId") String enterpriseId);
}
