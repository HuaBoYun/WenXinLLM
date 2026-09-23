package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.finance.dto.TransformTaskDTO;
import com.huabo.finance.entity.TransformTask;
import com.huabo.finance.vo.FieldInfoVO;
import com.huabo.finance.vo.SourceTableVO;

import java.util.List;

/**
 * 转换任务Service接口
 * 
 * @author 华博云开发团队
 * @since 2025-10-24
 */
public interface ITransformTaskService extends IService<TransformTask> {

    /**
     * 获取采集任务的源表列表
     *
     * @param taskId 采集任务ID
     * @return 源表列表
     */
    JsonBean getSourceTables(String taskId);

    /**
     * 获取源表字段列表
     *
     * @param taskId 采集任务ID
     * @param tableName 表名
     * @return 字段列表
     */
    JsonBean getSourceFields(String taskId, String tableName);

    /**
     * 获取系统表列表
     *
     * @return 系统表列表
     */
    JsonBean getSystemTables();

    /**
     * 获取系统表字段列表
     *
     * @param tableName 表名
     * @return 字段列表
     */
    JsonBean getSystemFields(String tableName);

    /**
     * 验证字段类型匹配
     *
     * @param sourceType 源字段类型
     * @param targetType 目标字段类型
     * @param sampleData 样本数据
     * @return 验证结果
     */
    JsonBean validateTypeConversion(String sourceType, String targetType, List<String> sampleData);

    /**
     * 创建转换任务
     *
     * @param taskDTO 转换任务DTO
     * @param createUser 创建人
     * @return 任务ID
     */
    JsonBean createTransformTask(TransformTaskDTO taskDTO, String createUser);

    /**
     * 执行转换任务
     *
     * @param taskId 任务ID
     * @return 执行结果
     */
    JsonBean executeTransform(String taskId);

    /**
     * 获取转换任务列表
     *
     * @param pageNumber 页码
     * @param pageSize 每页数量
     * @return 任务列表
     */
    JsonBean getTaskList(Integer pageNumber, Integer pageSize);

    /**
     * 获取转换任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    JsonBean getTaskDetail(String taskId);

    /**
     * 暂停转换任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    JsonBean pauseTask(String taskId);

    /**
     * 恢复转换任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    JsonBean resumeTask(String taskId);

    /**
     * 取消转换任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    JsonBean cancelTask(String taskId);

    /**
     * 删除转换任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    JsonBean deleteTask(String taskId);

    /**
     * 根据采集任务ID查询财务版本信息
     *
     * @param collectionTaskId 采集任务ID
     * @return 财务版本信息(包含FID和FNAME)
     */
    JsonBean getFinanceVersionByCollectionTask(String collectionTaskId);

    /**
     * 根据财务版本FID查询转换配置
     *
     * @param versionFid 财务版本FID
     * @return 转换配置列表(BD_INIT_SQLCONFIG表数据)
     */
    JsonBean getTransformConfigByVersion(String versionFid);
}

