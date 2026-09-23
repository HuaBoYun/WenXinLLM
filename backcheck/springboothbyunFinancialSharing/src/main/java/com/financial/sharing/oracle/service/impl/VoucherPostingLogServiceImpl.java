package com.financial.sharing.oracle.service.impl;

import com.financial.sharing.oracle.mapper.VoucherPostingLogMapper;
import com.financial.sharing.oracle.service.VoucherPostingLogService;
import com.hbfk.entity.TblStaffUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 凭证过账日志服务实现
 *
 * @author system
 * @since 2026-01-03
 */
@Service
@Transactional
public class VoucherPostingLogServiceImpl implements VoucherPostingLogService {

    private static final Logger logger = LoggerFactory.getLogger(VoucherPostingLogServiceImpl.class);

    @Autowired
    private VoucherPostingLogMapper voucherPostingLogMapper;

    @Override
    public List<Map<String, Object>> getLogsByVoucherId(Long voucherId) {
        logger.info("查询凭证过账日志，凭证ID：{}", voucherId);
        return voucherPostingLogMapper.selectLogsByVoucherId(voucherId);
    }

    @Override
    public Long getLogsCountByVoucherId(Long voucherId) {
        return voucherPostingLogMapper.selectLogsCountByVoucherId(voucherId);
    }

    @Override
    public boolean recordPostingLog(Long voucherId, String voucherNo, String operationType,
                                   String operationStatus, String errorMessage, String remark,
                                   TblStaffUtil loginStaff) {
        logger.info("记录过账日志，凭证ID：{}，操作类型：{}，状态：{}", voucherId, operationType, operationStatus);
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("voucherId", voucherId);
            logData.put("voucherNo", voucherNo);
            logData.put("operationType", operationType);
            logData.put("operationStatus", operationStatus);
            logData.put("errorMessage", errorMessage);
            logData.put("remark", remark);
            if (loginStaff != null) {
                logData.put("operatorId", loginStaff.getStaffid());
                logData.put("operatorName", loginStaff.getRealname());
            }
            
            int result = voucherPostingLogMapper.insertPostingLog(logData);
            return result > 0;
        } catch (Exception e) {
            logger.error("记录过账日志失败", e);
            return false;
        }
    }

    @Override
    public boolean batchRecordPostingLogs(List<Map<String, Object>> logList) {
        logger.info("批量记录过账日志，数量：{}", logList.size());
        try {
            int result = voucherPostingLogMapper.batchInsertPostingLogs(logList);
            return result > 0;
        } catch (Exception e) {
            logger.error("批量记录过账日志失败", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getLogById(Long logId) {
        return voucherPostingLogMapper.selectLogById(logId);
    }

    @Override
    public Map<String, Object> getLogsByPage(Map<String, Object> param) {
        logger.info("分页查询过账日志");
        
        // 计算偏移量
        int pageNumber = param.get("pageNumber") != null ? (Integer) param.get("pageNumber") : 1;
        int pageSize = param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10;
        param.put("offset", (pageNumber - 1) * pageSize);
        param.put("pageSize", pageSize);
        
        List<Map<String, Object>> list = voucherPostingLogMapper.selectLogsByPage(param);
        Long total = voucherPostingLogMapper.selectLogsCountByPage(param);
        
        Map<String, Object> result = new HashMap<>();
        result.put("logs", list);
        result.put("total", total != null ? total : 0);
        result.put("pageNumber", pageNumber);
        result.put("pageSize", pageSize);
        
        return result;
    }
}

