package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BondIssuanceDTO;
import com.global.treasurer.dto.BondIssuanceQueryDTO;
import com.global.treasurer.entity.TblBondIssuance;
import com.global.treasurer.mapper.BondIssuanceMapper;
import com.global.treasurer.service.BondIssuanceService;
import com.global.treasurer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BondIssuanceServiceImpl implements BondIssuanceService {
    private static final Logger log = LoggerFactory.getLogger(BondIssuanceServiceImpl.class);

    @Autowired
    private BondIssuanceMapper bondIssuanceMapper;

    @Override
    public PageInfo<TblBondIssuance> getIssuanceList(BondIssuanceQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("issuanceNo", queryDTO.getIssuanceNo());
        params.put("bondName", queryDTO.getBondName());
        params.put("bondType", queryDTO.getBondType());
        params.put("issuanceStatus", queryDTO.getIssuanceStatus());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("currencyCode", queryDTO.getCurrencyCode());
        params.put("startDate", queryDTO.getStartDate());
        params.put("endDate", queryDTO.getEndDate());

        log.info("=== Service层查询参数: {} ===", params);

        List<TblBondIssuance> list = bondIssuanceMapper.selectIssuanceList(params);

        log.info("=== Mapper返回结果数量: {} ===", list.size());

        return new PageInfo<>(list);
    }

    @Override
    public TblBondIssuance getIssuanceById(Long issuanceId) {
        TblBondIssuance issuance = bondIssuanceMapper.selectIssuanceById(issuanceId);
        if (issuance == null) {
            throw new ServiceException(404, "债券发行不存在");
        }
        return issuance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBondIssuance saveIssuance(BondIssuanceDTO dto) {
        if (dto.getIssuanceId() == null) {
            // 新增
            TblBondIssuance issuance = new TblBondIssuance();
            BeanUtils.copyProperties(dto, issuance);

            // 手动映射字段名不一致的属性
            issuance.setIssueAmount(dto.getIssuanceAmount());
            issuance.setIssueDate(dto.getIssuanceDate());  // issuanceDate -> issueDate

            // 设置默认值
            if (issuance.getCurrencyCode() == null || issuance.getCurrencyCode().isEmpty()) {
                issuance.setCurrencyCode("CNY");
            }
            if (issuance.getTermUnit() == null || issuance.getTermUnit().isEmpty()) {
                issuance.setTermUnit("YEAR");
            }

            issuance.setBondNo("BI" + System.currentTimeMillis());
            issuance.setBondStatus("DRAFT");
            issuance.setOutstandingAmount(BigDecimal.ZERO);
            issuance.setDeleteFlag(0);
            issuance.setCreatedTime(new Date());
            bondIssuanceMapper.insert(issuance);
            return issuance;
        } else {
            // 更新 - 先查询现有记录，然后只更新非空字段
            TblBondIssuance existing = getIssuanceById(dto.getIssuanceId());
            if (existing == null) {
                throw new ServiceException(404, "债券发行记录不存在");
            }

            // 只更新非空字段
            if (dto.getBondName() != null) existing.setBondName(dto.getBondName());
            if (dto.getBondCode() != null) existing.setBondCode(dto.getBondCode());
            if (dto.getBondType() != null) existing.setBondType(dto.getBondType());
            if (dto.getIssuanceAmount() != null) existing.setIssueAmount(dto.getIssuanceAmount());
            if (dto.getCurrencyCode() != null) existing.setCurrencyCode(dto.getCurrencyCode());
            if (dto.getFaceValue() != null) existing.setFaceValue(dto.getFaceValue());
            if (dto.getCouponRate() != null) existing.setCouponRate(dto.getCouponRate());
            if (dto.getBondTerm() != null) existing.setBondTerm(dto.getBondTerm());
            if (dto.getTermUnit() != null) existing.setTermUnit(dto.getTermUnit());
            if (dto.getPaymentFrequency() != null) existing.setPaymentFrequency(dto.getPaymentFrequency());
            if (dto.getUnderwriter() != null) existing.setUnderwriter(dto.getUnderwriter());
            if (dto.getTrustee() != null) existing.setTrustee(dto.getTrustee());
            if (dto.getRatingAgency() != null) existing.setRatingAgency(dto.getRatingAgency());
            if (dto.getCreditRating() != null) existing.setCreditRating(dto.getCreditRating());
            if (dto.getRatingDate() != null) existing.setRatingDate(dto.getRatingDate());
            if (dto.getRatingOutlook() != null) existing.setRatingOutlook(dto.getRatingOutlook());
            if (dto.getRatingNotes() != null) existing.setRatingNotes(dto.getRatingNotes());
            if (dto.getListingExchange() != null) existing.setListingExchange(dto.getListingExchange());
            if (dto.getListingDate() != null) existing.setListingDate(dto.getListingDate());
            if (dto.getStockCode() != null) existing.setStockCode(dto.getStockCode());
            if (dto.getListingNotes() != null) existing.setListingNotes(dto.getListingNotes());
            if (dto.getRedeemType() != null) existing.setRedeemType(dto.getRedeemType());
            if (dto.getRedeemAmount() != null) existing.setRedeemAmount(dto.getRedeemAmount());
            if (dto.getRedeemDate() != null) existing.setRedeemDate(dto.getRedeemDate());
            if (dto.getRedeemNotes() != null) existing.setRedeemNotes(dto.getRedeemNotes());
            if (dto.getIssuanceDate() != null) existing.setIssueDate(dto.getIssuanceDate());
            if (dto.getMaturityDate() != null) existing.setMaturityDate(dto.getMaturityDate());
            if (dto.getIssuanceStatus() != null) existing.setBondStatus(dto.getIssuanceStatus());

            existing.setUpdatedTime(new Date());
            bondIssuanceMapper.updateById(existing);
            return existing;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIssuance(Long issuanceId) {
        TblBondIssuance issuance = getIssuanceById(issuanceId);
        if (!"DRAFT".equals(issuance.getBondStatus())) {
            throw new ServiceException(400, "只能删除草稿状态的债券发行");
        }
        issuance.setDeleteFlag(1);
        issuance.setUpdatedTime(new Date());
        bondIssuanceMapper.updateById(issuance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteIssuances(List<Long> issuanceIds) {
        bondIssuanceMapper.batchDeleteByIds(issuanceIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long issuanceId) {
        bondIssuanceMapper.updateIssuanceStatus(issuanceId, "PENDING");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long issuanceId, String comments) {
        TblBondIssuance issuance = getIssuanceById(issuanceId);
        issuance.setBondStatus("APPROVED");
        issuance.setUpdatedTime(new Date());
        bondIssuanceMapper.updateById(issuance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long issuanceId, String comments) {
        TblBondIssuance issuance = getIssuanceById(issuanceId);
        issuance.setBondStatus("REJECTED");
        issuance.setUpdatedTime(new Date());
        bondIssuanceMapper.updateById(issuance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmIssuance(Long issuanceId, Map<String, Object> params) {
        TblBondIssuance issuance = getIssuanceById(issuanceId);
        issuance.setBondStatus("ACTIVE");
        issuance.setIssueDate(new Date());
        issuance.setOutstandingAmount(issuance.getIssueAmount());
        issuance.setUpdatedTime(new Date());
        bondIssuanceMapper.updateById(issuance);
    }

    @Override
    public List<TblBondIssuance> getExpiringBonds(Integer days) {
        return bondIssuanceMapper.selectExpiringBonds(days);
    }

    @Override
    public Map<String, Object> getIssuanceSummary(Long companyId) {
        Map<String, Object> dbResult = bondIssuanceMapper.selectIssuanceSummary(companyId);

        if (dbResult == null || dbResult.isEmpty()) {
            Map<String, Object> defaultResult = new HashMap<>();
            defaultResult.put("totalBonds", 0);
            defaultResult.put("outstandingAmount", BigDecimal.ZERO);
            defaultResult.put("averageCouponRate", BigDecimal.ZERO);
            defaultResult.put("averageRating", "N/A");
            return defaultResult;
        }

        // 处理字段名大小写兼容性（达梦数据库可能返回大写字段名）
        Map<String, Object> result = new HashMap<>();

        // 获取债券总数
        Object totalBonds = dbResult.getOrDefault("totalBonds", dbResult.get("TOTALBONDS"));
        if (totalBonds == null) {
            totalBonds = dbResult.getOrDefault("total_bonds", dbResult.get("TOTAL_BONDS"));
        }
        result.put("totalBonds", totalBonds != null ? Integer.parseInt(totalBonds.toString()) : 0);

        // 获取存续金额
        Object outstandingAmount = dbResult.getOrDefault("outstandingAmount", dbResult.get("OUTSTANDINGAMOUNT"));
        if (outstandingAmount == null) {
            outstandingAmount = dbResult.getOrDefault("outstanding_amount", dbResult.get("OUTSTANDING_AMOUNT"));
        }
        result.put("outstandingAmount", outstandingAmount != null ? new BigDecimal(outstandingAmount.toString()) : BigDecimal.ZERO);

        // 获取平均票面利率
        Object avgRate = dbResult.getOrDefault("averageCouponRate", dbResult.get("AVERAGECOUPONRATE"));
        if (avgRate == null) {
            avgRate = dbResult.getOrDefault("average_coupon_rate", dbResult.get("AVERAGE_COUPON_RATE"));
        }
        result.put("averageCouponRate", avgRate != null ? new BigDecimal(avgRate.toString()).setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);

        // 获取平均信用评级
        Object avgRating = dbResult.getOrDefault("averageRating", dbResult.get("AVERAGERATING"));
        if (avgRating == null) {
            avgRating = dbResult.getOrDefault("average_rating", dbResult.get("AVERAGE_RATING"));
        }
        result.put("averageRating", avgRating != null ? avgRating.toString() : "N/A");

        log.debug("债券概览数据处理完成: {}", result);
        return result;
    }

    @Override
    public List<Map<String, Object>> getBondTypeDistribution(Long companyId) {
        List<Map<String, Object>> dbResult = bondIssuanceMapper.selectBondTypeDistribution(companyId);
        List<Map<String, Object>> result = new ArrayList<>();

        log.info("=== 债券类型分布查询结果: {} ===", dbResult);

        // 债券类型映射
        Map<String, String> typeNameMap = new HashMap<>();
        typeNameMap.put("CORPORATE", "企业债券");
        typeNameMap.put("GOVERNMENT", "政府债券");
        typeNameMap.put("CONVERTIBLE", "可转换债券");
        typeNameMap.put("PERPETUAL", "永续债券");
        typeNameMap.put("MTN", "中期票据");
        typeNameMap.put("CP", "短期融资券");
        typeNameMap.put("PPN", "定向工具");

        if (dbResult != null && !dbResult.isEmpty()) {
            for (Map<String, Object> item : dbResult) {
                log.info("=== 处理数据项: {} ===", item);
                Map<String, Object> resultItem = new HashMap<>();

                // 兼容达梦数据库返回大写字段名
                String bondType = (String) item.getOrDefault("bondType", item.get("BOND_TYPE"));
                if (bondType == null) {
                    bondType = (String) item.get("BONDTYPE");
                }

                Object count = item.getOrDefault("count", item.get("COUNT"));
                Object amount = item.getOrDefault("amount", item.get("AMOUNT"));

                log.info("=== 解析字段 - bondType: {}, count: {}, amount: {} ===", bondType, count, amount);

                resultItem.put("name", typeNameMap.getOrDefault(bondType, bondType != null ? bondType : "其他"));
                resultItem.put("value", count != null ? Integer.parseInt(count.toString()) : 0);
                resultItem.put("amount", amount);
                result.add(resultItem);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getIssuanceTrend(Long companyId, Integer months) {
        List<Map<String, Object>> dbResult = bondIssuanceMapper.selectIssuanceTrend(companyId, months);

        if (dbResult == null || dbResult.isEmpty()) {
            return dbResult;
        }

        // 处理字段名大小写兼容性（达梦数据库可能返回大写字段名）
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : dbResult) {
            Map<String, Object> resultItem = new HashMap<>();

            // 获取月份字段（兼容多种大小写）
            String month = (String) item.getOrDefault("month", item.get("MONTH"));
            if (month == null) {
                month = (String) item.get("Month");
            }
            resultItem.put("month", month);

            // 获取发行数量字段
            Object count = item.getOrDefault("issuanceCount", item.get("ISSUANCECOUNT"));
            if (count == null) {
                count = item.getOrDefault("issuancecount", item.get("ISSUANCE_COUNT"));
            }
            resultItem.put("issuanceCount", count != null ? Integer.parseInt(count.toString()) : 0);

            // 获取发行金额字段
            Object amount = item.getOrDefault("issuanceAmount", item.get("ISSUANCEAMOUNT"));
            if (amount == null) {
                amount = item.getOrDefault("issuanceamount", item.get("ISSUANCE_AMOUNT"));
            }
            resultItem.put("issuanceAmount", amount != null ? new BigDecimal(amount.toString()) : BigDecimal.ZERO);

            result.add(resultItem);
        }

        log.debug("债券发行趋势数据处理完成，共{}条记录", result.size());
        return result;
    }

    @Override
    public List<TblBondIssuance> getAllForExport(BondIssuanceQueryDTO queryDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("issuanceNo", queryDTO.getIssuanceNo());
        params.put("bondName", queryDTO.getBondName());
        params.put("bondType", queryDTO.getBondType());
        params.put("issuanceStatus", queryDTO.getIssuanceStatus());
        params.put("companyId", queryDTO.getCompanyId());
        return bondIssuanceMapper.selectAllForExport(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchImportBonds(List<BondIssuanceDTO> bondList, Long companyId, String companyName, Long createdBy) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for (int i = 0; i < bondList.size(); i++) {
            try {
                BondIssuanceDTO dto = bondList.get(i);
                TblBondIssuance issuance = new TblBondIssuance();
                BeanUtils.copyProperties(dto, issuance);

                issuance.setBondNo("BI" + System.currentTimeMillis() + i);
                issuance.setBondStatus("DRAFT");
                issuance.setOutstandingAmount(BigDecimal.ZERO);
                issuance.setDeleteFlag(0);
                issuance.setCompanyId(companyId);
                issuance.setCompanyName(companyName);
                issuance.setCreatedBy(createdBy != null ? String.valueOf(createdBy) : null);
                issuance.setCreatedTime(new Date());

                bondIssuanceMapper.insert(issuance);
                successCount++;
            } catch (Exception e) {
                failCount++;
                errorMessages.add("第" + (i + 2) + "行导入失败: " + e.getMessage());
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("total", successCount + failCount);
        result.put("errorMessages", errorMessages);
        return result;
    }
}

