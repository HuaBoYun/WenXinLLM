package com.huabo.audit.oracle.mapper;

import cn.hutool.core.date.DateUtil;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.ProjectSuggestionNoticeEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectSuggestionNoticeMapperSqlConfig {

    public String selectByEntity(ProjectSuggestionNoticeEntity entity,TblStaff staff){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
//        sb.append("SELECT * FROM TBL_YQNS_PS_NOTICE RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        sb.append("SELECT * FROM TBL_YQNS_PS_NOTICE RS  WHERE 1=1 ");
        if(StringUtil.isNotEmpty(entity.getName())){
            sb.append("AND RS.NAME LIKE '%"+entity.getName()+"%'");
        }
        //用户创建只能看见自己的。部门负责人要查看全部内容；下发人员可以查询下发给自己的
        sb.append("AND (RS.CREATE_USER = "+ staff.getStaffid() );
        sb.append("OR INSTR (',' || PERSON_IDS || ',',',"+staff.getStaffid()+",') > 0");
        if(StringUtils.isNotBlank(entity.getQueryDeptIds())) {
            sb.append(" OR RS.CREATE_USER IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(entity.getQueryDeptIds()).append("))");
        }
        sb.append(")");

        sb.append(" order by CREATE_TIME desc");
        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        System.out.println(sb.toString());
        return sb.toString();

    }

    public String insertAttachments(BigDecimal id, String attachmentId){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO TBL_YQNS_PS_NOTICE_ATT(PID,ATTID) VALUES ("+ id + ","+ attachmentId +")");
        return sb.toString();
    }

    public String deleteAttachmentByIds(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_PS_NOTICE_ATT WHERE PID IN ("+ids+")");
        return sb.toString();
    }

    public String insertEntity(ProjectSuggestionNoticeEntity entity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_PS_NOTICE (ID");

        StringBuffer valSb = new StringBuffer();
        if(entity!=null && entity.getId()!=null) {
        	 valSb.append(" VALUES ("+entity.getId());
        }else {
        	 valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");
        }
       

        if(StringUtil.isNotEmpty(entity.getName())){
            colSb.append(", NAME");
            valSb.append(", '"+ entity.getName()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getOrgIds())){
            colSb.append(", ORG_IDS");
            valSb.append(", '"+ entity.getOrgIds()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getPsIds())){
            colSb.append(", PS_IDS");
            valSb.append(", '"+ entity.getPsIds()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getNoticeNo())){
            colSb.append(", NOTICENO");
            valSb.append(", '"+ entity.getNoticeNo()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getContent())){
            colSb.append(", CONTENT");
            valSb.append(", '"+ entity.getContent()+"'");
        }

        if(StringUtil.isNotEmpty(entity.getRemark())) {
            colSb.append(", REMARK");
            valSb.append(", '" + entity.getRemark() + "'");
        }

        if(entity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + entity.getCreateUser() + "'");
        }

        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }
}
