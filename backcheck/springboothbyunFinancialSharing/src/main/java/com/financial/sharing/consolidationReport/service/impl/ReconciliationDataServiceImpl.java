package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ReconciliationDataQueryParam;
import com.financial.sharing.consolidationReport.entity.TblReconciliationData;
import com.financial.sharing.consolidationReport.mapper.ReconciliationDataMapper;
import com.financial.sharing.consolidationReport.service.ReconciliationDataService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 对账数据Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ReconciliationDataServiceImpl implements ReconciliationDataService {

    @Autowired
    private ReconciliationDataMapper reconciliationDataMapper;

    @Override
    public PageInfo<TblReconciliationData> getReconciliationList(ReconciliationDataQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblReconciliationData> list = reconciliationDataMapper.selectReconciliationList(param);
        return new PageInfo<>(list);
    }

    @Override
    public TblReconciliationData getReconciliationById(String reconciliationId) {
        return reconciliationDataMapper.selectById(reconciliationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReconciliation(TblReconciliationData data) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 设置默认值
        data.setReconciliationId(UUID.randomUUID().toString().replace("-", ""));
        data.setTenantId(tenantId);
        data.setCreateUser(userId);
        data.setCreateTime(now);
        data.setUpdateUser(userId);
        data.setUpdateTime(now);

        // 计算差异金额
        calculateDiffAmount(data);

        // 根据差异金额设置状态
        setStatus(data);

        reconciliationDataMapper.insert(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReconciliation(TblReconciliationData data) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        data.setUpdateUser(userId);
        data.setUpdateTime(now);

        // 计算差异金额
        calculateDiffAmount(data);

        // 根据差异金额设置状态
        setStatus(data);

        reconciliationDataMapper.updateById(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReconciliation(String reconciliationId) {
        reconciliationDataMapper.deleteById(reconciliationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByModelIdAndPeriod(String modelId, String period) {
        reconciliationDataMapper.deleteByModelIdAndPeriod(modelId, period);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchImport(List<TblReconciliationData> dataList, String modelId, String period) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            List<TblReconciliationData> validDataList = new ArrayList<>();

            for (int i = 0; i < dataList.size(); i++) {
                TblReconciliationData data = dataList.get(i);
                try {
                    // 验证必填字段
                    if (data.getCompanyAId() == null || data.getCompanyAId().isEmpty()) {
                        errorMessages.add("第" + (i + 1) + "行:甲方公司ID不能为空");
                        continue;
                    }
                    if (data.getCompanyBId() == null || data.getCompanyBId().isEmpty()) {
                        errorMessages.add("第" + (i + 1) + "行:乙方公司ID不能为空");
                        continue;
                    }
                    if (data.getAmountA() == null) {
                        errorMessages.add("第" + (i + 1) + "行:甲方金额不能为空");
                        continue;
                    }
                    if (data.getAmountB() == null) {
                        errorMessages.add("第" + (i + 1) + "行:乙方金额不能为空");
                        continue;
                    }

                    // 设置默认值
                    data.setReconciliationId(UUID.randomUUID().toString().replace("-", ""));
                    data.setModelId(modelId);
                    data.setPeriod(period);
                    data.setTenantId(tenantId);
                    data.setCreateUser(userId);
                    data.setCreateTime(now);
                    data.setUpdateUser(userId);
                    data.setUpdateTime(now);

                    // 计算差异金额
                    calculateDiffAmount(data);

                    // 根据差异金额设置状态
                    setStatus(data);

                    validDataList.add(data);
                    successCount++;
                } catch (Exception e) {
                    errorMessages.add("第" + (i + 1) + "行:" + e.getMessage());
                }
            }

            // 批量插入
            if (!validDataList.isEmpty()) {
                reconciliationDataMapper.batchInsert(validDataList);
            }

            result.put("success", true);
            result.put("totalCount", dataList.size());
            result.put("successCount", successCount);
            result.put("errorCount", dataList.size() - successCount);
            result.put("errorMessages", errorMessages);
            result.put("message", "导入完成,共" + dataList.size() + "条,成功" + successCount + "条,失败" + (dataList.size() - successCount) + "条");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "导入失败: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> getStatistics(String modelId, String period) {
        List<Map<String, Object>> statusList = reconciliationDataMapper.countByStatus(modelId, period);

        Map<String, Object> result = new HashMap<>();
        int totalCount = 0;
        int pendingCount = 0;
        int matchedCount = 0;
        int diffCount = 0;

        for (Map<String, Object> item : statusList) {
            String status = (String) item.get("STATUS");
            int count = ((Number) item.get("COUNT")).intValue();
            totalCount += count;

            if ("PENDING".equals(status)) {
                pendingCount = count;
            } else if ("MATCHED".equals(status)) {
                matchedCount = count;
            } else if ("DIFF".equals(status)) {
                diffCount = count;
            }
        }

        result.put("totalCount", totalCount);
        result.put("pendingCount", pendingCount);
        result.put("matchedCount", matchedCount);
        result.put("diffCount", diffCount);

        return result;
    }

    /**
     * 计算差异金额
     */
    private void calculateDiffAmount(TblReconciliationData data) {
        if (data.getAmountA() != null && data.getAmountB() != null) {
            data.setDiffAmount(data.getAmountA().subtract(data.getAmountB()));
        } else {
            data.setDiffAmount(BigDecimal.ZERO);
        }
    }

    /**
     * 根据差异金额设置状态
     */
    private void setStatus(TblReconciliationData data) {
        if (data.getDiffAmount() == null) {
            data.setStatus("PENDING");
        } else if (data.getDiffAmount().compareTo(BigDecimal.ZERO) == 0) {
            data.setStatus("MATCHED");
        } else {
            data.setStatus("DIFF");
        }
    }
}

