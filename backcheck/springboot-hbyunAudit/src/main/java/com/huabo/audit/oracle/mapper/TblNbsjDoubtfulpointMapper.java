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
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblNbsjDoubtfulpointEntity;
import com.huabo.audit.oracle.vo.TblNbsjDoubtfulpointVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjDoubtfulpointMapper extends BaseMapper<TblNbsjDoubtfulpointEntity> {
	@Select("SELECT * from TBL_NBSJ_DOUBTFULPOINT WHERE DPOINTID= #{dpointid} ")
    TblNbsjDoubtfulpointEntity getById(String dpointid);
    
	@Select("SELECT DPNUMBER,DPNAME,DPDESCRIBE,TESTRESULT,EDITOR FROM TBL_NBSJ_DOUBTFULPOINT where DPOINTID in (${selectIds}) order by DPOINTID DESC")
	List<TblNbsjDoubtfulpointEntity> exclTblDoubtfulpointByids(@Param("selectIds")String selectIds) throws Exception;
	
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjDoubtfulpointMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjDoubtfulpointEntity> pageInfo,TblNbsjDoubtfulpointVo tblNbsjDoubtfulpointVo,TblStaffUtil loginStaff) throws Exception;

    @Select("SELECT TNA.* FROM TBL_NBSJ_DOUBTFULPOINT TNA  WHERE TNA.DPOINTID = #{dpointid}")
    @Results({
    	@Result(column="DPOINTID",property="dpointid"),
    	@Result(column="DPNUMBER",property="dpnumber"),
    	@Result(column="EDITOR",property="editor"),
    	@Result(column="EDITTIME",property="edittime"),
    	@Result(column="DPDESCRIBE",property="dpdescribe"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="DPNAME",property="dpname"),
    	@Result(column="TESTRESULT",property="testresult"),
    	@Result(column="DPSTSTUS",property="dpstatus"),
    	@Result(column="DPBYSYSTEM",property="dpbysystem"),
    	
    })
   	TblNbsjDoubtfulpointEntity selectById(@Param("dpointid") BigDecimal dpointid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjDoubtfulpointMapperSqlConfig.class)
    @Results({
    	@Result(column="DPOINTID",property="dpointid"),
    	@Result(column="DPNUMBER",property="dpnumber"),
    	@Result(column="EDITOR",property="editor"),
    	@Result(column="EDITTIME",property="edittime"),
    	@Result(column="DPDESCRIBE",property="dpdescribe"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="DPNAME",property="dpname"),
    	@Result(column="TESTRESULT",property="testresult"),
    	@Result(column="DPSTSTUS",property="dpstatus"),
    	@Result(column="DPBYSYSTEM",property="dpbysystem"),
    })
	List<TblNbsjDoubtfulpointEntity> selectListByPageInfo(PageInfo<TblNbsjDoubtfulpointEntity> pageInfo,TblNbsjDoubtfulpointVo tblNbsjDoubtfulpointVo,TblStaffUtil loginStaff) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_DOUBTFULPOINT WHERE DPOINTID = #{dpointid}")
    void deleteById(BigDecimal dpointid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjDoubtfulpointMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjDoubtfulpointEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjDoubtfulpointMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="dpointid", keyColumn="DPOINTID")
	void insertEntity(TblNbsjDoubtfulpointEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjDoubtfulpointMapperSqlConfig.class)
	void updateEntity(TblNbsjDoubtfulpointEntity plan) throws Exception;
    
    @Delete("DELETE FROM TBL_LEGAL_DOUBTFULPOINT_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);

}