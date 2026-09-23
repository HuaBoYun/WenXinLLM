package com.huabo.bigmodel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.bigmodel.entity.ChatHistory;
import com.huabo.bigmodel.vo.ChatHistoryPageVO;

import java.util.List;

/**
 * AI对话历史记录服务接口
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 保存对话历史
     */
    ChatHistory saveHistory(ChatHistory chatHistory);

    /**
     * 根据用户ID和类型获取对话历史列表（不含dialogue）
     */
    List<ChatHistory> getHistoryListByUserId(String userId, String type);

    /**
     * 根据用户ID和类型分页获取对话历史列表（不含dialogue，支持滚动加载）
     * @param userId 用户ID
     * @param type 类型
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @return 分页结果
     */
    ChatHistoryPageVO getHistoryListByPage(String userId, String type, Integer pageNum, Integer pageSize);

    /**
     * 根据ID获取对话历史
     */
    ChatHistory getHistoryById(String id);

    /**
     * 删除对话历史
     */
    void deleteHistory(String id);

    /**
     * 更新对话历史
     */
    ChatHistory updateHistory(ChatHistory chatHistory);
}
