package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.AccountingVoucherEntity;
import com.financial.sharing.vo.param.AccountingVoucherQueryParam;
import com.financial.sharing.vo.result.AccountingVoucherVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 会计凭证 Mapper接口 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Component("oracleAccountingVoucherMapper")
public interface AccountingVoucherMapper extends BaseMapper<AccountingVoucherEntity> {

    /**
     * 分页查询会计凭证
     *
     * @param param 查询参数
     * @return 会计凭证列表
     */
    List<AccountingVoucherVO> selectAccountingVoucherPage(@Param("param") AccountingVoucherQueryParam param);

    /**
     * 根据凭证编号查询会计凭证
     * 
     * @param voucherNo 凭证编号
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 会计凭证
     */
    AccountingVoucherEntity selectByVoucherNo(@Param("voucherNo") String voucherNo,
                                             @Param("bookId") Long bookId,
                                             @Param("tenantId") Long tenantId,
                                             @Param("excludeId") Long excludeId);

    /**
     * 根据凭证类型查询会计凭证列表
     * 
     * @param voucherTypeId 凭证类型ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计凭证列表
     */
    List<AccountingVoucherVO> selectByVoucherType(@Param("voucherTypeId") Long voucherTypeId,
                                                 @Param("bookId") Long bookId,
                                                 @Param("tenantId") Long tenantId);

    /**
     * 根据会计期间查询会计凭证列表
     * 
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计凭证列表
     */
    List<AccountingVoucherVO> selectByAccountingPeriod(@Param("accountingPeriod") String accountingPeriod,
                                                      @Param("bookId") Long bookId,
                                                      @Param("tenantId") Long tenantId);

    /**
     * 根据凭证状态查询会计凭证列表
     * 
     * @param voucherStatus 凭证状态
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计凭证列表
     */
    List<AccountingVoucherVO> selectByVoucherStatus(@Param("voucherStatus") Integer voucherStatus,
                                                   @Param("bookId") Long bookId,
                                                   @Param("tenantId") Long tenantId);

    /**
     * 根据来源事项查询会计凭证列表
     * 
     * @param sourceTransactionId 来源事项ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计凭证列表
     */
    List<AccountingVoucherVO> selectBySourceTransaction(@Param("sourceTransactionId") Long sourceTransactionId,
                                                       @Param("bookId") Long bookId,
                                                       @Param("tenantId") Long tenantId);

    /**
     * 批量删除会计凭证
     * 
     * @param ids 凭证ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 批量更新凭证状态
     * 
     * @param ids 凭证ID列表
     * @param voucherStatus 凭证状态
     * @param updater 更新人
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, 
                         @Param("voucherStatus") Integer voucherStatus, 
                         @Param("updater") Long updater);

    /**
     * 获取凭证类型列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 凭证类型列表
     */
    List<Long> selectVoucherTypes(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取会计期间列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 会计期间列表
     */
    List<String> selectAccountingPeriods(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计凭证数量按状态分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<AccountingVoucherVO> countByVoucherStatus(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计凭证数量按期间分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<AccountingVoucherVO> countByAccountingPeriod(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取下一个凭证编号
     * 
     * @param voucherTypeId 凭证类型ID
     * @param accountingPeriod 会计期间
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 下一个凭证编号
     */
    String getNextVoucherNo(@Param("voucherTypeId") Long voucherTypeId,
                           @Param("accountingPeriod") String accountingPeriod,
                           @Param("bookId") Long bookId,
                           @Param("tenantId") Long tenantId);

    /**
     * 审核凭证
     * 
     * @param voucherId 凭证ID
     * @param reviewer 审核人
     * @return 更新数量
     */
    int reviewVoucher(@Param("voucherId") Long voucherId, @Param("reviewer") Long reviewer);

    /**
     * 过账凭证
     * 
     * @param voucherId 凭证ID
     * @param poster 过账人
     * @return 更新数量
     */
    int postVoucher(@Param("voucherId") Long voucherId, @Param("poster") Long poster);

    /**
     * 取消过账凭证
     * 
     * @param voucherId 凭证ID
     * @param updater 更新人
     * @return 更新数量
     */
    int unpostVoucher(@Param("voucherId") Long voucherId, @Param("updater") Long updater);
}
