package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.AuxiliaryItemEntity;
import com.financial.sharing.vo.param.AuxiliaryItemQueryParam;
import com.financial.sharing.vo.result.AuxiliaryItemVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 辅助核算项 Mapper接口 - Oracle/达梦版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Component("oracleAuxiliaryItemMapper")
public interface AuxiliaryItemMapper extends BaseMapper<AuxiliaryItemEntity> {

    /**
     * 分页查询辅助核算项
     * 
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<AuxiliaryItemVO> selectAuxiliaryItemPage(Page<AuxiliaryItemVO> page, @Param("param") AuxiliaryItemQueryParam param);

    /**
     * 根据编码查询辅助核算项
     * 
     * @param auxiliaryCode 辅助核算项编码
     * @param auxiliaryType 辅助核算类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 辅助核算项
     */
    AuxiliaryItemEntity selectByCode(@Param("auxiliaryCode") String auxiliaryCode, 
                                   @Param("auxiliaryType") String auxiliaryType,
                                   @Param("bookId") Long bookId, 
                                   @Param("tenantId") Long tenantId, 
                                   @Param("excludeId") Long excludeId);

    /**
     * 根据上级ID查询子项列表
     * 
     * @param parentId 上级ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 子项列表
     */
    List<AuxiliaryItemVO> selectByParentId(@Param("parentId") Long parentId, 
                                         @Param("bookId") Long bookId, 
                                         @Param("tenantId") Long tenantId);

    /**
     * 获取辅助核算项树形结构
     * 
     * @param auxiliaryType 辅助核算类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 树形结构
     */
    List<AuxiliaryItemVO> selectAuxiliaryItemTree(@Param("auxiliaryType") String auxiliaryType,
                                                @Param("bookId") Long bookId, 
                                                @Param("tenantId") Long tenantId);

    /**
     * 根据类型查询辅助核算项
     * 
     * @param auxiliaryType 辅助核算类型
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 辅助核算项列表
     */
    List<AuxiliaryItemVO> selectByType(@Param("auxiliaryType") String auxiliaryType, 
                                     @Param("bookId") Long bookId, 
                                     @Param("tenantId") Long tenantId);

    /**
     * 批量启用/禁用辅助核算项
     * 
     * @param ids 辅助核算项ID列表
     * @param isEnabled 启用状态
     * @param updater 更新人
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, 
                         @Param("isEnabled") Integer isEnabled, 
                         @Param("updater") Long updater);

    /**
     * 批量删除辅助核算项
     * 
     * @param ids 辅助核算项ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<Long> ids, @Param("updater") Long updater);

    /**
     * 检查是否有子项
     * 
     * @param auxiliaryId 辅助核算项ID
     * @param tenantId 租户ID
     * @return 子项数量
     */
    int countChildrenByParentId(@Param("auxiliaryId") Long auxiliaryId, @Param("tenantId") Long tenantId);

    /**
     * 更新末级标识
     * 
     * @param auxiliaryId 辅助核算项ID
     * @param isLeaf 是否末级
     * @param updater 更新人
     * @return 更新数量
     */
    int updateLeafFlag(@Param("auxiliaryId") Long auxiliaryId, 
                      @Param("isLeaf") Integer isLeaf, 
                      @Param("updater") Long updater);

    /**
     * 获取辅助核算类型列表
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 类型列表
     */
    List<String> selectAuxiliaryTypes(@Param("bookId") Long bookId, @Param("tenantId") Long tenantId);

    /**
     * 分页查询辅助核算项目 - 适配PageHelper分页
     *
     * @param param 查询参数
     * @return 辅助核算项目列表
     */
    List<AuxiliaryItemVO> getAuxiliaryItemList(@Param("param") AuxiliaryItemQueryParam param);

    /**
     * 根据类型查询 - 兼容任务要求
     *
     * @param auxiliaryType 辅助核算类型
     * @param bookId 账簿ID
     * @return 辅助核算项目列表
     */
    List<AuxiliaryItemEntity> getByType(@Param("auxiliaryType") String auxiliaryType, @Param("bookId") Long bookId);

    /**
     * 查询树形结构 - 兼容任务要求
     *
     * @param parentId 上级ID
     * @param bookId 账簿ID
     * @return 树形结构
     */
    List<AuxiliaryItemEntity> getTreeStructure(@Param("parentId") Long parentId, @Param("bookId") Long bookId);
}
