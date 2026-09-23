package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.RiskAssessment;
import com.management.accountant.oracle.mapper.advanced.RiskAssessmentMapper;
import com.management.accountant.oracle.service.advanced.RiskAssessmentService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("riskAssessmentServiceOracle")
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    @Resource
    private RiskAssessmentMapper assessmentMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<RiskAssessment> selectList(Map<String, Object> params) {
        return assessmentMapper.selectList(buildWrapper(params));
    }

    @Override
    public Page<RiskAssessment> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        return assessmentMapper.selectPage(new Page<>(pageNum, pageSize), buildWrapper(params));
    }

    @Override
    public RiskAssessment selectById(String riskId) {
        return assessmentMapper.selectById(riskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(RiskAssessment assessment) {
        assessment.setRiskId("RA" + idWorker.nextId());
        assessment.setCreateTime(new Date());
        if (assessment.getAssessmentDate() == null) {
            assessment.setAssessmentDate(new Date());
        }
        return assessmentMapper.insert(assessment) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(RiskAssessment assessment) {
        assessment.setUpdateTime(new Date());
        return assessmentMapper.updateById(assessment) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        // 表无 DEL_FLAG，直接物理删除
        return assessmentMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyAssessment(String id) {
        RiskAssessment src = assessmentMapper.selectById(id);
        if (src == null) return false;
        RiskAssessment copy = new RiskAssessment();
        copy.setRiskId("RA" + idWorker.nextId());
        copy.setEnterpriseId(src.getEnterpriseId());
        copy.setRiskCategory(src.getRiskCategory());
        copy.setRiskItem(src.getRiskItem() + " - 副本");
        copy.setRiskLevel(src.getRiskLevel());
        copy.setRiskDescription(src.getRiskDescription());
        copy.setImpactDegree(src.getImpactDegree());
        copy.setOccurrenceProbability(src.getOccurrenceProbability());
        copy.setCountermeasures(src.getCountermeasures());
        copy.setStatus("PENDING");
        copy.setCreateTime(new Date());
        return assessmentMapper.insert(copy) > 0;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        // 总数
        stats.put("totalAssessments", assessmentMapper.selectCount(new QueryWrapper<>()));
        // 高风险
        stats.put("highRiskCount", assessmentMapper.selectCount(
                new QueryWrapper<RiskAssessment>().eq("RISK_LEVEL", "HIGH")));
        // 中风险
        stats.put("mediumRiskCount", assessmentMapper.selectCount(
                new QueryWrapper<RiskAssessment>().eq("RISK_LEVEL", "MEDIUM")));
        // 低风险
        stats.put("lowRiskCount", assessmentMapper.selectCount(
                new QueryWrapper<RiskAssessment>().eq("RISK_LEVEL", "LOW")));
        return stats;
    }

    private QueryWrapper<RiskAssessment> buildWrapper(Map<String, Object> params) {
        QueryWrapper<RiskAssessment> w = new QueryWrapper<>();
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) {
                w.and(q -> q.like("RISK_ITEM", keyword).or().like("RISK_CATEGORY", keyword));
            }
            String riskLevel = (String) params.get("riskLevel");
            if (StringUtils.hasText(riskLevel)) w.eq("RISK_LEVEL", riskLevel);
            String riskCategory = (String) params.get("riskCategory");
            if (StringUtils.hasText(riskCategory)) w.eq("RISK_CATEGORY", riskCategory);
        }
        w.orderByDesc("CREATE_TIME");
        return w;
    }
}
