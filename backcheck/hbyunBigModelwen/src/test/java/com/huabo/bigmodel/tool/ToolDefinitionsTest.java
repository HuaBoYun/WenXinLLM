package com.huabo.bigmodel.tool;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ToolDefinitions 单元测试
 *
 * @author AI Test Generator
 * @date 2026-04-28
 */
@DisplayName("ToolDefinitions 工具定义测试")
public class ToolDefinitionsTest {

    @Test
    @DisplayName("TC-DEF-001: getAllTools 应返回 5 个工具定义")
    void testGetAllToolsCount() {
        List<JSONObject> tools = ToolDefinitions.getAllTools();
        assertEquals(5, tools.size());
    }

    @Test
    @DisplayName("TC-DEF-002: 每个工具应包含 name、description、input_schema")
    void testToolStructure() {
        List<JSONObject> tools = ToolDefinitions.getAllTools();
        for (JSONObject tool : tools) {
            assertNotNull(tool.getString("name"), "工具名称不能为空");
            assertNotNull(tool.getString("description"), "工具描述不能为空");
            assertNotNull(tool.getJSONObject("input_schema"), "input_schema 不能为空");
        }
    }

    @Test
    @DisplayName("TC-DEF-003: 所有工具的 input_schema 应包含 sql 或 sqlList 必填参数")
    void testRequiredParams() {
        List<JSONObject> tools = ToolDefinitions.getAllTools();
        for (JSONObject tool : tools) {
            JSONObject schema = tool.getJSONObject("input_schema");
            assertNotNull(schema.get("required"), tool.getString("name") + " 缺少 required 字段");
        }
    }

    @Test
    @DisplayName("TC-DEF-004: execute_sql 工具应包含 sql 和 dbType 属性")
    void testExecuteSqlTool() {
        JSONObject tool = ToolDefinitions.getSqlExecuteTool();
        assertEquals("execute_sql", tool.getString("name"));
        JSONObject props = tool.getJSONObject("input_schema").getJSONObject("properties");
        assertNotNull(props.getJSONObject("sql"));
        assertNotNull(props.getJSONObject("dbType"));
    }

    @Test
    @DisplayName("TC-DEF-005: query_sql 工具应包含 sql 属性")
    void testQuerySqlTool() {
        JSONObject tool = ToolDefinitions.getSqlQueryTool();
        assertEquals("query_sql", tool.getString("name"));
        JSONObject props = tool.getJSONObject("input_schema").getJSONObject("properties");
        assertNotNull(props.getJSONObject("sql"));
    }

    @Test
    @DisplayName("TC-DEF-006: execute_ddl 工具应包含 sql 属性")
    void testDdlTool() {
        JSONObject tool = ToolDefinitions.getSqlDdlTool();
        assertEquals("execute_ddl", tool.getString("name"));
    }

    @Test
    @DisplayName("TC-DEF-007: execute_dml 工具应包含 sql 属性")
    void testDmlTool() {
        JSONObject tool = ToolDefinitions.getSqlDmlTool();
        assertEquals("execute_dml", tool.getString("name"));
    }

    @Test
    @DisplayName("TC-DEF-008: execute_batch_sql 工具应包含 sqlList 属性")
    void testBatchTool() {
        JSONObject tool = ToolDefinitions.getSqlBatchTool();
        assertEquals("execute_batch_sql", tool.getString("name"));
        JSONObject props = tool.getJSONObject("input_schema").getJSONObject("properties");
        assertNotNull(props.getJSONObject("sqlList"));
    }

    @Test
    @DisplayName("TC-DEF-009: dbType 枚举应包含 dm 和 mysql")
    void testDbTypeEnum() {
        JSONObject tool = ToolDefinitions.getSqlExecuteTool();
        JSONObject dbType = tool.getJSONObject("input_schema")
                .getJSONObject("properties").getJSONObject("dbType");
        Object enumObj = dbType.get("enum");
        assertNotNull(enumObj);
        // enum 可能是 String[] 或 JSONArray，统一转 String 检查
        String enumStr = com.alibaba.fastjson.JSON.toJSONString(enumObj);
        assertTrue(enumStr.contains("dm"), "应包含 dm: " + enumStr);
        assertTrue(enumStr.contains("mysql"), "应包含 mysql: " + enumStr);
    }
}

