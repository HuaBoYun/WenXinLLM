package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.audit.oracle.entity.ProjectEvaluationEntity;
import com.huabo.audit.oracle.entity.ProjectEvaluationItemEntity;
import com.huabo.audit.oracle.entity.ProjectProposalEvaluationEntity;
import com.huabo.audit.oracle.entity.QualityEntity;
import com.huabo.audit.oracle.entity.QualityItemEntity;

import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

/**
 * @author zkl
 * @InterfaceName ProjectEvaluationMapperSqlConfig
 * @Description
 * @DATE 2024/04/13
 */
public class ProjectProposalEvaluationMapperSqlConfig {

    public String findListByAnalysis(Integer xmnd, String projectType, String projectName) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT * FROM TBL_YQNS_PROJECT_EVALUATION E LEFT join TBL_STAFF S on E.CREATEUSER=S.STAFFID WHERE E.PROJECTTYPE = '").append(projectType).append("'")
                .append(" AND E.ID IN (SELECT GLID FROM TBL_YQNS_JHGL_JH_GL WHERE JHID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = '").append(xmnd).append("' )  AND GLTYPE IN ('11','12') )")
                .append(" AND E.ID NOT IN (SELECT GLJHXMID FROM TBL_YQNS_XMQD WHERE PLANID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = '").append(xmnd).append("' ) AND GLJHXMLX IN ('11','12') ) ");

        if (StringUtils.isNotBlank(projectName)) {
            sb.append(" AND E.PROJECTNAME LIKE '%").append(projectName).append("%'");
        }

        sb.append(" ORDER BY E.SORTNUMBER DESC ");
        String sql = sb.toString();
        return sb.toString();
    }


    public String selectLxZxsjHzChooseList(ProjectProposalEvaluationEntity projectProposalEvaluationEntity) throws Exception {
        StringBuffer sb = new StringBuffer("SELECT * FROM TBL_YQNS_PROJECT_EVALUATION E LEFT join TBL_STAFF S on E.CREATEUSER=S.STAFFID WHERE (E.ISSHOWLIST IS NULL OR E.ISSHOWLIST != 1) AND E.ID IN (SELECT EVAID FROM TBL_YQNS_NDTB_GL WHERE TBID IN (SELECT TBID FROM TBL_YQNS_NDTB WHERE STATUS = 6))");

        if (StringUtils.isNotBlank(projectProposalEvaluationEntity.getProjectName())) {
            sb.append(" AND E.PROJECTNAME LIKE '%").append(projectProposalEvaluationEntity.getProjectName()).append("%'");
        }

        if (StringUtils.isNotBlank(projectProposalEvaluationEntity.getProjectPurpose())) {
            sb.append(" AND E.PROJECTPURPOSE LIKE '%").append(projectProposalEvaluationEntity.getProjectPurpose()).append("%'");
        }

        if (StringUtils.isNotBlank(projectProposalEvaluationEntity.getProjectType())) {
            sb.append(" AND E.PROJECTTYPE = '").append(projectProposalEvaluationEntity.getProjectType()).append("'");
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        sb.append(" AND TO_CHAR( E.CREATETIME , 'YYYY') = '").append(year).append("'");

        sb.append(" ORDER BY  E.PROJECTTYPE,E.SORTNUMBER DESC ");
        String sql = sb.toString();
        return sql;
    }

    public String selectLxZxsjHzDetailList(ProjectProposalEvaluationEntity projectProposalEvaluationEntity) {
        StringBuffer sb = new StringBuffer("SELECT * FROM TBL_YQNS_PROJECT_EVALUATION E LEFT join TBL_STAFF S on E.CREATEUSER=S.STAFFID WHERE E.ISSHOWLIST = 1 ");

        if (StringUtils.isNotBlank(projectProposalEvaluationEntity.getProjectName())) {
            sb.append(" AND E.PROJECTNAME LIKE '%").append(projectProposalEvaluationEntity.getProjectName()).append("%'");
        }

        if (StringUtils.isNotBlank(projectProposalEvaluationEntity.getProjectPurpose())) {
            sb.append(" AND E.PROJECTPURPOSE LIKE '%").append(projectProposalEvaluationEntity.getProjectPurpose()).append("%'");
        }

        if (StringUtils.isNotBlank(projectProposalEvaluationEntity.getProjectType())) {
            sb.append(" AND E.PROJECTTYPE = '").append(projectProposalEvaluationEntity.getProjectType()).append("'");
        }
        sb.append(" ORDER BY  E.PROJECTTYPE,E.SORTNUMBER DESC ");
        String sql = sb.toString();
        return sql;
    }
}
