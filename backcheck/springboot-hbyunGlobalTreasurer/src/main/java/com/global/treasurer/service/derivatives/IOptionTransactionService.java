package com.global.treasurer.service.derivatives;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.derivatives.TblOptionTransaction;
import com.hbfk.entity.TblStaffUtil;

/**
 * 期权交易服务接口
 *
 * @author AI Developer
 * @date 2026-01-21
 */
public interface IOptionTransactionService extends IService<TblOptionTransaction> {
    /**
     * 取消期权交易
     *
     * @param transactionId 期权交易ID
     * @param reason 取消原因
     * @return 是否取消成功
     */
    boolean cancel(Long transactionId, String reason);
}
