package com.huabo.legal.exam.modules.sys.system.service.impl;

import com.huabo.legal.exam.modules.sys.system.mapper.SysDictMapper;
import com.huabo.legal.exam.modules.sys.system.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bool
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public String findDict(String table, String text, String key, String value) {
//        return sysDictMapper.findDict(table, text, key, value);
        return null;
    }
}
