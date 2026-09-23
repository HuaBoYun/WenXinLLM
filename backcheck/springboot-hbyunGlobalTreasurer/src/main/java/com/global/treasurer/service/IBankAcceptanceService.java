package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankAcceptanceDTO;
import com.global.treasurer.dto.BankAcceptanceQueryDTO;
import com.global.treasurer.entity.TblBankAcceptance;
import com.global.treasurer.vo.BankAcceptanceVO;

import java.util.Map;

/**
 * 银行承兑汇票Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface IBankAcceptanceService {

    /**
     * 分页查询银行承兑汇票列表
     */
    PageInfo<BankAcceptanceVO> selectBankAcceptanceList(BankAcceptanceQueryDTO queryDTO);

    /**
     * 根据ID查询银行承兑汇票详情
     */
    BankAcceptanceVO selectBankAcceptanceById(Long acceptanceId);

    /**
     * 新增银行承兑汇票申请
     */
    TblBankAcceptance insertBankAcceptance(BankAcceptanceDTO dto);

    /**
     * 修改银行承兑汇票申请
     */
    TblBankAcceptance updateBankAcceptance(BankAcceptanceDTO dto);

    /**
     * 批量删除银行承兑汇票
     */
    boolean deleteBankAcceptanceByIds(Long[] acceptanceIds);

    /**
     * 提交承兑申请
     */
    boolean submitAcceptanceApplication(Long acceptanceId);

    /**
     * 承兑申请审批
     */
    boolean approveAcceptanceApplication(Long acceptanceId, Map<String, Object> approvalData);

    /**
     * 开立承兑汇票
     */
    boolean issueAcceptance(Long acceptanceId);

    /**
     * 保证金管理
     */
    boolean manageMargin(Long acceptanceId, Map<String, Object> marginData);

    /**
     * 获取保证金信息
     */
    Map<String, Object> getMarginInfo(Long acceptanceId);
}

