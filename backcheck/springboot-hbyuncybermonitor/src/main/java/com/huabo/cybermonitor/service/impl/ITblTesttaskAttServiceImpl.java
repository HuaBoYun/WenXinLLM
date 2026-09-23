package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblTesttaskAtt;
import com.huabo.cybermonitor.mapper.ITblTesttaskAttMapper;
import com.huabo.cybermonitor.service.ITblTesttaskAttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-09-09
 */
@Service
public class ITblTesttaskAttServiceImpl extends ServiceImpl<ITblTesttaskAttMapper, TblTesttaskAtt> implements ITblTesttaskAttService {
    @Autowired
    ITblTesttaskAttMapper iTblTesttaskAttMapper;
}
