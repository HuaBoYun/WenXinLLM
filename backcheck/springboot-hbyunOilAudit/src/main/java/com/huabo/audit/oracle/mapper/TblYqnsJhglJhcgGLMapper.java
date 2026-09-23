package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgGL;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划草稿)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhcg
 */
public interface TblYqnsJhglJhcgGLMapper extends BaseMapper<TblYqnsJhglJhcgGL> {

	@SelectProvider(method = "selectGcxmjshzPage", type = TblYqnsJhglJhcgGLMapperSqlConfig.class)
	List<TblYqnsJhglJhcgGL> selectGcxmjshzPage() throws Exception;

	@SelectProvider(method = "selectJsxmtzPage", type = TblYqnsJhglJhcgGLMapperSqlConfig.class)
	List<TblYqnsJhglJhcgGL> selectJsxmtzPage() throws Exception;

	@SelectProvider(method = "selectSjdwlrsjSbList", type = TblYqnsJhglJhcgGLMapperSqlConfig.class)
	List<TblYqnsJhglJhcgGL> selectSjdwlrsjSbList();

	@Select("INSERT INTO TBL_YQNS_JHCGGL_RELA(GLID,CGID,FORMID) SELECT #{id},#{jhcgid},ZJB.GCXMZJZJBID FROM TBL_YQNS_GCXMZJ_ZJB ZJB LEFT JOIN TBL_YQNS_GCXMZJ GC ON ZJB.GCXMZJID = GC.GCXMZJID  WHERE ZJB.NWB != '工程建设公司' AND "
			+ " ZJB.GCXMZJZJBID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '31')) AND GC.JSDW = #{relaOrgName}")
	void insertGcxmjshzGl(@Param("id") BigDecimal id,@Param("jhcgid") long jhcgid,@Param("relaOrgName") String relaOrgName) throws Exception;

	@Select("INSERT INTO TBL_YQNS_JHCGGL_RELA(GLID,CGID,FORMID) SELECT ${id},${jhcgid},ID FROM TBL_YQNS_LEAVE_AUDIT_3L WHERE ID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '23')) AND ID IN (select NRID from TBL_YQNS_LEAVE_AUDIT_JD3L_GL where JDID IN ( SELECT JDID FROM TBL_YQNS_LEAVE_AUDIT_JD3L WHERE STATUS = 6 )) AND OLD_ORG_ID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID = ${relaOrgId})")
	void insertSjdwlrsjSbGl(@Param("id")BigDecimal id,@Param("jhcgid") long jhcgid,@Param("relaOrgId") BigDecimal relaOrgId) throws Exception;

	@Select("INSERT INTO TBL_YQNS_JHCGGL_RELA(GLID,CGID,FORMID) SELECT #{id},#{jhcgid},JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQK " + 
			" WHERE JSXMTZWCQKID NOT IN (SELECT FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '32')) AND JSXMTZWCQKID IN  (SELECT JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQKZJB WHERE HZID IN  (SELECT HZID FROM TBL_YQNS_JSXM_TZWCQKHZ WHERE status = 6 ) ) AND TBDW_NAME = #{relaOrgName}")
	void insertJsxmtzGl(@Param("id")BigDecimal id,@Param("jhcgid") long jhcgid,@Param("relaOrgName") String relaOrgName) throws Exception;

	@Delete("DELETE FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = #{id} ")
	void deleteGlRela(@Param("id")String id) throws Exception;

	@Select("INSERT INTO TBL_YQNS_JHCGGL_RELA(GLID,CGID,FORMID) SELECT #{newGlId},#{jhcgid},FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = #{oldGLId} ")
	void insertGlRealDataAllInfo(@Param("jhcgid")BigDecimal jhcgid,@Param("oldGLId") BigDecimal oldGLId,@Param("newGlId") BigDecimal newGlId);

	@SelectProvider(method = "selectSjdwlrsjSbListByJhcgId", type = TblYqnsJhglJhcgGLMapperSqlConfig.class)
	List<TblYqnsJhglJhcgGL> selectSjdwlrsjSbListByJhcgId(String jhcgid, String relaid, String glType) throws Exception;

	


}




