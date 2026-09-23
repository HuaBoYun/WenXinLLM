package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblTemplateDu;

import io.lettuce.core.dynamic.annotation.Param;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblTemplateDuMapper extends BaseMapper<TblTemplateDu> {

	@Select("SELECT * FROM TBL_TEMPLATE_UD WHERE    type=#{tempType}")
	List<TblTemplateDu> selectAttListByall(String tempType);
	
	
	@InsertProvider(type=TblTemplateDuMapperSqlConfig.class,method="insertEntity")
	@Options(useGeneratedKeys=true, keyProperty="tempId", keyColumn="TEMPID")
	void insertEntity(TblTemplateDu temp) throws Exception;
	
	
	@Select("SELECT * FROM TBL_TEMPLATE_UD WHERE tempId = #{tempId} ")
	TblTemplateDu selectEntityById(@Param("tempId") BigDecimal tempId) throws Exception;
	
	@Delete("DELETE FROM TBL_TEMPLATE_UD WHERE tempId=#{tempId}")
	void deletetemp(@Param("tempId")Integer tempId);
	

}
