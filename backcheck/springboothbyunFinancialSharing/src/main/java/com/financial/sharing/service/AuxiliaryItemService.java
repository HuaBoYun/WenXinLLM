package com.financial.sharing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.financial.sharing.dto.AuxiliaryItemBatchParam;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.AuxiliaryItemQueryParam;
import com.financial.sharing.vo.param.AuxiliaryItemSaveParam;
import com.financial.sharing.vo.result.AuxiliaryItemVO;

import java.util.List;

/**
 * 辅助核算项服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface AuxiliaryItemService {

    /**
     * 分页查询辅助核算项
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<AuxiliaryItemVO> getAuxiliaryItemPage(AuxiliaryItemQueryParam param);

    /**
     * 保存或更新辅助核算项
     * 
     * @param param 保存参数
     * @return 保存结果
     */
    AuxiliaryItemVO saveOrUpdateAuxiliaryItem(AuxiliaryItemSaveParam param);

    /**
     * 根据ID查询辅助核算项详情
     * 
     * @param auxiliaryId 辅助核算项ID
     * @return 辅助核算项详情
     */
    AuxiliaryItemVO getAuxiliaryItemById(Long auxiliaryId);

    /**
     * 删除辅助核算项
     * 
     * @param auxiliaryId 辅助核算项ID
     * @return 是否成功
     */
    boolean deleteAuxiliaryItem(Long auxiliaryId);

    /**
     * 批量删除辅助核算项
     * 
     * @param auxiliaryIds 辅助核算项ID列表
     * @return 是否成功
     */
    boolean batchDeleteAuxiliaryItems(List<Long> auxiliaryIds);

    /**
     * 启用/禁用辅助核算项
     * 
     * @param auxiliaryId 辅助核算项ID
     * @param isEnabled 启用状态
     * @return 是否成功
     */
    boolean updateAuxiliaryItemStatus(Long auxiliaryId, Integer isEnabled);

    /**
     * 批量启用/禁用辅助核算项
     * 
     * @param auxiliaryIds 辅助核算项ID列表
     * @param isEnabled 启用状态
     * @return 是否成功
     */
    boolean batchUpdateAuxiliaryItemStatus(List<Long> auxiliaryIds, Integer isEnabled);

    /**
     * 检查编码是否存在
     * 
     * @param auxiliaryCode 辅助核算项编码
     * @param auxiliaryType 辅助核算类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkAuxiliaryCodeExists(String auxiliaryCode, String auxiliaryType, Long bookId, Long tenantId, Long excludeId);

    /**
     * 获取辅助核算项树形结构
     * 
     * @param auxiliaryType 辅助核算类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 树形结构
     */
    List<AuxiliaryItemVO> getAuxiliaryItemTree(String auxiliaryType, Long bookId, Long tenantId);

    /**
     * 根据类型查询辅助核算项
     * 
     * @param auxiliaryType 辅助核算类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 辅助核算项列表
     */
    List<AuxiliaryItemVO> getAuxiliaryItemsByType(String auxiliaryType, Long bookId, Long tenantId);

    /**
     * 根据上级ID查询子项列表
     * 
     * @param parentId 上级ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 子项列表
     */
    List<AuxiliaryItemVO> getAuxiliaryItemsByParentId(Long parentId, Long bookId, Long tenantId);

    /**
     * 获取辅助核算类型列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 类型列表
     */
    List<String> getAuxiliaryTypes(Long bookId, Long tenantId);

    /**
     * 批量操作辅助核算项
     *
     * @param param 批量操作参数
     * @return 是否成功
     */
    boolean batchOperation(AuxiliaryItemBatchParam param);
}
