package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetAccountExportDTO;
import com.management.accountant.service.BudgetAccountService;
import com.management.accountant.util.ExcelUtil;
import com.management.accountant.util.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = {"NCV65全面预算-科目管理"})
@RequestMapping(value = "/accountant/budget/account")
@Slf4j
public class BudgetAccountController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAccountService accountService;

    @Operation(summary = "创建科目")
    @ApiOperation("创建科目")
    @PostMapping("/create")
    public MyJsonBean<BudgetAccount> create(@RequestBody @Validated BudgetAccount account) {
        MyJsonBean<BudgetAccount> result = new MyJsonBean<>();
        try {
            BudgetAccount created = accountService.create(account);

            // 保存科目映射
            if (account.getAccountMappings() != null && !account.getAccountMappings().isEmpty()) {
                accountService.saveAccountMappings(created.getAccountId(), account.getAccountMappings());
            }

            result.setCode(1); result.setMsg("创建成功"); result.setData(created);
        } catch (ServiceException ex) {
            result.setCode(0); result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建科目异常", e); result.setCode(0); result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "查询科目详情")
    @ApiOperation("查询科目详情")
    @GetMapping("/detail/{accountId}")
    public MyJsonBean<BudgetAccount> getDetail(@PathVariable String accountId) {
        MyJsonBean<BudgetAccount> result = new MyJsonBean<>();
        try {
            BudgetAccount account = accountService.getById(accountId);
            if (account != null) { result.setCode(1); result.setMsg("查询成功"); result.setData(account); }
            else { result.setCode(0); result.setMsg("科目不存在"); }
        } catch (Exception e) {
            log.error("查询科目详情异常", e); result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新科目")
    @ApiOperation("更新科目")
    @PutMapping("/update/{accountId}")
    public MyJsonBean<Void> update(@PathVariable String accountId, @RequestBody @Validated BudgetAccount account) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            account.setAccountId(accountId);
            accountService.update(account);

            // 保存科目映射
            if (account.getAccountMappings() != null && !account.getAccountMappings().isEmpty()) {
                accountService.saveAccountMappings(accountId, account.getAccountMappings());
            }

            result.setCode(1); result.setMsg("更新成功");
        } catch (ServiceException ex) {
            result.setCode(0); result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新科目异常", e); result.setCode(0); result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除科目")
    @ApiOperation("删除科目")
    @DeleteMapping("/delete/{accountId}")
    public MyJsonBean<Void> delete(@PathVariable String accountId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            accountService.delete(accountId);
            result.setCode(1); result.setMsg("删除成功");
        } catch (ServiceException ex) {
            result.setCode(0); result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除科目异常", e); result.setCode(0); result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "分页查询科目列表")
    @ApiOperation("分页查询科目列表")
    @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("查询成功"); result.setData(accountService.getPage(params));
        } catch (Exception e) {
            log.error("分页查询科目异常", e); result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取科目树")
    @ApiOperation("获取科目树")
    @GetMapping("/tree")
    public MyJsonBean<List<Map<String, Object>>> getTree() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("查询成功"); result.setData(accountService.getAccountTree());
        } catch (Exception e) {
            log.error("获取科目树异常", e); result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取上级科目列表")
    @ApiOperation("获取上级科目列表")
    @GetMapping("/parents")
    public MyJsonBean<List<BudgetAccount>> getParents() {
        MyJsonBean<List<BudgetAccount>> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("查询成功"); result.setData(accountService.getParentAccounts());
        } catch (Exception e) {
            log.error("获取上级科目异常", e); result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新科目状态")
    @ApiOperation("更新科目状态")
    @PutMapping("/{accountId}/status")
    public MyJsonBean<Void> updateStatus(@PathVariable String accountId, @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            Boolean isActive = (Boolean) params.get("isActive");
            accountService.updateStatus(accountId, isActive);
            result.setCode(1); result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新科目状态异常", e); result.setCode(0); result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量验证科目")
    @ApiOperation("批量验证科目")
    @PostMapping("/batch-validate")
    public MyJsonBean<Map<String, Object>> batchValidate(@RequestBody List<String> ids) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("验证完成"); result.setData(accountService.batchValidate(ids));
        } catch (Exception e) {
            log.error("批量验证异常", e); result.setCode(0); result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取科目统计")
    @ApiOperation("获取科目统计")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1); result.setMsg("查询成功"); result.setData(accountService.getStats());
        } catch (Exception e) {
            log.error("获取科目统计异常", e); result.setCode(0); result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出科目数据")
    @ApiOperation("导出科目数据")
    @PostMapping("/export")
    public void exportAccounts(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetAccount> data = accountService.getExportData(params);
            List<BudgetAccountExportDTO> exportList = new java.util.ArrayList<>();
            for (BudgetAccount a : data) { exportList.add(BudgetAccountExportDTO.fromEntity(a)); }
            ExcelUtil.exportExcel(exportList, BudgetAccountExportDTO.class, "预算科目", response);
        } catch (Exception e) {
            log.error("导出科目数据异常", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    @Operation(summary = "导出单个科目")
    @ApiOperation("导出单个科目")
    @GetMapping("/{accountId}/export")
    public void exportSingle(@PathVariable String accountId, HttpServletResponse response) {
        try {
            BudgetAccount a = accountService.getByIdDirect(accountId);
            List<BudgetAccountExportDTO> exportList = new java.util.ArrayList<>();
            if (a != null) { exportList.add(BudgetAccountExportDTO.fromEntity(a)); }
            ExcelUtil.exportExcel(exportList, BudgetAccountExportDTO.class, "预算科目_" + accountId, response);
        } catch (Exception e) {
            log.error("导出单个科目异常", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    @Operation(summary = "导入科目数据")
    @ApiOperation("导入科目数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importAccounts(@RequestParam("file") MultipartFile file) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            if (!ExcelUtil.validateExcelFile(file)) {
                result.setCode(0); result.setMsg("请上传有效的Excel文件(.xlsx/.xls)"); return result;
            }
            List<BudgetAccountExportDTO> dtoList = ExcelUtil.importExcel(file, BudgetAccountExportDTO.class);
            List<BudgetAccount> accounts = new java.util.ArrayList<>();
            for (BudgetAccountExportDTO dto : dtoList) { accounts.add(dto.toEntity()); }
            result.setCode(1); result.setMsg("导入完成"); result.setData(accountService.importAccounts(accounts));
        } catch (Exception e) {
            log.error("导入科目异常", e); result.setCode(0); result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "下载导入模板")
    @ApiOperation("下载导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            ExcelUtil.downloadTemplate(BudgetAccountExportDTO.class, "预算科目导入模板", response);
        } catch (Exception e) {
            log.error("下载模板异常", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"下载模板失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }
}
