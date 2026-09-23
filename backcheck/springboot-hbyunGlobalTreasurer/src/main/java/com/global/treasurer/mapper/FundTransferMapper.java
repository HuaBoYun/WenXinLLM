package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.FundTransfer;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 资金调拨Mapper
 *
 * @author AI Developer
 * @date 2025-01-15
 */
public interface FundTransferMapper extends BaseMapper<FundTransfer> {

    /**
     * 分页查询列表
     *
     * @param param 查询参数
     * @return 列表
     */
    List<FundTransfer> selectPage(@Param("param") Map<String, Object> param);

    /**
     * 批量删除（软删除）
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 批量更新状态
     *
     * @param ids ID列表
     * @param status 状态
     * @param updateByName 更新人
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status, @Param("updateByName") String updateByName);

    /**
     * 批量审批
     *
     * @param ids ID列表
     * @param status 状态
     * @param approveByName 审批人
     * @param approvalComment 审批意见
     * @return 影响行数
     */
    int batchApprove(@Param("ids") List<Long> ids, @Param("status") String status,
                     @Param("approveByName") String approveByName, @Param("approvalComment") String approvalComment);

    /**
     * 取消资金调拨
     *
     * @param transferId 调拨ID
     * @param cancelByName 取消人
     * @return 影响行数
     */
    int cancelTransfer(@Param("transferId") Long transferId, @Param("cancelByName") String cancelByName);

    /**
     * 单个审批资金调拨
     *
     * @param transferId 调拨ID
     * @param status 审批状态
     * @param approveByName 审批人
     * @param approvalComment 审批意见
     * @return 影响行数
     */
    int approveTransfer(@Param("transferId") Long transferId, @Param("status") String status,
                       @Param("approveByName") String approveByName, @Param("approvalComment") String approvalComment);

    /**
     * 执行资金调拨
     *
     * @param transferId 调拨ID
     * @param executeByName 执行人
     * @return 影响行数
     */
    int executeTransfer(@Param("transferId") Long transferId, @Param("executeByName") String executeByName);

    /**
     * 查询可用账户列表
     *
     * @param orgId 组织ID
     * @return 账户列表
     */
    List<Map<String, Object>> selectAvailableAccounts(@Param("orgId") Long orgId);
}
