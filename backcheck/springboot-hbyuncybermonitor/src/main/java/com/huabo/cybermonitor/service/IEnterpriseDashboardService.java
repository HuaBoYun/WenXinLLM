package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.EnterpriseDashboard;
import com.huabo.cybermonitor.vo.EnterpriseDashboardQueryVo;

import java.util.List;
import java.util.Map;

/**
 * 企业管理驾驶舱服务接口
 *
 * @author huabo
 * @since 2024-12-12
 */
public interface IEnterpriseDashboardService extends IService<EnterpriseDashboard> {

    /**
     * 分页查询企业管理驾驶舱数据
     *
     * @param queryVo 查询条件
     * @return 分页结果
     */
    IPage<EnterpriseDashboard> selectEnterpriseDashboardPage(EnterpriseDashboardQueryVo queryVo);

    /**
     * 查询企业管理驾驶舱列表
     *
     * @param queryVo 查询条件
     * @return 企业管理驾驶舱列表
     */
    List<EnterpriseDashboard> selectEnterpriseDashboardList(EnterpriseDashboardQueryVo queryVo);

    /**
     * 根据企业ID获取最新的驾驶舱数据
     *
     * @param enterpriseId 企业ID
     * @return 企业管理驾驶舱数据
     */
    EnterpriseDashboard getLatestByEnterpriseId(String enterpriseId);

    /**
     * 获取企业驾驶舱完整数据
     *
     * @param enterpriseId 企业ID
     * @return 驾驶舱完整数据
     */
    Map<String, Object> getEnterpriseDashboard(String enterpriseId);

    /**
     * 获取企业概览数据
     *
     * @param enterpriseId 企业ID
     * @return 企业概览数据
     */
    Map<String, Object> getEnterpriseOverview(String enterpriseId);

    /**
     * 获取企业关键指标数据
     *
     * @param enterpriseId 企业ID
     * @return 关键指标数据
     */
    Map<String, Object> getKeyIndicators(String enterpriseId);

    /**
     * 获取企业业务状态监控数据
     *
     * @param enterpriseId 企业ID
     * @return 业务状态监控数据
     */
    Map<String, Object> getBusinessStatus(String enterpriseId);

    /**
     * 获取企业风险监控数据
     *
     * @param enterpriseId 企业ID
     * @return 风险监控数据
     */
    Map<String, Object> getRiskMonitoring(String enterpriseId);

    /**
     * 获取企业数据报送状态
     *
     * @param enterpriseId 企业ID
     * @return 数据报送状态
     */
    Map<String, Object> getDataSubmissionStatus(String enterpriseId);

    /**
     * 获取企业财务指标趋势数据
     *
     * @param enterpriseId 企业ID
     * @param months 月份数
     * @return 财务指标趋势数据
     */
    List<Map<String, Object>> getFinancialTrend(String enterpriseId, Integer months);

    /**
     * 获取企业业务分布数据
     *
     * @param enterpriseId 企业ID
     * @return 业务分布数据
     */
    List<Map<String, Object>> getBusinessDistribution(String enterpriseId);

    /**
     * 获取企业经营计划执行进度
     *
     * @param enterpriseId 企业ID
     * @return 经营计划执行进度
     */
    List<Map<String, Object>> getOperatingPlanExecution(String enterpriseId);

    /**
     * 获取企业预算执行进度
     *
     * @param enterpriseId 企业ID
     * @return 预算执行进度
     */
    List<Map<String, Object>> getBudgetExecution(String enterpriseId);

    /**
     * 刷新企业驾驶舱数据
     *
     * @param enterpriseId 企业ID
     * @return 是否成功
     */
    boolean refreshDashboardData(String enterpriseId);

    /**
     * 导出企业驾驶舱报告
     *
     * @param enterpriseId 企业ID
     * @param reportType 报告类型
     * @return 报告文件路径
     */
    String exportDashboardReport(String enterpriseId, String reportType);

    /**
     * 导出企业驾驶舱报告（返回字节数组用于直接下载）
     *
     * @param enterpriseId 企业ID
     * @param reportType 报告类型
     * @return 报告文件字节数组
     */
    byte[] exportDashboardReportAsBytes(String enterpriseId, String reportType);

    /**
     * 获取企业驾驶舱配置
     *
     * @param enterpriseId 企业ID
     * @return 配置数据
     */
    Map<String, Object> getDashboardConfig(String enterpriseId);

    /**
     * 保存企业驾驶舱配置
     *
     * @param enterpriseId 企业ID
     * @param configData 配置数据
     * @return 是否成功
     */
    boolean saveDashboardConfig(String enterpriseId, Map<String, Object> configData);

    /**
     * 新增企业管理驾驶舱数据
     *
     * @param enterpriseDashboard 企业管理驾驶舱数据
     * @return 是否成功
     */
    boolean addEnterpriseDashboard(EnterpriseDashboard enterpriseDashboard);

    /**
     * 更新企业管理驾驶舱数据
     *
     * @param enterpriseDashboard 企业管理驾驶舱数据
     * @return 是否成功
     */
    boolean updateEnterpriseDashboard(EnterpriseDashboard enterpriseDashboard);

    /**
     * 删除企业管理驾驶舱数据
     *
     * @param dashboardId 驾驶舱ID
     * @return 是否成功
     */
    boolean deleteEnterpriseDashboard(String dashboardId);

    /**
     * 批量删除企业管理驾驶舱数据
     *
     * @param dashboardIds 驾驶舱ID列表
     * @return 是否成功
     */
    boolean deleteBatchEnterpriseDashboard(List<String> dashboardIds);

    /**
     * 根据企业ID删除驾驶舱数据
     *
     * @param enterpriseId 企业ID
     * @return 是否成功
     */
    boolean deleteByEnterpriseId(String enterpriseId);

    /**
     * 统计企业驾驶舱数据
     *
     * @param queryVo 查询条件
     * @return 统计结果
     */
    Map<String, Object> getStatistics(EnterpriseDashboardQueryVo queryVo);

    /**
     * 验证企业驾驶舱数据
     *
     * @param enterpriseDashboard 企业管理驾驶舱数据
     * @return 验证结果
     */
    Map<String, Object> validateDashboardData(EnterpriseDashboard enterpriseDashboard);

    /**
     * 生成企业驾驶舱数据
     *
     * @param enterpriseId 企业ID
     * @param statisticsPeriod 统计周期
     * @return 是否成功
     */
    boolean generateDashboardData(String enterpriseId, String statisticsPeriod);
}
