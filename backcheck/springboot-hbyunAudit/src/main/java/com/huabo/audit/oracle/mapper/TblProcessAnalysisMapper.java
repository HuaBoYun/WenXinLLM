package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblProcessAnalusisUser;
import com.huabo.audit.oracle.entity.TblProcessAnalysis;

import io.lettuce.core.dynamic.annotation.Param;

public interface TblProcessAnalysisMapper extends tk.mybatis.mapper.common.Mapper<TblProcessAnalysis> {

	@SelectProvider(method="getByModuel",type=TblProcessAnalysisMapperSqlConfig.class)
	Integer getByModuel(String flownumber, TblStaffUtil staff) throws Exception;
	/**
	 * 正则对字符串数字进行排序
	 * @param settingid
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT * FROM TBL_PROCESS_ANALYSIS WHERE PROCESSNAME = #{settingId} order by to_number(regexp_substr(USERTASKID,'[0-9]*[0-9]',1))")
	List<TblProcessAnalysis> getByFlowSetting(@Param("settingId")String settingid) throws Exception;

	@SelectProvider(method="findOndBytakdidstart",type=TblProcessAnalysisMapperSqlConfig.class)
	TblProcessAnalysis findOndBytakdidstart(String usertaskid,String anid) throws Exception;

	@Select("select * from TBL_PROCESS_ANALUSIS_USER where FROMID = #{fromid} and ANALID = #{analid} AND ROWNUM = 1 ")
	TblProcessAnalusisUser findOnd( @Param("analid")String analid, @Param("fromid")String fromid);

	@Select("select * from TBL_PROCESS_ANALYSIS where ANALID = #{analid} AND ROWNUM = 1 ")
	TblProcessAnalysis findOndAnalysis( @Param("analid")String analid);

	@SelectProvider(method="findOndBytakdid",type=TblProcessAnalysisMapperSqlConfig.class)
	TblProcessAnalysis findOndBytakdid(String usertaskid);

	@Select("SELECT * FROM TBL_PROCESS_ANALUSIS_USER WHERE ANALID = #{analid}")
	TblProcessAnalysis findOndByAnalid(@Param("analid") String analid) throws Exception;

	@SelectProvider(method="findOndBytakdidAnId",type=TblProcessAnalysisMapperSqlConfig.class)
	TblProcessAnalysis findOndBytakdidAnId(String usertaskid, String anid);


	@Select("SELECT * FROM TBL_PROCESS_ANALYSIS WHERE PROCESSNAME = #{settingid}")
    List<TblProcessAnalysis> findSettingId(String settingid);

	@SelectProvider(method="listBySql",type=TblProcessAnalysisMapperSqlConfig.class)
    List<TblProcessAnalysis> listBySql(String usertaskid, String anid);

	@SelectProvider(method="listBySqlUser",type=TblProcessAnalysisMapperSqlConfig.class)
	List<TblProcessAnalysis> listBySqlUser(String flownumber, TblStaffUtil user);

	@SelectProvider(method="getByModuelStaff",type=TblProcessAnalysisMapperSqlConfig.class)
	List<TblProcessAnalysis> getByModuelStaff(TblStaffUtil staff);

	@SelectProvider(method="getByStaff",type=TblProcessAnalysisMapperSqlConfig.class)
	boolean getByStaff(String flownumber, TblStaffUtil staff);

	@SelectProvider(method="getByModu",type=TblProcessAnalysisMapperSqlConfig.class)
	boolean getByModu(String flownumber, TblStaffUtil staff);
}