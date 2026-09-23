package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArReceivableSaveParam;
import com.financial.sharing.vo.param.ArReceivableQueryParam;

import java.util.List;

/**
 * 应收单据服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArReceivableService {

    /**
     * 分页查询应收单据列表
     */
    MyJsonBean<PageResult> getReceivableList(ArReceivableQueryParam param);

    /**
     * 根据ID查询应收单据详情
     */
    MyJsonBean getReceivableById(String receivableId);

    /**
     * 根据单据编号查询应收单据
     */
    MyJsonBean getReceivableByDocumentNo(String documentNo, Long tenantId);

    /**
     * 保存或更新应收单据
     */
    MyJsonBean saveOrUpdateReceivable(ArReceivableSaveParam param);

    /**
     * 删除应收单据
     */
    MyJsonBean deleteReceivable(String receivableId);

    /**
     * 批量删除应收单据
     */
    MyJsonBean batchDeleteReceivable(List<String> receivableIds);

    /**
     * 提交审核
     */
    MyJsonBean submitForAudit(String receivableId);

    /**
     * 批量提交审核
     */
    MyJsonBean batchSubmitForAudit(List<String> receivableIds);

    /**
     * 审核通过
     */
    MyJsonBean approveReceivable(String receivableId, String auditorId, String auditComments);

    /**
     * 审核驳回
     */
    MyJsonBean rejectReceivable(String receivableId, String auditorId, String auditComments);

    /**
     * 批量审核
     */
    MyJsonBean batchAudit(List<String> receivableIds, Integer status, String auditorId, String auditComments);

    /**
     * 查询客户的应收单据
     */
    MyJsonBean getReceivablesByCustomer(String customerId, Long tenantId);

    /**
     * 查询待核销的应收单据
     */
    MyJsonBean getPendingWriteOffReceivables(String customerId, Long tenantId);

    /**
     * 查询应收单据统计
     */
    MyJsonBean getReceivableStatistics(ArReceivableQueryParam param);

    /**
     * 导出应收单据
     */
    MyJsonBean exportReceivables(ArReceivableQueryParam param);

    /**
     * 生成单据编号
     */
    String generateDocumentNo(Long tenantId);
}

