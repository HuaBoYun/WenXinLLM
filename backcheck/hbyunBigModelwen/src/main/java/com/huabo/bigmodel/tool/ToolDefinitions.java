package com.huabo.bigmodel.tool;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * AI工具定义
 * 定义AI可以调用的工具列表
 */
public class ToolDefinitions {

    /**
     * 获取所有可用工具定义
     */
    public static List<JSONObject> getAllTools() {
        List<JSONObject> tools = new ArrayList<>();
        tools.add(getSqlExecuteTool());
        tools.add(getSqlQueryTool());
        tools.add(getSqlDdlTool());
        tools.add(getSqlDmlTool());
        tools.add(getSqlBatchTool());
        return tools;
    }

    /**
     * 通用SQL执行工具
     */
    public static JSONObject getSqlExecuteTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "execute_sql");
        tool.put("description", "执行SQL语句,自动识别SQL类型(DDL/DML/QUERY)。支持建表、查询、插入、更新、删除等操作。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();
        
        // sql参数
        JSONObject sqlParam = new JSONObject();
        sqlParam.put("type", "string");
        sqlParam.put("description", "要执行的SQL语句,例如: CREATE TABLE users (id INT, name VARCHAR(50)) 或 SELECT * FROM users");
        properties.put("sql", sqlParam);

        // dbType参数
        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("enum", new String[]{"dm", "mysql"});
        dbTypeParam.put("description", "数据库类型: dm(达梦数据库) 或 mysql(MySQL数据库),默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        inputSchema.put("required", new String[]{"sql"});

        tool.put("input_schema", inputSchema);
        return tool;
    }

    /**
     * SQL查询工具
     */
    public static JSONObject getSqlQueryTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "query_sql");
        tool.put("description", "执行SQL查询语句,返回查询结果。适用于SELECT查询。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();
        
        JSONObject sqlParam = new JSONObject();
        sqlParam.put("type", "string");
        sqlParam.put("description", "SELECT查询语句,例如: SELECT * FROM users WHERE age > 18");
        properties.put("sql", sqlParam);

        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("enum", new String[]{"dm", "mysql"});
        dbTypeParam.put("description", "数据库类型,默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        inputSchema.put("required", new String[]{"sql"});

        tool.put("input_schema", inputSchema);
        return tool;
    }

    /**
     * SQL DDL工具(建表)
     */
    public static JSONObject getSqlDdlTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "execute_ddl");
        tool.put("description", "执行DDL语句,用于创建表、修改表结构、删除表等操作。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();
        
        JSONObject sqlParam = new JSONObject();
        sqlParam.put("type", "string");
        sqlParam.put("description", "DDL语句,例如: CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(50), age INT)");
        properties.put("sql", sqlParam);

        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("enum", new String[]{"dm", "mysql"});
        dbTypeParam.put("description", "数据库类型,默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        inputSchema.put("required", new String[]{"sql"});

        tool.put("input_schema", inputSchema);
        return tool;
    }

    /**
     * SQL DML工具(增删改)
     */
    public static JSONObject getSqlDmlTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "execute_dml");
        tool.put("description", "执行DML语句,用于插入、更新、删除数据。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();

        JSONObject sqlParam = new JSONObject();
        sqlParam.put("type", "string");
        sqlParam.put("description", "DML语句,例如: INSERT INTO users (id, name, age) VALUES (1, '张三', 25)");
        properties.put("sql", sqlParam);

        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("enum", new String[]{"dm", "mysql"});
        dbTypeParam.put("description", "数据库类型,默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        inputSchema.put("required", new String[]{"sql"});

        tool.put("input_schema", inputSchema);
        return tool;
    }

    /**
     * 批量SQL执行工具
     */
    public static JSONObject getSqlBatchTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "execute_batch_sql");
        tool.put("description", "批量执行多条SQL语句,逐条执行,支持错误自动修复和进度反馈。适用于需要执行多条SQL的场景,如创建多个表、插入多条数据等。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();

        // sqlList参数
        JSONObject sqlListParam = new JSONObject();
        sqlListParam.put("type", "string");
        sqlListParam.put("description", "SQL列表JSON数组字符串,格式为: [{\"sql\":\"SQL语句\",\"type\":\"SQL类型\",\"description\":\"描述\"},...]");
        properties.put("sqlList", sqlListParam);

        // dbType参数
        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("enum", new String[]{"dm", "mysql"});
        dbTypeParam.put("description", "数据库类型: dm(达梦数据库) 或 mysql(MySQL数据库),默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        inputSchema.put("required", new String[]{"sqlList"});

        tool.put("input_schema", inputSchema);
        return tool;
    }
}

