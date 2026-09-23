package com.huabo.etl.exception.file;

import com.huabo.etl.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author zhibo.cao
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
