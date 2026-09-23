package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.eps.EpsBudgetSystem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算体系表 Mapper 接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetSystemMapper extends BaseMapper<EpsBudgetSystem> {

    /**
     * 分页查询预算体系
     * 
     * @param page 分页参数
     * @param systemName 体系名称（模糊查询）
     * @param systemType 体系类型
     * @param fiscalYear 预算年度
     * @param organizationId 组织ID
     * @param status 状态
     * @return 分页结果
     */
    IPage<EpsBudgetSystem> selectBudgetSystemPage(
            Page<EpsBudgetSystem> page,
            @Param("systemName") String systemName,
            @Param("systemType") String systemType,
            @Param("fiscalYear") Integer fiscalYear,
            @Param("organizationId") Long organizationId,
            @Param("status") String status
    );

    /**
     * 根据组织ID查询预算体系列表
     * 
     * @param organizationId 组织ID
     * @return 预算体系列表
     */
    List<EpsBudgetSystem> selectByOrganizationId(@Param("organizationId") Long organizationId);

    /**
     * 根据体系编码查询预算体系
     * 
     * @param systemCode 体系编码
     * @return 预算体系
     */
    EpsBudgetSystem selectBySystemCode(@Param("systemCode") String systemCode);

    /**
     * 查询默认预算体系
     * 
     * @param organizationId 组织ID
     * @return 默认预算体系
     */
    EpsBudgetSystem selectDefaultSystem(@Param("organizationId") Long organizationId);

    /**
     * 设置默认预算体系
     * 
     * @param systemId 体系ID
     * @param organizationId 组织ID
     * @return 影响行数
     */
    int setDefaultSystem(@Param("systemId") Long systemId, @Param("organizationId") Long organizationId);

    /**
     * 取消默认预算体系
     * 
     * @param organizationId 组织ID
     * @return 影响行数
     */
    int unsetDefaultSystem(@Param("organizationId") Long organizationId);

    /**
     * 根据预算年度查询预算体系
     * 
     * @param fiscalYear 预算年度
     * @param organizationId 组织ID
     * @return 预算体系列表
     */
    List<EpsBudgetSystem> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, 
                                           @Param("organizationId") Long organizationId);

    /**
     * 检查体系编码是否存在
     * 
     * @param systemCode 体系编码
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 数量
     */
    int checkSystemCodeExists(@Param("systemCode") String systemCode, @Param("excludeId") Long excludeId);

    /**
     * 批量更新状态
     * 
     * @param systemIds 体系ID列表
     * @param status 状态
     * @param updatedBy 更新人ID
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("systemIds") List<Long> systemIds, 
                         @Param("status") String status, 
                         @Param("updatedBy") Long updatedBy);
}
