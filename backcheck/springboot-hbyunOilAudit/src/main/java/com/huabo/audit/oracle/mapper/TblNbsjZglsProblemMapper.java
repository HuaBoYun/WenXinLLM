package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjZglsProblem;

public interface TblNbsjZglsProblemMapper extends BaseMapper<TblNbsjZglsProblem>{
	 
	
	    @Delete("DELETE FROM TBL_NBSJ_ZGLS_PROBLEM WHERE PROBLEMID = #{problemid}")
	    void deleteById(BigDecimal problemid) throws Exception;

	    
	    @InsertProvider(method="insertEntity",type=TblNbsjZglsProblemMapperSqlConfig.class)
	    @Options(useGeneratedKeys=true, keyProperty="problemid", keyColumn="PROBLEMID")
		void insertEntity(TblNbsjZglsProblem plan) throws Exception;

	
	@Select(" SELECT * FROM TBL_NBSJ_ZGLS_PROBLEM where REFORMID=#{refromid} and DATATYPE=#{datatype} ORDER BY PROBLEMID ")
	List<TblNbsjZglsProblem> getzgproblemlist(BigDecimal refromid,String datatype);
	
	
	
}
