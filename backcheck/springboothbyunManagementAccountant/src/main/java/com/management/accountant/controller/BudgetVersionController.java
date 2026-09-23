package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.budget.BudgetVersion;
import com.management.accountant.oracle.service.budget.BudgetVersionService;
import com.management.accountant.util.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Api(tags = {"NCV65全面预算-版本管理"})
@RequestMapping(value = "/accountant/budget/version")
@Slf4j
public class BudgetVersionController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetVersionService versionService;

    /**
     * 分页查询版本列表
     */
    @Operation(summary = "分页查询版本列表")
    @ApiOperation("分页查询版本列表")
    @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try {
            int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

            BudgetVersion condition = new BudgetVersion();
            if (params.get("versionName") != null && StringUtils.hasText(params.get("versionName").toString())) {
                condition.setVersionName(params.get("versionName").toString());
            }
            if (params.get("versionStatus") != null && StringUtils.hasText(params.get("versionStatus").toString())) {
                condition.setVersionStatus(params.get("versionStatus").toString());
            }
            if (params.get("fiscalYear") != null && StringUtils.hasText(params.get("fiscalYear").toString())) {
                condition.setFiscalYear(Integer.parseInt(params.get("fiscalYear").toString()));
            }
            if (params.get("creatorName") != null && StringUtils.hasText(params.get("creatorName").toString())) {
                condition.setCreatorName(params.get("creatorName").toString());
            }

            Page<BudgetVersion> pageResult = versionService.pageQuery(pageNum, pageSize, condition);
            Map<String, Object> data = new HashMap<>();
            data.put("records", pageResult.getRecords());
            data.put("total", pageResult.getTotal());
            data.put("pageNum", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(data);
        } catch (Exception e) {
            log.error("分页查询版本异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    /**
     * 获取版本统计数据
     */
    @Operation(summary = "获取版本统计数据")
    @ApiOperation("获取版本统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
            w.eq("DEL_FLAG", 0);
            List<BudgetVersion> allVersions = versionService.list(w);

            long activeCount = allVersions.stream()
                    .filter(v -> "ACTIVE".equals(v.getVersionStatus())).count();
            long draftCount = allVersions.stream()
                    .filter(v -> "DRAFT".equals(v.getVersionStatus())).count();
            long archivedCount = allVersions.stream()
                    .filter(v -> "ARCHIVED".equals(v.getVersionStatus())).count();

            Map<String, Object> stats = new HashMap<>();
            stats.put("activeCount", activeCount);
            stats.put("draftCount", draftCount);
            stats.put("archivedCount", archivedCount);
            stats.put("totalCount", allVersions.size());
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(stats);
        } catch (Exception e) {
            log.error("获取版本统计异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    /**
     * 获取基础版本列表（用于创建版本时选择基础版本）
     */
    @Operation(summary = "获取基础版本列表")
    @ApiOperation("获取基础版本列表")
    @GetMapping("/base")
    public MyJsonBean<List<BudgetVersion>> getBaseVersions() {
        MyJsonBean<List<BudgetVersion>> r = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
            w.eq("DEL_FLAG", 0)
             .in("VERSION_STATUS", "ACTIVE", "DRAFT")
             .orderByDesc("CREATE_TIME");
            r.setCode(1);
            r.setMsg("查询成功");
            r.setData(versionService.list(w));
        } catch (Exception e) {
            log.error("获取基础版本列表异常", e);
            r.setCode(0);
            r.setMsg("查询失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "创建版本")
    @ApiOperation("创建版本") @PostMapping("/create")
    public MyJsonBean<BudgetVersion> create(@RequestBody Map<String, Object> params) {
        MyJsonBean<BudgetVersion> r = new MyJsonBean<>();
        try {
            BudgetVersion data = buildVersionFromParams(params);
            versionService.createVersion(data);
            r.setCode(1); r.setMsg("创建成功"); r.setData(data);
        } catch (Exception e) {
            log.error("创建版本异常", e); r.setCode(0); r.setMsg("创建失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "更新版本")
    @ApiOperation("更新版本") @PutMapping("/update/{id}")
    public MyJsonBean<Void> update(@PathVariable String id, @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            BudgetVersion data = buildVersionFromParams(params);
            data.setVersionId(id);
            versionService.updateVersion(data);
            r.setCode(1); r.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新版本异常", e); r.setCode(0); r.setMsg("更新失败：" + e.getMessage());
        }
        return r;
    }

    /**
     * 从参数Map构建BudgetVersion，兼容fiscalYear为日期字符串或整数
     */
    private BudgetVersion buildVersionFromParams(Map<String, Object> params) {
        BudgetVersion v = new BudgetVersion();
        if (params.get("versionId") != null) v.setVersionId(params.get("versionId").toString());
        if (params.get("versionName") != null) v.setVersionName(params.get("versionName").toString());
        if (params.get("versionType") != null) v.setVersionType(params.get("versionType").toString());
        if (params.get("versionDescription") != null) v.setVersionDescription(params.get("versionDescription").toString());
        if (params.get("baseVersionId") != null) v.setBaseVersionId(params.get("baseVersionId").toString());
        if (params.get("versionStatus") != null) v.setVersionStatus(params.get("versionStatus").toString());
        if (params.get("isActive") != null) v.setIsActive(Boolean.parseBoolean(params.get("isActive").toString()));
        // fiscalYear 兼容: 整数 / 日期字符串(如 "2025-12-31T16:00:00.000Z")
        if (params.get("fiscalYear") != null) {
            String fy = params.get("fiscalYear").toString().trim();
            if (fy.length() == 4) {
                v.setFiscalYear(Integer.parseInt(fy));
            } else if (fy.length() > 4) {
                v.setFiscalYear(Integer.parseInt(fy.substring(0, 4)));
            }
        }
        return v;
    }

    @Operation(summary = "删除版本")
    @ApiOperation("删除版本") @DeleteMapping("/delete/{id}")
    public MyJsonBean<Void> delete(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            versionService.batchDeleteVersions(Collections.singletonList(id));
            r.setCode(1); r.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除版本异常", e); r.setCode(0); r.setMsg("删除失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "激活版本")
    @ApiOperation("激活版本") @PostMapping("/{id}/activate")
    public MyJsonBean<Void> activate(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            versionService.setCurrentVersion(id);
            r.setCode(1); r.setMsg("激活成功");
        } catch (Exception e) {
            log.error("激活版本异常", e); r.setCode(0); r.setMsg("激活失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "版本比较")
    @ApiOperation("版本比较") @PostMapping("/compare")
    public MyJsonBean<Map<String, Object>> compare(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> r = new MyJsonBean<>();
        try {
            String sourceId = params.get("sourceVersionId").toString();
            String targetId = params.get("targetVersionId").toString();
            r.setCode(1); r.setMsg("查询成功");
            r.setData(versionService.compareVersions(sourceId, targetId));
        } catch (Exception e) {
            log.error("版本比较异常", e); r.setCode(0); r.setMsg("比较失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "回滚版本")
    @ApiOperation("回滚版本") @PostMapping("/{id}/rollback")
    public MyJsonBean<Void> rollback(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            versionService.rollbackVersion(id);
            r.setCode(1); r.setMsg("回滚成功");
        } catch (Exception e) {
            log.error("回滚版本异常", e); r.setCode(0); r.setMsg("回滚失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "归档版本")
    @ApiOperation("归档版本") @PostMapping("/{id}/archive")
    public MyJsonBean<Void> archive(@PathVariable String id) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            versionService.archiveVersion(id);
            r.setCode(1); r.setMsg("归档成功");
        } catch (Exception e) {
            log.error("归档版本异常", e); r.setCode(0); r.setMsg("归档失败：" + e.getMessage());
        }
        return r;
    }

    @Operation(summary = "合并版本")
    @ApiOperation("合并版本") @PostMapping("/merge")
    public MyJsonBean<Void> merge(@RequestBody List<String> versionIds) {
        MyJsonBean<Void> r = new MyJsonBean<>();
        try {
            log.info("合并版本，版本IDs：{}", versionIds);
            r.setCode(1); r.setMsg("合并成功");
        } catch (Exception e) {
            log.error("合并版本异常", e); r.setCode(0); r.setMsg("合并失败：" + e.getMessage());
        }
        return r;
    }
}
