package com.global.treasurer.service.derivatives.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.derivatives.TblDerivativesRiskLimit;
import com.global.treasurer.mapper.derivatives.TblDerivativesRiskLimitMapper;
import com.global.treasurer.service.derivatives.IDerivativesRiskLimitService;
import org.springframework.stereotype.Service;

/**
 * 衍生品风险限额服务实现类
 *
 * @author 华博云开发团队
 * @date 2026-03-24
 */
@Service
public class DerivativesRiskLimitServiceImpl extends ServiceImpl<TblDerivativesRiskLimitMapper, TblDerivativesRiskLimit>
        implements IDerivativesRiskLimitService {
}

