package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblSealUsageRecord;
import com.global.treasurer.mapper.TblSealUsageRecordMapper;
import com.global.treasurer.service.TblSealUsageRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 印鉴使用记录Service实现类
 * 基于 01_create_seal_usage_record.sql 表结构
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Service
public class TblSealUsageRecordServiceImpl extends ServiceImpl<TblSealUsageRecordMapper, TblSealUsageRecord>
        implements TblSealUsageRecordService {
    private static final Logger log = LoggerFactory.getLogger(TblSealUsageRecordServiceImpl.class);

    @Autowired
    private TblSealUsageRecordMapper sealUsageRecordMapper;

    @Override
    public IPage<TblSealUsageRecord> getSealUsageRecordPage(Integer page, Integer limit,
                                                          String recordNumber,
                                                          String sealCode, String sealName,
                                                          String operatorName, String businessType,
                                                          String usageStatus) {
        // 构建查询条件
        QueryWrapper<TblSealUsageRecord> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(recordNumber)) {
            queryWrapper.like("RECORD_NUMBER", recordNumber);
        }
        if (StringUtils.hasText(sealCode)) {
            queryWrapper.like("SEAL_CODE", sealCode);
        }
        if (StringUtils.hasText(sealName)) {
            queryWrapper.like("SEAL_NAME", sealName);
        }
        if (StringUtils.hasText(operatorName)) {
            queryWrapper.like("OPERATOR_NAME", operatorName);
        }
        if (StringUtils.hasText(businessType)) {
            queryWrapper.eq("BUSINESS_TYPE", businessType);
        }
        if (StringUtils.hasText(usageStatus)) {
            queryWrapper.eq("USAGE_STATUS", usageStatus);
        }
        queryWrapper.orderByDesc("CREATE_TIME");

        // 分页查询
        Page<TblSealUsageRecord> pageParam = new Page<>(page, limit);
        return this.page(pageParam, queryWrapper);
    }

    @Override
    public TblSealUsageRecord getByRecordId(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRecord(TblSealUsageRecord record) {
        // 生成主键ID
        if (record.getId() == null) {
            Long nextId = sealUsageRecordMapper.getNextId();
            record.setId(nextId);
        }
        // 生成记录编号
        if (!StringUtils.hasText(record.getRecordNumber())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            record.setRecordNumber("REC" + sdf.format(new Date()) + String.format("%03d", record.getId()));
        }
        // 设置创建时间
        if (record.getCreateTime() == null) {
            record.setCreateTime(new Date());
        }
        // 设置更新时间
        record.setUpdateTime(new Date());
        // 设置使用时间
        if (record.getUsageTime() == null) {
            record.setUsageTime(new Date());
        }
        // 默认状态为待审批
        if (!StringUtils.hasText(record.getUsageStatus())) {
            record.setUsageStatus("PENDING");
        }
        return this.save(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecord(TblSealUsageRecord record) {
        if (record.getId() == null) {
            throw new IllegalArgumentException("记录ID不能为空");
        }
        // 设置更新时间
        record.setUpdateTime(new Date());
        return this.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByRecordId(Long id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchByIds(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        try {
            // 统计总数
            Long total = sealUsageRecordMapper.countTotal();
            statistics.put("totalUsages", total);

            // 统计今日使用次数
            Long todayCount = sealUsageRecordMapper.countToday();
            statistics.put("todayUsages", todayCount);

            // 统计本月使用次数
            Long monthCount = sealUsageRecordMapper.countMonth();
            statistics.put("monthUsages", monthCount);

            // 按状态统计
            List<Map<String, Object>> statusStats = sealUsageRecordMapper.countByStatus();
            statistics.put("statusStats", statusStats);

            // 按业务类型统计
            List<Map<String, Object>> businessTypeStats = sealUsageRecordMapper.countByBusinessType();
            statistics.put("businessTypeStats", businessTypeStats);

            // 计算活跃用户数
            QueryWrapper<TblSealUsageRecord> wrapper = new QueryWrapper<>();
            wrapper.select("DISTINCT OPERATOR_NAME");
            long activeUsers = this.count(wrapper);
            statistics.put("activeUsers", activeUsers);

        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            // 返回默认值
            statistics.put("totalUsages", 0L);
            statistics.put("todayUsages", 0L);
            statistics.put("monthUsages", 0L);
            statistics.put("activeUsers", 0L);
        }

        return statistics;
    }

    @Override
    public List<TblSealUsageRecord> queryByCondition(Map<String, Object> params) {
        return sealUsageRecordMapper.selectByCondition(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveRecord(Long id, String approver, String status) {
        TblSealUsageRecord record = this.getByRecordId(id);
        if (record == null) {
            throw new IllegalArgumentException("记录不存在");
        }
        record.setApprover(approver);
        record.setUsageStatus(status);
        record.setApprovalTime(new Date());
        record.setUpdateTime(new Date());
        return this.updateById(record);
    }
}
