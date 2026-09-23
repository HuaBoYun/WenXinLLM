package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblRiskReview;
import com.huabo.fxgl.entity.TblRiskReviewOpinion;
import com.huabo.fxgl.util.PageResult;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblRiskReviewService {


	PageInfo<TblRiskReview> riskReviewList(String token, Integer pageNumber, Integer pageSize, String mattername, String mattercode, String riskreviewcode, String state,
			BigDecimal staffid,Integer authorityType) throws  Exception;
	
	List<TblRiskReview> getRiskReviewListExport(String token,String mattername, String mattercode, String riskreviewcode, String state,
			BigDecimal staffid,Integer authorityType) throws  Exception;
	
	
	PageInfo<TblRiskReview> riskReviewOpinionList(String token, Integer pageNumber, Integer pageSize, String mattername, String mattercode, String riskreviewcode, String state,
			BigDecimal staffid,Integer authorityType,String unitname) throws  Exception;

    Integer updateRiskReview(TblRiskReview tblRiskReview);

    Integer insertRiskReview(TblRiskReview tblRiskReview);

    TblRiskReview riskReviewDetails(String reviewid);

    Integer riskReviewDelete(String reviewid);

    Long queryCodeNumber(String projectCode);
    
    Map<String, Object> getCompanyRiskReview(String token,String year)throws  Exception;
    
    TblRiskReviewOpinion  riskReviewOpinionDetails(String id)throws  Exception;
    
    Map<String, Object> saveReviewOpinion(TblRiskReviewOpinion opin)throws  Exception;
    
    Integer riskReviewOpinionDelete(String id);

}
