package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblTableStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Map;

/**
 * 表结构同步表 Mapper 接口
 * @author 华博云
 * @since 2025-01-21
 */
@Mapper
public interface TblTableStructureMapper extends BaseMapper<TblTableStructure> {

    /**
     * 根据数据源ID获取表统计信息
     * @param dataSourceId 数据源ID
     * @return 统计信息
     */
    @Select("SELECT COUNT(DISTINCT TABLE_NAME) as tableCount, " +
            "COUNT(*) as columnCount " +
            "FROM TBL_TABLE_STRUCTURE " +
            "WHERE DATA_SOURCE_ID = #{dataSourceId}")
    List<Object> getTableStatistics(@Param("dataSourceId") String dataSourceId);

    /**
     * 根据数据源ID获取所有表名
     * @param dataSourceId 数据源ID
     * @return 表名列表
     */
    @Select("SELECT DISTINCT TABLE_NAME FROM TBL_TABLE_STRUCTURE " +
            "WHERE DATA_SOURCE_ID = #{dataSourceId} " +
            "ORDER BY TABLE_NAME")
    List<String> getTableNamesByDataSourceId(@Param("dataSourceId") String dataSourceId);

    /**
     * 根据数据源ID和表名删除表结构
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return 删除行数
     */
    @Select("DELETE FROM TBL_TABLE_STRUCTURE " +
            "WHERE DATA_SOURCE_ID = #{dataSourceId} AND TABLE_NAME = #{tableName}")
    int deleteByDataSourceIdAndTableName(@Param("dataSourceId") String dataSourceId,
                                        @Param("tableName") String tableName);

    /**
     * 高效查询表统计信息（优化版）
     * @param dataSourceId 数据源ID
     * @return 统计信息Map
     */
    @Select("SELECT " +
            "COUNT(DISTINCT TABLE_NAME) as totalTables, " +
            "COUNT(*) as totalColumns, " +
            "MAX(SYNC_TIME) as lastSyncTime " +
            "FROM TBL_TABLE_STRUCTURE " +
            "WHERE DATA_SOURCE_ID = #{dataSourceId}")
    Map<String, Object> getTableStatisticsOptimized(@Param("dataSourceId") String dataSourceId);

    /**
     * 高效分页查询表信息（优化版）
     * @param dataSourceId 数据源ID
     * @param tableName 表名（可选）
     * @param tableComment 表注释（可选）
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 表信息列表
     */
    @Select("<script>" +
            "SELECT " +
            "    DATA_SOURCE_ID, " +
            "    TABLE_NAME, " +
            "    TABLE_COMMENT, " +
            "    COUNT(*) as columnCount, " +
            "    SUM(CASE WHEN IS_PRIMARY_KEY = 'Y' THEN 1 ELSE 0 END) as primaryKeyCount, " +
            "    MAX(SYNC_TIME) as syncTime " +
            "FROM TBL_TABLE_STRUCTURE " +
            "WHERE DATA_SOURCE_ID = #{dataSourceId} " +
            "<if test='tableName != null and tableName != \"\"'>" +
            "    AND TABLE_NAME LIKE '%' || #{tableName} || '%' " +
            "</if>" +
            "<if test='tableComment != null and tableComment != \"\"'>" +
            "    AND TABLE_COMMENT LIKE '%' || #{tableComment} || '%' " +
            "</if>" +
            "GROUP BY DATA_SOURCE_ID, TABLE_NAME, TABLE_COMMENT " +
            "ORDER BY MAX(SYNC_TIME) DESC " +
            "OFFSET #{offset} ROWS FETCH NEXT #{pageSize} ROWS ONLY" +
            "</script>")
    List<Map<String, Object>> getTableListOptimized(@Param("dataSourceId") String dataSourceId,
                                                   @Param("tableName") String tableName,
                                                   @Param("tableComment") String tableComment,
                                                   @Param("offset") int offset,
                                                   @Param("pageSize") int pageSize);

    /**
     * 获取表总数（用于分页）
     * @param dataSourceId 数据源ID
     * @param tableName 表名（可选）
     * @param tableComment 表注释（可选）
     * @return 表总数
     */
    @Select("<script>" +
            "SELECT COUNT(DISTINCT TABLE_NAME) " +
            "FROM TBL_TABLE_STRUCTURE " +
            "WHERE DATA_SOURCE_ID = #{dataSourceId} " +
            "<if test='tableName != null and tableName != \"\"'>" +
            "    AND TABLE_NAME LIKE '%' || #{tableName} || '%' " +
            "</if>" +
            "<if test='tableComment != null and tableComment != \"\"'>" +
            "    AND TABLE_COMMENT LIKE '%' || #{tableComment} || '%' " +
            "</if>" +
            "</script>")
    int getTableCount(@Param("dataSourceId") String dataSourceId,
                     @Param("tableName") String tableName,
                     @Param("tableComment") String tableComment);

    /**
     * 分页获取表结构详情（包含列信息）
     * @param dataSourceId 数据源ID
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 表结构详情列表
     */
    @Select("SELECT " +
            "    ts.DATA_SOURCE_ID, " +
            "    ts.TABLE_NAME, " +
            "    ts.TABLE_COMMENT, " +
            "    ts.COLUMN_NAME, " +
            "    ts.COLUMN_TYPE, " +
            "    ts.COLUMN_COMMENT, " +
            "    ts.IS_PRIMARY_KEY, " +
            "    ts.IS_NULLABLE, " +
            "    ts.COLUMN_ORDER " +
            "FROM TBL_TABLE_STRUCTURE ts " +
            "WHERE ts.DATA_SOURCE_ID = #{dataSourceId} " +
            "AND ts.TABLE_NAME IN ( " +
            "    SELECT DISTINCT TABLE_NAME " +
            "    FROM TBL_TABLE_STRUCTURE " +
            "    WHERE DATA_SOURCE_ID = #{dataSourceId} " +
            "    ORDER BY TABLE_NAME " +
            "    OFFSET #{offset} ROWS FETCH NEXT #{pageSize} ROWS ONLY " +
            ") " +
            "ORDER BY ts.TABLE_NAME, ts.COLUMN_ORDER")
    List<TblTableStructure> getTableStructureDetailsPaged(@Param("dataSourceId") String dataSourceId,
                                                          @Param("offset") int offset,
                                                          @Param("pageSize") int pageSize);
}
