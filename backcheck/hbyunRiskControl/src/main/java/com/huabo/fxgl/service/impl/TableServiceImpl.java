package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.dto.TableQueryDTO;
import com.huabo.fxgl.entity.TblDataSource;
import com.huabo.fxgl.entity.TblTableStructure;
import com.huabo.fxgl.mapper.TblDataSourceMapper;
import com.huabo.fxgl.mapper.TblTableStructureMapper;
import com.huabo.fxgl.service.ITableService;
import com.huabo.fxgl.util.DatabaseConnectionUtil;
import com.huabo.fxgl.vo.TableInfoVO;
import com.huabo.fxgl.vo.TableStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbfk.entity.TblStaffUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 表管理服务实现
 * @author 华博云
 * @since 2025-01-21
 */
@Slf4j
@Service
public class TableServiceImpl extends ServiceImpl<TblTableStructureMapper, TblTableStructure> implements ITableService {

    @Autowired
    private TblTableStructureMapper tableStructureMapper;

    @Autowired
    private TblDataSourceMapper dataSourceMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JsonBean getTableList(TableQueryDTO queryDTO) {
        try {
            // 参数校验
            if (!StringUtils.hasText(queryDTO.getDataSourceId())) {
                return new JsonBean(0, "数据源ID不能为空", null);
            }

            // 计算分页参数
            int offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();

            // 使用优化的查询方法
            List<Map<String, Object>> tableList = tableStructureMapper.getTableListOptimized(
                    queryDTO.getDataSourceId(),
                    queryDTO.getTableName(),
                    queryDTO.getTableComment(),
                    offset,
                    queryDTO.getPageSize()
            );

            // 获取总数
            int totalCount = tableStructureMapper.getTableCount(
                    queryDTO.getDataSourceId(),
                    queryDTO.getTableName(),
                    queryDTO.getTableComment()
            );

            // 转换为前端期望的格式
            List<Map<String, Object>> tableInfoList = new ArrayList<>();
            Map<String, String> dataSourceNameMap = getDataSourceNameMap();

            for (Map<String, Object> tableData : tableList) {
                // 直接构建前端期望的数据格式（大写字段名）
                Map<String, Object> frontendData = new HashMap<>();

                // 基本信息
                frontendData.put("TABLE_NAME", tableData.get("TABLE_NAME"));
                frontendData.put("TABLE_COMMENT", tableData.get("TABLE_COMMENT"));
                frontendData.put("DATA_SOURCE_ID", tableData.get("DATA_SOURCE_ID"));
                frontendData.put("DATA_SOURCE_NAME", dataSourceNameMap.get(tableData.get("DATA_SOURCE_ID")));

                // 统计信息（兼容大小写）
                Object columnCountObj = tableData.get("columnCount");
                if (columnCountObj == null) columnCountObj = tableData.get("COLUMNCOUNT");
                frontendData.put("COLUMN_COUNT", columnCountObj != null ? ((Number) columnCountObj).intValue() : 0);

                Object primaryKeyCountObj = tableData.get("primaryKeyCount");
                if (primaryKeyCountObj == null) primaryKeyCountObj = tableData.get("PRIMARYKEYCOUNT");
                frontendData.put("PRIMARY_KEY_COUNT", primaryKeyCountObj != null ? ((Number) primaryKeyCountObj).intValue() : 0);

                Object syncTimeObj = tableData.get("syncTime");
                if (syncTimeObj == null) syncTimeObj = tableData.get("SYNCTIME");

                // 处理同步时间
                if (syncTimeObj != null) {
                    if (syncTimeObj instanceof java.sql.Timestamp) {
                        java.sql.Timestamp timestamp = (java.sql.Timestamp) syncTimeObj;
                        frontendData.put("SYNC_TIME", timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    } else if (syncTimeObj instanceof LocalDateTime) {
                        LocalDateTime syncTime = (LocalDateTime) syncTimeObj;
                        frontendData.put("SYNC_TIME", syncTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    } else {
                        frontendData.put("SYNC_TIME", syncTimeObj.toString());
                    }
                } else {
                    frontendData.put("SYNC_TIME", null);
                }

                // 状态信息
                frontendData.put("IS_SYNCED", syncTimeObj != null);
                frontendData.put("SYNC_STATUS", syncTimeObj != null ? "SYNCED" : "NOT_SYNCED");
                frontendData.put("SYNC_STATUS_NAME", syncTimeObj != null ? "已同步" : "未同步");
                frontendData.put("CAN_EDIT", true);
                frontendData.put("CAN_DELETE", true);

                tableInfoList.add(frontendData);
            }

            // 计算分页信息
            int totalPages = (int) Math.ceil((double) totalCount / queryDTO.getPageSize());

            // 构建返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("records", tableInfoList);
            data.put("total", totalCount);
            data.put("pageNum", queryDTO.getPageNum());
            data.put("pageSize", queryDTO.getPageSize());
            data.put("pages", totalPages);

            return new JsonBean(1, "查询成功", data);
        } catch (Exception e) {
            log.error("查询表列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getTableStatistics(String dataSourceId) {
        try {
            // 参数校验
            if (!StringUtils.hasText(dataSourceId)) {
                return new JsonBean(0, "数据源ID不能为空", null);
            }

            // 获取数据源信息
            TblDataSource dataSource = dataSourceMapper.selectById(dataSourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }

            // 使用优化的统计查询
            Map<String, Object> statisticsData = tableStructureMapper.getTableStatisticsOptimized(dataSourceId);

            TableStatisticsVO statistics = new TableStatisticsVO();
            statistics.setDataSourceId(dataSourceId);
            statistics.setDataSourceName(dataSource.getSourceName());

            // 从查询结果中获取统计信息（兼容大小写）
            Object totalTablesObj = statisticsData.get("totalTables");
            if (totalTablesObj == null) totalTablesObj = statisticsData.get("TOTALTABLES");

            Object totalColumnsObj = statisticsData.get("totalColumns");
            if (totalColumnsObj == null) totalColumnsObj = statisticsData.get("TOTALCOLUMNS");

            Object lastSyncTimeObj = statisticsData.get("lastSyncTime");
            if (lastSyncTimeObj == null) lastSyncTimeObj = statisticsData.get("LASTSYNCTIME");

            int totalTables = totalTablesObj != null ? ((Number) totalTablesObj).intValue() : 0;
            int totalColumns = totalColumnsObj != null ? ((Number) totalColumnsObj).intValue() : 0;

            statistics.setTotalTables(totalTables);
            statistics.setSyncedTables(totalTables); // 已同步的表数量
            statistics.setUnsyncedTables(0); // 未同步的表数量（需要连接数据库获取）
            statistics.setTotalColumns(totalColumns);

            // 处理最后同步时间
            if (lastSyncTimeObj != null) {
                if (lastSyncTimeObj instanceof java.sql.Timestamp) {
                    java.sql.Timestamp timestamp = (java.sql.Timestamp) lastSyncTimeObj;
                    statistics.setLastSyncTime(timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else if (lastSyncTimeObj instanceof LocalDateTime) {
                    LocalDateTime syncTime = (LocalDateTime) lastSyncTimeObj;
                    statistics.setLastSyncTime(syncTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
                statistics.setSyncStatus("SYNCED");
                statistics.setSyncStatusName("已同步");
            } else {
                statistics.setLastSyncTime("未同步");
                statistics.setSyncStatus("NOT_SYNCED");
                statistics.setSyncStatusName("未同步");
            }

            return new JsonBean(1, "查询成功", statistics);
        } catch (Exception e) {
            log.error("获取表统计信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean syncTableStructures(String dataSourceId) {
        try {
            // TODO: 实现表结构同步逻辑
            // 1. 连接数据库
            // 2. 获取所有表信息
            // 3. 获取每个表的列信息
            // 4. 保存到TBL_TABLE_STRUCTURE表
            
            return new JsonBean(1, "表结构同步功能待实现", null);
        } catch (Exception e) {
            log.error("同步表结构失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean deleteTableStructure(String dataSourceId, String tableName) {
        try {
            QueryWrapper<TblTableStructure> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DATA_SOURCE_ID", dataSourceId);
            queryWrapper.eq("TABLE_NAME", tableName);
            
            boolean success = remove(queryWrapper);
            if (success) {
                return new JsonBean(1, "删除成功", null);
            } else {
                return new JsonBean(0, "删除失败", null);
            }
        } catch (Exception e) {
            log.error("删除表结构失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getTableDetail(String dataSourceId, String tableName) {
        try {
            QueryWrapper<TblTableStructure> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DATA_SOURCE_ID", dataSourceId);
            queryWrapper.eq("TABLE_NAME", tableName);
            queryWrapper.orderByAsc("COLUMN_ORDER");
            
            List<TblTableStructure> structures = list(queryWrapper);
            
            if (structures.isEmpty()) {
                return new JsonBean(0, "表结构信息不存在", null);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("tableName", tableName);
            result.put("tableComment", structures.get(0).getTableComment());
            result.put("dataSourceId", dataSourceId);
            result.put("columns", structures);
            
            return new JsonBean(1, "查询成功", result);
        } catch (Exception e) {
            log.error("获取表详细信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取数据源名称映射
     */
    private Map<String, String> getDataSourceNameMap() {
        List<TblDataSource> dataSources = dataSourceMapper.selectList(null);
        return dataSources.stream()
                .collect(Collectors.toMap(TblDataSource::getSourceId, TblDataSource::getSourceName));
    }

    @Override
    public JsonBean debugTableQuery(String dataSourceId) {
        try {
            Map<String, Object> debugInfo = new HashMap<>();

            // 1. 测试优化的表列表查询
            List<Map<String, Object>> tableList = tableStructureMapper.getTableListOptimized(
                    dataSourceId, null, null, 0, 5);
            debugInfo.put("tableListSample", tableList);

            // 2. 测试优化的统计查询
            Map<String, Object> statistics = tableStructureMapper.getTableStatisticsOptimized(dataSourceId);
            debugInfo.put("statisticsRaw", statistics);

            // 3. 显示字段名
            if (!tableList.isEmpty()) {
                Map<String, Object> firstTable = tableList.get(0);
                debugInfo.put("tableFieldNames", firstTable.keySet());
            }

            if (statistics != null) {
                debugInfo.put("statisticsFieldNames", statistics.keySet());
            }

            // 4. 测试表总数查询
            int totalCount = tableStructureMapper.getTableCount(dataSourceId, null, null);
            debugInfo.put("totalCount", totalCount);

            return new JsonBean(1, "调试信息获取成功", debugInfo);
        } catch (Exception e) {
            log.error("调试表查询失败", e);
            return new JsonBean(0, "调试失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getTableColumns(String dataSourceId, String tableName) {
        try {
            // 参数校验
            if (!StringUtils.hasText(dataSourceId)) {
                return new JsonBean(0, "数据源ID不能为空", null);
            }
            if (!StringUtils.hasText(tableName)) {
                return new JsonBean(0, "表名不能为空", null);
            }

            // 查询表的列信息
            QueryWrapper<TblTableStructure> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DATA_SOURCE_ID", dataSourceId);
            queryWrapper.eq("TABLE_NAME", tableName);
            queryWrapper.orderByAsc("COLUMN_ORDER");

            List<TblTableStructure> structures = list(queryWrapper);

            if (structures.isEmpty()) {
                return new JsonBean(0, "未找到表结构信息，请先同步表结构", null);
            }

            // 转换为前端期望的格式（小写驼峰）
            List<Map<String, Object>> columnList = new ArrayList<>();
            for (TblTableStructure structure : structures) {
                Map<String, Object> columnInfo = new HashMap<>();
                columnInfo.put("structureId", structure.getStructureId());
                columnInfo.put("dataSourceId", structure.getDataSourceId());
                columnInfo.put("tableName", structure.getTableName());
                columnInfo.put("tableComment", structure.getTableComment());
                columnInfo.put("columnName", structure.getColumnName());
                columnInfo.put("columnType", structure.getColumnType());
                columnInfo.put("columnLength", structure.getColumnLength());
                columnInfo.put("columnPrecision", structure.getColumnPrecision());
                columnInfo.put("columnScale", structure.getColumnScale());
                columnInfo.put("isNullable", structure.getIsNullable());
                columnInfo.put("isPrimaryKey", structure.getIsPrimaryKey());
                columnInfo.put("columnComment", structure.getColumnComment());
                columnInfo.put("columnDefault", structure.getColumnDefault());
                columnInfo.put("columnOrder", structure.getColumnOrder());

                // 格式化同步时间
                if (structure.getSyncTime() != null) {
                    columnInfo.put("syncTime", structure.getSyncTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else {
                    columnInfo.put("syncTime", null);
                }

                // 添加一些辅助信息
                columnInfo.put("isNullableName", "Y".equals(structure.getIsNullable()) ? "可空" : "不可空");
                columnInfo.put("isPrimaryKeyName", "Y".equals(structure.getIsPrimaryKey()) ? "主键" : "普通列");

                // 构建完整的列类型描述
                String fullColumnType = structure.getColumnType();
                if (structure.getColumnLength() != null && structure.getColumnLength() > 0) {
                    if (structure.getColumnPrecision() != null && structure.getColumnPrecision() > 0) {
                        fullColumnType += "(" + structure.getColumnLength() + "," + structure.getColumnPrecision() + ")";
                    } else {
                        fullColumnType += "(" + structure.getColumnLength() + ")";
                    }
                }
                columnInfo.put("fullColumnType", fullColumnType);

                columnList.add(columnInfo);
            }

            // 前端期望直接返回列数组，而不是包装在对象中
            return new JsonBean(1, "查询成功", columnList);
        } catch (Exception e) {
            log.error("获取表列信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean previewTableData(String dataSourceId, String tableName, Integer limit, Integer offset) {
        try {
            // 参数校验
            if (!StringUtils.hasText(dataSourceId)) {
                return new JsonBean(0, "数据源ID不能为空", null);
            }
            if (!StringUtils.hasText(tableName)) {
                return new JsonBean(0, "表名不能为空", null);
            }
            if (limit == null || limit <= 0) {
                limit = 100; // 默认100条
            }
            if (offset == null || offset < 0) {
                offset = 0; // 默认从0开始
            }

            // 限制最大查询条数，防止数据量过大
            if (limit > 1000) {
                limit = 1000;
            }

            // 查询数据源信息
            TblDataSource dataSource = dataSourceMapper.selectById(dataSourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }

            // 使用数据库连接工具连接远程数据库

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                // 建立数据库连接
                connection = DatabaseConnectionUtil.getConnection(dataSource);

                // 构建查询SQL（兼容不同数据库的分页语法）
                String sql = buildPreviewSql(dataSource.getSourceType(), tableName, limit, offset);
                log.info("预览表数据SQL: {}", sql);

                statement = connection.prepareStatement(sql);
                resultSet = statement.executeQuery();

                // 获取结果集元数据
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // 构建列信息
                List<Map<String, Object>> columns = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    Map<String, Object> column = new HashMap<>();
                    column.put("columnName", metaData.getColumnName(i));
                    column.put("columnType", metaData.getColumnTypeName(i));
                    column.put("columnLabel", metaData.getColumnLabel(i));
                    columns.add(column);
                }

                // 读取数据行
                List<Map<String, Object>> rows = new ArrayList<>();
                int rowCount = 0;
                while (resultSet.next() && rowCount < limit) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = resultSet.getObject(i);
                        // 处理特殊数据类型
                        if (value instanceof java.sql.Timestamp) {
                            value = value.toString();
                        } else if (value instanceof java.sql.Date) {
                            value = value.toString();
                        } else if (value instanceof java.sql.Clob) {
                            // 处理CLOB类型
                            java.sql.Clob clob = (java.sql.Clob) value;
                            value = clob.getSubString(1, (int) clob.length());
                        }
                        row.put(columnName, value);
                    }
                    rows.add(row);
                    rowCount++;
                }

                // 查询总记录数（可选，用于分页）
                int totalCount = getTotalCount(connection, dataSource.getSourceType(), tableName);

                // 构建返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("tableName", tableName);
                result.put("dataSourceId", dataSourceId);
                result.put("columns", columns);
                result.put("rows", rows);
                result.put("limit", limit);
                result.put("offset", offset);
                result.put("actualRows", rowCount);
                result.put("totalCount", totalCount);
                result.put("hasMore", offset + rowCount < totalCount);

                return new JsonBean(1, "预览成功", result);

            } finally {
                // 关闭资源
                if (resultSet != null) {
                    try { resultSet.close(); } catch (SQLException e) { log.warn("关闭ResultSet失败", e); }
                }
                if (statement != null) {
                    try { statement.close(); } catch (SQLException e) { log.warn("关闭Statement失败", e); }
                }
                if (connection != null) {
                    try { connection.close(); } catch (SQLException e) { log.warn("关闭Connection失败", e); }
                }
            }

        } catch (Exception e) {
            log.error("预览表数据失败", e);
            return new JsonBean(0, "预览失败: " + e.getMessage(), null);
        }
    }

    /**
     * 构建预览SQL语句（兼容不同数据库）
     */
    private String buildPreviewSql(String dbType, String tableName, int limit, int offset) {
        switch (dbType.toUpperCase()) {
            case "DM":
            case "ORACLE":
                // 达梦和Oracle使用OFFSET...ROWS FETCH NEXT...ROWS ONLY语法
                return String.format("SELECT * FROM %s ORDER BY ROWNUM OFFSET %d ROWS FETCH NEXT %d ROWS ONLY",
                                    tableName, offset, limit);
            case "MYSQL":
                // MySQL使用LIMIT语法
                return String.format("SELECT * FROM %s LIMIT %d OFFSET %d", tableName, limit, offset);
            default:
                // 默认使用标准SQL（可能不支持分页）
                return String.format("SELECT * FROM %s", tableName);
        }
    }

    /**
     * 获取表的总记录数
     */
    private int getTotalCount(Connection connection, String dbType, String tableName) {
        String countSql = String.format("SELECT COUNT(*) FROM %s", tableName);
        try (PreparedStatement statement = connection.prepareStatement(countSql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            log.warn("获取表总记录数失败: {}", e.getMessage());
        }
        return 0;
    }

    @Override
    public JsonBean generateTableDDL(String dataSourceId, String tableName) {
        try {
            // 参数校验
            if (!StringUtils.hasText(dataSourceId)) {
                return new JsonBean(0, "数据源ID不能为空", null);
            }
            if (!StringUtils.hasText(tableName)) {
                return new JsonBean(0, "表名不能为空", null);
            }

            // 查询数据源信息
            TblDataSource dataSource = dataSourceMapper.selectById(dataSourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }

            // 查询表结构信息
            QueryWrapper<TblTableStructure> wrapper = new QueryWrapper<>();
            wrapper.eq("DATA_SOURCE_ID", dataSourceId)
                   .eq("TABLE_NAME", tableName)
                   .orderByAsc("COLUMN_ORDER");

            List<TblTableStructure> structures = tableStructureMapper.selectList(wrapper);
            if (structures.isEmpty()) {
                return new JsonBean(0, "表结构信息不存在，请先同步表结构", null);
            }

            // 生成DDL语句
            String ddlStatement = buildDDLStatement(dataSource, tableName, structures);

            return new JsonBean(1, "生成成功", ddlStatement);

        } catch (Exception e) {
            log.error("生成表DDL失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null);
        }
    }

    /**
     * 构建DDL语句
     */
    private String buildDDLStatement(TblDataSource dataSource, String tableName, List<TblTableStructure> structures) {
        StringBuilder ddl = new StringBuilder();
        String dbType = dataSource.getSourceType().toUpperCase();

        // 获取表注释
        String tableComment = structures.get(0).getTableComment();

        // 1. CREATE TABLE语句开始
        ddl.append("-- ").append(tableComment != null ? tableComment : tableName).append("\n");
        ddl.append("CREATE TABLE ").append(tableName).append(" (\n");

        // 2. 列定义
        List<String> primaryKeys = new ArrayList<>();
        for (int i = 0; i < structures.size(); i++) {
            TblTableStructure structure = structures.get(i);

            // 列定义
            ddl.append("    ").append(structure.getColumnName()).append(" ");

            // 数据类型
            String columnType = buildColumnType(structure, dbType);
            ddl.append(columnType);

            // 是否可空
            if ("N".equals(structure.getIsNullable())) {
                ddl.append(" NOT NULL");
            }

            // 默认值
            if (StringUtils.hasText(structure.getColumnDefault())) {
                ddl.append(" DEFAULT ").append(structure.getColumnDefault());
            }

            // 列注释（达梦数据库支持内联注释）
            if (StringUtils.hasText(structure.getColumnComment())) {
                if ("DM".equals(dbType)) {
                    ddl.append(" COMMENT '").append(structure.getColumnComment()).append("'");
                }
            }

            // 收集主键
            if ("Y".equals(structure.getIsPrimaryKey())) {
                primaryKeys.add(structure.getColumnName());
            }

            // 添加逗号（除了最后一列）
            if (i < structures.size() - 1 || !primaryKeys.isEmpty()) {
                ddl.append(",");
            }
            ddl.append("\n");
        }

        // 3. 主键约束
        if (!primaryKeys.isEmpty()) {
            ddl.append("    CONSTRAINT PK_").append(tableName).append(" PRIMARY KEY (");
            ddl.append(String.join(", ", primaryKeys));
            ddl.append(")\n");
        }

        ddl.append(");\n\n");

        // 4. 表注释（Oracle/达梦语法）
        if (StringUtils.hasText(tableComment) && ("ORACLE".equals(dbType) || "DM".equals(dbType))) {
            ddl.append("COMMENT ON TABLE ").append(tableName)
               .append(" IS '").append(tableComment).append("';\n");
        }

        // 5. 列注释（Oracle/达梦语法）
        if ("ORACLE".equals(dbType) || "DM".equals(dbType)) {
            for (TblTableStructure structure : structures) {
                if (StringUtils.hasText(structure.getColumnComment())) {
                    ddl.append("COMMENT ON COLUMN ").append(tableName).append(".")
                       .append(structure.getColumnName()).append(" IS '")
                       .append(structure.getColumnComment()).append("';\n");
                }
            }
        }

        // 6. 建议的索引（可选）
        ddl.append("\n-- 建议索引\n");
        for (TblTableStructure structure : structures) {
            String columnName = structure.getColumnName();
            // 为外键字段建议索引
            if (columnName.endsWith("_ID") && !"Y".equals(structure.getIsPrimaryKey())) {
                ddl.append("-- CREATE INDEX IDX_").append(tableName).append("_")
                   .append(columnName).append(" ON ").append(tableName)
                   .append("(").append(columnName).append(");\n");
            }
        }

        return ddl.toString();
    }

    /**
     * 构建列类型定义
     */
    private String buildColumnType(TblTableStructure structure, String dbType) {
        String columnType = structure.getColumnType();
        Integer length = structure.getColumnLength();
        Integer precision = structure.getColumnPrecision();
        Integer scale = structure.getColumnScale();

        // 根据数据库类型调整类型名称
        switch (dbType) {
            case "DM":
            case "ORACLE":
                return buildOracleColumnType(columnType, length, precision, scale);
            case "MYSQL":
                return buildMySQLColumnType(columnType, length, precision, scale);
            default:
                return buildStandardColumnType(columnType, length, precision, scale);
        }
    }

    /**
     * 构建Oracle/达梦列类型
     */
    private String buildOracleColumnType(String columnType, Integer length, Integer precision, Integer scale) {
        switch (columnType.toUpperCase()) {
            case "VARCHAR":
            case "VARCHAR2":
                return length != null && length > 0 ? "VARCHAR2(" + length + ")" : "VARCHAR2(255)";
            case "CHAR":
                return length != null && length > 0 ? "CHAR(" + length + ")" : "CHAR(1)";
            case "NUMBER":
                if (precision != null && precision > 0) {
                    if (scale != null && scale > 0) {
                        return "NUMBER(" + precision + "," + scale + ")";
                    } else {
                        return "NUMBER(" + precision + ")";
                    }
                }
                return "NUMBER";
            case "INT":
            case "INTEGER":
                return "NUMBER(10)";
            case "BIGINT":
                return "NUMBER(19)";
            case "DECIMAL":
                if (precision != null && precision > 0) {
                    if (scale != null && scale > 0) {
                        return "NUMBER(" + precision + "," + scale + ")";
                    } else {
                        return "NUMBER(" + precision + ")";
                    }
                }
                return "NUMBER(10,2)";
            case "DATE":
                return "DATE";
            case "TIMESTAMP":
                return "TIMESTAMP";
            case "CLOB":
                return "CLOB";
            case "BLOB":
                return "BLOB";
            default:
                return columnType;
        }
    }

    /**
     * 构建MySQL列类型
     */
    private String buildMySQLColumnType(String columnType, Integer length, Integer precision, Integer scale) {
        switch (columnType.toUpperCase()) {
            case "VARCHAR":
            case "VARCHAR2":
                return length != null && length > 0 ? "VARCHAR(" + length + ")" : "VARCHAR(255)";
            case "CHAR":
                return length != null && length > 0 ? "CHAR(" + length + ")" : "CHAR(1)";
            case "NUMBER":
            case "DECIMAL":
                if (precision != null && precision > 0) {
                    if (scale != null && scale > 0) {
                        return "DECIMAL(" + precision + "," + scale + ")";
                    } else {
                        return "DECIMAL(" + precision + ")";
                    }
                }
                return "DECIMAL(10,2)";
            case "INT":
            case "INTEGER":
                return "INT";
            case "BIGINT":
                return "BIGINT";
            case "DATE":
                return "DATE";
            case "TIMESTAMP":
                return "TIMESTAMP";
            case "CLOB":
                return "LONGTEXT";
            case "BLOB":
                return "LONGBLOB";
            default:
                return columnType;
        }
    }

    /**
     * 构建标准SQL列类型
     */
    private String buildStandardColumnType(String columnType, Integer length, Integer precision, Integer scale) {
        switch (columnType.toUpperCase()) {
            case "VARCHAR":
            case "VARCHAR2":
                return length != null && length > 0 ? "VARCHAR(" + length + ")" : "VARCHAR(255)";
            case "CHAR":
                return length != null && length > 0 ? "CHAR(" + length + ")" : "CHAR(1)";
            case "NUMBER":
            case "DECIMAL":
                if (precision != null && precision > 0) {
                    if (scale != null && scale > 0) {
                        return "DECIMAL(" + precision + "," + scale + ")";
                    } else {
                        return "DECIMAL(" + precision + ")";
                    }
                }
                return "DECIMAL(10,2)";
            case "INT":
            case "INTEGER":
                return "INTEGER";
            case "BIGINT":
                return "BIGINT";
            case "DATE":
                return "DATE";
            case "TIMESTAMP":
                return "TIMESTAMP";
            default:
                return columnType;
        }
    }

    // =====================================================
    // SQL模板管理相关方法
    // =====================================================

    /**
     * 分页查询SQL模板列表
     */
    @Override
    public JsonBean getSqlTemplateList(Map<String, Object> params) {
        try {
            // 解析查询参数
            Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);
            Integer pageSize = (Integer) params.getOrDefault("pageSize", 20);
            String templateName = (String) params.get("templateName");
            String templateType = (String) params.get("templateType");
            String templateCategory = (String) params.get("templateCategory");
            String isSystem = (String) params.get("isSystem");
            // 统一使用isEnabled，移除status参数（避免语义混乱）
            String isEnabled = (String) params.get("isEnabled");
            String databaseType = (String) params.get("databaseType");

            // 构建查询SQL
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("    TEMPLATE_ID, ");
            sql.append("    TEMPLATE_CODE, ");
            sql.append("    TEMPLATE_NAME, ");
            sql.append("    DATA_SOURCE_ID, ");
            sql.append("    TEMPLATE_TYPE, ");
            sql.append("    CATEGORY, ");
            sql.append("    DESCRIPTION, ");
            sql.append("    DATABASE_TYPE, ");
            sql.append("    COMPLEXITY_LEVEL, ");
            sql.append("    USAGE_COUNT, ");
            sql.append("    SUCCESS_RATE, ");
            sql.append("    AVG_EXECUTE_TIME, ");
            sql.append("    LAST_USE_TIME, ");
            sql.append("    IS_SYSTEM, ");
            sql.append("    IS_PUBLIC, ");
            sql.append("    STATUS, ");
            sql.append("    CREATE_USER, ");
            sql.append("    CREATE_TIME, ");
            sql.append("    UPDATE_USER, ");
            sql.append("    UPDATE_TIME ");
            sql.append("FROM TBL_SQL_TEMPLATE ");
            sql.append("WHERE 1=1 ");

            List<Object> paramList = new ArrayList<>();

            // 添加查询条件
            if (StringUtils.hasText(templateName)) {
                sql.append("AND TEMPLATE_NAME LIKE ? ");
                paramList.add("%" + templateName + "%");
            }
            if (StringUtils.hasText(templateType)) {
                sql.append("AND TEMPLATE_TYPE = ? ");
                paramList.add(templateType);
            }
            if (StringUtils.hasText(templateCategory)) {
                sql.append("AND CATEGORY = ? ");
                paramList.add(templateCategory);
            }
            if (StringUtils.hasText(isSystem)) {
                sql.append("AND IS_SYSTEM = ? ");
                paramList.add(isSystem);
            }
            // 启用状态查询条件 - 统一使用isEnabled
            if (StringUtils.hasText(isEnabled)) {
                sql.append("AND STATUS = ? ");
                paramList.add(isEnabled);
                log.info("启用状态查询条件: STATUS = {}", isEnabled);
            }

            if (StringUtils.hasText(databaseType)) {
                sql.append("AND DATABASE_TYPE = ? ");
                paramList.add(databaseType);
                log.info("使用databaseType查询条件: {}", databaseType);
            }

            sql.append("ORDER BY CREATE_TIME DESC ");

            // 记录完整的SQL和参数用于调试
            log.info("执行SQL查询: {}", sql.toString());
            log.info("查询参数: {}", paramList);

            // 分页查询
            PageHelper.startPage(pageNum, pageSize);
            List<Map<String, Object>> templates = jdbcTemplate.queryForList(sql.toString(), paramList.toArray());

            log.info("查询结果数量: {}", templates.size());

            // 处理数据格式 - 转换字段名为前端期望的小写驼峰格式
            List<Map<String, Object>> processedTemplates = new ArrayList<>();
            for (Map<String, Object> template : templates) {
                Map<String, Object> processedTemplate = new HashMap<>();

                // 基本字段映射（大写转小写驼峰）
                processedTemplate.put("templateId", template.get("TEMPLATE_ID"));
                processedTemplate.put("templateCode", template.get("TEMPLATE_CODE"));
                processedTemplate.put("templateName", template.get("TEMPLATE_NAME"));
                processedTemplate.put("dataSourceId", template.get("DATA_SOURCE_ID"));
                processedTemplate.put("templateType", template.get("TEMPLATE_TYPE"));
                processedTemplate.put("category", template.get("CATEGORY"));
                processedTemplate.put("description", template.get("DESCRIPTION"));
                processedTemplate.put("databaseType", template.get("DATABASE_TYPE"));
                processedTemplate.put("complexityLevel", template.get("COMPLEXITY_LEVEL"));
                processedTemplate.put("usageCount", template.get("USAGE_COUNT"));
                processedTemplate.put("successRate", template.get("SUCCESS_RATE"));
                processedTemplate.put("avgExecuteTime", template.get("AVG_EXECUTE_TIME"));
                processedTemplate.put("isSystem", template.get("IS_SYSTEM"));
                processedTemplate.put("isPublic", template.get("IS_PUBLIC"));
                processedTemplate.put("status", template.get("STATUS"));
                processedTemplate.put("createUser", template.get("CREATE_USER"));
                processedTemplate.put("updateUser", template.get("UPDATE_USER"));

                // 格式化时间
                if (template.get("CREATE_TIME") != null) {
                    processedTemplate.put("createTime", DateUtil.parseDate((java.util.Date) template.get("CREATE_TIME"), DateUtil.DATE_FULL_STR));
                } else {
                    processedTemplate.put("createTime", null);
                }
                if (template.get("UPDATE_TIME") != null) {
                    processedTemplate.put("updateTime", DateUtil.parseDate((java.util.Date) template.get("UPDATE_TIME"), DateUtil.DATE_FULL_STR));
                } else {
                    processedTemplate.put("updateTime", null);
                }
                // 最后使用时间
                if (template.get("LAST_USE_TIME") != null) {
                    processedTemplate.put("lastUsedTime", DateUtil.parseDate((java.util.Date) template.get("LAST_USE_TIME"), DateUtil.DATE_FULL_STR));
                } else {
                    processedTemplate.put("lastUsedTime", processedTemplate.get("updateTime")); // 使用更新时间作为默认值
                }

                // 添加前端需要的字段
                processedTemplate.put("templateCategory", template.get("CATEGORY")); // 兼容旧字段名

                // 数据库类型处理 - 如果数据库中没有值，默认为DM
                String dbType = (String) template.get("DATABASE_TYPE");
                processedTemplate.put("databaseType", StringUtils.hasText(dbType) ? dbType : "DM");

                // 启用状态映射 - STATUS字段映射为isEnabled
                processedTemplate.put("isEnabled", template.get("STATUS"));

                processedTemplates.add(processedTemplate);
            }

            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(templates);

            Map<String, Object> result = new HashMap<>();
            result.put("records", processedTemplates); // 使用处理后的数据
            result.put("total", pageInfo.getTotal());
            result.put("pages", pageInfo.getPages());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", result);
        } catch (Exception e) {
            log.error("查询SQL模板列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取SQL模板统计信息
     */
    @Override
    public JsonBean getSqlTemplateStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();

            // 合并5条COUNT查询为1条SQL，减少数据库往返
            String mainStatsSql = "SELECT " +
                "COUNT(*) AS TOTAL_COUNT, " +
                "SUM(CASE WHEN IS_SYSTEM = 'Y' THEN 1 ELSE 0 END) AS SYSTEM_COUNT, " +
                "SUM(CASE WHEN STATUS = 'Y' THEN 1 ELSE 0 END) AS ENABLED_COUNT, " +
                "COUNT(DISTINCT CASE WHEN CATEGORY IS NOT NULL THEN CATEGORY END) AS CATEGORY_COUNT, " +
                "COALESCE(SUM(USAGE_COUNT), 0) AS TOTAL_USAGE " +
                "FROM TBL_SQL_TEMPLATE";
            Map<String, Object> mainStats = jdbcTemplate.queryForMap(mainStatsSql);

            statistics.put("totalCount", ((Number) mainStats.get("TOTAL_COUNT")).intValue());
            statistics.put("systemCount", ((Number) mainStats.get("SYSTEM_COUNT")).intValue());
            statistics.put("enabledCount", ((Number) mainStats.get("ENABLED_COUNT")).intValue());
            statistics.put("categoryCount", ((Number) mainStats.get("CATEGORY_COUNT")).intValue());
            statistics.put("totalUsage", ((Number) mainStats.get("TOTAL_USAGE")).intValue());

            // 按类型分组统计
            String typeStatsSql = "SELECT TEMPLATE_TYPE, COUNT(*) as count FROM TBL_SQL_TEMPLATE GROUP BY TEMPLATE_TYPE";
            List<Map<String, Object>> typeStats = jdbcTemplate.queryForList(typeStatsSql);
            statistics.put("typeStats", typeStats);

            // 按复杂度分组统计
            String complexityStatsSql = "SELECT COMPLEXITY_LEVEL, COUNT(*) as count FROM TBL_SQL_TEMPLATE GROUP BY COMPLEXITY_LEVEL";
            List<Map<String, Object>> complexityStats = jdbcTemplate.queryForList(complexityStatsSql);
            statistics.put("complexityStats", complexityStats);

            return new JsonBean(1, "查询成功", statistics);
        } catch (Exception e) {
            log.error("获取SQL模板统计信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 保存SQL模板
     */
    @Override
    public String getSqlTemplateDetail(String templateId, TblStaffUtil staff) {
        try {
            log.info("获取SQL模板详情，模板ID: {}, 用户: {}", templateId, staff.getStaffid());

            // 参数验证
            if (StringUtil.isEmpty(templateId)) {
                return JsonBean.error("模板ID不能为空");
            }

            // 查询模板基本信息
            String sql = "SELECT " +
                    "    TEMPLATE_ID, " +
                    "    TEMPLATE_CODE, " +
                    "    TEMPLATE_NAME, " +
                    "    DATA_SOURCE_ID, " +
                    "    TEMPLATE_TYPE, " +
                    "    CATEGORY, " +
                    "    DESCRIPTION, " +
                    "    SQL_CONTENT, " +
                    "    PARAMETER_CONFIG, " +
                    "    COMPLEXITY_LEVEL, " +
                    "    USAGE_COUNT, " +
                    "    SUCCESS_RATE, " +
                    "    AVG_EXECUTE_TIME, " +
                    "    LAST_USE_TIME, " +
                    "    IS_SYSTEM, " +
                    "    IS_PUBLIC, " +
                    "    STATUS, " +
                    "    CREATE_USER, " +
                    "    CREATE_TIME, " +
                    "    UPDATE_USER, " +
                    "    UPDATE_TIME " +
                    "FROM TBL_SQL_TEMPLATE " +
                    "WHERE TEMPLATE_ID = ?";

            List<Map<String, Object>> templates = jdbcTemplate.queryForList(sql, templateId);

            if (templates.isEmpty()) {
                return JsonBean.error("模板不存在或已被删除");
            }

            Map<String, Object> template = templates.get(0);

            // 构建返回数据
            Map<String, Object> templateDetail = new HashMap<>();

            // 基本信息映射
            templateDetail.put("templateId", template.get("TEMPLATE_ID"));
            templateDetail.put("templateCode", template.get("TEMPLATE_CODE"));
            templateDetail.put("templateName", template.get("TEMPLATE_NAME"));
            templateDetail.put("dataSourceId", template.get("DATA_SOURCE_ID"));
            templateDetail.put("templateType", template.get("TEMPLATE_TYPE"));
            templateDetail.put("category", template.get("CATEGORY"));
            templateDetail.put("description", template.get("DESCRIPTION"));
            templateDetail.put("sqlContent", template.get("SQL_CONTENT"));
            templateDetail.put("complexityLevel", template.get("COMPLEXITY_LEVEL"));
            templateDetail.put("usageCount", template.get("USAGE_COUNT"));
            templateDetail.put("isSystem", "Y".equals(template.get("IS_SYSTEM")));
            templateDetail.put("isPublic", "Y".equals(template.get("IS_PUBLIC")));
            templateDetail.put("status", template.get("STATUS"));
            templateDetail.put("createUser", template.get("CREATE_USER"));
            templateDetail.put("updateUser", template.get("UPDATE_USER"));

            // 时间格式化
            if (template.get("CREATE_TIME") != null) {
                templateDetail.put("createTime", DateUtil.parseDate((java.util.Date) template.get("CREATE_TIME"), DateUtil.DATE_FULL_STR));
            }
            if (template.get("UPDATE_TIME") != null) {
                templateDetail.put("updateTime", DateUtil.parseDate((java.util.Date) template.get("UPDATE_TIME"), DateUtil.DATE_FULL_STR));
            }
            // 最后使用时间
            if (template.get("LAST_USE_TIME") != null) {
                templateDetail.put("lastUseTime", DateUtil.parseDate((java.util.Date) template.get("LAST_USE_TIME"), DateUtil.DATE_FULL_STR));
            } else if (template.get("UPDATE_TIME") != null) {
                templateDetail.put("lastUseTime", DateUtil.parseDate((java.util.Date) template.get("UPDATE_TIME"), DateUtil.DATE_FULL_STR));
            } else {
                templateDetail.put("lastUseTime", "未使用");
            }

            // 统计信息
            templateDetail.put("useCount", template.get("USAGE_COUNT") != null ? template.get("USAGE_COUNT") : 0);
            templateDetail.put("avgExecuteTime", template.get("AVG_EXECUTE_TIME") != null ? template.get("AVG_EXECUTE_TIME") : 0);
            templateDetail.put("successRate", template.get("SUCCESS_RATE") != null ? template.get("SUCCESS_RATE") : 100);

            // 解析参数配置
            List<Map<String, Object>> parameters = new ArrayList<>();
            String parameterConfig = (String) template.get("PARAMETER_CONFIG");
            if (StringUtil.isNotEmpty(parameterConfig)) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    // 尝试解析为数组格式（新格式）
                    try {
                        parameters = objectMapper.readValue(parameterConfig, List.class);
                    } catch (Exception e1) {
                        // 如果解析数组失败，尝试解析为对象格式（旧格式）
                        Map<String, Object> paramMap = objectMapper.readValue(parameterConfig, Map.class);
                        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                            Map<String, Object> param = new HashMap<>();
                            param.put("name", entry.getKey());

                            if (entry.getValue() instanceof Map) {
                                Map<String, Object> paramDetail = (Map<String, Object>) entry.getValue();
                                param.put("type", paramDetail.get("type"));
                                param.put("description", paramDetail.get("description"));
                                param.put("example", String.valueOf(paramDetail.get("default")));
                            } else {
                                // 简单值格式
                                param.put("type", "String");
                                param.put("description", "");
                                param.put("example", String.valueOf(entry.getValue()));
                            }
                            parameters.add(param);
                        }
                    }
                } catch (Exception e) {
                    log.warn("解析参数配置失败: {}", e.getMessage());
                }
            }
            templateDetail.put("parameters", parameters);

            // 添加前端需要的额外字段
            templateDetail.put("templateCategory", template.get("CATEGORY")); // 兼容旧字段名
            templateDetail.put("databaseType", "DM");
            templateDetail.put("isEnabled", template.get("STATUS"));

            log.info("获取SQL模板详情成功，模板: {}", templateDetail.get("templateName"));

            // 构建成功响应
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(1);
            jsonBean.setMsg("获取成功");
            jsonBean.setData(templateDetail);
            return jsonBean.toString();

        } catch (Exception e) {
            log.error("获取SQL模板详情失败", e);
            return JsonBean.error("获取详情失败: " + e.getMessage());
        }
    }

    @Override
    public String saveSqlTemplate(Map<String, Object> templateData, TblStaffUtil staff) {
        try {
            // 获取模板数据
            String templateId = (String) templateData.get("templateId");
            String templateName = (String) templateData.get("templateName");
            String templateCode = (String) templateData.get("templateCode");
            String dataSourceId = (String) templateData.get("dataSourceId");
            String templateType = (String) templateData.get("templateType");
            String category = (String) templateData.get("category");
            String description = (String) templateData.get("description");
            String sqlContent = (String) templateData.get("sqlContent");
            String status = (String) templateData.get("status");
            Boolean isPublic = (Boolean) templateData.get("isPublic");
            List<Map<String, Object>> parameters = (List<Map<String, Object>>) templateData.get("parameters");

            // 参数校验
            if (StringUtil.isEmpty(templateName)) {
                return JsonBean.error("模板名称不能为空");
            }
            if (StringUtil.isEmpty(templateCode)) {
                return JsonBean.error("模板编码不能为空");
            }
            if (StringUtil.isEmpty(sqlContent)) {
                return JsonBean.error("SQL内容不能为空");
            }

            // 判断是新增还是更新
            boolean isUpdate = StringUtil.isNotEmpty(templateId);

            if (!isUpdate) {
                // 新增时生成ID和检查编码重复
                templateId = "TPL" + System.currentTimeMillis();

                String checkCodeSql = "SELECT COUNT(*) FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_CODE = ?";
                Integer codeCount = jdbcTemplate.queryForObject(checkCodeSql, Integer.class, templateCode);
                if (codeCount != null && codeCount > 0) {
                    return JsonBean.error("模板编码已存在");
                }
            }

            // 处理参数配置
            String parameterConfig = "";
            if (parameters != null && !parameters.isEmpty()) {
                try {
                    parameterConfig = new ObjectMapper().writeValueAsString(parameters);
                } catch (Exception e) {
                    log.warn("参数配置序列化失败", e);
                    parameterConfig = "[]";
                }
            }

            // 设置默认值
            if (StringUtil.isEmpty(templateType)) {
                templateType = "SELECT";
            }
            if (StringUtil.isEmpty(category)) {
                category = "BASIC";
            }
            if (StringUtil.isEmpty(status)) {
                status = "DRAFT";
            }

            String currentTime = DateUtil.getNowTime();
            String userId = staff.getStaffid() != null ? staff.getStaffid().toString() : "SYSTEM";

            if (isUpdate) {
                // 更新模板
                String updateSql = "UPDATE TBL_SQL_TEMPLATE SET " +
                        "TEMPLATE_NAME = ?, TEMPLATE_CODE = ?, DATA_SOURCE_ID = ?, TEMPLATE_TYPE = ?, " +
                        "CATEGORY = ?, SQL_CONTENT = ?, PARAMETER_CONFIG = ?, " +
                        "DESCRIPTION = ?, STATUS = ?, IS_PUBLIC = ?, " +
                        "UPDATE_TIME = ?, UPDATE_USER = ? " +
                        "WHERE TEMPLATE_ID = ?";

                int updateCount = jdbcTemplate.update(updateSql,
                        templateName, templateCode, dataSourceId, templateType,
                        category, sqlContent, parameterConfig,
                        description, status, isPublic != null && isPublic ? "Y" : "N",
                        currentTime, userId, templateId);

                if (updateCount > 0) {
                    return JsonBean.success("模板更新成功");
                } else {
                    return JsonBean.error("模板更新失败，模板不存在");
                }
            } else {
                // 新增模板
                String insertSql = "INSERT INTO TBL_SQL_TEMPLATE (" +
                        "TEMPLATE_ID, TEMPLATE_CODE, TEMPLATE_NAME, DATA_SOURCE_ID, TEMPLATE_TYPE, " +
                        "CATEGORY, SQL_CONTENT, PARAMETER_CONFIG, " +
                        "DESCRIPTION, COMPLEXITY_LEVEL, USAGE_COUNT, SUCCESS_RATE, AVG_EXECUTE_TIME, " +
                        "IS_SYSTEM, IS_PUBLIC, STATUS, CREATE_TIME, CREATE_USER, " +
                        "UPDATE_TIME, UPDATE_USER) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                // 简单的复杂度评估
                String complexityLevel = "LOW";
                if (sqlContent.toUpperCase().contains("WITH") ||
                    sqlContent.toUpperCase().contains("UNION") ||
                    sqlContent.toUpperCase().contains("CASE WHEN")) {
                    complexityLevel = "HIGH";
                } else if (sqlContent.toUpperCase().contains("JOIN") ||
                          sqlContent.toUpperCase().contains("GROUP BY")) {
                    complexityLevel = "MEDIUM";
                }

                int insertCount = jdbcTemplate.update(insertSql,
                        templateId, templateCode, templateName, dataSourceId, templateType,
                        category, sqlContent, parameterConfig,
                        description, complexityLevel, 0, 100, 0,
                        "N", isPublic != null && isPublic ? "Y" : "N", status,
                        currentTime, userId, currentTime, userId);

                if (insertCount > 0) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("templateId", templateId);

                    JsonBean jsonBean = new JsonBean();
                    jsonBean.setCode(1);
                    jsonBean.setMsg("模板保存成功");
                    jsonBean.setData(result);
                    return jsonBean.toString();
                } else {
                    return JsonBean.error("模板保存失败");
                }
            }
        } catch (Exception e) {
            log.error("保存SQL模板失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @Override
    public String deleteSqlTemplate(String templateId, TblStaffUtil staff) {
        try {
            log.info("删除SQL模板，模板ID: {}, 用户: {}", templateId, staff.getStaffid());

            // 参数验证
            if (StringUtil.isEmpty(templateId)) {
                return JsonBean.error("模板ID不能为空");
            }

            // 检查模板是否存在
            String checkSql = "SELECT COUNT(*) FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_ID = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, templateId);
            if (count == null || count == 0) {
                return JsonBean.error("模板不存在或已被删除");
            }

            // 检查是否为系统模板
            String systemCheckSql = "SELECT IS_SYSTEM FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_ID = ?";
            String isSystem = jdbcTemplate.queryForObject(systemCheckSql, String.class, templateId);
            if ("Y".equals(isSystem)) {
                return JsonBean.error("系统预置模板不能删除");
            }

            // 执行删除
            String deleteSql = "DELETE FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_ID = ?";
            int deleteCount = jdbcTemplate.update(deleteSql, templateId);

            if (deleteCount > 0) {
                log.info("删除SQL模板成功，模板ID: {}", templateId);
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除SQL模板失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @Override
    public String batchDeleteSqlTemplate(List<String> templateIds, TblStaffUtil staff) {
        try {
            log.info("批量删除SQL模板，模板数量: {}, 用户: {}", templateIds.size(), staff.getStaffid());

            if (templateIds == null || templateIds.isEmpty()) {
                return JsonBean.error("请选择要删除的模板");
            }

            // 检查是否包含系统模板
            String systemCheckSql = "SELECT COUNT(*) FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_ID IN (" +
                    templateIds.stream().map(id -> "?").collect(Collectors.joining(",")) +
                    ") AND IS_SYSTEM = 'Y'";
            Integer systemCount = jdbcTemplate.queryForObject(systemCheckSql, Integer.class, templateIds.toArray());
            if (systemCount != null && systemCount > 0) {
                return JsonBean.error("选中的模板中包含系统预置模板，无法删除");
            }

            // 批量删除
            String deleteSql = "DELETE FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_ID IN (" +
                    templateIds.stream().map(id -> "?").collect(Collectors.joining(",")) + ")";
            int deleteCount = jdbcTemplate.update(deleteSql, templateIds.toArray());

            log.info("批量删除SQL模板完成，删除数量: {}", deleteCount);
            return JsonBean.success("批量删除成功，共删除 " + deleteCount + " 个模板");

        } catch (Exception e) {
            log.error("批量删除SQL模板失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量更新SQL模板状态
     */
    public boolean batchUpdateSqlTemplateStatus(List<String> templateIds, String status, String updateUser) {
        try {
            log.info("开始批量更新SQL模板状态 - templateIds: {}, status: {}", templateIds, status);

            if (templateIds == null || templateIds.isEmpty()) {
                log.warn("模板ID列表为空");
                return false;
            }

            if (StringUtil.isEmpty(status)) {
                log.warn("状态参数为空");
                return false;
            }

            // 构建批量更新SQL
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE TBL_SQL_TEMPLATE SET STATUS = ?, UPDATE_USER = ?, UPDATE_TIME = SYSDATE WHERE TEMPLATE_ID IN (");

            List<Object> paramList = new ArrayList<>();
            paramList.add(status);
            paramList.add(updateUser);

            // 添加模板ID参数
            for (int i = 0; i < templateIds.size(); i++) {
                if (i > 0) {
                    sql.append(", ");
                }
                sql.append("?");
                paramList.add(templateIds.get(i));
            }
            sql.append(")");

            log.info("批量更新SQL: {}", sql.toString());
            log.info("参数列表: {}", paramList);

            // 执行批量更新
            int updateCount = jdbcTemplate.update(sql.toString(), paramList.toArray());

            log.info("批量更新SQL模板状态完成，更新数量: {}", updateCount);
            return updateCount > 0;

        } catch (Exception e) {
            log.error("批量更新SQL模板状态失败", e);
            return false;
        }
    }

    /**
     * 使用SQL模板（更新使用次数和最后使用时间）
     */
    @Override
    public String useSqlTemplate(String templateId, TblStaffUtil staff) {
        try {
            log.info("使用SQL模板，模板ID: {}, 用户: {}", templateId, staff.getStaffid());

            // 参数验证
            if (StringUtil.isEmpty(templateId)) {
                return JsonBean.error("模板ID不能为空");
            }

            // 检查模板是否存在
            String checkSql = "SELECT COUNT(*) FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_ID = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, templateId);
            if (count == null || count == 0) {
                return JsonBean.error("模板不存在");
            }

            // 更新使用次数和最后使用时间
            String updateSql = "UPDATE TBL_SQL_TEMPLATE SET " +
                    "USAGE_COUNT = COALESCE(USAGE_COUNT, 0) + 1, " +
                    "LAST_USE_TIME = SYSDATE, " +
                    "UPDATE_USER = ?, " +
                    "UPDATE_TIME = SYSDATE " +
                    "WHERE TEMPLATE_ID = ?";

            int updateCount = jdbcTemplate.update(updateSql,
                    staff.getStaffid().toString(),
                    templateId);

            if (updateCount > 0) {
                log.info("模板使用记录更新成功 - templateId: {}", templateId);
                return JsonBean.success("使用记录已更新");
            } else {
                return JsonBean.error("更新使用记录失败");
            }
        } catch (Exception e) {
            log.error("使用SQL模板失败", e);
            return JsonBean.error("使用模板失败: " + e.getMessage());
        }
    }

    /**
     * 复制SQL模板
     */
    @Override
    public String copySqlTemplate(String templateId, String newTemplateName, TblStaffUtil staff) {
        try {
            log.info("复制SQL模板，源模板ID: {}, 新模板名称: {}, 用户: {}", templateId, newTemplateName, staff.getStaffid());

            // 参数验证
            if (StringUtil.isEmpty(templateId)) {
                return JsonBean.error("源模板ID不能为空");
            }
            if (StringUtil.isEmpty(newTemplateName)) {
                return JsonBean.error("新模板名称不能为空");
            }

            // 检查源模板是否存在
            String checkSql = "SELECT COUNT(*) FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_ID = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, templateId);
            if (count == null || count == 0) {
                return JsonBean.error("源模板不存在");
            }

            // 检查新模板名称是否已存在
            String checkNameSql = "SELECT COUNT(*) FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_NAME = ?";
            Integer nameCount = jdbcTemplate.queryForObject(checkNameSql, Integer.class, newTemplateName);
            if (nameCount != null && nameCount > 0) {
                return JsonBean.error("模板名称已存在，请使用其他名称");
            }

            // 查询源模板信息
            String selectSql = "SELECT * FROM TBL_SQL_TEMPLATE WHERE TEMPLATE_ID = ?";
            Map<String, Object> sourceTemplate = jdbcTemplate.queryForMap(selectSql, templateId);

            // 生成新的模板ID
            String newTemplateId = "TPL" + System.currentTimeMillis();

            // 生成新的模板编码（在原编码基础上加后缀）
            String sourceCode = (String) sourceTemplate.get("TEMPLATE_CODE");
            String newTemplateCode = sourceCode + "_COPY_" + System.currentTimeMillis();

            // 插入新模板
            String insertSql = "INSERT INTO TBL_SQL_TEMPLATE (" +
                    "TEMPLATE_ID, TEMPLATE_CODE, TEMPLATE_NAME, DATA_SOURCE_ID, " +
                    "TEMPLATE_TYPE, CATEGORY, DESCRIPTION, DATABASE_TYPE, " +
                    "COMPLEXITY_LEVEL, SQL_CONTENT, PARAMETER_CONFIG, THRESHOLD_CONFIG, " +
                    "WARNING_CONFIG, USAGE_COUNT, SUCCESS_RATE, AVG_EXECUTE_TIME, " +
                    "IS_SYSTEM, IS_PUBLIC, STATUS, CREATE_USER, CREATE_TIME, " +
                    "UPDATE_USER, UPDATE_TIME" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE)";

            int result = jdbcTemplate.update(insertSql,
                    newTemplateId,
                    newTemplateCode,
                    newTemplateName,
                    sourceTemplate.get("DATA_SOURCE_ID"),
                    sourceTemplate.get("TEMPLATE_TYPE"),
                    sourceTemplate.get("CATEGORY"),
                    sourceTemplate.get("DESCRIPTION"),
                    sourceTemplate.get("DATABASE_TYPE"),
                    sourceTemplate.get("COMPLEXITY_LEVEL"),
                    sourceTemplate.get("SQL_CONTENT"),
                    sourceTemplate.get("PARAMETER_CONFIG"),
                    sourceTemplate.get("THRESHOLD_CONFIG"),
                    sourceTemplate.get("WARNING_CONFIG"),
                    0, // 使用次数重置为0
                    100.00, // 成功率重置为100%
                    0, // 平均执行时间重置为0
                    "N", // 复制的模板设为非系统模板
                    sourceTemplate.get("IS_PUBLIC"),
                    "Y", // 默认启用
                    staff.getStaffid(),
                    staff.getStaffid()
            );

            if (result > 0) {
                log.info("SQL模板复制成功，新模板ID: {}", newTemplateId);

                // 返回新模板的基本信息
                Map<String, Object> newTemplateInfo = new HashMap<>();
                newTemplateInfo.put("templateId", newTemplateId);
                newTemplateInfo.put("templateCode", newTemplateCode);
                newTemplateInfo.put("templateName", newTemplateName);
                newTemplateInfo.put("createTime", new java.util.Date());

                return JsonBean.success(newTemplateInfo, newTemplateInfo);
            } else {
                return JsonBean.error("复制失败");
            }

        } catch (Exception e) {
            log.error("复制SQL模板失败", e);
            return JsonBean.error("复制失败: " + e.getMessage());
        }
    }

    /**
     * 测试SQL语句执行
     */
    @Override
    public String testSqlExecution(String dataSourceId, String sqlContent, List<Map<String, Object>> parameters, TblStaffUtil staff) {
        try {
            log.info("测试SQL执行，数据源ID: {}, 用户: {}", dataSourceId, staff.getStaffid());

            // 参数验证
            if (StringUtil.isEmpty(dataSourceId)) {
                return JsonBean.error("数据源ID不能为空");
            }
            if (StringUtil.isEmpty(sqlContent)) {
                return JsonBean.error("SQL内容不能为空");
            }

            // 记录开始时间
            long startTime = System.currentTimeMillis();

            // 清理SQL语句
            String cleanSql = sqlContent.trim();
            if (cleanSql.endsWith(";")) {
                cleanSql = cleanSql.substring(0, cleanSql.length() - 1);
            }

            // 处理参数替换
            if (parameters != null && !parameters.isEmpty()) {
                log.info("开始处理SQL参数替换，参数数量: {}", parameters.size());
                log.info("参数详情: {}", parameters);

                for (Map<String, Object> param : parameters) {
                    log.info("处理参数: {}", param);
                    String paramName = (String) param.get("name");
                    Object paramValue = param.get("value");
                    String paramType = (String) param.get("type");

                    log.info("参数解析: name={}, value={}, type={}", paramName, paramValue, paramType);

                    if (StringUtil.isNotEmpty(paramName) && paramValue != null && !paramValue.toString().trim().isEmpty()) {
                        String placeholder = "${" + paramName + "}";
                        String valueStr;

                        // 根据参数类型处理值
                        if ("STRING".equals(paramType) || "VARCHAR".equals(paramType) || "String".equals(paramType)) {
                            valueStr = "'" + paramValue.toString().replace("'", "''") + "'";
                        } else if ("NUMBER".equals(paramType) || "INTEGER".equals(paramType) || "DECIMAL".equals(paramType) || "Number".equals(paramType)) {
                            valueStr = paramValue.toString();
                        } else if ("DATE".equals(paramType) || "DATETIME".equals(paramType) || "Date".equals(paramType)) {
                            valueStr = "'" + paramValue.toString() + "'";
                        } else if ("BOOLEAN".equals(paramType) || "Boolean".equals(paramType)) {
                            valueStr = paramValue.toString();
                        } else {
                            // 默认处理为字符串
                            valueStr = "'" + paramValue.toString().replace("'", "''") + "'";
                        }

                        log.info("参数替换前SQL包含占位符: {}", cleanSql.contains(placeholder));
                        cleanSql = cleanSql.replace(placeholder, valueStr);
                        log.info("参数替换: {} -> {}", placeholder, valueStr);
                    } else {
                        log.warn("跳过无效参数: name={}, value={}, type={}", paramName, paramValue, paramType);
                    }
                }
                log.info("参数替换完成，最终SQL: {}", cleanSql);
            } else {
                log.info("无参数需要替换，参数为空或null: {}", parameters);
            }

            // 检查是否还有未替换的参数
            if (cleanSql.contains("${") && cleanSql.contains("}")) {
                // 提取未替换的参数
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\$\\{([^}]+)\\}");
                java.util.regex.Matcher matcher = pattern.matcher(cleanSql);
                java.util.Set<String> unreplacedParams = new java.util.HashSet<>();
                while (matcher.find()) {
                    unreplacedParams.add(matcher.group(1));
                }
                if (!unreplacedParams.isEmpty()) {
                    return JsonBean.error("SQL中包含未定义的参数: " + String.join(", ", unreplacedParams) + "。请在参数定义中添加这些参数。");
                }
            }

            // 安全检查 - 禁止危险的DML和DDL操作
            String upperSql = cleanSql.toUpperCase().trim();

            // 检查是否包含危险操作（数据修改和结构修改）
            String[] dangerousKeywords = {
                "DELETE", "UPDATE", "INSERT", "MERGE",  // DML操作
                "DROP", "ALTER", "CREATE", "TRUNCATE",  // DDL操作
                "GRANT", "REVOKE",                      // 权限操作
                "EXEC", "EXECUTE"                       // 存储过程执行（可能有风险）
            };

            for (String keyword : dangerousKeywords) {
                if (upperSql.contains(keyword)) {
                    return JsonBean.error("为了安全考虑，测试功能不支持包含 " + keyword + " 的语句");
                }
            }

            // 限制查询结果数量 - 只对简单SELECT语句添加LIMIT
            if (!upperSql.contains("LIMIT") && !upperSql.contains("FETCH") &&
                !upperSql.contains("OFFSET") && upperSql.startsWith("SELECT")) {
                // 对于达梦数据库，使用FETCH语法
                cleanSql += " OFFSET 0 ROWS FETCH NEXT 100 ROWS ONLY";
                log.info("为SELECT语句自动添加LIMIT限制");
            }

            log.info("执行测试SQL: {}", cleanSql);

            // 执行SQL查询 - 使用限制查询避免大结果集
            List<Map<String, Object>> resultList;
            try {
                // 设置查询超时时间为30秒
                resultList = jdbcTemplate.queryForList(cleanSql);

                // 限制结果集大小，避免内存溢出
                if (resultList.size() > 1000) {
                    log.warn("查询结果超过1000条，截取前1000条显示");
                    resultList = resultList.subList(0, 1000);
                }
            } catch (Exception queryEx) {
                log.error("SQL查询执行异常: {}", queryEx.getMessage());
                throw new RuntimeException("SQL执行失败: " + queryEx.getMessage(), queryEx);
            }

            // 计算执行时间
            long executeTime = System.currentTimeMillis() - startTime;

            // 提取列名
            Set<String> columnSet = new LinkedHashSet<>();
            if (!resultList.isEmpty()) {
                columnSet.addAll(resultList.get(0).keySet());
            }
            List<String> columns = new ArrayList<>(columnSet);

            // 构建返回结果
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("executeTime", executeTime);
            testResult.put("affectedRows", resultList.size());
            testResult.put("data", resultList);
            testResult.put("columns", columns);
            testResult.put("executedSql", cleanSql);

            log.info("SQL测试执行成功，返回{}条记录，耗时{}ms", resultList.size(), executeTime);

            return JsonBean.success(testResult, testResult);

        } catch (Exception e) {
            log.error("测试SQL执行失败", e);

            // 构建错误结果
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("executeTime", 0);
            errorResult.put("affectedRows", 0);
            errorResult.put("data", new ArrayList<>());
            errorResult.put("columns", new ArrayList<>());
            errorResult.put("executedSql", sqlContent);
            errorResult.put("errorMessage", e.getMessage());

            return JsonBean.error("SQL执行失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getTableStructureDetails(TableQueryDTO queryDTO) {
        try {
            // 参数校验
            if (!StringUtils.hasText(queryDTO.getDataSourceId())) {
                return new JsonBean(0, "数据源ID不能为空", null);
            }

            // 获取表总数
            int total = tableStructureMapper.getTableCount(queryDTO.getDataSourceId(), null, null);

            if (total == 0) {
                return new JsonBean(1, "查询成功", new HashMap<String, Object>() {{
                    put("records", new ArrayList<>());
                    put("total", 0);
                    put("pageNum", queryDTO.getPageNum());
                    put("pageSize", queryDTO.getPageSize());
                    put("pages", 0);
                }});
            }

            // 计算分页偏移量
            int offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();

            // 一次性获取分页的表结构数据
            List<TblTableStructure> pagedStructures = tableStructureMapper.getTableStructureDetailsPaged(
                    queryDTO.getDataSourceId(), offset, queryDTO.getPageSize());

            // 按表名分组
            Map<String, List<TblTableStructure>> tableGroups = pagedStructures.stream()
                    .collect(Collectors.groupingBy(TblTableStructure::getTableName));

            // 构建前端期望的数据结构
            List<Map<String, Object>> tableList = new ArrayList<>();
            for (Map.Entry<String, List<TblTableStructure>> entry : tableGroups.entrySet()) {
                String tableName = entry.getKey();
                List<TblTableStructure> columns = entry.getValue();

                Map<String, Object> tableInfo = new HashMap<>();
                tableInfo.put("tableName", tableName);
                tableInfo.put("tableComment", columns.get(0).getTableComment());

                // 构建列信息
                List<Map<String, Object>> columnList = columns.stream().map(col -> {
                    Map<String, Object> columnInfo = new HashMap<>();
                    columnInfo.put("columnName", col.getColumnName());
                    columnInfo.put("dataType", col.getColumnType());
                    columnInfo.put("columnComment", col.getColumnComment());
                    columnInfo.put("isPrimaryKey", "Y".equals(col.getIsPrimaryKey()));
                    columnInfo.put("isNullable", "Y".equals(col.getIsNullable()));
                    return columnInfo;
                }).collect(Collectors.toList());

                tableInfo.put("columns", columnList);
                tableList.add(tableInfo);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("records", tableList);
            result.put("total", total);
            result.put("pageNum", queryDTO.getPageNum());
            result.put("pageSize", queryDTO.getPageSize());
            result.put("pages", (int) Math.ceil((double) total / queryDTO.getPageSize()));

            return new JsonBean(1, "查询成功", result);

        } catch (Exception e) {
            log.error("查询表结构详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean validateSqlSyntax(String sqlContent, String databaseType, TblStaffUtil staff) {
        try {
            log.info("验证SQL语法 - 用户: {}, 数据库类型: {}", staff.getStaffid(), databaseType);

            // 参数验证
            if (StringUtil.isEmpty(sqlContent)) {
                return new JsonBean(0, "SQL内容不能为空", null);
            }

            // 清理SQL内容
            String cleanSql = sqlContent.trim();
            if (cleanSql.isEmpty()) {
                return new JsonBean(0, "SQL内容不能为空", null);
            }

            // 基本语法检查
            Map<String, Object> validationResult = new HashMap<>();
            validationResult.put("valid", true);
            validationResult.put("message", "SQL语法验证通过");
            validationResult.put("warnings", new ArrayList<>());
            validationResult.put("errors", new ArrayList<>());

            // 检查SQL类型
            String upperSql = cleanSql.toUpperCase();
            if (!upperSql.startsWith("SELECT")) {
                List<String> warnings = new ArrayList<>();
                warnings.add("建议使用SELECT查询语句");
                validationResult.put("warnings", warnings);
            }

            // 检查危险操作
            if (upperSql.contains("DROP ") || upperSql.contains("DELETE ") ||
                upperSql.contains("TRUNCATE ") || upperSql.contains("ALTER ")) {
                validationResult.put("valid", false);
                validationResult.put("message", "SQL包含危险操作，验证失败");

                List<String> errors = new ArrayList<>();
                errors.add("不允许使用DDL或DML操作语句");
                validationResult.put("errors", errors);

                return new JsonBean(0, "SQL验证失败", validationResult);
            }

            // 基本语法结构检查
            if (!upperSql.contains("FROM") && upperSql.startsWith("SELECT")) {
                List<String> warnings = (List<String>) validationResult.get("warnings");
                warnings.add("SELECT语句通常需要FROM子句");
                validationResult.put("warnings", warnings);
            }

            log.info("SQL语法验证完成 - 结果: {}", validationResult.get("valid"));
            return new JsonBean(1, "验证完成", validationResult);

        } catch (Exception e) {
            log.error("验证SQL语法失败", e);

            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("valid", false);
            errorResult.put("message", "验证过程中发生错误: " + e.getMessage());
            errorResult.put("warnings", new ArrayList<>());

            List<String> errors = new ArrayList<>();
            errors.add("系统错误: " + e.getMessage());
            errorResult.put("errors", errors);

            return new JsonBean(0, "验证失败", errorResult);
        }
    }
}
