package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.RiskAssessment;
import com.huabo.contract.mapper.RiskAssessmentMapper;
import com.huabo.contract.service.RiskAssessmentDetailService;
import com.huabo.contract.service.RiskAssessmentService;
import com.huabo.contract.vo.RiskAssessmentQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 风险评估主表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class RiskAssessmentServiceImpl extends ServiceImpl<RiskAssessmentMapper, RiskAssessment> implements RiskAssessmentService {

    @Autowired
    private RiskAssessmentMapper riskAssessmentMapper;

    @Autowired
    private RiskAssessmentDetailService riskAssessmentDetailService;

    @Override
    public PageInfo<RiskAssessment> getRiskAssessmentList(RiskAssessmentQueryParam param) {
        try {
            // 设置分页参数
            PageHelper.startPage(param.getCurrent(), param.getSize());
            
            // 查询数据
            List<RiskAssessment> list = riskAssessmentMapper.selectRiskAssessmentList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询风险评估列表失败", e);
            throw new RuntimeException("查询风险评估列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRiskAssessment(RiskAssessment riskAssessment) {
        try {
            Date now = new Date();
            
            if (riskAssessment.getId() == null) {
                // 新增
                if (!StringUtils.hasText(riskAssessment.getAssessmentNo())) {
                    riskAssessment.setAssessmentNo(generateAssessmentNo());
                }
                riskAssessment.setCreateTime(now);
                riskAssessment.setUpdateTime(now);
                riskAssessment.setAssessmentStatus(1); // 待评估
                riskAssessment.setApprovalStatus(1); // 待审批
                return this.save(riskAssessment);
            } else {
                // 修改
                riskAssessment.setUpdateTime(now);
                return this.updateById(riskAssessment);
            }
        } catch (Exception e) {
            log.error("保存风险评估失败", e);
            throw new RuntimeException("保存风险评估失败：" + e.getMessage());
        }
    }

    @Override
    public RiskAssessment getRiskAssessmentById(Long id) {
        try {
            return this.getById(id);
        } catch (Exception e) {
            log.error("获取风险评估详情失败，ID：{}", id, e);
            throw new RuntimeException("获取风险评估详情失败：" + e.getMessage());
        }
    }

    @Override
    public RiskAssessment getRiskAssessmentByAssessmentNo(String assessmentNo) {
        try {
            return riskAssessmentMapper.selectByAssessmentNo(assessmentNo);
        } catch (Exception e) {
            log.error("根据评估编号获取风险评估失败，评估编号：{}", assessmentNo, e);
            throw new RuntimeException("根据评估编号获取风险评估失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRiskAssessment(Long id) {
        try {
            // 删除评估明细
            riskAssessmentDetailService.deleteDetailsByAssessmentId(id);
            
            // 删除主表
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除风险评估失败，ID：{}", id, e);
            throw new RuntimeException("删除风险评估失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRiskAssessment(List<Long> ids) {
        try {
            for (Long id : ids) {
                deleteRiskAssessment(id);
            }
            return true;
        } catch (Exception e) {
            log.error("批量删除风险评估失败", e);
            throw new RuntimeException("批量删除风险评估失败：" + e.getMessage());
        }
    }

    @Override
    public String generateAssessmentNo() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());

            // 查询当天最大序号 - 使用List查询避免达梦数据库语法问题
            QueryWrapper<RiskAssessment> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("assessment_no", "RA" + dateStr);
            queryWrapper.orderByDesc("assessment_no");

            // 使用list查询，然后取第一个，避免LIMIT语法问题
            List<RiskAssessment> assessments = this.list(queryWrapper);
            RiskAssessment lastAssessment = null;
            if (assessments != null && !assessments.isEmpty()) {
                lastAssessment = assessments.get(0);
            }

            int sequence = 1;
            if (lastAssessment != null && StringUtils.hasText(lastAssessment.getAssessmentNo())) {
                String lastNo = lastAssessment.getAssessmentNo();
                if (lastNo.length() >= 12) {
                    String lastSequence = lastNo.substring(10);
                    sequence = Integer.parseInt(lastSequence) + 1;
                }
            }

            return String.format("RA%s%04d", dateStr, sequence);
        } catch (Exception e) {
            log.error("生成评估编号失败", e);
            throw new RuntimeException("生成评估编号失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsAssessmentNo(String assessmentNo, Long excludeId) {
        try {
            return riskAssessmentMapper.existsAssessmentNo(assessmentNo, excludeId);
        } catch (Exception e) {
            log.error("检查评估编号是否存在失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAssessmentStatus(Long id, Integer assessmentStatus) {
        try {
            RiskAssessment riskAssessment = new RiskAssessment();
            riskAssessment.setId(id);
            riskAssessment.setAssessmentStatus(assessmentStatus);
            riskAssessment.setUpdateTime(new Date());
            
            return this.updateById(riskAssessment);
        } catch (Exception e) {
            log.error("更新评估状态失败，ID：{}，状态：{}", id, assessmentStatus, e);
            throw new RuntimeException("更新评估状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateAssessmentStatus(List<Long> ids, Integer assessmentStatus) {
        try {
            return riskAssessmentMapper.batchUpdateAssessmentStatus(ids, assessmentStatus, null) > 0;
        } catch (Exception e) {
            log.error("批量更新评估状态失败", e);
            throw new RuntimeException("批量更新评估状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApprovalStatus(Long id, Integer approvalStatus, Long approverId) {
        try {
            RiskAssessment riskAssessment = new RiskAssessment();
            riskAssessment.setId(id);
            riskAssessment.setApprovalStatus(approvalStatus);
            riskAssessment.setApproverId(approverId);
            riskAssessment.setApprovalDate(new Date());
            riskAssessment.setUpdateTime(new Date());
            
            return this.updateById(riskAssessment);
        } catch (Exception e) {
            log.error("更新审批状态失败，ID：{}，状态：{}", id, approvalStatus, e);
            throw new RuntimeException("更新审批状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateApprovalStatus(List<Long> ids, Integer approvalStatus, Long approverId) {
        try {
            return riskAssessmentMapper.batchUpdateApprovalStatus(ids, approvalStatus, approverId, null) > 0;
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            throw new RuntimeException("批量更新审批状态失败：" + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessment> getPendingApprovalList(Long approverId) {
        try {
            return riskAssessmentMapper.selectPendingApprovalList(approverId);
        } catch (Exception e) {
            log.error("获取待审批风险评估列表失败", e);
            throw new RuntimeException("获取待审批风险评估列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessment> getHighRiskList(Integer riskLevel) {
        try {
            return riskAssessmentMapper.selectHighRiskList(riskLevel);
        } catch (Exception e) {
            log.error("获取高风险评估列表失败", e);
            throw new RuntimeException("获取高风险评估列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessment> getRiskAssessmentByProjectId(Long projectId) {
        try {
            return riskAssessmentMapper.selectByProjectId(projectId);
        } catch (Exception e) {
            log.error("根据项目ID获取风险评估列表失败，项目ID：{}", projectId, e);
            throw new RuntimeException("根据项目ID获取风险评估列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<RiskAssessment> getRiskAssessmentByCounterpartId(Long counterpartId) {
        try {
            return riskAssessmentMapper.selectByCounterpartId(counterpartId);
        } catch (Exception e) {
            log.error("根据相对方ID获取风险评估列表失败，相对方ID：{}", counterpartId, e);
            throw new RuntimeException("根据相对方ID获取风险评估列表失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRiskAssessmentStatistics(RiskAssessmentQueryParam param) {
        try {
            return riskAssessmentMapper.selectRiskAssessmentStatistics(param);
        } catch (Exception e) {
            log.error("获取风险评估统计数据失败", e);
            throw new RuntimeException("获取风险评估统计数据失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRiskLevelDistribution(RiskAssessmentQueryParam param) {
        try {
            return riskAssessmentMapper.selectRiskLevelDistribution(param);
        } catch (Exception e) {
            log.error("获取风险等级分布统计失败", e);
            throw new RuntimeException("获取风险等级分布统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAssessmentStatusDistribution(RiskAssessmentQueryParam param) {
        try {
            return riskAssessmentMapper.selectAssessmentStatusDistribution(param);
        } catch (Exception e) {
            log.error("获取评估状态分布统计失败", e);
            throw new RuntimeException("获取评估状态分布统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyAssessmentTrend(RiskAssessmentQueryParam param) {
        try {
            return riskAssessmentMapper.selectMonthlyAssessmentTrend(param);
        } catch (Exception e) {
            log.error("获取月度评估趋势失败", e);
            throw new RuntimeException("获取月度评估趋势失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateRiskScoreAndLevel(Long id) {
        try {
            // 计算总分
            BigDecimal totalScore = riskAssessmentDetailService.calculateTotalScore(id);
            
            // 获取最高风险等级
            Integer maxRiskLevel = riskAssessmentDetailService.getMaxRiskLevel(id);
            
            // 更新主表
            RiskAssessment riskAssessment = new RiskAssessment();
            riskAssessment.setId(id);
            riskAssessment.setTotalScore(totalScore);
            riskAssessment.setRiskLevel(maxRiskLevel);
            riskAssessment.setUpdateTime(new Date());
            
            return this.updateById(riskAssessment);
        } catch (Exception e) {
            log.error("计算风险评估总分和等级失败，ID：{}", id, e);
            throw new RuntimeException("计算风险评估总分和等级失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitAssessment(Long id) {
        try {
            // 先计算总分和等级
            calculateRiskScoreAndLevel(id);
            
            // 更新状态为已完成
            return updateAssessmentStatus(id, 3);
        } catch (Exception e) {
            log.error("提交评估失败，ID：{}", id, e);
            throw new RuntimeException("提交评估失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveAssessment(Long id, Integer approvalStatus, Long approverId, String remarks) {
        try {
            RiskAssessment riskAssessment = new RiskAssessment();
            riskAssessment.setId(id);
            riskAssessment.setApprovalStatus(approvalStatus);
            riskAssessment.setApproverId(approverId);
            riskAssessment.setApprovalDate(new Date());
            riskAssessment.setRemarks(remarks);
            riskAssessment.setUpdateTime(new Date());
            
            return this.updateById(riskAssessment);
        } catch (Exception e) {
            log.error("审批评估失败，ID：{}", id, e);
            throw new RuntimeException("审批评估失败：" + e.getMessage());
        }
    }
}
