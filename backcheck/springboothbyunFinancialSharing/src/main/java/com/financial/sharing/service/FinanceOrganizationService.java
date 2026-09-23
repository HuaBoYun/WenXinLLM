package com.financial.sharing.service;

import com.financial.sharing.dto.param.FinanceOrganizationDTO;
import com.financial.sharing.vo.result.FinanceOrganizationVO;

import java.util.List;

/**
 * 财务组织Service接口
 *
 * @author system
 * @since 2024-12-19
 */
public interface FinanceOrganizationService {

    /**
     * 获取组织树形结构
     *
     * @param tenantId 租户ID
     * @param bookId   账簿ID
     * @return 组织树形列表
     */
    List<FinanceOrganizationVO> getOrganizationTree(Long tenantId, Long bookId);

    /**
     * 根据ID获取组织详情
     *
     * @param id 主键ID
     * @return 组织详情
     */
    FinanceOrganizationVO getOrganizationById(Long id);

    /**
     * 保存或更新组织
     *
     * @param dto 组织参数
     * @return 操作结果
     */
    boolean saveOrUpdateOrganization(FinanceOrganizationDTO dto);

    /**
     * 删除组织
     *
     * @param id 组织ID
     * @return 操作结果
     */
    boolean deleteOrganization(Long id);

    /**
     * 更新组织状态
     *
     * @param id         组织ID
     * @param isEnabled  启用状态
     * @return 操作结果
     */
    boolean updateOrganizationStatus(Long id, Integer isEnabled);

    /**
     * 同步组织数据
     *
     * @return 同步结果
     */
    boolean syncOrganizations();
}