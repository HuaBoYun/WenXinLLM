package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.entity.TblOverseasUnit;
import com.huabo.cybermonitor.service.ITblOverseasUnitService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TblOverseasUnitQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "境外单位穿透式监管")
@RestController
@RequestMapping("/v1/supervision/overseas/unit")
@Slf4j
public class OverseasUnitController {

    @Autowired
    private ITblOverseasUnitService overseasUnitService;
    @Autowired
    private com.huabo.cybermonitor.mapper.GzctOverseasInvestMapper overseasInvestMapper;
    @Autowired
    private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    @Operation(summary = "分页查询境外单位列表")
    @PostMapping("/list")
    public R<PageResult<TblOverseasUnit>> list(@RequestBody TblOverseasUnitQueryVO queryVO) {
        try {
            return R.success(overseasUnitService.selectByPage(queryVO));
        } catch (Exception e) {
            log.error("查询境外单位列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询境外单位详情")
    @GetMapping("/{id}")
    public R<TblOverseasUnit> detail(@PathVariable String id) {
        try {
            return R.success(overseasUnitService.getById(id));
        } catch (Exception e) {
            log.error("查询境外单位详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增境外单位")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody TblOverseasUnit record) {
        try {
            return overseasUnitService.addRecord(record) ? R.success(true) : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增境外单位失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新境外单位")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody TblOverseasUnit record) {
        try {
            if (StringUtils.isEmpty(record.getUnitId())) {
                return R.fail("单位ID不能为空");
            }
            return overseasUnitService.updateRecord(record) ? R.success(true) : R.fail("更新失败");
        } catch (Exception e) {
            log.error("更新境外单位失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除境外单位")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        try {
            return overseasUnitService.deleteRecord(id) ? R.success(true) : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除境外单位失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除境外单位")
    @PostMapping("/batch/delete")
    public R<Boolean> batchDelete(@RequestBody List<String> ids) {
        try {
            return overseasUnitService.removeByIds(ids) ? R.success(true) : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除境外单位失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取境外单位统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestParam(required = false) String companyId) {
        try {
            final String orgId = companyId;
            String orgPattern = orgQueryHelper.getOrgPathPattern(orgId);
            // 查 GZCT_OVERSEAS_INVEST（有 ORG_PATH）
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.huabo.cybermonitor.entity.GzctOverseasInvest> w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            if (orgPattern != null) {
                w.and(ww -> ww.like(com.huabo.cybermonitor.entity.GzctOverseasInvest::getOrgPath, orgPattern)
                    .or(sub -> sub.isNull(com.huabo.cybermonitor.entity.GzctOverseasInvest::getOrgPath)
                        .eq(com.huabo.cybermonitor.entity.GzctOverseasInvest::getUnitId, orgId)));
            }
            java.util.List<com.huabo.cybermonitor.entity.GzctOverseasInvest> all = overseasInvestMapper.selectList(w);
            long unitCount = all.size();
            long countryCount = all.stream().map(com.huabo.cybermonitor.entity.GzctOverseasInvest::getCountry).filter(c -> c != null).distinct().count();
            long riskCount = all.stream().filter(i -> "HIGH".equals(i.getRiskLevel())).count();
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("unitCount", unitCount);
            result.put("overseasCount", unitCount);
            result.put("totalUnits", unitCount);
            result.put("countryCount", countryCount);
            result.put("countries", countryCount);
            result.put("riskCount", riskCount);
            result.put("warningCount", riskCount);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取境外单位统计数据失败", e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }
}

