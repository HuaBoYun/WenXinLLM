package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblExpenseProvision;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 费用预提 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizExpenseProvisionMapper")
public interface ExpenseProvisionMapper extends BaseMapper<TblExpenseProvision> {

    /**
     * 分页查询预提单
     *
     * @param page 分页参数
     * @param provisionCode 预提单号
     * @param applicantName 申请人
     * @param provisionStatus 状态
     * @param provisionType 预提类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param applicantDeptId 部门ID
     * @return 分页结果
     */
    IPage<TblExpenseProvision> selectExpenseProvisionPage(Page<TblExpenseProvision> page,
                                                              @Param("provisionCode") String provisionCode,
                                                              @Param("applicantName") String applicantName,
                                                              @Param("provisionStatus") String provisionStatus,
                                                              @Param("provisionType") String provisionType,
                                                              @Param("startDate") LocalDate startDate,
                                                              @Param("endDate") LocalDate endDate,
                                                              @Param("applicantDeptId") String applicantDeptId);

    /**
     * 根据预提单号查询
     *
     * @param provisionCode 预提单号
     * @return 预提单
     */
    TblExpenseProvision selectByProvisionCode(@Param("provisionCode") String provisionCode);

    /**
     * 批量删除预提单
     *
     * @param ids 预提单ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<String> ids);
}
