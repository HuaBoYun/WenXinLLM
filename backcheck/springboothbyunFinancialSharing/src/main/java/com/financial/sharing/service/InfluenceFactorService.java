package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.InfluenceFactorQueryParam;
import com.financial.sharing.vo.param.InfluenceFactorSaveParam;
import com.financial.sharing.vo.result.InfluenceFactorVO;

import java.util.List;

/**
 * 影响因素定义服务接口
 * @author system
 * @since 2024-12-19
 */
public interface InfluenceFactorService {

    /**
     * 分页查询影响因素
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<InfluenceFactorVO> getInfluenceFactorPage(InfluenceFactorQueryParam param);

    /**
     * 根据ID查询影响因素详情
     * @param factorId 影响因素ID
     * @return 影响因素详情
     */
    InfluenceFactorVO getInfluenceFactorById(Long factorId);

    /**
     * 根据编码查询影响因素
     * @param factorCode 影响因素编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 影响因素详情
     */
    InfluenceFactorVO getInfluenceFactorByCode(String factorCode, Long tenantId, Long bookId);

    /**
     * 根据类型查询影响因素列表
     * @param factorType 影响因素类型
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 影响因素列表
     */
    List<InfluenceFactorVO> getInfluenceFactorsByType(Integer factorType, Long tenantId, Long bookId);

    /**
     * 保存影响因素
     * @param param 保存参数
     * @param userId 操作用户ID
     * @return 影响因素ID
     */
    Long saveInfluenceFactor(InfluenceFactorSaveParam param, Long userId);

    /**
     * 更新影响因素
     * @param param 更新参数
     * @param userId 操作用户ID
     * @return 是否成功
     */
    boolean updateInfluenceFactor(InfluenceFactorSaveParam param, Long userId);

    /**
     * 删除影响因素
     * @param factorId 影响因素ID
     * @param userId 操作用户ID
     * @return 是否成功
     */
    boolean deleteInfluenceFactor(Long factorId, Long userId);

    /**
     * 批量删除影响因素
     * @param factorIds 影响因素ID列表
     * @param userId 操作用户ID
     * @return 是否成功
     */
    boolean batchDeleteInfluenceFactors(List<Long> factorIds, Long userId);

    /**
     * 启用/禁用影响因素
     * @param factorId 影响因素ID
     * @param isEnabled 启用状态
     * @param userId 操作用户ID
     * @return 是否成功
     */
    boolean updateInfluenceFactorStatus(Long factorId, Integer isEnabled, Long userId);

    /**
     * 批量启用/禁用影响因素
     * @param factorIds 影响因素ID列表
     * @param isEnabled 启用状态
     * @param userId 操作用户ID
     * @return 是否成功
     */
    boolean batchUpdateInfluenceFactorStatus(List<Long> factorIds, Integer isEnabled, Long userId);

    /**
     * 检查编码是否存在
     * @param factorCode 影响因素编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkFactorCodeExists(String factorCode, Long tenantId, Long bookId, Long excludeId);
}
