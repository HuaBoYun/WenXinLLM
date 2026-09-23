package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblTreasuryParameter;
import com.global.treasurer.mapper.TblTreasuryParameterMapper;
import com.global.treasurer.service.TblTreasuryParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 司库参数配置Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
@Service
public class TblTreasuryParameterServiceImpl extends ServiceImpl<TblTreasuryParameterMapper, TblTreasuryParameter> implements TblTreasuryParameterService {
    @Autowired
    private TblTreasuryParameterMapper tblTreasuryParameterMapper;
}
