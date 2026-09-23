package com.financial.sharing.service;

import com.financial.sharing.dto.param.PayableDocumentQueryParam;
import com.financial.sharing.dto.param.PayableDocumentSaveParam;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PayableDocumentVO;

import java.util.List;

/**
 * 应付单据Service接口
 * @author system
 * @since 2025-01-05
 */
public interface PayableDocumentService {

    /**
     * 分页查询应付单据
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<PayableDocumentVO> queryPage(PayableDocumentQueryParam param);

    /**
     * 查询应付单据详情
     * @param documentId 单据ID
     * @return 单据详情
     */
    PayableDocumentVO getDetail(String documentId);

    /**
     * 保存应付单据(新增或修改)
     * @param param 保存参数
     * @return 单据ID
     */
    String save(PayableDocumentSaveParam param);

    /**
     * 删除应付单据
     * @param documentId 单据ID
     */
    void delete(String documentId);

    /**
     * 审核应付单据
     * @param documentId 单据ID
     * @param approved 是否通过
     * @param comments 审核意见
     */
    void audit(String documentId, boolean approved, String comments);

    /**
     * 查询逾期单据
     * @return 逾期单据列表
     */
    List<PayableDocumentVO> queryOverdueList();

    /**
     * 查询供应商应付单据
     * @param supplierId 供应商ID
     * @return 应付单据列表
     */
    List<PayableDocumentVO> queryBySupplier(String supplierId);
}

