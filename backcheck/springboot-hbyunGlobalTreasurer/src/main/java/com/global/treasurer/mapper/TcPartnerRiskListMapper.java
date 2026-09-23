package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcPartnerRiskList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 伙伴黑白灰名单Mapper接口
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
@Mapper
public interface TcPartnerRiskListMapper extends BaseMapper<TcPartnerRiskList> {

    /**
     * 根据伙伴ID查询风险名单
     * 
     * @param partnerId 伙伴ID
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectByPartnerId(@Param("partnerId") String partnerId);

    /**
     * 根据名单类型查询风险名单
     * 
     * @param listType 名单类型(WHITE-白名单,BLACK-黑名单,GRAY-灰名单)
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectByListType(@Param("listType") String listType);

    /**
     * 根据风险等级查询风险名单
     * 
     * @param riskLevel 风险等级
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectByRiskLevel(@Param("riskLevel") String riskLevel);

    /**
     * 检查伙伴是否在指定名单中
     * 
     * @param partnerId 伙伴ID
     * @param listType 名单类型
     * @return 存在数量
     */
    int checkPartnerInList(@Param("partnerId") String partnerId, @Param("listType") String listType);

    /**
     * 查询需要审查的风险名单
     * 
     * @param days 提前天数
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectNeedReview(@Param("days") Integer days);

    /**
     * 查询即将到期的风险名单
     * 
     * @param days 提前天数
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectExpiring(@Param("days") Integer days);

    /**
     * 批量更新审查日期
     * 
     * @param ids 名单ID列表
     * @param nextReviewDate 下次审查日期
     * @param updateUser 更新人
     * @return 更新数量
     */
    int batchUpdateReviewDate(@Param("ids") List<String> ids, @Param("nextReviewDate") String nextReviewDate, @Param("updateUser") String updateUser);

    /**
     * 批量移除风险名单
     * 
     * @param ids 名单ID列表
     * @param removalReason 移除原因
     * @param removalUser 移除人
     * @return 更新数量
     */
    int batchRemoveFromList(@Param("ids") List<String> ids, @Param("removalReason") String removalReason, @Param("removalUser") String removalUser);

    /**
     * 查询风险名单统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> selectRiskListStatistics();

    /**
     * 根据业务范围查询风险名单
     * 
     * @param businessScope 业务范围
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectByBusinessScope(@Param("businessScope") String businessScope);

    /**
     * 多条件查询风险名单
     * 
     * @param params 查询参数
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectByMultipleConditions(@Param("params") Map<String, Object> params);

    /**
     * 查询有效的风险名单
     * 
     * @param partnerId 伙伴ID
     * @param listType 名单类型
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectEffectiveList(@Param("partnerId") String partnerId, @Param("listType") String listType);

    /**
     * 查询历史风险名单
     * 
     * @param partnerId 伙伴ID
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectHistoryList(@Param("partnerId") String partnerId);

    /**
     * 批量审批风险名单
     *
     * @param ids 名单ID列表
     * @param approvalUser 审批人
     * @return 更新数量
     */
    int batchApprove(@Param("ids") List<String> ids, @Param("approvalUser") String approvalUser);

    /**
     * 根据参数查询风险名单
     *
     * @param params 查询参数
     * @return 风险名单列表
     */
    List<TcPartnerRiskList> selectByParams(@Param("params") Map<String, Object> params);

    /**
     * 查询风险统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> selectRiskStatistics();

    /**
     * 查询风险预警信息
     *
     * @return 预警信息列表
     */
    List<TcPartnerRiskList> selectRiskAlerts();
}
