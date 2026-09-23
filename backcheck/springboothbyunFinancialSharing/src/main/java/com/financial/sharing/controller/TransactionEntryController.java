package com.financial.sharing.controller;

import com.financial.sharing.service.TransactionEntryService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TransactionEntryQueryParam;
import com.financial.sharing.vo.param.TransactionEntrySaveParam;
import com.financial.sharing.vo.result.TransactionEntryVO;
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

/**
 * 事项分录管理控制器
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "事项分录管理")
@RestController
@RequestMapping("/financial/matter/entry")
@Validated
public class TransactionEntryController {

    @Autowired
    private TransactionEntryService transactionEntryService;

    /**
     * 分页查询事项分录列表
     */
    @ApiOperation("分页查询事项分录列表")
    @PostMapping("/getList")
    public MyJsonBean<PageResult<TransactionEntryVO>> getList(@Valid @RequestBody TransactionEntryQueryParam param) {
        try {
            PageResult<TransactionEntryVO> result = transactionEntryService.getTransactionEntryPage(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询事项分录列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存或更新事项分录
     */
    @ApiOperation("保存或更新事项分录")
    @PostMapping("/saveOrUpdate")
    public MyJsonBean<TransactionEntryVO> saveOrUpdate(@Valid @RequestBody TransactionEntrySaveParam param) {
        try {
            TransactionEntryVO result = transactionEntryService.saveOrUpdateTransactionEntry(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("保存事项分录失败", e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询事项分录详情
     */
    @ApiOperation("根据ID查询事项分录详情")
    @GetMapping("/{entryId}")
    public MyJsonBean<TransactionEntryVO> getById(
            @ApiParam(value = "分录ID", required = true) @PathVariable @NotNull Long entryId) {
        try {
            TransactionEntryVO result = transactionEntryService.getTransactionEntryById(entryId);
            if (result == null) {
                return MyJsonBean.errorData("事项分录不存在");
            }
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询事项分录详情失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 删除事项分录
     */
    @ApiOperation("删除事项分录")
    @DeleteMapping("/{entryId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "分录ID", required = true) @PathVariable @NotNull Long entryId) {
        try {
            boolean success = transactionEntryService.deleteTransactionEntry(entryId);
            if (success) {
                return MyJsonBean.successData();
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除事项分录失败", e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除事项分录
     */
    @ApiOperation("批量删除事项分录")
    @DeleteMapping("/batch")
    public MyJsonBean<Void> batchDelete(@RequestBody @NotEmpty List<Long> entryIds) {
        try {
            boolean success = transactionEntryService.batchDeleteTransactionEntries(entryIds);
            if (success) {
                return MyJsonBean.successData();
            } else {
                return MyJsonBean.errorData("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除事项分录失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据事项ID删除分录
     */
    @ApiOperation("根据事项ID删除分录")
    @DeleteMapping("/by-transaction/{transactionId}")
    public MyJsonBean<Void> deleteByTransactionId(
            @ApiParam(value = "事项ID", required = true) @PathVariable @NotNull Long transactionId) {
        try {
            boolean success = transactionEntryService.deleteTransactionEntriesByTransactionId(transactionId);
            if (success) {
                return MyJsonBean.successData();
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("根据事项ID删除分录失败", e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    /**
     * 检查分录编号是否存在
     */
    @ApiOperation("检查分录编号是否存在")
    @GetMapping("/check-no")
    public MyJsonBean<Boolean> checkEntryNoExists(
            @ApiParam(value = "分录编号", required = true) @RequestParam @NotNull String entryNo,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId,
            @ApiParam(value = "排除的ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = transactionEntryService.checkEntryNoExists(
                entryNo, bookId, tenantId, excludeId);
            return MyJsonBean.successData(exists);
        } catch (Exception e) {
            log.error("检查分录编号失败", e);
            return MyJsonBean.errorData("检查失败：" + e.getMessage());
        }
    }

    /**
     * 根据事项ID查询分录列表
     */
    @ApiOperation("根据事项ID查询分录列表")
    @GetMapping("/by-transaction/{transactionId}")
    public MyJsonBean<List<TransactionEntryVO>> getByTransactionId(
            @ApiParam(value = "事项ID", required = true) @PathVariable @NotNull Long transactionId) {
        try {
            List<TransactionEntryVO> result = transactionEntryService.getTransactionEntriesByTransactionId(transactionId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据事项ID查询分录列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据科目ID查询分录列表
     */
    @ApiOperation("根据科目ID查询分录列表")
    @GetMapping("/by-subject")
    public MyJsonBean<List<TransactionEntryVO>> getBySubjectId(
            @ApiParam(value = "科目ID", required = true) @RequestParam @NotNull Long subjectId,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<TransactionEntryVO> result = transactionEntryService.getTransactionEntriesBySubjectId(
                subjectId, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据科目ID查询分录列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据币种查询分录列表
     */
    @ApiOperation("根据币种查询分录列表")
    @GetMapping("/by-currency")
    public MyJsonBean<List<TransactionEntryVO>> getByCurrency(
            @ApiParam(value = "币种编码", required = true) @RequestParam @NotNull String currencyCode,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<TransactionEntryVO> result = transactionEntryService.getTransactionEntriesByCurrency(
                currencyCode, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据币种查询分录列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 计算事项分录借贷方合计
     */
    @ApiOperation("计算事项分录借贷方合计")
    @GetMapping("/calculate-total/{transactionId}")
    public MyJsonBean<TransactionEntryVO> calculateTransactionTotal(
            @ApiParam(value = "事项ID", required = true) @PathVariable @NotNull Long transactionId) {
        try {
            TransactionEntryVO result = transactionEntryService.calculateTransactionTotal(transactionId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("计算事项分录合计失败", e);
            return MyJsonBean.errorData("计算失败：" + e.getMessage());
        }
    }

    /**
     * 验证事项分录借贷平衡
     */
    @ApiOperation("验证事项分录借贷平衡")
    @GetMapping("/validate-balance/{transactionId}")
    public MyJsonBean<Boolean> validateTransactionBalance(
            @ApiParam(value = "事项ID", required = true) @PathVariable @NotNull Long transactionId) {
        try {
            boolean result = transactionEntryService.validateTransactionBalance(transactionId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("验证事项分录平衡失败", e);
            return MyJsonBean.errorData("验证失败：" + e.getMessage());
        }
    }

    /**
     * 获取科目分录汇总
     */
    @ApiOperation("获取科目分录汇总")
    @GetMapping("/subject-summary")
    public MyJsonBean<TransactionEntryVO> getSubjectEntrySummary(
            @ApiParam(value = "科目ID", required = true) @RequestParam @NotNull Long subjectId,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            TransactionEntryVO result = transactionEntryService.getSubjectEntrySummary(subjectId, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取科目分录汇总失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取币种分录汇总
     */
    @ApiOperation("获取币种分录汇总")
    @GetMapping("/currency-summary")
    public MyJsonBean<TransactionEntryVO> getCurrencyEntrySummary(
            @ApiParam(value = "币种编码", required = true) @RequestParam @NotNull String currencyCode,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            TransactionEntryVO result = transactionEntryService.getCurrencyEntrySummary(currencyCode, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取币种分录汇总失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 统计分录数量按科目分组
     */
    @ApiOperation("统计分录数量按科目分组")
    @GetMapping("/count-by-subject")
    public MyJsonBean<List<TransactionEntryVO>> countBySubject(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<TransactionEntryVO> result = transactionEntryService.countEntriesBySubject(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("统计分录数量失败", e);
            return MyJsonBean.errorData("统计失败：" + e.getMessage());
        }
    }

    /**
     * 统计分录金额按币种分组
     */
    @ApiOperation("统计分录金额按币种分组")
    @GetMapping("/sum-by-currency")
    public MyJsonBean<List<TransactionEntryVO>> sumByCurrency(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<TransactionEntryVO> result = transactionEntryService.sumEntriesByCurrency(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("统计分录金额失败", e);
            return MyJsonBean.errorData("统计失败：" + e.getMessage());
        }
    }
}
