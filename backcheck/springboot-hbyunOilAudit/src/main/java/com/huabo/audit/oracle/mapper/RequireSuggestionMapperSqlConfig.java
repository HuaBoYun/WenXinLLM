package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.RequireSuggestionEntity;
import com.hbfk.util.StringUtil;
import com.hbfk.util.database.DataBaseSqlConfig;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.cert.ocsp.Req;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName RequireSuggestionMapperSqlConfig
 * @Description
 * @DATE 2023/9/7
 */
public class RequireSuggestionMapperSqlConfig {

    public String selectByEntity( RequireSuggestionEntity requireSuggestionEntity) throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT RS.*,TS.STAFFID,TS.REALNAME,ORG.ORGID,ORG.ORGNAME FROM TBL_YQNS_REQUIRE_SUGGESTION RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID LEFT JOIN TBL_ORGANIZATION ORG ON RS.UNIT_ID = ORG.ORGID WHERE 1=1 ");
        
        sb.append(" AND ( RS.CREATE_USER = ").append(requireSuggestionEntity.getCurrentStaffId()).append(" OR ")
        	.append(DataBaseSqlConfig.getWhereColumnInStr("PERSON_IDS", requireSuggestionEntity.getCurrentStaffId().toString(), ","));
        if(StringUtils.isNotBlank(requireSuggestionEntity.getQueryDeptIds())) {
        	sb.append(" OR RS.CREATE_USER IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(requireSuggestionEntity.getQueryDeptIds()).append("))");
        }
        sb.append(" ) ");
        
        if(requireSuggestionEntity.getId() != null){
            sb.append(" AND RS.ID = "+ requireSuggestionEntity.getId());
        }
        if(StringUtil.isNotEmpty(requireSuggestionEntity.getIds())){
            sb.append(" AND RS.ID in ("+ requireSuggestionEntity.getIds()).append(" )");;
        }
        if(requireSuggestionEntity.getSuggestionNo() != null) {
        	sb.append(" AND RS.SUGGESTIONNO LIKE '%").append(requireSuggestionEntity.getSuggestionNo()).append("%'");
        }
        if(StringUtils.isNotBlank(requireSuggestionEntity.getProjectType())) {
        	sb.append(" AND RS.PROJECT_TYPE LIKE '%").append(requireSuggestionEntity.getProjectType()).append("%'");
        }
        if(requireSuggestionEntity.getQueryYear() != null){
            sb.append(" AND RS.CREATE_TIME LIKE '"+ requireSuggestionEntity.getQueryYear()+"%'");
        }
        if(StringUtil.isNotEmpty(requireSuggestionEntity.getConcerns())){
            sb.append(" AND RS.CONCERNS LIKE '%"+requireSuggestionEntity.getConcerns()+"%'");
        }
        if(requireSuggestionEntity.getDraftId() != null){
            sb.append(" AND RS.DRAFT_Id = "+ requireSuggestionEntity.getDraftId());
        }
        sb.append(" ORDER BY RS.CREATE_TIME DESC");
        System.out.println("findListSql:"+ sb.toString());
        return sb.toString();
    }

    public String selectCountByEntity(RequireSuggestionEntity requireSuggestionEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_REQUIRE_SUGGESTION RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        if(requireSuggestionEntity.getId() != null){
            sb.append("AND RS.ID = "+ requireSuggestionEntity.getId());
        }
        if(StringUtil.isNotEmpty(requireSuggestionEntity.getConcerns())){
            sb.append("AND RS.CONCERNS LIKE '%"+requireSuggestionEntity.getConcerns()+"%'");
        }
        if(requireSuggestionEntity.getDraftId() != null){
            sb.append("AND RS.DRAFT_Id = "+ requireSuggestionEntity.getDraftId());
        }
        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(RequireSuggestionEntity requireSuggestionEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_REQUIRE_SUGGESTION SET ");
        sb.append("CONCERNS = '"+requireSuggestionEntity.getConcerns()+"'");
        if(StringUtil.isNotEmpty(requireSuggestionEntity.getConcernsContent())){
            sb.append(", CONCERNS_CONTENT = '"+requireSuggestionEntity.getConcernsContent()+"'");
        }

        if(requireSuggestionEntity.getSuggestionNo() != null){
            sb.append(", SUGGESTIONNO = '"+requireSuggestionEntity.getSuggestionNo()+"'");
        }
        
        if(StringUtil.isNotEmpty(requireSuggestionEntity.getOrganizationId())){
            sb.append(", UNIT_ID = '"+requireSuggestionEntity.getOrganizationId()+"'");
        }

        if(requireSuggestionEntity.getProjectType() != null && !"".equals(requireSuggestionEntity.getProjectType())){
            sb.append(", PROJECT_TYPE = '"+requireSuggestionEntity.getProjectType()+"'");
        }

        if(requireSuggestionEntity.getDraftId() != null && !"".equals(requireSuggestionEntity.getDraftId())){
            sb.append(", DRAFT_ID = '"+requireSuggestionEntity.getDraftId()+"'");
        }

        if(StringUtil.isNotEmpty(requireSuggestionEntity.getRemark())){
            sb.append(", REMARK = '"+requireSuggestionEntity.getRemark()+"'");
        }

        if(StringUtil.isNotEmpty(requireSuggestionEntity.getPersonIds())){
            sb.append(", PERSON_IDS = '"+requireSuggestionEntity.getPersonIds()+"'");
        }

        sb.append(" WHERE ID = '"+requireSuggestionEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(RequireSuggestionEntity requireSuggestionEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_REQUIRE_SUGGESTION (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (").append(requireSuggestionEntity.getId());

        if(StringUtil.isNotEmpty(requireSuggestionEntity.getConcerns())){
            colSb.append(", CONCERNS");
            valSb.append(", '"+ requireSuggestionEntity.getConcerns()+"'");
        }

        if(StringUtil.isNotEmpty(requireSuggestionEntity.getConcernsContent())){
            colSb.append(", CONCERNS_CONTENT");
            valSb.append(", '"+ requireSuggestionEntity.getConcernsContent()+"'");
        }

        if(StringUtil.isNotEmpty(requireSuggestionEntity.getOrganizationId())){
            colSb.append(", UNIT_ID");
            valSb.append(", '"+ requireSuggestionEntity.getOrganizationId()+"'");
        }
        
        if(requireSuggestionEntity.getSuggestionNo() != null){
            colSb.append(", SUGGESTIONNO");
            valSb.append(", '"+ requireSuggestionEntity.getSuggestionNo()+"'");
        }

        if(requireSuggestionEntity.getProjectType() != null && !"".equals(requireSuggestionEntity.getProjectType())){
            colSb.append(", PROJECT_TYPE");
            valSb.append(", '"+ requireSuggestionEntity.getProjectType()+"'");
        }

        if(requireSuggestionEntity.getDraftId() != null && !"".equals(requireSuggestionEntity.getDraftId())){
            colSb.append(", DRAFT_ID");
            valSb.append(", '"+ requireSuggestionEntity.getDraftId()+"'");
        }
        
        if(StringUtil.isNotEmpty(requireSuggestionEntity.getRemark())) {
            colSb.append(", REMARK");
            valSb.append(", '" + requireSuggestionEntity.getRemark() + "'");
        }

        if(requireSuggestionEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + requireSuggestionEntity.getCreateUser().getStaffid() + "'");
        }

        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_REQUIRE_SUGGESTION WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
