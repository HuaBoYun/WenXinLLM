package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblYqnsResult;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.hbfk.util.PageInfo;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class TblYqnsResultMapperSqlConfig {
	
	
public String findbyhj(TBlNbsjSheetVo tBlNbsjSheetVo, List<String> idList) {
		
		StringBuffer sb = new StringBuffer("select RS.RESULTID,RS.RESULTCODE,RS.CONTRACTCODE,RS.CONTRACTNAME,RS.SGORGNAME,RS.CONTRACTMONEY,RS.SDMONEY,RS.HJMONEY,round(RS.HJMONEY/RS.CONTRACTNAME,2) hjl,jc.RWNAMES,"
	    		+ "SJ.GCLJS,SJ.DETY,SJ.WZJG,SJ.QTSJ,SJ.XCSC "
	    		+ "  from TBL_YQNS_RESULT rs  LEFT JOIN TBL_YQNS_GCXMZJ_ZJB jc ON JC.GCXMZJZJBID=RS.TEMPLATEID  "
	    		+ " LEFT JOIN TBL_YQNS_SJSS_WDRW_SJNR sj on sj.TEMPLATEID=JC.GCXMZJZJBID WHERE jc.RWNAMES  is not NULL ");
		
		
		if(tBlNbsjSheetVo.getContractcode()!=null && tBlNbsjSheetVo.getContractcode().length()>0) {
			sb.append(" AND RS.CONTRACTCODE LIKE '%"+tBlNbsjSheetVo.getContractcode()+"%'");
		}
		
		
		if(tBlNbsjSheetVo.getContractname()!=null && tBlNbsjSheetVo.getContractname().length()>0) {
			sb.append(" AND RS.CONTRACTNAME LIKE '%"+tBlNbsjSheetVo.getContractname()+"%'");
		}
		
		if(tBlNbsjSheetVo.getContractmoney()!=null && tBlNbsjSheetVo.getContractmoney().length()>0) {
			sb.append(" AND RS.CONTRACTMONEY LIKE '%"+tBlNbsjSheetVo.getContractmoney()+"%'");
		}

	if (CollectionUtils.isNotEmpty(idList)) {
		StringBuffer idListSub = new StringBuffer();
		for (int i = 0; i < idList.size(); i++) {
			idListSub.append(idList.get(i));
			if (i < idList.size() - 1) {
				idListSub.append(",");
			}
		}
		sb.append(" AND RS.RESULTID in("+ idListSub + ")");
	}
		 
		
		return sb.toString();
	}
	
	
	
	public String selectListByPageInfo(PageInfo<TblYqnsResult> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo, List<String> idList) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,PRINCIPAL.REALNAME "
				+ "FROM TBL_YQNS_RESULT TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "WHERE TNA.RESULTIDS is null ");
		
		if(tBlNbsjSheetVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectId()+"'");
		}
		
		if(tBlNbsjSheetVo.getCreatestaff()!=null) {
			sb.append(" AND TNA.CREATESTAFF = '"+tBlNbsjSheetVo.getCreatestaff()+"'");
		}
		
		if(tBlNbsjSheetVo.getTemplateId() != null) {
			sb.append(" AND TNA.TEMPLATEID = ").append(tBlNbsjSheetVo.getTemplateId());
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.RESULTCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getProjectName()!=null && tBlNbsjSheetVo.getProjectName().length()>0) {
			sb.append(" AND TNP.prjoectname LIKE '%"+tBlNbsjSheetVo.getProjectName()+"%'");
		}
		
		if(tBlNbsjSheetVo.getOrgidnames()!=null && tBlNbsjSheetVo.getOrgidnames().length()>0) {
			sb.append(" AND TNA.orgidnames LIKE '%"+tBlNbsjSheetVo.getOrgidnames()+"%'");
		}
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		if(tBlNbsjSheetVo.getStatus()!=null) {
			sb.append(" AND TNA.STATUS = '"+tBlNbsjSheetVo.getStatus()+"'");
		}
		
		if(tBlNbsjSheetVo.getOverview()!=null && tBlNbsjSheetVo.getOverview().length()>0) {
			sb.append(" AND TNA.OVERVIEW LIKE '%"+tBlNbsjSheetVo.getOverview()+"%'");
		}
		
		if(tBlNbsjSheetVo.getContractcode()!=null && tBlNbsjSheetVo.getContractcode().length()>0) {
			sb.append(" AND TNA.CONTRACTCODE LIKE '%"+tBlNbsjSheetVo.getContractcode()+"%'");
		}

		if (CollectionUtils.isNotEmpty(idList)) {
			StringBuffer idListSub = new StringBuffer();
			for (int i = 0; i < idList.size(); i++) {
				idListSub.append(idList.get(i));
				if (i < idList.size() - 1) {
					idListSub.append(",");
				}
			}
			sb.append(" AND TNA.RESULTID in("+ idListSub + ")");
		}

		sb.append(" ORDER BY TNA.RESULTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblYqnsResult> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_YQNS_RESULT TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID "
				+ "WHERE TNA.RESULTIDS is null ");
		
		if(tBlNbsjSheetVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectId()+"'");
		} 
		if(tBlNbsjSheetVo.getCreatestaff()!=null) {
			sb.append(" AND TNA.CREATESTAFF = '"+tBlNbsjSheetVo.getCreatestaff()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.RESULTCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		
		if(tBlNbsjSheetVo.getProjectName()!=null && tBlNbsjSheetVo.getProjectName().length()>0) {
			sb.append(" AND TNP.prjoectname LIKE '%"+tBlNbsjSheetVo.getProjectName()+"%'");
		}
		if(tBlNbsjSheetVo.getOrgidnames()!=null && tBlNbsjSheetVo.getOrgidnames().length()>0) {
			sb.append(" AND TNA.orgidnames LIKE '%"+tBlNbsjSheetVo.getOrgidnames()+"%'");
		}
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		if(tBlNbsjSheetVo.getTemplateId() != null) {
			sb.append(" AND TNA.TEMPLATEID = ").append(tBlNbsjSheetVo.getTemplateId());
		}
		
		if(tBlNbsjSheetVo.getStatus()!=null) {
			sb.append(" AND TNA.STATUS = '"+tBlNbsjSheetVo.getStatus()+"'");
		}
		if(tBlNbsjSheetVo.getOverview()!=null && tBlNbsjSheetVo.getOverview().length()>0) {
			sb.append(" AND TNA.OVERVIEW LIKE '%"+tBlNbsjSheetVo.getOverview()+"%'");
		}
		
		if(tBlNbsjSheetVo.getContractcode()!=null && tBlNbsjSheetVo.getContractcode().length()>0) {
			sb.append(" AND TNA.CONTRACTCODE LIKE '%"+tBlNbsjSheetVo.getContractcode()+"%'");
		}
		
		return sb.toString();
	}
	 
	
	
	 
		
}
