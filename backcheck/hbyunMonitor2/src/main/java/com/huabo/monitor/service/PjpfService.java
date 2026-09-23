package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.ElemGrade;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessStaff;
import com.huabo.monitor.entity.TblAssessVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author meng
 * @date 2022-08-26 9:53
 * @description
 */
public interface PjpfService extends IService<TblAssess>  {

    IPage<TblAssessVo> findPageBean(IPage<TblAssessVo> iPage, BigDecimal staffid, String projkey, String projname, String startDate, String endDate);
    //plus xml 
    PageInfo<TblAssessVo> findPageBeanNew(Integer pageNumber,BigDecimal staffid, String projkey, String projname, String startDate, String endDate,TblStaffUtil staff) throws Exception;

    
    
    IPage getTblAssessGroupOrg(IPage iPage, BigDecimal selectedPlans, BigDecimal userStaffid);

    String GetTree(BigDecimal tmplId, String s, String toString);

    List<ElemGrade> findElementByassIdAndUserId(BigDecimal nodeId, BigDecimal assId, BigDecimal staffid, BigDecimal orgId);

    List<TblAssessStaff> checkSubmit(BigDecimal assId, BigDecimal orgId, BigDecimal staffid);

    String doSubmint(BigDecimal assId, BigDecimal orgId, BigDecimal staffid);

    List<TblAssessStaff> checkSubmitAll(BigDecimal assId, BigDecimal orgId);

    List<Object[]> pjpfExport(BigDecimal bigDecimal, BigDecimal bigDecimal1, BigDecimal staffid, BigDecimal bigDecimal2);
}
