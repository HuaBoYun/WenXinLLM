package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblContract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 合同 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizContractMapper")
public interface ContractMapper extends BaseMapper<TblContract> {

    /**
     * 分页查询合同
     *
     * @param page 分页参数
     * @param contractCode 合同编号
     * @param contractName 合同名称
     * @param contractType 合同类型
     * @param contractStatus 合同状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页结果
     */
    IPage<TblContract> selectContractPage(Page<TblContract> page,
                                                 @Param("contractCode") String contractCode,
                                                 @Param("contractName") String contractName,
                                                 @Param("contractType") String contractType,
                                                 @Param("contractStatus") String contractStatus,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    /**
     * 根据合同编号查询
     *
     * @param contractCode 合同编号
     * @return 合同
     */
    TblContract selectByContractCode(@Param("contractCode") String contractCode);

    /**
     * 批量删除合同
     *
     * @param ids 合同ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<String> ids);
}
