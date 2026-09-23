package com.huabo.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.hbfk.util.RandowUtil;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.entity.*;
import com.huabo.finance.mapper.*;
import com.huabo.finance.service.ICollectionTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 采集任务Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-10-23
 */
@Slf4j
@Service
public class CollectionTaskServiceImpl extends ServiceImpl<CollectionTaskMapper, CollectionTask> implements ICollectionTaskService {

    @Autowired
    private CollectionTaskMapper collectionTaskMapper;

    @Autowired
    private CollectionTaskDetailMapper collectionTaskDetailMapper;

    @Autowired
    private CollectionTaskLogMapper collectionTaskLogMapper;

    @Autowired
    private BdFinancedateMapper bdFinancedateMapper;

    @Autowired
    private TblConfigTableInfoMapper tblConfigTableInfoMapper;

    @Autowired
    private TblConfigColumnInfoMapper tblConfigColumnInfoMapper;

    @Autowired
    private VersionFieldMappingMapper versionFieldMappingMapper;

    @Autowired
    private javax.sql.DataSource dataSource;

    // 任务控制信号Map (taskId -> signal)
    private static final Map<String, String> TASK_CONTROL_SIGNALS = new ConcurrentHashMap<>();

    // 线程池配置 - 降低并发数,避免耗尽数据库连接
    private static final int MIN_THREAD_COUNT = 1;      // 最小线程数
    private static final int MAX_THREAD_COUNT = 3;      // 最大线程数(每个线程2个连接,3个线程=6个连接)
    private static final int THREAD_PER_TABLE = 10;     // 每10张表开启1个线程

    // 采集任务专用线程池(独立于Spring默认线程池)
    // 使用SynchronousQueue确保任务立即执行,不进入队列等待
    private static final ExecutorService COLLECTION_EXECUTOR = new ThreadPoolExecutor(
        3,                                      // 核心线程数=最大线程数,确保线程立即可用
        3,                                      // 最大线程数
        60L,                                    // 空闲线程存活时间
        TimeUnit.SECONDS,                       // 时间单位
        new SynchronousQueue<>(),               // 不缓存任务,直接交给线程执行
        new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "collection-worker-" + threadNumber.getAndIncrement());
                t.setDaemon(false);  // 非守护线程
                return t;
            }
        },
        new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略:调用者线程执行
    );

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean startCollection(String taskName, String planId, String dataSourceId,
                                    String collectionType, String remark, String createUser) {
        try {
            // 1. 生成任务ID
            String taskId = "collection_" + System.currentTimeMillis();

            // 2. 创建任务记录
            CollectionTask task = new CollectionTask();
            task.setTaskId(taskId);
            task.setTaskName(taskName);
            task.setPlanId(planId);
            task.setDataSourceId(dataSourceId);
            task.setCollectionType(collectionType);
            task.setTaskStatus(CollectionTask.STATUS_PENDING);
            task.setProgress(BigDecimal.ZERO);
            task.setRecordCount(0L);
            task.setTotalCount(0L);
            task.setSuccessCount(0L);
            task.setFailedCount(0L);
            task.setStartTime(new Date());  // 设置开始时间
            task.setRemark(remark);
            task.setCreateTime(new Date());
            task.setUpdateTime(new Date());
            task.setCreateUser(createUser);

            // 3. 保存到数据库
            collectionTaskMapper.insert(task);

            // 4. 记录日志
            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, CollectionTaskLog.TYPE_CONNECT,
                    "采集任务创建成功", "任务ID: " + taskId);

            log.info("采集任务创建成功，任务ID: {}", taskId);

            // 5. 立即返回响应给前端
            String finalTaskId = taskId;

            // 6. 异步执行采集(使用独立线程池,不占用Spring默认线程池)
            COLLECTION_EXECUTOR.submit(() -> {
                try {
                    log.info("开始异步执行采集任务: {}", finalTaskId);
                    executeCollection(finalTaskId);
                } catch (Exception e) {
                    log.error("异步执行采集任务失败: {}", finalTaskId, e);
                }
            });

            return ResponseFormat.retParam(1, "采集任务已创建,正在后台执行", taskId);

        } catch (Exception e) {
            log.error("启动采集任务失败", e);
            return ResponseFormat.retParam(0, "启动采集任务失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean pauseCollection(String taskId) {
        try {
            CollectionTask task = collectionTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "采集任务不存在", null);
            }

            if (!CollectionTask.STATUS_RUNNING.equals(task.getTaskStatus())) {
                return ResponseFormat.retParam(0, "只能暂停运行中的任务", null);
            }

            // 设置暂停信号
            TASK_CONTROL_SIGNALS.put(taskId, "PAUSE");

            // 更新任务状态
            task.setTaskStatus(CollectionTask.STATUS_PAUSED);
            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, null, "任务已暂停", null);
            log.info("采集任务暂停成功，任务ID: {}", taskId);
            return ResponseFormat.retParam(1, 200, null);

        } catch (Exception e) {
            log.error("暂停采集任务失败", e);
            return ResponseFormat.retParam(0, "暂停采集任务失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean resumeCollection(String taskId) {
        try {
            CollectionTask task = collectionTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "采集任务不存在", null);
            }

            if (!CollectionTask.STATUS_PAUSED.equals(task.getTaskStatus())) {
                return ResponseFormat.retParam(0, "只能恢复已暂停的任务", null);
            }

            // 清除暂停信号
            TASK_CONTROL_SIGNALS.remove(taskId);

            // 更新任务状态
            task.setTaskStatus(CollectionTask.STATUS_RUNNING);
            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, null, "任务已恢复", null);
            log.info("采集任务恢复成功，任务ID: {}", taskId);
            return ResponseFormat.retParam(1, 200, null);

        } catch (Exception e) {
            log.error("恢复采集任务失败", e);
            return ResponseFormat.retParam(0, "恢复采集任务失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean cancelCollection(String taskId) {
        try {
            CollectionTask task = collectionTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "采集任务不存在", null);
            }

            // 设置取消信号
            TASK_CONTROL_SIGNALS.put(taskId, "CANCEL");

            // 更新任务状态
            task.setTaskStatus(CollectionTask.STATUS_CANCELLED);
            task.setEndTime(new Date());
            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, null, "任务已取消", null);
            log.info("采集任务取消成功，任务ID: {}", taskId);
            return ResponseFormat.retParam(1, 200, null);

        } catch (Exception e) {
            log.error("取消采集任务失败", e);
            return ResponseFormat.retParam(0, "取消采集任务失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean deleteCollection(String taskId) {
        try {
            CollectionTask task = collectionTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "采集任务不存在", null);
            }

            // 如果任务正在运行，先取消
            if (CollectionTask.STATUS_RUNNING.equals(task.getTaskStatus())) {
                cancelCollection(taskId);
                // 等待任务取消
                Thread.sleep(1000);
            }

            // 删除任务（级联删除详情和日志）
            collectionTaskMapper.deleteById(taskId);

            log.info("采集任务删除成功，任务ID: {}", taskId);
            return ResponseFormat.retParam(1, 200, null);

        } catch (Exception e) {
            log.error("删除采集任务失败", e);
            return ResponseFormat.retParam(0, "删除采集任务失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getTaskStatus(String taskId) {
        try {
            CollectionTask task = collectionTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "采集任务不存在", null);
            }

            // 查询详情列表
            List<CollectionTaskDetail> details = collectionTaskDetailMapper.selectByTaskId(taskId);

            // 组装返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("task", task);
            result.put("details", details);

            return ResponseFormat.retParam(1, 200, result);

        } catch (Exception e) {
            log.error("获取采集任务状态失败", e);
            return ResponseFormat.retParam(0, "获取采集任务状态失败: " + e.getMessage(), null);
        }
    }

    @Async
    @Override
    public void executeCollection(String taskId) {
        Connection sourceConn = null;
        Connection targetConn = null;

        try {
            // 1. 更新任务状态为运行中
            CollectionTask task = collectionTaskMapper.selectById(taskId);
            task.setTaskStatus(CollectionTask.STATUS_RUNNING);
            task.setStartTime(new Date());
            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, null, "开始执行采集任务", null);

            // 2. planId实际上是财务版本ID(BD_FINVERSION.FID)
            String versionFid = task.getPlanId();
            if (versionFid == null || versionFid.isEmpty()) {
                throw new Exception("财务版本ID不能为空");
            }

            // 3. 获取数据源信息
            BdFinancedate dataSource = bdFinancedateMapper.selectById(task.getDataSourceId());
            if (dataSource == null) {
                throw new Exception("数据源不存在");
            }

            // 4. 连接源数据库
            sourceConn = connectToDataSource(dataSource);
            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, CollectionTaskLog.TYPE_CONNECT,
                    "源数据库连接成功", "数据源: " + dataSource.getFintext());

            // 5. 连接目标数据库(当前系统数据库)
            targetConn = getTargetConnection();
            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, CollectionTaskLog.TYPE_CONNECT,
                    "目标数据库连接成功", null);

            // 6. 获取版本字段映射配置
            List<VersionFieldMapping> allMappings = versionFieldMappingMapper.selectByVersionFid(versionFid);
            if (allMappings == null || allMappings.isEmpty()) {
                throw new Exception("财务版本未配置字段映射,versionFid=" + versionFid);
            }

            // 按源表名分组
            Map<String, List<VersionFieldMapping>> tableGroupMap = new HashMap<>();
            for (VersionFieldMapping mapping : allMappings) {
                String sourceTableName = mapping.getSourceTableName();
                if (!tableGroupMap.containsKey(sourceTableName)) {
                    tableGroupMap.put(sourceTableName, new ArrayList<>());
                }
                tableGroupMap.get(sourceTableName).add(mapping);
            }

            task.setTotalCount((long) tableGroupMap.size());
            collectionTaskMapper.updateById(task);

            int tableCount = tableGroupMap.size();
            // 计算线程数: 每10张表开启1个线程,最少1个,最多3个(避免耗尽数据库连接)
            int threadCount = Math.max(MIN_THREAD_COUNT, Math.min(MAX_THREAD_COUNT, (tableCount + THREAD_PER_TABLE - 1) / THREAD_PER_TABLE));

            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, null,
                    "共需采集 " + tableCount + " 张表,开启 " + threadCount + " 个线程并发处理", null);
            log.info("任务 {} 开启 {} 个线程并发采集 {} 张表", taskId, threadCount, tableCount);

            // 7. 使用信号量控制并发数(不创建新线程池,复用COLLECTION_EXECUTOR)
            Semaphore semaphore = new Semaphore(threadCount);

            // 使用原子变量统计进度
            AtomicInteger completedTableCount = new AtomicInteger(0);
            AtomicInteger failedTableCount = new AtomicInteger(0);

            // 提交所有采集任务(使用信号量控制并发)
            List<Future<?>> futures = new ArrayList<>();
            log.info("开始提交 {} 个表的采集任务到线程池", tableGroupMap.size());

            for (Map.Entry<String, List<VersionFieldMapping>> entry : tableGroupMap.entrySet()) {
                String sourceTableName = entry.getKey();
                List<VersionFieldMapping> tableMappings = entry.getValue();

                log.info("提交表 {} 的采集任务", sourceTableName);

                Future<?> future = COLLECTION_EXECUTOR.submit(() -> {
                    Connection threadSourceConn = null;
                    Connection threadTargetConn = null;
                    try {
                        log.info("[{}] 准备采集表 {}", Thread.currentThread().getName(), sourceTableName);

                        // 获取信号量许可(控制并发数)
                        semaphore.acquire();
                        log.info("[{}] 获取信号量成功,开始采集表 {}", Thread.currentThread().getName(), sourceTableName);

                        // 检查控制信号
                        String signal = TASK_CONTROL_SIGNALS.get(taskId);
                        if ("CANCEL".equals(signal)) {
                            log.warn("任务 {} 被取消,跳过表 {}", taskId, sourceTableName);
                            return;
                        }
                        if ("PAUSE".equals(signal)) {
                            log.warn("任务 {} 被暂停,跳过表 {}", taskId, sourceTableName);
                            return;
                        }

                        // 每个线程创建独立的数据库连接
                        log.info("[{}] 开始连接数据源...", Thread.currentThread().getName());
                        threadSourceConn = connectToDataSource(dataSource);
                        log.info("[{}] 源数据库连接成功", Thread.currentThread().getName());

                        threadTargetConn = getTargetConnection();
                        log.info("[{}] 目标数据库连接成功", Thread.currentThread().getName());

                        // 采集单表数据
                        collectTableDataByMapping(taskId, threadSourceConn, threadTargetConn, sourceTableName, tableMappings);

                        // 更新完成计数
                        int completed = completedTableCount.incrementAndGet();

                        // 更新任务进度
                        updateTaskProgress(taskId, completed, tableCount);

                        log.info("表 {} 采集完成,进度: {}/{}", sourceTableName, completed, tableCount);

                    } catch (InterruptedException e) {
                        log.error("表 {} 采集被中断", sourceTableName, e);
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        failedTableCount.incrementAndGet();
                        log.error("表 {} 采集失败: {}", sourceTableName, e.getMessage(), e);
                        saveLog(taskId, null, CollectionTaskLog.LEVEL_ERROR, CollectionTaskLog.TYPE_ERROR,
                                "表采集失败: " + sourceTableName, e.getMessage());
                    } finally {
                        // 关闭线程的数据库连接
                        closeConnection(threadSourceConn);
                        closeConnection(threadTargetConn);
                        // 释放信号量许可
                        semaphore.release();
                    }
                });

                futures.add(future);
                log.info("表 {} 的采集任务已提交到线程池", sourceTableName);
            }

            log.info("所有 {} 个表的采集任务已提交完成,开始等待执行结果", futures.size());

            // 等待所有任务完成
            for (Future<?> future : futures) {
                try {
                    future.get(2, TimeUnit.HOURS);  // 最多等待2小时
                } catch (TimeoutException e) {
                    log.warn("任务 {} 某个表采集超时", taskId);
                    future.cancel(true);
                } catch (Exception e) {
                    log.error("任务 {} 等待完成时出错", taskId, e);
                }
            }

            // 检查是否有失败的表
            int failed = failedTableCount.get();
            if (failed > 0) {
                log.warn("任务 {} 完成,但有 {} 张表采集失败", taskId, failed);
                saveLog(taskId, null, CollectionTaskLog.LEVEL_WARN, null,
                        "采集完成,但有 " + failed + " 张表失败", null);
            }

            // 8. 任务完成 - 最后一次更新进度和记录数
            int completed = completedTableCount.get();
            updateTaskProgress(taskId, completed, tableCount);

            // 重新查询任务,获取最新的进度和记录数
            task = collectionTaskMapper.selectById(taskId);
            task.setTaskStatus(CollectionTask.STATUS_SUCCESS);
            task.setEndTime(new Date());
            task.setElapsedTime(task.getEndTime().getTime() - task.getStartTime().getTime());
            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

            log.info("任务 {} 完成: 进度={}%, 总记录数={}", taskId, task.getProgress(), task.getRecordCount());

            saveLog(taskId, null, CollectionTaskLog.LEVEL_INFO, null, "采集任务执行完成", null);
            log.info("采集任务执行完成，任务ID: {}", taskId);

        } catch (Exception e) {
            log.error("执行采集任务失败，任务ID: " + taskId, e);

            // 更新任务状态为失败
            CollectionTask task = collectionTaskMapper.selectById(taskId);
            task.setTaskStatus(CollectionTask.STATUS_FAILED);
            task.setEndTime(new Date());
            task.setErrorMessage(e.getMessage());
            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

            saveLog(taskId, null, CollectionTaskLog.LEVEL_ERROR, CollectionTaskLog.TYPE_ERROR,
                    "采集任务执行失败", e.getMessage());

        } finally {
            // 关闭连接
            closeConnection(sourceConn);
            closeConnection(targetConn);
            // 清除控制信号
            TASK_CONTROL_SIGNALS.remove(taskId);
        }
    }

    /**
     * 采集单表数据
     */
    private void collectTableData(String taskId, Connection sourceConn, Connection targetConn,
                                  TblConfigTableInfo tableInfo) throws Exception {
        String detailId = RandowUtil.uuId();
        CollectionTaskDetail detail = new CollectionTaskDetail();
        detail.setDetailId(detailId);
        detail.setTaskId(taskId);
        detail.setTableConfigId(tableInfo.getFid());
        detail.setSourceTableName(tableInfo.getOutsTableName());
        detail.setTargetTableName(tableInfo.getOursTableName());
        detail.setDetailStatus("RUNNING");
        detail.setRecordCount(0L);
        detail.setSuccessCount(0L);
        detail.setFailedCount(0L);
        detail.setStartTime(new Date());
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        collectionTaskDetailMapper.insert(detail);

        try {
            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_INFO, null,
                    "开始采集表: " + tableInfo.getOutsTableName(), null);

            // 1. 获取列配置
            List<TblConfigColumnInfo> columnList = tblConfigColumnInfoMapper.selectListByTableId(tableInfo.getFid());
            if (columnList == null || columnList.isEmpty()) {
                throw new Exception("表未配置字段映射");
            }

            // 2. 在目标数据库创建表
            createTargetTable(targetConn, tableInfo, columnList);
            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_INFO, CollectionTaskLog.TYPE_CREATE_TABLE,
                    "目标表创建成功: " + tableInfo.getOursTableName(), null);

            // 3. 构建查询SQL
            String querySql = buildQuerySql(tableInfo, columnList);
            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_DEBUG, null,
                    "查询SQL", querySql);

            // 4. 提取字段名列表
            List<String> sourceFieldNames = new ArrayList<>();
            List<String> targetFieldNames = new ArrayList<>();
            for (TblConfigColumnInfo column : columnList) {
                sourceFieldNames.add(column.getOutsColname());
                targetFieldNames.add(column.getOursColname());
            }

            // 5. 使用流式查询并插入(边查边插,避免内存溢出和连接超时)
            int insertCount = streamQueryAndInsert(sourceConn, targetConn, querySql,
                                                   tableInfo.getOursTableName(),
                                                   sourceFieldNames, targetFieldNames);

            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_INFO, null,
                    "流式处理完成,共插入: " + insertCount + " 条", null);

            // 6. 更新详情状态
            detail.setDetailStatus("SUCCESS");
            detail.setRecordCount((long) insertCount);
            detail.setSuccessCount((long) insertCount);
            detail.setEndTime(new Date());
            detail.setElapsedTime(detail.getEndTime().getTime() - detail.getStartTime().getTime());
            detail.setUpdateTime(new Date());
            collectionTaskDetailMapper.updateById(detail);

            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_INFO, null,
                    "表采集完成: " + tableInfo.getOutsTableName() + ", 记录数: " + insertCount, null);

        } catch (Exception e) {
            log.error("采集表数据失败: " + tableInfo.getOutsTableName(), e);

            detail.setDetailStatus("FAILED");
            detail.setErrorMessage(e.getMessage());
            detail.setEndTime(new Date());
            detail.setUpdateTime(new Date());
            collectionTaskDetailMapper.updateById(detail);

            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_ERROR, CollectionTaskLog.TYPE_ERROR,
                    "表采集失败: " + tableInfo.getOutsTableName(), e.getMessage());

            throw e;
        }
    }

    /**
     * 采集单表数据(使用版本字段映射)
     */
    private void collectTableDataByMapping(String taskId, Connection sourceConn, Connection targetConn,
                                           String sourceTableName, List<VersionFieldMapping> mappings) throws Exception {
        String detailId = RandowUtil.uuId();

        // 获取目标表名(取第一个映射的目标表名)
        String targetTableName = mappings.get(0).getTargetTableName();

        // 创建任务明细记录
        CollectionTaskDetail detail = new CollectionTaskDetail();
        detail.setDetailId(detailId);
        detail.setTaskId(taskId);
        detail.setTableConfigId(null);  // 使用版本映射时没有tableConfigId
        detail.setSourceTableName(sourceTableName);
        detail.setTargetTableName(targetTableName);
        detail.setDetailStatus("RUNNING");
        detail.setRecordCount(0L);
        detail.setSuccessCount(0L);
        detail.setFailedCount(0L);
        detail.setStartTime(new Date());
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        collectionTaskDetailMapper.insert(detail);

        try {
            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_INFO, null,
                    "开始采集表: " + sourceTableName + " -> " + targetTableName, null);

            // 1. 在目标数据库创建表
            createTargetTableByMapping(sourceConn, targetConn, sourceTableName, targetTableName, mappings);
            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_INFO, CollectionTaskLog.TYPE_CREATE_TABLE,
                    "目标表创建成功: " + targetTableName, null);

            // 2. 构建查询SQL
            String querySql = buildQuerySqlByMapping(sourceTableName, mappings);
            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_DEBUG, null,
                    "查询SQL", querySql);

            // 3. 提取字段名列表
            List<String> sourceFieldNames = new ArrayList<>();
            List<String> targetFieldNames = new ArrayList<>();

            // 检查是否有通配符*
            boolean hasWildcard = false;
            for (VersionFieldMapping mapping : mappings) {
                if ("*".equals(mapping.getSourceFieldName()) || "*".equals(mapping.getTargetFieldName())) {
                    hasWildcard = true;
                    break;
                }
            }

            if (hasWildcard) {
                // 如果有通配符,从目标表获取实际字段列表
                log.info("检测到通配符*,从目标表获取字段列表");
                List<String> actualFields = getTableFields(targetConn, targetTableName);
                sourceFieldNames.addAll(actualFields);
                targetFieldNames.addAll(actualFields);
            } else {
                // 没有通配符,直接使用映射字段
                for (VersionFieldMapping mapping : mappings) {
                    sourceFieldNames.add(mapping.getSourceFieldName());
                    targetFieldNames.add(mapping.getTargetFieldName());
                }
            }

            // 4. 使用流式查询并插入(边查边插,避免内存溢出和连接超时)
            int insertCount = streamQueryAndInsert(sourceConn, targetConn, querySql,
                                                   targetTableName,
                                                   sourceFieldNames, targetFieldNames);

            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_INFO, null,
                    "流式处理完成,共插入: " + insertCount + " 条", null);

            // 5. 更新详情状态
            detail.setDetailStatus("SUCCESS");
            detail.setRecordCount((long) insertCount);
            detail.setSuccessCount((long) insertCount);
            detail.setEndTime(new Date());
            detail.setElapsedTime(detail.getEndTime().getTime() - detail.getStartTime().getTime());
            detail.setUpdateTime(new Date());
            collectionTaskDetailMapper.updateById(detail);

            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_INFO, null,
                    "表采集完成: " + sourceTableName + ", 记录数: " + insertCount, null);

        } catch (Exception e) {
            log.error("采集表数据失败: " + sourceTableName, e);

            detail.setDetailStatus("FAILED");
            detail.setErrorMessage(e.getMessage());
            detail.setEndTime(new Date());
            detail.setUpdateTime(new Date());
            collectionTaskDetailMapper.updateById(detail);

            saveLog(taskId, detailId, CollectionTaskLog.LEVEL_ERROR, CollectionTaskLog.TYPE_ERROR,
                    "表采集失败: " + sourceTableName, e.getMessage());

            throw e;
        }
    }

    /**
     * 连接数据源
     */
    private Connection connectToDataSource(BdFinancedate dataSource) throws Exception {
        String dbType = dataSource.getFinancedbtype();
        String host = dataSource.getFinanceconn();
        String port = dataSource.getFinanceport();
        String dbInstance = dataSource.getFinancedbexpm();
        String username = dataSource.getFinanceuser();
        String password = dataSource.getFinancepwd();

        // 构建完整的JDBC URL
        String url = buildJdbcUrl(dbType, host, port, dbInstance);

        log.info("连接数据源: dbType={}, host={}, port={}, instance={}, url={}, username={}",
                 dbType, host, port, dbInstance, url, username);

        // 根据数据库类型加载驱动
        String driverClass;
        if ("Oracle".equalsIgnoreCase(dbType)) {
            driverClass = "oracle.jdbc.driver.OracleDriver";
        } else if ("MySQL".equalsIgnoreCase(dbType) || "Mysql".equalsIgnoreCase(dbType)) {
            driverClass = "com.mysql.cj.jdbc.Driver";
        } else if ("DM".equalsIgnoreCase(dbType)) {
            driverClass = "dm.jdbc.driver.DmDriver";
        } else if ("SqlServer".equalsIgnoreCase(dbType)) {
            driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else {
            throw new Exception("不支持的数据库类型: " + dbType);
        }

        Class.forName(driverClass);
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * 构建JDBC URL
     * 根据数据库类型和配置参数构建完整的JDBC连接字符串
     *
     * @param dbType 数据库类型 (Oracle/MySQL/DM/SqlServer)
     * @param host 主机地址
     * @param port 端口号
     * @param dbInstance 数据库实例(Oracle的SID/SERVICE_NAME, MySQL的database等)
     * @return 完整的JDBC URL
     */
    private String buildJdbcUrl(String dbType, String host, String port, String dbInstance) throws Exception {
        // 如果host已经是完整的JDBC URL,直接返回
        if (host != null && host.toLowerCase().startsWith("jdbc:")) {
            return host;
        }

        // 参数校验
        if (host == null || host.trim().isEmpty()) {
            throw new Exception("数据库连接地址不能为空");
        }

        // 根据数据库类型构建JDBC URL
        if ("Oracle".equalsIgnoreCase(dbType)) {
            // Oracle格式: jdbc:oracle:thin:@host:port:sid 或 jdbc:oracle:thin:@host:port/service_name
            String oraclePort = (port != null && !port.isEmpty()) ? port : "1521";
            if (dbInstance != null && !dbInstance.isEmpty()) {
                // 如果实例名包含/,使用SERVICE_NAME方式,否则使用SID方式
                if (dbInstance.contains("/")) {
                    return String.format("jdbc:oracle:thin:@%s:%s%s", host, oraclePort, dbInstance);
                } else {
                    return String.format("jdbc:oracle:thin:@%s:%s:%s", host, oraclePort, dbInstance);
                }
            } else {
                throw new Exception("Oracle数据库实例(SID或SERVICE_NAME)不能为空");
            }
        } else if ("MySQL".equalsIgnoreCase(dbType) || "Mysql".equalsIgnoreCase(dbType)) {
            // MySQL格式: jdbc:mysql://host:port/database
            String mysqlPort = (port != null && !port.isEmpty()) ? port : "3306";
            String database = (dbInstance != null && !dbInstance.isEmpty()) ? dbInstance : "";
            return String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai",
                               host, mysqlPort, database);
        } else if ("DM".equalsIgnoreCase(dbType)) {
            // 达梦格式: jdbc:dm://host:port
            String dmPort = (port != null && !port.isEmpty()) ? port : "5236";
            return String.format("jdbc:dm://%s:%s", host, dmPort);
        } else if ("SqlServer".equalsIgnoreCase(dbType)) {
            // SQL Server格式: jdbc:sqlserver://host:port;databaseName=database
            String sqlServerPort = (port != null && !port.isEmpty()) ? port : "1433";
            if (dbInstance != null && !dbInstance.isEmpty()) {
                return String.format("jdbc:sqlserver://%s:%s;databaseName=%s", host, sqlServerPort, dbInstance);
            } else {
                return String.format("jdbc:sqlserver://%s:%s", host, sqlServerPort);
            }
        } else {
            throw new Exception("不支持的数据库类型: " + dbType);
        }
    }

    /**
     * 获取目标数据库连接(当前系统数据库)
     */
    private Connection getTargetConnection() throws Exception {
        // 使用Spring注入的DataSource获取连接
        return dataSource.getConnection();
    }

    /**
     * 创建目标表
     */
    private void createTargetTable(Connection targetConn, TblConfigTableInfo tableInfo,
                                   List<TblConfigColumnInfo> columnList) throws Exception {
        String tableName = tableInfo.getOursTableName();

        // 检查表是否存在
        DatabaseMetaData metaData = targetConn.getMetaData();
        ResultSet rs = metaData.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"});
        boolean tableExists = rs.next();
        rs.close();

        if (tableExists) {
            log.info("目标表已存在: {}", tableName);
            return;
        }

        // 构建建表SQL
        StringBuilder createSql = new StringBuilder();
        createSql.append("CREATE TABLE ").append(tableName).append(" (");

        for (int i = 0; i < columnList.size(); i++) {
            TblConfigColumnInfo column = columnList.get(i);
            if (i > 0) {
                createSql.append(", ");
            }
            createSql.append(column.getOursColname()).append(" ");
            createSql.append(column.getColType());
            if (column.getColLength() != null && !column.getColLength().isEmpty()) {
                createSql.append("(").append(column.getColLength()).append(")");
            }
        }

        createSql.append(")");

        // 执行建表SQL
        try (Statement stmt = targetConn.createStatement()) {
            stmt.execute(createSql.toString());
            log.info("创建目标表成功: {}", tableName);
        }
    }

    /**
     * 构建查询SQL
     */
    private String buildQuerySql(TblConfigTableInfo tableInfo, List<TblConfigColumnInfo> columnList) {
        StringBuilder sql = new StringBuilder("SELECT ");

        // 构建字段列表
        for (int i = 0; i < columnList.size(); i++) {
            TblConfigColumnInfo column = columnList.get(i);
            if (i > 0) {
                sql.append(", ");
            }

            String sourceCol = column.getOutsColname();
            // 如果是 * 则选择全部字段
            if ("*".equals(sourceCol)) {
                sql.append("*");
                break;
            } else {
                sql.append(sourceCol);
            }
        }

        sql.append(" FROM ").append(tableInfo.getOutsTableName());

        // 添加查询条件(如果有)
        if (tableInfo.getSqlText() != null && !tableInfo.getSqlText().isEmpty()) {
            sql.append(" WHERE ").append(tableInfo.getSqlText());
        }

        return sql.toString();
    }

    /**
     * 查询源数据(旧方法,保留用于小数据量场景)
     */
    private List<Map<String, Object>> querySourceData(Connection sourceConn, String sql) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        try (Statement stmt = sourceConn.createStatement()) {
            // 设置查询超时(30分钟,用于大数据量查询)
            stmt.setQueryTimeout(1800);

            // 设置fetchSize,避免一次性加载所有数据到内存
            stmt.setFetchSize(1000);

            log.info("开始执行查询SQL: {}", sql);
            long startTime = System.currentTimeMillis();

            try (ResultSet rs = stmt.executeQuery(sql)) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                int rowCount = 0;
                int lastLogCount = 0;

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        row.put(columnName, value);
                    }
                    result.add(row);
                    rowCount++;

                    // 每查询10000条记录输出一次进度日志
                    if (rowCount - lastLogCount >= 10000) {
                        long elapsed = System.currentTimeMillis() - startTime;
                        log.info("查询进度: 已读取 {} 条记录, 耗时 {} 秒", rowCount, elapsed / 1000);
                        lastLogCount = rowCount;
                    }
                }

                long totalTime = System.currentTimeMillis() - startTime;
                log.info("查询完成: 共 {} 条记录, 总耗时 {} 秒", rowCount, totalTime / 1000);
            }
        }

        return result;
    }

    /**
     * 流式查询并插入数据(边查边插,避免内存溢出和连接超时)
     * 适用于大数据量场景
     */
    private int streamQueryAndInsert(Connection sourceConn, Connection targetConn,
                                     String querySql, String tableName,
                                     List<String> sourceFieldNames, List<String> targetFieldNames) throws Exception {

        int totalProcessed = 0;
        long startTime = System.currentTimeMillis();
        int lastLogCount = 0;

        log.info("开始流式查询并插入数据");
        log.info("查询SQL: {}", querySql);

        // 获取主键字段
        Set<String> primaryKeySet = new LinkedHashSet<>();
        try {
            DatabaseMetaData metaData = targetConn.getMetaData();
            ResultSet primaryKeysRs = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
            while (primaryKeysRs.next()) {
                String pkColumn = primaryKeysRs.getString("COLUMN_NAME");
                primaryKeySet.add(pkColumn.toUpperCase());
            }
            primaryKeysRs.close();

            if (primaryKeySet.isEmpty()) {
                log.info("表 {} 没有主键,将使用INSERT模式", tableName);
            } else {
                log.info("表 {} 的主键字段: {}", tableName, String.join(", ", primaryKeySet));

                // 检查主键字段是否在字段列表中
                Set<String> fieldNameSet = new HashSet<>();
                for (String fieldName : targetFieldNames) {
                    fieldNameSet.add(fieldName.toUpperCase());
                }

                boolean allPkInFields = true;
                for (String pkColumn : primaryKeySet) {
                    if (!fieldNameSet.contains(pkColumn.toUpperCase())) {
                        log.warn("主键字段 {} 不在字段列表中,将使用INSERT模式", pkColumn);
                        allPkInFields = false;
                        break;
                    }
                }

                // 如果主键字段不在字段列表中,清空主键集合,使用INSERT模式
                if (!allPkInFields) {
                    primaryKeySet.clear();
                }
            }
        } catch (Exception e) {
            log.warn("获取主键信息失败: {}", e.getMessage());
        }

        // 构建INSERT或MERGE SQL
        String insertSql = buildInsertOrMergeSql(tableName, targetFieldNames, primaryKeySet);

        try (Statement stmt = sourceConn.createStatement()) {
            // 设置查询超时(30分钟)
            stmt.setQueryTimeout(1800);

            // 设置fetchSize为1000,分批从数据库获取数据
            stmt.setFetchSize(1000);

            log.info("开始执行查询SQL...");
            long queryStartTime = System.currentTimeMillis();

            try (ResultSet rs = stmt.executeQuery(querySql)) {
                long queryEndTime = System.currentTimeMillis();
                log.info("查询SQL执行完成,耗时 {} 秒", (queryEndTime - queryStartTime) / 1000);

                log.info("准备PreparedStatement...");
                try (PreparedStatement pstmt = targetConn.prepareStatement(insertSql)) {
                    log.info("PreparedStatement准备完成,开始处理数据...");

                    int batchSize = 0;
                    int queryCount = 0;

                    while (rs.next()) {
                        queryCount++;

                        // 第一条数据时输出日志
                        if (queryCount == 1) {
                            log.info("开始处理第一条数据...");
                        }

                    // 设置参数
                    for (int i = 0; i < sourceFieldNames.size(); i++) {
                        String sourceFieldName = sourceFieldNames.get(i);
                        Object value = rs.getObject(sourceFieldName);

                        // 根据数据类型进行转换,确保达梦数据库兼容
                        setParameterValue(pstmt, i + 1, value, rs, sourceFieldName);
                    }

                    pstmt.addBatch();
                    batchSize++;

                    // 每1000条执行一次批量操作
                    if (batchSize >= 1000) {
                        try {
                            int[] results = pstmt.executeBatch();
                            totalProcessed += results.length;

                            // 每处理10000条输出一次进度
                            if (totalProcessed - lastLogCount >= 10000) {
                                long elapsed = System.currentTimeMillis() - startTime;
                                log.info("流式处理进度: 已查询 {} 条, 已插入 {} 条, 耗时 {} 秒",
                                        queryCount, totalProcessed, elapsed / 1000);
                                lastLogCount = totalProcessed;
                            }
                        } catch (Exception e) {
                            log.warn("批量插入部分失败: {}", e.getMessage());
                        }
                        batchSize = 0;
                    }
                }

                // 处理剩余数据
                if (batchSize > 0) {
                    try {
                        int[] results = pstmt.executeBatch();
                        totalProcessed += results.length;
                    } catch (Exception e) {
                        log.warn("批量插入部分失败: {}", e.getMessage());
                    }
                }

                long totalTime = System.currentTimeMillis() - startTime;
                log.info("流式处理完成: 共查询 {} 条, 共插入 {} 条, 总耗时 {} 秒",
                        queryCount, totalProcessed, totalTime / 1000);
                }
            }
        }

        return totalProcessed;
    }

    /**
     * 获取表的所有字段名
     */
    private List<String> getTableFields(Connection conn, String tableName) throws Exception {
        List<String> fields = new ArrayList<>();

        // 使用SELECT * WHERE 1=0获取字段信息
        String querySql = "SELECT * FROM " + tableName + " WHERE 1=0";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(querySql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                fields.add(columnName);
            }

            log.info("从目标表获取到 {} 个字段: {}", fields.size(), String.join(", ", fields));
        }

        return fields;
    }

    /**
     * 构建INSERT或MERGE SQL
     */
    private String buildInsertOrMergeSql(String tableName, List<String> fieldNames, Set<String> primaryKeySet) {
        if (primaryKeySet.isEmpty()) {
            // 没有主键,使用INSERT
            StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
            for (int i = 0; i < fieldNames.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append(fieldNames.get(i));
            }
            sql.append(") VALUES (");
            for (int i = 0; i < fieldNames.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("?");
            }
            sql.append(")");
            log.info("使用INSERT模式: {}", sql.toString());
            return sql.toString();
        } else {
            // 有主键,使用MERGE
            StringBuilder sql = new StringBuilder("MERGE INTO ").append(tableName).append(" T1 USING (SELECT ");
            for (int i = 0; i < fieldNames.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("? AS ").append(fieldNames.get(i));
            }
            sql.append(" FROM DUAL) T2 ON (");

            int pkIndex = 0;
            for (String pkColumn : primaryKeySet) {
                if (pkIndex > 0) sql.append(" AND ");
                sql.append("T1.").append(pkColumn).append(" = T2.").append(pkColumn);
                pkIndex++;
            }
            sql.append(")");

            // UPDATE部分(排除主键字段)
            int updateFieldCount = 0;
            for (String fieldName : fieldNames) {
                if (!primaryKeySet.contains(fieldName.toUpperCase())) {
                    updateFieldCount++;
                }
            }

            // 如果所有字段都是主键,则不使用MERGE,改用INSERT
            if (updateFieldCount == 0) {
                log.warn("表 {} 所有字段都是主键,无法使用MERGE,改用INSERT模式", tableName);
                StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
                for (int i = 0; i < fieldNames.size(); i++) {
                    if (i > 0) insertSql.append(", ");
                    insertSql.append(fieldNames.get(i));
                }
                insertSql.append(") VALUES (");
                for (int i = 0; i < fieldNames.size(); i++) {
                    if (i > 0) insertSql.append(", ");
                    insertSql.append("?");
                }
                insertSql.append(")");
                log.info("使用INSERT模式: {}", insertSql.toString());
                return insertSql.toString();
            }

            sql.append(" WHEN MATCHED THEN UPDATE SET ");
            int updateIndex = 0;
            for (String fieldName : fieldNames) {
                if (!primaryKeySet.contains(fieldName.toUpperCase())) {
                    if (updateIndex > 0) sql.append(", ");
                    sql.append("T1.").append(fieldName).append(" = T2.").append(fieldName);
                    updateIndex++;
                }
            }

            // INSERT部分
            sql.append(" WHEN NOT MATCHED THEN INSERT (");
            for (int i = 0; i < fieldNames.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append(fieldNames.get(i));
            }
            sql.append(") VALUES (");
            for (int i = 0; i < fieldNames.size(); i++) {
                if (i > 0) sql.append(", ");
                sql.append("T2.").append(fieldNames.get(i));
            }
            sql.append(")");

            String mergeSql = sql.toString();
            log.info("使用MERGE模式: {}", mergeSql);
            return mergeSql;
        }
    }

    /**
     * 插入目标数据
     */
    private int insertTargetData(Connection targetConn, String tableName,
                                 List<TblConfigColumnInfo> columnList,
                                 List<Map<String, Object>> sourceData) throws Exception {
        if (sourceData.isEmpty()) {
            return 0;
        }

        // 构建插入SQL
        StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder valuesSql = new StringBuilder(" VALUES (");

        for (int i = 0; i < columnList.size(); i++) {
            TblConfigColumnInfo column = columnList.get(i);
            if (i > 0) {
                insertSql.append(", ");
                valuesSql.append(", ");
            }
            insertSql.append(column.getOursColname());
            valuesSql.append("?");
        }

        insertSql.append(")").append(valuesSql).append(")");

        // 批量插入
        int insertCount = 0;
        try (PreparedStatement pstmt = targetConn.prepareStatement(insertSql.toString())) {
            for (Map<String, Object> row : sourceData) {
                for (int i = 0; i < columnList.size(); i++) {
                    TblConfigColumnInfo column = columnList.get(i);
                    String sourceCol = column.getOutsColname();
                    Object value = row.get(sourceCol);
                    pstmt.setObject(i + 1, value);
                }
                pstmt.addBatch();
                insertCount++;

                // 每1000条提交一次
                if (insertCount % 1000 == 0) {
                    pstmt.executeBatch();
                }
            }

            // 提交剩余数据
            pstmt.executeBatch();
        }

        return insertCount;
    }

    /**
     * 保存日志
     */
    private void saveLog(String taskId, String detailId, String level, String type,
                        String message, String detail) {
        try {
            CollectionTaskLog log = new CollectionTaskLog();
            log.setLogId(RandowUtil.uuId());
            log.setTaskId(taskId);
            log.setDetailId(detailId);
            log.setLogLevel(level);
            log.setLogType(type);
            log.setLogMessage(message);
            log.setLogDetail(detail);
            log.setCreateTime(new Date());
            collectionTaskLogMapper.insert(log);
        } catch (Exception e) {
            log.error("保存日志失败", e);
        }
    }

    /**
     * 关闭连接
     */
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("关闭数据库连接失败", e);
            }
        }
    }

    /**
     * 根据版本字段映射创建目标表
     */
    private void createTargetTableByMapping(Connection sourceConn, Connection targetConn,
                                           String sourceTableName, String targetTableName,
                                           List<VersionFieldMapping> mappings) throws Exception {
        // 检查表是否存在 - 使用SQL查询更可靠
        boolean tableExists = false;
        try (Statement stmt = targetConn.createStatement()) {
            String checkSql = "SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME = '" + targetTableName.toUpperCase() + "'";
            try (ResultSet rs = stmt.executeQuery(checkSql)) {
                if (rs.next() && rs.getInt(1) > 0) {
                    tableExists = true;
                }
            } catch (Exception e) {
                // 如果USER_TABLES不存在(非Oracle/DM),尝试直接查询表
                try {
                    String testSql = "SELECT 1 FROM " + targetTableName + " WHERE 1=0";
                    stmt.executeQuery(testSql).close();
                    tableExists = true;
                } catch (Exception ex) {
                    tableExists = false;
                }
            }
        }

        if (tableExists) {
            log.info("目标表已存在: {}", targetTableName);
            return;
        }

        log.info("目标表不存在,准备创建: {}", targetTableName);

        // 检查是否包含*字段
        boolean hasWildcard = false;
        for (VersionFieldMapping mapping : mappings) {
            if ("*".equals(mapping.getSourceFieldName())) {
                hasWildcard = true;
                break;
            }
        }

        if (hasWildcard) {
            // 如果包含*,从源表获取所有字段结构
            createTableFromSourceStructure(sourceConn, targetConn, sourceTableName, targetTableName);
        } else {
            // 否则根据映射配置创建表
            createTableFromMappings(targetConn, targetTableName, mappings);
        }
    }

    /**
     * 根据源表结构创建目标表
     */
    private void createTableFromSourceStructure(Connection sourceConn, Connection targetConn,
                                               String sourceTableName, String targetTableName) throws Exception {
        // 直接查询源表结构(使用SELECT * WHERE 1=0获取字段信息)
        String querySql = "SELECT * FROM " + sourceTableName + " WHERE 1=0";

        StringBuilder createSql = new StringBuilder();
        createSql.append("CREATE TABLE ").append(targetTableName).append(" (");

        Set<String> addedColumns = new LinkedHashSet<>();  // 用于去重并保持顺序
        boolean firstColumn = true;

        try (Statement stmt = sourceConn.createStatement();
             ResultSet rs = stmt.executeQuery(querySql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            log.info("源表 {} 实际字段数: {}", sourceTableName, columnCount);

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);

                // 去重:如果字段名已存在,跳过
                if (addedColumns.contains(columnName.toUpperCase())) {
                    log.warn("跳过重复字段: {}", columnName);
                    continue;
                }
                addedColumns.add(columnName.toUpperCase());

                if (!firstColumn) {
                    createSql.append(", ");
                }
                firstColumn = false;

                String columnType = metaData.getColumnTypeName(i);
                int columnSize = metaData.getPrecision(i);
                int nullable = metaData.isNullable(i);

                createSql.append(columnName).append(" ").append(columnType);

                // 添加长度
                if (columnSize > 0 && (columnType.contains("VARCHAR") || columnType.contains("CHAR"))) {
                    createSql.append("(").append(columnSize).append(")");
                }

                // 是否非空
                if (nullable == ResultSetMetaData.columnNoNulls) {
                    createSql.append(" NOT NULL");
                }

                log.info("字段 {}: {} {} ({})", i, columnName, columnType, columnSize);
            }
        }

        // 获取主键信息
        Set<String> primaryKeySet = new LinkedHashSet<>();
        try {
            DatabaseMetaData sourceMetaData = sourceConn.getMetaData();
            ResultSet primaryKeysRs = sourceMetaData.getPrimaryKeys(null, null, sourceTableName.toUpperCase());
            while (primaryKeysRs.next()) {
                String pkColumn = primaryKeysRs.getString("COLUMN_NAME");
                // 只添加实际存在的字段作为主键
                if (addedColumns.contains(pkColumn.toUpperCase())) {
                    primaryKeySet.add(pkColumn);
                }
            }
            primaryKeysRs.close();
        } catch (Exception e) {
            log.warn("获取主键信息失败: {}", e.getMessage());
        }

        // 添加主键约束
        if (!primaryKeySet.isEmpty()) {
            createSql.append(", PRIMARY KEY (");
            createSql.append(String.join(", ", primaryKeySet));
            createSql.append(")");
            log.info("主键字段: {}", String.join(", ", primaryKeySet));
        } else {
            log.warn("未找到主键信息,将创建无主键表");
        }

        createSql.append(")");

        log.info("建表SQL: {}", createSql.toString());

        // 执行建表SQL
        try (Statement stmt = targetConn.createStatement()) {
            stmt.execute(createSql.toString());
            log.info("根据源表结构创建目标表成功: {} -> {}", sourceTableName, targetTableName);
        }
    }

    /**
     * 根据字段映射创建目标表
     */
    private void createTableFromMappings(Connection targetConn, String tableName,
                                        List<VersionFieldMapping> mappings) throws Exception {
        // 构建建表SQL
        StringBuilder createSql = new StringBuilder();
        createSql.append("CREATE TABLE ").append(tableName).append(" (");

        List<String> primaryKeys = new ArrayList<>();
        for (int i = 0; i < mappings.size(); i++) {
            VersionFieldMapping mapping = mappings.get(i);
            if (i > 0) {
                createSql.append(", ");
            }

            String fieldName = mapping.getTargetFieldName();
            String fieldType = mapping.getFieldType();
            Integer fieldLength = mapping.getFieldLength();

            createSql.append(fieldName).append(" ").append(fieldType);

            // 添加长度
            if (fieldLength != null && fieldLength > 0 &&
                    (fieldType.contains("VARCHAR") || fieldType.contains("CHAR"))) {
                createSql.append("(").append(fieldLength).append(")");
            }

            // 是否主键
            if ("Y".equalsIgnoreCase(mapping.getIsKey())) {
                primaryKeys.add(fieldName);
            }

            // 是否非空
            if ("Y".equalsIgnoreCase(mapping.getIsRequired())) {
                createSql.append(" NOT NULL");
            }
        }

        // 添加主键约束
        if (!primaryKeys.isEmpty()) {
            createSql.append(", PRIMARY KEY (");
            createSql.append(String.join(", ", primaryKeys));
            createSql.append(")");
        }

        createSql.append(")");

        // 执行建表SQL
        try (Statement stmt = targetConn.createStatement()) {
            stmt.execute(createSql.toString());
            log.info("根据字段映射创建目标表成功: {}", tableName);
        }
    }

    /**
     * 根据版本字段映射构建查询SQL
     */
    private String buildQuerySqlByMapping(String sourceTableName, List<VersionFieldMapping> mappings) {
        StringBuilder sql = new StringBuilder("SELECT ");

        // 检查是否包含*字段
        boolean hasWildcard = false;
        for (VersionFieldMapping mapping : mappings) {
            if ("*".equals(mapping.getSourceFieldName())) {
                hasWildcard = true;
                break;
            }
        }

        if (hasWildcard) {
            // 如果包含*,直接使用SELECT *
            sql.append("*");
        } else {
            // 否则列出所有字段
            for (int i = 0; i < mappings.size(); i++) {
                VersionFieldMapping mapping = mappings.get(i);
                if (i > 0) {
                    sql.append(", ");
                }

                String sourceFieldName = mapping.getSourceFieldName();
                String calculationLogic = mapping.getCalculationLogic();

                // 如果有计算逻辑,使用计算逻辑;否则直接使用字段名
                if (calculationLogic != null && !calculationLogic.trim().isEmpty()) {
                    sql.append(calculationLogic).append(" AS ").append(sourceFieldName);
                } else {
                    sql.append(sourceFieldName);
                }
            }
        }

        sql.append(" FROM ").append(sourceTableName);

        // 添加查询条件(取第一个有查询条件的映射)
        for (VersionFieldMapping mapping : mappings) {
            String queryCondition = mapping.getQueryCondition();
            if (queryCondition != null && !queryCondition.trim().isEmpty()) {
                sql.append(" WHERE ").append(queryCondition);
                break;
            }
        }

        return sql.toString();
    }

    /**
     * 根据版本字段映射插入目标数据
     */
    private int insertTargetDataByMapping(Connection targetConn, String tableName,
                                         List<VersionFieldMapping> mappings,
                                         List<Map<String, Object>> sourceData) throws Exception {
        if (sourceData == null || sourceData.isEmpty()) {
            return 0;
        }

        // 检查是否包含*字段,如果包含则需要动态解析所有字段
        boolean hasWildcard = false;
        for (VersionFieldMapping mapping : mappings) {
            if ("*".equals(mapping.getSourceFieldName())) {
                hasWildcard = true;
                break;
            }
        }

        List<String> targetFieldNames = new ArrayList<>();
        List<String> sourceFieldNames = new ArrayList<>();

        if (hasWildcard) {
            // 如果包含*,从目标表实际结构中获取所有字段名
            String querySql = "SELECT * FROM " + tableName + " WHERE 1=0";
            try (Statement stmt = targetConn.createStatement();
                 ResultSet rs = stmt.executeQuery(querySql)) {

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    targetFieldNames.add(columnName);
                    sourceFieldNames.add(columnName);
                }

                log.info("从目标表获取到 {} 个字段: {}", columnCount, String.join(", ", targetFieldNames));
            }
        } else {
            // 否则使用映射配置的字段
            for (VersionFieldMapping mapping : mappings) {
                targetFieldNames.add(mapping.getTargetFieldName());
                sourceFieldNames.add(mapping.getSourceFieldName());
            }
        }

        if (targetFieldNames.isEmpty()) {
            log.warn("没有可插入的字段,跳过插入");
            return 0;
        }

        // 获取主键字段
        Set<String> primaryKeySet = new LinkedHashSet<>();
        try {
            DatabaseMetaData metaData = targetConn.getMetaData();
            ResultSet primaryKeysRs = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
            while (primaryKeysRs.next()) {
                String pkColumn = primaryKeysRs.getString("COLUMN_NAME");
                primaryKeySet.add(pkColumn.toUpperCase());
            }
            primaryKeysRs.close();
        } catch (Exception e) {
            log.warn("获取主键信息失败: {}", e.getMessage());
        }

        // 如果没有主键,使用普通INSERT(跳过已存在的数据)
        if (primaryKeySet.isEmpty()) {
            log.warn("表 {} 没有主键,使用INSERT IGNORE模式", tableName);

            // 构建INSERT SQL
            StringBuilder insertSql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
            StringBuilder valuesSql = new StringBuilder(" VALUES (");

            for (int i = 0; i < targetFieldNames.size(); i++) {
                if (i > 0) {
                    insertSql.append(", ");
                    valuesSql.append(", ");
                }
                insertSql.append(targetFieldNames.get(i));
                valuesSql.append("?");
            }

            insertSql.append(")").append(valuesSql).append(")");
            log.info("INSERT SQL: {}", insertSql.toString());

            // 批量插入(忽略重复数据错误)
            int totalProcessed = 0;
            long insertStartTime = System.currentTimeMillis();
            int lastLogCount = 0;

            log.info("开始插入数据,共 {} 条记录", sourceData.size());

            try (PreparedStatement pstmt = targetConn.prepareStatement(insertSql.toString())) {
                int batchSize = 0;
                int rowIndex = 0;

                for (Map<String, Object> row : sourceData) {
                    try {
                        for (int i = 0; i < sourceFieldNames.size(); i++) {
                            String sourceFieldName = sourceFieldNames.get(i);
                            Object value = row.get(sourceFieldName.toUpperCase());
                            pstmt.setObject(i + 1, value);
                        }

                        pstmt.addBatch();
                        batchSize++;
                        rowIndex++;

                        // 每1000条执行一次批量操作
                        if (batchSize >= 1000) {
                            try {
                                int[] results = pstmt.executeBatch();
                                totalProcessed += results.length;

                                // 每插入10000条输出一次进度
                                if (totalProcessed - lastLogCount >= 10000) {
                                    long elapsed = System.currentTimeMillis() - insertStartTime;
                                    log.info("插入进度: 已插入 {} 条记录, 耗时 {} 秒", totalProcessed, elapsed / 1000);
                                    lastLogCount = totalProcessed;
                                }
                            } catch (Exception e) {
                                log.warn("批量插入部分失败(可能是重复数据): {}", e.getMessage());
                            }
                            batchSize = 0;
                        }
                    } catch (Exception e) {
                        log.warn("准备数据失败,跳过该行: {}", e.getMessage());
                    }
                }

                // 处理剩余数据
                if (batchSize > 0) {
                    try {
                        int[] results = pstmt.executeBatch();
                        totalProcessed += results.length;
                    } catch (Exception e) {
                        log.warn("批量插入部分失败(可能是重复数据): {}", e.getMessage());
                    }
                }
            }

            long totalInsertTime = System.currentTimeMillis() - insertStartTime;
            log.info("插入完成: 共 {} 条记录, 总耗时 {} 秒", totalProcessed, totalInsertTime / 1000);

            return totalProcessed;
        }

        // 有主键,使用MERGE SQL(达梦数据库支持)
        StringBuilder mergeSql = new StringBuilder();
        mergeSql.append("MERGE INTO ").append(tableName).append(" T1 USING (");
        mergeSql.append("SELECT ");

        for (int i = 0; i < targetFieldNames.size(); i++) {
            if (i > 0) mergeSql.append(", ");
            mergeSql.append("? AS ").append(targetFieldNames.get(i));
        }

        mergeSql.append(" FROM DUAL) T2 ON (");

        // 构建主键匹配条件
        int pkIndex = 0;
        for (String pkColumn : primaryKeySet) {
            if (pkIndex > 0) mergeSql.append(" AND ");
            mergeSql.append("T1.").append(pkColumn).append(" = T2.").append(pkColumn);
            pkIndex++;
        }

        mergeSql.append(") WHEN MATCHED THEN UPDATE SET ");

        // 构建UPDATE部分(排除主键字段)
        int updateFieldCount = 0;
        for (String fieldName : targetFieldNames) {
            if (!primaryKeySet.contains(fieldName.toUpperCase())) {
                if (updateFieldCount > 0) mergeSql.append(", ");
                mergeSql.append("T1.").append(fieldName).append(" = T2.").append(fieldName);
                updateFieldCount++;
            }
        }

        // 如果所有字段都是主键,则只有INSERT部分
        if (updateFieldCount == 0) {
            mergeSql.setLength(0);
            mergeSql.append("MERGE INTO ").append(tableName).append(" T1 USING (");
            mergeSql.append("SELECT ");
            for (int i = 0; i < targetFieldNames.size(); i++) {
                if (i > 0) mergeSql.append(", ");
                mergeSql.append("? AS ").append(targetFieldNames.get(i));
            }
            mergeSql.append(" FROM DUAL) T2 ON (");
            pkIndex = 0;
            for (String pkColumn : primaryKeySet) {
                if (pkIndex > 0) mergeSql.append(" AND ");
                mergeSql.append("T1.").append(pkColumn).append(" = T2.").append(pkColumn);
                pkIndex++;
            }
            mergeSql.append(")");
        }

        mergeSql.append(" WHEN NOT MATCHED THEN INSERT (");

        for (int i = 0; i < targetFieldNames.size(); i++) {
            if (i > 0) mergeSql.append(", ");
            mergeSql.append(targetFieldNames.get(i));
        }

        mergeSql.append(") VALUES (");

        for (int i = 0; i < targetFieldNames.size(); i++) {
            if (i > 0) mergeSql.append(", ");
            mergeSql.append("T2.").append(targetFieldNames.get(i));
        }

        mergeSql.append(")");

        log.info("MERGE SQL: {}", mergeSql.toString());

        // 批量执行MERGE
        int totalProcessed = 0;
        long mergeStartTime = System.currentTimeMillis();
        int lastLogCount = 0;

        log.info("开始MERGE数据,共 {} 条记录", sourceData.size());

        try (PreparedStatement pstmt = targetConn.prepareStatement(mergeSql.toString())) {
            int batchSize = 0;

            for (Map<String, Object> row : sourceData) {
                for (int i = 0; i < sourceFieldNames.size(); i++) {
                    String sourceFieldName = sourceFieldNames.get(i);
                    Object value = row.get(sourceFieldName.toUpperCase());
                    pstmt.setObject(i + 1, value);
                }

                pstmt.addBatch();
                batchSize++;

                // 每1000条执行一次批量操作
                if (batchSize >= 1000) {
                    int[] results = pstmt.executeBatch();
                    totalProcessed += results.length;

                    // 每处理10000条输出一次进度
                    if (totalProcessed - lastLogCount >= 10000) {
                        long elapsed = System.currentTimeMillis() - mergeStartTime;
                        log.info("MERGE进度: 已处理 {} 条记录, 耗时 {} 秒", totalProcessed, elapsed / 1000);
                        lastLogCount = totalProcessed;
                    }

                    batchSize = 0;
                }
            }

            // 处理剩余数据
            if (batchSize > 0) {
                int[] results = pstmt.executeBatch();
                totalProcessed += results.length;
            }
        }

        long totalMergeTime = System.currentTimeMillis() - mergeStartTime;
        log.info("MERGE完成: 共 {} 条记录, 总耗时 {} 秒", totalProcessed, totalMergeTime / 1000);

        return totalProcessed;
    }

    /**
     * 更新任务进度(线程安全)
     */
    private synchronized void updateTaskProgress(String taskId, int completedCount, int totalCount) {
        try {
            CollectionTask task = collectionTaskMapper.selectById(taskId);
            if (task == null) {
                return;
            }

            // 计算进度百分比
            BigDecimal progress = BigDecimal.valueOf(completedCount * 100.0 / totalCount);
            task.setProgress(progress);

            // 查询总记录数(累加所有详情的记录数)
            Long totalRecordCount = collectionTaskDetailMapper.selectTotalRecordCountByTaskId(taskId);
            task.setRecordCount(totalRecordCount != null ? totalRecordCount : 0L);

            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

        } catch (Exception e) {
            log.warn("更新任务进度失败: {}", e.getMessage());
        }
    }

    /**
     * 设置PreparedStatement参数值,处理达梦数据库类型兼容性
     */
    private void setParameterValue(PreparedStatement pstmt, int paramIndex, Object value,
                                   ResultSet rs, String columnName) throws SQLException {
        if (value == null) {
            pstmt.setObject(paramIndex, null);
            return;
        }

        try {
            // 获取源字段的数据类型
            int columnType = rs.getMetaData().getColumnType(rs.findColumn(columnName));

            // 根据数据类型进行转换
            switch (columnType) {
                case java.sql.Types.DATE:
                case java.sql.Types.TIME:
                case java.sql.Types.TIMESTAMP:
                    // 日期时间类型:转换为java.sql.Timestamp
                    if (value instanceof java.util.Date) {
                        pstmt.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) value).getTime()));
                    } else if (value instanceof java.sql.Date) {
                        pstmt.setTimestamp(paramIndex, new java.sql.Timestamp(((java.sql.Date) value).getTime()));
                    } else if (value instanceof java.sql.Timestamp) {
                        pstmt.setTimestamp(paramIndex, (java.sql.Timestamp) value);
                    } else {
                        pstmt.setObject(paramIndex, value);
                    }
                    break;

                case java.sql.Types.CLOB:
                    // CLOB类型:转换为String
                    if (value instanceof java.sql.Clob) {
                        java.sql.Clob clob = (java.sql.Clob) value;
                        String clobStr = clob.getSubString(1, (int) clob.length());
                        pstmt.setString(paramIndex, clobStr);
                    } else {
                        pstmt.setString(paramIndex, value.toString());
                    }
                    break;

                case java.sql.Types.BLOB:
                    // BLOB类型:转换为byte[]
                    if (value instanceof java.sql.Blob) {
                        java.sql.Blob blob = (java.sql.Blob) value;
                        byte[] blobBytes = blob.getBytes(1, (int) blob.length());
                        pstmt.setBytes(paramIndex, blobBytes);
                    } else if (value instanceof byte[]) {
                        pstmt.setBytes(paramIndex, (byte[]) value);
                    } else {
                        pstmt.setObject(paramIndex, value);
                    }
                    break;

                case java.sql.Types.NUMERIC:
                case java.sql.Types.DECIMAL:
                    // 数值类型:转换为BigDecimal
                    if (value instanceof Number) {
                        pstmt.setBigDecimal(paramIndex, new java.math.BigDecimal(value.toString()));
                    } else {
                        pstmt.setObject(paramIndex, value);
                    }
                    break;

                case java.sql.Types.INTEGER:
                case java.sql.Types.SMALLINT:
                case java.sql.Types.TINYINT:
                    // 整数类型
                    if (value instanceof Number) {
                        pstmt.setInt(paramIndex, ((Number) value).intValue());
                    } else {
                        pstmt.setObject(paramIndex, value);
                    }
                    break;

                case java.sql.Types.BIGINT:
                    // 长整数类型
                    if (value instanceof Number) {
                        pstmt.setLong(paramIndex, ((Number) value).longValue());
                    } else {
                        pstmt.setObject(paramIndex, value);
                    }
                    break;

                case java.sql.Types.DOUBLE:
                case java.sql.Types.FLOAT:
                case java.sql.Types.REAL:
                    // 浮点数类型
                    if (value instanceof Number) {
                        pstmt.setDouble(paramIndex, ((Number) value).doubleValue());
                    } else {
                        pstmt.setObject(paramIndex, value);
                    }
                    break;

                case java.sql.Types.VARCHAR:
                case java.sql.Types.CHAR:
                case java.sql.Types.LONGVARCHAR:
                    // 字符串类型
                    pstmt.setString(paramIndex, value.toString());
                    break;

                default:
                    // 其他类型:直接使用setObject
                    pstmt.setObject(paramIndex, value);
                    break;
            }
        } catch (Exception e) {
            // 如果类型转换失败,尝试直接设置
            log.warn("字段 {} 类型转换失败,使用默认方式: {}", columnName, e.getMessage());
            pstmt.setObject(paramIndex, value);
        }
    }
}

