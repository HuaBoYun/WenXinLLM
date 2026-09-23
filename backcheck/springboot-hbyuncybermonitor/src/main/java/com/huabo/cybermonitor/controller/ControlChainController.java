package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.ControlChain;
import com.huabo.cybermonitor.mapper.ControlChainMapper;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import io.swagger.v3.oas.annotations.Operation;

/**
 * 控制链分析 Controller
 */
@RestController
@RequestMapping("/v1/supervision/equity/control-chain")
public class ControlChainController {

    @Autowired
    private ControlChainMapper controlChainMapper;

    @Operation(summary = "list")
    @PostMapping("/list")
    public R<PageResult<ControlChain>> list(@RequestBody Map<String, Object> params) {
        int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

        LambdaQueryWrapper<ControlChain> wrapper = new LambdaQueryWrapper<>();
        if (params.get("enterpriseId") != null && StringUtils.hasText(params.get("enterpriseId").toString())) {
            wrapper.eq(ControlChain::getEnterpriseId, params.get("enterpriseId").toString());
        }
        if (params.get("enterpriseName") != null && StringUtils.hasText(params.get("enterpriseName").toString())) {
            wrapper.like(ControlChain::getEnterpriseName, params.get("enterpriseName").toString());
        }
        if (params.get("controlType") != null && StringUtils.hasText(params.get("controlType").toString())) {
            wrapper.eq(ControlChain::getControlType, params.get("controlType").toString());
        }
        if (params.get("stabilityLevel") != null && StringUtils.hasText(params.get("stabilityLevel").toString())) {
            wrapper.eq(ControlChain::getStabilityLevel, params.get("stabilityLevel").toString());
        }
        if (params.get("riskLevel") != null && StringUtils.hasText(params.get("riskLevel").toString())) {
            wrapper.eq(ControlChain::getRiskLevel, params.get("riskLevel").toString());
        }
        if (params.get("minStrength") != null && StringUtils.hasText(params.get("minStrength").toString())) {
            try { wrapper.ge(ControlChain::getControlStrength, new BigDecimal(params.get("minStrength").toString())); } catch (NumberFormatException ignored) {}
        }
        if (params.get("maxStrength") != null && StringUtils.hasText(params.get("maxStrength").toString())) {
            try { wrapper.le(ControlChain::getControlStrength, new BigDecimal(params.get("maxStrength").toString())); } catch (NumberFormatException ignored) {}
        }
        if (params.get("maxLength") != null && StringUtils.hasText(params.get("maxLength").toString())) {
            try { wrapper.le(ControlChain::getChainLength, Integer.parseInt(params.get("maxLength").toString())); } catch (NumberFormatException ignored) {}
        }

        // 排序支持
        String orderBy = params.get("orderBy") != null ? params.get("orderBy").toString() : null;
        String orderDirection = params.get("orderDirection") != null ? params.get("orderDirection").toString() : "DESC";
        if (StringUtils.hasText(orderBy)) {
            boolean isAsc = "ASC".equalsIgnoreCase(orderDirection);
            switch (orderBy) {
                case "controlStrength":
                    if (isAsc) wrapper.orderByAsc(ControlChain::getControlStrength);
                    else wrapper.orderByDesc(ControlChain::getControlStrength);
                    break;
                case "chainLength":
                    if (isAsc) wrapper.orderByAsc(ControlChain::getChainLength);
                    else wrapper.orderByDesc(ControlChain::getChainLength);
                    break;
                case "nodeCount":
                    if (isAsc) wrapper.orderByAsc(ControlChain::getNodeCount);
                    else wrapper.orderByDesc(ControlChain::getNodeCount);
                    break;
                case "createTime":
                    if (isAsc) wrapper.orderByAsc(ControlChain::getCreateTime);
                    else wrapper.orderByDesc(ControlChain::getCreateTime);
                    break;
                default:
                    wrapper.orderByDesc(ControlChain::getCreateTime);
                    break;
            }
        } else {
            wrapper.orderByDesc(ControlChain::getCreateTime);
        }

        // 使用PageHelper分页（避免与MyBatis-Plus分页插件冲突）
        PageHelper.startPage(pageNumber, pageSize);
        List<ControlChain> records = controlChainMapper.selectList(wrapper);
        PageInfo<ControlChain> pageInfo = new PageInfo<>(records);

        PageResult<ControlChain> pageResult = new PageResult<>();
        pageResult.setTlist(pageInfo.getList());
        pageResult.setTotalRecord((int) pageInfo.getTotal());
        pageResult.setCurrentPage(pageNumber);
        pageResult.setPageNumber(pageNumber);
        pageResult.setTotalPage(pageInfo.getPages());
        pageResult.setPageSize(pageSize);
        return R.success(pageResult);
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public R<ControlChain> getById(@PathVariable("id") String id) {
        ControlChain chain = controlChainMapper.selectById(id);
        if (chain == null) {
            return R.fail("记录不存在");
        }
        return R.success(chain);
    }

    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<String> add(@RequestBody ControlChain controlChain) {
        controlChain.setCreateTime(LocalDateTime.now());
        controlChain.setUpdateTime(LocalDateTime.now());
        controlChainMapper.insert(controlChain);
        return R.success("新增成功");
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public R<String> update(@RequestBody ControlChain controlChain) {
        controlChain.setUpdateTime(LocalDateTime.now());
        controlChainMapper.updateById(controlChain);
        return R.success("更新成功");
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable("id") String id) {
        controlChainMapper.deleteById(id);
        return R.success("删除成功");
    }

    @Operation(summary = "批量操作")
    @PostMapping("/batch/delete")
    public R<String> batchDelete(@RequestBody Map<String, Object> params) {
        List<String> ids = (List<String>) params.get("ids");
        if (ids == null || ids.isEmpty()) {
            return R.fail("请选择要删除的记录");
        }
        controlChainMapper.deleteBatchIds(ids);
        return R.success("批量删除成功");
    }

    @Operation(summary = "")
    @PostMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestBody(required = false) Map<String, Object> params) {
        Map<String, Object> stats = new HashMap<>();
        LambdaQueryWrapper<ControlChain> wrapper = new LambdaQueryWrapper<>();
        Long totalChains = controlChainMapper.selectCount(wrapper);
        stats.put("totalChains", totalChains);

        List<ControlChain> allChains = controlChainMapper.selectList(null);
        BigDecimal avgStrength = BigDecimal.ZERO;
        if (!allChains.isEmpty()) {
            BigDecimal sum = allChains.stream()
                    .map(ControlChain::getControlStrength)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            avgStrength = sum.divide(new BigDecimal(allChains.size()), 2, RoundingMode.HALF_UP);
        }
        stats.put("avgStrength", avgStrength);

        LambdaQueryWrapper<ControlChain> loopWrapper = new LambdaQueryWrapper<>();
        loopWrapper.eq(ControlChain::getHasLoop, 1);
        Long loopCount = controlChainMapper.selectCount(loopWrapper);
        stats.put("loopCount", loopCount);

        LambdaQueryWrapper<ControlChain> riskWrapper = new LambdaQueryWrapper<>();
        riskWrapper.in(ControlChain::getRiskLevel, Arrays.asList("HIGH", "CRITICAL"));
        Long riskChains = controlChainMapper.selectCount(riskWrapper);
        stats.put("riskChains", riskChains);

        return R.success(stats);
    }

    @Operation(summary = "控制链图谱数据")
    @PostMapping("/map")
    public R<Map<String, Object>> map(@RequestBody(required = false) Map<String, Object> params) {
        if (params == null) params = new HashMap<>();
        String chainId = params.get("chainId") != null ? params.get("chainId").toString() : null;
        String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;

        LambdaQueryWrapper<ControlChain> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(chainId)) {
            wrapper.eq(ControlChain::getChainId, chainId);
        } else if (StringUtils.hasText(enterpriseId)) {
            wrapper.eq(ControlChain::getEnterpriseId, enterpriseId);
        }
        wrapper.orderByAsc(ControlChain::getChainLength);
        List<ControlChain> list = controlChainMapper.selectList(wrapper);

        // 构建图谱节点和连接
        Set<String> nodeNameSet = new LinkedHashSet<>();
        List<Map<String, Object>> links = new ArrayList<>();

        for (ControlChain chain : list) {
            // 添加企业节点
            if (StringUtils.hasText(chain.getEnterpriseName())) {
                nodeNameSet.add(chain.getEnterpriseName());
            }
            // 添加控制方节点
            if (StringUtils.hasText(chain.getControllerName())) {
                nodeNameSet.add(chain.getControllerName());
            }
            // 解析chainPath构建链接
            if (StringUtils.hasText(chain.getChainPath())) {
                String[] pathNodes = chain.getChainPath().split("->");
                for (String pn : pathNodes) {
                    nodeNameSet.add(pn.trim());
                }
                for (int i = 0; i < pathNodes.length - 1; i++) {
                    Map<String, Object> link = new HashMap<>();
                    link.put("source", pathNodes[i].trim());
                    link.put("target", pathNodes[i + 1].trim());
                    BigDecimal linkStrength = chain.getControlStrength() != null ? chain.getControlStrength() : BigDecimal.ZERO;
                    link.put("label", linkStrength + "%");
                    link.put("controlType", chain.getControlType());
                    links.add(link);
                }
            } else {
                // 无路径时使用controllerName -> enterpriseName
                if (StringUtils.hasText(chain.getControllerName()) && StringUtils.hasText(chain.getEnterpriseName())) {
                    Map<String, Object> link = new HashMap<>();
                    link.put("source", chain.getControllerName());
                    link.put("target", chain.getEnterpriseName());
                    BigDecimal linkStrength = chain.getControlStrength() != null ? chain.getControlStrength() : BigDecimal.ZERO;
                    link.put("label", linkStrength + "%");
                    link.put("controlType", chain.getControlType());
                    links.add(link);
                }
            }
        }

        // 构建节点列表
        List<Map<String, Object>> nodes = new ArrayList<>();
        for (String nodeName : nodeNameSet) {
            Map<String, Object> node = new HashMap<>();
            node.put("name", nodeName);
            // 根据节点是否为控制方或被控方设置类别和大小
            boolean isController = list.stream().anyMatch(c -> nodeName.equals(c.getControllerName()));
            boolean isEnterprise = list.stream().anyMatch(c -> nodeName.equals(c.getEnterpriseName()));
            if (isController && !isEnterprise) {
                node.put("category", "controller");
                node.put("symbolSize", 60);
            } else if (isEnterprise && !isController) {
                node.put("category", "enterprise");
                node.put("symbolSize", 40);
            } else if (isController && isEnterprise) {
                node.put("category", "both");
                node.put("symbolSize", 50);
            } else {
                node.put("category", "intermediate");
                node.put("symbolSize", 30);
            }
            nodes.add(node);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("links", links);
        result.put("totalNodes", nodes.size());
        result.put("totalLinks", links.size());
        return R.success(result);
    }

    @Operation(summary = "导出")
    @PostMapping("/export")
    public void export(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params == null) params = new HashMap<>();

            LambdaQueryWrapper<ControlChain> wrapper = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.hasText(params.get("enterpriseId").toString())) {
                wrapper.eq(ControlChain::getEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("enterpriseName") != null && StringUtils.hasText(params.get("enterpriseName").toString())) {
                wrapper.like(ControlChain::getEnterpriseName, params.get("enterpriseName").toString());
            }
            if (params.get("controlType") != null && StringUtils.hasText(params.get("controlType").toString())) {
                wrapper.eq(ControlChain::getControlType, params.get("controlType").toString());
            }
            if (params.get("stabilityLevel") != null && StringUtils.hasText(params.get("stabilityLevel").toString())) {
                wrapper.eq(ControlChain::getStabilityLevel, params.get("stabilityLevel").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.hasText(params.get("riskLevel").toString())) {
                wrapper.eq(ControlChain::getRiskLevel, params.get("riskLevel").toString());
            }
            if (params.get("minStrength") != null && StringUtils.hasText(params.get("minStrength").toString())) {
                try { wrapper.ge(ControlChain::getControlStrength, new BigDecimal(params.get("minStrength").toString())); } catch (NumberFormatException ignored) {}
            }
            if (params.get("maxStrength") != null && StringUtils.hasText(params.get("maxStrength").toString())) {
                try { wrapper.le(ControlChain::getControlStrength, new BigDecimal(params.get("maxStrength").toString())); } catch (NumberFormatException ignored) {}
            }
            wrapper.orderByDesc(ControlChain::getCreateTime);
            List<ControlChain> list = controlChainMapper.selectList(wrapper);

            // 使用CSV格式导出（避免POI版本与commons-io冲突）
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String filename = "控制链分析数据_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv";
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            java.io.PrintWriter w = response.getWriter();
            w.write("\uFEFF"); // BOM头，确保Excel正确识别UTF-8
            // 表头
            w.println("企业名称,控制方,控制类型,控制强度(%),链路长度,节点数量,稳定性,环路检测,风险等级,最后分析时间");
            // 数据行
            for (ControlChain c : list) {
                w.println(
                    csvCell(c.getEnterpriseName()) + "," +
                    csvCell(c.getControllerName()) + "," +
                    csvCell(mapControlType(c.getControlType())) + "," +
                    csvCell(c.getControlStrength() != null ? c.getControlStrength().toString() : "") + "," +
                    csvCell(c.getChainLength() != null ? c.getChainLength().toString() : "") + "," +
                    csvCell(c.getNodeCount() != null ? c.getNodeCount().toString() : "") + "," +
                    csvCell(mapStabilityLevel(c.getStabilityLevel())) + "," +
                    csvCell(c.getHasLoop() != null && c.getHasLoop() == 1 ? "存在" : "无") + "," +
                    csvCell(mapRiskLevel(c.getRiskLevel())) + "," +
                    csvCell(c.getLastAnalysisTime() != null ? c.getLastAnalysisTime().format(dtf) : "")
                );
            }
            w.flush();
        } catch (Exception e) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    /** CSV单元格转义：包含逗号、引号、换行时用双引号包裹 */
    private String csvCell(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String mapControlType(String type) {
        if (type == null) return "";
        switch (type) {
            case "DIRECT": return "直接控制";
            case "INDIRECT": return "间接控制";
            case "MIXED": return "混合控制";
            case "PROXY": return "代理控制";
            default: return type;
        }
    }

    private String mapStabilityLevel(String level) {
        if (level == null) return "";
        switch (level) {
            case "HIGH": return "高稳定";
            case "MEDIUM": return "中稳定";
            case "LOW": return "低稳定";
            case "UNSTABLE": return "不稳定";
            case "STABLE": return "稳定";
            case "RELATIVELY_STABLE": return "相对稳定";
            default: return level;
        }
    }

    private String mapRiskLevel(String level) {
        if (level == null) return "";
        switch (level) {
            case "HIGH": return "高";
            case "MEDIUM": return "中";
            case "LOW": return "低";
            case "CRITICAL": return "严重";
            default: return level;
        }
    }

    @Operation(summary = "批量更新")
    @PostMapping("/batch/update")
    public R<String> batchUpdate(@RequestBody Map<String, Object> params) {
        List<String> ids = (List<String>) params.get("chainIds");
        if (ids == null || ids.isEmpty()) {
            ids = (List<String>) params.get("ids");
        }
        if (ids == null || ids.isEmpty()) {
            return R.fail("请选择要更新的记录");
        }
        String action = params.get("action") != null ? params.get("action").toString() : null;
        for (String id : ids) {
            ControlChain chain = controlChainMapper.selectById(id);
            if (chain != null) {
                if (params.get("riskLevel") != null) chain.setRiskLevel(params.get("riskLevel").toString());
                if (params.get("stabilityLevel") != null) chain.setStabilityLevel(params.get("stabilityLevel").toString());
                if (params.get("status") != null) chain.setStatus(params.get("status").toString());
                // 批量分析时更新最后分析时间
                if ("analyze".equalsIgnoreCase(action)) {
                    chain.setLastAnalysisTime(LocalDateTime.now());
                }
                chain.setUpdateTime(LocalDateTime.now());
                controlChainMapper.updateById(chain);
            }
        }
        return R.success("批量更新成功");
    }

    @Operation(summary = "控制路径分析")
    @PostMapping("/paths")
    public R<Map<String, Object>> analyzeControlPaths(@RequestBody(required = false) Map<String, Object> params) {
        if (params == null) params = new HashMap<>();
        String chainId = params.get("chainId") != null ? params.get("chainId").toString() : null;

        if (StringUtils.hasText(chainId)) {
            // 查询单条链的路径详情
            ControlChain chain = controlChainMapper.selectById(chainId);
            if (chain == null) {
                return R.fail("控制链不存在");
            }
            Map<String, Object> result = new HashMap<>();
            result.put("chainId", chain.getChainId());
            result.put("enterpriseName", chain.getEnterpriseName());
            result.put("controllerName", chain.getControllerName());
            result.put("controlType", mapControlType(chain.getControlType()));
            result.put("controlStrength", chain.getControlStrength());
            result.put("chainLength", chain.getChainLength());

            // 解析chainPath字段构建路径列表
            List<Map<String, Object>> pathList = new ArrayList<>();
            String chainPath = chain.getChainPath();
            if (StringUtils.hasText(chainPath)) {
                String[] nodes = chainPath.split("->");
                for (int i = 0; i < nodes.length - 1; i++) {
                    Map<String, Object> segment = new HashMap<>();
                    segment.put("from", nodes[i].trim());
                    segment.put("to", nodes[i + 1].trim());
                    segment.put("step", i + 1);
                    // 计算每段的控制强度（均匀递减模型）
                    BigDecimal segmentStrength = chain.getControlStrength() != null
                            ? chain.getControlStrength().multiply(new BigDecimal(nodes.length - 1 - i))
                            .divide(new BigDecimal(nodes.length - 1), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;
                    segment.put("strength", segmentStrength);
                    segment.put("type", i == 0 ? "DIRECT" : "INDIRECT");
                    pathList.add(segment);
                }
            }
            result.put("paths", pathList);

            // 构建路径描述列表
            List<Map<String, Object>> pathDescList = new ArrayList<>();
            Map<String, Object> mainPath = new HashMap<>();
            mainPath.put("desc", chainPath != null ? chainPath : "未设置路径");
            mainPath.put("length", chain.getChainLength());
            mainPath.put("strength", chain.getControlStrength());
            mainPath.put("type", mapControlType(chain.getControlType()));
            pathDescList.add(mainPath);
            result.put("pathDetails", pathDescList);

            return R.success(result);
        } else {
            // 兼容旧接口：按enterpriseId查询
            LambdaQueryWrapper<ControlChain> wrapper = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.hasText(params.get("enterpriseId").toString())) {
                wrapper.eq(ControlChain::getEnterpriseId, params.get("enterpriseId").toString());
            }
            List<ControlChain> chains = controlChainMapper.selectList(wrapper);
            List<Map<String, Object>> paths = new ArrayList<>();
            for (ControlChain chain : chains) {
                Map<String, Object> path = new HashMap<>();
                path.put("chainId", chain.getChainId());
                path.put("enterpriseName", chain.getEnterpriseName());
                path.put("controlType", chain.getControlType());
                path.put("controlStrength", chain.getControlStrength());
                path.put("chainLength", chain.getChainLength());
                path.put("stabilityLevel", chain.getStabilityLevel());
                path.put("desc", chain.getChainPath() != null ? chain.getChainPath() : "");
                path.put("length", chain.getChainLength());
                path.put("strength", chain.getControlStrength());
                path.put("type", mapControlType(chain.getControlType()));
                paths.add(path);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("paths", paths);
            result.put("total", paths.size());
            return R.success(result);
        }
    }

    @Operation(summary = "")
    @PostMapping("/loops")
    public R<List<Map<String, Object>>> detectControlLoops(@RequestBody(required = false) Map<String, Object> params) {
        LambdaQueryWrapper<ControlChain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ControlChain::getHasLoop, 1);
        List<ControlChain> loops = controlChainMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (ControlChain chain : loops) {
            Map<String, Object> item = new HashMap<>();
            item.put("chainId", chain.getChainId());
            item.put("enterpriseName", chain.getEnterpriseName());
            item.put("controlType", chain.getControlType());
            item.put("hasLoop", true);
            item.put("riskLevel", chain.getRiskLevel());
            result.add(item);
        }
        return R.success(result);
    }

    @Operation(summary = "控制变更模拟")
    @PostMapping("/simulate")
    public R<Map<String, Object>> simulateControlChanges(@RequestBody Map<String, Object> params) {
        String chainId = params.get("chainId") != null ? params.get("chainId").toString() : null;
        String changeType = params.get("changeType") != null ? params.get("changeType").toString() : "INCREASE";
        double changeRatio = params.get("changeRatio") != null
                ? Double.parseDouble(params.get("changeRatio").toString()) : 10.0;

        if (!StringUtils.hasText(chainId)) {
            return R.fail("请指定控制链ID");
        }
        ControlChain chain = controlChainMapper.selectById(chainId);
        if (chain == null) {
            return R.fail("控制链不存在");
        }

        BigDecimal originalStrength = chain.getControlStrength() != null ? chain.getControlStrength() : BigDecimal.ZERO;
        BigDecimal ratio = new BigDecimal(changeRatio).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
        BigDecimal newStrength;

        switch (changeType.toUpperCase()) {
            case "INCREASE":
                newStrength = originalStrength.add(originalStrength.multiply(ratio)).setScale(2, RoundingMode.HALF_UP);
                break;
            case "DECREASE":
                newStrength = originalStrength.subtract(originalStrength.multiply(ratio)).setScale(2, RoundingMode.HALF_UP);
                break;
            case "TRANSFER":
                newStrength = originalStrength.multiply(BigDecimal.ONE.subtract(ratio)).setScale(2, RoundingMode.HALF_UP);
                break;
            default:
                newStrength = originalStrength;
                break;
        }
        // 限制在0-100范围
        if (newStrength.compareTo(BigDecimal.ZERO) < 0) newStrength = BigDecimal.ZERO;
        if (newStrength.compareTo(new BigDecimal("100")) > 0) newStrength = new BigDecimal("100");

        // 判断状态变化
        String statusChange = "STABLE";
        String riskDesc;
        String impactLevel;
        BigDecimal diff = newStrength.subtract(originalStrength).abs();

        if (diff.compareTo(new BigDecimal("20")) >= 0) {
            statusChange = "MAJOR_CHANGE";
            impactLevel = "HIGH";
            riskDesc = "控制权变动幅度较大（" + diff.setScale(1, RoundingMode.HALF_UP) + "%），可能导致控制权转移，建议谨慎评估。";
        } else if (diff.compareTo(new BigDecimal("10")) >= 0) {
            statusChange = "MODERATE_CHANGE";
            impactLevel = "MEDIUM";
            riskDesc = "控制权发生中等幅度变动（" + diff.setScale(1, RoundingMode.HALF_UP) + "%），需关注后续影响。";
        } else {
            statusChange = "MINOR_CHANGE";
            impactLevel = "LOW";
            riskDesc = "控制权变动幅度较小（" + diff.setScale(1, RoundingMode.HALF_UP) + "%），影响可控。";
        }

        // 判断新强度是否低于控制阈值
        if (newStrength.compareTo(new BigDecimal("50")) < 0 && originalStrength.compareTo(new BigDecimal("50")) >= 0) {
            statusChange = "CONTROL_LOSS";
            impactLevel = "CRITICAL";
            riskDesc = "模拟结果显示控制强度低于50%，可能导致失去实际控制权，风险极高。";
        }

        Map<String, Object> result = new HashMap<>();
        result.put("originalStrength", originalStrength);
        result.put("newStrength", newStrength);
        result.put("changeType", changeType);
        result.put("changeRatio", changeRatio);
        result.put("statusChange", statusChange);
        result.put("impactLevel", impactLevel);
        result.put("riskDesc", riskDesc);
        result.put("enterpriseName", chain.getEnterpriseName());
        result.put("controllerName", chain.getControllerName());
        return R.success(result);
    }

    @Operation(summary = "优化建议")
    @PostMapping("/optimize")
    public R<Map<String, Object>> optimizeControlStructure(@RequestBody(required = false) Map<String, Object> params) {
        if (params == null) params = new HashMap<>();
        String chainId = params.get("chainId") != null ? params.get("chainId").toString() : null;

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> suggestions = new ArrayList<>();

        if (StringUtils.hasText(chainId)) {
            ControlChain chain = controlChainMapper.selectById(chainId);
            if (chain == null) {
                return R.fail("控制链不存在");
            }

            // 基于实际数据计算当前得分
            int score = 100;
            // 控制强度评分
            BigDecimal strength = chain.getControlStrength() != null ? chain.getControlStrength() : BigDecimal.ZERO;
            if (strength.compareTo(new BigDecimal("30")) < 0) score -= 30;
            else if (strength.compareTo(new BigDecimal("50")) < 0) score -= 15;

            // 链路长度评分
            int chainLength = chain.getChainLength() != null ? chain.getChainLength() : 0;
            if (chainLength > 5) score -= 20;
            else if (chainLength > 3) score -= 10;

            // 环路评分
            if (chain.getHasLoop() != null && chain.getHasLoop() == 1) score -= 20;

            // 稳定性评分
            String stability = chain.getStabilityLevel();
            if ("LOW".equals(stability) || "UNSTABLE".equals(stability)) score -= 15;
            else if ("MEDIUM".equals(stability)) score -= 5;

            if (score < 0) score = 0;
            result.put("currentScore", score);

            // 生成针对性建议
            if (strength.compareTo(new BigDecimal("50")) < 0) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("title", "提高控制强度");
                suggestion.put("desc", "当前控制强度为" + strength + "%，建议通过增持股份或签订一致行动协议提高控制力度。");
                suggestion.put("priority", "HIGH");
                suggestion.put("difficulty", "MEDIUM");
                suggestion.put("improvement", 15);
                suggestions.add(suggestion);
            }

            if (chainLength > 3) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("title", "缩短控制链路");
                suggestion.put("desc", "当前链路长度为" + chainLength + "级，层级过多增加管理成本和信息失真风险，建议精简中间层级。");
                suggestion.put("priority", "HIGH");
                suggestion.put("difficulty", "HIGH");
                suggestion.put("improvement", 20);
                suggestions.add(suggestion);
            }

            if (chain.getHasLoop() != null && chain.getHasLoop() == 1) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("title", "消除交叉持股环路");
                suggestion.put("desc", "检测到控制链中存在环路结构，可能导致权责不清和利益输送风险，建议理清股权关系。");
                suggestion.put("priority", "CRITICAL");
                suggestion.put("difficulty", "HIGH");
                suggestion.put("improvement", 20);
                suggestions.add(suggestion);
            }

            if ("LOW".equals(stability) || "UNSTABLE".equals(stability)) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("title", "增强控制稳定性");
                suggestion.put("desc", "当前控制结构稳定性较低，建议通过完善公司章程、签订长期协议等方式提高稳定性。");
                suggestion.put("priority", "MEDIUM");
                suggestion.put("difficulty", "LOW");
                suggestion.put("improvement", 15);
                suggestions.add(suggestion);
            }

            if (suggestions.isEmpty()) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("title", "保持当前控制结构");
                suggestion.put("desc", "当前控制链结构较为合理，建议持续关注市场变化和政策调整。");
                suggestion.put("priority", "LOW");
                suggestion.put("difficulty", "LOW");
                suggestion.put("improvement", 0);
                suggestions.add(suggestion);
            }
        } else {
            // 无特定chainId时返回通用建议
            result.put("currentScore", 75);
            Map<String, Object> s1 = new HashMap<>();
            s1.put("title", "减少管理层级");
            s1.put("desc", "精简中间控制层级，降低管理成本和信息传递延迟。");
            s1.put("priority", "HIGH");
            s1.put("difficulty", "MEDIUM");
            s1.put("improvement", 10);
            suggestions.add(s1);

            Map<String, Object> s2 = new HashMap<>();
            s2.put("title", "提高核心控股比例");
            s2.put("desc", "增持核心子公司股份，确保实际控制权稳定。");
            s2.put("priority", "MEDIUM");
            s2.put("difficulty", "MEDIUM");
            s2.put("improvement", 8);
            suggestions.add(s2);

            Map<String, Object> s3 = new HashMap<>();
            s3.put("title", "消除交叉持股");
            s3.put("desc", "理清交叉持股关系，降低利益冲突和监管风险。");
            s3.put("priority", "MEDIUM");
            s3.put("difficulty", "HIGH");
            s3.put("improvement", 12);
            suggestions.add(s3);
        }

        result.put("suggestions", suggestions);
        return R.success(result);
    }

    @Operation(summary = "生成分析报告")
    @PostMapping("/report")
    public R<Map<String, Object>> generateReport(@RequestBody Map<String, Object> params) {
        String chainId = params.get("chainId") != null ? params.get("chainId").toString() : null;
        if (!StringUtils.hasText(chainId)) {
            return R.fail("请指定控制链ID");
        }
        ControlChain chain = controlChainMapper.selectById(chainId);
        if (chain == null) {
            return R.fail("控制链不存在");
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> report = new HashMap<>();
        report.put("reportTitle", "控制链分析报告 - " + (chain.getEnterpriseName() != null ? chain.getEnterpriseName() : "未知企业"));
        report.put("enterpriseName", chain.getEnterpriseName());
        report.put("controllerName", chain.getControllerName());
        report.put("generateTime", LocalDateTime.now().format(dtf));

        // 摘要信息
        StringBuilder summary = new StringBuilder();
        summary.append("该控制链由「").append(chain.getControllerName() != null ? chain.getControllerName() : "未知")
                .append("」控制「").append(chain.getEnterpriseName() != null ? chain.getEnterpriseName() : "未知").append("」，");
        summary.append("控制类型为").append(mapControlType(chain.getControlType())).append("，");
        summary.append("控制强度").append(chain.getControlStrength() != null ? chain.getControlStrength() + "%" : "未知").append("，");
        summary.append("链路长度").append(chain.getChainLength() != null ? chain.getChainLength() + "级" : "未知").append("，");
        summary.append("包含").append(chain.getNodeCount() != null ? chain.getNodeCount() : 0).append("个节点。");
        if (chain.getHasLoop() != null && chain.getHasLoop() == 1) {
            summary.append("检测到存在控制环路。");
        }
        report.put("summary", summary.toString());

        // 风险分析
        Map<String, Object> riskAnalysis = new HashMap<>();
        riskAnalysis.put("riskLevel", chain.getRiskLevel());
        riskAnalysis.put("riskLevelDesc", mapRiskLevel(chain.getRiskLevel()));
        riskAnalysis.put("stabilityLevel", chain.getStabilityLevel());
        riskAnalysis.put("stabilityLevelDesc", mapStabilityLevel(chain.getStabilityLevel()));
        riskAnalysis.put("hasLoop", chain.getHasLoop() != null && chain.getHasLoop() == 1);

        List<String> riskFactors = new ArrayList<>();
        if (chain.getControlStrength() != null && chain.getControlStrength().compareTo(new BigDecimal("50")) < 0) {
            riskFactors.add("控制强度低于50%，存在失去控制权风险");
        }
        if (chain.getChainLength() != null && chain.getChainLength() > 4) {
            riskFactors.add("链路层级过长（" + chain.getChainLength() + "级），管理效率可能下降");
        }
        if (chain.getHasLoop() != null && chain.getHasLoop() == 1) {
            riskFactors.add("存在控制环路，可能导致利益冲突");
        }
        if ("HIGH".equals(chain.getRiskLevel()) || "CRITICAL".equals(chain.getRiskLevel())) {
            riskFactors.add("当前风险等级为" + mapRiskLevel(chain.getRiskLevel()) + "，需重点关注");
        }
        if (riskFactors.isEmpty()) {
            riskFactors.add("当前未发现明显风险因素");
        }
        riskAnalysis.put("riskFactors", riskFactors);
        report.put("riskAnalysis", riskAnalysis);

        // 建议
        List<String> suggestions = new ArrayList<>();
        if (chain.getControlStrength() != null && chain.getControlStrength().compareTo(new BigDecimal("50")) < 0) {
            suggestions.add("建议提高控制强度至50%以上，确保实际控制权");
        }
        if (chain.getChainLength() != null && chain.getChainLength() > 3) {
            suggestions.add("建议优化管理层级，缩短控制链路");
        }
        if (chain.getHasLoop() != null && chain.getHasLoop() == 1) {
            suggestions.add("建议清理交叉持股关系，消除控制环路");
        }
        if ("LOW".equals(chain.getStabilityLevel()) || "UNSTABLE".equals(chain.getStabilityLevel())) {
            suggestions.add("建议采取措施增强控制结构稳定性");
        }
        if (suggestions.isEmpty()) {
            suggestions.add("当前控制结构整体健康，建议定期复查");
        }
        report.put("suggestions", suggestions);

        // 控制路径信息
        report.put("chainPath", chain.getChainPath() != null ? chain.getChainPath() : "未设置");
        report.put("lastAnalysisTime", chain.getLastAnalysisTime() != null ? chain.getLastAnalysisTime().format(dtf) : "未分析");

        return R.success(report);
    }
}
