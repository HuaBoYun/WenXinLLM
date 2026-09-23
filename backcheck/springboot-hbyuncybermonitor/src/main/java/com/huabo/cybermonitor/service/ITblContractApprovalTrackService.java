package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.TblContractApprovalTrack;
import com.huabo.cybermonitor.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 合同审批追踪服务接口
 */
public interface ITblContractApprovalTrackService extends IService<TblContractApprovalTrack> {

    /**
     * 分页查询合同审批追踪列表
     * 支持按派生的approvalStatus筛选
     *
     * @param params 查询条件（Map形式，兼容controller层Map接收）
     * @return 分页结果
     */
    PageResult<Map<String, Object>> selectApprovalList(Map<String, Object> params);

    /**
     * 获取合同的审批流程详情
     *
     * @param contractId 合同ID
     * @return 审批流程列表
     */
    List<TblContractApprovalTrack> getApprovalFlow(String contractId);
}
