package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblTravelArchiveEvaluation;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 评价记录Mapper
 */
public interface TblTravelArchiveEvaluationMapper extends BaseMapper<TblTravelArchiveEvaluation> {

    /**
     * 根据档案ID查询
     */
    List<TblTravelArchiveEvaluation> selectByArchiveId(@Param("archiveId") String archiveId);

    /**
     * 根据评价人ID查询
     */
    List<TblTravelArchiveEvaluation> selectByEvaluationUser(@Param("evaluationUser") String evaluationUser);

    /**
     * 根据综合评分范围查询
     */
    List<TblTravelArchiveEvaluation> selectByOverallScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore);

    /**
     * 根据档案ID查询平均评分
     */
    Double selectAvgOverallScoreByArchiveId(@Param("archiveId") String archiveId);

    /**
     * 根据档案ID统计评价数量
     */
    Integer countByArchiveId(@Param("archiveId") String archiveId);
}
