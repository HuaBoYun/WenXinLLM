package com.huabo.cybermonitor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.EquityStructure;
import com.huabo.cybermonitor.mapper.EquityStructureMapper;
import com.huabo.cybermonitor.service.IEquityStructureService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.EquityStructureQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 股权结构服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class EquityStructureServiceImpl extends ServiceImpl<EquityStructureMapper, EquityStructure> implements IEquityStructureService {

    @Autowired
    private EquityStructureMapper equityStructureMapper;

    @Override
    public PageResult<EquityStructure> getEquityStructureList(EquityStructureQueryVO queryVO) {
        try {
            List<EquityStructure> list = equityStructureMapper.selectEquityStructureList(queryVO);
            
            // 计算总数（这里简化处理，实际应该有单独的count查询）
            QueryWrapper<EquityStructure> countWrapper = new QueryWrapper<>();
            if (queryVO.getInvesteeEnterpriseId() != null) {
                countWrapper.eq("INVESTEE_ENTERPRISE_ID", queryVO.getInvesteeEnterpriseId());
            }
            if (queryVO.getInvestorEnterpriseId() != null) {
                countWrapper.eq("INVESTOR_ENTERPRISE_ID", queryVO.getInvestorEnterpriseId());
            }
            long total = this.count(countWrapper);

            return new PageResult<EquityStructure>((int)total, list);
        } catch (Exception e) {
            log.error("查询股权结构列表失败", e);
            return new PageResult<EquityStructure>(0, new ArrayList<>());
        }
    }

    @Override
    public EquityStructure getEquityStructureById(String equityId) {
        try {
            return this.getById(equityId);
        } catch (Exception e) {
            log.error("根据ID获取股权结构详情失败，equityId: {}", equityId, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addEquityStructure(EquityStructure equityStructure) {
        try {
            equityStructure.setCreateTime(new Date());
            equityStructure.setUpdateTime(new Date());
            
            // 计算间接持股比例和综合持股比例
            this.calculateShareholdingRatios(equityStructure);
            
            return this.save(equityStructure);
        } catch (Exception e) {
            log.error("新增股权结构失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEquityStructure(EquityStructure equityStructure) {
        try {
            equityStructure.setUpdateTime(new Date());
            
            // 重新计算间接持股比例和综合持股比例
            this.calculateShareholdingRatios(equityStructure);
            
            return this.updateById(equityStructure);
        } catch (Exception e) {
            log.error("更新股权结构失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEquityStructure(String equityId) {
        try {
            return this.removeById(equityId);
        } catch (Exception e) {
            log.error("删除股权结构失败，equityId: {}", equityId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteEquityStructure(List<String> equityIds) {
        try {
            return this.removeByIds(equityIds);
        } catch (Exception e) {
            log.error("批量删除股权结构失败", e);
            return false;
        }
    }

    @Override
    public List<EquityStructure> getEquityStructureByInvesteeEnterpriseId(String investeeEnterpriseId) {
        try {
            return equityStructureMapper.selectByInvesteeEnterpriseId(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("根据被投资企业ID查询股权结构失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<EquityStructure> getEquityStructureByInvestorEnterpriseId(String investorEnterpriseId) {
        try {
            return equityStructureMapper.selectByInvestorEnterpriseId(investorEnterpriseId);
        } catch (Exception e) {
            log.error("根据投资方企业ID查询股权结构失败，investorEnterpriseId: {}", investorEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<EquityStructure> getControllingEquity(String investeeEnterpriseId) {
        try {
            return equityStructureMapper.selectControllingEquity(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("查询控股股权结构失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<EquityStructure> getActualControllerEquity(String investeeEnterpriseId) {
        try {
            return equityStructureMapper.selectActualControllerEquity(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("查询实际控制人股权结构失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<EquityStructure> getEquityPenetrationUp(String investeeEnterpriseId, Integer maxLevel) {
        try {
            if (maxLevel == null || maxLevel <= 0) {
                maxLevel = 10; // 默认最大穿透10层
            }
            return equityStructureMapper.selectEquityPenetrationUp(investeeEnterpriseId, maxLevel);
        } catch (Exception e) {
            log.error("股权穿透查询（向上穿透）失败，investeeEnterpriseId: {}, maxLevel: {}", investeeEnterpriseId, maxLevel, e);
            return null;
        }
    }

    @Override
    public List<EquityStructure> getEquityPenetrationDown(String investorEnterpriseId, Integer maxLevel) {
        try {
            if (maxLevel == null || maxLevel <= 0) {
                maxLevel = 10; // 默认最大穿透10层
            }
            return equityStructureMapper.selectEquityPenetrationDown(investorEnterpriseId, maxLevel);
        } catch (Exception e) {
            log.error("股权穿透查询（向下穿透）失败，investorEnterpriseId: {}, maxLevel: {}", investorEnterpriseId, maxLevel, e);
            return null;
        }
    }

    @Override
    public List<EquityStructure> getEquityPath(String startEnterpriseId, String endEnterpriseId) {
        try {
            return equityStructureMapper.selectEquityPath(startEnterpriseId, endEnterpriseId);
        } catch (Exception e) {
            log.error("查询股权路径失败，startEnterpriseId: {}, endEnterpriseId: {}", startEnterpriseId, endEnterpriseId, e);
            return null;
        }
    }

    @Override
    public BigDecimal calculateIndirectShareholdingRatio(String investorEnterpriseId, String investeeEnterpriseId) {
        try {
            return equityStructureMapper.calculateIndirectShareholdingRatio(investorEnterpriseId, investeeEnterpriseId);
        } catch (Exception e) {
            log.error("计算间接持股比例失败，investorEnterpriseId: {}, investeeEnterpriseId: {}", investorEnterpriseId, investeeEnterpriseId, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal calculateTotalShareholdingRatio(String investorEnterpriseId, String investeeEnterpriseId) {
        try {
            return equityStructureMapper.calculateTotalShareholdingRatio(investorEnterpriseId, investeeEnterpriseId);
        } catch (Exception e) {
            log.error("计算综合持股比例失败，investorEnterpriseId: {}, investeeEnterpriseId: {}", investorEnterpriseId, investeeEnterpriseId, e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<EquityStructure> getPledgedEquity(String investeeEnterpriseId) {
        try {
            return equityStructureMapper.selectPledgedEquity(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("查询质押股权失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getEquityDistributionByInvestorType(String investeeEnterpriseId) {
        try {
            return equityStructureMapper.selectEquityDistributionByInvestorType(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("按投资方类型统计股权分布失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getEquityDistributionByNature(String investeeEnterpriseId) {
        try {
            return equityStructureMapper.selectEquityDistributionByNature(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("按股权性质统计股权分布失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getEquityDistributionByLevel(String investeeEnterpriseId) {
        try {
            return equityStructureMapper.selectEquityDistributionByLevel(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("按投资层级统计股权分布失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getEquityConcentration(String investeeEnterpriseId) {
        try {
            return equityStructureMapper.selectEquityConcentration(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("查询股权集中度失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> getEquityChangeTrend(String investeeEnterpriseId, String startDate, String endDate) {
        try {
            return equityStructureMapper.selectEquityChangeTrend(investeeEnterpriseId, startDate, endDate);
        } catch (Exception e) {
            log.error("查询股权变动趋势失败，investeeEnterpriseId: {}, startDate: {}, endDate: {}", investeeEnterpriseId, startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<EquityStructure> getEquityRiskWarning(String riskLevel) {
        try {
            return equityStructureMapper.selectEquityRiskWarning(riskLevel);
        } catch (Exception e) {
            log.error("查询股权风险预警失败，riskLevel: {}", riskLevel, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateEquityStatus(List<String> equityIds, String equityStatus) {
        try {
            int result = equityStructureMapper.batchUpdateEquityStatus(equityIds, equityStatus);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新股权状态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateIndirectShareholdingRatio(List<EquityStructure> equityStructures) {
        try {
            int result = equityStructureMapper.batchUpdateIndirectShareholdingRatio(equityStructures);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新间接持股比例失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteExpiredEquityRecords(Integer days) {
        try {
            if (days == null || days <= 0) {
                days = 365; // 默认删除365天前的过期记录
            }
            return equityStructureMapper.deleteExpiredEquityRecords(days);
        } catch (Exception e) {
            log.error("删除过期股权记录失败，days: {}", days, e);
            return 0;
        }
    }

    @Override
    public Map<String, Object> getEquityStatisticsOverview() {
        try {
            return equityStructureMapper.selectEquityStatisticsOverview();
        } catch (Exception e) {
            log.error("获取股权统计概览失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> getInvestorTypeDistribution() {
        try {
            return equityStructureMapper.selectInvestorTypeDistribution();
        } catch (Exception e) {
            log.error("获取投资方类型分布失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getEquityNatureDistribution() {
        try {
            return equityStructureMapper.selectEquityNatureDistribution();
        } catch (Exception e) {
            log.error("获取股权性质分布失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getInvestmentLevelDistribution() {
        try {
            return equityStructureMapper.selectInvestmentLevelDistribution();
        } catch (Exception e) {
            log.error("获取投资层级分布失败", e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getControllingStatistics() {
        try {
            return equityStructureMapper.selectControllingStatistics();
        } catch (Exception e) {
            log.error("获取控股情况统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getPledgeStatistics() {
        try {
            return equityStructureMapper.selectPledgeStatistics();
        } catch (Exception e) {
            log.error("获取质押情况统计失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> exportEquityStructureList(EquityStructureQueryVO queryVO) {
        try {
            return equityStructureMapper.exportEquityStructureList(queryVO);
        } catch (Exception e) {
            log.error("导出股权结构列表失败", e);
            return null;
        }
    }

    @Override
    public Map<String, Object> getEquityStructureGraph(String enterpriseId, String direction, Integer maxLevel) {
        try {
            Map<String, Object> result = new HashMap<>();

            if (maxLevel == null || maxLevel <= 0) {
                maxLevel = 10;
            }

            if ("up".equals(direction)) {
                List<EquityStructure> upData = this.getEquityPenetrationUp(enterpriseId, maxLevel);
                result.put("upData", upData);
            } else if ("down".equals(direction)) {
                List<EquityStructure> downData = this.getEquityPenetrationDown(enterpriseId, maxLevel);
                result.put("downData", downData);
            } else {
                List<EquityStructure> upData = this.getEquityPenetrationUp(enterpriseId, maxLevel);
                List<EquityStructure> downData = this.getEquityPenetrationDown(enterpriseId, maxLevel);
                result.put("upData", upData);
                result.put("downData", downData);
            }

            return result;
        } catch (Exception e) {
            log.error("获取股权结构图谱数据失败，enterpriseId: {}, direction: {}, maxLevel: {}", enterpriseId, direction, maxLevel, e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getEquityPenetrationAnalysisReport(String investeeEnterpriseId) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 基础股权结构
            List<EquityStructure> equityStructures = this.getEquityStructureByInvesteeEnterpriseId(investeeEnterpriseId);
            result.put("equityStructures", equityStructures);

            // 控股股权
            List<EquityStructure> controllingEquity = this.getControllingEquity(investeeEnterpriseId);
            result.put("controllingEquity", controllingEquity);

            // 实际控制人股权
            List<EquityStructure> actualControllerEquity = this.getActualControllerEquity(investeeEnterpriseId);
            result.put("actualControllerEquity", actualControllerEquity);

            // 股权集中度
            Map<String, Object> concentration = this.getEquityConcentration(investeeEnterpriseId);
            result.put("concentration", concentration);

            // 投资方类型分布
            List<Map<String, Object>> typeDistribution = this.getEquityDistributionByInvestorType(investeeEnterpriseId);
            result.put("typeDistribution", typeDistribution);

            // 股权性质分布
            List<Map<String, Object>> natureDistribution = this.getEquityDistributionByNature(investeeEnterpriseId);
            result.put("natureDistribution", natureDistribution);

            return result;
        } catch (Exception e) {
            log.error("获取股权穿透分析报告失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> getEquityRiskAssessment(String investeeEnterpriseId) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 质押股权风险
            List<EquityStructure> pledgedEquity = this.getPledgedEquity(investeeEnterpriseId);
            result.put("pledgedEquity", pledgedEquity);

            // 股权集中度风险
            Map<String, Object> concentration = this.getEquityConcentration(investeeEnterpriseId);
            result.put("concentrationRisk", concentration);

            // 控制权稳定性风险
            List<EquityStructure> controllingEquity = this.getControllingEquity(investeeEnterpriseId);
            result.put("controlStabilityRisk", controllingEquity);

            // 风险等级评估
            String riskLevel = this.assessOverallRiskLevel(investeeEnterpriseId);
            result.put("overallRiskLevel", riskLevel);

            return result;
        } catch (Exception e) {
            log.error("获取股权风险评估失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return new HashMap<>();
        }
    }

    @Override
    public String getInvestorTypeLabel(String investorType) {
        if (investorType == null) return "";
        switch (investorType) {
            case EquityStructure.INVESTOR_TYPE_ENTERPRISE: return "企业";
            case EquityStructure.INVESTOR_TYPE_INDIVIDUAL: return "个人";
            case EquityStructure.INVESTOR_TYPE_GOVERNMENT: return "政府";
            case EquityStructure.INVESTOR_TYPE_INSTITUTION: return "机构";
            case EquityStructure.INVESTOR_TYPE_FUND: return "基金";
            case EquityStructure.INVESTOR_TYPE_OTHER: return "其他";
            default: return investorType;
        }
    }

    @Override
    public String getInvestmentMethodLabel(String investmentMethod) {
        if (investmentMethod == null) return "";
        switch (investmentMethod) {
            case EquityStructure.INVESTMENT_METHOD_CASH: return "现金投资";
            case EquityStructure.INVESTMENT_METHOD_ASSET: return "资产投资";
            case EquityStructure.INVESTMENT_METHOD_EQUITY: return "股权投资";
            case EquityStructure.INVESTMENT_METHOD_DEBT: return "债权投资";
            case EquityStructure.INVESTMENT_METHOD_MIXED: return "混合投资";
            default: return investmentMethod;
        }
    }

    @Override
    public String getEquityNatureLabel(String equityNature) {
        if (equityNature == null) return "";
        switch (equityNature) {
            case EquityStructure.EQUITY_NATURE_STATE_OWNED: return "国有股权";
            case EquityStructure.EQUITY_NATURE_COLLECTIVE: return "集体股权";
            case EquityStructure.EQUITY_NATURE_PRIVATE: return "民营股权";
            case EquityStructure.EQUITY_NATURE_FOREIGN: return "外资股权";
            case EquityStructure.EQUITY_NATURE_MIXED: return "混合股权";
            default: return equityNature;
        }
    }

    @Override
    public String getEquityStatusLabel(String equityStatus) {
        if (equityStatus == null) return "";
        switch (equityStatus) {
            case EquityStructure.EQUITY_STATUS_NORMAL: return "正常";
            case EquityStructure.EQUITY_STATUS_PLEDGED: return "已质押";
            case EquityStructure.EQUITY_STATUS_FROZEN: return "冻结";
            case EquityStructure.EQUITY_STATUS_TRANSFERRED: return "已转让";
            case EquityStructure.EQUITY_STATUS_CANCELLED: return "已注销";
            default: return equityStatus;
        }
    }

    @Override
    public String getEquitySourceLabel(String equitySource) {
        if (equitySource == null) return "";
        switch (equitySource) {
            case EquityStructure.EQUITY_SOURCE_INITIAL: return "初始投资";
            case EquityStructure.EQUITY_SOURCE_INCREASE: return "增资扩股";
            case EquityStructure.EQUITY_SOURCE_TRANSFER: return "股权转让";
            case EquityStructure.EQUITY_SOURCE_MERGER: return "合并重组";
            case EquityStructure.EQUITY_SOURCE_SPLIT: return "分立";
            case EquityStructure.EQUITY_SOURCE_OTHER: return "其他";
            default: return equitySource;
        }
    }

    /**
     * 计算持股比例
     */
    private void calculateShareholdingRatios(EquityStructure equityStructure) {
        try {


        } catch (Exception e) {
            log.error("计算持股比例失败", e);
        }
    }

    /**
     * 评估整体风险等级
     */
    private String assessOverallRiskLevel(String investeeEnterpriseId) {
        try {
            // 简化的风险评估逻辑
            List<EquityStructure> pledgedEquity = this.getPledgedEquity(investeeEnterpriseId);
            Map<String, Object> concentration = this.getEquityConcentration(investeeEnterpriseId);

            if (pledgedEquity != null && pledgedEquity.size() > 0) {
                return "HIGH"; // 存在质押股权，风险较高
            }

            // 根据股权集中度判断风险
            // 这里需要根据实际业务逻辑进行完善
            return "MEDIUM";
        } catch (Exception e) {
            log.error("评估整体风险等级失败", e);
            return "UNKNOWN";
        }
    }
}
