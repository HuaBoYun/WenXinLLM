package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblFlowTemplate;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblFlowTemplateMapper extends BaseMapper<TblFlowTemplate> {

	@Insert("INSERT INTO TBL_FLOW_TEMPLATE(TEMPID, LINKSTAFF, CREATETIME, FLOWID, TASKNODEID, TEMPMEMO, TEMPTITLE ) VALUES (#{tempId}, #{linkStaff}, SYSDATE, #{flowId}, #{taskNodeId}, #{tempMemo}, #{tempTitle})")
	@Options(useGeneratedKeys=true, keyProperty="tempId", keyColumn="TEMPID")
	void insertReturnId(TblFlowTemplate temp) throws Exception;
	
	@Update("UPDATE TBL_FLOW_TEMPLATE SET TEMPMEMO = #{tempMemo} , TEMPTITLE = #{tempTitle} WHERE TEMPID = #{tempId}")
	void updateEntity(TblFlowTemplate temp) throws Exception;
	
	@Delete("DELETE FROM TBL_FLOW_TEMPLATE WHERE TEMPID = #{tempId}")
	void deleteEntity(BigDecimal tempId) throws Exception;
	
	@Select("SELECT * FROM TBL_FLOW_TEMPLATE WHERE TEMPID = #{tempId}")
	TblFlowTemplate findById(BigDecimal tempId) throws Exception;

	@SelectProvider(type=TblFlowTemplateMapperSqlConfig.class,method="selectListByPageInfo")
	IPage<TblFlowTemplate> selectListByPageInfo(IPage<TblFlowTemplate> page, TblFlowTemplate temp) throws Exception;
}
