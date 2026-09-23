package com.huabo.log.db.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.log.db.entity.UserLoginLog;
import com.huabo.log.db.mapper.UserLoginLogMapper;
import org.springframework.stereotype.Service;

@Service
public class UserLoginLogService extends ServiceImpl<UserLoginLogMapper, UserLoginLog> {
}
