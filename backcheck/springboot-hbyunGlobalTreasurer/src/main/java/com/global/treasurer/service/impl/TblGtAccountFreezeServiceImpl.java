package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.dto.export.ExportAccountFreezeDTO;
import com.global.treasurer.entity.TblGtAccountFreeze;
import com.global.treasurer.mapper.TblGtAccountFreezeMapper;
import com.global.treasurer.service.TblGtAccountFreezeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全球司库-账户冻结Service实现类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Service
public class TblGtAccountFreezeServiceImpl extends ServiceImpl<TblGtAccountFreezeMapper, TblGtAccountFreeze>
        implements TblGtAccountFreezeService {
    private static final Logger log = LoggerFactory.getLogger(TblGtAccountFreezeServiceImpl.class);

    @Override
    public IPage<TblGtAccountFreeze> getPageList(Page<TblGtAccountFreeze> page,
                                                  Long recordId,
                                                  String accountNumber,
                                                  String freezeType,
                                                  String freezeStatus) {
        return baseMapper.selectPageList(page, recordId, accountNumber, freezeType, freezeStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFreezeRecord(TblGtAccountFreeze entity) {
        if (entity.getFreezeStatus() == null) {
            entity.setFreezeStatus("FROZEN");
        }
        if (entity.getFreezeDate() == null) {
            entity.setFreezeDate(LocalDate.now());
        }
        if (entity.getUnfreezeAmount() == null) {
            entity.setUnfreezeAmount(BigDecimal.ZERO);
        }
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfreezeRecord(Long recordId, String unfreezeType,
                                  BigDecimal unfreezeAmount, String unfreezeReason) {
        TblGtAccountFreeze entity = getById(recordId);
        if (entity == null) {
            return false;
        }

        if ("FULL".equals(unfreezeType)) {
            entity.setUnfreezeAmount(entity.getFreezeAmount());
            entity.setFreezeStatus("UNFROZEN");
        } else {
            BigDecimal newUnfreezeAmount = entity.getUnfreezeAmount().add(unfreezeAmount);
            entity.setUnfreezeAmount(newUnfreezeAmount);
            if (newUnfreezeAmount.compareTo(entity.getFreezeAmount()) >= 0) {
                entity.setFreezeStatus("UNFROZEN");
            } else {
                entity.setFreezeStatus("PARTIAL_UNFROZEN");
            }
        }

        entity.setUnfreezeDate(LocalDate.now());
        entity.setUnfreezeReason(unfreezeReason);
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFreezeRecord(Long recordId) {
        return removeById(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUnfreeze(List<Long> recordIds) {
        int successCount = 0;
        for (Long recordId : recordIds) {
            TblGtAccountFreeze entity = getById(recordId);
            if (entity != null && "FROZEN".equals(entity.getFreezeStatus())) {
                entity.setFreezeStatus("UNFROZEN");
                entity.setUnfreezeAmount(entity.getFreezeAmount());
                entity.setUnfreezeDate(LocalDate.now());
                if (updateById(entity)) {
                    successCount++;
                }
            }
        }
        return successCount;
    }

    @Override
    public List<ExportAccountFreezeDTO> exportList(Long recordId, String accountNumber,
                                                    String freezeType, String freezeStatus) {
        QueryWrapper<TblGtAccountFreeze> wrapper = new QueryWrapper<>();
        if (recordId != null) wrapper.eq("RECORD_ID", recordId);
        if (accountNumber != null && !accountNumber.isEmpty()) wrapper.like("ACCOUNT_NUMBER", accountNumber);
        if (freezeType != null && !freezeType.isEmpty()) wrapper.eq("FREEZE_TYPE", freezeType);
        if (freezeStatus != null && !freezeStatus.isEmpty()) wrapper.eq("FREEZE_STATUS", freezeStatus);
        wrapper.orderByDesc("CREATE_TIME");

        List<TblGtAccountFreeze> entities = list(wrapper);
        List<ExportAccountFreezeDTO> result = new ArrayList<>();
        for (TblGtAccountFreeze entity : entities) {
            result.add(ExportAccountFreezeDTO.fromEntity(entity));
        }
        return result;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        try {
            int totalCount = list().size();
            int frozenCount = list(new QueryWrapper<TblGtAccountFreeze>().eq("FREEZE_STATUS", "FROZEN")).size();
            int unfrozenCount = list(new QueryWrapper<TblGtAccountFreeze>().eq("FREEZE_STATUS", "UNFROZEN")).size();

            stats.put("totalRecords", totalCount);
            stats.put("frozenRecords", frozenCount);
            stats.put("unfrozenRecords", unfrozenCount);
            stats.put("unfreezeRate", totalCount > 0 ? Math.round((unfrozenCount * 100.0 / totalCount) * 10.0) / 10.0 : 0.0);

            BigDecimal totalFrozenAmount = list(new QueryWrapper<TblGtAccountFreeze>().eq("FREEZE_STATUS", "FROZEN"))
                    .stream()
                    .map(TblGtAccountFreeze::getFreezeAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("totalFrozenAmount", totalFrozenAmount.divide(BigDecimal.valueOf(10000), 2, BigDecimal.ROUND_HALF_UP));
        } catch (Exception e) {
            log.warn("获取统计数据失败: {}", e.getMessage());
            stats.put("totalRecords", 0);
            stats.put("frozenRecords", 0);
            stats.put("unfrozenRecords", 0);
            stats.put("unfreezeRate", 0.0);
            stats.put("totalFrozenAmount", 0.0);
        }
        return stats;
    }
}

