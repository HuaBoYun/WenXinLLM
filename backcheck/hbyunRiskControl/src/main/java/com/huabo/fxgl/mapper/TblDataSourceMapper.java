package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.dto.DataSourceQueryDTO;
import com.huabo.fxgl.entity.TblDataSource;
import com.huabo.fxgl.vo.DataSourceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 数据源配置表 Mapper 接口
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Mapper
public interface TblDataSourceMapper extends BaseMapper<TblDataSource> {

    /**
     * 分页查询数据源列表
     *
     * @return 数据源列表
     */
    IPage<DataSourceVO> selectDataSourcePage(Page<DataSourceVO> page, @Param("query") DataSourceQueryDTO queryDTO);

    /**
     * 根据数据源名称查询数据源
     * 
     * @param sourceName 数据源名称
     * @return 数据源
     */
    @Select("SELECT * FROM TBL_DATA_SOURCE WHERE SOURCE_NAME = #{sourceName}")
    TblDataSource selectBySourceName(@Param("sourceName") String sourceName);

    /**
     * 根据连接信息查询数据源
     * 
     * @param hostIp 主机IP
     * @param port 端口
     * @param databaseName 数据库名
     * @return 数据源列表
     */
    @Select("SELECT * FROM TBL_DATA_SOURCE WHERE HOST_IP = #{hostIp} AND PORT = #{port} AND DATABASE_NAME = #{databaseName}")
    List<TblDataSource> selectByConnection(@Param("hostIp") String hostIp, 
                                          @Param("port") Integer port, 
                                          @Param("databaseName") String databaseName);

    /**
     * 更新连接测试结果
     * 
     * @param sourceId 数据源ID
     * @param testResult 测试结果
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_SOURCE SET CONNECTION_TEST_RESULT = #{testResult}, UPDATE_TIME = SYSDATE WHERE SOURCE_ID = #{sourceId}")
    int updateConnectionTestResult(@Param("sourceId") String sourceId, @Param("testResult") String testResult);

    /**
     * 更新表数量
     * 
     * @param sourceId 数据源ID
     * @param tableCount 表数量
     * @return 更新行数
     */
    @Update("UPDATE TBL_DATA_SOURCE SET TABLE_COUNT = #{tableCount}, LAST_SYNC_TIME = SYSDATE, UPDATE_TIME = SYSDATE WHERE SOURCE_ID = #{sourceId}")
    int updateTableCount(@Param("sourceId") String sourceId, @Param("tableCount") Integer tableCount);

    /**
     * 获取活跃数据源列表
     * 
     * @return 活跃数据源列表
     */
    @Select("SELECT * FROM TBL_DATA_SOURCE WHERE STATUS = 'ACTIVE' ORDER BY CREATE_TIME DESC")
    List<TblDataSource> selectActiveDataSources();

    /**
     * 获取数据源统计信息
     * 
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) AS total_count, " +
            "SUM(CASE WHEN STATUS = 'ACTIVE' THEN 1 ELSE 0 END) AS active_count, " +
            "SUM(CASE WHEN STATUS = 'INACTIVE' THEN 1 ELSE 0 END) AS inactive_count, " +
            "SUM(CASE WHEN CONNECTION_TEST_RESULT = 'SUCCESS' THEN 1 ELSE 0 END) AS success_count, " +
            "SUM(CASE WHEN CONNECTION_TEST_RESULT = 'FAILED' THEN 1 ELSE 0 END) AS failed_count, " +
            "SUM(NVL(TABLE_COUNT, 0)) AS total_table_count " +
            "FROM TBL_DATA_SOURCE")
    DataSourceStatisticsVO selectDataSourceStatistics();

    /**
     * 数据源统计信息VO
     */
    class DataSourceStatisticsVO {
        private Integer totalCount;
        private Integer activeCount;
        private Integer inactiveCount;
        private Integer successCount;
        private Integer failedCount;
        private Integer totalTableCount;

        // getters and setters
        public Integer getTotalCount() { return totalCount; }
        public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
        public Integer getActiveCount() { return activeCount; }
        public void setActiveCount(Integer activeCount) { this.activeCount = activeCount; }
        public Integer getInactiveCount() { return inactiveCount; }
        public void setInactiveCount(Integer inactiveCount) { this.inactiveCount = inactiveCount; }
        public Integer getSuccessCount() { return successCount; }
        public void setSuccessCount(Integer successCount) { this.successCount = successCount; }
        public Integer getFailedCount() { return failedCount; }
        public void setFailedCount(Integer failedCount) { this.failedCount = failedCount; }
        public Integer getTotalTableCount() { return totalTableCount; }
        public void setTotalTableCount(Integer totalTableCount) { this.totalTableCount = totalTableCount; }
    }
}
