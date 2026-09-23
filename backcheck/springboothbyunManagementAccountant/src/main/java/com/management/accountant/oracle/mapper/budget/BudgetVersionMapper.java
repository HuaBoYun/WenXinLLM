package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算版本Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetVersionMapper extends BaseMapper<BudgetVersion> {

    /**
     * 根据版本编码查询版本
     * 
     * @param versionCode 版本编码
     * @return 预算版本
     */
    BudgetVersion selectByVersionCode(@Param("versionCode") String versionCode);

    /**
     * 查询当前版本（按财年）
     */
    BudgetVersion selectCurrentVersion(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 根据财年查询版本列表
     */
    List<BudgetVersion> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    /**
     * 更新当前版本标志
     *
     * @param versionId 版本ID
     * @param isCurrent 是否当前版本
     * @return 更新数量
     */
    int updateCurrentFlag(@Param("versionId") String versionId, @Param("isCurrent") Boolean isCurrent);

    /**
     * 清除当前版本
     *
     * @param budgetYear 预算年度
     * @return 更新数量
     */
    int clearCurrentVersion(@Param("budgetYear") Integer budgetYear);

    /**
     * 设置当前版本
     *
     * @param versionId 版本ID
     * @return 更新数量
     */
    int setCurrentVersion(@Param("versionId") String versionId);

    /**
     * 批量删除版本
     *
     * @param versionIds 版本ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("versionIds") List<String> versionIds);
}

