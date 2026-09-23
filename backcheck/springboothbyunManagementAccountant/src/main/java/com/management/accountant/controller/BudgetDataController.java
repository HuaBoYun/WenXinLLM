package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetData;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.service.BudgetDataService;
import com.management.accountant.util.ExcelUtil;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 预算数据录入Controller
 *
 * @description 预算数据录入管理接口，支持数据录入、批量导入、数据校验等功能
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-数据录入"})
@RequestMapping(value = "/accountant/budget/data")
@Slf4j
public class BudgetDataController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetDataService dataService;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    /**
     * 创建预算数据
     */
    @Operation(summary = "创建预算数据")
    @ApiOperation("创建预算数据")
    @PostMapping("/create")
    public MyJsonBean<BudgetData> create(@RequestBody @Validated BudgetData data) {
        MyJsonBean<BudgetData> result = new MyJsonBean<>();
        try {
            BudgetData created = dataService.create(data);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建预算数据异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询预算数据详情
     */
    @Operation(summary = "查询预算数据详情")
    @ApiOperation("查询预算数据详情")
    @GetMapping("/detail/{dataId}")
    public MyJsonBean<BudgetData> getDetail(
            @ApiParam(value = "数据ID", required = true) @PathVariable String dataId) {
        MyJsonBean<BudgetData> result = new MyJsonBean<>();
        try {
            BudgetData data = dataService.getById(dataId);
            if (data != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(data);
            } else {
                result.setCode(0);
                result.setMsg("数据不存在");
            }
        } catch (Exception e) {
            log.error("查询预算数据详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新预算数据
     */
    @Operation(summary = "更新预算数据")
    @ApiOperation("更新预算数据")
    @PutMapping("/update/{dataId}")
    public MyJsonBean<BudgetData> update(
            @ApiParam(value = "数据ID", required = true) @PathVariable String dataId,
            @RequestBody @Validated BudgetData data) {
        MyJsonBean<BudgetData> result = new MyJsonBean<>();
        try {
            data.setDataId(dataId);
            BudgetData updated = dataService.update(data);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (ServiceException ex) {
            log.error("更新预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新预算数据异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除预算数据
     */
    @Operation(summary = "删除预算数据")
    @ApiOperation("删除预算数据")
    @DeleteMapping("/delete/{dataId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "数据ID", required = true) @PathVariable String dataId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dataService.delete(dataId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除预算数据异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询预算数据列表
     */
    @Operation(summary = "分页查询预算数据列表")
    @ApiOperation("分页查询预算数据列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetData>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetData>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetData> pageResult = dataService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询预算数据列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量保存预算数据
     */
    @Operation(summary = "批量保存预算数据")
    @ApiOperation("批量保存预算数据")
    @PostMapping("/batch-save")
    public MyJsonBean<Void> batchSave(@RequestBody List<BudgetData> dataList) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dataService.batchSave(dataList);
            result.setCode(1);
            result.setMsg("批量保存成功");
        } catch (ServiceException ex) {
            log.error("批量保存预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量保存预算数据异常", e);
            result.setCode(0);
            result.setMsg("批量保存失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除预算数据
     */
    @Operation(summary = "批量删除预算数据")
    @ApiOperation("批量删除预算数据")
    @DeleteMapping("/batch-delete")
    public MyJsonBean<Void> batchDelete(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要删除的数据");
                return result;
            }
            dataService.batchDelete(ids);
            result.setCode(1);
            result.setMsg("批量删除成功");
        } catch (ServiceException ex) {
            log.error("批量删除预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量删除预算数据异常", e);
            result.setCode(0);
            result.setMsg("批量删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 提交预算数据
     */
    @Operation(summary = "提交预算数据")
    @ApiOperation("提交预算数据")
    @PutMapping("/submit/{dataId}")
    public MyJsonBean<Void> submit(
            @ApiParam(value = "数据ID", required = true) @PathVariable String dataId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dataService.submit(dataId);
            result.setCode(1);
            result.setMsg("提交成功");
        } catch (ServiceException ex) {
            log.error("提交预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("提交预算数据异常", e);
            result.setCode(0);
            result.setMsg("提交失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量提交预算数据
     */
    @Operation(summary = "批量提交预算数据")
    @ApiOperation("批量提交预算数据")
    @PostMapping("/batch-submit")
    public MyJsonBean<Void> batchSubmit(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要提交的数据");
                return result;
            }
            dataService.batchSubmit(ids);
            result.setCode(1);
            result.setMsg("批量提交成功");
        } catch (ServiceException ex) {
            log.error("批量提交预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量提交预算数据异常", e);
            result.setCode(0);
            result.setMsg("批量提交失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导入预算数据
     */
    @Operation(summary = "导入预算数据")
    @ApiOperation("导入预算数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importData(@RequestBody List<Map<String, Object>> dataList) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> importResult = dataService.importData(dataList);
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (ServiceException ex) {
            log.error("导入预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("导入预算数据异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导入Excel文件
     */
    @Operation(summary = "导入Excel文件")
    @ApiOperation("导入Excel文件")
    @PostMapping("/import-excel")
    public MyJsonBean<Map<String, Object>> importExcel(
            @ApiParam(value = "Excel文件", required = true) @RequestParam("file") MultipartFile file) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            if (!ExcelUtil.validateExcelFile(file)) {
                result.setCode(0);
                result.setMsg("请上传有效的Excel文件");
                return result;
            }

            List<BudgetData> dataList = ExcelUtil.importExcel(file, BudgetData.class);
            dataService.batchSave(dataList);

            Map<String, Object> importResult = new java.util.HashMap<>();
            importResult.put("total", dataList.size());
            importResult.put("success", dataList.size());
            importResult.put("fail", 0);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (ServiceException ex) {
            log.error("导入Excel失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("导入Excel异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出预算数据
     */
    @Operation(summary = "导出预算数据")
    @ApiOperation("导出预算数据")
    @PostMapping("/export")
    public void export(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetData> dataList = dataService.getPage(params).getTlist();
            if (dataList == null) dataList = new java.util.ArrayList<>();
            ExcelUtil.exportExcel(dataList, BudgetData.class, "预算数据", response);
        } catch (Exception e) {
            log.error("导出预算数据异常", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /**
     * 下载导入模板
     */
    @Operation(summary = "下载导入模板")
    @ApiOperation("下载导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            ExcelUtil.downloadTemplate(BudgetData.class, "预算数据导入模板", response);
        } catch (Exception e) {
            log.error("下载模板异常", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().println("{\"code\":0,\"msg\":\"下载模板失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /**
     * 校验预算数据
     */
    @Operation(summary = "校验预算数据")
    @ApiOperation("校验预算数据")
    @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validate(@RequestBody BudgetData data) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validationResult = dataService.validate(data);
            result.setCode(1);
            result.setMsg("校验完成");
            result.setData(validationResult);
        } catch (ServiceException ex) {
            log.error("校验预算数据失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("校验预算数据异常", e);
            result.setCode(0);
            result.setMsg("校验失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 计算差异
     */
    @Operation(summary = "计算差异")
    @ApiOperation("计算差异")
    @PostMapping("/{dataId}/variance")
    public MyJsonBean<Void> calculateVariance(
            @ApiParam(value = "数据ID", required = true) @PathVariable String dataId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dataService.calculateVariance(dataId);
            result.setCode(1);
            result.setMsg("计算差异成功");
        } catch (ServiceException ex) {
            log.error("计算差异失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("计算差异异常", e);
            result.setCode(0);
            result.setMsg("计算差异失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取组织列表
     */
    @Operation(summary = "获取组织列表")
    @ApiOperation("获取组织列表")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetOrganization> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0).orderByAsc("ORGANIZATION_CODE");
            List<BudgetOrganization> orgList = organizationMapper.selectList(qw);
            List<Map<String, Object>> orgs = new java.util.ArrayList<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("id", org.getOrganizationId());
                item.put("organizationId", org.getOrganizationId());
                item.put("organizationCode", org.getOrganizationCode());
                item.put("organizationName", org.getOrganizationName());
                item.put("label", org.getOrganizationName());
                item.put("value", org.getOrganizationId());
                orgs.add(item);
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

    /**
     * 获取预算科目列表
     */
    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/accounts")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", 0).orderByAsc("ACCOUNT_CODE");
            List<BudgetAccount> accountList = accountMapper.selectList(qw);
            List<Map<String, Object>> accounts = new java.util.ArrayList<>();
            for (BudgetAccount acc : accountList) {
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("id", acc.getAccountId());
                item.put("accountId", acc.getAccountId());
                item.put("accountCode", acc.getAccountCode());
                item.put("accountName", acc.getAccountName());
                item.put("label", acc.getAccountName());
                item.put("value", acc.getAccountId());
                accounts.add(item);
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

    /**
     * 获取数据变更历史（简单实现：返回该数据的操作记录）
     */
    @Operation(summary = "获取预算数据变更历史")
    @ApiOperation("获取预算数据变更历史")
    @GetMapping("/history/{dataId}")
    public MyJsonBean<List<Map<String, Object>>> getHistory(@PathVariable String dataId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            // 查询该数据的历史版本（通过数据ID关联查询）
            List<Map<String, Object>> historyList = dataService.getHistory(dataId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(historyList);
        } catch (Exception e) {
            log.error("获取变更历史异常, dataId={}", dataId, e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取审计日志
     */
    @Operation(summary = "获取预算数据审计日志")
    @ApiOperation("获取预算数据审计日志")
    @GetMapping("/audit/{dataId}")
    public MyJsonBean<List<Map<String, Object>>> getAuditLog(@PathVariable String dataId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> auditList = dataService.getAuditLog(dataId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(auditList);
        } catch (Exception e) {
            log.error("获取审计日志异常, dataId={}", dataId, e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

