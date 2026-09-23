package com.global.treasurer.service.impl;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblGtAccountCheck;
import com.global.treasurer.mapper.TblGtAccountCheckMapper;
import com.global.treasurer.service.TblGtAccountCheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 全球司库-账户检查Service实现类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Service
public class TblGtAccountCheckServiceImpl extends ServiceImpl<TblGtAccountCheckMapper, TblGtAccountCheck>
        implements TblGtAccountCheckService {
    @Override
    public IPage<TblGtAccountCheck> getPageList(Page<TblGtAccountCheck> page,
                                                 String accountNumber,
                                                 String checkType,
                                                 String checkStatus,
                                                 Long checkId,
                                                 String startDate,
                                                 String endDate,
                                                 BigDecimal orgId) {
        return baseMapper.selectPageList(page, accountNumber, checkType, checkStatus, checkId, startDate, endDate, orgId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAccountCheck(TblGtAccountCheck entity) {
        if (entity.getCheckStatus() == null) {
            entity.setCheckStatus("PENDING");
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccountCheck(TblGtAccountCheck entity) {
        entity.setUpdateTime(LocalDateTime.now());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAccountCheck(BigDecimal checkId) {
        return removeById(checkId.longValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recheckAccountCheck(Long checkId, BigDecimal updateUser) {
        LambdaUpdateWrapper<TblGtAccountCheck> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TblGtAccountCheck::getCheckId, checkId)
               .set(TblGtAccountCheck::getCheckStatus, "PENDING")
               .set(TblGtAccountCheck::getCheckResult, null)
               .set(TblGtAccountCheck::getUpdateUser, updateUser)
               .set(TblGtAccountCheck::getUpdateTime, LocalDateTime.now());
        return update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveAccountCheck(BigDecimal checkId, BigDecimal approverId,
                                       String approvalOpinion, String checkStatus) {
        TblGtAccountCheck entity = getById(checkId);
        if (entity == null) {
            return false;
        }
        entity.setApproverId(approverId);
        entity.setApprovalDate(java.time.LocalDate.now());
        entity.setApprovalOpinion(approvalOpinion);
        entity.setCheckStatus(checkStatus);
        entity.setUpdateTime(LocalDateTime.now());
        return updateById(entity);
    }
}
