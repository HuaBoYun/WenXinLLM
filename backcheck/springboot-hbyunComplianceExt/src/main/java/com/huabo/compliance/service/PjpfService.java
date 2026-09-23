package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.compliance.entity.ElemGrade;
import com.huabo.compliance.entity.TblAssess;
import com.huabo.compliance.entity.TblAssessStaff;
import com.huabo.compliance.entity.TblAssessVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author meng
 * @date 2022-08-26 9:53
 * @description
 */
public interface PjpfService extends IService<TblAssess>  {

    IPage<TblAssessVo> findPageBean(IPage<TblAssessVo> iPage, BigDecimal staffid, String projkey, String projname, String startDate, String endDate);

    IPage getTblAssessGroupOrg(IPage iPage, BigDecimal selectedPlans, BigDecimal userStaffid);

    String GetTree(BigDecimal tmplId, String s, String toString);

    List<ElemGrade> findElementByassIdAndUserId(BigDecimal nodeId, BigDecimal assId, BigDecimal staffid, BigDecimal orgId);

    List<TblAssessStaff> checkSubmit(BigDecimal assId, BigDecimal orgId, BigDecimal staffid);

    String doSubmint(BigDecimal assId, BigDecimal orgId, BigDecimal staffid);

    List<TblAssessStaff> checkSubmitAll(BigDecimal assId, BigDecimal orgId);

    List<Object[]> pjpfExport(BigDecimal bigDecimal, BigDecimal bigDecimal1, BigDecimal staffid, BigDecimal bigDecimal2);
}
