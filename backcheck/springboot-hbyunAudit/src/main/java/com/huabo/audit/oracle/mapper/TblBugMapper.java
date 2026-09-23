package com.huabo.audit.oracle.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblBugEntity;

public interface TblBugMapper extends BaseMapper<TblBugEntity>{
	
	@Select("SELECT * from TBL_BUG WHERE BUGID= #{bugid} ")
	TblBugEntity findById(String bugid);

}
