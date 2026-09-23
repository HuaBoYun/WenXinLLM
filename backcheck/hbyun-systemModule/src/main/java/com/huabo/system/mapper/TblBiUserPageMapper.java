package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.huabo.system.entity.TblBiReportMenu;

import tk.mybatis.mapper.common.Mapper;

public interface TblBiUserPageMapper extends Mapper<TblBiReportMenu> {

	@Select("SELECT COUNT(0) FROM TBL_BI_USER_PAGE WHERE STAFFID = #{staffId} AND PAGEID = #{pageId} ")
	Integer selectUserPageCount(String staffId, String pageId) throws Exception;

	@Insert("INSERT INTO TBL_BI_USER_PAGE(STAFFID,PAGEID,PAGETYPE) VALUES (#{staffId},#{pageId},#{pageType})")
	void insertEntity(String staffId, String pageId, String pageType) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_BI_USER_PAGE WHERE PAGEID = #{pageid} ")
	Integer selectPageCount(BigDecimal pageid) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_SYSTEM_BIMODULE WHERE PAGEID = #{pageid} ")
	Integer selectModuleTypePageCount(BigDecimal pageid) throws Exception;

}
