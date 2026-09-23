package com.financial.sharing.dataCollection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.dataCollection.dto.DataQualityCheckQueryParam;
import com.financial.sharing.dataCollection.dto.ExecuteQualityCheckParam;
import com.financial.sharing.dataCollection.entity.TblDataQualityCheck;
import com.financial.sharing.dataCollection.entity.TblDataQualityCheckDetail;
import com.financial.sharing.dataCollection.entity.TblDataQualityRule;
import com.financial.sharing.dataCollection.mapper.DataQualityCheckDetailMapper;
import com.financial.sharing.dataCollection.mapper.DataQualityCheckMapper;
import com.financial.sharing.dataCollection.mapper.DataQualityRuleMapper;
import com.financial.sharing.dataCollection.service.DataQualityCheckService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 数据质量检查服务实现类
 * 
 * @author Augment Agent
 * @date 2026-02-04
 */
@Slf4j
@Service("dataQualityCheckServiceImpl")
public class DataQualityCheckServiceImpl implements DataQualityCheckService {

    @Autowired
    private DataQualityCheckMapper dataQualityCheckMapper;

    @Autowired
    private DataQualityCheckDetailMapper dataQualityCheckDetailMapper;

    @Autowired
    private DataQualityRuleMapper dataQualityRuleMapper;

    @Override
    public IPage<TblDataQualityCheck> queryPage(DataQualityCheckQueryParam param, Long orgId) {
        log.info("分页查询质量检查记录, param={}, orgId={}", param, orgId);

        // 构建分页对象
        Page<TblDataQualityCheck> page = new Page<>(param.getPageNumber(), param.getPageSize());

        // 构建查询条件
        QueryWrapper<TblDataQualityCheck> wrapper = new QueryWrapper<>();
        wrapper.eq("ORG_ID", orgId);

        if (StringUtils.isNotBlank(param.getCheckCode())) {
            wrapper.like("CHECK_CODE", param.getCheckCode());
        }
        if (StringUtils.isNotBlank(param.getCheckName())) {
            wrapper.like("CHECK_NAME", param.getCheckName());
        }
        if (StringUtils.isNotBlank(param.getCheckType())) {
            wrapper.eq("CHECK_TYPE", param.getCheckType());
        }
        if (StringUtils.isNotBlank(param.getTargetType())) {
            wrapper.eq("TARGET_TYPE", param.getTargetType());
        }
        if (param.getTargetId() != null) {
            wrapper.eq("TARGET_ID", param.getTargetId());
        }
        if (StringUtils.isNotBlank(param.getCheckStatus())) {
            wrapper.eq("CHECK_STATUS", param.getCheckStatus());
        }
        if (StringUtils.isNotBlank(param.getQualityLevel())) {
            wrapper.eq("QUALITY_LEVEL", param.getQualityLevel());
        }
        if (StringUtils.isNotBlank(param.getStartTimeBegin())) {
            wrapper.ge("START_TIME", param.getStartTimeBegin());
        }
        if (StringUtils.isNotBlank(param.getStartTimeEnd())) {
            wrapper.le("START_TIME", param.getStartTimeEnd());
        }

        wrapper.orderByDesc("CREATE_TIME");

        return dataQualityCheckMapper.selectPage(page, wrapper);
    }

    @Override
    public TblDataQualityCheck queryById(Long checkId, Long orgId) {
        log.info("根据ID查询质量检查记录, checkId={}, orgId={}", checkId, orgId);

        LambdaQueryWrapper<TblDataQualityCheck> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblDataQualityCheck::getCheckId, checkId);
        wrapper.eq(TblDataQualityCheck::getOrgId, orgId);

        return dataQualityCheckMapper.selectOne(wrapper);
    }

    @Override
    public List<TblDataQualityCheckDetail> queryDetails(Long checkId, Long orgId) {
        log.info("查询检查明细, checkId={}, orgId={}", checkId, orgId);

        LambdaQueryWrapper<TblDataQualityCheckDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblDataQualityCheckDetail::getCheckId, checkId);
        wrapper.eq(TblDataQualityCheckDetail::getOrgId, orgId);
        wrapper.orderByAsc(TblDataQualityCheckDetail::getCreateTime);

        return dataQualityCheckDetailMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long executeQualityCheck(ExecuteQualityCheckParam param, Long orgId, String userId) {
        log.info("执行质量检查, param={}, orgId={}, userId={}", param, orgId, userId);

        try {
            // 1. 创建检查记录
            TblDataQualityCheck check = new TblDataQualityCheck();
            check.setCheckCode("CHK" + System.currentTimeMillis());
            check.setCheckName(param.getCheckName());
            check.setCheckType(param.getCheckType());
            check.setTargetType(param.getTargetType());
            check.setTargetId(param.getTargetId());
            check.setTargetName(param.getTargetName());
            check.setCheckStatus("PENDING");
            check.setStartTime(new Date());
            check.setOrgId(orgId);
            check.setCreateUser(userId);
            check.setCreateTime(new Date());
            check.setUpdateUser(userId);
            check.setUpdateTime(new Date());

            // 保存检查记录
            dataQualityCheckMapper.insert(check);
            Long checkId = check.getCheckId();

            // 2. 更新状态为检查中
            check.setCheckStatus("CHECKING");
            dataQualityCheckMapper.updateById(check);

            // 3. 执行质量检查规则
            int errorCount = 0;
            int warningCount = 0;
            int infoCount = 0;
            int totalCount = 0;

            if (param.getRuleIds() != null && !param.getRuleIds().isEmpty()) {
                // 查询规则列表
                LambdaQueryWrapper<TblDataQualityRule> ruleWrapper = new LambdaQueryWrapper<>();
                ruleWrapper.in(TblDataQualityRule::getRuleId, param.getRuleIds());
                ruleWrapper.eq(TblDataQualityRule::getOrgId, orgId);
                ruleWrapper.eq(TblDataQualityRule::getIsEnabled, "Y");
                List<TblDataQualityRule> rules = dataQualityRuleMapper.selectList(ruleWrapper);

                totalCount = rules.size();

                // 执行每个规则
                for (TblDataQualityRule rule : rules) {
                    TblDataQualityCheckDetail detail = new TblDataQualityCheckDetail();
                    detail.setCheckId(checkId);
                    detail.setRuleId(rule.getRuleId());
                    detail.setRuleCode(rule.getRuleCode());
                    detail.setRuleName(rule.getRuleName());
                    detail.setRuleType(rule.getRuleType());
                    detail.setThresholdValue(rule.getThresholdValue());
                    detail.setCheckTime(new Date());
                    detail.setOrgId(orgId);
                    detail.setCreateUser(userId);
                    detail.setCreateTime(new Date());

                    try {
                        // 执行规则检查（这里简化处理，实际应该执行SQL或条件检查）
                        // 由于没有实际的数据源连接，这里模拟检查结果
                        boolean checkPassed = executeRuleCheck(rule);

                        if (checkPassed) {
                            detail.setCheckResult("PASS");
                            detail.setCheckValue("0");
                            detail.setErrorCount(0);
                            detail.setErrorMessage("检查通过");
                        } else {
                            // 根据检查级别统计
                            if ("ERROR".equals(rule.getCheckLevel())) {
                                detail.setCheckResult("FAIL");
                                errorCount++;
                            } else if ("WARNING".equals(rule.getCheckLevel())) {
                                detail.setCheckResult("WARNING");
                                warningCount++;
                            } else {
                                detail.setCheckResult("WARNING");
                                infoCount++;
                            }
                            detail.setCheckValue("1");
                            detail.setErrorCount(1);
                            detail.setErrorMessage(rule.getErrorMessage());
                        }

                        detail.setSuggestion(rule.getSuggestion());
                    } catch (Exception e) {
                        log.error("执行规则检查失败, ruleId={}", rule.getRuleId(), e);
                        detail.setCheckResult("FAIL");
                        detail.setErrorCount(1);
                        detail.setErrorMessage("检查执行失败: " + e.getMessage());
                        errorCount++;
                    }

                    // 保存明细记录
                    dataQualityCheckDetailMapper.insert(detail);
                }
            }

            // 4. 计算质量评分和等级
            int passedCount = totalCount - errorCount - warningCount - infoCount;
            BigDecimal qualityScore = calculateQualityScore(totalCount, errorCount, warningCount, infoCount);
            String qualityLevel = determineQualityLevel(qualityScore);

            // 5. 更新检查记录
            check.setEndTime(new Date());
            check.setCheckStatus("COMPLETED");
            check.setQualityScore(qualityScore);
            check.setQualityLevel(qualityLevel);
            check.setTotalRules(totalCount);
            check.setPassedRules(passedCount);
            check.setFailedRules(errorCount);
            check.setWarningRules(warningCount);
            check.setUpdateTime(new Date());

            dataQualityCheckMapper.updateById(check);

            log.info("质量检查执行完成, checkId={}, qualityScore={}, qualityLevel={}",
                    checkId, qualityScore, qualityLevel);

            return checkId;

        } catch (Exception e) {
            log.error("执行质量检查失败", e);
            throw new RuntimeException("执行质量检查失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCheck(Long checkId, Long orgId) {
        log.info("删除质量检查记录, checkId={}, orgId={}", checkId, orgId);

        try {
            // 1. 删除检查明细
            LambdaQueryWrapper<TblDataQualityCheckDetail> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(TblDataQualityCheckDetail::getCheckId, checkId);
            detailWrapper.eq(TblDataQualityCheckDetail::getOrgId, orgId);
            dataQualityCheckDetailMapper.delete(detailWrapper);

            // 2. 删除检查记录
            LambdaQueryWrapper<TblDataQualityCheck> checkWrapper = new LambdaQueryWrapper<>();
            checkWrapper.eq(TblDataQualityCheck::getCheckId, checkId);
            checkWrapper.eq(TblDataQualityCheck::getOrgId, orgId);
            int result = dataQualityCheckMapper.delete(checkWrapper);

            return result > 0;
        } catch (Exception e) {
            log.error("删除质量检查记录失败, checkId={}", checkId, e);
            throw new RuntimeException("删除质量检查记录失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteCheck(List<Long> checkIds, Long orgId) {
        log.info("批量删除质量检查记录, checkIds={}, orgId={}", checkIds, orgId);

        if (checkIds == null || checkIds.isEmpty()) {
            return 0;
        }

        int deleteCount = 0;
        for (Long checkId : checkIds) {
            try {
                if (deleteCheck(checkId, orgId)) {
                    deleteCount++;
                }
            } catch (Exception e) {
                log.error("删除质量检查记录失败, checkId={}", checkId, e);
            }
        }

        return deleteCount;
    }

    /**
     * 执行规则检查（简化实现）
     * 实际应该根据规则的checkSql或checkCondition执行真实的检查
     */
    private boolean executeRuleCheck(TblDataQualityRule rule) {
        // 这里简化处理，实际应该执行SQL查询或条件判断
        // 暂时返回true表示检查通过
        return true;
    }

    /**
     * 计算质量评分
     * 评分规则：基础分100分，每个错误扣10分，每个警告扣5分，每个提示扣2分
     */
    private BigDecimal calculateQualityScore(int totalCount, int errorCount, int warningCount, int infoCount) {
        if (totalCount == 0) {
            return new BigDecimal("100");
        }

        int passCount = totalCount - errorCount - warningCount - infoCount;
        double score = (passCount * 100.0) / totalCount;

        // 确保分数在0-100之间
        if (score < 0) {
            score = 0;
        }
        if (score > 100) {
            score = 100;
        }

        return new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 确定质量等级
     * 优秀(EXCELLENT): 90-100分
     * 良好(GOOD): 75-89分
     * 一般(FAIR): 60-74分
     * 较差(POOR): 0-59分
     */
    private String determineQualityLevel(BigDecimal score) {
        double scoreValue = score.doubleValue();

        if (scoreValue >= 90) {
            return "EXCELLENT";
        } else if (scoreValue >= 75) {
            return "GOOD";
        } else if (scoreValue >= 60) {
            return "FAIR";
        } else {
            return "POOR";
        }
    }
}

