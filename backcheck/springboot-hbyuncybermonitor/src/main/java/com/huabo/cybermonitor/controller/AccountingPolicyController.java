package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.TblAccountingPolicy;
import com.huabo.cybermonitor.service.ITblAccountingPolicyService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblAccountingPolicyQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会计穿透式监管")
@RestController
@RequestMapping("/v1/supervision/accounting/policy")
@Slf4j
public class AccountingPolicyController {

    @Autowired
    private ITblAccountingPolicyService accountingPolicyService;

    @Operation(summary = "分页查询会计政策列表")
    @PostMapping("/list")
    public R<PageResult<TblAccountingPolicy>> list(@RequestBody TblAccountingPolicyQueryVO queryVO) {
        try {
            return R.success(accountingPolicyService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询会计政策列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询会计政策详情")
    @GetMapping("/{id}")
    public R<TblAccountingPolicy> detail(@PathVariable String id) {
        try {
            return R.success(accountingPolicyService.getById(id));
        } catch (Exception e) {
            log.error("查询会计政策详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增会计政策")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblAccountingPolicy record) {
        try {
            return accountingPolicyService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增会计政策失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新会计政策")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblAccountingPolicy record) {
        try {
            if (StringUtils.isEmpty(record.getPolicyId())) {
                return R.fail("政策ID不能为空");
            }
            return accountingPolicyService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新会计政策失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除会计政策")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return accountingPolicyService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除会计政策失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除会计政策")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return accountingPolicyService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除会计政策失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }
}

