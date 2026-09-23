package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjLeavemeetingEntity;
import com.huabo.audit.oracle.vo.TblNbsjLeavemeetingVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjLeavemeetingMapper extends BaseMapper<TblNbsjLeavemeetingEntity>{
    
    @Select("SELECT * from TBL_NBSJ_LEAVEMEETING WHERE LEAVEID= #{leaveid} ")
    TblNbsjLeavemeetingEntity getById(String leaveid);
    
    @SelectProvider(method="selectNbsjLeavemeetingCountByPageInfo",type=TblNbsjLeavemeetingMapperSqlConfig.class)
   	Integer selectNbsjLeavemeetingCountByPageInfo(PageInfo<TblNbsjLeavemeetingEntity> pageInfo,TblNbsjLeavemeetingVo tblNbsjLeavemeetingVo) throws Exception;

    @Select("SELECT TNA.* FROM TBL_NBSJ_LEAVEMEETING TNA  WHERE TNA.LEAVEID = #{leaveid}")
    @Results({
    	@Result(column="LEAVEID",property="leaveid"),
    	@Result(column="LEAVECOED",property="leavecoed"),
    	@Result(column="LEAVENAME",property="leavename"),
    	@Result(column="CREATRTIME",property="creatrtime"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="CONTENT",property="content"),
    })
   	TblNbsjLeavemeetingEntity selectNbsjLeavemeetingEntityById(@Param("leaveid") BigDecimal leaveid) throws Exception;
    
    @SelectProvider(method="selectNbsjLeavemeetingListByPageInfo",type=TblNbsjLeavemeetingMapperSqlConfig.class)
    @Results({
    	@Result(column="LEAVEID",property="leaveid"),
    	@Result(column="LEAVECOED",property="leavecoed"),
    	@Result(column="LEAVENAME",property="leavename"),
    	@Result(column="CREATRTIME",property="creatrtime"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="CONTENT",property="content"),
    })
	List<TblNbsjLeavemeetingEntity> selectNbsjLeavemeetingListByPageInfo(PageInfo<TblNbsjLeavemeetingEntity> pageInfo,TblNbsjLeavemeetingVo tblNbsjLeavemeetingVo) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_LEAVEMEETING WHERE LEAVEID = #{leaveid}")
    void deleteLeavemeetingEntityById(BigDecimal leaveid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjLeavemeetingMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjLeavemeetingEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjLeavemeetingMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="leaveid", keyColumn="LEAVEID")
	void insertEntity(TblNbsjLeavemeetingEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjLeavemeetingMapperSqlConfig.class)
	void updateEntity(TblNbsjLeavemeetingEntity plan) throws Exception;
    
    @Update("UPDATE TBL_NBSJ_LEAVEMEETING SET STATUS='1' WHERE LEAVEID = #{leaveid}")
    void calcelLeavemeetingEntityById(BigDecimal leaveid) throws Exception;
    
    @Delete("DELETE FROM TBL_LEGAL_LEAVEMEETING_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);
    
}
