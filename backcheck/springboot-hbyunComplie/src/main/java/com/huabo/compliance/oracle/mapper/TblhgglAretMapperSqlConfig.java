package com.huabo.compliance.oracle.mapper;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.oracle.entity.TblhgglAret;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:19:41
 */
public class TblhgglAretMapperSqlConfig {
    public String getwgzzContListaret(PageInfo<TblhgglAret> pageInfo, String business) {
        StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_HGGL_ARET TNA WHERE 1=1 ");

        if(business != null){
            sb.append("AND BUSINESS"+business);
        }
        return sb.toString();
    }
    
    
    
    
    public String updateEntity(TblhgglAret plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_HGGL_ARET SET ID = '"+plan.getId()+"'");
		if(plan.getNumberno() != null && !"".equals(plan.getNumberno())) {
			sqlSb.append(" ,NUMBERNO = '"+plan.getNumberno()+"'");
		}
		if(plan.getIsuue() != null && !"".equals(plan.getIsuue())) {
			sqlSb.append(" ,ISUUE = '"+plan.getIsuue()+"'");
		}
		
		if(plan.getType() != null && !"".equals(plan.getType())) {
			sqlSb.append(" ,TYPE = '"+plan.getType()+"'");
		}
		if(plan.getBusiness() != null && !"".equals(plan.getBusiness())) {
			sqlSb.append(" ,BUSINESS = '"+plan.getBusiness()+"'");
		}
		if(plan.getRectification() != null && !"".equals(plan.getRectification())) {
			sqlSb.append(" ,RECTIFICATION = '"+plan.getRectification()+"'");
		}
		if(plan.getRectificationtime() != null && !"".equals(plan.getRectificationtime())) {
			sqlSb.append(" ,RECTIFICATIONTIME = TO_DATE('"+DateUtil.parseDate(plan.getRectificationtime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
		}
		if(plan.getFileids() != null && !"".equals(plan.getFileids())) {
			sqlSb.append(" ,FILEIDS = '"+plan.getFileids()+"'");
		}
		
		sqlSb.append(" WHERE LEAVEID = "+plan.getId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblhgglAret plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_HGGL_ARET(ID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval ");
		
		if(plan.getNumberno() != null && !"".equals(plan.getNumberno())) {
			colSb.append(",NUMBERNO");
			valSb.append(",'"+plan.getNumberno()+"'");
		}
		
		if(plan.getIsuue() != null && !"".equals(plan.getIsuue())) {
			colSb.append(",ISUUE");
			valSb.append(",'"+plan.getIsuue()+"'");
		}
		
		if(plan.getType() != null && !"".equals(plan.getType())) {
			colSb.append(",TYPE");
			valSb.append(",'"+plan.getType()+"'");
		}
		
		if(plan.getBusiness() != null) {
			colSb.append(",BUSINESS");
			valSb.append(",'"+plan.getBusiness()+"'");
		}
		
		if(plan.getRectification() != null) {
			colSb.append(",RECTIFICATION");
			valSb.append(",'"+plan.getRectification()+"'");
		}

		if(plan.getFileids() != null) {
			colSb.append(",FILEIDS");
			valSb.append(",'"+plan.getFileids()+"'");
		}
		
		if(plan.getRectificationtime() != null) {
			colSb.append(",RECTIFICATIONTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getRectificationtime(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
