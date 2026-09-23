package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgGL;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkhz;

public interface TblYqnsJsxmTzwcqkhzMapper extends BaseMapper<TblYqnsJsxmTzwcqkhz> {
	
	@Select(" SELECT * " +
            " FROM TBL_YQNS_JSXM_TZWCQK WHERE" +
            " JSXMTZWCQKID IN (SELECT JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQKZJB " +
            " WHERE HZID = #{hzid})")
    List<TblYqnsJsxmTzwcqk> selectSubListById(Long hzid);
	
	@Delete("DELETE FROM TBL_YQNS_JSXM_TZWCQKZJB WHERE HZID = #{hzid}")
    void deleteLine(Long hzid);
	
	@Select(" SELECT JSXMTZWCQKID " +
            " FROM TBL_YQNS_JSXM_TZWCQK WHERE" +
            " JSXMTZWCQKID IN (SELECT JSXMTZWCQKID FROM TBL_YQNS_JSXM_TZWCQKZJB " +
            " WHERE HZID = #{hzid})")
    List<String> selectSubIdsListById(Long hzid);
	
	@Insert("INSERT INTO TBL_YQNS_JSXM_TZWCQKZJB(HZID,JSXMTZWCQKID) VALUES(#{hzid},#{jsxmtzwcqkid})")
    void saveLine(Long hzid, String jsxmtzwcqkid);
	
	@SelectProvider(method = "selectJsxmtzPage", type = TblYqnsJhglJhcgGLMapperSqlConfig.class)
	List<TblYqnsJhglJhcgGL> selectJsxmtzPage() throws Exception;


	@SelectProvider(method = "getJsxmjgjsPage", type = TblYqnsJhglJhcgGLMapperSqlConfig.class)
	List<TblYqnsJhglJhcgGL> getJsxmjgjsPage(String year) throws Exception;

}




