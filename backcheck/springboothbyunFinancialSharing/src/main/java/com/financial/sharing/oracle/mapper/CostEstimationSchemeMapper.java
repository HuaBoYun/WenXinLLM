package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.CostEstimationSchemeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 成本估算方案Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
public interface CostEstimationSchemeMapper extends BaseMapper<CostEstimationSchemeEntity> {

    /**
     * 分页查询估算方案列表
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param schemeCode 方案编码
     * @param schemeName 方案名称
     * @param schemeType 方案类型
     * @param isEnabled 启用状态
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 方案列表
     */
    List<Map<String, Object>> selectSchemeListWithPagination(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("schemeCode") String schemeCode,
            @Param("schemeName") String schemeName,
            @Param("schemeType") String schemeType,
            @Param("isEnabled") Integer isEnabled,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询总记录数
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param schemeCode 方案编码
     * @param schemeName 方案名称
     * @param schemeType 方案类型
     * @param isEnabled 启用状态
     * @return 总记录数
     */
    int countSchemeList(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("schemeCode") String schemeCode,
            @Param("schemeName") String schemeName,
            @Param("schemeType") String schemeType,
            @Param("isEnabled") Integer isEnabled
    );

    /**
     * 根据方案编码查询方案
     *
     * @param schemeCode 方案编码
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 方案信息
     */
    Map<String, Object> selectBySchemeCode(
            @Param("schemeCode") String schemeCode,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 批量更新方案状态
     *
     * @param schemeIds 方案ID列表
     * @param isEnabled 启用状态
     * @return 更新数量
     */
    int batchUpdateStatus(
            @Param("schemeIds") List<String> schemeIds,
            @Param("isEnabled") Integer isEnabled
    );

    /**
     * 检查方案编码是否存在
     *
     * @param schemeCode 方案编码
     * @param schemeId 方案ID（更新时排除自己）
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 数量
     */
    int checkSchemeCodeExists(
            @Param("schemeCode") String schemeCode,
            @Param("schemeId") String schemeId,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );
}

