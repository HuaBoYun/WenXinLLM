package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.dto.param.PaymentTermsQueryParam;
import com.financial.sharing.mysql.entity.TblPaymentTerms;
import com.financial.sharing.vo.result.PaymentTermsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账期Mapper(应付模块)
 * @author system
 * @since 2025-01-05
 */
public interface ApPaymentTermsMapper extends BaseMapper<TblPaymentTerms> {

    /**
     * 分页查询账期
     * @param param 查询参数
     * @return 账期列表
     */
    List<PaymentTermsVO> selectPageList(@Param("param") PaymentTermsQueryParam param);

    /**
     * 查询账期详情
     * @param termsId 账期ID
     * @return 账期详情
     */
    PaymentTermsVO selectDetailById(@Param("termsId") String termsId);

    /**
     * 根据编码查询账期
     * @param termsCode 账期编码
     * @return 账期信息
     */
    TblPaymentTerms selectByCode(@Param("termsCode") String termsCode);

    /**
     * 查询供应商账期
     * @param supplierId 供应商ID
     * @return 账期列表
     */
    List<PaymentTermsVO> selectBySupplier(@Param("supplierId") String supplierId);

    /**
     * 查询所有启用的账期
     * @return 账期列表
     */
    List<PaymentTermsVO> selectAllEnabled();
}

