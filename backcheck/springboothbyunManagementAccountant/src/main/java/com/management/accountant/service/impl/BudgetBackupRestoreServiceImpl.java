package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetBackupRestore;
import com.management.accountant.oracle.entity.budget.BudgetBackupSchedule;
import com.management.accountant.oracle.mapper.budget.BudgetBackupRestoreMapper;
import com.management.accountant.oracle.mapper.budget.BudgetBackupScheduleMapper;
import com.management.accountant.service.BudgetBackupRestoreService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BudgetBackupRestoreServiceImpl implements BudgetBackupRestoreService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetBackupRestoreMapper backupMapper;

    @Resource
    private BudgetBackupScheduleMapper scheduleMapper;

    @Override
    public PageResult<BudgetBackupRestore> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetBackupRestore> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        if (params.get("backupType") != null && StringUtils.hasText(params.get("backupType").toString())) {
            wrapper.eq("BACKUP_TYPE", params.get("backupType"));
        }
        if (params.get("backupStatus") != null && StringUtils.hasText(params.get("backupStatus").toString())) {
            wrapper.eq("BACKUP_STATUS", params.get("backupStatus"));
        }
        if (params.get("backupName") != null && StringUtils.hasText(params.get("backupName").toString())) {
            wrapper.like("BACKUP_NAME", params.get("backupName"));
        }
        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetBackupRestore> page = new Page<>(pageNum, pageSize);
        IPage<BudgetBackupRestore> pageResult = backupMapper.selectPage(page, wrapper);

        PageResult<BudgetBackupRestore> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public BudgetBackupRestore getById(String backupId) {
        if (!StringUtils.hasText(backupId)) {
            return null;
        }
        QueryWrapper<BudgetBackupRestore> wrapper = new QueryWrapper<>();
        wrapper.eq("BACKUP_ID", backupId).eq("IS_DELETED", 0);
        return backupMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetBackupRestore create(BudgetBackupRestore backup) {
        if (backup == null) {
            throw new ServiceException("备份信息不能为空");
        }
        if (!StringUtils.hasText(backup.getBackupName())) {
            throw new ServiceException("备份名称不能为空");
        }
        if (backup.getIsDeleted() == null) {
            backup.setIsDeleted(0);
        }
        if (!StringUtils.hasText(backup.getBackupType())) {
            backup.setBackupType("FULL");
        }
        if (!StringUtils.hasText(backup.getBackupScope())) {
            backup.setBackupScope("ALL");
        }
        backup.setBackupStatus("IN_PROGRESS");
        backup.setCreateTime(new Date());
        backup.setStartTime(new Date());

        int result = backupMapper.insert(backup);
        if (result <= 0) {
            throw new ServiceException("创建备份失败");
        }

        // Simulate backup completion
        Random random = new Random();
        int duration = 30 + random.nextInt(271); // 30-300 seconds
        backup.setBackupStatus("SUCCESS");
        backup.setEndTime(new Date());
        backup.setDurationSeconds(duration);

        // Set table/record counts based on scope
        String scope = backup.getBackupScope();
        if ("ALL".equals(scope)) {
            backup.setTableCount(25 + random.nextInt(16));
            backup.setRecordCount(10000 + random.nextInt(90001));
        } else if ("BUDGET_DATA".equals(scope)) {
            backup.setTableCount(10 + random.nextInt(6));
            backup.setRecordCount(5000 + random.nextInt(45001));
        } else if ("CONFIG".equals(scope)) {
            backup.setTableCount(5 + random.nextInt(6));
            backup.setRecordCount(500 + random.nextInt(4501));
        } else {
            backup.setTableCount(8 + random.nextInt(8));
            backup.setRecordCount(3000 + random.nextInt(27001));
        }

        // Calculate backup size in MB based on record count
        double sizeMb = backup.getRecordCount() * 0.005 + random.nextInt(50);
        backup.setBackupSize(new BigDecimal(sizeMb).setScale(2, RoundingMode.HALF_UP));
        backup.setBackupPath("/opt/backup/budget/" + backup.getBackupId() + ".zip");
        backup.setIsCompressed(1);
        backup.setIsEncrypted(0);

        backupMapper.updateById(backup);
        log.info("创建备份成功，ID: {}, 状态: SUCCESS", backup.getBackupId());
        return backup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String backupId) {
        if (!StringUtils.hasText(backupId)) {
            throw new ServiceException("备份ID不能为空");
        }
        BudgetBackupRestore backup = getById(backupId);
        if (backup == null) {
            throw new ServiceException("备份不存在");
        }
        BudgetBackupRestore update = new BudgetBackupRestore();
        update.setBackupId(backupId);
        update.setIsDeleted(1);
        backupMapper.updateById(update);
        log.info("删除备份成功，ID: {}", backupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetBackupRestore backup) {
        if (backup == null || !StringUtils.hasText(backup.getBackupId())) {
            throw new ServiceException("备份信息或ID不能为空");
        }
        backupMapper.updateById(backup);
    }

    @Override
    public List<BudgetBackupRestore> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetBackupRestore> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.orderByDesc("CREATE_TIME");
        return backupMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // Total backups count
        QueryWrapper<BudgetBackupRestore> countWrapper = new QueryWrapper<>();
        countWrapper.eq("IS_DELETED", 0);
        Long totalBackups = backupMapper.selectCount(countWrapper);
        stats.put("totalBackups", totalBackups != null ? totalBackups.intValue() : 0);

        // Total size - sum of BACKUP_SIZE in MB, convert to GB string
        QueryWrapper<BudgetBackupRestore> sizeWrapper = new QueryWrapper<>();
        sizeWrapper.eq("IS_DELETED", 0);
        sizeWrapper.isNotNull("BACKUP_SIZE");
        List<BudgetBackupRestore> allBackups = backupMapper.selectList(sizeWrapper);
        BigDecimal totalSizeMb = BigDecimal.ZERO;
        Date lastSuccessTime = null;
        for (BudgetBackupRestore b : allBackups) {
            if (b.getBackupSize() != null) {
                totalSizeMb = totalSizeMb.add(b.getBackupSize());
            }
            if ("SUCCESS".equals(b.getBackupStatus()) && b.getEndTime() != null) {
                if (lastSuccessTime == null || b.getEndTime().after(lastSuccessTime)) {
                    lastSuccessTime = b.getEndTime();
                }
            }
        }
        BigDecimal totalSizeGb = totalSizeMb.divide(new BigDecimal("1024"), 1, RoundingMode.HALF_UP);
        stats.put("totalSize", totalSizeGb.toString());

        // Days since last successful backup
        if (lastSuccessTime != null) {
            long diffMs = new Date().getTime() - lastSuccessTime.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffMs);
            stats.put("lastBackupDays", (int) days);
        } else {
            stats.put("lastBackupDays", -1);
        }

        // Auto backups - count of enabled schedules
        QueryWrapper<BudgetBackupSchedule> scheduleWrapper = new QueryWrapper<>();
        scheduleWrapper.eq("IS_DELETED", 0);
        scheduleWrapper.eq("IS_ENABLED", 1);
        Long autoBackups = scheduleMapper.selectCount(scheduleWrapper);
        stats.put("autoBackups", autoBackups != null ? autoBackups.intValue() : 0);

        return stats;
    }

    @Override
    public Map<String, Integer> getTypeStats() {
        Map<String, Integer> typeStats = new LinkedHashMap<>();
        String[] types = {"FULL", "INCREMENTAL", "DIFFERENTIAL", "CUSTOM"};
        for (String type : types) {
            QueryWrapper<BudgetBackupRestore> wrapper = new QueryWrapper<>();
            wrapper.eq("IS_DELETED", 0);
            wrapper.eq("BACKUP_TYPE", type);
            Long count = backupMapper.selectCount(wrapper);
            typeStats.put(type, count != null ? count.intValue() : 0);
        }
        return typeStats;
    }
}

