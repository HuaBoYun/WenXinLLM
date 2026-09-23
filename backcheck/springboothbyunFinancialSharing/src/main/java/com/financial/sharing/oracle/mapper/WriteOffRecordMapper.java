package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.dto.param.WriteOffRecordQueryParam;
import com.financial.sharing.oracle.entity.TblWriteOffRecord;
import com.financial.sharing.vo.result.WriteOffRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 核销记录Mapper
 * @author system
 * @since 2025-01-05
 */
public interface WriteOffRecordMapper extends BaseMapper<TblWriteOffRecord> {

    /**
     * 查询付款单核销记录
     * @param paymentId 付款ID
     * @return 核销记录列表
     */
    List<TblWriteOffRecord> selectByPaymentId(@Param("paymentId") String paymentId);

    /**
     * 查询应付单据核销记录
     * @param documentId 单据ID
     * @return 核销记录列表
     */
    List<TblWriteOffRecord> selectByDocumentId(@Param("documentId") String documentId);

    /**
     * 分页查询核销记录
     * @param param 查询参数
     * @param offset 偏移量
     * @param pageSize 每页条数
     * @return 核销记录列表
     */
    List<WriteOffRecordVO> selectWriteOffRecordPage(
        @Param("param") WriteOffRecordQueryParam param,
        @Param("offset") int offset,
        @Param("pageSize") int pageSize
    );

    /**
     * 查询核销记录总数
     * @param param 查询参数
     * @return 总数
     */
    Long selectWriteOffRecordCount(@Param("param") WriteOffRecordQueryParam param);

    /**
     * 查询核销记录详情
     * @param writeOffId 核销ID
     * @return 核销记录详情
     */
    WriteOffRecordVO selectWriteOffById(@Param("writeOffId") String writeOffId);
}

