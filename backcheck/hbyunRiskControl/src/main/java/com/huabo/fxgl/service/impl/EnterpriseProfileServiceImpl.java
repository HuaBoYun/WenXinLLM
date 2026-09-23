package com.huabo.fxgl.service.impl;

import com.hbfk.util.DateUtil;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.dto.EnterpriseInfoQueryParam;
import com.huabo.fxgl.dto.FinancialRadarQueryParam;
import com.huabo.fxgl.entity.EnterpriseProfileInfo;
import com.huabo.fxgl.entity.FinancialIndicatorData;
import com.huabo.fxgl.mapper.EnterpriseProfileInfoMapper;
import com.huabo.fxgl.mapper.FinancialIndicatorDataMapper;
import com.huabo.fxgl.service.IEnterpriseProfileService;
import com.huabo.fxgl.vo.EnterpriseInfoVO;
import com.huabo.fxgl.vo.FinancialRadarVO;
import com.huabo.fxgl.vo.EnterpriseHologramVO;
import com.huabo.fxgl.vo.EnterpriseDetailInfoVO;
import com.huabo.fxgl.mapper.EnterpriseHologramMapper;
import com.huabo.fxgl.entity.*;
import java.text.SimpleDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业画像服务实现类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Slf4j
@Service
public class EnterpriseProfileServiceImpl implements IEnterpriseProfileService {

    @Autowired
    private EnterpriseProfileInfoMapper enterpriseProfileInfoMapper;

    @Autowired
    private FinancialIndicatorDataMapper financialIndicatorDataMapper;

    @Autowired
    private EnterpriseHologramMapper enterpriseHologramMapper;

    @Override
    public EnterpriseInfoVO getEnterpriseInfo(EnterpriseInfoQueryParam param) {
        log.info("获取企业基本信息，企业ID: {}", param.getEnterpriseId());
        
        try {
            // 获取企业基本信息
            EnterpriseProfileInfo enterpriseInfo = enterpriseProfileInfoMapper.getEnterpriseInfoWithExtended(param.getEnterpriseId());
            if (enterpriseInfo == null) {
                throw new RuntimeException("企业信息不存在");
            }

            // 构建返回对象
            EnterpriseInfoVO result = new EnterpriseInfoVO();

            // 设置企业基本信息
            EnterpriseInfoVO.EnterpriseBasicInfo basicInfo = new EnterpriseInfoVO.EnterpriseBasicInfo();
            BeanUtils.copyProperties(enterpriseInfo, basicInfo);

            // 设置名称映射
            basicInfo.setIndustryTypeName(getIndustryTypeName(enterpriseInfo.getIndustryType()));
            basicInfo.setEnterpriseScaleName(getEnterpriseScaleName(enterpriseInfo.getEnterpriseScale()));
            basicInfo.setEnterpriseTypeName(getEnterpriseScaleName(enterpriseInfo.getEnterpriseScale()));
            basicInfo.setStatusName(getStatusName(enterpriseInfo.getStatus()));

            // 获取统一社会信用代码（从扩展信息表）
            try {
                EnterpriseExtendedInfo extendedInfo = enterpriseHologramMapper.getEnterpriseExtendedInfoByEnterpriseId(param.getEnterpriseId());
                if (extendedInfo != null && StringUtil.isNotEmpty(extendedInfo.getUnifiedSocialCreditCode())) {
                    basicInfo.setCreditCode(extendedInfo.getUnifiedSocialCreditCode());
                }
            } catch (Exception ex) {
                log.warn("获取企业扩展信息失败，企业ID: {}", param.getEnterpriseId());
            }

            result.setEnterpriseInfo(basicInfo);

            // 获取关键指标
            if (param.getIncludeExtendedInfo()) {
                Map<String, BigDecimal> keyMetrics = enterpriseProfileInfoMapper.getEnterpriseKeyMetrics(param.getEnterpriseId());

                EnterpriseInfoVO.KeyMetrics metrics = new EnterpriseInfoVO.KeyMetrics();
                metrics.setTotalAssets(keyMetrics.getOrDefault("TOTAL_ASSETS", BigDecimal.ZERO));
                metrics.setAnnualRevenue(keyMetrics.getOrDefault("ANNUAL_REVENUE", BigDecimal.ZERO));
                metrics.setNetProfit(keyMetrics.getOrDefault("NET_PROFIT", BigDecimal.ZERO));
                metrics.setEmployeeCount(enterpriseInfo.getEmployeeCount());
                metrics.setOrganizationLevels(enterpriseInfo.getOrganizationLevels());

                result.setKeyMetrics(metrics);

                // 将关键指标同步到basicInfo，供监控大屏直接使用
                basicInfo.setEmployeeCount(enterpriseInfo.getEmployeeCount());
                basicInfo.setTotalAssets(keyMetrics.getOrDefault("TOTAL_ASSETS", BigDecimal.ZERO));
                // 子公司数量：查询组织架构中的下级企业数
                try {
                    Integer subsidiaryCount = enterpriseProfileInfoMapper.getSubsidiaryCount(param.getEnterpriseId());
                    basicInfo.setSubsidiaryCount(subsidiaryCount != null ? subsidiaryCount : 0);
                } catch (Exception ex) {
                    basicInfo.setSubsidiaryCount(0);
                    log.warn("获取子公司数量失败，企业ID: {}", param.getEnterpriseId());
                }
                // 风险等级默认低风险
                basicInfo.setRiskLevel("1");
            }

            // 设置系统状态
            EnterpriseInfoVO.SystemStatus systemStatus = new EnterpriseInfoVO.SystemStatus();
            systemStatus.setLastUpdateTime(DateUtil.getNowTime());
            systemStatus.setDataStatus("NORMAL");
            systemStatus.setRefreshInterval(300); // 5分钟刷新间隔
            
            result.setSystemStatus(systemStatus);

            log.info("成功获取企业信息，企业名称: {}", enterpriseInfo.getEnterpriseName());
            return result;
            
        } catch (Exception e) {
            log.error("获取企业信息失败，企业ID: {}", param.getEnterpriseId(), e);
            throw new RuntimeException("获取企业信息失败: " + e.getMessage());
        }
    }

    @Override
    public FinancialRadarVO getFinancialRadarData(FinancialRadarQueryParam param) {
        log.info("获取财务雷达图数据，企业ID: {}, 期间: {}", param.getEnterpriseId(), param.getPeriodDate());
        
        try {
            FinancialRadarVO result = new FinancialRadarVO();
            
            // 获取雷达图数据
            List<Map<String, Object>> radarDataList = financialIndicatorDataMapper.getFinancialRadarData(
                param.getEnterpriseId(), param.getPeriodDate());
            
            FinancialRadarVO.RadarData radarData = new FinancialRadarVO.RadarData();
            List<FinancialRadarVO.Dimension> dimensions = new ArrayList<>();
            
            BigDecimal totalScore = BigDecimal.ZERO;
            int dimensionCount = 0;
            
            for (Map<String, Object> data : radarDataList) {
                FinancialRadarVO.Dimension dimension = new FinancialRadarVO.Dimension();
                dimension.setDimension((String) data.get("INDICATOR_TYPE"));
                dimension.setDimensionName(getIndicatorTypeName((String) data.get("INDICATOR_TYPE")));
                
                // 计算得分（这里使用简化的评分逻辑）
                BigDecimal avgValue = (BigDecimal) data.get("AVG_VALUE");
                BigDecimal score = calculateDimensionScore(dimension.getDimension(), avgValue);
                
                dimension.setScore(score);
                dimension.setMaxScore(new BigDecimal("100"));
                dimension.setLevel(getScoreLevel(score));
                
                dimensions.add(dimension);
                totalScore = totalScore.add(score);
                dimensionCount++;
            }
            
            radarData.setDimensions(dimensions);
            
            // 计算综合得分
            if (dimensionCount > 0) {
                BigDecimal overallScore = totalScore.divide(new BigDecimal(dimensionCount), 2, RoundingMode.HALF_UP);
                radarData.setOverallScore(overallScore);
                radarData.setOverallLevel(getScoreLevel(overallScore));
            }
            
            result.setRadarData(radarData);

            // 获取关键指标
            if (param.getIncludeKeyIndicators()) {
                List<FinancialIndicatorData> keyIndicatorDataList = financialIndicatorDataMapper.getKeyFinancialIndicators(
                    param.getEnterpriseId(), param.getPeriodDate());
                
                List<FinancialRadarVO.KeyIndicator> keyIndicators = new ArrayList<>();
                
                for (FinancialIndicatorData data : keyIndicatorDataList) {
                    FinancialRadarVO.KeyIndicator indicator = new FinancialRadarVO.KeyIndicator();
                    indicator.setIndicatorCode(data.getIndicatorCode());
                    indicator.setIndicatorName(data.getIndicatorName());
                    indicator.setCurrentValue(data.getIndicatorValue());
                    indicator.setUnit(data.getUnit());
                    
                    // 计算变化率
                    BigDecimal changeRate = financialIndicatorDataMapper.calculateYearOverYearGrowth(
                        param.getEnterpriseId(), data.getIndicatorCode());
                    indicator.setChangeRate(changeRate);
                    indicator.setChangeDirection(getChangeDirection(changeRate));
                    
                    // 设置等级和颜色
                    indicator.setLevel(getIndicatorLevel(data.getIndicatorValue(), data.getIndicatorCode()));
                    indicator.setColor(getIndicatorColor(indicator.getLevel()));
                    
                    keyIndicators.add(indicator);
                }
                
                result.setKeyIndicators(keyIndicators);
            }

            log.info("成功获取财务雷达图数据，维度数量: {}", dimensions.size());
            return result;
            
        } catch (Exception e) {
            log.error("获取财务雷达图数据失败，企业ID: {}", param.getEnterpriseId(), e);
            throw new RuntimeException("获取财务雷达图数据失败: " + e.getMessage());
        }
    }

    @Override
    public List<EnterpriseProfileInfo> getEnterpriseList() {
        log.info("获取企业列表");
        
        try {
            List<EnterpriseProfileInfo> enterpriseList = enterpriseProfileInfoMapper.getActiveEnterpriseList();
            log.info("成功获取企业列表，数量: {}", enterpriseList.size());
            return enterpriseList;
            
        } catch (Exception e) {
            log.error("获取企业列表失败", e);
            throw new RuntimeException("获取企业列表失败: " + e.getMessage());
        }
    }

    @Override
    public Boolean refreshEnterpriseData(String enterpriseId) {
        log.info("刷新企业数据，企业ID: {}", enterpriseId);
        
        try {
            // 这里可以实现数据刷新逻辑，比如重新计算指标等
            // 目前返回成功
            log.info("企业数据刷新成功，企业ID: {}", enterpriseId);
            return true;
            
        } catch (Exception e) {
            log.error("刷新企业数据失败，企业ID: {}", enterpriseId, e);
            return false;
        }
    }

    @Override
    public Boolean checkEnterpriseExists(String enterpriseId) {
        if (StringUtil.isEmpty(enterpriseId)) {
            return false;
        }
        
        try {
            Integer count = enterpriseProfileInfoMapper.checkEnterpriseExists(enterpriseId);
            return count != null && count > 0;
            
        } catch (Exception e) {
            log.error("检查企业是否存在失败，企业ID: {}", enterpriseId, e);
            return false;
        }
    }

    @Override
    public String resolveEnterpriseId(String input, String name) {
        // 1. 先尝试直接按企业ID查找
        if (!StringUtil.isEmpty(input)) {
            try {
                Integer count = enterpriseProfileInfoMapper.checkEnterpriseExists(input);
                if (count != null && count > 0) {
                    return input;
                }
            } catch (Exception e) {
                log.warn("按企业ID查找失败: {}", input);
            }

            // 2. 尝试按企业编码查找
            try {
                String idByCode = enterpriseProfileInfoMapper.getEnterpriseIdByCode(input);
                if (idByCode != null) {
                    return idByCode;
                }
            } catch (Exception e) {
                log.warn("按企业编码查找失败: {}", input);
            }
        }

        // 3. 尝试按企业名称查找
        if (!StringUtil.isEmpty(name)) {
            try {
                String idByName = enterpriseProfileInfoMapper.getEnterpriseIdByName(name);
                if (idByName != null) {
                    return idByName;
                }
            } catch (Exception e) {
                log.warn("按企业名称查找失败: {}", name);
            }
        }

        log.warn("无法解析企业ID，输入: {}，名称: {}", input, name);
        return null;
    }

    @Override
    public Map<String, Object> getPersonnelAnalysisData(String enterpriseId) {
        log.info("获取人员分析数据，企业ID: {}", enterpriseId);
        Map<String, Object> result = new HashMap<>();

        try {
            // 从企业信息中获取员工数
            EnterpriseProfileInfo enterpriseInfo = enterpriseProfileInfoMapper.getEnterpriseInfoWithExtended(enterpriseId);
            int employeeCount = 0;
            if (enterpriseInfo != null && enterpriseInfo.getEmployeeCount() != null) {
                employeeCount = enterpriseInfo.getEmployeeCount();
            }
            result.put("totalEmployees", employeeCount);

            // 从财务指标表中获取人员相关指标
            List<FinancialIndicatorData> indicators = financialIndicatorDataMapper.getLatestIndicatorsByEnterprise(enterpriseId);
            if (indicators != null) {
                for (FinancialIndicatorData data : indicators) {
                    String code = data.getIndicatorCode();
                    BigDecimal value = data.getIndicatorValue();
                    if (code == null || value == null) continue;

                    switch (code) {
                        case "ORG_HIERARCHY_LEVELS":
                            result.put("organizationLevels", value.intValue());
                            break;
                        case "LEADERSHIP_COUNT":
                            result.put("leadershipCount", value.intValue());
                            break;
                        case "TOTAL_LABOR_COST":
                            result.put("totalLaborCost", value);
                            break;
                        case "TOTAL_EMPLOYEE_COMPENSATION":
                            result.put("totalCompensation", value);
                            break;
                        case "EDUCATION_DISTRIBUTION":
                            result.put("educationDistributionText", data.getIndicatorName());
                            break;
                        case "THREE_YEAR_EMPLOYEE_TREND":
                            result.put("employeeTrend", value.intValue());
                            break;
                    }
                }
            }

            // 学历分布 - 转为数组格式供前端组件直接渲染
            List<Map<String, Object>> educationList = new ArrayList<>();
            if (result.containsKey("educationDistributionText")) {
                String eduText = (String) result.get("educationDistributionText");
                // 尝试解析文本格式如 "本科50%,硕士30%,博士20%"
                if (eduText != null && !eduText.isEmpty()) {
                    String[] parts = eduText.split("[,，、]");
                    for (String part : parts) {
                        part = part.trim();
                        if (part.isEmpty()) continue;
                        Map<String, Object> edu = new HashMap<>();
                        edu.put("educationName", part);
                        edu.put("percentage", 0);
                        edu.put("count", 0);
                        educationList.add(edu);
                    }
                }
            }
            result.put("educationDistribution", educationList);

            // 年龄分布 - 暂无数据源，返回空数组
            result.put("ageDistribution", new ArrayList<>());

            // 默认值
            result.putIfAbsent("averageAge", 0);
            result.putIfAbsent("turnoverRate", BigDecimal.ZERO);

        } catch (Exception e) {
            log.error("获取人员分析数据失败，企业ID: {}", enterpriseId, e);
        }

        return result;
    }

    @Override
    public Map<String, Object> getAuditData(String enterpriseId) {
        log.info("获取审计数据，企业ID: {}", enterpriseId);
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> auditRecords = new ArrayList<>();
            int totalIssues = 0;

            List<FinancialIndicatorData> indicators = financialIndicatorDataMapper.getLatestIndicatorsByEnterprise(enterpriseId);
            if (indicators != null) {
                for (FinancialIndicatorData data : indicators) {
                    String code = data.getIndicatorCode();
                    BigDecimal value = data.getIndicatorValue();
                    if (code == null) continue;

                    switch (code) {
                        case "INTERNAL_AUDIT_ISSUES":
                        case "GROUP_AUDIT_ISSUES":
                        case "SOCIAL_AUDIT_ISSUES":
                        case "OTHER_INTERNAL_CHECK_ISSUES":
                        case "GOVERNMENT_CHECK_COUNT":
                            Map<String, Object> record = new HashMap<>();
                            record.put("auditType", code);
                            record.put("auditTypeName", data.getIndicatorName());
                            record.put("issueCount", value != null ? value.intValue() : 0);
                            record.put("auditDate", data.getPeriodEndDate());
                            auditRecords.add(record);
                            if (value != null) totalIssues += value.intValue();
                            break;
                    }
                }
            }

            result.put("auditRecords", auditRecords);
            
            Map<String, Object> auditStats = new HashMap<>();
            auditStats.put("totalAudits", auditRecords.size());
            auditStats.put("totalIssues", totalIssues);
            auditStats.put("resolvedIssues", 0);
            result.put("auditStats", auditStats);

        } catch (Exception e) {
            log.error("获取审计数据失败，企业ID: {}", enterpriseId, e);
        }

        return result;
    }

    @Override
    public Map<String, Object> getLegalData(String enterpriseId) {
        log.info("获取法律案件数据，企业ID: {}", enterpriseId);
        Map<String, Object> result = new HashMap<>();

        try {
            List<Map<String, Object>> legalCases = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            List<FinancialIndicatorData> indicators = financialIndicatorDataMapper.getLatestIndicatorsByEnterprise(enterpriseId);
            if (indicators != null) {
                for (FinancialIndicatorData data : indicators) {
                    String code = data.getIndicatorCode();
                    BigDecimal value = data.getIndicatorValue();
                    if (code == null) continue;

                    if ("LEGAL_CASE_OVERVIEW".equals(code)) {
                        Map<String, Object> caseRecord = new HashMap<>();
                        caseRecord.put("caseType", "1");
                        caseRecord.put("caseTypeName", data.getIndicatorName());
                        caseRecord.put("caseStatus", "2");
                        caseRecord.put("caseStatusName", "已结案");
                        caseRecord.put("caseTitle", data.getIndicatorName());
                        caseRecord.put("caseDate", data.getPeriodEndDate());
                        caseRecord.put("involvedAmount", value != null ? value : BigDecimal.ZERO);
                        legalCases.add(caseRecord);
                        if (value != null) totalAmount = totalAmount.add(value);
                    }
                }
            }

            result.put("legalCases", legalCases);

            Map<String, Object> legalStats = new HashMap<>();
            legalStats.put("totalCases", legalCases.size());
            legalStats.put("activeCases", 0);
            legalStats.put("totalAmount", totalAmount);
            result.put("legalStats", legalStats);

        } catch (Exception e) {
            log.error("获取法律案件数据失败，企业ID: {}", enterpriseId, e);
        }

        return result;
    }

    // 私有辅助方法
    private String getIndustryTypeName(String industryType) {
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("TECHNOLOGY", "科技行业");
        typeMap.put("FINANCE", "金融行业");
        typeMap.put("MANUFACTURING", "制造业");
        typeMap.put("RETAIL", "零售业");
        typeMap.put("ENERGY", "能源行业");
        return typeMap.getOrDefault(industryType, industryType);
    }

    private String getEnterpriseScaleName(String enterpriseScale) {
        Map<String, String> scaleMap = new HashMap<>();
        scaleMap.put("LARGE", "大型企业");
        scaleMap.put("MEDIUM", "中型企业");
        scaleMap.put("SMALL", "小型企业");
        scaleMap.put("MICRO", "微型企业");
        return scaleMap.getOrDefault(enterpriseScale, enterpriseScale);
    }

    private String getStatusName(String status) {
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("ACTIVE", "正常");
        statusMap.put("INACTIVE", "停用");
        return statusMap.getOrDefault(status, status);
    }

    private String getIndicatorTypeName(String indicatorType) {
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("PROFITABILITY", "盈利能力");
        typeMap.put("ASSET_QUALITY", "资产质量");
        typeMap.put("SOLVENCY", "偿债能力");
        typeMap.put("GROWTH", "经营增长");
        typeMap.put("SUPPLEMENT", "补充指标");
        return typeMap.getOrDefault(indicatorType, indicatorType);
    }

    private BigDecimal calculateDimensionScore(String dimension, BigDecimal avgValue) {
        // 简化的评分逻辑，实际应该根据行业标准和历史数据计算
        if (avgValue == null) {
            return BigDecimal.ZERO;
        }
        
        // 这里使用简单的线性映射，实际应该更复杂
        BigDecimal score = avgValue.multiply(new BigDecimal("10"));
        if (score.compareTo(new BigDecimal("100")) > 0) {
            score = new BigDecimal("100");
        }
        if (score.compareTo(BigDecimal.ZERO) < 0) {
            score = BigDecimal.ZERO;
        }
        
        return score.setScale(2, RoundingMode.HALF_UP);
    }

    private String getScoreLevel(BigDecimal score) {
        if (score.compareTo(new BigDecimal("90")) >= 0) {
            return "优秀";
        } else if (score.compareTo(new BigDecimal("80")) >= 0) {
            return "良好";
        } else if (score.compareTo(new BigDecimal("70")) >= 0) {
            return "一般";
        } else {
            return "较差";
        }
    }

    private String getChangeDirection(BigDecimal changeRate) {
        if (changeRate == null) {
            return "STABLE";
        }
        
        if (changeRate.compareTo(new BigDecimal("1")) > 0) {
            return "UP";
        } else if (changeRate.compareTo(new BigDecimal("-1")) < 0) {
            return "DOWN";
        } else {
            return "STABLE";
        }
    }

    private String getIndicatorLevel(BigDecimal value, String indicatorCode) {
        // 简化的指标等级判断逻辑
        if (value == null) {
            return "一般";
        }
        
        // 根据不同指标类型设置不同的判断标准
        switch (indicatorCode) {
            case "ROE":
                return value.compareTo(new BigDecimal("15")) >= 0 ? "优秀" : 
                       value.compareTo(new BigDecimal("10")) >= 0 ? "良好" : "一般";
            case "ROA":
                return value.compareTo(new BigDecimal("8")) >= 0 ? "优秀" : 
                       value.compareTo(new BigDecimal("5")) >= 0 ? "良好" : "一般";
            default:
                return "一般";
        }
    }

    private String getIndicatorColor(String level) {
        Map<String, String> colorMap = new HashMap<>();
        colorMap.put("优秀", "green");
        colorMap.put("良好", "blue");
        colorMap.put("一般", "orange");
        colorMap.put("较差", "red");
        return colorMap.getOrDefault(level, "gray");
    }

    @Override
    public List<Map<String, Object>> getIndicatorWarnings(String enterpriseId) {
        log.info("获取指标预警数据，企业ID: {}", enterpriseId);

        List<Map<String, Object>> warningList = new ArrayList<>();

        try {
            // 检查数据库表是否存在，如果不存在直接返回模拟数据
            if (!checkTablesExist()) {
                log.warn("财务指标相关表不存在，返回模拟预警数据");
                return getMockWarningData(enterpriseId);
            }

            // 获取企业的关键财务指标数据
            List<FinancialIndicatorData> indicatorDataList = financialIndicatorDataMapper.getLatestIndicatorsByEnterprise(enterpriseId);

            if (indicatorDataList == null || indicatorDataList.isEmpty()) {
                log.warn("企业 {} 没有找到财务指标数据，返回模拟预警数据", enterpriseId);
                return getMockWarningData(enterpriseId);
            }

            // 遍历指标数据，检查预警条件
            for (FinancialIndicatorData data : indicatorDataList) {
                Map<String, Object> warningItem = checkIndicatorWarning(data);
                if (warningItem != null) {
                    warningList.add(warningItem);
                }
            }

            // 如果没有预警数据，返回正常状态的关键指标
            if (warningList.isEmpty()) {
                warningList = getNormalIndicatorStatus(indicatorDataList);
            }

        } catch (Exception e) {
            log.error("获取指标预警数据失败，企业ID: {}", enterpriseId, e);
            // 返回模拟数据
            return getMockWarningData(enterpriseId);
        }

        return warningList;
    }

    /**
     * 检查单个指标的预警状态
     */
    private Map<String, Object> checkIndicatorWarning(FinancialIndicatorData data) {
        String indicatorCode = data.getIndicatorCode();
        BigDecimal value = data.getIndicatorValue();

        if (value == null) {
            return null;
        }

        Map<String, Object> warning = new HashMap<>();
        warning.put("indicatorCode", indicatorCode);
        warning.put("indicatorName", getIndicatorName(indicatorCode));
        warning.put("currentValue", value.toString());
        warning.put("unit", getIndicatorUnit(indicatorCode));
        warning.put("periodDate", data.getPeriodEndDate());

        // 根据指标类型判断预警级别
        int warningLevel = getWarningLevel(indicatorCode, value);
        warning.put("warningLevel", warningLevel);
        warning.put("thresholdValue", getThresholdValue(indicatorCode));
        warning.put("showValue", true);

        return warning;
    }

    /**
     * 获取预警级别
     */
    private int getWarningLevel(String indicatorCode, BigDecimal value) {
        switch (indicatorCode) {
            case "DEBT_TO_ASSET_RATIO":
                if (value.compareTo(new BigDecimal("70")) > 0) return 3; // 危险
                if (value.compareTo(new BigDecimal("60")) > 0) return 2; // 预警
                return 1; // 正常
            case "QUICK_RATIO":
                if (value.compareTo(new BigDecimal("0.8")) < 0) return 3; // 危险
                if (value.compareTo(new BigDecimal("1.0")) < 0) return 2; // 预警
                return 1; // 正常
            case "ROE_EXCLUDING_MINORITY":
                if (value.compareTo(new BigDecimal("5")) < 0) return 2; // 预警
                return 1; // 正常
            case "SALES_GROWTH_RATE":
                if (value.compareTo(BigDecimal.ZERO) < 0) return 3; // 危险
                return 1; // 正常
            default:
                return 1; // 默认正常
        }
    }

    /**
     * 获取指标名称
     */
    private String getIndicatorName(String indicatorCode) {
        Map<String, String> nameMap = new HashMap<>();

        // 原有指标
        nameMap.put("ROE", "净资产收益率");
        nameMap.put("ROA", "总资产收益率");
        nameMap.put("SALES_PROFIT_RATE", "销售利润率");
        nameMap.put("ASSET_LIABILITY_RATIO", "资产负债率");
        nameMap.put("CURRENT_RATIO", "流动比率");

        // 扩展的财务指标
        nameMap.put("NET_ASSET_RETURN_RATE", "净资产收益率");
        nameMap.put("TOTAL_ASSET_RETURN_RATE", "总资产报酬率");
        nameMap.put("SALES_PROFIT_MARGIN", "销售（营业）利润率");
        nameMap.put("SURPLUS_CASH_GUARANTEE_RATIO", "盈余现金保障倍数");
        nameMap.put("COST_EXPENSE_PROFIT_RATE", "成本费用利润率");
        nameMap.put("CAPITAL_RETURN_RATE", "资本收益率");
        nameMap.put("OPERATING_CASH_RATIO", "营业现金比率");
        nameMap.put("TOTAL_ASSET_TURNOVER", "总资产周转率（次）");
        nameMap.put("ACCOUNTS_RECEIVABLE_TURNOVER", "应收账款周转率（次）");
        nameMap.put("THREE_FUNDS_BALANCE", "三资余额");
        nameMap.put("CURRENT_ASSET_TURNOVER", "流动资产周转率（次）");
        nameMap.put("TOTAL_ASSET_CASH_RECOVERY_RATE", "总资产现金回收率");
        nameMap.put("DEBT_TO_ASSET_RATIO", "资产负债率");
        nameMap.put("INTEREST_COVERAGE_RATIO", "已获利息倍数");
        nameMap.put("QUICK_RATIO", "速动比率");
        nameMap.put("CASH_CURRENT_LIABILITY_RATIO", "现金流动负债比率");
        nameMap.put("INTEREST_BEARING_DEBT_RATIO", "带息负债比率");
        nameMap.put("SALES_GROWTH_RATE", "销售（营业）增长率");
        nameMap.put("CAPITAL_PRESERVATION_APPRECIATION_RATE", "资本保值增值率");
        nameMap.put("SALES_PROFIT_GROWTH_RATE", "销售（营业）利润增长率");
        nameMap.put("TOTAL_ASSET_GROWTH_RATE", "总资产增长率");
        nameMap.put("INVENTORY_TURNOVER", "存货周转率（次）");
        nameMap.put("TWO_FUNDS_TO_CURRENT_ASSETS_RATIO", "两金占流动资产比率");
        nameMap.put("COST_EXPENSE_TO_REVENUE_RATIO", "成本费用总额占营业收入的比率");
        nameMap.put("ECONOMIC_VALUE_ADDED_RATE", "经济增加值率");
        nameMap.put("EBITDA_RATE", "EBITDA率");
        nameMap.put("CAPITAL_ACCUMULATION_RATE", "资本积累率");

        // 兼容旧的指标代码
        nameMap.put("ROE_EXCLUDING_MINORITY", "净资产收益率");
        nameMap.put("CASH_FLOW_RATIO", "现金流量比率");

        // ENT038等企业的额外指标代码映射
        nameMap.put("CASH_COVERAGE_RATIO", "现金覆盖率");
        nameMap.put("COST_PROFIT_RATIO", "成本利润率");
        nameMap.put("RECEIVABLES_TURNOVER", "应收账款周转率");
        nameMap.put("TOTAL_ASSET_CASH_RECOVERY", "总资产现金回收率");
        nameMap.put("CASH_CURRENT_DEBT_RATIO", "现金流动负债比率");
        nameMap.put("CAPITAL_PRESERVATION_RATE", "资本保值率");
        nameMap.put("PROFIT_GROWTH_RATE", "利润增长率");
        nameMap.put("TWO_FUNDS_RATIO", "两金占比");
        nameMap.put("COST_TO_REVENUE_RATIO", "成本收入比");
        nameMap.put("EVA_RATE", "经济增加值率");
        nameMap.put("ORG_HIERARCHY_LEVELS", "组织层级数");
        nameMap.put("LEADERSHIP_COUNT", "领导人数");
        nameMap.put("THREE_YEAR_EMPLOYEE_TREND", "三年员工趋势");
        nameMap.put("EDUCATION_DISTRIBUTION", "学历分布");
        nameMap.put("TOTAL_LABOR_COST", "总人工成本");
        nameMap.put("TOTAL_EMPLOYEE_COMPENSATION", "员工总薪酬");
        nameMap.put("LEGAL_CASE_OVERVIEW", "法律案件概览");
        nameMap.put("INTERNAL_AUDIT_ISSUES", "内部审计问题");
        nameMap.put("GROUP_AUDIT_ISSUES", "集团审计问题");
        nameMap.put("SOCIAL_AUDIT_ISSUES", "社会审计问题");
        nameMap.put("OTHER_INTERNAL_CHECK_ISSUES", "其他内部检查问题");
        nameMap.put("GOVERNMENT_CHECK_COUNT", "政府检查次数");
        nameMap.put("RISK_ASSESSMENT_SCORE", "风险评估得分");

        return nameMap.getOrDefault(indicatorCode, indicatorCode);
    }

    /**
     * 获取指标单位
     */
    private String getIndicatorUnit(String indicatorCode) {
        Map<String, String> unitMap = new HashMap<>();

        // 原有指标
        unitMap.put("ROE", "%");
        unitMap.put("ROA", "%");
        unitMap.put("SALES_PROFIT_RATE", "%");
        unitMap.put("ASSET_LIABILITY_RATIO", "%");
        unitMap.put("CURRENT_RATIO", "");

        // 扩展的财务指标
        unitMap.put("NET_ASSET_RETURN_RATE", "%");
        unitMap.put("TOTAL_ASSET_RETURN_RATE", "%");
        unitMap.put("SALES_PROFIT_MARGIN", "%");
        unitMap.put("SURPLUS_CASH_GUARANTEE_RATIO", "倍");
        unitMap.put("COST_EXPENSE_PROFIT_RATE", "%");
        unitMap.put("CAPITAL_RETURN_RATE", "%");
        unitMap.put("OPERATING_CASH_RATIO", "%");
        unitMap.put("TOTAL_ASSET_TURNOVER", "次");
        unitMap.put("ACCOUNTS_RECEIVABLE_TURNOVER", "次");
        unitMap.put("THREE_FUNDS_BALANCE", "万元");
        unitMap.put("CURRENT_ASSET_TURNOVER", "次");
        unitMap.put("TOTAL_ASSET_CASH_RECOVERY_RATE", "%");
        unitMap.put("DEBT_TO_ASSET_RATIO", "%");
        unitMap.put("INTEREST_COVERAGE_RATIO", "倍");
        unitMap.put("QUICK_RATIO", "");
        unitMap.put("CASH_CURRENT_LIABILITY_RATIO", "%");
        unitMap.put("INTEREST_BEARING_DEBT_RATIO", "%");
        unitMap.put("SALES_GROWTH_RATE", "%");
        unitMap.put("CAPITAL_PRESERVATION_APPRECIATION_RATE", "%");
        unitMap.put("SALES_PROFIT_GROWTH_RATE", "%");
        unitMap.put("TOTAL_ASSET_GROWTH_RATE", "%");
        unitMap.put("INVENTORY_TURNOVER", "次");
        unitMap.put("TWO_FUNDS_TO_CURRENT_ASSETS_RATIO", "%");
        unitMap.put("COST_EXPENSE_TO_REVENUE_RATIO", "%");
        unitMap.put("ECONOMIC_VALUE_ADDED_RATE", "%");
        unitMap.put("EBITDA_RATE", "%");
        unitMap.put("CAPITAL_ACCUMULATION_RATE", "%");

        // 兼容旧的指标代码
        unitMap.put("ROE_EXCLUDING_MINORITY", "%");
        unitMap.put("CASH_FLOW_RATIO", "%");

        // ENT038等企业的额外指标单位映射
        unitMap.put("CASH_COVERAGE_RATIO", "倍");
        unitMap.put("COST_PROFIT_RATIO", "%");
        unitMap.put("RECEIVABLES_TURNOVER", "次");
        unitMap.put("TOTAL_ASSET_CASH_RECOVERY", "%");
        unitMap.put("CASH_CURRENT_DEBT_RATIO", "%");
        unitMap.put("CAPITAL_PRESERVATION_RATE", "%");
        unitMap.put("PROFIT_GROWTH_RATE", "%");
        unitMap.put("TWO_FUNDS_RATIO", "%");
        unitMap.put("COST_TO_REVENUE_RATIO", "%");
        unitMap.put("EVA_RATE", "%");
        unitMap.put("ORG_HIERARCHY_LEVELS", "级");
        unitMap.put("LEADERSHIP_COUNT", "人");
        unitMap.put("THREE_YEAR_EMPLOYEE_TREND", "人");
        unitMap.put("EDUCATION_DISTRIBUTION", "%");
        unitMap.put("TOTAL_LABOR_COST", "万元");
        unitMap.put("TOTAL_EMPLOYEE_COMPENSATION", "万元");
        unitMap.put("LEGAL_CASE_OVERVIEW", "件");
        unitMap.put("INTERNAL_AUDIT_ISSUES", "项");
        unitMap.put("GROUP_AUDIT_ISSUES", "项");
        unitMap.put("SOCIAL_AUDIT_ISSUES", "项");
        unitMap.put("OTHER_INTERNAL_CHECK_ISSUES", "项");
        unitMap.put("GOVERNMENT_CHECK_COUNT", "次");
        unitMap.put("RISK_ASSESSMENT_SCORE", "分");

        return unitMap.getOrDefault(indicatorCode, "");
    }

    /**
     * 获取阈值
     */
    private String getThresholdValue(String indicatorCode) {
        Map<String, String> thresholdMap = new HashMap<>();
        thresholdMap.put("DEBT_TO_ASSET_RATIO", "70");
        thresholdMap.put("QUICK_RATIO", "1.0");
        thresholdMap.put("ROE_EXCLUDING_MINORITY", "10");
        thresholdMap.put("SALES_GROWTH_RATE", "0");
        thresholdMap.put("CURRENT_RATIO", "1.0");
        thresholdMap.put("CASH_FLOW_RATIO", "10");
        return thresholdMap.getOrDefault(indicatorCode, "0");
    }

    /**
     * 获取正常状态的关键指标
     */
    private List<Map<String, Object>> getNormalIndicatorStatus(List<FinancialIndicatorData> indicatorDataList) {
        List<Map<String, Object>> normalList = new ArrayList<>();

        // 选择几个关键指标显示正常状态
        String[] keyIndicators = {"DEBT_TO_ASSET_RATIO", "QUICK_RATIO", "ROE_EXCLUDING_MINORITY"};

        for (String indicatorCode : keyIndicators) {
            for (FinancialIndicatorData data : indicatorDataList) {
                if (indicatorCode.equals(data.getIndicatorCode())) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("indicatorCode", indicatorCode);
                    item.put("indicatorName", getIndicatorName(indicatorCode));
                    item.put("currentValue", data.getIndicatorValue().toString());
                    item.put("unit", getIndicatorUnit(indicatorCode));
                    item.put("warningLevel", 1); // 正常
                    item.put("thresholdValue", getThresholdValue(indicatorCode));
                    item.put("showValue", true);
                    item.put("periodDate", data.getPeriodEndDate());
                    normalList.add(item);
                    break;
                }
            }
        }

        return normalList;
    }

    /**
     * 获取模拟预警数据
     */
    private List<Map<String, Object>> getMockWarningData(String enterpriseId) {
        List<Map<String, Object>> mockData = new ArrayList<>();

        // 模拟一些预警指标
        Map<String, Object> item1 = new HashMap<>();
        item1.put("indicatorCode", "DEBT_TO_ASSET_RATIO");
        item1.put("indicatorName", "资产负债率");
        item1.put("currentValue", "75.2");
        item1.put("unit", "%");
        item1.put("warningLevel", 2);
        item1.put("thresholdValue", "70");
        item1.put("showValue", true);
        item1.put("periodDate", DateUtil.getNowTime("yyyy-MM-dd"));
        mockData.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("indicatorCode", "QUICK_RATIO");
        item2.put("indicatorName", "速动比率");
        item2.put("currentValue", "0.85");
        item2.put("unit", "");
        item2.put("warningLevel", 3);
        item2.put("thresholdValue", "1.0");
        item2.put("showValue", true);
        item2.put("periodDate", DateUtil.getNowTime("yyyy-MM-dd"));
        mockData.add(item2);

        Map<String, Object> item3 = new HashMap<>();
        item3.put("indicatorCode", "ROE_EXCLUDING_MINORITY");
        item3.put("indicatorName", "净资产收益率");
        item3.put("currentValue", "8.5");
        item3.put("unit", "%");
        item3.put("warningLevel", 1);
        item3.put("thresholdValue", "10");
        item3.put("showValue", true);
        item3.put("periodDate", DateUtil.getNowTime("yyyy-MM-dd"));
        mockData.add(item3);

        return mockData;
    }

    @Override
    public EnterpriseHologramVO getEnterpriseHologramData(String enterpriseId) {
        log.info("获取企业全息画像数据，企业ID: {}", enterpriseId);

        try {
            EnterpriseHologramVO result = new EnterpriseHologramVO();

            // 获取企业标签
            List<EnterpriseTagRel> tagRels = enterpriseHologramMapper.getEnterpriseTagsByEnterpriseId(enterpriseId);
            List<EnterpriseHologramVO.EnterpriseTag> enterpriseTags = new ArrayList<>();
            for (EnterpriseTagRel tagRel : tagRels) {
                EnterpriseHologramVO.EnterpriseTag tag = new EnterpriseHologramVO.EnterpriseTag();
                tag.setTagId(tagRel.getTagId());
                tag.setTagCode(tagRel.getTagCode());
                tag.setTagName(tagRel.getTagName());
                tag.setTagType(tagRel.getTagType());
                tag.setTagCategory(tagRel.getTagCategory());
                tag.setTagValue(tagRel.getTagValue());
                enterpriseTags.add(tag);
            }
            result.setEnterpriseTags(enterpriseTags);

            // 获取关键指标
            List<EnterpriseKeyMetrics> keyMetrics = enterpriseHologramMapper.getEnterpriseKeyMetricsByEnterpriseId(enterpriseId);
            List<EnterpriseHologramVO.KeyIndicator> keyIndicators = new ArrayList<>();
            for (EnterpriseKeyMetrics metric : keyMetrics) {
                EnterpriseHologramVO.KeyIndicator indicator = new EnterpriseHologramVO.KeyIndicator();
                indicator.setCode(metric.getMetricCode());
                indicator.setName(metric.getMetricName());
                indicator.setValue(metric.getMetricValueText() != null ? metric.getMetricValueText() :
                    (metric.getMetricValue() != null ? metric.getMetricValue().toString() : "0"));
                indicator.setUnit(metric.getMetricUnit());
                indicator.setType(metric.getMetricType());
                keyIndicators.add(indicator);
            }
            result.setKeyIndicators(keyIndicators);

            // 获取风险等级
            EnterpriseRiskLevel riskLevel = enterpriseHologramMapper.getCurrentRiskLevelByEnterpriseId(enterpriseId);
            if (riskLevel != null) {
                result.setRiskLevel(riskLevel.getRiskLevel());
                result.setRiskScore(riskLevel.getRiskScore() != null ? riskLevel.getRiskScore().toString() : "0");
                result.setRiskFactors(riskLevel.getRiskFactors());
            } else {
                result.setRiskLevel("1");
                result.setRiskScore("0");
                result.setRiskFactors("暂无风险评估数据");
            }

            log.info("成功获取企业全息画像数据，标签数量: {}, 指标数量: {}", enterpriseTags.size(), keyIndicators.size());
            return result;

        } catch (Exception e) {
            log.error("获取企业全息画像数据失败，企业ID: {}", enterpriseId, e);
            throw new RuntimeException("获取企业全息画像数据失败: " + e.getMessage());
        }
    }

    @Override
    public EnterpriseDetailInfoVO getEnterpriseDetailInfo(String enterpriseId) {
        log.info("获取企业详细信息，企业ID: {}", enterpriseId);

        try {
            // 获取企业基本信息
            EnterpriseProfileInfo enterpriseInfo = enterpriseProfileInfoMapper.getEnterpriseInfoWithExtended(enterpriseId);
            if (enterpriseInfo == null) {
                throw new RuntimeException("企业信息不存在");
            }

            // 获取企业扩展信息
            EnterpriseExtendedInfo extendedInfo = enterpriseHologramMapper.getEnterpriseExtendedInfoByEnterpriseId(enterpriseId);

            // 获取财务关键指标
            List<EnterpriseKeyMetrics> financialMetrics = enterpriseHologramMapper.getFinancialKeyMetrics(enterpriseId);
            Map<String, BigDecimal> metricsMap = new HashMap<>();
            for (EnterpriseKeyMetrics metric : financialMetrics) {
                metricsMap.put(metric.getMetricCode(), metric.getMetricValue());
            }

            // 构建返回对象
            EnterpriseDetailInfoVO result = new EnterpriseDetailInfoVO();
            BeanUtils.copyProperties(enterpriseInfo, result);

            // 设置扩展信息
            if (extendedInfo != null) {
                result.setRegisteredAddress(extendedInfo.getRegisteredAddress());
                result.setContactPhone(extendedInfo.getContactPhone());
                result.setEmail(extendedInfo.getEmail());
                result.setWebsite(extendedInfo.getWebsite());
                result.setUnifiedSocialCreditCode(extendedInfo.getUnifiedSocialCreditCode());
                result.setListingStatus(extendedInfo.getListingStatus());
                result.setListingExchange(extendedInfo.getListingExchange());
                result.setStockCode(extendedInfo.getStockCode());
            }

            // 设置财务数据
            BigDecimal totalAssets = metricsMap.get("TOTAL_ASSETS");
            BigDecimal revenue = metricsMap.get("REVENUE");

            if (totalAssets != null) {
                result.setTotalAssets(totalAssets);
                result.setTotalAssetsText(formatMoney(totalAssets));
            }

            if (revenue != null) {
                result.setTotalRevenue(revenue);
                result.setTotalRevenueText(formatMoney(revenue));
            }

            // 设置名称映射
            result.setIndustryTypeName(getIndustryTypeName(enterpriseInfo.getIndustryType()));
            result.setEnterpriseScaleName(getEnterpriseScaleName(enterpriseInfo.getEnterpriseScale()));
            result.setStatusName(getStatusName(enterpriseInfo.getStatus()));

            // 格式化日期
            if (enterpriseInfo.getEstablishmentDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                result.setEstablishmentDate(sdf.format(enterpriseInfo.getEstablishmentDate()));
            }

            log.info("成功获取企业详细信息，企业名称: {}", enterpriseInfo.getEnterpriseName());
            return result;

        } catch (Exception e) {
            log.error("获取企业详细信息失败，企业ID: {}", enterpriseId, e);
            throw new RuntimeException("获取企业详细信息失败: " + e.getMessage());
        }
    }

    /**
     * 格式化金额
     */
    private String formatMoney(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0万元";
        }

        if (value.compareTo(new BigDecimal("100000000")) >= 0) {
            return value.divide(new BigDecimal("100000000"), 1, RoundingMode.HALF_UP) + "亿元";
        } else if (value.compareTo(new BigDecimal("10000")) >= 0) {
            return value.divide(new BigDecimal("10000"), 1, RoundingMode.HALF_UP) + "万元";
        } else {
            return value.setScale(1, RoundingMode.HALF_UP) + "元";
        }
    }



    /**
     * 检查数据库表是否存在
     */
    private boolean checkTablesExist() {
        try {
            // 尝试查询表是否存在，如果查询失败说明表不存在
            financialIndicatorDataMapper.getLatestPeriodDate("TEST");
            return true;
        } catch (Exception e) {
            log.warn("财务指标数据表不存在或不可访问: {}", e.getMessage());
            return false;
        }
    }
}
