package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.dto.SqlExecuteRequest;
import com.huabo.bigmodel.dto.SqlExecuteResponse;
import com.huabo.bigmodel.service.SqlExecuteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * SQL执行控制器
 * 提供SQL语句执行接口,支持DDL、DML、QUERY
 */
@Slf4j
@RestController
@RequestMapping("/v1/sql")
@Tag(name = "SQL执行接口", description = "执行SQL语句,支持建表、查询、插入数据等操作")
public class SqlExecuteController {

    private final SqlExecuteService sqlExecuteService;

    public SqlExecuteController(SqlExecuteService sqlExecuteService) {
        this.sqlExecuteService = sqlExecuteService;
    }

    /**
     * 执行SQL语句
     */
    @PostMapping("/execute")
    @Operation(summary = "执行SQL语句", description = "执行SQL语句,自动识别SQL类型(DDL/DML/QUERY)")
    public ResponseEntity<SqlExecuteResponse> executeSql(@Valid @RequestBody SqlExecuteRequest request) {
        log.info("收到SQL执行请求: dbType={}, sql={}", request.getDbType(), request.getSql());
        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 执行建表语句
     */
    @PostMapping("/ddl")
    @Operation(summary = "执行DDL语句", description = "执行建表、修改表结构等DDL语句")
    public ResponseEntity<SqlExecuteResponse> executeDdl(
            @Parameter(description = "SQL语句") @RequestParam String sql,
            @Parameter(description = "数据库类型") @RequestParam(defaultValue = "dm") String dbType) {
        
        SqlExecuteRequest request = SqlExecuteRequest.builder()
                .sql(sql)
                .dbType(dbType)
                .sqlType("DDL")
                .autoDetect(false)
                .build();
        
        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 执行查询语句
     */
    @PostMapping("/query")
    @Operation(summary = "执行查询语句", description = "执行SELECT查询语句")
    public ResponseEntity<SqlExecuteResponse> executeQuery(
            @Parameter(description = "SQL语句") @RequestParam String sql,
            @Parameter(description = "数据库类型") @RequestParam(defaultValue = "dm") String dbType) {
        
        SqlExecuteRequest request = SqlExecuteRequest.builder()
                .sql(sql)
                .dbType(dbType)
                .sqlType("QUERY")
                .autoDetect(false)
                .build();
        
        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 执行插入/更新/删除语句
     */
    @PostMapping("/dml")
    @Operation(summary = "执行DML语句", description = "执行INSERT、UPDATE、DELETE等DML语句")
    public ResponseEntity<SqlExecuteResponse> executeDml(
            @Parameter(description = "SQL语句") @RequestParam String sql,
            @Parameter(description = "数据库类型") @RequestParam(defaultValue = "dm") String dbType) {
        
        SqlExecuteRequest request = SqlExecuteRequest.builder()
                .sql(sql)
                .dbType(dbType)
                .sqlType("DML")
                .autoDetect(false)
                .build();
        
        SqlExecuteResponse response = sqlExecuteService.executeSql(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 批量执行SQL语句
     */
    @PostMapping("/batch")
    @Operation(summary = "批量执行SQL", description = "批量执行多条SQL语句")
    public ResponseEntity<SqlExecuteResponse> executeBatch(
            @Parameter(description = "SQL语句列表,用分号分隔") @RequestParam String sqls,
            @Parameter(description = "数据库类型") @RequestParam(defaultValue = "dm") String dbType) {
        
        String[] sqlArray = sqls.split(";");
        int successCount = 0;
        int failCount = 0;
        StringBuilder message = new StringBuilder();
        
        for (String sql : sqlArray) {
            sql = sql.trim();
            if (sql.isEmpty()) {
                continue;
            }
            
            SqlExecuteRequest request = SqlExecuteRequest.builder()
                    .sql(sql)
                    .dbType(dbType)
                    .autoDetect(true)
                    .build();
            
            SqlExecuteResponse response = sqlExecuteService.executeSql(request);
            if (response.getSuccess()) {
                successCount++;
            } else {
                failCount++;
                message.append("失败: ").append(sql).append(" - ").append(response.getMessage()).append("\n");
            }
        }
        
        return ResponseEntity.ok(SqlExecuteResponse.builder()
                .success(failCount == 0)
                .message(String.format("批量执行完成: 成功%d条, 失败%d条\n%s", successCount, failCount, message))
                .affectedRows(successCount)
                .build());
    }
}

