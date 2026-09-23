package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillMaturityProcessDTO;
import com.global.treasurer.dto.BillMaturityQueryDTO;
import com.global.treasurer.service.IBillMaturityService;
import com.global.treasurer.vo.BillMaturityVO;
import com.hbfk.util.BizException;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 票据到期管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill/maturity")
@Api(tags = "票据到期管理")
public class BillMaturityController {
    private static final Logger log = LoggerFactory.getLogger(BillMaturityController.class);

    @Autowired
    private IBillMaturityService billMaturityService;

    @PostMapping("/list")
    @ApiOperation(value = "查询票据到期列表", notes = "分页查询票据到期列表")
    public String list(BillMaturityQueryDTO queryDTO) {
        try {
            PageInfo<BillMaturityVO> pageInfo = billMaturityService.selectBillMaturityList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据到期列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询票据到期", notes = "分页查询票据到期列表")
    public String page(BillMaturityQueryDTO queryDTO) {
        try {
            PageInfo<BillMaturityVO> pageInfo = billMaturityService.selectBillMaturityList(queryDTO);
            // 构造符合前端要求的数据格式
            Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("tlist", pageInfo.getList());
            resultMap.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(resultMap, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据到期列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{maturityId}")
    @ApiOperation(value = "获取票据到期详情", notes = "根据到期记录ID获取详细信息")
    public String detail(@PathVariable Long maturityId) {
        try {
            BillMaturityVO vo = billMaturityService.selectBillMaturityById(maturityId);
            if (vo == null) {
                return JsonBean.error("到期记录不存在");
            }
            return JsonBean.success(vo);
        } catch (BizException e) {
            log.warn("获取票据到期详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取票据到期详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/process")
    @ApiOperation(value = "处理票据到期", notes = "批量处理到期票据")
    public String process(@Valid @FlexibleRequestBody BillMaturityProcessDTO dto) {
        try {
            boolean result = billMaturityService.processBillMaturity(dto);
            return result ? JsonBean.success("处理成功") : JsonBean.error("处理失败");
        } catch (BizException e) {
            log.warn("处理票据到期失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("处理票据到期失败", e);
            return JsonBean.error("处理失败: " + e.getMessage());
        }
    }

    @PostMapping("/reminder")
    @ApiOperation(value = "发送到期提醒", notes = "发送票据到期提醒通知")
    public String sendReminder(@RequestParam(value = "maturityIds", required = false) List<Long> maturityIds) {
        try {
            boolean result = billMaturityService.sendMaturityReminder(maturityIds);
            return result ? JsonBean.success("提醒发送成功") : JsonBean.error("提醒发送失败");
        } catch (BizException e) {
            log.warn("发送到期提醒失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("发送到期提醒失败", e);
            return JsonBean.error("发送失败: " + e.getMessage());
        }
    }

    @PostMapping("/calendar")
    @ApiOperation(value = "获取到期日历数据", notes = "获取票据到期日历展示数据")
    public String getCalendarData(@RequestParam(required = false) Map<String, Object> params) {
        try {
            List<Map<String, Object>> data = billMaturityService.getMaturityCalendarData(params);
            return JsonBean.success(data);
        } catch (Exception e) {
            log.error("获取到期日历数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 批量处理到期票据
     */
    @PostMapping("/batch-process")
    @ApiOperation(value = "批量处理到期票据", notes = "批量处理到期票据")
    public String batchProcess(@Valid @FlexibleRequestBody BillMaturityProcessDTO dto) {
        try {
            int successCount = billMaturityService.batchProcessBillMaturity(dto);
            return JsonBean.success("批量处理成功，共处理 " + successCount + " 条记录");
        } catch (BizException e) {
            log.warn("批量处理到期票据失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("批量处理到期票据失败", e);
            return JsonBean.error("处理失败: " + e.getMessage());
        }
    }

    /**
     * 获取票据处理历史
     */
    @PostMapping("/history/{billId}")
    @ApiOperation(value = "获取票据处理历史", notes = "获取票据的处理历史记录")
    public String getHistory(@PathVariable Long billId) {
        try {
            List<Map<String, Object>> history = billMaturityService.getBillHistory(billId);
            return JsonBean.success(history);
        } catch (Exception e) {
            log.error("获取票据处理历史失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 导出到期数据
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出到期数据", notes = "导出票据到期数据到Excel")
    public void export(BillMaturityQueryDTO queryDTO, javax.servlet.http.HttpServletResponse response) {
        try {
            billMaturityService.exportBillMaturity(queryDTO, response);
        } catch (Exception e) {
            log.error("导出到期数据失败", e);
        }
    }

    /**
     * 获取到期统计数据
     */
    @PostMapping("/statistics")
    @ApiOperation(value = "获取到期统计", notes = "获取票据到期统计数据")
    public String statistics(BillMaturityQueryDTO queryDTO) {
        try {
            Map<String, Object> statistics = billMaturityService.getBillMaturityStatistics(queryDTO);
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取到期统计数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 延期申请
     */
    @PostMapping("/extend")
    @ApiOperation(value = "延期申请", notes = "票据到期延期申请")
    public String extend(@RequestParam Map<String, Object> extendData) {
        try {
            boolean result = billMaturityService.applyExtension(extendData);
            return result ? JsonBean.success("延期申请成功") : JsonBean.error("延期申请失败");
        } catch (BizException e) {
            log.warn("延期申请失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("延期申请失败", e);
            return JsonBean.error("延期申请失败: " + e.getMessage());
        }
    }

    /**
     * 托收申请
     */
    @PostMapping("/collect")
    @ApiOperation(value = "托收申请", notes = "票据到期托收申请")
    public String collect(@RequestParam Map<String, Object> collectData) {
        try {
            boolean result = billMaturityService.applyCollection(collectData);
            return result ? JsonBean.success("托收申请成功") : JsonBean.error("托收申请失败");
        } catch (BizException e) {
            log.warn("托收申请失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("托收申请失败", e);
            return JsonBean.error("托收申请失败: " + e.getMessage());
        }
    }

    /**
     * 获取到期趋势数据
     */
    @PostMapping("/trend")
    @ApiOperation(value = "获取到期趋势", notes = "获取票据到期趋势数据")
    public String getTrend(@RequestParam(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> trendData = billMaturityService.getMaturityTrendData(params);
            return JsonBean.success(trendData);
        } catch (Exception e) {
            log.error("获取到期趋势数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}

