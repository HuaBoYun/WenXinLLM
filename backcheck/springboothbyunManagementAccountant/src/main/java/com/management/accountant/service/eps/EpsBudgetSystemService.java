package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.eps.EpsBudgetSystem;

import java.util.List;

/**
 * 预算体系管理服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetSystemService extends IService<EpsBudgetSystem> {

    /**
     * 分页查询预算体系
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param systemName 体系名称（模糊查询）
     * @param systemType 体系类型
     * @param fiscalYear 预算年度
     * @param organizationId 组织ID
     * @param status 状态
     * @return 分页结果
     */
    IPage<EpsBudgetSystem> queryBudgetSystemPage(Long current, Long size, String systemName, 
                                               String systemType, Integer fiscalYear, 
                                               Long organizationId, String status);

    /**
     * 创建预算体系
     * 
     * @param budgetSystem 预算体系信息
     * @return 创建结果
     */
    boolean createBudgetSystem(EpsBudgetSystem budgetSystem);

    /**
     * 更新预算体系
     * 
     * @param budgetSystem 预算体系信息
     * @return 更新结果
     */
    boolean updateBudgetSystem(EpsBudgetSystem budgetSystem);

    /**
     * 删除预算体系
     * 
     * @param systemId 体系ID
     * @return 删除结果
     */
    boolean deleteBudgetSystem(Long systemId);

    /**
     * 批量删除预算体系
     * 
     * @param systemIds 体系ID列表
     * @return 删除结果
     */
    boolean batchDeleteBudgetSystem(List<Long> systemIds);

    /**
     * 根据ID查询预算体系详情
     * 
     * @param systemId 体系ID
     * @return 预算体系详情
     */
    EpsBudgetSystem getBudgetSystemById(Long systemId);

    /**
     * 根据体系编码查询预算体系
     * 
     * @param systemCode 体系编码
     * @return 预算体系
     */
    EpsBudgetSystem getBudgetSystemByCode(String systemCode);

    /**
     * 根据组织ID查询预算体系列表
     * 
     * @param organizationId 组织ID
     * @return 预算体系列表
     */
    List<EpsBudgetSystem> getBudgetSystemsByOrganization(Long organizationId);

    /**
     * 查询默认预算体系
     * 
     * @param organizationId 组织ID
     * @return 默认预算体系
     */
    EpsBudgetSystem getDefaultBudgetSystem(Long organizationId);

    /**
     * 设置默认预算体系
     * 
     * @param systemId 体系ID
     * @param organizationId 组织ID
     * @return 设置结果
     */
    boolean setDefaultBudgetSystem(Long systemId, Long organizationId);

    /**
     * 激活预算体系
     * 
     * @param systemId 体系ID
     * @return 激活结果
     */
    boolean activateBudgetSystem(Long systemId);

    /**
     * 停用预算体系
     * 
     * @param systemId 体系ID
     * @return 停用结果
     */
    boolean deactivateBudgetSystem(Long systemId);

    /**
     * 归档预算体系
     * 
     * @param systemId 体系ID
     * @return 归档结果
     */
    boolean archiveBudgetSystem(Long systemId);

    /**
     * 复制预算体系
     * 
     * @param sourceSystemId 源体系ID
     * @param targetSystemCode 目标体系编码
     * @param targetSystemName 目标体系名称
     * @param targetFiscalYear 目标预算年度
     * @return 复制结果
     */
    boolean copyBudgetSystem(Long sourceSystemId, String targetSystemCode, 
                           String targetSystemName, Integer targetFiscalYear);

    /**
     * 检查体系编码是否存在
     * 
     * @param systemCode 体系编码
     * @param excludeId 排除的ID（用于更新时检查）
     * @return 是否存在
     */
    boolean checkSystemCodeExists(String systemCode, Long excludeId);

    /**
     * 批量更新状态
     * 
     * @param systemIds 体系ID列表
     * @param status 状态
     * @param updatedBy 更新人ID
     * @return 更新结果
     */
    boolean batchUpdateStatus(List<Long> systemIds, String status, Long updatedBy);

    /**
     * 根据预算年度查询预算体系
     * 
     * @param fiscalYear 预算年度
     * @param organizationId 组织ID
     * @return 预算体系列表
     */
    List<EpsBudgetSystem> getBudgetSystemsByFiscalYear(Integer fiscalYear, Long organizationId);
}
