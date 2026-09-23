package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.GuaranteeContractDTO;
import com.global.treasurer.dto.GuaranteeContractQueryDTO;
import com.global.treasurer.entity.TblGuaranteeContract;

import java.util.List;
import java.util.Map;

/**
 * 担保合同服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface GuaranteeContractService {

    PageInfo<TblGuaranteeContract> getContractList(GuaranteeContractQueryDTO queryDTO);

    TblGuaranteeContract getContractById(Long contractId);

    TblGuaranteeContract saveContract(GuaranteeContractDTO dto);

    void deleteContract(Long contractId);

    void batchDeleteContracts(List<Long> contractIds);

    void submitForApproval(Long contractId);

    void approve(Long contractId, String comments);

    void reject(Long contractId, String comments);

    void signContract(Long contractId);

    void activateContract(Long contractId);

    void terminateContract(Long contractId, String reason);

    List<TblGuaranteeContract> getExpiringContracts(Integer days);

    Map<String, Object> getContractSummary(Long companyId);
}

