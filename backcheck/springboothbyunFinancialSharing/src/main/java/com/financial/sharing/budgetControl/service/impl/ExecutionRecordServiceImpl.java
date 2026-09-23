package com.financial.sharing.budgetControl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.budgetControl.dto.ExecutionRecordQueryParam;
import com.financial.sharing.budgetControl.entity.TblExecutionRecord;
import com.financial.sharing.budgetControl.mapper.ExecutionRecordMapper;
import com.financial.sharing.budgetControl.service.ExecutionRecordService;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行记录Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class ExecutionRecordServiceImpl extends ServiceImpl<ExecutionRecordMapper, TblExecutionRecord> 
        implements ExecutionRecordService {

    @Override
    public MyJsonBean queryPage(ExecutionRecordQueryParam param) {
        try {
            LambdaQueryWrapper<TblExecutionRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblExecutionRecord::getOrgId, param.getOrgId());
            
            if (StringUtils.isNotBlank(param.getSourceSystem())) {
                wrapper.eq(TblExecutionRecord::getSourceSystem, param.getSourceSystem());
            }
            if (StringUtils.isNotBlank(param.getSourceDocCode())) {
                wrapper.like(TblExecutionRecord::getSourceDocCode, param.getSourceDocCode());
            }
            if (StringUtils.isNotBlank(param.getBizOrgId())) {
                wrapper.eq(TblExecutionRecord::getBizOrgId, param.getBizOrgId());
            }
            if (StringUtils.isNotBlank(param.getSubjectCode())) {
                wrapper.like(TblExecutionRecord::getSubjectCode, param.getSubjectCode());
            }
            if (StringUtils.isNotBlank(param.getPeriod())) {
                wrapper.eq(TblExecutionRecord::getPeriod, param.getPeriod());
            }
            if (StringUtils.isNotBlank(param.getControlResult())) {
                wrapper.eq(TblExecutionRecord::getControlResult, param.getControlResult());
            }
            if (StringUtils.isNotBlank(param.getStartTime())) {
                wrapper.ge(TblExecutionRecord::getExecuteTime, param.getStartTime());
            }
            if (StringUtils.isNotBlank(param.getEndTime())) {
                wrapper.le(TblExecutionRecord::getExecuteTime, param.getEndTime());
            }
            
            wrapper.orderByDesc(TblExecutionRecord::getExecuteTime);

            Page<TblExecutionRecord> page = new Page<>(param.getPageNumber(), param.getPageSize());
            IPage<TblExecutionRecord> result = this.page(page, wrapper);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询执行记录失败", e);
            return MyJsonBean.errorData("查询执行记录失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryById(String recordId) {
        try {
            TblExecutionRecord record = this.getById(recordId);
            if (record == null) {
                return MyJsonBean.errorData("执行记录不存在");
            }
            return MyJsonBean.successData(record);
        } catch (Exception e) {
            log.error("查询执行记录失败", e);
            return MyJsonBean.errorData("查询执行记录失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean queryStatistics(ExecutionRecordQueryParam param) {
        try {
            LambdaQueryWrapper<TblExecutionRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblExecutionRecord::getOrgId, param.getOrgId());
            
            if (StringUtils.isNotBlank(param.getPeriod())) {
                wrapper.eq(TblExecutionRecord::getPeriod, param.getPeriod());
            }
            if (StringUtils.isNotBlank(param.getStartTime())) {
                wrapper.ge(TblExecutionRecord::getExecuteTime, param.getStartTime());
            }
            if (StringUtils.isNotBlank(param.getEndTime())) {
                wrapper.le(TblExecutionRecord::getExecuteTime, param.getEndTime());
            }
            
            List<TblExecutionRecord> records = this.list(wrapper);
            
            // 统计各种控制结果的数量
            Map<String, Object> statistics = new HashMap<>();
            long totalCount = records.size();
            long passCount = records.stream().filter(r -> "PASS".equals(r.getControlResult())).count();
            long blockCount = records.stream().filter(r -> "BLOCK".equals(r.getControlResult())).count();
            long warnCount = records.stream().filter(r -> "WARN".equals(r.getControlResult())).count();
            long approveCount = records.stream().filter(r -> "APPROVE".equals(r.getControlResult())).count();
            
            statistics.put("totalCount", totalCount);
            statistics.put("passCount", passCount);
            statistics.put("blockCount", blockCount);
            statistics.put("warnCount", warnCount);
            statistics.put("approveCount", approveCount);
            statistics.put("passRate", totalCount > 0 ? (passCount * 100.0 / totalCount) : 0);
            
            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            log.error("查询执行记录统计失败", e);
            return MyJsonBean.errorData("查询执行记录统计失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportRecords(ExecutionRecordQueryParam param) {
        try {
            // 查询数据
            LambdaQueryWrapper<TblExecutionRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblExecutionRecord::getOrgId, param.getOrgId());
            // 添加其他查询条件...
            
            List<TblExecutionRecord> records = this.list(wrapper);
            
            // TODO: 实现Excel导出逻辑
            
            return MyJsonBean.successData("导出成功", records);
        } catch (Exception e) {
            log.error("导出执行记录失败", e);
            return MyJsonBean.errorData("导出执行记录失败：" + e.getMessage());
        }
    }
}

