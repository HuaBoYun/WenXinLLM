package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgGL;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhchugGL;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_JHCG(计划管理计划初稿)】的数据库操作Mapper
 * @Entity TblYqnsJhglJhcg
 */
public interface TblYqnsJhglJhchugGLMapper extends BaseMapper<TblYqnsJhglJhchugGL> {

	@Select("INSERT INTO TBL_YQNS_JHCHUGGL_RELA(GLID,JHCHUGID,FORMID) SELECT #{id},#{jhchugid},FORMID FROM TBL_YQNS_JHCGGL_RELA WHERE GLID = #{relaId} ")
	void insertSjdwlrsjSbGl(@Param("id") BigDecimal id,@Param("jhchugid") long jhchugid,@Param("relaId") BigDecimal relaId) throws Exception;

	@Delete("DELETE FROM TBL_YQNS_JHCHUGGL_RELA WHERE GLID = #{id} ")
	void deleteGlRela(@Param("id")String id) throws Exception; 

	@SelectProvider(method = "selecthzsjListByJhchugId", type = TblYqnsJhglJhchugGLMapperSqlConfig.class) 
	List<TblYqnsJhglJhchugGL> selecthzsjListByJhchugId(String jhchugid, String relaid, String glType) throws Exception;


}




