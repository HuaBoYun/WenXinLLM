package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.TblMilitaryTask;
import com.huabo.cybermonitor.service.ITblMilitaryTaskService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblMilitaryTaskQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "军品业务穿透式监管")
@RestController
@RequestMapping("/v1/supervision/military/task")
@Slf4j
public class MilitaryTaskController {

    @Autowired
    private ITblMilitaryTaskService militaryTaskService;

    @Operation(summary = "分页查询军品任务列表")
    @PostMapping("/list")
    public R<PageResult<TblMilitaryTask>> list(@RequestBody TblMilitaryTaskQueryVO queryVO) {
        try {
            return R.success(militaryTaskService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询军品任务列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询军品任务详情")
    @GetMapping("/{id}")
    public R<TblMilitaryTask> detail(@PathVariable String id) {
        try {
            return R.success(militaryTaskService.getById(id));
        } catch (Exception e) {
            log.error("查询军品任务详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增军品任务")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblMilitaryTask record) {
        try {
            return militaryTaskService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增军品任务失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新军品任务")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblMilitaryTask record) {
        try {
            if (StringUtils.isEmpty(record.getTaskId())) {
                return R.fail("任务ID不能为空");
            }
            return militaryTaskService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新军品任务失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除军品任务")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return militaryTaskService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除军品任务失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除军品任务")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return militaryTaskService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除军品任务失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    // statistics 方法已移至 MilitarySupervisionController，避免路径冲突
    // @GetMapping("/statistics")
    // public R<Map<String, Object>> statistics(...)
}

