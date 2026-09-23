package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.DecisionRecommendation;
import com.global.treasurer.mapper.DecisionRecommendationMapper;
import com.global.treasurer.service.IDecisionRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 决策建议Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
// // 已移除,使用手动声明
@Service
public class DecisionRecommendationServiceImpl extends ServiceImpl<DecisionRecommendationMapper, DecisionRecommendation>
        implements IDecisionRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(DecisionRecommendationServiceImpl.class);


    @Override
    public IPage<DecisionRecommendation> selectPage(IPage<DecisionRecommendation> page, Map<String, Object> params) {
        log.info("========== 决策建议查询Service ==========");
        log.info("接收到的分页参数 - 当前页: {}, 每页大小: {}", page.getCurrent(), page.getSize());
        log.info("接收到的查询参数: {}", params);

        // 提取查询参数
        String recommendationNo = params.get("recommendationNo") != null ? params.get("recommendationNo").toString() : null;
        String recommendationName = params.get("recommendationName") != null ? params.get("recommendationName").toString() : null;
        String recommendationType = params.get("recommendationType") != null ? params.get("recommendationType").toString() : null;
        String recommendationStatus = params.get("recommendationStatus") != null ? params.get("recommendationStatus").toString() : null;
        String priority = params.get("priority") != null ? params.get("priority").toString() : null;
        Long orgId = params.get("orgId") != null ? Long.parseLong(params.get("orgId").toString()) : null;

        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage((int)page.getCurrent(), (int)page.getSize());

        // 调用自定义的条件查询方法
        List<DecisionRecommendation> list = baseMapper.selectByCondition(
                recommendationNo, recommendationName, recommendationType, recommendationStatus, priority, orgId);

        log.info("查询结果记录数: {}", list.size());

        // 使用 PageInfo 获取分页信息
        PageInfo<DecisionRecommendation> pageInfo = new PageInfo<>(list);

        // 构建返回的 IPage 对象
        Page<DecisionRecommendation> resultPage = new Page<>(page.getCurrent(), page.getSize(), pageInfo.getTotal());
        resultPage.setRecords(list);
        resultPage.setPages(pageInfo.getPages());

        log.info("返回的分页信息 - 当前页: {}, 每页大小: {}, 总记录数: {}, 总页数: {}",
                resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), resultPage.getPages());

        return resultPage;
    }

    @Override
    public boolean reviewRecommendation(Long recommendationId, String reviewOpinion, Long reviewUser) {
        try {
            DecisionRecommendation recommendation = this.getById(recommendationId);
            if (recommendation == null) {
                log.error("决策建议不存在，recommendationId: {}", recommendationId);
                return false;
            }

            // 检查建议状态
            if (!"PENDING".equals(recommendation.getRecommendationStatus())) {
                log.warn("决策建议状态不为待审核，无法审核，recommendationId: {}, recommendationStatus: {}",
                         recommendationId, recommendation.getRecommendationStatus());
                return false;
            }

            recommendation.setRecommendationStatus("REVIEWED");
            recommendation.setReviewUser(reviewUser);
            recommendation.setReviewDate(LocalDateTime.now());
            recommendation.setReviewOpinion(reviewOpinion);
            recommendation.setUpdateBy(reviewUser);
            recommendation.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(recommendation);
            if (updated) {
                log.info("决策建议审核成功，recommendationId: {}", recommendationId);
            }

            return updated;
        } catch (Exception e) {
            log.error("审核决策建议失败，recommendationId: {}", recommendationId, e);
            return false;
        }
    }

    @Override
    public boolean approveRecommendation(Long recommendationId, String approvalOpinion, Long approvalUser) {
        try {
            DecisionRecommendation recommendation = this.getById(recommendationId);
            if (recommendation == null) {
                log.error("决策建议不存在，recommendationId: {}", recommendationId);
                return false;
            }

            // 检查建议状态
            if ("APPROVED".equals(recommendation.getRecommendationStatus()) ||
                "REJECTED".equals(recommendation.getRecommendationStatus()) ||
                "IMPLEMENTED".equals(recommendation.getRecommendationStatus())) {
                log.warn("决策建议已处理，无法批准，recommendationId: {}, recommendationStatus: {}",
                         recommendationId, recommendation.getRecommendationStatus());
                return false;
            }

            recommendation.setRecommendationStatus("APPROVED");
            recommendation.setApprovalUser(approvalUser);
            recommendation.setApprovalDate(LocalDateTime.now());
            recommendation.setApprovalOpinion(approvalOpinion);
            recommendation.setUpdateBy(approvalUser);
            recommendation.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(recommendation);
            if (updated) {
                log.info("决策建议批准成功，recommendationId: {}", recommendationId);
            }

            return updated;
        } catch (Exception e) {
            log.error("批准决策建议失败，recommendationId: {}", recommendationId, e);
            return false;
        }
    }

    @Override
    public boolean rejectRecommendation(Long recommendationId, String rejectOpinion, Long rejectUser) {
        try {
            DecisionRecommendation recommendation = this.getById(recommendationId);
            if (recommendation == null) {
                log.error("决策建议不存在，recommendationId: {}", recommendationId);
                return false;
            }

            // 检查建议状态
            if ("APPROVED".equals(recommendation.getRecommendationStatus()) ||
                "REJECTED".equals(recommendation.getRecommendationStatus()) ||
                "IMPLEMENTED".equals(recommendation.getRecommendationStatus())) {
                log.warn("决策建议已处理，无法拒绝，recommendationId: {}, recommendationStatus: {}",
                         recommendationId, recommendation.getRecommendationStatus());
                return false;
            }

            recommendation.setRecommendationStatus("REJECTED");
            recommendation.setApprovalUser(rejectUser);
            recommendation.setApprovalDate(LocalDateTime.now());
            recommendation.setApprovalOpinion(rejectOpinion);
            recommendation.setUpdateBy(rejectUser);
            recommendation.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(recommendation);
            if (updated) {
                log.info("决策建议拒绝成功，recommendationId: {}", recommendationId);
            }

            return updated;
        } catch (Exception e) {
            log.error("拒绝决策建议失败，recommendationId: {}", recommendationId, e);
            return false;
        }
    }

    @Override
    public boolean implementRecommendation(Long recommendationId, String implementationResult, Long implementUser) {
        try {
            DecisionRecommendation recommendation = this.getById(recommendationId);
            if (recommendation == null) {
                log.error("决策建议不存在，recommendationId: {}", recommendationId);
                return false;
            }

            // 检查建议状态
            if (!"APPROVED".equals(recommendation.getRecommendationStatus())) {
                log.warn("决策建议状态不为已批准，无法实施，recommendationId: {}, recommendationStatus: {}",
                         recommendationId, recommendation.getRecommendationStatus());
                return false;
            }

            recommendation.setRecommendationStatus("IMPLEMENTED");
            recommendation.setImplementationUser(implementUser);
            recommendation.setImplementationDate(LocalDateTime.now());
            recommendation.setImplementationResult(implementationResult);
            recommendation.setUpdateBy(implementUser);
            recommendation.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(recommendation);
            if (updated) {
                log.info("决策建议实施成功，recommendationId: {}", recommendationId);
            }

            return updated;
        } catch (Exception e) {
            log.error("实施决策建议失败，recommendationId: " + recommendationId, e);
            return false;
        }
    }
}
