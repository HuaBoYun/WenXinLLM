package com.huabo.bigmodel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.bigmodel.entity.PromptConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI提示词配置Mapper
 */
@Mapper
public interface PromptConfigMapper extends BaseMapper<PromptConfig> {
}
