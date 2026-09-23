package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.dto.param.PrepaymentQueryParam;
import com.financial.sharing.mysql.entity.TblPrepayment;
import com.financial.sharing.vo.result.PrepaymentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预付款Mapper
 * @author system
 * @since 2025-01-05
 */
public interface PrepaymentMapper extends BaseMapper<TblPrepayment> {

    /**
     * 查询供应商预付款
     * @param supplierId 供应商ID
     * @return 预付款列表
     */
    List<TblPrepayment> selectBySupplier(@Param("supplierId") String supplierId);

    /**
     * 查询可用预付款(未完全冲销)
     * @param supplierId 供应商ID
     * @return 预付款列表
     */
    List<TblPrepayment> selectAvailableBySupplier(@Param("supplierId") String supplierId);

    /**
     * 分页查询预付款列表
     * @param param 查询参数
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 预付款VO列表
     */
    List<PrepaymentVO> selectPrepaymentPage(@Param("param") PrepaymentQueryParam param,
                                          @Param("offset") int offset,
                                          @Param("pageSize") Integer pageSize);

    /**
     * 查询预付款总数
     * @param param 查询参数
     * @return 总数
     */
    Long selectPrepaymentCount(@Param("param") PrepaymentQueryParam param);

    /**
     * 根据ID查询预付款详情
     * @param prepaymentId 预付款ID
     * @return 预付款VO
     */
    PrepaymentVO selectPrepaymentById(@Param("prepaymentId") String prepaymentId);
}

