package com.global.treasurer.financialProductDefinition.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.entity.TblBillRiskAlert;
import com.global.treasurer.financialProductDefinition.service.RiskAlertService;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 风险预警Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/financial/product-definition/risk-alert")
@Api(tags = "风险预警管理")
public class RiskAlertController {

    private static final Logger log = LoggerFactory.getLogger(RiskAlertController.class);

    @Autowired
    private RiskAlertService riskAlertService;

    @PostMapping("/getList")
    @ApiOperation("分页查询风险预警列表")
    public String getList(@ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
                          @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                          @ApiParam("预警类型") @RequestParam(required = false) String alertType,
                          @ApiParam("预警级别") @RequestParam(required = false) String alertLevel,
                          @ApiParam("预警状态") @RequestParam(required = false) String alertStatus,
                          @ApiParam("产品名称") @RequestParam(required = false) String productName,
                          @ApiParam("组织ID") @RequestParam(required = false) Long orgId) {
        try {
            log.info("查询风险预警列表 - pageNo:{}, pageSize:{}, alertType:{}, alertLevel:{}, alertStatus:{}, productName:{}, orgId:{}",
                     pageNo, pageSize, alertType, alertLevel, alertStatus, productName, orgId);

            IPage<TblBillRiskAlert> result = riskAlertService.getAlertPage(
                pageNo, pageSize, alertType, alertLevel, alertStatus, productName, orgId
            );

            return JsonBean.successByString(result);
        } catch (Exception e) {
            log.error("查询风险预警列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/getStatistics")
    @ApiOperation("获取预警统计信息")
    public String getStatistics() {
        try {
            log.info("获取预警统计信息");

            Map<String, Object> statistics = riskAlertService.getStatistics();

            return JsonBean.successByStringData(statistics);
        } catch (Exception e) {
            log.error("获取预警统计信息失败", e);
            return JsonBean.error("获取统计信息失败: " + e.getMessage());
        }
    }
}
