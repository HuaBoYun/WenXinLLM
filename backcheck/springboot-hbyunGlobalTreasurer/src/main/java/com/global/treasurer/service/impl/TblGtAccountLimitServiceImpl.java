package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtAccountLimit;
import com.global.treasurer.mapper.TblGtAccountLimitMapper;
import com.global.treasurer.service.TblGtAccountLimitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 全球司库-账户限额Service实现类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Service
public class TblGtAccountLimitServiceImpl extends ServiceImpl<TblGtAccountLimitMapper, TblGtAccountLimit>
        implements TblGtAccountLimitService {
    @Resource
    private TblGtAccountLimitMapper tblGtAccountLimitMapper;

    @Override
    public IPage<TblGtAccountLimit> getPageList(Page<TblGtAccountLimit> page,
                                                 String accountNumber,
                                                 String limitType,
                                                 String limitStatus) {
        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage((int) page.getCurrent(), (int) page.getSize());

        // 使用自定义的 XML 查询方法
        List<TblGtAccountLimit> list = tblGtAccountLimitMapper.selectPageList(
                accountNumber, limitType, limitStatus);

        // 获取分页信息
        PageInfo<TblGtAccountLimit> pageInfo = new PageInfo<>(list);

        // 转换为 MyBatis-Plus 的 IPage 格式
        Page<TblGtAccountLimit> pageResult = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        pageResult.setRecords(list);
        pageResult.setTotal(pageInfo.getTotal());

        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAccountLimit(TblGtAccountLimit entity) {
        // 设置默认限额状态
        if (entity.getLimitStatus() == null) {
            entity.setLimitStatus("ACTIVE");
        }
        return tblGtAccountLimitMapper.insert(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccountLimit(TblGtAccountLimit entity) {
        return tblGtAccountLimitMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAccountLimit(BigDecimal limitId) {
        return tblGtAccountLimitMapper.deleteById(limitId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLimitStatus(BigDecimal limitId, String limitStatus) {
        TblGtAccountLimit entity = tblGtAccountLimitMapper.selectById(limitId);
        if (entity == null) {
            return false;
        }
        entity.setLimitStatus(limitStatus);
        return tblGtAccountLimitMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveAccountLimit(BigDecimal limitId, BigDecimal approverId,
                                       String approvalOpinion, String limitStatus) {
        TblGtAccountLimit entity = tblGtAccountLimitMapper.selectById(limitId);
        if (entity == null) {
            return false;
        }
        entity.setLimitStatus(limitStatus);
        return tblGtAccountLimitMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean suspendAccountLimit(BigDecimal limitId, BigDecimal operatorId) {
        try {
            TblGtAccountLimit entity = tblGtAccountLimitMapper.selectById(limitId);
            if (entity == null) {
                return false;
            }
            entity.setLimitStatus("SUSPENDED");
            return tblGtAccountLimitMapper.updateById(entity) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateAccountLimit(BigDecimal limitId, BigDecimal operatorId) {
        try {
            TblGtAccountLimit entity = tblGtAccountLimitMapper.selectById(limitId);
            if (entity == null) {
                return false;
            }
            entity.setLimitStatus("ACTIVE");
            return tblGtAccountLimitMapper.updateById(entity) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
