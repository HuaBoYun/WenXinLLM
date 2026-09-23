package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.DataSubmitRecord;
import com.huabo.cybermonitor.vo.DataSubmitRecordQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 数据报送记录 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface DataSubmitRecordMapper extends BaseMapper<DataSubmitRecord> {

    /**
     * 分页查询数据报送记录列表
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<DataSubmitRecord> selectRecordList(IPage<DataSubmitRecord> page, @Param("query") DataSubmitRecordQueryVO query);

    /**
     * 根据记录ID查询记录详情
     *
     * @param recordId 记录ID
     * @return 记录详情
     */
    Map<String, Object> selectRecordDetail(@Param("recordId") String recordId);

    /**
     * 根据任务ID查询报送记录列表
     *
     * @param taskId 任务ID
     * @return 记录列表
     */
    @Select("SELECT r.*, t.TASK_NAME, e.ENTERPRISE_NAME " +
            "FROM DATA_SUBMIT_RECORD r " +
            "LEFT JOIN DATA_SUBMIT_TASK t ON r.TASK_ID = t.TASK_ID " +
            "LEFT JOIN ENTERPRISE_INFO e ON r.ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "WHERE r.TASK_ID = #{taskId} ORDER BY r.SUBMIT_TIME DESC")
    List<Map<String, Object>> selectRecordsByTaskId(@Param("taskId") String taskId);

    /**
     * 根据企业ID查询报送记录列表
     *
     * @param enterpriseId 企业ID
     * @return 记录列表
     */
    @Select("SELECT r.*, t.TASK_NAME, e.ENTERPRISE_NAME " +
            "FROM DATA_SUBMIT_RECORD r " +
            "LEFT JOIN DATA_SUBMIT_TASK t ON r.TASK_ID = t.TASK_ID " +
            "LEFT JOIN ENTERPRISE_INFO e ON r.ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "WHERE r.ENTERPRISE_ID = #{enterpriseId} ORDER BY r.SUBMIT_TIME DESC")
    List<Map<String, Object>> selectRecordsByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据报送状态查询记录列表
     *
     * @param submitStatus 报送状态
     * @return 记录列表
     */
    @Select("SELECT r.*, t.TASK_NAME, e.ENTERPRISE_NAME " +
            "FROM DATA_SUBMIT_RECORD r " +
            "LEFT JOIN DATA_SUBMIT_TASK t ON r.TASK_ID = t.TASK_ID " +
            "LEFT JOIN ENTERPRISE_INFO e ON r.ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "WHERE r.SUBMIT_STATUS = #{submitStatus} ORDER BY r.SUBMIT_TIME DESC")
    List<Map<String, Object>> selectRecordsByStatus(@Param("submitStatus") String submitStatus);

    /**
     * 根据审核状态查询记录列表
     *
     * @param reviewStatus 审核状态
     * @return 记录列表
     */
    @Select("SELECT r.*, t.TASK_NAME, e.ENTERPRISE_NAME " +
            "FROM DATA_SUBMIT_RECORD r " +
            "LEFT JOIN DATA_SUBMIT_TASK t ON r.TASK_ID = t.TASK_ID " +
            "LEFT JOIN ENTERPRISE_INFO e ON r.ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "WHERE r.REVIEW_STATUS = #{reviewStatus} ORDER BY r.SUBMIT_TIME DESC")
    List<Map<String, Object>> selectRecordsByReviewStatus(@Param("reviewStatus") String reviewStatus);

    /**
     * 获取待审核的记录列表
     *
     * @return 待审核记录列表
     */
    @Select("SELECT r.*, t.TASK_NAME, e.ENTERPRISE_NAME " +
            "FROM DATA_SUBMIT_RECORD r " +
            "LEFT JOIN DATA_SUBMIT_TASK t ON r.TASK_ID = t.TASK_ID " +
            "LEFT JOIN ENTERPRISE_INFO e ON r.ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "WHERE r.REVIEW_STATUS = 'PENDING' AND r.SUBMIT_STATUS = 'SUBMITTED' " +
            "ORDER BY r.SUBMIT_TIME ASC")
    List<Map<String, Object>> selectPendingReviewRecords();

    /**
     * 获取数据质量评分低于阈值的记录
     *
     * @param threshold 质量评分阈值
     * @return 低质量记录列表
     */
    @Select("SELECT r.*, t.TASK_NAME, e.ENTERPRISE_NAME " +
            "FROM DATA_SUBMIT_RECORD r " +
            "LEFT JOIN DATA_SUBMIT_TASK t ON r.TASK_ID = t.TASK_ID " +
            "LEFT JOIN ENTERPRISE_INFO e ON r.ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "WHERE r.DATA_QUALITY_SCORE < #{threshold} " +
            "ORDER BY r.DATA_QUALITY_SCORE ASC, r.SUBMIT_TIME DESC")
    List<Map<String, Object>> selectLowQualityRecords(@Param("threshold") Integer threshold);

    /**
     * 获取报送记录统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalCount, " +
            "SUM(CASE WHEN SUBMIT_STATUS = 'SUBMITTED' THEN 1 ELSE 0 END) as submittedCount, " +
            "SUM(CASE WHEN SUBMIT_STATUS = 'APPROVED' THEN 1 ELSE 0 END) as approvedCount, " +
            "SUM(CASE WHEN SUBMIT_STATUS = 'REJECTED' THEN 1 ELSE 0 END) as rejectedCount, " +
            "AVG(DATA_QUALITY_SCORE) as avgQualityScore " +
            "FROM DATA_SUBMIT_RECORD")
    Map<String, Object> selectRecordStatistics();

    /**
     * 根据报送状态获取统计分布
     *
     * @return 状态分布统计
     */
    @Select("SELECT SUBMIT_STATUS as submitStatus, COUNT(*) as count FROM DATA_SUBMIT_RECORD GROUP BY SUBMIT_STATUS ORDER BY count DESC")
    List<Map<String, Object>> selectStatusDistribution();

    /**
     * 根据数据质量评分获取统计分布
     *
     * @return 质量分布统计
     */
    @Select("SELECT " +
            "CASE " +
            "WHEN DATA_QUALITY_SCORE >= 90 THEN 'EXCELLENT' " +
            "WHEN DATA_QUALITY_SCORE >= 80 THEN 'GOOD' " +
            "WHEN DATA_QUALITY_SCORE >= 70 THEN 'FAIR' " +
            "WHEN DATA_QUALITY_SCORE >= 60 THEN 'POOR' " +
            "ELSE 'FAILED' " +
            "END as qualityLevel, " +
            "COUNT(*) as count " +
            "FROM DATA_SUBMIT_RECORD " +
            "WHERE DATA_QUALITY_SCORE IS NOT NULL " +
            "GROUP BY qualityLevel " +
            "ORDER BY count DESC")
    List<Map<String, Object>> selectQualityDistribution();

    /**
     * 根据企业获取报送统计
     *
     * @return 企业报送统计
     */
    @Select("SELECT e.ENTERPRISE_NAME, COUNT(*) as submitCount, " +
            "AVG(r.DATA_QUALITY_SCORE) as avgQualityScore " +
            "FROM DATA_SUBMIT_RECORD r " +
            "LEFT JOIN ENTERPRISE_INFO e ON r.ENTERPRISE_ID = e.ENTERPRISE_ID " +
            "GROUP BY r.ENTERPRISE_ID, e.ENTERPRISE_NAME " +
            "ORDER BY submitCount DESC")
    List<Map<String, Object>> selectEnterpriseSubmitStatistics();

    /**
     * 批量插入记录
     *
     * @param recordList 记录列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<DataSubmitRecord> recordList);

    /**
     * 根据条件导出记录列表
     *
     * @param query 查询条件
     * @return 记录列表
     */
    List<Map<String, Object>> selectRecordListForExport(@Param("query") DataSubmitRecordQueryVO query);

    /**
     * 更新记录状态
     *
     * @param recordId     记录ID
     * @param submitStatus 报送状态
     * @return 更新数量
     */
    @Select("UPDATE DATA_SUBMIT_RECORD SET SUBMIT_STATUS = #{submitStatus}, UPDATE_TIME = NOW() WHERE RECORD_ID = #{recordId}")
    int updateRecordStatus(@Param("recordId") String recordId, @Param("submitStatus") String submitStatus);

    /**
     * 更新审核状态
     *
     * @param recordId      记录ID
     * @param reviewStatus  审核状态
     * @param reviewComments 审核意见
     * @param reviewer      审核人
     * @return 更新数量
     */
    @Select("UPDATE DATA_SUBMIT_RECORD SET REVIEW_STATUS = #{reviewStatus}, REVIEW_COMMENTS = #{reviewComments}, " +
            "REVIEWER = #{reviewer}, REVIEW_TIME = NOW(), UPDATE_TIME = NOW() WHERE RECORD_ID = #{recordId}")
    int updateReviewStatus(@Param("recordId") String recordId, @Param("reviewStatus") String reviewStatus,
                          @Param("reviewComments") String reviewComments, @Param("reviewer") String reviewer);

    /**
     * 检查企业在指定任务中是否已有报送记录
     *
     * @param taskId       任务ID
     * @param enterpriseId 企业ID
     * @return 记录数量
     */
    @Select("SELECT COUNT(*) FROM DATA_SUBMIT_RECORD WHERE TASK_ID = #{taskId} AND ENTERPRISE_ID = #{enterpriseId}")
    Integer checkExistingRecord(@Param("taskId") String taskId, @Param("enterpriseId") String enterpriseId);
}
