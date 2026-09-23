package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetLimit;
import com.management.accountant.oracle.mapper.budget.BudgetLimitMapper;
import com.management.accountant.service.BudgetLimitService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 预算限额Service实现类
 * 
 * @description 预算限额业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetLimitServiceImpl implements BudgetLimitService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetLimitMapper limitMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetLimit create(BudgetLimit limit) {
        // 1. 参数校验
        if (limit == null) {
            throw new ServiceException("限额信息不能为空");
        }
        if (!StringUtils.hasText(limit.getLimitName())) {
            throw new ServiceException("限额名称不能为空");
        }
        if (!StringUtils.hasText(limit.getLimitType())) {
            throw new ServiceException("限额类型不能为空");
        }
        if (limit.getLimitAmount() == null || limit.getLimitAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("限额金额必须大于0");
        }

        // 2. 生成限额编码
        if (!StringUtils.hasText(limit.getLimitCode())) {
            limit.setLimitCode(generateLimitCode());
        }

        // 3. 检查编码唯一性
        QueryWrapper<BudgetLimit> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("LIMIT_CODE", limit.getLimitCode())
                   .eq("DEL_FLAG", 0);
        if (limitMapper.selectCount(checkWrapper) > 0) {
            throw new ServiceException("限额编码已存在");
        }

        // 4. 设置默认值
        if (limit.getDelFlag() == null) {
            limit.setDelFlag(0);
        }
        if (!StringUtils.hasText(limit.getStatus())) {
            limit.setStatus("NORMAL");
        }
        if (limit.getUsedAmount() == null) {
            limit.setUsedAmount(BigDecimal.ZERO);
        }
        if (limit.getFrozenAmount() == null) {
            limit.setFrozenAmount(BigDecimal.ZERO);
        }
        if (limit.getAvailableAmount() == null) {
            limit.setAvailableAmount(limit.getLimitAmount());
        }
        limit.setCreateTime(new Date());
        limit.setUpdateTime(new Date());

        // 5. 插入数据库
        int result = limitMapper.insert(limit);
        if (result <= 0) {
            throw new ServiceException("创建预算限额失败");
        }

        log.info("创建预算限额成功，ID: {}", limit.getLimitId());
        return limit;
    }

    @Override
    public BudgetLimit getById(String limitId) {
        if (!StringUtils.hasText(limitId)) {
            throw new ServiceException("限额ID不能为空");
        }
        
        QueryWrapper<BudgetLimit> wrapper = new QueryWrapper<>();
        wrapper.eq("LIMIT_ID", limitId)
               .eq("DEL_FLAG", 0);
        
        return limitMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetLimit limit) {
        if (limit == null || !StringUtils.hasText(limit.getLimitId())) {
            throw new ServiceException("限额ID不能为空");
        }

        BudgetLimit existing = getById(limit.getLimitId());
        if (existing == null) {
            throw new ServiceException("预算限额不存在");
        }

        // 如果修改了编码，检查唯一性
        if (StringUtils.hasText(limit.getLimitCode()) && !limit.getLimitCode().equals(existing.getLimitCode())) {
            QueryWrapper<BudgetLimit> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("LIMIT_CODE", limit.getLimitCode())
                       .ne("LIMIT_ID", limit.getLimitId())
                       .eq("DEL_FLAG", 0);
            if (limitMapper.selectCount(checkWrapper) > 0) {
                throw new ServiceException("限额编码已存在");
            }
        }

        // 重新计算可用金额
        if (limit.getLimitAmount() != null && limit.getUsedAmount() != null) {
            limit.setAvailableAmount(limit.getLimitAmount().subtract(limit.getUsedAmount()));
        }

        limit.setUpdateTime(new Date());
        int result = limitMapper.updateById(limit);
        if (result <= 0) {
            throw new ServiceException("更新预算限额失败");
        }

        log.info("更新预算限额成功，ID: {}", limit.getLimitId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String limitId) {
        if (!StringUtils.hasText(limitId)) {
            throw new ServiceException("限额ID不能为空");
        }

        BudgetLimit limit = getById(limitId);
        if (limit == null) {
            throw new ServiceException("预算限额不存在");
        }

        // 检查是否正在使用（NORMAL 状态不允许直接删除，需先冻结）
        if ("NORMAL".equals(limit.getStatus())) {
            throw new ServiceException("限额正在使用中，请先冻结");
        }

        BudgetLimit update = new BudgetLimit();
        update.setLimitId(limitId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = limitMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除预算限额失败");
        }

        log.info("删除预算限额成功，ID: {}", limitId);
    }

    @Override
    public PageResult<BudgetLimit> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetLimit> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 限额编码
        if (params.get("limitCode") != null && StringUtils.hasText(params.get("limitCode").toString())) {
            wrapper.like("LIMIT_CODE", params.get("limitCode"));
        }

        // 限额名称
        if (params.get("limitName") != null && StringUtils.hasText(params.get("limitName").toString())) {
            wrapper.like("LIMIT_NAME", params.get("limitName"));
        }

        // 限额类型
        if (params.get("limitType") != null && StringUtils.hasText(params.get("limitType").toString())) {
            wrapper.eq("LIMIT_TYPE", params.get("limitType"));
        }

        // 预算ID
        if (params.get("budgetId") != null && StringUtils.hasText(params.get("budgetId").toString())) {
            wrapper.eq("BUDGET_ID", params.get("budgetId"));
        }

        // 状态
        if (params.get("limitStatus") != null && StringUtils.hasText(params.get("limitStatus").toString())) {
            wrapper.eq("STATUS", params.get("limitStatus"));
        }

        // 组织单元
        if (params.get("organizationId") != null && StringUtils.hasText(params.get("organizationId").toString())) {
            wrapper.eq("ORGANIZATION_ID", params.get("organizationId"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetLimit> page = new Page<>(pageNum, pageSize);
        IPage<BudgetLimit> pageResult = limitMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetLimit> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String limitId) {
        if (!StringUtils.hasText(limitId)) {
            throw new ServiceException("限额ID不能为空");
        }

        BudgetLimit limit = getById(limitId);
        if (limit == null) {
            throw new ServiceException("预算限额不存在");
        }

        if ("NORMAL".equals(limit.getStatus())) {
            throw new ServiceException("限额已启用");
        }

        BudgetLimit update = new BudgetLimit();
        update.setLimitId(limitId);
        update.setStatus("NORMAL");
        update.setUpdateTime(new Date());

        int result = limitMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("启用限额失败");
        }

        log.info("启用限额成功，ID: {}", limitId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String limitId) {
        if (!StringUtils.hasText(limitId)) {
            throw new ServiceException("限额ID不能为空");
        }

        BudgetLimit limit = getById(limitId);
        if (limit == null) {
            throw new ServiceException("预算限额不存在");
        }

        if ("FROZEN".equals(limit.getStatus())) {
            throw new ServiceException("限额已冻结");
        }

        BudgetLimit update = new BudgetLimit();
        update.setLimitId(limitId);
        update.setStatus("FROZEN");
        update.setUpdateTime(new Date());

        int result = limitMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("停用限额失败");
        }

        log.info("停用限额成功，ID: {}", limitId);
    }

    @Override
    public Map<String, Object> checkLimit(Map<String, Object> params) {
        String limitId = (String) params.get("limitId");
        BigDecimal amount = params.get("amount") != null ? new BigDecimal(params.get("amount").toString()) : BigDecimal.ZERO;

        if (!StringUtils.hasText(limitId)) {
            throw new ServiceException("限额ID不能为空");
        }

        BudgetLimit limit = getById(limitId);
        if (limit == null) {
            throw new ServiceException("预算限额不存在");
        }

        if (!"NORMAL".equals(limit.getStatus())) {
            throw new ServiceException("限额未启用");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("limitId", limitId);
        result.put("limitName", limit.getLimitName());
        result.put("limitAmount", limit.getLimitAmount());
        result.put("usedAmount", limit.getUsedAmount());
        result.put("availableAmount", limit.getAvailableAmount());
        result.put("checkAmount", amount);

        // 检查是否超限
        BigDecimal afterAmount = limit.getUsedAmount().add(amount);
        boolean exceeded = afterAmount.compareTo(limit.getLimitAmount()) > 0;

        result.put("exceeded", exceeded);
        result.put("afterAmount", afterAmount);
        result.put("exceedAmount", exceeded ? afterAmount.subtract(limit.getLimitAmount()) : BigDecimal.ZERO);
        result.put("checkResult", exceeded ? "超出限额" : "未超限额");
        result.put("checkTime", new Date());

        log.info("检查限额完成，ID: {}, 结果: {}", limitId, exceeded ? "超限" : "正常");
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchEnable(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        java.util.List<String> limitIds = (java.util.List<String>) params.get("limitIds");

        if (limitIds == null || limitIds.isEmpty()) {
            throw new ServiceException("限额ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        java.util.List<String> failedIds = new java.util.ArrayList<>();

        for (String limitId : limitIds) {
            try {
                enable(limitId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(limitId);
                log.error("批量启用限额失败，ID: {}", limitId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", limitIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量启用限额完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchDisable(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        java.util.List<String> limitIds = (java.util.List<String>) params.get("limitIds");

        if (limitIds == null || limitIds.isEmpty()) {
            throw new ServiceException("限额ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        java.util.List<String> failedIds = new java.util.ArrayList<>();

        for (String limitId : limitIds) {
            try {
                disable(limitId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(limitId);
                log.error("批量停用限额失败，ID: {}", limitId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", limitIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量停用限额完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public java.util.List<BudgetLimit> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetLimit> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 应用查询条件
        if (params.get("limitType") != null && StringUtils.hasText(params.get("limitType").toString())) {
            wrapper.eq("LIMIT_TYPE", params.get("limitType"));
        }
        if (params.get("limitStatus") != null && StringUtils.hasText(params.get("limitStatus").toString())) {
            wrapper.eq("STATUS", params.get("limitStatus"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        return limitMapper.selectList(wrapper);
    }

    /**
     * 生成限额编码
     */
    private String generateLimitCode() {
        return "LMT" + System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        try {
            QueryWrapper<BudgetLimit> wrapper = new QueryWrapper<>();
            Integer totalCount = limitMapper.selectCount(wrapper).intValue();
            statistics.put("totalCount", totalCount);

            QueryWrapper<BudgetLimit> activeWrapper = new QueryWrapper<>();
            activeWrapper.eq("STATUS", "NORMAL");
            Integer activeCount = limitMapper.selectCount(activeWrapper).intValue();
            statistics.put("activeCount", activeCount);

            QueryWrapper<BudgetLimit> exceedWrapper = new QueryWrapper<>();
            exceedWrapper.eq("STATUS", "EXCEEDED");
            Integer exceedCount = limitMapper.selectCount(exceedWrapper).intValue();
            statistics.put("exceedCount", exceedCount);

            statistics.put("normalCount", totalCount - activeCount - exceedCount);
        } catch (Exception e) {
            log.error("获取限额统计信息异常", e);
        }
        return statistics;
    }

    @Override
    public java.util.List<BudgetLimit> list(QueryWrapper<BudgetLimit> wrapper) {
        return limitMapper.selectList(wrapper);
    }

    @Override
    public boolean updateById(BudgetLimit limit) {
        return limitMapper.updateById(limit) > 0;
    }
}

