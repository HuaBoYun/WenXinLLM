package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArCustomerQueryParam;
import com.financial.sharing.vo.param.ArCustomerSaveParam;

import java.util.List;

/**
 * 客户档案服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArCustomerService {

    /**
     * 分页查询客户档案列表
     */
    MyJsonBean<PageResult> getCustomerList(ArCustomerQueryParam param);

    /**
     * 根据ID查询客户详情
     */
    MyJsonBean getCustomerById(String customerId);

    /**
     * 根据客户编码查询客户
     */
    MyJsonBean getCustomerByCode(String customerCode, Long tenantId);

    /**
     * 保存或更新客户档案
     */
    MyJsonBean saveOrUpdateCustomer(ArCustomerSaveParam param);

    /**
     * 删除客户档案
     */
    MyJsonBean deleteCustomer(String customerId);

    /**
     * 批量删除客户档案
     */
    MyJsonBean batchDeleteCustomer(List<String> customerIds);

    /**
     * 批量更新客户状态
     */
    MyJsonBean batchUpdateStatus(List<String> customerIds, Integer status);

    /**
     * 查询客户应收统计
     */
    MyJsonBean getCustomerReceivableStats(String customerId);

    /**
     * 检查客户编码是否存在
     */
    MyJsonBean checkCustomerCodeExists(String customerCode, String customerId, Long tenantId);

    /**
     * 导出客户档案
     */
    MyJsonBean exportCustomers(ArCustomerQueryParam param);
}

