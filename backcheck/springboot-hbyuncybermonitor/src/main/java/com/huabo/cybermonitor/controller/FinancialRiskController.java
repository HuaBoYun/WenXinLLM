package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.TblFinancingRecord;
import com.huabo.cybermonitor.entity.TblGuaranteeRecord;
import com.huabo.cybermonitor.service.ITblFinancingRecordService;
import com.huabo.cybermonitor.service.ITblGuaranteeRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblFinancingRecordQueryVO;
import com.huabo.cybermonitor.vo.TblGuaranteeRecordQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "金融风险穿透式监管")
@RestController
@RequestMapping("/v1/supervision/financial-risk/financing")
@Slf4j
public class FinancialRiskController {

    @Autowired
    private ITblFinancingRecordService financingRecordService;
    @Autowired
    private ITblGuaranteeRecordService guaranteeRecordService;

    @Operation(summary = "分页查询融资记录列表")
    @PostMapping("/list")
    public R<PageResult<TblFinancingRecord>> list(@RequestBody TblFinancingRecordQueryVO queryVO) {
        try {
            return R.success(financingRecordService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询融资记录列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询融资记录详情")
    @GetMapping("/{id}")
    public R<TblFinancingRecord> detail(@PathVariable String id) {
        try {
            return R.success(financingRecordService.getById(id));
        } catch (Exception e) {
            log.error("查询融资记录详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增融资记录")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblFinancingRecord record) {
        try {
            return financingRecordService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增融资记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新融资记录")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblFinancingRecord record) {
        try {
            return financingRecordService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新融资记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除融资记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return financingRecordService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除融资记录失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除融资记录")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return financingRecordService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除融资记录失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    // statistics 方法已移至 FinancialRiskSupervisionController（含组织穿透逻辑），避免路径冲突
    // @GetMapping("/statistics")
    // public R<Map<String, Object>> statistics(...)

    // ========== 担保记录相关接口 ==========

    @Operation(summary = "分页查询担保记录列表")
    @PostMapping("/guarantee/list")
    public R<PageResult<TblGuaranteeRecord>> guaranteeList(@RequestBody TblGuaranteeRecordQueryVO queryVO) {
        try {
            return R.success(guaranteeRecordService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询担保记录列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询担保记录详情")
    @GetMapping("/guarantee/{id}")
    public R<TblGuaranteeRecord> guaranteeDetail(@PathVariable String id) {
        try {
            return R.success(guaranteeRecordService.getById(id));
        } catch (Exception e) {
            log.error("查询担保记录详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增担保记录")
    @PostMapping("/guarantee/add")
    public R<Boolean> addGuarantee(@RequestBody TblGuaranteeRecord record) {
        try {
            return guaranteeRecordService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增担保记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新担保记录")
    @PostMapping("/guarantee/update")
    public R<Boolean> updateGuarantee(@RequestBody TblGuaranteeRecord record) {
        try {
            return guaranteeRecordService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新担保记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除担保记录")
    @DeleteMapping("/guarantee/{id}")
    public R<Boolean> deleteGuarantee(@PathVariable String id) {
        try {
            return guaranteeRecordService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除担保记录失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除担保记录")
    @PostMapping("/guarantee/batch/delete")
    public R<Boolean> batchDeleteGuarantee(@RequestBody List<String> ids) {
        try {
            return guaranteeRecordService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除担保记录失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }
}
