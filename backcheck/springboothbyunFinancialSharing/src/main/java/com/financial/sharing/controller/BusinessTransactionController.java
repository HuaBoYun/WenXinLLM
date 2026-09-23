package com.financial.sharing.controller;

import com.financial.sharing.service.BusinessTransactionService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.BusinessTransactionQueryParam;
import com.financial.sharing.vo.param.BusinessTransactionSaveParam;
import com.financial.sharing.vo.result.BusinessTransactionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;

/**
 * 业务事项管理控制器
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "业务事项管理")
@RestController
@RequestMapping("/transaction/business")
@Validated
public class BusinessTransactionController {

    @Autowired
    private BusinessTransactionService businessTransactionService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 分页查询业务事项列表
     */
    @ApiOperation("分页查询业务事项列表")
    @PostMapping("/getList")
    public MyJsonBean<PageResult<BusinessTransactionVO>> getList(@Valid @RequestBody BusinessTransactionQueryParam param) {
        try {
            PageResult<BusinessTransactionVO> result = businessTransactionService.getBusinessTransactionPage(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询业务事项列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存业务事项
     */
    @ApiOperation("保存业务事项")
    @PostMapping("/save")
    public MyJsonBean<BusinessTransactionVO> save(@Valid @RequestBody BusinessTransactionSaveParam param) {
        try {
            BusinessTransactionVO result = businessTransactionService.saveOrUpdateBusinessTransaction(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("保存业务事项失败", e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    /**
     * 更新业务事项
     */
    @ApiOperation("更新业务事项")
    @PutMapping("/update")
    public MyJsonBean<BusinessTransactionVO> update(@Valid @RequestBody BusinessTransactionSaveParam param) {
        try {
            BusinessTransactionVO result = businessTransactionService.saveOrUpdateBusinessTransaction(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("更新业务事项失败", e);
            return MyJsonBean.errorData("更新失败：" + e.getMessage());
        }
    }

    /**
     * 保存或更新业务事项
     */
    @ApiOperation("保存或更新业务事项")
    @PostMapping("/saveOrUpdate")
    public MyJsonBean<BusinessTransactionVO> saveOrUpdate(@Valid @RequestBody BusinessTransactionSaveParam param) {
        try {
            BusinessTransactionVO result = businessTransactionService.saveOrUpdateBusinessTransaction(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("保存业务事项失败", e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询业务事项详情
     */
    @ApiOperation("根据ID查询业务事项详情")
    @GetMapping("/{transactionId}")
    public MyJsonBean<BusinessTransactionVO> getById(
            @ApiParam(value = "事项ID", required = true) @PathVariable @NotNull Long transactionId) {
        try {
            BusinessTransactionVO result = businessTransactionService.getBusinessTransactionById(transactionId);
            if (result == null) {
                return MyJsonBean.errorData("业务事项不存在");
            }
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询业务事项详情失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 删除业务事项
     */
    @ApiOperation("删除业务事项")
    @DeleteMapping("/{transactionId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "事项ID", required = true) @PathVariable @NotNull Long transactionId) {
        try {
            boolean success = businessTransactionService.deleteBusinessTransaction(transactionId);
            if (success) {
                return MyJsonBean.successData();
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除业务事项失败", e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除业务事项
     */
    @ApiOperation("批量删除业务事项")
    @DeleteMapping("/batch")
    public MyJsonBean<Void> batchDelete(@RequestBody @NotEmpty List<Long> transactionIds) {
        try {
            boolean success = businessTransactionService.batchDeleteBusinessTransactions(transactionIds);
            if (success) {
                return MyJsonBean.successData();
            } else {
                return MyJsonBean.errorData("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除业务事项失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除业务事项（兼容旧API）
     */
    @ApiOperation("批量删除业务事项")
    @DeleteMapping("/batch-delete")
    public MyJsonBean<Void> batchDeleteCompat(@RequestBody @NotEmpty List<Long> transactionIds) {
        try {
            boolean success = businessTransactionService.batchDeleteBusinessTransactions(transactionIds);
            if (success) {
                return MyJsonBean.successData();
            } else {
                return MyJsonBean.errorData("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除业务事项失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 处理事项数据
     */
    @ApiOperation("处理事项数据")
    @PostMapping("/process")
    public MyJsonBean<Void> processMatterData(@RequestBody @NotEmpty List<Long> transactionIds) {
        try {
            businessTransactionService.processTransactions(transactionIds);
            return MyJsonBean.successData();
        } catch (Exception e) {
            log.error("处理事项数据失败", e);
            return MyJsonBean.errorData("处理失败：" + e.getMessage());
        }
    }

    /**
     * 批量处理事项数据
     */
    @ApiOperation("批量处理事项数据")
    @PostMapping("/batch-process")
    public MyJsonBean<Void> batchProcessMatterData(@RequestBody java.util.Map<String, Object> data) {
        try {
            businessTransactionService.batchProcessTransactions(data);
            return MyJsonBean.successData();
        } catch (Exception e) {
            log.error("批量处理事项数据失败", e);
            return MyJsonBean.errorData("批量处理失败：" + e.getMessage());
        }
    }

    /**
     * 重新处理失败的事项数据
     */
    @ApiOperation("重新处理失败的事项数据")
    @PostMapping("/reprocess")
    public MyJsonBean<Void> reprocessFailedMatterData(@RequestBody @NotEmpty List<Long> transactionIds) {
        try {
            businessTransactionService.reprocessTransactions(transactionIds);
            return MyJsonBean.successData();
        } catch (Exception e) {
            log.error("重新处理事项数据失败", e);
            return MyJsonBean.errorData("重新处理失败：" + e.getMessage());
        }
    }

    /**
     * 获取事项处理进度
     */
    @ApiOperation("获取事项处理进度")
    @GetMapping("/process-progress/{batchId}")
    public MyJsonBean<java.util.Map<String, Object>> getMatterProcessProgress(
            @PathVariable String batchId) {
        try {
            java.util.Map<String, Object> progress = businessTransactionService.getProcessProgress(batchId);
            return MyJsonBean.successData(progress);
        } catch (Exception e) {
            log.error("获取事项处理进度失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 更新事项状态
     */
    @ApiOperation("更新事项状态")
    @PutMapping("/{transactionId}/status")
    public MyJsonBean<Void> updateStatus(
            @ApiParam(value = "事项ID", required = true) @PathVariable @NotNull Long transactionId,
            @ApiParam(value = "事项状态", required = true) @RequestParam @NotNull Integer transactionStatus) {
        try {
            boolean success = businessTransactionService.updateTransactionStatus(transactionId, transactionStatus);
            if (success) {
                return MyJsonBean.successData();
            } else {
                return MyJsonBean.errorData("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新事项状态失败", e);
            return MyJsonBean.errorData("状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新事项状态
     */
    @ApiOperation("批量更新事项状态")
    @PutMapping("/batch/status")
    public MyJsonBean<Void> batchUpdateStatus(
            @RequestBody @NotEmpty List<Long> transactionIds,
            @ApiParam(value = "事项状态", required = true) @RequestParam @NotNull Integer transactionStatus) {
        try {
            boolean success = businessTransactionService.batchUpdateTransactionStatus(transactionIds, transactionStatus);
            if (success) {
                return MyJsonBean.successData();
            } else {
                return MyJsonBean.errorData("批量状态更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新事项状态失败", e);
            return MyJsonBean.errorData("批量状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 检查事项编号是否存在
     */
    @ApiOperation("检查事项编号是否存在")
    @GetMapping("/check-no")
    public MyJsonBean<Boolean> checkTransactionNoExists(
            @ApiParam(value = "事项编号", required = true) @RequestParam @NotNull String transactionNo,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId,
            @ApiParam(value = "排除的ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = businessTransactionService.checkTransactionNoExists(
                transactionNo, bookId, tenantId, excludeId);
            return MyJsonBean.successData(exists);
        } catch (Exception e) {
            log.error("检查事项编号失败", e);
            return MyJsonBean.errorData("检查失败：" + e.getMessage());
        }
    }

    /**
     * 根据事项类型查询业务事项列表
     */
    @ApiOperation("根据事项类型查询业务事项列表")
    @GetMapping("/by-type")
    public MyJsonBean<List<BusinessTransactionVO>> getByType(
            @ApiParam(value = "事项类型", required = true) @RequestParam @NotNull String transactionType,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<BusinessTransactionVO> result = businessTransactionService.getBusinessTransactionsByType(
                transactionType, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据类型查询业务事项失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据日期范围查询业务事项列表
     */
    @ApiOperation("根据日期范围查询业务事项列表")
    @GetMapping("/by-date-range")
    public MyJsonBean<List<BusinessTransactionVO>> getByDateRange(
            @ApiParam(value = "开始日期", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate startDate,
            @ApiParam(value = "结束日期", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate endDate,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<BusinessTransactionVO> result = businessTransactionService.getBusinessTransactionsByDateRange(
                startDate, endDate, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据日期范围查询业务事项失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据状态查询业务事项列表
     */
    @ApiOperation("根据状态查询业务事项列表")
    @GetMapping("/by-status")
    public MyJsonBean<List<BusinessTransactionVO>> getByStatus(
            @ApiParam(value = "事项状态", required = true) @RequestParam @NotNull Integer transactionStatus,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<BusinessTransactionVO> result = businessTransactionService.getBusinessTransactionsByStatus(
                transactionStatus, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据状态查询业务事项失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取事项类型列表
     */
    @ApiOperation("获取事项类型列表")
    @GetMapping("/types")
    public MyJsonBean<List<String>> getTransactionTypes(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<String> result = businessTransactionService.getTransactionTypes(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取事项类型列表失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取来源系统列表
     */
    @ApiOperation("获取来源系统列表")
    @GetMapping("/source-systems")
    public MyJsonBean<List<String>> getSourceSystems(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<String> result = businessTransactionService.getSourceSystems(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取来源系统列表失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 统计事项数量按状态分组
     */
    @ApiOperation("统计事项数量按状态分组")
    @GetMapping("/count-by-status")
    public MyJsonBean<List<BusinessTransactionVO>> countByStatus(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<BusinessTransactionVO> result = businessTransactionService.countTransactionsByStatus(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("统计事项数量失败", e);
            return MyJsonBean.errorData("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计事项数量按类型分组
     */
    @ApiOperation("统计事项数量按类型分组")
    @GetMapping("/count-by-type")
    public MyJsonBean<List<BusinessTransactionVO>> countByType(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<BusinessTransactionVO> result = businessTransactionService.countTransactionsByType(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("统计事项数量失败", e);
            return MyJsonBean.errorData("统计失败：" + e.getMessage());
        }
    }

    /**
     * 批量导入事项数据
     */
    @ApiOperation("批量导入事项数据")
    @PostMapping("/import")
    public MyJsonBean<java.util.Map<String, Object>> importMatterData(
            @RequestParam("file") MultipartFile file) {
        try {
            java.util.Map<String, Object> result = businessTransactionService.importTransactions(file);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("导入事项数据失败", e);
            return MyJsonBean.errorData("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出事项数据
     */
    @ApiOperation("导出事项数据")
    @PostMapping("/export")
    public MyJsonBean<String> exportMatterData(@RequestBody java.util.Map<String, Object> exportParams) {
        try {
            String fileUrl = businessTransactionService.exportTransactions(exportParams);
            return MyJsonBean.successData(fileUrl);
        } catch (Exception e) {
            log.error("导出事项数据失败", e);
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }

    /**
     * 根据业务事项生成凭证
     */
    @ApiOperation("根据业务事项生成凭证")
    @PostMapping("/generate-vouchers")
    public MyJsonBean<java.util.Map<String, Object>> generateVouchers(@RequestBody java.util.Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            @SuppressWarnings("unchecked")
            List<Object> transactionIdsTemp = (List<Object>) params.get("transactionIds");
            List<Long> transactionIds = new ArrayList<>();
            if (transactionIdsTemp != null) {
                for (Object id : transactionIdsTemp) {
                    if (id instanceof Integer) {
                        transactionIds.add(((Integer) id).longValue());
                    } else if (id instanceof Long) {
                        transactionIds.add((Long) id);
                    } else if (id instanceof String) {
                        transactionIds.add(Long.valueOf(id.toString()));
                    }
                }
            }
            Long templateId = params.get("templateId") != null ? Long.valueOf(params.get("templateId").toString()) : null;

            if (transactionIds == null || transactionIds.isEmpty()) {
                return MyJsonBean.errorData("业务事项ID不能为空");
            }

            java.util.Map<String, Object> result = businessTransactionService.generateVouchersFromTransactions(
                transactionIds, templateId, loginStaff);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("生成凭证失败", e);
            return MyJsonBean.errorData("生成凭证失败：" + e.getMessage());
        }
    }

    /**
     * 预览凭证生成结果
     */
    @ApiOperation("预览凭证生成结果")
    @PostMapping("/preview-voucher-generation")
    public MyJsonBean<java.util.Map<String, Object>> previewVoucherGeneration(@RequestBody java.util.Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> transactionIdsTemp = (List<Object>) params.get("transactionIds");
            List<Long> transactionIds = new ArrayList<>();
            if (transactionIdsTemp != null) {
                for (Object id : transactionIdsTemp) {
                    if (id instanceof Integer) {
                        transactionIds.add(((Integer) id).longValue());
                    } else if (id instanceof Long) {
                        transactionIds.add((Long) id);
                    } else if (id instanceof String) {
                        transactionIds.add(Long.valueOf(id.toString()));
                    }
                }
            }
            Long templateId = params.get("templateId") != null ? Long.valueOf(params.get("templateId").toString()) : null;

            if (transactionIds == null || transactionIds.isEmpty()) {
                return MyJsonBean.errorData("业务事项ID不能为空");
            }

            java.util.Map<String, Object> result = businessTransactionService.previewVoucherGeneration(
                transactionIds, templateId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("预览凭证生成失败", e);
            return MyJsonBean.errorData("预览凭证生成失败：" + e.getMessage());
        }
    }
}
