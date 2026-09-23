package com.huabo.system.mappersql;

import com.huabo.system.controller.TblOrganizationController;
import org.apache.commons.lang.StringUtils;
import java.math.BigDecimal;
import com.huabo.system.vo.param.TblSystemProjectQueryParam;
import org.springframework.beans.factory.annotation.Autowired;

public class TblSystemProjectOracleMapperSqlConfig {

	@Autowired
	private TblOrganizationController tblOrganizationController;
	
	public String getList(TblSystemProjectQueryParam project) {
		System.out.println("project = "+project);
		StringBuffer sb = new StringBuffer();
		
		if(project.getBelongGroup() != null) {
			sb.append("select distinct a.* from tbl_system_project a left join tbl_system_project_auth b on a.id = b.projectid where (b.authorgid = ").append(project.getBelongGroup()).append(" and b.STATE = 1 or a.BELONGGROUP = ").append(project.getBelongGroup()).append(") ");
			if(project.getRoleIdStrs() != null && project.getRoleIdStrs().length()>0) {
				 sb.append(" AND ( a.UNIQUEIDENTIFICATION in (" +  
				 		" SELECT DISTINCT TMR.MODULETYPE from TBL_SYSTEM_RIGHT TMR" + 
				 		" LEFT JOIN TBL_SYSTEM_ROLE_RIGHT TMUR ON TMR.ID = TMUR.RIGHTID" + 
				 		" AND TMUR.ROLEID IN (SELECT ROLEID FROM TBL_ORG_ROLE WHERE  ROLEID IN ("+project.getRoleIdStrs()+")) " +  
				 		" AND TMR.TYPE=0  )");
				 
				 sb.append(" or a.UNIQUEIDENTIFICATION in ('zcgl','ywzt'))");
			}
		}else {
			sb.append("select distinct a.* from tbl_system_project a left join tbl_system_project_auth b on a.id = b.projectid where 1=1 ");
		}
		
//		if (tblOrganizationController.OID!=null){
//			sb.append(" and b.AUTHORGID = ").append(tblOrganizationController.OID);
//		}
		if(StringUtils.isNotBlank(project.getUniqueIdentification())) {
			sb.append(" and a.uniqueIdentification = '").append(project.getUniqueIdentification()).append("'");
		}
		
		if(StringUtils.isNotBlank(project.getProjectName())) {
			sb.append(" and a.projectname LIKE '%").append(project.getProjectName()).append("%'");
		}
		// if (project.getBelongGroup()!=null && project.getBelongGroup().compareTo(BigDecimal.ZERO)>0) {
 		// 	sb.append(" and b.belonggroup = ").append(project.getBelongGroup());
		// }
		sb.append(" order by a.sort asc,a.id desc");
		String sql = sb.toString();
		return sql;
	}
}
