package com.global.treasurer.service.derivatives.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.derivatives.TblDerivativesAlert;
import com.global.treasurer.mapper.derivatives.TblDerivativesAlertMapper;
import com.global.treasurer.service.derivatives.IDerivativesAlertService;
import org.springframework.stereotype.Service;

/**
 * 衍生品预警服务实现类
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@Service
public class DerivativesAlertServiceImpl extends ServiceImpl<TblDerivativesAlertMapper, TblDerivativesAlert>
        implements IDerivativesAlertService {
}
