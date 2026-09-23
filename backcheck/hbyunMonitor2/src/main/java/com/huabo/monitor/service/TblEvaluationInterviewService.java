package com.huabo.monitor.service;

import com.huabo.monitor.entity.TblAssessPlan;
import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblEvaluateDefects;
import com.huabo.monitor.entity.TblEvaluationInterview;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface TblEvaluationInterviewService extends IService<TblEvaluationInterview> {

	PageInfo<TblEvaluationInterview> getHomepage_List(Integer pageNumber,Integer pageSize,Integer authorityType,TblEvaluationInterview entity,TblStaffUtil staff) throws Exception;

	Map<String, Object> saveOrUpdate(String token,TblEvaluationInterview entity,String attids)throws Exception;
	
	TblEvaluationInterview getDetatilsById(BigDecimal id)throws Exception;

	String deleteById(BigDecimal id)throws Exception;
	
	List<TblEvaluationInterview>exportPjft(Integer authorityType,TblEvaluationInterview entity)throws Exception;

	List<TblAttachment> getAttList(BigDecimal id) throws Exception;
	
	
	void delAssessAtt(BigDecimal attid) ;

}
