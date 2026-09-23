package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetFileIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算文件集成Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetFileIntegrationMapper extends BaseMapper<BudgetFileIntegration> {

    /**
     * 根据文件类型查询集成列表
     * 
     * @param fileType 文件类型
     * @return 集成列表
     */
    List<BudgetFileIntegration> listByFileType(@Param("fileType") String fileType);

    /**
     * 根据路径类型查询集成列表
     * 
     * @param pathType 路径类型
     * @return 集成列表
     */
    List<BudgetFileIntegration> listByPathType(@Param("pathType") String pathType);

    /**
     * 根据导入导出方向查询集成列表
     * 
     * @param direction 导入导出方向
     * @return 集成列表
     */
    List<BudgetFileIntegration> listByDirection(@Param("direction") String direction);

    /**
     * 查询启用的文件集成列表
     * 
     * @return 集成列表
     */
    List<BudgetFileIntegration> listEnabled();

    /**
     * 更新处理统计
     * 
     * @param fileId 文件集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @return 更新记录数
     */
    int updateProcessStatistics(@Param("fileId") String fileId, 
                               @Param("success") boolean success, 
                               @Param("recordCount") Integer recordCount);
}

