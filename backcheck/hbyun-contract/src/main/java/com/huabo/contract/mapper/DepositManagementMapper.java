package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.DepositManagement;
import com.huabo.contract.vo.DepositManagementQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保证金管理Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface DepositManagementMapper extends BaseMapper<DepositManagement> {

    /**
     * 分页查询保证金管理列表
     * 
     * @param page 分页参数
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<DepositManagement> selectDepositManagementPage(Page<DepositManagement> page, @Param("param") DepositManagementQueryParam queryParam);

    /**
     * 根据招投标项目ID查询保证金列表
     * 
     * @param biddingProjectId 招投标项目ID
     * @return 保证金列表
     */
    List<DepositManagement> selectByBiddingProjectId(@Param("biddingProjectId") Long biddingProjectId);

    /**
     * 根据保证金状态查询保证金列表
     * 
     * @param depositStatus 保证金状态
     * @return 保证金列表
     */
    List<DepositManagement> selectByDepositStatus(@Param("depositStatus") Integer depositStatus);

    /**
     * 查询即将到期的保证金列表（7天内）
     * 
     * @return 即将到期的保证金列表
     */
    List<DepositManagement> selectExpiringSoon();

    /**
     * 查询已逾期的保证金列表
     * 
     * @return 已逾期的保证金列表
     */
    List<DepositManagement> selectOverdue();

    /**
     * 根据保证金类型统计保证金总额
     * 
     * @param depositType 保证金类型
     * @return 保证金总额
     */
    BigDecimal sumDepositAmountByType(@Param("depositType") Integer depositType);

    /**
     * 根据招投标项目ID统计保证金总额
     * 
     * @param biddingProjectId 招投标项目ID
     * @return 保证金总额
     */
    BigDecimal sumDepositAmountByProject(@Param("biddingProjectId") Long biddingProjectId);

    /**
     * 根据保证金状态统计数量
     * 
     * @param depositStatus 保证金状态
     * @return 数量
     */
    Integer countByDepositStatus(@Param("depositStatus") Integer depositStatus);

    /**
     * 查询负责人的保证金列表
     * 
     * @param managerId 负责人ID
     * @return 保证金列表
     */
    List<DepositManagement> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 根据银行名称查询保证金列表
     * 
     * @param bankName 银行名称
     * @return 保证金列表
     */
    List<DepositManagement> selectByBankName(@Param("bankName") String bankName);

    /**
     * 根据保函编号查询保证金
     * 
     * @param guaranteeNo 保函编号
     * @return 保证金信息
     */
    DepositManagement selectByGuaranteeNo(@Param("guaranteeNo") String guaranteeNo);

    /**
     * 批量更新保证金状态
     * 
     * @param ids 保证金ID列表
     * @param depositStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateStatus(@Param("ids") List<Long> ids, @Param("depositStatus") Integer depositStatus, @Param("updateBy") Long updateBy);

    /**
     * 查询保证金统计信息
     * 
     * @return 统计信息
     */
    List<DepositManagement> selectDepositStatistics();
}
