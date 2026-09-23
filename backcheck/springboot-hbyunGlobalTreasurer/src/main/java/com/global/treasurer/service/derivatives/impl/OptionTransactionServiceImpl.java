package com.global.treasurer.service.derivatives.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.derivatives.TblOptionTransaction;
import com.global.treasurer.mapper.derivatives.TblOptionTransactionMapper;
import com.global.treasurer.service.derivatives.IOptionTransactionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import java.util.Date;

/**
 * 期权交易服务实现类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@Service
public class OptionTransactionServiceImpl extends ServiceImpl<TblOptionTransactionMapper, TblOptionTransaction>
        implements IOptionTransactionService {

    @Resource
    private UserProvider userProvider;

    /**
     * 取消期权交易
     *
     * @param transactionId 期权交易ID
     * @param reason 取消原因
     * @return 是否取消成功
     */
    @Override
    public boolean cancel(Long transactionId, String reason) {
        TblOptionTransaction transaction = this.getById(transactionId);
        if (transaction == null) {
            return false;
        }

        // 验证状态：只有待生效状态可以取消
        if (!"PENDING".equals(transaction.getStatus())) {
            return false;
        }

        transaction.setStatus("CANCELLED");
        transaction.setUpdateTime(new Date());

        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null) {
                transaction.setUpdateBy(loginStaff.getStaffid().longValue());
            }
        } catch (Exception e) {
            // 忽略获取用户信息异常
        }

        return this.updateById(transaction);
    }
}
