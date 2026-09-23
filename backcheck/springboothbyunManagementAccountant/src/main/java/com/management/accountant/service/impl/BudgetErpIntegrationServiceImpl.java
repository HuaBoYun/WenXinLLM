package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.integration.BudgetErpIntegration;
import com.management.accountant.oracle.mapper.integration.BudgetErpIntegrationMapper;
import com.management.accountant.service.BudgetErpIntegrationService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算ERP集成Service实现类
 * 
 * @description 预算ERP集成业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetErpIntegrationServiceImpl implements BudgetErpIntegrationService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetErpIntegrationMapper erpMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetErpIntegration create(BudgetErpIntegration erp) {
        // 1. 参数校验
        if (erp == null) {
            throw new ServiceException("ERP集成信息不能为空");
        }
        if (!StringUtils.hasText(erp.getErpName())) {
            throw new ServiceException("ERP集成名称不能为空");
        }
        if (!StringUtils.hasText(erp.getErpType())) {
            throw new ServiceException("ERP系统类型不能为空");
        }
        if (!StringUtils.hasText(erp.getConnectionType())) {
            throw new ServiceException("连接方式不能为空");
        }

        // 2. 生成ERP编码
        if (!StringUtils.hasText(erp.getErpCode())) {
            erp.setErpCode(generateErpCode());
        }

        // 3. 检查编码唯一性
        QueryWrapper<BudgetErpIntegration> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("ERP_CODE", erp.getErpCode())
                   .eq("DEL_FLAG", 0);
        if (erpMapper.selectCount(checkWrapper) > 0) {
            throw new ServiceException("ERP集成编码已存在");
        }

        // 4. 设置默认值
        if (erp.getDelFlag() == null) {
            erp.setDelFlag(0);
        }
        if (erp.getIsEnabled() == null) {
            erp.setIsEnabled(false);
        }
        if (!StringUtils.hasText(erp.getIntegrationStatus())) {
            erp.setIntegrationStatus("INACTIVE");
        }
        if (erp.getSuccessCount() == null) {
            erp.setSuccessCount(0);
        }
        if (erp.getFailureCount() == null) {
            erp.setFailureCount(0);
        }
        erp.setCreateTime(new Date());
        erp.setUpdateTime(new Date());

        // 5. 插入数据库
        int result = erpMapper.insert(erp);
        if (result <= 0) {
            throw new ServiceException("创建ERP集成失败");
        }

        log.info("创建ERP集成成功，ID: {}", erp.getErpId());
        return erp;
    }

    @Override
    public BudgetErpIntegration getById(String erpId) {
        if (!StringUtils.hasText(erpId)) {
            throw new ServiceException("ERP集成ID不能为空");
        }
        
        QueryWrapper<BudgetErpIntegration> wrapper = new QueryWrapper<>();
        wrapper.eq("ERP_ID", erpId)
               .eq("DEL_FLAG", 0);
        
        return erpMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetErpIntegration erp) {
        if (erp == null || !StringUtils.hasText(erp.getErpId())) {
            throw new ServiceException("ERP集成ID不能为空");
        }

        BudgetErpIntegration existing = getById(erp.getErpId());
        if (existing == null) {
            throw new ServiceException("ERP集成不存在");
        }

        // 如果修改了编码，检查唯一性
        if (StringUtils.hasText(erp.getErpCode()) && !erp.getErpCode().equals(existing.getErpCode())) {
            QueryWrapper<BudgetErpIntegration> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("ERP_CODE", erp.getErpCode())
                       .ne("ERP_ID", erp.getErpId())
                       .eq("DEL_FLAG", 0);
            if (erpMapper.selectCount(checkWrapper) > 0) {
                throw new ServiceException("ERP集成编码已存在");
            }
        }

        erp.setUpdateTime(new Date());
        int result = erpMapper.updateById(erp);
        if (result <= 0) {
            throw new ServiceException("更新ERP集成失败");
        }

        log.info("更新ERP集成成功，ID: {}", erp.getErpId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String erpId) {
        if (!StringUtils.hasText(erpId)) {
            throw new ServiceException("ERP集成ID不能为空");
        }

        BudgetErpIntegration erp = getById(erpId);
        if (erp == null) {
            throw new ServiceException("ERP集成不存在");
        }

        // 检查是否正在使用
        if (Boolean.TRUE.equals(erp.getIsEnabled())) {
            throw new ServiceException("ERP集成正在使用中，请先停用");
        }

        BudgetErpIntegration update = new BudgetErpIntegration();
        update.setErpId(erpId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = erpMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除ERP集成失败");
        }

        log.info("删除ERP集成成功，ID: {}", erpId);
    }

    @Override
    public PageResult<BudgetErpIntegration> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetErpIntegration> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // ERP编码
        if (params.get("erpCode") != null) {
            wrapper.like("ERP_CODE", params.get("erpCode"));
        }

        // ERP名称
        if (params.get("erpName") != null) {
            wrapper.like("ERP_NAME", params.get("erpName"));
        }

        // ERP类型
        if (params.get("erpType") != null) {
            wrapper.eq("ERP_TYPE", params.get("erpType"));
        }

        // 连接方式
        if (params.get("connectionType") != null) {
            wrapper.eq("CONNECTION_TYPE", params.get("connectionType"));
        }

        // 同步频率
        if (params.get("syncFrequency") != null) {
            wrapper.eq("SYNC_FREQUENCY", params.get("syncFrequency"));
        }

        // 是否启用
        if (params.get("isEnabled") != null) {
            wrapper.eq("IS_ENABLED", params.get("isEnabled"));
        }

        // 集成状态
        if (params.get("integrationStatus") != null) {
            wrapper.eq("INTEGRATION_STATUS", params.get("integrationStatus"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetErpIntegration> page = new Page<>(pageNum, pageSize);
        IPage<BudgetErpIntegration> pageResult = erpMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetErpIntegration> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public Map<String, Object> testConnection(String erpId) {
        if (!StringUtils.hasText(erpId)) {
            throw new ServiceException("ERP集成ID不能为空");
        }

        BudgetErpIntegration erp = getById(erpId);
        if (erp == null) {
            throw new ServiceException("ERP集成不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("erpId", erpId);
        result.put("erpName", erp.getErpName());
        result.put("erpType", erp.getErpType());
        result.put("testTime", new Date());

        try {
            // TODO: 实现实际的连接测试逻辑
            // 根据不同的ERP类型和连接方式进行测试
            boolean success = performConnectionTest(erp);
            
            result.put("success", success);
            result.put("message", success ? "连接测试成功" : "连接测试失败");
            result.put("responseTime", 150); // 模拟响应时间（毫秒）
            
            log.info("ERP连接测试完成，ID: {}, 结果: {}", erpId, success);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "连接测试异常：" + e.getMessage());
            log.error("ERP连接测试异常，ID: {}", erpId, e);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeSync(String erpId) {
        if (!StringUtils.hasText(erpId)) {
            throw new ServiceException("ERP集成ID不能为空");
        }

        BudgetErpIntegration erp = getById(erpId);
        if (erp == null) {
            throw new ServiceException("ERP集成不存在");
        }

        if (!Boolean.TRUE.equals(erp.getIsEnabled())) {
            throw new ServiceException("ERP集成未启用");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("erpId", erpId);
        result.put("erpName", erp.getErpName());
        result.put("syncStartTime", new Date());

        try {
            // TODO: 实现实际的ERP同步逻辑
            int recordCount = performDataSync(erp);
            
            // 更新同步统计
            updateSyncStatistics(erpId, true, recordCount);
            
            result.put("success", true);
            result.put("message", "同步成功");
            result.put("recordCount", recordCount);
            result.put("syncEndTime", new Date());
            
            log.info("ERP数据同步成功，ID: {}, 记录数: {}", erpId, recordCount);
        } catch (Exception e) {
            updateSyncStatistics(erpId, false, 0);
            
            result.put("success", false);
            result.put("message", "同步失败：" + e.getMessage());
            result.put("syncEndTime", new Date());
            
            log.error("ERP数据同步失败，ID: {}", erpId, e);
            throw new ServiceException("ERP数据同步失败：" + e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String erpId) {
        if (!StringUtils.hasText(erpId)) {
            throw new ServiceException("ERP集成ID不能为空");
        }

        BudgetErpIntegration erp = getById(erpId);
        if (erp == null) {
            throw new ServiceException("ERP集成不存在");
        }

        if (Boolean.TRUE.equals(erp.getIsEnabled())) {
            throw new ServiceException("ERP集成已启用");
        }

        BudgetErpIntegration update = new BudgetErpIntegration();
        update.setErpId(erpId);
        update.setIsEnabled(true);
        update.setIntegrationStatus("ACTIVE");
        update.setUpdateTime(new Date());

        int result = erpMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("启用ERP集成失败");
        }

        log.info("启用ERP集成成功，ID: {}", erpId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String erpId) {
        if (!StringUtils.hasText(erpId)) {
            throw new ServiceException("ERP集成ID不能为空");
        }

        BudgetErpIntegration erp = getById(erpId);
        if (erp == null) {
            throw new ServiceException("ERP集成不存在");
        }

        if (!Boolean.TRUE.equals(erp.getIsEnabled())) {
            throw new ServiceException("ERP集成已停用");
        }

        BudgetErpIntegration update = new BudgetErpIntegration();
        update.setErpId(erpId);
        update.setIsEnabled(false);
        update.setIntegrationStatus("INACTIVE");
        update.setUpdateTime(new Date());

        int result = erpMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("停用ERP集成失败");
        }

        log.info("停用ERP集成成功，ID: {}", erpId);
    }

    @Override
    public List<Map<String, Object>> getSyncHistory(String erpId) {
        if (!StringUtils.hasText(erpId)) {
            throw new ServiceException("ERP集成ID不能为空");
        }

        BudgetErpIntegration erp = getById(erpId);
        if (erp == null) {
            throw new ServiceException("ERP集成不存在");
        }

        // TODO: 从同步历史表中查询
        List<Map<String, Object>> history = new ArrayList<>();
        
        // 模拟返回同步历史数据
        Map<String, Object> record = new HashMap<>();
        record.put("syncTime", erp.getLastSyncTime());
        record.put("syncStatus", "SUCCESS");
        record.put("recordCount", erp.getLastSyncRecords());
        record.put("duration", 5000); // 毫秒
        history.add(record);

        return history;
    }

    @Override
    public List<BudgetErpIntegration> getList(String erpType) {
        QueryWrapper<BudgetErpIntegration> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);
        if (StringUtils.hasText(erpType)) {
            wrapper.eq("ERP_TYPE", erpType);
        }
        wrapper.orderByDesc("CREATE_TIME");
        return erpMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        QueryWrapper<BudgetErpIntegration> allWrapper = new QueryWrapper<>();
        allWrapper.eq("DEL_FLAG", 0);
        int totalConnections = erpMapper.selectCount(allWrapper).intValue();

        QueryWrapper<BudgetErpIntegration> activeWrapper = new QueryWrapper<>();
        activeWrapper.eq("DEL_FLAG", 0).eq("IS_ENABLED", true).eq("INTEGRATION_STATUS", "ACTIVE");
        int activeConnections = erpMapper.selectCount(activeWrapper).intValue();

        // 今日同步次数：统计今日有lastSyncTime的记录
        QueryWrapper<BudgetErpIntegration> todaySyncWrapper = new QueryWrapper<>();
        todaySyncWrapper.eq("DEL_FLAG", 0);
        todaySyncWrapper.apply("TRUNC(LAST_SYNC_TIME) = TRUNC(SYSDATE)");
        int todaySync = erpMapper.selectCount(todaySyncWrapper).intValue();

        // 成功率
        int totalSuccess = 0;
        int totalFailure = 0;
        List<BudgetErpIntegration> allList = erpMapper.selectList(allWrapper);
        for (BudgetErpIntegration erp : allList) {
            totalSuccess += erp.getSuccessCount() != null ? erp.getSuccessCount() : 0;
            totalFailure += erp.getFailureCount() != null ? erp.getFailureCount() : 0;
        }
        double successRate = (totalSuccess + totalFailure) > 0
            ? Math.round((double) totalSuccess / (totalSuccess + totalFailure) * 10000) / 100.0
            : 100.0;

        stats.put("totalConnections", totalConnections);
        stats.put("activeConnections", activeConnections);
        stats.put("todaySync", todaySync);
        stats.put("successRate", successRate);

        return stats;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFieldMappings(String erpId, String mappings) {
        if (!StringUtils.hasText(erpId)) {
            throw new ServiceException("ERP集成ID不能为空");
        }
        BudgetErpIntegration erp = getById(erpId);
        if (erp == null) {
            throw new ServiceException("ERP集成不存在");
        }
        BudgetErpIntegration update = new BudgetErpIntegration();
        update.setErpId(erpId);
        update.setDataMapping(mappings);
        update.setUpdateTime(new Date());
        erpMapper.updateById(update);
        log.info("保存字段映射成功，ID: {}", erpId);
    }

    /**
     * 执行连接测试
     */
    private boolean performConnectionTest(BudgetErpIntegration erp) {
        // TODO: 根据不同的ERP类型和连接方式实现实际的连接测试
        // 这里返回模拟结果
        return true;
    }

    /**
     * 执行数据同步
     */
    private int performDataSync(BudgetErpIntegration erp) {
        // TODO: 根据不同的ERP类型和同步方向实现实际的数据同步
        // 这里返回模拟的同步记录数
        return 100;
    }

    /**
     * 更新同步统计
     */
    private void updateSyncStatistics(String erpId, boolean success, int recordCount) {
        BudgetErpIntegration erp = getById(erpId);
        if (erp == null) {
            return;
        }

        BudgetErpIntegration update = new BudgetErpIntegration();
        update.setErpId(erpId);
        update.setLastSyncTime(new Date());
        update.setLastSyncRecords(recordCount);

        if (success) {
            update.setSuccessCount(erp.getSuccessCount() + 1);
            update.setIntegrationStatus("ACTIVE");
            update.setErrorMessage(null);
        } else {
            update.setFailureCount(erp.getFailureCount() + 1);
            update.setIntegrationStatus("ERROR");
        }

        update.setUpdateTime(new Date());
        erpMapper.updateById(update);
    }

    /**
     * 生成ERP编码
     */
    private String generateErpCode() {
        return "ERP" + System.currentTimeMillis();
    }
}

