package com.huabo.cybermonitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.DataSubmitRecord;
import com.huabo.cybermonitor.service.IDataSubmitRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.DataSubmitRecordQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 数据报送记录管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="数据报送记录管理",description="数据报送记录管理")
@RestController
@RequestMapping("/v1/data/submit/record")
public class DataSubmitRecordController {

	private static final Logger log = LoggerFactory.getLogger(DataSubmitRecordController.class);

    @Autowired
    private IDataSubmitRecordService recordService;

    @Operation(summary = "分页查询数据报送记录列表")
    @PostMapping("/list")
    public R<PageResult<DataSubmitRecord>> getRecordList(@RequestBody DataSubmitRecordQueryVO queryVO) {
        try {
            IPage<DataSubmitRecord> page = recordService.getRecordList(queryVO);
            PageResult<DataSubmitRecord> pageResult = new PageResult<>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());
            
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询数据报送记录列表失败", e);
            return R.fail("查询数据报送记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取数据报送记录详情")
    @PostMapping("/detail")
    public R<DataSubmitRecord> getRecordDetail(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            DataSubmitRecord record = recordService.getRecordDetail(recordId);
            return R.success(record);
        } catch (Exception e) {
            log.error("获取数据报送记录详情失败", e);
            return R.fail("获取数据报送记录详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增数据报送记录")
    @PostMapping("/add")
    public R<String> addRecord(@RequestBody DataSubmitRecord record) {
        try {
            boolean success = recordService.addRecord(record);
            if (success) {
                return R.success("新增数据报送记录成功");
            } else {
                return R.fail("新增数据报送记录失败");
            }
        } catch (Exception e) {
            log.error("新增数据报送记录失败", e);
            return R.fail("新增数据报送记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新数据报送记录")
    @PostMapping("/update")
    public R<String> updateRecord(@RequestBody DataSubmitRecord record) {
        try {
            if (record.getRecordId() == null || record.getRecordId().trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            boolean success = recordService.updateRecord(record);
            if (success) {
                return R.success("更新数据报送记录成功");
            } else {
                return R.fail("更新数据报送记录失败");
            }
        } catch (Exception e) {
            log.error("更新数据报送记录失败", e);
            return R.fail("更新数据报送记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除数据报送记录")
    @PostMapping("/delete")
    public R<String> deleteRecord(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            boolean success = recordService.deleteRecord(recordId);
            if (success) {
                return R.success("删除数据报送记录成功");
            } else {
                return R.fail("删除数据报送记录失败");
            }
        } catch (Exception e) {
            log.error("删除数据报送记录失败", e);
            return R.fail("删除数据报送记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除数据报送记录")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteRecord(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> recordIds = params.get("recordIds");
            if (recordIds == null || recordIds.isEmpty()) {
                return R.fail("记录ID列表不能为空");
            }
            
            boolean success = recordService.batchDeleteRecord(recordIds);
            if (success) {
                return R.success("批量删除数据报送记录成功");
            } else {
                return R.fail("批量删除数据报送记录失败");
            }
        } catch (Exception e) {
            log.error("批量删除数据报送记录失败", e);
            return R.fail("批量删除数据报送记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "提交记录")
    @PostMapping("/submit")
    public R<String> submitRecord(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            boolean success = recordService.submitRecord(recordId);
            if (success) {
                return R.success("提交记录成功");
            } else {
                return R.fail("提交记录失败");
            }
        } catch (Exception e) {
            log.error("提交记录失败", e);
            return R.fail("提交记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "审核记录")
    @PostMapping("/review")
    public R<String> reviewRecord(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            String reviewStatus = params.get("reviewStatus");
            String reviewRemark = params.get("reviewRemark");
            
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            if (reviewStatus == null || reviewStatus.trim().isEmpty()) {
                return R.fail("审核状态不能为空");
            }
            
            boolean success = recordService.reviewRecord(recordId, reviewStatus, reviewRemark);
            if (success) {
                return R.success("审核记录成功");
            } else {
                return R.fail("审核记录失败");
            }
        } catch (Exception e) {
            log.error("审核记录失败", e);
            return R.fail("审核记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量审核记录")
    @PostMapping("/batch-review")
    public R<String> batchReviewRecord(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> recordIds = (List<String>) params.get("recordIds");
            String reviewStatus = (String) params.get("reviewStatus");
            String reviewRemark = (String) params.get("reviewRemark");
            
            if (recordIds == null || recordIds.isEmpty()) {
                return R.fail("记录ID列表不能为空");
            }
            if (reviewStatus == null || reviewStatus.trim().isEmpty()) {
                return R.fail("审核状态不能为空");
            }
            
            boolean success = recordService.batchReviewRecord(recordIds, reviewStatus, reviewRemark);
            if (success) {
                return R.success("批量审核记录成功");
            } else {
                return R.fail("批量审核记录失败");
            }
        } catch (Exception e) {
            log.error("批量审核记录失败", e);
            return R.fail("批量审核记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据任务ID查询记录列表")
    @PostMapping("/list-by-task")
    public R<List<DataSubmitRecord>> getRecordsByTaskId(@RequestBody Map<String, String> params) {
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            List<DataSubmitRecord> records = recordService.getRecordsByTaskId(taskId);
            return R.success(records);
        } catch (Exception e) {
            log.error("根据任务ID查询记录列表失败", e);
            return R.fail("根据任务ID查询记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据企业ID查询记录列表")
    @PostMapping("/list-by-enterprise")
    public R<List<DataSubmitRecord>> getRecordsByEnterpriseId(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            List<DataSubmitRecord> records = recordService.getRecordsByEnterpriseId(enterpriseId);
            return R.success(records);
        } catch (Exception e) {
            log.error("根据企业ID查询记录列表失败", e);
            return R.fail("根据企业ID查询记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据提交状态查询记录列表")
    @PostMapping("/list-by-submit-status")
    public R<List<DataSubmitRecord>> getRecordsBySubmitStatus(@RequestBody Map<String, String> params) {
        try {
            String submitStatus = params.get("submitStatus");
            if (submitStatus == null || submitStatus.trim().isEmpty()) {
                return R.fail("提交状态不能为空");
            }
            
            List<DataSubmitRecord> records = recordService.getRecordsBySubmitStatus(submitStatus);
            return R.success(records);
        } catch (Exception e) {
            log.error("根据提交状态查询记录列表失败", e);
            return R.fail("根据提交状态查询记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据审核状态查询记录列表")
    @PostMapping("/list-by-review-status")
    public R<List<DataSubmitRecord>> getRecordsByReviewStatus(@RequestBody Map<String, String> params) {
        try {
            String reviewStatus = params.get("reviewStatus");
            if (reviewStatus == null || reviewStatus.trim().isEmpty()) {
                return R.fail("审核状态不能为空");
            }
            
            List<DataSubmitRecord> records = recordService.getRecordsByReviewStatus(reviewStatus);
            return R.success(records);
        } catch (Exception e) {
            log.error("根据审核状态查询记录列表失败", e);
            return R.fail("根据审核状态查询记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询待审核的记录列表")
    @PostMapping("/pending-review")
    public R<List<DataSubmitRecord>> getPendingReviewRecords() {
        try {
            List<DataSubmitRecord> records = recordService.getPendingReviewRecords();
            return R.success(records);
        } catch (Exception e) {
            log.error("查询待审核的记录列表失败", e);
            return R.fail("查询待审核的记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询数据质量评分低于阈值的记录")
    @PostMapping("/low-quality")
    public R<List<DataSubmitRecord>> getLowQualityRecords(@RequestBody Map<String, Integer> params) {
        try {
            Integer threshold = params.get("threshold");
            if (threshold == null || threshold < 0 || threshold > 100) {
                threshold = 60; // 默认阈值
            }
            
            List<DataSubmitRecord> records = recordService.getLowQualityRecords(threshold);
            return R.success(records);
        } catch (Exception e) {
            log.error("查询低质量记录失败", e);
            return R.fail("查询低质量记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取记录统计信息")
    @PostMapping("/statistics/overview")
    public R<Map<String, Object>> getRecordStatistics() {
        try {
            Map<String, Object> statistics = recordService.getRecordStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取记录统计信息失败", e);
            return R.fail("获取记录统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取提交状态分布统计")
    @PostMapping("/statistics/submit-status-distribution")
    public R<List<Map<String, Object>>> getSubmitStatusDistribution() {
        try {
            List<Map<String, Object>> distribution = recordService.getSubmitStatusDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取提交状态分布统计失败", e);
            return R.fail("获取提交状态分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取审核状态分布统计")
    @PostMapping("/statistics/review-status-distribution")
    public R<List<Map<String, Object>>> getReviewStatusDistribution() {
        try {
            List<Map<String, Object>> distribution = recordService.getReviewStatusDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取审核状态分布统计失败", e);
            return R.fail("获取审核状态分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取数据质量分布统计")
    @PostMapping("/statistics/quality-distribution")
    public R<List<Map<String, Object>>> getQualityDistribution() {
        try {
            List<Map<String, Object>> distribution = recordService.getQualityDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取数据质量分布统计失败", e);
            return R.fail("获取数据质量分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据任务ID获取记录统计")
    @PostMapping("/statistics/by-task")
    public R<Map<String, Object>> getRecordStatisticsByTaskId(@RequestBody Map<String, String> params) {
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            Map<String, Object> statistics = recordService.getRecordStatisticsByTaskId(taskId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("根据任务ID获取记录统计失败", e);
            return R.fail("根据任务ID获取记录统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据企业ID获取记录统计")
    @PostMapping("/statistics/by-enterprise")
    public R<Map<String, Object>> getRecordStatisticsByEnterpriseId(@RequestBody Map<String, String> params) {
        try {
            String enterpriseId = params.get("enterpriseId");
            if (enterpriseId == null || enterpriseId.trim().isEmpty()) {
                return R.fail("企业ID不能为空");
            }
            
            Map<String, Object> statistics = recordService.getRecordStatisticsByEnterpriseId(enterpriseId);
            return R.success(statistics);
        } catch (Exception e) {
            log.error("根据企业ID获取记录统计失败", e);
            return R.fail("根据企业ID获取记录统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "计算数据质量评分")
    @PostMapping("/calculate-quality-score")
    public R<Map<String, Object>> calculateQualityScore(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            Integer score = recordService.calculateQualityScore(recordId);

            Map<String, Object> result = new HashMap<>();
            result.put("recordId", recordId);
            result.put("qualityScore", score);
            result.put("qualityLevel", recordService.getQualityLevelLabel(score));
            
            return R.success(result);
        } catch (Exception e) {
            log.error("计算数据质量评分失败", e);
            return R.fail("计算数据质量评分失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量计算数据质量评分")
    @PostMapping("/batch-calculate-quality-score")
    public R<String> batchCalculateQualityScore(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> recordIds = params.get("recordIds");
            if (recordIds == null || recordIds.isEmpty()) {
                return R.fail("记录ID列表不能为空");
            }
            
            boolean success = recordService.batchCalculateQualityScore(recordIds);
            if (success) {
                return R.success("批量计算数据质量评分成功");
            } else {
                return R.fail("批量计算数据质量评分失败");
            }
        } catch (Exception e) {
            log.error("批量计算数据质量评分失败", e);
            return R.fail("批量计算数据质量评分失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证记录数据完整性")
    @PostMapping("/validate-data")
    public R<Map<String, Object>> validateRecordData(@RequestBody DataSubmitRecord record) {
        try {
            Map<String, Object> result = recordService.validateRecordData(record);
            return R.success(result);
        } catch (Exception e) {
            log.error("验证记录数据完整性失败", e);
            return R.fail("验证记录数据完整性失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出记录列表")
    @PostMapping("/export")
    public void exportRecordList(@RequestBody DataSubmitRecordQueryVO queryVO, HttpServletResponse response) {
        try {
            recordService.exportRecordList(queryVO, response);
        } catch (Exception e) {
            log.error("导出记录列表失败", e);
            throw new RuntimeException("导出记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "下载记录导入模板")
    @GetMapping("/template")
    public void downloadRecordTemplate(HttpServletResponse response) {
        try {
            recordService.downloadRecordTemplate(response);
        } catch (Exception e) {
            log.error("下载记录导入模板失败", e);
            throw new RuntimeException("下载记录导入模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量导入记录")
    @PostMapping("/import")
    public R<Map<String, Object>> importRecordList(
            @Parameter(description = "导入文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("导入文件不能为空");
            }
            
            Map<String, Object> result = recordService.importRecordList(file);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量导入记录失败", e);
            return R.fail("批量导入记录失败：" + e.getMessage());
        }
    }
}
