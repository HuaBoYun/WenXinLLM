package com.huabo.audit.oracle.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.huabo.audit.oracle.entity.TblFlowardRecord;
import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 * 整改落实转发表 Mapper 接口
 * </p>
 *
 * @author  
 * @since 2023-11-24
 */
public interface TblFlowardRecordMapper extends Mapper<TblFlowardRecord> {
	
	
	@Select("SELECT * FROM TBL_FORWARD_RECORD WHERE  FORMID=#{formid}")
	List<TblFlowardRecord> findbyFromid(String formid);
	

}
