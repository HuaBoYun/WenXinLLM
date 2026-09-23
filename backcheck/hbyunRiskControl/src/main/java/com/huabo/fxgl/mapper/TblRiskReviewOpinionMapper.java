package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.TblRiskReview;
import com.huabo.fxgl.entity.TblRiskReviewOpinion;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TblRiskReviewOpinionMapper extends BaseMapper<TblRiskReviewOpinion> {

	
	@Select("select * from TBL_RISK_REVIEWOPINION  where reviewid=#{id}")
	TblRiskReviewOpinion getOneDetail(@Param("id")String id);
	
	@Select("delete from TBL_RISK_REVIEWOPINION  where reviewid=#{id}")
	Integer delOneById(@Param("id")String id);
}
