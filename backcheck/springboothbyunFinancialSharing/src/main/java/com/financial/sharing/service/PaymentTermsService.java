package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;

/**
 * 账期设置服务接口
 * 
 * @author Financial Sharing System
 * @since 2024-12-28
 */
public interface PaymentTermsService {

    /**
     * 分页查询账期设置列表
     */
    MyJsonBean<PageResult> getPaymentTermsList(PageableParam param);

    /**
     * 根据ID查询账期设置详情
     */
    MyJsonBean getPaymentTermsById(Long termsId);

    /**
     * 保存或更新账期设置
     */
    MyJsonBean saveOrUpdatePaymentTerms(Object param);

    /**
     * 删除账期设置
     */
    MyJsonBean deletePaymentTerms(Long termsId);
}

