package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.TblProcurementProject;
import com.huabo.cybermonitor.entity.TblSupplier;
import com.huabo.cybermonitor.service.ITblProcurementProjectService;
import com.huabo.cybermonitor.service.ITblSupplierService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblProcurementProjectQueryVO;
import com.huabo.cybermonitor.vo.TblSupplierQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @deprecated 该Controller已被 ProcurementPenetrationController 替代，
 *             路由完全重叠，禁用以避免 Ambiguous mapping 冲突。
 */
@Deprecated
@Tag(name = "采购供应链穿透式监管(旧)")
// @RestController  // 禁用：路由与 ProcurementPenetrationController 冲突
@RequestMapping("/v1/supervision/procurement/project")
@Slf4j
public class ProcurementController {

    @Autowired
    private ITblProcurementProjectService procurementProjectService;
    @Autowired
    private ITblSupplierService supplierService;

    @Operation(summary = "分页查询采购项目列表")
    @PostMapping("/list")
    public R<PageResult<TblProcurementProject>> list(@RequestBody TblProcurementProjectQueryVO queryVO) {
        try {
            return R.success(procurementProjectService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询采购项目列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询采购项目详情")
    @GetMapping("/{id}")
    public R<TblProcurementProject> detail(@PathVariable String id) {
        try {
            return R.success(procurementProjectService.getById(id));
        } catch (Exception e) {
            log.error("查询采购项目详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增采购项目")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblProcurementProject record) {
        try {
            return procurementProjectService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增采购项目失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新采购项目")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblProcurementProject record) {
        try {
            return procurementProjectService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新采购项目失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除采购项目")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return procurementProjectService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除采购项目失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除采购项目")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return procurementProjectService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除采购项目失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取采购统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestParam(required = false) String companyId) {
        try {
            return R.success(procurementProjectService.getStatistics(companyId));
        } catch (Exception e) {
            log.error("获取采购统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }

    // ========== 供应商相关接口 ==========

    @Operation(summary = "分页查询供应商列表")
    @PostMapping("/supplier/list")
    public R<PageResult<TblSupplier>> supplierList(@RequestBody TblSupplierQueryVO queryVO) {
        try {
            return R.success(supplierService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询供应商列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询供应商详情")
    @GetMapping("/supplier/{id}")
    public R<TblSupplier> supplierDetail(@PathVariable String id) {
        try {
            return R.success(supplierService.getById(id));
        } catch (Exception e) {
            log.error("查询供应商详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增供应商")
    @PostMapping("/supplier/add")
    public R<Boolean> addSupplier(@RequestBody TblSupplier record) {
        try {
            return supplierService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增供应商失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新供应商")
    @PostMapping("/supplier/update")
    public R<Boolean> updateSupplier(@RequestBody TblSupplier record) {
        try {
            return supplierService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新供应商失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }
}

