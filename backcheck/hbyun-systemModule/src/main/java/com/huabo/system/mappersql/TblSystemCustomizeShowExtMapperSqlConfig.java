package com.huabo.system.mappersql;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.vo.param.CustomizeShowExtPreviewDetailsQueryParam;

public class TblSystemCustomizeShowExtMapperSqlConfig {

	public String getCustomizeShowExtPreview(CustomizeShowExtPreviewDetailsQueryParam param)throws Exception{
		StringBuffer sb = new StringBuffer("CASE WHEN a.STATE == 1 THEN ").append(DataBaseSqlConfig.getNullColumn(DataBaseSqlConfig.getNullColumn("a.CHANGENAME", "a.CHANGENAME"), " a.ORLNAME")).append(" ELSE b.ORLNAME END AS name ,")
				.append("CASE WHEN a.STATE == 1 THEN a.FIELD ELSE b.FIELD END AS FIELD,")
				.append("CASE WHEN a.STATE == 1 THEN a.ISREQUIRED ELSE b.ISREQUIRED END AS ISREQUIRED,")
				.append("CASE WHEN a.STATE == 1 THEN a.ISSYSTEMREQUIRED ELSE b.ISSYSTEMREQUIRED END AS ISSYSTEMREQUIRED,")
				.append("CASE WHEN a.STATE == 1 THEN a.COMPONENTTYPE ELSE b.COMPONENTTYPE END AS COMPONENTTYPE,")
				.append("CASE WHEN a.STATE == 1 THEN a.COMPONENTWIDTH ELSE b.COMPONENTWIDTH END AS COMPONENTWIDTH,")
				.append("CASE WHEN a.STATE == 1 THEN a.GROUPNAME ELSE b.GROUPNAME END AS GROUPNAME,")
				.append("CASE WHEN a.STATE == 1 THEN a.SCENEID ELSE b.SCENEID END AS SCENEID,")
				.append("CASE WHEN a.STATE == 1 THEN a.ATTACHEDFIELD ELSE b.ATTACHEDFIELD END AS ATTACHEDFIELD,")
				.append("CASE WHEN a.STATE == 1 THEN a.EXTJSON ELSE b.EXTJSON END AS EXTJSON,")
				.append("CASE WHEN a.STATE == 1 THEN a.ISEDIT ELSE b.ISEDIT END AS ISEDIT,")
				.append("CASE WHEN a.STATE == 1 THEN a.ISASSOCIATE ELSE b.ISASSOCIATE END AS ISASSOCIATE,")
				.append("CASE WHEN a.STATE == 1 THEN a.ASSOCIATEINFOJSON ELSE b.ASSOCIATEINFOJSON END AS ASSOCIATEINFOJSON,")
				.append("CASE WHEN a.STATE == 1 THEN a.ISDETAILS ELSE b.ISDETAILS END AS ISDETAILS,")
				.append("CASE WHEN a.STATE == 1 THEN a.ISLIST ELSE b.ISLIST END AS ISLIST,")
				.append("a.LISTSORT,a.STATE,a.SORT from TBL_SYSTEM_CUSTOMIZE_SHOW_EXT a inner join TBL_SYSTEM_CUSTOMIZE_SHOW b on a.id = b.SHOWEXTID where 1=1 AND a.STATE = 1 ");
		
		if(param.getSceneId() != null) {
			sb.append(" AND a.SCENEID = ").append(param.getSceneId());
		}
		if(StringUtils.isNotBlank(param.getSceneCode())){
			sb.append(" AND a.sceneCode = '").append(param.getSceneCode()).append("'");
		}
		
		sb.append(" order by a.SORT,a.id desc");
		String sql = sb.toString();
		return sql;
	}
	
	
}
