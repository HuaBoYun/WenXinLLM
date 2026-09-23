package com.huabo.cybermonitor.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.ActualController;
import com.huabo.cybermonitor.mapper.ActualControllerMapper;
import com.huabo.cybermonitor.service.IActualControllerService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.ActualControllerQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 实际控制人服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class ActualControllerServiceImpl extends ServiceImpl<ActualControllerMapper, ActualController> implements IActualControllerService {

    @Autowired
    private ActualControllerMapper actualControllerMapper;

    @Override
    public PageResult<ActualController> getActualControllerList(ActualControllerQueryVO queryVO) {
        try {
            List<ActualController> list = actualControllerMapper.selectActualControllerList(queryVO);
            
            QueryWrapper<ActualController> countWrapper = new QueryWrapper<>();
            if (queryVO.getControlledEnterpriseId() != null) {
                countWrapper.eq("CONTROLLED_ENTERPRISE_ID", queryVO.getControlledEnterpriseId());
            }
            long total = this.count(countWrapper);

            PageResult<ActualController> result = new PageResult<ActualController>((int)total, list);
            result.setPageNumber(queryVO.getPageNumber());
            result.setPageSize(queryVO.getPageSize());
            return result;
        } catch (Exception e) {
            log.error("查询实际控制人列表失败", e);
            return new PageResult<ActualController>();
        }
    }

    @Override
    public ActualController getActualControllerById(String controllerId) {
        try {
            return this.getById(controllerId);
        } catch (Exception e) {
            log.error("根据ID获取实际控制人详情失败，controllerId: {}", controllerId, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addActualController(ActualController actualController) {
        try {
            actualController.setCreateTime(LocalDateTime.now());
            actualController.setUpdateTime(LocalDateTime.now());
            actualController.setIdentificationTime(LocalDateTime.now());
            
            // 进行控制关系分析
            this.analyzeControlRelation(actualController);
            
            return this.save(actualController);
        } catch (Exception e) {
            log.error("新增实际控制人失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateActualController(ActualController actualController) {
        try {
            actualController.setUpdateTime(LocalDateTime.now());
            return this.updateById(actualController);
        } catch (Exception e) {
            log.error("更新实际控制人失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteActualController(String controllerId) {
        try {
            return this.removeById(controllerId);
        } catch (Exception e) {
            log.error("删除实际控制人失败，controllerId: {}", controllerId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteActualController(List<String> controllerIds) {
        try {
            return this.removeByIds(controllerIds);
        } catch (Exception e) {
            log.error("批量删除实际控制人失败", e);
            return false;
        }
    }

    @Override
    public List<ActualController> getActualControllerByControlledEnterpriseId(String controlledEnterpriseId) {
        try {
            return actualControllerMapper.selectByControlledEnterpriseId(controlledEnterpriseId);
        } catch (Exception e) {
            log.error("根据被控制企业ID查询实际控制人失败，controlledEnterpriseId: {}", controlledEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getActualControllerByControllerEnterpriseId(String controllerEnterpriseId) {
        try {
            return actualControllerMapper.selectByControllerEnterpriseId(controllerEnterpriseId);
        } catch (Exception e) {
            log.error("根据控制人企业ID查询控制关系失败，controllerEnterpriseId: {}", controllerEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getUltimateControllers(String controlledEnterpriseId) {
        try {
            return actualControllerMapper.selectUltimateControllers(controlledEnterpriseId);
        } catch (Exception e) {
            log.error("查询最终控制人失败，controlledEnterpriseId: {}", controlledEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getConcertedActionControllers(String controlledEnterpriseId) {
        try {
            return actualControllerMapper.selectConcertedActionControllers(controlledEnterpriseId);
        } catch (Exception e) {
            log.error("查询一致行动人失败，controlledEnterpriseId: {}", controlledEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getActualControllerByControllerType(String controllerType) {
        try {
            return actualControllerMapper.selectByControllerType(controllerType);
        } catch (Exception e) {
            log.error("按控制人类型查询失败，controllerType: {}", controllerType, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getActualControllerByControllerNature(String controllerNature) {
        try {
            return actualControllerMapper.selectByControllerNature(controllerNature);
        } catch (Exception e) {
            log.error("按控制人性质查询失败，controllerNature: {}", controllerNature, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getActualControllerByControlMethod(String controlMethod) {
        try {
            return actualControllerMapper.selectByControlMethod(controlMethod);
        } catch (Exception e) {
            log.error("按控制方式查询失败，controlMethod: {}", controlMethod, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getActualControllerByControlStatus(String controlStatus) {
        try {
            return actualControllerMapper.selectByControlStatus(controlStatus);
        } catch (Exception e) {
            log.error("按控制状态查询失败，controlStatus: {}", controlStatus, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getActualControllerByControlRiskLevel(String controlRiskLevel) {
        try {
            return actualControllerMapper.selectByControlRiskLevel(controlRiskLevel);
        } catch (Exception e) {
            log.error("按控制风险等级查询失败，controlRiskLevel: {}", controlRiskLevel, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getSpecialSupervisionControllers(Boolean needSpecialSupervision) {
        try {
            return actualControllerMapper.selectSpecialSupervisionControllers(needSpecialSupervision);
        } catch (Exception e) {
            log.error("查询需要特别监管的控制关系失败，needSpecialSupervision: {}", needSpecialSupervision, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getActualControllerByRegulatoryAttention(String regulatoryAttention) {
        try {
            return actualControllerMapper.selectByRegulatoryAttention(regulatoryAttention);
        } catch (Exception e) {
            log.error("按监管关注度查询失败，regulatoryAttention: {}", regulatoryAttention, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getActualControllerByConfirmationStatus(String confirmationStatus) {
        try {
            return actualControllerMapper.selectByConfirmationStatus(confirmationStatus);
        } catch (Exception e) {
            log.error("按确认状态查询失败，confirmationStatus: {}", confirmationStatus, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getControlChain(String startEnterpriseId, String endEnterpriseId) {
        try {
            return actualControllerMapper.selectControlChain(startEnterpriseId, endEnterpriseId);
        } catch (Exception e) {
            log.error("查询控制链路失败，startEnterpriseId: {}, endEnterpriseId: {}", startEnterpriseId, endEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<ActualController> getControlHierarchy(String controllerEnterpriseId, Integer maxLevel) {
        try {
            if (maxLevel == null || maxLevel <= 0) {
                maxLevel = 10;
            }
            return actualControllerMapper.selectControlHierarchy(controllerEnterpriseId, maxLevel);
        } catch (Exception e) {
            log.error("查询控制层级结构失败，controllerEnterpriseId: {}, maxLevel: {}", controllerEnterpriseId, maxLevel, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getControllerTypeStatistics() {
        try {
            return actualControllerMapper.selectControllerTypeStatistics();
        } catch (Exception e) {
            log.error("按控制人类型统计失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getControllerNatureStatistics() {
        try {
            return actualControllerMapper.selectControllerNatureStatistics();
        } catch (Exception e) {
            log.error("按控制人性质统计失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getControlMethodStatistics() {
        try {
            return actualControllerMapper.selectControlMethodStatistics();
        } catch (Exception e) {
            log.error("按控制方式统计失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getControlStatusStatistics() {
        try {
            return actualControllerMapper.selectControlStatusStatistics();
        } catch (Exception e) {
            log.error("按控制状态统计失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getControlStabilityStatistics() {
        try {
            return actualControllerMapper.selectControlStabilityStatistics();
        } catch (Exception e) {
            log.error("按控制稳定性统计失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getControlRiskLevelStatistics() {
        try {
            return actualControllerMapper.selectControlRiskLevelStatistics();
        } catch (Exception e) {
            log.error("按控制风险等级统计失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getRegulatoryAttentionStatistics() {
        try {
            return actualControllerMapper.selectRegulatoryAttentionStatistics();
        } catch (Exception e) {
            log.error("按监管关注度统计失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getControlRelationTrend(String startDate, String endDate) {
        try {
            return actualControllerMapper.selectControlRelationTrend(startDate, endDate);
        } catch (Exception e) {
            log.error("查询控制关系变化趋势失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getControlConcentrationAnalysis() {
        try {
            return actualControllerMapper.selectControlConcentrationAnalysis();
        } catch (Exception e) {
            log.error("查询控制集中度分析失败", e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateControlStatus(List<String> controllerIds, String controlStatus) {
        try {
            int result = actualControllerMapper.batchUpdateControlStatus(controllerIds, controlStatus);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新控制状态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateConfirmationStatus(List<String> controllerIds, String confirmationStatus, String confirmedBy) {
        try {
            int result = actualControllerMapper.batchUpdateConfirmationStatus(controllerIds, confirmationStatus, confirmedBy);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新确认状态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateRegulatoryAttention(List<String> controllerIds, String regulatoryAttention) {
        try {
            int result = actualControllerMapper.batchUpdateRegulatoryAttention(controllerIds, regulatoryAttention);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新监管关注度失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteExpiredControlRelations(Integer days) {
        try {
            if (days == null || days <= 0) {
                days = 365;
            }
            return actualControllerMapper.deleteExpiredControlRelations(days);
        } catch (Exception e) {
            log.error("删除过期控制关系失败，days: {}", days, e);
            return 0;
        }
    }

    @Override
    public Map<String, Object> getControlStatisticsOverview() {
        try {
            return actualControllerMapper.selectControlStatisticsOverview();
        } catch (Exception e) {
            log.error("获取控制关系统计概览失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> exportActualControllerList(ActualControllerQueryVO queryVO) {
        try {
            return actualControllerMapper.exportActualControllerList(queryVO);
        } catch (Exception e) {
            log.error("导出实际控制人列表失败", e);
            return null;
        }
    }

    /**
     * 分析控制关系
     */
    private void analyzeControlRelation(ActualController controller) {
        try {
            // 分析控制稳定性
            String stability = this.analyzeControlStabilityInternal(controller);
            controller.setControlStability(stability);
            
            // 评估控制风险
            String riskLevel = this.assessControlRiskInternal(controller);
            controller.setControlRiskLevel(riskLevel);
            
            // 确定监管关注度
            String attention = this.determineRegulatoryAttention(controller);
            controller.setRegulatoryAttention(attention);
            
        } catch (Exception e) {
            log.error("分析控制关系失败", e);
        }
    }

    /**
     * 分析控制稳定性
     */
    private String analyzeControlStabilityInternal(ActualController controller) {
        try {
            // 简化的稳定性分析逻辑
            if (controller.getTotalShareholdingRatio() != null) {
                BigDecimal ratio = controller.getTotalShareholdingRatio();
                if (ratio.compareTo(new BigDecimal("50")) >= 0) {
                    return ActualController.CONTROL_STABILITY_STABLE;
                } else if (ratio.compareTo(new BigDecimal("30")) >= 0) {
                    return ActualController.CONTROL_STABILITY_UNSTABLE;
                } else {
                    return ActualController.CONTROL_STABILITY_VOLATILE;
                }
            }
            return ActualController.CONTROL_STABILITY_UNCERTAIN;
        } catch (Exception e) {
            log.error("分析控制稳定性失败", e);
            return ActualController.CONTROL_STABILITY_UNCERTAIN;
        }
    }

    /**
     * 评估控制风险
     */
    private String assessControlRiskInternal(ActualController controller) {
        try {
            // 简化的风险评估逻辑
            if (ActualController.CONTROL_STABILITY_VOLATILE.equals(controller.getControlStability())) {
                return ActualController.CONTROL_RISK_LEVEL_HIGH;
            } else if (ActualController.CONTROL_STABILITY_UNSTABLE.equals(controller.getControlStability())) {
                return ActualController.CONTROL_RISK_LEVEL_MEDIUM;
            } else {
                return ActualController.CONTROL_RISK_LEVEL_LOW;
            }
        } catch (Exception e) {
            log.error("评估控制风险失败", e);
            return ActualController.CONTROL_RISK_LEVEL_MEDIUM;
        }
    }

    @Override
    public List<ActualController> identifyActualControllers(String controlledEnterpriseId) {
        try {
            // 实际控制人识别算法
            List<ActualController> controllers = actualControllerMapper.selectByControlledEnterpriseId(controlledEnterpriseId);

            // 对控制人进行分析和排序
            for (ActualController controller : controllers) {
                this.analyzeControlRelation(controller);
            }

            return controllers;
        } catch (Exception e) {
            log.error("识别实际控制人失败，controlledEnterpriseId: {}", controlledEnterpriseId, e);
            return null;
        }
    }

    @Override
    public Map<String, Object> analyzeControlStability(String controllerId) {
        try {
            ActualController controller = this.getById(controllerId);
            if (controller == null) {
                return new HashMap<>();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("controller", controller);
            result.put("stability", controller.getControlStability());

            // 详细稳定性分析
            Map<String, Object> analysis = new HashMap<>();
            analysis.put("shareholdingRatio", controller.getTotalShareholdingRatio());
            analysis.put("votingRightRatio", controller.getVotingRightRatio());
            analysis.put("controlLevel", controller.getControlLevel());
            analysis.put("isConcertedAction", controller.getIsConcertedAction());

            result.put("stabilityAnalysis", analysis);
            return result;
        } catch (Exception e) {
            log.error("控制稳定性分析失败，controllerId: {}", controllerId, e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> assessControlRisk(String controllerId) {
        try {
            ActualController controller = this.getById(controllerId);
            if (controller == null) {
                return new HashMap<>();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("controller", controller);
            result.put("riskLevel", controller.getControlRiskLevel());

            // 详细风险评估
            Map<String, Object> assessment = new HashMap<>();
            assessment.put("controlStability", controller.getControlStability());
            assessment.put("regulatoryAttention", controller.getRegulatoryAttention());
            assessment.put("needSpecialSupervision", controller.getNeedSpecialSupervision());

            result.put("riskAssessment", assessment);
            return result;
        } catch (Exception e) {
            log.error("控制风险评估失败，controllerId: {}", controllerId, e);
            return new HashMap<>();
        }
    }

    @Override
    public String getControllerTypeLabel(String controllerType) {
        if (controllerType == null) return "";
        switch (controllerType) {
            case ActualController.CONTROLLER_TYPE_INDIVIDUAL: return "个人";
            case ActualController.CONTROLLER_TYPE_ENTERPRISE: return "企业";
            case ActualController.CONTROLLER_TYPE_GOVERNMENT: return "政府";
            case ActualController.CONTROLLER_TYPE_INSTITUTION: return "机构";
            case ActualController.CONTROLLER_TYPE_FUND: return "基金";
            case ActualController.CONTROLLER_TYPE_TRUST: return "信托";
            case ActualController.CONTROLLER_TYPE_OTHER: return "其他";
            default: return controllerType;
        }
    }

    @Override
    public String getControllerNatureLabel(String controllerNature) {
        if (controllerNature == null) return "";
        switch (controllerNature) {
            case ActualController.CONTROLLER_NATURE_STATE_OWNED: return "国有";
            case ActualController.CONTROLLER_NATURE_COLLECTIVE: return "集体";
            case ActualController.CONTROLLER_NATURE_PRIVATE: return "民营";
            case ActualController.CONTROLLER_NATURE_FOREIGN: return "外资";
            case ActualController.CONTROLLER_NATURE_MIXED: return "混合";
            default: return controllerNature;
        }
    }

    @Override
    public String getControlMethodLabel(String controlMethod) {
        if (controlMethod == null) return "";
        switch (controlMethod) {
            case ActualController.CONTROL_METHOD_SHAREHOLDING: return "股权控制";
            case ActualController.CONTROL_METHOD_VOTING_RIGHT: return "表决权控制";
            case ActualController.CONTROL_METHOD_BOARD_CONTROL: return "董事会控制";
            case ActualController.CONTROL_METHOD_MANAGEMENT: return "经营管理控制";
            case ActualController.CONTROL_METHOD_AGREEMENT: return "协议控制";
            case ActualController.CONTROL_METHOD_TRUST: return "信托控制";
            case ActualController.CONTROL_METHOD_OTHER: return "其他控制";
            default: return controlMethod;
        }
    }

    @Override
    public String getControlStatusLabel(String controlStatus) {
        if (controlStatus == null) return "";
        switch (controlStatus) {
            case ActualController.CONTROL_STATUS_ACTIVE: return "有效";
            case ActualController.CONTROL_STATUS_INACTIVE: return "失效";
            case ActualController.CONTROL_STATUS_SUSPENDED: return "暂停";
            case ActualController.CONTROL_STATUS_TERMINATED: return "终止";
            case ActualController.CONTROL_STATUS_DISPUTED: return "争议";
            default: return controlStatus;
        }
    }

    @Override
    public String getControlStabilityLabel(String controlStability) {
        if (controlStability == null) return "";
        switch (controlStability) {
            case ActualController.CONTROL_STABILITY_STABLE: return "稳定";
            case ActualController.CONTROL_STABILITY_UNSTABLE: return "不稳定";
            case ActualController.CONTROL_STABILITY_VOLATILE: return "易变";
            case ActualController.CONTROL_STABILITY_UNCERTAIN: return "不确定";
            default: return controlStability;
        }
    }

    @Override
    public String getControlRiskLevelLabel(String controlRiskLevel) {
        if (controlRiskLevel == null) return "";
        switch (controlRiskLevel) {
            case ActualController.CONTROL_RISK_LEVEL_LOW: return "低风险";
            case ActualController.CONTROL_RISK_LEVEL_MEDIUM: return "中风险";
            case ActualController.CONTROL_RISK_LEVEL_HIGH: return "高风险";
            case ActualController.CONTROL_RISK_LEVEL_CRITICAL: return "严重风险";
            default: return controlRiskLevel;
        }
    }

    @Override
    public String getRegulatoryAttentionLabel(String regulatoryAttention) {
        if (regulatoryAttention == null) return "";
        switch (regulatoryAttention) {
            case ActualController.REGULATORY_ATTENTION_LOW: return "低关注";
            case ActualController.REGULATORY_ATTENTION_MEDIUM: return "中关注";
            case ActualController.REGULATORY_ATTENTION_HIGH: return "高关注";
            case ActualController.REGULATORY_ATTENTION_CRITICAL: return "重点关注";
            default: return regulatoryAttention;
        }
    }

    @Override
    public String getConfirmationStatusLabel(String confirmationStatus) {
        if (confirmationStatus == null) return "";
        switch (confirmationStatus) {
            case ActualController.CONFIRMATION_STATUS_PENDING: return "待确认";
            case ActualController.CONFIRMATION_STATUS_CONFIRMED: return "已确认";
            case ActualController.CONFIRMATION_STATUS_REJECTED: return "已拒绝";
            case ActualController.CONFIRMATION_STATUS_DISPUTED: return "存在争议";
            default: return confirmationStatus;
        }
    }

    /**
     * 确定监管关注度
     */
    private String determineRegulatoryAttention(ActualController controller) {
        try {
            // 简化的监管关注度确定逻辑
            if (ActualController.CONTROL_RISK_LEVEL_HIGH.equals(controller.getControlRiskLevel()) ||
                ActualController.CONTROL_RISK_LEVEL_CRITICAL.equals(controller.getControlRiskLevel())) {
                return ActualController.REGULATORY_ATTENTION_HIGH;
            } else if (ActualController.CONTROL_RISK_LEVEL_MEDIUM.equals(controller.getControlRiskLevel())) {
                return ActualController.REGULATORY_ATTENTION_MEDIUM;
            } else {
                return ActualController.REGULATORY_ATTENTION_LOW;
            }
        } catch (Exception e) {
            log.error("确定监管关注度失败", e);
            return ActualController.REGULATORY_ATTENTION_MEDIUM;
        }
    }
}
