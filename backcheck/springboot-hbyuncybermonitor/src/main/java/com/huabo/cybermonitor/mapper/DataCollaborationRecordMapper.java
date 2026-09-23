package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.DataCollaborationRecord;
import com.huabo.cybermonitor.vo.DataCollaborationRecordQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 数据协同记录 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface DataCollaborationRecordMapper extends BaseMapper<DataCollaborationRecord> {

    /**
     * 分页查询数据协同记录列表
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<DataCollaborationRecord> selectCollaborationList(IPage<DataCollaborationRecord> page, @Param("query") DataCollaborationRecordQueryVO query);

    /**
     * 根据记录ID查询协同详情
     *
     * @param collaborationId 协同记录ID
     * @return 协同详情
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD WHERE COLLABORATION_ID = #{collaborationId}")
    DataCollaborationRecord selectCollaborationDetail(@Param("collaborationId") String collaborationId);

    /**
     * 根据协同类型查询记录列表
     *
     * @param collaborationType 协同类型
     * @return 记录列表
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD WHERE COLLABORATION_TYPE = #{collaborationType} ORDER BY CREATE_TIME DESC")
    List<DataCollaborationRecord> selectRecordsByType(@Param("collaborationType") String collaborationType);

    /**
     * 根据协同状态查询记录列表
     *
     * @param collaborationStatus 协同状态
     * @return 记录列表
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD WHERE COLLABORATION_STATUS = #{collaborationStatus} ORDER BY CREATE_TIME DESC")
    List<DataCollaborationRecord> selectRecordsByStatus(@Param("collaborationStatus") String collaborationStatus);

    /**
     * 根据源系统查询记录列表
     *
     * @param sourceSystem 源系统
     * @return 记录列表
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD WHERE SOURCE_SYSTEM = #{sourceSystem} ORDER BY CREATE_TIME DESC")
    List<DataCollaborationRecord> selectRecordsBySourceSystem(@Param("sourceSystem") String sourceSystem);

    /**
     * 根据目标系统查询记录列表
     *
     * @param targetSystem 目标系统
     * @return 记录列表
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD WHERE TARGET_SYSTEM = #{targetSystem} ORDER BY CREATE_TIME DESC")
    List<DataCollaborationRecord> selectRecordsByTargetSystem(@Param("targetSystem") String targetSystem);

    /**
     * 根据数据类型查询记录列表
     *
     * @param dataType 数据类型
     * @return 记录列表
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD WHERE DATA_TYPE = #{dataType} ORDER BY CREATE_TIME DESC")
    List<DataCollaborationRecord> selectRecordsByDataType(@Param("dataType") String dataType);

    /**
     * 获取正在处理中的协同记录
     *
     * @return 处理中的记录列表
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD WHERE COLLABORATION_STATUS = 'PROCESSING' ORDER BY START_TIME ASC")
    List<DataCollaborationRecord> selectProcessingRecords();

    /**
     * 获取失败的协同记录
     *
     * @return 失败的记录列表
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD WHERE COLLABORATION_STATUS = 'FAILED' ORDER BY CREATE_TIME DESC")
    List<DataCollaborationRecord> selectFailedRecords();

    /**
     * 获取协同记录统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalCount, " +
            "SUM(CASE WHEN COLLABORATION_STATUS = 'SUCCESS' THEN 1 ELSE 0 END) as successCount, " +
            "SUM(CASE WHEN COLLABORATION_STATUS = 'FAILED' THEN 1 ELSE 0 END) as failedCount, " +
            "SUM(CASE WHEN COLLABORATION_STATUS = 'PROCESSING' THEN 1 ELSE 0 END) as processingCount, " +
            "SUM(CASE WHEN COLLABORATION_STATUS = 'PARTIAL' THEN 1 ELSE 0 END) as partialCount, " +
            "AVG(PROCESSING_DURATION) as avgProcessingDuration, " +
            "SUM(DATA_VOLUME) as totalDataVolume " +
            "FROM DATA_COLLABORATION_RECORD")
    Map<String, Object> selectCollaborationStatistics();

    /**
     * 根据协同类型获取统计分布
     *
     * @return 类型分布统计
     */
    @Select("SELECT COLLABORATION_TYPE as collaborationType, COUNT(*) as count FROM DATA_COLLABORATION_RECORD GROUP BY COLLABORATION_TYPE ORDER BY count DESC")
    List<Map<String, Object>> selectTypeDistribution();

    /**
     * 根据协同状态获取统计分布
     *
     * @return 状态分布统计
     */
    @Select("SELECT COLLABORATION_STATUS as collaborationStatus, COUNT(*) as count FROM DATA_COLLABORATION_RECORD GROUP BY COLLABORATION_STATUS ORDER BY count DESC")
    List<Map<String, Object>> selectStatusDistribution();

    /**
     * 根据数据类型获取统计分布
     *
     * @return 数据类型分布统计
     */
    @Select("SELECT DATA_TYPE as dataType, COUNT(*) as count FROM DATA_COLLABORATION_RECORD GROUP BY DATA_TYPE ORDER BY count DESC")
    List<Map<String, Object>> selectDataTypeDistribution();

    /**
     * 根据系统获取协同统计
     *
     * @return 系统协同统计
     */
    @Select("SELECT " +
            "SOURCE_SYSTEM as sourceSystem, " +
            "TARGET_SYSTEM as targetSystem, " +
            "COUNT(*) as collaborationCount, " +
            "SUM(CASE WHEN COLLABORATION_STATUS = 'SUCCESS' THEN 1 ELSE 0 END) as successCount, " +
            "AVG(PROCESSING_DURATION) as avgDuration " +
            "FROM DATA_COLLABORATION_RECORD " +
            "GROUP BY SOURCE_SYSTEM, TARGET_SYSTEM " +
            "ORDER BY collaborationCount DESC")
    List<Map<String, Object>> selectSystemCollaborationStatistics();

    /**
     * 获取协同性能统计
     *
     * @return 性能统计
     */
    @Select("SELECT " +
            "DATE(CREATE_TIME) as collaborationDate, " +
            "COUNT(*) as totalCount, " +
            "SUM(CASE WHEN COLLABORATION_STATUS = 'SUCCESS' THEN 1 ELSE 0 END) as successCount, " +
            "AVG(PROCESSING_DURATION) as avgDuration, " +
            "SUM(DATA_VOLUME) as totalVolume " +
            "FROM DATA_COLLABORATION_RECORD " +
            "WHERE CREATE_TIME >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
            "GROUP BY DATE(CREATE_TIME) " +
            "ORDER BY collaborationDate DESC")
    List<Map<String, Object>> selectPerformanceStatistics();

    /**
     * 批量插入协同记录
     *
     * @param recordList 记录列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<DataCollaborationRecord> recordList);

    /**
     * 根据条件导出协同记录列表
     *
     * @param query 查询条件
     * @return 记录列表
     */
    List<DataCollaborationRecord> selectCollaborationListForExport(@Param("query") DataCollaborationRecordQueryVO query);

    /**
     * 更新协同状态
     *
     * @param collaborationId     协同记录ID
     * @param collaborationStatus 协同状态
     * @param errorMessage        错误信息
     * @return 更新数量
     */
    @Select("UPDATE DATA_COLLABORATION_RECORD SET COLLABORATION_STATUS = #{collaborationStatus}, " +
            "ERROR_MESSAGE = #{errorMessage}, UPDATE_TIME = NOW() WHERE COLLABORATION_ID = #{collaborationId}")
    int updateCollaborationStatus(@Param("collaborationId") String collaborationId,
                                 @Param("collaborationStatus") String collaborationStatus,
                                 @Param("errorMessage") String errorMessage);

    /**
     * 更新协同结果
     *
     * @param collaborationId 协同记录ID
     * @param successCount    成功数量
     * @param failureCount    失败数量
     * @param endTime         结束时间
     * @param duration        处理时长
     * @return 更新数量
     */
    @Select("UPDATE DATA_COLLABORATION_RECORD SET SUCCESS_COUNT = #{successCount}, FAILURE_COUNT = #{failureCount}, " +
            "END_TIME = #{endTime}, PROCESSING_DURATION = #{duration}, UPDATE_TIME = NOW() " +
            "WHERE COLLABORATION_ID = #{collaborationId}")
    int updateCollaborationResult(@Param("collaborationId") String collaborationId,
                                 @Param("successCount") Integer successCount,
                                 @Param("failureCount") Integer failureCount,
                                 @Param("endTime") String endTime,
                                 @Param("duration") Integer duration);

    /**
     * 获取最近的协同记录
     *
     * @param sourceSystem 源系统
     * @param targetSystem 目标系统
     * @param dataType     数据类型
     * @return 最近的协同记录
     */
    @Select("SELECT * FROM DATA_COLLABORATION_RECORD " +
            "WHERE SOURCE_SYSTEM = #{sourceSystem} AND TARGET_SYSTEM = #{targetSystem} AND DATA_TYPE = #{dataType} " +
            "ORDER BY CREATE_TIME DESC LIMIT 1")
    DataCollaborationRecord selectLatestCollaboration(@Param("sourceSystem") String sourceSystem,
                                                     @Param("targetSystem") String targetSystem,
                                                     @Param("dataType") String dataType);
}
