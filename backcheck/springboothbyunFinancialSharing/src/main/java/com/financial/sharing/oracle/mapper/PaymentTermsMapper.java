package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.dto.param.PaymentTermsQueryParam;
import com.financial.sharing.oracle.entity.PaymentTermsEntity;
import com.financial.sharing.vo.result.PaymentTermsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 账期设置Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-28
 */
public interface PaymentTermsMapper extends BaseMapper<PaymentTermsEntity> {

    /**
     * 查询账期设置列表（配合 PageHelper 使用）
     */
    List<Map<String, Object>> selectPaymentTermsList(@Param("param") Map<String, Object> param);

    /**
     * 根据ID查询账期设置详情
     */
    Map<String, Object> selectPaymentTermsDetail(@Param("termsId") Long termsId);

    /**
     * 检查账期编码是否存在
     */
    int checkTermsCodeExists(@Param("termsCode") String termsCode, @Param("termsId") Long termsId,
                             @Param("tenantId") Long tenantId);

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
    PaymentTermsVO selectDetailById(@Param("termsId") Long termsId);

    /**
     * 根据编码查询账期
     * @param termsCode 账期编码
     * @return 账期信息
     */
    PaymentTermsEntity selectByCode(@Param("termsCode") String termsCode);

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

