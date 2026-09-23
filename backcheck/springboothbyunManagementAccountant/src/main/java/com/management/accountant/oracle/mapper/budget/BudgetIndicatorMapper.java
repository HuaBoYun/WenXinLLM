package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetIndicator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算指标Mapper接口
 * 
 * @description 预算指标数据访问层
 * @author AI Assistant
 * @date 2025-12-31
 */
@Mapper
public interface BudgetIndicatorMapper extends BaseMapper<BudgetIndicator> {

    /**
     * 根据指标编码查询
     * 
     * @param indicatorCode 指标编码
     * @param companyId 公司ID
     * @return 指标信息
     */
    BudgetIndicator selectByCode(@Param("indicatorCode") String indicatorCode,
                                  @Param("companyId") String companyId);

    /**
     * 查询完整指标树
     * 
     * @param companyId 公司ID
     * @return 树形结构数据
     */
    List<Map<String, Object>> selectIndicatorTreeFull(@Param("companyId") String companyId);

    /**
     * 分页查询指标列表
     * 
     * @param params 查询参数
     * @return 指标列表
     */
    List<BudgetIndicator> selectPageList(@Param("params") Map<String, Object> params);

    /**
     * 统计指标数量
     * 
     * @param params 查询参数
     * @return 数量
     */
    int countByParams(@Param("params") Map<String, Object> params);

    /**
     * 启用指标
     * 
     * @param indicatorId 指标ID
     * @param companyId 公司ID
     * @return 影响行数
     */
    int enableIndicator(@Param("indicatorId") String indicatorId,
                        @Param("companyId") String companyId);

    /**
     * 禁用指标
     * 
     * @param indicatorId 指标ID
     * @param companyId 公司ID
     * @return 影响行数
     */
    int disableIndicator(@Param("indicatorId") String indicatorId,
                         @Param("companyId") String companyId);

    /**
     * 批量删除指标
     * 
     * @param ids ID列表
     * @param companyId 公司ID
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<String> ids, @Param("companyId") String companyId);
}

