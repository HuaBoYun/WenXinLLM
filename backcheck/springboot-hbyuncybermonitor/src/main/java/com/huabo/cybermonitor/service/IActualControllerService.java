package com.huabo.cybermonitor.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.ActualController;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.ActualControllerQueryVO;

/**
 * 实际控制人服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IActualControllerService extends IService<ActualController> {

    /**
     * 分页查询实际控制人列表
     *
     * @param queryVO 查询参数
     * @return 分页结果
     */
    PageResult<ActualController> getActualControllerList(ActualControllerQueryVO queryVO);

    /**
     * 根据ID获取实际控制人详情
     *
     * @param controllerId 实际控制人ID
     * @return 实际控制人详情
     */
    ActualController getActualControllerById(String controllerId);

    /**
     * 新增实际控制人
     *
     * @param actualController 实际控制人信息
     * @return 是否成功
     */
    boolean addActualController(ActualController actualController);

    /**
     * 更新实际控制人
     *
     * @param actualController 实际控制人信息
     * @return 是否成功
     */
    boolean updateActualController(ActualController actualController);

    /**
     * 删除实际控制人
     *
     * @param controllerId 实际控制人ID
     * @return 是否成功
     */
    boolean deleteActualController(String controllerId);

    /**
     * 批量删除实际控制人
     *
     * @param controllerIds 实际控制人ID列表
     * @return 是否成功
     */
    boolean batchDeleteActualController(List<String> controllerIds);

    /**
     * 根据被控制企业ID查询实际控制人
     *
     * @param controlledEnterpriseId 被控制企业ID
     * @return 实际控制人列表
     */
    List<ActualController> getActualControllerByControlledEnterpriseId(String controlledEnterpriseId);

    /**
     * 根据控制人企业ID查询控制关系
     *
     * @param controllerEnterpriseId 控制人企业ID
     * @return 控制关系列表
     */
    List<ActualController> getActualControllerByControllerEnterpriseId(String controllerEnterpriseId);

    /**
     * 查询最终控制人
     *
     * @param controlledEnterpriseId 被控制企业ID
     * @return 最终控制人列表
     */
    List<ActualController> getUltimateControllers(String controlledEnterpriseId);

    /**
     * 查询一致行动人
     *
     * @param controlledEnterpriseId 被控制企业ID
     * @return 一致行动人列表
     */
    List<ActualController> getConcertedActionControllers(String controlledEnterpriseId);

    /**
     * 按控制人类型查询
     *
     * @param controllerType 控制人类型
     * @return 控制关系列表
     */
    List<ActualController> getActualControllerByControllerType(String controllerType);

    /**
     * 按控制人性质查询
     *
     * @param controllerNature 控制人性质
     * @return 控制关系列表
     */
    List<ActualController> getActualControllerByControllerNature(String controllerNature);

    /**
     * 按控制方式查询
     *
     * @param controlMethod 控制方式
     * @return 控制关系列表
     */
    List<ActualController> getActualControllerByControlMethod(String controlMethod);

    /**
     * 按控制状态查询
     *
     * @param controlStatus 控制状态
     * @return 控制关系列表
     */
    List<ActualController> getActualControllerByControlStatus(String controlStatus);

    /**
     * 按控制风险等级查询
     *
     * @param controlRiskLevel 控制风险等级
     * @return 控制关系列表
     */
    List<ActualController> getActualControllerByControlRiskLevel(String controlRiskLevel);

    /**
     * 查询需要特别监管的控制关系
     *
     * @param needSpecialSupervision 是否需要特别监管
     * @return 控制关系列表
     */
    List<ActualController> getSpecialSupervisionControllers(Boolean needSpecialSupervision);

    /**
     * 按监管关注度查询
     *
     * @param regulatoryAttention 监管关注度
     * @return 控制关系列表
     */
    List<ActualController> getActualControllerByRegulatoryAttention(String regulatoryAttention);

    /**
     * 按确认状态查询
     *
     * @param confirmationStatus 确认状态
     * @return 控制关系列表
     */
    List<ActualController> getActualControllerByConfirmationStatus(String confirmationStatus);

    /**
     * 查询控制链路
     *
     * @param startEnterpriseId 起始企业ID
     * @param endEnterpriseId 终止企业ID
     * @return 控制链路列表
     */
    List<ActualController> getControlChain(String startEnterpriseId, String endEnterpriseId);

    /**
     * 查询控制层级结构
     *
     * @param controllerEnterpriseId 控制人企业ID
     * @param maxLevel 最大层级
     * @return 控制层级结构
     */
    List<ActualController> getControlHierarchy(String controllerEnterpriseId, Integer maxLevel);

    /**
     * 按控制人类型统计
     *
     * @return 类型统计
     */
    List<Map<String, Object>> getControllerTypeStatistics();

    /**
     * 按控制人性质统计
     *
     * @return 性质统计
     */
    List<Map<String, Object>> getControllerNatureStatistics();

    /**
     * 按控制方式统计
     *
     * @return 方式统计
     */
    List<Map<String, Object>> getControlMethodStatistics();

    /**
     * 按控制状态统计
     *
     * @return 状态统计
     */
    List<Map<String, Object>> getControlStatusStatistics();

    /**
     * 按控制稳定性统计
     *
     * @return 稳定性统计
     */
    List<Map<String, Object>> getControlStabilityStatistics();

    /**
     * 按控制风险等级统计
     *
     * @return 风险等级统计
     */
    List<Map<String, Object>> getControlRiskLevelStatistics();

    /**
     * 按监管关注度统计
     *
     * @return 关注度统计
     */
    List<Map<String, Object>> getRegulatoryAttentionStatistics();

    /**
     * 查询控制关系变化趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变化趋势数据
     */
    List<Map<String, Object>> getControlRelationTrend(String startDate, String endDate);

    /**
     * 查询控制集中度分析
     *
     * @return 控制集中度数据
     */
    List<Map<String, Object>> getControlConcentrationAnalysis();

    /**
     * 批量更新控制状态
     *
     * @param controllerIds 控制人ID列表
     * @param controlStatus 控制状态
     * @return 是否成功
     */
    boolean batchUpdateControlStatus(List<String> controllerIds, String controlStatus);

    /**
     * 批量更新确认状态
     *
     * @param controllerIds 控制人ID列表
     * @param confirmationStatus 确认状态
     * @param confirmedBy 确认人
     * @return 是否成功
     */
    boolean batchUpdateConfirmationStatus(List<String> controllerIds, String confirmationStatus, String confirmedBy);

    /**
     * 批量更新监管关注度
     *
     * @param controllerIds 控制人ID列表
     * @param regulatoryAttention 监管关注度
     * @return 是否成功
     */
    boolean batchUpdateRegulatoryAttention(List<String> controllerIds, String regulatoryAttention);

    /**
     * 删除过期控制关系
     *
     * @param days 过期天数
     * @return 删除数量
     */
    int deleteExpiredControlRelations(Integer days);

    /**
     * 获取控制关系统计概览
     *
     * @return 统计概览
     */
    Map<String, Object> getControlStatisticsOverview();

    /**
     * 导出实际控制人列表
     *
     * @param queryVO 查询参数
     * @return 导出数据列表
     */
    List<Map<String, Object>> exportActualControllerList(ActualControllerQueryVO queryVO);

    /**
     * 实际控制人识别算法
     *
     * @param controlledEnterpriseId 被控制企业ID
     * @return 识别结果
     */
    List<ActualController> identifyActualControllers(String controlledEnterpriseId);

    /**
     * 控制关系稳定性分析
     *
     * @param controllerId 控制人ID
     * @return 稳定性分析结果
     */
    Map<String, Object> analyzeControlStability(String controllerId);

    /**
     * 控制风险评估
     *
     * @param controllerId 控制人ID
     * @return 风险评估结果
     */
    Map<String, Object> assessControlRisk(String controllerId);

    /**
     * 获取控制人类型标签转换
     *
     * @param controllerType 控制人类型
     * @return 类型标签
     */
    String getControllerTypeLabel(String controllerType);

    /**
     * 获取控制人性质标签转换
     *
     * @param controllerNature 控制人性质
     * @return 性质标签
     */
    String getControllerNatureLabel(String controllerNature);

    /**
     * 获取控制方式标签转换
     *
     * @param controlMethod 控制方式
     * @return 方式标签
     */
    String getControlMethodLabel(String controlMethod);

    /**
     * 获取控制状态标签转换
     *
     * @param controlStatus 控制状态
     * @return 状态标签
     */
    String getControlStatusLabel(String controlStatus);

    /**
     * 获取控制稳定性标签转换
     *
     * @param controlStability 控制稳定性
     * @return 稳定性标签
     */
    String getControlStabilityLabel(String controlStability);

    /**
     * 获取控制风险等级标签转换
     *
     * @param controlRiskLevel 控制风险等级
     * @return 风险等级标签
     */
    String getControlRiskLevelLabel(String controlRiskLevel);

    /**
     * 获取监管关注度标签转换
     *
     * @param regulatoryAttention 监管关注度
     * @return 关注度标签
     */
    String getRegulatoryAttentionLabel(String regulatoryAttention);

    /**
     * 获取确认状态标签转换
     *
     * @param confirmationStatus 确认状态
     * @return 状态标签
     */
    String getConfirmationStatusLabel(String confirmationStatus);
}
