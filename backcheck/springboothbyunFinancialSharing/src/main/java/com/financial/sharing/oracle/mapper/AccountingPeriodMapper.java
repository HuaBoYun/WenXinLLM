package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.dto.AccountingPeriodQueryParam;
import com.financial.sharing.oracle.entity.TblAccountingPeriod;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会计期间Mapper接口
 *
 * @author system
 * @since 2025-12-08
 */
@Repository
public interface AccountingPeriodMapper extends BaseMapper<TblAccountingPeriod> {

    /**
     * 分页查询会计期间
     *
     * @param page  分页对象
     * @param param 查询参数
     * @return 会计期间列表
     */
    IPage<TblAccountingPeriod> selectPeriodPage(Page<TblAccountingPeriod> page, @Param("param") AccountingPeriodQueryParam param);

    /**
     * 查询当前会计期间
     *
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 当前会计期间
     */
    TblAccountingPeriod selectCurrentPeriod(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 根据年度和月份查询期间
     *
     * @param yearNo   年度
     * @param monthNo  月份
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 会计期间
     */
    TblAccountingPeriod selectPeriodByYearMonth(@Param("yearNo") Integer yearNo,
                                              @Param("monthNo") Integer monthNo,
                                              @Param("bookId") Long bookId,
                                              @Param("tenantId") Long tenantId);

    /**
     * 更新当前期间标识
     *
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @param periodId 期间ID
     * @return 影响行数
     */
    int updateCurrentPeriod(@Param("bookId") Long bookId,
                           @Param("tenantId") Long tenantId,
                           @Param("periodId") Long periodId);

    /**
     * 查询指定状态的期间数量
     *
     * @param status   状态
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 数量
     */
    int countByStatus(@Param("status") String status,
                     @Param("bookId") Long bookId,
                     @Param("tenantId") Long tenantId);

    /**
     * 批量创建年度期间
     *
     * @param periods 期间列表
     * @return 影响行数
     */
    int batchInsertPeriods(@Param("periods") List<TblAccountingPeriod> periods);

    /**
     * 查询期间状态列表
     *
     * @param bookId   账簿ID
     * @param tenantId 租户ID
     * @return 状态列表
     */
    List<String> selectStatusList(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);
}