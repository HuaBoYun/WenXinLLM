package com.financial.sharing.service;

import com.financial.sharing.dto.param.PrepaymentQueryParam;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PrepaymentVO;

/**
 * 预付款Service
 * @author system
 * @since 2025-01-13
 */
public interface PrepaymentService {

    /**
     * 分页查询预付款列表
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<PrepaymentVO> queryPage(PrepaymentQueryParam param);

    /**
     * 查询预付款详情
     * @param prepaymentId 预付款ID
     * @return 预付款详情
     */
    PrepaymentVO getDetail(String prepaymentId);

    /**
     * 预付款冲销
     * @param prepaymentId 预付款ID
     * @param documentId 应付单据ID
     * @param offsetAmount 冲销金额
     */
    void offsetPrepayment(String prepaymentId, String documentId, java.math.BigDecimal offsetAmount);
}
