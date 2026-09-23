package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.TransactionEntryEntity;
import com.financial.sharing.vo.param.TransactionEntryQueryParam;
import com.financial.sharing.vo.result.TransactionEntryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 事项分录 Mapper接口 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Component("oracleTransactionEntryMapper")
public interface TransactionEntryMapper extends BaseMapper<TransactionEntryEntity> {

    /**
     * 分页查询事项分录
     * 
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<TransactionEntryVO> selectTransactionEntryPage(Page<TransactionEntryVO> page, @Param("param") TransactionEntryQueryParam param);

    /**
     * 根据事项ID查询分录列表
     * 
     * @param transactionId 事项ID
     * @return 分录列表
     */
    List<TransactionEntryVO> selectByTransactionId(@Param("transactionId") Long transactionId);

    /**
     * 根据分录编号查询分录
     * 
     * @param entryNo 分录编号
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 事项分录
     */
    TransactionEntryEntity selectByEntryNo(@Param("entryNo") String entryNo,
                                          @Param("bookId") Long bookId,
                                          @Param("tenantId") Long tenantId,
                                          @Param("excludeId") Long excludeId);

    /**
     * 根据科目ID查询分录列表
     * 
     * @param subjectId 科目ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 分录列表
     */
    List<TransactionEntryVO> selectBySubjectId(@Param("subjectId") Long subjectId,
                                              @Param("bookId") Long bookId,
                                              @Param("tenantId") Long tenantId);

    /**
     * 根据币种查询分录列表
     * 
     * @param currencyCode 币种编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 分录列表
     */
    List<TransactionEntryVO> selectByCurrency(@Param("currencyCode") String currencyCode,
                                             @Param("bookId") Long bookId,
                                             @Param("tenantId") Long tenantId);

    /**
     * 批量删除事项分录
     * 
     * @param ids 分录ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 根据事项ID删除分录
     * 
     * @param transactionId 事项ID
     * @param updater 更新人
     * @return 删除数量
     */
    int deleteByTransactionId(@Param("transactionId") Long transactionId, @Param("updater") Long updater);

    /**
     * 计算事项分录借贷方合计
     * 
     * @param transactionId 事项ID
     * @return 借贷方合计
     */
    TransactionEntryVO calculateTransactionTotal(@Param("transactionId") Long transactionId);

    /**
     * 验证事项分录借贷平衡
     * 
     * @param transactionId 事项ID
     * @return 是否平衡
     */
    boolean validateTransactionBalance(@Param("transactionId") Long transactionId);

    /**
     * 获取科目分录汇总
     * 
     * @param subjectId 科目ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 科目分录汇总
     */
    TransactionEntryVO getSubjectEntrySummary(@Param("subjectId") Long subjectId,
                                             @Param("bookId") Long bookId,
                                             @Param("tenantId") Long tenantId);

    /**
     * 获取币种分录汇总
     * 
     * @param currencyCode 币种编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 币种分录汇总
     */
    TransactionEntryVO getCurrencyEntrySummary(@Param("currencyCode") String currencyCode,
                                              @Param("bookId") Long bookId,
                                              @Param("tenantId") Long tenantId);

    /**
     * 统计分录数量按科目分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<TransactionEntryVO> countBySubject(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计分录金额按币种分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<TransactionEntryVO> sumByCurrency(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);
}
