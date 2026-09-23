package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.TblInvestigationTask;
import com.huabo.cybermonitor.entity.TblRectificationRecord;
import com.huabo.cybermonitor.service.ITblInvestigationTaskService;
import com.huabo.cybermonitor.service.ITblRectificationRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblInvestigationTaskQueryVO;
import com.huabo.cybermonitor.vo.TblRectificationRecordQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "协同核查平台", description = "核查任务管理、整改跟踪")
@RestController
@RequestMapping("/v1/supervision/investigation/task")
@Slf4j
public class InvestigationController {

    @Autowired
    private ITblInvestigationTaskService investigationTaskService;
    @Autowired
    private ITblRectificationRecordService rectificationRecordService;

    @Operation(summary = "分页查询核查任务列表")
    @PostMapping("/list")
    public R<PageResult<TblInvestigationTask>> list(@RequestBody TblInvestigationTaskQueryVO queryVO) {
        try {
            return R.success(investigationTaskService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询核查任务列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询核查任务详情")
    @GetMapping("/{id}")
    public R<TblInvestigationTask> detail(@PathVariable String id) {
        try {
            return R.success(investigationTaskService.getById(id));
        } catch (Exception e) {
            log.error("查询核查任务详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增核查任务")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblInvestigationTask task) {
        try {
            return investigationTaskService.addTask(task) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增核查任务失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新核查任务")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblInvestigationTask task) {
        try {
            if (StringUtils.isEmpty(task.getTaskId())) {
                return R.fail("任务ID不能为空");
            }
            return investigationTaskService.updateTask(task) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新核查任务失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除核查任务")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return investigationTaskService.deleteTask(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除核查任务失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除核查任务")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return investigationTaskService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除核查任务失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取核查统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestParam(required = false) String companyId) {
        try {
            return R.success(investigationTaskService.getStatistics(companyId));
        } catch (Exception e) {
            log.error("获取核查统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }

    // ========== 整改记录相关接口 ==========

    @Operation(summary = "分页查询整改记录列表")
    @PostMapping("/rectification/list")
    public R<PageResult<TblRectificationRecord>> rectificationList(@RequestBody TblRectificationRecordQueryVO queryVO) {
        try {
            return R.success(rectificationRecordService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询整改记录列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询整改记录详情")
    @GetMapping("/rectification/{id}")
    public R<TblRectificationRecord> rectificationDetail(@PathVariable String id) {
        try {
            return R.success(rectificationRecordService.getById(id));
        } catch (Exception e) {
            log.error("查询整改记录详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增整改记录")
    @PostMapping("/rectification/add")
    public R<Boolean> addRectification(@RequestBody TblRectificationRecord record) {
        try {
            return rectificationRecordService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增整改记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新整改记录")
    @PostMapping("/rectification/update")
    public R<Boolean> updateRectification(@RequestBody TblRectificationRecord record) {
        try {
            return rectificationRecordService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新整改记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }
}

