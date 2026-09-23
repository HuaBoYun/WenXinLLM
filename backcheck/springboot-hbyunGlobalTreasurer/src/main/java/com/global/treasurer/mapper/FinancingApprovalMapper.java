package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFinancingApproval;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 融资审批Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface FinancingApprovalMapper extends BaseMapper<TblFinancingApproval> {

    /**
     * 分页查询审批列表
     *
     * @param params 查询参数
     * @return 审批列表
     */
    List<TblFinancingApproval> selectApprovalList(Map<String, Object> params);

    /**
     * 根据ID查询审批详情
     *
     * @param approvalId 审批ID
     * @return 融资审批
     */
    TblFinancingApproval selectApprovalById(@Param("approvalId") Long approvalId);

    /**
     * 根据融资ID和融资类型查询
     *
     * @param financingId 融资ID
     * @param financingType 融资类型
     * @return 融资审批
     */
    TblFinancingApproval selectByFinancingId(@Param("financingId") Long financingId,
                                            @Param("financingType") String financingType);

    /**
     * 统计审批数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countApprovalList(Map<String, Object> params);

    /**
     * 查询审批历史记录
     *
     * @param approvalId 审批ID
     * @return 历史记录列表
     */
    List<Map<String, Object>> selectApprovalHistory(@Param("approvalId") Long approvalId);

    /**
     * 更新审批状态
     *
     * @param approvalId 审批ID
     * @param status 状态
     * @return 影响行数
     */
    int updateApprovalStatus(@Param("approvalId") Long approvalId, @Param("status") String status);

    /**
     * 批量更新审批状态
     *
     * @param approvalIds 审批ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("approvalIds") List<Long> approvalIds, @Param("status") String status);

    /**
     * 查询待审批数量
     *
     * @param approverId 审批人ID
     * @return 数量
     */
    int countPendingApprovals(@Param("approverId") Long approverId);
}
