package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.oracle.entity.VoucherEntryEntity;
import com.financial.sharing.oracle.mapper.VoucherEntryMapper;
import com.financial.sharing.service.VoucherEntryService;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 凭证分录服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Service
public class VoucherEntryServiceImpl extends ServiceImpl<VoucherEntryMapper, VoucherEntryEntity>
        implements VoucherEntryService {

    @Override
    public List<VoucherEntryEntity> getEntriesByVoucherId(Long voucherId) {
        LambdaQueryWrapper<VoucherEntryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VoucherEntryEntity::getVoucherId, voucherId)
               .orderByAsc(VoucherEntryEntity::getEntrySeq);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveVoucherEntries(Long voucherId, List<Map<String, Object>> entries, TblStaffUtil currentUser) {
        if (CollectionUtils.isEmpty(entries)) {
            return false;
        }

        // 校验分录数据
        if (!validateEntries(entries)) {
            throw new RuntimeException("分录数据校验失败");
        }

        List<VoucherEntryEntity> entryEntities = new ArrayList<>();

        for (int i = 0; i < entries.size(); i++) {
            Map<String, Object> entryData = entries.get(i);
            VoucherEntryEntity entry = new VoucherEntryEntity();

            // 复制属性
            BeanUtils.copyProperties(entryData, entry, "entryId");

            // 设置基础信息
            entry.setVoucherId(voucherId);
            entry.setEntrySeq(i + 1);
            entry.setBookId(currentUser.getLinkDetp().getOrgid().longValue());
            entry.setTenantId(currentUser.getCurrentOrg().getOrgid().longValue());
            entry.setCreator(currentUser.getStaffid().longValue());
            entry.setUpdater(currentUser.getStaffid().longValue());

            // 处理金额
            BigDecimal debitAmount = new BigDecimal(entryData.getOrDefault("debitAmount", "0").toString());
            BigDecimal creditAmount = new BigDecimal(entryData.getOrDefault("creditAmount", "0").toString());
            BigDecimal exchangeRate = new BigDecimal(entryData.getOrDefault("exchangeRate", "1").toString());

            entry.setDebitAmount(debitAmount);
            entry.setCreditAmount(creditAmount);
            entry.setExchangeRate(exchangeRate);

            // 计算原币金额
            entry.setOriginalDebit(debitAmount.multiply(exchangeRate));
            entry.setOriginalCredit(creditAmount.multiply(exchangeRate));

            // 设置币种
            entry.setCurrencyCode(entryData.getOrDefault("currencyCode", "CNY").toString());

            entryEntities.add(entry);
        }
        return this.saveBatch(entryEntities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVoucherEntries(Long voucherId, List<Map<String, Object>> entries, TblStaffUtil currentUser) {
        if (CollectionUtils.isEmpty(entries)) {
            return false;
        }

        // 校验分录数据
        if (!validateEntries(entries)) {
            throw new RuntimeException("分录数据校验失败");
        }

        // 先删除原有分录
        deleteByVoucherId(voucherId);

        // 保存新分录
        return saveVoucherEntries(voucherId, entries, currentUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByVoucherId(Long voucherId) {
        LambdaUpdateWrapper<VoucherEntryEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(VoucherEntryEntity::getVoucherId, voucherId);
        return this.remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteEntries(List<Long> entryIds) {
        if (CollectionUtils.isEmpty(entryIds)) {
            return false;
        }
        return this.removeByIds(entryIds);
    }

    @Override
    public boolean validateEntries(List<Map<String, Object>> entries) {
        if (CollectionUtils.isEmpty(entries)) {
            return false;
        }

        for (Map<String, Object> entry : entries) {
            // 校验必填字段
            if (!entry.containsKey("subjectId") || entry.get("subjectId") == null) {
                log.error("分录科目不能为空");
                return false;
            }

            // 校验金额
            BigDecimal debitAmount = new BigDecimal(entry.getOrDefault("debitAmount", "0").toString());
            BigDecimal creditAmount = new BigDecimal(entry.getOrDefault("creditAmount", "0").toString());

            if (debitAmount.compareTo(BigDecimal.ZERO) <= 0 && creditAmount.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("借贷金额不能同时为0");
                return false;
            }
            if (debitAmount.compareTo(BigDecimal.ZERO) > 0 && creditAmount.compareTo(BigDecimal.ZERO) > 0) {
                log.error("借贷金额不能同时大于0");
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> calculateEntryAmount(List<Map<String, Object>> entries) {
        Map<String, Object> result = new HashMap<>();

        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalOriginalDebit = BigDecimal.ZERO;
        BigDecimal totalOriginalCredit = BigDecimal.ZERO;

        for (Map<String, Object> entry : entries) {
            BigDecimal debitAmount = new BigDecimal(entry.getOrDefault("debitAmount", "0").toString());
            BigDecimal creditAmount = new BigDecimal(entry.getOrDefault("creditAmount", "0").toString());
            BigDecimal exchangeRate = new BigDecimal(entry.getOrDefault("exchangeRate", "1").toString());

            totalDebit = totalDebit.add(debitAmount);
            totalCredit = totalCredit.add(creditAmount);
            totalOriginalDebit = totalOriginalDebit.add(debitAmount.multiply(exchangeRate));
            totalOriginalCredit = totalOriginalCredit.add(creditAmount.multiply(exchangeRate));
        }

        result.put("totalDebit", totalDebit);
        result.put("totalCredit", totalCredit);
        result.put("totalOriginalDebit", totalOriginalDebit);
        result.put("totalOriginalCredit", totalOriginalCredit);
        result.put("isBalanced", totalDebit.compareTo(totalCredit) == 0);
        result.put("difference", totalDebit.subtract(totalCredit).abs());

        return result;
    }

    @Override
    public VoucherEntryEntity getEntryDetail(Long entryId) {
        return this.getById(entryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyEntries(Long sourceVoucherId, Long targetVoucherId, TblStaffUtil currentUser) {
        List<VoucherEntryEntity> sourceEntries = getEntriesByVoucherId(sourceVoucherId);
        if (CollectionUtils.isEmpty(sourceEntries)) {
            return false;
        }

        List<VoucherEntryEntity> targetEntries = new ArrayList<>();
        for (VoucherEntryEntity sourceEntry : sourceEntries) {
            VoucherEntryEntity targetEntry = new VoucherEntryEntity();
            BeanUtils.copyProperties(sourceEntry, targetEntry, "entryId", "createTime", "creator");

            targetEntry.setVoucherId(targetVoucherId);
            targetEntry.setCreator(currentUser.getStaffid().longValue());
            targetEntry.setUpdater(currentUser.getStaffid().longValue());

            targetEntries.add(targetEntry);
        }
        return this.saveBatch(targetEntries);
    }
}