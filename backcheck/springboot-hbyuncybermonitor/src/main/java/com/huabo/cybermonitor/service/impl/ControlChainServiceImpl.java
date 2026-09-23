package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.ControlChain;
import com.huabo.cybermonitor.mapper.ControlChainMapper;
import com.huabo.cybermonitor.service.IControlChainService;
import org.springframework.stereotype.Service;

/**
 * 控制链分析 Service实现
 */
@Service
public class ControlChainServiceImpl extends ServiceImpl<ControlChainMapper, ControlChain> implements IControlChainService {
}
