package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblAcquisitionRecord;
import com.huabo.system.mappersql.TblAcquisitionRecordMapperSqlConfig;


public interface TblAcquisitionRecordMapper extends BaseMapper<TblAcquisitionRecord> {


    @SelectProvider(type=TblAcquisitionRecordMapperSqlConfig.class,method="selectListByPageInfo")
    @Results({
		@Result(column="RECORDID",property="recordId"),
		@Result(column="RECORDYEAR",property="recordYear"),
		@Result(column="RECORDSTART",property="recordStart"),
		@Result(column="RECORDEND",property="recordEnd"),
		@Result(column="RECORDTIME",property="recordTime"),
		@Result(column="REALNAME",property="staffName"),
		@Result(column="ORGNAME",property="orgName"),
		@Result(column="RETYPE",property="reType"),
		@Result(column="RECORDIP",property="recordIp"),
		@Result(column="RECORDMEMO",property="recordMemo"),
	})
    IPage<TblAcquisitionRecord> selectListByPageInfo(IPage<TblAcquisitionRecord> page, BigDecimal orgid);

}
