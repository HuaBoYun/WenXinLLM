package com.financial.sharing.dataCollection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.dataCollection.dto.CollectionLogQueryParam;
import com.financial.sharing.dataCollection.entity.TblCollectionLog;
import com.financial.sharing.dataCollection.mapper.CollectionLogMapper;
import com.financial.sharing.dataCollection.service.CollectionLogService;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 归集日志Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class CollectionLogServiceImpl implements CollectionLogService {

    @Autowired
    private CollectionLogMapper collectionLogMapper;

    @Override
    public MyJsonBean queryPage(CollectionLogQueryParam param, String orgId) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<TblCollectionLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblCollectionLog::getOrgId, orgId);
            
            if (StringUtils.isNotBlank(param.getTaskId())) {
                wrapper.eq(TblCollectionLog::getTaskId, param.getTaskId());
            }
            if (StringUtils.isNotBlank(param.getTaskCode())) {
                wrapper.like(TblCollectionLog::getTaskCode, param.getTaskCode());
            }
            if (StringUtils.isNotBlank(param.getTaskName())) {
                wrapper.like(TblCollectionLog::getTaskName, param.getTaskName());
            }
            if (StringUtils.isNotBlank(param.getExecuteType())) {
                wrapper.eq(TblCollectionLog::getExecuteType, param.getExecuteType());
            }
            if (StringUtils.isNotBlank(param.getExecuteStatus())) {
                wrapper.eq(TblCollectionLog::getExecuteStatus, param.getExecuteStatus());
            }
            if (param.getStartTimeBegin() != null) {
                wrapper.ge(TblCollectionLog::getStartTime, param.getStartTimeBegin());
            }
            if (param.getStartTimeEnd() != null) {
                wrapper.le(TblCollectionLog::getStartTime, param.getStartTimeEnd());
            }
            
            // 按开始时间倒序排列
            wrapper.orderByDesc(TblCollectionLog::getStartTime);
            
            // 分页查询
            int pageNumber = param.getPageNumber() != null ? param.getPageNumber() : 1;
            int pageSize = param.getPageSize() != null ? param.getPageSize() : 15;
            Page<TblCollectionLog> page = new Page<>(pageNumber, pageSize);
            Page<TblCollectionLog> result = collectionLogMapper.selectPage(page, wrapper);
            
            // 构建返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("list", result.getRecords());
            data.put("total", result.getTotal());
            data.put("pageNumber", result.getCurrent());
            data.put("pageSize", result.getSize());
            
            return MyJsonBean.successData(data);
        } catch (Exception e) {
            log.error("查询归集日志失败", e);
            return MyJsonBean.errorData("查询归集日志失败：" + e.getMessage());
        }
    }

    @Override
    public TblCollectionLog queryById(String logId, String orgId) {
        LambdaQueryWrapper<TblCollectionLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblCollectionLog::getLogId, logId);
        wrapper.eq(TblCollectionLog::getOrgId, orgId);
        return collectionLogMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveCollectionLog(TblCollectionLog collectionLog) {
        try {
            if (StringUtils.isBlank(collectionLog.getLogId())) {
                // 新增
                collectionLogMapper.insert(collectionLog);
            } else {
                // 更新
                collectionLogMapper.updateById(collectionLog);
            }
            return MyJsonBean.successData(collectionLog);
        } catch (Exception e) {
            log.error("保存归集日志失败", e);
            return MyJsonBean.errorData("保存归集日志失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteCollectionLog(String logId, String orgId) {
        try {
            LambdaQueryWrapper<TblCollectionLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblCollectionLog::getLogId, logId);
            wrapper.eq(TblCollectionLog::getOrgId, orgId);

            collectionLogMapper.delete(wrapper);

            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除归集日志失败", e);
            return MyJsonBean.errorData("删除归集日志失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDeleteCollectionLog(String[] logIds, String orgId) {
        try {
            if (logIds == null || logIds.length == 0) {
                return MyJsonBean.errorData("请选择要删除的日志");
            }

            LambdaQueryWrapper<TblCollectionLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(TblCollectionLog::getLogId, (Object[]) logIds);
            wrapper.eq(TblCollectionLog::getOrgId, orgId);

            int count = collectionLogMapper.delete(wrapper);

            return MyJsonBean.successData("成功删除 " + count + " 条日志");
        } catch (Exception e) {
            log.error("批量删除归集日志失败", e);
            return MyJsonBean.errorData("批量删除归集日志失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean cleanHistoryLogs(Integer days, String orgId) {
        try {
            if (days == null || days <= 0) {
                return MyJsonBean.errorData("保留天数必须大于0");
            }

            // 计算截止日期
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -days);
            Date cutoffDate = calendar.getTime();

            // 删除截止日期之前的日志
            LambdaQueryWrapper<TblCollectionLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblCollectionLog::getOrgId, orgId);
            wrapper.lt(TblCollectionLog::getStartTime, cutoffDate);

            int count = collectionLogMapper.delete(wrapper);

            return MyJsonBean.successData("成功清理 " + count + " 条历史日志");
        } catch (Exception e) {
            log.error("清理历史日志失败", e);
            return MyJsonBean.errorData("清理历史日志失败：" + e.getMessage());
        }
    }
}

