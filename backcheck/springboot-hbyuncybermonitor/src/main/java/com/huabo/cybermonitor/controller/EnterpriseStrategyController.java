package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.GzctEnterpriseStrategy;
import com.huabo.cybermonitor.mapper.GzctEnterpriseStrategyMapper;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

@Tag(name = "企业战略管理(旧)", description = "已废弃，请使用StrategyManagementController")
@RestController
@RequestMapping("/v1/enterprise/strategy-legacy")
@Slf4j
public class EnterpriseStrategyController {

    @Autowired
    private GzctEnterpriseStrategyMapper strategyMapper;

    @Operation(summary = "分页查询战略列表")
    @PostMapping("/list")
    public R<PageResult<GzctEnterpriseStrategy>> list(@RequestBody Map<String, Object> params) {
        try {
            int currentPage = params.get("currentPage") != null ? Integer.parseInt(params.get("currentPage").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            String strategyType = params.get("strategyType") != null ? params.get("strategyType").toString() : null;
            String status = params.get("status") != null ? params.get("status").toString() : null;
            String strategyName = params.get("strategyName") != null ? params.get("strategyName").toString() : null;

            LambdaQueryWrapper<GzctEnterpriseStrategy> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(enterpriseId)) wrapper.eq(GzctEnterpriseStrategy::getEnterpriseId, enterpriseId);
            if (StringUtils.hasText(strategyType)) wrapper.eq(GzctEnterpriseStrategy::getStrategyType, strategyType);
            if (StringUtils.hasText(status)) wrapper.eq(GzctEnterpriseStrategy::getStatus, status);
            if (StringUtils.hasText(strategyName)) wrapper.like(GzctEnterpriseStrategy::getStrategyName, strategyName);
            wrapper.orderByDesc(GzctEnterpriseStrategy::getCreateTime);

            List<GzctEnterpriseStrategy> all = strategyMapper.selectList(wrapper);
            int total = all.size();
            int fromIndex = (currentPage - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);
            List<GzctEnterpriseStrategy> records = fromIndex < total ? all.subList(fromIndex, toIndex) : new ArrayList<>();

            PageResult<GzctEnterpriseStrategy> pageResult = new PageResult<>();
            pageResult.setTotalRecord(total);
            pageResult.setCurrentPage(currentPage);
            pageResult.setPageSize(pageSize);
            pageResult.setTotalPage((total + pageSize - 1) / pageSize);
            pageResult.setPageNumber(currentPage);
            pageResult.setTlist(records);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询战略列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取战略详情")
    @GetMapping("/{id}")
    public R<GzctEnterpriseStrategy> detail(@PathVariable String id) {
        try {
            GzctEnterpriseStrategy strategy = strategyMapper.selectById(id);
            return strategy != null ? R.success(strategy) : R.fail("记录不存在");
        } catch (Exception e) {
            log.error("查询战略详情失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增战略")
    @PostMapping("/add")
    public R<String> add(@RequestBody GzctEnterpriseStrategy strategy) {
        try {
            strategy.setId(UUID.randomUUID().toString().replace("-", ""));
            strategy.setCreateTime(LocalDateTime.now());
            if (!StringUtils.hasText(strategy.getStatus())) strategy.setStatus("规划中");
            strategyMapper.insert(strategy);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增战略失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新战略")
    @PostMapping("/update")
    public R<String> update(@RequestBody GzctEnterpriseStrategy strategy) {
        try {
            strategy.setUpdateTime(LocalDateTime.now());
            strategyMapper.updateById(strategy);
            return R.success("更新成功");
        } catch (Exception e) {
            log.error("更新战略失败", e);
            return R.fail("更新失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除战略")
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable String id) {
        try {
            strategyMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除战略失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除战略")
    @PostMapping("/batch/delete")
    public R<String> batchDelete(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的记录");
            strategyMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除战略失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出战略数据")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            List<GzctEnterpriseStrategy> list = strategyMapper.selectList(
                    new LambdaQueryWrapper<GzctEnterpriseStrategy>().orderByDesc(GzctEnterpriseStrategy::getCreateTime));
            String[] headers = {"企业名称", "战略名称", "战略类型", "目标", "关键举措", "起始年度", "结束年度", "进度率", "状态", "负责人"};
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            SXSSFSheet sheet = workbook.createSheet("企业战略");
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctEnterpriseStrategy s = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(s.getEnterpriseName() != null ? s.getEnterpriseName() : "");
                row.createCell(1).setCellValue(s.getStrategyName() != null ? s.getStrategyName() : "");
                row.createCell(2).setCellValue(s.getStrategyType() != null ? s.getStrategyType() : "");
                row.createCell(3).setCellValue(s.getObjective() != null ? s.getObjective() : "");
                row.createCell(4).setCellValue(s.getKeyMeasures() != null ? s.getKeyMeasures() : "");
                row.createCell(5).setCellValue(s.getStartYear() != null ? s.getStartYear().toString() : "");
                row.createCell(6).setCellValue(s.getEndYear() != null ? s.getEndYear().toString() : "");
                row.createCell(7).setCellValue(s.getProgressRate() != null ? s.getProgressRate().toString() : "0");
                row.createCell(8).setCellValue(s.getStatus() != null ? s.getStatus() : "");
                row.createCell(9).setCellValue(s.getResponsiblePerson() != null ? s.getResponsiblePerson() : "");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("企业战略数据.xlsx", "UTF-8"));
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            log.error("导出战略数据失败", e);
        }
    }

    @Operation(summary = "战略统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        try {
            List<GzctEnterpriseStrategy> all = strategyMapper.selectList(null);
            long activePlans = all.stream().filter(s -> "执行中".equals(s.getStatus())).count();
            long completedMilestones = all.stream().filter(s -> "已完成".equals(s.getStatus())).count();
            BigDecimal achievementRate = BigDecimal.ZERO;
            if (!all.isEmpty()) {
                BigDecimal sum = all.stream()
                        .map(s -> s.getProgressRate() != null ? s.getProgressRate() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                achievementRate = sum.divide(new BigDecimal(all.size()), 2, RoundingMode.HALF_UP);
            }
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("activePlans", activePlans);
            stats.put("achievementRate", achievementRate);
            stats.put("completedMilestones", completedMilestones);
            stats.put("totalStrategies", all.size());
            return R.success(stats);
        } catch (Exception e) {
            log.error("查询战略统计失败", e);
            return R.fail("查询统计失败: " + e.getMessage());
        }
    }
}