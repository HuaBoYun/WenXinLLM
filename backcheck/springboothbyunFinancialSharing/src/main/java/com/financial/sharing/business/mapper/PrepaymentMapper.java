package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblPrepayment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 预付款 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizPrepaymentMapper")
public interface PrepaymentMapper extends BaseMapper<TblPrepayment> {

    /**
     * 分页查询预付款
     *
     * @param page 分页参数
     * @param prepaymentCode 预付款单号
     * @param applicantName 申请人
     * @param prepaymentStatus 状态
     * @param prepaymentType 预付款类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param applicantDeptId 部门ID
     * @return 分页结果
     */
    IPage<TblPrepayment> selectPrepaymentPage(Page<TblPrepayment> page,
                                                        @Param("prepaymentCode") String prepaymentCode,
                                                        @Param("applicantName") String applicantName,
                                                        @Param("prepaymentStatus") String prepaymentStatus,
                                                        @Param("prepaymentType") String prepaymentType,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("applicantDeptId") String applicantDeptId);

    /**
     * 根据预付款单号查询
     *
     * @param prepaymentCode 预付款单号
     * @return 预付款
     */
    TblPrepayment selectByPrepaymentCode(@Param("prepaymentCode") String prepaymentCode);

    /**
     * 批量删除预付款
     *
     * @param ids 预付款单ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<String> ids);
}
