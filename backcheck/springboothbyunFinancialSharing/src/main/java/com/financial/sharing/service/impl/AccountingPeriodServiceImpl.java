package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.dto.AccountingPeriodQueryParam;
import com.financial.sharing.dto.AccountingPeriodSaveParam;
import com.financial.sharing.oracle.entity.TblAccountingPeriod;
import com.financial.sharing.oracle.entity.TblAccountingPeriodLog;
import com.financial.sharing.oracle.mapper.AccountingPeriodLogMapper;
import com.financial.sharing.oracle.mapper.AccountingPeriodMapper;
import com.financial.sharing.service.AccountingPeriodService;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会计期间服务实现类
 *
 * @author system
 * @since 2025-12-08
 */
@Slf4j
@Service
public class AccountingPeriodServiceImpl extends ServiceImpl<AccountingPeriodMapper, TblAccountingPeriod>
        implements AccountingPeriodService {

    @Resource
    private AccountingPeriodMapper accountingPeriodMapper;

    @Resource
    private AccountingPeriodLogMapper accountingPeriodLogMapper;

    @Override
    public IPage<TblAccountingPeriod> getPeriodPage(AccountingPeriodQueryParam param) {
        Page<TblAccountingPeriod> page = new Page<>(param.getPageNo(), param.getPageSize());
        return accountingPeriodMapper.selectPeriodPage(page, param);
    }

    @Override
    public TblAccountingPeriod getCurrentPeriod(Long bookId, Long tenantId) {
        return accountingPeriodMapper.selectCurrentPeriod(bookId, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblAccountingPeriod saveOrUpdatePeriod(AccountingPeriodSaveParam param, TblStaffUtil userInfo) {
        TblAccountingPeriod period = new TblAccountingPeriod();
        BeanUtils.copyProperties(param, period);

        if (param.getPeriodId() != null) {
            // 更新期间
            TblAccountingPeriod existingPeriod = getById(param.getPeriodId());
            if (existingPeriod == null) {
                throw new RuntimeException("会计期间不存在");
            }

            String oldStatus = existingPeriod.getPeriodStatus();
            period.setUpdatedBy(userInfo.getStaffid().toString());
            updateById(period);

            // 记录日志
            if (!Objects.equals(oldStatus, param.getPeriodStatus())) {
                recordPeriodLog(param.getPeriodId(), oldStatus, param.getPeriodStatus(),
                        TblAccountingPeriodLog.OperationType.UPDATE,
                        "更新期间信息", userInfo, param.getBookId(), param.getTenantId());
            }
        } else {
            // 新增期间
            period.setCreatedBy(userInfo.getStaffid().toString());
            period.setCreatedTime(new Date());
            period.setVersion(1);
            period.setIsDeleted(0);
            save(period);

            // 记录日志
            recordPeriodLog(period.getPeriodId(), null, period.getPeriodStatus(),
                    TblAccountingPeriodLog.OperationType.CREATE,
                    "创建会计期间", userInfo, param.getBookId(), param.getTenantId());
        }
        return period;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean openPeriod(Long periodId, TblStaffUtil userInfo) {
        TblAccountingPeriod period = getById(periodId);
        if (period == null) {
            throw new RuntimeException("会计期间不存在");
        }
        if (TblAccountingPeriod.PeriodStatus.OPEN.equals(period.getPeriodStatus())) {
            throw new RuntimeException("期间已经开启，无需重复操作");
        }
        if (TblAccountingPeriod.PeriodStatus.LOCKED.equals(period.getPeriodStatus())) {
            throw new RuntimeException("期间已锁定，无法开启");
        }

        String oldStatus = period.getPeriodStatus();
        period.setPeriodStatus(TblAccountingPeriod.PeriodStatus.OPEN);
        period.setUpdatedBy(userInfo.getStaffid().toString());
        period.setUpdatedTime(new Date());

        boolean result = updateById(period);

        if (result) {
            recordPeriodLog(periodId, oldStatus, period.getPeriodStatus(),
                    TblAccountingPeriodLog.OperationType.OPEN,
                    "开启会计期间", userInfo, period.getBookId(), period.getTenantId());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closePeriod(Long periodId, TblStaffUtil userInfo) {
        TblAccountingPeriod period = getById(periodId);
        if (period == null) {
            throw new RuntimeException("会计期间不存在");
        }
        if (TblAccountingPeriod.PeriodStatus.CLOSED.equals(period.getPeriodStatus())) {
            throw new RuntimeException("期间已经关闭，无需重复操作");
        }
        if (TblAccountingPeriod.PeriodStatus.LOCKED.equals(period.getPeriodStatus())) {
            throw new RuntimeException("期间已锁定，无法关闭");
        }

        // TODO: 检查期间是否有关联的未处理凭证

        String oldStatus = period.getPeriodStatus();
        period.setPeriodStatus(TblAccountingPeriod.PeriodStatus.CLOSED);
        period.setUpdatedBy(userInfo.getStaffid().toString());
        period.setUpdatedTime(new Date());

        boolean result = updateById(period);

        if (result) {
            recordPeriodLog(periodId, oldStatus, period.getPeriodStatus(),
                    TblAccountingPeriodLog.OperationType.CLOSE,
                    "关闭会计期间", userInfo, period.getBookId(), period.getTenantId());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean lockPeriod(Long periodId, TblStaffUtil userInfo) {
        TblAccountingPeriod period = getById(periodId);
        if (period == null) {
            throw new RuntimeException("会计期间不存在");
        }
        if (TblAccountingPeriod.PeriodStatus.LOCKED.equals(period.getPeriodStatus())) {
            throw new RuntimeException("期间已经锁定，无需重复操作");
        }

        String oldStatus = period.getPeriodStatus();
        period.setPeriodStatus(TblAccountingPeriod.PeriodStatus.LOCKED);
        period.setUpdatedBy(userInfo.getStaffid().toString());
        period.setUpdatedTime(new Date());

        boolean result = updateById(period);

        if (result) {
            recordPeriodLog(periodId, oldStatus, period.getPeriodStatus(),
                    TblAccountingPeriodLog.OperationType.LOCK,
                    "锁定会计期间", userInfo, period.getBookId(), period.getTenantId());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setCurrentPeriod(Long periodId, TblStaffUtil userInfo) {
        TblAccountingPeriod period = getById(periodId);
        if (period == null) {
            throw new RuntimeException("会计期间不存在");
        }

        // 更新所有期间为非当前
        QueryWrapper<TblAccountingPeriod> wrapper = new QueryWrapper<>();
        wrapper.eq("BOOK_ID", period.getBookId())
               .eq("TENANT_ID", period.getTenantId())
               .eq("IS_DELETED", 0);

        TblAccountingPeriod updateEntity = new TblAccountingPeriod();
        updateEntity.setIsCurrent(0);
        update(updateEntity, wrapper);

        // 设置当前期间
        period.setIsCurrent(1);
        period.setUpdatedBy(userInfo.getStaffid().toString());
        period.setUpdatedTime(new Date());

        boolean result = updateById(period);

        if (result) {
            recordPeriodLog(periodId, null, period.getPeriodStatus(),
                    TblAccountingPeriodLog.OperationType.SET_CURRENT,
                    "设置当前会计期间", userInfo, period.getBookId(), period.getTenantId());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TblAccountingPeriod> batchCreateYearPeriods(Integer yearNo, Long bookId, Long tenantId, TblStaffUtil userInfo) {
        List<TblAccountingPeriod> periods = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int month = 1; month <= 12; month++) {
            // 检查期间是否已存在
            TblAccountingPeriod existing = accountingPeriodMapper.selectPeriodByYearMonth(yearNo, month, bookId, tenantId);
            if (existing != null) {
                continue;
            }

            TblAccountingPeriod period = new TblAccountingPeriod();
            period.setYearNo(yearNo);
            period.setMonthNo(month);
            period.setPeriodCode(yearNo + String.format("%02d", month));
            period.setPeriodName(yearNo + "年" + month + "月");

            // 计算开始和结束日期
            Calendar cal = Calendar.getInstance();
            cal.set(yearNo, month - 1, 1);
            Date startDate = cal.getTime();

            cal.set(yearNo, month, 0); // 上个月的最后一天
            Date endDate = cal.getTime();

            period.setStartDate(startDate);
            period.setEndDate(endDate);

            // 设置状态：当前月份之前为关闭，当前月份为开启，未来月份为已创建
            Calendar now = Calendar.getInstance();
            if (yearNo < now.get(Calendar.YEAR) ||
                (yearNo == now.get(Calendar.YEAR) && month < now.get(Calendar.MONTH) + 1)) {
                period.setPeriodStatus(TblAccountingPeriod.PeriodStatus.CLOSED);
            } else if (yearNo == now.get(Calendar.YEAR) && month == now.get(Calendar.MONTH) + 1) {
                period.setPeriodStatus(TblAccountingPeriod.PeriodStatus.OPEN);
                period.setIsCurrent(1);
            } else {
                period.setPeriodStatus(TblAccountingPeriod.PeriodStatus.CREATED);
                period.setIsCurrent(0);
            }
            period.setCreatedBy(userInfo.getStaffid().toString());
            period.setCreatedTime(new Date());
            period.setBookId(bookId);
            period.setTenantId(tenantId);
            period.setVersion(1);
            period.setIsDeleted(0);

            periods.add(period);
        }
        if (!periods.isEmpty()) {
            accountingPeriodMapper.batchInsertPeriods(periods);

            // 记录创建日志
            for (TblAccountingPeriod period : periods) {
                recordPeriodLog(period.getPeriodId(), null, period.getPeriodStatus(),
                        TblAccountingPeriodLog.OperationType.CREATE,
                        "批量创建年度期间", userInfo, bookId, tenantId);
            }
        }
        return periods;
    }

    @Override
    public List<String> getPeriodStatusList(Long bookId, Long tenantId) {
        return accountingPeriodMapper.selectStatusList(bookId, tenantId);
    }

    @Override
    public Map<String, Object> getPeriodStatistics(Long bookId, Long tenantId) {
        Map<String, Object> statistics = new HashMap<>();

        // 统计总数
        QueryWrapper<TblAccountingPeriod> wrapper = new QueryWrapper<>();
        wrapper.eq("BOOK_ID", bookId)
               .eq("TENANT_ID", tenantId)
               .eq("IS_DELETED", 0);

        statistics.put("totalPeriods", count(wrapper));

        // 统计开启数量
        wrapper.eq("PERIOD_STATUS", TblAccountingPeriod.PeriodStatus.OPEN);
        statistics.put("openPeriods", count(wrapper));

        // 统计关闭数量
        wrapper = new QueryWrapper<>();
        wrapper.eq("BOOK_ID", bookId)
               .eq("TENANT_ID", tenantId)
               .eq("IS_DELETED", 0)
               .eq("PERIOD_STATUS", TblAccountingPeriod.PeriodStatus.CLOSED);
        statistics.put("closedPeriods", count(wrapper));

        // 统计锁定数量
        wrapper = new QueryWrapper<>();
        wrapper.eq("BOOK_ID", bookId)
               .eq("TENANT_ID", tenantId)
               .eq("IS_DELETED", 0)
               .eq("PERIOD_STATUS", TblAccountingPeriod.PeriodStatus.LOCKED);
        statistics.put("lockedPeriods", count(wrapper));

        // 获取当前期间
        TblAccountingPeriod currentPeriod = getCurrentPeriod(bookId, tenantId);
        if (currentPeriod != null) {
            statistics.put("currentPeriod", currentPeriod.getPeriodCode());

            // 获取下一期间
            TblAccountingPeriod nextPeriod = getPeriodByYearMonth(
                currentPeriod.getYearNo(),
                currentPeriod.getMonthNo() + 1,
                bookId, tenantId
            );
            if (nextPeriod != null) {
                statistics.put("nextPeriod", nextPeriod.getPeriodCode());
            } else {
                // 下一年一月
                TblAccountingPeriod nextYearPeriod = getPeriodByYearMonth(
                    currentPeriod.getYearNo() + 1, 1, bookId, tenantId
                );
                if (nextYearPeriod != null) {
                    statistics.put("nextPeriod", nextYearPeriod.getPeriodCode());
                }
            }
        }
        return statistics;
    }

    @Override
    public TblAccountingPeriod getPeriodByYearMonth(Integer yearNo, Integer monthNo, Long bookId, Long tenantId) {
        return accountingPeriodMapper.selectPeriodByYearMonth(yearNo, monthNo, bookId, tenantId);
    }

    @Override
    public List<TblAccountingPeriodLog> getPeriodLogs(Long periodId) {
        return accountingPeriodLogMapper.selectByPeriodId(periodId);
    }

    @Override
    public void recordPeriodLog(Long periodId, String oldStatus, String newStatus, String operationType,
                               String reason, TblStaffUtil userInfo, Long bookId, Long tenantId) {
        TblAccountingPeriodLog log = new TblAccountingPeriodLog();
        log.setPeriodId(periodId);
        log.setOldStatus(oldStatus);
        log.setNewStatus(newStatus);
        log.setOperationType(operationType);
        log.setOperationReason(reason);
        log.setOperatorId(userInfo.getStaffid().toString());
        log.setOperatorName(userInfo.getStaffid().toString());
        log.setOperationTime(new Date());
        log.setBookId(bookId);
        log.setTenantId(tenantId);

        accountingPeriodLogMapper.insert(log);
    }
}