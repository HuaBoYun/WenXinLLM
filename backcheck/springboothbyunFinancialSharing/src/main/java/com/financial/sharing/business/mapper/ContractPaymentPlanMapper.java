package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblContractPaymentPlan;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 合同收付款计划 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizContractPaymentPlanMapper")
public interface ContractPaymentPlanMapper extends BaseMapper<TblContractPaymentPlan> {

    /**
     * 根据合同ID查询收付款计划
     *
     * @param contractId 合同ID
     * @return 收付款计划列表
     */
    List<TblContractPaymentPlan> selectByContractId(@Param("contractId") String contractId);
}
