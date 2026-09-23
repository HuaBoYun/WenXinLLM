package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjMb;

public interface TblNbsjMbMapper extends BaseMapper<TblNbsjMb>{
	
	 @Delete("DELETE FROM TBL_NBSJ_MB WHERE MBID = #{mbid}")
	 void deleteMbById(@Param("mbid")BigDecimal mbid) throws Exception;
	 
	 
	 @InsertProvider(method="insertEntity",type=TblNbsjMbMapperSqlConfig.class)
	 @Options(useGeneratedKeys=true, keyProperty="mbid", keyColumn="MBID")
	 void insertEntity(TblNbsjMb mb) throws Exception;
	 
	 
	 
	 @UpdateProvider(method="updateEntity",type=TblNbsjMbMapperSqlConfig.class)
	 void updateEntity(TblNbsjMb mb) throws Exception;
	 
	 
	 @SelectProvider(method="selectNbsjMbByPageInfo",type=TblNbsjMbMapperSqlConfig.class)
	 @Results({
	 	@Result(column="MBID",property="mbid"),
	 	@Result(column="MBCODE",property="mbcode"),
	 	@Result(column="MBNAME",property="mbname"),
	 	@Result(column="AUDITTYPE",property="audittype"),
	 	@Result(column="UPDATEDTIME",property="updatedtime"),
	 	@Result(column="STAFFID",property="createStaff.staffid",id=true),
	 	@Result(column="REALNAME",property="createStaff.realname"),
	 })
	List<TblNbsjMb> selectNbsjMbByPageInfo(PageInfo<TblNbsjMb> pageInfo, BigDecimal orgId, TblNbsjMb mb) throws Exception;

	/**
	 *
	 * @param orgId
	 * @param re
	 * @return
	 * @throws Exception
	 */
	List<TblNbsjMb> findList(@Param("orgId") BigDecimal orgId, @Param("re") TblNbsjMb re) throws Exception;
	 
	@SelectProvider(method="selectNbsjMbByPageInfoCount",type=TblNbsjMbMapperSqlConfig.class)
	Integer selectNbsjMbByPageInfoCount(BigDecimal orgId, TblNbsjMb mb) throws Exception; 
	 
	 
	 
	 @Select("SELECT s.*,STA.REALNAME FROM TBL_NBSJ_MB s LEFT JOIN TBL_STAFF sta on s.STAFFID=STA.STAFFID    WHERE MBID = #{mbid}")
	 @Results({
		 @Result(column="MBID",property="mbid"),
		 	@Result(column="MBCODE",property="mbcode"),
		 	@Result(column="MBNAME",property="mbname"),
		 	@Result(column="AUDITTYPE",property="audittype"),
		 	@Result(column="STAFFID",property="createStaff.staffid",id=true),
		 	@Result(column="REALNAME",property="createStaff.realname"),
	 })
	 TblNbsjMb selectNbsjMbByID(@Param("mbid") BigDecimal mbid) throws Exception;
	 
	 
	  @Insert("INSERT INTO TBL_NBSJ_MB_ATT(MBID,ATTID) VALUES (#{mbid},#{aid})")
	  void insertAttInfoAtt(@Param("mbid")BigDecimal mbid,@Param("aid") String aid) throws Exception;
	  
	  @Insert("INSERT INTO TBL_NBSJ_MB_DATAPRE(MBID,DATAOREID) VALUES (#{mbid},#{datepreid})")
	  void insertMbdatapre(String mbid, Integer datepreid) throws Exception;

	  @Insert("DELETE FROM TBL_NBSJ_MB_DATAPRE where  DATAOREID=#{datepreid}")
	  void deletetMbdatapre(Integer datepreid) throws Exception;
	  
	  @Insert("DELETE FROM TBL_NBSJ_MB_ATT where  MBID=#{mbid}")
	  void deleteAttInfoAttBymbid(@Param("mbid") BigDecimal mbid) throws Exception;
	  
	  
	  @Insert("DELETE FROM TBL_NBSJ_MB_ATT where  ATTID=#{attid}")
	  void deleteAttInfoAttid(@Param("attid") BigDecimal attid) throws Exception;
	  
	  @Insert("DELETE FROM TBL_NBSJ_MB_DATAPRE where  MBID=#{mbid}")
	  void delAttListBymbId(BigDecimal mbid) throws Exception;

	
	@SelectProvider(method="selectNbsjMbByyy",type=TblNbsjMbMapperSqlConfig.class)
	Integer selectNbsjMbByyy(BigDecimal orgId, BigDecimal mbid) throws Exception; 
	
	
	 @SelectProvider(method="selectNbsjMbByDateperidPageInfo",type=TblNbsjMbMapperSqlConfig.class)
	 @Results({
	 	@Result(column="MBID",property="mbid"),
	 	@Result(column="MBCODE",property="mbcode"),
	 	@Result(column="MBNAME",property="mbname"),
	 	@Result(column="AUDITTYPE",property="audittype"),
	 	@Result(column="UPDATEDTIME",property="updatedtime"),
	 	@Result(column="STAFFID",property="createStaff.staffid",id=true),
	 	@Result(column="REALNAME",property="createStaff.realname"),
	 })
	List<TblNbsjMb> selectNbsjMbByDateperidPageInfo(Integer dataperid) throws Exception;
		 
		 
}
