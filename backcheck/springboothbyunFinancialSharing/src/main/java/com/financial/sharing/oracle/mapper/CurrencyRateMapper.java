package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.CurrencyRateEntity;
import com.financial.sharing.vo.param.CurrencyRateQueryParam;
import com.financial.sharing.vo.result.CurrencyRateVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 币种汇率 Mapper接口 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Component("oracleCurrencyRateMapper")
public interface CurrencyRateMapper extends BaseMapper<CurrencyRateEntity> {

    /**
     * 分页查询币种汇率
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param currencyCode 币种编码
     * @param currencyName 币种名称
     * @param rateType 汇率类型
     * @param rateDateStart 汇率日期开始
     * @param rateDateEnd 汇率日期结束
     * @param isBaseCurrency 是否本位币
     * @param isEnabled 是否启用
     * @return 汇率列表
     */
    List<CurrencyRateVO> selectCurrencyRatePage(
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId,
            @Param("currencyCode") String currencyCode,
            @Param("currencyName") String currencyName,
            @Param("rateType") Integer rateType,
            @Param("rateDateStart") LocalDate rateDateStart,
            @Param("rateDateEnd") LocalDate rateDateEnd,
            @Param("isBaseCurrency") Integer isBaseCurrency,
            @Param("isEnabled") Integer isEnabled);

    /**
     * 根据币种编码和日期查询汇率
     * 
     * @param currencyCode 币种编码
     * @param rateDate 汇率日期
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 币种汇率
     */
    CurrencyRateEntity selectByCurrencyAndDate(@Param("currencyCode") String currencyCode,
                                             @Param("rateDate") LocalDate rateDate,
                                             @Param("bookId") Long bookId,
                                             @Param("tenantId") Long tenantId,
                                             @Param("excludeId") Long excludeId);

    /**
     * 获取最新汇率
     * 
     * @param currencyCode 币种编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 最新汇率
     */
    CurrencyRateVO selectLatestRate(@Param("currencyCode") String currencyCode,
                                  @Param("bookId") Long bookId,
                                  @Param("tenantId") Long tenantId);

    /**
     * 根据日期范围查询汇率
     * 
     * @param currencyCode 币种编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 汇率列表
     */
    List<CurrencyRateVO> selectByDateRange(@Param("currencyCode") String currencyCode,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate,
                                         @Param("bookId") Long bookId,
                                         @Param("tenantId") Long tenantId);

    /**
     * 获取所有启用的币种列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 币种列表
     */
    List<CurrencyRateVO> selectEnabledCurrencies(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取本位币信息
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 本位币信息
     */
    CurrencyRateVO selectBaseCurrency(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 批量启用/禁用币种汇率
     * 
     * @param ids 汇率ID列表
     * @param isEnabled 启用状态
     * @param updater 更新人
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids,
                         @Param("isEnabled") Integer isEnabled,
                         @Param("updater") Long updater);

    /**
     * 批量删除币种汇率
     * 
     * @param ids 汇率ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 设置本位币
     * 
     * @param rateId 汇率ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param updater 更新人
     * @return 更新数量
     */
    int setBaseCurrency(@Param("rateId") Long rateId,
                       @Param("bookId") Long bookId,
                       @Param("tenantId") Long tenantId,
                       @Param("updater") Long updater);

    /**
     * 清除本位币标识
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param updater 更新人
     * @return 更新数量
     */
    int clearBaseCurrency(@Param("bookId") Long bookId,
                         @Param("tenantId") Long tenantId,
                         @Param("updater") Long updater);

    /**
     * 获取币种编码列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 币种编码列表
     */
    List<String> selectCurrencyCodes(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);
}
