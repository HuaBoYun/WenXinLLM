package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.TblFinanceStatement;
import com.huabo.cybermonitor.entity.TblRelatedTransaction;
import com.huabo.cybermonitor.service.ITblFinanceStatementService;
import com.huabo.cybermonitor.service.ITblRelatedTransactionService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblFinanceStatementQueryVO;
import com.huabo.cybermonitor.vo.TblRelatedTransactionQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "财务穿透式监管")
@RestController
@RequestMapping("/v1/supervision/financial/report")
@Slf4j
public class FinanceStatementController {

    @Autowired
    private ITblFinanceStatementService financeStatementService;
    @Autowired
    private ITblRelatedTransactionService relatedTransactionService;

    @Operation(summary = "分页查询财务报表列表")
    @PostMapping("/list")
    public R<PageResult<TblFinanceStatement>> list(@RequestBody TblFinanceStatementQueryVO queryVO) {
        try {
            return R.success(financeStatementService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询财务报表列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询财务报表详情")
    @GetMapping("/{id}")
    public R<TblFinanceStatement> detail(@PathVariable String id) {
        try {
            return R.success(financeStatementService.getById(id));
        } catch (Exception e) {
            log.error("查询财务报表详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增财务报表")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblFinanceStatement record) {
        try {
            return financeStatementService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增财务报表失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新财务报表")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblFinanceStatement record) {
        try {
            if (StringUtils.isEmpty(record.getStatementId())) {
                return R.fail("报表ID不能为空");
            }
            return financeStatementService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新财务报表失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除财务报表")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return financeStatementService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除财务报表失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除财务报表")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return financeStatementService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除财务报表失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取财务统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestParam(required = false) String companyId) {
        try {
            return R.success(financeStatementService.getStatistics(companyId));
        } catch (Exception e) {
            log.error("获取财务统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }

    // ========== 关联交易相关接口 ==========

    @Operation(summary = "分页查询关联交易列表")
    @PostMapping("/related/list")
    public R<PageResult<TblRelatedTransaction>> relatedList(@RequestBody TblRelatedTransactionQueryVO queryVO) {
        try {
            return R.success(relatedTransactionService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询关联交易列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询关联交易详情")
    @GetMapping("/related/{id}")
    public R<TblRelatedTransaction> relatedDetail(@PathVariable String id) {
        try {
            return R.success(relatedTransactionService.getById(id));
        } catch (Exception e) {
            log.error("查询关联交易详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增关联交易")
    @PostMapping("/related/add")
    public R<Boolean> addRelated(@RequestBody TblRelatedTransaction record) {
        try {
            return relatedTransactionService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增关联交易失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新关联交易")
    @PostMapping("/related/update")
    public R<Boolean> updateRelated(@RequestBody TblRelatedTransaction record) {
        try {
            return relatedTransactionService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新关联交易失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }
}

