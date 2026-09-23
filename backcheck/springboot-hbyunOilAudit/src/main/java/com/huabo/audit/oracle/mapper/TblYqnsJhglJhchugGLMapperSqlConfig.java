package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcg;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhchug;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCHUG(计划管理计划初稿)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhchug
 */
public class TblYqnsJhglJhchugGLMapperSqlConfig {

	public String selecthzsjListByJhchugId(String jhchugid, String relaid, String glType) throws Exception{
		String sql = "SELECT * FROM TBL_YQNS_JHCGGL_RELA WHERE GLTYPE = '"+glType+"' AND JHCHUGID = "+jhchugid+" AND ID NOT IN (SELECT RELAID FROM TBL_YQNS_JHGL_JH_GL WHERE RELAID IS NOT NULL) ";
		if(StringUtils.isNotBlank(relaid)) {
			sql += " AND ID NOT IN ("+relaid+")";
		}
		return sql;
	}
}




