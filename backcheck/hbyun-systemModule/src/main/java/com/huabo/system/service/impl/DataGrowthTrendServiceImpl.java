package com.huabo.system.service.impl;

import com.huabo.system.dto.*;
import com.huabo.system.mapper.DataGrowthTrendMapper;
import com.huabo.system.service.DataGrowthTrendService;
import com.hbfk.util.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据增长趋势Service实现类
 */
@Service
@Slf4j
public class DataGrowthTrendServiceImpl implements DataGrowthTrendService {

    @Resource
    private DataGrowthTrendMapper dataGrowthTrendMapper;

    @Override
    public JsonBean getDataGrowthTrend() {
        try {
            List<DataGrowthTrendDTO> list = dataGrowthTrendMapper.queryDataGrowthTrend();
            return new JsonBean(1, "成功", list);
        } catch (Exception e) {
            log.error("获取数据增长趋势失败", e);
            return new JsonBean(0, "获取数据增长趋势失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getDataTotal() {
        try {
            DataTotalDTO dataTotal = dataGrowthTrendMapper.queryDataTotal();
            return new JsonBean(1, "成功", dataTotal);
        } catch (Exception e) {
            log.error("获取数据总量失败", e);
            return new JsonBean(0, "获取数据总量失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getCoreFunctionUsage() {
        try {
            CoreFunctionUsageDTO usage = dataGrowthTrendMapper.queryCoreFunctionUsage();
            return new JsonBean(1, "成功", usage);
        } catch (Exception e) {
            log.error("获取核心功能使用率失败", e);
            return new JsonBean(0, "获取核心功能使用率失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getBusinessActivityRanking() {
        try {
            List<BusinessActivityRankingDTO> list = dataGrowthTrendMapper.queryBusinessActivityRanking();
            // 只取前10条
            if (list != null && list.size() > 10) {
                list = list.subList(0, 10);
            }
            return new JsonBean(1, "成功", list);
        } catch (Exception e) {
            log.error("获取业务活跃度排名失败", e);
            return new JsonBean(0, "获取业务活跃度排名失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getNewUserCount() {
        try {
            List<NewUserCountDTO> list = dataGrowthTrendMapper.queryNewUserCount();
            return new JsonBean(1, "成功", list);
        } catch (Exception e) {
            log.error("获取新增用户数失败", e);
            return new JsonBean(0, "获取新增用户数失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getUserActivity() {
        try {
            List<UserActivityDTO> list = dataGrowthTrendMapper.queryUserActivity();
            return new JsonBean(1, "成功", list);
        } catch (Exception e) {
            log.error("获取用户活跃度失败", e);
            return new JsonBean(0, "获取用户活跃度失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getRiskAuditDimension() {
        try {
            List<Map<String, Object>> resultList = dataGrowthTrendMapper.queryRiskAuditDimension();

            RiskAuditDimensionDTO result = new RiskAuditDimensionDTO();
            List<String> categories = new ArrayList<>();
            List<RiskAuditDimensionDTO.SeriesData> seriesList = new ArrayList<>();

            // 初始化系列数据
            List<BigDecimal> fxData = new ArrayList<>();
            List<BigDecimal> nkData = new ArrayList<>();
            List<BigDecimal> sjData = new ArrayList<>();

            for (Map<String, Object> item : resultList) {
                // 修复：使用大写字段名（达梦数据库返回大写）
                String yearMonth = (String) item.get("YEAR_MONTH");
                categories.add(yearMonth);

                Number fxNum = (Number) item.get("FX");
                Number nkNum = (Number) item.get("NK");
                Number sjNum = (Number) item.get("SJ");

                fxData.add(fxNum != null ? new BigDecimal(fxNum.toString()) : BigDecimal.ZERO);
                nkData.add(nkNum != null ? new BigDecimal(nkNum.toString()) : BigDecimal.ZERO);
                sjData.add(sjNum != null ? new BigDecimal(sjNum.toString()) : BigDecimal.ZERO);
            }

            // 创建风险系列
            RiskAuditDimensionDTO.SeriesData fxSeries = new RiskAuditDimensionDTO.SeriesData();
            fxSeries.setName("风险");
            fxSeries.setData(fxData);
            seriesList.add(fxSeries);

            // 创建内控系列
            RiskAuditDimensionDTO.SeriesData nkSeries = new RiskAuditDimensionDTO.SeriesData();
            nkSeries.setName("内控");
            nkSeries.setData(nkData);
            seriesList.add(nkSeries);

            // 创建审计系列
            RiskAuditDimensionDTO.SeriesData sjSeries = new RiskAuditDimensionDTO.SeriesData();
            sjSeries.setName("审计");
            sjSeries.setData(sjData);
            seriesList.add(sjSeries);

            result.setCategories(categories);
            result.setSeries(seriesList);

            return new JsonBean(1, "成功", result);
        } catch (Exception e) {
            log.error("获取风险内控审计维度失败", e);
            return new JsonBean(0, "获取风险内控审计维度失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getInternalControlDefect() {
        try {
            List<InternalControlDefectDTO> list = dataGrowthTrendMapper.queryInternalControlDefect();

            // 转换为前端需要的格式
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (InternalControlDefectDTO dto : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", dto.getB());
                map.put("value", dto.getC());
                resultList.add(map);
            }

            return new JsonBean(1, "成功", resultList);
        } catch (Exception e) {
            log.error("获取内控测试缺陷程度失败", e);
            return new JsonBean(0, "获取内控测试缺陷程度失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getRiskEventHandling() {
        try {
            // 直接使用Map接收查询结果
            List<Map<String, Object>> list = dataGrowthTrendMapper.queryRiskEventHandling();

            if (list == null || list.isEmpty()) {
                return new JsonBean(1, "成功", new ArrayList<>());
            }

            // 将单行数据转换为数组格式，适配前端饼图
            Map<String, Object> data = list.get(0);
            List<Map<String, Object>> resultList = new ArrayList<>();

            // 类别1 - 已处理
            Map<String, Object> category1 = new HashMap<>();
            category1.put("name", "已处理");
            Object cat1Value = data.get("CATEGORY1");
            category1.put("value", cat1Value != null ? ((Number) cat1Value).intValue() : 0);
            resultList.add(category1);

            // 类别2 - 未处理
            Map<String, Object> category2 = new HashMap<>();
            category2.put("name", "未处理");
            Object cat2Value = data.get("CATEGORY2");
            category2.put("value", cat2Value != null ? ((Number) cat2Value).intValue() : 0);
            resultList.add(category2);

            return new JsonBean(1, "成功", resultList);
        } catch (Exception e) {
            log.error("获取风险事件处理失败", e);
            return new JsonBean(0, "获取风险事件处理失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getAuditIssueCount() {
        try {
            List<AuditIssueCountDTO> list = dataGrowthTrendMapper.queryAuditIssueCount();

            // 转换为前端需要的格式
            Map<String, Object> result = new HashMap<>();
            List<String> categories = new ArrayList<>();
            List<BigDecimal> data = new ArrayList<>();

            for (AuditIssueCountDTO dto : list) {
                categories.add(dto.getD());
                data.add(dto.getC());
            }

            result.put("categories", categories);

            Map<String, Object> seriesItem = new HashMap<>();
            seriesItem.put("name", "审计问题");
            seriesItem.put("data", data);

            List<Map<String, Object>> seriesList = new ArrayList<>();
            seriesList.add(seriesItem);

            result.put("series", seriesList);

            return new JsonBean(1, "成功", result);
        } catch (Exception e) {
            log.error("获取审计问题数量失败", e);
            return new JsonBean(0, "获取审计问题数量失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getInternalControlRectification() {
        try {
            List<InternalControlRectificationDTO> list = dataGrowthTrendMapper.queryInternalControlRectification();

            // 转换为前端需要的格式
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (InternalControlRectificationDTO dto : list) {
                Map<String, Object> map = new HashMap<>();

                // 数据库中PLANSTATUS字段存储的是中文状态名称，直接使用
                String statusName = dto.getS();

                // 处理空值或空字符串
                if (statusName == null || statusName.trim().isEmpty()) {
                    statusName = "未知";
                }

                map.put("name", statusName);
                map.put("value", dto.getC());
                resultList.add(map);
            }

            return new JsonBean(1, "成功", resultList);
        } catch (Exception e) {
            log.error("获取内控缺陷整改跟进失败", e);
            return new JsonBean(0, "获取内控缺陷整改跟进失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getAuditIssueVerificationRate() {
        try {
            List<Map<String, Object>> list = dataGrowthTrendMapper.queryAuditIssueVerificationRate();

            if (list == null || list.isEmpty()) {
                log.warn("查询审计问题整改验证完成率返回为空");
                Map<String, Object> result = new HashMap<>();
                result.put("label", "审计问题整改验证完成率");
                result.put("value", 0);
                result.put("data", 0);
                result.put("rate", 0.0);
                return new JsonBean(1, "成功", result);
            }

            int total = 0;
            int completed = 0;

            for (Map<String, Object> item : list) {
                // 达梦数据库返回的字段名是大写的
                Object sObj = item.get("S");
                Object cObj = item.get("C");

                // 处理数量
                int count = 0;
                if (cObj instanceof Number) {
                    count = ((Number) cObj).intValue();
                }

                total += count;

                // 处理状态码（可能是null、数字或字符串）
                if (sObj != null) {
                    try {
                        // 修复：状态码8表示已完成（根据用户需求）
                        int s = sObj instanceof Number ? ((Number) sObj).intValue() : Integer.parseInt(sObj.toString());
                        if (s == 8) {
                            completed += count;
                        }
                    } catch (NumberFormatException e) {
                        // 如果状态码不是数字，跳过
                        log.warn("审计问题状态码不是数字: [{}]", sObj);
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("label", "审计问题整改验证完成率");
            result.put("value", total);
            result.put("data", completed);
            double rate = total > 0 ? (completed * 100.0 / total) : 0;
            result.put("rate", Math.round(rate * 100.0) / 100.0);

            return new JsonBean(1, "成功", result);
        } catch (Exception e) {
            log.error("获取审计问题整改验证完成率失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("label", "审计问题整改验证完成率");
            result.put("value", 0);
            result.put("data", 0);
            result.put("rate", 0.0);
            return new JsonBean(1, "成功", result);
        }
    }

    @Override
    public JsonBean getRiskWarningResponse() {
        try {
            List<Map<String, Object>> list = dataGrowthTrendMapper.queryRiskWarningResponse();

            if (list == null || list.isEmpty()) {
                log.warn("查询风险预警响应率返回为空");
                Map<String, Object> result = new HashMap<>();
                result.put("label", "风险预警响应率");
                result.put("value", 0);
                result.put("data", 0);
                result.put("responseRate", 0.0);
                return new JsonBean(1, "成功", result);
            }

            int total = 0;
            int targetData = 0;  // 状态6的数量

            for (Map<String, Object> item : list) {
                // 达梦数据库返回的字段名是大写的
                Object sObj = item.get("S");
                Object cObj = item.get("C");

                // 处理数量
                int count = 0;
                if (cObj instanceof Number) {
                    count = ((Number) cObj).intValue();
                }

                total += count;

                // 处理状态码（可能是null、数字或字符串）
                if (sObj != null) {
                    try {
                        int s = sObj instanceof Number ? ((Number) sObj).intValue() : Integer.parseInt(sObj.toString());
                        // 修复：状态6表示已响应（根据前端计算逻辑）
                        if (s == 6) {
                            targetData = count;
                        }
                    } catch (NumberFormatException e) {
                        // 如果状态码不是数字，跳过
                        log.warn("状态码不是数字: [{}]", sObj);
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("label", "风险预警响应率");
            result.put("value", total);
            result.put("data", targetData);
            double rate = total > 0 ? (targetData * 100.0 / total) : 0;
            result.put("responseRate", Math.round(rate * 100.0) / 100.0);

            return new JsonBean(1, "成功", result);
        } catch (Exception e) {
            log.error("获取风险预警响应率失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("label", "风险预警响应率");
            result.put("value", 0);
            result.put("data", 0);
            result.put("responseRate", 0.0);
            return new JsonBean(1, "成功", result);
        }
    }

    @Override
    public JsonBean getBasicUserInfo() {
        try {
            List<Map<String, Object>> list = dataGrowthTrendMapper.queryBasicUserInfo();

            // 转换字段名为小写，以匹配前端期望
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Map<String, Object> item : list) {
                Map<String, Object> map = new HashMap<>();
                // 数据库返回大写字段名，转换为小写
                map.put("type1", item.get("TYPE1"));
                map.put("type2", item.get("TYPE2"));
                map.put("type3", item.get("TYPE3"));
                map.put("type4", item.get("TYPE4"));
                map.put("gl", item.get("GL"));
                resultList.add(map);
            }

            return new JsonBean(1, "成功", resultList);
        } catch (Exception e) {
            log.error("获取基础用户信息失败", e);
            return new JsonBean(0, "获取基础用户信息失败: " + e.getMessage(), null);
        }
    }

    /**
     * 根据状态码获取状态名称
     */
    private String getStatusName(String status) {
        if (status == null || status.isEmpty()) {
            return "未知";
        }
        try {
            int statusCode = Integer.parseInt(status);
            switch (statusCode) {
                case 0:
                    return "未完成";
                case 1:
                    return "已完成";
                case 2:
                    return "进行中";
                default:
                    return "其他";
            }
        } catch (NumberFormatException e) {
            return "其他";
        }
    }
}
