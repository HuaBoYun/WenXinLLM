package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.RiskAssessmentDetail;
import com.huabo.contract.mapper.RiskAssessmentDetailMapper;
import com.huabo.contract.service.RiskAssessmentDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 风险评估明细表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class RiskAssessmentDetailServiceImpl extends ServiceImpl<RiskAssessmentDetailMapper, RiskAssessmentDetail> implements RiskAssessmentDetailService {

    @Autowired
    private RiskAssessmentDetailMapper riskAssessmentDetailMapper;

    @Override
    public List<RiskAssessmentDetail> getDetailsByAssessmentId(Long assessmentId) {
        try {
            return riskAssessmentDetailMapper.selectByAssessmentId(assessmentId);
        } catch (Exception e) {
            log.error("根据评估ID获取明细列表失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("根据评估ID获取明细列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAssessmentDetails(Long assessmentId, List<RiskAssessmentDetail> detailList) {
        try {
            // 先删除原有明细
            riskAssessmentDetailMapper.deleteByAssessmentId(assessmentId);
            
            if (detailList != null && !detailList.isEmpty()) {
                // 设置评估ID和创建时间
                Date now = new Date();
                for (RiskAssessmentDetail detail : detailList) {
                    detail.setAssessmentId(assessmentId);
                    detail.setCreateTime(now);
                    
                    // 自动计算风险得分
                    if (detail.getRiskScore() == null) {
                        detail.setRiskScore(detail.calculateRiskScore());
                    }
                }
                
                // 批量插入
                return riskAssessmentDetailMapper.batchInsert(detailList) > 0;
            }
            
            return true;
        } catch (Exception e) {
            log.error("保存评估明细失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("保存评估明细失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDetailsByAssessmentId(Long assessmentId) {
        try {
            return riskAssessmentDetailMapper.deleteByAssessmentId(assessmentId) >= 0;
        } catch (Exception e) {
            log.error("删除评估明细失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("删除评估明细失败：" + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateTotalScore(Long assessmentId) {
        try {
            BigDecimal totalScore = riskAssessmentDetailMapper.calculateTotalScore(assessmentId);
            return totalScore != null ? totalScore : BigDecimal.ZERO;
        } catch (Exception e) {
            log.error("计算评估总分失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("计算评估总分失败：" + e.getMessage());
        }
    }

    @Override
    public Integer getMaxRiskLevel(Long assessmentId) {
        try {
            Integer maxRiskLevel = riskAssessmentDetailMapper.getMaxRiskLevel(assessmentId);
            return maxRiskLevel != null ? maxRiskLevel : 1;
        } catch (Exception e) {
            log.error("获取最高风险等级失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("获取最高风险等级失败：" + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessmentDetail> getDetailsByRiskCategory(Long assessmentId, Integer riskCategory) {
        try {
            return riskAssessmentDetailMapper.selectByRiskCategory(assessmentId, riskCategory);
        } catch (Exception e) {
            log.error("根据风险类别获取明细失败，评估ID：{}，风险类别：{}", assessmentId, riskCategory, e);
            throw new RuntimeException("根据风险类别获取明细失败：" + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessmentDetail> getHighRiskItems(Long assessmentId, BigDecimal minRiskScore) {
        try {
            if (minRiskScore == null) {
                minRiskScore = new BigDecimal("75"); // 默认75分以上为高风险
            }
            return riskAssessmentDetailMapper.selectHighRiskItems(assessmentId, minRiskScore);
        } catch (Exception e) {
            log.error("获取高风险项目明细失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("获取高风险项目明细失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRiskCategoryDistribution(Long assessmentId) {
        try {
            return riskAssessmentDetailMapper.selectRiskCategoryDistribution(assessmentId);
        } catch (Exception e) {
            log.error("获取风险类别分布失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("获取风险类别分布失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRiskImpactDistribution(Long assessmentId) {
        try {
            return riskAssessmentDetailMapper.selectRiskImpactDistribution(assessmentId);
        } catch (Exception e) {
            log.error("获取风险影响分布失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("获取风险影响分布失败：" + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessmentDetail> getItemsNeedMitigation(Long assessmentId) {
        try {
            return riskAssessmentDetailMapper.selectItemsNeedMitigation(assessmentId);
        } catch (Exception e) {
            log.error("获取需要缓解措施的风险项目失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("获取需要缓解措施的风险项目失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRiskScore(Long id, BigDecimal riskScore) {
        try {
            return riskAssessmentDetailMapper.updateRiskScore(id, riskScore) > 0;
        } catch (Exception e) {
            log.error("更新风险得分失败，ID：{}，得分：{}", id, riskScore, e);
            throw new RuntimeException("更新风险得分失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateRiskScore(Long assessmentId) {
        try {
            return riskAssessmentDetailMapper.batchUpdateRiskScore(assessmentId) >= 0;
        } catch (Exception e) {
            log.error("批量更新风险得分失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("批量更新风险得分失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean autoCalculateRiskScores(Long assessmentId) {
        try {
            List<RiskAssessmentDetail> detailList = getDetailsByAssessmentId(assessmentId);
            
            for (RiskAssessmentDetail detail : detailList) {
                BigDecimal calculatedScore = detail.calculateRiskScore();
                if (!calculatedScore.equals(detail.getRiskScore())) {
                    updateRiskScore(detail.getId(), calculatedScore);
                }
            }
            
            return true;
        } catch (Exception e) {
            log.error("自动计算风险得分失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("自动计算风险得分失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> validateAssessmentDetails(List<RiskAssessmentDetail> detailList) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        try {
            if (detailList == null || detailList.isEmpty()) {
                errors.add("评估明细不能为空");
            } else {
                for (int i = 0; i < detailList.size(); i++) {
                    RiskAssessmentDetail detail = detailList.get(i);
                    String prefix = "第" + (i + 1) + "项：";
                    
                    if (detail.getRiskCategory() == null) {
                        errors.add(prefix + "风险类别不能为空");
                    }
                    
                    if (detail.getRiskItem() == null || detail.getRiskItem().trim().isEmpty()) {
                        errors.add(prefix + "风险项目不能为空");
                    }
                    
                    if (detail.getRiskProbability() == null || 
                        detail.getRiskProbability().compareTo(BigDecimal.ZERO) < 0 || 
                        detail.getRiskProbability().compareTo(BigDecimal.ONE) > 0) {
                        errors.add(prefix + "风险概率必须在0-1之间");
                    }
                    
                    if (detail.getRiskImpact() == null || 
                        detail.getRiskImpact() < 1 || 
                        detail.getRiskImpact() > 4) {
                        errors.add(prefix + "风险影响必须在1-4之间");
                    }
                }
            }
            
            result.put("valid", errors.isEmpty());
            result.put("errors", errors);
            
            return result;
        } catch (Exception e) {
            log.error("验证评估明细数据失败", e);
            result.put("valid", false);
            result.put("errors", Arrays.asList("验证数据时发生错误：" + e.getMessage()));
            return result;
        }
    }

    @Override
    public Map<String, Object> generateAssessmentReport(Long assessmentId) {
        try {
            Map<String, Object> report = new HashMap<>();
            
            // 基本信息
            List<RiskAssessmentDetail> detailList = getDetailsByAssessmentId(assessmentId);
            report.put("detailList", detailList);
            report.put("totalItems", detailList.size());
            
            // 总分和等级
            BigDecimal totalScore = calculateTotalScore(assessmentId);
            Integer maxRiskLevel = getMaxRiskLevel(assessmentId);
            report.put("totalScore", totalScore);
            report.put("maxRiskLevel", maxRiskLevel);
            
            // 高风险项目
            List<RiskAssessmentDetail> highRiskItems = getHighRiskItems(assessmentId, new BigDecimal("75"));
            report.put("highRiskItems", highRiskItems);
            report.put("highRiskCount", highRiskItems.size());
            
            // 风险类别分布
            List<Map<String, Object>> categoryDistribution = getRiskCategoryDistribution(assessmentId);
            report.put("categoryDistribution", categoryDistribution);
            
            // 风险影响分布
            List<Map<String, Object>> impactDistribution = getRiskImpactDistribution(assessmentId);
            report.put("impactDistribution", impactDistribution);
            
            // 需要缓解措施的项目
            List<RiskAssessmentDetail> itemsNeedMitigation = getItemsNeedMitigation(assessmentId);
            report.put("itemsNeedMitigation", itemsNeedMitigation);
            report.put("mitigationCount", itemsNeedMitigation.size());
            
            return report;
        } catch (Exception e) {
            log.error("生成风险评估报告失败，评估ID：{}", assessmentId, e);
            throw new RuntimeException("生成风险评估报告失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyAssessmentDetails(Long sourceAssessmentId, Long targetAssessmentId) {
        try {
            List<RiskAssessmentDetail> sourceDetails = getDetailsByAssessmentId(sourceAssessmentId);
            
            if (sourceDetails != null && !sourceDetails.isEmpty()) {
                // 复制明细，重置ID和评估ID
                List<RiskAssessmentDetail> targetDetails = new ArrayList<>();
                for (RiskAssessmentDetail sourceDetail : sourceDetails) {
                    RiskAssessmentDetail targetDetail = new RiskAssessmentDetail();
                    targetDetail.setAssessmentId(targetAssessmentId);
                    targetDetail.setRiskCategory(sourceDetail.getRiskCategory());
                    targetDetail.setRiskItem(sourceDetail.getRiskItem());
                    targetDetail.setRiskDescription(sourceDetail.getRiskDescription());
                    targetDetail.setRiskProbability(sourceDetail.getRiskProbability());
                    targetDetail.setRiskImpact(sourceDetail.getRiskImpact());
                    targetDetail.setRiskScore(sourceDetail.getRiskScore());
                    targetDetail.setMitigationMeasures(sourceDetail.getMitigationMeasures());
                    targetDetail.setResponsiblePerson(sourceDetail.getResponsiblePerson());
                    targetDetail.setCreateTime(new Date());
                    
                    targetDetails.add(targetDetail);
                }
                
                return saveAssessmentDetails(targetAssessmentId, targetDetails);
            }
            
            return true;
        } catch (Exception e) {
            log.error("复制评估明细失败，源评估ID：{}，目标评估ID：{}", sourceAssessmentId, targetAssessmentId, e);
            throw new RuntimeException("复制评估明细失败：" + e.getMessage());
        }
    }
}
