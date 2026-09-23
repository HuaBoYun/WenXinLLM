package com.huabo.bigmodel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.bigmodel.entity.ChatHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI对话历史记录Mapper
 */
@Mapper
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {
}
