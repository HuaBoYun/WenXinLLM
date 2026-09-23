package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetTransfer;
import com.management.accountant.oracle.entity.TblStaffOracle;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.TblStaffOracleMapper;
import com.management.accountant.service.BudgetTransferService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import com.management.accountant.util.excel.ExcelExport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 预算转移Controller
 * 
 * @description 预算转移管理接口，支持转移申请、审批、执行等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算转移"})
@RequestMapping(value = "/accountant/budget/transfer")
@Slf4j
public class BudgetTransferController {

    @Resource
    private BudgetTransferService transferService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private TblStaffOracleMapper staffMapper;

    /**
     * 创建转移申请
     */
    @Operation(summary = "创建转移申请")
    @ApiOperation("创建转移申请")
    @PostMapping("/create")
    public MyJsonBean<BudgetTransfer> create(@RequestBody @Validated BudgetTransfer transfer) {
        MyJsonBean<BudgetTransfer> result = new MyJsonBean<>();
        try {
            // 根据ID自动补全名称字段
            fillTransferNames(transfer);
            BudgetTransfer created = transferService.create(transfer);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建转移申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建转移申请异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询转移详情
     */
    @Operation(summary = "查询转移详情")
    @ApiOperation("查询转移详情")
    @GetMapping("/detail/{transferId}")
    public MyJsonBean<BudgetTransfer> getDetail(
            @ApiParam(value = "转移ID", required = true) @PathVariable String transferId) {
        MyJsonBean<BudgetTransfer> result = new MyJsonBean<>();
        try {
            BudgetTransfer transfer = transferService.getById(transferId);
            if (transfer != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(transfer);
            } else {
                result.setCode(0);
                result.setMsg("转移记录不存在");
            }
        } catch (Exception e) {
            log.error("查询转移详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新转移申请
     */
    @Operation(summary = "更新转移申请")
    @ApiOperation("更新转移申请")
    @PutMapping("/update/{transferId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "转移ID", required = true) @PathVariable String transferId,
            @RequestBody @Validated BudgetTransfer transfer) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            transfer.setTransferId(transferId);
            // 根据ID自动补全名称字段
            fillTransferNames(transfer);
            transferService.update(transfer);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新转移申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新转移申请异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除转移申请
     */
    @Operation(summary = "删除转移申请")
    @ApiOperation("删除转移申请")
    @DeleteMapping("/delete/{transferId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "转移ID", required = true) @PathVariable String transferId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            transferService.delete(transferId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除转移申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除转移申请异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询转移列表
     */
    @Operation(summary = "分页查询转移列表")
    @ApiOperation("分页查询转移列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetTransfer>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetTransfer>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetTransfer> pageResult = transferService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询转移列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 审批转移申请
     */
    @Operation(summary = "审批转移申请")
    @ApiOperation("审批转移申请")
    @PostMapping("/approve/{transferId}")
    public MyJsonBean<Void> approve(
            @ApiParam(value = "转移ID", required = true) @PathVariable String transferId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            params.put("transferId", transferId);
            transferService.approve(params);
            result.setCode(1);
            result.setMsg("审批成功");
        } catch (ServiceException ex) {
            log.error("审批转移申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("审批转移申请异常", e);
            result.setCode(0);
            result.setMsg("审批失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行转移
     */
    @Operation(summary = "执行转移")
    @ApiOperation("执行转移")
    @PostMapping("/execute/{transferId}")
    public MyJsonBean<Void> execute(
            @ApiParam(value = "转移ID", required = true) @PathVariable String transferId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            transferService.execute(transferId);
            result.setCode(1);
            result.setMsg("执行成功");
        } catch (ServiceException ex) {
            log.error("执行转移失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行转移异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量转移
     */
    @Operation(summary = "批量转移")
    @ApiOperation("批量转移")
    @PostMapping("/batch/transfer")
    public MyJsonBean<Map<String, Object>> batchTransfer(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = transferService.batchTransfer(params);
            result.setCode(1);
            result.setMsg("批量转移成功");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量转移失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量转移异常", e);
            result.setCode(0);
            result.setMsg("批量转移失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出转移数据
     */
    @Operation(summary = "导出转移数据")
    @ApiOperation("导出转移数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetTransfer> dataList = transferService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算转移数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算转移", BudgetTransfer.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算转移数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算转移数据异常", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (java.io.IOException ex) {
                log.error("响应写入异常", ex);
            }
        }
    }

    @Operation(summary = "获取组织列表")
    @ApiOperation("获取组织列表")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetOrganization> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_ENABLED", 1);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetOrganization> orgList = organizationMapper.selectList(wrapper);
            List<Map<String, Object>> orgs = new ArrayList<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", org.getOrganizationId());
                map.put("name", org.getOrganizationName());
                map.put("code", org.getOrganizationCode());
                orgs.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(orgs);
        } catch (Exception e) {
            log.error("获取组织列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/accounts")
    public MyJsonBean<List<Map<String, Object>>> getAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_ENABLED", 1);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetAccount> accList = accountMapper.selectList(wrapper);
            List<Map<String, Object>> accounts = new ArrayList<>();
            for (BudgetAccount acc : accList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", acc.getAccountId());
                map.put("name", acc.getAccountName());
                map.put("code", acc.getAccountCode());
                accounts.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            log.error("获取预算科目列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取用户列表")
    @ApiOperation("获取用户列表")
    @GetMapping("/users")
    public MyJsonBean<List<Map<String, Object>>> getUsers() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<TblStaffOracle> staffList = staffMapper.selectAllActiveStaff();
            List<Map<String, Object>> users = new ArrayList<>();
            for (TblStaffOracle staff : staffList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", staff.getStaffId());
                map.put("name", staff.getRealName());
                users.add(map);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(users);
        } catch (Exception e) {
            log.error("获取用户列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "验证转移")
    @ApiOperation("验证转移")
    @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validateTransfer(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validateResult = new HashMap<>();
            boolean valid = true;
            String message = "转移验证通过";

            java.math.BigDecimal requestAmount = java.math.BigDecimal.ZERO;
            if (params.get("transferAmount") != null) {
                requestAmount = new java.math.BigDecimal(params.get("transferAmount").toString());
            }
            if (requestAmount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                valid = false;
                message = "转移金额必须大于0";
            }

            // TODO: 查询实际可用金额，目前返回模拟数据
            java.math.BigDecimal fromAvailableAmount = new java.math.BigDecimal("999999999");
            java.math.BigDecimal toAcceptableAmount = new java.math.BigDecimal("999999999");

            if (valid && requestAmount.compareTo(fromAvailableAmount) > 0) {
                valid = false;
                message = "转出金额超出转出单元可用预算";
            }

            validateResult.put("valid", valid);
            validateResult.put("message", message);
            validateResult.put("requestAmount", requestAmount);
            validateResult.put("fromAvailableAmount", fromAvailableAmount);
            validateResult.put("toAcceptableAmount", toAcceptableAmount);
            result.setCode(1);
            result.setMsg("验证成功");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("验证转移异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量审批转移")
    @ApiOperation("批量审批转移")
    @PostMapping("/batch/approve")
    public MyJsonBean<Map<String, Object>> batchApprove(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<String> ids = (List<String>) params.get("ids");
            int totalCount = ids != null ? ids.size() : 0;
            int successCount = 0;
            if (ids != null) {
                for (String id : ids) {
                    try {
                        BudgetTransfer transfer = transferService.getById(id);
                        if (transfer != null && "PENDING".equals(transfer.getTransferStatus())) {
                            BudgetTransfer update = new BudgetTransfer();
                            update.setTransferId(id);
                            update.setTransferStatus("APPROVED");
                            update.setApprovalStatus("APPROVED");
                            update.setApproveDate(new java.util.Date());
                            update.setApproveTime(new java.util.Date());
                            update.setUpdateTime(new java.util.Date());
                            transferService.updateById(update);
                            successCount++;
                        }
                    } catch (Exception ex) {
                        log.warn("审批转移失败, id={}", id, ex);
                    }
                }
            }
            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", totalCount);
            batchResult.put("successCount", successCount);
            result.setCode(1);
            result.setMsg("批量审批完成，成功" + successCount + "条");
            result.setData(batchResult);
        } catch (Exception e) {
            log.error("批量审批转移异常", e);
            result.setCode(0);
            result.setMsg("批量审批失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "驳回转移申请")
    @ApiOperation("驳回转移申请")
    @PostMapping("/reject/{transferId}")
    public MyJsonBean<Void> reject(
            @ApiParam(value = "转移ID", required = true) @PathVariable String transferId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetTransfer transfer = transferService.getById(transferId);
            if (transfer == null) {
                result.setCode(0);
                result.setMsg("转移记录不存在");
                return result;
            }
            if (!"PENDING".equals(transfer.getTransferStatus())) {
                result.setCode(0);
                result.setMsg("只能驳回待审批状态的转移申请");
                return result;
            }
            BudgetTransfer update = new BudgetTransfer();
            update.setTransferId(transferId);
            update.setTransferStatus("REJECTED");
            update.setApprovalStatus("REJECTED");
            String rejectRemark = params.get("remark") != null ? params.get("remark").toString() : "";
            update.setApproveRemark(rejectRemark);
            update.setApproveDate(new java.util.Date());
            update.setApproveTime(new java.util.Date());
            update.setUpdateTime(new java.util.Date());
            transferService.updateById(update);
            result.setCode(1);
            result.setMsg("驳回成功");
        } catch (Exception e) {
            log.error("驳回转移申请异常", e);
            result.setCode(0);
            result.setMsg("驳回失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个转移数据")
    @ApiOperation("导出单个转移数据")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "转移ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            BudgetTransfer transfer = transferService.getById(id);
            if (transfer == null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"转移记录不存在\"}");
                return;
            }

            response.setContentType("text/csv;charset=utf-8");
            String fileName = java.net.URLEncoder.encode("预算转移_" + (transfer.getTransferCode() != null ? transfer.getTransferCode() : id) + ".csv", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            StringBuilder sb = new StringBuilder();
            // BOM for Excel UTF-8 recognition
            sb.append("\uFEFF");
            sb.append("转移编码,转移标题,转移类型,转移金额,转出单元,转入单元,转移状态,审批状态,申请人,审批人,申请日期,转移日期,转移原因,转移描述,备注\n");
            sb.append(csvField(transfer.getTransferCode())).append(",");
            sb.append(csvField(transfer.getTransferTitle())).append(",");
            sb.append(csvField(transfer.getTransferType())).append(",");
            sb.append(transfer.getTransferAmount() != null ? transfer.getTransferAmount() : "").append(",");
            sb.append(csvField(transfer.getFromOrganizationName())).append(",");
            sb.append(csvField(transfer.getToOrganizationName())).append(",");
            sb.append(csvField(transfer.getTransferStatus())).append(",");
            sb.append(csvField(transfer.getApprovalStatus())).append(",");
            sb.append(csvField(transfer.getApplicant())).append(",");
            sb.append(csvField(transfer.getApprover())).append(",");
            sb.append(transfer.getApplyDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(transfer.getApplyDate()) : "").append(",");
            sb.append(transfer.getTransferDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(transfer.getTransferDate()) : "").append(",");
            sb.append(csvField(transfer.getTransferReason())).append(",");
            sb.append(csvField(transfer.getTransferDescription())).append(",");
            sb.append(csvField(transfer.getRemark())).append("\n");

            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个转移数据异常", e);
        }
    }

    /**
     * CSV字段安全处理（处理逗号、引号）
     */
    private String csvField(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * 获取转移统计信息
     */
    @Operation(summary = "获取转移统计信息")
    @ApiOperation("获取转移统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = transferService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取转移统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 根据ID自动补全转移记录中的名称字段
     */
    private void fillTransferNames(BudgetTransfer transfer) {
        // 补全转出单元名称
        if (transfer.getFromOrganizationId() != null && transfer.getFromOrganizationId().length() > 0
                && (transfer.getFromOrganizationName() == null || transfer.getFromOrganizationName().isEmpty())) {
            try {
                BudgetOrganization fromOrg = organizationMapper.selectById(transfer.getFromOrganizationId());
                if (fromOrg != null) {
                    transfer.setFromOrganizationName(fromOrg.getOrganizationName());
                }
            } catch (Exception e) {
                log.warn("查询转出单元名称失败, id={}", transfer.getFromOrganizationId());
            }
        }
        // 补全转入单元名称
        if (transfer.getToOrganizationId() != null && transfer.getToOrganizationId().length() > 0
                && (transfer.getToOrganizationName() == null || transfer.getToOrganizationName().isEmpty())) {
            try {
                BudgetOrganization toOrg = organizationMapper.selectById(transfer.getToOrganizationId());
                if (toOrg != null) {
                    transfer.setToOrganizationName(toOrg.getOrganizationName());
                }
            } catch (Exception e) {
                log.warn("查询转入单元名称失败, id={}", transfer.getToOrganizationId());
            }
        }
        // 补全审批人名称（前端传的是staffId，转为真实姓名）
        if (transfer.getApprover() != null && transfer.getApprover().length() > 0) {
            try {
                Long approverId = Long.parseLong(transfer.getApprover());
                TblStaffOracle staff = staffMapper.getUserInfoForId(approverId);
                if (staff != null && staff.getRealName() != null) {
                    transfer.setApprover(staff.getRealName());
                }
            } catch (NumberFormatException ignored) {
                // approver已经是名称，无需转换
            } catch (Exception e) {
                log.warn("查询审批人名称失败, id={}", transfer.getApprover());
            }
        }
        // 补全申请人（如果为空，默认设为当前操作人）
        if (transfer.getApplicant() == null || transfer.getApplicant().isEmpty()) {
            // 优先使用审批人已转换的名称以外的方式，此处设默认值
            transfer.setApplicant("当前用户");
        }
    }
}