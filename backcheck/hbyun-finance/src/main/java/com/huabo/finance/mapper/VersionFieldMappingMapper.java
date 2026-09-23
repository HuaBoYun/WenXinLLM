package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.VersionFieldMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 版本字段映射Mapper接口
 *
 * @author 开发者
 * @date 2025-10-23
 */
@Mapper
public interface VersionFieldMappingMapper extends BaseMapper<VersionFieldMapping> {

    /**
     * 根据版本FID获取所有字段映射
     *
     * @param versionFid 版本FID
     * @return 字段映射列表
     */
    @Select("SELECT * FROM TBL_VERSION_FIELD_MAPPING WHERE VERSION_FID = #{versionFid} AND STATUS = 'ACTIVE' ORDER BY SORT_ORDER")
    List<VersionFieldMapping> selectByVersionFid(@Param("versionFid") String versionFid);

    /**
     * 根据版本FID和源表名获取字段映射
     *
     * @param versionFid 版本FID
     * @param sourceTableName 源表名
     * @return 字段映射列表
     */
    @Select("SELECT * FROM TBL_VERSION_FIELD_MAPPING WHERE VERSION_FID = #{versionFid} AND SOURCE_TABLE_NAME = #{sourceTableName} AND STATUS = 'ACTIVE' ORDER BY SORT_ORDER")
    List<VersionFieldMapping> selectByVersionFidAndSourceTable(@Param("versionFid") String versionFid, @Param("sourceTableName") String sourceTableName);

    /**
     * 根据版本FID和目标表名获取字段映射
     *
     * @param versionFid 版本FID
     * @param targetTableName 目标表名
     * @return 字段映射列表
     */
    @Select("SELECT * FROM TBL_VERSION_FIELD_MAPPING WHERE VERSION_FID = #{versionFid} AND TARGET_TABLE_NAME = #{targetTableName} AND STATUS = 'ACTIVE' ORDER BY SORT_ORDER")
    List<VersionFieldMapping> selectByVersionFidAndTargetTable(@Param("versionFid") String versionFid, @Param("targetTableName") String targetTableName);

    /**
     * 删除版本的所有字段映射
     *
     * @param versionFid 版本FID
     * @return 删除数量
     */
    @Select("DELETE FROM TBL_VERSION_FIELD_MAPPING WHERE VERSION_FID = #{versionFid}")
    int deleteByVersionFid(@Param("versionFid") String versionFid);

    /**
     * 根据 mappingId 查询记录
     *
     * @param mappingId 映射ID
     * @return 字段映射记录
     */
    @Select("SELECT * FROM TBL_VERSION_FIELD_MAPPING WHERE MAPPING_ID = #{mappingId}")
    VersionFieldMapping selectByMappingId(@Param("mappingId") String mappingId);
}

