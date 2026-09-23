package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.dto.EvaluationModelQueryDTO;
import com.huabo.fxgl.entity.TblEvaluationModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 评估模型Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Mapper
public interface TblEvaluationModelMapper extends BaseMapper<TblEvaluationModel> {

    /**
     * 分页查询评估模型列表
     * 
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<TblEvaluationModel> selectEvaluationModelPage(Page<TblEvaluationModel> page, 
                                                       @Param("query") EvaluationModelQueryDTO queryDTO);

    /**
     * 根据模型编码查询模型
     * 
     * @param modelCode 模型编码
     * @return 评估模型
     */
    @Select("SELECT * FROM TBL_EVALUATION_MODEL WHERE MODEL_CODE = #{modelCode}")
    TblEvaluationModel selectByModelCode(@Param("modelCode") String modelCode);

    /**
     * 查询启用的评估模型列表
     * 
     * @return 启用的评估模型列表
     */
    @Select("SELECT * FROM TBL_EVALUATION_MODEL WHERE IS_ENABLED = 'Y' ORDER BY CREATE_TIME DESC")
    List<TblEvaluationModel> selectEnabledModels();

    /**
     * 根据业务场景查询模型列表
     * 
     * @param businessScenario 业务场景
     * @return 模型列表
     */
    @Select("SELECT * FROM TBL_EVALUATION_MODEL WHERE BUSINESS_SCENARIO = #{businessScenario} AND IS_ENABLED = 'Y' ORDER BY CREATE_TIME DESC")
    List<TblEvaluationModel> selectByBusinessScenario(@Param("businessScenario") String businessScenario);

    /**
     * 统计各状态的模型数量
     * 兼容达梦数据库语法
     *
     * @return 统计结果
     */
    @Select("SELECT \"STATUS\", COUNT(*) as count FROM \"TBL_EVALUATION_MODEL\" WHERE \"STATUS\" IS NOT NULL GROUP BY \"STATUS\" ORDER BY \"STATUS\"")
    List<Map<String, Object>> selectStatusStatistics();

    /**
     * 统计各业务场景的模型数量
     * 兼容达梦数据库语法
     *
     * @return 统计结果
     */
    @Select("SELECT \"BUSINESS_SCENARIO\", COUNT(*) as count FROM \"TBL_EVALUATION_MODEL\" WHERE \"BUSINESS_SCENARIO\" IS NOT NULL GROUP BY \"BUSINESS_SCENARIO\" ORDER BY \"BUSINESS_SCENARIO\"")
    List<Map<String, Object>> selectScenarioStatistics();

    /**
     * 查询最近创建的模型
     * 
     * @param limit 限制数量
     * @return 最近创建的模型列表
     */
    @Select("SELECT * FROM TBL_EVALUATION_MODEL ORDER BY CREATE_TIME DESC LIMIT #{limit}")
    List<TblEvaluationModel> selectRecentModels(@Param("limit") Integer limit);
}
