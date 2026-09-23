package com.huabo.audit.oracle.mapper;

import cn.hutool.core.date.DateUtil;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.ImplementPlanTeamEntity;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Rui
 * @ClassName ImplementPlanMapperSqlConfig
 * @Description
 * @DATE 2023/9/30
 */
public class ImplementPlanMapperSqlConfig {
	
	public String selectPageListForSjbgdg(ImplementPlanEntity implementPlanEntity, TblStaffUtil user) throws Exception{
		StringBuffer sqlSb = new StringBuffer(" SELECT PL.* from TBL_YQNS_IMPLEMENT_PLAN pl LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN_TEAM te on PL.id=TE.IM_PLAN_ID ");
		
		sqlSb.append(" WHERE pl.SPZT = 6 ");
		
		sqlSb.append(" AND pl.XMAPBID IN (SELECT ID FROM TBL_YQNS_FUND_AUDIT_PROJECT WHERE FPSLKRYID = '").append(user.getStaffid()).append("' UNION ALL SELECT ID FROM TBL_YQNS_ENGIN_AUDIT_PROJECT WHERE FPSLKRYID = '").append(user.getStaffid()).append("')");
		
          if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
        	  sqlSb.append(" and PL.PROJECT_NAME LIKE '%"+implementPlanEntity.getProjectName()+"%'");
          }
          
          sqlSb.append("  and ( 1=1  ");
          if (StringUtils.isNotBlank(user.getDeptIds())) {
        	  sqlSb.append(" or PL.CREATESTAFFID="+user.getStaffid()+"  or PL.CREATESTAFFID in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+user.getDeptIds()+")  )");
           }
          sqlSb.append(" ) ");
          
          sqlSb.append("  ORDER BY PL.ID DESC");
		
		return sqlSb.toString();
	}
	
	
	
	public String  selectReviewStatusList(ImplementPlanEntity implementPlanEntity) throws Exception{
		return new SQL(){
            {
                SELECT(" TYIT.TEAM_LEADER_ID AS LEADERID,TS.REALNAME AS LEADERNAME,TYIT.FZZSTAFFID,TYIT.FZZNAME,TYIP.* ");
                FROM(" TBL_YQNS_IMPLEMENT_PLAN_TEAM TYIT LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN TYIP ON TYIT.IM_PLAN_ID = TYIP.ID LEFT JOIN TBL_STAFF TS ON TYIT.TEAM_LEADER_ID = TS.STAFFID ");
                //已归档的项目不再查询
                WHERE("TYIP.ID IS NOT NULL");
                if(implementPlanEntity.getProjectOrderId() != null){
                   WHERE("TYIP.PROJECT_ORDER_ID = #{projectOrderId}");
                }
                if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
                    WHERE("TYIP.PROJECT_NAME LIKE '%"+implementPlanEntity.getProjectName()+"%'");
                }
                if(implementPlanEntity.getPlanStarttime() != null){
                    WHERE("TYIP.PLAN_STARTTIME >= #{planStarttime}");
                }
                if(implementPlanEntity.getPlanEndtime() != null){
                    WHERE("TYIP.PLAN_ENDTIME < #{planEndtime}");
                }
                ORDER_BY(" TYIP.ID DESC");
            }
        }.toString();
	}
	
	 public String selectByrwfpEntity( ImplementPlanEntity implementPlanEntity){
       StringBuffer sb = new StringBuffer();
       	sb.append("SELECT * FROM TBL_YQNS_IMPLEMENT_PLAN WHERE 1=1 and STATUS!=4 and SPZT=6   ");

        if(implementPlanEntity.getProjectOrderId() != null){
           sb.append("AND ( ZSSTAFFID ='"+implementPlanEntity.getProjectOrderId()+"' or PROJECT_ORDER_ID= '"+implementPlanEntity.getProjectOrderId()+"' )");
       } 

       if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
           sb.append("AND PROJECT_NAME LIKE '%"+implementPlanEntity.getProjectName()+"%'");
       }
       
       sb.append(" order by ID desc ");
       return sb.toString();

   }
	 
	 
	 public String selectBysqEntity( ImplementPlanEntity implementPlanEntity,BigDecimal staffId){
	       StringBuffer sb = new StringBuffer();
	       	sb.append("SELECT pl.* FROM TBL_YQNS_IMPLEMENT_PLAN pl LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN_TEAM te on PL.id=TE.IM_PLAN_ID WHERE 1=1 and pl.STATUS!=4  and  pl.SPZT=6   ");

	        if(implementPlanEntity.getProjectOrderId() != null){
	           sb.append("AND pl.PROJECT_ORDER_ID= '"+implementPlanEntity.getProjectOrderId()+"'");
	       }

	       if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
	           sb.append("AND pl.PROJECT_NAME LIKE '%"+implementPlanEntity.getProjectName()+"%'");
	       }
	       if(staffId != null){
	        	  sb.append(" and (PL.ZSSTAFFID = "+staffId+" OR PL.PROJECT_ORDER_ID = "+staffId+"  OR PL.CREATESTAFFID ="+staffId+"  OR TE.FZZSTAFFID ="+staffId+" or TE.TEAM_MEMBERS_IDS like '%"+staffId+"%' )");
	          }
	       
	       sb.append(" order by pl.ID desc ");
	       return sb.toString();

	   }
	 
	 public String updateFpStatus(Integer fpStatus,BigDecimal projectId) {
			StringBuffer sqlSb = new StringBuffer("UPDATE TBL_YQNS_IMPLEMENT_PLAN SET FPSTATUS='"+fpStatus+"' ");
			
			sqlSb.append(" WHERE ID= "+projectId);
			return sqlSb.toString();
		}
	 
	 public String updatePjStart(BigDecimal projectid) {
			StringBuffer sqlSb = new StringBuffer("UPDATE TBL_YQNS_IMPLEMENT_PLAN SET STATUS = 1,FPSTATUS=3 ");
			
			sqlSb.append(" WHERE ID= "+projectid);
			return sqlSb.toString();
		}
	
	
	

    public String selectByEntity( ImplementPlanEntity implementPlanEntity, BigDecimal staffId, Integer xmnd, TblStaffUtil user){
//        StringBuffer sb = new StringBuffer();
//        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
//        sb.append("SELECT * FROM TBL_YQNS_IMPLEMENT_PLAN RS WHERE 1=1 ");
//
//
//        if(implementPlanEntity.getProjectOrderId() != null){
//            sb.append("AND RS.PROJECT_ORDER_ID = '"+implementPlanEntity.getProjectOrderId()+"'");
//        }
//
//        if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
//            sb.append("AND RS.PROJECT_NAME LIKE '%"+implementPlanEntity.getProjectName()+"%'");
//        }
//
//        if(implementPlanEntity.getPlanStarttime() != null){
//            sb.append("AND RS.PLAN_STARTTIME >= '"+implementPlanEntity.getPlanStarttime()+"'");
//        }
//
//        if(implementPlanEntity.getPlanEndtime() != null){
//            sb.append("AND RS.PLAN_ENDTIME < '"+implementPlanEntity.getPlanEndtime()+"'");
//        }
//
//        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
//        return sb.toString();
    	StringBuffer sqlSb = new StringBuffer(" SELECT PL.* from TBL_YQNS_IMPLEMENT_PLAN pl LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN_TEAM te on PL.id=TE.IM_PLAN_ID ");
		
		sqlSb.append(" WHERE 1=1 ");
		if(xmnd==null ) {
			sqlSb.append(" and  PL.STATUS <> 4 and  PL.STATUS <> 5 ");
			
        }else {
    	   sqlSb.append(" and PL.SPZT = 6 ");
    	  
        }
		
		 if(implementPlanEntity.getProjectOrderId() != null){
			 sqlSb.append(" and PL.PROJECT_ORDER_ID = "+implementPlanEntity.getProjectOrderId());
          }
          if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
        	  sqlSb.append(" and PL.PROJECT_NAME LIKE '%"+implementPlanEntity.getProjectName()+"%'");
          }
          if(implementPlanEntity.getPlanStarttime() != null){
        	  sqlSb.append(" and PL.PLAN_STARTTIME >= "+implementPlanEntity.getPlanStarttime());
          }
          if(implementPlanEntity.getPlanEndtime() != null){
        	  sqlSb.append(" and PL.PLAN_ENDTIME < "+implementPlanEntity.getPlanEndtime());
          }
          if(implementPlanEntity.getSpzt() != null){
        	  sqlSb.append(" and PL.SPZT = '"+implementPlanEntity.getSpzt()+"'");
           }
          
          
          if (StringUtils.isNotBlank(implementPlanEntity.getXctype()) && implementPlanEntity.getXctype().equals("sjtzsp") ) {
        	  sqlSb.append(" and   PL.ID  NOT IN (select PROGECTID from TBL_YQNS_ADVICEAPR where PROGECTID is not NULL)  ");
           }
          
          if (StringUtils.isNotBlank(implementPlanEntity.getXctype()) && implementPlanEntity.getXctype().equals("xmqd") ) {
        	  sqlSb.append(" and   PL.ID  NOT IN (select gljhxmid from TBL_YQNS_XMQD where gljhxmid is not NULL)  ");
           }
          
          if(xmnd != null) {
        	  sqlSb.append(" and PL.PLAN_YEAR = "+xmnd);
          }
          sqlSb.append("  and ( 1=1  ");
          if (StringUtils.isNotBlank(user.getDeptIds())) {
        	  sqlSb.append(" and PL.CREATESTAFFID="+user.getStaffid()+"  or PL.CREATESTAFFID in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+user.getDeptIds()+")  )");
        	  if(staffId != null){
            	  sqlSb.append(" or (PL.ZSSTAFFID = "+staffId+" OR PL.PROJECT_ORDER_ID = "+staffId+"  OR PL.CREATESTAFFID ="+staffId+"  OR TE.FZZSTAFFID ="+staffId+" or TE.TEAM_MEMBERS_IDS like '%"+staffId+"%' )");
              }
          }else {
        	  if(staffId != null){
            	  sqlSb.append(" and (PL.ZSSTAFFID = "+staffId+" OR PL.PROJECT_ORDER_ID = "+staffId+"  OR PL.CREATESTAFFID ="+staffId+"  OR TE.FZZSTAFFID ="+staffId+" or TE.TEAM_MEMBERS_IDS like '%"+staffId+"%' )");
              }
          }
          
          if(staffId != null){
        	  sqlSb.append(" or (PL.ZSSTAFFID = "+staffId+" OR PL.PROJECT_ORDER_ID = "+staffId+"  OR PL.CREATESTAFFID ="+staffId+"  OR TE.FZZSTAFFID ="+staffId+" or TE.TEAM_MEMBERS_IDS like '%"+staffId+"%' )");
          }
          sqlSb.append(" ) ");
          
          sqlSb.append("  ORDER BY PL.CREATEDATE DESC");
		
		return sqlSb.toString();
    	
//    	
//        return new SQL(){
//            {
//                SELECT(" PL.* ");
//                FROM(" TBL_YQNS_IMPLEMENT_PLAN pl LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN_TEAM te on PL.id=TE.IM_PLAN_ID ");
//                //已归档的项目不再查询
//                if(xmnd==null ) {
//                	 WHERE(" PL.STATUS <> 4 ");
//                }else {
//                	WHERE(" PL.SPZT = 6 ");
//                }
//               
//                if(implementPlanEntity.getProjectOrderId() != null){
//                   WHERE("PL.PROJECT_ORDER_ID = "+implementPlanEntity.getProjectOrderId());
//                }
//                if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
//                    WHERE("PL.PROJECT_NAME LIKE '%"+implementPlanEntity.getProjectName()+"%'");
//                }
//                if(implementPlanEntity.getPlanStarttime() != null){
//                    WHERE("PL.PLAN_STARTTIME >= "+implementPlanEntity.getPlanStarttime());
//                }
//                if(implementPlanEntity.getPlanEndtime() != null){
//                    WHERE("PL.PLAN_ENDTIME < "+implementPlanEntity.getPlanEndtime());
//                }
//                if(implementPlanEntity.getSpzt() != null){
//                    WHERE("PL.SPZT = '"+implementPlanEntity.getSpzt()+"'");
//                 }
//                
//                if(xmnd != null) {
//                	WHERE(" PL.PLAN_YEAR = "+xmnd);
//                }
//                
//                if(staffId != null){
//                	WHERE(" (PL.ZSSTAFFID = #{staffId} OR PL.PROJECT_ORDER_ID = #{staffId}  OR PL.CREATESTAFFID = #{staffId}  OR TE.FZZSTAFFID =#{staffId} or TE.TEAM_MEMBERS_IDS like '%#{staffId}%' )");
//                }
//                ORDER_BY(" PL.ID DESC");
//            }
//        }.toString();

    }

    public String selectCountByEntity(ImplementPlanEntity implementPlanEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_IMPLEMENT_PLAN RS  WHERE 1=1 ");


        if(implementPlanEntity.getProjectOrderId() != null){
            sb.append("AND RS.PROJECT_ORDER_ID = '"+implementPlanEntity.getProjectOrderId()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+implementPlanEntity.getProjectName()+"%'");
        }

        if(implementPlanEntity.getPlanStarttime() != null){
            sb.append("AND RS.PLAN_STARTTIME >= TO_DATE('"+implementPlanEntity.getPlanStarttime()+"','yyyy-mm-dd hh24:mi:ss')");
        }

        if(implementPlanEntity.getPlanEndtime() != null){
            sb.append("AND RS.PLAN_ENDTIME < TO_DATE('"+implementPlanEntity.getPlanEndtime()+"','yyyy-mm-dd hh24:mi:ss')");
        }

        sb.append(")"); 
        return sb.toString();
    }

    public String updateEntity(ImplementPlanEntity implementPlanEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_IMPLEMENT_PLAN SET QDCODE='"+implementPlanEntity.getQdcode()+"'");
        if(implementPlanEntity.getPlanId()!=null) {
        	 sb.append(",PLAN_ID = '"+implementPlanEntity.getPlanId()+"'");
        }
        
        if(StringUtil.isNotEmpty(implementPlanEntity.getNo())){
            sb.append(", NO = '"+implementPlanEntity.getNo()+"'");
        }
       
        if(StringUtil.isNotEmpty(implementPlanEntity.getPlanName())){
            sb.append(", PLAN_NAME = '"+implementPlanEntity.getPlanName()+"'");
        }


        if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
            sb.append(", PROJECT_NAME = '"+implementPlanEntity.getProjectName()+"'");

        }

        if(implementPlanEntity.getPlanProjectId() != null){
            sb.append(", PLAN_PROJECT_ID = '"+implementPlanEntity.getPlanProjectId()+"'");

        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getPlanProjectName())){
            sb.append(", PLAN_PROJECT_NAME = '"+implementPlanEntity.getPlanProjectName()+"'");

        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getProjectOrderName())){
            sb.append(", PROJECT_ORDER_NAME = '"+implementPlanEntity.getProjectOrderName()+"'");

        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditOrgName())){
            sb.append(", AUDIT_ORG_NAME = '"+implementPlanEntity.getAuditOrgName()+"'");

        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getTempName())){
            sb.append(", TEMP_NAME = '"+implementPlanEntity.getTempName()+"'");

        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getDeptName())){
            sb.append(", DEPT_NAME = '"+implementPlanEntity.getDeptName()+"'");

        }
   
        if(implementPlanEntity.getProjectType() != null){
            sb.append(", PROJECT_TYPE = '"+implementPlanEntity.getProjectType()+"'");
        }

        if(implementPlanEntity.getAuditOrgId() != null){
            sb.append(", AUDIT_ORG_ID = '"+implementPlanEntity.getAuditOrgId()+"'");
        }

        if(implementPlanEntity.getPlanYear() != null){
            sb.append(", PLAN_YEAR = '"+implementPlanEntity.getPlanYear()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getPlanTime())){
            sb.append(", PLAN_TIME = '"+implementPlanEntity.getPlanTime()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getProjectSummary())){ 
            sb.append(", PROJECT_SUMMARY = '"+implementPlanEntity.getProjectSummary()+"'");
        }

        if(implementPlanEntity.getProjectOrderId() != null){
            sb.append(", PROJECT_ORDER_ID = '"+implementPlanEntity.getProjectOrderId()+"'");
        }

        if(implementPlanEntity.getPlanStarttime() != null){
        	 SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			try {
				Date date = formatter.parse(implementPlanEntity.getPlanStarttime().toString());
				 // 创建另一个SimpleDateFormat对象，定义输出格式
	             SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
	  
	  
	             // 使用format方法格式化日期
	             String formattedDate = outputFormatter.format(date);
	             System.out.println(formattedDate);
//	            sb.append(", PLAN_STARTTIME = '"+ DateUtil.format(implementPlanEntity.getPlanStarttime(),"yyyy-MM-dd")+"'");
	            sb.append(",PLAN_STARTTIME= TO_DATE('"+formattedDate+"','yyyy-mm-dd')");
			} catch (ParseException e) {
				e.printStackTrace();
			}
  
            
        }
        
        if(implementPlanEntity.getUpdatedate() != null) {
        	sb.append(",UPDATEDATE = SYSDATE ");
        }
        
        if(implementPlanEntity.getPlanEndtime() != null){
//        	sb.append(",PLAN_ENDTIME= TO_DATE('"+DateUtil.format(implementPlanEntity.getPlanStarttime(),"yyyy-mm-dd hh24:mi:ss")+"','yyyy-mm-dd hh24:mi:ss')");
//            sb.append(", PLAN_ENDTIME = '"+DateUtil.format(implementPlanEntity.getPlanEndtime(),"yyyy-MM-dd")+"'");
        	try {
        		 SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = formatter.parse(implementPlanEntity.getPlanEndtime().toString());
				 // 创建另一个SimpleDateFormat对象，定义输出格式
	             SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
	  
	  
	             // 使用format方法格式化日期
	             String formattedDate = outputFormatter.format(date);
	             System.out.println(formattedDate);
//	            sb.append(", PLAN_STARTTIME = '"+ DateUtil.format(implementPlanEntity.getPlanStarttime(),"yyyy-MM-dd")+"'");
	            sb.append(",PLAN_ENDTIME= TO_DATE('"+formattedDate+"','yyyy-mm-dd')");
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }

        if(implementPlanEntity.getAuditMethod() != null){
            sb.append(", AUDIT_METHOD = '"+implementPlanEntity.getAuditMethod()+"'");
        }

        if(implementPlanEntity.getCostEstimation() != null){
            sb.append(", COST_ESTIMATION = '"+implementPlanEntity.getCostEstimation()+"'");
        }

        if(implementPlanEntity.getIsWw() != null){
            sb.append(", IS_WW = '"+implementPlanEntity.getIsWw()+"'");
        }

        if(implementPlanEntity.getTempId() != null){
            sb.append(", TEMP_ID = '"+implementPlanEntity.getTempId()+"'");
        }

        if(implementPlanEntity.getProjecttempId() != null){
            sb.append(", PROJECTTEMP_ID = '"+implementPlanEntity.getProjecttempId()+"'");
        }
        
        if(implementPlanEntity.getProjecttempId() != null){
            sb.append(", PROJECTTEMP_NAME = '"+implementPlanEntity.getProjecttempName()+"'");
        }

        if(implementPlanEntity.getSjlxId() != null){
            sb.append(", SJLX_ID = '"+implementPlanEntity.getSjlxId()+"'");
        }
        
        if(implementPlanEntity.getSjlxName() != null){
            sb.append(", SJLX_NAME = '"+implementPlanEntity.getSjlxName()+"'");
        }

        if(implementPlanEntity.getImplementType() != null){
            sb.append(", IMPLEMENT_TYPE = '"+implementPlanEntity.getImplementType()+"'");
        }

        if(implementPlanEntity.getDeptId() != null){
            sb.append(", DEPT_ID = '"+implementPlanEntity.getDeptId()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getImplementSteps())){
            sb.append(", IMPLEMENT_STEPS = '"+implementPlanEntity.getImplementSteps()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditRequirement())){
            sb.append(", AUDIT_REQUIREMENT = '"+implementPlanEntity.getAuditRequirement()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditReason())){
            sb.append(", AUDIT_REASON = '"+implementPlanEntity.getAuditReason()+"'");
        }
 
        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditContent())){
            sb.append(", AUDIT_CONTENT = '"+implementPlanEntity.getAuditContent()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditProcess())){
            sb.append(", AUDIT_PROCESS = '"+implementPlanEntity.getAuditProcess()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditResultUse())){
            sb.append(", AUDIT_RESULT_USE = '"+implementPlanEntity.getAuditResultUse()+"'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getOtherContent())){
            sb.append(", OTHER_CONTENT = '"+implementPlanEntity.getOtherContent()+"'");
        }
        
        
        if(StringUtil.isNotEmpty(implementPlanEntity.getIsgc())){
        	sb.append(", ISGC = '"+implementPlanEntity.getIsgc()+"'");
        }
//        if(StringUtil.isNotEmpty(implementPlanEntity.getQdcode())){
//        	sb.append(", QDCODE = '"+implementPlanEntity.getQdcode()+"'");
//        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getXmname())){
        	sb.append(", XMNAME = '"+implementPlanEntity.getXmname()+"'");
        }

        if(implementPlanEntity.getXmdqid() != null){
        	sb.append(", XMDQID = '"+implementPlanEntity.getXmdqid()+"'");
        }
        
        if(StringUtil.isNotEmpty(implementPlanEntity.getIsjy())){
        	sb.append(", ISJY = '"+implementPlanEntity.getIsjy()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getFzname())){
        	sb.append(", FZNAME = '"+implementPlanEntity.getFzname()+"'");
        }
        
        if(implementPlanEntity.getFzstaffid() != null){
        	sb.append(", FZSTAFFID = '"+implementPlanEntity.getFzstaffid()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getZsname())){
        	sb.append(", ZSNAME = '"+implementPlanEntity.getZsname()+"'");
        }
        
        if(implementPlanEntity.getZsstaffid() != null){
        	sb.append(", ZSSTAFFID = '"+implementPlanEntity.getZsstaffid()+"'");
        }
        
        if(implementPlanEntity.getZyksryids() != null){
        	sb.append(", ZYKSRYIDS = '"+implementPlanEntity.getZyksryids()+"'");
        }
        
        if(implementPlanEntity.getZyksryrwnames() != null){
        	sb.append(", ZYKSRYRWNAMES = '"+implementPlanEntity.getZyksryrwnames()+"'");
        }
        if(implementPlanEntity.getXmapbid() != null){
        	sb.append(", XMAPBID = '"+implementPlanEntity.getXmapbid()+"'");
        }

// 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString1())){
            sb.append(", RESERVEDSTRING1 = '"+implementPlanEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString2())){
            sb.append(", RESERVEDSTRING2 = '"+implementPlanEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString3())){
            sb.append(", RESERVEDSTRING3 = '"+implementPlanEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString4())){
            sb.append(", RESERVEDSTRING4 = '"+implementPlanEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString5())){
            sb.append(", RESERVEDSTRING5 = '"+implementPlanEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString6())){
            sb.append(", RESERVEDSTRING6 = '"+implementPlanEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString7())){
            sb.append(", RESERVEDSTRING7 = '"+implementPlanEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString8())){
            sb.append(", RESERVEDSTRING8 = '"+implementPlanEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString9())){
            sb.append(", RESERVEDSTRING9 = '"+implementPlanEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString10())){
            sb.append(", RESERVEDSTRING10 = '"+implementPlanEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent1())){
            sb.append(", RESERVEDCONTENT1 = '"+implementPlanEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent2())){
            sb.append(", RESERVEDCONTENT2 = '"+implementPlanEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent3())){
            sb.append(", RESERVEDCONTENT3 = '"+implementPlanEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent4())){
            sb.append(", RESERVEDCONTENT4 = '"+implementPlanEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent5())){
            sb.append(", RESERVEDCONTENT5 = '"+implementPlanEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent6())){
            sb.append(", RESERVEDCONTENT6 = '"+implementPlanEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent7())){
            sb.append(", RESERVEDCONTENT7 = '"+implementPlanEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent8())){
            sb.append(", RESERVEDCONTENT8 = '"+implementPlanEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent9())){
            sb.append(", RESERVEDCONTENT9 = '"+implementPlanEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent10())){
            sb.append(", RESERVEDCONTENT10 = '"+implementPlanEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple1())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '"+implementPlanEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple2())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '"+implementPlanEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple3())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '"+implementPlanEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple4())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '"+implementPlanEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple5())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '"+implementPlanEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice1())){
            sb.append(", RESERVEDMULTIPLECHOICE1 = '"+implementPlanEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice2())){
            sb.append(", RESERVEDMULTIPLECHOICE2 = '"+implementPlanEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice3())){
            sb.append(", RESERVEDMULTIPLECHOICE3 = '"+implementPlanEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice4())){
            sb.append(", RESERVEDMULTIPLECHOICE4 = '"+implementPlanEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice5())){
            sb.append(", RESERVEDMULTIPLECHOICE5 = '"+implementPlanEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(implementPlanEntity.getReservedYearTime1() != null){
            sb.append(", RESERVEDYEARTIME1 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(implementPlanEntity.getReservedYearTime2() != null){
            sb.append(", RESERVEDYEARTIME2 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(implementPlanEntity.getReservedYearTime3() != null){
            sb.append(", RESERVEDYEARTIME3 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(implementPlanEntity.getReservedYearTime4() != null){
            sb.append(", RESERVEDYEARTIME4 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(implementPlanEntity.getReservedYearTime5() != null){
            sb.append(", RESERVEDYEARTIME5 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(implementPlanEntity.getReservedYearAccurateTime1() != null){
            sb.append(", RESERVEDYEARACCURATETIME1 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(implementPlanEntity.getReservedYearAccurateTime2() != null){
            sb.append(", RESERVEDYEARACCURATETIME2 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(implementPlanEntity.getReservedYearAccurateTime3() != null){
            sb.append(", RESERVEDYEARACCURATETIME3 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(implementPlanEntity.getReservedYearAccurateTime4() != null){
            sb.append(", RESERVEDYEARACCURATETIME4 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(implementPlanEntity.getReservedYearAccurateTime5() != null){
            sb.append(", RESERVEDYEARACCURATETIME5 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(implementPlanEntity.getReservedTime1() != null){
            sb.append(", RESERVEDTIME1 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(implementPlanEntity.getReservedTime2() != null){
            sb.append(", RESERVEDTIME2 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(implementPlanEntity.getReservedTime3() != null){
            sb.append(", RESERVEDTIME3 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(implementPlanEntity.getReservedTime4() != null){
            sb.append(", RESERVEDTIME4 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(implementPlanEntity.getReservedTime5() != null){
            sb.append(", RESERVEDTIME5 = TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice1())){
            sb.append(", RESERVEDSINGLECHOICE1 = '"+implementPlanEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice2())){
            sb.append(", RESERVEDSINGLECHOICE2 = '"+implementPlanEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice3())){
            sb.append(", RESERVEDSINGLECHOICE3 = '"+implementPlanEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice4())){
            sb.append(", RESERVEDSINGLECHOICE4 = '"+implementPlanEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice5())){
            sb.append(", RESERVEDSINGLECHOICE5 = '"+implementPlanEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice1())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '"+implementPlanEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice2())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '"+implementPlanEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice3())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '"+implementPlanEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice4())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '"+implementPlanEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice5())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '"+implementPlanEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(implementPlanEntity.getReservedNum1() != null){
            sb.append(", RESERVEDNUM1 = "+implementPlanEntity.getReservedNum1());
        }
        if(implementPlanEntity.getReservedNum2() != null){
            sb.append(", RESERVEDNUM2 = "+implementPlanEntity.getReservedNum2());
        }
        if(implementPlanEntity.getReservedNum3() != null){
            sb.append(", RESERVEDNUM3 = "+implementPlanEntity.getReservedNum3());
        }
        if(implementPlanEntity.getReservedNum4() != null){
            sb.append(", RESERVEDNUM4 = "+implementPlanEntity.getReservedNum4());
        }
        if(implementPlanEntity.getReservedNum5() != null){
            sb.append(", RESERVEDNUM5 = "+implementPlanEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(implementPlanEntity.getStaffid1() != null){
            sb.append(", STAFFID1 = "+implementPlanEntity.getStaffid1());
        }
        if(implementPlanEntity.getStaffid2() != null){
            sb.append(", STAFFID2 = "+implementPlanEntity.getStaffid2());
        }
        if(implementPlanEntity.getStaffid3() != null){
            sb.append(", STAFFID3 = "+implementPlanEntity.getStaffid3());
        }
        if(implementPlanEntity.getStaffid4() != null){
            sb.append(", STAFFID4 = "+implementPlanEntity.getStaffid4());
        }
        if(implementPlanEntity.getStaffid5() != null){
            sb.append(", STAFFID5 = "+implementPlanEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids1())){
            sb.append(", STAFFIDS1 = '"+implementPlanEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids2())){
            sb.append(", STAFFIDS2 = '"+implementPlanEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids3())){
            sb.append(", STAFFIDS3 = '"+implementPlanEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids4())){
            sb.append(", STAFFIDS4 = '"+implementPlanEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids5())){
            sb.append(", STAFFIDS5 = '"+implementPlanEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(implementPlanEntity.getOrgid1() != null){
            sb.append(", ORGID1 = "+implementPlanEntity.getOrgid1());
        }
        if(implementPlanEntity.getOrgid2() != null){
            sb.append(", ORGID2 = "+implementPlanEntity.getOrgid2());
        }
        if(implementPlanEntity.getOrgid3() != null){
            sb.append(", ORGID3 = "+implementPlanEntity.getOrgid3());
        }
        if(implementPlanEntity.getOrgid4() != null){
            sb.append(", ORGID4 = "+implementPlanEntity.getOrgid4());
        }
        if(implementPlanEntity.getOrgid5() != null){
            sb.append(", ORGID5 = "+implementPlanEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids1())){
            sb.append(", ORGIDS1 = '"+implementPlanEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids2())){
            sb.append(", ORGIDS2 = '"+implementPlanEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids3())){
            sb.append(", ORGIDS3 = '"+implementPlanEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids4())){
            sb.append(", ORGIDS4 = '"+implementPlanEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids5())){
            sb.append(", ORGIDS5 = '"+implementPlanEntity.getOrgids5()+"'");
        }

        sb.append(" WHERE ID = '"+implementPlanEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(ImplementPlanEntity implementPlanEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_IMPLEMENT_PLAN (ID");

        StringBuffer valSb = new StringBuffer();
      
        if(implementPlanEntity.getId()!=null) {
        	  valSb.append(" VALUES ("+implementPlanEntity.getId());
        }else {
        	  valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");
        }

        if(implementPlanEntity.getPlanId() != null){
            colSb.append(", PLAN_ID");
            valSb.append(", '" + implementPlanEntity.getPlanId() + "'");
        }
        
        if(implementPlanEntity.getCreatestaffid() != null){
            colSb.append(", CREATESTAFFID");
            valSb.append(", '" + implementPlanEntity.getCreatestaffid() + "'");
        }
        
        if(implementPlanEntity.getFpStatus()!= null){
            colSb.append(", FPSTATUS");
            valSb.append(", " + implementPlanEntity.getFpStatus() );
        }
        
        if(implementPlanEntity.getXmapbid() != null){
            colSb.append(", XMAPBID");
            valSb.append(", '" + implementPlanEntity.getXmapbid() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getPlanName())){
            colSb.append(", PLAN_NAME");
            valSb.append(", '" + implementPlanEntity.getPlanName() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getNo())){
            colSb.append(", NO");
            valSb.append(", '" + implementPlanEntity.getNo() + "'");
        }
        
        if(StringUtil.isNotEmpty(implementPlanEntity.getProjectOrderName())){
            colSb.append(", PROJECT_ORDER_NAME");
            valSb.append(", '" + implementPlanEntity.getProjectOrderName() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getDeptName())){
            colSb.append(", DEPT_NAME");
            valSb.append(", '" + implementPlanEntity.getDeptName() + "'");
        }

         
        
        if(StringUtil.isNotEmpty(implementPlanEntity.getZykstype())){
            colSb.append(", ZYKSTYPE");
            valSb.append(", '" + implementPlanEntity.getZykstype() + "'");
        }
        
        if(StringUtil.isNotEmpty(implementPlanEntity.getTempName())){
            colSb.append(", TEMP_NAME");
            valSb.append(", '" + implementPlanEntity.getTempName() + "'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getSjlxName())){
            colSb.append(", SJLX_NAME");
            valSb.append(", '" + implementPlanEntity.getSjlxName() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '" + implementPlanEntity.getProjectName() + "'");
        }

        if(implementPlanEntity.getPlanProjectId() != null){
            colSb.append(", PLAN_PROJECT_ID");
            valSb.append(", '" + implementPlanEntity.getPlanProjectId() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getPlanProjectName())){
            colSb.append(", PLAN_PROJECT_NAME");
            valSb.append(", '" + implementPlanEntity.getPlanProjectName() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditOrgName())){
            colSb.append(", AUDIT_ORG_NAME");
            valSb.append(", '" + implementPlanEntity.getAuditOrgName() + "'");
        }

        if(implementPlanEntity.getProjectType() != null){
            colSb.append(", PROJECT_TYPE");
            valSb.append(", '" + implementPlanEntity.getProjectType() + "'");
        }

        if(implementPlanEntity.getAuditOrgId() != null){
            colSb.append(", AUDIT_ORG_ID");
            valSb.append(", '" + implementPlanEntity.getAuditOrgId() + "'");
        }

        if(implementPlanEntity.getPlanYear() != null){
            colSb.append(", PLAN_YEAR");
            valSb.append(", '" + implementPlanEntity.getPlanYear() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getPlanTime())){
            colSb.append(", PLAN_TIME");
            valSb.append(", '" + implementPlanEntity.getPlanTime() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getProjectSummary())){
            colSb.append(", PROJECT_SUMMARY");
            valSb.append(", '" + implementPlanEntity.getProjectSummary() + "'");
        }

        if(implementPlanEntity.getProjectOrderId() != null){
            colSb.append(", PROJECT_ORDER_ID");
            valSb.append(", '" + implementPlanEntity.getProjectOrderId() + "'");
        }

        if(implementPlanEntity.getPlanStarttime() != null){
            colSb.append(", PLAN_STARTTIME");
            try {
       		 SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = formatter.parse(implementPlanEntity.getPlanStarttime().toString());
				 // 创建另一个SimpleDateFormat对象，定义输出格式
	             SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
	  
	  
	             // 使用format方法格式化日期
	             String formattedDate = outputFormatter.format(date);
	             System.out.println(formattedDate);
//	            sb.append(", PLAN_STARTTIME = '"+ DateUtil.format(implementPlanEntity.getPlanStarttime(),"yyyy-MM-dd")+"'");
	            valSb.append(", TO_DATE('"+formattedDate+"','yyyy-mm-dd hh24:mi:ss')");
			} catch (ParseException e) {
				e.printStackTrace();
			}
           
        }

        if(implementPlanEntity.getPlanEndtime() != null){
            colSb.append(", PLAN_ENDTIME");
//            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getPlanEndtime(),"yyyy-mm-dd hh24:mi:ss")+"','yyyy-mm-dd hh24:mi:ss')");
            try {
          		 SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
   				Date date = formatter.parse(implementPlanEntity.getPlanEndtime().toString());
   				 // 创建另一个SimpleDateFormat对象，定义输出格式
   	             SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
   	  
   	             // 使用format方法格式化日期
   	             String formattedDate = outputFormatter.format(date);
   	             System.out.println(formattedDate);
//   	            sb.append(", PLAN_STARTTIME = '"+ DateUtil.format(implementPlanEntity.getPlanStarttime(),"yyyy-MM-dd")+"'");
   	            valSb.append(", TO_DATE('"+formattedDate+"','yyyy-mm-dd hh24:mi:ss')");
   			} catch (ParseException e) {
   				e.printStackTrace();
   			}
        }

        if(implementPlanEntity.getCreatedate() != null){
            colSb.append(", CREATEDATE");
//            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getCreatedate(),"yyyy-mm-dd hh24:mi:ss")+"','yyyy-mm-dd hh24:mi:ss')");
            try {
         		 SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
  				Date date = formatter.parse(implementPlanEntity.getCreatedate().toString());
  				 // 创建另一个SimpleDateFormat对象，定义输出格式
  	             SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
  	  
  	             // 使用format方法格式化日期
  	             String formattedDate = outputFormatter.format(date);
  	             System.out.println(formattedDate);
//  	            sb.append(", PLAN_STARTTIME = '"+ DateUtil.format(implementPlanEntity.getPlanStarttime(),"yyyy-MM-dd")+"'");
  	            valSb.append(", TO_DATE('"+formattedDate+"','yyyy-mm-dd hh24:mi:ss')");
  			} catch (ParseException e) {
  				e.printStackTrace();
  			}
        }
        
        
        if(implementPlanEntity.getAuditMethod() != null){
            colSb.append(", AUDIT_METHOD");
            valSb.append(", '" + implementPlanEntity.getAuditMethod() + "'");
        }

        if(implementPlanEntity.getCostEstimation() != null){
            colSb.append(", COST_ESTIMATION");
            valSb.append(", '" + implementPlanEntity.getCostEstimation() + "'");
        }

        if(implementPlanEntity.getIsWw() != null){
            colSb.append(", IS_WW");
            valSb.append(", '" + implementPlanEntity.getIsWw() + "'");
        }

        if(implementPlanEntity.getTempId() != null){
            colSb.append(", TEMP_ID");
            valSb.append(", '" + implementPlanEntity.getTempId() + "'");
        }

        if(implementPlanEntity.getImplementType() != null){
            colSb.append(", IMPLEMENT_TYPE");
            valSb.append(", '" + implementPlanEntity.getImplementType() + "'");
        }

        if(implementPlanEntity.getDeptId() != null){
            colSb.append(", DEPT_ID");
            valSb.append(", '" + implementPlanEntity.getDeptId() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getImplementSteps())){
            colSb.append(", IMPLEMENT_STEPS");
            valSb.append(", '" + implementPlanEntity.getImplementSteps() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditRequirement())){
            colSb.append(", AUDIT_REQUIREMENT");
            valSb.append(", '" + implementPlanEntity.getAuditRequirement() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditReason())){
            colSb.append(", AUDIT_REASON");
            valSb.append(", '" + implementPlanEntity.getAuditReason() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditContent())){
            colSb.append(", AUDIT_CONTENT");
            valSb.append(", '" + implementPlanEntity.getAuditContent() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditProcess())){
            colSb.append(", AUDIT_PROCESS");
            valSb.append(", '" + implementPlanEntity.getAuditProcess() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getAuditResultUse())){
            colSb.append(", AUDIT_RESULT_USE");
            valSb.append(", '" + implementPlanEntity.getAuditResultUse() + "'");
        }

        if(StringUtil.isNotEmpty(implementPlanEntity.getOtherContent())){
            colSb.append(", OTHER_CONTENT");
            valSb.append(", '" + implementPlanEntity.getOtherContent() + "'");
        }
        
        if(StringUtil.isNotEmpty(implementPlanEntity.getIsgc())){
            colSb.append(", ISGC");
            valSb.append(", '" + implementPlanEntity.getIsgc() + "'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getQdcode())){
            colSb.append(", QDCODE");
            valSb.append(", '" + implementPlanEntity.getQdcode() + "'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getXmname())){
            colSb.append(", XMNAME");
            valSb.append(", '" + implementPlanEntity.getXmname() + "'");
        }

        if(implementPlanEntity.getXmdqid() != null){
            colSb.append(", XMDQID");
            valSb.append(", " + implementPlanEntity.getXmdqid() );
        }
        
        if(StringUtil.isNotEmpty(implementPlanEntity.getIsjy())){
            colSb.append(", ISJY");
            valSb.append(", '" + implementPlanEntity.getIsjy() + "'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getFzname())){
            colSb.append(", FZNAME");
            valSb.append(", '" + implementPlanEntity.getFzname() + "'");
        }
        
        if(implementPlanEntity.getFzstaffid() != null){
            colSb.append(", FZSTAFFID");
            valSb.append(", " + implementPlanEntity.getFzstaffid() );
        }
//        if(StringUtil.isNotEmpty(implementPlanEntity.getZsname())){
//            colSb.append(", ZSNAME");
//            valSb.append(", '" + implementPlanEntity.getZsname() + "'");
//        }
        
        if(implementPlanEntity.getZsstaffid() != null){
            colSb.append(", ZSSTAFFID");
            valSb.append(", " + implementPlanEntity.getZsstaffid() );
        }
        if(implementPlanEntity.getUpdateStatus() != null){
            colSb.append(", UPDATESTATUS");
            valSb.append(", " + implementPlanEntity.getUpdateStatus() );
        }
        if(implementPlanEntity.getCurrentStatre() != null){
            colSb.append(", CURRENTSTATRE");
            valSb.append(", " + implementPlanEntity.getCurrentStatre() );
        }
        if(implementPlanEntity.getCreatedate() != null){
            colSb.append(", STATUS");
            valSb.append(", " + implementPlanEntity.getStatus() );
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getZsname())){
            colSb.append(", ZSNAME");
            valSb.append(", '" + implementPlanEntity.getZsname() + "'");
        }
        if(implementPlanEntity.getProjecttempId() != null){
            colSb.append(", PROJECTTEMP_ID");
            valSb.append(", " + implementPlanEntity.getProjecttempId() );
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getProjecttempName())){
            colSb.append(", PROJECTTEMP_NAME");
            valSb.append(", '" + implementPlanEntity.getProjecttempName() + "'");
        }

        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString1())){
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '"+implementPlanEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString2())){
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '"+implementPlanEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString3())){
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '"+implementPlanEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString4())){
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '"+implementPlanEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString5())){
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '"+implementPlanEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString6())){
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '"+implementPlanEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString7())){
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '"+implementPlanEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString8())){
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '"+implementPlanEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString9())){
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '"+implementPlanEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedString10())){
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '"+implementPlanEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent1())){
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '"+implementPlanEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent2())){
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '"+implementPlanEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent3())){
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '"+implementPlanEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent4())){
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '"+implementPlanEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent5())){
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '"+implementPlanEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent6())){
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '"+implementPlanEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent7())){
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '"+implementPlanEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent8())){
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '"+implementPlanEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent9())){
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '"+implementPlanEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedContent10())){
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '"+implementPlanEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple1())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple2())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple3())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple4())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownMultiple5())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice1())){
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '"+implementPlanEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice2())){
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '"+implementPlanEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice3())){
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '"+implementPlanEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice4())){
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '"+implementPlanEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedMultipleChoice5())){
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '"+implementPlanEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(implementPlanEntity.getReservedYearTime1() != null){
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(implementPlanEntity.getReservedYearTime2() != null){
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(implementPlanEntity.getReservedYearTime3() != null){
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(implementPlanEntity.getReservedYearTime4() != null){
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(implementPlanEntity.getReservedYearTime5() != null){
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(implementPlanEntity.getReservedYearAccurateTime1() != null){
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(implementPlanEntity.getReservedYearAccurateTime2() != null){
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(implementPlanEntity.getReservedYearAccurateTime3() != null){
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(implementPlanEntity.getReservedYearAccurateTime4() != null){
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(implementPlanEntity.getReservedYearAccurateTime5() != null){
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(implementPlanEntity.getReservedTime1() != null){
            colSb.append(", RESERVEDTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(implementPlanEntity.getReservedTime2() != null){
            colSb.append(", RESERVEDTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(implementPlanEntity.getReservedTime3() != null){
            colSb.append(", RESERVEDTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(implementPlanEntity.getReservedTime4() != null){
            colSb.append(", RESERVEDTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(implementPlanEntity.getReservedTime5() != null){
            colSb.append(", RESERVEDTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(implementPlanEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice1())){
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '"+implementPlanEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice2())){
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '"+implementPlanEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice3())){
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '"+implementPlanEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice4())){
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '"+implementPlanEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedSingleChoice5())){
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '"+implementPlanEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice1())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice2())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice3())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice4())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getReservedDropdownSingleChoice5())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '"+implementPlanEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(implementPlanEntity.getReservedNum1() != null){
            colSb.append(", RESERVEDNUM1");
            valSb.append(", "+implementPlanEntity.getReservedNum1());
        }
        if(implementPlanEntity.getReservedNum2() != null){
            colSb.append(", RESERVEDNUM2");
            valSb.append(", "+implementPlanEntity.getReservedNum2());
        }
        if(implementPlanEntity.getReservedNum3() != null){
            colSb.append(", RESERVEDNUM3");
            valSb.append(", "+implementPlanEntity.getReservedNum3());
        }
        if(implementPlanEntity.getReservedNum4() != null){
            colSb.append(", RESERVEDNUM4");
            valSb.append(", "+implementPlanEntity.getReservedNum4());
        }
        if(implementPlanEntity.getReservedNum5() != null){
            colSb.append(", RESERVEDNUM5");
            valSb.append(", "+implementPlanEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(implementPlanEntity.getStaffid1() != null){
            colSb.append(", STAFFID1");
            valSb.append(", "+implementPlanEntity.getStaffid1());
        }
        if(implementPlanEntity.getStaffid2() != null){
            colSb.append(", STAFFID2");
            valSb.append(", "+implementPlanEntity.getStaffid2());
        }
        if(implementPlanEntity.getStaffid3() != null){
            colSb.append(", STAFFID3");
            valSb.append(", "+implementPlanEntity.getStaffid3());
        }
        if(implementPlanEntity.getStaffid4() != null){
            colSb.append(", STAFFID4");
            valSb.append(", "+implementPlanEntity.getStaffid4());
        }
        if(implementPlanEntity.getStaffid5() != null){
            colSb.append(", STAFFID5");
            valSb.append(", "+implementPlanEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids1())){
            colSb.append(", STAFFIDS1");
            valSb.append(", '"+implementPlanEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids2())){
            colSb.append(", STAFFIDS2");
            valSb.append(", '"+implementPlanEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids3())){
            colSb.append(", STAFFIDS3");
            valSb.append(", '"+implementPlanEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids4())){
            colSb.append(", STAFFIDS4");
            valSb.append(", '"+implementPlanEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getStaffids5())){
            colSb.append(", STAFFIDS5");
            valSb.append(", '"+implementPlanEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(implementPlanEntity.getOrgid1() != null){
            colSb.append(", ORGID1");
            valSb.append(", "+implementPlanEntity.getOrgid1());
        }
        if(implementPlanEntity.getOrgid2() != null){
            colSb.append(", ORGID2");
            valSb.append(", "+implementPlanEntity.getOrgid2());
        }
        if(implementPlanEntity.getOrgid3() != null){
            colSb.append(", ORGID3");
            valSb.append(", "+implementPlanEntity.getOrgid3());
        }
        if(implementPlanEntity.getOrgid4() != null){
            colSb.append(", ORGID4");
            valSb.append(", "+implementPlanEntity.getOrgid4());
        }
        if(implementPlanEntity.getOrgid5() != null){
            colSb.append(", ORGID5");
            valSb.append(", "+implementPlanEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids1())){
            colSb.append(", ORGIDS1");
            valSb.append(", '"+implementPlanEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids2())){
            colSb.append(", ORGIDS2");
            valSb.append(", '"+implementPlanEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids3())){
            colSb.append(", ORGIDS3");
            valSb.append(", '"+implementPlanEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids4())){
            colSb.append(", ORGIDS4");
            valSb.append(", '"+implementPlanEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(implementPlanEntity.getOrgids5())){
            colSb.append(", ORGIDS5");
            valSb.append(", '"+implementPlanEntity.getOrgids5()+"'");
        }

        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        System.out.println(valSb.toString());
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_IMPLEMENT_PLAN WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String insertAttachments(BigDecimal id, String attachmentId){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO TBL_YQNS_IMPLEMENT_PLAN_ATT(IM_PLAN_ID,ATTID) VALUES ("+ id + ","+ attachmentId +")");
        return sb.toString();
    }

    public String deleteAttachmentByIds(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_IMPLEMENT_PLAN_ATT WHERE IM_PLAN_ID IN ("+ids+")");
        return sb.toString();
    }

    public String deleteImplementPlanTeamByIds(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_IMPLEMENT_PLAN_TEAM WHERE IM_PLAN_ID IN ("+ids+")");
        return sb.toString();
    }

    public String insertImplementPlanTeamWidthId(BigDecimal id, ImplementPlanTeamEntity team){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_IMPLEMENT_PLAN_TEAM (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(team.getTeamName())){
            colSb.append(", TEAM_NAME");
            valSb.append(", '" + team.getTeamName() + "'");
        }

        if(id != null){
            colSb.append(", IM_PLAN_ID");
            valSb.append(", '" + id + "'");
        }
        
        if(StringUtil.isNotEmpty(team.getFzzname())){
            colSb.append(", FZZNAME");
            valSb.append(", '" + team.getFzzname() + "'");
        }

        if(team.getFzzstaffid() != null){
            colSb.append(", FZZSTAFFID");
            valSb.append(", '" + team.getFzzstaffid() + "'");
        }

        if(team.getTeamLeaderId() != null){
            colSb.append(", TEAM_LEADER_ID");
            valSb.append(", '" + team.getTeamLeaderId() + "'");
        }

        if(StringUtil.isNotEmpty(team.getTeamMembersIds())){
            colSb.append(", TEAM_MEMBERS_IDS");
            valSb.append(", '" + team.getTeamMembersIds() + "'");
        }

        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }
    
    
    public String updateImplementPlanTeamWidthId(BigDecimal id, ImplementPlanTeamEntity team){
        StringBuffer colSb = new StringBuffer();
        colSb.append(" UPDATE  TBL_YQNS_IMPLEMENT_PLAN_TEAM SET TEAM_NAME= '"+team.getTeamName()+"'");


        if(StringUtil.isNotEmpty(team.getFzzname())){
            colSb.append(", FZZNAME="+team.getFzzname());
        }

        if(team.getFzzstaffid() != null){
            colSb.append(", FZZSTAFFID="+team.getFzzstaffid());
        }

        if(team.getTeamLeaderId() != null){
            colSb.append(", TEAM_LEADER_ID="+team.getTeamLeaderId());
        }

        if(StringUtil.isNotEmpty(team.getTeamMembersIds())){
            colSb.append(", TEAM_MEMBERS_IDS="+team.getTeamMembersIds());
        }

        colSb.append(" where ID= "+id);

        return colSb.toString();
    }
    

    public String selectTblStaffByIds(String ids){
        return "SELECT * FROM TBL_STAFF WHERE STAFFID IN ("+ids+")";
    }
    
    
    public String daList(PageInfo<ImplementPlanEntity> pageInfo, String projectName, String qdcode, BigDecimal staffid){
        StringBuffer sb = new StringBuffer();
        	sb.append("SELECT * FROM TBL_YQNS_IMPLEMENT_PLAN TNA WHERE 1=1 and (STATUS=4 or STATUS=5)");

        if(StringUtil.isNotEmpty(projectName)){
            sb.append(" AND TNA.PROJECT_NAME LIKE '%"+projectName+"%'");
        }
        if(StringUtil.isNotEmpty(qdcode)){
            sb.append(" AND TNA.QDCODE LIKE '%"+qdcode+"%'");
        }
        if(null!=staffid) {
        	sb.append(" and (TNA.PROJECT_ORDER_ID="+staffid+" OR TNA.ID IN (SELECT PROJECTID FROM TBL_NBSJ_BORROWRECORD WHERE STATUS =6 AND RETURNDATE >=SYSDATE AND STAFFID="+staffid+"))");
        }
        
        
        sb.append(" order by ID desc ");
        return sb.toString();

    }
    
    public String jyrzList(PageInfo<ImplementPlanEntity> pageInfo, String projectName, String qdcode, BigDecimal staffid,Integer isadmin){
        StringBuffer sb = new StringBuffer();
        	sb.append("SELECT TNA.*,TC.PCOUNT PCNT FROM TBL_YQNS_IMPLEMENT_PLAN TNA "
        			+ " LEFT JOIN (SELECT PROJECTID,COUNT(PROJECTID) PCOUNT FROM TBL_NBSJ_BORROWRECORD TB WHERE 1=1 ");
        	
        	if(isadmin==0) {
        		sb.append(" AND STAFFID = "+staffid);
        	}
        	
        	sb.append(" GROUP BY PROJECTID) TC ON TNA.ID=TC.PROJECTID "
        			+ " WHERE 1=1 and STATUS=4 ");
        if(StringUtil.isNotEmpty(projectName)){
            sb.append(" AND TNA.PROJECT_NAME LIKE '%"+projectName+"%'");
        }
        if(StringUtil.isNotEmpty(qdcode)){
            sb.append(" AND TNA.QDCODE LIKE '%"+qdcode+"%'");
        }
//        if(isadmin==0) {
//        	sb.append(" AND TNA.ID IN (SELECT PROJECTID FROM TBL_NBSJ_BORROWRECORD WHERE STATUS =6 AND RETURNDATE >=SYSDATE AND STAFFID="+staffid+")");
//        }
//        if(null!=staffid) {
//        	sb.append(" and (TNA.PROJECT_ORDER_ID="+staffid+" OR TNA.ID IN (SELECT PROJECTID FROM TBL_NBSJ_BORROWRECORD WHERE STATUS =6 AND RETURNDATE >=SYSDATE AND STAFFID="+staffid+"))");
//        }
        
        sb.append(" order by ID desc ");
        return sb.toString();
    }
    
    public String jyrzxqList(String id,Integer isadmin,BigDecimal staffid){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT TNB.*,TS.REALNAME FROM TBL_NBSJ_BORROWRECORD TNB "
        		+ " LEFT JOIN TBL_STAFF TS ON TNB.STAFFID=TS.STAFFID "
        		+ " WHERE TNB.PROJECTID = "+id);
        if(isadmin==0) {
        	sb.append(" AND TNB.staffid= "+staffid);
        }
        
        sb.append(" order by BORROWID desc ");
        return sb.toString();
    }
    
    
    
    public String selectBytjEntity( Integer queryYear){
	       StringBuffer sb = new StringBuffer();
	       	sb.append("SELECT pl.*,(SELECT sum(MONEY) from TBL_YQNS_ISSUE_LIST WHERE pl.ID=PROJECTID ) wtje  FROM TBL_YQNS_IMPLEMENT_PLAN pl  WHERE 1=1  and  SPZT=6   ");

	        if(queryYear != null){
	           sb.append("AND PLAN_YEAR= '"+queryYear+"'");
	       }
	       sb.append(" order by ID desc ");
	       return sb.toString();

	   }
    
}
