package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancialLeaseDTO;
import com.global.treasurer.dto.FinancialLeaseQueryDTO;
import com.global.treasurer.entity.TblFinancialLease;
import com.global.treasurer.mapper.FinancialLeaseMapper;
import com.global.treasurer.service.FinancialLeaseService;
import com.global.treasurer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 融资租赁服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@Service
public class FinancialLeaseServiceImpl implements FinancialLeaseService {

    private static final Logger log = LoggerFactory.getLogger(FinancialLeaseServiceImpl.class);

    @Autowired
    private FinancialLeaseMapper financialLeaseMapper;

    @Override
    public PageInfo<TblFinancialLease> getLeaseList(FinancialLeaseQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("leasingType", queryDTO.getLeasingType());
        params.put("applicationStatus", queryDTO.getApplicationStatus());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("currencyCode", queryDTO.getCurrencyCode());
        params.put("leasingCompany", queryDTO.getLeasingCompany());
        params.put("leaseNo", queryDTO.getLeaseNo());
        List<TblFinancialLease> list = financialLeaseMapper.selectLeaseList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblFinancialLease getLeaseById(Long leaseId) {
        TblFinancialLease lease = financialLeaseMapper.selectLeaseById(leaseId);
        if (lease == null) {
            throw new ServiceException(404, "融资租赁不存在");
        }
        return lease;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancialLease saveLease(FinancialLeaseDTO dto) {
        TblFinancialLease lease = new TblFinancialLease();

        // 手动映射字段（前端字段名与数据库字段名不一致）
        // ID：前端使用 id，后端使用 leaseId
        if (dto.getLeaseId() != null) {
            lease.setLeaseId(dto.getLeaseId());
        } else if (dto.getId() != null) {
            lease.setLeaseId(dto.getId());
        }
        lease.setLeaseNo(dto.getLeaseNo());
        lease.setLeaseName(dto.getLeaseName());
        lease.setRemark(dto.getRemark());

        // 资产信息
        lease.setAssetName(dto.getAssetName());
        lease.setAssetValue(dto.getAssetValue());

        // 支付方式
        lease.setPaymentMethod(dto.getPaymentMethod());

        // 租赁期限
        lease.setLeasePeriod(dto.getLeasePeriod());
        lease.setPeriodUnit(dto.getPeriodUnit());

        // 币种：前端使用 currencyCode
        if (dto.getCurrencyCode() != null) {
            lease.setCurrencyCode(dto.getCurrencyCode());
        } else if (dto.getCurrency() != null) {
            lease.setCurrencyCode(dto.getCurrency());
        }

        // 租赁类型：前端使用 leasingType，数据库使用 leaseType
        if (dto.getLeasingType() != null) {
            lease.setLeaseType(dto.getLeasingType());
        } else if (dto.getLeaseType() != null) {
            lease.setLeaseType(dto.getLeaseType());
        }

        // 租赁利率：前端使用 leaseRate（百分比），数据库使用 interestRate（小数）
        if (dto.getLeaseRate() != null) {
            // 前端传的是百分比（如5.5），数据库存储小数（如0.055）
            lease.setInterestRate(dto.getLeaseRate().divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP));
        }

        // 租赁金额：前端使用 leaseAmount
        if (dto.getLeaseAmount() != null) {
            lease.setLeaseAmount(dto.getLeaseAmount());
        } else if (dto.getTotalLeaseAmount() != null) {
            lease.setLeaseAmount(dto.getTotalLeaseAmount());
        }

        // 租赁公司：前端使用 leasingCompany，数据库使用 lessorName
        if (dto.getLeasingCompany() != null) {
            lease.setLessorName(dto.getLeasingCompany());
        } else if (dto.getLessorCompany() != null) {
            lease.setLessorName(dto.getLessorCompany());
        }

        // 公司信息
        if (dto.getCompanyId() != null && !dto.getCompanyId().isEmpty()) {
            try {
                lease.setCompanyId(Long.parseLong(dto.getCompanyId()));
            } catch (NumberFormatException e) {
                log.warn("公司ID格式错误: {}", dto.getCompanyId());
            }
        }
        if (dto.getCompanyName() != null) {
            lease.setCompanyName(dto.getCompanyName());
        }

        // 日期：前端使用 startDate/endDate（字符串格式 yyyy-MM-dd）
        if (dto.getStartDate() != null && !dto.getStartDate().isEmpty()) {
            try {
                lease.setStartDate(java.sql.Date.valueOf(dto.getStartDate()));
            } catch (Exception e) {
                log.warn("开始日期格式错误: {}", dto.getStartDate());
            }
        } else if (dto.getLeaseStartDate() != null) {
            lease.setStartDate(java.sql.Date.valueOf(dto.getLeaseStartDate()));
        }

        if (dto.getEndDate() != null && !dto.getEndDate().isEmpty()) {
            try {
                lease.setEndDate(java.sql.Date.valueOf(dto.getEndDate()));
            } catch (Exception e) {
                log.warn("结束日期格式错误: {}", dto.getEndDate());
            }
        } else if (dto.getLeaseEndDate() != null) {
            lease.setEndDate(java.sql.Date.valueOf(dto.getLeaseEndDate()));
        }

        // 状态：前端使用 applicationStatus，数据库使用 leaseStatus
        if (dto.getApplicationStatus() != null) {
            lease.setLeaseStatus(dto.getApplicationStatus());
        } else if (dto.getLeaseStatus() != null) {
            lease.setLeaseStatus(dto.getLeaseStatus());
        }

        // 判断是新增还是更新：检查 leaseId 和 id
        boolean isNew = (dto.getLeaseId() == null && dto.getId() == null);

        if (isNew) {
            // 新增
            lease.setLeaseNo("FL" + System.currentTimeMillis());
            lease.setLeaseStatus("PENDING");
            lease.setDeleteFlag(0);
            lease.setCreatedTime(new Date());
            lease.setCreatedBy("system");
            // 设置默认未偿还金额等于租赁金额
            if (lease.getLeaseAmount() != null) {
                lease.setOutstandingAmount(lease.getLeaseAmount());
            }
            financialLeaseMapper.insert(lease);
            log.info("新增融资租赁成功, leaseNo: {}, leaseType: {}, lessorName: {}, leaseAmount: {}",
                    lease.getLeaseNo(), lease.getLeaseType(), lease.getLessorName(), lease.getLeaseAmount());
        } else {
            // 更新
            lease.setUpdatedTime(new Date());
            lease.setUpdatedBy("system");
            financialLeaseMapper.updateById(lease);
            log.info("更新融资租赁成功, leaseId: {}", lease.getLeaseId());
        }
        return lease;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLease(Long leaseId) {
        TblFinancialLease lease = getLeaseById(leaseId);
        if (!"PENDING".equals(lease.getApplicationStatus()) && !"REJECTED".equals(lease.getApplicationStatus())) {
            throw new ServiceException(400, "只能删除待提交或已拒绝状态的融资租赁");
        }
        lease.setDeleteFlag(1);
        lease.setUpdatedTime(new Date());
        financialLeaseMapper.updateById(lease);
        log.info("删除融资租赁成功, leaseId: {}", leaseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteLeases(List<Long> leaseIds) {
        financialLeaseMapper.batchDeleteByIds(leaseIds);
        log.info("批量删除融资租赁成功, count: {}", leaseIds.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long leaseId) {
        TblFinancialLease lease = getLeaseById(leaseId);
        if (!"PENDING".equals(lease.getApplicationStatus())) {
            throw new ServiceException(400, "只能提交待提交状态的融资租赁");
        }
        financialLeaseMapper.updateLeaseStatus(leaseId, "SUBMITTED");
        log.info("提交融资租赁审批成功, leaseId: {}", leaseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long leaseId, String comments) {
        TblFinancialLease lease = getLeaseById(leaseId);
        if (!"SUBMITTED".equals(lease.getApplicationStatus())) {
            throw new ServiceException(400, "只能审批已提交状态的融资租赁");
        }
        lease.setApplicationStatus("APPROVED");
        lease.setUpdatedTime(new Date());
        financialLeaseMapper.updateById(lease);
        log.info("审批通过融资租赁, leaseId: {}", leaseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long leaseId, String comments) {
        TblFinancialLease lease = getLeaseById(leaseId);
        if (!"SUBMITTED".equals(lease.getApplicationStatus())) {
            throw new ServiceException(400, "只能拒绝已提交状态的融资租赁");
        }
        lease.setApplicationStatus("REJECTED");
        lease.setUpdatedTime(new Date());
        financialLeaseMapper.updateById(lease);
        log.info("拒绝融资租赁, leaseId: {}", leaseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activateLease(Long leaseId) {
        TblFinancialLease lease = getLeaseById(leaseId);
        lease.setApplicationStatus("ACTIVE");
        lease.setUpdatedTime(new Date());
        financialLeaseMapper.updateById(lease);
        log.info("激活融资租赁, leaseId: {}", leaseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void terminateLease(Long leaseId, String reason) {
        TblFinancialLease lease = getLeaseById(leaseId);
        lease.setApplicationStatus("TERMINATED");
        lease.setUpdatedTime(new Date());
        financialLeaseMapper.updateById(lease);
        log.info("终止融资租赁, leaseId: {}", leaseId);
    }

    @Override
    public List<TblFinancialLease> getExpiringLeases(Integer days) {
        return financialLeaseMapper.selectExpiringLeases(days);
    }

    @Override
    public Map<String, Object> getLeaseSummary(Long companyId) {
        return financialLeaseMapper.selectLeaseSummary(companyId);
    }

    @Override
    public Map<String, Object> getOverviewStatistics() {
        try {
            Map<String, Object> dbResult = financialLeaseMapper.selectOverviewStatistics();
            Map<String, Object> result = new HashMap<>();

            if (dbResult == null || dbResult.isEmpty()) {
                result.put("totalApplications", 0);
                result.put("outstandingAmount", BigDecimal.ZERO);
                result.put("averageRate", BigDecimal.ZERO);
                result.put("approvalRate", BigDecimal.ZERO);
            } else {
                // 兼容达梦数据库大写列名
                result.put("totalApplications", getMapValue(dbResult, "totalApplications", "TOTALAPPLICATIONS", 0));
                result.put("outstandingAmount", getMapValue(dbResult, "outstandingAmount", "OUTSTANDINGAMOUNT", BigDecimal.ZERO));
                result.put("averageRate", getMapValue(dbResult, "averageRate", "AVERAGERATE", BigDecimal.ZERO));
                result.put("approvalRate", getMapValue(dbResult, "approvalRate", "APPROVALRATE", BigDecimal.ZERO));
            }

            // 金额转换为万元
            Object outstandingAmount = result.get("outstandingAmount");
            if (outstandingAmount != null && !BigDecimal.ZERO.equals(outstandingAmount)) {
                BigDecimal amount = new BigDecimal(outstandingAmount.toString());
                result.put("outstandingAmount", amount.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP));
            }
            return result;
        } catch (Exception e) {
            log.error("获取统计概览数据失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("totalApplications", 0);
            result.put("outstandingAmount", BigDecimal.ZERO);
            result.put("averageRate", BigDecimal.ZERO);
            result.put("approvalRate", BigDecimal.ZERO);
            return result;
        }
    }

    /**
     * 从Map中获取值，兼容大小写列名
     */
    private Object getMapValue(Map<String, Object> map, String lowerKey, String upperKey, Object defaultValue) {
        Object value = map.get(lowerKey);
        if (value == null) {
            value = map.get(upperKey);
        }
        return value != null ? value : defaultValue;
    }

    @Override
    public List<Map<String, Object>> getTypeDistribution() {
        try {
            List<Map<String, Object>> dbResult = financialLeaseMapper.selectTypeDistribution();
            List<Map<String, Object>> result = new ArrayList<>();

            if (dbResult == null || dbResult.isEmpty()) {
                // 返回默认数据
                Map<String, Object> item1 = new HashMap<>();
                item1.put("name", "直接租赁");
                item1.put("value", 0);
                result.add(item1);
            } else {
                // 转换数据格式（达梦数据库返回大写列名）
                for (Map<String, Object> item : dbResult) {
                    Map<String, Object> newItem = new HashMap<>();
                    // 兼容大写和小写列名
                    String type = item.get("NAME") != null ? (String) item.get("NAME") : (String) item.get("name");
                    Object value = item.get("VALUE") != null ? item.get("VALUE") : item.get("value");

                    // 转换类型代码为中文名称
                    newItem.put("name", getLeaseTypeName(type));
                    newItem.put("value", value);
                    result.add(newItem);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("获取租赁类型分布统计失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getTrendStatistics(String period) {
        try {
            // 根据周期计算开始日期
            Calendar calendar = Calendar.getInstance();
            Date endDate = calendar.getTime();

            int months = 12; // 默认1年
            if ("6M".equals(period)) {
                months = 6;
            } else if ("2Y".equals(period)) {
                months = 24;
            }
            calendar.add(Calendar.MONTH, -months);
            Date startDate = calendar.getTime();

            List<Map<String, Object>> trendData = financialLeaseMapper.selectTrendStatistics(startDate, endDate);

            // 构建返回结果
            List<String> monthLabels = new ArrayList<>();
            List<Integer> applicationData = new ArrayList<>();
            List<BigDecimal> amountData = new ArrayList<>();

            if (trendData != null) {
                for (Map<String, Object> item : trendData) {
                    // 兼容达梦数据库大写列名
                    String month = item.get("month") != null ? (String) item.get("month") : (String) item.get("MONTH");
                    Object appCount = item.get("applicationCount") != null ? item.get("applicationCount") : item.get("APPLICATIONCOUNT");
                    Object amount = item.get("totalAmount") != null ? item.get("totalAmount") : item.get("TOTALAMOUNT");

                    if (month != null) {
                        monthLabels.add(month);
                    }
                    if (appCount != null) {
                        applicationData.add(((Number) appCount).intValue());
                    } else {
                        applicationData.add(0);
                    }
                    if (amount != null) {
                        BigDecimal amountValue = new BigDecimal(amount.toString());
                        amountData.add(amountValue.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP));
                    } else {
                        amountData.add(BigDecimal.ZERO);
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("months", monthLabels);
            result.put("applicationData", applicationData);
            result.put("amountData", amountData);
            return result;
        } catch (Exception e) {
            log.error("获取租赁申请趋势统计失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("months", new ArrayList<>());
            result.put("applicationData", new ArrayList<>());
            result.put("amountData", new ArrayList<>());
            return result;
        }
    }

    private String getLeaseTypeName(String type) {
        if (type == null) return "未知";
        switch (type) {
            // 新的类型代码（匹配实际数据库数据）
            case "DIRECT": return "直接租赁";
            case "LEASEBACK": return "售后回租";
            case "LEVERAGED": return "杠杆租赁";
            case "OPERATING": return "经营租赁";
            // 旧的类型代码（兼容）
            case "FINANCE_LEASE": return "融资租赁";
            case "OPERATING_LEASE": return "经营租赁";
            case "SALE_LEASEBACK": return "售后回租";
            default: return type;
        }
    }

    @Override
    public List<TblFinancialLease> getLeaseListForExport(FinancialLeaseQueryDTO queryDTO) {
        Map<String, Object> params = new HashMap<>();
        if (queryDTO != null) {
            params.put("leasingType", queryDTO.getLeasingType());
            params.put("applicationStatus", queryDTO.getApplicationStatus());
            params.put("companyId", queryDTO.getCompanyId());
            params.put("currencyCode", queryDTO.getCurrencyCode());
            params.put("leasingCompany", queryDTO.getLeasingCompany());
            params.put("leaseNo", queryDTO.getLeaseNo());
        }
        // 不使用分页，直接查询全部数据
        return financialLeaseMapper.selectLeaseList(params);
    }
}

