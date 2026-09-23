package com.huabo.system.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblVersion;
import com.huabo.system.mapper.TblVersionMapper;
import com.huabo.system.service.TblVersionService;

@Service("TblVersionService")
public class TblVersionServiceImpl implements TblVersionService {

    @Resource
    private TblVersionMapper tblVersionMapper;

    @Override
    public TblVersion findbyFid(String fid) {
        List<TblVersion> findBysqlOne = tblVersionMapper.findByfid(fid);
        if (findBysqlOne.size() > 0) {
            return findBysqlOne.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<TblVersion> selectAllTblVersion() {
        List<TblVersion> tblVersions = tblVersionMapper.findBysql();
        return tblVersions;
    }

}
