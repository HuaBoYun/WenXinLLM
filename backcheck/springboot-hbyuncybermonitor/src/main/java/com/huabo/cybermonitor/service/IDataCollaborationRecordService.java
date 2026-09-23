package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.DataCollaborationRecord;
import com.huabo.cybermonitor.vo.DataCollaborationRecordQueryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 数据协同记录服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IDataCollaborationRecordService extends IService<DataCollaborationRecord> {

    /**
     * 分页查询数据协同记录列表
     *
     * @param queryVO 查询条件
     * @return 分页结果
     */
    IPage<DataCollaborationRecord> getCollaborationList(DataCollaborationRecordQueryVO queryVO);

    /**
     * 根据记录ID获取协同记录详情
     *
     * @param recordId 记录ID
     * @return 记录详情
     */
    DataCollaborationRecord getCollaborationDetail(String recordId);

    /**
     * 新增数据协同记录
     *
     * @param record 记录信息
     * @return 是否成功
     */
    boolean addCollaborationRecord(DataCollaborationRecord record);

    /**
     * 更新数据协同记录
     *
     * @param record 记录信息
     * @return 是否成功
     */
    boolean updateCollaborationRecord(DataCollaborationRecord record);

    /**
     * 删除数据协同记录
     *
     * @param recordId 记录ID
     * @return 是否成功
     */
    boolean deleteCollaborationRecord(String recordId);

    /**
     * 批量删除数据协同记录
     *
     * @param recordIds 记录ID列表
     * @return 是否成功
     */
    boolean batchDeleteCollaborationRecord(List<String> recordIds);

    /**
     * 根据协同类型查询记录列表
     *
     * @param collaborationType 协同类型
     * @return 记录列表
     */
    List<DataCollaborationRecord> getRecordsByType(String collaborationType);

    /**
     * 根据协同状态查询记录列表
     *
     * @param collaborationStatus 协同状态
     * @return 记录列表
     */
    List<DataCollaborationRecord> getRecordsByStatus(String collaborationStatus);

    /**
     * 根据源系统查询记录列表
     *
     * @param sourceSystem 源系统
     * @return 记录列表
     */
    List<DataCollaborationRecord> getRecordsBySourceSystem(String sourceSystem);

    /**
     * 根据目标系统查询记录列表
     *
     * @param targetSystem 目标系统
     * @return 记录列表
     */
    List<DataCollaborationRecord> getRecordsByTargetSystem(String targetSystem);

    /**
     * 查询正在处理的协同记录
     *
     * @return 处理中的记录列表
     */
    List<DataCollaborationRecord> getProcessingRecords();

    /**
     * 查询失败的协同记录
     *
     * @return 失败的记录列表
     */
    List<DataCollaborationRecord> getFailedRecords();

    /**
     * 重试失败的协同记录
     *
     * @param recordId 记录ID
     * @return 是否成功
     */
    boolean retryCollaboration(String recordId);

    /**
     * 批量重试失败的协同记录
     *
     * @param recordIds 记录ID列表
     * @return 是否成功
     */
    boolean batchRetryCollaboration(List<String> recordIds);

    /**
     * 取消协同记录
     *
     * @param recordId 记录ID
     * @return 是否成功
     */
    boolean cancelCollaboration(String recordId);

    /**
     * 更新协同状态
     *
     * @param recordId            记录ID
     * @param collaborationStatus 协同状态
     * @param errorMessage        错误信息
     * @return 是否成功
     */
    boolean updateCollaborationStatus(String recordId, String collaborationStatus, String errorMessage);

    /**
     * 获取协同记录统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getCollaborationStatistics();

    /**
     * 获取协同类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getCollaborationTypeDistribution();

    /**
     * 获取协同状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> getCollaborationStatusDistribution();

    /**
     * 获取系统协同统计
     *
     * @return 系统协同统计
     */
    List<Map<String, Object>> getSystemCollaborationStatistics();

    /**
     * 获取协同性能统计
     *
     * @return 性能统计
     */
    Map<String, Object> getCollaborationPerformanceStatistics();

    /**
     * 根据时间范围获取协同趋势
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 协同趋势数据
     */
    List<Map<String, Object>> getCollaborationTrend(String startDate, String endDate);

    /**
     * 导出协同记录列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportCollaborationList(DataCollaborationRecordQueryVO queryVO, HttpServletResponse response);

    /**
     * 获取协同类型标签
     *
     * @param collaborationType 协同类型
     * @return 类型标签
     */
    String getCollaborationTypeLabel(String collaborationType);

    /**
     * 获取协同状态标签
     *
     * @param collaborationStatus 协同状态
     * @return 状态标签
     */
    String getCollaborationStatusLabel(String collaborationStatus);

    /**
     * 获取触发类型标签
     *
     * @param triggerType 触发类型
     * @return 触发类型标签
     */
    String getTriggerTypeLabel(String triggerType);

    /**
     * 获取数据类型标签
     *
     * @param dataType 数据类型
     * @return 数据类型标签
     */
    String getDataTypeLabel(String dataType);

    /**
     * 验证协同配置
     *
     * @param sourceSystem 源系统
     * @param targetSystem 目标系统
     * @param dataType     数据类型
     * @return 验证结果
     */
    Map<String, Object> validateCollaborationConfig(String sourceSystem, String targetSystem, String dataType);

    /**
     * 创建数据协同任务
     *
     * @param collaborationType 协同类型
     * @param sourceSystem      源系统
     * @param targetSystem      目标系统
     * @param dataType          数据类型
     * @param triggerType       触发类型
     * @param dataContent       数据内容
     * @return 协同记录ID
     */
    String createCollaborationTask(String collaborationType, String sourceSystem, String targetSystem,
                                   String dataType, String triggerType, String dataContent);

    /**
     * 执行数据协同
     *
     * @param recordId 记录ID
     * @return 执行结果
     */
    Map<String, Object> executeCollaboration(String recordId);

    /**
     * 获取协同进度
     *
     * @param recordId 记录ID
     * @return 协同进度信息
     */
    Map<String, Object> getCollaborationProgress(String recordId);

    /**
     * 获取协同日志
     *
     * @param recordId 记录ID
     * @return 协同日志列表
     */
    List<Map<String, Object>> getCollaborationLogs(String recordId);

    /**
     * 清理过期的协同记录
     *
     * @param days 保留天数
     * @return 清理数量
     */
    Integer cleanExpiredRecords(Integer days);

    /**
     * 获取协同效率报告
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 效率报告
     */
    Map<String, Object> getCollaborationEfficiencyReport(String startDate, String endDate);
}
