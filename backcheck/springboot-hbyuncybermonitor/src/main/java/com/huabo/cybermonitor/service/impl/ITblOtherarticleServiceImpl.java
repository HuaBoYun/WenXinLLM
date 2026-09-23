package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblOtherarticle;
import com.huabo.cybermonitor.mapper.ITblOtherarticleMapper;
import com.huabo.cybermonitor.service.ITblOtherarticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@Service
public class ITblOtherarticleServiceImpl extends ServiceImpl<ITblOtherarticleMapper, TblOtherarticle> implements ITblOtherarticleService {
    @Autowired
    ITblOtherarticleMapper iTblOtherarticleMapper;



}
