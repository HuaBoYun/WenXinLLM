package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.VoucherEntryEntity;
import com.financial.sharing.vo.result.VoucherEntryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 凭证分录 Mapper接口 - MySQL版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Mapper
@Component("mysqlVoucherEntryMapper")
public interface VoucherEntryMapper extends BaseMapper<VoucherEntryEntity> {

    /**
     * 根据凭证ID查询分录列表
     * 
     * @param voucherId 凭证ID
     * @return 分录列表
     */
    List<VoucherEntryVO> selectByVoucherId(@Param("voucherId") Long voucherId);

    /**
     * 根据科目ID查询分录列表
     * 
     * @param subjectId 科目ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 分录列表
     */
    List<VoucherEntryVO> selectBySubjectId(@Param("subjectId") Long subjectId,
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
    List<VoucherEntryVO> selectByCurrencyCode(@Param("currencyCode") String currencyCode,
                                             @Param("bookId") Long bookId,
                                             @Param("tenantId") Long tenantId);

    /**
     * 批量删除凭证分录
     * 
     * @param ids 分录ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 根据凭证ID删除分录
     * 
     * @param voucherId 凭证ID
     * @param updater 更新人
     * @return 删除数量
     */
    int deleteByVoucherId(@Param("voucherId") Long voucherId, @Param("updater") Long updater);

    /**
     * 计算凭证借贷方合计
     * 
     * @param voucherId 凭证ID
     * @return 借贷方合计
     */
    VoucherEntryVO calculateVoucherTotal(@Param("voucherId") Long voucherId);

    /**
     * 验证凭证借贷平衡
     * 
     * @param voucherId 凭证ID
     * @return 是否平衡
     */
    boolean validateVoucherBalance(@Param("voucherId") Long voucherId);

    /**
     * 获取科目余额汇总
     * 
     * @param subjectId 科目ID
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 科目余额汇总
     */
    VoucherEntryVO getSubjectBalance(@Param("subjectId") Long subjectId,
                                    @Param("accountingPeriod") String accountingPeriod,
                                    @Param("bookId") Long bookId,
                                    @Param("tenantId") Long tenantId);

    /**
     * 获取币种汇总
     * 
     * @param currencyCode 币种编码
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 币种汇总
     */
    VoucherEntryVO getCurrencyTotal(@Param("currencyCode") String currencyCode,
                                   @Param("accountingPeriod") String accountingPeriod,
                                   @Param("bookId") Long bookId,
                                   @Param("tenantId") Long tenantId);

    /**
     * 统计分录数量按科目分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<VoucherEntryVO> countBySubject(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计分录数量按币种分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<VoucherEntryVO> countByCurrency(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取科目发生额明细
     * 
     * @param subjectId 科目ID
     * @param accountingPeriodStart 会计期间开始
     * @param accountingPeriodEnd 会计期间结束
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 科目发生额明细
     */
    List<VoucherEntryVO> getSubjectTransactionDetail(@Param("subjectId") Long subjectId,
                                                    @Param("accountingPeriodStart") String accountingPeriodStart,
                                                    @Param("accountingPeriodEnd") String accountingPeriodEnd,
                                                    @Param("bookId") Long bookId,
                                                    @Param("tenantId") Long tenantId);

    /**
     * 获取试算平衡表数据
     * 
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 试算平衡表数据
     */
    List<VoucherEntryVO> getTrialBalanceData(@Param("accountingPeriod") String accountingPeriod,
                                            @Param("bookId") Long bookId,
                                            @Param("tenantId") Long tenantId);
}
