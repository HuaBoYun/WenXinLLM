package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ReconciliationAnalysisQueryParam;
import com.financial.sharing.consolidationReport.dto.ReconciliationDataQueryParam;
import com.financial.sharing.consolidationReport.entity.TblReconciliationAnalysis;
import com.financial.sharing.consolidationReport.entity.TblReconciliationData;
import com.financial.sharing.consolidationReport.mapper.ReconciliationAnalysisMapper;
import com.financial.sharing.consolidationReport.mapper.ReconciliationDataMapper;
import com.financial.sharing.consolidationReport.service.ReconciliationAnalysisService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 对账差异分析Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ReconciliationAnalysisServiceImpl implements ReconciliationAnalysisService {

    @Autowired
    private ReconciliationAnalysisMapper analysisMapper;

    @Autowired
    private ReconciliationDataMapper reconciliationDataMapper;

    @Override
    public PageInfo<TblReconciliationAnalysis> getAnalysisList(ReconciliationAnalysisQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblReconciliationAnalysis> list = analysisMapper.selectAnalysisList(param);
        return new PageInfo<>(list);
    }

    @Override
    public TblReconciliationAnalysis getAnalysisById(String analysisId) {
        return analysisMapper.selectById(analysisId);
    }

    @Override
    public TblReconciliationAnalysis getAnalysisByReconciliationId(String reconciliationId) {
        return analysisMapper.selectByReconciliationId(reconciliationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAnalysis(TblReconciliationAnalysis analysis) {
        String tenantId = UserUtils.getTenantId().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 设置默认值
        analysis.setAnalysisId(UUID.randomUUID().toString().replace("-", ""));
        analysis.setTenantId(tenantId);
        analysis.setCreateUser(userId);
        analysis.setCreateTime(now);
        analysis.setUpdateUser(userId);
        analysis.setUpdateTime(now);

        // 默认状态为待处理
        if (analysis.getStatus() == null || analysis.getStatus().isEmpty()) {
            analysis.setStatus("PENDING");
        }

        analysisMapper.insert(analysis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAnalysis(TblReconciliationAnalysis analysis) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        analysis.setUpdateUser(userId);
        analysis.setUpdateTime(now);

        analysisMapper.updateById(analysis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAnalysis(String analysisId) {
        analysisMapper.deleteById(analysisId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleDifference(String analysisId, String solution, BigDecimal adjustAmount) {
        String userId = UserUtils.getUser().getStaffid().toString();
        String userName = UserUtils.getUser().getRealname();
        Date now = new Date();

        TblReconciliationAnalysis analysis = analysisMapper.selectById(analysisId);
        if (analysis == null) {
            throw new RuntimeException("差异分析不存在");
        }

        analysis.setSolution(solution);
        analysis.setAdjustAmount(adjustAmount);
        analysis.setStatus("PROCESSED");
        analysis.setHandler(userId);
        analysis.setHandlerName(userName);
        analysis.setHandleTime(now);
        analysis.setUpdateUser(userId);
        analysis.setUpdateTime(now);

        analysisMapper.updateById(analysis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeDifference(String analysisId) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        TblReconciliationAnalysis analysis = analysisMapper.selectById(analysisId);
        if (analysis == null) {
            throw new RuntimeException("差异分析不存在");
        }

        analysis.setStatus("CLOSED");
        analysis.setUpdateUser(userId);
        analysis.setUpdateTime(now);

        analysisMapper.updateById(analysis);
    }

    @Override
    public Map<String, Object> getStatistics(String modelId, String period) {
        List<Map<String, Object>> statusList = analysisMapper.countByStatus(modelId, period);
        List<Map<String, Object>> reasonList = analysisMapper.countByReason(modelId, period);

        Map<String, Object> result = new HashMap<>();
        int totalCount = 0;
        int pendingCount = 0;
        int processingCount = 0;
        int processedCount = 0;
        int closedCount = 0;

        for (Map<String, Object> item : statusList) {
            String status = (String) item.get("STATUS");
            int count = ((Number) item.get("COUNT")).intValue();
            totalCount += count;

            if ("PENDING".equals(status)) {
                pendingCount = count;
            } else if ("PROCESSING".equals(status)) {
                processingCount = count;
            } else if ("PROCESSED".equals(status)) {
                processedCount = count;
            } else if ("CLOSED".equals(status)) {
                closedCount = count;
            }
        }

        result.put("totalCount", totalCount);
        result.put("pendingCount", pendingCount);
        result.put("processingCount", processingCount);
        result.put("processedCount", processedCount);
        result.put("closedCount", closedCount);

        // 差异原因统计
        Map<String, Integer> reasonMap = new HashMap<>();
        for (Map<String, Object> item : reasonList) {
            String reason = (String) item.get("DIFF_REASON");
            int count = ((Number) item.get("COUNT")).intValue();
            reasonMap.put(reason, count);
        }
        result.put("reasonMap", reasonMap);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> autoCreateAnalysis(String modelId, String period) {
        String tenantId = UserUtils.getTenantId().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        List<String> errorMessages = new ArrayList<>();

        try {
            // 查询有差异的对账数据
            ReconciliationDataQueryParam param = new ReconciliationDataQueryParam();
            param.setModelId(modelId);
            param.setPeriod(period);
            param.setStatus("DIFF");
            List<TblReconciliationData> diffDataList = reconciliationDataMapper.selectReconciliationList(param);

            if (diffDataList.isEmpty()) {
                result.put("success", true);
                result.put("message", "没有需要分析的差异数据");
                result.put("totalCount", 0);
                result.put("successCount", 0);
                return result;
            }

            List<TblReconciliationAnalysis> analysisList = new ArrayList<>();

            for (TblReconciliationData data : diffDataList) {
                try {
                    // 检查是否已经创建过分析
                    TblReconciliationAnalysis existAnalysis = analysisMapper.selectByReconciliationId(data.getReconciliationId());
                    if (existAnalysis != null) {
                        continue; // 已存在,跳过
                    }

                    // 创建差异分析
                    TblReconciliationAnalysis analysis = new TblReconciliationAnalysis();
                    analysis.setAnalysisId(UUID.randomUUID().toString().replace("-", ""));
                    analysis.setReconciliationId(data.getReconciliationId());
                    analysis.setModelId(modelId);
                    analysis.setPeriod(period);

                    // 自动判断差异原因(简单规则)
                    String diffReason = determineDiffReason(data);
                    analysis.setDiffReason(diffReason);

                    // 生成差异描述
                    String diffDescription = generateDiffDescription(data);
                    analysis.setDiffDescription(diffDescription);

                    analysis.setStatus("PENDING");
                    analysis.setTenantId(tenantId);
                    analysis.setCreateUser(userId);
                    analysis.setCreateTime(now);
                    analysis.setUpdateUser(userId);
                    analysis.setUpdateTime(now);

                    analysisList.add(analysis);
                    successCount++;
                } catch (Exception e) {
                    errorMessages.add("对账数据ID[" + data.getReconciliationId() + "]创建失败: " + e.getMessage());
                }
            }

            // 批量插入
            if (!analysisList.isEmpty()) {
                for (TblReconciliationAnalysis analysis : analysisList) {
                    analysisMapper.insert(analysis);
                }
            }

            result.put("success", true);
            result.put("totalCount", diffDataList.size());
            result.put("successCount", successCount);
            result.put("errorCount", diffDataList.size() - successCount);
            result.put("errorMessages", errorMessages);
            result.put("message", "自动创建完成,共" + diffDataList.size() + "条,成功" + successCount + "条,失败" + (diffDataList.size() - successCount) + "条");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "自动创建失败: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }

    /**
     * 判断差异原因
     */
    private String determineDiffReason(TblReconciliationData data) {
        // 简单规则:根据差异金额的大小判断
        BigDecimal diffAmount = data.getDiffAmount();
        if (diffAmount == null) {
            return "OTHER";
        }

        BigDecimal absAmount = diffAmount.abs();
        // 如果差异金额很小,可能是时间性差异
        if (absAmount.compareTo(new BigDecimal("1000")) < 0) {
            return "TIMING";
        }
        // 否则判断为金额差异
        return "AMOUNT";
    }

    /**
     * 生成差异描述
     */
    private String generateDiffDescription(TblReconciliationData data) {
        StringBuilder sb = new StringBuilder();
        sb.append("甲方公司[").append(data.getCompanyAName()).append("]");
        sb.append("与乙方公司[").append(data.getCompanyBName()).append("]");
        sb.append("在交易类型[").append(getTransactionTypeName(data.getTransactionType())).append("]");
        sb.append("上存在差异,");
        sb.append("甲方金额:").append(data.getAmountA()).append(",");
        sb.append("乙方金额:").append(data.getAmountB()).append(",");
        sb.append("差异金额:").append(data.getDiffAmount());
        return sb.toString();
    }

    /**
     * 获取交易类型名称
     */
    private String getTransactionTypeName(String transactionType) {
        if (transactionType == null) {
            return "未知";
        }
        switch (transactionType) {
            case "RECEIVABLE_PAYABLE":
                return "应收应付";
            case "REVENUE_COST":
                return "收入成本";
            case "INVENTORY":
                return "存货";
            case "INVESTMENT":
                return "投资";
            case "OTHER":
                return "其他";
            default:
                return transactionType;
        }
    }
}

