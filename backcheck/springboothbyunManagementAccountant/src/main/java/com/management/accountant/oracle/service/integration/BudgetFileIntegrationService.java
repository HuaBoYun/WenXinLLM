package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetFileIntegration;

import java.util.List;

/**
 * 预算文件集成Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetFileIntegrationService extends IService<BudgetFileIntegration> {

    /**
     * 根据文件类型查询集成列表
     * 
     * @param fileType 文件类型
     * @return 集成列表
     */
    List<BudgetFileIntegration> listByFileType(String fileType);

    /**
     * 根据路径类型查询集成列表
     * 
     * @param pathType 路径类型
     * @return 集成列表
     */
    List<BudgetFileIntegration> listByPathType(String pathType);

    /**
     * 根据导入导出方向查询集成列表
     * 
     * @param direction 导入导出方向
     * @return 集成列表
     */
    List<BudgetFileIntegration> listByDirection(String direction);

    /**
     * 查询启用的文件集成列表
     * 
     * @return 集成列表
     */
    List<BudgetFileIntegration> listEnabled();

    /**
     * 执行文件处理
     * 
     * @param fileId 文件集成ID
     * @return 处理结果
     */
    boolean executeProcess(String fileId);

    /**
     * 导入文件数据
     * 
     * @param fileId 文件集成ID
     * @param filePath 文件路径
     * @return 导入结果
     */
    boolean importFile(String fileId, String filePath);

    /**
     * 导出文件数据
     * 
     * @param fileId 文件集成ID
     * @return 导出文件路径
     */
    String exportFile(String fileId);

    /**
     * 更新处理统计
     * 
     * @param fileId 文件集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @return 是否更新成功
     */
    boolean updateProcessStatistics(String fileId, boolean success, Integer recordCount);

    /**
     * 批量启用/禁用文件集成
     * 
     * @param fileIds 文件集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> fileIds, boolean enabled);
}

