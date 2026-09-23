package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_JHGL_JHCHUG_MX(计划初稿明细)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhchugMx
 */
public class TblYqnsJhglJhGLMapperSqlConfig {

    public String findListByAnalysis(Integer xmnd, String glType, String projectName){
        StringBuffer sb = new StringBuffer("SELECT * FROM TBL_YQNS_JHGL_JH_GL WHERE JHID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+xmnd+") AND GLTYPE = '").append(glType).append("'");
        
        sb.append(" AND JHID NOT IN (SELECT GLJHXMID FROM TBL_YQNS_XMQD WHERE PLANID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+xmnd+") AND GLJHXMLX = '"+glType+"')");

        if(StringUtils.isNotBlank(projectName)) {
        	sb.append(" AND PROJECTNAME LIKE '%").append(projectName).append("%'");
        }
        
        sb.append(" ORDER BY JHID ASC");
        return sb.toString();
    }



}




