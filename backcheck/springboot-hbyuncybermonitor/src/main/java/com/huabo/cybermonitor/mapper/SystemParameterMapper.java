package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.SystemParameter;
import com.huabo.cybermonitor.vo.SystemParameterQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统参数配置 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface SystemParameterMapper extends BaseMapper<SystemParameter> {

    /**
     * 分页查询系统参数列表
     *
     * @param page    分页对象
     * @param queryVO 查询条件
     * @return 系统参数列表
     */
    IPage<SystemParameter> selectParameterList(Page<SystemParameter> page, @Param("queryVO") SystemParameterQueryVO queryVO);

    /**
     * 根据参数键名查询参数（用于唯一性验证）
     *
     * @param parameterKey 参数键名
     * @param excludeId    排除的参数ID
     * @return 系统参数
     */
    SystemParameter selectByParameterKey(@Param("parameterKey") String parameterKey, @Param("excludeId") String excludeId);

    /**
     * 根据参数分组查询参数列表
     *
     * @param parameterGroup 参数分组
     * @return 系统参数列表
     */
    List<SystemParameter> selectParametersByGroup(@Param("parameterGroup") String parameterGroup);

    /**
     * 根据参数类型查询参数列表
     *
     * @param parameterType 参数类型
     * @return 系统参数列表
     */
    List<SystemParameter> selectParametersByType(@Param("parameterType") String parameterType);

    /**
     * 查询启用的参数列表
     *
     * @return 系统参数列表
     */
    List<SystemParameter> selectEnabledParameters();

    /**
     * 查询系统参数列表
     *
     * @return 系统参数列表
     */
    List<SystemParameter> selectSystemParameters();

    /**
     * 更新参数状态
     *
     * @param parameterId 参数ID
     * @param enabled     是否启用
     * @return 影响行数
     */
    int updateParameterStatus(@Param("parameterId") String parameterId, @Param("enabled") Boolean enabled);

    /**
     * 批量更新参数状态
     *
     * @param parameterIds 参数ID列表
     * @param enabled      是否启用
     * @return 影响行数
     */
    int batchUpdateParameterStatus(@Param("parameterIds") List<String> parameterIds, @Param("enabled") Boolean enabled);

    /**
     * 根据参数键名获取参数值
     *
     * @param parameterKey 参数键名
     * @return 参数值
     */
    String selectParameterValue(@Param("parameterKey") String parameterKey);

    /**
     * 根据参数键名更新参数值
     *
     * @param parameterKey   参数键名
     * @param parameterValue 参数值
     * @return 影响行数
     */
    int updateParameterValue(@Param("parameterKey") String parameterKey, @Param("parameterValue") String parameterValue);

    /**
     * 批量更新参数值
     *
     * @param parameters 参数键值对
     * @return 影响行数
     */
    int batchUpdateParameterValues(@Param("parameters") Map<String, String> parameters);

    /**
     * 查询参数统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> selectParameterStatistics();

    /**
     * 查询参数分组分布统计
     *
     * @return 分组分布统计
     */
    List<Map<String, Object>> selectParameterGroupDistribution();

    /**
     * 查询参数类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> selectParameterTypeDistribution();

    /**
     * 导出系统参数列表
     *
     * @param queryVO 查询条件
     * @return 系统参数列表
     */
    List<SystemParameter> selectParameterListForExport(@Param("queryVO") SystemParameterQueryVO queryVO);

    /**
     * 重置参数为默认值
     *
     * @param parameterId 参数ID
     * @return 影响行数
     */
    int resetParameterToDefault(@Param("parameterId") String parameterId);

    /**
     * 批量重置参数为默认值
     *
     * @param parameterIds 参数ID列表
     * @return 影响行数
     */
    int batchResetParametersToDefault(@Param("parameterIds") List<String> parameterIds);

    /**
     * 更新参数排序
     *
     * @param parameterId 参数ID
     * @param sortOrder   排序号
     * @return 影响行数
     */
    int updateParameterSortOrder(@Param("parameterId") String parameterId, @Param("sortOrder") Integer sortOrder);

    /**
     * 批量更新参数排序
     *
     * @param sortOrders 排序配置
     * @return 影响行数
     */
    int batchUpdateParameterSortOrder(@Param("sortOrders") Map<String, Integer> sortOrders);
}
