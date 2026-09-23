package com.global.treasurer.service.derivatives.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.derivatives.TblSwapTransaction;
import com.global.treasurer.mapper.derivatives.TblSwapTransactionMapper;
import com.global.treasurer.service.derivatives.ISwapTransactionService;
import org.springframework.stereotype.Service;

/**
 * 掉期交易服务实现类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@Service
public class SwapTransactionServiceImpl extends ServiceImpl<TblSwapTransactionMapper, TblSwapTransaction>
        implements ISwapTransactionService {
}
