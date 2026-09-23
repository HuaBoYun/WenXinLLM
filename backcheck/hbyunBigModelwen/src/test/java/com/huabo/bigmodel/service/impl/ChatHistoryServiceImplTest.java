package com.huabo.bigmodel.service.impl;

import com.huabo.bigmodel.entity.ChatHistory;
import com.huabo.bigmodel.mapper.ChatHistoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ChatHistoryServiceImpl 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ChatHistoryServiceImpl 单元测试")
public class ChatHistoryServiceImplTest {

    @Mock
    private ChatHistoryMapper chatHistoryMapper;

    @InjectMocks
    private ChatHistoryServiceImpl chatHistoryService;

    private ChatHistory sampleHistory;

    @BeforeEach
    void setUp() {
        sampleHistory = new ChatHistory();
        sampleHistory.setId("hist_001");
        sampleHistory.setUserId("user_001");
        sampleHistory.setSessionId("session_001");
        sampleHistory.setTitle("测试对话");
        sampleHistory.setDialogue("[{\"role\":\"user\",\"content\":\"你好\"}]");
        sampleHistory.setHasDocument(false);
        sampleHistory.setType("writing");
    }

    // ================================================================
    // 保存历史
    // ================================================================
    @Nested
    @DisplayName("saveHistory")
    class SaveHistory {

        @Test
        @DisplayName("TC-SAVE-001: 保存时应自动设置 createTime 和 updateTime")
        void testAutoSetTimestamps() {
            // ServiceImpl.save() 内部依赖 TableInfo，需要 spy 绕过
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(true).when(spyService).save(any());

            Date before = new Date();
            ChatHistory result = spyService.saveHistory(sampleHistory);
            Date after = new Date();

            assertNotNull(result.getCreateTime());
            assertNotNull(result.getUpdateTime());
            assertTrue(result.getCreateTime().getTime() >= before.getTime());
            assertTrue(result.getCreateTime().getTime() <= after.getTime());
        }

        @Test
        @DisplayName("TC-SAVE-002: 保存应调用 save 方法")
        void testCallsSave() {
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(true).when(spyService).save(any());

            spyService.saveHistory(sampleHistory);

            verify(spyService).save(any(ChatHistory.class));
        }

        @Test
        @DisplayName("TC-SAVE-003: 保存应返回原对象（含时间戳）")
        void testReturnsSameObject() {
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(true).when(spyService).save(any());

            ChatHistory result = spyService.saveHistory(sampleHistory);

            assertSame(sampleHistory, result);
            assertEquals("hist_001", result.getId());
        }
    }

    // ================================================================
    // 获取详情
    // ================================================================
    @Nested
    @DisplayName("getHistoryById")
    class GetHistoryById {

        @Test
        @DisplayName("TC-GET-001: 存在的 ID 应返回完整数据")
        void testGetExisting() {
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(sampleHistory).when(spyService).getById("hist_001");

            ChatHistory result = spyService.getHistoryById("hist_001");

            assertNotNull(result);
            assertEquals("hist_001", result.getId());
            assertEquals("测试对话", result.getTitle());
            assertNotNull(result.getDialogue());
        }

        @Test
        @DisplayName("TC-GET-002: 不存在的 ID 应返回 null")
        void testGetNonExistent() {
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(null).when(spyService).getById("not_exist");

            ChatHistory result = spyService.getHistoryById("not_exist");

            assertNull(result);
        }
    }

    // ================================================================
    // 删除历史
    // ================================================================
    @Nested
    @DisplayName("deleteHistory")
    class DeleteHistory {

        @Test
        @DisplayName("TC-DEL-001: 删除应调用 removeById")
        void testCallsDelete() {
            // MyBatis-Plus ServiceImpl.removeById 内部依赖 TableInfo 等 Spring 上下文
            // 无法在纯 Mockito 环境下直接调用，改为 spy + doReturn 绕过
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(true).when(spyService).removeById("hist_001");

            spyService.deleteHistory("hist_001");

            verify(spyService).removeById("hist_001");
        }
    }

    // ================================================================
    // 更新历史
    // ================================================================
    @Nested
    @DisplayName("updateHistory")
    class UpdateHistory {

        @Test
        @DisplayName("TC-UPD-001: 更新时应自动设置 updateTime")
        void testAutoSetUpdateTime() {
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(true).when(spyService).updateById(any());

            sampleHistory.setTitle("更新后的标题");
            Date before = new Date();
            ChatHistory result = spyService.updateHistory(sampleHistory);
            Date after = new Date();

            assertNotNull(result.getUpdateTime());
            assertTrue(result.getUpdateTime().getTime() >= before.getTime());
            assertTrue(result.getUpdateTime().getTime() <= after.getTime());
        }

        @Test
        @DisplayName("TC-UPD-002: 更新应调用 updateById")
        void testCallsUpdate() {
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(true).when(spyService).updateById(any());

            spyService.updateHistory(sampleHistory);

            verify(spyService).updateById(any(ChatHistory.class));
        }

        @Test
        @DisplayName("TC-UPD-003: 更新应返回原对象")
        void testReturnsSameObject() {
            ChatHistoryServiceImpl spyService = spy(chatHistoryService);
            doReturn(true).when(spyService).updateById(any());

            ChatHistory result = spyService.updateHistory(sampleHistory);

            assertSame(sampleHistory, result);
        }
    }

    // ================================================================
    // Entity 属性
    // ================================================================
    @Nested
    @DisplayName("ChatHistory Entity")
    class EntityTest {

        @Test
        @DisplayName("TC-ENTITY-001: 所有属性应正确设置和获取")
        void testAllProperties() {
            ChatHistory entity = new ChatHistory();
            Date now = new Date();

            entity.setId("id_001");
            entity.setUserId("user_001");
            entity.setSessionId("session_001");
            entity.setTitle("标题");
            entity.setDialogue("[{\"role\":\"user\"}]");
            entity.setHasDocument(true);
            entity.setType("chat");
            entity.setCreateTime(now);
            entity.setUpdateTime(now);

            assertEquals("id_001", entity.getId());
            assertEquals("user_001", entity.getUserId());
            assertEquals("session_001", entity.getSessionId());
            assertEquals("标题", entity.getTitle());
            assertEquals("[{\"role\":\"user\"}]", entity.getDialogue());
            assertTrue(entity.getHasDocument());
            assertEquals("chat", entity.getType());
            assertEquals(now, entity.getCreateTime());
            assertEquals(now, entity.getUpdateTime());
        }
    }
}

