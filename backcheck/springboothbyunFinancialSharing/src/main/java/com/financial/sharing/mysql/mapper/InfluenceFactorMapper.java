package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.mysql.entity.InfluenceFactorEntity;
import com.financial.sharing.vo.param.InfluenceFactorQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 影响因素定义 Mapper 接口
 * @author system
 * @since 2024-12-19
 */
@Mapper
@Component("mysqlInfluenceFactorMapper")
public interface InfluenceFactorMapper extends BaseMapper<InfluenceFactorEntity> {

    /**
     * 分页查询影响因素
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<InfluenceFactorEntity> selectInfluenceFactorPage(Page<InfluenceFactorEntity> page, @Param("param") InfluenceFactorQueryParam param);

    /**
     * 根据编码查询影响因素
     * @param factorCode 影响因素编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 影响因素实体
     */
    InfluenceFactorEntity selectByFactorCode(@Param("factorCode") String factorCode, @Param("tenantId") Long tenantId, @Param("bookId") Long bookId);

    /**
     * 根据类型查询影响因素列表
     * @param factorType 影响因素类型
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 影响因素列表
     */
    List<InfluenceFactorEntity> selectByFactorType(@Param("factorType") Integer factorType, @Param("tenantId") Long tenantId, @Param("bookId") Long bookId);

    /**
     * 检查编码是否存在
     * @param factorCode 影响因素编码
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @param excludeId 排除的ID
     * @return 数量
     */
    int checkFactorCodeExists(@Param("factorCode") String factorCode, @Param("tenantId") Long tenantId, @Param("bookId") Long bookId, @Param("excludeId") Long excludeId);

    /**
     * 获取最大排序号
     * @param tenantId 租户ID
     * @param bookId 账簿ID
     * @return 最大排序号
     */
    Integer getMaxSortOrder(@Param("tenantId") Long tenantId, @Param("bookId") Long bookId);

    /**
     * 批量更新启用状态
     * @param factorIds 影响因素ID列表
     * @param isEnabled 启用状态
     * @param updater 更新人
     * @return 更新数量
     */
    int batchUpdateEnabled(@Param("factorIds") List<Long> factorIds, @Param("isEnabled") Integer isEnabled, @Param("updater") Long updater);

    /**
     * 批量删除
     * @param factorIds 影响因素ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("factorIds") List<Long> factorIds, @Param("updater") Long updater);
}
