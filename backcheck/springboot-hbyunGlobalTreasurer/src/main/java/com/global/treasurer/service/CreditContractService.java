package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CreditContractDTO;
import com.global.treasurer.dto.CreditContractQueryDTO;
import com.global.treasurer.entity.TblCreditContract;

import java.util.List;
import java.util.Map;

/**
 * 授信合同服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface CreditContractService {

    /**
     * 分页查询授信合同列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblCreditContract> getContractList(CreditContractQueryDTO queryDTO);

    /**
     * 根据ID查询授信合同详情
     *
     * @param contractId 合同ID
     * @return 授信合同
     */
    TblCreditContract getContractById(Long contractId);

    /**
     * 保存授信合同（新增或更新）
     *
     * @param dto 授信合同DTO
     * @return 保存后的授信合同
     */
    TblCreditContract saveContract(CreditContractDTO dto);

    /**
     * 删除授信合同
     *
     * @param contractId 合同ID
     */
    void deleteContract(Long contractId);

    /**
     * 批量删除授信合同
     *
     * @param contractIds 合同ID列表
     */
    void batchDeleteContracts(List<Long> contractIds);

    /**
     * 签署合同
     *
     * @param contractId 合同ID
     */
    void signContract(Long contractId);

    /**
     * 生效合同
     *
     * @param contractId 合同ID
     */
    void activateContract(Long contractId);

    /**
     * 终止合同
     *
     * @param contractId 合同ID
     * @param reason 终止原因
     */
    void terminateContract(Long contractId, String reason);

    /**
     * 查询即将到期的合同
     *
     * @param days 天数
     * @return 合同列表
     */
    List<TblCreditContract> getExpiringContracts(Integer days);

    /**
     * 查询授信合同汇总
     *
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> getContractSummary(Long companyId);

    /**
     * 导出授信合同
     *
     * @param queryDTO 查询条件
     * @param response HTTP响应对象
     */
    void exportContract(CreditContractQueryDTO queryDTO, javax.servlet.http.HttpServletResponse response);
}

