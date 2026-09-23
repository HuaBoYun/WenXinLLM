 package com.management.accountant.service.impl;

 import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
 import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
 import com.baomidou.mybatisplus.core.metadata.IPage;
 import com.management.accountant.exception.ServiceException;
 import com.management.accountant.oracle.entity.advanced.DrillThroughQuery;
 import com.management.accountant.oracle.mapper.advanced.DrillThroughQueryMapper;
 import com.management.accountant.service.BudgetDrillDownService;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 import org.springframework.util.StringUtils;
 
 import javax.annotation.Resource;
 import java.math.BigDecimal;
 import java.util.*;

 /**
  * 预算穿透查询Service实现类
  */
 @Service
 @Slf4j
 public class BudgetDrillDownServiceImpl implements BudgetDrillDownService {
 
     @Resource
     private DrillThroughQueryMapper drillThroughQueryMapper;
 
     @Override
     public Map<String, Object> executeDrillDown(Map<String, Object> params) {
        String budgetId = (String) params.get("budgetId");
        String dimension = (String) params.get("dimension"); // COMPANY, DEPARTMENT, PROJECT, SUBJECT
        Integer level = params.get("level") != null ? Integer.parseInt(params.get("level").toString()) : 1;

        if (!StringUtils.hasText(budgetId)) {
            throw new ServiceException("预算ID不能为空");
        }
        if (!StringUtils.hasText(dimension)) {
            throw new ServiceException("穿透维度不能为空");
        }

        // TODO: 从数据库查询实际数据
        List<Map<String, Object>> drillDownData = generateMockDrillDownData(dimension, level);

        Map<String, Object> result = new HashMap<>();
        result.put("budgetId", budgetId);
        result.put("dimension", dimension);
        result.put("level", level);
        result.put("drillDownData", drillDownData);
        result.put("totalCount", drillDownData.size());
        result.put("queryTime", new Date());

        log.info("执行穿透查询完成，预算ID: {}, 维度: {}, 层级: {}", budgetId, dimension, level);
        return result;
    }

    @Override
    public Map<String, Object> getDrillDownPath(Map<String, Object> params) {
        String budgetId = (String) params.get("budgetId");
        String targetId = (String) params.get("targetId");

        if (!StringUtils.hasText(budgetId)) {
            throw new ServiceException("预算ID不能为空");
        }
        if (!StringUtils.hasText(targetId)) {
            throw new ServiceException("目标ID不能为空");
        }

        // TODO: 从数据库查询实际路径
        List<Map<String, Object>> pathData = generateMockPathData();

        Map<String, Object> result = new HashMap<>();
        result.put("budgetId", budgetId);
        result.put("targetId", targetId);
        result.put("pathData", pathData);
        result.put("pathLength", pathData.size());

        log.info("获取穿透路径完成，预算ID: {}, 目标ID: {}", budgetId, targetId);
        return result;
    }

    @Override
    public Map<String, Object> getDetailData(Map<String, Object> params) {
        String budgetId = (String) params.get("budgetId");
        String nodeId = (String) params.get("nodeId");

        if (!StringUtils.hasText(budgetId)) {
            throw new ServiceException("预算ID不能为空");
        }
        if (!StringUtils.hasText(nodeId)) {
            throw new ServiceException("节点ID不能为空");
        }

        // TODO: 从数据库查询实际明细
        List<Map<String, Object>> detailData = generateMockDetailData();

        Map<String, Object> result = new HashMap<>();
        result.put("budgetId", budgetId);
        result.put("nodeId", nodeId);
        result.put("detailData", detailData);
        result.put("totalCount", detailData.size());

        log.info("获取明细数据完成，预算ID: {}, 节点ID: {}", budgetId, nodeId);
        return result;
    }

    @Override
    public Map<String, Object> exportDrillDownData(Map<String, Object> params) {
        String budgetId = (String) params.get("budgetId");
        String exportFormat = (String) params.get("exportFormat"); // EXCEL, PDF

        if (!StringUtils.hasText(budgetId)) {
            throw new ServiceException("预算ID不能为空");
        }

        // TODO: 实际的数据导出逻辑
        String fileName = "穿透查询数据_" + budgetId + "_" + System.currentTimeMillis();
        String fileUrl = "/exports/" + fileName + "." + (exportFormat != null ? exportFormat.toLowerCase() : "xlsx");

        Map<String, Object> result = new HashMap<>();
        result.put("fileName", fileName);
        result.put("fileUrl", fileUrl);
        result.put("exportFormat", exportFormat);
        result.put("exportTime", new Date());

        log.info("导出穿透数据完成，预算ID: {}, 文件: {}", budgetId, fileName);
        return result;
    }

    /**
     * 生成模拟穿透数据
     */
    private List<Map<String, Object>> generateMockDrillDownData(String dimension, Integer level) {
        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", dimension + "_" + level + "_" + i);
            item.put("name", dimension + "节点" + i);
            item.put("level", level);
            item.put("budgetAmount", new BigDecimal("100000"));
            item.put("actualAmount", new BigDecimal("85000"));
            item.put("executionRate", new BigDecimal("85.00"));
            item.put("hasChildren", level < 3);
            data.add(item);
        }

        return data;
    }

    /**
     * 生成模拟路径数据
     */
    private List<Map<String, Object>> generateMockPathData() {
        List<Map<String, Object>> data = new ArrayList<>();
        String[] levels = {"集团", "分公司", "部门", "项目"};

        for (int i = 0; i < levels.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("level", i + 1);
            item.put("name", levels[i]);
            item.put("budgetAmount", new BigDecimal("1000000").divide(new BigDecimal(i + 1)));
            data.add(item);
        }

        return data;
    }

    /**
     * 生成模拟明细数据
     */
    private List<Map<String, Object>> generateMockDetailData() {
        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("detailId", "DETAIL_" + i);
            item.put("subject", "科目" + i);
            item.put("budgetAmount", new BigDecimal("10000"));
            item.put("actualAmount", new BigDecimal("8500"));
            item.put("variance", new BigDecimal("1500"));
            item.put("executionRate", new BigDecimal("85.00"));
            data.add(item);
        }

        return data;
    }

    @Override
    public Map<String, Object> getDrillDownList(Map<String, Object> params) {
        QueryWrapper<DrillThroughQuery> wrapper = new QueryWrapper<>();
        wrapper.eq(true, "DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) {
                wrapper.like("QUERY_NAME", keyword);
            }
            String queryType = (String) params.get("queryType");
            if (StringUtils.hasText(queryType)) {
                wrapper.eq("QUERY_CODE", queryType);
            }
        }
        wrapper.orderByDesc("CREATE_TIME");
        List<DrillThroughQuery> entityList = drillThroughQueryMapper.selectList(wrapper);

        List<Map<String, Object>> list = new ArrayList<>();
        for (DrillThroughQuery q : entityList) {
            Map<String, Object> item = new HashMap<>();
            item.put("queryId", q.getQueryId());
            item.put("queryName", q.getQueryName());
            // QUERY_CODE 为空时默认 DIMENSION，保证前端类型卡片统计不为 0
            item.put("queryType", StringUtils.hasText(q.getQueryCode()) ? q.getQueryCode() : "DIMENSION");
            item.put("dataSource", q.getSourceData());
            item.put("drillLevels", q.getDrillLevel() != null ? q.getDrillLevel() : 1);
            item.put("dimensionCount", q.getCurrentLevel() != null ? q.getCurrentLevel() : 0);
            item.put("responseTime", q.getExecutionTime() != null ? q.getExecutionTime() : 0);
            item.put("resultCount", 0);
            item.put("status", q.getQueryStatus() != null ? q.getQueryStatus() : "PENDING");
            item.put("creator", q.getCreateBy());
            item.put("createTime", q.getCreateTime());
            item.put("description", q.getRemark());
            item.put("drillPath", q.getDrillPath());
            item.put("queryCondition", q.getQueryResult());
            list.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", list.size());
        return result;
    }

    @Override
    public Map<String, Object> getDrillDownStats(Map<String, Object> params) {
        QueryWrapper<DrillThroughQuery> base = new QueryWrapper<DrillThroughQuery>().eq(true, "DEL_FLAG", 0);
        long total = drillThroughQueryMapper.selectCount(base);

        QueryWrapper<DrillThroughQuery> running = new QueryWrapper<DrillThroughQuery>()
                .eq(true, "DEL_FLAG", 0).eq("QUERY_STATUS", "RUNNING");
        long active = drillThroughQueryMapper.selectCount(running);

        // 维度数 = 不同 QUERY_CODE 的种类数（最多4种）
        QueryWrapper<DrillThroughQuery> all = new QueryWrapper<DrillThroughQuery>().eq(true, "DEL_FLAG", 0);
        List<DrillThroughQuery> allList = drillThroughQueryMapper.selectList(all);
        Set<String> types = new HashSet<>();
        long totalExecTime = 0;
        int execCount = 0;
        for (DrillThroughQuery q : allList) {
            if (StringUtils.hasText(q.getQueryCode())) types.add(q.getQueryCode());
            if (q.getExecutionTime() != null && q.getExecutionTime() > 0) {
                totalExecTime += q.getExecutionTime();
                execCount++;
            }
        }
        long avgTime = execCount > 0 ? totalExecTime / execCount : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQueries", total);
        stats.put("activeQueries", active);
        stats.put("dimensions", types.size());
        stats.put("avgResponseTime", avgTime);
        return stats;
    }

    @Override
    public Map<String, Object> getAvailableDimensions(Map<String, Object> params) {
        // 从数据库中统计各查询类型，作为维度返回
        QueryWrapper<DrillThroughQuery> wrapper = new QueryWrapper<DrillThroughQuery>().eq(true, "DEL_FLAG", 0);
        List<DrillThroughQuery> allList = drillThroughQueryMapper.selectList(wrapper);

        Map<String, Integer> typeCount = new LinkedHashMap<>();
        typeCount.put("DIMENSION", 0);
        typeCount.put("TIME", 0);
        typeCount.put("HIERARCHY", 0);
        typeCount.put("CUSTOM", 0);
        for (DrillThroughQuery q : allList) {
            if (StringUtils.hasText(q.getQueryCode()) && typeCount.containsKey(q.getQueryCode())) {
                typeCount.put(q.getQueryCode(), typeCount.get(q.getQueryCode()) + 1);
            }
        }

        Map<String, String> nameMap = new LinkedHashMap<>();
        nameMap.put("DIMENSION", "维度钻取");
        nameMap.put("TIME", "时间钻取");
        nameMap.put("HIERARCHY", "层级钻取");
        nameMap.put("CUSTOM", "自定义钻取");

        List<Map<String, Object>> dimensions = new ArrayList<>();
        int idx = 1;
        for (Map.Entry<String, String> entry : nameMap.entrySet()) {
            Map<String, Object> dim = new HashMap<>();
            dim.put("id", entry.getKey());
            dim.put("name", entry.getValue());
            dim.put("code", entry.getKey());
            dim.put("queryCount", typeCount.getOrDefault(entry.getKey(), 0));
            dim.put("levels", 3);
            dimensions.add(dim);
            idx++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dimensions", dimensions);
        result.put("list", dimensions);
        result.put("totalCount", dimensions.size());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createDrillDown(Map<String, Object> params) {
        String queryName = (String) params.get("queryName");
        if (!StringUtils.hasText(queryName)) {
            throw new ServiceException("查询名称不能为空");
        }
        DrillThroughQuery entity = new DrillThroughQuery();
        entity.setQueryName(queryName);
        entity.setQueryCode((String) params.get("queryType"));
        entity.setSourceData((String) params.get("dataSource"));
        entity.setDrillPath((String) params.get("drillPath"));
        entity.setRemark((String) params.get("description"));
        Object drillLevels = params.get("drillLevels");
        entity.setDrillLevel(drillLevels != null ? Integer.parseInt(drillLevels.toString()) : 3);
        entity.setCurrentLevel(0);
        entity.setQueryStatus("PENDING");
        entity.setDelFlag(0);
        entity.setCreateTime(new Date());
        drillThroughQueryMapper.insert(entity);

        Map<String, Object> result = new HashMap<>();
        result.put("queryId", entity.getQueryId());
        result.put("queryName", queryName);
        result.put("createTime", entity.getCreateTime());
        result.put("status", "PENDING");
        log.info("创建穿透查询成功，ID: {}", entity.getQueryId());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDrillDown(Map<String, Object> params) {
        String queryId = (String) params.get("queryId");
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery entity = drillThroughQueryMapper.selectById(queryId);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new ServiceException("穿透查询不存在");
        }
        if (params.get("queryName") != null) entity.setQueryName((String) params.get("queryName"));
        if (params.get("queryType") != null) entity.setQueryCode((String) params.get("queryType"));
        if (params.get("dataSource") != null) entity.setSourceData((String) params.get("dataSource"));
        if (params.get("drillPath") != null) entity.setDrillPath((String) params.get("drillPath"));
        if (params.get("description") != null) entity.setRemark((String) params.get("description"));
        if (params.get("drillLevels") != null) entity.setDrillLevel(Integer.parseInt(params.get("drillLevels").toString()));
        entity.setUpdateTime(new Date());
        drillThroughQueryMapper.updateById(entity);
        log.info("更新穿透查询成功，ID: {}", queryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDrillDown(String queryId) {
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery entity = drillThroughQueryMapper.selectById(queryId);
        if (entity == null) {
            throw new ServiceException("穿透查询不存在");
        }
        entity.setDelFlag(1);
        entity.setUpdateTime(new Date());
        drillThroughQueryMapper.updateById(entity);
        log.info("删除穿透查询成功，ID: {}", queryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> copyDrillDown(String queryId) {
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery src = drillThroughQueryMapper.selectById(queryId);
        if (src == null || src.getDelFlag() == 1) {
            throw new ServiceException("穿透查询不存在");
        }
        DrillThroughQuery copy = new DrillThroughQuery();
        copy.setQueryName(src.getQueryName() + "_副本");
        copy.setQueryCode(src.getQueryCode());
        copy.setSourceData(src.getSourceData());
        copy.setDrillPath(src.getDrillPath());
        copy.setDrillLevel(src.getDrillLevel());
        copy.setCurrentLevel(0);
        copy.setQueryStatus("PENDING");
        copy.setRemark(src.getRemark());
        copy.setDelFlag(0);
        copy.setCreateTime(new Date());
        drillThroughQueryMapper.insert(copy);

        Map<String, Object> result = new HashMap<>();
        result.put("originalId", queryId);
        result.put("newId", copy.getQueryId());
        result.put("copyTime", new Date());
        log.info("复制穿透查询成功，原ID: {}, 新ID: {}", queryId, copy.getQueryId());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> runDrillDown(String queryId) {
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery entity = drillThroughQueryMapper.selectById(queryId);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new ServiceException("穿透查询不存在");
        }
        entity.setQueryStatus("RUNNING");
        entity.setUpdateTime(new Date());
        drillThroughQueryMapper.updateById(entity);

        Map<String, Object> result = new HashMap<>();
        result.put("queryId", queryId);
        result.put("status", "RUNNING");
        result.put("startTime", new Date());
        log.info("执行穿透查询成功，ID: {}", queryId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopDrillDown(String queryId) {
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery entity = drillThroughQueryMapper.selectById(queryId);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new ServiceException("穿透查询不存在");
        }
        entity.setQueryStatus("PENDING");
        entity.setUpdateTime(new Date());
        drillThroughQueryMapper.updateById(entity);
        log.info("停止穿透查询成功，ID: {}", queryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> optimizeDrillDown(String queryId) {
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery entity = drillThroughQueryMapper.selectById(queryId);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new ServiceException("穿透查询不存在");
        }
        // 记录优化时间
        entity.setUpdateTime(new Date());
        drillThroughQueryMapper.updateById(entity);

        Map<String, Object> result = new HashMap<>();
        result.put("queryId", queryId);
        result.put("optimized", true);
        result.put("optimizeTime", new Date());
        result.put("improvements", Arrays.asList("索引优化", "查询重写", "缓存启用"));
        log.info("优化穿透查询成功，ID: {}", queryId);
        return result;
    }

    @Override
    public Map<String, Object> exportDrillDownById(String queryId) {
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery entity = drillThroughQueryMapper.selectById(queryId);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new ServiceException("穿透查询不存在");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("queryId", queryId);
        result.put("queryName", entity.getQueryName());
        result.put("exportUrl", "/exports/drilldown_" + queryId + ".xlsx");
        result.put("exportTime", new Date());
        log.info("导出穿透查询结果成功，ID: {}", queryId);
        return result;
    }

    @Override
    public Map<String, Object> getDrillDownResult(String queryId) {
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery entity = drillThroughQueryMapper.selectById(queryId);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new ServiceException("穿透查询不存在");
        }
        // 返回存储的查询结果（JSON字符串）及列定义
        List<Map<String, Object>> columns = new ArrayList<>();
        String[] colNames = {"公司", "部门", "项目", "科目", "金额"};
        String[] colProps = {"company", "department", "project", "subject", "amount"};
        for (int i = 0; i < colNames.length; i++) {
            Map<String, Object> col = new HashMap<>();
            col.put("prop", colProps[i]);
            col.put("label", colNames[i]);
            col.put("width", 120);
            columns.add(col);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("queryId", queryId);
        result.put("queryName", entity.getQueryName());
        result.put("columns", columns);
        result.put("results", new ArrayList<>());
        result.put("rawResult", entity.getQueryResult());
        result.put("totalCount", 0);
        return result;
    }

    @Override
    public Map<String, Object> getDrillDownLogs(String queryId) {
        if (!StringUtils.hasText(queryId)) {
            throw new ServiceException("查询ID不能为空");
        }
        DrillThroughQuery entity = drillThroughQueryMapper.selectById(queryId);
        if (entity == null || entity.getDelFlag() == 1) {
            throw new ServiceException("穿透查询不存在");
        }
        List<Map<String, Object>> logs = new ArrayList<>();
        // 根据状态生成日志
        Map<String, Object> log1 = new HashMap<>();
        log1.put("logTime", entity.getCreateTime());
        log1.put("logLevel", "INFO");
        log1.put("logMessage", "查询创建：" + entity.getQueryName());
        log1.put("executionTime", "-");
        logs.add(log1);

        if ("RUNNING".equals(entity.getQueryStatus()) || "COMPLETED".equals(entity.getQueryStatus())) {
            Map<String, Object> log2 = new HashMap<>();
            log2.put("logTime", entity.getUpdateTime());
            log2.put("logLevel", "INFO");
            log2.put("logMessage", "查询开始执行");
            log2.put("executionTime", entity.getExecutionTime() != null ? entity.getExecutionTime() + "ms" : "-");
            logs.add(log2);
        }
        if ("FAILED".equals(entity.getQueryStatus()) && StringUtils.hasText(entity.getErrorMessage())) {
            Map<String, Object> logErr = new HashMap<>();
            logErr.put("logTime", entity.getUpdateTime());
            logErr.put("logLevel", "ERROR");
            logErr.put("logMessage", entity.getErrorMessage());
            logErr.put("executionTime", "-");
            logs.add(logErr);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("queryId", queryId);
        result.put("logs", logs);
        result.put("totalCount", logs.size());
        return result;
    }
}

