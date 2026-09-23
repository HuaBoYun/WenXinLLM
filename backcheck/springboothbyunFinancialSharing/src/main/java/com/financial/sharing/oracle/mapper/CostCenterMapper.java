package com.financial.sharing.oracle.mapper;

import com.financial.sharing.oracle.entity.CostCenterEntity;
import com.financial.sharing.vo.param.CostCenterQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 成本中心Mapper接口
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface CostCenterMapper {

    /**
     * 分页查询成本中心列表（使用原生分页）
     */
    List<Map<String, Object>> selectCostCenterListWithPagination(@Param("param") CostCenterQueryParam param, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询成本中心列表总数
     */
    int countCostCenterList(@Param("param") CostCenterQueryParam param);

    /**
     * 根据ID查询成本中心详情
     */
    Map<String, Object> selectCostCenterDetail(@Param("centerId") String centerId);

    /**
     * 根据ID查询成本中心详情（用于删除操作，不限制删除状态）
     */
    Map<String, Object> selectCostCenterDetailForDelete(@Param("centerId") String centerId);

    /**
     * 查询成本中心选项列表（用于下拉框）
     */
    List<Map<String, Object>> selectCostCenterOptions(@Param("bookId") String bookId, @Param("tenantId") String tenantId);

    /**
     * 检查成本中心编码是否存在
     */
    int checkCenterCodeExists(@Param("centerCode") String centerCode, @Param("centerId") String centerId,
                              @Param("bookId") String bookId, @Param("tenantId") String tenantId);

    /**
     * 查询子成本中心数量
     */
    int countChildCenters(@Param("parentCenterId") String parentCenterId);

    /**
     * 查询所有子成本中心数量（用于删除操作，不限制删除状态）
     */
    int countAllChildCenters(@Param("parentCenterId") String parentCenterId);

    /**
     * 更新父节点的叶子节点标识
     */
    int updateParentLeafFlag(@Param("parentCenterId") String parentCenterId, @Param("isLeaf") Integer isLeaf);

    /**
     * 插入成本中心
     */
    int insertCostCenter(@Param("entity") CostCenterEntity entity);

    /**
     * 更新成本中心
     */
    int updateCostCenter(@Param("entity") CostCenterEntity entity);

    /**
     * 逻辑删除成本中心
     */
    int logicalDeleteById(@Param("centerId") String centerId);

    /**
     * 启用/停用成本中心
     */
    int enableCostCenter(@Param("centerId") String centerId, @Param("isEnabled") Integer isEnabled);

    /**
     * 批量更新成本中心状态
     */
    int batchUpdateStatus(@Param("centerIds") List<String> centerIds, @Param("isEnabled") Integer isEnabled);

    /**
     * 获取成本中心统计概览
     */
    Map<String, Object> selectCostCenterStats(@Param("param") Map<String, Object> param);

    // ==================== 成本预算相关方法 ====================

    /**
     * 分页查询成本预算列表
     */
    List<Map<String, Object>> selectCostBudgetList(@Param("param") Map<String, Object> param);

    /**
     * 插入成本预算
     */
    int insertCostBudget(@Param("param") Map<String, Object> param);

    /**
     * 更新成本预算
     */
    int updateCostBudget(@Param("param") Map<String, Object> param);

    /**
     * 删除成本预算
     */
    int deleteCostBudget(@Param("budgetId") String budgetId);

    /**
     * 查询成本预算统计数据
     */
    Map<String, Object> selectCostBudgetStats(@Param("param") Map<String, Object> param);

    /**
     * 根据ID查询成本预算详情
     */
    Map<String, Object> selectCostBudgetById(@Param("param") Map<String, Object> param);

    /**
     * 更新成本预算状态
     */
    int updateCostBudgetStatus(@Param("param") Map<String, Object> param);

    /**
     * 查询预算报告列表用于导出
     */
    List<Map<String, Object>> selectBudgetReportList(@Param("param") Map<String, Object> param);

    // ==================== 成本控制相关方法 ====================

    /**
     * 分页查询成本控制列表
     */
    List<Map<String, Object>> selectCostControlList(@Param("param") Map<String, Object> param);

    /**
     * 查询成本控制列表总数
     */
    int countCostControlList(@Param("param") Map<String, Object> param);

    /**
     * 插入成本控制规则
     */
    int insertCostControl(@Param("param") Map<String, Object> param);

    /**
     * 更新成本控制规则
     */
    int updateCostControl(@Param("param") Map<String, Object> param);

    /**
     * 根据ID查询成本控制详情
     */
    Map<String, Object> selectCostControlById(@Param("controlId") String controlId);

    /**
     * 单条切换成本控制启用状态
     */
    int updateCostControlStatus(@Param("param") Map<String, Object> param);

    /**
     * 批量切换成本控制启用状态
     */
    int batchUpdateCostControlStatus(@Param("param") Map<String, Object> param);

    /**
     * 查询成本控制统计数据
     */
    Map<String, Object> selectCostControlStats(@Param("param") Map<String, Object> param);

    // ==================== 成本分析相关方法 ====================

    /**
     * 查询成本分析"中心维度汇总"列表（前端 summaryData）
     * 返回字段：centerId / centerName / budgetAmount / actualAmount / variance / varianceRate / efficiency
     */
    List<Map<String, Object>> selectCostAnalysisDataList(@Param("param") Map<String, Object> param);

    /**
     * 查询成本分析"中心维度汇总"总数（一般 = 启用中心数）
     */
    int countCostAnalysisDataList(@Param("param") Map<String, Object> param);

    /**
     * 查询成本分析明细（前端 detailData，按 期间×中心×成本类型）
     */
    List<Map<String, Object>> selectCostAnalysisDetail(@Param("param") Map<String, Object> param);

    /**
     * 查询成本分析明细总数
     */
    int countCostAnalysisDetail(@Param("param") Map<String, Object> param);

    /**
     * 查询成本分析概览（4 张统计卡）
     */
    Map<String, Object> selectCostAnalysisOverview(@Param("param") Map<String, Object> param);

    /**
     * 查询成本结构分析数据（按 COST_CATEGORY 分组的 list，前端饼图直接消费）
     */
    List<Map<String, Object>> selectCostStructureAnalysis(@Param("param") Map<String, Object> param);

    /**
     * 查询成本趋势分析数据
     */
    List<Map<String, Object>> selectCostTrendAnalysis(@Param("param") Map<String, Object> param);

    /**
     * 生成成本分析报告数据
     */
    Map<String, Object> selectCostAnalysisReportData(@Param("param") Map<String, Object> param);
}

