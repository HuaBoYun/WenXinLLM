package com.huabo.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.finance.entity.TransformAlgorithmConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 转化算法配置Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface TransformAlgorithmConfigMapper extends BaseMapper<TransformAlgorithmConfig> {

    /**
     * 根据转化任务ID查询算法配置列表
     * 
     * @param transformTaskId 转化任务ID
     * @return 算法配置列表
     */
    @Select("SELECT * FROM TBL_TRANSFORM_ALGORITHM_CONFIG WHERE TRANSFORM_TASK_ID = #{transformTaskId} AND IS_ENABLED = 1 ORDER BY EXECUTE_ORDER")
    List<TransformAlgorithmConfig> selectByTransformTaskId(@Param("transformTaskId") String transformTaskId);

    /**
     * 根据转化任务ID和算法编码查询算法配置
     * 
     * @param transformTaskId 转化任务ID
     * @param algorithmCode 算法编码
     * @return 算法配置
     */
    @Select("SELECT * FROM TBL_TRANSFORM_ALGORITHM_CONFIG WHERE TRANSFORM_TASK_ID = #{transformTaskId} AND ALGORITHM_CODE = #{algorithmCode}")
    TransformAlgorithmConfig selectByTaskIdAndCode(@Param("transformTaskId") String transformTaskId, @Param("algorithmCode") String algorithmCode);
}

