package com.global.treasurer.service.impl;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblGtAccountChange;
import com.global.treasurer.mapper.TblGtAccountChangeMapper;
import com.global.treasurer.service.TblGtAccountChangeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * 全球司库-账户变更Service实现类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Service
public class TblGtAccountChangeServiceImpl extends ServiceImpl<TblGtAccountChangeMapper, TblGtAccountChange>
        implements TblGtAccountChangeService {
    @Override
    public IPage<TblGtAccountChange> getPageList(Page<TblGtAccountChange> page,
                                                  String accountName,
                                                  String changeType,
                                                  String applicationStatus,
                                                  BigDecimal orgId) {
        return baseMapper.selectPageList(page, accountName, changeType, applicationStatus, orgId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAccountChange(TblGtAccountChange entity) {
        // 生成申请编号
        if (entity.getApplicationNo() == null || entity.getApplicationNo().isEmpty()) {
            String applicationNo = "AC" + java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                    .format(java.time.LocalDateTime.now())
                    + String.format("%04d", (int)(Math.random() * 10000));
            entity.setApplicationNo(applicationNo);
        }
        // 设置默认申请状态
        if (entity.getApplicationStatus() == null) {
            entity.setApplicationStatus("PENDING");
        }
        // 设置申请日期
        if (entity.getApplicationDate() == null) {
            entity.setApplicationDate(LocalDate.now());
        }
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccountChange(TblGtAccountChange entity) {
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAccountChange(BigDecimal applicationId) {
        return removeById(applicationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveAccountChange(BigDecimal applicationId, BigDecimal approverId,
                                        String approvalOpinion, String applicationStatus) {
        TblGtAccountChange entity = getById(applicationId);
        if (entity == null) {
            return false;
        }
        entity.setApproverId(approverId);
        entity.setApprovalDate(LocalDate.now());
        entity.setApprovalOpinion(approvalOpinion);
        entity.setApplicationStatus(applicationStatus);
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<BigDecimal> applicationIds) {
        int successCount = 0;
        for (BigDecimal applicationId : applicationIds) {
            if (removeById(applicationId)) {
                successCount++;
            }
        }
        return successCount > 0;
    }
}
