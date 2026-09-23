package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.TblModelVersionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 模型版本操作日志表 Mapper 接口
 *
 * @author 华博云
 * @date 2025-09-30
 */
@Mapper
public interface TblModelVersionLogMapper extends BaseMapper<TblModelVersionLog> {

    /**
     * 分页查询模型操作日志
     *
     * @param page 分页参数
     * @param modelId 模型ID
     * @param operationType 操作类型
     * @param operationUser 操作人
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT * FROM TBL_MODEL_VERSION_LOG " +
            "WHERE 1=1 " +
            "<if test='modelId != null and modelId != \"\"'>" +
            "AND MODEL_ID = #{modelId} " +
            "</if>" +
            "<if test='operationType != null and operationType != \"\"'>" +
            "AND OPERATION_TYPE = #{operationType} " +
            "</if>" +
            "<if test='operationUser != null and operationUser != \"\"'>" +
            "AND OPERATION_USER LIKE CONCAT('%', #{operationUser}, '%') " +
            "</if>" +
            "ORDER BY OPERATION_TIME DESC" +
            "</script>")
    IPage<TblModelVersionLog> selectLogPage(Page<TblModelVersionLog> page,
                                          @Param("modelId") String modelId,
                                          @Param("operationType") String operationType,
                                          @Param("operationUser") String operationUser);

    /**
     * 根据模型ID查询操作日志
     *
     * @param modelId 模型ID
     * @param limit 限制条数
     * @return 日志列表
     */
    @Select("SELECT * FROM TBL_MODEL_VERSION_LOG " +
            "WHERE MODEL_ID = #{modelId} " +
            "ORDER BY OPERATION_TIME DESC " +
            "LIMIT #{limit}")
    List<TblModelVersionLog> selectByModelId(@Param("modelId") String modelId, 
                                           @Param("limit") Integer limit);

    /**
     * 根据版本ID查询操作日志
     *
     * @param versionId 版本ID
     * @return 日志列表
     */
    @Select("SELECT * FROM TBL_MODEL_VERSION_LOG " +
            "WHERE VERSION_ID = #{versionId} " +
            "ORDER BY OPERATION_TIME DESC")
    List<TblModelVersionLog> selectByVersionId(@Param("versionId") String versionId);

    /**
     * 查询用户操作统计
     *
     * @param operationUser 操作人
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    @Select("SELECT OPERATION_TYPE, COUNT(*) as COUNT " +
            "FROM TBL_MODEL_VERSION_LOG " +
            "WHERE OPERATION_USER = #{operationUser} " +
            "AND OPERATION_TIME BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY OPERATION_TYPE " +
            "ORDER BY COUNT DESC")
    List<Object> selectUserOperationStats(@Param("operationUser") String operationUser,
                                        @Param("startTime") String startTime,
                                        @Param("endTime") String endTime);

    /**
     * 查询操作失败的日志
     *
     * @param modelId 模型ID
     * @param limit 限制条数
     * @return 失败日志列表
     */
    @Select("SELECT * FROM TBL_MODEL_VERSION_LOG " +
            "WHERE OPERATION_RESULT = 'FAILED' " +
            "<if test='modelId != null and modelId != \"\"'>" +
            "AND MODEL_ID = #{modelId} " +
            "</if>" +
            "ORDER BY OPERATION_TIME DESC " +
            "LIMIT #{limit}")
    List<TblModelVersionLog> selectFailedLogs(@Param("modelId") String modelId, 
                                            @Param("limit") Integer limit);
}
