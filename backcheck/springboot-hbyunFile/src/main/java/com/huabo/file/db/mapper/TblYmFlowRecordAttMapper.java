package com.huabo.file.db.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.file.db.entity.TblYmFlowRecordAtt;



@Mapper
public interface TblYmFlowRecordAttMapper extends BaseMapper<TblYmFlowRecordAtt> {

	@Select("SELECT * FROM TBL_YMFLOWRECORD_ATT WHERE ATTID = #{attId} ")
	TblYmFlowRecordAtt selectEntityById(String attId);



	@Select("SELECT ATTPATH FROM TBL_YMFLOWRECORD_ATT WHERE ATTID = #{attId} ")
	String selectSignFileNameById(BigDecimal attId) throws Exception;
	
	@Select("SELECT JMURL FROM TBL_YMFLOWRECORD_ATT WHERE ATTID = #{attId} ")
	String selectJmurlFileNameById(BigDecimal attId) throws Exception;
}
