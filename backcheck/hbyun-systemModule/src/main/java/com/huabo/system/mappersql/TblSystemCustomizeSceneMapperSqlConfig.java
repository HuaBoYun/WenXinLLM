package com.huabo.system.mappersql;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblSystemCustomizeScene;
import com.huabo.system.vo.param.TblSystemCustomizeSceneQueryParam;

public class TblSystemCustomizeSceneMapperSqlConfig {

	public String getList(IPage<TblSystemCustomizeScene> page,TblSystemCustomizeSceneQueryParam param) throws Exception{
		StringBuffer sb = new StringBuffer("select a.*,b.NAME as parentCatalogueName,c.NAME as catalogueName from TBL_SYSTEM_CUSTOMIZE_SCENE a eft join Tbl_System_Right b on a.PARENTCATALOGUEID=b.ID ")
				.append("left join Tbl_System_Right c on a.CATALOGUEID=c.ID where 1=1 ");
		
		if(StringUtils.isNotBlank(param.getModuleType())) {
			sb.append(" AND a.moduleType = '").append(param.getModuleType()).append("' ");
		}
		
		if(StringUtils.isNotBlank(param.getSceneCode())) {
			sb.append(" AND a.sceneCode = '").append(param.getSceneCode()).append("' ");
		}
		
		if(param.getCatalogueName() != null) {
			sb.append(" AND b.NAME LIKE '%").append(param.getCatalogueName()).append("%' ");
		}
		
		if(param.getParentCatalogueName() != null) {
			sb.append(" AND c.NAME LIKE '%").append(param.getParentCatalogueName()).append("%' ");
		}
		sb.append(" ORDER BY CREATEDTIME ASC ");
		String sql = sb.toString();
		return sql;
	}
	
}
