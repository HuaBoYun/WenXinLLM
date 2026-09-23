package com.huabo.compliance.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import net.sf.json.JSONObject;

import java.util.List;

public interface TblAutonoNumberMapper {
	


	@Select("SELECT ISAUTONUMBER FROM TBL_ORGANIZATION WHERE ORGID =  #{orgid} ")
	String getisUse(Integer orgid);
	
	@Select("SELECT CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTCODE	ELSE TON.NOCODE	END AS \"CODE\" ,"
			+ "CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTSEPARTOR ELSE TON.NOSEPARTOR END AS SEPARTOR,	CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTNUMBER ELSE TON.NONUMBER END AS \"NUMBER\""
			+ " FROM TBL_AUTONO_INFO TAI LEFT JOIN TBL_ORG_NO TON ON TAI.NOID = TON.NOID WHERE TON.ORGID = #{orgid} AND TON.NOID = #{noId}")
	List<JSONObject> getcodeRule(@Param("orgid")Integer orgid,@Param("noId")Integer noId);
	
	@SelectProvider(method="selectUniqueColumn",type= TblAutonoNumberMapperSqlConfig.class)
	String selectUniqueColumn(String noSql) throws Exception;
	
	
	
}