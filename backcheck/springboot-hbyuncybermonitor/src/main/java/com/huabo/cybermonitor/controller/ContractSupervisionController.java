package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.TblContractRecord;
import com.huabo.cybermonitor.entity.TblContractDispute;
import com.huabo.cybermonitor.service.ITblContractRecordService;
import com.huabo.cybermonitor.service.ITblContractDisputeService;
import com.huabo.cybermonitor.util.OrgQueryHelper;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblContractRecordQueryVO;
import com.huabo.cybermonitor.vo.TblContractDisputeQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "合同穿透式监管")
@RestController
@RequestMapping("/v1/supervision/contract/record")
@Slf4j
public class ContractSupervisionController {

    @Autowired
    private ITblContractRecordService contractRecordService;
    @Autowired
    private ITblContractDisputeService contractDisputeService;
    @Autowired
    private OrgQueryHelper orgQueryHelper;

    @Operation(summary = "分页查询合同记录列表")
    @PostMapping("/list")
    public R<PageResult<TblContractRecord>> list(@RequestBody TblContractRecordQueryVO queryVO) {
        try {
            return R.success(contractRecordService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询合同记录列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询合同记录详情")
    @GetMapping("/{id}")
    public R<TblContractRecord> detail(@PathVariable String id) {
        try {
            return R.success(contractRecordService.getById(id));
        } catch (Exception e) {
            log.error("查询合同记录详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增合同记录")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblContractRecord record) {
        try {
            return contractRecordService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增合同记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新合同记录")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblContractRecord record) {
        try {
            if (StringUtils.isEmpty(record.getContractId())) {
                return R.fail("合同ID不能为空");
            }
            return contractRecordService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新合同记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除合同记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return contractRecordService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除合同记录失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除合同记录")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return contractRecordService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失���");
        } catch (Exception e) {
            log.error("批量删除合同记录失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取合同统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestParam(required = false) String companyId) {
        try {
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            return R.success(contractRecordService.getStatisticsByOrgPattern(orgPattern));
        } catch (Exception e) {
            log.error("获取合同统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }

    // ========== 合同纠纷相关接口 ==========

    @Operation(summary = "分页查询合同纠纷列表")
    @PostMapping("/dispute/list")
    public R<PageResult<TblContractDispute>> disputeList(@RequestBody TblContractDisputeQueryVO queryVO) {
        try {
            return R.success(contractDisputeService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询合同纠纷列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询合同纠纷详情")
    @GetMapping("/dispute/{id}")
    public R<TblContractDispute> disputeDetail(@PathVariable String id) {
        try {
            return R.success(contractDisputeService.getById(id));
        } catch (Exception e) {
            log.error("查询合同纠纷详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增合同纠纷")
    @PostMapping("/dispute/add")
    public R<Boolean> addDispute(@RequestBody TblContractDispute record) {
        try {
            return contractDisputeService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增合同纠纷失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新合同纠纷")
    @PostMapping("/dispute/update")
    public R<Boolean> updateDispute(@RequestBody TblContractDispute record) {
        try {
            return contractDisputeService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新合同纠纷失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }
}

