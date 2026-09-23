package com.financial.sharing.budgetControl.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.budgetControl.dto.BatchReleaseRequest;
import com.financial.sharing.budgetControl.dto.BatchTransferRequest;
import com.financial.sharing.budgetControl.dto.BudgetControlRequest;
import com.financial.sharing.budgetControl.dto.BudgetControlResponse;
import com.financial.sharing.budgetControl.dto.BudgetOccupancyQueryParam;
import com.financial.sharing.budgetControl.dto.BudgetReleaseRequest;
import com.financial.sharing.budgetControl.dto.BudgetTransferRequest;
import com.financial.sharing.budgetControl.dto.ReleaseRecordQueryParam;
import com.financial.sharing.budgetControl.dto.TransferRecordQueryParam;
import com.financial.sharing.budgetControl.entity.TblControlRule;
import com.financial.sharing.budgetControl.entity.TblExecutionRecord;
import com.financial.sharing.budgetControl.mapper.ControlRuleMapper;
import com.financial.sharing.budgetControl.mapper.ExecutionRecordMapper;
import com.financial.sharing.budgetControl.service.BudgetControlService;
import com.financial.sharing.budgetPlanning.entity.TblBudgetExecution;
import com.financial.sharing.budgetPlanning.mapper.BudgetExecutionMapper;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 预算控制Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class BudgetControlServiceImpl implements BudgetControlService {

    @Autowired
    private ControlRuleMapper controlRuleMapper;

    @Autowired
    private ExecutionRecordMapper executionRecordMapper;

    @Autowired
    private BudgetExecutionMapper budgetExecutionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetControlResponse checkBudget(BudgetControlRequest request) {
        BudgetControlResponse response = new BudgetControlResponse();
        
        try {
            // 1. 查找适用的控制规则
            TblControlRule rule = findApplicableRule(request);
            if (rule == null || !"Y".equals(rule.getIsEnabled())) {
                // 没有规则或规则未启用，直接通过
                response.setControlResult("PASS");
                response.setControlMessage("无适用的控制规则");
                return response;
            }

            // 2. 查询预算执行情况
            TblBudgetExecution execution = queryBudgetExecution(request);
            if (execution == null) {
                response.setControlResult("BLOCK");
                response.setControlMessage("未找到预算数据");
                return response;
            }

            // 3. 计算可用预算
            BigDecimal budgetAmount = execution.getAdjustedAmount() != null ? 
                execution.getAdjustedAmount() : execution.getBudgetAmount();
            BigDecimal usedAmount = execution.getOccupiedAmount() != null ? 
                execution.getOccupiedAmount() : BigDecimal.ZERO;
            BigDecimal availableAmount = budgetAmount.subtract(usedAmount);

            response.setBudgetAmount(budgetAmount);
            response.setUsedAmount(usedAmount);
            response.setAvailableAmount(availableAmount);

            // 4. 执行控制检查
            String controlResult = performControl(rule, request.getApplyAmount(), availableAmount, budgetAmount);
            response.setControlResult(controlResult);

            // 5. 生成控制消息
            String message = generateControlMessage(controlResult, request.getApplyAmount(), availableAmount, budgetAmount);
            response.setControlMessage(message);

            // 6. 判断是否需要审批
            if ("APPROVE".equals(controlResult)) {
                response.setNeedApproval(true);
                response.setApprovalWorkflowId(rule.getApprovalWorkflowId());
            } else {
                response.setNeedApproval(false);
            }

            // 7. 记录执行日志
            String recordId = saveExecutionRecord(request, rule, response);
            response.setRecordId(recordId);

            return response;
        } catch (Exception e) {
            log.error("预算控制检查失败", e);
            response.setControlResult("ERROR");
            response.setControlMessage("预算控制检查失败：" + e.getMessage());
            return response;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean occupyBudget(BudgetControlRequest request) {
        try {
            // 1. 执行预算控制检查
            BudgetControlResponse checkResult = checkBudget(request);
            
            // 2. 如果控制结果是阻止，则不允许占用
            if ("BLOCK".equals(checkResult.getControlResult())) {
                return MyJsonBean.errorData(checkResult.getControlMessage());
            }

            // 3. 更新预算执行表的占用金额
            TblBudgetExecution execution = queryBudgetExecution(request);
            if (execution != null) {
                BigDecimal currentOccupied = execution.getOccupiedAmount() != null ? 
                    execution.getOccupiedAmount() : BigDecimal.ZERO;
                execution.setOccupiedAmount(currentOccupied.add(request.getApplyAmount()));
                execution.setAvailableAmount(execution.getAvailableAmount().subtract(request.getApplyAmount()));
                execution.setLastUpdateTime(new Date());
                budgetExecutionMapper.updateById(execution);
            }

            return MyJsonBean.successData("预算占用成功", checkResult);
        } catch (Exception e) {
            log.error("预算占用失败", e);
            return MyJsonBean.errorData("预算占用失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean releaseBudget(BudgetControlRequest request) {
        try {
            // 更新预算执行表的占用金额
            TblBudgetExecution execution = queryBudgetExecution(request);
            if (execution != null) {
                BigDecimal currentOccupied = execution.getOccupiedAmount() != null ? 
                    execution.getOccupiedAmount() : BigDecimal.ZERO;
                BigDecimal newOccupied = currentOccupied.subtract(request.getApplyAmount());
                if (newOccupied.compareTo(BigDecimal.ZERO) < 0) {
                    newOccupied = BigDecimal.ZERO;
                }
                execution.setOccupiedAmount(newOccupied);
                execution.setAvailableAmount(execution.getAvailableAmount().add(request.getApplyAmount()));
                execution.setLastUpdateTime(new Date());
                budgetExecutionMapper.updateById(execution);
            }

            // 记录执行日志
            saveReleaseRecord(request);

            return MyJsonBean.successData("预算释放成功");
        } catch (Exception e) {
            log.error("预算释放失败", e);
            return MyJsonBean.errorData("预算释放失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean transferBudget(BudgetControlRequest fromRequest, BudgetControlRequest toRequest) {
        try {
            // 1. 从源预算释放
            MyJsonBean releaseResult = releaseBudget(fromRequest);
            if (releaseResult.getCode() != 1) {
                return releaseResult;
            }

            // 2. 向目标预算占用
            MyJsonBean occupyResult = occupyBudget(toRequest);
            if (occupyResult.getCode() != 1) {
                // 如果占用失败，回滚释放操作
                occupyBudget(fromRequest);
                return occupyResult;
            }

            return MyJsonBean.successData("预算转移成功");
        } catch (Exception e) {
            log.error("预算转移失败", e);
            return MyJsonBean.errorData("预算转移失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryOccupancy(String bizOrgId, String subjectCode, String period, String orgId) {
        try {
            LambdaQueryWrapper<TblBudgetExecution> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblBudgetExecution::getBizOrgId, bizOrgId);
            wrapper.eq(TblBudgetExecution::getSubjectCode, subjectCode);
            wrapper.eq(TblBudgetExecution::getPeriod, period);
            wrapper.eq(TblBudgetExecution::getOrgId, orgId);

            TblBudgetExecution execution = budgetExecutionMapper.selectOne(wrapper);
            if (execution == null) {
                return MyJsonBean.errorData("未找到预算数据");
            }

            return MyJsonBean.successData(execution);
        } catch (Exception e) {
            log.error("查询预算占用失败", e);
            return MyJsonBean.errorData("查询预算占用失败：" + e.getMessage());
        }
    }

    /**
     * 查找适用的控制规则
     */
    private TblControlRule findApplicableRule(BudgetControlRequest request) {
        try {
            LambdaQueryWrapper<TblControlRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblControlRule::getOrgId, request.getOrgId());
            wrapper.eq(TblControlRule::getIsEnabled, "Y");

            // 根据控制级别查找规则
            wrapper.and(w -> w
                .eq(TblControlRule::getControlLevel, "SUBJECT")
                .or()
                .eq(TblControlRule::getControlLevel, "ORG")
                .or()
                .eq(TblControlRule::getControlLevel, "CUSTOM")
            );

            // 按优先级排序,取第一条
            wrapper.orderByDesc(TblControlRule::getPriority);
            wrapper.last("LIMIT 1");

            List<TblControlRule> rules = controlRuleMapper.selectList(wrapper);
            return rules.isEmpty() ? null : rules.get(0);
        } catch (Exception e) {
            log.error("查找控制规则失败", e);
            return null;
        }
    }

    /**
     * 查询预算执行数据
     */
    private TblBudgetExecution queryBudgetExecution(BudgetControlRequest request) {
        try {
            LambdaQueryWrapper<TblBudgetExecution> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblBudgetExecution::getBizOrgId, request.getBizOrgId());
            wrapper.eq(TblBudgetExecution::getSubjectCode, request.getSubjectCode());
            wrapper.eq(TblBudgetExecution::getPeriod, request.getPeriod());
            wrapper.eq(TblBudgetExecution::getOrgId, request.getOrgId());

            return budgetExecutionMapper.selectOne(wrapper);
        } catch (Exception e) {
            log.error("查询预算执行数据失败", e);
            return null;
        }
    }

    /**
     * 执行控制检查
     */
    private String performControl(TblControlRule rule, BigDecimal applyAmount,
                                  BigDecimal availableAmount, BigDecimal budgetAmount) {
        // 1. 检查可用预算是否充足
        if (availableAmount.compareTo(applyAmount) < 0) {
            // 可用预算不足
            String controlType = rule.getControlType();

            if ("RIGID".equals(controlType)) {
                // 刚性控制：直接阻止
                return "BLOCK";
            } else if ("FLEXIBLE".equals(controlType)) {
                // 弹性控制：需要审批
                return "APPROVE";
            } else if ("WARNING".equals(controlType)) {
                // 预警控制：仅警告
                return "WARN";
            }
        }

        // 2. 检查是否达到预警阈值
        if (rule.getWarningRatio() != null) {
            BigDecimal usedAmount = budgetAmount.subtract(availableAmount);
            BigDecimal usedRatio = usedAmount.divide(budgetAmount, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));

            if (usedRatio.compareTo(rule.getWarningRatio()) >= 0) {
                return "WARN";
            }
        }

        return "PASS";
    }

    /**
     * 生成控制消息
     */
    private String generateControlMessage(String controlResult, BigDecimal applyAmount,
                                         BigDecimal availableAmount, BigDecimal budgetAmount) {
        StringBuilder message = new StringBuilder();

        switch (controlResult) {
            case "PASS":
                message.append("预算控制检查通过。");
                break;
            case "BLOCK":
                message.append("预算不足，操作被阻止！");
                break;
            case "WARN":
                message.append("预算即将超支，请注意！");
                break;
            case "APPROVE":
                message.append("预算不足，需要审批。");
                break;
            default:
                message.append("预算控制检查完成。");
        }

        message.append(String.format(" 申请金额：%.2f，可用预算：%.2f，预算总额：%.2f",
            applyAmount, availableAmount, budgetAmount));

        return message.toString();
    }

    /**
     * 保存执行记录
     */
    private String saveExecutionRecord(BudgetControlRequest request, TblControlRule rule,
                                      BudgetControlResponse response) {
        try {
            TblExecutionRecord record = new TblExecutionRecord();
            record.setRuleId(rule.getRuleId());
            record.setSourceSystem(request.getSourceSystem());
            record.setSourceDocType(request.getSourceDocType());
            record.setSourceDocId(request.getSourceDocId());
            record.setSourceDocCode(request.getSourceDocCode());
            record.setOrgId(request.getOrgId());
            record.setSubjectCode(request.getSubjectCode());
            record.setPeriod(request.getPeriod());
            record.setDimensionValues(JSON.toJSONString(request.getDimensionValues()));
            record.setApplyAmount(request.getApplyAmount());
            record.setBudgetAmount(response.getBudgetAmount());
            record.setUsedAmount(response.getUsedAmount());
            record.setAvailableAmount(response.getAvailableAmount());
            record.setControlResult(response.getControlResult());
            record.setControlMessage(response.getControlMessage());
            record.setExecuteTime(new Date());
            record.setOrgId(request.getOrgId());
            record.setCreateUser(request.getOperateUser());
            record.setCreateTime(new Date());

            executionRecordMapper.insert(record);
            return record.getRecordId();
        } catch (Exception e) {
            log.error("保存执行记录失败", e);
            return null;
        }
    }

    /**
     * 保存释放记录
     */
    private void saveReleaseRecord(BudgetControlRequest request) {
        try {
            TblExecutionRecord record = new TblExecutionRecord();
            record.setSourceSystem(request.getSourceSystem());
            record.setSourceDocId(request.getSourceDocId());
            record.setOrgId(request.getOrgId());
            record.setSubjectCode(request.getSubjectCode());
            record.setPeriod(request.getPeriod());
            record.setApplyAmount(request.getApplyAmount());
            record.setControlResult("RELEASE");
            record.setControlMessage("预算释放成功");
            record.setExecuteTime(new Date());
            record.setOrgId(request.getOrgId());
            record.setCreateUser(request.getOperateUser());
            record.setCreateTime(new Date());

            executionRecordMapper.insert(record);
        } catch (Exception e) {
            log.error("保存释放记录失败", e);
        }
    }

    @Override
    public MyJsonBean queryOccupancyPage(BudgetOccupancyQueryParam param) {
        try {
            LambdaQueryWrapper<TblBudgetExecution> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblBudgetExecution::getOrgId, param.getOrgId());

            // 添加查询条件
            if (StringUtils.isNotBlank(param.getModelId())) {
                wrapper.eq(TblBudgetExecution::getModelId, param.getModelId());
            }
            if (StringUtils.isNotBlank(param.getBizOrgId())) {
                wrapper.eq(TblBudgetExecution::getBizOrgId, param.getBizOrgId());
            }
            if (StringUtils.isNotBlank(param.getOrgCode())) {
                wrapper.like(TblBudgetExecution::getOrgCode, param.getOrgCode());
            }
            if (StringUtils.isNotBlank(param.getOrgName())) {
                wrapper.like(TblBudgetExecution::getOrgName, param.getOrgName());
            }
            if (StringUtils.isNotBlank(param.getSubjectCode())) {
                wrapper.like(TblBudgetExecution::getSubjectCode, param.getSubjectCode());
            }
            if (StringUtils.isNotBlank(param.getSubjectName())) {
                wrapper.like(TblBudgetExecution::getSubjectName, param.getSubjectName());
            }
            if (StringUtils.isNotBlank(param.getPeriod())) {
                wrapper.eq(TblBudgetExecution::getPeriod, param.getPeriod());
            }
            if (StringUtils.isNotBlank(param.getBudgetYear())) {
                wrapper.eq(TblBudgetExecution::getBudgetYear, param.getBudgetYear());
            }
            // TODO: TblBudgetExecution没有projectCode和projectName字段，暂时注释
            // if (StringUtils.isNotBlank(param.getProjectCode())) {
            //     wrapper.like(TblBudgetExecution::getProjectCode, param.getProjectCode());
            // }
            // if (StringUtils.isNotBlank(param.getProjectName())) {
            //     wrapper.like(TblBudgetExecution::getProjectName, param.getProjectName());
            // }
            if (StringUtils.isNotBlank(param.getStatus())) {
                wrapper.eq(TblBudgetExecution::getStatus, param.getStatus());
            }
            if (StringUtils.isNotBlank(param.getWarningLevel())) {
                wrapper.eq(TblBudgetExecution::getWarningLevel, param.getWarningLevel());
            }

            // 期间范围查询
            if (StringUtils.isNotBlank(param.getStartPeriod())) {
                wrapper.ge(TblBudgetExecution::getPeriod, param.getStartPeriod());
            }
            if (StringUtils.isNotBlank(param.getEndPeriod())) {
                wrapper.le(TblBudgetExecution::getPeriod, param.getEndPeriod());
            }

            // 占用率范围查询
            if (StringUtils.isNotBlank(param.getMinOccupancyRate())) {
                // 计算占用率 = 占用金额 / 预算金额 * 100
                // 这里需要在查询后过滤，因为占用率是计算字段
            }

            wrapper.orderByDesc(TblBudgetExecution::getPeriod);
            wrapper.orderByAsc(TblBudgetExecution::getOrgCode);
            wrapper.orderByAsc(TblBudgetExecution::getSubjectCode);

            Page<TblBudgetExecution> page = new Page<>(param.getPageNumber(), param.getPageSize());
            IPage<TblBudgetExecution> result = budgetExecutionMapper.selectPage(page, wrapper);

            // 计算占用率并过滤
            List<TblBudgetExecution> records = result.getRecords();
            for (TblBudgetExecution record : records) {
                calculateOccupancyRate(record);
            }

            // 如果有占用率过滤条件，进行过滤
            if (StringUtils.isNotBlank(param.getMinOccupancyRate()) ||
                StringUtils.isNotBlank(param.getMaxOccupancyRate())) {
                records = filterByOccupancyRate(records, param.getMinOccupancyRate(),
                    param.getMaxOccupancyRate());
                result.setRecords(records);
                result.setTotal(records.size());
            }

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询预算占用分页失败", e);
            return MyJsonBean.errorData("查询预算占用分页失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryOccupancyStatistics(BudgetOccupancyQueryParam param) {
        try {
            LambdaQueryWrapper<TblBudgetExecution> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblBudgetExecution::getOrgId, param.getOrgId());

            // 添加查询条件（与分页查询相同）
            applyQueryConditions(wrapper, param);

            List<TblBudgetExecution> list = budgetExecutionMapper.selectList(wrapper);

            // 统计数据
            Map<String, Object> statistics = new HashMap<>();
            BigDecimal totalBudget = BigDecimal.ZERO;
            BigDecimal totalAdjusted = BigDecimal.ZERO;
            BigDecimal totalOccupied = BigDecimal.ZERO;
            BigDecimal totalAvailable = BigDecimal.ZERO;
            int normalCount = 0;
            int warningCount = 0;
            int exceededCount = 0;

            for (TblBudgetExecution execution : list) {
                BigDecimal budget = execution.getBudgetAmount() != null ?
                    execution.getBudgetAmount() : BigDecimal.ZERO;
                BigDecimal adjusted = execution.getAdjustedAmount() != null ?
                    execution.getAdjustedAmount() : BigDecimal.ZERO;
                BigDecimal occupied = execution.getOccupiedAmount() != null ?
                    execution.getOccupiedAmount() : BigDecimal.ZERO;
                BigDecimal available = execution.getAvailableAmount() != null ?
                    execution.getAvailableAmount() : BigDecimal.ZERO;

                totalBudget = totalBudget.add(budget);
                totalAdjusted = totalAdjusted.add(adjusted);
                totalOccupied = totalOccupied.add(occupied);
                totalAvailable = totalAvailable.add(available);

                String status = execution.getStatus();
                if ("NORMAL".equals(status)) {
                    normalCount++;
                } else if ("WARNING".equals(status)) {
                    warningCount++;
                } else if ("EXCEEDED".equals(status)) {
                    exceededCount++;
                }
            }

            // 计算平均占用率
            BigDecimal avgOccupancyRate = BigDecimal.ZERO;
            if (totalBudget.compareTo(BigDecimal.ZERO) > 0) {
                avgOccupancyRate = totalOccupied.multiply(new BigDecimal("100"))
                    .divide(totalBudget, 2, RoundingMode.HALF_UP);
            }

            statistics.put("totalCount", list.size());
            statistics.put("totalBudget", totalBudget);
            statistics.put("totalAdjusted", totalAdjusted);
            statistics.put("totalOccupied", totalOccupied);
            statistics.put("totalAvailable", totalAvailable);
            statistics.put("avgOccupancyRate", avgOccupancyRate);
            statistics.put("normalCount", normalCount);
            statistics.put("warningCount", warningCount);
            statistics.put("exceededCount", exceededCount);

            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            log.error("查询预算占用统计失败", e);
            return MyJsonBean.errorData("查询预算占用统计失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryOccupancyTrend(BudgetOccupancyQueryParam param) {
        try {
            LambdaQueryWrapper<TblBudgetExecution> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblBudgetExecution::getOrgId, param.getOrgId());

            // 添加查询条件
            applyQueryConditions(wrapper, param);

            // 按期间分组统计
            wrapper.orderByAsc(TblBudgetExecution::getPeriod);

            List<TblBudgetExecution> list = budgetExecutionMapper.selectList(wrapper);

            // 按期间分组
            Map<String, Map<String, BigDecimal>> trendMap = new LinkedHashMap<>();
            for (TblBudgetExecution execution : list) {
                String period = execution.getPeriod();
                if (!trendMap.containsKey(period)) {
                    Map<String, BigDecimal> periodData = new HashMap<>();
                    periodData.put("budgetAmount", BigDecimal.ZERO);
                    periodData.put("occupiedAmount", BigDecimal.ZERO);
                    periodData.put("availableAmount", BigDecimal.ZERO);
                    trendMap.put(period, periodData);
                }

                Map<String, BigDecimal> periodData = trendMap.get(period);
                BigDecimal budget = execution.getBudgetAmount() != null ?
                    execution.getBudgetAmount() : BigDecimal.ZERO;
                BigDecimal occupied = execution.getOccupiedAmount() != null ?
                    execution.getOccupiedAmount() : BigDecimal.ZERO;
                BigDecimal available = execution.getAvailableAmount() != null ?
                    execution.getAvailableAmount() : BigDecimal.ZERO;

                periodData.put("budgetAmount", periodData.get("budgetAmount").add(budget));
                periodData.put("occupiedAmount", periodData.get("occupiedAmount").add(occupied));
                periodData.put("availableAmount", periodData.get("availableAmount").add(available));
            }

            // 转换为列表格式
            List<Map<String, Object>> trendList = new ArrayList<>();
            for (Map.Entry<String, Map<String, BigDecimal>> entry : trendMap.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("period", entry.getKey());
                item.put("budgetAmount", entry.getValue().get("budgetAmount"));
                item.put("occupiedAmount", entry.getValue().get("occupiedAmount"));
                item.put("availableAmount", entry.getValue().get("availableAmount"));

                // 计算占用率
                BigDecimal budget = entry.getValue().get("budgetAmount");
                BigDecimal occupied = entry.getValue().get("occupiedAmount");
                BigDecimal occupancyRate = BigDecimal.ZERO;
                if (budget.compareTo(BigDecimal.ZERO) > 0) {
                    occupancyRate = occupied.multiply(new BigDecimal("100"))
                        .divide(budget, 2, RoundingMode.HALF_UP);
                }
                item.put("occupancyRate", occupancyRate);

                trendList.add(item);
            }

            return MyJsonBean.successData(trendList);
        } catch (Exception e) {
            log.error("查询预算占用趋势失败", e);
            return MyJsonBean.errorData("查询预算占用趋势失败：" + e.getMessage());
        }
    }

    /**
     * 应用查询条件
     */
    private void applyQueryConditions(LambdaQueryWrapper<TblBudgetExecution> wrapper,
                                     BudgetOccupancyQueryParam param) {
        if (StringUtils.isNotBlank(param.getModelId())) {
            wrapper.eq(TblBudgetExecution::getModelId, param.getModelId());
        }
        if (StringUtils.isNotBlank(param.getBizOrgId())) {
            wrapper.eq(TblBudgetExecution::getBizOrgId, param.getBizOrgId());
        }
        if (StringUtils.isNotBlank(param.getOrgCode())) {
            wrapper.like(TblBudgetExecution::getOrgCode, param.getOrgCode());
        }
        if (StringUtils.isNotBlank(param.getOrgName())) {
            wrapper.like(TblBudgetExecution::getOrgName, param.getOrgName());
        }
        if (StringUtils.isNotBlank(param.getSubjectCode())) {
            wrapper.like(TblBudgetExecution::getSubjectCode, param.getSubjectCode());
        }
        if (StringUtils.isNotBlank(param.getSubjectName())) {
            wrapper.like(TblBudgetExecution::getSubjectName, param.getSubjectName());
        }
        if (StringUtils.isNotBlank(param.getPeriod())) {
            wrapper.eq(TblBudgetExecution::getPeriod, param.getPeriod());
        }
        if (StringUtils.isNotBlank(param.getBudgetYear())) {
            wrapper.eq(TblBudgetExecution::getBudgetYear, param.getBudgetYear());
        }
        // TODO: TblBudgetExecution没有projectCode和projectName字段，暂时注释
        // if (StringUtils.isNotBlank(param.getProjectCode())) {
        //     wrapper.like(TblBudgetExecution::getProjectCode, param.getProjectCode());
        // }
        // if (StringUtils.isNotBlank(param.getProjectName())) {
        //     wrapper.like(TblBudgetExecution::getProjectName, param.getProjectName());
        // }
        if (StringUtils.isNotBlank(param.getStatus())) {
            wrapper.eq(TblBudgetExecution::getStatus, param.getStatus());
        }
        if (StringUtils.isNotBlank(param.getWarningLevel())) {
            wrapper.eq(TblBudgetExecution::getWarningLevel, param.getWarningLevel());
        }
        if (StringUtils.isNotBlank(param.getStartPeriod())) {
            wrapper.ge(TblBudgetExecution::getPeriod, param.getStartPeriod());
        }
        if (StringUtils.isNotBlank(param.getEndPeriod())) {
            wrapper.le(TblBudgetExecution::getPeriod, param.getEndPeriod());
        }
    }

    /**
     * 计算占用率
     */
    private void calculateOccupancyRate(TblBudgetExecution execution) {
        BigDecimal budget = execution.getBudgetAmount() != null ?
            execution.getBudgetAmount() : BigDecimal.ZERO;
        BigDecimal occupied = execution.getOccupiedAmount() != null ?
            execution.getOccupiedAmount() : BigDecimal.ZERO;

        if (budget.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rate = occupied.multiply(new BigDecimal("100"))
                .divide(budget, 2, RoundingMode.HALF_UP);
            // 注意：TblBudgetExecution没有occupancyRate字段，这里只是计算，不保存
        }
    }

    /**
     * 按占用率过滤
     */
    private List<TblBudgetExecution> filterByOccupancyRate(List<TblBudgetExecution> records,
                                                           String minRate, String maxRate) {
        List<TblBudgetExecution> filtered = new ArrayList<>();
        BigDecimal min = StringUtils.isNotBlank(minRate) ? new BigDecimal(minRate) : null;
        BigDecimal max = StringUtils.isNotBlank(maxRate) ? new BigDecimal(maxRate) : null;

        for (TblBudgetExecution record : records) {
            BigDecimal budget = record.getBudgetAmount() != null ?
                record.getBudgetAmount() : BigDecimal.ZERO;
            BigDecimal occupied = record.getOccupiedAmount() != null ?
                record.getOccupiedAmount() : BigDecimal.ZERO;

            if (budget.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rate = occupied.multiply(new BigDecimal("100"))
                    .divide(budget, 2, RoundingMode.HALF_UP);

                boolean match = true;
                if (min != null && rate.compareTo(min) < 0) {
                    match = false;
                }
                if (max != null && rate.compareTo(max) > 0) {
                    match = false;
                }

                if (match) {
                    filtered.add(record);
                }
            }
        }

        return filtered;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchReleaseBudget(BatchReleaseRequest request) {
        try {
            if (request.getReleaseRequests() == null || request.getReleaseRequests().isEmpty()) {
                return MyJsonBean.errorData("释放请求列表不能为空");
            }

            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (BudgetReleaseRequest releaseRequest : request.getReleaseRequests()) {
                try {
                    // 设置租户ID和操作用户
                    releaseRequest.setOrgId(request.getOrgId());
                    releaseRequest.setOperateUser(request.getOperateUser());

                    // 构建BudgetControlRequest
                    BudgetControlRequest controlRequest = new BudgetControlRequest();
                    controlRequest.setOrgId(releaseRequest.getOrgId());
                    controlRequest.setSubjectCode(releaseRequest.getSubjectCode());
                    controlRequest.setPeriod(releaseRequest.getPeriod());
                    controlRequest.setApplyAmount(releaseRequest.getReleaseAmount());
                    controlRequest.setSourceSystem(releaseRequest.getSourceSystem());
                    controlRequest.setSourceDocId(releaseRequest.getSourceDocId());
                    controlRequest.setOperateUser(releaseRequest.getOperateUser());
                    controlRequest.setOrgId(releaseRequest.getOrgId());

                    // 执行释放
                    MyJsonBean result = releaseBudget(controlRequest);
                    if (result.getCode() == 1) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add(String.format("组织[%s]科目[%s]期间[%s]释放失败：%s",
                            releaseRequest.getOrgId(), releaseRequest.getSubjectCode(),
                            releaseRequest.getPeriod(), result.getMsg()));
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add(String.format("组织[%s]科目[%s]期间[%s]释放异常：%s",
                        releaseRequest.getOrgId(), releaseRequest.getSubjectCode(),
                        releaseRequest.getPeriod(), e.getMessage()));
                    log.error("批量释放预算失败", e);
                }
            }

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("totalCount", request.getReleaseRequests().size());
            resultData.put("successCount", successCount);
            resultData.put("failCount", failCount);
            resultData.put("errorMessages", errorMessages);

            if (failCount == 0) {
                return MyJsonBean.successData("批量释放成功", resultData);
            } else if (successCount == 0) {
                return MyJsonBean.errorData("批量释放全部失败", resultData);
            } else {
                return MyJsonBean.successData("批量释放部分成功", resultData);
            }
        } catch (Exception e) {
            log.error("批量释放预算失败", e);
            return MyJsonBean.errorData("批量释放预算失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryReleaseRecords(ReleaseRecordQueryParam param) {
        try {
            // 用自定义 SQL + LEFT JOIN TBL_STAFF/TBL_ORGANIZATION/TBL_ACCOUNT_SUBJECT,
            // 把操作人姓名 / 业务组织名称 / 科目名称带出来, 并支持 operateUser 按姓名模糊匹配
            Page<TblExecutionRecord> page = new Page<>(param.getPageNumber(), param.getPageSize());
            IPage<TblExecutionRecord> result = executionRecordMapper.selectReleaseRecordPage(page, param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询释放记录失败", e);
            return MyJsonBean.errorData("查询释放记录失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getReleaseRecordStatistics(ReleaseRecordQueryParam param) {
        try {
            // 复用 mapper 的统计列表查询, where 条件和列表完全一致 (通过 sql include 共享)
            // 这样不会再出现"列表筛 5 条, 卡片统计的是 50 条"那种诡异不一致
            List<TblExecutionRecord> records = executionRecordMapper.selectReleaseRecordList(param);

            // 汇总
            BigDecimal totalReleaseAmount = BigDecimal.ZERO;
            int releaseCount = records.size();
            Map<String, BigDecimal> releaseByPeriod = new LinkedHashMap<>();
            Map<String, BigDecimal> releaseByOrg = new HashMap<>();
            Map<String, BigDecimal> releaseBySubject = new HashMap<>();

            for (TblExecutionRecord record : records) {
                BigDecimal amount = record.getApplyAmount() != null ? record.getApplyAmount() : BigDecimal.ZERO;
                totalReleaseAmount = totalReleaseAmount.add(amount);

                String period = record.getPeriod() != null ? record.getPeriod() : "";
                releaseByPeriod.put(period, releaseByPeriod.getOrDefault(period, BigDecimal.ZERO).add(amount));

                String bizOrgId = record.getBizOrgId() != null ? record.getBizOrgId() : "";
                releaseByOrg.put(bizOrgId, releaseByOrg.getOrDefault(bizOrgId, BigDecimal.ZERO).add(amount));

                String subjectCode = record.getSubjectCode() != null ? record.getSubjectCode() : "";
                releaseBySubject.put(subjectCode, releaseBySubject.getOrDefault(subjectCode, BigDecimal.ZERO).add(amount));
            }

            BigDecimal avgReleaseAmount = releaseCount > 0
                    ? totalReleaseAmount.divide(new BigDecimal(releaseCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalReleaseAmount", totalReleaseAmount);
            statistics.put("releaseCount", releaseCount);
            statistics.put("avgReleaseAmount", avgReleaseAmount);
            statistics.put("releaseByPeriod", releaseByPeriod);
            statistics.put("releaseByOrg", releaseByOrg);
            statistics.put("releaseBySubject", releaseBySubject);

            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            log.error("查询释放记录统计失败", e);
            return MyJsonBean.errorData("查询释放记录统计失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean transferBudgetEnhanced(BudgetTransferRequest request) {
        try {
            // 1. 验证转移金额
            if (request.getTransferAmount() == null || request.getTransferAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return MyJsonBean.errorData("转移金额必须大于0");
            }

            // 2. 构建源预算释放请求
            BudgetControlRequest fromRequest = new BudgetControlRequest();
            fromRequest.setOrgId(request.getFromOrgId());
            fromRequest.setSubjectCode(request.getFromSubjectCode());
            fromRequest.setPeriod(request.getFromPeriod());
            fromRequest.setApplyAmount(request.getTransferAmount());
            fromRequest.setSourceSystem(request.getSourceSystem());
            fromRequest.setSourceDocId(request.getSourceDocId());
            fromRequest.setOperateUser(request.getOperateUser());
            fromRequest.setOrgId(request.getOrgId());

            // 3. 构建目标预算占用请求
            BudgetControlRequest toRequest = new BudgetControlRequest();
            toRequest.setOrgId(request.getToOrgId());
            toRequest.setSubjectCode(request.getToSubjectCode());
            toRequest.setPeriod(request.getToPeriod());
            toRequest.setApplyAmount(request.getTransferAmount());
            toRequest.setSourceSystem(request.getSourceSystem());
            toRequest.setSourceDocId(request.getSourceDocId());
            toRequest.setOperateUser(request.getOperateUser());
            toRequest.setOrgId(request.getOrgId());

            // 4. 执行转移
            MyJsonBean result = transferBudget(fromRequest, toRequest);

            if (result.getCode() == 1) {
                // 5. 记录转移日志
                saveTransferRecord(request);
            }

            return result;
        } catch (Exception e) {
            log.error("预算转移失败", e);
            return MyJsonBean.errorData("预算转移失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchTransferBudget(BatchTransferRequest request) {
        try {
            if (request.getTransferRequests() == null || request.getTransferRequests().isEmpty()) {
                return MyJsonBean.errorData("转移请求列表不能为空");
            }

            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (BudgetTransferRequest transferRequest : request.getTransferRequests()) {
                try {
                    // 设置租户ID和操作用户
                    transferRequest.setOrgId(request.getOrgId());
                    transferRequest.setOperateUser(request.getOperateUser());

                    // 执行转移
                    MyJsonBean result = transferBudgetEnhanced(transferRequest);
                    if (result.getCode() == 1) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add(String.format("从[%s-%s-%s]转移到[%s-%s-%s]失败：%s",
                            transferRequest.getFromOrgId(), transferRequest.getFromSubjectCode(), transferRequest.getFromPeriod(),
                            transferRequest.getToOrgId(), transferRequest.getToSubjectCode(), transferRequest.getToPeriod(),
                            result.getMsg()));
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add(String.format("从[%s-%s-%s]转移到[%s-%s-%s]异常：%s",
                        transferRequest.getFromOrgId(), transferRequest.getFromSubjectCode(), transferRequest.getFromPeriod(),
                        transferRequest.getToOrgId(), transferRequest.getToSubjectCode(), transferRequest.getToPeriod(),
                        e.getMessage()));
                    log.error("批量转移预算失败", e);
                }
            }

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("totalCount", request.getTransferRequests().size());
            resultData.put("successCount", successCount);
            resultData.put("failCount", failCount);
            resultData.put("errorMessages", errorMessages);

            if (failCount == 0) {
                return MyJsonBean.successData("批量转移成功", resultData);
            } else if (successCount == 0) {
                return MyJsonBean.errorData("批量转移全部失败", resultData);
            } else {
                return MyJsonBean.successData("批量转移部分成功", resultData);
            }
        } catch (Exception e) {
            log.error("批量转移预算失败", e);
            return MyJsonBean.errorData("批量转移预算失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryTransferRecords(TransferRecordQueryParam param) {
        try {
            // 跟 release 同款方案: 自定义 SQL + LEFT JOIN, 一并拿到操作人姓名 / 业务组织名称 / 科目名称
            Page<TblExecutionRecord> page = new Page<>(param.getPageNumber(), param.getPageSize());
            IPage<TblExecutionRecord> result = executionRecordMapper.selectTransferRecordPage(page, param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询转移记录失败", e);
            return MyJsonBean.errorData("查询转移记录失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getTransferRecordStatistics(TransferRecordQueryParam param) {
        try {
            // 复用 mapper 的统计列表查询, where 条件和列表完全一致 (通过 sql include 共享)
            List<TblExecutionRecord> records = executionRecordMapper.selectTransferRecordList(param);

            BigDecimal totalTransferAmount = BigDecimal.ZERO;
            int transferCount = records.size();
            Map<String, BigDecimal> transferByPeriod = new LinkedHashMap<>();
            Map<String, BigDecimal> transferByOrg = new HashMap<>();
            Map<String, BigDecimal> transferBySubject = new HashMap<>();

            for (TblExecutionRecord record : records) {
                BigDecimal amount = record.getApplyAmount() != null ? record.getApplyAmount() : BigDecimal.ZERO;
                totalTransferAmount = totalTransferAmount.add(amount);

                String period = record.getPeriod() != null ? record.getPeriod() : "";
                transferByPeriod.put(period, transferByPeriod.getOrDefault(period, BigDecimal.ZERO).add(amount));

                String bizOrgId = record.getBizOrgId() != null ? record.getBizOrgId() : "";
                transferByOrg.put(bizOrgId, transferByOrg.getOrDefault(bizOrgId, BigDecimal.ZERO).add(amount));

                String subjectCode = record.getSubjectCode() != null ? record.getSubjectCode() : "";
                transferBySubject.put(subjectCode, transferBySubject.getOrDefault(subjectCode, BigDecimal.ZERO).add(amount));
            }

            BigDecimal avgTransferAmount = transferCount > 0
                    ? totalTransferAmount.divide(new BigDecimal(transferCount), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalTransferAmount", totalTransferAmount);
            statistics.put("transferCount", transferCount);
            statistics.put("avgTransferAmount", avgTransferAmount);
            statistics.put("transferByPeriod", transferByPeriod);
            statistics.put("transferByOrg", transferByOrg);
            statistics.put("transferBySubject", transferBySubject);

            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            log.error("获取转移记录统计失败", e);
            return MyJsonBean.errorData("获取转移记录统计失败：" + e.getMessage());
        }
    }

    /**
     * 保存转移记录
     */
    private void saveTransferRecord(BudgetTransferRequest request) {
        try {
            TblExecutionRecord record = new TblExecutionRecord();
            record.setSourceSystem(request.getSourceSystem());
            record.setSourceDocId(request.getSourceDocId());
            record.setOrgId(request.getFromOrgId());
            record.setSubjectCode(request.getFromSubjectCode());
            record.setPeriod(request.getFromPeriod());
            record.setApplyAmount(request.getTransferAmount());
            record.setControlResult("TRANSFER");
            record.setControlMessage(String.format("预算转移成功：从[%s-%s-%s]转移到[%s-%s-%s]，金额：%s，原因：%s",
                request.getFromOrgId(), request.getFromSubjectCode(), request.getFromPeriod(),
                request.getToOrgId(), request.getToSubjectCode(), request.getToPeriod(),
                request.getTransferAmount(), request.getTransferReason()));
            record.setExecuteTime(new Date());
            record.setOrgId(request.getOrgId());
            record.setCreateUser(request.getOperateUser());
            record.setCreateTime(new Date());

            executionRecordMapper.insert(record);
        } catch (Exception e) {
            log.error("保存转移记录失败", e);
        }
    }
}
