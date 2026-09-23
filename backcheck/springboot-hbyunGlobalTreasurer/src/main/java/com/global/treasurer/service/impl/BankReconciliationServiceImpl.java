package com.global.treasurer.service.impl;

import com.global.treasurer.entity.BankReconciliation;
import com.global.treasurer.mapper.BankReconciliationMapper;
import com.global.treasurer.service.BankReconciliationService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 银行对账服务实现
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@Service
public class BankReconciliationServiceImpl implements BankReconciliationService {
    @Resource
    private BankReconciliationMapper bankReconciliationMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> getReconciliationPage(Map<String, Object> param) {
        try {
            // 安全获取分页参数,设置默认值
            int pageNum = 1;
            int pageSize = 10;

            if (param != null) {
                if (param.get("pageNum") != null) {
                    try {
                        pageNum = Integer.parseInt(param.get("pageNum").toString());
                    } catch (NumberFormatException e) {
                        // 使用默认值
                    }
                }
                if (param.get("pageSize") != null) {
                    try {
                        pageSize = Integer.parseInt(param.get("pageSize").toString());
                    } catch (NumberFormatException e) {
                        // 使用默认值
                    }
                }
                // 前端传的是limit,需要转换为pageSize
                if (param.get("limit") != null) {
                    try {
                        pageSize = Integer.parseInt(param.get("limit").toString());
                    } catch (NumberFormatException e) {
                        // 使用默认值
                    }
                }
            }

            // 使用PageHelper分页
            com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);

            List<BankReconciliation> list = bankReconciliationMapper.selectPage(param);

            // PageInfo格式
            com.github.pagehelper.PageInfo<BankReconciliation> pageInfo = new com.github.pagehelper.PageInfo<>(list);

            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("total", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // 返回空数据而不是抛出异常
            Map<String, Object> result = new HashMap<>();
            result.put("list", new ArrayList<>());
            result.put("total", 0);
            result.put("pageNo", 1);
            result.put("pageSize", 10);
            return result;
        }
    }

    @Override
    public BankReconciliation getReconciliationById(Long reconciliationId) {
        return bankReconciliationMapper.selectById(reconciliationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createReconciliation(BankReconciliation reconciliation) {
        // 获取当前用户信息
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        // 生成对账单期次号
        String periodNo = generateReconciliationNo(reconciliation.getOrgId());
        reconciliation.setReconciliationPeriod(periodNo);
        reconciliation.setReconciliationStatus("PENDING");
        reconciliation.setDeleteFlag(0);
        // BigDecimal转Long
        reconciliation.setCreatedBy(loginStaff.getStaffid().longValue());
        reconciliation.setCreatedTime(new Date());

        return bankReconciliationMapper.insert(reconciliation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateReconciliation(BankReconciliation reconciliation) {
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        // BigDecimal转Long
        reconciliation.setUpdatedBy(loginStaff.getStaffid().longValue());
        reconciliation.setUpdatedTime(new Date());

        return bankReconciliationMapper.updateById(reconciliation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Long> ids) {
        return bankReconciliationMapper.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeReconciliation(Long reconciliationId, String executeByName) throws Exception {
        // 更新对账状态为"已完成"
        BankReconciliation reconciliation = new BankReconciliation();
        reconciliation.setReconciliationId(reconciliationId);
        reconciliation.setReconciliationStatus("COMPLETED");
        reconciliation.setUpdatedTime(new Date());

        return bankReconciliationMapper.updateById(reconciliation);
    }

    @Override
    public String generateReconciliationNo(Long orgId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        // 简化版：实际应使用序列或雪花算法
        return "REC" + dateStr + String.format("%04d", new Random().nextInt(10000));
    }

    @Override
    public Map<String, Object> getReconciliationOverview(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 获取orgId
        Long orgId = params.get("orgId") != null ? Long.parseLong(params.get("orgId").toString()) : null;

        // 构建查询条件
        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("orgId", orgId);
        queryParam.put("deleteFlag", 0);

        // 查询所有数据(不分页)
        com.github.pagehelper.PageHelper.startPage(1, Integer.MAX_VALUE);
        List<BankReconciliation> allList = bankReconciliationMapper.selectPage(queryParam);
        com.github.pagehelper.PageInfo<BankReconciliation> pageInfo = new com.github.pagehelper.PageInfo<>(allList);

        // 统计数据
        int totalReconciliations = (int) pageInfo.getTotal();
        int matchedRecords = 0;
        int unmatchedRecords = 0;
        java.math.BigDecimal totalDifferenceAmount = java.math.BigDecimal.ZERO;

        for (BankReconciliation item : allList) {
            // 统计匹配状态
            if ("MATCHED".equals(item.getMatchStatus())) {
                matchedRecords++;
            } else if ("UNMATCHED".equals(item.getMatchStatus())) {
                unmatchedRecords++;
            }

            // 累加差异金额
            if (item.getDifferenceAmount() != null) {
                totalDifferenceAmount = totalDifferenceAmount.add(item.getDifferenceAmount());
            }
        }

        // 计算匹配率
        double matchRate = 0.0;
        if (totalReconciliations > 0) {
            matchRate = (matchedRecords * 100.0) / totalReconciliations;
        }

        // 组装结果
        result.put("totalReconciliations", totalReconciliations);
        result.put("matchedRecords", matchedRecords);
        result.put("unmatchedRecords", unmatchedRecords);
        result.put("matchRate", matchRate);
        result.put("differenceAmount", totalDifferenceAmount);

        return result;
    }
}
