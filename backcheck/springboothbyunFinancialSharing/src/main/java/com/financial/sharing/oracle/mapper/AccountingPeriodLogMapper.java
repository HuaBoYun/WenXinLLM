package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.TblAccountingPeriodLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会计期间状态变更日志Mapper接口
 *
 * @author system
 * @since 2025-12-08
 */
@Repository
public interface AccountingPeriodLogMapper extends BaseMapper<TblAccountingPeriodLog> {

    /**
     * 根据期间ID查询日志
     *
     * @param periodId 期间ID
     * @return 日志列表
     */
    List<TblAccountingPeriodLog> selectByPeriodId(@Param("periodId") Long periodId);

    /**
     * 查询指定操作类型的日志
     *
     * @param operationType 操作类型
     * @param bookId        账簿ID
     * @param tenantId      租户ID
     * @return 日志列表
     */
    List<TblAccountingPeriodLog> selectByOperationType(@Param("operationType") String operationType,
                                                     @Param("bookId") Long bookId,
                                                     @Param("tenantId") Long tenantId);
}