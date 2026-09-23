package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblNbsjInnerrule;
import com.huabo.contract.mappersql.TblNbsjInnerruleMapperSqlConfig;
import com.huabo.contract.vo.TblNbsjInnerRuleVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-07-05
 */
public interface TblNbsjInnerruleMapper extends BaseMapper<TblNbsjInnerrule> {
	
	@SelectProvider(method="selectInnerruleListView",type=TblNbsjInnerruleMapperSqlConfig.class)
	IPage<TblNbsjInnerrule> selectInnerruleListView(IPage<TblNbsjInnerrule> page,TblNbsjInnerRuleVo tblNbsjInnerRuleVo) throws Exception;
		  
	@Select("select * from TBL_CONTRACT_INNERRULE where INNRULID =#{id}")
	TblNbsjInnerrule findById(@Param("id")String id) throws Exception;
		
	@Delete("delete TBL_CONTRACT_INNERRULE where INNRULID =#{id}")
	void deleteById(@Param("id")String id) throws Exception;
		  
	@InsertProvider(type=TblNbsjInnerruleMapperSqlConfig.class,method="insertEntity")
	@Options(useGeneratedKeys=true, keyProperty="innrulid", keyColumn="INNRULID")
	void insertEntity(TblNbsjInnerrule outer) throws Exception;
		
	@UpdateProvider(type=TblNbsjInnerruleMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblNbsjInnerrule outer) throws Exception;
		
	@Insert("INSERT INTO TBL_CONTRACT_INNERRULE_ATT(INNRULID,ATTID) VALUES (#{innerid},#{attid})")
	void insertAttInfo(BigDecimal innerid,String attid) throws Exception;
	
	@Delete("delete TBL_CONTRACT_INNERRULE_ATT where INNRULID =#{innerid}")
	void deleteAttInfo(BigDecimal innerid) throws Exception;
	
	@Delete("delete TBL_CONTRACT_INNERRULE_ATT where attid =#{attid}")
	void deleteFileInfoByAttId(String attid) throws Exception;
	
	@Select("SELECT ATTID FROM TBL_CONTRACT_INNERRULE_ATT WHERE INNRULID=#{innerid} ")
	String selectOuterAtt(@Param("outid")String innerid) throws Exception;
	//=======合同制度自动编号
    @SelectProvider(method="selectMaxRulecode",type=TblNbsjInnerruleMapperSqlConfig.class)
	String selectMaxRulecode(String rulecode) throws Exception;
}
