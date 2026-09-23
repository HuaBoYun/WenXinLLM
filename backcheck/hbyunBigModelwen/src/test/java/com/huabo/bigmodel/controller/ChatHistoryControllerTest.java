package com.huabo.bigmodel.controller;

import com.alibaba.fastjson.JSON;
import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.entity.ChatHistory;
import com.huabo.bigmodel.service.ChatHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ChatHistoryController 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ChatHistoryController 单元测试")
public class ChatHistoryControllerTest {

    @Mock
    private ChatHistoryService chatHistoryService;

    @InjectMocks
    private ChatHistoryController controller;

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
        sampleHistory.setCreateTime(new Date());
        sampleHistory.setUpdateTime(new Date());
    }

    // ================================================================
    // 保存历史记录
    // ================================================================
    @Nested
    @DisplayName("saveHistory")
    class SaveHistory {

        @Test
        @DisplayName("TC-SAVE-001: 正常保存应返回 code=200")
        void testSaveSuccess() {
            when(chatHistoryService.saveHistory(any())).thenReturn(sampleHistory);
            Result<ChatHistory> result = controller.saveHistory(sampleHistory);
            assertEquals(200, result.getCode());
            assertNotNull(result.getData());
            assertEquals("hist_001", result.getData().getId());
        }

        @Test
        @DisplayName("TC-SAVE-002: 保存异常应返回 code=500")
        void testSaveFailure() {
            when(chatHistoryService.saveHistory(any())).thenThrow(new RuntimeException("数据库异常"));
            Result<ChatHistory> result = controller.saveHistory(sampleHistory);
            assertEquals(500, result.getCode());
            assertTrue(result.getMsg().contains("保存失败"));
        }
    }

    // ================================================================
    // 获取历史列表
    // ================================================================
    @Nested
    @DisplayName("getHistoryList")
    class GetHistoryList {

        @Test
        @DisplayName("TC-LIST-001: 正常获取列表应返回数据")
        void testGetListSuccess() {
            List<ChatHistory> list = Arrays.asList(sampleHistory);
            when(chatHistoryService.getHistoryListByUserId("user_001", null)).thenReturn(list);

            Result<?> result = controller.getHistoryList("user_001", null, null, null);
            assertEquals(200, result.getCode());
            assertEquals(1, ((List<?>) result.getData()).size());
        }

        @Test
        @DisplayName("TC-LIST-002: 按类型过滤应传递 type 参数")
        void testGetListWithType() {
            when(chatHistoryService.getHistoryListByUserId("user_001", "writing"))
                    .thenReturn(Arrays.asList(sampleHistory));

            Result<?> result = controller.getHistoryList("user_001", "writing", null, null);
            assertEquals(200, result.getCode());
            verify(chatHistoryService).getHistoryListByUserId("user_001", "writing");
        }

        @Test
        @DisplayName("TC-LIST-003: 获取列表异常应返回错误")
        void testGetListFailure() {
            when(chatHistoryService.getHistoryListByUserId(any(), any()))
                    .thenThrow(new RuntimeException("查询超时"));

            Result<?> result = controller.getHistoryList("user_001", null, null, null);
            assertEquals(500, result.getCode());
            assertTrue(result.getMsg().contains("获取失败"));
        }
    }

    // ================================================================
    // 获取历史详情
    // ================================================================
    @Nested
    @DisplayName("getHistoryDetail")
    class GetHistoryDetail {

        @Test
        @DisplayName("TC-DETAIL-001: 正常获取详情应返回完整数据")
        void testGetDetailSuccess() {
            when(chatHistoryService.getHistoryById("hist_001")).thenReturn(sampleHistory);

            Result<ChatHistory> result = controller.getHistoryDetail("hist_001");
            assertEquals(200, result.getCode());
            assertNotNull(result.getData().getDialogue());
        }

        @Test
        @DisplayName("TC-DETAIL-002: ID 不存在应返回 null data")
        void testGetDetailNotFound() {
            when(chatHistoryService.getHistoryById("not_exist")).thenReturn(null);

            Result<ChatHistory> result = controller.getHistoryDetail("not_exist");
            assertEquals(200, result.getCode());
            assertNull(result.getData());
        }

        @Test
        @DisplayName("TC-DETAIL-003: 获取详情异常应返回错误")
        void testGetDetailFailure() {
            when(chatHistoryService.getHistoryById(any())).thenThrow(new RuntimeException("IO异常"));

            Result<ChatHistory> result = controller.getHistoryDetail("hist_001");
            assertEquals(500, result.getCode());
        }
    }

    // ================================================================
    // 删除历史
    // ================================================================
    @Nested
    @DisplayName("deleteHistory")
    class DeleteHistory {

        @Test
        @DisplayName("TC-DEL-001: 正常删除应返回 code=200")
        void testDeleteSuccess() {
            doNothing().when(chatHistoryService).deleteHistory("hist_001");

            Result<Void> result = controller.deleteHistory("hist_001");
            assertEquals(200, result.getCode());
            verify(chatHistoryService).deleteHistory("hist_001");
        }

        @Test
        @DisplayName("TC-DEL-002: 删除异常应返回错误")
        void testDeleteFailure() {
            doThrow(new RuntimeException("删除失败")).when(chatHistoryService).deleteHistory("hist_001");

            Result<Void> result = controller.deleteHistory("hist_001");
            assertEquals(500, result.getCode());
            assertTrue(result.getMsg().contains("删除失败"));
        }
    }

    // ================================================================
    // 更新历史
    // ================================================================
    @Nested
    @DisplayName("updateHistory")
    class UpdateHistory {

        @Test
        @DisplayName("TC-UPD-001: 正常更新应返回更新后的数据")
        void testUpdateSuccess() {
            sampleHistory.setTitle("更新后的标题");
            when(chatHistoryService.updateHistory(any())).thenReturn(sampleHistory);

            Result<ChatHistory> result = controller.updateHistory(sampleHistory);
            assertEquals(200, result.getCode());
            assertEquals("更新后的标题", result.getData().getTitle());
        }

        @Test
        @DisplayName("TC-UPD-002: 更新异常应返回错误")
        void testUpdateFailure() {
            when(chatHistoryService.updateHistory(any())).thenThrow(new RuntimeException("并发冲突"));

            Result<ChatHistory> result = controller.updateHistory(sampleHistory);
            assertEquals(500, result.getCode());
            assertTrue(result.getMsg().contains("更新失败"));
        }
    }
}

