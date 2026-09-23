package com.huabo.monitor.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.mockito.internal.matchers.Find;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.entity.TblProblemTransfer;


public interface TblProblemTransferMapper extends BaseMapper<TblProblemTransfer> {

	 @Select("select *from TBL_PROBLEM_TRANSFER where PROBLEMID=#{id}  order by id desc")
	 List<TblProblemTransfer> getProblemTransferList(@Param("id")BigDecimal id);
}
