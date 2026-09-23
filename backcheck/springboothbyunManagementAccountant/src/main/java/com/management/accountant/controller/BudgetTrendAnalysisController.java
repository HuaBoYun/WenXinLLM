package com.management.accountant.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetTrendAnalysis;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetTrendAnalysisMapper;
import com.management.accountant.service.BudgetTrendAnalysisService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预算趋势分析Controller
 * 
 * @description 预算趋势分析接口，支持时间序列分析、趋势预测等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-趋势分析"})
@RequestMapping(value = "/accountant/budget/analysis/trend")
@Slf4j
public class BudgetTrendAnalysisController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetTrendAnalysisService trendAnalysisService;

    @Resource
    private BudgetTrendAnalysisMapper trendMapper;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    /**
     * 分页查询趋势分析列表（前端主查询接口）
     */
    @Operation(summary = "分页查询趋势分析列表")
    @ApiOperation("分页查询趋势分析列表")
    @PostMapping("/list")
    public MyJsonBean<Map<String, Object>> listTrendAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysisResult = trendAnalysisService.executeTrendAnalysis(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(analysisResult);
        } catch (ServiceException ex) {
            log.error("查询趋势分析列表失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("查询趋势分析列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 创建趋势分析记录
     */
    @Operation(summary = "创建趋势分析")
    @ApiOperation("创建趋势分析")
    @PostMapping("/create")
    public MyJsonBean<String> createTrendAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            BudgetTrendAnalysis entity = new BudgetTrendAnalysis();
            entity.setId(UUID.randomUUID().toString().replace("-", ""));
            entity.setAnalysisName(str(params.get("analysisName")));
            entity.setTrendType(str(params.get("trendType")));
            Object yr = params.get("budgetYear");
            if (yr != null && !yr.toString().isEmpty()) {
                try { entity.setBudgetYear(Integer.parseInt(yr.toString())); } catch (Exception ignored) {}
            }
            entity.setOrganizationId(str(params.get("organizationId")));
            entity.setOrganizationName(str(params.get("organizationName")));
            entity.setAccountId(str(params.get("accountId")));
            entity.setAccountName(str(params.get("accountName")));
            entity.setStartPeriod(str(params.get("startDate")));
            entity.setEndPeriod(str(params.get("endDate")));
            entity.setRemark(str(params.get("remark")));
            entity.setAnalysisStatus("draft");
            entity.setCreateBy("admin");
            entity.setCreateTime(new Date());
            entity.setDelFlag(0);
            trendMapper.insert(entity);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(entity.getId());
        } catch (Exception e) {
            log.error("创建趋势分析失败", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行趋势分析（兼容旧接口）
     */
    @Operation(summary = "执行趋势分析")
    @ApiOperation("执行趋势分析")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeTrendAnalysis(@RequestBody Map<String, Object> params) {
        return listTrendAnalysis(params);
    }

    /**
     * 获取趋势图表数据
     */
    @Operation(summary = "获取趋势图表数据")
    @ApiOperation("获取趋势图表数据")
    @PostMapping("/chart")
    public MyJsonBean<Map<String, Object>> getTrendChart(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = trendAnalysisService.getTrendChart(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取趋势图表数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出趋势分析报告（文件流）
     */
    @Operation(summary = "导出趋势分析报告")
    @ApiOperation("导出趋势分析报告")
    @PostMapping("/export")
    public void exportTrendReport(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            Map<String, Object> exportResult = trendAnalysisService.exportTrendReport(params);
            @SuppressWarnings("unchecked")
            List<BudgetTrendAnalysis> list = (List<BudgetTrendAnalysis>) exportResult.get("list");
            if (list == null) list = new java.util.ArrayList<>();
            String fileName = URLEncoder.encode("趋势分析报告", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            EasyExcel.write(response.getOutputStream(), BudgetTrendAnalysis.class)
                    .sheet("趋势分析数据")
                    .doWrite(list);
        } catch (Exception e) {
            log.error("导出趋势分析报告异常", e);
            response.setStatus(500);
        }
    }

    /**
     * 获取组织单元树形列表（el-cascader 格式：value/label/children）
     */
    @Operation(summary = "获取组织单元列表")
    @ApiOperation("获取组织单元列表")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetOrganization> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            wrapper.orderByAsc("SORT_ORDER", "ORGANIZATION_CODE");
            List<BudgetOrganization> orgList = organizationMapper.selectList(wrapper);

            // 构建 id -> node 映射，节点格式符合 el-cascader 需要的 {value, label, children}
            Map<String, Map<String, Object>> nodeMap = new LinkedHashMap<>();
            for (BudgetOrganization org : orgList) {
                Map<String, Object> node = new LinkedHashMap<>();
                node.put("value", org.getOrganizationId());
                node.put("label", org.getOrganizationName());
                node.put("id", org.getOrganizationId());
                node.put("name", org.getOrganizationName());
                node.put("parentId", org.getParentId());
                node.put("children", new ArrayList<>());
                nodeMap.put(org.getOrganizationId(), node);
            }

            // 组装树形结构
            List<Map<String, Object>> roots = new ArrayList<>();
            for (Map<String, Object> node : nodeMap.values()) {
                String parentId = (String) node.get("parentId");
                if (parentId == null || parentId.isEmpty() || !nodeMap.containsKey(parentId)) {
                    roots.add(node);
                } else {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> children = (List<Map<String, Object>>) nodeMap.get(parentId).get("children");
                    children.add(node);
                }
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(roots);
        } catch (Exception e) {
            log.error("获取组织单元列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预算科目列表（el-select 格式：value/label）
     */
    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/budget-accounts")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetAccount> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);
            wrapper.orderByAsc("SORT_ORDER", "ACCOUNT_CODE");
            List<BudgetAccount> accountList = accountMapper.selectList(wrapper);

            List<Map<String, Object>> accounts = new ArrayList<>();
            for (BudgetAccount acc : accountList) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("value", acc.getAccountId());
                item.put("label", acc.getAccountCode() + " - " + acc.getAccountName());
                item.put("id", acc.getAccountId());
                item.put("name", acc.getAccountName());
                accounts.add(item);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            log.error("获取预算科目列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    private String str(Object val) { return val == null ? "" : val.toString(); }

    @Operation(summary = "获取趋势分析统计数据")
    @ApiOperation("获取趋势分析统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = trendAnalysisService.getTrendStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取趋势分析统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取趋势分析图表数据")
    @ApiOperation("获取趋势分析图表数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = trendAnalysisService.getTrendChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取趋势分析图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新趋势分析
     */
    @Operation(summary = "更新趋势分析")
    @ApiOperation("更新趋势分析")
    @PostMapping("/update")
    public MyJsonBean<String> updateTrendAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            String id = str(params.get("id"));
            if (id.isEmpty()) {
                result.setCode(0);
                result.setMsg("id不能为空");
                return result;
            }
            BudgetTrendAnalysis entity = trendMapper.selectById(id);
            if (entity == null) {
                result.setCode(0);
                result.setMsg("记录不存在");
                return result;
            }
            if (params.containsKey("analysisName"))   entity.setAnalysisName(str(params.get("analysisName")));
            if (params.containsKey("trendType"))      entity.setTrendType(str(params.get("trendType")));
            if (params.containsKey("organizationId")) entity.setOrganizationId(str(params.get("organizationId")));
            if (params.containsKey("organizationName")) entity.setOrganizationName(str(params.get("organizationName")));
            if (params.containsKey("accountId"))      entity.setAccountId(str(params.get("accountId")));
            if (params.containsKey("accountName"))    entity.setAccountName(str(params.get("accountName")));
            if (params.containsKey("remark"))         entity.setRemark(str(params.get("remark")));
            Object yr = params.get("budgetYear");
            if (yr != null && !yr.toString().isEmpty()) {
                try { entity.setBudgetYear(Integer.parseInt(yr.toString())); } catch (Exception ignored) {}
            }
            entity.setUpdateBy("admin");
            entity.setUpdateTime(new Date());
            trendMapper.updateById(entity);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(id);
        } catch (Exception e) {
            log.error("更新趋势分析失败", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除趋势分析（逻辑删除）
     */
    @Operation(summary = "删除趋势分析")
    @ApiOperation("删除趋势分析")
    @PostMapping("/delete")
    public MyJsonBean<String> deleteTrendAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            String id = str(params.get("id"));
            if (id.isEmpty()) {
                result.setCode(0);
                result.setMsg("id不能为空");
                return result;
            }
            BudgetTrendAnalysis entity = trendMapper.selectById(id);
            if (entity == null) {
                result.setCode(0);
                result.setMsg("记录不存在");
                return result;
            }
            trendMapper.logicDeleteById(id);
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(id);
        } catch (Exception e) {
            log.error("删除趋势分析失败", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }
}

