package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.SystemParamsQueryParam;
import com.financial.sharing.vo.param.SystemParamsSaveParam;
import com.financial.sharing.vo.result.SystemParamsVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统参数配置Service接口
 * @author system
 * @since 2024-12-19
 */
public interface SystemParamsService {

    /**
     * 分页查询系统参数
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<SystemParamsVO> getSystemParamsPage(SystemParamsQueryParam param);

    /**
     * 根据ID查询参数详情
     * @param id 参数ID
     * @return 参数详情
     */
    SystemParamsVO getSystemParamsById(Long id);

    /**
     * 保存系统参数
     * @param param 保存参数
     * @return 保存结果
     */
    boolean saveSystemParams(SystemParamsSaveParam param);

    /**
     * 更新系统参数
     * @param param 更新参数
     * @return 更新结果
     */
    boolean updateSystemParams(SystemParamsSaveParam param);

    /**
     * 删除系统参数
     * @param id 参数ID
     * @return 删除结果
     */
    boolean deleteSystemParams(Long id);

    /**
     * 批量删除系统参数
     * @param ids ID列表
     * @return 删除结果
     */
    boolean batchDeleteSystemParams(List<Long> ids);

    /**
     * 更新参数状态
     * @param id 参数ID
     * @param isEnabled 启用状态
     * @return 更新结果
     */
    boolean updateSystemParamsStatus(Long id, Integer isEnabled);

    /**
     * 批量更新参数状态
     * @param ids ID列表
     * @param isEnabled 启用状态
     * @return 更新结果
     */
    boolean batchUpdateSystemParamsStatus(List<Long> ids, Integer isEnabled);

    /**
     * 导入系统参数
     * @param file 导入文件
     * @return 导入结果
     */
    String importSystemParams(org.springframework.web.multipart.MultipartFile file);

    /**
     * 导出系统参数
     * @param response HTTP响应
     * @param param 查询参数
     */
    void exportSystemParams(HttpServletResponse response, SystemParamsQueryParam param);

    /**
     * 根据参数编码查询参数值
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 参数值
     */
    String getParamValue(String paramCode, Long tenantId, Long bookId);

    /**
     * 根据分类查询参数列表
     * @param categoryCode 分类编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 参数列表
     */
    List<SystemParamsVO> getParamsByCategory(String categoryCode, Long tenantId, Long bookId);
}