package com.financial.sharing.service;

import com.financial.sharing.entity.TblMobileSetting;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TblMobileSettingQueryParam;
import com.financial.sharing.vo.param.TblMobileSettingSaveParam;

import java.util.List;

/**
 * 移动设置服务接口
 *
 * @author system
 * @since 2025-01-30
 */
public interface TblMobileSettingService {

    /**
     * 分页查询移动设置列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<TblMobileSetting>> getList(TblMobileSettingQueryParam param);

    /**
     * 根据ID查询移动设置详情
     *
     * @param settingId 设置ID
     * @return 设置详情
     */
    MyJsonBean getById(String settingId);

    /**
     * 保存或更新移动设置
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblMobileSettingSaveParam param);

    /**
     * 删除移动设置
     *
     * @param settingId 设置ID
     * @return 操作结果
     */
    MyJsonBean delete(String settingId);

    /**
     * 更新启用状态
     *
     * @param settingId 设置ID
     * @param isEnabled 启用状态
     * @return 操作结果
     */
    MyJsonBean updateStatus(String settingId, Integer isEnabled);

    /**
     * 同步到移动端
     *
     * @param settingIds 设置ID列表
     * @return 操作结果
     */
    MyJsonBean syncToMobile(List<String> settingIds);

    /**
     * 获取分组列表
     *
     * @return 分组列表
     */
    MyJsonBean getGroups();

    /**
     * 获取模板列表
     *
     * @param param 查询参数
     * @return 模板列表
     */
    MyJsonBean getTemplates(TblMobileSettingQueryParam param);

    /**
     * 保存模板
     *
     * @param param 模板保存参数
     * @return 操作结果
     */
    MyJsonBean saveTemplate(com.financial.sharing.vo.param.TblMobileSettingTemplateSaveParam param);

    /**
     * 删除模板
     *
     * @param templateId 模板ID
     * @return 操作结果
     */
    MyJsonBean deleteTemplate(String templateId);

    /**
     * 获取版本列表
     *
     * @param settingId 设置ID
     * @return 版本列表
     */
    MyJsonBean getVersions(String settingId);

    /**
     * 获取同步日志
     *
     * @param settingId 设置ID
     * @return 同步日志列表
     */
    MyJsonBean getSyncLogs(String settingId);

    /**
     * 重置为默认值
     *
     * @param settingId 设置ID
     * @return 操作结果
     */
    MyJsonBean resetToDefault(String settingId);

    /**
     * 获取使用统计
     *
     * @return 统计信息
     */
    MyJsonBean getStatistics();

    /**
     * 测试移动设置配置
     *
     * @param settingId 设置ID
     * @param testData 测试数据
     * @return 测试结果
     */
    MyJsonBean test(String settingId, java.util.Map<String, Object> testData);
}
