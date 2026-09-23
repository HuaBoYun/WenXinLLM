package com.huabo.bigmodel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.bigmodel.entity.PromptConfig;

/**
 * AI提示词配置服务接口
 */
public interface PromptConfigService extends IService<PromptConfig> {

    /**
     * 获取指定用户、指定类型的提示词配置
     *
     * @param userId     用户ID
     * @param promptType 提示词类型（如 consult）
     * @return 提示词配置，不存在时返回 null
     */
    PromptConfig getPrompt(String userId, String promptType);

    /**
     * 保存或更新提示词配置（按 userId + promptType 唯一）
     *
     * @param userId        用户ID
     * @param promptType    提示词类型
     * @param promptContent 提示词内容
     * @return 保存后的配置
     */
    PromptConfig savePrompt(String userId, String promptType, String promptContent);
}
