package com.huabo.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.system.entity.TblUserRequestLog;
import com.huabo.system.mapper.TblUserRequestLogMapper;
import com.huabo.system.service.TblUserRequestLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户请求日志 Service 实现类
 * 
 * @author Augment Agent
 * @date 2025-10-20
 */
@Slf4j
@Service
public class TblUserRequestLogServiceImpl extends ServiceImpl<TblUserRequestLogMapper, TblUserRequestLog> 
        implements TblUserRequestLogService {

    /**
     * 保存用户请求日志
     * 
     * @param log 日志对象
     * @return 是否保存成功
     */
    @Override
    public boolean saveRequestLog(TblUserRequestLog logRecord) {
        try {
            // 设置创建时间
            if (logRecord.getCreatedAt() == null) {
                logRecord.setCreatedAt(new Date());
            }

            // 保存到数据库
            boolean result = this.save(logRecord);

            if (result) {
                log.info("用户请求日志保存成功: userId={}, method={}, duration={}ms",
                    logRecord.getUserId(), logRecord.getRequestClassMethod(), logRecord.getRequestDurationMs());
            } else {
                log.warn("用户请求日志保存失败: userId={}, method={}",
                    logRecord.getUserId(), logRecord.getRequestClassMethod());
            }

            return result;
        } catch (Exception e) {
            log.error("保存用户请求日志异常", e);
            return false;
        }
    }

}

