package com.financial.sharing.service;

import com.financial.sharing.dto.param.BillEndorseParam;
import com.financial.sharing.dto.param.BillPaymentParam;
import com.financial.sharing.dto.param.PayableBillQueryParam;
import com.financial.sharing.dto.param.PayableBillSaveParam;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.BillEndorseVO;
import com.financial.sharing.vo.result.BillPaymentVO;
import com.financial.sharing.vo.result.PayableBillVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 应付票据Service接口
 * @author system
 * @since 2025-01-05
 */
public interface PayableBillService {

    /**
     * 分页查询应付票据列表
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<PayableBillVO> getPayableBillList(PayableBillQueryParam param);

    /**
     * 查询应付票据详情
     * @param billId 票据ID
     * @return 票据详情
     */
    PayableBillVO getDetail(String billId);

    /**
     * 保存或更新应付票据
     * @param param 保存参数
     * @return 票据ID
     */
    String saveOrUpdate(PayableBillSaveParam param);

    /**
     * 删除应付票据
     * @param billId 票据ID
     */
    void delete(String billId);

    /**
     * 作废应付票据
     * @param billId 票据ID
     * @param remarks 作废原因
     */
    void cancelBill(String billId, String remarks);

    /**
     * 审核票据
     * @param billId 票据ID
     * @param approved 是否通过
     * @param comments 审核意见
     */
    void audit(String billId, boolean approved, String comments);

    /**
     * 票据背书
     * @param billId 票据ID
     * @param param 背书参数
     */
    void endorse(String billId, BillEndorseParam param);

    /**
     * 票据兑付
     * @param billId 票据ID
     * @param param 兑付参数
     */
    void payment(String billId, BillPaymentParam param);

    /**
     * 获取票据到期提醒
     * @param range 到期范围
     * @param billType 票据类型
     * @return 票据列表
     */
    List<PayableBillVO> getBillDueReminders(String range, String billType);

    /**
     * 获取票据统计信息
     * @return 统计信息
     */
    Map<String, Object> getBillStatistics();

    /**
     * 获取背书列表
     * @return 背书列表
     */
    List<BillEndorseVO> getEndorseList();

    /**
     * 审批背书
     * @param endorseId 背书ID
     * @param approved 是否通过
     * @param comments 审批意见
     */
    void approveEndorse(String endorseId, boolean approved, String comments);

    /**
     * 撤销背书
     * @param endorseId 背书ID
     */
    void cancelEndorse(String endorseId);

    /**
     * 获取兑付列表
     * @return 兑付列表
     */
    List<BillPaymentVO> getPaymentList();

    /**
     * 确认兑付
     * @param paymentId 兑付ID
     * @param confirmData 确认数据
     */
    void confirmPayment(String paymentId, Map<String, Object> confirmData);

    /**
     * 批量操作
     * @param operation 操作类型(audit/endorse/delete)
     * @param billIds 票据ID列表
     * @param operationData 操作数据
     */
    void batchOperation(String operation, List<String> billIds, Map<String, Object> operationData);

    /**
     * 导出票据
     * @param params 导出参数
     * @param response 响应对象
     */
    void exportBills(Map<String, Object> params, HttpServletResponse response);
}

