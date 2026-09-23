package com.huabo.bigmodel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.bigmodel.entity.ChatHistory;
import com.huabo.bigmodel.mapper.ChatHistoryMapper;
import com.huabo.bigmodel.service.ChatHistoryService;
import com.huabo.bigmodel.service.CombinationDocLinkService;
import com.huabo.bigmodel.vo.ChatHistoryPageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * AI对话历史记录服务实现
 */
@Slf4j
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatHistoryService {

    private final CombinationDocLinkService combinationDocLinkService;

    public ChatHistoryServiceImpl(CombinationDocLinkService combinationDocLinkService) {
        this.combinationDocLinkService = combinationDocLinkService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatHistory saveHistory(ChatHistory chatHistory) {
        chatHistory.setCreateTime(new Date());
        chatHistory.setUpdateTime(new Date());
        this.save(chatHistory);
        log.info("保存对话历史成功: id={}, userId={}", chatHistory.getId(), chatHistory.getUserId());
        autoLinkModelDoc(chatHistory);
        return chatHistory;
    }

    @Override
    public List<ChatHistory> getHistoryListByUserId(String userId, String type) {
        LambdaQueryWrapper<ChatHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatHistory::getUserId, userId)
                // 列表不查 dialogue，减少数据传输量
                .select(ChatHistory::getId, ChatHistory::getUserId, ChatHistory::getSessionId,
                        ChatHistory::getTitle, ChatHistory::getHasDocument, ChatHistory::getType,
                        ChatHistory::getCreateTime, ChatHistory::getUpdateTime)
                .orderByDesc(ChatHistory::getUpdateTime)
                .last("LIMIT 100"); // 最多返回100条
        // 按类型过滤
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq(ChatHistory::getType, type);
        }
        List<ChatHistory> list = this.list(queryWrapper);
        log.info("获取用户对话历史列表: userId={}, type={}, count={}", userId, type, list.size());
        return list;
    }

    @Override
    public ChatHistoryPageVO getHistoryListByPage(String userId, String type, Integer pageNum, Integer pageSize) {
        // 参数校验与默认值
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 20;
        }
        // 限制单页最大条数，防止滥用
        if (pageSize > 50) {
            pageSize = 50;
        }

        LambdaQueryWrapper<ChatHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatHistory::getUserId, userId)
                // 列表不查 dialogue，减少数据传输量
                .select(ChatHistory::getId, ChatHistory::getUserId, ChatHistory::getSessionId,
                        ChatHistory::getTitle, ChatHistory::getHasDocument, ChatHistory::getType,
                        ChatHistory::getCreateTime, ChatHistory::getUpdateTime)
                .orderByDesc(ChatHistory::getUpdateTime);

        // 按类型过滤
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq(ChatHistory::getType, type);
        }

        // 使用 MyBatis-Plus 分页
        Page<ChatHistory> page = new Page<>(pageNum, pageSize);
        IPage<ChatHistory> pageResult = this.page(page, queryWrapper);

        // 构建分页结果
        ChatHistoryPageVO vo = new ChatHistoryPageVO();
        vo.setList(pageResult.getRecords());
        vo.setTotal(pageResult.getTotal());
        vo.setPageNum(pageNum);
        vo.setPageSize(pageSize);
        vo.setHasMore((long) pageNum * pageSize < pageResult.getTotal());

        log.info("分页获取用户对话历史: userId={}, type={}, pageNum={}, pageSize={}, total={}",
                userId, type, pageNum, pageSize, pageResult.getTotal());
        return vo;
    }

    @Override
    public ChatHistory getHistoryById(String id) {
        ChatHistory history = this.getById(id);
        if (history == null) {
            log.warn("对话历史不存在: id={}", id);
        }
        return history;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHistory(String id) {
        this.removeById(id);
        log.info("删除对话历史: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChatHistory updateHistory(ChatHistory chatHistory) {
        chatHistory.setUpdateTime(new Date());
        this.updateById(chatHistory);
        log.info("更新对话历史: id={}", chatHistory.getId());
        autoLinkModelDoc(chatHistory);
        return chatHistory;
    }

    /**
     * 保存/更新历史后自动关联模型文档：
     * 该会话中 AI 创建过组合指标时，把组合关联到本条历史记录（DOC_ID=记录ID），
     * 文档名取对话中最后一个文档标题。失败不影响历史保存。
     */
    private void autoLinkModelDoc(ChatHistory chatHistory) {
        try {
            if (chatHistory.getSessionId() == null || chatHistory.getId() == null) {
                return;
            }
            String docName = combinationDocLinkService.extractLastDocTitle(chatHistory.getDialogue());
            if (docName == null) {
                docName = chatHistory.getTitle();
            }
            combinationDocLinkService.linkPending(chatHistory.getSessionId(), chatHistory.getId(), docName);
        } catch (Exception e) {
            log.warn("自动关联模型文档失败（不影响历史保存）: {}", e.getMessage());
        }
    }
}
