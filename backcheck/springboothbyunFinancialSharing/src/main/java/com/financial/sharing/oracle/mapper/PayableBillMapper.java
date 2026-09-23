package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.dto.param.PayableBillQueryParam;
import com.financial.sharing.oracle.entity.TblPayableBill;
import com.financial.sharing.vo.result.PayableBillVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 应付票据Mapper (Oracle版本)
 * @author system
 * @since 2025-01-14
 */
public interface PayableBillMapper extends BaseMapper<TblPayableBill> {

    /**
     * 分页查询应付票据
     * @param param 查询参数
     * @return 应付票据列表
     */
    List<PayableBillVO> selectPageList(@Param("param") PayableBillQueryParam param);

    /**
     * 查询应付票据详情
     * @param billId 票据ID
     * @return 应付票据详情
     */
    PayableBillVO selectDetailById(@Param("billId") String billId);

    /**
     * 统计票据金额汇总
     * @return 汇总信息
     */
    Map<String, BigDecimal> selectBillSummary();

    /**
     * 查询即将到期票据
     * @param days 天数
     * @return 票据列表
     */
    List<PayableBillVO> selectDueSoonList(@Param("days") Integer days);

    /**
     * 查询供应商票据
     * @param supplierId 供应商ID
     * @return 票据列表
     */
    List<PayableBillVO> selectBySupplier(@Param("supplierId") String supplierId);

    /**
     * 更新票据状态
     * @param billId 票据ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("billId") String billId, @Param("status") String status);

    /**
     * 查询到期提醒票据
     * @param days 天数
     * @param includeOverdue 是否包含已过期
     * @param billType 票据类型
     * @return 票据列表
     */
    List<PayableBillVO> selectDueReminders(@Param("days") int days,
                                           @Param("includeOverdue") boolean includeOverdue,
                                           @Param("billType") String billType);

    /**
     * 根据状态统计票据数量
     * @param status 状态
     * @return 数量
     */
    Integer countByStatus(@Param("status") String status);

    /**
     * 统计即将到期票据数量
     * @param days 天数
     * @return 数量
     */
    Integer countDueSoon(@Param("days") int days);

    /**
     * 统计票据总金额
     * @return 总金额
     */
    BigDecimal sumAmount();
}
