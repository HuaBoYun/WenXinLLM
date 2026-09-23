package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetData;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetDataMapper;
import com.management.accountant.service.BudgetDataService;
import com.management.accountant.util.PageResult;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 预算数据录入Service实现类
 * 
 * @description 预算数据录入业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetDataServiceImpl implements BudgetDataService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetDataMapper dataMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取当前登录用户
     */
    private String getCurrentUser() {
        try {
            TblStaffUtil staff = userProvider.get();
            return staff != null ? staff.getUsername() : "admin";
        } catch (Exception e) {
            log.error("获取当前用户失败", e);
            return "admin";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetData create(BudgetData data) {
        // 1. 参数校验
        if (data == null) {
            throw new ServiceException("预算数据不能为空");
        }

        // 2. 设置默认值
        if (data.getDelFlag() == null) {
            data.setDelFlag(0);
        }
        if (data.getDataStatus() == null) {
            data.setDataStatus("DRAFT");
        }
        if (data.getDataSource() == null) {
            data.setDataSource("MANUAL");
        }
        if (data.getCurrency() == null) {
            data.setCurrency("CNY");
        }
        // 设置创建人和更新人（从当前登录用户获取，如果没有则设为 "admin"）
        String currentUser = getCurrentUser();
        if (!StringUtils.hasText(data.getCreateBy())) {
            data.setCreateBy(currentUser != null ? currentUser : "admin");
        }
        if (!StringUtils.hasText(data.getUpdateBy())) {
            data.setUpdateBy(currentUser != null ? currentUser : "admin");
        }
        // 根据 indicatorId 自动填充 indicatorName
        if (StringUtils.hasText(data.getIndicatorId()) && !StringUtils.hasText(data.getIndicatorName())) {
            try {
                BudgetAccount account = accountMapper.selectById(data.getIndicatorId());
                if (account != null) {
                    data.setIndicatorName(account.getAccountName());
                }
            } catch (Exception e) {
                log.warn("根据indicatorId查询科目名称失败: {}", data.getIndicatorId(), e);
            }
        }
        data.setCreateTime(new Date());
        data.setUpdateTime(new Date());

        // 3. 插入数据库
        int result = dataMapper.insert(data);
        if (result <= 0) {
            throw new ServiceException("创建预算数据失败");
        }

        log.info("创建预算数据成功，ID: {}", data.getDataId());
        return data;
    }

    @Override
    public BudgetData getById(String dataId) {
        if (!StringUtils.hasText(dataId)) {
            throw new ServiceException("数据ID不能为空");
        }
        
        QueryWrapper<BudgetData> wrapper = new QueryWrapper<>();
        wrapper.eq("DATA_ID", dataId)
               .eq("DEL_FLAG", 0);
        
        return dataMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetData update(BudgetData data) {
        // 1. 参数校验
        if (data == null || !StringUtils.hasText(data.getDataId())) {
            throw new ServiceException("数据ID不能为空");
        }

        // 2. 检查是否存在
        BudgetData existing = getById(data.getDataId());
        if (existing == null) {
            throw new ServiceException("预算数据不存在");
        }

        // 3. 检查状态是否允许修改
        if ("APPROVED".equals(existing.getDataStatus())) {
            throw new ServiceException("已审批的数据不允许修改");
        }

        // 4. 更新时间
        data.setUpdateTime(new Date());

        // 5. 更新数据库
        int result = dataMapper.updateById(data);
        if (result <= 0) {
            throw new ServiceException("更新预算数据失败");
        }

        log.info("更新预算数据成功，ID: {}", data.getDataId());
        return getById(data.getDataId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String dataId) {
        if (!StringUtils.hasText(dataId)) {
            throw new ServiceException("数据ID不能为空");
        }

        // 检查是否存在
        BudgetData existing = getById(dataId);
        if (existing == null) {
            throw new ServiceException("预算数据不存在");
        }

        // 检查状态是否允许删除
        if ("APPROVED".equals(existing.getDataStatus())) {
            throw new ServiceException("已审批的数据不允许删除");
        }

        // 逻辑删除
        BudgetData data = new BudgetData();
        data.setDataId(dataId);
        data.setDelFlag(1);
        data.setUpdateTime(new Date());

        int result = dataMapper.updateById(data);
        if (result <= 0) {
            throw new ServiceException("删除预算数据失败");
        }

        log.info("删除预算数据成功，ID: {}", dataId);
    }

    @Override
    public PageResult<BudgetData> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetData> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 任务ID
        if (hasValue(params, "taskId")) {
            wrapper.eq("TASK_ID", params.get("taskId"));
        }

        // 预算年度（兼容前端传 fiscalYear 或 budgetYear）
        Object yearVal = null;
        if (hasValue(params, "budgetYear")) {
            yearVal = params.get("budgetYear");
        } else if (hasValue(params, "fiscalYear")) {
            yearVal = params.get("fiscalYear");
        }
        if (yearVal != null) {
            // el-date-picker type=year 返回的是 Date 对象，需要取年份
            if (yearVal instanceof java.util.Date) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime((java.util.Date) yearVal);
                wrapper.eq("BUDGET_YEAR", cal.get(java.util.Calendar.YEAR));
            } else {
                String yearStr = yearVal.toString();
                // 可能是 "2024-01-01T00:00:00.000Z" 格式，只取年份
                if (yearStr.length() >= 4) {
                    try {
                        wrapper.eq("BUDGET_YEAR", Integer.parseInt(yearStr.substring(0, 4)));
                    } catch (NumberFormatException e) {
                        // 忽略无法解析的年份
                    }
                }
            }
        }

        // 预算期间
        if (hasValue(params, "budgetPeriod")) {
            wrapper.eq("BUDGET_PERIOD", params.get("budgetPeriod"));
        }

        // 组织ID（前端 cascader 可能传数组，取最后一个元素）
        if (hasValue(params, "organizationId")) {
            Object orgId = params.get("organizationId");
            if (orgId instanceof java.util.List) {
                java.util.List<?> list = (java.util.List<?>) orgId;
                if (!list.isEmpty()) {
                    wrapper.eq("ORGANIZATION_ID", list.get(list.size() - 1));
                }
            } else {
                wrapper.eq("ORGANIZATION_ID", orgId);
            }
        }

        // 指标ID
        if (hasValue(params, "indicatorId")) {
            wrapper.eq("INDICATOR_ID", params.get("indicatorId"));
        }

        // 数据状态
        if (hasValue(params, "dataStatus")) {
            wrapper.eq("DATA_STATUS", params.get("dataStatus"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetData> page = new Page<>(pageNum, pageSize);
        IPage<BudgetData> pageResult = dataMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetData> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<BudgetData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new ServiceException("数据列表不能为空");
        }

        for (BudgetData data : dataList) {
            if (StringUtils.hasText(data.getDataId())) {
                // 更新
                update(data);
            } else {
                // 创建
                create(data);
            }
        }

        log.info("批量保存预算数据成功，数量: {}", dataList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要删除的数据");
        }

        for (String id : ids) {
            delete(id);
        }

        log.info("批量删除预算数据成功，数量: {}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(String dataId) {
        if (!StringUtils.hasText(dataId)) {
            throw new ServiceException("数据ID不能为空");
        }

        BudgetData data = getById(dataId);
        if (data == null) {
            throw new ServiceException("预算数据不存在");
        }

        if (!"DRAFT".equals(data.getDataStatus())) {
            throw new ServiceException("只有草稿状态的数据才能提交");
        }

        BudgetData update = new BudgetData();
        update.setDataId(dataId);
        update.setDataStatus("SUBMITTED");
        update.setUpdateTime(new Date());

        dataMapper.updateById(update);
        log.info("提交预算数据成功，ID: {}", dataId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSubmit(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要提交的数据");
        }

        for (String id : ids) {
            submit(id);
        }

        log.info("批量提交预算数据成功，数量: {}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importData(List<Map<String, Object>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new ServiceException("导入数据不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            try {
                Map<String, Object> dataMap = dataList.get(i);
                BudgetData data = convertMapToData(dataMap);
                create(data);
                successCount++;
            } catch (Exception e) {
                failCount++;
                errorMessages.add("第" + (i + 1) + "行: " + e.getMessage());
                log.error("导入第{}行数据失败", i + 1, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", dataList.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errorMessages", errorMessages);

        log.info("导入预算数据完成，总数: {}, 成功: {}, 失败: {}", dataList.size(), successCount, failCount);
        return result;
    }

    @Override
    public Map<String, Object> validate(BudgetData data) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (!StringUtils.hasText(data.getTaskId())) {
            errors.add("任务ID不能为空");
        }
        if (!StringUtils.hasText(data.getIndicatorId())) {
            errors.add("指标ID不能为空");
        }
        if (data.getBudgetValue() == null) {
            errors.add("预算值不能为空");
        }
        if (data.getBudgetYear() == null) {
            errors.add("预算年度不能为空");
        }

        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateVariance(String dataId) {
        if (!StringUtils.hasText(dataId)) {
            throw new ServiceException("数据ID不能为空");
        }

        BudgetData data = getById(dataId);
        if (data == null) {
            throw new ServiceException("预算数据不存在");
        }

        BigDecimal budgetValue = data.getBudgetValue();
        BigDecimal actualValue = data.getActualValue();

        if (budgetValue != null && actualValue != null) {
            // 计算差异值
            BigDecimal variance = actualValue.subtract(budgetValue);
            data.setVarianceValue(variance);

            // 计算差异率
            if (budgetValue.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal varianceRate = variance.divide(budgetValue, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                data.setVarianceRate(varianceRate);
            }

            data.setUpdateTime(new Date());
            dataMapper.updateById(data);
            log.info("计算差异成功，ID: {}", dataId);
        }
    }

    @Override
    public List<Map<String, Object>> getHistory(String dataId) {
        // 查询该数据的历史版本（通过 DATA_ID 关联，按更新时间倒序）
        QueryWrapper<BudgetData> wrapper = new QueryWrapper<>();
        wrapper.eq("DATA_ID", dataId).orderByDesc("UPDATE_TIME");
        List<BudgetData> list = dataMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (BudgetData d : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("operationType", d.getDataStatus());
            item.put("operatorName", d.getCreateBy());
            item.put("operateTime", d.getUpdateTime());
            item.put("fieldName", "dataStatus");
            item.put("oldValue", "");
            item.put("newValue", d.getDataStatus());
            item.put("remark", d.getRemark());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getAuditLog(String dataId) {
        // 简单实现：返回该数据的基本操作记录
        List<Map<String, Object>> result = new ArrayList<>();
        BudgetData data = getById(dataId);
        if (data != null) {
            Map<String, Object> item = new HashMap<>();
            item.put("operationType", "CREATE");
            item.put("operatorName", data.getCreateBy());
            item.put("operateTime", data.getCreateTime());
            item.put("ipAddress", "");
            item.put("requestUrl", "/accountant/budget/data/create");
            item.put("remark", "创建预算数据");
            result.add(item);
            if (data.getUpdateTime() != null && !data.getUpdateTime().equals(data.getCreateTime())) {
                Map<String, Object> updateItem = new HashMap<>();
                updateItem.put("operationType", "UPDATE");
                updateItem.put("operatorName", data.getCreateBy());
                updateItem.put("operateTime", data.getUpdateTime());
                updateItem.put("ipAddress", "");
                updateItem.put("requestUrl", "/accountant/budget/data/update/" + dataId);
                updateItem.put("remark", "更新预算数据");
                result.add(updateItem);
            }
        }
        return result;
    }

    /**
     * 将Map转换为BudgetData对象
     */
    private BudgetData convertMapToData(Map<String, Object> dataMap) {
        BudgetData data = new BudgetData();

        if (dataMap.get("taskId") != null) {
            data.setTaskId(dataMap.get("taskId").toString());
        }
        if (dataMap.get("budgetYear") != null) {
            data.setBudgetYear(Integer.parseInt(dataMap.get("budgetYear").toString()));
        }
        if (dataMap.get("budgetPeriod") != null) {
            data.setBudgetPeriod(dataMap.get("budgetPeriod").toString());
        }
        if (dataMap.get("organizationId") != null) {
            data.setOrganizationId(dataMap.get("organizationId").toString());
        }
        if (dataMap.get("indicatorId") != null) {
            data.setIndicatorId(dataMap.get("indicatorId").toString());
        }
        if (dataMap.get("budgetValue") != null) {
            data.setBudgetValue(new BigDecimal(dataMap.get("budgetValue").toString()));
        }
        if (dataMap.get("currency") != null) {
            data.setCurrency(dataMap.get("currency").toString());
        }
        if (dataMap.get("remark") != null) {
            data.setRemark(dataMap.get("remark").toString());
        }

        return data;
    }

    /**
     * 判断 params 中某个 key 是否有有效值（非null、非空字符串、非空集合）
     */
    private boolean hasValue(Map<String, Object> params, String key) {
        Object val = params.get(key);
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }
}

