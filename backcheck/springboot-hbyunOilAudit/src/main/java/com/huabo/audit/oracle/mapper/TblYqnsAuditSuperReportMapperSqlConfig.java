package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.TblYqnsAuditSuperReport;

import java.text.SimpleDateFormat;
import java.util.List;

public class TblYqnsAuditSuperReportMapperSqlConfig {

    public String selectReportList(PageInfo pageInfo, TblYqnsAuditSuperReport entity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_AUDIT_SUPER_REPORT TAS WHERE 1=1 ");

        if(StringUtil.isNotEmpty(entity.getReportName())){
            sb.append("AND TAS.REPORTNAME = '"+entity.getReportName()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getStartTime())){
            sb.append("AND TAS.REPORTTIME >= TO_DATE('"+entity.getStartTime()+"', 'yyyy-MM-dd HH24:MI:SS')");
        }

        if(StringUtil.isNotEmpty(entity.getEndTime())){
            sb.append("AND TAS.REPORTTIME <= TO_DATE('"+entity.getEndTime()+"', 'yyyy-MM-dd HH24:MI:SS')");
        }

        if(StringUtil.isNotEmpty(entity.getReportType())){
            sb.append("AND TAS.REPORTTYPE LIKE '%"+entity.getReportType()+"%'");
        }

        if(StringUtil.isNotEmpty(entity.getReportWay())){
            sb.append("AND TAS.REPORTWAY LIKE '%"+entity.getReportWay()+"%'");
        }
        sb.append(" ORDER BY TAS.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String saveReport(TblYqnsAuditSuperReport tblYqnsAuditSuperReport){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_AUDIT_SUPER_REPORT (ID");

        StringBuffer valSb = new StringBuffer();
        if(tblYqnsAuditSuperReport.getId()!=null) {
        	 valSb.append(" VALUES ("+tblYqnsAuditSuperReport.getId());
        }else {
        	 valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");
        }
       

        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportName())){
            colSb.append(", REPORTNAME");
            valSb.append(", '" + tblYqnsAuditSuperReport.getReportName() + "'");
        }
        if (tblYqnsAuditSuperReport.getReportTime() != null){
            colSb.append(", REPORTTIME");
            valSb.append(", TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(tblYqnsAuditSuperReport.getReportTime()) + "', 'yyyy-MM-dd')");
//            valSb.append(", TO_DATE('" + tblYqnsAuditSuperReport.getReportTime() + "'");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportType())){
            colSb.append(", REPORTTYPE");
            valSb.append(", '" + tblYqnsAuditSuperReport.getReportType() + "'");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportWay())){
            colSb.append(", REPORTWAY");
            valSb.append(", '" + tblYqnsAuditSuperReport.getReportWay() + "'");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportDept())){
            colSb.append(", REPORTDEPT");
            valSb.append(", '" + tblYqnsAuditSuperReport.getReportDept() + "'");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReporter())){
            colSb.append(", REPORTER");
            valSb.append(", '" + tblYqnsAuditSuperReport.getReporter() + "'");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportContent())){
            colSb.append(", REPORTCONTENT");
            valSb.append(", '" + tblYqnsAuditSuperReport.getReportContent() + "'");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getAnnex())){
            colSb.append(", ANNEX");
            valSb.append(", '" + tblYqnsAuditSuperReport.getAnnex() + "'");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getProjectId())){
            colSb.append(", PROJECTID");
            valSb.append(", '" + tblYqnsAuditSuperReport.getProjectId() + "'");
        }
        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String updateReport(TblYqnsAuditSuperReport tblYqnsAuditSuperReport){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_AUDIT_SUPER_REPORT SET ");

        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportName())){
            sb.append("REPORTNAME = '" + tblYqnsAuditSuperReport.getReportName() + "', ");
        }
        if (tblYqnsAuditSuperReport.getReportTime() != null){
            sb.append("REPORTTIME = TO_DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(tblYqnsAuditSuperReport.getReportTime()) + "', 'yyyy-MM-dd'), ");
//            sb.append("REPORTTIME = '" + tblYqnsAuditSuperReport.getReportTime() + "', ");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportType())){
            sb.append("REPORTTYPE = '" + tblYqnsAuditSuperReport.getReportType() + "', ");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportWay())){
            sb.append("REPORTWAY = '" + tblYqnsAuditSuperReport.getReportWay() + "', ");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportDept())){
            sb.append("REPORTDEPT = '" + tblYqnsAuditSuperReport.getReportDept() + "', ");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReporter())){
            sb.append("REPORTER = '" + tblYqnsAuditSuperReport.getReporter() + "', ");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getReportContent())){
            sb.append("REPORTCONTENT = '" + tblYqnsAuditSuperReport.getReportContent() + "', ");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getAnnex())){
            sb.append("ANNEX = '" + tblYqnsAuditSuperReport.getAnnex() + "', ");
        }
        if (StringUtil.isNotEmpty(tblYqnsAuditSuperReport.getProjectId())){
            sb.append("PROJECTID = '" + tblYqnsAuditSuperReport.getProjectId() + "', ");
        }

//        sb.deleteCharAt(sb.length() - 1);
        sb.delete(sb.length() - 2, sb.length()); // 删除最后一个逗号和空格
        sb.append(" WHERE ID = '"+tblYqnsAuditSuperReport.getId()+"'");

        return sb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_AUDIT_SUPER_REPORT WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
