package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.ActualController;
import com.huabo.cybermonitor.vo.ActualControllerQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 实际控制人数据访问接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface ActualControllerMapper extends BaseMapper<ActualController> {

    /**
     * 分页查询实际控制人列表
     *
     * @param queryVO 查询参数
     * @return 实际控制人列表
     */
    List<ActualController> selectActualControllerList(@Param("queryVO") ActualControllerQueryVO queryVO);

    /**
     * 根据被控制企业ID查询实际控制人
     *
     * @param controlledEnterpriseId 被控制企业ID
     * @return 实际控制人列表
     */
    List<ActualController> selectByControlledEnterpriseId(@Param("controlledEnterpriseId") String controlledEnterpriseId);

    /**
     * 根据控制人企业ID查询控制关系
     *
     * @param controllerEnterpriseId 控制人企业ID
     * @return 控制关系列表
     */
    List<ActualController> selectByControllerEnterpriseId(@Param("controllerEnterpriseId") String controllerEnterpriseId);

    /**
     * 查询最终控制人
     *
     * @param controlledEnterpriseId 被控制企业ID
     * @return 最终控制人列表
     */
    List<ActualController> selectUltimateControllers(@Param("controlledEnterpriseId") String controlledEnterpriseId);

    /**
     * 查询一致行动人
     *
     * @param controlledEnterpriseId 被控制企业ID
     * @return 一致行动人列表
     */
    List<ActualController> selectConcertedActionControllers(@Param("controlledEnterpriseId") String controlledEnterpriseId);

    /**
     * 按控制人类型查询
     *
     * @param controllerType 控制人类型
     * @return 控制关系列表
     */
    List<ActualController> selectByControllerType(@Param("controllerType") String controllerType);

    /**
     * 按控制人性质查询
     *
     * @param controllerNature 控制人性质
     * @return 控制关系列表
     */
    List<ActualController> selectByControllerNature(@Param("controllerNature") String controllerNature);

    /**
     * 按控制方式查询
     *
     * @param controlMethod 控制方式
     * @return 控制关系列表
     */
    List<ActualController> selectByControlMethod(@Param("controlMethod") String controlMethod);

    /**
     * 按控制状态查询
     *
     * @param controlStatus 控制状态
     * @return 控制关系列表
     */
    List<ActualController> selectByControlStatus(@Param("controlStatus") String controlStatus);

    /**
     * 按控制风险等级查询
     *
     * @param controlRiskLevel 控制风险等级
     * @return 控制关系列表
     */
    List<ActualController> selectByControlRiskLevel(@Param("controlRiskLevel") String controlRiskLevel);

    /**
     * 查询需要特别监管的控制关系
     *
     * @param needSpecialSupervision 是否需要特别监管
     * @return 控制关系列表
     */
    List<ActualController> selectSpecialSupervisionControllers(@Param("needSpecialSupervision") Boolean needSpecialSupervision);

    /**
     * 按监管关注度查询
     *
     * @param regulatoryAttention 监管关注度
     * @return 控制关系列表
     */
    List<ActualController> selectByRegulatoryAttention(@Param("regulatoryAttention") String regulatoryAttention);

    /**
     * 按确认状态查询
     *
     * @param confirmationStatus 确认状态
     * @return 控制关系列表
     */
    List<ActualController> selectByConfirmationStatus(@Param("confirmationStatus") String confirmationStatus);

    /**
     * 查询控制链路
     *
     * @param startEnterpriseId 起始企业ID
     * @param endEnterpriseId 终止企业ID
     * @return 控制链路列表
     */
    List<ActualController> selectControlChain(@Param("startEnterpriseId") String startEnterpriseId,
                                             @Param("endEnterpriseId") String endEnterpriseId);

    /**
     * 查询控制层级结构
     *
     * @param controllerEnterpriseId 控制人企业ID
     * @param maxLevel 最大层级
     * @return 控制层级结构
     */
    List<ActualController> selectControlHierarchy(@Param("controllerEnterpriseId") String controllerEnterpriseId,
                                                 @Param("maxLevel") Integer maxLevel);

    /**
     * 按控制人类型统计
     *
     * @return 类型统计
     */
    List<Map<String, Object>> selectControllerTypeStatistics();

    /**
     * 按控制人性质统计
     *
     * @return 性质统计
     */
    List<Map<String, Object>> selectControllerNatureStatistics();

    /**
     * 按控制方式统计
     *
     * @return 方式统计
     */
    List<Map<String, Object>> selectControlMethodStatistics();

    /**
     * 按控制状态统计
     *
     * @return 状态统计
     */
    List<Map<String, Object>> selectControlStatusStatistics();

    /**
     * 按控制稳定性统计
     *
     * @return 稳定性统计
     */
    List<Map<String, Object>> selectControlStabilityStatistics();

    /**
     * 按控制风险等级统计
     *
     * @return 风险等级统计
     */
    List<Map<String, Object>> selectControlRiskLevelStatistics();

    /**
     * 按监管关注度统计
     *
     * @return 关注度统计
     */
    List<Map<String, Object>> selectRegulatoryAttentionStatistics();

    /**
     * 查询控制关系变化趋势
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变化趋势数据
     */
    List<Map<String, Object>> selectControlRelationTrend(@Param("startDate") String startDate,
                                                         @Param("endDate") String endDate);

    /**
     * 查询控制集中度分析
     *
     * @return 控制集中度数据
     */
    List<Map<String, Object>> selectControlConcentrationAnalysis();

    /**
     * 批量更新控制状态
     *
     * @param controllerIds 控制人ID列表
     * @param controlStatus 控制状态
     * @return 更新数量
     */
    int batchUpdateControlStatus(@Param("controllerIds") List<String> controllerIds,
                                @Param("controlStatus") String controlStatus);

    /**
     * 批量更新确认状态
     *
     * @param controllerIds 控制人ID列表
     * @param confirmationStatus 确认状态
     * @param confirmedBy 确认人
     * @return 更新数量
     */
    int batchUpdateConfirmationStatus(@Param("controllerIds") List<String> controllerIds,
                                     @Param("confirmationStatus") String confirmationStatus,
                                     @Param("confirmedBy") String confirmedBy);

    /**
     * 批量更新监管关注度
     *
     * @param controllerIds 控制人ID列表
     * @param regulatoryAttention 监管关注度
     * @return 更新数量
     */
    int batchUpdateRegulatoryAttention(@Param("controllerIds") List<String> controllerIds,
                                      @Param("regulatoryAttention") String regulatoryAttention);

    /**
     * 删除过期控制关系
     *
     * @param days 过期天数
     * @return 删除数量
     */
    int deleteExpiredControlRelations(@Param("days") Integer days);

    /**
     * 获取控制关系统计概览
     *
     * @return 统计概览
     */
    Map<String, Object> selectControlStatisticsOverview();

    /**
     * 导出实际控制人列表
     *
     * @param queryVO 查询参数
     * @return 导出数据列表
     */
    List<Map<String, Object>> exportActualControllerList(@Param("queryVO") ActualControllerQueryVO queryVO);
}
