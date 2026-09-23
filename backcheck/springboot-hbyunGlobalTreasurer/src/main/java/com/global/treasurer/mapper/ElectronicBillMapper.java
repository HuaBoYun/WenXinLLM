package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.ElectronicBillQueryDTO;
import com.global.treasurer.entity.TblElectronicBill;
import com.global.treasurer.vo.ElectronicBillVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 电子票据Mapper接口
 */
@Mapper
public interface ElectronicBillMapper extends BaseMapper<TblElectronicBill> {

    /**
     * 查询电子票据列表
     */
    List<ElectronicBillVO> selectElectronicBillList(@Param("query") ElectronicBillQueryDTO queryDTO);

    /**
     * 根据ID查询电子票据详情
     */
    ElectronicBillVO selectElectronicBillById(Long electronicBillId);

    /**
     * 根据票据号码查询
     */
    TblElectronicBill selectByBillNumber(String billNumber);

    /**
     * 批量删除电子票据(逻辑删除)
     */
    int deleteElectronicBillByIds(@Param("billIds") Long[] billIds);

    /**
     * 更新签名状态
     */
    int updateSignatureStatus(@Param("electronicBillId") Long electronicBillId,
                              @Param("signatureStatus") String signatureStatus,
                              @Param("digitalSignature") String digitalSignature,
                              @Param("signerName") String signerName,
                              @Param("signerOrg") String signerOrg);

    /**
     * 更新验证状态
     */
    int updateVerificationStatus(@Param("electronicBillId") Long electronicBillId,
                                  @Param("verificationStatus") String verificationStatus);

    /**
     * 更新电子票据状态
     */
    int updateElectronicStatus(@Param("electronicBillId") Long electronicBillId,
                               @Param("electronicStatus") String electronicStatus);

    /**
     * 统计签名状态数量
     */
    Integer countBySignatureStatus(String signatureStatus);

    /**
     * 查询待验证的电子票据
     */
    List<ElectronicBillVO> selectPendingVerification();
}
