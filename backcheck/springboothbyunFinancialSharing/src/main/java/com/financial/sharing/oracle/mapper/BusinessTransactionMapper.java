package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.BusinessTransactionEntity;
import com.financial.sharing.vo.param.BusinessTransactionQueryParam;
import com.financial.sharing.vo.result.BusinessTransactionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 业务事项 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2024-12-19
 */
@Component("oracleBusinessTransactionMapper")
public interface BusinessTransactionMapper extends BaseMapper<BusinessTransactionEntity> {

    /**
     * 分页查询业务事项（配合 PageHelper 使用）
     *
     * @param param 查询参数
     * @return 业务事项列表
     */
    List<BusinessTransactionVO> selectBusinessTransactionPage(@Param("param") BusinessTransactionQueryParam param);

    /**
     * 根据事项编号查询业务事项
     * 
     * @param transactionNo 事项编号
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 业务事项
     */
    BusinessTransactionEntity selectByTransactionNo(@Param("transactionNo") String transactionNo,
                                                   @Param("bookId") Long bookId,
                                                   @Param("tenantId") Long tenantId,
                                                   @Param("excludeId") Long excludeId);

    /**
     * 根据事项类型查询业务事项列表
     * 
     * @param transactionType 事项类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 业务事项列表
     */
    List<BusinessTransactionVO> selectByTransactionType(@Param("transactionType") String transactionType,
                                                       @Param("bookId") Long bookId,
                                                       @Param("tenantId") Long tenantId);

    /**
     * 根据日期范围查询业务事项列表
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 业务事项列表
     */
    List<BusinessTransactionVO> selectByDateRange(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 @Param("bookId") Long bookId,
                                                 @Param("tenantId") Long tenantId);

    /**
     * 根据状态查询业务事项列表
     * 
     * @param transactionStatus 事项状态
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 业务事项列表
     */
    List<BusinessTransactionVO> selectByStatus(@Param("transactionStatus") Integer transactionStatus,
                                              @Param("bookId") Long bookId,
                                              @Param("tenantId") Long tenantId);

    /**
     * 批量更新事项状态
     * 
     * @param ids 事项ID列表
     * @param transactionStatus 事项状态
     * @param updater 更新人
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids,
                         @Param("transactionStatus") Integer transactionStatus,
                         @Param("updater") Long updater);

    /**
     * 批量删除业务事项
     * 
     * @param ids 事项ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 获取事项类型列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 事项类型列表
     */
    List<String> selectTransactionTypes(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 获取来源系统列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 来源系统列表
     */
    List<String> selectSourceSystems(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计事项数量按状态分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<BusinessTransactionVO> countByStatus(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 统计事项数量按类型分组
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计结果
     */
    List<BusinessTransactionVO> countByType(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);
}
