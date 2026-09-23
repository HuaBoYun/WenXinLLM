package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqk;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqkhz;

public interface TblYqnsJsxmJbqkhzMapper extends BaseMapper<TblYqnsJsxmJbqkhz> {
	
	@Select(" SELECT * " +
            " FROM TBL_YQNS_JSXM_JBQK WHERE" +
            " JSXMJBQKID IN (SELECT JSXMJBQKID FROM TBL_YQNS_JSXM_JBQKZJB " +
            " WHERE HZID = #{hzid})")
    List<TblYqnsJsxmJbqk> selectSubListById(Long hzid);
	
	@Delete("DELETE FROM TBL_YQNS_JSXM_JBQKZJB WHERE HZID = #{hzid}")
    void deleteLine(Long hzid);
	
	@Select(" SELECT JSXMJBQKID " +
            " FROM TBL_YQNS_JSXM_JBQK WHERE" +
            " JSXMJBQKID IN (SELECT JSXMJBQKID FROM TBL_YQNS_JSXM_JBQKZJB " +
            " WHERE HZID = #{hzid})")
    List<String> selectSubIdsListById(Long hzid);
	
	@Insert("INSERT INTO TBL_YQNS_JSXM_JBQKZJB(HZID,JSXMJBQKID) VALUES(#{hzid},#{jsxmjbqkid})")
    void saveLine(Long hzid, String jsxmjbqkid);
	
}




