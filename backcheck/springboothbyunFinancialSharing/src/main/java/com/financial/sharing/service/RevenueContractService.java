package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.RevenueContractVO;
import com.financial.sharing.vo.param.RevenueContractQueryParam;

/**
 * 收入合同服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface RevenueContractService {

    /**
     * 分页查询收入合同列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<RevenueContractVO> getRevenueContractList(RevenueContractQueryParam param);
}

