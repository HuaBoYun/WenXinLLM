package com.huabo.audit.service.impl;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.vo.TblReportVo;
import com.huabo.audit.service.TblJhyjgService;

public class TblJhyjgServiceImpl implements TblJhyjgService {
	
	
    @Override
    public JsonBean zdyPageList(String token, Integer pageNumber, Integer pageSize, TblReportVo tblReportVo, Integer projectId)
            throws Exception {
				return null;
        /*TblStaffUtil loginStaff = DealUserToken.parseUserToken(token);
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();

        if(pageNumber == null) {
            pageNumber = 1;
        }
        if(pageSize==null) {
            pageSize=15;
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);


        if(null == projectId) {
            //==查询当前实施的项目！
            TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if(tnp == null) {
                return ResponseFormat.retParam(0,30003,resultMap);
            }
            projectId = tnp.getProjectId();
        }


        PageInfo<TblReportEntity> pageInfo = new PageInfo<TblReportEntity>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblReportMapper.selectListByPageInfo(pageInfo,tblReportVo,orgid.intValue(),projectId));
        pageInfo.setTotalRecord(this.tblReportMapper.selectCountByPageInfo(pageInfo,tblReportVo,orgid.intValue(),projectId));
        pageInfo.getTotalPage();
        String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
        resultMap.put("identifier", identifier);
        resultMap.put("pageInfo", pageInfo);
        return ResponseFormat.retParam(1,200,resultMap);*/
    	
//    	return null;
    }

}
