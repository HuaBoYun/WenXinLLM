package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Transient;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblYmFlowRecordAtt;

import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblYmFlowRecordAttMapper extends BaseMapper<TblYmFlowRecordAtt> {
	@InsertProvider(type=TblYmFlowRecordAttMapperSqlConfig.class,method="insertEntity")
	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
	void insertEntity(TblYmFlowRecordAtt entity) throws Exception;

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,UPLOADTIME,UPLOADER FROM TBL_YMFLOWRECORD_ATT WHERE ATTID = #{attId} ")
	@Results({
		@Result(column="ATTID",property="attid"),
		@Result(column="ATTNAME",property="attname"),
		@Result(column="ATTPATH",property="attpath"),
		@Result(column="ATTSIZE",property="attsize"),
		@Result(column="UPLOADTIME",property="uploadtime"),
		@Result(column="UPLOADER",property="uploader"),
	})
	TblYmFlowRecordAtt selectEntityById(@Param("attId") String attId) throws Exception;

	@Delete("DELETE FROM TBL_YMFLOWRECORD_ATT WHERE ATTID = #{attid}")
	void deleteEntityById(String attId) throws Exception;

	//TYA.ATTID,TYA.ATTNAME,TYA.ATTSIZE,TYA.UPLOADTIME,TYA.ATTPATH,TYA.FLOWTASKID,TYA.OPERATORID,TS.REALNAME AS UPLOADERNAME
	@SelectProvider(type=TblYmFlowRecordAttMapperSqlConfig.class,method="selectAllList")
	@Results({
		@Result(column="ATTID",property="attid"),
		@Result(column="ATTNAME",property="attname"),
		@Result(column="ATTPATH",property="attpath"),
		@Result(column="ATTSIZE",property="attsize"),
		@Result(column="UPLOADTIME",property="uploadtime"),
		@Result(column="FLOWTASKID",property="flowtaskid"),
		@Result(column="OPERATORID",property="operatorid"),
		@Result(column="REALNAME",property="uploaderName"),
	})
	List<TblYmFlowRecordAtt> selectAllList(String flowTaskOperatorId, String flowTaskId) throws Exception;

	@Select("SELECT ATTPATH FROM TBL_YMFLOWRECORD_ATT WHERE ATTID = #{attId} ")
	String selectSignFileNameById(BigDecimal attId) throws Exception;

	@Select("SELECT ATTNAME FROM TBL_YMFLOWRECORD_ATT WHERE ATTID = #{attId} ")
	String selectEntityByIdalal(BigDecimal attId) throws Exception;
	
	@Select("SELECT JMURL FROM TBL_YMFLOWRECORD_ATT WHERE ATTID = #{attId} ")
	String selectJmurlFileNameById(BigDecimal attId) throws Exception;
}
