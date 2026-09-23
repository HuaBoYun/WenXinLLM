package com.management.accountant.service.eps;

import java.util.List;
import java.util.Map;

/**
 * 预算在线编辑服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetOnlineEditService {

    /**
     * 获取预算编辑表格数据
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param templateId 模板ID
     * @param orgId 组织ID
     * @param subjectIds 科目ID列表
     * @param period 期间
     * @return 表格数据
     */
    Map<String, Object> getBudgetTableData(Long systemId, Long versionId, Long templateId, 
                                         Long orgId, List<Long> subjectIds, String period);

    /**
     * 保存单元格数据
     * 
     * @param cellData 单元格数据
     * @return 保存结果
     */
    boolean saveCellData(Map<String, Object> cellData);

    /**
     * 批量保存表格数据
     * 
     * @param tableData 表格数据
     * @return 保存结果
     */
    Map<String, Object> batchSaveTableData(Map<String, Object> tableData);

    /**
     * 获取编辑锁状态
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param orgId 组织ID
     * @param subjectId 科目ID
     * @return 锁状态
     */
    Map<String, Object> getEditLockStatus(Long systemId, Long versionId, Long orgId, Long subjectId);

    /**
     * 申请编辑锁
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param orgId 组织ID
     * @param subjectId 科目ID
     * @param userId 用户ID
     * @return 申请结果
     */
    boolean acquireEditLock(Long systemId, Long versionId, Long orgId, Long subjectId, Long userId);

    /**
     * 释放编辑锁
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param orgId 组织ID
     * @param subjectId 科目ID
     * @param userId 用户ID
     * @return 释放结果
     */
    boolean releaseEditLock(Long systemId, Long versionId, Long orgId, Long subjectId, Long userId);

    /**
     * 强制释放编辑锁
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param orgId 组织ID
     * @param subjectId 科目ID
     * @param operatorUserId 操作用户ID
     * @return 释放结果
     */
    boolean forceReleaseEditLock(Long systemId, Long versionId, Long orgId, Long subjectId, Long operatorUserId);

    /**
     * 获取编辑历史
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param orgId 组织ID
     * @param subjectId 科目ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param current 当前页
     * @param size 每页大小
     * @return 编辑历史
     */
    List<Map<String, Object>> getEditHistory(Long systemId, Long versionId, Long orgId, Long subjectId,
                                           String startDate, String endDate, Long current, Long size);

    /**
     * 撤销编辑操作
     * 
     * @param editHistoryId 编辑历史ID
     * @param userId 用户ID
     * @return 撤销结果
     */
    boolean undoEdit(Long editHistoryId, Long userId);

    /**
     * 重做编辑操作
     * 
     * @param editHistoryId 编辑历史ID
     * @param userId 用户ID
     * @return 重做结果
     */
    boolean redoEdit(Long editHistoryId, Long userId);

    /**
     * 获取在线用户列表
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @return 在线用户列表
     */
    List<Map<String, Object>> getOnlineUsers(Long systemId, Long versionId);

    /**
     * 发送协作消息
     * 
     * @param messageData 消息数据
     * @return 发送结果
     */
    boolean sendCollaborationMessage(Map<String, Object> messageData);

    /**
     * 获取协作消息
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param userId 用户ID
     * @param lastMessageId 最后消息ID
     * @return 消息列表
     */
    List<Map<String, Object>> getCollaborationMessages(Long systemId, Long versionId, Long userId, Long lastMessageId);

    /**
     * 验证数据有效性
     * 
     * @param validationData 验证数据
     * @return 验证结果
     */
    Map<String, Object> validateData(Map<String, Object> validationData);

    /**
     * 设置自动保存设置
     * 
     * @param userId 用户ID
     * @param autoSaveInterval 自动保存间隔
     * @param enableAutoSave 是否启用自动保存
     * @return 设置结果
     */
    boolean setAutoSaveSettings(Long userId, Integer autoSaveInterval, Boolean enableAutoSave);

    /**
     * 获取自动保存设置
     * 
     * @param userId 用户ID
     * @return 自动保存设置
     */
    Map<String, Object> getAutoSaveSettings(Long userId);

    /**
     * 清理过期锁
     * 
     * @param lockExpirationMinutes 锁过期时间(分钟)
     * @return 清理数量
     */
    int cleanupExpiredLocks(Integer lockExpirationMinutes);

    /**
     * 获取编辑统计信息
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计信息
     */
    Map<String, Object> getEditStatistics(Long systemId, Long versionId, String startDate, String endDate);

    /**
     * 创建编辑会话
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param userId 用户ID
     * @return 会话ID
     */
    String createEditSession(Long systemId, Long versionId, Long userId);

    /**
     * 关闭编辑会话
     * 
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 关闭结果
     */
    boolean closeEditSession(String sessionId, Long userId);

    /**
     * 获取编辑会话信息
     * 
     * @param sessionId 会话ID
     * @return 会话信息
     */
    Map<String, Object> getEditSessionInfo(String sessionId);

    /**
     * 更新用户在线状态
     * 
     * @param userId 用户ID
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param isOnline 是否在线
     * @return 更新结果
     */
    boolean updateUserOnlineStatus(Long userId, Long systemId, Long versionId, Boolean isOnline);

    /**
     * 获取单元格编辑权限
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param orgId 组织ID
     * @param subjectId 科目ID
     * @param userId 用户ID
     * @return 权限信息
     */
    Map<String, Object> getCellEditPermission(Long systemId, Long versionId, Long orgId, Long subjectId, Long userId);

    /**
     * 保存编辑历史
     * 
     * @param editHistory 编辑历史数据
     * @return 保存结果
     */
    boolean saveEditHistory(Map<String, Object> editHistory);

    /**
     * 获取数据变更记录
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param orgId 组织ID
     * @param subjectId 科目ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 变更记录
     */
    List<Map<String, Object>> getDataChangeRecords(Long systemId, Long versionId, Long orgId, Long subjectId,
                                                  String startDate, String endDate);

    /**
     * 计算公式结果
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param formulaId 公式ID
     * @param cellData 单元格数据
     * @return 计算结果
     */
    Map<String, Object> calculateFormulaResult(Long systemId, Long versionId, Long formulaId, Map<String, Object> cellData);

    /**
     * 批量计算公式
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param calculationData 计算数据
     * @return 计算结果
     */
    Map<String, Object> batchCalculateFormulas(Long systemId, Long versionId, Map<String, Object> calculationData);

    /**
     * 获取表格配置
     * 
     * @param templateId 模板ID
     * @param userId 用户ID
     * @return 表格配置
     */
    Map<String, Object> getTableConfiguration(Long templateId, Long userId);

    /**
     * 保存表格配置
     * 
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param configuration 配置数据
     * @return 保存结果
     */
    boolean saveTableConfiguration(Long templateId, Long userId, Map<String, Object> configuration);

    /**
     * 导出编辑数据
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param exportParams 导出参数
     * @return 导出结果
     */
    Map<String, Object> exportEditData(Long systemId, Long versionId, Map<String, Object> exportParams);

    /**
     * 导入编辑数据
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param importData 导入数据
     * @return 导入结果
     */
    Map<String, Object> importEditData(Long systemId, Long versionId, Map<String, Object> importData);

    /**
     * 获取编辑模板
     * 
     * @param templateId 模板ID
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @return 编辑模板
     */
    Map<String, Object> getEditTemplate(Long templateId, Long systemId, Long versionId);

    /**
     * 应用编辑模板
     * 
     * @param templateId 模板ID
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param applyParams 应用参数
     * @return 应用结果
     */
    boolean applyEditTemplate(Long templateId, Long systemId, Long versionId, Map<String, Object> applyParams);

    /**
     * 获取编辑提示信息
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param cellPosition 单元格位置
     * @return 提示信息
     */
    Map<String, Object> getEditHints(Long systemId, Long versionId, Map<String, Object> cellPosition);

    /**
     * 检查数据一致性
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @return 一致性检查结果
     */
    Map<String, Object> checkDataConsistency(Long systemId, Long versionId);

    /**
     * 修复数据一致性
     * 
     * @param systemId 预算体系ID
     * @param versionId 预算版本ID
     * @param repairParams 修复参数
     * @return 修复结果
     */
    Map<String, Object> repairDataConsistency(Long systemId, Long versionId, Map<String, Object> repairParams);
}
