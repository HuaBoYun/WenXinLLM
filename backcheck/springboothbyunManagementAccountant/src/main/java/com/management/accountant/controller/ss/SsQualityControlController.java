package com.management.accountant.controller.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ss.SsQualityControl;
import com.management.accountant.service.ss.SsQualityControlService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 质量管控控制器
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ss/quality-control")
@Api(tags = "质量管控管理")
public class SsQualityControlController {

    @Autowired
    private SsQualityControlService qualityControlService;

    /**
     * 分页查询质量管控
     */
    @GetMapping("/page")
    @ApiOperation("分页查询质量管控")
    public MyJsonBean getQualityControlPage(@RequestParam Map<String, Object> params) {
        try {
            IPage<SsQualityControl> page = qualityControlService.getQualityControlPage(params);
            return MyJsonBean.success(page);
        } catch (Exception e) {
            log.error("分页查询质量管控失败", e);
            return MyJsonBean.error("分页查询质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询质量管控
     */
    @GetMapping("/{qualityId}")
    @ApiOperation("根据ID查询质量管控")
    public MyJsonBean getQualityControlById(@PathVariable Long qualityId) {
        try {
            SsQualityControl qualityControl = qualityControlService.getQualityControlById(qualityId);
            return MyJsonBean.success(qualityControl);
        } catch (Exception e) {
            log.error("根据ID查询质量管控失败", e);
            return MyJsonBean.error("根据ID查询质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 根据编码查询质量管控
     */
    @GetMapping("/code/{qualityCode}")
    @ApiOperation("根据编码查询质量管控")
    public MyJsonBean getQualityControlByCode(@PathVariable String qualityCode, 
                                             @RequestParam(defaultValue = "1") Long tenantId) {
        try {
            SsQualityControl qualityControl = qualityControlService.getQualityControlByCode(qualityCode, tenantId);
            return MyJsonBean.success(qualityControl);
        } catch (Exception e) {
            log.error("根据编码查询质量管控失败", e);
            return MyJsonBean.error("根据编码查询质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 创建质量管控
     */
    @PostMapping
    @ApiOperation("创建质量管控")
    public MyJsonBean createQualityControl(@RequestBody SsQualityControl qualityControl) {
        try {
            boolean result = qualityControlService.createQualityControl(qualityControl);
            return result ? MyJsonBean.success("创建成功") : MyJsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建质量管控失败", e);
            return MyJsonBean.error("创建质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 更新质量管控
     */
    @PutMapping("/{qualityId}")
    @ApiOperation("更新质量管控")
    public MyJsonBean updateQualityControl(@PathVariable Long qualityId, 
                                          @RequestBody SsQualityControl qualityControl) {
        try {
            boolean result = qualityControlService.updateQualityControl(qualityId, qualityControl);
            return result ? MyJsonBean.success("更新成功") : MyJsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新质量管控失败", e);
            return MyJsonBean.error("更新质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 删除质量管控
     */
    @DeleteMapping("/{qualityId}")
    @ApiOperation("删除质量管控")
    public MyJsonBean deleteQualityControl(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.deleteQualityControl(qualityId);
            return result ? MyJsonBean.success("删除成功") : MyJsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除质量管控失败", e);
            return MyJsonBean.error("删除质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除质量管控
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除质量管控")
    public MyJsonBean batchDeleteQualityControl(@RequestBody List<Long> qualityIds) {
        try {
            boolean result = qualityControlService.batchDeleteQualityControl(qualityIds);
            return result ? MyJsonBean.success("批量删除成功") : MyJsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除质量管控失败", e);
            return MyJsonBean.error("批量删除质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 启用质量管控
     */
    @PutMapping("/{qualityId}/enable")
    @ApiOperation("启用质量管控")
    public MyJsonBean enableQualityControl(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.enableQualityControl(qualityId);
            return result ? MyJsonBean.success("启用成功") : MyJsonBean.error("启用失败");
        } catch (Exception e) {
            log.error("启用质量管控失败", e);
            return MyJsonBean.error("启用质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 禁用质量管控
     */
    @PutMapping("/{qualityId}/disable")
    @ApiOperation("禁用质量管控")
    public MyJsonBean disableQualityControl(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.disableQualityControl(qualityId);
            return result ? MyJsonBean.success("禁用成功") : MyJsonBean.error("禁用失败");
        } catch (Exception e) {
            log.error("禁用质量管控失败", e);
            return MyJsonBean.error("禁用质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 批量启用质量管控
     */
    @PutMapping("/batch/enable")
    @ApiOperation("批量启用质量管控")
    public MyJsonBean batchEnableQualityControl(@RequestBody List<Long> qualityIds) {
        try {
            boolean result = qualityControlService.batchEnableQualityControl(qualityIds);
            return result ? MyJsonBean.success("批量启用成功") : MyJsonBean.error("批量启用失败");
        } catch (Exception e) {
            log.error("批量启用质量管控失败", e);
            return MyJsonBean.error("批量启用质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 批量禁用质量管控
     */
    @PutMapping("/batch/disable")
    @ApiOperation("批量禁用质量管控")
    public MyJsonBean batchDisableQualityControl(@RequestBody List<Long> qualityIds) {
        try {
            boolean result = qualityControlService.batchDisableQualityControl(qualityIds);
            return result ? MyJsonBean.success("批量禁用成功") : MyJsonBean.error("批量禁用失败");
        } catch (Exception e) {
            log.error("批量禁用质量管控失败", e);
            return MyJsonBean.error("批量禁用质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 开始检测
     */
    @PutMapping("/{qualityId}/start-detection")
    @ApiOperation("开始检测")
    public MyJsonBean startDetection(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.startDetection(qualityId);
            return result ? MyJsonBean.success("开始检测成功") : MyJsonBean.error("开始检测失败");
        } catch (Exception e) {
            log.error("开始检测失败", e);
            return MyJsonBean.error("开始检测失败: " + e.getMessage());
        }
    }

    /**
     * 停止检测
     */
    @PutMapping("/{qualityId}/stop-detection")
    @ApiOperation("停止检测")
    public MyJsonBean stopDetection(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.stopDetection(qualityId);
            return result ? MyJsonBean.success("停止检测成功") : MyJsonBean.error("停止检测失败");
        } catch (Exception e) {
            log.error("停止检测失败", e);
            return MyJsonBean.error("停止检测失败: " + e.getMessage());
        }
    }

    /**
     * 暂停检测
     */
    @PutMapping("/{qualityId}/pause-detection")
    @ApiOperation("暂停检测")
    public MyJsonBean pauseDetection(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.pauseDetection(qualityId);
            return result ? MyJsonBean.success("暂停检测成功") : MyJsonBean.error("暂停检测失败");
        } catch (Exception e) {
            log.error("暂停检测失败", e);
            return MyJsonBean.error("暂停检测失败: " + e.getMessage());
        }
    }

    /**
     * 恢复检测
     */
    @PutMapping("/{qualityId}/resume-detection")
    @ApiOperation("恢复检测")
    public MyJsonBean resumeDetection(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.resumeDetection(qualityId);
            return result ? MyJsonBean.success("恢复检测成功") : MyJsonBean.error("恢复检测失败");
        } catch (Exception e) {
            log.error("恢复检测失败", e);
            return MyJsonBean.error("恢复检测失败: " + e.getMessage());
        }
    }

    /**
     * 完成检测
     */
    @PutMapping("/{qualityId}/complete-detection")
    @ApiOperation("完成检测")
    public MyJsonBean completeDetection(@PathVariable Long qualityId,
                                       @RequestParam String detectionResult,
                                       @RequestParam BigDecimal qualityScore) {
        try {
            boolean result = qualityControlService.completeDetection(qualityId, detectionResult, qualityScore);
            return result ? MyJsonBean.success("完成检测成功") : MyJsonBean.error("完成检测失败");
        } catch (Exception e) {
            log.error("完成检测失败", e);
            return MyJsonBean.error("完成检测失败: " + e.getMessage());
        }
    }

    /**
     * 批量开始检测
     */
    @PutMapping("/batch/start-detection")
    @ApiOperation("批量开始检测")
    public MyJsonBean batchStartDetection(@RequestBody List<Long> qualityIds) {
        try {
            boolean result = qualityControlService.batchStartDetection(qualityIds);
            return result ? MyJsonBean.success("批量开始检测成功") : MyJsonBean.error("批量开始检测失败");
        } catch (Exception e) {
            log.error("批量开始检测失败", e);
            return MyJsonBean.error("批量开始检测失败: " + e.getMessage());
        }
    }

    /**
     * 批量停止检测
     */
    @PutMapping("/batch/stop-detection")
    @ApiOperation("批量停止检测")
    public MyJsonBean batchStopDetection(@RequestBody List<Long> qualityIds) {
        try {
            boolean result = qualityControlService.batchStopDetection(qualityIds);
            return result ? MyJsonBean.success("批量停止检测成功") : MyJsonBean.error("批量停止检测失败");
        } catch (Exception e) {
            log.error("批量停止检测失败", e);
            return MyJsonBean.error("批量停止检测失败: " + e.getMessage());
        }
    }

    /**
     * 批量暂停检测
     */
    @PutMapping("/batch/pause-detection")
    @ApiOperation("批量暂停检测")
    public MyJsonBean batchPauseDetection(@RequestBody List<Long> qualityIds) {
        try {
            boolean result = qualityControlService.batchPauseDetection(qualityIds);
            return result ? MyJsonBean.success("批量暂停检测成功") : MyJsonBean.error("批量暂停检测失败");
        } catch (Exception e) {
            log.error("批量暂停检测失败", e);
            return MyJsonBean.error("批量暂停检测失败: " + e.getMessage());
        }
    }

    /**
     * 批量恢复检测
     */
    @PutMapping("/batch/resume-detection")
    @ApiOperation("批量恢复检测")
    public MyJsonBean batchResumeDetection(@RequestBody List<Long> qualityIds) {
        try {
            boolean result = qualityControlService.batchResumeDetection(qualityIds);
            return result ? MyJsonBean.success("批量恢复检测成功") : MyJsonBean.error("批量恢复检测失败");
        } catch (Exception e) {
            log.error("批量恢复检测失败", e);
            return MyJsonBean.error("批量恢复检测失败: " + e.getMessage());
        }
    }

    /**
     * 立即执行检测
     */
    @PutMapping("/{qualityId}/execute-immediately")
    @ApiOperation("立即执行检测")
    public MyJsonBean executeDetectionImmediately(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.executeDetectionImmediately(qualityId);
            return result ? MyJsonBean.success("立即执行检测成功") : MyJsonBean.error("立即执行检测失败");
        } catch (Exception e) {
            log.error("立即执行检测失败", e);
            return MyJsonBean.error("立即执行检测失败: " + e.getMessage());
        }
    }

    /**
     * 重新检测
     */
    @PutMapping("/{qualityId}/re-detection")
    @ApiOperation("重新检测")
    public MyJsonBean reDetection(@PathVariable Long qualityId) {
        try {
            boolean result = qualityControlService.reDetection(qualityId);
            return result ? MyJsonBean.success("重新检测成功") : MyJsonBean.error("重新检测失败");
        } catch (Exception e) {
            log.error("重新检测失败", e);
            return MyJsonBean.error("重新检测失败: " + e.getMessage());
        }
    }

    // 查询接口
    /**
     * 查询待检测的质量管控
     */
    @GetMapping("/pending-detection")
    @ApiOperation("查询待检测的质量管控")
    public MyJsonBean getPendingDetection(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsQualityControl> list = qualityControlService.getPendingDetection(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询待检测的质量管控失败", e);
            return MyJsonBean.error("查询待检测的质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 查询正在检测的质量管控
     */
    @GetMapping("/in-detection")
    @ApiOperation("查询正在检测的质量管控")
    public MyJsonBean getInDetection(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsQualityControl> list = qualityControlService.getInDetection(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询正在检测的质量管控失败", e);
            return MyJsonBean.error("查询正在检测的质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 查询已完成检测的质量管控
     */
    @GetMapping("/completed-detection")
    @ApiOperation("查询已完成检测的质量管控")
    public MyJsonBean getCompletedDetection(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsQualityControl> list = qualityControlService.getCompletedDetection(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询已完成检测的质量管控失败", e);
            return MyJsonBean.error("查询已完成检测的质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 查询检测失败的质量管控
     */
    @GetMapping("/failed-detection")
    @ApiOperation("查询检测失败的质量管控")
    public MyJsonBean getFailedDetection(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsQualityControl> list = qualityControlService.getFailedDetection(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询检测失败的质量管控失败", e);
            return MyJsonBean.error("查询检测失败的质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 查询需要改进的质量管控
     */
    @GetMapping("/need-improvement")
    @ApiOperation("查询需要改进的质量管控")
    public MyJsonBean getNeedImprovement(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsQualityControl> list = qualityControlService.getNeedImprovement(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询需要改进的质量管控失败", e);
            return MyJsonBean.error("查询需要改进的质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 查询高风险质量管控
     */
    @GetMapping("/high-risk")
    @ApiOperation("查询高风险质量管控")
    public MyJsonBean getHighRisk(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsQualityControl> list = qualityControlService.getHighRisk(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询高风险质量管控失败", e);
            return MyJsonBean.error("查询高风险质量管控失败: " + e.getMessage());
        }
    }

    /**
     * 查询超期未检测的质量管控
     */
    @GetMapping("/overdue-detection")
    @ApiOperation("查询超期未检测的质量管控")
    public MyJsonBean getOverdueDetection(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<SsQualityControl> list = qualityControlService.getOverdueDetection(tenantId);
            return MyJsonBean.success(list);
        } catch (Exception e) {
            log.error("查询超期未检测的质量管控失败", e);
            return MyJsonBean.error("查询超期未检测的质量管控失败: " + e.getMessage());
        }
    }

    // 统计接口
    /**
     * 统计质量管控数据
     */
    @GetMapping("/statistics")
    @ApiOperation("统计质量管控数据")
    public MyJsonBean getQualityControlStatistics(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            Map<String, Object> statistics = qualityControlService.getQualityControlStatistics(tenantId);
            return MyJsonBean.success(statistics);
        } catch (Exception e) {
            log.error("统计质量管控数据失败", e);
            return MyJsonBean.error("统计质量管控数据失败: " + e.getMessage());
        }
    }

    /**
     * 统计质量管控状态分布
     */
    @GetMapping("/statistics/status-distribution")
    @ApiOperation("统计质量管控状态分布")
    public MyJsonBean getQualityStatusDistribution(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> distribution = qualityControlService.getQualityStatusDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计质量管控状态分布失败", e);
            return MyJsonBean.error("统计质量管控状态分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计质量管控类型分布
     */
    @GetMapping("/statistics/type-distribution")
    @ApiOperation("统计质量管控类型分布")
    public MyJsonBean getQualityTypeDistribution(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> distribution = qualityControlService.getQualityTypeDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计质量管控类型分布失败", e);
            return MyJsonBean.error("统计质量管控类型分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计质量等级分布
     */
    @GetMapping("/statistics/level-distribution")
    @ApiOperation("统计质量等级分布")
    public MyJsonBean getQualityLevelDistribution(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> distribution = qualityControlService.getQualityLevelDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计质量等级分布失败", e);
            return MyJsonBean.error("统计质量等级分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计检测状态分布
     */
    @GetMapping("/statistics/detection-status-distribution")
    @ApiOperation("统计检测状态分布")
    public MyJsonBean getDetectionStatusDistribution(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> distribution = qualityControlService.getDetectionStatusDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计检测状态分布失败", e);
            return MyJsonBean.error("统计检测状态分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计风险等级分布
     */
    @GetMapping("/statistics/risk-level-distribution")
    @ApiOperation("统计风险等级分布")
    public MyJsonBean getRiskLevelDistribution(@RequestParam(defaultValue = "1") Long tenantId) {
        try {
            List<Map<String, Object>> distribution = qualityControlService.getRiskLevelDistribution(tenantId);
            return MyJsonBean.success(distribution);
        } catch (Exception e) {
            log.error("统计风险等级分布失败", e);
            return MyJsonBean.error("统计风险等级分布失败: " + e.getMessage());
        }
    }

    /**
     * 统计质量管控趋势
     */
    @GetMapping("/statistics/trend")
    @ApiOperation("统计质量管控趋势")
    public MyJsonBean getQualityControlTrend(@RequestParam Map<String, Object> params) {
        try {
            List<Map<String, Object>> trend = qualityControlService.getQualityControlTrend(params);
            return MyJsonBean.success(trend);
        } catch (Exception e) {
            log.error("统计质量管控趋势失败", e);
            return MyJsonBean.error("统计质量管控趋势失败: " + e.getMessage());
        }
    }
}
