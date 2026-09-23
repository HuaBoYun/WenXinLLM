package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
 
public interface IRiskGroupPlanService extends IService<TblRiskGroupplan> {
    PageInfo<TblRiskGroupplan> findAll(TblStaffUtil staffUtil, TblRiskGroupplan TblRiskGroupplan, BigDecimal orgid,Integer pageno,Integer pagesize) throws Exception;
    
    TblRiskGroupplan getOneDetail(String planid) throws Exception;
 
    public Integer selectTblRiskGroupplanNumber(String plancode,String orgid) throws Exception;
 
    JsonBean get_riskpgplan_no(String token) throws Exception;
	
	TblRiskGroupplan saveEntity(TblRiskGroupplan plan) throws Exception;

	TblRiskGroupplan updateEntity(TblRiskGroupplan plan) throws Exception;

	JsonBean toIssued(String id,String staffids, String staffnames)throws Exception;
}
