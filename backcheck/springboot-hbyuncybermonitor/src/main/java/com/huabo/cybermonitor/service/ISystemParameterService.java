package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.SystemParameter;
import com.huabo.cybermonitor.vo.SystemParameterQueryVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 系统参数配置服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface ISystemParameterService extends IService<SystemParameter> {

    /**
     * 分页查询系统参数列表
     *
     * @param queryVO 查询条件
     * @return 系统参数列表
     */
    IPage<SystemParameter> getParameterList(SystemParameterQueryVO queryVO);

    /**
     * 获取系统参数详情
     *
     * @param parameterId 参数ID
     * @return 系统参数详情
     */
    SystemParameter getParameterDetail(String parameterId);

    /**
     * 新增系统参数
     *
     * @param parameter 系统参数
     * @return 是否成功
     */
    boolean addParameter(SystemParameter parameter);

    /**
     * 更新系统参数
     *
     * @param parameter 系统参数
     * @return 是否成功
     */
    boolean updateParameter(SystemParameter parameter);

    /**
     * 删除系统参数
     *
     * @param parameterId 参数ID
     * @return 是否成功
     */
    boolean deleteParameter(String parameterId);

    /**
     * 批量删除系统参数
     *
     * @param parameterIds 参数ID列表
     * @return 是否成功
     */
    boolean batchDeleteParameter(List<String> parameterIds);

    /**
     * 启用系统参数
     *
     * @param parameterId 参数ID
     * @return 是否成功
     */
    boolean enableParameter(String parameterId);

    /**
     * 禁用系统参数
     *
     * @param parameterId 参数ID
     * @return 是否成功
     */
    boolean disableParameter(String parameterId);

    /**
     * 批量启用系统参数
     *
     * @param parameterIds 参数ID列表
     * @return 是否成功
     */
    boolean batchEnableParameter(List<String> parameterIds);

    /**
     * 批量禁用系统参数
     *
     * @param parameterIds 参数ID列表
     * @return 是否成功
     */
    boolean batchDisableParameter(List<String> parameterIds);

    /**
     * 根据参数分组查询参数列表
     *
     * @param parameterGroup 参数分组
     * @return 系统参数列表
     */
    List<SystemParameter> getParametersByGroup(String parameterGroup);

    /**
     * 根据参数类型查询参数列表
     *
     * @param parameterType 参数类型
     * @return 系统参数列表
     */
    List<SystemParameter> getParametersByType(String parameterType);

    /**
     * 查询启用的参数列表
     *
     * @return 系统参数列表
     */
    List<SystemParameter> getEnabledParameters();

    /**
     * 查询系统参数列表
     *
     * @return 系统参数列表
     */
    List<SystemParameter> getSystemParameters();

    /**
     * 验证参数键名唯一性
     *
     * @param parameterKey 参数键名
     * @param excludeId    排除的参数ID
     * @return 是否重复
     */
    boolean validateParameterKey(String parameterKey, String excludeId);

    /**
     * 根据参数键名获取参数值
     *
     * @param parameterKey 参数键名
     * @return 参数值
     */
    String getParameterValue(String parameterKey);

    /**
     * 根据参数键名获取参数值（带默认值）
     *
     * @param parameterKey 参数键名
     * @param defaultValue 默认值
     * @return 参数值
     */
    String getParameterValue(String parameterKey, String defaultValue);

    /**
     * 根据参数键名更新参数值
     *
     * @param parameterKey   参数键名
     * @param parameterValue 参数值
     * @return 是否成功
     */
    boolean updateParameterValue(String parameterKey, String parameterValue);

    /**
     * 批量更新参数值
     *
     * @param parameters 参数键值对
     * @return 是否成功
     */
    boolean batchUpdateParameterValues(Map<String, String> parameters);

    /**
     * 重置参数为默认值
     *
     * @param parameterId 参数ID
     * @return 是否成功
     */
    boolean resetParameterToDefault(String parameterId);

    /**
     * 批量重置参数为默认值
     *
     * @param parameterIds 参数ID列表
     * @return 是否成功
     */
    boolean batchResetParametersToDefault(List<String> parameterIds);

    /**
     * 更新参数排序
     *
     * @param parameterId 参数ID
     * @param sortOrder   排序号
     * @return 是否成功
     */
    boolean updateParameterSortOrder(String parameterId, Integer sortOrder);

    /**
     * 批量更新参数排序
     *
     * @param sortOrders 排序配置
     * @return 是否成功
     */
    boolean batchUpdateParameterSortOrder(Map<String, Integer> sortOrders);

    /**
     * 验证参数值
     *
     * @param parameter 系统参数
     * @return 验证结果
     */
    Map<String, Object> validateParameterValue(SystemParameter parameter);

    /**
     * 获取参数统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getParameterStatistics();

    /**
     * 获取参数分组分布统计
     *
     * @return 分组分布统计
     */
    List<Map<String, Object>> getParameterGroupDistribution();

    /**
     * 获取参数类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getParameterTypeDistribution();

    /**
     * 导出系统参数列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportParameterList(SystemParameterQueryVO queryVO, HttpServletResponse response);

    /**
     * 下载参数导入模板
     *
     * @param response HTTP响应
     */
    void downloadParameterTemplate(HttpServletResponse response);

    /**
     * 批量导入系统参数
     *
     * @param file 导入文件
     * @return 导入结果
     */
    Map<String, Object> importParameterList(MultipartFile file);

    /**
     * 获取参数类型标签
     *
     * @param parameterType 参数类型
     * @return 类型标签
     */
    String getParameterTypeLabel(String parameterType);

    /**
     * 获取参数分组标签
     *
     * @param parameterGroup 参数分组
     * @return 分组标签
     */
    String getParameterGroupLabel(String parameterGroup);

    /**
     * 刷新系统参数缓存
     *
     * @return 是否成功
     */
    boolean refreshParameterCache();

    /**
     * 备份系统参数配置
     *
     * @param response HTTP响应
     */
    void backupParameterConfig(HttpServletResponse response);

    /**
     * 恢复系统参数配置
     *
     * @param file 备份文件
     * @return 恢复结果
     */
    Map<String, Object> restoreParameterConfig(MultipartFile file);
}
