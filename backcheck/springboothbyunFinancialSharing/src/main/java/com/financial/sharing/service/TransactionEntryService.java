package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TransactionEntryQueryParam;
import com.financial.sharing.vo.param.TransactionEntrySaveParam;
import com.financial.sharing.vo.result.TransactionEntryVO;

import java.util.List;

/**
 * 事项分录服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface TransactionEntryService {

    /**
     * 分页查询事项分录
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<TransactionEntryVO> getTransactionEntryPage(TransactionEntryQueryParam param);

    /**
     * 保存或更新事项分录
     * 
     * @param param 保存参数
     * @return 保存结果
     */
    TransactionEntryVO saveOrUpdateTransactionEntry(TransactionEntrySaveParam param);

    /**
     * 根据ID查询事项分录详情
     * 
     * @param entryId 分录ID
     * @return 事项分录详情
     */
    TransactionEntryVO getTransactionEntryById(Long entryId);

    /**
     * 删除事项分录
     * 
     * @param entryId 分录ID
     * @return 是否成功
     */
    boolean deleteTransactionEntry(Long entryId);

    /**
     * 批量删除事项分录
     * 
     * @param entryIds 分录ID列表
     * @return 是否成功
     */
    boolean batchDeleteTransactionEntries(List<Long> entryIds);

    /**
     * 根据事项ID删除分录
     * 
     * @param transactionId 事项ID
     * @return 是否成功
     */
    boolean deleteTransactionEntriesByTransactionId(Long transactionId);

    /**
     * 检查分录编号是否存在
     * 
     * @param entryNo 分录编号
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkEntryNoExists(String entryNo, Long bookId, Long tenantId, Long excludeId);

    /**
     * 根据事项ID查询分录列表
     * 
     * @param transactionId 事项ID
     * @return 分录列表
     */
    List<TransactionEntryVO> getTransactionEntriesByTransactionId(Long transactionId);

    /**
     * 根据科目ID查询分录列表
     * 
     * @param subjectId 科目ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 分录列表
     */
    List<TransactionEntryVO> getTransactionEntriesBySubjectId(Long subjectId, Long bookId, Long tenantId);

    /**
     * 根据币种查询分录列表
     * 
     * @param currencyCode 币种编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 分录列表
     */
    List<TransactionEntryVO> getTransactionEntriesByCurrency(String currencyCode, Long bookId, Long tenantId);

    /**
     * 计算事项分录借贷方合计
     * 
     * @param transactionId 事项ID
     * @return 借贷方合计
     */
    TransactionEntryVO calculateTransactionTotal(Long transactionId);

    /**
     * 验证事项分录借贷平衡
     * 
     * @param transactionId 事项ID
     * @return 是否平衡
     */
    boolean validateTransactionBalance(Long transactionId);

    /**
     * 获取科目分录汇总
     * 
     * @param subjectId 科目ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 科目分录汇总
     */
    TransactionEntryVO getSubjectEntrySummary(Long subjectId, Long bookId, Long tenantId);

    /**
     * 获取币种分录汇总
     * 
     * @param currencyCode 币种编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 币种分录汇总
     */
    TransactionEntryVO getCurrencyEntrySummary(String currencyCode, Long bookId, Long tenantId);

    /**
     * 统计分录数量按科目分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<TransactionEntryVO> countEntriesBySubject(Long bookId, Long tenantId);

    /**
     * 统计分录金额按币种分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<TransactionEntryVO> sumEntriesByCurrency(Long bookId, Long tenantId);
}
