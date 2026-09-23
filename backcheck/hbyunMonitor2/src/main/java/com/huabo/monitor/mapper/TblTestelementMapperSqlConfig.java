package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblOuterrule;
import com.huabo.monitor.entity.TblTestelement;

public class TblTestelementMapperSqlConfig {
	
	public String selectPageList(PageInfo<TblTestelement> pageInfo) {
		TblTestelement ele = pageInfo.getCondition();
		
		String sql = "SELECT T2.* FROM (SELECT T1.*,ROWNUM ROW_ID FROM (SELECT * FROM TBL_TESTELEMENT WHERE TEMPLID = "+ele.getTemplid();
		
		if(ele.getTypeid() != null ) {
			sql += " AND TYPEID = "+ele.getTypeid();
		}
		
		if (StringUtils.isNotBlank(ele.getBusinessdesc())) {
			sql += " AND BUSINESSDESC LIKE '%"+ele.getBusinessdesc()+"%'";
        }
		if (StringUtils.isNotBlank(ele.getElementcode())) {
			sql += " AND ELEMENTCODE LIKE '%"+ele.getElementcode()+"%'";
        }
		sql += " ORDER BY ELEMENTID ASC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+") T2 WHERE T2.ROW_ID > "+pageInfo.getCurrentRecord();
		return sql;
	}
	
	public String selectPageCount(PageInfo<TblTestelement> pageInfo) {
		TblTestelement ele = pageInfo.getCondition();
		
		String sql = "SELECT COUNT(0) FROM TBL_TESTELEMENT WHERE TEMPLID = "+ele.getTemplid();
		
		if(ele.getTypeid() != null ) {
			sql += " AND TYPEID = "+ele.getTypeid();
		}
		
		if (StringUtils.isNotBlank(ele.getBusinessdesc())) {
			sql += " AND BUSINESSDESC LIKE '%"+ele.getBusinessdesc()+"%'";
        }
		if (StringUtils.isNotBlank(ele.getElementcode())) {
			sql += " AND ELEMENTCODE LIKE '%"+ele.getElementcode()+"%'";
        }
		return sql;
		
	}
	
	public String insertEntity(TblTestelement newEle) {
		String colSql = "INSERT INTO TBL_TESTELEMENT(ELEMENTID, CREATETIME ";
		String valueSql = " VALUES (HIBERNATE_SEQUENCE.nextval, SYSDATE";
		
		if (StringUtils.isNotBlank(newEle.getBusinessdesc())) {
			colSql += ",BUSINESSDESC ";
			valueSql += ",'"+newEle.getBusinessdesc()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getCheckmethod())) {
			colSql += ",CHECKMETHOD ";
			valueSql += ",'"+newEle.getCheckmethod()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControlmeasures())) {
			colSql += ",CONTROLMEASURES ";
			valueSql += ",'"+newEle.getControlmeasures()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControlmethod())) {
			colSql += ",CONTROLMETHOD ";
			valueSql += ",'"+newEle.getControlmethod()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControlreq())) {
			colSql += ",CONTROLREQ ";
			valueSql += ",'"+newEle.getControlreq()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControltarget())) {
			colSql += ",CONTROLTARGET ";
			valueSql += ",'"+newEle.getControltarget()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControltype())) {
			colSql += ",CONTROLTYPE ";
			valueSql += ",'"+newEle.getControltype()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getElementcode())) {
			colSql += ",ELEMENTCODE ";
			valueSql += ",'"+newEle.getElementcode()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getMaterial())) {
			colSql += ",MATERIAL ";
			valueSql += ",'"+newEle.getMaterial()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getRisktype())) {
			colSql += ",RISKTYPE ";
			valueSql += ",'"+newEle.getRisktype()+"'";
        }
		if(newEle.getTemplid() != null) {
			colSql += ",TEMPLID ";
			valueSql += ",'"+newEle.getTemplid()+"'";
		}
		if(newEle.getTypeid() != null) {
			colSql += ",TYPEID ";
			valueSql += ",'"+newEle.getTypeid()+"'";
		}
		if(newEle.getLongString1()!= null) {
			colSql += ",LONGSTRING1 ";
			valueSql += ",'"+newEle.getLongString1()+"'";
		}
		if(newEle.getLongString2()!= null) {
			colSql += ",LONGSTRING2 ";
			valueSql += ",'"+newEle.getLongString2()+"'";
		}
		return colSql + ") " +valueSql+")";
	}

	public String updateEntity(TblTestelement newEle) {
		String sql = "UPDATE TBL_TESTELEMENT SET TEMPLID = "+newEle.getTemplid()+",TYPEID = "+newEle.getTypeid()+",ELEMENTCODE = '"+newEle.getElementcode()+"'";
		
		if (StringUtils.isNotBlank(newEle.getMaterial())) {
			sql += ",MATERIAL = '"+newEle.getMaterial()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getRisktype())) {
			sql += ",RISKTYPE = '"+newEle.getRisktype()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getBusinessdesc())) {
			sql += ",BUSINESSDESC = '"+newEle.getBusinessdesc()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getCheckmethod())) {
			sql += ",CHECKMETHOD = '"+newEle.getCheckmethod()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControlmeasures())) {
			sql += ",CONTROLMEASURES = '"+newEle.getControlmeasures()+"' ";
        }
		if (StringUtils.isNotBlank(newEle.getControlmethod())) {
			sql += ",CONTROLMETHOD = '"+newEle.getControlmethod()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControlreq())) {
			sql += ",CONTROLREQ = '"+newEle.getControlreq()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControltarget())) {
			sql += ",CONTROLTARGET = '"+newEle.getControltarget()+"'";
        }
		if (StringUtils.isNotBlank(newEle.getControltype())) {
			sql += ",CONTROLTYPE = '"+newEle.getControltype()+"'";
        }
		
		if (StringUtils.isNotBlank(newEle.getLongString1())) {
			sql += ",LONGSTRING1 = '"+newEle.getLongString1()+"'";
        }
		
		if (StringUtils.isNotBlank(newEle.getLongString2())) {
			sql += ",LONGSTRING2 = '"+newEle.getLongString2()+"'";
        }
		
		sql += " WHERE ELEMENTID = "+newEle.getElementid();
		return sql;
	}
}
