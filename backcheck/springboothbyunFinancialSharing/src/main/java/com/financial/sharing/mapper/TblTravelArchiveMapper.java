package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblTravelArchive;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 商旅档案Mapper
 */
public interface TblTravelArchiveMapper extends BaseMapper<TblTravelArchive> {

    /**
     * 根据档案编码查询
     */
    TblTravelArchive selectByArchiveCode(@Param("archiveCode") String archiveCode);

    /**
     * 根据档案类型查询
     */
    List<TblTravelArchive> selectByArchiveType(@Param("archiveType") String archiveType);

    /**
     * 根据供应商编码查询
     */
    List<TblTravelArchive> selectByProviderCode(@Param("providerCode") String providerCode);

    /**
     * 根据供应商名称模糊查询
     */
    List<TblTravelArchive> selectByProviderNameLike(@Param("providerName") String providerName);

    /**
     * 根据星级评定查询
     */
    List<TblTravelArchive> selectByStarLevel(@Param("starLevel") Integer starLevel);

    /**
     * 根据是否启用查询
     */
    List<TblTravelArchive> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);
}
