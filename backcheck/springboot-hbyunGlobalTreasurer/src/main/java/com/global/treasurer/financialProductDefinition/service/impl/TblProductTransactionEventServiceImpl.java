package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.SnowflakeIdWorker;
import com.global.treasurer.financialProductDefinition.entity.TblProductTransactionEvent;
import com.global.treasurer.financialProductDefinition.mapper.TblProductTransactionEventMapper;
import com.global.treasurer.financialProductDefinition.service.TblProductTransactionEventService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 产品交易事件管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class TblProductTransactionEventServiceImpl extends ServiceImpl<TblProductTransactionEventMapper, TblProductTransactionEvent>
        implements TblProductTransactionEventService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TblProductTransactionEventServiceImpl.class);
    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public IPage<TblProductTransactionEvent> getPage(Integer pageNo, Integer pageSize, String eventCode,
                                                      String eventName, String eventType, String productType, String eventStatus, Integer isEnabled, Long orgId) {
        log.info("【调试】Service层接收参数 - pageNo: {}, pageSize: {}, eventCode: {}, eventName: {}, eventType: {}, productType: {}, eventStatus: {}, isEnabled: {}, orgId: {}",
                pageNo, pageSize, eventCode, eventName, eventType, productType, eventStatus, isEnabled, orgId);
        Page<TblProductTransactionEvent> page = new Page<>(pageNo, pageSize);
        QueryWrapper<TblProductTransactionEvent> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(eventCode)) {
            wrapper.like("EVENT_CODE", eventCode);
            log.info("【调试】添加eventCode筛选条件: {}", eventCode);
        }
        if (StringUtils.hasText(eventName)) {
            wrapper.like("EVENT_NAME", eventName);
            log.info("【调试】添加eventName筛选条件: {}", eventName);
        }
        if (StringUtils.hasText(eventType)) {
            wrapper.eq("EVENT_TYPE", eventType);
            log.info("【调试】添加eventType筛选条件: {}", eventType);
        }
        if (StringUtils.hasText(productType)) {
            wrapper.eq("PRODUCT_TYPE", productType);
            log.info("【调试】添加productType筛选条件: {}", productType);
        }
        if (StringUtils.hasText(eventStatus)) {
            wrapper.eq("EVENT_STATUS", eventStatus);
            log.info("【调试】添加eventStatus筛选条件: {}", eventStatus);
        }
        if (isEnabled != null) {
            wrapper.eq("IS_ENABLED", isEnabled);
            log.info("【调试】添加isEnabled筛选条件: {}", isEnabled);
        }
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
            log.info("【调试】添加orgId筛选条件: {}", orgId);
        } else {
            log.info("【调试】orgId为null,不添加组织筛选条件");
        }
        wrapper.orderByAsc("SORT_ORDER").orderByDesc("CREATE_TIME");
        IPage<TblProductTransactionEvent> result = this.page(page, wrapper);
        log.info("【调试】查询结果 - 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());
        return result;
    }

    @Override
    public TblProductTransactionEvent getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblProductTransactionEvent create(TblProductTransactionEvent entity, String createBy) {
        entity.setEventId(snowflakeIdWorker.nextId());
        entity.setCreateBy(createBy);
        entity.setCreateTime(new Date());
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(new Date());
        if (entity.getIsEnabled() == null) {
            entity.setIsEnabled(1);
        }
        // EXECUTION_ORDER字段在数据库表中不存在,改为使用SORT_ORDER
        if (entity.getSortOrder() == null) {
            entity.setSortOrder(0);
        }
        // 设置默认优先级（即使不保存到数据库，前端回退逻辑仍需要）
        if (entity.getPriority() == null) {
            entity.setPriority(5);
        }
        // 设置默认事件状态（即使不保存到数据库，前端回退逻辑仍需要）
        if (entity.getEventStatus() == null) {
            entity.setEventStatus("ACTIVE");
        }
        // 设置默认影响方向（即使不保存到数据库，前端回退逻辑仍需要）
        if (entity.getImpactDirection() == null) {
            entity.setImpactDirection("INCREASE");
        }
        this.save(entity);
        return entity;
    }

    @Override
    public boolean update(TblProductTransactionEvent entity, String updateBy) {
        entity.setUpdateBy(updateBy);
        // 设置更新时间
        entity.setUpdateTime(new Date());
        // 设置默认值（即使不保存到数据库，前端回退逻辑仍需要）
        if (entity.getPriority() == null) {
            entity.setPriority(5);
        }
        if (entity.getEventStatus() == null) {
            entity.setEventStatus("ACTIVE");
        }
        if (entity.getImpactDirection() == null) {
            entity.setImpactDirection("INCREASE");
        }
        return this.updateById(entity);
    }

    @Override
    public boolean delete(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public boolean updateStatus(Long id, Integer isEnabled, String updateBy) {
        TblProductTransactionEvent entity = new TblProductTransactionEvent();
        entity.setEventId(id);
        entity.setIsEnabled(isEnabled);
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new Date());
        return this.updateById(entity);
    }

    @Override
    public boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy) {
        return baseMapper.batchUpdateStatus(ids, isEnabled, updateBy) > 0;
    }

    @Override
    public List<TblProductTransactionEvent> getEnabledList(Long orgId) {
        QueryWrapper<TblProductTransactionEvent> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_ENABLED", 1);
        if (orgId != null) {
            wrapper.eq("ORG_ID", orgId);
        }
        // EXECUTION_ORDER字段在数据库表中不存在,改为使用SORT_ORDER
        wrapper.orderByAsc("SORT_ORDER");
        return this.list(wrapper);
    }

    @Override
    public boolean checkCodeUnique(String eventCode, Long excludeId) {
        return baseMapper.checkCodeUnique(eventCode, excludeId) == 0;
    }

    @Override
    public List<TblProductTransactionEvent> getByProductType(String productType, Long orgId) {
        return baseMapper.selectByProductType(productType, orgId);
    }

    @Override
    public List<TblProductTransactionEvent> getByTransactionTypeId(Long transactionTypeId, Long orgId) {
        return baseMapper.selectByTransactionTypeId(transactionTypeId, orgId);
    }

    @Override
    public TblProductTransactionEvent copy(Long id, String newCode, String newName, String createBy) {
        TblProductTransactionEvent source = this.getById(id);
        if (source == null) {
            return null;
        }
        TblProductTransactionEvent target = new TblProductTransactionEvent();
        BeanUtils.copyProperties(source, target);
        target.setEventId(snowflakeIdWorker.nextId());
        target.setEventCode(newCode);
        target.setEventName(newName);
        target.setCreateBy(createBy);
        target.setCreateTime(new Date());
        target.setUpdateBy(createBy);
        target.setUpdateTime(new Date());
        this.save(target);
        return target;
    }

    @Override
    public Map<String, Object> validateDelete(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("canDelete", usage == 0);
        result.put("usageCount", usage);
        if (usage > 0) {
            result.put("message", "该交易事件已被使用,无法删除");
        }
        return result;
    }

    @Override
    public Map<String, Object> getUsage(Long id) {
        Map<String, Object> result = new HashMap<>();
        int usage = baseMapper.countUsage(id);
        result.put("usageCount", usage);
        result.put("isUsed", usage > 0);
        return result;
    }

    @Override
    public Map<String, Object> getEventMonitorData(Integer pageNo, Integer pageSize) {
        log.info("【调试】获取事件监控数据 - pageNo: {}, pageSize: {}", pageNo, pageSize);
        Map<String, Object> result = new HashMap<>();

        // 查询所有事件
        List<TblProductTransactionEvent> allEvents = this.list();

        // 统计基础数据
        long totalEvents = allEvents.size();

        // 统计启用事件（isEnabled = 1）
        long enabledEvents = allEvents.stream()
                .filter(e -> e.getIsEnabled() != null && e.getIsEnabled() == 1)
                .count();

        // 统计活跃事件（eventStatus = ACTIVE）
        long activeEvents = allEvents.stream()
                .filter(e -> "ACTIVE".equals(e.getEventStatus()))
                .count();

        // 统计暂停事件（eventStatus = PAUSED）
        long pausedEvents = allEvents.stream()
                .filter(e -> "PAUSED".equals(e.getEventStatus()))
                .count();

        // 统计停用事件（eventStatus = INACTIVE）
        long inactiveEvents = allEvents.stream()
                .filter(e -> "INACTIVE".equals(e.getEventStatus()))
                .count();

        // 按事件类型统计
        long purchaseEvents = allEvents.stream()
                .filter(e -> "INVESTMENT_PURCHASE".equals(e.getEventType()))
                .count();

        long redemptionEvents = allEvents.stream()
                .filter(e -> "INVESTMENT_REDEMPTION".equals(e.getEventType()))
                .count();

        long interestEvents = allEvents.stream()
                .filter(e -> "INTEREST_INCOME".equals(e.getEventType()))
                .count();

        long principalEvents = allEvents.stream()
                .filter(e -> "PRINCIPAL_RECOVERY".equals(e.getEventType()))
                .count();

        long dividendEvents = allEvents.stream()
                .filter(e -> "DIVIDEND_INCOME".equals(e.getEventType()))
                .count();

        long maturityEvents = allEvents.stream()
                .filter(e -> "INVESTMENT_MATURITY".equals(e.getEventType()))
                .count();

        long lossEvents = allEvents.stream()
                .filter(e -> "INVESTMENT_LOSS".equals(e.getEventType()))
                .count();

        // 按产品类型统计
        long bankWealthEvents = allEvents.stream()
                .filter(e -> "BANK_WEALTH".equals(e.getProductType()))
                .count();

        long bondEvents = allEvents.stream()
                .filter(e -> "BOND".equals(e.getProductType()))
                .count();

        long equityEvents = allEvents.stream()
                .filter(e -> "EQUITY".equals(e.getProductType()))
                .count();

        long fundEvents = allEvents.stream()
                .filter(e -> "FUND".equals(e.getProductType()))
                .count();

        long derivativeEvents = allEvents.stream()
                .filter(e -> "DERIVATIVE".equals(e.getProductType()))
                .count();

        // 计算启用率
        double enabledRate = totalEvents > 0 ? (double) enabledEvents / totalEvents * 100 : 0.0;

        // 准备监控数据列表（根据触发条件记录）
        List<Map<String, Object>> monitorItems = new ArrayList<>();
        // 这里返回统计信息，实际监控需要根据触发条件记录
        // 暂时返回空列表，只提供统计数据

        // 填充结果
        result.put("total", totalEvents);
        result.put("enabled", enabledEvents);
        result.put("active", activeEvents);
        result.put("paused", pausedEvents);
        result.put("inactive", inactiveEvents);
        result.put("enabledRate", String.format("%.1f", enabledRate));
        result.put("purchaseEvents", purchaseEvents);
        result.put("redemptionEvents", redemptionEvents);
        result.put("interestEvents", interestEvents);
        result.put("principalEvents", principalEvents);
        result.put("dividendEvents", dividendEvents);
        result.put("maturityEvents", maturityEvents);
        result.put("lossEvents", lossEvents);
        result.put("bankWealthEvents", bankWealthEvents);
        result.put("bondEvents", bondEvents);
        result.put("equityEvents", equityEvents);
        result.put("fundEvents", fundEvents);
        result.put("derivativeEvents", derivativeEvents);
        result.put("items", monitorItems);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);

        log.info("【调试】监控数据统计 - total: {}, enabled: {}, active: {}, paused: {}, inactive: {}, enabledRate: {}%, purchase: {}, redemption: {}, interest: {}, principal: {}, dividend: {}, maturity: {}, loss: {}, bankWealth: {}, bond: {}, equity: {}, fund: {}, derivative: {}",
                totalEvents, enabledEvents, activeEvents, pausedEvents, inactiveEvents, String.format("%.1f", enabledRate),
                purchaseEvents, redemptionEvents, interestEvents, principalEvents, dividendEvents, maturityEvents, lossEvents,
                bankWealthEvents, bondEvents, equityEvents, fundEvents, derivativeEvents);
        return result;
    }
}
