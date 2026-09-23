package com.global.treasurer.service.impl;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtAccountOpening;
import com.global.treasurer.mapper.TblGtAccountOpeningMapper;
import com.global.treasurer.service.TblGtAccountOpeningService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 全球司库-开户申请Service实现类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Service
public class TblGtAccountOpeningServiceImpl extends ServiceImpl<TblGtAccountOpeningMapper, TblGtAccountOpening>
        implements TblGtAccountOpeningService {
    @Resource
    private TblGtAccountOpeningMapper tblGtAccountOpeningMapper;

    @Override
    public IPage<TblGtAccountOpening> getPageList(Page<TblGtAccountOpening> page,
                                                   String applicationNo,
                                                   String accountName,
                                                   String applicationStatus,
                                                   String bankCode,
                                                   BigDecimal orgId) {
        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage((int) page.getCurrent(), (int) page.getSize());

        // 使用自定义的 XML 查询方法
        List<TblGtAccountOpening> list = tblGtAccountOpeningMapper.selectPageList(
                applicationNo, accountName, applicationStatus, bankCode, orgId);

        // 获取分页信息
        PageInfo<TblGtAccountOpening> pageInfo = new PageInfo<>(list);

        // 转换为 MyBatis-Plus 的 IPage 格式
        Page<TblGtAccountOpening> pageResult = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        pageResult.setRecords(list);
        pageResult.setTotal(pageInfo.getTotal());

        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAccountOpening(TblGtAccountOpening entity) {
        // 自动生成申请编号
        if (entity.getApplicationNo() == null || entity.getApplicationNo().isEmpty()) {
            String applicationNo = "OP" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
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
        return tblGtAccountOpeningMapper.insert(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccountOpening(TblGtAccountOpening entity) {
        return tblGtAccountOpeningMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAccountOpening(BigDecimal applicationId) {
        return tblGtAccountOpeningMapper.deleteById(applicationId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveAccountOpening(BigDecimal applicationId, BigDecimal approverId,
                                         String approvalOpinion, String applicationStatus) {
        TblGtAccountOpening entity = tblGtAccountOpeningMapper.selectById(applicationId);
        if (entity == null) {
            return false;
        }
        entity.setApproverId(approverId);
        entity.setApprovalDate(LocalDate.now());
        entity.setApprovalOpinion(approvalOpinion);
        entity.setApplicationStatus(applicationStatus);
        return tblGtAccountOpeningMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelAccountOpening(BigDecimal applicationId, BigDecimal updateUser) {
        TblGtAccountOpening entity = tblGtAccountOpeningMapper.selectById(applicationId);
        if (entity == null) {
            return false;
        }
        entity.setApplicationStatus("CANCELLED");
        entity.setUpdateUser(updateUser);
        entity.setUpdateTime(LocalDateTime.now());
        return tblGtAccountOpeningMapper.updateById(entity) > 0;
    }

    @Override
    public Map<String, Object> getStatusStatistics(BigDecimal orgId) {
        return tblGtAccountOpeningMapper.selectStatusStatistics(orgId);
    }
}
