package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.QualityResultEntity;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;

/**
 * @author Rui
 * @ClassName QualityMapperSqlConfig
 * @Description
 * @DATE 2023/10/9
 */
public class QualityResultMapperSqlConfig {

    public String selectByEntity( QualityResultEntity qualityResultEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_QUALITY_RESULT RS WHERE 1 = 1");

        if(qualityResultEntity.getProject() != null &&  StringUtil.isNotEmpty(qualityResultEntity.getProject().getProjectName())){
            sb.append("AND RS.PROJECT_ID IN (SELECT PROJECTID FROM TBL_NBSJ_PROJECT WHERE PRJOECTNAME LIKE '%"+qualityResultEntity.getProject().getProjectName()+"%')");
        }

//        sb.append(" ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(QualityResultEntity qualityResultEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_QUALITY_RESULT RS  WHERE 1 = 1 ");

        if(qualityResultEntity.getProject() != null &&  StringUtil.isNotEmpty(qualityResultEntity.getProject().getProjectName())){
            sb.append("AND RS.PROJECT_ID IN (SELECT PROJECTID FROM TBL_NBSJ_PROJECT WHERE PRJOECTNAME LIKE '%"+qualityResultEntity.getProject().getProjectName()+"%')");
        }

        sb.append(")");
        return sb.toString();
    }


    public String updateEntity(QualityResultEntity qualityResultEntity){
//        StringBuffer sb = new StringBuffer();
//        sb.append("UPDATE TBL_YQNS_QUALITY_RESULT SET ");
//        sb.append("PROJECT_ID = '"+qualityResultEntity.getProject().getProjectId()+"'");
//
//        if(StringUtil.isNotEmpty(qualityResultEntity.getResult())){
//            sb.append(", RESULT = '"+qualityResultEntity.getResult()+"'");
//        }
//        if(null != qualityResultEntity.getAssessorId()){
//            sb.append(", ASSESSOR_ID = '"+qualityResultEntity.getAssessorId()+"'");
//        }
//        if(null != qualityResultEntity.getReviewerId()){
//            sb.append(", REVIEWER_ID = " + qualityResultEntity.getReviewerId());
//        }
//        if(null != qualityResultEntity.getScore()){
//            sb.append(", SCORE = " + qualityResultEntity.getScore());
//        }
//        if(StringUtil.isNotEmpty(qualityResultEntity.getRemark())){
//            sb.append(", REMARK = '"+qualityResultEntity.getRemark()+"'");
//        }
//
//        sb.append(" WHERE ID = '"+qualityResultEntity.getId()+"'");
        String sql = new SQL() {
            {
                UPDATE("TBL_YQNS_QUALITY_RESULT");
                SET("PROJECT_ID=#{projectId}", "ASSESSOR_ID=#{assessorId}", "REVIEWER_ID=#{reviewerId}", "RESULT=#{result}", "START_TIME=#{startTime}", "SCORE=#{score}","NO=#{no}");
                WHERE("id=#{id}");
            }
        }.toString();
        return sql;
//        return sb.toString();
    }


    public String insertEntity(QualityResultEntity qualityResultEntity){
//        StringBuffer colSb = new StringBuffer();
//        colSb.append("INSERT INTO TBL_YQNS_QUALITY_RESULT (ID");
//
//        StringBuffer valSb = new StringBuffer();
//        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");
//
//        if(qualityResultEntity.getProject() != null && qualityResultEntity.getProject().getProjectId() != null){
//            colSb.append(", PROJECT_ID");
//            valSb.append(", '" + qualityResultEntity.getProject().getProjectId() + "'");
//        }
//        if(null != qualityResultEntity.getAssessorId()){
//            colSb.append(", ASSESSOR_ID");
//            valSb.append(", " + qualityResultEntity.getAssessorId());
//        }
//        if(null != qualityResultEntity.getReviewerId()){
//            colSb.append(", REVIEWER_ID");
//            valSb.append(", " + qualityResultEntity.getReviewerId());
//        }
//
//        if(StringUtil.isNotEmpty(qualityResultEntity.getResult())){
//            colSb.append(", RESULT");
//            valSb.append(", '" + qualityResultEntity.getResult() + "'");
//        }
//
//        if(null != qualityResultEntity.getStartTime()){
//            colSb.append(", START_TIME");
//            valSb.append(", " + qualityResultEntity.getStartTime());
//        }
//
//        if(StringUtil.isNotEmpty(qualityResultEntity.getRemark())){
//            colSb.append(", REMARK");
//            valSb.append(", '" + qualityResultEntity.getRemark() + "'");
//        }
//
//        colSb.append(")");
//        valSb.append(")");
//
//        colSb.append(valSb);
        return new SQL() {
            {
                INSERT_INTO("TBL_YQNS_QUALITY_RESULT");
                INTO_COLUMNS("ID", "PROJECT_ID", "ASSESSOR_ID", "REVIEWER_ID", "RESULT", "START_TIME", "SCORE","NO");
                INTO_VALUES("HIBERNATE_SEQUENCE.nextval", "#{projectId}", "#{assessorId}", "#{reviewerId}", "#{result}", "#{startTime}", "#{score}","#{no}");
            }
        }.toString();
//        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_QUALITY_RESULT WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
