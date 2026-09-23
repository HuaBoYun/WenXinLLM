package com.huabo.contract.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblFundPool;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-23
 */
public interface TblFundPoolMapper extends BaseMapper<TblFundPool> {

	 
	@Insert("INSERT INTO Tbl_FundPool(FUNDPOOLID,FUNDPOOLNO,FUNDPOOLNAME) VALUES(#{id},#{no},#{name})")
	void insertTblFundPool(@Param("id")String id, @Param("no")String no,@Param("name")String name);
 
	@Update("UPDATE Tbl_FundPool SET FUNDPOOLNO = #{no} ,FUNDPOOLNAME = #{name} WHERE FUNDPOOLID = #{id}")
	void updateTblFundPool(@Param("id")String id, @Param("no")String no,@Param("name")String name) throws Exception;
 
	 @Select("select count(*) from Tbl_FundPool where FUNDPOOLID=#{id}")
	 Integer selectTblFundPool(@Param("id")String id);

}
