package com.huabo.finance.mapper;

import com.huabo.finance.entity.TblConfigColumnInfo;
import com.huabo.finance.entity.TblConfigTableInfo;
import com.huabo.finance.entity.caiji.BdTaxrate;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TblConfigColumnInfoMapper extends BaseMapper<TblConfigColumnInfo> {

	@Delete("DELETE FROM TBL_CONFIG_COLUMNINFO WHERE FTBLID = #{tableId}")
	void deleteByTableId(@Param("tableId")String tableId) throws Exception;

	@Select("SELECT * FROM TBL_CONFIG_COLUMNINFO WHERE FTBLID = #{tableId}")
	List<TblConfigColumnInfo> selectListByTableId(@Param("tableId")String tableId) throws Exception;

}
