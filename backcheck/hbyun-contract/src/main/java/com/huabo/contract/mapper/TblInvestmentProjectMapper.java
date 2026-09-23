package com.huabo.contract.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblInvestmentProject;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-23
 */
public interface TblInvestmentProjectMapper extends BaseMapper<TblInvestmentProject> {

	 
	@Insert("INSERT INTO TBL_INVESTMENT_PROJECT(PROJECTID,PROJECTNO,PROJECTNAME) VALUES(#{id},#{no},#{name})")
	void insertTblInvestmentProject(@Param("id")String id, @Param("no")String no,@Param("name")String name);
 
	@Update("UPDATE TBL_INVESTMENT_PROJECT SET PROJECTNO = #{no} ,PROJECTNAME = #{name} WHERE PROJECTID = #{id}")
	void updateTblInvestmentProject(@Param("id")String id, @Param("no")String no,@Param("name")String name) throws Exception;
  
	@Select("SELECT COUNT(*) FROM TBL_INVESTMENT_PROJECT WHERE PROJECTID=#{id}")
	Integer selectTblInvestmentProject(@Param("id")String id) throws Exception;

}
