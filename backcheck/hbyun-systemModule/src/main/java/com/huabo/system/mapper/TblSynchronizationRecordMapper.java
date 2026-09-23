package com.huabo.system.mapper;

import com.huabo.system.entity.TblSynchronizationRecord;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 同步信息记录表 Mapper 接口
 * </p>
 *
 * @author lhp
 * @since 2025-08-14
 */
public interface TblSynchronizationRecordMapper extends BaseMapper<TblSynchronizationRecord> {

	@Select("SELECT MAX(CREATETIME) FROM TBL_SYNCHRONIZATION_RECORD WHERE RECORDTYPE = #{recordtype}")
	Date selectLastSyncDateByType(@Param("recordtype")String recordtype) ;

}
