package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.CostCollectionParam;
import com.financial.sharing.vo.result.CostCollectionVO;
import com.financial.sharing.vo.result.CostCollectionStatsVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 成本归集服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
public interface CostCollectionService {

    /**
     * 分页查询成本归集列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<CostCollectionVO>> getCostCollectionList(Map<String, Object> param);

    /**
     * 开始成本归集
     *
     * @param param 归集参数
     * @return 操作结果
     */
    MyJsonBean startCostCollection(CostCollectionParam param);

    /**
     * 审核成本归集
     *
     * @param collectionId 归集ID
     * @param auditData 审核数据
     * @return 操作结果
     */
    MyJsonBean auditCostCollection(Long collectionId, Map<String, Object> auditData);

    /**
     * 取消成本归集
     *
     * @param collectionId 归集ID
     * @return 操作结果
     */
    MyJsonBean cancelCostCollection(Long collectionId);

    /**
     * 获取成本归集统计数据
     *
     * @param period 期间
     * @return 统计数据
     */
    MyJsonBean<CostCollectionStatsVO> getCostCollectionStats(String period);

    /**
     * 获取成本归集详情
     *
     * @param collectionId 归集ID
     * @return 归集详情
     */
    MyJsonBean<CostCollectionVO> getCostCollectionById(Long collectionId);

    /**
     * 批量审核成本归集
     *
     * @param collectionIds 归集ID列表
     * @param auditData 审核数据
     * @return 操作结果
     */
    MyJsonBean batchAuditCostCollection(List<Long> collectionIds, Map<String, Object> auditData);

    /**
     * 批量取消成本归集
     *
     * @param collectionIds 归集ID列表
     * @return 操作结果
     */
    MyJsonBean batchCancelCostCollection(List<Long> collectionIds);

    /**
     * 自动归集成本
     *
     * @param period 归集期间
     * @param costCenterIds 成本中心ID列表
     * @param collectionTypes 归集类型列表
     * @return 操作结果
     */
    MyJsonBean autoCostCollection(String period, List<Long> costCenterIds, List<Integer> collectionTypes);

    /**
     * 重新计算成本归集
     *
     * @param collectionId 归集ID
     * @return 操作结果
     */
    MyJsonBean recalculateCostCollection(Long collectionId);

    /**
     * 导出成本归集结果
     *
     * @param period 归集期间
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param response HTTP响应
     */
    void exportCollectionResult(String period, Long bookId, Long tenantId, HttpServletResponse response);

    // ==================== 新增接口：成本归集规则管理 ====================

    /**
     * 保存或更新成本归集配置
     *
     * @param collectionData 归集配置数据
     * @return 操作结果
     */
    MyJsonBean saveOrUpdateCostCollection(Map<String, Object> collectionData);

    /**
     * 删除成本归集记录
     *
     * @param collectionId 归集ID
     * @return 操作结果
     */
    MyJsonBean deleteCostCollection(String collectionId);

    /**
     * 获取归集规则列表
     *
     * @param centerId 成本中心ID
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 规则列表
     */
    MyJsonBean<List<Map<String, Object>>> getCostCollectionRules(String centerId, String bookId, String tenantId);

    /**
     * 保存归集规则
     *
     * @param ruleData 规则数据
     * @return 操作结果
     */
    MyJsonBean saveCostCollectionRule(Map<String, Object> ruleData);

    /**
     * 批量执行成本归集
     *
     * @param collectionIds 归集ID列表
     * @return 操作结果
     */
    MyJsonBean batchExecuteCostCollection(List<String> collectionIds);
}