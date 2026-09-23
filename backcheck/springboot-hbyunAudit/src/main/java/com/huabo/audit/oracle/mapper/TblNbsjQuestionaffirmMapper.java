package com.huabo.audit.oracle.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjQuestionaffirmEntity;

import java.math.BigDecimal;

public interface TblNbsjQuestionaffirmMapper extends BaseMapper<TblNbsjQuestionaffirmEntity>{

	
	@Delete("delete from TBL_NBSJ_QUESTIONAFFIRM where QUESTIONID in (select q.QUESTIONID from TBL_NBSJ_QUESTION q left join TBL_NBSJ_SHEET s on q.SHEETID = s.SHEETID"
			+ " where s.PROJECTID = #{projectid} and s.SHEETID = #{sheetid}) and FACTID = #{factid}")
    void deleteTblNbsjQuestionaffirmBySql(String sheetid, BigDecimal projectid, BigDecimal factid) throws Exception;
	
	
	@InsertProvider(method="insertEntity",type=TblNbsjQuestionaffirmMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="affirmid", keyColumn="AFFIRMID")
	void insertEntity(TblNbsjQuestionaffirmEntity plan) throws Exception;
}
