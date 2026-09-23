package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CreditLimitDTO;
import com.global.treasurer.dto.CreditLimitQueryDTO;
import com.global.treasurer.entity.TblCreditLimit;
import com.global.treasurer.mapper.CreditLimitMapper;
import com.global.treasurer.service.CreditLimitService;
import com.global.treasurer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 授信额度服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class CreditLimitServiceImpl implements CreditLimitService {
    private static final Logger log = LoggerFactory.getLogger(CreditLimitServiceImpl.class);

    @Autowired
    private CreditLimitMapper creditLimitMapper;

    @Override
    public PageInfo<TblCreditLimit> getLimitList(CreditLimitQueryDTO queryDTO) {
        log.info("查询授信额度列表, queryDTO: {}", queryDTO);
        try {
            // 使用MyBatis-Plus的QueryWrapper构建查询条件
            QueryWrapper<TblCreditLimit> wrapper = new QueryWrapper<>();

            // 添加查询条件
            if (queryDTO.getLimitType() != null && !queryDTO.getLimitType().isEmpty()) {
                wrapper.eq("LIMIT_TYPE", queryDTO.getLimitType());
            }
            if (queryDTO.getLimitStatus() != null && !queryDTO.getLimitStatus().isEmpty()) {
                wrapper.eq("LIMIT_STATUS", queryDTO.getLimitStatus());
            }
            if (queryDTO.getCompanyId() != null) {
                wrapper.eq("COMPANY_ID", queryDTO.getCompanyId());
            }
            if (queryDTO.getCurrencyCode() != null && !queryDTO.getCurrencyCode().isEmpty()) {
                wrapper.eq("CURRENCY_CODE", queryDTO.getCurrencyCode());
            }

            // 只查询未删除的记录
            wrapper.eq("DELETE_FLAG", 0);

            // 按创建时间倒序
            wrapper.orderByDesc("CREATED_TIME");

            // 分页查询
            int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

            Page<TblCreditLimit> page = new Page<>(pageNum, pageSize);
            IPage<TblCreditLimit> result = creditLimitMapper.selectPage(page, wrapper);

            // 转换为PageInfo
            PageInfo<TblCreditLimit> pageInfo = new PageInfo<>();
            pageInfo.setList(result.getRecords());
            pageInfo.setTotal(result.getTotal());
            pageInfo.setPageNum((int) result.getCurrent());
            pageInfo.setPageSize((int) result.getSize());
            pageInfo.setPages((int) result.getPages());

            log.info("查询授信额度列表成功, 共{}条记录", result.getTotal());
            return pageInfo;
        } catch (Exception e) {
            log.error("查询授信额度列表失败", e);
            return new PageInfo<>(new java.util.ArrayList<>());
        }
    }

    @Override
    public TblCreditLimit getLimitById(Long limitId) {
        TblCreditLimit limit = creditLimitMapper.selectLimitById(limitId);
        if (limit == null) {
            throw new ServiceException(404, "授信额度不存在");
        }
        return limit;
    }

    @Override
    public List<TblCreditLimit> getLimitsByContractId(Long contractId) {
        return creditLimitMapper.selectByContractId(contractId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblCreditLimit saveLimit(CreditLimitDTO dto) {
        TblCreditLimit limit = new TblCreditLimit();
        BeanUtils.copyProperties(dto, limit);
        
        if (dto.getLimitId() == null) {
            limit.setLimitNo(generateLimitNo());
            limit.setLimitStatus("NORMAL");
            limit.setUsedLimit(BigDecimal.ZERO);
            limit.setFrozenLimit(BigDecimal.ZERO);
            limit.setAvailableLimit(dto.getTotalLimit());
            limit.setDeleteFlag(0);
            limit.setCreatedTime(new Date());
            creditLimitMapper.insert(limit);
        } else {
            limit.setUpdatedTime(new Date());
            creditLimitMapper.updateById(limit);
        }
        return limit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLimit(Long limitId) {
        TblCreditLimit limit = getLimitById(limitId);
        limit.setDeleteFlag(1);
        limit.setUpdatedTime(new Date());
        creditLimitMapper.updateById(limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteLimits(List<Long> limitIds) {
        creditLimitMapper.batchDeleteByIds(limitIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useLimit(Long limitId, BigDecimal amount) {
        TblCreditLimit limit = getLimitById(limitId);
        if (limit.getAvailableLimit().compareTo(amount) < 0) {
            throw new ServiceException(400, "可用额度不足");
        }
        limit.setUsedLimit(limit.getUsedLimit().add(amount));
        limit.setAvailableLimit(limit.getAvailableLimit().subtract(amount));
        limit.setUpdatedTime(new Date());
        creditLimitMapper.updateById(limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseLimit(Long limitId, BigDecimal amount) {
        TblCreditLimit limit = getLimitById(limitId);
        limit.setUsedLimit(limit.getUsedLimit().subtract(amount));
        limit.setAvailableLimit(limit.getAvailableLimit().add(amount));
        limit.setUpdatedTime(new Date());
        creditLimitMapper.updateById(limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeLimit(Long limitId, BigDecimal amount) {
        TblCreditLimit limit = getLimitById(limitId);
        if (limit.getAvailableLimit().compareTo(amount) < 0) {
            throw new ServiceException(400, "可用额度不足");
        }
        limit.setFrozenLimit(limit.getFrozenLimit().add(amount));
        limit.setAvailableLimit(limit.getAvailableLimit().subtract(amount));
        limit.setUpdatedTime(new Date());
        creditLimitMapper.updateById(limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfreezeLimit(Long limitId, BigDecimal amount) {
        TblCreditLimit limit = getLimitById(limitId);
        limit.setFrozenLimit(limit.getFrozenLimit().subtract(amount));
        limit.setAvailableLimit(limit.getAvailableLimit().add(amount));
        limit.setUpdatedTime(new Date());
        creditLimitMapper.updateById(limit);
    }

    @Override
    public Map<String, Object> getLimitSummary(Long companyId) {
        return creditLimitMapper.selectLimitSummary(companyId);
    }

    private String generateLimitNo() {
        return "CL" + System.currentTimeMillis();
    }
}

