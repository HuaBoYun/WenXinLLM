package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.DataSubmitRecord;
import com.huabo.cybermonitor.vo.DataSubmitRecordQueryVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 数据报送记录服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IDataSubmitRecordService extends IService<DataSubmitRecord> {

    /**
     * 分页查询数据报送记录列表
     *
     * @param queryVO 查询条件
     * @return 分页结果
     */
    IPage<DataSubmitRecord> getRecordList(DataSubmitRecordQueryVO queryVO);

    /**
     * 根据记录ID获取记录详情
     *
     * @param recordId 记录ID
     * @return 记录详情
     */
    DataSubmitRecord getRecordDetail(String recordId);

    /**
     * 新增数据报送记录
     *
     * @param record 记录信息
     * @return 是否成功
     */
    boolean addRecord(DataSubmitRecord record);

    /**
     * 更新数据报送记录
     *
     * @param record 记录信息
     * @return 是否成功
     */
    boolean updateRecord(DataSubmitRecord record);

    /**
     * 删除数据报送记录
     *
     * @param recordId 记录ID
     * @return 是否成功
     */
    boolean deleteRecord(String recordId);

    /**
     * 批量删除数据报送记录
     *
     * @param recordIds 记录ID列表
     * @return 是否成功
     */
    boolean batchDeleteRecord(List<String> recordIds);

    /**
     * 提交记录
     *
     * @param recordId 记录ID
     * @return 是否成功
     */
    boolean submitRecord(String recordId);

    /**
     * 审核记录
     *
     * @param recordId     记录ID
     * @param reviewStatus 审核状态
     * @param reviewRemark 审核备注
     * @return 是否成功
     */
    boolean reviewRecord(String recordId, String reviewStatus, String reviewRemark);

    /**
     * 批量审核记录
     *
     * @param recordIds    记录ID列表
     * @param reviewStatus 审核状态
     * @param reviewRemark 审核备注
     * @return 是否成功
     */
    boolean batchReviewRecord(List<String> recordIds, String reviewStatus, String reviewRemark);

    /**
     * 根据任务ID查询记录列表
     *
     * @param taskId 任务ID
     * @return 记录列表
     */
    List<DataSubmitRecord> getRecordsByTaskId(String taskId);

    /**
     * 根据企业ID查询记录列表
     *
     * @param enterpriseId 企业ID
     * @return 记录列表
     */
    List<DataSubmitRecord> getRecordsByEnterpriseId(String enterpriseId);

    /**
     * 根据提交状态查询记录列表
     *
     * @param submitStatus 提交状态
     * @return 记录列表
     */
    List<DataSubmitRecord> getRecordsBySubmitStatus(String submitStatus);

    /**
     * 根据审核状态查询记录列表
     *
     * @param reviewStatus 审核状态
     * @return 记录列表
     */
    List<DataSubmitRecord> getRecordsByReviewStatus(String reviewStatus);

    /**
     * 查询待审核的记录列表
     *
     * @return 待审核记录列表
     */
    List<DataSubmitRecord> getPendingReviewRecords();

    /**
     * 查询数据质量评分低于阈值的记录
     *
     * @param threshold 质量评分阈值
     * @return 低质量记录列表
     */
    List<DataSubmitRecord> getLowQualityRecords(Integer threshold);

    /**
     * 获取记录统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getRecordStatistics();

    /**
     * 获取提交状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> getSubmitStatusDistribution();

    /**
     * 获取审核状态分布统计
     *
     * @return 状态分布统计
     */
    List<Map<String, Object>> getReviewStatusDistribution();

    /**
     * 获取数据质量分布统计
     *
     * @return 质量分布统计
     */
    List<Map<String, Object>> getQualityDistribution();

    /**
     * 根据任务ID获取记录统计
     *
     * @param taskId 任务ID
     * @return 记录统计
     */
    Map<String, Object> getRecordStatisticsByTaskId(String taskId);

    /**
     * 根据企业ID获取记录统计
     *
     * @param enterpriseId 企业ID
     * @return 记录统计
     */
    Map<String, Object> getRecordStatisticsByEnterpriseId(String enterpriseId);

    /**
     * 导出记录列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportRecordList(DataSubmitRecordQueryVO queryVO, HttpServletResponse response);

    /**
     * 下载记录导入模板
     *
     * @param response HTTP响应
     */
    void downloadRecordTemplate(HttpServletResponse response);

    /**
     * 批量导入记录
     *
     * @param file 导入文件
     * @return 导入结果
     */
    Map<String, Object> importRecordList(MultipartFile file);

    /**
     * 计算数据质量评分
     *
     * @param recordId 记录ID
     * @return 质量评分
     */
    Integer calculateQualityScore(String recordId);

    /**
     * 批量计算数据质量评分
     *
     * @param recordIds 记录ID列表
     * @return 是否成功
     */
    boolean batchCalculateQualityScore(List<String> recordIds);

    /**
     * 获取提交状态标签
     *
     * @param submitStatus 提交状态
     * @return 状态标签
     */
    String getSubmitStatusLabel(String submitStatus);

    /**
     * 获取审核状态标签
     *
     * @param reviewStatus 审核状态
     * @return 状态标签
     */
    String getReviewStatusLabel(String reviewStatus);

    /**
     * 获取质量等级标签
     *
     * @param qualityScore 质量评分
     * @return 质量等级标签
     */
    String getQualityLevelLabel(Integer qualityScore);

    /**
     * 验证记录数据完整性
     *
     * @param record 记录信息
     * @return 验证结果
     */
    Map<String, Object> validateRecordData(DataSubmitRecord record);

    /**
     * 获取记录的文件信息
     *
     * @param recordId 记录ID
     * @return 文件信息列表
     */
    List<Map<String, Object>> getRecordFiles(String recordId);

    /**
     * 上传记录文件
     *
     * @param recordId 记录ID
     * @param file     文件
     * @return 上传结果
     */
    Map<String, Object> uploadRecordFile(String recordId, MultipartFile file);

    /**
     * 删除记录文件
     *
     * @param recordId 记录ID
     * @param fileName 文件名
     * @return 是否成功
     */
    boolean deleteRecordFile(String recordId, String fileName);

    /**
     * 下载记录文件
     *
     * @param recordId 记录ID
     * @param fileName 文件名
     * @param response HTTP响应
     */
    void downloadRecordFile(String recordId, String fileName, HttpServletResponse response);
}
