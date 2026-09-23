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

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskAssplanService extends IService<RiskAssplan> {
    PageInfo<RiskAssplan> findUserAndRiskLevelById(TblStaffUtil staffUtil, RiskRiskmarking riskmarking, Find find, BigDecimal orgid,Integer pageNo,Integer pageSize) throws Exception;
    
    PageInfo<RiskAssplan> findRiskAssPlanPageDetails(Find find,Integer pageNo,Integer pageSize, TblStaffUtil staffUtil,Integer authorityType);
    
    PageInfo<RiskAssplan> findGroupResultList(Find find,Integer pageNo,Integer pageSize);

    
    PageInfo<RiskAssplan> riplanTrackList(RiskAssplan riskAssplan, BigDecimal orgid,Integer pageno,Integer pagesize,TblStaffUtil staffUtil) throws Exception;

    PageInfo<RiskAssplan> findAll(RiskAssplan riskAssplan, BigDecimal orgid,Integer pageno,Integer pagesize,TblStaffUtil staffUtil) throws Exception;
//    List<RiskAssplanRisk> get(BigDecimal planid);

    /**
     * 查询同个公司下风险评估计划的编号重复情况
     * @param plancode
     * @param orgid
     * @return
     */
    public Integer selectRiskAssplanNumber(String plancode,String orgid) throws Exception;
	void setPingguStatusByPlanId(RiskAssplan riskAssplan);
	
	JsonBean get_riskpgplan_no(String token) throws Exception;
	
	   
    Map<String, Object> getRiskPointTask(String token,String company)throws Exception;

}
