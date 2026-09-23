package com.financial.sharing.service.impl;
import com.financial.sharing.oracle.entity.TransactionEntryEntity;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.service.TransactionEntryService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TransactionEntryQueryParam;
import com.financial.sharing.vo.param.TransactionEntrySaveParam;
import com.financial.sharing.vo.result.TransactionEntryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 事项分录服务实现类
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class TransactionEntryServiceImpl implements TransactionEntryService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    @Override
    public PageResult<TransactionEntryVO> getTransactionEntryPage(TransactionEntryQueryParam param) {
        Page<TransactionEntryVO> page = new Page<>(param.getPageNum(), param.getPageSize());
        
        IPage<TransactionEntryVO> result;
        
            result = dateBaseConfig.getOracleTransactionEntryMapper().selectTransactionEntryPage(page, param);
        
        
        return new PageResult<>((int)result.getTotal(), (int)result.getCurrent(), (int)result.getPages(), (int)result.getSize(), result.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionEntryVO saveOrUpdateTransactionEntry(TransactionEntrySaveParam param) {
        // 使用Oracle数据库
        com.financial.sharing.oracle.entity.TransactionEntryEntity oracleEntity =
            new com.financial.sharing.oracle.entity.TransactionEntryEntity();
        BeanUtils.copyProperties(param, oracleEntity);

        boolean success;

        if (param.getEntryId() == null) {
            success = dateBaseConfig.getOracleTransactionEntryMapper().insert(oracleEntity) > 0;
        } else {
            success = dateBaseConfig.getOracleTransactionEntryMapper().updateById(oracleEntity) > 0;
        }
        if (!success) {
            log.warn("保存或更新事项分录失败");
            return null;
        }
        return getTransactionEntryById(oracleEntity.getEntryId());
    }

    @Override
    public TransactionEntryVO getTransactionEntryById(Long entryId) {
        TransactionEntryEntity entity;

        com.financial.sharing.oracle.entity.TransactionEntryEntity oracleEntity =
            dateBaseConfig.getOracleTransactionEntryMapper().selectById(entryId);
        if (oracleEntity == null) {
            return null;
        }

        entity = new TransactionEntryEntity();
        BeanUtils.copyProperties(oracleEntity, entity);

        if (entity == null) {
            return null;
        }

        TransactionEntryVO vo = new TransactionEntryVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTransactionEntry(Long entryId) {
        boolean success;
        
            success = dateBaseConfig.getOracleTransactionEntryMapper().deleteById(entryId) > 0;
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTransactionEntries(List<Long> entryIds) {
        if (CollectionUtils.isEmpty(entryIds)) {
            return false;
        }

        int result = dateBaseConfig.getOracleTransactionEntryMapper().batchDelete(entryIds, null);

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTransactionEntriesByTransactionId(Long transactionId) {
        int result;
        
            result = dateBaseConfig.getOracleTransactionEntryMapper().deleteByTransactionId(transactionId, null);
        
        
        return result > 0;
    }

    @Override
    public boolean checkEntryNoExists(String entryNo, Long bookId, Long tenantId, Long excludeId) {
        TransactionEntryEntity entity;
        
            com.financial.sharing.oracle.entity.TransactionEntryEntity oracleEntity = 
                dateBaseConfig.getOracleTransactionEntryMapper().selectByEntryNo(entryNo, bookId, tenantId, excludeId);
            entity = oracleEntity != null ? new TransactionEntryEntity() : null;
        
        
        return entity != null;
    }

    @Override
    public List<TransactionEntryVO> getTransactionEntriesByTransactionId(Long transactionId) {
        List<TransactionEntryVO> list;
        
            list = dateBaseConfig.getOracleTransactionEntryMapper().selectByTransactionId(transactionId);
        
        
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public List<TransactionEntryVO> getTransactionEntriesBySubjectId(Long subjectId, Long bookId, Long tenantId) {
        List<TransactionEntryVO> list;
        
            list = dateBaseConfig.getOracleTransactionEntryMapper().selectBySubjectId(subjectId, bookId, tenantId);
        
        
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public List<TransactionEntryVO> getTransactionEntriesByCurrency(String currencyCode, Long bookId, Long tenantId) {
        List<TransactionEntryVO> list;
        
            list = dateBaseConfig.getOracleTransactionEntryMapper().selectByCurrency(currencyCode, bookId, tenantId);
        
        
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public TransactionEntryVO calculateTransactionTotal(Long transactionId) {
        TransactionEntryVO vo;
        
            vo = dateBaseConfig.getOracleTransactionEntryMapper().calculateTransactionTotal(transactionId);
        
        
        return vo;
    }

    @Override
    public boolean validateTransactionBalance(Long transactionId) {
        boolean result;
        
            result = dateBaseConfig.getOracleTransactionEntryMapper().validateTransactionBalance(transactionId);
        
        
        return result;
    }

    @Override
    public TransactionEntryVO getSubjectEntrySummary(Long subjectId, Long bookId, Long tenantId) {
        TransactionEntryVO vo;
        
            vo = dateBaseConfig.getOracleTransactionEntryMapper().getSubjectEntrySummary(subjectId, bookId, tenantId);
        
        
        return vo;
    }

    @Override
    public TransactionEntryVO getCurrencyEntrySummary(String currencyCode, Long bookId, Long tenantId) {
        TransactionEntryVO vo;
        
            vo = dateBaseConfig.getOracleTransactionEntryMapper().getCurrencyEntrySummary(currencyCode, bookId, tenantId);
        
        
        return vo;
    }

    @Override
    public List<TransactionEntryVO> countEntriesBySubject(Long bookId, Long tenantId) {
        List<TransactionEntryVO> list;
        
            list = dateBaseConfig.getOracleTransactionEntryMapper().countBySubject(bookId, tenantId);
        
        
        return list != null ? list : new ArrayList<>();
    }

    @Override
    public List<TransactionEntryVO> sumEntriesByCurrency(Long bookId, Long tenantId) {
        List<TransactionEntryVO> list;
        
            list = dateBaseConfig.getOracleTransactionEntryMapper().sumByCurrency(bookId, tenantId);
        
        
        return list != null ? list : new ArrayList<>();
    }
}

