package com.huabo.financialdata.config.log;


import com.huabo.financialdata.config.exception.BizException;

/**
 * 默认日志服务类
 *
 * @author lee
 * @version 1.0.0
 **/
public class LogDefaultServiceImpl implements ILogService {

    @Override
    public void info(String message) {

    }

    @Override
    public void info(String message, BizException e) {

    }

    @Override
    public void info(String message, BizException e, Object... objects) {

    }

    @Override
    public void info(String message, Object... objects) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public void error(String message, BizException e) {

    }

    @Override
    public void error(String message, Object... objects) {

    }

    @Override
    public void error(String message, BizException e, Object... objects) {

    }
}
