package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhGL;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhcg
 */
public interface TblYqnsJhglJhGLMapper extends BaseMapper<TblYqnsJhglJhGL> {

	@Select("INSERT INTO TBL_YQNS_JHZGGL_RELA(GLID,JHID,FORMID) SELECT #{id},#{jhid},FORMID FROM TBL_YQNS_JHCHUGGL_RELA WHERE GLID = #{relaId} ")
	void insertHzjhSjbGl(@Param("id") BigDecimal id,@Param("jhid")  long jhid,@Param("relaId")  BigDecimal relaId);

	@Delete("DELETE FROM TBL_YQNS_JHZGGL_RELA WHERE GLID = #{id} ")
	void deleteGlRela(@Param("id")String id);

	@SelectProvider(type = TblYqnsJhglJhGLMapperSqlConfig.class , method = "findListByAnalysis")
	List<TblYqnsJhglJhGL> findListByAnalysis(Integer xmnd, String glType, String projectName);
	
	@Select("INSERT INTO TBL_YQNS_JHCGGL_RELA(GLID,CGID,FORMID) SELECT #{id},#{Jhid},ZJB.GCXMZJZJBID FROM TBL_YQNS_GCXMZJ_ZJB ZJB LEFT JOIN TBL_YQNS_GCXMZJ GC ON ZJB.GCXMZJID = GC.GCXMZJID  WHERE ZJB.NWB != '工程建设公司' and ZJB.NWB != '工程建设' AND "
			+ " ZJB.GCXMZJZJBID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '31')) AND GC.JSDW = #{relaOrgName}")
	void insertGcxmjshzGl(@Param("id") BigDecimal id,@Param("Jhid") long Jhid,@Param("relaOrgName") String relaOrgName) throws Exception;
	
	@Select("INSERT INTO TBL_YQNS_JHCGGL_RELA(GLID,CGID,FORMID) SELECT ${id},${Jhid},ID FROM TBL_YQNS_LEAVE_AUDIT_3L WHERE ID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '23')) AND ID IN (select NRID from TBL_YQNS_LEAVE_AUDIT_JD3L_GL where JDID IN ( SELECT JDID FROM TBL_YQNS_LEAVE_AUDIT_JD3L WHERE STATUS = 6 )) AND OLD_ORG_ID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID = ${relaOrgId})")
	void insertSjdwlrsjSbGl(@Param("id")BigDecimal id,@Param("Jhid") long Jhid,@Param("relaOrgId") BigDecimal relaOrgId) throws Exception;
	

	@Select("INSERT INTO TBL_YQNS_JHCGGL_RELA(GLID,CGID,FORMID) SELECT #{id},#{Jhid},JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQK " + 
			" WHERE JSXMTZWCQKID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '32')) AND JSXMTZWCQKID IN  (SELECT JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQKZJB WHERE HZID IN  (SELECT HZID FROM TBL_YQNS_JSXM_TZWCQKHZ WHERE status = 6 ) ) AND TBDW_NAME = #{relaOrgName}")
	void insertJsxmtzGl(@Param("id")BigDecimal id,@Param("Jhid") long Jhid,@Param("relaOrgName") String relaOrgName) throws Exception;

	@Delete("DELETE FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = #{id} ")
	void deleteGlcgRela(@Param("id")String id);
}




