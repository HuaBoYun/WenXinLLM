package com.financial.sharing.service;

import com.financial.sharing.dto.param.PaymentTermsQueryParam;
import com.financial.sharing.dto.param.PaymentTermsSaveParam;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PaymentTermsVO;

import java.util.List;

/**
 * 账期Service接口
 * @author system
 * @since 2025-01-05
 */
public interface PaymentTermsServiceAp {

    /**
     * 分页查询账期
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<PaymentTermsVO> queryPage(PaymentTermsQueryParam param);

    /**
     * 查询账期详情
     * @param termsId 账期ID
     * @return 账期详情
     */
    PaymentTermsVO getDetail(String termsId);

    /**
     * 保存账期(新增或修改)
     * @param param 保存参数
     * @return 账期ID
     */
    String save(PaymentTermsSaveParam param);

    /**
     * 删除账期
     * @param termsId 账期ID
     */
    void delete(String termsId);

    /**
     * 查询供应商账期
     * @param supplierId 供应商ID
     * @return 账期列表
     */
    List<PaymentTermsVO> queryBySupplier(String supplierId);

    /**
     * 查询所有启用的账期
     * @return 账期列表
     */
    List<PaymentTermsVO> queryAllEnabled();

    /**
     * 更新账期状态
     * @param termsId 账期ID
     * @param status 状态
     */
    void updateStatus(String termsId, Integer status);

    /**
     * 计算到期日期
     * @param termsId 账期ID
     * @param baseDate 基准日期
     * @return 到期日期
     */
    String calculateDueDate(String termsId, String baseDate);

    /**
     * 批量更新账期状态
     * @param termsIds 账期ID列表
     * @param status 状态
     */
    void batchUpdateStatus(List<String> termsIds, Integer status);

    /**
     * 批量删除账期
     * @param termsIds 账期ID列表
     */
    void batchDelete(List<String> termsIds);
}

