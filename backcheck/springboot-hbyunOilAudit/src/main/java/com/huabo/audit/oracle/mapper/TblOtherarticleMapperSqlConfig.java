package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblOtherarticle;
import com.huabo.audit.oracle.vo.TblOtherarticleVo;
import com.huabo.audit.util.PageInfo;

import cn.hutool.core.util.StrUtil;

public class TblOtherarticleMapperSqlConfig {
	public String selectOtherarticlePageInfo(PageInfo<TblOtherarticle> pageInfo,TblOtherarticleVo tblOtherarticleVo)throws Exception{
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT * from TBL_OTHERARTICLE  WHERE 1=1 ");
		if(StrUtil.isNotBlank(tblOtherarticleVo.getArticletitle())) {
			sqlSb.append(" AND ARTICLETITLE="+tblOtherarticleVo.getArticletitle());
		}
		if(StrUtil.isNotBlank(tblOtherarticleVo.getAruticleauther())) {
			sqlSb.append(" AND ARUTICLEAUTHER="+tblOtherarticleVo.getAruticleauther());
		}
		if(StrUtil.isNotBlank(tblOtherarticleVo.getOrgid())) {
			sqlSb.append(" AND ORGID="+tblOtherarticleVo.getOrgid());
		}
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	public String selectOtherarticleCountPageInfo(PageInfo<TblOtherarticle> pageInfo,TblOtherarticleVo tblOtherarticleVo){
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) from TBL_OTHERARTICLE  WHERE 1=1 ");
		if(StrUtil.isNotBlank(tblOtherarticleVo.getArticletitle())) {
			sqlSb.append(" AND ARTICLETITLE="+tblOtherarticleVo.getArticletitle());
		}
		if(StrUtil.isNotBlank(tblOtherarticleVo.getAruticleauther())) {
			sqlSb.append(" AND ARUTICLEAUTHER="+tblOtherarticleVo.getAruticleauther());
		}
		if(StrUtil.isNotBlank(tblOtherarticleVo.getOrgid())) {
			sqlSb.append(" AND ORGID="+tblOtherarticleVo.getOrgid());
		}
		return sqlSb.toString();
	}
	public String insertEntity(TblOtherarticle article){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_OTHERARTICLE(OTHARTID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		if(StrUtil.isNotBlank(article.getArticletitle())) {
			colSb.append(",ARTICLETITLE");
			valSb.append(",'"+article.getArticletitle()+"'");
		}
		if(StrUtil.isNotBlank(article.getArticlestatus())) {
			colSb.append(",ARTICLESTATUS");
			valSb.append(",'"+article.getArticlestatus()+"'");
		}
		if(StrUtil.isNotBlank(article.getAruticleauther())) {
			colSb.append(",ARUTICLEAUTHER");
			valSb.append(",'"+article.getAruticleauther()+"'");
		}
		if(StrUtil.isNotBlank(article.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+article.getMemo()+"'");
		}
		if(article.getOrgid()!=null) {
			colSb.append(",ORGID");
			valSb.append(",'"+article.getOrgid()+"'");
		}
		if(StrUtil.isNotBlank(article.getArticlebody())) {
			colSb.append(",ARTICLEBODY");
			valSb.append(",'"+article.getArticlebody()+"'");
		}
		if(StrUtil.isNotBlank(article.getModeltype())) {
			colSb.append(",ARTICLEBODY");
			valSb.append(",'"+article.getModeltype()+"'");
		}
		if(article.getPublishtime()!=null) {
			colSb.append(",PUBLISHTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(article.getPublishtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	public String updateEntity(TblOtherarticle article){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_OTHERARTICLE set");
		if(StrUtil.isNotBlank(article.getArticletitle())) {
			colSb.append(" ARTICLETITLE='"+article.getArticletitle()+"',");
		}
		
		if(StrUtil.isNotBlank(article.getArticlestatus())) {
			colSb.append(" ARTICLESTATUS='"+article.getArticlestatus()+"',");
		}
		
		if(StrUtil.isNotBlank(article.getAruticleauther())) {
			colSb.append(" ARUTICLEAUTHER='"+article.getAruticleauther()+"',");
		}
		if(StrUtil.isNotBlank(article.getMemo())) {
			colSb.append(" MEMO='"+article.getMemo()+"',");
		}
		if(StrUtil.isNotBlank(article.getArticlebody())) {
			colSb.append(" ARTICLEBODY='"+article.getArticlebody()+"',");
		}
		colSb.append(" PUBLISHTIME=TO_DATE('"+DateUtil.parseDate(article.getPublishtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		colSb.append(" where OTHARTID="+article.getOthartid());
		String sql = colSb.toString();
		System.out.println(sql);
		return sql;
	}
}
