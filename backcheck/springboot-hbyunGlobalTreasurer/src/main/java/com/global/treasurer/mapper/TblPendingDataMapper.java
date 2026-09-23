package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblPendingData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 待结算数据Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface TblPendingDataMapper extends BaseMapper<TblPendingData> {

    /**
     * 分页查询待结算数据
     *
     * @param params 查询参数
     * @return 待结算数据列表
     */
    List<TblPendingData> selectPendingDataPage(Map<String, Object> params);

    /**
     * 统计待结算数据数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countPendingDataList(Map<String, Object> params);

    /**
     * 查询高优先级待结算数据
     *
     * @param orgId 组织ID
     * @return 待结算数据列表
     */
    List<TblPendingData> selectHighPriorityPending(@Param("orgId") Long orgId);

    /**
     * 查询逾期待结算数据
     *
     * @param orgId 组织ID
     * @return 待结算数据列表
     */
    List<TblPendingData> selectOverduePending(@Param("orgId") Long orgId);

    /**
     * 查询大额待结算数据
     *
     * @param params 查询参数
     * @return 待结算数据列表
     */
    List<TblPendingData> selectLargeAmountPending(Map<String, Object> params);

    /**
     * 统计待结算数据概要
     *
     * @param orgId 组织ID
     * @return 统计数据
     */
    Map<String, Object> selectPendingDataSummary(@Param("orgId") Long orgId);

    /**
     * 批量更新结算状态
     *
     * @param ids 待结算ID列表
     * @param status 结算状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 更新审批状态
     *
     * @param pendingId 待结算ID
     * @param approvalStatus 审批状态
     * @param approvalBy 审批人
     * @param approvalOpinion 审批意见
     * @return 影响行数
     */
    int updateApprovalStatus(@Param("pendingId") Long pendingId,
                            @Param("approvalStatus") String approvalStatus,
                            @Param("approvalBy") Long approvalBy,
                            @Param("approvalOpinion") String approvalOpinion);
}
