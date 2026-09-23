package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblExceptionHandling;
import com.global.treasurer.mapper.TblExceptionHandlingMapper;
import com.global.treasurer.service.TblExceptionHandlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 异常处理Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblExceptionHandlingServiceImpl implements TblExceptionHandlingService {
    private static final Logger log = LoggerFactory.getLogger(TblExceptionHandlingServiceImpl.class);

    @Resource
    private TblExceptionHandlingMapper tblExceptionHandlingMapper;

    @Override
    public PageInfo<TblExceptionHandling> getExceptionPage(Integer pageNum, Integer pageSize,
                                                           String exceptionNo, String exceptionType,
                                                           String exceptionLevel, String exceptionStatus,
                                                           String startDate, String endDate) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblExceptionHandling> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(exceptionNo), TblExceptionHandling::getExceptionNo, exceptionNo)
               .eq(StringUtils.isNotBlank(exceptionType), TblExceptionHandling::getExceptionType, exceptionType)
               .eq(StringUtils.isNotBlank(exceptionLevel), TblExceptionHandling::getExceptionLevel, exceptionLevel)
               .eq(StringUtils.isNotBlank(exceptionStatus), TblExceptionHandling::getExceptionStatus, exceptionStatus)
               .ge(StringUtils.isNotBlank(startDate), TblExceptionHandling::getExceptionTime, parseDate(startDate))
               .le(StringUtils.isNotBlank(endDate), TblExceptionHandling::getExceptionTime, parseDate(endDate))
               .eq(TblExceptionHandling::getDelFlag, "0")
               .orderByDesc(TblExceptionHandling::getCreateTime);
        List<TblExceptionHandling> list = tblExceptionHandlingMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    /**
     * 解析日期字符串为 Date 对象
     */
    private Date parseDate(String dateStr) {
        try {
            // 支持 yyyy-MM-dd 格式
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            log.warn("日期解析失败: {}", dateStr, e);
            return null;
        }
    }

    @Override
    public TblExceptionHandling getExceptionById(String exceptionId) {
        return tblExceptionHandlingMapper.selectById(exceptionId);
    }

    @Override
    public TblExceptionHandling saveException(TblExceptionHandling exception) {
        exception.setExceptionNo("EH" + System.currentTimeMillis());
        exception.setCreateTime(new Date());
        exception.setDelFlag("0");
        if (exception.getExceptionStatus() == null) {
            exception.setExceptionStatus("PENDING");
        }
        if (exception.getRetryCount() == null) {
            exception.setRetryCount(0);
        }
        tblExceptionHandlingMapper.insert(exception);
        return exception;
    }

    @Override
    public void updateException(TblExceptionHandling exception) {
        exception.setUpdateTime(new Date());
        tblExceptionHandlingMapper.updateById(exception);
    }

    @Override
    public void handleException(String exceptionId, String handleMethod, String handleResult) {
        TblExceptionHandling exception = new TblExceptionHandling();
        exception.setExceptionId(exceptionId);
        exception.setHandleMethod(handleMethod);
        exception.setHandleResult(handleResult);
        exception.setHandleTime(new Date());
        exception.setExceptionStatus("RESOLVED");
        exception.setUpdateTime(new Date());
        tblExceptionHandlingMapper.updateById(exception);
    }

    @Override
    public String retryException(String exceptionId) {
        TblExceptionHandling exception = tblExceptionHandlingMapper.selectById(exceptionId);
        if (exception == null) {
            return null;
        }
        exception.setRetryCount(exception.getRetryCount() + 1);
        exception.setExceptionStatus("HANDLING");
        exception.setUpdateTime(new Date());
        tblExceptionHandlingMapper.updateById(exception);
        log.info("重试异常处理: {}", exception.getExceptionNo());
        return exception.getExceptionNo();
    }

    @Override
    public void closeException(String exceptionId, String closeRemark) {
        TblExceptionHandling exception = new TblExceptionHandling();
        exception.setExceptionId(exceptionId);
        exception.setExceptionStatus("CLOSED");
        exception.setRemark(closeRemark);
        exception.setUpdateTime(new Date());
        tblExceptionHandlingMapper.updateById(exception);
    }

    @Override
    public void deleteException(String exceptionId) {
        TblExceptionHandling exception = new TblExceptionHandling();
        exception.setExceptionId(exceptionId);
        exception.setDelFlag("1");
        exception.setUpdateTime(new Date());
        tblExceptionHandlingMapper.updateById(exception);
    }

    @Override
    public List<TblExceptionHandling> getPendingExceptionList() {
        LambdaQueryWrapper<TblExceptionHandling> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblExceptionHandling::getExceptionStatus, "PENDING")
               .eq(TblExceptionHandling::getDelFlag, "0");
        return tblExceptionHandlingMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getExceptionStatistics() {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<TblExceptionHandling> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblExceptionHandling::getDelFlag, "0");
        List<TblExceptionHandling> list = tblExceptionHandlingMapper.selectList(wrapper);

        long pendingCount = list.stream().filter(e -> "PENDING".equals(e.getExceptionStatus())).count();
        long processingCount = list.stream().filter(e -> "HANDLING".equals(e.getExceptionStatus())).count();
        long resolvedCount = list.stream().filter(e -> "RESOLVED".equals(e.getExceptionStatus())).count();
        long closedCount = list.stream().filter(e -> "CLOSED".equals(e.getExceptionStatus())).count();
        long totalCount = list.size();

        // 前端期望的字段名
        result.put("pending", pendingCount);
        result.put("processing", processingCount);
        result.put("resolved", resolvedCount);
        result.put("closed", closedCount);
        result.put("total", totalCount);

        // 计算解决率
        String resolveRate = "0%";
        if (totalCount > 0) {
            double rate = (resolvedCount + closedCount) * 100.0 / totalCount;
            resolveRate = String.format("%.1f%%", rate);
        }
        result.put("resolveRate", resolveRate);

        return result;
    }

    @Override
    public String testExceptionLevel(String exceptionLevel) {
        log.info("测试方法接收到的 exceptionLevel 参数: {}", exceptionLevel);
        return "接收到的异常级别: " + exceptionLevel;
    }
}

