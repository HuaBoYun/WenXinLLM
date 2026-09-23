package com.financial.sharing.business.service;

import com.financial.sharing.business.entity.TblBill;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import java.util.Map;

/**
 * 账单管理服务接口
 */
public interface BillManagementService {

    /**
     * 分页查询账单列表
     */
    MyJsonBean<PageResult<TblBill>> getList(Map<String, Object> param);

    /**
     * 根据ID查询账单详情
     */
    MyJsonBean getById(String billId);

    /**
     * 保存或更新账单
     */
    MyJsonBean saveOrUpdate(TblBill bill);

    /**
     * 删除账单
     */
    MyJsonBean delete(String billId);

    /**
     * 批量处理账单
     */
    MyJsonBean batchProcess(Map<String, Object> batchData);

    /**
     * OCR识别
     */
    MyJsonBean ocrRecognition(String billId, Map<String, Object> ocrParams);

    /**
     * 智能稽核
     */
    MyJsonBean intelligentAudit(String billId, Map<String, Object> auditParams);

    /**
     * 账单应用
     */
    MyJsonBean applyBill(String billId, Map<String, Object> applyData);

    /**
     * 账单统计
     */
    MyJsonBean getStatistics(Map<String, Object> param);

    /**
     * 导出账单
     */
    MyJsonBean export(Map<String, Object> param);
}
