package com.huabo.bigmodel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.bigmodel.entity.PromptConfig;
import com.huabo.bigmodel.mapper.PromptConfigMapper;
import com.huabo.bigmodel.service.PromptConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * AI提示词配置服务实现
 */
@Slf4j
@Service
public class PromptConfigServiceImpl extends ServiceImpl<PromptConfigMapper, PromptConfig>
        implements PromptConfigService {

    @Override
    public PromptConfig getPrompt(String userId, String promptType) {
        LambdaQueryWrapper<PromptConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromptConfig::getUserId, userId)
                .eq(PromptConfig::getPromptType, promptType)
                .last("LIMIT 1");
        PromptConfig config = this.getOne(queryWrapper);
        log.info("获取提示词配置: userId={}, promptType={}, found={}", userId, promptType, config != null);
        return config;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromptConfig savePrompt(String userId, String promptType, String promptContent) {
        // 按 userId + promptType 唯一，存在则更新，不存在则新增
        PromptConfig existing = getPrompt(userId, promptType);
        Date now = new Date();
        if (existing != null) {
            existing.setPromptContent(promptContent);
            existing.setUpdateTime(now);
            this.updateById(existing);
            log.info("更新提示词配置: id={}, userId={}, promptType={}", existing.getId(), userId, promptType);
            return existing;
        }
        PromptConfig config = new PromptConfig();
        config.setUserId(userId);
        config.setPromptType(promptType);
        config.setPromptContent(promptContent);
        config.setCreateTime(now);
        config.setUpdateTime(now);
        this.save(config);
        log.info("新增提示词配置: id={}, userId={}, promptType={}", config.getId(), userId, promptType);
        return config;
    }
}
