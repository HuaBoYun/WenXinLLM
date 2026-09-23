package com.global.treasurer.controller;

import com.hbfk.util.JsonBean;
import com.global.treasurer.dto.CreditApplicationDTO;
import com.global.treasurer.dto.CreditApplicationQueryDTO;
import com.global.treasurer.entity.TblCreditApplication;
import com.global.treasurer.service.CreditApplicationService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 授信申请管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-02-04
 */
@RestController
@RequestMapping({"/financing/credit-application", "/financial/rzgl/credit/application"})
@Api(tags = "授信申请管理")
public class CreditApplicationController {

    private static final Logger log = LoggerFactory.getLogger(CreditApplicationController.class);

    @Autowired
    private CreditApplicationService creditApplicationService;

    @PostMapping("/page")
    @ApiOperation("授信申请分页查询")
    public String page(@RequestParam Map<String, Object> params) {
        try {
            log.info("授信申请分页查询, params: {}", params);

            // 从 Map 中提取参数
            Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String applicationNo = (String) params.get("applicationNo");
            String orgName = (String) params.get("orgName");
            String creditType = (String) params.get("creditType");
            String applicationStatus = (String) params.get("applicationStatus");

            // 构建 QueryDTO
            CreditApplicationQueryDTO queryDTO = new CreditApplicationQueryDTO();
            queryDTO.setPageNum(pageNum);
            queryDTO.setPageSize(pageSize);
            queryDTO.setCreditType(creditType);
            queryDTO.setApplicationStatus(applicationStatus);

            PageInfo<TblCreditApplication> pageInfo = creditApplicationService.getApplicationList(queryDTO);

            // 构建前端期望的响应格式
            Map<String, Object> data = new HashMap<>();
            data.put("rows", pageInfo.getList());
            data.put("total", pageInfo.getTotal());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("授信申请分页查询失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("授信申请详情查询")
    public String detail(@ApiParam("申请ID") @PathVariable Long id) {
        try {
            log.info("查询授信申请详情, id: {}", id);
            TblCreditApplication application = creditApplicationService.getApplicationById(id);
            return new JsonBean(1, "查询成功", application).toString();
        } catch (Exception e) {
            log.error("查询授信申请详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping({"/add", ""})
    @ApiOperation("新增授信申请")
    public String add(@FlexibleRequestBody CreditApplicationDTO dto) {
        try {
            log.info("新增授信申请, dto: {}", dto);
            TblCreditApplication application = creditApplicationService.saveApplication(dto);
            return new JsonBean(1, "新增成功", application).toString();
        } catch (Exception e) {
            log.error("新增授信申请失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping({"/update", ""})
    @ApiOperation("更新授信申请")
    public String update(@FlexibleRequestBody CreditApplicationDTO dto) {
        try {
            log.info("更新授信申请, dto: {}", dto);
            TblCreditApplication application = creditApplicationService.saveApplication(dto);
            return new JsonBean(1, "更新成功", application).toString();
        } catch (Exception e) {
            log.error("更新授信申请失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage()).toString();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除授信申请")
    public String delete(@ApiParam("申请ID") @PathVariable Long id) {
        try {
            log.info("删除授信申请, id: {}", id);
            creditApplicationService.deleteApplication(id);
            return new JsonBean(1, "删除成功").toString();
        } catch (Exception e) {
            log.error("删除授信申请失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage()).toString();
        }
    }

    @PutMapping("/{id}/submit")
    @ApiOperation("提交授信申请")
    public String submit(@ApiParam("申请ID") @PathVariable Long id) {
        try {
            log.info("提交授信申请, id: {}", id);
            creditApplicationService.submitForApproval(id);
            return new JsonBean(1, "提交成功").toString();
        } catch (Exception e) {
            log.error("提交授信申请失败, id: {}", id, e);
            return new JsonBean(0, "提交失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/approve")
    @ApiOperation("审批授信申请")
    public String approve(
            @ApiParam("申请ID") @RequestParam Long id,
            @ApiParam("批准金额") @RequestParam java.math.BigDecimal approvedAmount,
            @ApiParam("审批意见") @RequestParam(required = false) String approvalComments) {
        try {
            log.info("审批授信申请, id: {}, approvedAmount: {}, approvalComments: {}", id, approvedAmount, approvalComments);
            creditApplicationService.approve(id, approvedAmount, approvalComments);
            return new JsonBean(1, "审批成功").toString();
        } catch (Exception e) {
            log.error("审批授信申请失败, id: {}", id, e);
            return new JsonBean(0, "审批失败: " + e.getMessage()).toString();
        }
    }

    @PostMapping("/reject")
    @ApiOperation("拒绝授信申请")
    public String reject(
            @ApiParam("申请ID") @RequestParam Long id,
            @ApiParam("拒绝原因") @RequestParam String rejectReason) {
        try {
            log.info("拒绝授信申请, id: {}, rejectReason: {}", id, rejectReason);
            creditApplicationService.reject(id, rejectReason);
            return new JsonBean(1, "拒绝成功").toString();
        } catch (Exception e) {
            log.error("拒绝授信申请失败, id: {}", id, e);
            return new JsonBean(0, "拒绝失败: " + e.getMessage()).toString();
        }
    }
}
