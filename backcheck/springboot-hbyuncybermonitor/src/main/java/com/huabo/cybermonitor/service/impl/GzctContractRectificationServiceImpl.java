package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.GzctContractRectification;
import com.huabo.cybermonitor.mapper.GzctContractRectificationMapper;
import com.huabo.cybermonitor.service.IGzctContractRectificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 合同整改记录服务实现类
 */
@Service
public class GzctContractRectificationServiceImpl
        extends ServiceImpl<GzctContractRectificationMapper, GzctContractRectification>
        implements IGzctContractRectificationService {

    @Override
    public boolean submitRectification(GzctContractRectification rectification) {
        rectification.setRectifyStatus("PROCESSING");
        rectification.setIssuedTime(LocalDateTime.now());
        rectification.setCreateTime(LocalDateTime.now());
        return save(rectification);
    }

    @Override
    public List<GzctContractRectification> getByContractNo(String contractNo) {
        return baseMapper.selectByContractNo(contractNo);
    }

    @Override
    public String getLatestRectifyStatus(String contractId) {
        List<GzctContractRectification> list = baseMapper.selectByContractId(contractId);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0).getRectifyStatus();
    }
}
