package com.financial.sharing.oracle.mapper;

import com.financial.sharing.oracle.entity.SystemParamsEntity;
import com.financial.sharing.vo.param.SystemParamsQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统参数配置Mapper
 * @author system
 * @since 2024-12-19
 */
public interface SystemParamsMapper {

    /**
     * 分页查询系统参数 (MyBatis-Plus分页，已弃用)
     * @param page 分页对象
     * @param param 查询参数
     * @return 分页结果
     */
    // IPage<SystemParamsEntity> getSystemParamsPage(Page<SystemParamsEntity> page, @Param("param") SystemParamsQueryParam param);

    /**
     * 分页查询系统参数 (PageHelper分页)
     * @param param 查询参数
     * @return 系统参数列表
     */
    List<SystemParamsEntity> selectSystemParamsList(@Param("param") SystemParamsQueryParam param);

    /**
     * 根据参数编码查询
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 参数信息
     */
    SystemParamsEntity getByParamCode(@Param("paramCode") String paramCode,
                                     @Param("tenantId") Long tenantId,
                                     @Param("bookId") Long bookId);

    /**
     * 根据分类查询参数列表
     * @param categoryCode 分类编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 参数列表
     */
    List<SystemParamsEntity> getByCategoryCode(@Param("categoryCode") String categoryCode,
                                             @Param("tenantId") Long tenantId,
                                             @Param("bookId") Long bookId);

    /**
     * 批量更新状态
     * @param ids ID列表
     * @param isEnabled 启用状态
     * @param tenantId 租户ID
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("isEnabled") Integer isEnabled, @Param("tenantId") Long tenantId);

    /**
     * 检查参数编码是否存在
     * @param paramCode 参数编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    int checkParamCodeExists(@Param("paramCode") String paramCode,
                            @Param("tenantId") Long tenantId,
                            @Param("bookId") Long bookId,
                            @Param("excludeId") Long excludeId);

    /**
     * 根据ID查询
     * @param id ID
     * @return 实体对象
     */
    SystemParamsEntity selectById(@Param("id") Long id);

    /**
     * 插入记录
     * @param entity 实体对象
     * @return 影响行数
     */
    int insert(SystemParamsEntity entity);

    /**
     * 根据ID更新记录
     * @param entity 实体对象
     * @return 影响行数
     */
    int updateById(SystemParamsEntity entity);

}