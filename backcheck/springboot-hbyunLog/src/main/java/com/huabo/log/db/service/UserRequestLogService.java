package com.huabo.log.db.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.log.db.entity.UserRequestLog;
import com.huabo.log.db.mapper.UserRequestLogMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRequestLogService extends ServiceImpl<UserRequestLogMapper, UserRequestLog> {
}
