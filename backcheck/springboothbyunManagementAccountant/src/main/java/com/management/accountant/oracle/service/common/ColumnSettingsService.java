package com.management.accountant.oracle.service.common;

import java.util.List;
import java.util.Map;

/**
 * 列设置Service接口
 * 
 * @description 提供统一的列设置功能
 * @author AI Assistant
 * @date 2026-02-06
 */
public interface ColumnSettingsService {

    /**
     * 获取列设置
     * 
     * @param pageCode 页面编码
     * @param userId 用户ID
     * @param companyId 公司ID
     * @return 列设置信息
     */
    Map<String, Object> getColumnSettings(String pageCode, String userId, String companyId);

    /**
     * 保存列设置
     * 
     * @param pageCode 页面编码
     * @param settings 列设置信息
     * @param userId 用户ID
     * @param companyId 公司ID
     * @return 是否成功
     */
    boolean saveColumnSettings(String pageCode, Map<String, Object> settings, 
                              String userId, String companyId);

    /**
     * 重置列设置
     * 
     * @param pageCode 页面编码
     * @param userId 用户ID
     * @param companyId 公司ID
     * @return 是否成功
     */
    boolean resetColumnSettings(String pageCode, String userId, String companyId);

    /**
     * 获取默认列设置
     * 
     * @param pageCode 页面编码
     * @return 默认列设置
     */
    Map<String, Object> getDefaultColumnSettings(String pageCode);

    /**
     * 批量更新列设置
     * 
     * @param settingsList 列设置列表
     * @param userId 用户ID
     * @param companyId 公司ID
     * @return 是否成功
     */
    boolean batchUpdateColumnSettings(List<Map<String, Object>> settingsList, 
                                     String userId, String companyId);
}

