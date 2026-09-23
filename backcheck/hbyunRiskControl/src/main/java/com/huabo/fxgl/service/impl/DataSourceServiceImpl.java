package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.DataSourceQueryDTO;
import com.huabo.fxgl.dto.DataSourceSaveDTO;
import com.huabo.fxgl.entity.TblDataSource;
import com.huabo.fxgl.entity.TblTableStructure;
//import com.huabo.fxgl.mapper.TblDataSourceMapper;
import com.huabo.fxgl.mapper.TblDataSourceMapper;
import com.huabo.fxgl.mapper.TblTableStructureMapper;
import com.huabo.fxgl.service.IDataSourceService;
import com.huabo.fxgl.util.DatabaseConnectionUtil;
import com.huabo.fxgl.vo.DataSourceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.UUID;

/**
 * 数据源管理服务实现类
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Slf4j
@Service
public class DataSourceServiceImpl extends ServiceImpl<TblDataSourceMapper, TblDataSource> implements IDataSourceService {

    @Autowired
    private TblTableStructureMapper tableStructureMapper;

    @Override
    public JsonBean getDataSourceList(DataSourceQueryDTO queryDTO) {
        try {
            // 设置分页参数
            Page<DataSourceVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
            
            // 分页查询
            IPage<DataSourceVO> pageResult = baseMapper.selectDataSourcePage(page, queryDTO);
            
            // 构建返回结果 - 前端期望数据在data.records中
            Map<String, Object> data = new HashMap<>();
            data.put("records", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            data.put("pageNum", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());
            data.put("pages", pageResult.getPages());

            return new JsonBean(1, "查询成功", data);
        } catch (Exception e) {
            log.error("查询数据源列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveDataSource(DataSourceSaveDTO saveDTO, String currentUser) {
        try {
            // 手动参数校验
            if (!StringUtils.hasText(saveDTO.getSourceName())) {
                return new JsonBean(0, "数据源名称不能为空", null);
            }
            if (!StringUtils.hasText(saveDTO.getSourceType())) {
                return new JsonBean(0, "数据源类型不能为空", null);
            }
            if (!StringUtils.hasText(saveDTO.getHostIp())) {
                return new JsonBean(0, "主机IP不能为空", null);
            }
            if (saveDTO.getPort() == null) {
                return new JsonBean(0, "端口不能为空", null);
            }
            if (!StringUtils.hasText(saveDTO.getDatabaseName())) {
                return new JsonBean(0, "数据库名不能为空", null);
            }
            if (!StringUtils.hasText(saveDTO.getUsername())) {
                return new JsonBean(0, "用户名不能为空", null);
            }
            if (!StringUtils.hasText(saveDTO.getPassword())) {
                return new JsonBean(0, "密码不能为空", null);
            }

            // 验证数据源名称唯一性
            if (!isSourceNameUnique(saveDTO.getSourceName(), saveDTO.getSourceId())) {
                return new JsonBean(0, "数据源名称已存在", null);
            }
            
            TblDataSource dataSource;
            boolean isUpdate = StringUtils.hasText(saveDTO.getSourceId());
            
            if (isUpdate) {
                // 更新操作
                dataSource = getById(saveDTO.getSourceId());
                if (dataSource == null) {
                    return new JsonBean(0, "数据源不存在", null);
                }
                BeanUtils.copyProperties(saveDTO, dataSource);
                dataSource.setUpdateUser(currentUser);
                dataSource.setUpdateTime(LocalDateTime.now());
            } else {
                // 新增操作
                dataSource = new TblDataSource();
                BeanUtils.copyProperties(saveDTO, dataSource);
                dataSource.setSourceId(UUID.randomUUID().toString().replace("-", ""));
                dataSource.setCreateUser(currentUser);
                dataSource.setCreateTime(LocalDateTime.now());
                dataSource.setStatus("ACTIVE");
            }
            
            // 构建连接URL
            if (!StringUtils.hasText(dataSource.getConnectionUrl())) {
                String connectionUrl = buildConnectionUrl(
                    dataSource.getSourceType(), 
                    dataSource.getHostIp(), 
                    dataSource.getPort(), 
                    dataSource.getDatabaseName()
                );
                dataSource.setConnectionUrl(connectionUrl);
            }
            
            // 保存数据源
            boolean success = saveOrUpdate(dataSource);
            if (success) {
                JsonBean result = new JsonBean(1, isUpdate ? "更新成功" : "保存成功", null);
                result.setResult(dataSource.getSourceId());
                return result;
            } else {
                return new JsonBean(0, isUpdate ? "更新失败" : "保存失败", null);
            }
        } catch (Exception e) {
            log.error("保存数据源失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean deleteDataSource(String sourceId) {
        try {
            TblDataSource dataSource = getById(sourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }
            
            // TODO: 检查是否有关联的数据模型，如果有则不允许删除
            
            boolean success = removeById(sourceId);
            if (success) {
                return new JsonBean(1, "删除成功", null);
            } else {
                return new JsonBean(0, "删除失败", null);
            }
        } catch (Exception e) {
            log.error("删除数据源失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean batchDeleteDataSource(List<String> sourceIds) {
        try {
            if (sourceIds == null || sourceIds.isEmpty()) {
                return new JsonBean(0, "请选择要删除的数据源", null);
            }
            
            // TODO: 批量检查是否有关联的数据模型
            
            boolean success = removeByIds(sourceIds);
            if (success) {
                return new JsonBean(1, "批量删除成功", null);
            } else {
                return new JsonBean(0, "批量删除失败", null);
            }
        } catch (Exception e) {
            log.error("批量删除数据源失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getDataSourceDetail(String sourceId) {
        try {
            TblDataSource dataSource = getById(sourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }
            
            // 转换为VO对象，隐藏密码
            DataSourceVO vo = new DataSourceVO();
            BeanUtils.copyProperties(dataSource, vo);
            // 不返回密码信息
            
            JsonBean jsonBean = new JsonBean(1, "查询成功", null);
            jsonBean.setResult(vo);
            return jsonBean;
        } catch (Exception e) {
            log.error("查询数据源详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean testDataSourceConnection(String sourceId) {
        try {
            TblDataSource dataSource = getById(sourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }
            
            long startTime = System.currentTimeMillis();
            String testResult = "FAILED";
            String errorMessage = "";
            
            try {
                // 测试数据库连接
                Connection connection = DriverManager.getConnection(
                    dataSource.getConnectionUrl(),
                    dataSource.getUsername(),
                    dataSource.getPassword()
                );
                connection.close();
                testResult = "SUCCESS";
            } catch (SQLException e) {
                errorMessage = e.getMessage();
                log.warn("数据源连接测试失败: {}", e.getMessage());
            }
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            // 更新测试结果
            baseMapper.updateConnectionTestResult(sourceId, testResult);
            
            Map<String, Object> result = new HashMap<>();
            result.put("connectionResult", testResult);
            result.put("responseTime", responseTime);
            result.put("testTime", LocalDateTime.now());
            if (StringUtils.hasText(errorMessage)) {
                result.put("errorMessage", errorMessage);
            }
            
            JsonBean jsonBean = new JsonBean(1, "连接测试" + ("SUCCESS".equals(testResult) ? "成功" : "失败"), null);
            jsonBean.setResult(result);
            return jsonBean;
        } catch (Exception e) {
            log.error("测试数据源连接失败", e);
            return new JsonBean(0, "测试失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean syncTableStructure(String sourceId) {
        try {
            // 1. 获取数据源信息
            TblDataSource dataSource = getById(sourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }

            log.info("开始同步数据源[{}]的表结构", dataSource.getSourceName());

            // 2. 清除旧的表结构数据
            QueryWrapper<TblTableStructure> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("DATA_SOURCE_ID", sourceId);
            tableStructureMapper.delete(deleteWrapper);

            // 3. 获取数据库中的所有表
            List<Map<String, Object>> tables = DatabaseConnectionUtil.getAllTables(dataSource);
            log.info("发现{}个表", tables.size());

            int totalColumns = 0;
            int syncedTables = 0;

            // 4. 遍历每个表，获取列信息并保存
            for (Map<String, Object> tableInfo : tables) {
                String tableName = (String) tableInfo.get("tableName");
                String tableComment = (String) tableInfo.get("tableComment");

                try {
                    // 获取表的列信息
                    List<Map<String, Object>> columns = DatabaseConnectionUtil.getTableColumns(dataSource, tableName);

                    // 保存表结构信息
                    for (Map<String, Object> columnInfo : columns) {
                        TblTableStructure structure = new TblTableStructure();
                        structure.setStructureId(generateId());
                        structure.setDataSourceId(sourceId);
                        structure.setTableName(tableName);
                        structure.setTableComment(tableComment);
                        structure.setColumnName((String) columnInfo.get("columnName"));
                        structure.setColumnType((String) columnInfo.get("columnType"));
                        structure.setColumnLength((Integer) columnInfo.get("columnLength"));
                        structure.setColumnPrecision((Integer) columnInfo.get("columnPrecision"));
                        structure.setColumnScale((Integer) columnInfo.get("columnScale"));
                        structure.setIsNullable((String) columnInfo.get("isNullable"));
                        structure.setIsPrimaryKey((String) columnInfo.get("isPrimaryKey"));
                        structure.setColumnComment((String) columnInfo.get("columnComment"));
                        structure.setColumnDefault((String) columnInfo.get("columnDefault"));
                        structure.setColumnOrder((Integer) columnInfo.get("columnOrder"));
                        structure.setSyncTime(LocalDateTime.now());

                        tableStructureMapper.insert(structure);
                        totalColumns++;
                    }

                    syncedTables++;
                    log.info("已同步表[{}]，包含{}列", tableName, columns.size());
                } catch (Exception e) {
                    log.error("同步表[{}]失败: {}", tableName, e.getMessage());
                }
            }

            // 5. 更新数据源的表数量
            dataSource.setTableCount(syncedTables);
            dataSource.setUpdateTime(LocalDateTime.now());
            updateById(dataSource);

            Map<String, Object> result = new HashMap<>();
            result.put("syncedTables", syncedTables);
            result.put("totalColumns", totalColumns);
            result.put("syncTime", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            log.info("表结构同步完成，共同步{}个表，{}列", syncedTables, totalColumns);
            return new JsonBean(1, "表结构同步成功", result);
        } catch (Exception e) {
            log.error("同步表结构失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getActiveDataSources() {
        try {
            List<TblDataSource> activeDataSources = baseMapper.selectActiveDataSources();
            JsonBean jsonBean = new JsonBean(1, "查询成功", null);
            jsonBean.setResult(activeDataSources);
            return jsonBean;
        } catch (Exception e) {
            log.error("查询活跃数据源失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getDataSourceStatistics() {
        try {
            TblDataSourceMapper.DataSourceStatisticsVO statistics = baseMapper.selectDataSourceStatistics();
            // 前端期望数据在data字段中，而不是result字段
            return new JsonBean(1, "查询成功", statistics);
        } catch (Exception e) {
            log.error("查询数据源统计信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean enableDataSource(String sourceId) {
        try {
            TblDataSource dataSource = getById(sourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }
            
            dataSource.setStatus("ACTIVE");
            dataSource.setUpdateTime(LocalDateTime.now());
            boolean success = updateById(dataSource);
            
            if (success) {
                return new JsonBean(1, "启用成功", null);
            } else {
                return new JsonBean(0, "启用失败", null);
            }
        } catch (Exception e) {
            log.error("启用数据源失败", e);
            return new JsonBean(0, "启用失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean disableDataSource(String sourceId) {
        try {
            TblDataSource dataSource = getById(sourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }
            
            dataSource.setStatus("INACTIVE");
            dataSource.setUpdateTime(LocalDateTime.now());
            boolean success = updateById(dataSource);
            
            if (success) {
                return new JsonBean(1, "禁用成功", null);
            } else {
                return new JsonBean(0, "禁用失败", null);
            }
        } catch (Exception e) {
            log.error("禁用数据源失败", e);
            return new JsonBean(0, "禁用失败: " + e.getMessage(), null);
        }
    }

    @Override
    public boolean isSourceNameUnique(String sourceName, String sourceId) {
        QueryWrapper<TblDataSource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("SOURCE_NAME", sourceName);
        if (StringUtils.hasText(sourceId)) {
            queryWrapper.ne("SOURCE_ID", sourceId);
        }
        return count(queryWrapper) == 0;
    }

    @Override
    public String buildConnectionUrl(String sourceType, String hostIp, Integer port, String databaseName) {
        switch (sourceType.toUpperCase()) {
            case "DM":
                return String.format("jdbc:dm://%s:%d/%s", hostIp, port, databaseName);
            case "ORACLE":
                return String.format("jdbc:oracle:thin:@%s:%d:%s", hostIp, port, databaseName);
            case "MYSQL":
                return String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8&useSSL=false",
                                   hostIp, port, databaseName);
            default:
                return String.format("jdbc:%s://%s:%d/%s", sourceType.toLowerCase(), hostIp, port, databaseName);
        }
    }

    /**
     * 生成32位UUID
     */
    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
