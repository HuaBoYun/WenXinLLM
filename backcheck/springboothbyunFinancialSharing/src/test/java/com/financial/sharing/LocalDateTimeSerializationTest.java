package com.financial.sharing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financial.sharing.util.JsonMapper;
import com.financial.sharing.vo.result.AccountSubjectVO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LocalDateTime序列化测试
 */
public class LocalDateTimeSerializationTest {

    @Test
    public void testLocalDateTimeSerialization() {
        // 创建包含LocalDateTime的AccountSubjectVO
        AccountSubjectVO subject = new AccountSubjectVO();
        subject.setSubjectId(1L);
        subject.setSubjectCode("1001");
        subject.setSubjectName("测试科目");
        subject.setSubjectLevel(1);
        subject.setSubjectType(1);
        subject.setBalanceDirection(1);
        subject.setIsLeaf(1);
        subject.setCreateTime(LocalDateTime.of(2024, 12, 19, 10, 30, 45));
        subject.setUpdateTime(LocalDateTime.now());

        // 使用JsonMapper序列化
        String json = JsonMapper.toJson(subject);

        System.out.println("序列化结果:");
        System.out.println(json);

        // 验证序列化结果包含LocalDateTime字段
        assertNotNull(json);
        assertTrue(json.contains("\"createTime\""));
        assertTrue(json.contains("2024-12-19"));

        // 验证不会抛出序列化异常
        assertDoesNotThrow(() -> {
            JsonMapper.toJson(subject);
        });
    }

    @Test
    public void testListWithLocalDateTimeSerialization() {
        // 创建包含LocalDateTime的列表
        List<AccountSubjectVO> subjects = new ArrayList<>();

        AccountSubjectVO subject1 = new AccountSubjectVO();
        subject1.setSubjectId(1L);
        subject1.setSubjectCode("1001");
        subject1.setSubjectName("测试科目1");
        subject1.setCreateTime(LocalDateTime.of(2024, 12, 19, 10, 30, 45));
        subjects.add(subject1);

        AccountSubjectVO subject2 = new AccountSubjectVO();
        subject2.setSubjectId(2L);
        subject2.setSubjectCode("1002");
        subject2.setSubjectName("测试科目2");
        subject2.setCreateTime(LocalDateTime.of(2024, 12, 20, 14, 25, 30));
        subjects.add(subject2);

        // 使用JsonMapper序列化列表
        String json = JsonMapper.toJson(subjects);

        System.out.println("列表序列化结果:");
        System.out.println(json);

        // 验证序列化结果
        assertNotNull(json);
        assertTrue(json.contains("\"createTime\""));
        assertTrue(json.contains("2024-12-19"));
        assertTrue(json.contains("2024-12-20"));

        // 验证不会抛出序列化异常
        assertDoesNotThrow(() -> {
            JsonMapper.toJson(subjects);
        });
    }
}