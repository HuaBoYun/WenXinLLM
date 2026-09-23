package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.CollectionTransformConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 采集任务转化配置Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface CollectionTransformConfigMapper extends BaseMapper<CollectionTransformConfig> {

    /**
     * 根据采集任务ID查询转化配置
     */
    @Select("SELECT * FROM TBL_COLLECTION_TRANSFORM_CONFIG WHERE COLLECTION_TASK_ID = #{collectionTaskId}")
    CollectionTransformConfig selectByCollectionTaskId(@Param("collectionTaskId") String collectionTaskId);

    /**
     * 查询需要自动执行的配置列表
     */
    @Select("SELECT * FROM TBL_COLLECTION_TRANSFORM_CONFIG WHERE AUTO_EXECUTE = 1 AND EXECUTE_STATUS = 'PENDING'")
    List<CollectionTransformConfig> selectAutoExecuteList();

    /**
     * 更新执行状态
     */
    @Update("UPDATE TBL_COLLECTION_TRANSFORM_CONFIG SET EXECUTE_STATUS = #{status}, MODIFY_TIME = SYSDATE WHERE CONFIG_ID = #{configId}")
    int updateExecuteStatus(@Param("configId") String configId, @Param("status") String status);

    /**
     * 更新执行结果
     */
    @Update("UPDATE TBL_COLLECTION_TRANSFORM_CONFIG SET EXECUTE_STATUS = #{status}, END_TIME = SYSDATE, " +
            "SUCCESS_COUNT = #{successCount}, FAILED_COUNT = #{failedCount}, ERROR_MESSAGE = #{errorMessage}, " +
            "MODIFY_TIME = SYSDATE WHERE CONFIG_ID = #{configId}")
    int updateExecuteResult(@Param("configId") String configId, @Param("status") String status,
                           @Param("successCount") Integer successCount, @Param("failedCount") Integer failedCount,
                           @Param("errorMessage") String errorMessage);
}

