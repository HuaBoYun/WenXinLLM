package com.global.treasurer.service.derivatives.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.derivatives.TblDerivativesMarketData;
import com.global.treasurer.mapper.derivatives.TblDerivativesMarketDataMapper;
import com.global.treasurer.service.derivatives.IDerivativesMarketDataService;
import org.springframework.stereotype.Service;

/**
 * 衍生品市场数据服务实现类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@Service
public class DerivativesMarketDataServiceImpl extends ServiceImpl<TblDerivativesMarketDataMapper, TblDerivativesMarketData>
        implements IDerivativesMarketDataService {
}
