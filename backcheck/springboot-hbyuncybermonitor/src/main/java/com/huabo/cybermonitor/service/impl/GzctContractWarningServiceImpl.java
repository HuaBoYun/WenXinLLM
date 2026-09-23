package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.GzctContractWarning;
import com.huabo.cybermonitor.mapper.GzctContractWarningMapper;
import com.huabo.cybermonitor.service.IGzctContractWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 合同风险预警 Service 实现
 */
@Slf4j
@Service
public class GzctContractWarningServiceImpl extends ServiceImpl<GzctContractWarningMapper, GzctContractWarning>
        implements IGzctContractWarningService {

    @Override
    public GzctContractWarning getByWarnNo(String warnNo) {
        return baseMapper.selectByWarnNo(warnNo);
    }

    @Override
    public List<GzctContractWarning> getByContractId(String contractId) {
        return baseMapper.selectByContractId(contractId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleWarning(String warnNo, String measures, String owner, String deadline, String remark) {
        GzctContractWarning warning = baseMapper.selectByWarnNo(warnNo);
        if (warning == null) {
            // 首次处置，创建记录
            warning = new GzctContractWarning();
            warning.setWarnNo(warnNo);
            warning.setStatus("PROCESSING");
            warning.setMeasures(measures);
            warning.setOwner(owner);
            warning.setDeadline(deadline);
            warning.setRemark(remark);
            warning.setHandleTime(LocalDateTime.now());
            warning.setCreateTime(LocalDateTime.now());
            return baseMapper.insert(warning) > 0;
        } else {
            warning.setStatus("PROCESSING");
            warning.setMeasures(measures);
            warning.setOwner(owner);
            warning.setDeadline(deadline);
            warning.setRemark(remark);
            warning.setHandleTime(LocalDateTime.now());
            warning.setUpdateTime(LocalDateTime.now());
            return baseMapper.updateById(warning) > 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeWarning(String warnNo) {
        GzctContractWarning warning = baseMapper.selectByWarnNo(warnNo);
        if (warning == null) {
            warning = new GzctContractWarning();
            warning.setWarnNo(warnNo);
            warning.setStatus("CLOSED");
            warning.setCloseTime(LocalDateTime.now());
            warning.setCreateTime(LocalDateTime.now());
            return baseMapper.insert(warning) > 0;
        } else {
            warning.setStatus("CLOSED");
            warning.setCloseTime(LocalDateTime.now());
            warning.setUpdateTime(LocalDateTime.now());
            return baseMapper.updateById(warning) > 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GzctContractWarning saveOrUpdateByWarnNo(GzctContractWarning newWarning) {
        GzctContractWarning existing = baseMapper.selectByWarnNo(newWarning.getWarnNo());
        if (existing == null) {
            newWarning.setCreateTime(LocalDateTime.now());
            if (newWarning.getStatus() == null) {
                newWarning.setStatus("PENDING");
            }
            baseMapper.insert(newWarning);
            return newWarning;
        }
        // 已存在则返回持久化的记录（保留其处置状态）
        return existing;
    }
}
