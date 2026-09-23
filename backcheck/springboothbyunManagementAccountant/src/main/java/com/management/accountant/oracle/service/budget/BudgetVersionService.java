package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetVersion;

import java.util.List;
import java.util.Map;

/**
 * 预算版本Service接口
 *
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetVersionService extends IService<BudgetVersion> {

    /**
     * 根据版本编码查询版本
     */
    BudgetVersion getByVersionCode(String versionCode);

    /**
     * 查询当前版本（按财年）
     */
    BudgetVersion getCurrentVersion(Integer fiscalYear);

    /**
     * 根据财年查询版本列表
     */
    List<BudgetVersion> getByFiscalYear(Integer fiscalYear);

    /**
     * 创建预算版本
     *
     * @param version 预算版本
     * @return 是否成功
     */
    boolean createVersion(BudgetVersion version);

    /**
     * 更新预算版本
     *
     * @param version 预算版本
     * @return 是否成功
     */
    boolean updateVersion(BudgetVersion version);

    /**
     * 设置当前版本
     *
     * @param versionId 版本ID
     * @return 是否成功
     */
    boolean setCurrentVersion(String versionId);

    /**
     * 归档版本
     *
     * @param versionId 版本ID
     * @return 是否成功
     */
    boolean archiveVersion(String versionId);

    /**
     * 锁定版本
     *
     * @param versionId 版本ID
     * @return 是否成功
     */
    boolean lockVersion(String versionId);

    /**
     * 复制版本
     *
     * @param versionId 版本ID
     * @param newVersionName 新版本名称
     * @return 新版本
     */
    BudgetVersion copyVersion(String versionId, String newVersionName);

    /**
     * 批量删除版本
     *
     * @param versionIds 版本ID列表
     * @return 是否成功
     */
    boolean batchDeleteVersions(List<String> versionIds);

    /**
     * 分页查询版本列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param version 查询条件
     * @return 版本列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetVersion> pageQuery(
        int pageNum, int pageSize, BudgetVersion version);

    /**
     * 版本对比
     *
     * @param version1 版本1
     * @param version2 版本2
     * @return 对比结果
     */
    Map<String, Object> compareVersions(String version1, String version2);

    /**
     * 版本回滚
     *
     * @param versionId 版本ID
     * @return 是否成功
     */
    boolean rollbackVersion(String versionId);

    /**
     * 根据预算ID查询版本列表
     *
     * @param budgetId 预算ID
     * @return 版本列表
     */
    List<BudgetVersion> listByBudgetId(String budgetId);

    /**
     * 获取版本详情
     *
     * @param versionId 版本ID
     * @return 版本详情
     */
    BudgetVersion getVersionDetail(String versionId);

    /**
     * 发布版本
     *
     * @param versionId 版本ID
     * @return 是否成功
     */
    boolean publishVersion(String versionId);
}

