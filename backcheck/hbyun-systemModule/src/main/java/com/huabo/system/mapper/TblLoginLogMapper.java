package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.UserLoginLog;

public interface TblLoginLogMapper extends BaseMapper<UserLoginLog> {

	 //通过username查询最近一次的登录日志
    @Select("select ID from TBL_USER_LOGIN_LOG WHERE USER_NAME = #{username} AND LOGIN_TIME=(select MAX(LOGIN_TIME) from TBL_USER_LOGIN_LOG WHERE USER_NAME = #{username})  ")
    Long selectLastLogByUsername(@Param("username")String username);
    
}
