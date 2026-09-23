package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetReserve;
import com.management.accountant.oracle.entity.budget.BudgetReserveHistory;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetReserveHistoryMapper;
import com.management.accountant.service.BudgetReserveService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import com.management.accountant.util.excel.ExcelExport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 预算保留Controller
 * 
 * @description 预算保留管理接口，支持保留申请、审批、执行等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算保留"})
@RequestMapping(value = "/accountant/budget/reserve")
public class BudgetReserveController {

    private static final Logger log = LoggerFactory.getLogger(BudgetReserveController.class);

    @Resource
    private BudgetReserveService reserveService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private BudgetReserveHistoryMapper reserveHistoryMapper;

    /**
     * 创建保留申请
     */
    @Operation(summary = "创建保留申请")
    @ApiOperation("创建保留申请")
    @PostMapping("/create")
    public MyJsonBean<BudgetReserve> create(@RequestBody @Validated BudgetReserve reserve) {
        MyJsonBean<BudgetReserve> result = new MyJsonBean<>();
        try {
            BudgetReserve created = reserveService.create(reserve);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建保留申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建保留申请异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询保留详情
     */
    @Operation(summary = "查询保留详情")
    @ApiOperation("查询保留详情")
    @GetMapping("/detail/{reserveId}")
    public MyJsonBean<BudgetReserve> getDetail(
            @ApiParam(value = "保留ID", required = true) @PathVariable String reserveId) {
        MyJsonBean<BudgetReserve> result = new MyJsonBean<>();
        try {
            BudgetReserve reserve = reserveService.getById(reserveId);
            if (reserve != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(reserve);
            } else {
                result.setCode(0);
                result.setMsg("保留记录不存在");
            }
        } catch (Exception e) {
            log.error("查询保留详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新保留申请
     */
    @Operation(summary = "更新保留申请")
    @ApiOperation("更新保留申请")
    @PutMapping("/update/{reserveId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "保留ID", required = true) @PathVariable String reserveId,
            @RequestBody @Validated BudgetReserve reserve) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reserve.setReserveId(reserveId);
            reserveService.update(reserve);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新保留申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新保留申请异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除保留申请
     */
    @Operation(summary = "删除保留申请")
    @ApiOperation("删除保留申请")
    @DeleteMapping("/delete/{reserveId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "保留ID", required = true) @PathVariable String reserveId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reserveService.delete(reserveId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除保留申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除保留申请异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询保留列表
     */
    @Operation(summary = "分页查询保留列表")
    @ApiOperation("分页查询保留列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetReserve>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetReserve>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetReserve> pageResult = reserveService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询保留列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 审批保留申请
     */
    @Operation(summary = "审批保留申请")
    @ApiOperation("审批保留申请")
    @PostMapping("/approve/{reserveId}")
    public MyJsonBean<Void> approve(
            @ApiParam(value = "保留ID", required = true) @PathVariable String reserveId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            params.put("reserveId", reserveId);
            reserveService.approve(params);
            result.setCode(1);
            result.setMsg("审批成功");
        } catch (ServiceException ex) {
            log.error("审批保留申请失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("审批保留申请异常", e);
            result.setCode(0);
            result.setMsg("审批失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行保留
     */
    @Operation(summary = "执行保留")
    @ApiOperation("执行保留")
    @PostMapping("/execute/{reserveId}")
    public MyJsonBean<Void> execute(
            @ApiParam(value = "保留ID", required = true) @PathVariable String reserveId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reserveService.execute(reserveId);
            result.setCode(1);
            result.setMsg("执行成功");
        } catch (ServiceException ex) {
            log.error("执行保留失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行保留异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量保留
     */
    @Operation(summary = "批量保留")
    @ApiOperation("批量保留")
    @PostMapping("/batch/reserve")
    public MyJsonBean<Map<String, Object>> batchReserve(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = reserveService.batchReserve(params);
            result.setCode(1);
            result.setMsg("批量保留成功");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量保留失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量保留异常", e);
            result.setCode(0);
            result.setMsg("批量保留失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出保留数据
     */
    @Operation(summary = "导出保留数据")
    @ApiOperation("导出保留数据")
    @PostMapping("/export")
    public void exportData(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetReserve> dataList = reserveService.exportData(params);

            // 使用ExcelExport工具类导出
            String fileName = "预算保留数据.xlsx";
            try (ExcelExport ee = new ExcelExport("预算保留", BudgetReserve.class)) {
                ee.setDataList(dataList);
                ee.write(response, fileName);
            }

            log.info("导出预算保留数据成功，数量: {}", dataList.size());
        } catch (Exception e) {
            log.error("导出预算保留数据异常", e);
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
            wrapper.eq("DEL_FLAG", 0);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetOrganization> orgList = organizationMapper.selectList(wrapper);

            // 构建 id -> node 映射
            Map<String, Map<String, Object>> nodeMap = new LinkedHashMap<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> node = new HashMap<>();
                node.put("value", org.getOrganizationId());
                node.put("label", org.getOrganizationName());
                node.put("parentId", org.getParentId());
                node.put("children", new ArrayList<>());
                nodeMap.put(org.getOrganizationId(), node);
            }

            // 组装树形结构
            List<Map<String, Object>> tree = new ArrayList<>();
            for (Map<String, Object> node : nodeMap.values()) {
                String parentId = (String) node.get("parentId");
                if (parentId != null && nodeMap.containsKey(parentId)) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> siblings = (List<Map<String, Object>>) nodeMap.get(parentId).get("children");
                    siblings.add(node);
                } else {
                    tree.add(node);
                }
            }

            // 移除空 children 和 parentId（el-cascader 不需要）
            cleanTree(tree);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tree);
        } catch (Exception e) {
            log.error("获取组织列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void cleanTree(List<Map<String, Object>> nodes) {
        for (Map<String, Object> node : nodes) {
            node.remove("parentId");
            List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
            if (children == null || children.isEmpty()) {
                node.remove("children");
            } else {
                cleanTree(children);
            }
        }
    }

    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/accounts")
    public MyJsonBean<List<Map<String, Object>>> getAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            wrapper.orderByAsc("SORT_ORDER");
            List<BudgetAccount> accList = accountMapper.selectList(wrapper);
            List<Map<String, Object>> accounts = new ArrayList<>();
            for (BudgetAccount acc : accList) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", acc.getAccountId());
                map.put("label", acc.getAccountName());
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

    @Operation(summary = "计算保留金额")
    @ApiOperation("计算保留金额")
    @PostMapping("/calculate")
    public MyJsonBean<Map<String, Object>> calculateReserve(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> calcResult = new HashMap<>();
            double reserveAmount = params.get("reserveAmount") != null ? Double.parseDouble(params.get("reserveAmount").toString()) : 0;
            String reserveType = params.get("reserveType") != null ? params.get("reserveType").toString() : "";

            // 根据类型计算建议期限和预计使用率
            int suggestedPeriod = 180; // 默认180天
            double expectedUsageRate = 60.0;
            if ("CONTINGENCY".equals(reserveType)) {
                suggestedPeriod = 365;
                expectedUsageRate = 30.0;
            } else if ("STRATEGIC".equals(reserveType)) {
                suggestedPeriod = 730;
                expectedUsageRate = 50.0;
            } else if ("OPERATIONAL".equals(reserveType)) {
                suggestedPeriod = 90;
                expectedUsageRate = 80.0;
            }

            calcResult.put("reserveAmount", reserveAmount);
            calcResult.put("reserveType", reserveType);
            calcResult.put("suggestedPeriod", suggestedPeriod);
            calcResult.put("expectedUsageRate", expectedUsageRate);

            result.setCode(1);
            result.setMsg("计算成功");
            result.setData(calcResult);
        } catch (Exception e) {
            log.error("计算保留金额异常", e);
            result.setCode(0);
            result.setMsg("计算失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "使用保留预算")
    @ApiOperation("使用保留预算")
    @PostMapping("/use")
    public MyJsonBean<Map<String, Object>> useReserve(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String reserveId = params.get("reserveId") != null ? params.get("reserveId").toString() : null;
            if (reserveId == null) {
                result.setCode(0);
                result.setMsg("保留ID不能为空");
                return result;
            }
            BudgetReserve reserve = reserveService.getById(reserveId);
            if (reserve == null) {
                result.setCode(0);
                result.setMsg("保留记录不存在");
                return result;
            }
            java.math.BigDecimal useAmount = params.get("useAmount") != null
                    ? new java.math.BigDecimal(params.get("useAmount").toString()) : java.math.BigDecimal.ZERO;
            java.math.BigDecimal currentUsed = reserve.getUsedAmount() != null ? reserve.getUsedAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal currentAvailable = reserve.getAvailableAmount() != null ? reserve.getAvailableAmount() : reserve.getReserveAmount();
            reserve.setUsedAmount(currentUsed.add(useAmount));
            reserve.setAvailableAmount(currentAvailable.subtract(useAmount));
            reserveService.updateById(reserve);

            // 记录使用历史
            BudgetReserveHistory history = new BudgetReserveHistory();
            history.setReserveId(reserveId);
            history.setOperationType("USE");
            String usePurpose = params.get("usePurpose") != null ? params.get("usePurpose").toString() : "";
            history.setOperationDesc("使用储备金额 " + useAmount + (usePurpose.isEmpty() ? "" : "，用途：" + usePurpose));
            history.setAmount(useAmount);
            history.setBeforeValue("可用:" + currentAvailable);
            history.setAfterValue("可用:" + reserve.getAvailableAmount());
            history.setOperator(params.get("operator") != null ? params.get("operator").toString() : "system");
            history.setOperateTime(new Date());
            history.setCreateTime(new Date());
            reserveHistoryMapper.insert(history);

            Map<String, Object> useResult = new HashMap<>();
            useResult.put("usedAmount", useAmount);
            useResult.put("remainingReserve", reserve.getAvailableAmount());
            useResult.put("status", "SUCCESS");
            result.setCode(1);
            result.setMsg("使用成功");
            result.setData(useResult);
        } catch (Exception e) {
            log.error("使用保留预算异常", e);
            result.setCode(0);
            result.setMsg("使用失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "释放保留预算")
    @ApiOperation("释放保留预算")
    @PostMapping("/release")
    public MyJsonBean<Map<String, Object>> releaseReserve(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String reserveId = params.get("reserveId") != null ? params.get("reserveId").toString() : null;
            if (reserveId == null) {
                result.setCode(0);
                result.setMsg("保留ID不能为空");
                return result;
            }
            BudgetReserve reserve = reserveService.getById(reserveId);
            if (reserve == null) {
                result.setCode(0);
                result.setMsg("保留记录不存在");
                return result;
            }

            String releaseType = params.get("releaseType") != null ? params.get("releaseType").toString() : "FULL";
            BigDecimal currentAvailable = reserve.getAvailableAmount() != null ? reserve.getAvailableAmount() : BigDecimal.ZERO;
            BigDecimal releasedAmount;

            if ("PARTIAL".equals(releaseType)) {
                releasedAmount = params.get("releaseAmount") != null
                        ? new BigDecimal(params.get("releaseAmount").toString()) : BigDecimal.ZERO;
                if (releasedAmount.compareTo(currentAvailable) > 0) {
                    result.setCode(0);
                    result.setMsg("释放金额不能超过可用金额");
                    return result;
                }
                reserve.setAvailableAmount(currentAvailable.subtract(releasedAmount));
                reserve.setUpdateTime(new Date());
                // 部分释放后如果可用金额为0，改状态为RELEASED
                if (reserve.getAvailableAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    reserve.setReserveStatus("RELEASED");
                }
            } else {
                releasedAmount = currentAvailable;
                reserve.setReserveStatus("RELEASED");
                reserve.setAvailableAmount(BigDecimal.ZERO);
                reserve.setUpdateTime(new Date());
            }

            reserveService.updateById(reserve);

            // 记录释放历史
            BudgetReserveHistory history = new BudgetReserveHistory();
            history.setReserveId(reserveId);
            history.setOperationType("RELEASE");
            String releaseRemark = params.get("releaseRemark") != null ? params.get("releaseRemark").toString() : "";
            history.setOperationDesc(("PARTIAL".equals(releaseType) ? "部分释放" : "全部释放") + "金额 " + releasedAmount + (releaseRemark.isEmpty() ? "" : "，备注：" + releaseRemark));
            history.setAmount(releasedAmount);
            history.setBeforeValue("可用:" + currentAvailable);
            history.setAfterValue("可用:" + reserve.getAvailableAmount());
            history.setOperator("system");
            history.setOperateTime(new Date());
            history.setCreateTime(new Date());
            reserveHistoryMapper.insert(history);

            Map<String, Object> releaseResult = new HashMap<>();
            releaseResult.put("releasedAmount", releasedAmount);
            releaseResult.put("status", reserve.getReserveStatus());
            result.setCode(1);
            result.setMsg("释放成功");
            result.setData(releaseResult);
        } catch (Exception e) {
            log.error("释放保留预算异常", e);
            result.setCode(0);
            result.setMsg("释放失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量释放保留预算")
    @ApiOperation("批量释放保留预算")
    @PostMapping("/batch/release")
    public MyJsonBean<Map<String, Object>> batchRelease(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<String> ids = (List<String>) params.get("ids");
            int totalCount = ids != null ? ids.size() : 0;
            int successCount = 0;
            if (ids != null) {
                for (String id : ids) {
                    try {
                        BudgetReserve reserve = reserveService.getById(id);
                        if (reserve != null) {
                            reserve.setReserveStatus("RELEASED");
                            reserve.setAvailableAmount(java.math.BigDecimal.ZERO);
                            reserveService.updateById(reserve);
                            successCount++;
                        }
                    } catch (Exception ex) {
                        log.warn("释放保留失败, id={}", id, ex);
                    }
                }
            }
            Map<String, Object> batchResult = new HashMap<>();
            batchResult.put("totalCount", totalCount);
            batchResult.put("successCount", successCount);
            result.setCode(1);
            result.setMsg("批量释放完成，成功" + successCount + "条");
            result.setData(batchResult);
        } catch (Exception e) {
            log.error("批量释放保留预算异常", e);
            result.setCode(0);
            result.setMsg("批量释放失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "取消保留")
    @ApiOperation("取消保留")
    @PostMapping("/cancel/{reserveId}")
    public MyJsonBean<Void> cancelReserve(
            @ApiParam(value = "保留ID", required = true) @PathVariable String reserveId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            BudgetReserve reserve = reserveService.getById(reserveId);
            if (reserve == null) {
                result.setCode(0);
                result.setMsg("保留记录不存在");
                return result;
            }
            reserve.setReserveStatus("CANCELLED");
            reserveService.updateById(reserve);
            result.setCode(1);
            result.setMsg("取消成功");
        } catch (Exception e) {
            log.error("取消保留异常", e);
            result.setCode(0);
            result.setMsg("取消失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个保留数据")
    @ApiOperation("导出单个保留数据")
    @GetMapping("/export/{id}")
    public void exportSingle(
            @ApiParam(value = "保留ID", required = true) @PathVariable String id,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=reserve_" + id + ".xlsx");
            StringBuilder sb = new StringBuilder();
            sb.append("ID,保留名称,保留金额,状态\n");
            BudgetReserve reserve = reserveService.getById(id);
            if (reserve != null) {
                sb.append(reserve.getReserveId() != null ? reserve.getReserveId() : id).append(",");
                sb.append(reserve.getReserveName() != null ? reserve.getReserveName() : "").append(",");
                sb.append(reserve.getReserveAmount() != null ? reserve.getReserveAmount() : "").append(",");
                sb.append(reserve.getReserveStatus() != null ? reserve.getReserveStatus() : "").append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出单个保留数据异常", e);
        }
    }

    /**
     * 获取保留统计信息
     */
    @Operation(summary = "获取保留统计信息")
    @ApiOperation("获取保留统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = reserveService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取保留统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 转移储备
     */
    @Operation(summary = "转移储备")
    @ApiOperation("转移储备")
    @PostMapping("/transfer")
    public MyJsonBean<Map<String, Object>> transferReserve(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String reserveId = params.get("reserveId") != null ? params.get("reserveId").toString() : null;
            if (reserveId == null) {
                result.setCode(0);
                result.setMsg("储备ID不能为空");
                return result;
            }
            BigDecimal transferAmount = params.get("transferAmount") != null
                    ? new BigDecimal(params.get("transferAmount").toString()) : BigDecimal.ZERO;
            if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
                result.setCode(0);
                result.setMsg("转移金额必须大于0");
                return result;
            }

            BudgetReserve source = reserveService.getById(reserveId);
            if (source == null) {
                result.setCode(0);
                result.setMsg("源储备记录不存在");
                return result;
            }
            BigDecimal sourceAvailable = source.getAvailableAmount() != null ? source.getAvailableAmount() : BigDecimal.ZERO;
            if (transferAmount.compareTo(sourceAvailable) > 0) {
                result.setCode(0);
                result.setMsg("转移金额不能超过可用金额");
                return result;
            }

            // 扣减源储备
            source.setAvailableAmount(sourceAvailable.subtract(transferAmount));
            source.setUpdateTime(new Date());
            reserveService.updateById(source);

            // 创建新储备
            BudgetReserve target = new BudgetReserve();
            target.setReserveName(source.getReserveName() + "-转移");
            target.setReserveType(source.getReserveType());
            target.setBudgetId(source.getBudgetId());
            target.setReserveAmount(transferAmount);
            target.setAvailableAmount(transferAmount);
            target.setUsedAmount(BigDecimal.ZERO);
            target.setReserveReason("从 " + source.getReserveCode() + " 转移");
            target.setReserveDescription(params.get("transferRemark") != null ? params.get("transferRemark").toString() : "");
            reserveService.create(target);

            // 记录转移历史
            BudgetReserveHistory history = new BudgetReserveHistory();
            history.setReserveId(reserveId);
            history.setOperationType("TRANSFER");
            history.setOperationDesc("转移金额 " + transferAmount + " 至新储备 " + target.getReserveCode());
            history.setAmount(transferAmount);
            history.setBeforeValue("可用:" + sourceAvailable);
            history.setAfterValue("可用:" + source.getAvailableAmount());
            history.setOperator("system");
            history.setOperateTime(new Date());
            history.setCreateTime(new Date());
            reserveHistoryMapper.insert(history);

            Map<String, Object> transferResult = new HashMap<>();
            transferResult.put("sourceReserveId", reserveId);
            transferResult.put("targetReserveId", target.getReserveId());
            transferResult.put("transferAmount", transferAmount);
            result.setCode(1);
            result.setMsg("转移成功");
            result.setData(transferResult);
        } catch (Exception e) {
            log.error("转移储备异常", e);
            result.setCode(0);
            result.setMsg("转移失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询储备操作历史
     */
    @Operation(summary = "查询储备操作历史")
    @ApiOperation("查询储备操作历史")
    @GetMapping("/history/{reserveId}")
    public MyJsonBean<List<BudgetReserveHistory>> getHistory(
            @ApiParam(value = "储备ID", required = true) @PathVariable String reserveId) {
        MyJsonBean<List<BudgetReserveHistory>> result = new MyJsonBean<>();
        try {
            List<BudgetReserveHistory> historyList = reserveHistoryMapper.selectByReserveId(reserveId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(historyList);
        } catch (Exception e) {
            log.error("查询储备操作历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}