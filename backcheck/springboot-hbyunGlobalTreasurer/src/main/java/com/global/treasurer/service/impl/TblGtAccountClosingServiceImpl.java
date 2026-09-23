package com.global.treasurer.service.impl;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblGtAccountClosing;
import com.global.treasurer.mapper.TblGtAccountClosingMapper;
import com.global.treasurer.service.TblGtAccountClosingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 全球司库-销户申请Service实现类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Service
public class TblGtAccountClosingServiceImpl extends ServiceImpl<TblGtAccountClosingMapper, TblGtAccountClosing>
        implements TblGtAccountClosingService {
    @Override
    public IPage<TblGtAccountClosing> getPageList(Page<TblGtAccountClosing> page,
                                                   String applicationNo,
                                                   String accountNumber,
                                                   String applicationStatus,
                                                   BigDecimal orgId) {
        return baseMapper.selectPageList(page, applicationNo, accountNumber, applicationStatus, orgId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAccountClosing(TblGtAccountClosing entity) {
        // 自动生成申请编号
        if (entity.getApplicationNo() == null || entity.getApplicationNo().isEmpty()) {
            String applicationNo = "CLS" + java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                    .format(LocalDateTime.now())
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
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccountClosing(TblGtAccountClosing entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAccountClosing(BigDecimal applicationId) {
        return removeById(applicationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveAccountClosing(BigDecimal applicationId, BigDecimal approverId,
                                         String approvalOpinion, String applicationStatus) {
        TblGtAccountClosing entity = getById(applicationId);
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
    public int batchApproveAccountClosing(List<BigDecimal> applicationIds, BigDecimal approverId,
                                           String approvalOpinion, String applicationStatus) {
        int successCount = 0;
        for (BigDecimal applicationId : applicationIds) {
            TblGtAccountClosing entity = getById(applicationId);
            if (entity != null && "PENDING".equals(entity.getApplicationStatus())) {
                entity.setApproverId(approverId);
                entity.setApprovalDate(LocalDate.now());
                entity.setApprovalOpinion(approvalOpinion);
                entity.setApplicationStatus(applicationStatus);
                entity.setUpdateTime(LocalDateTime.now());
                if (updateById(entity)) {
                    successCount++;
                }
            }
        }
        return successCount;
    }
}
