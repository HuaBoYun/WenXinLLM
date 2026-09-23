package com.financial.sharing.controller;

import com.financial.sharing.service.VoucherGenerationService;
import com.financial.sharing.service.BusinessTransactionService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.BatchGenerationParam;
import com.financial.sharing.vo.result.VoucherGenerationVO;
import com.financial.sharing.vo.result.GenerationPreviewVO;
import com.financial.sharing.vo.result.VoucherGenerationProgress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 凭证生成控制器
 *
 * @author system
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "凭证生成管理")
@RestController
@RequestMapping("/voucher")
@Validated
public class VoucherGenerationController {

    @Autowired
    private VoucherGenerationService voucherGenerationService;

    @Autowired
    private BusinessTransactionService businessTransactionService;

    /**
     * 批量生成凭证
     */
    @ApiOperation("批量生成凭证")
    @PostMapping("/batch-generate")
    public MyJsonBean<VoucherGenerationVO> batchGenerateVouchers(@Valid @RequestBody BatchGenerationParam param) {
        try {
            log.info("开始批量生成凭证，参数：{}", param);

            VoucherGenerationVO result = voucherGenerationService.batchGenerateVouchers(param);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("批量生成凭证失败", e);
            return MyJsonBean.errorData("批量生成失败：" + e.getMessage());
        }
    }

    /**
     * 预览凭证生成
     */
    @ApiOperation("预览凭证生成")
    @PostMapping("/preview-generation")
    public MyJsonBean<List<GenerationPreviewVO>> previewVoucherGeneration(@RequestBody List<Long> transactionIds) {
        try {
            log.info("开始预览凭证生成，事项ID数量：{}", transactionIds.size());

            if (transactionIds == null || transactionIds.isEmpty()) {
                return MyJsonBean.errorData("请选择要预览的事项");
            }

            List<GenerationPreviewVO> result = voucherGenerationService.previewVoucherGeneration(transactionIds);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("预览凭证生成失败", e);
            return MyJsonBean.errorData("预览失败：" + e.getMessage());
        }
    }

    /**
     * 查询生成进度
     */
    @ApiOperation("查询生成进度")
    @GetMapping("/generation-progress/{batchId}")
    public MyJsonBean<VoucherGenerationProgress> getVoucherGenerationProgress(
            @ApiParam(value = "批次ID", required = true) @PathVariable @NotNull String batchId) {
        try {
            log.info("查询生成进度，批次ID：{}", batchId);

            VoucherGenerationProgress progress = voucherGenerationService.getGenerationProgress(batchId);

            return MyJsonBean.successData(progress);
        } catch (Exception e) {
            log.error("查询生成进度失败", e);
            return MyJsonBean.errorData("查询进度失败：" + e.getMessage());
        }
    }

    /**
     * 停止生成任务
     */
    @ApiOperation("停止生成任务")
    @PutMapping("/stop-task/{taskId}")
    public MyJsonBean<String> stopGenerationTask(
            @ApiParam(value = "任务ID", required = true) @PathVariable @NotNull String taskId) {
        try {
            log.info("停止生成任务，任务ID：{}", taskId);

            boolean success = voucherGenerationService.stopGenerationTask(taskId);
            if (success) {
                return MyJsonBean.successData("任务停止成功");
            } else {
                return MyJsonBean.errorData("任务停止失败");
            }
        } catch (Exception e) {
            log.error("停止生成任务失败", e);
            return MyJsonBean.errorData("停止任务失败：" + e.getMessage());
        }
    }

    /**
     * 删除生成任务
     */
    @ApiOperation("删除生成任务")
    @DeleteMapping("/delete-task/{taskId}")
    public MyJsonBean<String> deleteGenerationTask(
            @ApiParam(value = "任务ID", required = true) @PathVariable @NotNull String taskId) {
        try {
            log.info("删除生成任务，任务ID：{}", taskId);

            boolean success = voucherGenerationService.deleteGenerationTask(taskId);
            if (success) {
                return MyJsonBean.successData("任务删除成功");
            } else {
                return MyJsonBean.errorData("任务删除失败");
            }
        } catch (Exception e) {
            log.error("删除生成任务失败", e);
            return MyJsonBean.errorData("删除任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询任务详情
     */
    @ApiOperation("查询任务详情")
    @GetMapping("/task-detail/{taskId}")
    public MyJsonBean<Map<String, Object>> getTaskDetail(
            @ApiParam(value = "任务ID", required = true) @PathVariable @NotNull String taskId) {
        try {
            log.info("查询任务详情，任务ID：{}", taskId);

            Map<String, Object> detail = voucherGenerationService.getTaskDetail(taskId);

            return MyJsonBean.successData(detail);
        } catch (Exception e) {
            log.error("查询任务详情失败", e);
            return MyJsonBean.errorData("查询详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取统计数据
     */
    @ApiOperation("获取统计数据")
    @PostMapping("/generation-statistics")
    public MyJsonBean<Map<String, Object>> getVoucherStatistics(@RequestBody Map<String, Object> param) {
        try {
            log.info("获取统计数据，参数：{}", param);

            Map<String, Object> statistics = voucherGenerationService.getVoucherStatistics(param);

            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return MyJsonBean.errorData("获取统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取生成趋势
     */
    @ApiOperation("获取生成趋势")
    @PostMapping("/generation-trend")
    public MyJsonBean<List<Map<String, Object>>> getVoucherGenerationTrend(@RequestBody Map<String, Object> param) {
        try {
            log.info("获取生成趋势，参数：{}", param);

            List<Map<String, Object>> trend = voucherGenerationService.getVoucherGenerationTrend(param);

            return MyJsonBean.successData(trend);
        } catch (Exception e) {
            log.error("获取生成趋势失败", e);
            return MyJsonBean.errorData("获取趋势失败：" + e.getMessage());
        }
    }
}