package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.user.UserProvider;

import com.huabo.contract.vo.ProjectSettlementQueryParam;
import com.huabo.contract.entity.ProjectSettlement;
import com.huabo.contract.mapper.ProjectSettlementMapper;
import com.huabo.contract.mapper.ProjectSettlementMapper.ProjectSettlementStatistics;
import com.huabo.contract.service.ProjectSettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 项目结算服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectSettlementServiceImpl extends ServiceImpl<ProjectSettlementMapper, ProjectSettlement> 
        implements ProjectSettlementService {

    private final ProjectSettlementMapper projectSettlementMapper;
    private final UserProvider userProvider;

    @Override
    public IPage<ProjectSettlement> getSettlementPage(ProjectSettlementQueryParam queryParam) {
        Page<ProjectSettlement> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        return projectSettlementMapper.selectSettlementPage(page, queryParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectSettlement createSettlement(ProjectSettlement settlement) {
        // 设置创建信息
        settlement.setCreateBy(1L); // 默认用户ID
        settlement.setUpdateBy(1L); // 默认用户ID
        settlement.setSettlorId(1L); // 默认结算人ID
        settlement.setCreateTime(new Date());
        settlement.setUpdateTime(new Date());

        // 如果没有设置结算编号，自动生成
        if (!StringUtils.hasText(settlement.getSettlementNo())) {
            settlement.setSettlementNo(generateSettlementNo());
        }

        // 验证结算编号唯一性
        if (!isSettlementNoUnique(settlement.getSettlementNo())) {
            throw new RuntimeException("结算编号已存在：" + settlement.getSettlementNo());
        }

        // 设置默认状态
        if (settlement.getSettlementStatus() == null) {
            settlement.setSettlementStatus((short) 1); // 待结算
        }

        // 保存结算记录
        boolean success = save(settlement);
        if (!success) {
            throw new RuntimeException("结算记录保存失败");
        }

        log.info("创建项目结算成功，结算编号：{}", settlement.getSettlementNo());
        return settlement;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectSettlement updateSettlement(ProjectSettlement settlement) {
        // 设置更新用户信息

        // 检查记录是否存在
        ProjectSettlement existingSettlement = getById(settlement.getId());
        if (existingSettlement == null) {
            throw new RuntimeException("结算记录不存在");
        }

        // 设置更新信息
        settlement.setUpdateBy(1L); // 默认用户ID
        settlement.setUpdateTime(new Date());

        // 更新记录
        boolean success = updateById(settlement);
        if (!success) {
            throw new RuntimeException("结算记录更新失败");
        }

        log.info("更新项目结算成功，结算编号：{}", settlement.getSettlementNo());
        return settlement;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSettlement(Long id) {
        // 检查记录是否存在
        ProjectSettlement settlement = getById(id);
        if (settlement == null) {
            throw new RuntimeException("结算记录不存在");
        }

        // 检查是否可以删除（已审核的不能删除）
        if (settlement.getSettlementStatus() != null && settlement.getSettlementStatus() >= 4) {
            throw new RuntimeException("已审核的结算记录不能删除");
        }

        boolean success = removeById(id);
        if (success) {
            log.info("删除项目结算成功，结算编号：{}", settlement.getSettlementNo());
        }
        return success;
    }

    @Override
    public ProjectSettlement getSettlementById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewSettlement(Long id, String reviewComments, Long reviewerId) {
        ProjectSettlement settlement = getById(id);
        if (settlement == null) {
            throw new RuntimeException("结算记录不存在");
        }

        // 设置审核信息
        settlement.setReviewerId(reviewerId);
        settlement.setReviewDate(new Date());
        settlement.setReviewComments(reviewComments);
        settlement.setSettlementStatus((short) 4); // 已审核
        settlement.setUpdateTime(new Date());

        boolean success = updateById(settlement);
        if (success) {
            log.info("结算审核成功，结算编号：{}", settlement.getSettlementNo());
        }
        return success;
    }

    @Override
    public ProjectSettlementStatistics getSettlementStatistics(Long projectId) {
        return projectSettlementMapper.getSettlementStatistics(projectId);
    }

    @Override
    public List<ProjectSettlement> getPendingReviewSettlements() {
        return projectSettlementMapper.getPendingReviewSettlements();
    }

    @Override
    public String generateSettlementNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        
        // 查询当天已有的结算编号数量
        QueryWrapper<ProjectSettlement> wrapper = new QueryWrapper<>();
        wrapper.likeRight("settlement_no", "JS" + dateStr);
        int count = Math.toIntExact(count(wrapper));
        
        // 生成新的编号
        return String.format("JS%s%03d", dateStr, count + 1);
    }

    @Override
    public boolean isSettlementNoUnique(String settlementNo) {
        int count = projectSettlementMapper.countBySettlementNo(settlementNo);
        return count == 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchReviewSettlements(List<Long> ids, String reviewComments, Long reviewerId) {
        boolean allSuccess = true;
        for (Long id : ids) {
            try {
                reviewSettlement(id, reviewComments, reviewerId);
            } catch (Exception e) {
                log.error("批量审核结算失败，ID：{}, 错误：{}", id, e.getMessage());
                allSuccess = false;
            }
        }
        return allSuccess;
    }
}
