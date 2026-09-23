package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.entity.TblIndicatorControlConfig;
import com.huabo.fxgl.mapper.IndicatorControlConfigMapper;
import com.huabo.fxgl.service.IIndicatorControlConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IndicatorControlConfigServiceImpl
        extends ServiceImpl<IndicatorControlConfigMapper, TblIndicatorControlConfig>
        implements IIndicatorControlConfigService {

    @Override
    public IPage<TblIndicatorControlConfig> getConfigPage(int pageNum, int pageSize) {
        Page<TblIndicatorControlConfig> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectConfigPage(page);
    }

    @Override
    public void saveConfig(TblIndicatorControlConfig config, String currentUser) {
        if (StringUtil.isEmpty(config.getId())) {
            config.setId(UUID.randomUUID().toString().replace("-", ""));
            config.setCreateUser(currentUser);
            config.setCreateTime(LocalDateTime.now());
            this.save(config);
        } else {
            config.setUpdateUser(currentUser);
            config.setUpdateTime(LocalDateTime.now());
            this.updateById(config);
        }
    }

    @Override
    public List<TblIndicatorControlConfig> getEnabledConfigs(String rightId, String operationType) {
        List<TblIndicatorControlConfig> configs = baseMapper.selectEnabledByRightId(rightId);
        return configs.stream()
            .filter(c -> c.getOperationType() != null && c.getOperationType().contains(operationType))
            .collect(Collectors.toList());
    }

    @Override
    public List<TblIndicatorControlConfig> getEnabledConfigsByExternalKey(String externalPageKey, String operationType) {
        List<TblIndicatorControlConfig> configs = baseMapper.selectEnabledByExternalKey(externalPageKey);
        return configs.stream()
            .filter(c -> c.getOperationType() != null && c.getOperationType().contains(operationType))
            .collect(Collectors.toList());
    }
}
