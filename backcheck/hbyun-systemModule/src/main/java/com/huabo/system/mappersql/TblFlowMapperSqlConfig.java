package com.huabo.system.mappersql;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblFlow;
import com.huabo.system.oracle.vo.CopyVo;

public class TblFlowMapperSqlConfig {

    public String selectTblFlowList(IPage<TblFlow> page,String orgid, String faflowid, String name, String code) {
        StringBuffer sqlSb = new StringBuffer("");
        if(faflowid != null && !"".equals(faflowid)) {
            sqlSb = new StringBuffer("SELECT f.FLOWID,f.FLOWNAME,f.FLOWNUMBER,o.ORGNAME as comname,deo.ORGNAME as depname,f.CREATETIME,f.FLOWSTATUS,f.RELATEDRULES" +
                    " FROM TBL_FLOW f LEFT JOIN TBL_ORGANIZATION o on f.COMPANY=o.ORGID" +
                    " LEFT JOIN TBL_ORGANIZATION deo on f.DEPARTINCHARGE=deo.ORGID  WHERE FLOWBYSYSTEM='1' and f.VERSIONTYPE is NULL and COMPANY=" +
                    " "+orgid+"" +
                    " and f.fatherflowid!='-1' and  f.FLOWID IN ( SELECT MAX(w.FLOWID) FROM TBL_FLOW w WHERE w.FATHERFLOWID = "+faflowid+"  GROUP BY w.flownumber)");

        }else {
            sqlSb = new StringBuffer("SELECT f.FLOWID,f.FLOWNAME,f.FLOWNUMBER,o.ORGNAME as comname,deo.ORGNAME as depname,f.CREATETIME,f.FLOWSTATUS,f.RELATEDRULES" +
                    " FROM TBL_FLOW f LEFT JOIN TBL_ORGANIZATION o on f.COMPANY=o.ORGID " +
                    " LEFT JOIN TBL_ORGANIZATION deo on f.DEPARTINCHARGE=deo.ORGID  WHERE FLOWBYSYSTEM='1' and f.VERSIONTYPE is NULL  and COMPANY=" +
                    " "+orgid+"" +
                    " and f.fatherflowid!='-1'and  f.FLOWID IN (SELECT MAX(w.FLOWID) FROM TBL_FLOW w WHERE w.FATHERFLOWID =  " +
                    " 0 and  w.COMPANY="+orgid+"  GROUP BY w.flownumber) ");
        }
        if(name != null && name.trim().length()>0) {
            sqlSb.append(" AND f.FLOWNAME LIKE '%"+name+"%'");
        }
        if(code != null && code.trim().length()>0) {
            sqlSb.append(" AND f.FLOWNUMBER LIKE '%"+code+"%'");
        }
        return sqlSb.toString();

    }

    public String findBysqAllversion(IPage<TblFlow> page, String orgid, String flowid) {
        StringBuffer sqlSb = new StringBuffer("SELECT 	f.FLOWID,	f.FLOWNAME,	f.FLOWNUMBER,	o.ORGNAME AS comname,	deo.ORGNAME AS depname,	"
                + " f.CREATETIME,f.LASTMODIFIEDTIME,f.VERSION FROM TBL_FLOW f LEFT JOIN TBL_ORGANIZATION o on f.COMPANY=o.ORGID"
                + " LEFT JOIN TBL_ORGANIZATION deo on f.DEPARTINCHARGE=deo.ORGID  WHERE  InFlowDB is null and   FLOWBYSYSTEM='1'"
                + " and f.FLOWNUMBER=(SELECT fl.FLOWNUMBER from TBL_FLOW fl where fl.FLOWID='" + flowid + "') "
                + " and f.FLOWID !='" + flowid + "' and f.COMPANY= '"+orgid+"'");
        sqlSb.append(" ORDER BY VERSION desc ");
        return sqlSb.toString();
    }

    public String findListByPageInfo(IPage<TblFlow> page, BigDecimal orgid,String faflowid, String flowname, String flownumber, String stutes, String desc, String belongsto, BigDecimal fatherFlowid, Integer firingStatus) {
        StringBuffer sqlSb = new StringBuffer("");
        if(faflowid == null || "".equals(faflowid)) {
            sqlSb = new StringBuffer("SELECT f.FLOWID,f.FLOWNUMBER,f.FLOWNAME,f.COMPANY,f.DEPARTINCHARGE,f.CREATETIME," +
                    " f.FLOWSTATUS,f.FATHERFLOWID,f.DEPARTASSIST,f.STATUS,f.fromid,f.SETTINGID,f.FIRINGSTATUS  FROM TBL_FLOW f WHERE f.FLOWID IN (" +
                    " SELECT max(w.FLOWID) from TBL_FLOW w WHERE  w.FATHERFLOWID = 0 and w.InFlowDB is null and w.COMPANY= "+orgid+"  GROUP BY w.flownumber)");

        }else {
            sqlSb = new StringBuffer("SELECT f.FLOWID,f.FLOWNUMBER,f.FLOWNAME,f.COMPANY,f.DEPARTINCHARGE,f.CREATETIME," +
                    " f.FLOWSTATUS,f.FATHERFLOWID,f.DEPARTASSIST,f.STATUS,f.fromid,f.SETTINGID,f.FIRINGSTATUS FROM TBL_FLOW f WHERE f.FLOWID IN (" +
                    " SELECT max(w.FLOWID) from TBL_FLOW w WHERE w.FATHERFLOWID = "+faflowid+" and w.InFlowDB is null and w.company= "+orgid+" GROUP BY w.flownumber)");
        }
        if (flowname!=null && !"".equals(flowname)) {
            sqlSb.append(" AND f.FLOWNAME LIKE '%"+flowname+"%'");
        }
        if (flownumber!=null && !"".equals(flownumber)) {
            sqlSb.append(" AND f.FLOWNUMBER LIKE '%"+flownumber+"%'");
        }
        if (belongsto!=null && !"".equals(belongsto)) {
            sqlSb.append(" AND f.DEPARTINCHARGE = "+ belongsto+" ");
        }
        if(fatherFlowid !=null && fatherFlowid.intValue() !=0 && fatherFlowid.intValue() !=-1 && firingStatus != null) {
            if(firingStatus != null) {
                sqlSb.append(" and f.FIRINGSTATUS = "+firingStatus+" AND FLOWID NOT IN (SELECT FLOWID FROM TBL_SYSTEM_MODELFLOW WHERE MODELID IN (SELECT MODELID FROM TBL_SYSTEM_MODULE WHERE MODELORG = "+orgid+"))");
            }
        }
        if (StringUtils.isNotBlank(stutes)) {
            if (desc!=null && !"".equals(desc)) {
                sqlSb.append(" order by "+stutes+","+desc+"");
            }else{
                sqlSb.append(" order by f.POSITION ");
            }
        }else{
            sqlSb.append(" order by f.POSITION,f.FLOWNUMBER ");
        }
        return sqlSb.toString();
    }

    public String findListByPageInfoFlow(IPage<TblFlow> page, String orgid, CopyVo vo) {
        StringBuffer sqlSb = new StringBuffer("");
        if (vo.getFaflowid() ==null|| "".equals(vo.getFaflowid())) {
            sqlSb = new StringBuffer("SELECT f.FLOWID,f.FLOWNUMBER,f.FLOWNAME,f.COMPANY,f.DEPARTINCHARGE,f.CREATETIME,"
                    + "f.FLOWSTATUS FROM TBL_FLOW f WHERE f.FLOWID IN (		" +
                    "SELECT		max(w.FLOWID) from TBL_FLOW w	WHERE		w.FATHERFLOWID = 0 and w.InFlowDB=1 and company="+orgid+" and VERSIONTYPE is NULL	GROUP BY w.flownumber)") ;
        }else{
            sqlSb = new StringBuffer("SELECT f.FLOWID,f.FLOWNUMBER,f.FLOWNAME,f.COMPANY,f.DEPARTINCHARGE,f.CREATETIME,"
                    + "f.FLOWSTATUS FROM TBL_FLOW f WHERE f.FLOWID IN (		" +
                    "SELECT		max(w.FLOWID) from TBL_FLOW w	WHERE		w.FATHERFLOWID = "+ vo.getPid()+" and w.InFlowDB=1 and w.company="+orgid+" and VERSIONTYPE is NULL	GROUP BY w.flownumber)") ;

        }
        if (vo.getFlowname()!=null && !"".equals(vo.getFlowname())) {
            sqlSb.append(" and f.FLOWNAME like '%"+vo.getFlowname()+"%'");
        }
        if (vo.getFlownumber()!=null && !"".equals(vo.getFlownumber())) {
            sqlSb.append (" and f.FLOWNUMBER like '%"+vo.getFlownumber()+"%'");
        }
        if (vo.getStutes()!=null && !"".equals(vo.getStutes().trim())) {
            sqlSb.append(" order by "+ vo.getStutes());
            if (vo.getDesc() !=null && !"".equals(vo.getDesc())) {
                sqlSb.append (" "+ vo.getDesc());
            }
        }
        return sqlSb.toString();
    }
	
}
