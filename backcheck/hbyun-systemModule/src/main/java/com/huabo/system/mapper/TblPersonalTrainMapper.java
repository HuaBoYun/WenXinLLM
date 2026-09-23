package com.huabo.system.mapper;


import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblPersonalTrain;

import io.lettuce.core.dynamic.annotation.Param;

import org.apache.ibatis.annotations.*;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import java.math.BigDecimal;

public interface TblPersonalTrainMapper extends BaseMapper<TblPersonalTrain> {
 
	@UpdateProvider(method = "updateTrain",type = TblPersonTrainMapperSqlConfig.class)
    void updateTrain(TblPersonalTrain train);

	@InsertProvider(method = "saveTrain",type = TblPersonTrainMapperSqlConfig.class)
	@Options(useGeneratedKeys=true, keyProperty="trainid", keyColumn="TRAINID")
	void  saveTrain(TblPersonalTrain train);
	
	@Delete("delete tbl_personal_train where trainid=#{id}")
	void deleteTrainById(String id);
	 
	@Select("select att.attpath,att.isencrypted,att.jmurl, t.trainid,traintime,trainlocation,trainevidence,trainwitness,staffid,a.attid from tbl_personal_train t left join TBL_TRAINING_ATT a on t.trainid=a.trainid   left join tbl_attachment att on att.attid=a.attid  where staffid=#{staffid}")
    List<TblPersonalTrain> findAllTrain(String staffid);
	
}
