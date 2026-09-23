package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblProjectBudget;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 项目预算Mapper
 */
public interface TblProjectBudgetMapper extends BaseMapper<TblProjectBudget> {
    
    /**
     * 根据项目ID查询预算列表
     */
    List<TblProjectBudget> selectByProjectId(@Param("projectId") String projectId);
    
    /**
     * 根据项目ID和年月查询预算
     */
    TblProjectBudget selectByProjectAndYearMonth(@Param("projectId") String projectId, 
                                                 @Param("year") Integer year, 
                                                 @Param("month") Integer month);
    
    /**
     * 根据预算编码查询
     */
    TblProjectBudget selectByBudgetCode(@Param("budgetCode") String budgetCode);
    
    /**
     * 删除项目的所有预算
     */
    int deleteByProjectId(@Param("projectId") String projectId);
}

