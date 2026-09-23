package com.huabo.fxgl.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.DataModelQueryDTO;
import com.huabo.fxgl.dto.DataModelSaveDTO;
import com.huabo.fxgl.dto.DataSourceQueryDTO;
import com.huabo.fxgl.dto.DataSourceSaveDTO;
import com.huabo.fxgl.dto.EvaluationModelQueryDTO;
import com.huabo.fxgl.dto.TableQueryDTO;
import com.huabo.fxgl.entity.TblEvaluationModel;
import com.huabo.fxgl.service.IDataModelService;
import com.huabo.fxgl.service.IDataSourceService;
import com.huabo.fxgl.service.IEvaluationModelService;
import com.huabo.fxgl.service.IRiskWarningService;
import com.huabo.fxgl.service.ITableService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 模型管理控制器
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Slf4j
@RestController
@RequestMapping("/model")
@Tag(name="模型管理",description="模型管理")
public class ModelController {

    @Autowired
    private IDataSourceService dataSourceService;

    @Autowired
    private IDataModelService dataModelService;

    @Autowired
    private ITableService tableService;

    @Autowired
    private IEvaluationModelService evaluationModelService;

    @Autowired
    private IRiskWarningService riskWarningService;

    @Autowired
    private UserProvider userProvider;

    // =====================================================
    // 数据源管理API
    // =====================================================

    @PostMapping("/datasource/list")
    @Operation(summary = "分页查询数据源列表")
    public String getDataSourceList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNum", description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(name = "pageSize", description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(name = "sourceName", description = "数据源名称") @RequestParam(required = false) String sourceName,
            @Parameter(name = "sourceType", description = "数据源类型") @RequestParam(required = false) String sourceType,
            @Parameter(name = "status", description = "状态") @RequestParam(required = false) String status) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 构建查询DTO
            DataSourceQueryDTO queryDTO = new DataSourceQueryDTO();
            queryDTO.setPageNum(pageNum);
            queryDTO.setPageSize(pageSize);
            queryDTO.setSourceName(sourceName);
            queryDTO.setSourceType(sourceType);
            queryDTO.setStatus(status);

            JsonBean result = dataSourceService.getDataSourceList(queryDTO);
            return result.toString();
        } catch (Exception e) {
            log.error("查询数据源列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/datasource/save")
    @Operation(summary = "保存数据源配置")
    public String saveDataSource(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sourceId", description = "数据源ID") @RequestParam(required = false) String sourceId,
            @Parameter(name = "sourceName", description = "数据源名称") @RequestParam String sourceName,
            @Parameter(name = "sourceType", description = "数据源类型") @RequestParam String sourceType,
            @Parameter(name = "hostIp", description = "主机IP") @RequestParam String hostIp,
            @Parameter(name = "port", description = "端口") @RequestParam Integer port,
            @Parameter(name = "databaseName", description = "数据库名") @RequestParam String databaseName,
            @Parameter(name = "username", description = "用户名") @RequestParam String username,
            @Parameter(name = "password", description = "密码") @RequestParam String password,
            @Parameter(name = "description", description = "描述") @RequestParam(required = false) String description) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();

            // 构建保存DTO
            DataSourceSaveDTO saveDTO = new DataSourceSaveDTO();
            saveDTO.setSourceId(sourceId);
            saveDTO.setSourceName(sourceName);
            saveDTO.setSourceType(sourceType);
            saveDTO.setHostIp(hostIp);
            saveDTO.setPort(port);
            saveDTO.setDatabaseName(databaseName);
            saveDTO.setUsername(username);
            saveDTO.setPassword(password);
            saveDTO.setDescription(description);

            JsonBean result = dataSourceService.saveDataSource(saveDTO, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("保存数据源失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/datasource/delete/{sourceId}")
    @Operation(summary = "删除数据源")
    public String deleteDataSource(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sourceId", description = "数据源ID", required = true) @PathVariable String sourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.deleteDataSource(sourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("删除数据源失败", e);
            return JsonBean.error("删除失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/datasource/batchDelete")
    @Operation(summary = "批量删除数据源")
    public String batchDeleteDataSource(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody List<String> sourceIds) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.batchDeleteDataSource(sourceIds);
            return result.toString();
        } catch (Exception e) {
            log.error("批量删除数据源失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/datasource/detail/{sourceId}")
    @Operation(summary = "获取数据源详情")
    public String getDataSourceDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sourceId", description = "数据源ID", required = true) @PathVariable String sourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.getDataSourceDetail(sourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("查询数据源详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/datasource/testConnection/{sourceId}")
    @Operation(summary = "测试数据源连接")
    public String testDataSourceConnection(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sourceId", description = "数据源ID", required = true) @PathVariable String sourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.testDataSourceConnection(sourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("测试数据源连接失败", e);
            return JsonBean.error("测试失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/datasource/syncTables/{sourceId}")
    @Operation(summary = "同步表结构")
    public String syncTableStructure(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sourceId", description = "数据源ID", required = true) @PathVariable String sourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.syncTableStructure(sourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("同步表结构失败", e);
            return JsonBean.error("同步失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/datasource/activeList")
    @Operation(summary = "获取活跃数据源列表")
    public String getActiveDataSources(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.getActiveDataSources();
            return result.toString();
        } catch (Exception e) {
            log.error("查询活跃数据源失败", e);
            return JsonBean.error("查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/datasource/statistics")
    @Operation(summary = "获取数据源统计信息")
    public String getDataSourceStatistics(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.getDataSourceStatistics();
            return result.toString();
        } catch (Exception e) {
            log.error("查询数据源统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/datasource/enable/{sourceId}")
    @Operation(summary = "启用数据源")
    public String enableDataSource(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sourceId", description = "数据源ID", required = true) @PathVariable String sourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.enableDataSource(sourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("启用数据源失败", e);
            return JsonBean.error("启用失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/datasource/disable/{sourceId}")
    @Operation(summary = "禁用数据源")
    public String disableDataSource(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sourceId", description = "数据源ID", required = true) @PathVariable String sourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataSourceService.disableDataSource(sourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("禁用数据源失败", e);
            return JsonBean.error("禁用失败: " + e.getMessage()).toString();
        }
    }

    // =====================================================
    // 表管理API
    // =====================================================

    @PostMapping("/table/list")
    @Operation(summary = "分页查询表列表")
    public String getTableList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNum", description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(name = "pageSize", description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(name = "dataSourceId", description = "数据源ID") @RequestParam(required = false) String dataSourceId,
            @Parameter(name = "tableName", description = "表名") @RequestParam(required = false) String tableName,
            @Parameter(name = "tableComment", description = "表注释") @RequestParam(required = false) String tableComment,
            @Parameter(name = "syncStatus", description = "同步状态") @RequestParam(required = false) String syncStatus) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 构建查询DTO
            TableQueryDTO queryDTO = new TableQueryDTO();
            queryDTO.setPageNum(pageNum);
            queryDTO.setPageSize(pageSize);
            queryDTO.setDataSourceId(dataSourceId);
            queryDTO.setTableName(tableName);
            queryDTO.setTableComment(tableComment);
            queryDTO.setSyncStatus(syncStatus);

            JsonBean result = tableService.getTableList(queryDTO);
            return result.toString();
        } catch (Exception e) {
            log.error("查询表列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/table/structureList")
    @Operation(summary = "获取表结构列表")
    public String getTableStructureList(@RequestBody TableQueryDTO queryDTO) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 设置默认值
            if (queryDTO.getPageNum() == null) {
                queryDTO.setPageNum(1);
            }
            if (queryDTO.getPageSize() == null) {
                queryDTO.setPageSize(15);
            }

            log.info("查询表结构列表，参数: {}", queryDTO);

            JsonBean result = tableService.getTableStructureDetails(queryDTO);
            return result.toString();
        } catch (Exception e) {
            log.error("查询表结构列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/table/statistics/{dataSourceId}")
    @Operation(summary = "获取数据源表统计信息")
    public String getTableStatistics(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "dataSourceId", description = "数据源ID", required = true) @PathVariable String dataSourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = tableService.getTableStatistics(dataSourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("获取表统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/table/columns")
    @Operation(summary = "获取表的列信息")
    public String getTableColumns(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "dataSourceId", description = "数据源ID", required = true) @RequestParam String dataSourceId,
            @Parameter(name = "tableName", description = "表名", required = true) @RequestParam String tableName) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = tableService.getTableColumns(dataSourceId, tableName);
            return result.toString();
        } catch (Exception e) {
            log.error("获取表列信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/table/preview")
    @Operation(summary = "预览表数据")
    public String previewTableData(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "dataSourceId", description = "数据源ID", required = true) @RequestParam String dataSourceId,
            @Parameter(name = "tableName", description = "表名", required = true) @RequestParam String tableName,
            @Parameter(name = "limit", description = "限制条数", required = false) @RequestParam(defaultValue = "100") Integer limit,
            @Parameter(name = "offset", description = "偏移量", required = false) @RequestParam(defaultValue = "0") Integer offset) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = tableService.previewTableData(dataSourceId, tableName, limit, offset);
            return result.toString();
        } catch (Exception e) {
            log.error("预览表数据失败", e);
            return JsonBean.error("预览失败: " + e.getMessage());
        }
    }

    @PostMapping("/table/generateDDL")
    @Operation(summary = "生成表DDL语句")
    public String generateTableDDL(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "dataSourceId", description = "数据源ID", required = true) @RequestParam String dataSourceId,
            @Parameter(name = "tableName", description = "表名", required = true) @RequestParam String tableName) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = tableService.generateTableDDL(dataSourceId, tableName);
            return result.toString();
        } catch (Exception e) {
            log.error("生成表DDL失败", e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }

    @PostMapping("/table/debug/{dataSourceId}")
    @Operation(summary = "调试表查询结果")
    public String debugTableQuery(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "dataSourceId", description = "数据源ID", required = true) @PathVariable String dataSourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = tableService.debugTableQuery(dataSourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("调试表查询失败", e);
            return JsonBean.error("调试失败: " + e.getMessage());
        }
    }

    // =====================================================
    // SQL模板管理相关接口
    // =====================================================

    /**
     * 分页查询SQL模板列表
     */
    @PostMapping(value = "/template/list")
    @Operation(summary = "分页查询SQL模板列表", description = "支持多条件筛选的SQL模板分页查询")
    public String getSqlTemplateList(
            HttpServletRequest request,
            @Parameter(name = "templateName", description = "模板名称") @RequestParam(required = false) String templateName,
            @Parameter(name = "templateType", description = "模板类型") @RequestParam(required = false) String templateType,
            @Parameter(name = "templateCategory", description = "模板分类") @RequestParam(required = false) String templateCategory,
            @Parameter(name = "isSystem", description = "是否系统模板") @RequestParam(required = false) String isSystem,
            @Parameter(name = "isEnabled", description = "是否启用(Y/N)") @RequestParam(required = false) String isEnabled,
            @Parameter(name = "status", description = "状态(兼容旧参数)") @RequestParam(required = false) String status,
            @Parameter(name = "pageNum", description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(name = "pageSize", description = "每页条数") @RequestParam(defaultValue = "20") Integer pageSize) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 智能解析请求参数 - 支持JSON和form-data两种格式
            Map<String, Object> params = new HashMap<>();
            String contentType = request.getContentType();
            log.info("请求Content-Type: {}", contentType);

            // 检查是否为JSON请求
            if (contentType != null && contentType.contains("application/json")) {
                // JSON格式请求 - 从请求体读取
                try {
                    BufferedReader reader = request.getReader();
                    StringBuilder jsonBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }
                    String jsonString = jsonBuilder.toString();
                    log.info("接收到JSON请求体: {}", jsonString);

                    if (!jsonString.trim().isEmpty()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> jsonParams = objectMapper.readValue(jsonString, Map.class);

                        // 从JSON中提取参数
                        if (jsonParams.containsKey("templateName") && jsonParams.get("templateName") != null) {
                            params.put("templateName", jsonParams.get("templateName").toString().trim());
                        }
                        if (jsonParams.containsKey("templateType") && jsonParams.get("templateType") != null) {
                            params.put("templateType", jsonParams.get("templateType").toString().trim());
                        }
                        if (jsonParams.containsKey("templateCategory") && jsonParams.get("templateCategory") != null) {
                            params.put("templateCategory", jsonParams.get("templateCategory").toString().trim());
                        }
                        if (jsonParams.containsKey("isSystem") && jsonParams.get("isSystem") != null) {
                            params.put("isSystem", jsonParams.get("isSystem").toString().trim());
                        }

                        // 处理启用状态参数 - 优先使用isEnabled，兼容status
                        String enabledStatus = null;
                        if (jsonParams.containsKey("isEnabled") && jsonParams.get("isEnabled") != null) {
                            enabledStatus = jsonParams.get("isEnabled").toString().trim();
                        } else if (jsonParams.containsKey("status") && jsonParams.get("status") != null) {
                            enabledStatus = jsonParams.get("status").toString().trim();
                        }
                        if (enabledStatus != null && !enabledStatus.isEmpty()) {
                            params.put("isEnabled", enabledStatus);
                            log.info("从JSON设置启用状态查询条件: isEnabled = {}", enabledStatus);
                        }

                        // 分页参数
                        if (jsonParams.containsKey("pageNum") && jsonParams.get("pageNum") != null) {
                            params.put("pageNum", Integer.valueOf(jsonParams.get("pageNum").toString()));
                        } else {
                            params.put("pageNum", 1);
                        }
                        if (jsonParams.containsKey("pageSize") && jsonParams.get("pageSize") != null) {
                            params.put("pageSize", Integer.valueOf(jsonParams.get("pageSize").toString()));
                        } else {
                            params.put("pageSize", 20);
                        }
                    }
                } catch (Exception jsonEx) {
                    log.warn("解析JSON请求体失败，回退到form参数模式: {}", jsonEx.getMessage());
                }
            }

            // 如果JSON解析失败或者是form-data请求，使用form参数
            if (params.isEmpty()) {
                log.info("使用form参数模式");

                if (templateName != null && !templateName.trim().isEmpty()) {
                    params.put("templateName", templateName.trim());
                }
                if (templateType != null && !templateType.trim().isEmpty()) {
                    params.put("templateType", templateType.trim());
                }
                if (templateCategory != null && !templateCategory.trim().isEmpty()) {
                    params.put("templateCategory", templateCategory.trim());
                }
                if (isSystem != null && !isSystem.trim().isEmpty()) {
                    params.put("isSystem", isSystem.trim());
                }

                // 处理启用状态参数 - 优先使用isEnabled，兼容status
                String enabledStatus = null;
                if (isEnabled != null && !isEnabled.trim().isEmpty()) {
                    enabledStatus = isEnabled.trim();
                } else if (status != null && !status.trim().isEmpty()) {
                    enabledStatus = status.trim();
                }
                if (enabledStatus != null) {
                    params.put("isEnabled", enabledStatus);
                    log.info("从form设置启用状态查询条件: isEnabled = {}", enabledStatus);
                }

                params.put("pageNum", pageNum);
                params.put("pageSize", pageSize);
            }

            log.info("最终查询参数: {}", params);
            JsonBean result = tableService.getSqlTemplateList(params);
            return result.toString();
        } catch (Exception e) {
            log.error("查询SQL模板列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取SQL模板统计信息
     */
    @PostMapping(value = "/template/statistics", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "获取SQL模板统计信息", description = "获取模板总数、分类统计、使用情况等信息")
    public String getSqlTemplateStatistics(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = tableService.getSqlTemplateStatistics();
            return result.toString();
        } catch (Exception e) {
            log.error("获取SQL模板统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取SQL模板详情
     */
    @PostMapping(value = "/template/detail/{templateId}", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "获取SQL模板详情", description = "根据模板ID获取SQL模板的详细信息")
    public String getSqlTemplateDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "templateId", description = "模板ID", required = true) @PathVariable String templateId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String result = tableService.getSqlTemplateDetail(templateId, staff);
            return result;
        } catch (Exception e) {
            log.error("获取SQL模板详情失败", e);
            return JsonBean.error("获取详情失败: " + e.getMessage());
        }
    }

    /**
     * 保存SQL模板
     */
    @PostMapping(value = "/template/save", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "保存SQL模板", description = "新增或更新SQL模板")
    public String saveSqlTemplate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "templateId", description = "模板ID") @RequestParam(required = false) String templateId,
            @Parameter(name = "templateName", description = "模板名称") @RequestParam(required = false) String templateName,
            @Parameter(name = "templateCode", description = "模板编码") @RequestParam(required = false) String templateCode,
            @Parameter(name = "dataSourceId", description = "数据源ID") @RequestParam(required = false) String dataSourceId,
            @Parameter(name = "templateType", description = "模板类型") @RequestParam(required = false) String templateType,
            @Parameter(name = "category", description = "业务场景") @RequestParam(required = false) String category,
            @Parameter(name = "description", description = "模板描述") @RequestParam(required = false) String description,
            @Parameter(name = "sqlContent", description = "SQL内容") @RequestParam(required = false) String sqlContent,
            @Parameter(name = "status", description = "状态") @RequestParam(required = false) String status,
            @Parameter(name = "isPublic", description = "是否公开") @RequestParam(required = false) Boolean isPublic,
            @Parameter(name = "parameters", description = "参数配置JSON") @RequestParam(required = false) String parameters,
            HttpServletRequest request) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 构建模板数据Map
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("templateId", templateId);
            templateData.put("templateName", templateName);
            templateData.put("templateCode", templateCode);
            templateData.put("dataSourceId", dataSourceId);
            templateData.put("templateType", templateType);
            templateData.put("category", category);
            templateData.put("description", description);
            templateData.put("sqlContent", sqlContent);
            templateData.put("status", status);
            templateData.put("isPublic", isPublic);

            // 调试信息
            log.info("接收到的参数 - dataSourceId: {}, templateName: {}, category: {}", dataSourceId, templateName, category);

            // 处理参数配置
            List<Map<String, Object>> paramList = new ArrayList<>();

            // 首先尝试从JSON字符串解析参数
            if (StringUtil.isNotEmpty(parameters)) {
                try {
                    paramList = new ObjectMapper().readValue(parameters, List.class);
                } catch (Exception e) {
                    log.warn("参数配置JSON解析失败: " + e.getMessage());
                }
            }

            // 如果JSON解析失败或为空，尝试从表单参数解析
            if (paramList.isEmpty()) {
                paramList = parseFormParameters(request);
            }

            templateData.put("parameters", paramList);

            String result = tableService.saveSqlTemplate(templateData, staff);
            return result;
        } catch (Exception e) {
            log.error("保存SQL模板失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除SQL模板
     */
    @PostMapping(value = "/template/delete/{templateId}", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "删除SQL模板", description = "根据模板ID删除SQL模板")
    public String deleteSqlTemplate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "templateId", description = "模板ID", required = true) @PathVariable String templateId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String result = tableService.deleteSqlTemplate(templateId, staff);
            return result;
        } catch (Exception e) {
            log.error("删除SQL模板失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除SQL模板
     */
    @PostMapping(value = "/template/batchDelete", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "批量删除SQL模板", description = "批量删除多个SQL模板")
    public String batchDeleteSqlTemplate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "templateIds", description = "模板ID列表") @RequestParam(required = false) String[] templateIds,
            HttpServletRequest request) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("接收到批量删除请求 - templateIds: {}", Arrays.toString(templateIds));

            // 处理模板ID列表
            List<String> templateIdList = new ArrayList<>();

            // 优先使用直接传递的数组参数
            if (templateIds != null && templateIds.length > 0) {
                templateIdList = Arrays.asList(templateIds);
                log.info("从数组参数解析到模板ID: {}", templateIdList);
            } else {
                // 尝试从request中获取数组格式的参数
                String[] paramArray = request.getParameterValues("templateIds");
                if (paramArray != null && paramArray.length > 0) {
                    templateIdList = Arrays.asList(paramArray);
                    log.info("从request参数解析到模板ID: {}", templateIdList);
                } else {
                    // 尝试解析JSON格式
                    String templateIdsJson = request.getParameter("templateIds");
                    if (StringUtil.isNotEmpty(templateIdsJson)) {
                        try {
                            templateIdList = new ObjectMapper().readValue(templateIdsJson, List.class);
                            log.info("从JSON参数解析到模板ID: {}", templateIdList);
                        } catch (Exception e) {
                            log.warn("JSON解析失败: {}", e.getMessage());
                        }
                    }
                }
            }

            if (templateIdList.isEmpty()) {
                return JsonBean.error("请选择要删除的模板");
            }

            String result = tableService.batchDeleteSqlTemplate(templateIdList, staff);
            return result;
        } catch (Exception e) {
            log.error("批量删除SQL模板失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 解析表单格式的参数数据
     * 处理类似 parameters[0][name]=yx, parameters[0][type]=Number 的格式
     */
    private List<Map<String, Object>> parseFormParameters(HttpServletRequest request) {
        List<Map<String, Object>> paramList = new ArrayList<>();
        Map<String, String[]> parameterMap = request.getParameterMap();

        // 用于存储按索引分组的参数
        Map<Integer, Map<String, Object>> indexedParams = new HashMap<>();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();

            // 匹配 parameters[index][field] 格式
            if (paramName.startsWith("parameters[") && paramName.contains("][") && paramName.endsWith("]")) {
                try {
                    // 提取索引和字段名
                    int firstBracket = paramName.indexOf('[');
                    int secondBracket = paramName.indexOf("][");
                    int lastBracket = paramName.lastIndexOf(']');

                    String indexStr = paramName.substring(firstBracket + 1, secondBracket);
                    String fieldName = paramName.substring(secondBracket + 2, lastBracket);

                    int index = Integer.parseInt(indexStr);
                    String value = paramValues.length > 0 ? paramValues[0] : "";

                    // 按索引分组参数
                    indexedParams.computeIfAbsent(index, k -> new HashMap<>()).put(fieldName, value);
                } catch (Exception e) {
                    log.warn("解析表单参数失败: " + paramName, e);
                }
            }
        }

        // 按索引顺序构建参数列表
        for (int i = 0; i < indexedParams.size(); i++) {
            Map<String, Object> param = indexedParams.get(i);
            if (param != null && !param.isEmpty()) {
                paramList.add(param);
            }
        }

        log.info("解析表单参数成功，共解析到 {} 个参数", paramList.size());
        return paramList;
    }

    /**
     * 验证SQL语法
     */
    @PostMapping(value = "/template/validateSql", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "验证SQL语法", description = "验证SQL语句的语法正确性")
    public String validateSqlSyntax(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sqlContent", description = "SQL内容", required = true) @RequestParam String sqlContent,
            @Parameter(name = "databaseType", description = "数据库类型") @RequestParam(required = false) String databaseType) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            if (StringUtil.isEmpty(sqlContent)) {
                return JsonBean.error("SQL内容不能为空");
            }

            log.info("验证SQL语法 - 用户: {}, 数据库类型: {}", staff.getStaffid(), databaseType);

            // 调用Service层验证SQL
            JsonBean result = tableService.validateSqlSyntax(sqlContent, databaseType, staff);
            return result.toString();
        } catch (Exception e) {
            log.error("验证SQL语法失败", e);
            return JsonBean.error("验证失败: " + e.getMessage());
        }
    }

    /**
     * 使用SQL模板（更新使用次数）
     */
    @PostMapping("/template/use/{templateId}")
    @Operation(summary = "使用SQL模板", description = "记录模板使用次数，更新最后使用时间")
    public String useSqlTemplate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "templateId", description = "模板ID", required = true) @PathVariable String templateId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            if (StringUtil.isEmpty(templateId)) {
                return JsonBean.error("模板ID不能为空");
            }

            log.info("使用模板 - templateId: {}, 用户: {}", templateId, staff.getStaffid());

            // 调用Service层更新使用记录
            String result = tableService.useSqlTemplate(templateId, staff);
            return result;
        } catch (Exception e) {
            log.error("使用SQL模板失败", e);
            return JsonBean.error("使用模板失败: " + e.getMessage());
        }
    }

    /**
     * 单个切换SQL模板状态
     */
    @PostMapping("/template/toggleStatus")
    @Operation(summary = "切换SQL模板状态", description = "启用或禁用单个SQL模板")
    public String toggleSqlTemplateStatus(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "templateId", description = "模板ID") @RequestParam(required = false) String templateId,
            @Parameter(name = "isEnabled", description = "是否启用(Y:启用,N:禁用)") @RequestParam(required = false) String isEnabled,
            HttpServletRequest request) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            if (StringUtil.isEmpty(templateId)) {
                return JsonBean.error("模板ID不能为空");
            }

            if (StringUtil.isEmpty(isEnabled)) {
                return JsonBean.error("请指定状态");
            }

            log.info("切换模板状态 - templateId: {}, isEnabled: {}", templateId, isEnabled);

            // 调用Service层批量更新状态（单个也用批量接口）
            boolean success = tableService.batchUpdateSqlTemplateStatus(Arrays.asList(templateId), isEnabled, staff.getStaffid().toString());

            if (success) {
                String action = "Y".equals(isEnabled) ? "启用" : "禁用";
                return JsonBean.success(action + "成功");
            } else {
                return JsonBean.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("切换SQL模板状态失败", e);
            return JsonBean.error("状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 批量更新SQL模板状态
     */
    @PostMapping("/template/batchUpdateStatus")
    @Operation(summary = "批量更新SQL模板状态", description = "批量启用或禁用SQL模板")
    public String batchUpdateSqlTemplateStatus(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "templateIds", description = "模板ID列表") @RequestParam(required = false) String[] templateIds,
            @Parameter(name = "status", description = "状态(Y:启用,N:禁用) - 兼容旧版本") @RequestParam(required = false) String status,
            @Parameter(name = "isEnabled", description = "启用状态(Y:启用,N:禁用) - 推荐使用") @RequestParam(required = false) String isEnabled,
            HttpServletRequest request) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 处理参数：支持多种格式
            List<String> templateIdList = new ArrayList<>();

            // 方式1：直接从数组参数获取
            if (templateIds != null && templateIds.length > 0) {
                templateIdList = Arrays.asList(templateIds);
                log.info("从数组参数获取模板ID: {}", templateIdList);
            } else {
                // 方式2：从request中解析JSON格式的参数
                String templateIdsParam = request.getParameter("templateIds");
                if (StringUtil.isNotEmpty(templateIdsParam)) {
                    try {
                        // 尝试解析JSON数组格式
                        if (templateIdsParam.startsWith("[") && templateIdsParam.endsWith("]")) {
                            templateIdList = new ObjectMapper().readValue(templateIdsParam, List.class);
                            log.info("从JSON参数解析模板ID: {}", templateIdList);
                        } else {
                            // 单个ID的情况
                            templateIdList.add(templateIdsParam);
                            log.info("单个模板ID: {}", templateIdList);
                        }
                    } catch (Exception e) {
                        log.warn("JSON解析失败，尝试其他方式: {}", e.getMessage());
                    }
                }

                // 方式3：从request中获取数组格式的参数（支持 templateIds[0], templateIds[1] 格式）
                if (templateIdList.isEmpty()) {
                    String[] paramArray = request.getParameterValues("templateIds");
                    if (paramArray != null && paramArray.length > 0) {
                        templateIdList = Arrays.asList(paramArray);
                        log.info("从数组参数解析模板ID: {}", templateIdList);
                    } else {
                        // 处理 templateIds[0], templateIds[1] 这种格式
                        Map<String, String[]> paramMap = request.getParameterMap();
                        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                            String paramName = entry.getKey();
                            if (paramName.startsWith("templateIds[") && paramName.endsWith("]")) {
                                String[] values = entry.getValue();
                                if (values != null && values.length > 0) {
                                    templateIdList.add(values[0]);
                                }
                            }
                        }
                        log.info("从索引参数解析模板ID: {}", templateIdList);
                    }
                }
            }

            if (templateIdList.isEmpty()) {
                return JsonBean.error("请选择要操作的模板");
            }

            // 统一状态参数处理 - isEnabled优先于status
            String finalStatus = null;
            if (StringUtil.isNotEmpty(isEnabled)) {
                finalStatus = isEnabled;
                log.info("使用isEnabled参数: {}", isEnabled);
            } else if (StringUtil.isNotEmpty(status)) {
                finalStatus = status;
                log.info("使用status参数(兼容模式): {}", status);
            }

            if (StringUtil.isEmpty(finalStatus)) {
                return JsonBean.error("请指定状态");
            }

            log.info("批量更新模板状态 - templateIds: {}, finalStatus: {}", templateIdList, finalStatus);

            // 调用Service层批量更新状态
            boolean success = tableService.batchUpdateSqlTemplateStatus(templateIdList, finalStatus, staff.getStaffid().toString());

            if (success) {
                return JsonBean.success("批量更新状态成功");
            } else {
                return JsonBean.error("批量更新状态失败");
            }
        } catch (Exception e) {
            log.error("批量更新SQL模板状态失败", e);
            return JsonBean.error("批量更新状态失败: " + e.getMessage());
        }
    }

    /**
     * 复制SQL模板
     */
    @PostMapping("/template/copy")
    @Operation(summary = "复制SQL模板", description = "复制现有模板创建新模板")
    public String copySqlTemplate(
            HttpServletRequest request,
            @Parameter(name = "templateId", description = "源模板ID") @RequestParam(required = false) String templateId,
            @Parameter(name = "newTemplateName", description = "新模板名称") @RequestParam(required = false) String newTemplateName) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 智能解析请求参数 - 支持JSON和form-data两种格式
            String finalTemplateId = templateId;
            String finalNewTemplateName = newTemplateName;

            String contentType = request.getContentType();
            log.info("复制模板请求Content-Type: {}", contentType);

            // 检查是否为JSON请求
            if (contentType != null && contentType.contains("application/json")) {
                try {
                    BufferedReader reader = request.getReader();
                    StringBuilder jsonBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }
                    String jsonString = jsonBuilder.toString();
                    log.info("接收到JSON请求体: {}", jsonString);

                    if (!jsonString.trim().isEmpty()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> jsonParams = objectMapper.readValue(jsonString, Map.class);

                        if (jsonParams.containsKey("templateId") && jsonParams.get("templateId") != null) {
                            finalTemplateId = jsonParams.get("templateId").toString().trim();
                        }
                        if (jsonParams.containsKey("newTemplateName") && jsonParams.get("newTemplateName") != null) {
                            finalNewTemplateName = jsonParams.get("newTemplateName").toString().trim();
                        }
                    }
                } catch (Exception jsonEx) {
                    log.warn("解析JSON请求体失败，使用form参数: {}", jsonEx.getMessage());
                }
            }

            log.info("复制SQL模板参数: templateId={}, newTemplateName={}", finalTemplateId, finalNewTemplateName);

            // 参数验证
            if (StringUtil.isEmpty(finalTemplateId)) {
                return JsonBean.error("源模板ID不能为空");
            }
            if (StringUtil.isEmpty(finalNewTemplateName)) {
                return JsonBean.error("新模板名称不能为空");
            }

            // 调用Service层复制方法
            String result = tableService.copySqlTemplate(finalTemplateId, finalNewTemplateName, staff);
            return result;

        } catch (Exception e) {
            log.error("复制SQL模板失败", e);
            return JsonBean.error("复制失败: " + e.getMessage());
        }
    }

    /**
     * 测试SQL语句执行
     */
    @PostMapping("/template/test")
    @Operation(summary = "测试SQL语句执行", description = "在指定数据源上测试SQL语句")
    public String testSqlExecution(@RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("接收到测试SQL请求: {}", requestBody);

            // 解析请求参数
            String dataSourceId = (String) requestBody.get("dataSourceId");
            String sqlContent = (String) requestBody.get("sqlContent");
            List<Map<String, Object>> parameters = new ArrayList<>();

            if (requestBody.containsKey("parameters") && requestBody.get("parameters") != null) {
                Object paramObj = requestBody.get("parameters");
                log.info("参数对象类型: {}, 内容: {}", paramObj.getClass().getName(), paramObj);
                if (paramObj instanceof List) {
                    parameters = (List<Map<String, Object>>) paramObj;
                    log.info("解析到参数列表，数量: {}", parameters.size());
                }
            }

            log.info("测试SQL参数: dataSourceId={}, sqlContent={}, parameters={}", dataSourceId, sqlContent, parameters);

            // 参数验证
            if (StringUtil.isEmpty(dataSourceId)) {
                return JsonBean.error("数据源ID不能为空");
            }
            if (StringUtil.isEmpty(sqlContent)) {
                return JsonBean.error("SQL内容不能为空");
            }

            // 调用Service层测试方法
            String result = tableService.testSqlExecution(dataSourceId, sqlContent, parameters, staff);
            return result;

        } catch (Exception e) {
            log.error("测试SQL执行失败", e);
            return JsonBean.error("测试失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 数据模型管理API
    // =====================================================

    @PostMapping("/datamodel/list")
    @Operation(summary = "分页查询数据模型列表")
    public String getDataModelList(@RequestBody DataModelQueryDTO queryDTO) {
        try {
            // 临时移除Token验证用于测试
            log.info("查询数据模型列表，参数: {}", queryDTO);

            // 设置默认值
            if (queryDTO.getPageNum() == null) {
                queryDTO.setPageNum(1);
            }
            if (queryDTO.getPageSize() == null) {
                queryDTO.setPageSize(10);
            }

            log.info("查询数据模型列表，参数: {}", queryDTO);

            JsonBean result = dataModelService.getDataModelList(queryDTO);
            return result.toString();
        } catch (Exception e) {
            log.error("查询数据模型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/save")
    @Operation(summary = "保存数据模型")
    public String saveDataModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody DataModelSaveDTO saveDTO) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();

            // 保存为普通数据模型（移除评估模型的特殊处理）
            JsonBean result = dataModelService.saveDataModel(saveDTO, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("保存数据模型失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/delete/{modelId}")
    @Operation(summary = "删除数据模型")
    public String deleteDataModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataModelService.deleteDataModel(modelId);
            return result.toString();
        } catch (Exception e) {
            log.error("删除数据模型失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/batchDelete")
    @Operation(summary = "批量删除数据模型")
    public String batchDeleteDataModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelIds", description = "模型ID列表", required = true) @RequestParam String modelIds) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            if (StringUtil.isEmpty(modelIds)) {
                return JsonBean.error("请选择要删除的数据模型");
            }

            List<String> modelIdList = Arrays.asList(modelIds.split(","));
            JsonBean result = dataModelService.batchDeleteDataModel(modelIdList);
            return result.toString();
        } catch (Exception e) {
            log.error("批量删除数据模型失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/detail/{modelId}")
    @Operation(summary = "获取数据模型详情")
    public String getDataModelDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataModelService.getDataModelDetail(modelId);
            return result.toString();
        } catch (Exception e) {
            log.error("查询数据模型详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/copy")
    @Operation(summary = "复制数据模型")
    public String copyDataModel(
            HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID") @RequestParam(required = false) String modelId,
            @Parameter(name = "newModelName", description = "新模型名称") @RequestParam(required = false) String newModelName,
            @Parameter(name = "newModelCode", description = "新模型编码") @RequestParam(required = false) String newModelCode) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 智能解析请求参数 - 支持JSON和form-data两种格式
            String finalModelId = modelId;
            String finalNewModelName = newModelName;
            String finalNewModelCode = newModelCode;

            String contentType = request.getContentType();
            log.info("复制数据模型请求Content-Type: {}", contentType);

            // 检查是否为JSON请求
            if (contentType != null && contentType.contains("application/json")) {
                try {
                    BufferedReader reader = request.getReader();
                    StringBuilder jsonBody = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBody.append(line);
                    }

                    String jsonStr = jsonBody.toString();
                    log.info("复制数据模型JSON请求体: {}", jsonStr);

                    if (StringUtil.isNotEmpty(jsonStr)) {
                        JSONObject jsonObject = JSON.parseObject(jsonStr);
                        if (jsonObject.containsKey("modelId")) {
                            finalModelId = jsonObject.getString("modelId");
                        }
                        if (jsonObject.containsKey("newModelName")) {
                            finalNewModelName = jsonObject.getString("newModelName");
                        }
                        if (jsonObject.containsKey("newModelCode")) {
                            finalNewModelCode = jsonObject.getString("newModelCode");
                        }
                    }
                } catch (Exception jsonEx) {
                    log.warn("解析JSON请求体失败，使用form参数: {}", jsonEx.getMessage());
                }
            }

            log.info("复制数据模型参数: modelId={}, newModelName={}, newModelCode={}",
                    finalModelId, finalNewModelName, finalNewModelCode);

            // 参数验证
            if (StringUtil.isEmpty(finalModelId)) {
                return JsonBean.error("模型ID不能为空");
            }
            if (StringUtil.isEmpty(finalNewModelName)) {
                return JsonBean.error("新模型名称不能为空");
            }

            String currentUser = staff.getRealname();
            JsonBean result = dataModelService.copyDataModel(finalModelId, finalNewModelName, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("复制数据模型失败", e);
            return JsonBean.error("复制失败: " + e.getMessage());
        }
    }

    /**
     * 规则同步：把组合指标下的指标同步为数据模型（#TASK-2026-08-01-DATA-MODEL-SYNC）。
     * 请求体格式（均为可选字段）：
     * <pre>
     * {
     *   "combinationIds": ["C001", "C002"],   // 为空则同步全部组合
     *   "onlyEnabled": true                   // 是否仅同步启用的指标，默认 true
     * }
     * </pre>
     * 每个组合按 executionOrder 升序，跳过最后一个指标不同步。
     */
    @PostMapping("/datamodel/syncFromCombinations")
    @Operation(summary = "规则同步：从组合指标同步到数据模型")
    public String syncDataModelFromCombinations(
            @RequestHeader(value = "token", required = false) String token,
            @RequestBody(required = false) Map<String, Object> requestBody) {
        try {
            String currentUser = "system-sync";
            try {
                TblStaffUtil staff = userProvider.get();
                if (staff != null && StringUtil.isNotEmpty(staff.getRealname())) {
                    currentUser = staff.getRealname();
                }
            } catch (Exception ignore) {
                log.warn("[数据模型规则同步] 用户信息获取失败, 使用默认用户");
            }

            List<String> combinationIds = null;
            boolean onlyEnabled = true;
            if (requestBody != null) {
                Object cids = requestBody.get("combinationIds");
                if (cids instanceof List) {
                    combinationIds = new ArrayList<>();
                    for (Object o : (List<?>) cids) {
                        if (o != null) combinationIds.add(o.toString());
                    }
                }
                Object oe = requestBody.get("onlyEnabled");
                if (oe instanceof Boolean) {
                    onlyEnabled = (Boolean) oe;
                } else if (oe != null) {
                    onlyEnabled = !"false".equalsIgnoreCase(oe.toString());
                }
            }
            log.info("[数据模型规则同步] 请求: combinationIds={}, onlyEnabled={}, user={}",
                    combinationIds, onlyEnabled, currentUser);

            JsonBean result = dataModelService.syncFromCombinations(combinationIds, onlyEnabled, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("[数据模型规则同步] 失败", e);
            return JsonBean.error("同步失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/publish/{modelId}")
    @Operation(summary = "发布数据模型")
    public String publishDataModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();
            JsonBean result = dataModelService.publishDataModel(modelId, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("发布数据模型失败", e);
            return JsonBean.error("发布失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/execute/{modelId}")
    @Operation(summary = "执行数据模型")
    public String executeDataModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @PathVariable String modelId,
            HttpServletRequest request) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 解析请求参数
            Map<String, Object> parameters = new HashMap<>();
            try {
                BufferedReader reader = request.getReader();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String requestBody = sb.toString();

                if (StringUtil.isNotEmpty(requestBody)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    parameters = objectMapper.readValue(requestBody, Map.class);
                }
            } catch (Exception e) {
                log.warn("解析执行参数失败: {}", e.getMessage());
            }

            String currentUser = staff.getRealname();
            JsonBean result = dataModelService.executeDataModel(modelId, parameters, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("执行数据模型失败", e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    @PostMapping(value = "/datamodel/test", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "测试数据模型SQL")
    public String testDataModelSql(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sqlContent", description = "SQL内容") @RequestParam(required = false) String sqlContent,
            @Parameter(name = "dataSourceId", description = "数据源ID") @RequestParam(required = false) String dataSourceId,
            @Parameter(name = "parameters", description = "参数") @RequestParam(required = false) String parameters,
            @Parameter(name = "indicatorCode", description = "组合指标编码") @RequestParam(required = false) String indicatorCode,
            HttpServletRequest request) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 参数验证
            if (StringUtil.isEmpty(dataSourceId)) {
                return JsonBean.error("数据源ID不能为空");
            }
            if (StringUtil.isEmpty(sqlContent)) {
                return JsonBean.error("SQL内容不能为空");
            }

            log.info("测试数据模型SQL - dataSourceId: {}, sqlContent: {}", dataSourceId,
                    sqlContent.length() > 100 ? sqlContent.substring(0, 100) + "..." : sqlContent);

            // 解析参数
            List<Map<String, Object>> parameterList = new ArrayList<>();
            if (StringUtil.isNotEmpty(parameters)) {
                try {
                    // 尝试解析JSON格式的参数
                    Object paramsObj = JSON.parse(parameters);
                    if (paramsObj instanceof Map) {
                        parameterList.add((Map<String, Object>) paramsObj);
                    } else if (paramsObj instanceof List) {
                        parameterList = (List<Map<String, Object>>) paramsObj;
                    }
                } catch (Exception e) {
                    log.warn("解析参数失败: {}", e.getMessage());
                }
            }

            JsonBean result = dataModelService.testDataModelSql(dataSourceId, sqlContent, parameterList, staff, indicatorCode);
            return result.toString();
        } catch (Exception e) {
            log.error("测试数据模型SQL失败", e);
            return JsonBean.error("测试失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/statistics")
    @Operation(summary = "获取数据模型统计信息")
    public String getDataModelStatistics(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataModelService.getDataModelStatistics();
            return result.toString();
        } catch (Exception e) {
            log.error("查询数据模型统计信息失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping(value = "/datamodel/sql/parse", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "解析SQL语句结构", description = "解析SQL语句并提取字段信息")
    public String parseSQLStatement(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sqlStatement", description = "SQL语句", required = true) @RequestParam String sqlStatement,
            @Parameter(name = "dataSourceId", description = "数据源ID") @RequestParam(required = false) String dataSourceId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            if (StringUtil.isEmpty(sqlStatement)) {
                return JsonBean.error("SQL语句不能为空");
            }

            log.info("解析SQL语句: dataSourceId={}, sqlStatement长度={}", dataSourceId, sqlStatement.length());

            JsonBean result = dataModelService.parseSQLStatement(sqlStatement, dataSourceId);
            return result.toString();
        } catch (Exception e) {
            log.error("解析SQL语句失败", e);
            return JsonBean.error("解析失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/search")
    @Operation(summary = "搜索数据模型")
    public String searchDataModels(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "keyword", description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(name = "modelType", description = "模型类型") @RequestParam(required = false) String modelType,
            @Parameter(name = "pageNum", description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(name = "pageSize", description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataModelService.searchDataModels(keyword, modelType, pageNum, pageSize);
            return result.toString();
        } catch (Exception e) {
            log.error("搜索数据模型失败", e);
            return JsonBean.error("搜索失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/popular")
    @Operation(summary = "获取热门数据模型")
    public String getPopularDataModels(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "limit", description = "限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataModelService.getPopularDataModels(limit);
            return result.toString();
        } catch (Exception e) {
            log.error("查询热门数据模型失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/versions/{modelCode}")
    @Operation(summary = "获取模型版本列表")
    public String getDataModelVersions(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelCode", description = "模型编码", required = true) @PathVariable String modelCode) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataModelService.getDataModelVersions(modelCode);
            return result.toString();
        } catch (Exception e) {
            log.error("查询模型版本列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/createVersion")
    @Operation(summary = "创建新版本")
    public String createDataModelVersion(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @RequestParam String modelId,
            @Parameter(name = "changeDescription", description = "变更说明") @RequestParam(required = false) String changeDescription) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();
            JsonBean result = dataModelService.createDataModelVersion(modelId, changeDescription, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("创建模型版本失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/updateStatus")
    @Operation(summary = "更新模型状态")
    public String updateDataModelStatus(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @RequestParam String modelId,
            @Parameter(name = "status", description = "状态", required = true) @RequestParam String status) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String currentUser = staff.getRealname();
            JsonBean result = dataModelService.updateDataModelStatus(modelId, status, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("更新模型状态失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping(value = "/datamodel/execute",
                 consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "执行数据模型")
    public String executeDataModelByParams(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @RequestParam String modelId,
            @Parameter(name = "params", description = "执行参数JSON字符串", required = false) @RequestParam(required = false) String params) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 验证模型ID
            if (StringUtil.isEmpty(modelId)) {
                return JsonBean.error("模型ID不能为空");
            }

            // 解析执行参数
            Map<String, Object> parameters = new HashMap<>();
            if (StringUtil.isNotEmpty(params)) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    parameters = objectMapper.readValue(params, Map.class);
                } catch (Exception e) {
                    log.warn("解析执行参数失败: {}", e.getMessage());
                    return JsonBean.error("参数格式错误: " + e.getMessage());
                }
            }

            String currentUser = staff.getRealname();
            JsonBean result = dataModelService.executeDataModel(modelId, parameters, currentUser);
            return result.toString();
        } catch (Exception e) {
            log.error("执行数据模型失败", e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/generateCode/{modelId}")
    @Operation(summary = "生成模型代码")
    public String generateDataModelCode(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            JsonBean result = dataModelService.generateDataModelCode(modelId);
            return result.toString();
        } catch (Exception e) {
            log.error("生成模型代码失败", e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/generateSqlTemplate/{modelId}")
    @Operation(summary = "生成SQL模板", description = "将数据模型转换为可复用的SQL模板")
    public String generateSqlTemplate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("开始生成SQL模板，模型ID: {}, 操作用户: {}", modelId, staff.getRealname());

            JsonBean result = dataModelService.generateSqlTemplate(modelId, staff.getRealname());
            return result.toString();
        } catch (Exception e) {
            log.error("生成SQL模板失败，模型ID: {}", modelId, e);
            return JsonBean.error("生成SQL模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/types")
    @Operation(summary = "获取数据模型类型列表")
    public String getDataModelTypes(
            @Parameter(name = "pageNum", description = "页码(可选)") @RequestParam(required = false) Integer pageNum) {
        try {
            // 临时移除Token验证用于测试
            log.info("获取数据模型类型列表");

            // 定义模型类型枚举
            List<Map<String, Object>> modelTypes = new ArrayList<>();

            Map<String, Object> financial = new HashMap<>();
            financial.put("value", "FINANCIAL");
            financial.put("label", "财务模型");
            financial.put("description", "用于财务分析和核算的数据模型");
            modelTypes.add(financial);

            Map<String, Object> risk = new HashMap<>();
            risk.put("value", "RISK");
            risk.put("label", "风险模型");
            risk.put("description", "用于风险识别和评估的数据模型");
            modelTypes.add(risk);

            Map<String, Object> audit = new HashMap<>();
            audit.put("value", "AUDIT");
            audit.put("label", "审计模型");
            audit.put("description", "用于审计分析和检查的数据模型");
            modelTypes.add(audit);

            Map<String, Object> business = new HashMap<>();
            business.put("value", "BUSINESS");
            business.put("label", "业务模型");
            business.put("description", "用于业务分析和管理的数据模型");
            modelTypes.add(business);

            Map<String, Object> evaluation = new HashMap<>();
            evaluation.put("value", "EVALUATION");
            evaluation.put("label", "评估模型");
            evaluation.put("description", "用于风险评估和模型测试的数据模型");
            modelTypes.add(evaluation);

            return JsonBean.success("获取成功", modelTypes);
        } catch (Exception e) {
            log.error("获取数据模型类型失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/datamodel/status")
    @Operation(summary = "获取数据模型状态列表")
    public String getDataModelStatus(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 定义模型状态枚举
            List<Map<String, Object>> modelStatus = new ArrayList<>();

            Map<String, Object> draft = new HashMap<>();
            draft.put("value", "DRAFT");
            draft.put("label", "草稿");
            draft.put("description", "初始状态，可编辑修改");
            draft.put("color", "#909399");
            modelStatus.add(draft);

            Map<String, Object> testing = new HashMap<>();
            testing.put("value", "TESTING");
            testing.put("label", "测试中");
            testing.put("description", "正在测试验证中");
            testing.put("color", "#E6A23C");
            modelStatus.add(testing);

            Map<String, Object> published = new HashMap<>();
            published.put("value", "PUBLISHED");
            published.put("label", "已发布");
            published.put("description", "正式发布，可执行");
            published.put("color", "#67C23A");
            modelStatus.add(published);

            return JsonBean.success("获取成功", modelStatus);
        } catch (Exception e) {
            log.error("获取数据模型状态失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 将数据模型DTO转换为评估模型实体
     */
    private TblEvaluationModel convertToEvaluationModel(DataModelSaveDTO saveDTO, String currentUser) {
        TblEvaluationModel evaluationModel = new TblEvaluationModel();

        // 基本信息
        evaluationModel.setModelCode(saveDTO.getModelCode());
        evaluationModel.setModelName(saveDTO.getModelName());

        // 组合业务含义和计算逻辑作为描述
        String description = "";
        if (StringUtil.isNotEmpty(saveDTO.getBusinessMeaning())) {
            description += "业务含义: " + saveDTO.getBusinessMeaning();
        }
        if (StringUtil.isNotEmpty(saveDTO.getCalculationLogic())) {
            if (StringUtil.isNotEmpty(description)) {
                description += "; ";
            }
            description += "计算逻辑: " + saveDTO.getCalculationLogic();
        }
        evaluationModel.setDescription(description);

        // 业务场景映射
        if ("FINANCIAL".equals(saveDTO.getModelType())) {
            evaluationModel.setBusinessScenario("财务风险评估");
        } else if ("RISK".equals(saveDTO.getModelType())) {
            evaluationModel.setBusinessScenario("风险管理评估");
        } else if ("AUDIT".equals(saveDTO.getModelType())) {
            evaluationModel.setBusinessScenario("审计风险评估");
        } else {
            evaluationModel.setBusinessScenario("综合风险评估");
        }

        // 默认值设置
        evaluationModel.setIndustryType("通用");
        evaluationModel.setScoreAlgorithm("加权平均");
        evaluationModel.setTotalWeight(new BigDecimal("100.0"));
        evaluationModel.setRiskThresholdLow(new BigDecimal("30.0"));
        evaluationModel.setRiskThresholdMedium(new BigDecimal("60.0"));
        evaluationModel.setRiskThresholdHigh(new BigDecimal("80.0"));
        evaluationModel.setVersion("1.0");
        evaluationModel.setStatus("DRAFT");
        evaluationModel.setIsEnabled("Y");
        evaluationModel.setAccuracyRate(new BigDecimal("0.0"));

        // 审计信息
        evaluationModel.setCreateUser(currentUser);
        evaluationModel.setCreateTime(LocalDateTime.now());
        evaluationModel.setUpdateUser(currentUser);
        evaluationModel.setUpdateTime(LocalDateTime.now());

        return evaluationModel;
    }

    // =====================================================
    // 评估模型管理API
    // =====================================================

    /**
     * 获取可用于评估的数据模型列表
     */
    @PostMapping("/evaluation/available-models")
    @Operation(summary = "获取可用于评估的数据模型列表", description = "获取已发布的数据模型，用于创建评估模型")
    public String getAvailableDataModels(@RequestBody Map<String, Object> queryParams) {
        try {
            // 临时移除Token验证用于测试
            log.info("获取可用数据模型列表，参数: {}", queryParams);

            log.info("获取可用数据模型列表，参数: {}", queryParams);

            // 构造查询条件：只查询已发布的数据模型
            DataModelQueryDTO queryDTO = new DataModelQueryDTO();
            queryDTO.setPageNum((Integer) queryParams.getOrDefault("pageNum", 1));
            queryDTO.setPageSize((Integer) queryParams.getOrDefault("pageSize", 20));
            queryDTO.setStatus("PUBLISHED"); // 只查询已发布的模型
            queryDTO.setModelName((String) queryParams.get("modelName"));
            queryDTO.setModelType((String) queryParams.get("modelType"));

            JsonBean result = dataModelService.getDataModelList(queryDTO);
            return result.toString();
        } catch (Exception e) {
            log.error("获取可用数据模型列表失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询评估模型列表
     */
    @PostMapping("/evaluation/list")
    @Operation(summary = "分页查询评估模型列表", description = "根据条件分页查询评估模型列表")
    public String getEvaluationModelList(@RequestBody Map<String, Object> queryParams) {
        try {
            log.info("接收到评估模型查询请求: {}", queryParams);

            // 构造查询DTO
            EvaluationModelQueryDTO queryDTO = new EvaluationModelQueryDTO();
            queryDTO.setPageNum((Integer) queryParams.getOrDefault("pageNum", 1));
            queryDTO.setPageSize((Integer) queryParams.getOrDefault("pageSize", 20));
            queryDTO.setModelName((String) queryParams.get("modelName"));
            queryDTO.setBusinessScenario((String) queryParams.get("businessScenario"));
            queryDTO.setStatus((String) queryParams.get("status"));
            queryDTO.setIsEnabled((String) queryParams.get("isEnabled"));
            queryDTO.setIndustryType((String) queryParams.get("industryType"));
            queryDTO.setCreateUser((String) queryParams.get("createUser"));
            queryDTO.setStartTime((String) queryParams.get("startTime"));
            queryDTO.setEndTime((String) queryParams.get("endTime"));

            log.info("查询评估模型列表，参数: {}", queryDTO);

            log.info("构造的查询DTO: {}", queryDTO);

            // 调用Service层查询数据
            String result = evaluationModelService.getEvaluationModelList(queryDTO);
            log.info("Service层返回结果: {}", result);

            return result;
        } catch (Exception e) {
            log.error("查询评估模型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存评估模型
     */
    @PostMapping("/evaluation/save")
    @Operation(summary = "保存评估模型", description = "新增或更新评估模型配置")
    public String saveEvaluationModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TblEvaluationModel model) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("保存评估模型，参数: {}", model);

            // 调用Service层保存数据
            return evaluationModelService.saveEvaluationModel(model, staff.getRealname());
        } catch (Exception e) {
            log.error("保存评估模型失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 获取评估模型详情
     */
    @GetMapping("/evaluation/detail/{modelId}")
    @Operation(summary = "获取评估模型详情", description = "根据模型ID获取详细信息")
    public String getEvaluationModelDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("获取评估模型详情，modelId: {}", modelId);

            // 调用Service层获取数据
            return evaluationModelService.getEvaluationModelDetail(modelId);
        } catch (Exception e) {
            log.error("获取评估模型详情失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 复制评估模型
     */
    @PostMapping("/evaluation/copy")
    @Operation(summary = "复制评估模型", description = "复制现有评估模型创建新模型")
    public String copyEvaluationModel(
            HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "sourceModelId", description = "源模型ID") @RequestParam(required = false) String sourceModelId,
            @Parameter(name = "newModelName", description = "新模型名称") @RequestParam(required = false) String newModelName) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 智能解析请求参数 - 支持JSON和form-data两种格式
            String finalSourceModelId = sourceModelId;
            String finalNewModelName = newModelName;

            String contentType = request.getContentType();
            log.info("复制评估模型请求Content-Type: {}", contentType);

            // 检查是否为JSON请求
            if (contentType != null && contentType.contains("application/json")) {
                try {
                    BufferedReader reader = request.getReader();
                    StringBuilder jsonBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }

                    String jsonStr = jsonBuilder.toString();
                    log.info("复制评估模型JSON请求体: {}", jsonStr);

                    if (StringUtil.isNotEmpty(jsonStr)) {
                        JSONObject jsonObject = JSON.parseObject(jsonStr);
                        if (jsonObject.containsKey("sourceModelId")) {
                            finalSourceModelId = jsonObject.getString("sourceModelId");
                        }
                        if (jsonObject.containsKey("newModelName")) {
                            finalNewModelName = jsonObject.getString("newModelName");
                        }
                    }
                } catch (Exception jsonEx) {
                    log.warn("解析JSON请求体失败，使用form参数: {}", jsonEx.getMessage());
                }
            }

            log.info("复制评估模型参数: sourceModelId={}, newModelName={}", finalSourceModelId, finalNewModelName);

            // 参数验证
            if (StringUtil.isEmpty(finalSourceModelId)) {
                return JsonBean.error("源模型ID不能为空");
            }
            if (StringUtil.isEmpty(finalNewModelName)) {
                return JsonBean.error("新模型名称不能为空");
            }

            String currentUser = staff.getRealname();
            // 调用Service层复制方法
            return evaluationModelService.copyEvaluationModel(finalSourceModelId, finalNewModelName, currentUser);

        } catch (Exception e) {
            log.error("复制评估模型失败", e);
            return JsonBean.error("复制失败: " + e.getMessage());
        }
    }

    /**
     * 发布评估模型
     */
    @PostMapping("/evaluation/publish/{modelId}")
    @Operation(summary = "发布评估模型", description = "将评估模型发布为可用状态")
    public String publishEvaluationModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("发布评估模型，modelId: {}", modelId);

            // 参数验证
            if (StringUtil.isEmpty(modelId)) {
                return JsonBean.error("模型ID不能为空");
            }

            String currentUser = staff.getRealname();
            // 调用Service层发布方法
            return evaluationModelService.publishEvaluationModel(modelId, currentUser);

        } catch (Exception e) {
            log.error("发布评估模型失败", e);
            return JsonBean.error("发布失败: " + e.getMessage());
        }
    }

    /**
     * 删除评估模型
     */
    @PostMapping("/evaluation/delete/{modelId}")
    @Operation(summary = "删除评估模型", description = "根据模型ID删除评估模型")
    public String deleteEvaluationModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("删除评估模型，modelId: {}", modelId);

            // 调用Service层删除数据
            return evaluationModelService.deleteEvaluationModel(modelId);
        } catch (Exception e) {
            log.error("删除评估模型失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取评估模型统计信息
     */
    @PostMapping("/evaluation/statistics")
    @Operation(summary = "获取评估模型统计信息", description = "获取评估模型的统计数据")
    public String getEvaluationModelStatistics(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                log.warn("用户未登录或登录已过期，token: {}", token);
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 请求获取评估模型统计信息", staff.getRealname());

            // 调用Service层获取统计数据
            String result = evaluationModelService.getEvaluationModelStatistics();
            log.info("评估模型统计信息获取完成，用户: {}", staff.getRealname());
            return result;
        } catch (Exception e) {
            // 增强异常信息处理
            String errorMsg = "系统异常";
            if (e != null) {
                if (e.getMessage() != null && !e.getMessage().trim().isEmpty()) {
                    errorMsg = e.getMessage();
                } else {
                    errorMsg = e.getClass().getSimpleName() + ": " + (e.getCause() != null ? e.getCause().getMessage() : "未知错误");
                }
            }

            log.error("获取评估模型统计信息失败: {}", errorMsg, e);
            return JsonBean.error("获取失败: " + errorMsg);
        }
    }

    /**
     * 测试评估模型
     */
    @PostMapping("/evaluation/test")
    @Operation(summary = "测试评估模型", description = "执行评估模型测试")
    public String testEvaluationModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("接收到评估模型测试请求: {}", requestBody);

            // 解析请求参数 - 兼容前端参数名
            String evalModelId = (String) requestBody.get("evalModelId");
            if (StringUtil.isEmpty(evalModelId)) {
                // 兼容前端传递的modelId参数名
                evalModelId = (String) requestBody.get("modelId");
            }
            String testDataSource = (String) requestBody.get("testDataSource");
            String testDescription = (String) requestBody.get("testDescription");

            log.info("测试评估模型参数: evalModelId={}, testDataSource={}, testDescription={}",
                    evalModelId, testDataSource, testDescription);

            // 参数验证
            if (StringUtil.isEmpty(evalModelId)) {
                return JsonBean.error("评估模型ID不能为空");
            }

            // 调用Service层测试方法
            JsonBean result = evaluationModelService.testEvaluationModel(evalModelId, testDataSource, testDescription, staff);
            return result.toString();

        } catch (Exception e) {
            log.error("测试评估模型失败", e);
            return JsonBean.error("测试失败: " + e.getMessage());
        }
    }

    /**
     * 数据库连接测试接口（用于问题诊断）
     */
    @PostMapping("/evaluation/debug/database")
    @Operation(summary = "数据库连接测试", description = "用于诊断评估模型统计接口问题")
    public String testDatabaseConnection(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 请求数据库连接测试", staff.getRealname());

            // 使用DatabaseTestUtil进行测试
            com.huabo.fxgl.util.DatabaseTestUtil testUtil = new com.huabo.fxgl.util.DatabaseTestUtil();

            Map<String, Object> result = new HashMap<>();
            result.put("connectionTest", testUtil.testDatabaseConnection());
            result.put("tableCheck", testUtil.checkEvaluationModelTable());
            result.put("tableStructure", testUtil.getTableStructure("TBL_EVALUATION_MODEL"));

            return JsonBean.success("数据库测试完成", result);
        } catch (Exception e) {
            log.error("数据库连接测试失败", e);
            return JsonBean.error("测试失败: " + (e.getMessage() != null ? e.getMessage() : "未知错误"));
        }
    }

    /**
     * 调试统计查询接口
     */
    @PostMapping("/evaluation/debug/statistics")
    @Operation(summary = "调试统计查询", description = "详细调试统计数据查询过程")
    public String debugStatistics(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 请求调试统计查询", staff.getRealname());

            Map<String, Object> debugInfo = new HashMap<>();

            // 1. 检查总数
            long totalCount = evaluationModelService.count();
            debugInfo.put("totalCount", totalCount);

            // 2. 通过Service层检查统计数据
            String statisticsResult = evaluationModelService.getEvaluationModelStatistics();
            debugInfo.put("serviceStatisticsResult", statisticsResult);

            // 3. 直接查询所有数据
            List<TblEvaluationModel> allModels = evaluationModelService.list();
            debugInfo.put("allModelsCount", allModels.size());
            debugInfo.put("allModels", allModels);

            // 4. 手动统计各状态数量
            Map<String, Long> manualStatusCount = new HashMap<>();
            for (TblEvaluationModel model : allModels) {
                String status = model.getStatus();
                manualStatusCount.put(status, manualStatusCount.getOrDefault(status, 0L) + 1);
            }
            debugInfo.put("manualStatusCount", manualStatusCount);

            // 5. 测试具体的DRAFT查询
            long draftCountDirect = evaluationModelService.count(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TblEvaluationModel>()
                    .eq(TblEvaluationModel::getStatus, "DRAFT"));
            debugInfo.put("draftCountDirect", draftCountDirect);

            return JsonBean.success("调试信息获取成功", debugInfo);
        } catch (Exception e) {
            log.error("调试统计查询失败", e);
            return JsonBean.error("调试失败: " + (e.getMessage() != null ? e.getMessage() : "未知错误"));
        }
    }

    /**
     * 启动评估模型
     */
    @PostMapping("/evaluation/start/{modelId}")
    @Operation(summary = "启动评估模型", description = "启动指定的评估模型，开始定时执行")
    public String startEvaluationModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "评估模型ID", required = true) @PathVariable String modelId,
            @RequestBody Map<String, Object> scheduleConfig) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("启动评估模型，模型ID: {}, 操作用户: {}, 调度配置: {}", modelId, staff.getRealname(), scheduleConfig);

            // 调用Service层启动模型
            return evaluationModelService.startEvaluationModel(modelId, scheduleConfig, staff.getRealname());
        } catch (Exception e) {
            log.error("启动评估模型失败，模型ID: {}", modelId, e);
            return JsonBean.error("启动失败: " + e.getMessage());
        }
    }

    /**
     * 批量启动评估模型
     */
    @PostMapping("/evaluation/batch-start")
    @Operation(summary = "批量启动评估模型", description = "批量启动多个评估模型，支持并行执行")
    public String batchStartEvaluationModels(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> request) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            @SuppressWarnings("unchecked")
            List<String> modelIds = (List<String>) request.get("modelIds");
            @SuppressWarnings("unchecked")
            Map<String, Object> scheduleConfig = (Map<String, Object>) request.get("scheduleConfig");

            if (modelIds == null || modelIds.isEmpty()) {
                return JsonBean.error("模型ID列表不能为空");
            }

            log.info("批量启动评估模型，模型数量: {}, 操作用户: {}", modelIds.size(), staff.getRealname());

            // 调用Service层批量启动模型
            return evaluationModelService.batchStartEvaluationModels(modelIds, scheduleConfig, staff.getRealname());
        } catch (Exception e) {
            log.error("批量启动评估模型失败", e);
            return JsonBean.error("批量启动失败: " + e.getMessage());
        }
    }

    /**
     * 停止评估模型
     */
    @PostMapping("/evaluation/stop/{modelId}")
    @Operation(summary = "停止评估模型", description = "停止指定的评估模型执行")
    public String stopEvaluationModel(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "评估模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("停止评估模型，模型ID: {}, 操作用户: {}", modelId, staff.getRealname());

            // 调用Service层停止模型
            return evaluationModelService.stopEvaluationModel(modelId, staff.getRealname());
        } catch (Exception e) {
            log.error("停止评估模型失败，模型ID: {}", modelId, e);
            return JsonBean.error("停止失败: " + e.getMessage());
        }
    }

    /**
     * 获取评估模型SQL执行结果
     */
    @PostMapping("/evaluation/sqlExecutionResult")
    @Operation(summary = "获取评估模型SQL执行结果", description = "根据评估模型类型获取对应的SQL执行结果")
    public String getEvaluationModelSqlExecutionResult(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("获取评估模型SQL执行结果，参数: {}", requestBody);

            // 调用Service层获取SQL执行结果
            return evaluationModelService.getEvaluationModelSqlExecutionResult(requestBody, staff.getRealname());
        } catch (Exception e) {
            log.error("获取评估模型SQL执行结果失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 测试真实SQL执行功能
     */
    @PostMapping("/evaluation/testSqlExecution")
    @Operation(summary = "测试真实SQL执行功能", description = "测试评估模型中真实SQL执行重构后的功能")
    public String testSqlExecution(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> params) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String modelId = (String) params.get("modelId");
            if (StringUtil.isEmpty(modelId)) {
                return JsonBean.error("模型ID不能为空");
            }

            log.info("开始测试真实SQL执行功能，模型ID: {}, 操作用户: {}", modelId, staff.getRealname());

            // 使用测试方法来验证SQL执行功能
            JsonBean result = evaluationModelService.testEvaluationModel(modelId, "SYSTEM_TEST", "真实SQL执行功能测试", staff);

            log.info("真实SQL执行功能测试完成，模型ID: {}", modelId);
            return result.toString();
        } catch (Exception e) {
            log.error("测试真实SQL执行功能失败", e);
            return JsonBean.error("测试失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 风险预警管理接口
    // =====================================================

    /**
     * 分页查询风险预警列表
     */
    @PostMapping(value = "/warning/list")
    @Operation(summary = "分页查询风险预警列表", description = "获取风险预警列表，支持多条件IN查询")
    public String getRiskWarningList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody(required = false) Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            // 如果requestBody为null，初始化为空Map
            if (requestBody == null) {
                requestBody = new HashMap<>();
            }

            log.info("🚀 用户 {} 查询风险预警列表，原始参数: {}", staff.getRealname(), requestBody);

            JsonBean result = riskWarningService.getRiskWarningList(requestBody);
            return result.toString();
        } catch (Exception e) {
            log.error("❌ 查询风险预警列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取风险预警详情
     */
    @PostMapping("/warning/detail/{warningId}")
    @Operation(summary = "获取风险预警详情", description = "根据预警ID获取详情")
    public String getRiskWarningDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "warningId", description = "预警ID", required = true) @PathVariable String warningId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取风险预警详情，预警ID: {}", staff.getRealname(), warningId);

            JsonBean result = riskWarningService.getRiskWarningDetail(warningId);
            return result.toString();
        } catch (Exception e) {
            log.error("获取风险预警详情失败，预警ID: {}", warningId, e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 处理风险预警
     */
    @PostMapping(value = "/warning/process", consumes = {"application/json", "application/x-www-form-urlencoded"})
    @Operation(summary = "处理风险预警", description = "处理单个风险预警")
    public String processRiskWarning(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestParam(required = false) String warningId,
            @RequestParam(required = false) String processAction,
            @RequestParam(required = false) String processNote) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            if (StringUtil.isEmpty(warningId)) {
                return JsonBean.error("预警ID不能为空");
            }

            // 🔧 修复：将前端参数映射到后端服务层
            // 前端传递：processAction -> 后端需要：status
            // 前端传递：processNote -> 后端需要：handleRemark
            String status = mapProcessActionToStatus(processAction);
            String handleRemark = processNote;

            log.info("用户 {} 处理风险预警，预警ID: {}, 动作: {}, 状态: {}", staff.getRealname(), warningId, processAction, status);

            JsonBean result = riskWarningService.processRiskWarning(warningId, status, staff.getRealname(), handleRemark);
            return result.toString();
        } catch (Exception e) {
            log.error("处理风险预警失败", e);
            return JsonBean.error("处理失败: " + e.getMessage());
        }
    }

    /**
     * 将前端处理动作映射为后端状态
     * @param processAction 前端处理动作
     * @return 后端状态
     */
    private String mapProcessActionToStatus(String processAction) {
        if (StringUtil.isEmpty(processAction)) {
            return "PROCESSING"; // 默认状态
        }

        switch (processAction.toUpperCase()) {
            case "CONFIRM":
                return "PROCESSED";      // 🔧 修复：确认处理 -> 已处理
            case "IGNORE":
                return "IGNORED";
            case "ESCALATE":
                return "ESCALATED";
            case "FALSE_POSITIVE":
                return "FALSE_POSITIVE";
            default:
                return "PROCESSED";      // 🔧 修复：默认也改为已处理
        }
    }

    /**
     * 批量处理风险预警
     */
    @PostMapping("/warning/batchProcess")
    @Operation(summary = "批量处理风险预警", description = "批量处理多个风险预警")
    public String batchProcessRiskWarning(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            @SuppressWarnings("unchecked")
            List<String> warningIds = (List<String>) requestBody.get("warningIds");
            String status = (String) requestBody.get("status");
            String handleRemark = (String) requestBody.get("handleRemark");

            if (warningIds == null || warningIds.isEmpty()) {
                return JsonBean.error("预警ID列表不能为空");
            }

            log.info("用户 {} 批量处理风险预警，数量: {}, 状态: {}", staff.getRealname(), warningIds.size(), status);

            JsonBean result = riskWarningService.batchProcessRiskWarning(warningIds, status, staff.getRealname(), handleRemark);
            return result.toString();
        } catch (Exception e) {
            log.error("批量处理风险预警失败", e);
            return JsonBean.error("批量处理失败: " + e.getMessage());
        }
    }

    /**
     * 获取预警统计信息
     */
    @PostMapping("/warning/statistics")
    @Operation(summary = "获取预警统计信息", description = "获取风险预警的统计数据")
    public String getWarningStatistics(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取预警统计信息", staff.getRealname());

            JsonBean result = riskWarningService.getWarningStatistics();
            return result.toString();
        } catch (Exception e) {
            log.error("获取预警统计信息失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 🔧 新增：获取今日新增预警数量
     */
    @PostMapping("/warning/todayCount")
    @Operation(summary = "获取今日新增预警数量", description = "获取今日新增的风险预警数量")
    public String getTodayWarningCount(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取今日新增预警数量", staff.getRealname());

            JsonBean result = riskWarningService.getTodayWarningCount();
            return result.toString();
        } catch (Exception e) {
            log.error("获取今日新增预警数量失败", e);
            return JsonBean.error("获取今日新增数量失败: " + e.getMessage());
        }
    }

    /**
     * 生成预警报告
     */
    @PostMapping("/warning/generateReport")
    @Operation(summary = "生成预警报告", description = "生成风险预警报告")
    public String generateWarningReport(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 生成预警报告，参数: {}", staff.getRealname(), requestBody);

            JsonBean result = riskWarningService.generateWarningReport(requestBody);
            return result.toString();
        } catch (Exception e) {
            log.error("生成预警报告失败", e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }

    /**
     * 下载预警报告
     */
    @GetMapping("/warning/downloadReport")
    @Operation(summary = "下载预警报告", description = "下载生成的预警报告文件")
    public void downloadWarningReport(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "reportId", description = "报告ID", required = true) @RequestParam("reportId") String reportId,
            @Parameter(name = "fileName", description = "文件名", required = true) @RequestParam("fileName") String fileName,
            HttpServletResponse response) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            log.info("用户 {} 下载预警报告，报告ID: {}, 文件名: {}", staff.getRealname(), reportId, fileName);

            // 构建文件路径
            String reportDir = System.getProperty("java.io.tmpdir") + File.separator + "reports";
            String filePath = reportDir + File.separator + fileName;
            File file = new File(filePath);

            if (!file.exists()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 🔥 根据文件后缀动态设置 Content-Type
            String contentType;
            String lowerFileName = fileName.toLowerCase();
            if (lowerFileName.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (lowerFileName.endsWith(".docx")) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            } else if (lowerFileName.endsWith(".doc")) {
                contentType = "application/msword";
            } else if (lowerFileName.endsWith(".xlsx")) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else if (lowerFileName.endsWith(".xls")) {
                contentType = "application/vnd.ms-excel";
            } else {
                contentType = "application/octet-stream";
            }
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" +
                java.net.URLEncoder.encode(fileName, "UTF-8") + "\"");
            response.setContentLength((int) file.length());

            // 输出文件
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }

            log.info("预警报告下载成功: {}", fileName);

        } catch (Exception e) {
            log.error("下载预警报告失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 获取预警数据穿透分析
     */
    @PostMapping("/warning/drillDown")
    @Operation(summary = "预警数据穿透分析", description = "获取预警相关的原始业务数据")
    public String getWarningDrillDownData(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("获取预警数据穿透分析，参数: {}", requestBody);

            // 调用Service层获取穿透数据
            return riskWarningService.getWarningDrillDownData(requestBody);
        } catch (Exception e) {
            log.error("获取预警数据穿透分析失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 导出预警穿透数据
     */
    @PostMapping("/warning/exportDrillDown")
    @Operation(summary = "导出预警穿透数据", description = "导出预警相关的原始业务数据")
    public String exportWarningDrillDownData(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("导出预警穿透数据，参数: {}", requestBody);

            // 调用Service层导出数据
            return riskWarningService.exportWarningDrillDownData(requestBody);
        } catch (Exception e) {
            log.error("导出预警穿透数据失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    /**
     * 获取预警配置
     */
    @PostMapping("/warning/config")
    @Operation(summary = "获取预警配置", description = "获取风险预警配置")
    public String getWarningConfig(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取预警配置", staff.getRealname());

            JsonBean result = riskWarningService.getWarningConfig();
            return result.toString();
        } catch (Exception e) {
            log.error("获取预警配置失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 更新预警配置
     */
    @PostMapping("/warning/updateConfig")
    @Operation(summary = "更新预警配置", description = "更新风险预警配置")
    public String updateWarningConfig(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 更新预警配置，参数: {}", staff.getRealname(), requestBody);

            JsonBean result = riskWarningService.updateWarningConfig(requestBody);
            return result.toString();
        } catch (Exception e) {
            log.error("更新预警配置失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    // =====================================================
    // 评估模型预警功能增强接口
    // =====================================================

    /**
     * 获取模型预警数量
     */
    @PostMapping("/evaluation/warningCount/{modelId}")
    @Operation(summary = "获取模型预警数量", description = "获取指定评估模型的预警数量统计")
    public String getModelWarningCount(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "评估模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取模型预警数量，模型ID: {}", staff.getRealname(), modelId);

            // 调用Service层获取预警数量
            JsonBean result = riskWarningService.getModelWarningCount(modelId);
            return result.toString();
        } catch (Exception e) {
            log.error("获取模型预警数量失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 🔧 新增：获取模型待处理预警数量
     */
    @PostMapping("/evaluation/pendingWarningCount/{modelId}")
    @Operation(summary = "获取模型待处理预警数量", description = "获取指定评估模型的待处理预警数量统计（只统计PENDING和PROCESSING状态）")
    public String getModelPendingWarningCount(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "modelId", description = "评估模型ID", required = true) @PathVariable String modelId) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取模型待处理预警数量，模型ID: {}", staff.getRealname(), modelId);

            // 调用Service层获取待处理预警数量
            JsonBean result = riskWarningService.getModelPendingWarningCount(modelId);
            return result.toString();
        } catch (Exception e) {
            log.error("获取模型待处理预警数量失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取模型预警结果
     */
    @PostMapping("/evaluation/warningResults")
    @Operation(summary = "获取模型预警结果", description = "分页查询指定评估模型的预警结果")
    public String getModelWarningResults(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取模型预警结果，参数: {}", staff.getRealname(), requestBody);

            // 调用Service层获取预警结果
            JsonBean result = riskWarningService.getModelWarningResults(requestBody);
            return result.toString();
        } catch (Exception e) {
            log.error("获取模型预警结果失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 批量获取所有评估模型的预警数量与待处理预警数量
     * 一次GROUP BY查询替代逐模型两次COUNT，避免前端N+1请求
     */
    @PostMapping("/evaluation/warningCountBatch")
    @Operation(summary = "批量获取模型预警数量", description = "一次查询返回所有评估模型的预警数量与待处理预警数量（待处理=PENDING+PROCESSING，排除误报）")
    public String getModelWarningCountBatch(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 批量获取模型预警数量", staff.getRealname());

            JsonBean result = riskWarningService.getModelWarningCountBatch();
            return result.toString();
        } catch (Exception e) {
            log.error("批量获取模型预警数量失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 按预警状态分页查询存在该状态预警的评估模型列表（fxyj2 专用）
     * 返回结构与 /evaluation/list 一致；数量为 0 的模型不返回
     */
    @PostMapping("/evaluation/warningModelList")
    @Operation(summary = "按预警状态查询模型列表", description = "分页查询存在指定状态预警的评估模型（数量为0的模型不返回），返回结构与模型列表一致")
    public String getWarningModelList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 按预警状态查询模型列表，参数: {}", staff.getRealname(), requestBody);

            String result = evaluationModelService.getWarningModelList(requestBody);
            return result;
        } catch (Exception e) {
            log.error("按预警状态查询模型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 按预警状态获取统计明细（总数 + 按预警级别分组 + 按模型分组数量）
     */
    @PostMapping("/warning/statusDetail")
    @Operation(summary = "按状态获取预警统计明细", description = "返回指定预警状态的总数、按预警级别分组数量、按模型分组数量")
    public String getWarningStatusDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            String warningStatus = requestBody != null ? (String) requestBody.get("warningStatus") : null;
            log.info("用户 {} 获取状态统计明细，状态: {}", staff.getRealname(), warningStatus);

            JsonBean result = riskWarningService.getWarningStatusDetail(warningStatus);
            return result.toString();
        } catch (Exception e) {
            log.error("获取状态统计明细失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取预警源数据
     */
    @PostMapping("/evaluation/sourceData")
    @Operation(summary = "获取预警源数据", description = "获取预警相关的业务源数据")
    public String getWarningSourceData(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取预警源数据，参数: {}", staff.getRealname(), requestBody);

            // 调用Service层获取源数据
            JsonBean result = riskWarningService.getWarningSourceData(requestBody);
            return result.toString();
        } catch (Exception e) {
            log.error("获取预警源数据失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取预警源数据详情
     */
    @PostMapping("/evaluation/warningSourceData")
    @Operation(summary = "获取预警源数据详情", description = "获取指标SQL查询出来的具体数据和计算过程")
    public String getWarningSourceDataDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 获取预警源数据详情，参数: {}", staff.getRealname(), requestBody);

            // 调用Service层获取预警源数据详情
            JsonBean result = riskWarningService.getWarningSourceDataDetail(requestBody);
            return result.toString();
        } catch (Exception e) {
            log.error("获取预警源数据详情失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 生成单条预警报告
     */
    @PostMapping("/warning/generateSingleReport")
    @Operation(summary = "生成单条预警报告", description = "为单个预警记录生成详细报告")
    public String generateSingleWarningReport(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 生成单条预警报告，参数: {}", staff.getRealname(), requestBody);

            // 调用Service层生成单条报告
            JsonBean result = riskWarningService.generateSingleWarningReport(requestBody);
            return result.toString();
        } catch (Exception e) {
            log.error("生成单条预警报告失败", e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }

    /**
     * 生成模型整体报告
     */
    @PostMapping("/warning/generateModelReport")
    @Operation(summary = "生成模型整体报告", description = "为评估模型生成整体预警分析报告")
    public String generateModelWarningReport(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Token验证
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户未登录或登录已过期");
            }

            log.info("用户 {} 生成模型整体报告，参数: {}", staff.getRealname(), requestBody);

            // 调用Service层生成模型报告
            JsonBean result = riskWarningService.generateModelWarningReport(requestBody);
            return result.toString();
        } catch (Exception e) {
            log.error("生成模型整体报告失败", e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }
}
