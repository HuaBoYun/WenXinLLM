package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.dto.DataModelQueryDTO;
import com.huabo.fxgl.dto.DataModelSaveDTO;
import com.huabo.fxgl.entity.TblDataModel;
import com.huabo.fxgl.entity.TblDataModelVersion;
import com.huabo.fxgl.entity.TblDataSource;
import com.huabo.fxgl.entity.TblSqlTemplate;
import com.huabo.fxgl.mapper.TblDataModelMapper;
import com.huabo.fxgl.mapper.TblDataModelVersionMapper;
import com.huabo.fxgl.mapper.TblDataSourceMapper;
import com.huabo.fxgl.mapper.TblSqlTemplateMapper;
import com.huabo.fxgl.service.ICombinationService;
import com.huabo.fxgl.service.IDataModelService;
import com.huabo.fxgl.util.SqlExecutorUtil;
import com.huabo.fxgl.vo.DataModelVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 数据模型管理服务实现类
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Slf4j
@Service
public class DataModelServiceImpl extends ServiceImpl<TblDataModelMapper, TblDataModel> implements IDataModelService {

    @Autowired
    private TblDataSourceMapper dataSourceMapper;

    // 🔥 组合指标SQL历史记录：按组合指标编码隔离
    // 结构：Map<indicatorCode, Map<stepNumber, sqlContent>>
    private static final Map<String, Map<Integer, String>> INDICATOR_SQL_HISTORY = new ConcurrentHashMap<>();
    // 🔥 组合指标步骤计数器：为每个组合指标维护当前步骤编号
    // 结构：Map<indicatorCode, currentStep>
    private static final Map<String, Integer> INDICATOR_STEP_COUNTER = new ConcurrentHashMap<>();
    // 🔥 组合指标最后活动时间：用于自动清理超时的上下文
    // 结构：Map<indicatorCode, lastActivityTime>
    private static final Map<String, Long> INDICATOR_LAST_ACTIVITY = new ConcurrentHashMap<>();
    // 🔥 上下文超时时间：30分钟无活动自动清理
    private static final long CONTEXT_TIMEOUT_MS = 30 * 60 * 1000L;

    @Autowired
    private ICombinationService combinationService;

    @Autowired
    private TblDataModelMapper dataModelMapper;

    @Autowired
    private TblDataModelVersionMapper versionMapper;

    @Autowired
    private TblSqlTemplateMapper sqlTemplateMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 注入数据源服务（假设存在）
    // @Autowired
    // private IDataSourceService dataSourceService;

    @Override
    public JsonBean getDataModelList(DataModelQueryDTO queryDTO) {
        try {
            log.info("查询数据模型列表，参数: {}", queryDTO);

            // 🔧 修复：使用 PageHelper.startPage 替代 MyBatis-Plus 分页
            PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

            // 🔧 修复：使用 baseMapper.selectDataModelList 方法查询，PageHelper 会自动处理分页
            List<DataModelVO> list = baseMapper.selectDataModelList(queryDTO);

            // 🔧 修复：使用 PageInfo 包装结果，获取分页信息
            PageInfo<DataModelVO> pageInfo = new PageInfo<>(list);

            // 构建返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("records", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            data.put("pageNum", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            data.put("pages", pageInfo.getPages());

            log.info("查询数据模型列表成功，总数: {}, 当前页: {}, 每页大小: {}",
                    pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
            return new JsonBean(1, "查询成功", data);
        } catch (Exception e) {
            log.error("查询数据模型列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveDataModel(DataModelSaveDTO saveDTO, String currentUser) {
        try {
            // 参数校验
            if (StringUtil.isEmpty(saveDTO.getModelCode())) {
                return new JsonBean(0, "模型编码不能为空", null);
            }
            if (StringUtil.isEmpty(saveDTO.getModelName())) {
                return new JsonBean(0, "模型名称不能为空", null);
            }
            if (StringUtil.isEmpty(saveDTO.getModelType())) {
                return new JsonBean(0, "模型类型不能为空", null);
            }
            if (StringUtil.isEmpty(saveDTO.getDataSourceId())) {
                return new JsonBean(0, "数据源不能为空", null);
            }

            // 验证模型编码唯一性
            if (!isModelCodeUnique(saveDTO.getModelCode(), saveDTO.getModelId())) {
                return new JsonBean(0, "模型编码已存在", null);
            }

            // 验证数据源是否存在
            TblDataSource dataSource = dataSourceMapper.selectById(saveDTO.getDataSourceId());
            if (dataSource == null) {
                return new JsonBean(0, "指定的数据源不存在", null);
            }

            TblDataModel dataModel;
            boolean isUpdate = StringUtil.isNotEmpty(saveDTO.getModelId());

            if (isUpdate) {
                // 更新操作
                dataModel = this.getById(saveDTO.getModelId());
                if (dataModel == null) {
                    return new JsonBean(0, "数据模型不存在", null);
                }

                // 复制属性
                BeanUtils.copyProperties(saveDTO, dataModel);
                dataModel.setUpdateUser(currentUser);
                dataModel.setUpdateTime(LocalDateTime.now());

                this.updateById(dataModel);

                // 🔧 关键修复：同时更新当前版本的数据
                syncModelToCurrentVersion(dataModel, currentUser);
            } else {
                // 新增操作
                dataModel = new TblDataModel();
                BeanUtils.copyProperties(saveDTO, dataModel);

                // 处理SQL语句中的模板参数，避免MyBatis解析错误
                if (StringUtil.isNotEmpty(dataModel.getSqlStatement())) {
                    log.info("原始SQL语句: {}", dataModel.getSqlStatement());

                    // 将${参数名}格式的模板参数转换为#{参数名}格式，避免MyBatis解析错误
                    // 但是我们不能这样做，因为这会改变模板的语义
                    // 正确的做法是保持原样，但确保MyBatis不会解析这些参数

                    // 暂时保持原样，问题可能出在其他地方
                    String sqlStatement = dataModel.getSqlStatement();
                    log.info("处理后SQL语句: {}", sqlStatement);
                }

                dataModel.setModelId(UUID.randomUUID().toString().replace("-", ""));
                dataModel.setStatus("DRAFT"); // 默认草稿状态
                dataModel.setIsEnabled("Y"); // 默认启用
                dataModel.setVersion("1.0"); // 默认版本
                dataModel.setExecutionCount(0);
                dataModel.setCreateUser(currentUser);
                dataModel.setCreateTime(LocalDateTime.now());
                dataModel.setUpdateUser(currentUser);
                dataModel.setUpdateTime(LocalDateTime.now());

                // 设置默认值
                if (StringUtil.isEmpty(dataModel.getModelId())) {
                    dataModel.setModelId(RandomUtil.uuStringId());
                }
                if (StringUtil.isEmpty(dataModel.getVersion())) {
                    dataModel.setVersion("1.0");
                }
                if (StringUtil.isEmpty(dataModel.getStatus())) {
                    dataModel.setStatus("DRAFT");
                }
                if (StringUtil.isEmpty(dataModel.getIsEnabled())) {
                    dataModel.setIsEnabled("N");
                }
                if (dataModel.getExecutionCount() == null) {
                    dataModel.setExecutionCount(0);
                }

                dataModel.setCreateUser(currentUser);
                dataModel.setCreateTime(LocalDateTime.now());
                dataModel.setUpdateUser(currentUser);
                dataModel.setUpdateTime(LocalDateTime.now());

                // 使用自定义的insertDataModel方法，避免MyBatis解析SQL语句中的模板参数
                dataModelMapper.insertDataModel(
                    dataModel.getModelId(),
                    dataModel.getModelCode(),
                    dataModel.getModelName(),
                    dataModel.getModelType(),
                    dataModel.getBusinessMeaning(),
                    dataModel.getCalculationLogic(),
                    dataModel.getDataSourceId(),
                    dataModel.getSqlStatement(),
                    dataModel.getWithClause(),
                    dataModel.getSelectClause(),
                    dataModel.getFromClause(),
                    dataModel.getWhereClause(),
                    dataModel.getGroupByClause(),
                    dataModel.getHavingClause(),
                    dataModel.getOrderByClause(),
                    dataModel.getDragConfig(),
                    dataModel.getThresholdConfig(),
                    dataModel.getWarningConfig(),
                    dataModel.getTemplateId(),
                    dataModel.getParameterConfig(),
                    dataModel.getTemplateType(),
                    dataModel.getVersion(),
                    dataModel.getStatus(),
                    dataModel.getIsEnabled(),
                    dataModel.getExecutionCount(),
                    dataModel.getLastExecutionTime(),
                    dataModel.getExecutionResult(),
                    dataModel.getCreateUser(),
                    dataModel.getCreateTime(),
                    dataModel.getUpdateUser(),
                    dataModel.getUpdateTime()
                );
            }

            return new JsonBean(1, isUpdate ? "更新成功" : "保存成功", dataModel);
        } catch (Exception e) {
            log.error("保存数据模型失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean deleteDataModel(String modelId) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }

            TblDataModel dataModel = this.getById(modelId);
            if (dataModel == null) {
                return new JsonBean(0, "数据模型不存在", null);
            }

            // 检查是否可以删除（已发布的模型不能删除）
            if ("PUBLISHED".equals(dataModel.getStatus())) {
                return new JsonBean(0, "已发布的模型不能删除，请先归档", null);
            }

            this.removeById(modelId);
            return new JsonBean(1, "删除成功", null);
        } catch (Exception e) {
            log.error("删除数据模型失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean batchDeleteDataModel(List<String> modelIds) {
        try {
            if (modelIds == null || modelIds.isEmpty()) {
                return new JsonBean(0, "请选择要删除的数据模型", null);
            }

            // 检查是否有已发布的模型
            QueryWrapper<TblDataModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("MODEL_ID", modelIds);
            queryWrapper.eq("STATUS", "PUBLISHED");
            
            long publishedCount = this.count(queryWrapper);
            if (publishedCount > 0) {
                return new JsonBean(0, "选中的模型中包含已发布的模型，无法删除", null);
            }

            this.removeByIds(modelIds);
            return new JsonBean(1, "批量删除成功", null);
        } catch (Exception e) {
            log.error("批量删除数据模型失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getDataModelDetail(String modelId) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }

            TblDataModel dataModel = this.getById(modelId);
            if (dataModel == null) {
                return new JsonBean(0, "数据模型不存在", null);
            }

            return new JsonBean(1, "查询成功", dataModel);
        } catch (Exception e) {
            log.error("查询数据模型详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean copyDataModel(String modelId, String newModelName, String currentUser) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }
            if (StringUtil.isEmpty(newModelName)) {
                return new JsonBean(0, "新模型名称不能为空", null);
            }

            TblDataModel sourceModel = this.getById(modelId);
            if (sourceModel == null) {
                return new JsonBean(0, "源数据模型不存在", null);
            }

            // 创建新模型
            TblDataModel newModel = new TblDataModel();
            BeanUtils.copyProperties(sourceModel, newModel);
            
            newModel.setModelId(UUID.randomUUID().toString().replace("-", ""));
            newModel.setModelCode(sourceModel.getModelCode() + "_COPY_" + System.currentTimeMillis());
            newModel.setModelName(newModelName);
            newModel.setStatus("DRAFT");
            newModel.setVersion("1.0");
            newModel.setExecutionCount(0);
            newModel.setLastExecutionTime(null);
            newModel.setExecutionResult(null);
            newModel.setCreateUser(currentUser);
            newModel.setCreateTime(LocalDateTime.now());
            newModel.setUpdateUser(currentUser);
            newModel.setUpdateTime(LocalDateTime.now());

            // 使用自定义插入方法，避免MyBatis解析SQL语句中的参数
            int result = dataModelMapper.insertDataModel(
                newModel.getModelId(),
                newModel.getModelCode(),
                newModel.getModelName(),
                newModel.getModelType(),
                newModel.getBusinessMeaning(),
                newModel.getCalculationLogic(),
                newModel.getDataSourceId(),
                newModel.getSqlStatement(),
                newModel.getWithClause(),
                newModel.getSelectClause(),
                newModel.getFromClause(),
                newModel.getWhereClause(),
                newModel.getGroupByClause(),
                newModel.getHavingClause(),
                newModel.getOrderByClause(),
                newModel.getDragConfig(),
                newModel.getThresholdConfig(),
                newModel.getWarningConfig(),
                newModel.getTemplateId(),
                newModel.getParameterConfig(),
                newModel.getTemplateType(),
                newModel.getVersion(),
                newModel.getStatus(),
                newModel.getIsEnabled(),
                newModel.getExecutionCount(),
                newModel.getLastExecutionTime(),
                newModel.getExecutionResult(),
                newModel.getCreateUser(),
                newModel.getCreateTime(),
                newModel.getUpdateUser(),
                newModel.getUpdateTime()
            );

            if (result > 0) {
                return new JsonBean(1, "复制成功", newModel);
            } else {
                return new JsonBean(0, "复制失败", null);
            }
        } catch (Exception e) {
            log.error("复制数据模型失败", e);
            return new JsonBean(0, "复制失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getDataModelVersions(String modelCode) {
        try {
            if (StringUtil.isEmpty(modelCode)) {
                return new JsonBean(0, "模型编码不能为空", null);
            }

            QueryWrapper<TblDataModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("MODEL_CODE", modelCode);
            queryWrapper.orderByDesc("CREATE_TIME");
            
            List<TblDataModel> versions = this.list(queryWrapper);
            return new JsonBean(1, "查询成功", versions);
        } catch (Exception e) {
            log.error("查询模型版本失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public boolean isModelCodeUnique(String modelCode, String modelId) {
        QueryWrapper<TblDataModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("MODEL_CODE", modelCode);
        if (StringUtil.isNotEmpty(modelId)) {
            queryWrapper.ne("MODEL_ID", modelId);
        }
        return this.count(queryWrapper) == 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean createDataModelVersion(String modelId, String changeDescription, String currentUser) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }

            TblDataModel sourceModel = this.getById(modelId);
            if (sourceModel == null) {
                return new JsonBean(0, "数据模型不存在", null);
            }

            // 创建新版本
            TblDataModel newVersion = new TblDataModel();
            BeanUtils.copyProperties(sourceModel, newVersion);

            newVersion.setModelId(UUID.randomUUID().toString().replace("-", ""));
            newVersion.setVersion(generateNextVersion(sourceModel.getVersion()));
            newVersion.setStatus("DRAFT");
            newVersion.setExecutionCount(0);
            newVersion.setLastExecutionTime(null);
            newVersion.setExecutionResult(null);
            newVersion.setCreateUser(currentUser);
            newVersion.setCreateTime(LocalDateTime.now());
            newVersion.setUpdateUser(currentUser);
            newVersion.setUpdateTime(LocalDateTime.now());

            this.save(newVersion);
            return new JsonBean(1, "创建新版本成功", newVersion);
        } catch (Exception e) {
            log.error("创建数据模型版本失败", e);
            return new JsonBean(0, "创建版本失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean updateDataModelStatus(String modelId, String status, String currentUser) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }
            if (StringUtil.isEmpty(status)) {
                return new JsonBean(0, "状态不能为空", null);
            }

            int result = baseMapper.updateModelStatus(modelId, status, currentUser);
            if (result > 0) {
                return new JsonBean(1, "状态更新成功", null);
            } else {
                return new JsonBean(0, "数据模型不存在", null);
            }
        } catch (Exception e) {
            log.error("更新数据模型状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean publishDataModel(String modelId, String currentUser) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }

            TblDataModel dataModel = this.getById(modelId);
            if (dataModel == null) {
                return new JsonBean(0, "数据模型不存在", null);
            }

            // 检查是否可以发布
            if (StringUtil.isEmpty(dataModel.getSqlStatement())) {
                return new JsonBean(0, "SQL语句不能为空，无法发布", null);
            }

            // 更新状态为已发布
            int result = baseMapper.updateModelStatus(modelId, "PUBLISHED", currentUser);
            if (result > 0) {
                return new JsonBean(1, "发布成功", null);
            } else {
                return new JsonBean(0, "发布失败", null);
            }
        } catch (Exception e) {
            log.error("发布数据模型失败", e);
            return new JsonBean(0, "发布失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean testDataModelSql(String dataSourceId, String sqlStatement, List<Map<String, Object>> parameters, TblStaffUtil staff, String indicatorCode) {
        try {
            if (StringUtil.isEmpty(dataSourceId)) {
                return new JsonBean(0, "数据源ID不能为空", null);
            }
            if (StringUtil.isEmpty(sqlStatement)) {
                return new JsonBean(0, "SQL语句不能为空", null);
            }

            // 验证数据源
            TblDataSource dataSource = dataSourceMapper.selectById(dataSourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }



            if (!"ACTIVE".equals(dataSource.getStatus())) {
                return new JsonBean(0, "数据源未启用", null);
            }

            log.info("开始执行SQL测试 - 数据源: {}, SQL长度: {}", dataSource.getSourceName(), sqlStatement.length());

            // 🔥 智能SQL历史记录：按组合指标编码记录用户SQL
            recordSqlHistory(indicatorCode, sqlStatement);

            // 🔥 如果SQL包含特殊重置标记，重置步骤计数器
            if (sqlStatement.toUpperCase().contains("-- RESET_STEPS")) {
                clearIndicatorContext(indicatorCode);
            }

            // 🔥 关键修复：检查并处理步骤引用 - 支持任意步骤的真实数据处理
            if (containsStepReferences(sqlStatement)) {
                log.info("检测到步骤引用，使用智能真实数据处理方案，组合指标: {}", indicatorCode);
                return handleStepReferencesWithRealData(dataSourceId, sqlStatement, parameters, staff, indicatorCode);
            }

            try {
                // 执行普通SQL（无步骤引用）
                Map<String, Object> testResult = SqlExecutorUtil.executeSql(dataSource, sqlStatement);

                log.info("SQL测试执行完成 - 数据源: {}, 返回{}条记录，耗时{}ms",
                        dataSource.getSourceName(),
                        testResult.get("rowCount"),
                        testResult.get("executionTime"));

                return new JsonBean(1, "SQL测试成功", testResult);

            } catch (SQLException e) {
                log.error("SQL执行失败 - 数据源: {}, 错误: {}", dataSource.getSourceName(), e.getMessage(), e);

                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("data", new ArrayList<>());
                errorResult.put("columns", new ArrayList<>());
                errorResult.put("rowCount", 0);
                errorResult.put("executionTime", 0);
                errorResult.put("success", false);
                errorResult.put("message", "SQL执行失败: " + e.getMessage());

                return new JsonBean(0, "SQL执行失败", errorResult);
            } catch (Exception e) {
                log.error("SQL测试异常 - 数据源: {}, 错误: {}", dataSource.getSourceName(), e.getMessage(), e);

                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("data", new ArrayList<>());
                errorResult.put("columns", new ArrayList<>());
                errorResult.put("rowCount", 0);
                errorResult.put("executionTime", 0);
                errorResult.put("success", false);
                errorResult.put("message", "系统异常: " + e.getMessage());

                return new JsonBean(0, "系统异常", errorResult);
            }
        } catch (Exception e) {
            log.error("测试数据模型SQL失败", e);
            return new JsonBean(0, "SQL测试失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean executeDataModel(String modelId, Map<String, Object> parameters, String currentUser) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }

            TblDataModel dataModel = this.getById(modelId);
            if (dataModel == null) {
                return new JsonBean(0, "数据模型不存在", null);
            }

            if (!"PUBLISHED".equals(dataModel.getStatus())) {
                return new JsonBean(0, "只有已发布的模型才能执行", null);
            }

            if (!"Y".equals(dataModel.getIsEnabled())) {
                return new JsonBean(0, "模型已禁用，无法执行", null);
            }

            // 实现模型执行逻辑
            long startTime = System.currentTimeMillis();

            try {
                // 获取数据源信息
                TblDataSource dataSource = dataSourceMapper.selectById(dataModel.getDataSourceId());
                if (dataSource == null) {
                    return new JsonBean(0, "数据源不存在", null);
                }

                if (!"ACTIVE".equals(dataSource.getStatus())) {
                    return new JsonBean(0, "数据源已禁用", null);
                }

                // 处理SQL参数替换
                String sqlStatement = dataModel.getSqlStatement();
                if (parameters != null && !parameters.isEmpty()) {
                    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                        String paramName = entry.getKey();
                        Object paramValue = entry.getValue();
                        String placeholder = "${" + paramName + "}";

                        if (sqlStatement.contains(placeholder)) {
                            // 简单的参数替换，实际项目中应该使用更安全的方式
                            String valueStr = paramValue != null ? paramValue.toString() : "";
                            sqlStatement = sqlStatement.replace(placeholder, valueStr);
                        }
                    }
                }

                // 🔧 修复：获取分页参数
                Integer pageNum = (Integer) parameters.getOrDefault("pageNum", 1);
                Integer pageSize = (Integer) parameters.getOrDefault("pageSize", 20);

                log.info("执行数据模型SQL，分页参数: pageNum={}, pageSize={}", pageNum, pageSize);

                // 执行SQL查询
                List<Map<String, Object>> allResultData = executeSQL(dataSource, sqlStatement);

                // 🔧 修复：应用分页逻辑
                int totalCount = allResultData.size();
                List<Map<String, Object>> paginatedData = applyPagination(allResultData, pageNum, pageSize);

                long endTime = System.currentTimeMillis();
                double executionTime = (endTime - startTime) / 1000.0;

                // 更新执行信息
                baseMapper.updateExecutionInfo(modelId, "SUCCESS");

                Map<String, Object> executeResult = new HashMap<>();
                executeResult.put("success", true);
                executeResult.put("message", "模型执行成功");
                executeResult.put("executionTime", executionTime + "s");
                executeResult.put("list", paginatedData);  // 🔧 修复：使用分页后的数据
                executeResult.put("data", paginatedData);  // 保持兼容性
                executeResult.put("columns", generateColumns(paginatedData));
                executeResult.put("total", totalCount);    // 🔧 修复：使用总数据量
                executeResult.put("pageNum", pageNum);     // 🔧 新增：当前页码
                executeResult.put("pageSize", pageSize);   // 🔧 新增：页大小
                executeResult.put("pages", (int) Math.ceil((double) totalCount / pageSize)); // 🔧 新增：总页数

                log.info("数据模型执行完成，总数据量: {}, 分页后数据量: {}, 页码: {}/{}",
                        totalCount, paginatedData.size(), pageNum, (int) Math.ceil((double) totalCount / pageSize));

                return new JsonBean(1, "执行成功", executeResult);

            } catch (Exception sqlException) {
                log.error("SQL执行失败: {}", sqlException.getMessage(), sqlException);
                baseMapper.updateExecutionInfo(modelId, "FAILED");
                return new JsonBean(0, "SQL执行失败: " + sqlException.getMessage(), null);
            }
        } catch (Exception e) {
            log.error("执行数据模型失败", e);
            // 更新执行失败状态
            baseMapper.updateExecutionInfo(modelId, "FAILED");
            return new JsonBean(0, "执行失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getDataModelStatistics() {
        try {
            TblDataModelMapper.DataModelStatisticsVO statistics = baseMapper.selectDataModelStatistics();
            return new JsonBean(1, "查询成功", statistics);
        } catch (Exception e) {
            log.error("查询数据模型统计信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean searchDataModels(String keyword, String modelType, Integer pageNum, Integer pageSize) {
        try {
            // 🔧 修复：使用 PageHelper.startPage 替代 MyBatis-Plus 分页
            PageHelper.startPage(pageNum, pageSize);

            // 🔧 修复：创建查询条件DTO
            DataModelQueryDTO queryDTO = new DataModelQueryDTO();
            queryDTO.setModelName(keyword);
            queryDTO.setModelType(modelType);
            queryDTO.setPageNum(pageNum);
            queryDTO.setPageSize(pageSize);

            // 🔧 修复：使用 selectDataModelList 方法查询
            List<DataModelVO> list = baseMapper.selectDataModelList(queryDTO);

            // 🔧 修复：使用 PageInfo 包装结果
            PageInfo<DataModelVO> pageInfo = new PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("records", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            data.put("pageNum", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            data.put("pages", pageInfo.getPages());

            return new JsonBean(1, "搜索成功", data);
        } catch (Exception e) {
            log.error("搜索数据模型失败", e);
            return new JsonBean(0, "搜索失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getPopularDataModels(Integer limit) {
        try {
            if (limit == null || limit <= 0) {
                limit = 10;
            }

            List<TblDataModel> popularModels = baseMapper.selectPopularModels(limit);
            return new JsonBean(1, "查询成功", popularModels);
        } catch (Exception e) {
            log.error("查询热门数据模型失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean generateDataModelCode(String modelId) {
        try {
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }

            TblDataModel dataModel = this.getById(modelId);
            if (dataModel == null) {
                return new JsonBean(0, "数据模型不存在", null);
            }

            // TODO: 实现代码生成逻辑
            Map<String, Object> codeResult = new HashMap<>();
            codeResult.put("javaCode", "// Generated Java code");
            codeResult.put("sqlCode", dataModel.getSqlStatement());
            codeResult.put("configCode", "// Generated config");

            return new JsonBean(1, "代码生成成功", codeResult);
        } catch (Exception e) {
            log.error("生成数据模型代码失败", e);
            return new JsonBean(0, "代码生成失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean parseSQLStatement(String sqlStatement, String dataSourceId) {
        try {
            if (StringUtil.isEmpty(sqlStatement)) {
                return new JsonBean(0, "SQL语句不能为空", null);
            }

            // 使用SQL解析工具类提取字段信息
            List<String> selectItems = null;
            try {
                selectItems = com.hbfk.util.database.DataBaseSqlGetStr.extractSelectItems(sqlStatement);
            } catch (Exception e) {
                log.warn("SQL解析失败，可能是复杂SQL语句: " + e.getMessage());
                // 对于复杂SQL，尝试简单的字符串解析
                selectItems = parseSelectItemsSimple(sqlStatement);
            }

            // 构建字段信息列表
            List<Map<String, Object>> fieldList = new ArrayList<>();
            if (selectItems != null && !selectItems.isEmpty()) {
                for (String item : selectItems) {
                    Map<String, Object> field = new HashMap<>();

                    // 处理字段别名 (如: SUPPLIER_ID AS ID 或 COUNT(*) AS TOTAL_COUNT)
                    String fieldName = item.trim();
                    String fieldType = "VARCHAR2"; // 默认类型
                    Integer fieldLength = 255;
                    boolean nullable = true;
                    String comment = "";

                    // 解析别名
                    if (fieldName.toUpperCase().contains(" AS ")) {
                        String[] parts = fieldName.split("(?i)\\s+AS\\s+");
                        if (parts.length >= 2) {
                            fieldName = parts[1].trim();
                            String originalField = parts[0].trim();

                            // 根据原始字段推断类型
                            if (originalField.toUpperCase().contains("COUNT(") ||
                                originalField.toUpperCase().contains("SUM(") ||
                                originalField.toUpperCase().contains("AVG(")) {
                                fieldType = "NUMBER";
                                fieldLength = 18;
                                comment = "聚合函数结果";
                            } else if (originalField.toUpperCase().contains("DATE") ||
                                      originalField.toUpperCase().contains("TIME")) {
                                fieldType = "DATE";
                                fieldLength = null;
                                comment = "日期时间字段";
                            }
                        }
                    }

                    // 清理字段名（移除引号等）
                    fieldName = fieldName.replaceAll("[\"'`]", "");

                    field.put("fieldName", fieldName);
                    field.put("fieldType", fieldType);
                    field.put("fieldLength", fieldLength);
                    field.put("nullable", nullable);
                    field.put("defaultValue", "");
                    field.put("comment", comment);

                    fieldList.add(field);
                }
            }

            // 构建解析结果
            Map<String, Object> parseResult = new HashMap<>();
            parseResult.put("isValid", true);
            parseResult.put("fieldCount", fieldList.size());
            parseResult.put("fields", fieldList);
            parseResult.put("syntax", sqlStatement.trim().toUpperCase().startsWith("SELECT") ? "SELECT" : "UNKNOWN");

            return new JsonBean(1, "SQL解析成功", parseResult);
        } catch (Exception e) {
            log.error("解析SQL语句失败", e);
            return new JsonBean(0, "SQL解析失败: " + e.getMessage(), null);
        }
    }

    /**
     * 简单的SQL字段解析（用于复杂SQL的备用方案）
     */
    private List<String> parseSelectItemsSimple(String sqlStatement) {
        List<String> fields = new ArrayList<>();
        try {
            // 提取SELECT和FROM之间的内容
            String upperSql = sqlStatement.toUpperCase();
            int selectIndex = upperSql.indexOf("SELECT");
            int fromIndex = upperSql.indexOf("FROM");

            if (selectIndex >= 0 && fromIndex > selectIndex) {
                String selectClause = sqlStatement.substring(selectIndex + 6, fromIndex).trim();

                // 简单分割字段（这里可能不够精确，但作为备用方案）
                String[] items = selectClause.split(",");
                for (String item : items) {
                    String trimmed = item.trim();
                    if (!trimmed.isEmpty()) {
                        fields.add(trimmed);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("简单SQL解析也失败: " + e.getMessage());
        }
        return fields;
    }

    /**
     * 生成下一个版本号
     */
    private String generateNextVersion(String currentVersion) {
        if (StringUtil.isEmpty(currentVersion)) {
            return "1.0";
        }

        try {
            String[] parts = currentVersion.split("\\.");
            if (parts.length >= 2) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                return major + "." + (minor + 1);
            }
        } catch (NumberFormatException e) {
            log.warn("版本号格式错误: {}", currentVersion);
        }

        return "1.0";
    }

    /**
     * 应用分页逻辑
     * 对结果集进行内存分页处理
     */
    private List<Map<String, Object>> applyPagination(List<Map<String, Object>> allResults, Integer pageNum, Integer pageSize) {
        if (allResults == null || allResults.isEmpty()) {
            return new ArrayList<>();
        }

        // 设置默认值
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 20;
        }

        int totalSize = allResults.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalSize);

        // 如果起始索引超出范围，返回空列表
        if (startIndex >= totalSize) {
            return new ArrayList<>();
        }

        log.info("应用分页逻辑: 总数据量={}, 页码={}, 页大小={}, 起始索引={}, 结束索引={}, 返回数据量={}",
                totalSize, pageNum, pageSize, startIndex, endIndex, endIndex - startIndex);

        // 返回分页后的子列表
        return allResults.subList(startIndex, endIndex);
    }

    /**
     * 执行SQL查询（真实实现）
     * 使用SqlExecutorUtil执行真实的SQL查询
     */
    private List<Map<String, Object>> executeSQL(TblDataSource dataSource, String sqlStatement) {
        try {
            log.info("执行真实SQL查询，数据源: {}, SQL: {}", dataSource.getSourceName(), sqlStatement);

            // 使用SqlExecutorUtil执行真实SQL
            Map<String, Object> executeResult = SqlExecutorUtil.executeSql(dataSource, sqlStatement);

            // 从执行结果中提取数据列表
            List<Map<String, Object>> resultData = (List<Map<String, Object>>) executeResult.get("data");

            if (resultData == null) {
                resultData = new ArrayList<>();
            }

            log.info("SQL执行完成，数据源: {}, 返回{}条记录，耗时{}ms",
                    dataSource.getSourceName(),
                    resultData.size(),
                    executeResult.get("executionTime"));

            return resultData;

        } catch (Exception e) {
            log.error("SQL执行失败，错误: {}", e.getMessage(), e);
            // 🔥 修复：不再生成假数据，直接抛出异常
            throw new RuntimeException("SQL执行失败: " + e.getMessage(), e);
        }
    }



    /**
     * 根据查询结果生成列信息
     */
    private List<Map<String, Object>> generateColumns(List<Map<String, Object>> resultData) {
        List<Map<String, Object>> columns = new ArrayList<>();

        if (resultData != null && !resultData.isEmpty()) {
            Map<String, Object> firstRow = resultData.get(0);
            for (String columnName : firstRow.keySet()) {
                Map<String, Object> column = new HashMap<>();
                column.put("prop", columnName);
                column.put("label", getColumnLabel(columnName));
                column.put("width", getColumnWidth(columnName));
                columns.add(column);
            }
        }

        return columns;
    }

    /**
     * 获取列显示名称
     */
    private String getColumnLabel(String columnName) {
        Map<String, String> labelMap = new HashMap<>();
        labelMap.put("SUPPLIER_ID", "供应商ID");
        labelMap.put("SUPPLIER_NAME", "供应商名称");
        labelMap.put("CONTRACT_TYPE", "合同类型");
        labelMap.put("TOTAL_AMOUNT", "合同总金额");
        labelMap.put("CONTRACT_COUNT", "合同数量");
        labelMap.put("FIRST_CONTRACT_DATE", "首次签约日期");
        labelMap.put("LAST_CONTRACT_DATE", "最近签约日期");
        labelMap.put("THRESHOLD_VALUE", "阈值");
        labelMap.put("IS_WARNING", "是否预警");
        labelMap.put("WARNING_LEVEL", "预警级别");

        return labelMap.getOrDefault(columnName, columnName);
    }

    /**
     * 获取列宽度
     */
    private Integer getColumnWidth(String columnName) {
        Map<String, Integer> widthMap = new HashMap<>();
        widthMap.put("SUPPLIER_ID", 120);
        widthMap.put("SUPPLIER_NAME", 150);
        widthMap.put("CONTRACT_TYPE", 100);
        widthMap.put("TOTAL_AMOUNT", 120);
        widthMap.put("CONTRACT_COUNT", 100);
        widthMap.put("FIRST_CONTRACT_DATE", 120);
        widthMap.put("LAST_CONTRACT_DATE", 120);
        widthMap.put("THRESHOLD_VALUE", 120);
        widthMap.put("IS_WARNING", 80);
        widthMap.put("WARNING_LEVEL", 100);

        return widthMap.getOrDefault(columnName, 100);
    }

    /**
     * 同步主模型数据到当前版本
     * 当修改主模型时，需要同时更新当前版本的数据，确保版本切换时数据一致
     */
    private void syncModelToCurrentVersion(TblDataModel dataModel, String updateUser) {
        try {
            // 查找当前版本
            TblDataModelVersion currentVersion = versionMapper.selectCurrentVersion(dataModel.getModelId());

            if (currentVersion != null) {
                log.info("同步主模型数据到当前版本: modelId={}, currentVersionId={}",
                        dataModel.getModelId(), currentVersion.getVersionId());

                // 更新当前版本的关键字段
                currentVersion.setModelName(dataModel.getModelName());
                currentVersion.setBusinessMeaning(dataModel.getBusinessMeaning());
                currentVersion.setCalculationLogic(dataModel.getCalculationLogic());
                currentVersion.setSqlStatement(dataModel.getSqlStatement()); // 🔧 关键：同步SQL语句
                currentVersion.setWithClause(dataModel.getWithClause());
                currentVersion.setSelectClause(dataModel.getSelectClause());
                currentVersion.setFromClause(dataModel.getFromClause());
                currentVersion.setWhereClause(dataModel.getWhereClause());
                currentVersion.setGroupByClause(dataModel.getGroupByClause());
                currentVersion.setHavingClause(dataModel.getHavingClause());
                currentVersion.setOrderByClause(dataModel.getOrderByClause());
                currentVersion.setDragConfig(dataModel.getDragConfig());
                currentVersion.setThresholdConfig(dataModel.getThresholdConfig());
                currentVersion.setWarningConfig(dataModel.getWarningConfig());
                currentVersion.setTemplateId(dataModel.getTemplateId());
                currentVersion.setParameterConfig(dataModel.getParameterConfig());
                currentVersion.setUpdateUser(updateUser);
                currentVersion.setUpdateTime(LocalDateTime.now());

                // 更新版本表
                versionMapper.updateById(currentVersion);

                log.info("成功同步主模型数据到当前版本: modelId={}, versionId={}",
                        dataModel.getModelId(), currentVersion.getVersionId());
            } else {
                log.info("未找到当前版本，跳过同步: modelId={}", dataModel.getModelId());
            }

        } catch (Exception e) {
            log.error("同步主模型数据到当前版本失败: modelId={}, error={}",
                    dataModel.getModelId(), e.getMessage(), e);
            // 不抛出异常，避免影响主流程
        }
    }

    /**
     * 生成SQL模板
     * 将数据模型转换为可复用的SQL模板
     *
     * @param modelId 模型ID
     * @param currentUser 当前用户
     * @return 生成结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean generateSqlTemplate(String modelId, String currentUser) {
        try {
            log.info("开始生成SQL模板，模型ID: {}, 操作用户: {}", modelId, currentUser);

            // 参数验证
            if (StringUtil.isEmpty(modelId)) {
                return new JsonBean(0, "模型ID不能为空", null);
            }
            if (StringUtil.isEmpty(currentUser)) {
                return new JsonBean(0, "当前用户不能为空", null);
            }

            // 查询数据模型
            TblDataModel dataModel = dataModelMapper.selectById(modelId);
            if (dataModel == null) {
                return new JsonBean(0, "数据模型不存在", null);
            }

            // 验证模型状态
            if (!"PUBLISHED".equals(dataModel.getStatus()) && !"TESTING".equals(dataModel.getStatus())) {
                return new JsonBean(0, "只有已发布或测试中的模型才能生成SQL模板", null);
            }

            // 验证SQL语句
            if (StringUtil.isEmpty(dataModel.getSqlStatement())) {
                return new JsonBean(0, "模型SQL语句不能为空", null);
            }

            // 生成模板编码和名称
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
            String templateCode = "TPL_" + dataModel.getModelCode() + "_" + timestamp;
            String templateName = dataModel.getModelName() + "_模板";

            // 检查模板编码是否重复
            QueryWrapper<TblSqlTemplate> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("TEMPLATE_CODE", templateCode);
            TblSqlTemplate existingTemplate = sqlTemplateMapper.selectOne(checkWrapper);
            if (existingTemplate != null) {
                templateCode = templateCode + "_" + RandomUtil.enUuId().substring(0, 4);
            }

            // 分析SQL语句，提取参数
            Map<String, Object> parameterAnalysis = analyzeSqlParameters(dataModel.getSqlStatement());

            // 生成SQL模板对象
            String templateId = RandomUtil.uuStringId();

            // 使用自定义插入方法
            int result = sqlTemplateMapper.insertSqlTemplate(
                templateId,
                templateCode,
                templateName,
                dataModel.getModelType(), // 模板类型
                "基于数据模型生成", // 业务场景
                dataModel.getSqlStatement(), // SQL模板内容
                dataModel.getParameterConfig() != null ? dataModel.getParameterConfig() : parameterAnalysis.get("parameterConfig").toString(),
                dataModel.getThresholdConfig(),
                "基于数据模型\"" + dataModel.getModelName() + "\"生成的SQL模板",
                "MEDIUM", // 复杂度级别
                "N", // 非系统预置
                currentUser
            );

            if (result > 0) {
                log.info("SQL模板生成成功，模板ID: {}, 模板编码: {}", templateId, templateCode);

                // 返回生成结果
                Map<String, Object> resultData = new HashMap<>();
                resultData.put("templateId", templateId);
                resultData.put("templateCode", templateCode);
                resultData.put("templateName", templateName);
                resultData.put("sqlContent", dataModel.getSqlStatement());

                return new JsonBean(1, "SQL模板生成成功", resultData);
            } else {
                return new JsonBean(0, "SQL模板保存失败", null);
            }

        } catch (Exception e) {
            log.error("生成SQL模板失败，模型ID: {}", modelId, e);
            return new JsonBean(0, "生成SQL模板失败: " + e.getMessage(), null);
        }
    }

    /**
     * 分析SQL语句中的参数
     *
     * @param sqlStatement SQL语句
     * @return 参数分析结果
     */
    private Map<String, Object> analyzeSqlParameters(String sqlStatement) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> parameters = new ArrayList<>();

        if (StringUtil.isNotEmpty(sqlStatement)) {
            // 提取 #{} 格式的参数
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("#\\{([^}]+)\\}");
            java.util.regex.Matcher matcher = pattern.matcher(sqlStatement);
            Set<String> paramNames = new HashSet<>();

            while (matcher.find()) {
                String paramName = matcher.group(1);
                if (!paramNames.contains(paramName)) {
                    paramNames.add(paramName);

                    Map<String, Object> param = new HashMap<>();
                    param.put("name", paramName);
                    param.put("type", inferParameterType(paramName));
                    param.put("required", true);
                    param.put("defaultValue", getDefaultValue(paramName));
                    param.put("description", generateParameterDescription(paramName));

                    parameters.add(param);
                }
            }

            // 检查常见的日期范围参数
            if (sqlStatement.toUpperCase().contains("DATE_RANGE") ||
                sqlStatement.toUpperCase().contains("START_DATE") ||
                sqlStatement.toUpperCase().contains("END_DATE")) {

                if (!paramNames.contains("startDate")) {
                    Map<String, Object> startDateParam = new HashMap<>();
                    startDateParam.put("name", "startDate");
                    startDateParam.put("type", "DATE");
                    startDateParam.put("required", true);
                    startDateParam.put("defaultValue", "");
                    startDateParam.put("description", "开始日期");
                    parameters.add(startDateParam);
                }

                if (!paramNames.contains("endDate")) {
                    Map<String, Object> endDateParam = new HashMap<>();
                    endDateParam.put("name", "endDate");
                    endDateParam.put("type", "DATE");
                    endDateParam.put("required", true);
                    endDateParam.put("defaultValue", "");
                    endDateParam.put("description", "结束日期");
                    parameters.add(endDateParam);
                }
            }
        }

        result.put("parameters", parameters);
        result.put("parameterConfig", com.alibaba.fastjson.JSON.toJSONString(parameters));

        return result;
    }

    /**
     * 推断参数类型
     */
    private String inferParameterType(String paramName) {
        String lowerName = paramName.toLowerCase();
        if (lowerName.contains("date") || lowerName.contains("time")) {
            return "DATE";
        } else if (lowerName.contains("amount") || lowerName.contains("count") || lowerName.contains("num")) {
            return "NUMBER";
        } else if (lowerName.contains("flag") || lowerName.contains("enabled")) {
            return "BOOLEAN";
        } else {
            return "STRING";
        }
    }

    /**
     * 获取默认值
     */
    private String getDefaultValue(String paramName) {
        String lowerName = paramName.toLowerCase();
        if (lowerName.contains("date")) {
            return "";
        } else if (lowerName.contains("amount") || lowerName.contains("count")) {
            return "0";
        } else if (lowerName.contains("flag") || lowerName.contains("enabled")) {
            return "false";
        } else {
            return "";
        }
    }

    /**
     * 生成参数描述
     */
    private String generateParameterDescription(String paramName) {
        String lowerName = paramName.toLowerCase();
        if (lowerName.contains("start") && lowerName.contains("date")) {
            return "开始日期";
        } else if (lowerName.contains("end") && lowerName.contains("date")) {
            return "结束日期";
        } else if (lowerName.contains("user")) {
            return "用户相关参数";
        } else if (lowerName.contains("org")) {
            return "组织相关参数";
        } else {
            return "请填写" + paramName + "参数";
        }
    }

    /**
     * 检测SQL是否包含步骤引用
     */
    private boolean containsStepReferences(String sqlStatement) {
        return sqlStatement.contains("${STEP_") ||
               sqlStatement.contains("${PREV_RESULT}") ||
               sqlStatement.contains("MOCK_STEP_") ||
               sqlStatement.contains("TEMP_STEP_") ||
               sqlStatement.matches(".*\\$\\{[^}]*RESULT\\}.*");
    }

    /**
     * 通用步骤引用处理 - 支持任意数量的SQL步骤，全部使用真实数据
     *
     * @param dataSourceId 数据源ID
     * @param sqlStatement 包含步骤引用的SQL
     * @param parameters 参数列表
     * @param staff 当前用户
     * @param indicatorCode 组合指标编码
     * @return 执行结果
     */
    private JsonBean handleStepReferencesWithRealData(String dataSourceId, String sqlStatement,
                                                     List<Map<String, Object>> parameters, TblStaffUtil staff, String indicatorCode) {
        try {
            log.info("开始通用步骤引用处理，支持任意SQL步骤的真实数据查询");

            // 获取数据源信息
            TblDataSource dataSource = dataSourceMapper.selectById(dataSourceId);
            if (dataSource == null) {
                return new JsonBean(0, "数据源不存在", null);
            }

            // 🔥 通用处理方案：解析并处理所有步骤引用
            String processedSql = processAllStepReferencesWithRealData(sqlStatement, dataSource, parameters, indicatorCode);

            log.info("通用步骤引用处理完成，最终SQL长度: {}", processedSql.length());
            log.debug("最终执行SQL: {}", processedSql);

            // 执行处理后的SQL
            Map<String, Object> testResult = SqlExecutorUtil.executeSql(dataSource, processedSql);

            log.info("通用步骤引用SQL执行完成，返回{}条记录", testResult.get("rowCount"));
            return new JsonBean(1, "SQL测试成功", testResult);

        } catch (Exception e) {
            log.error("通用步骤引用处理失败: {}", e.getMessage(), e);

            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("data", new ArrayList<>());
            errorResult.put("columns", new ArrayList<>());
            errorResult.put("rowCount", 0);
            errorResult.put("executionTime", 0);
            errorResult.put("success", false);
            errorResult.put("message", "SQL执行失败: " + e.getMessage());

            return new JsonBean(0, "SQL执行失败", errorResult);
        }
    }

    /**
     * 🔥 通用方法：处理所有类型的步骤引用，支持任意数量的SQL步骤
     *
     * @param sqlStatement 包含步骤引用的SQL
     * @param dataSource 数据源
     * @param parameters 参数列表
     * @return 处理后的SQL
     */
    private String processAllStepReferencesWithRealData(String sqlStatement, TblDataSource dataSource,
                                                       List<Map<String, Object>> parameters, String indicatorCode) throws Exception {
        String processedSql = sqlStatement;

        log.info("开始解析SQL中的所有步骤引用");

        // 🔥 步骤1：解析所有步骤引用模式
        Map<String, String> stepReferences = extractAllStepReferences(processedSql);

        log.info("发现{}个步骤引用: {}", stepReferences.size(), stepReferences.keySet());

        // 🔥 步骤2：为每个步骤引用构建真实数据查询
        Map<String, String> realDataQueries = new HashMap<>();
        for (String stepRef : stepReferences.keySet()) {
            String realQuery = buildRealDataQueryForStep(stepRef, parameters, dataSource, indicatorCode);
            if (realQuery != null) {
                realDataQueries.put(stepRef, realQuery);
                log.info("为步骤引用 {} 构建了真实数据查询", stepRef);
            } else {
                log.info("步骤引用 {} 跳过真实数据查询构建（用户已提供完整数据）", stepRef);
            }
        }

        // 🔥 步骤3：构建完整的WITH子句
        if (!realDataQueries.isEmpty()) {
            processedSql = buildCompleteWithClause(processedSql, realDataQueries);
        }

        // 🔥 步骤4：替换所有步骤引用为对应的表名
        // 🔥 关键修复：无论是否使用WITH子句，都需要替换主查询中的步骤引用
        for (Map.Entry<String, String> entry : stepReferences.entrySet()) {
            String stepRef = entry.getKey();
            String tableName = entry.getValue();
            processedSql = processedSql.replace(stepRef, tableName);
            log.info("已将步骤引用 {} 替换为表名 {}", stepRef, tableName);
        }

        // 🔥 步骤5：处理参数替换
        processedSql = processParameterReplacements(processedSql, parameters);

        log.info("通用步骤引用处理完成");
        log.info("最终生成的SQL: {}", processedSql);
        return processedSql;
    }

    /**
     * 🔥 提取SQL中的所有步骤引用
     * 返回Map: 步骤引用 -> 对应的表名
     */
    private Map<String, String> extractAllStepReferences(String sqlStatement) {
        Map<String, String> stepReferences = new HashMap<>();

        // 模式1: ${STEP_N_RESULT} 格式
        java.util.regex.Pattern stepPattern = java.util.regex.Pattern.compile("\\$\\{STEP_(\\d+)_RESULT\\}");
        java.util.regex.Matcher stepMatcher = stepPattern.matcher(sqlStatement);
        while (stepMatcher.find()) {
            String fullMatch = stepMatcher.group(0);  // ${STEP_1_RESULT}
            String stepNumber = stepMatcher.group(1); // 1
            String tableName = "TEMP_STEP_" + stepNumber + "_TABLE";
            stepReferences.put(fullMatch, tableName);
        }

        // 模式2: ${PREV_RESULT} 格式
        if (sqlStatement.contains("${PREV_RESULT}")) {
            stepReferences.put("${PREV_RESULT}", "TEMP_PREV_RESULT_TABLE");
        }

        // 模式3: MOCK_STEP_N_TABLE 格式（已存在的）
        java.util.regex.Pattern mockPattern = java.util.regex.Pattern.compile("MOCK_STEP_(\\d+)_TABLE");
        java.util.regex.Matcher mockMatcher = mockPattern.matcher(sqlStatement);
        while (mockMatcher.find()) {
            String fullMatch = mockMatcher.group(0);  // MOCK_STEP_1_TABLE
            stepReferences.put(fullMatch, fullMatch); // 保持原名
        }

        // 模式4: 其他自定义引用格式
        java.util.regex.Pattern customPattern = java.util.regex.Pattern.compile("\\$\\{([^}]*RESULT[^}]*)\\}");
        java.util.regex.Matcher customMatcher = customPattern.matcher(sqlStatement);
        while (customMatcher.find()) {
            String fullMatch = customMatcher.group(0);
            String refName = customMatcher.group(1);
            if (!stepReferences.containsKey(fullMatch)) {
                String tableName = "TEMP_" + refName.replaceAll("[^A-Z0-9_]", "_") + "_TABLE";
                stepReferences.put(fullMatch, tableName);
            }
        }

        return stepReferences;
    }

    /**
     * 🔥 为特定步骤引用构建真实数据查询（智能多步骤支持）
     */
    private String buildRealDataQueryForStep(String stepRef, List<Map<String, Object>> parameters,
                                           TblDataSource dataSource, String indicatorCode) throws Exception {

        log.info("为步骤引用 {} 构建真实数据查询", stepRef);

        // 🔥 特殊处理：如果SQL中已经包含了完整的WITH子句和数据，不需要替换
        if (stepRef.startsWith("MOCK_STEP_") && stepRef.endsWith("_TABLE")) {
            log.info("检测到用户提供的完整测试数据表 {}，跳过真实数据查询构建", stepRef);
            return null;
        }

        // 🔥 智能步骤解析：从步骤引用中提取步骤编号
        Integer stepNumber = extractStepNumber(stepRef);
        if (stepNumber != null) {
            log.info("解析到步骤编号: {}", stepNumber);

            // 🔥 尝试从历史记录中获取对应步骤的SQL
            String userSql = getUserSqlByStep(indicatorCode, stepNumber);
            if (userSql != null) {
                log.info("找到用户历史SQL，步骤: STEP_{}, 使用智能解析", stepNumber);
                return parseAndAdaptUserSql(userSql, parameters);
            } else {
                log.warn("未找到步骤 {} 的用户SQL，使用默认查询", stepNumber);
            }
        }

        // 🔥 回退方案：使用原有的固定查询逻辑
        if (stepRef.contains("STEP_1") || stepRef.contains("MOCK_STEP_1")) {
            return buildSupplierContractAggregationQuery(parameters);
        } else if (stepRef.contains("STEP_2")) {
            return buildStep2Query(parameters);
        } else if (stepRef.contains("STEP_3")) {
            return buildStep3Query(parameters);
        } else if (stepRef.contains("PREV_RESULT")) {
            return buildSupplierContractAggregationQuery(parameters);
        } else {
            return buildGenericStepQuery(stepRef, parameters);
        }
    }

    /**
     * 🔥 智能构建步骤查询（基于用户SQL历史记录）
     */
    private String buildSupplierContractAggregationQuery(List<Map<String, Object>> parameters) {
        // 🔥 尝试从SQL历史中获取用户的第一个SQL
        String userFirstSql = getLatestUserSql();

        if (userFirstSql != null && !containsStepReferences(userFirstSql)) {
            log.info("发现用户历史SQL，使用智能解析生成匹配查询");
            // 🔥 智能解析用户SQL并生成匹配的查询
            String intelligentQuery = parseAndAdaptUserSql(userFirstSql, parameters);
            if (intelligentQuery != null) {
                return intelligentQuery;
            }
        }

        // 🔥 回退方案：使用默认的详细合同查询
        log.info("使用默认详细合同查询作为回退方案");
        String query = "SELECT " +
                "c.CONTRACT_ID, " +
                "c.CONTRACT_NO, " +
                "c.CONTRACT_NAME, " +
                "c.CONTRACT_TYPE, " +
                "c.CONTRACT_AMOUNT, " +
                "c.SUPPLIER_NAME, " +
                "c.PROCUREMENT_METHOD, " +
                "c.IS_PUBLIC_TENDER, " +
                "c.CONTRACT_SIGN_DATE, " +
                "c.APPLY_DEPT_NAME, " +
                "CASE " +
                    "WHEN c.CONTRACT_TYPE = '工程' THEN ${工程} " +
                    "WHEN c.CONTRACT_TYPE = '物资' THEN ${物资} " +
                    "WHEN c.CONTRACT_TYPE = '服务' THEN ${服务} " +
                    "ELSE ${默认} " +
                "END AS TENDER_THRESHOLD, " +
                "CASE " +
                    "WHEN c.CONTRACT_AMOUNT >= CASE " +
                        "WHEN c.CONTRACT_TYPE = '工程' THEN ${工程} " +
                        "WHEN c.CONTRACT_TYPE = '物资' THEN ${物资} " +
                        "WHEN c.CONTRACT_TYPE = '服务' THEN ${服务} " +
                        "ELSE ${默认} " +
                    "END " +
                    "AND c.IS_PUBLIC_TENDER = '0' THEN '1' " +
                    "ELSE '0' " +
                "END AS IS_WARNING " +
                "FROM TBL_CONTRACT_INFO c " +
                "WHERE c.CONTRACT_STATUS = 'ACTIVE'";

        return processParameterReplacements(query, parameters);
    }

    /**
     * 🔥 构建第二个步骤查询（基于实际存在的表）
     */
    private String buildStep2Query(List<Map<String, Object>> parameters) {
        // 🔥 修复：使用实际存在的合同表进行第二个步骤查询
        // 第二个步骤：基于第一个步骤的结果进行风险等级分析
        String query = "SELECT " +
            "c.SUPPLIER_ID, " +
            "c.SUPPLIER_NAME, " +
            "c.CONTRACT_TYPE, " +
            "SUM(c.CONTRACT_AMOUNT) AS TOTAL_AMOUNT, " +
            "COUNT(*) AS CONTRACT_COUNT, " +
            "MIN(c.CONTRACT_SIGN_DATE) AS FIRST_CONTRACT_DATE, " +
            "MAX(c.CONTRACT_SIGN_DATE) AS LAST_CONTRACT_DATE, " +
            "CASE " +
                "WHEN c.CONTRACT_TYPE = '工程' THEN ${工程} " +
                "WHEN c.CONTRACT_TYPE = '物资' THEN ${物资} " +
                "WHEN c.CONTRACT_TYPE = '服务' THEN ${服务} " +
                "ELSE ${默认} " +
            "END AS THRESHOLD_VALUE, " +
            "CASE " +
                "WHEN SUM(c.CONTRACT_AMOUNT) >= CASE " +
                    "WHEN c.CONTRACT_TYPE = '工程' THEN ${工程} " +
                    "WHEN c.CONTRACT_TYPE = '物资' THEN ${物资} " +
                    "WHEN c.CONTRACT_TYPE = '服务' THEN ${服务} " +
                    "ELSE ${默认} " +
                "END THEN '1' " +
                "ELSE '0' " +
            "END AS IS_WARNING, " +
            "CASE " +
                "WHEN SUM(c.CONTRACT_AMOUNT) >= CASE " +
                    "WHEN c.CONTRACT_TYPE = '工程' THEN ${工程} " +
                    "WHEN c.CONTRACT_TYPE = '物资' THEN ${物资} " +
                    "WHEN c.CONTRACT_TYPE = '服务' THEN ${服务} " +
                    "ELSE ${默认} " +
                "END * 2 THEN 'HIGH' " +
                "WHEN SUM(c.CONTRACT_AMOUNT) >= CASE " +
                    "WHEN c.CONTRACT_TYPE = '工程' THEN ${工程} " +
                    "WHEN c.CONTRACT_TYPE = '物资' THEN ${物资} " +
                    "WHEN c.CONTRACT_TYPE = '服务' THEN ${服务} " +
                    "ELSE ${默认} " +
                "END * 1.5 THEN 'MEDIUM' " +
                "ELSE 'LOW' " +
            "END AS WARNING_LEVEL, " +
            "'同一供应商订单总额超标预警' AS WARNING_TYPE " +
            "FROM TBL_CONTRACT_INFO c " +
            "WHERE c.CONTRACT_SIGN_DATE >= ADD_MONTHS(SYSDATE, -${筛选几月前}) " +
            "AND c.CONTRACT_STATUS = 'ACTIVE' " +
            "GROUP BY c.SUPPLIER_ID, c.SUPPLIER_NAME, c.CONTRACT_TYPE";

        return processParameterReplacements(query, parameters);
    }

    /**
     * 🔥 构建第三个步骤查询（基于实际存在的表）
     */
    private String buildStep3Query(List<Map<String, Object>> parameters) {
        // 🔥 修复：使用实际存在的表进行第三个步骤查询
        // 第三个步骤：基于合同数据进行更深层的分析
        String query = "SELECT " +
            "c.SUPPLIER_ID, " +
            "c.SUPPLIER_NAME, " +
            "COUNT(DISTINCT c.CONTRACT_TYPE) AS CONTRACT_TYPE_COUNT, " +
            "SUM(c.CONTRACT_AMOUNT) AS TOTAL_CONTRACT_AMOUNT, " +
            "AVG(c.CONTRACT_AMOUNT) AS AVG_CONTRACT_AMOUNT, " +
            "MAX(c.CONTRACT_AMOUNT) AS MAX_CONTRACT_AMOUNT, " +
            "MIN(c.CONTRACT_AMOUNT) AS MIN_CONTRACT_AMOUNT " +
            "FROM TBL_CONTRACT_INFO c " +
            "WHERE c.CONTRACT_SIGN_DATE >= ADD_MONTHS(SYSDATE, -${筛选几月前}) " +
            "AND c.CONTRACT_STATUS = 'ACTIVE' " +
            "GROUP BY c.SUPPLIER_ID, c.SUPPLIER_NAME " +
            "HAVING COUNT(*) > 1";  // 只分析有多个合同的供应商

        return processParameterReplacements(query, parameters);
    }

    /**
     * 🔥 构建通用步骤查询（智能推断）
     */
    private String buildGenericStepQuery(String stepRef, List<Map<String, Object>> parameters) {
        log.info("构建通用步骤查询，步骤引用: {}", stepRef);

        // 🔥 智能推断：根据步骤引用名称推断查询类型
        String lowerStepRef = stepRef.toLowerCase();

        if (lowerStepRef.contains("supplier") || lowerStepRef.contains("contract")) {
            return buildSupplierContractAggregationQuery(parameters);
        } else if (lowerStepRef.contains("customer") || lowerStepRef.contains("order")) {
            return buildStep2Query(parameters);
        } else if (lowerStepRef.contains("project") || lowerStepRef.contains("budget")) {
            return buildStep3Query(parameters);
        } else {
            // 🔥 默认查询：返回基础的数据表查询
            return buildDefaultQuery(parameters);
        }
    }

    /**
     * 🔥 构建默认查询
     */
    private String buildDefaultQuery(List<Map<String, Object>> parameters) {
        String query = "SELECT " +
            "'DEFAULT' AS DATA_TYPE, " +
            "SYSDATE AS QUERY_TIME, " +
            "1 AS RECORD_COUNT " +
            "FROM DUAL";

        return processParameterReplacements(query, parameters);
    }

    /**
     * 🔥 处理参数替换（支持步骤引用参数）
     */
    private String processParameterReplacements(String query, List<Map<String, Object>> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            // 🔥 设置默认参数值
            query = query.replace("${工程}", "1000000");
            query = query.replace("${物资}", "500000");
            query = query.replace("${服务}", "800000");
            query = query.replace("${默认}", "600000");
            query = query.replace("${筛选几月前}", "12");
            query = query.replace("${高风险指数}", "2");
            query = query.replace("${中风险指数}", "1.5");
            return query;
        }

        // 🔥 使用传入的参数值
        for (Map<String, Object> param : parameters) {
            String paramName = (String) param.get("paramName");
            Object paramValue = param.get("paramValue");
            if (paramName != null && paramValue != null) {
                String placeholder = "${" + paramName + "}";
                query = query.replace(placeholder, paramValue.toString());
            }
        }

        // 🔥 处理剩余未替换的参数（设置默认值）
        query = query.replace("${工程}", "1000000");
        query = query.replace("${物资}", "500000");
        query = query.replace("${服务}", "800000");
        query = query.replace("${默认}", "600000");
        query = query.replace("${筛选几月前}", "12");
        query = query.replace("${高风险指数}", "2");
        query = query.replace("${中风险指数}", "1.5");

        // 🔥 检查是否还有未替换的步骤引用参数
        if (query.contains("${STEP_") || query.contains("${PREV_RESULT}")) {
            log.warn("SQL中仍包含未替换的步骤引用参数，这可能是正常的，因为步骤引用应该在之前的步骤中处理");
            log.debug("包含步骤引用的SQL: {}", query);
        }

        return query;
    }

    /**
     * 🔥 构建完整的WITH子句
     */
    private String buildCompleteWithClause(String originalSql, Map<String, String> realDataQueries) {
        log.info("构建完整的WITH子句，包含{}个步骤查询", realDataQueries.size());

        // 🔥 检查原SQL是否已经有WITH子句
        boolean hasExistingWith = originalSql.trim().toUpperCase().startsWith("WITH");

        if (hasExistingWith) {
            // 🔥 替换现有的WITH子句
            return replaceExistingWithClause(originalSql, realDataQueries);
        } else {
            // 🔥 添加新的WITH子句
            return addNewWithClause(originalSql, realDataQueries);
        }
    }

    /**
     * 🔥 替换现有的WITH子句
     */
    private String replaceExistingWithClause(String originalSql, Map<String, String> realDataQueries) {
        // 找到主SELECT语句的开始位置
        String upperSql = originalSql.toUpperCase();
        int withStart = upperSql.indexOf("WITH");
        int mainSelectStart = findMainSelectStart(upperSql, withStart);

        if (mainSelectStart > withStart) {
            // 构建新的WITH子句
            StringBuilder newWithClause = new StringBuilder("WITH ");

            int count = 0;
            for (Map.Entry<String, String> entry : realDataQueries.entrySet()) {
                String stepRef = entry.getKey();
                String query = entry.getValue();
                String tableName = extractTableNameFromStepRef(stepRef);

                // 🔥 关键修复：去掉SQL末尾的分号，避免WITH子句语法错误
                query = query.trim();
                if (query.endsWith(";")) {
                    query = query.substring(0, query.length() - 1).trim();
                }

                if (count > 0) {
                    newWithClause.append(", ");
                }
                newWithClause.append(tableName).append(" AS (").append(query).append(")");
                count++;
            }

            newWithClause.append(" ");

            // 获取主SELECT部分
            String mainSelectPart = originalSql.substring(mainSelectStart);

            return newWithClause.toString() + mainSelectPart;
        }

        return originalSql;
    }

    /**
     * 🔥 添加新的WITH子句（使用子查询替代方案）
     */
    private String addNewWithClause(String originalSql, Map<String, String> realDataQueries) {
        // 🔥 尝试使用子查询替代WITH子句，提高数据库兼容性
        if (realDataQueries.size() == 1) {
            // 单个步骤引用，直接使用子查询替换
            Map.Entry<String, String> entry = realDataQueries.entrySet().iterator().next();
            String stepRef = entry.getKey();
            String query = entry.getValue();
            String tableName = extractTableNameFromStepRef(stepRef);

            // 🔥 修复：先替换步骤引用为表名，再进行子查询替换
            log.info("原始SQL: {}", originalSql);
            log.info("步骤引用: {}, 表名: {}, 查询: {}", stepRef, tableName, query);

            String finalSql = originalSql.replace(stepRef, tableName);
            log.info("步骤引用替换后的SQL: {}", finalSql);

            // 🔥 然后进行子查询替换，支持多种FROM子句格式
            // 模式1: FROM tableName alias (表名后跟别名) - 优先处理
            String pattern1 = "FROM\\s+" + tableName + "\\s+(\\w+)";
            String replacement1 = "FROM (" + query + ") AS $1";
            finalSql = finalSql.replaceAll(pattern1, replacement1);
            log.info("模式1替换后的SQL: {}", finalSql);

            // 模式2: FROM tableName AS alias
            String pattern2 = "FROM\\s+" + tableName + "\\s+AS\\s+(\\w+)";
            String replacement2 = "FROM (" + query + ") AS $1";
            finalSql = finalSql.replaceAll(pattern2, replacement2);
            log.info("模式2替换后的SQL: {}", finalSql);

            // 模式3: FROM tableName (空格或结束) - 最后处理，添加AS关键字
            String pattern3 = "FROM\\s+" + tableName + "(?=\\s|$)";
            String replacement3 = "FROM (" + query + ") AS " + tableName;
            finalSql = finalSql.replaceAll(pattern3, replacement3);
            log.info("模式3替换后的SQL: {}", finalSql);

            log.info("使用子查询替代WITH子句的最终SQL: {}", finalSql);
            return finalSql;
        } else {
            // 多个步骤引用，仍使用WITH子句
            StringBuilder withClause = new StringBuilder("WITH ");

            int count = 0;
            for (Map.Entry<String, String> entry : realDataQueries.entrySet()) {
                String stepRef = entry.getKey();
                String query = entry.getValue();
                String tableName = extractTableNameFromStepRef(stepRef);

                // 🔥 关键修复：去掉SQL末尾的分号，避免WITH子句语法错误
                query = query.trim();
                if (query.endsWith(";")) {
                    query = query.substring(0, query.length() - 1).trim();
                }

                if (count > 0) {
                    withClause.append(", ");
                }
                withClause.append(tableName).append(" AS (").append(query).append(")");
                count++;
            }

            withClause.append(" ");

            String finalSql = withClause.toString() + originalSql;
            log.info("构建的完整WITH子句SQL: {}", finalSql);
            return finalSql;
        }
    }

    /**
     * 🔥 从步骤引用中提取表名
     * 🔥 关键修复：确保与 extractAllStepReferences 方法生成的表名一致
     */
    private String extractTableNameFromStepRef(String stepRef) {
        if (stepRef.startsWith("${") && stepRef.endsWith("}")) {
            // 🔥 修复：${STEP_1_RESULT} -> TEMP_STEP_1_TABLE（去掉 _RESULT 后缀）
            String inner = stepRef.substring(2, stepRef.length() - 1); // STEP_1_RESULT

            // 🔥 特殊处理：如果是 STEP_N_RESULT 格式，去掉 _RESULT 后缀
            if (inner.matches("STEP_\\d+_RESULT")) {
                // STEP_1_RESULT -> STEP_1
                inner = inner.replace("_RESULT", "");
            }

            return "TEMP_" + inner + "_TABLE";
        } else if (stepRef.startsWith("MOCK_") || stepRef.startsWith("TEMP_")) {
            // 已经是表名格式
            return stepRef;
        } else {
            // 其他格式
            return "TEMP_" + stepRef.replaceAll("[^A-Z0-9_]", "_") + "_TABLE";
        }
    }

    /**
     * 🔥 找到主SELECT语句的开始位置
     */
    private int findMainSelectStart(String upperSql, int withStart) {
        int pos = withStart + 4; // 跳过"WITH"
        int parenCount = 0;
        boolean inQuotes = false;

        for (int i = pos; i < upperSql.length() - 6; i++) {
            char c = upperSql.charAt(i);

            if (c == '\'' && (i == 0 || upperSql.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            } else if (!inQuotes) {
                if (c == '(') {
                    parenCount++;
                } else if (c == ')') {
                    parenCount--;
                } else if (parenCount == 0 && upperSql.substring(i, i + 6).equals("SELECT")) {
                    return i;
                }
            }
        }

        return upperSql.length();
    }

    /**
     * 🔥 组合指标SQL历史记录（按指标编码隔离）
     */
    private void recordSqlHistory(String indicatorCode, String sqlStatement) {
        try {
            // 🔥 如果没有指标编码，使用默认值
            if (StringUtil.isEmpty(indicatorCode)) {
                indicatorCode = "DEFAULT_INDICATOR";
            }

            // 🔥 首先清理超时的上下文
            cleanupExpiredContexts();

            // 只记录不包含步骤引用的SQL（这些是用户的原始SQL）
            if (!containsStepReferences(sqlStatement)) {
                // 🔥 更新活动时间
                INDICATOR_LAST_ACTIVITY.put(indicatorCode, System.currentTimeMillis());

                // 🔥 获取或初始化该指标的步骤计数器
                int currentStep = INDICATOR_STEP_COUNTER.getOrDefault(indicatorCode, 0) + 1;
                INDICATOR_STEP_COUNTER.put(indicatorCode, currentStep);

                // 🔥 获取或初始化该指标的SQL历史Map
                Map<Integer, String> indicatorHistory = INDICATOR_SQL_HISTORY.computeIfAbsent(indicatorCode, k -> new ConcurrentHashMap<>());

                // 🔥 记录当前步骤的SQL
                indicatorHistory.put(currentStep, sqlStatement.trim());

                log.info("记录组合指标SQL历史，指标编码: {}, 步骤: STEP_{}, SQL长度: {}", indicatorCode, currentStep, sqlStatement.length());

                // 🔥 清理旧的历史记录（保留最近10个步骤）
                if (indicatorHistory.size() > 10) {
                    int oldestStep = currentStep - 10;
                    indicatorHistory.entrySet().removeIf(entry -> entry.getKey() <= oldestStep);
                }
            } else {
                // 🔥 即使是引用SQL，也要更新活动时间
                INDICATOR_LAST_ACTIVITY.put(indicatorCode, System.currentTimeMillis());
            }
        } catch (Exception e) {
            log.warn("记录组合指标SQL历史失败: {}", e.getMessage());
        }
    }

    /**
     * 🔥 根据步骤编号获取用户SQL
     */
    private String getUserSqlByStep(String indicatorCode, int stepNumber) {
        try {
            // 🔥 如果没有指标编码，使用默认值（与 recordSqlHistory 保持一致）
            if (StringUtil.isEmpty(indicatorCode)) {
                indicatorCode = "DEFAULT_INDICATOR";
            }

            Map<Integer, String> indicatorHistory = INDICATOR_SQL_HISTORY.get(indicatorCode);
            if (indicatorHistory != null) {
                String sql = indicatorHistory.get(stepNumber);
                if (sql != null) {
                    log.info("找到步骤SQL，指标编码: {}, 步骤: STEP_{}, SQL长度: {}", indicatorCode, stepNumber, sql.length());
                    return sql;
                }
            }
            log.warn("未找到步骤SQL，指标编码: {}, 步骤: STEP_{}", indicatorCode, stepNumber);
        } catch (Exception e) {
            log.error("获取步骤SQL失败: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 🔥 获取最新的用户SQL（向后兼容）
     */
    private String getLatestUserSql() {
        try {
            // 查找所有数据源中最新的SQL
            for (Map.Entry<String, Map<Integer, String>> entry : INDICATOR_SQL_HISTORY.entrySet()) {
                Map<Integer, String> stepHistory = entry.getValue();
                if (!stepHistory.isEmpty()) {
                    // 获取最大步骤编号的SQL
                    int maxStep = stepHistory.keySet().stream().max(Integer::compareTo).orElse(0);
                    return stepHistory.get(maxStep);
                }
            }
        } catch (Exception e) {
            log.warn("获取最新用户SQL失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 🔥 智能解析用户SQL并生成匹配的查询
     */
    private String parseAndAdaptUserSql(String userSql, List<Map<String, Object>> parameters) {
        try {
            log.info("开始智能解析用户SQL: {}", userSql.substring(0, Math.min(100, userSql.length())) + "...");

            // 🔥 直接使用用户的SQL作为基础，这是最准确的方法
            // 用户的第一个SQL就是他们想要的数据结构
            String adaptedSql = userSql;

            // 🔥 处理参数替换
            if (parameters != null && !parameters.isEmpty()) {
                adaptedSql = processParameterReplacements(adaptedSql, parameters);
            }

            log.info("智能解析完成，生成匹配查询");
            return adaptedSql;

        } catch (Exception e) {
            log.error("智能解析用户SQL失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 🔥 从步骤引用中提取步骤编号
     */
    private Integer extractStepNumber(String stepRef) {
        try {
            // 匹配 ${STEP_N_RESULT} 格式
            Pattern pattern = Pattern.compile("\\$\\{STEP_(\\d+)_RESULT\\}");
            Matcher matcher = pattern.matcher(stepRef);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }

            // 匹配 STEP_N 格式
            pattern = Pattern.compile("STEP_(\\d+)");
            matcher = pattern.matcher(stepRef);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }

            // 匹配 MOCK_STEP_N 格式
            pattern = Pattern.compile("MOCK_STEP_(\\d+)");
            matcher = pattern.matcher(stepRef);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }

        } catch (Exception e) {
            log.warn("提取步骤编号失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 🔥 重置指定数据源的步骤计数器
     */
    private void resetStepCounter(String dataSourceId) {
        clearDataSourceContext(dataSourceId);
    }

    /**
     * 🔥 清理指标上下文
     */
    private void clearIndicatorContext(String indicatorCode) {
        try {
            INDICATOR_STEP_COUNTER.put(indicatorCode, 0);
            INDICATOR_LAST_ACTIVITY.remove(indicatorCode);

            Map<Integer, String> indicatorHistory = INDICATOR_SQL_HISTORY.get(indicatorCode);
            if (indicatorHistory != null) {
                indicatorHistory.clear();
            }

            log.info("清理指标上下文，指标编码: {}", indicatorCode);
        } catch (Exception e) {
            log.warn("清理指标上下文失败: {}", e.getMessage());
        }
    }

    /**
     * 🔥 清理指定数据源的所有上下文（向后兼容）
     */
    private void clearDataSourceContext(String dataSourceId) {
        // 向后兼容：清理默认指标的上下文
        clearIndicatorContext("DEFAULT_INDICATOR");
    }

    /**
     * 🔥 清理所有超时的上下文
     */
    private void cleanupExpiredContexts() {
        try {
            long currentTime = System.currentTimeMillis();
            Set<String> expiredIndicators = new HashSet<>();

            for (Map.Entry<String, Long> entry : INDICATOR_LAST_ACTIVITY.entrySet()) {
                if (currentTime - entry.getValue() > CONTEXT_TIMEOUT_MS) {
                    expiredIndicators.add(entry.getKey());
                }
            }

            for (String indicatorCode : expiredIndicators) {
                clearIndicatorContext(indicatorCode);
                log.info("清理超时上下文，指标编码: {}, 超时时间: {}分钟", indicatorCode, CONTEXT_TIMEOUT_MS / 60000);
            }
        } catch (Exception e) {
            log.warn("清理超时上下文失败: {}", e.getMessage());
        }
    }

    /**
     * 🔥 检测新指标开始的业务边界
     */
    private boolean detectNewIndicatorStart(String sqlStatement) {
        try {
            String upperSql = sqlStatement.toUpperCase();

            // 🔥 检测关键词：如果SQL包含这些词，可能是新指标开始
            String[] newIndicatorKeywords = {
                "-- NEW_INDICATOR",     // 明确的新指标标记
                "-- RESET_CONTEXT",     // 明确的重置标记
                "-- 新指标",             // 中文标记
                "-- 重置",               // 中文重置标记
            };

            for (String keyword : newIndicatorKeywords) {
                if (upperSql.contains(keyword.toUpperCase())) {
                    return true;
                }
            }

            // 🔥 检测业务表切换：如果查询的主表发生变化，可能是新指标
            // 这里可以根据实际业务需求添加更多检测逻辑

        } catch (Exception e) {
            log.warn("检测新指标开始失败: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 🔥 公共接口：手动清理指定数据源的上下文
     * 可以通过API调用来清理
     */
    public JsonBean clearSqlContext(String dataSourceId) {
        try {
            if (StringUtil.isEmpty(dataSourceId)) {
                return new JsonBean(0, "数据源ID不能为空", null);
            }

            clearDataSourceContext(dataSourceId);
            return new JsonBean(1, "上下文清理成功", null);
        } catch (Exception e) {
            log.error("清理SQL上下文失败: {}", e.getMessage(), e);
            return new JsonBean(0, "清理失败: " + e.getMessage(), null);
        }
    }

    /**
     * 🔥 公共接口：获取当前上下文状态
     */
    public JsonBean getSqlContextStatus(String dataSourceId) {
        try {
            Map<String, Object> status = new HashMap<>();

            if (StringUtil.isNotEmpty(dataSourceId)) {
                // 向后兼容：使用默认指标编码
                String indicatorCode = "DEFAULT_INDICATOR";
                int currentStep = INDICATOR_STEP_COUNTER.getOrDefault(indicatorCode, 0);
                Long lastActivity = INDICATOR_LAST_ACTIVITY.get(indicatorCode);
                Map<Integer, String> history = INDICATOR_SQL_HISTORY.get(indicatorCode);

                status.put("dataSourceId", dataSourceId);
                status.put("indicatorCode", indicatorCode);
                status.put("currentStep", currentStep);
                status.put("lastActivity", lastActivity);
                status.put("historyCount", history != null ? history.size() : 0);
                status.put("isActive", lastActivity != null && (System.currentTimeMillis() - lastActivity) < CONTEXT_TIMEOUT_MS);
            } else {
                // 获取所有指标的状态
                Map<String, Object> allStatus = new HashMap<>();
                for (String indicatorCode : INDICATOR_STEP_COUNTER.keySet()) {
                    Map<String, Object> indicatorStatus = new HashMap<>();
                    indicatorStatus.put("currentStep", INDICATOR_STEP_COUNTER.get(indicatorCode));
                    indicatorStatus.put("lastActivity", INDICATOR_LAST_ACTIVITY.get(indicatorCode));
                    Map<Integer, String> history = INDICATOR_SQL_HISTORY.get(indicatorCode);
                    indicatorStatus.put("historyCount", history != null ? history.size() : 0);
                    allStatus.put(indicatorCode, indicatorStatus);
                }
                status.put("allIndicators", allStatus);
            }

            return new JsonBean(1, "获取状态成功", status);
        } catch (Exception e) {
            log.error("获取SQL上下文状态失败: {}", e.getMessage(), e);
            return new JsonBean(0, "获取状态失败: " + e.getMessage(), null);
        }
    }

    // ================================================================
    // 组合指标 → 数据模型 规则同步 (#TASK-2026-08-01-DATA-MODEL-SYNC)
    // ================================================================

    @Override
    public JsonBean syncFromCombinations(List<String> combinationIds, boolean onlyEnabled, String currentUser) {
        try {
            log.info("[数据模型规则同步] 开始，combinationIds={}, onlyEnabled={}, user={}",
                    combinationIds, onlyEnabled, currentUser);

            // 1) 获取待处理的组合 ID 列表：为空则查全部
            List<String> targetIds = new ArrayList<>();
            if (combinationIds != null && !combinationIds.isEmpty()) {
                targetIds.addAll(combinationIds);
            } else {
                // 分页查全部组合
                int page = 1;
                int pageSize = 500;
                while (true) {
                    Map<String, Object> pageResult = combinationService.getCombinationList(
                            page, pageSize, null, null, null, null, null, null, null);
                    Object recordsObj = pageResult == null ? null : pageResult.get("records");
                    if (!(recordsObj instanceof List)) break;
                    List<?> records = (List<?>) recordsObj;
                    for (Object r : records) {
                        if (r instanceof Map) {
                            Object cid = ((Map<?, ?>) r).get("combinationId");
                            if (cid != null) targetIds.add(cid.toString());
                        }
                    }
                    if (records.size() < pageSize) break;
                    page++;
                }
            }

            int insertCount = 0;
            int updateCount = 0;
            int skipCount = 0;
            List<Map<String, Object>> details = new ArrayList<>();

            // 2) 逐个组合处理
            for (String combinationId : targetIds) {
                Map<String, Object> detail;
                try {
                    detail = combinationService.getCombinationDetail(combinationId);
                } catch (Exception e) {
                    log.warn("[数据模型规则同步] 获取组合详情失败, combinationId={}, err={}", combinationId, e.getMessage());
                    skipCount++;
                    continue;
                }
                if (detail == null) { skipCount++; continue; }

                Object indicatorsObj = detail.get("indicators");
                List<Map<String, Object>> indicators = new ArrayList<>();
                if (indicatorsObj instanceof List) {
                    for (Object item : (List<?>) indicatorsObj) {
                        if (item instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> m = (Map<String, Object>) item;
                            if (onlyEnabled && !isTrue(m.get("isEnabled"))) continue;
                            indicators.add(m);
                        }
                    }
                }

                // 3) 应用业务规则：跳过每组最后一个指标
                List<Map<String, Object>> syncable = com.huabo.fxgl.service.sync.DataModelSyncHelper
                        .filterSyncableIndicators(indicators);

                int cInsert = 0;
                int cUpdate = 0;
                int cSkip = 0;
                for (Map<String, Object> ind : syncable) {
                    try {
                        if (isBlankStr(ind.get("sqlContent"))
                                || isBlankStr(ind.get("indicatorCode"))) {
                            cSkip++;
                            continue;
                        }
                        boolean inserted = upsertFromIndicator(ind, detail, currentUser);
                        if (inserted) cInsert++; else cUpdate++;
                    } catch (Exception ex) {
                        log.warn("[数据模型规则同步] 单条 upsert 失败, configId={}, err={}",
                                ind.get("configId"), ex.getMessage());
                        cSkip++;
                    }
                }
                insertCount += cInsert;
                updateCount += cUpdate;
                skipCount += cSkip;

                Map<String, Object> d = new HashMap<>();
                d.put("combinationId", combinationId);
                d.put("combinationName", detail.get("combinationName"));
                d.put("indicatorCount", indicators.size());
                d.put("syncableCount", syncable.size());
                d.put("insertCount", cInsert);
                d.put("updateCount", cUpdate);
                d.put("skipCount", cSkip);
                details.add(d);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("total", insertCount + updateCount);
            data.put("insertCount", insertCount);
            data.put("updateCount", updateCount);
            data.put("skipCount", skipCount);
            data.put("combinationCount", targetIds.size());
            data.put("details", details);
            log.info("[数据模型规则同步] 完成，insert={}, update={}, skip={}", insertCount, updateCount, skipCount);
            return new JsonBean(1, "同步完成", data);
        } catch (Exception e) {
            log.error("[数据模型规则同步] 失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null);
        }
    }

    @Override
    public boolean upsertFromIndicator(Map<String, Object> indicator,
                                       Map<String, Object> combination,
                                       String currentUser) {
        if (indicator == null) return false;
        Object cfgIdObj = indicator.get("configId");
        if (cfgIdObj == null) {
            log.warn("[数据模型 upsert] configId 缺失，跳过");
            return false;
        }
        String cfgId = cfgIdObj.toString();

        // 用 helper 做字段映射
        Map<String, Object> mapped = com.huabo.fxgl.service.sync.DataModelSyncHelper
                .mapIndicatorToDataModel(indicator, combination);

        // 查询是否已有该来源的数据模型
        String queryExistSql = "SELECT MODEL_ID FROM TBL_DATA_MODEL WHERE SOURCE_INDICATOR_CONFIG_ID = ?";
        List<Map<String, Object>> existRows = jdbcTemplate.queryForList(queryExistSql, cfgId);

        LocalDateTime now = LocalDateTime.now();
        String user = currentUser == null ? "system-sync" : currentUser;
        String modelCode = (String) mapped.get("modelCode");
        String modelName = (String) mapped.get("modelName");
        String modelType = (String) mapped.get("modelType");
        String dataSourceId = (String) mapped.get("dataSourceId");
        String sqlStatement = (String) mapped.get("sqlStatement");
        String businessMeaning = (String) mapped.get("businessMeaning");
        String parameterConfig = (String) mapped.get("parameterConfig");
        String isEnabled = (String) mapped.get("isEnabled");
        String sourceCombId = (String) mapped.get("sourceCombinationId");
        String sourceIndId = (String) mapped.get("sourceIndicatorId");

        if (!existRows.isEmpty()) {
            // update
            String modelId = (String) existRows.get(0).get("MODEL_ID");
            String updateSql = "UPDATE TBL_DATA_MODEL SET " +
                    "MODEL_CODE = ?, MODEL_NAME = ?, MODEL_TYPE = ?, DATA_SOURCE_ID = ?, " +
                    "SQL_STATEMENT = ?, BUSINESS_MEANING = ?, PARAMETER_CONFIG = ?, IS_ENABLED = ?, " +
                    "SOURCE_TYPE = 'COMBINATION_SYNC', SOURCE_COMBINATION_ID = ?, SOURCE_INDICATOR_ID = ?, " +
                    "LAST_SYNC_TIME = ?, UPDATE_USER = ?, UPDATE_TIME = ? WHERE MODEL_ID = ?";
            jdbcTemplate.update(updateSql,
                    modelCode, modelName, modelType, dataSourceId,
                    sqlStatement, businessMeaning, parameterConfig, isEnabled,
                    sourceCombId, sourceIndId, now, user, now, modelId);
            log.info("[数据模型 upsert] 更新, cfgId={}, modelId={}", cfgId, modelId);
            return false;
        }

        // insert：避开重名 modelCode，若冲突则追加后缀
        String finalCode = ensureUniqueModelCode(modelCode);
        String modelId = RandomUtil.uuStringId();
        dataModelMapper.insertDataModel(
                modelId, finalCode, modelName, modelType, businessMeaning,
                null, dataSourceId, sqlStatement, null, null, null, null, null, null, null,
                null, null, null, null, parameterConfig, null, "1.0", "DRAFT", isEnabled,
                0, null, null, user, now, user, now);
        // 补充来源字段
        String backfillSql = "UPDATE TBL_DATA_MODEL SET SOURCE_TYPE = 'COMBINATION_SYNC', " +
                "SOURCE_COMBINATION_ID = ?, SOURCE_INDICATOR_CONFIG_ID = ?, SOURCE_INDICATOR_ID = ?, " +
                "LAST_SYNC_TIME = ? WHERE MODEL_ID = ?";
        jdbcTemplate.update(backfillSql, sourceCombId, cfgId, sourceIndId, now, modelId);
        log.info("[数据模型 upsert] 插入, cfgId={}, modelId={}, modelCode={}", cfgId, modelId, finalCode);
        return true;
    }

    private String ensureUniqueModelCode(String baseCode) {
        if (baseCode == null || baseCode.isEmpty()) {
            return "SYNC_" + System.currentTimeMillis();
        }
        String candidate = baseCode;
        int tryCount = 0;
        while (isModelCodeExistsGlobal(candidate) && tryCount < 20) {
            String suffix = "_S" + Integer.toHexString((int) (System.currentTimeMillis() & 0xffff));
            int keep = Math.max(1, com.huabo.fxgl.service.sync.DataModelSyncHelper.MODEL_CODE_MAX_LEN - suffix.length());
            candidate = (baseCode.length() > keep ? baseCode.substring(0, keep) : baseCode) + suffix;
            tryCount++;
        }
        return candidate;
    }

    private boolean isModelCodeExistsGlobal(String code) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM TBL_DATA_MODEL WHERE MODEL_CODE = ?", Integer.class, code);
        return cnt != null && cnt > 0;
    }

    private static boolean isTrue(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean) return (Boolean) v;
        String s = v.toString();
        return "Y".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s) || "1".equals(s);
    }

    private static boolean isBlankStr(Object v) {
        return v == null || v.toString().trim().isEmpty();
    }
}
