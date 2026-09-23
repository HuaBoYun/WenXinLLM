package com.huabo.monitor.service.impl;

import com.huabo.monitor.mapper.ScreenDisplayMapper;
import com.huabo.monitor.service.ScreenDisplayService;
import com.huabo.monitor.util.JsonBean;
import com.huabo.monitor.util.ResponseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大屏展示数据 Service 实现类
 */
@Service
@Slf4j
public class ScreenDisplayServiceImpl implements ScreenDisplayService {

    @Resource
    private ScreenDisplayMapper screenDisplayMapper;

    /**
     * 各部门评价项目数统计
     * @return 返回各部门的评价项目数量统计结果
     */
    @Override
    public JsonBean<Map<String, Object>> getDepartmentProjectCount() {
        try {
            // 调用 Mapper 执行 SQL 查询
            List<Map<String, Object>> dataList = screenDisplayMapper.getDepartmentProjectCount();
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            
            // 提取部门名称和数量
            List<String> categories = new ArrayList<>();
            List<Integer> values = new ArrayList<>();
            
            for (Map<String, Object> item : dataList) {
                String orgName = (String) item.get("ORGNAME");
                Object countObj = item.get("C");
                
                // 处理数量字段，可能是 Long 或 BigDecimal
                int count = 0;
                if (countObj != null) {
                    if (countObj instanceof Number) {
                        count = ((Number) countObj).intValue();
                    }
                }
                
                categories.add(orgName != null ? orgName : "未知");
                values.add(count);
            }
            
            result.put("categories", categories);
            result.put("values", values);
            
            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取各部门评价项目数统计失败", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    /**
     * 控制有效性数据查询
     * @return 返回控制有效性数据列表
     */
    @Override
    public JsonBean<Map<String, Object>> getControlEffectivenessData() {
        try {
            // 调用 Mapper 执行 SQL 查询
            List<Map<String, Object>> dataList = screenDisplayMapper.getControlEffectivenessData();

            // 处理数据，转换为前端需要的格式
            List<Map<String, Object>> processedList = new ArrayList<>();

            for (Map<String, Object> item : dataList) {
                Map<String, Object> processedItem = new HashMap<>();

                // 编号
                processedItem.put("type1", item.get("BM") != null ? item.get("BM") : "");

                // 方案指定部门
                processedItem.put("type2", item.get("DM") != null ? item.get("DM") : "");

                // 控制目标
                processedItem.put("type3", item.get("MB") != null ? item.get("MB") : "");

                // 设计有效性
                String sjValue = item.get("SJ") != null ? item.get("SJ").toString() : "";
                String type4Text = "其他";
                if ("有效".equals(sjValue)) {
                    type4Text = "有效";
                } else if ("无效".equals(sjValue)) {
                    type4Text = "无效";
                } else if ("不适用".equals(sjValue)) {
                    type4Text = "不适用";
                }
                processedItem.put("type4", type4Text);

                // 执行有效性
                String zxValue = item.get("ZX") != null ? item.get("ZX").toString() : "";
                String type5Text = "其他";
                if ("有效".equals(zxValue)) {
                    type5Text = "有效";
                } else if ("无效".equals(zxValue)) {
                    type5Text = "无效";
                } else if ("不适用".equals(zxValue)) {
                    type5Text = "不适用";
                }
                processedItem.put("type5", type5Text);

                // 测试有效性
                String csValue = item.get("CS") != null ? item.get("CS").toString() : "";
                String type6Text = "其他";
                if ("1".equals(csValue)) {
                    type6Text = "有效";
                } else if ("2".equals(csValue)) {
                    type6Text = "无效";
                } else if ("3".equals(csValue)) {
                    type6Text = "不适用";
                }
                processedItem.put("type6", type6Text);

                processedList.add(processedItem);
            }

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", processedList);

            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取控制有效性数据失败", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    /**
     * 各部门评价项目数占比统计
     * @return 返回各部门的评价项目数量占比
     */
    @Override
    public JsonBean<Map<String, Object>> getDepartmentProjectRatio() {
        try {
            // 调用 Mapper 执行 SQL 查询
            List<Map<String, Object>> dataList = screenDisplayMapper.getDepartmentProjectRatio();

            // 处理数据，转换为饼图需要的格式
            List<Map<String, Object>> processedList = new ArrayList<>();

            for (Map<String, Object> item : dataList) {
                Map<String, Object> processedItem = new HashMap<>();

                // 获取部门名称
                String orgName = (String) item.get("ORGNAME");
                if (orgName == null || orgName.trim().isEmpty()) {
                    orgName = "未知";
                }

                // 获取数量
                Object countObj = item.get("C");
                int value = 0;
                if (countObj != null) {
                    if (countObj instanceof Number) {
                        value = ((Number) countObj).intValue();
                    }
                }

                processedItem.put("name", orgName);
                processedItem.put("value", value);

                processedList.add(processedItem);
            }

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("data", processedList);

            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取各部门评价项目数占比失败", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    /**
     * 缺陷类型分布统计
     * @return 返回各缺陷类型的数量分布
     */
    @Override
    public JsonBean<Map<String, Object>> getDefectTypeDistribution() {
        try {
            // 调用 Mapper 执行 SQL 查询
            List<Map<String, Object>> dataList = screenDisplayMapper.getDefectTypeDistribution();

            // 处理数据，转换为饼图需要的格式
            List<Map<String, Object>> processedList = new ArrayList<>();

            for (Map<String, Object> item : dataList) {
                Map<String, Object> processedItem = new HashMap<>();

                // 获取缺陷类型名称
                Object nameObj = item.get("L");
                String name = "未知";
                if (nameObj != null) {
                    String nameStr = nameObj.toString();
                    if (nameStr != null && !nameStr.trim().isEmpty()) {
                        name = nameStr;
                    }
                }

                // 获取数量
                Object countObj = item.get("C");
                int value = 0;
                if (countObj != null) {
                    if (countObj instanceof Number) {
                        value = ((Number) countObj).intValue();
                    }
                }

                processedItem.put("name", name);
                processedItem.put("value", value);

                processedList.add(processedItem);
            }

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("data", processedList);

            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取缺陷类型分布失败", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    /**
     * 各单位缺陷数量对比分析
     * @return 返回各单位的缺陷数量对比数据
     */
    @Override
    public JsonBean<Map<String, Object>> getDepartmentDefectComparison() {
        try {
            // 调用 Mapper 执行 SQL 查询
            List<Map<String, Object>> dataList = screenDisplayMapper.getDepartmentDefectComparison();

            // 处理数据，转换为柱状图需要的格式
            List<String> categories = new ArrayList<>();
            List<Integer> seriesData = new ArrayList<>();

            for (Map<String, Object> item : dataList) {
                // 获取单位名称
                Object nameObj = item.get("N");
                String name = "";
                if (nameObj != null) {
                    name = nameObj.toString();
                }
                categories.add(name);

                // 获取数量
                Object countObj = item.get("C");
                int count = 0;
                if (countObj != null) {
                    if (countObj instanceof Number) {
                        count = ((Number) countObj).intValue();
                    }
                }
                seriesData.add(count);
            }

            // 构建系列数据
            Map<String, Object> seriesItem = new HashMap<>();
            seriesItem.put("name", "缺陷数量");
            seriesItem.put("data", seriesData);

            List<Map<String, Object>> series = new ArrayList<>();
            series.add(seriesItem);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("categories", categories);
            result.put("series", series);

            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取各单位缺陷数量对比分析失败", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    /**
     * 本年缺陷项目趋势分析
     * @return 返回近12个月的缺陷数量趋势数据
     */
    @Override
    public JsonBean<Map<String, Object>> getDefectTrendAnalysis() {
        try {
            // 调用 Mapper 执行 SQL 查询
            List<Map<String, Object>> dataList = screenDisplayMapper.getDefectTrendAnalysis();

            // 处理数据，转换为折线图需要的格式
            List<String> categories = new ArrayList<>();
            List<Integer> seriesData = new ArrayList<>();

            for (Map<String, Object> item : dataList) {
                // 获取月份
                Object monthObj = item.get("M");
                String month = "";
                if (monthObj != null) {
                    month = monthObj.toString();
                }
                categories.add(month);

                // 获取数量
                Object countObj = item.get("C");
                int count = 0;
                if (countObj != null) {
                    if (countObj instanceof Number) {
                        count = ((Number) countObj).intValue();
                    }
                }
                seriesData.add(count);
            }

            // 构建系列数据
            Map<String, Object> seriesItem = new HashMap<>();
            seriesItem.put("name", "缺陷数量");
            seriesItem.put("data", seriesData);

            List<Map<String, Object>> series = new ArrayList<>();
            series.add(seriesItem);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("categories", categories);
            result.put("series", series);

            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取本年缺陷项目趋势分析失败", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }

    /**
     * 缺陷属性分布统计
     * @return 返回各缺陷属性的数量分布
     */
    @Override
    public JsonBean<Map<String, Object>> getDefectPropertyDistribution() {
        try {
            // 调用 Mapper 执行 SQL 查询
            List<Map<String, Object>> dataList = screenDisplayMapper.getDefectPropertyDistribution();

            // 处理数据，转换为饼图需要的格式
            List<Map<String, Object>> result = new ArrayList<>();

            for (Map<String, Object> item : dataList) {
                Map<String, Object> pieItem = new HashMap<>();

                // 获取缺陷属性
                Object propertyObj = item.get("B");
                String name = "未知";
                if (propertyObj != null && !propertyObj.toString().isEmpty()) {
                    name = propertyObj.toString();
                }
                pieItem.put("name", name);

                // 获取数量
                Object countObj = item.get("C");
                int value = 0;
                if (countObj != null) {
                    if (countObj instanceof Number) {
                        value = ((Number) countObj).intValue();
                    }
                }
                pieItem.put("value", value);

                result.add(pieItem);
            }

            // 构建返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("data", result);

            return ResponseFormat.retParam(1, 200, data);
        } catch (Exception e) {
            log.error("获取缺陷属性分布统计失败", e);
            return ResponseFormat.retParam(0, 500, null);
        }
    }
}

