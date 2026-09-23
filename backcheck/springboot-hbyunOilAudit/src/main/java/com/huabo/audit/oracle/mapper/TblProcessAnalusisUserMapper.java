package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblProcessAnalusisUser;

import io.lettuce.core.dynamic.annotation.Param;

public interface TblProcessAnalusisUserMapper extends BaseMapper<TblProcessAnalusisUser> {

	@Select("select * from TBL_PROCESS_ANALUSIS_USER where FROMID = #{fromid} and ANALID = #{analid}")
	TblProcessAnalusisUser findOnd(@Param("analid")String analid,@Param("fromid") String fromid) throws Exception;

	void updateSetting(TblProcessAnalusisUser analysisUser) throws Exception;

	@InsertProvider(method="insertSetting",type = TblProcessAnalusisUserMapperSqlConfig.class)
	void insertSetting(TblProcessAnalusisUser analysisUser) throws Exception;

	@Select("SELECT * FROM TBL_PROCESS_ANALUSIS_USER where FROMID= #{fromid} and ANALID= #{analid}")
    List<TblProcessAnalusisUser> listBySql(String analid, String fromid);

	@UpdateProvider(method="updateAnalysisUser",type=TblProcessAnalusisUserMapperSqlConfig.class)
	void updateAnalysisUser(TblProcessAnalusisUser analysisUser);
}