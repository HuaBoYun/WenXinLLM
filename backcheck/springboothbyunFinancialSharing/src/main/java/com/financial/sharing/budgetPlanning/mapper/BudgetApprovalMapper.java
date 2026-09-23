package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetApprovalQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetApproval;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算数据审批Mapper
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetApprovalMapper extends BaseMapper<TblBudgetApproval> {

    /**
     * 查询审批记录列表
     * 
     * @param param 查询参数
     * @return 审批记录列表
     */
    List<TblBudgetApproval> selectApprovalList(@Param("param") BudgetApprovalQueryParam param);

    /**
     * 根据数据ID查询审批记录
     * 
     * @param dataId 数据ID
     * @return 审批记录列表
     */
    List<TblBudgetApproval> selectByDataId(@Param("dataId") String dataId);

    /**
     * 根据数据ID查询当前待审批记录
     * 
     * @param dataId 数据ID
     * @return 待审批记录
     */
    TblBudgetApproval selectPendingByDataId(@Param("dataId") String dataId);

    /**
     * 查询我的待审批列表
     * 
     * @param param 查询参数
     * @return 待审批列表
     */
    List<TblBudgetApproval> selectMyPendingList(@Param("param") BudgetApprovalQueryParam param);

    /**
     * 查询我的已审批列表
     * 
     * @param param 查询参数
     * @return 已审批列表
     */
    List<TblBudgetApproval> selectMyApprovedList(@Param("param") BudgetApprovalQueryParam param);
}

