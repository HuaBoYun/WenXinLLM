package com.huabo.log.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.log.db.entity.UserLoginLog;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {
	
	@Select("SELECT * from TBL_STAFF WHERE ROLEIDSTRS  LIKE '%${roleid}%'")
	List<String> findbyusserid(String roleid);
	
	@Select("SELECT RID FROM TBL_ROLE WHERE RNAME='安全审计员' and rownum=1")
	String findbyRoleid();
}
