package com.global.treasurer.service.derivatives.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.derivatives.TblFuturesTransaction;
import com.global.treasurer.mapper.derivatives.TblFuturesTransactionMapper;
import com.global.treasurer.service.derivatives.IFuturesTransactionService;
import org.springframework.stereotype.Service;

/**
 * 期货交易服务实现类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@Service
public class FuturesTransactionServiceImpl extends ServiceImpl<TblFuturesTransactionMapper, TblFuturesTransaction>
        implements IFuturesTransactionService {
}
