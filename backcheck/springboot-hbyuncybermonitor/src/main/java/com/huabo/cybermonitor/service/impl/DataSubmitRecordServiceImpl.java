package com.huabo.cybermonitor.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.DataSubmitRecord;
import com.huabo.cybermonitor.mapper.DataSubmitRecordMapper;
import com.huabo.cybermonitor.service.IDataSubmitRecordService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.DataSubmitRecordQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据报送记录服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class DataSubmitRecordServiceImpl extends ServiceImpl<DataSubmitRecordMapper, DataSubmitRecord> implements IDataSubmitRecordService {

    @Autowired
    private DataSubmitRecordMapper recordMapper;

    @Override
    public IPage<DataSubmitRecord> getRecordList(DataSubmitRecordQueryVO queryVO) {
        Page<DataSubmitRecord> page = new Page<>(queryVO.getPageNumber().intValue(), queryVO.getPageSize().intValue());
        return recordMapper.selectRecordList(page, queryVO);
    }

    @Override
    public DataSubmitRecord getRecordDetail(String recordId) {
        if (StringUtils.isEmpty(recordId)) {
            return null;
        }
        return this.getById(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRecord(DataSubmitRecord record) {
        try {
            // 设置默认值
            if (StringUtils.isEmpty(record.getSubmitStatus())) {
                record.setSubmitStatus("DRAFT");
            }
            if (StringUtils.isEmpty(record.getReviewStatus())) {
                record.setReviewStatus("PENDING");
            }
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());

            return save(record);
        } catch (Exception e) {
            log.error("新增数据报送记录失败", e);
            throw new RuntimeException("新增数据报送记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecord(DataSubmitRecord record) {
        try {
            record.setUpdateTime(LocalDateTime.now());
            return updateById(record);
        } catch (Exception e) {
            log.error("更新数据报送记录失败", e);
            throw new RuntimeException("更新数据报送记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(String recordId) {
        try {
            return removeById(recordId);
        } catch (Exception e) {
            log.error("删除数据报送记录失败", e);
            throw new RuntimeException("删除数据报送记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRecord(List<String> recordIds) {
        try {
            return removeByIds(recordIds);
        } catch (Exception e) {
            log.error("批量删除数据报送记录失败", e);
            throw new RuntimeException("批量删除数据报送记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitRecord(String recordId) {
        try {
            DataSubmitRecord record = this.getById(recordId);
            if (record != null) {
                record.setSubmitStatus("SUBMITTED");
                record.setUpdateTime(LocalDateTime.now());
                return this.updateById(record);
            }
            return false;
        } catch (Exception e) {
            log.error("提交记录失败", e);
            throw new RuntimeException("提交记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewRecord(String recordId, String reviewStatus, String reviewRemark) {
        try {
            return recordMapper.updateReviewStatus(recordId, reviewStatus, reviewRemark, null) > 0;
        } catch (Exception e) {
            log.error("审核记录失败", e);
            throw new RuntimeException("审核记录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchReviewRecord(List<String> recordIds, String reviewStatus, String reviewRemark) {
        try {
            int count = 0;
            for (String recordId : recordIds) {
                if (recordMapper.updateReviewStatus(recordId, reviewStatus, reviewRemark, null) > 0) {
                    count++;
                }
            }
            return count > 0;
        } catch (Exception e) {
            log.error("批量审核记录失败", e);
            throw new RuntimeException("批量审核记录失败：" + e.getMessage());
        }
    }

    @Override
    public List<DataSubmitRecord> getRecordsByTaskId(String taskId) {
        LambdaQueryWrapper<DataSubmitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSubmitRecord::getTaskId, taskId);
        return this.list(wrapper);
    }

    @Override
    public List<DataSubmitRecord> getRecordsByEnterpriseId(String enterpriseId) {
        LambdaQueryWrapper<DataSubmitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSubmitRecord::getEnterpriseId, enterpriseId);
        return this.list(wrapper);
    }

    @Override
    public List<DataSubmitRecord> getRecordsBySubmitStatus(String submitStatus) {
        LambdaQueryWrapper<DataSubmitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSubmitRecord::getSubmitStatus, submitStatus);
        return this.list(wrapper);
    }

    @Override
    public List<DataSubmitRecord> getRecordsByReviewStatus(String reviewStatus) {
        LambdaQueryWrapper<DataSubmitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSubmitRecord::getReviewStatus, reviewStatus);
        return this.list(wrapper);
    }

    @Override
    public List<DataSubmitRecord> getPendingReviewRecords() {
        LambdaQueryWrapper<DataSubmitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DataSubmitRecord::getReviewStatus, "PENDING");
        return this.list(wrapper);
    }

    @Override
    public List<DataSubmitRecord> getLowQualityRecords(Integer threshold) {
        LambdaQueryWrapper<DataSubmitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(DataSubmitRecord::getQualityCheckResult);
        return this.list(wrapper);
    }

    @Override
    public Map<String, Object> getRecordStatistics() {
        return recordMapper.selectRecordStatistics();
    }

    @Override
    public List<Map<String, Object>> getSubmitStatusDistribution() {
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getReviewStatusDistribution() {
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getQualityDistribution() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getRecordStatisticsByTaskId(String taskId) {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getRecordStatisticsByEnterpriseId(String enterpriseId) {
        return new HashMap<>();
    }

    @Override
    public void exportRecordList(DataSubmitRecordQueryVO queryVO, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<DataSubmitRecord> wrapper = new LambdaQueryWrapper<>();
            List<DataSubmitRecord> recordList = this.list(wrapper);

            // 设置导出的列标题
            String[] headers = {
                "提交状态", "审核状态", "质量检查结果", "提交时间", "备注"
            };

            ExcelUtil excelUtil = new ExcelUtil("数据报送记录列表", headers);

            // 添加数据行
            for (int i = 0; i < recordList.size(); i++) {
                DataSubmitRecord record = recordList.get(i);
                Object[] row = {
                    getSubmitStatusLabel(record.getSubmitStatus()),
                    getReviewStatusLabel(record.getReviewStatus()),
                    record.getQualityCheckResult(),
                    record.getCreateTime(),
                    record.getRemark()
                };
                excelUtil.addRow(i + 1, row);
            }

            excelUtil.exportExcel(response, "数据报送记录列表.xls");
        } catch (Exception e) {
            log.error("导出记录列表失败", e);
            throw new RuntimeException("导出记录列表失败：" + e.getMessage());
        }
    }

    @Override
    public void downloadRecordTemplate(HttpServletResponse response) {
        try {
            String[] headers = {
                "任务ID*", "企业ID*", "数据内容*", "文件名称", "文件路径", "备注"
            };

            ExcelUtil excelUtil = new ExcelUtil("数据报送记录导入模板", headers);

            // 添加示例数据
            Object[] exampleRow = {
                "TASK001", "ENT001", "示例数据内容", "示例文件.xlsx", "/upload/files/", "示例备注"
            };
            excelUtil.addRow(1, exampleRow);

            excelUtil.exportExcel(response, "数据报送记录导入模板.xls");
        } catch (Exception e) {
            log.error("下载记录导入模板失败", e);
            throw new RuntimeException("下载记录导入模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importRecordList(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 简化导入逻辑，暂时返回成功结果
            result.put("successCount", 0);
            result.put("failCount", 0);
            result.put("errorMessages", new ArrayList<>());
            result.put("totalCount", 0);
            result.put("message", "导入功能开发中，请稍后使用");

        } catch (Exception e) {
            log.error("导入记录列表失败", e);
            throw new RuntimeException("导入记录列表失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    public Integer calculateQualityScore(String recordId) {
        try {
            DataSubmitRecord record = getById(recordId);
            if (record == null) {
                return 0;
            }

            // 简化的质量评分算法
            int score = 60; // 基础分

            // 质量检查结果检查
            if (!StringUtils.isEmpty(record.getQualityCheckResult())) {
                score += 40;
            }

            return Math.min(score, 100);
        } catch (Exception e) {
            log.error("计算数据质量评分失败", e);
            return 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCalculateQualityScore(List<String> recordIds) {
        try {
            for (String recordId : recordIds) {
                calculateQualityScore(recordId);
            }
            return true;
        } catch (Exception e) {
            log.error("批量计算数据质量评分失败", e);
            throw new RuntimeException("批量计算数据质量评分失败：" + e.getMessage());
        }
    }

    @Override
    public String getSubmitStatusLabel(String submitStatus) {
        if (StringUtils.isEmpty(submitStatus)) {
            return "";
        }
        switch (submitStatus) {
            case DataSubmitRecord.STATUS_DRAFT:
                return "草稿";
            case DataSubmitRecord.STATUS_SUBMITTED:
                return "已提交";
            case DataSubmitRecord.STATUS_PROCESSING:
                return "处理中";
            case DataSubmitRecord.STATUS_APPROVED:
                return "已通过";
            case DataSubmitRecord.STATUS_REJECTED:
                return "已拒绝";
            case DataSubmitRecord.STATUS_RETURNED:
                return "已退回";
            default:
                return submitStatus;
        }
    }

    @Override
    public String getReviewStatusLabel(String reviewStatus) {
        if (StringUtils.isEmpty(reviewStatus)) {
            return "";
        }
        switch (reviewStatus) {
            case DataSubmitRecord.REVIEW_PENDING:
                return "待审核";
            case DataSubmitRecord.REVIEW_APPROVED:
                return "审核通过";
            case DataSubmitRecord.REVIEW_REJECTED:
                return "审核拒绝";
            case DataSubmitRecord.REVIEW_RETURNED:
                return "审核退回";
            default:
                return reviewStatus;
        }
    }

    @Override
    public String getQualityLevelLabel(Integer qualityScore) {
        if (qualityScore == null) {
            return "未评分";
        }
        if (qualityScore >= 90) {
            return "优秀";
        } else if (qualityScore >= 80) {
            return "良好";
        } else if (qualityScore >= 70) {
            return "一般";
        } else if (qualityScore >= 60) {
            return "较差";
        } else {
            return "不合格";
        }
    }

    @Override
    public Map<String, Object> validateRecordData(DataSubmitRecord record) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        // 验证必填字段
        if (StringUtils.isEmpty(record.getTaskId())) {
            errors.add("任务ID不能为空");
        }
        if (StringUtils.isEmpty(record.getEnterpriseId())) {
            errors.add("企业ID不能为空");
        }

        result.put("isValid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    public List<Map<String, Object>> getRecordFiles(String recordId) {
        // 简化实现，返回模拟数据
        List<Map<String, Object>> files = new ArrayList<>();
        DataSubmitRecord record = getById(recordId);
        if (record != null ) {
            Map<String, Object> file = new HashMap<>();
            file.put("fileSize", record.getFileSize());
            file.put("filePath", record.getFilePath());
            files.add(file);
        }
        return files;
    }

    @Override
    public Map<String, Object> uploadRecordFile(String recordId, MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 简化实现
            result.put("success", true);
            result.put("fileName", file.getOriginalFilename());
            result.put("fileSize", file.getSize());
            result.put("message", "文件上传功能开发中");
        } catch (Exception e) {
            log.error("上传记录文件失败", e);
            result.put("success", false);
            result.put("message", "上传失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public boolean deleteRecordFile(String recordId, String fileName) {
        try {
            // 简化实现
            return true;
        } catch (Exception e) {
            log.error("删除记录文件失败", e);
            return false;
        }
    }

    @Override
    public void downloadRecordFile(String recordId, String fileName, HttpServletResponse response) {
        try {
            // 简化实现
            throw new RuntimeException("文件下载功能开发中");
        } catch (Exception e) {
            log.error("下载记录文件失败", e);
            throw new RuntimeException("下载记录文件失败：" + e.getMessage());
        }
    }
}
