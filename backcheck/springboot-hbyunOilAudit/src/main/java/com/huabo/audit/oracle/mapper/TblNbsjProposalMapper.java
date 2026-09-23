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
import com.huabo.audit.oracle.entity.TblNbsjProposalEntity;
import com.huabo.audit.oracle.vo.TblNbsjProposalVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjProposalMapper extends BaseMapper<TblNbsjProposalEntity>{
	@Select("SELECT * from TBL_NBSJ_PROPOSAL WHERE PROID= #{proid} ")
    TblNbsjProposalEntity getById(String proid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjProposalMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjProposalEntity> pageInfo,TblNbsjProposalVo tblNbsjProposalVo,Integer projectId) throws Exception;

    @Select("SELECT TNA.*,STAFF.REALNAME "
    		+ "FROM TBL_NBSJ_PROPOSAL TNA "
    		+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATESTAFFID "
    		+ " WHERE TNA.PROID = #{proid}")
    @Results({
    	@Result(column="PROID",property="proid"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PROCODE",property="procode"),
    	@Result(column="PRONAME",property="proname"),
    	@Result(column="CREATETIME",property="createTime"),
    	@Result(column="CONTENT",property="content"),
    	@Result(column="REALNAME",property="tblStaff.realname"),
    })
   	TblNbsjProposalEntity selectById(@Param("proid") Integer proid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjProposalMapperSqlConfig.class)
    @Results({
    	@Result(column="PROID",property="proid"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="PROCODE",property="procode"),
    	@Result(column="PRONAME",property="proname"),
    	@Result(column="CREATETIME",property="createTime"),
    	@Result(column="REALNAME",property="tblStaff.realname"),
    	
    })
	List<TblNbsjProposalEntity> selectListByPageInfo(PageInfo<TblNbsjProposalEntity> pageInfo,TblNbsjProposalVo tblNbsjProposalVo,Integer projectId) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_PROPOSAL WHERE PROID = #{proid}")
    void deleteById(Integer proid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjProposalMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjProposalEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjProposalMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="proid", keyColumn="PROID")
	void insertEntity(TblNbsjProposalEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjProposalMapperSqlConfig.class)
	void updateEntity(TblNbsjProposalEntity plan) throws Exception;
    
    @Update("UPDATE TBL_NBSJ_PROPOSAL SET STATUS='1' WHERE PROID = #{proid}")
    void calcelById(Integer proid) throws Exception;
    
    
    @Delete("DELETE FROM TBL_LEGAL_AUDITSUGGEST_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(Integer attid);
}
