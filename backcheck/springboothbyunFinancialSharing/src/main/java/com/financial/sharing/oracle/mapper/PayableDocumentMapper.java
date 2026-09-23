package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.dto.param.PayableDocumentQueryParam;
import com.financial.sharing.oracle.entity.TblPayableDocument;
import com.financial.sharing.vo.result.PayableDocumentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 应付单据Mapper (Oracle/达梦数据库)
 * @author system
 * @since 2025-01-05
 */
@Repository("oraclePayableDocumentMapper")
public interface PayableDocumentMapper extends BaseMapper<TblPayableDocument> {

    /**
     * 分页查询应付单据
     * @param param 查询参数
     * @return 应付单据列表
     */
    List<PayableDocumentVO> selectPageList(@Param("param") PayableDocumentQueryParam param);

    /**
     * 查询应付单据详情
     * @param documentId 单据ID
     * @return 应付单据详情
     */
    PayableDocumentVO selectDetailById(@Param("documentId") String documentId);

    /**
     * 统计应付金额汇总
     * @param supplierId 供应商ID(可选)
     * @return 汇总信息
     */
    Map<String, BigDecimal> selectPayableSummary(@Param("supplierId") String supplierId);

    /**
     * 查询逾期单据
     * @return 逾期单据列表
     */
    List<PayableDocumentVO> selectOverdueList();

    /**
     * 查询供应商应付单据
     * @param supplierId 供应商ID
     * @return 应付单据列表
     */
    List<PayableDocumentVO> selectBySupplier(@Param("supplierId") String supplierId);

    /**
     * 更新已付金额
     * @param documentId 单据ID
     * @param paidAmount 已付金额
     * @return 影响行数
     */
    int updatePaidAmount(@Param("documentId") String documentId, @Param("paidAmount") BigDecimal paidAmount);

    /**
     * 查询账龄分析数据
     * @return 账龄分析结果
     */
    List<Map<String, Object>> selectAgingAnalysis();

    /**
     * 查询月度付款趋势(用于现金流预测)
     * @param months 查询最近几个月
     * @return 月度付款数据
     */
    List<Map<String, Object>> selectMonthlyPaymentTrend(@Param("months") Integer months);
}

