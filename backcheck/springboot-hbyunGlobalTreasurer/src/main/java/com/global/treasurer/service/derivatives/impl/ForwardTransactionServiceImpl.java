package com.global.treasurer.service.derivatives.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.derivatives.TblForwardTransaction;
import com.global.treasurer.mapper.derivatives.TblForwardTransactionMapper;
import com.global.treasurer.service.derivatives.IForwardTransactionService;
import org.springframework.stereotype.Service;

/**
 * 远期交易服务实现类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@Service
public class ForwardTransactionServiceImpl extends ServiceImpl<TblForwardTransactionMapper, TblForwardTransaction>
        implements IForwardTransactionService {
}
