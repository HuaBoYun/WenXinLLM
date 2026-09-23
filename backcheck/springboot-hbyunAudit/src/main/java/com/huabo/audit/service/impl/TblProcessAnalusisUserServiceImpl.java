package com.huabo.audit.service.impl;

import org.springframework.stereotype.Service;

import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.oracle.entity.TblProcessAnalusisUser;
import com.huabo.audit.oracle.mapper.TblProcessAnalusisUserMapper;
import com.huabo.audit.service.TblProcessAnalusisUserService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TblProcessAnalusisUserServiceImpl implements TblProcessAnalusisUserService {

    @Resource
    private TblProcessAnalusisUserMapper tblProcessAnalusisUserMapper;

    @Override
    public TblProcessAnalusisUser findOnd(String analid, String fromid) {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List<TblProcessAnalusisUser> list = this.tblProcessAnalusisUserMapper.listBySql(analid, fromid);
            return list != null && list.size() > 0 ? (TblProcessAnalusisUser) list.get(0) : null;
        } else {
            List<TblProcessAnalusisUser> list = this.tblProcessAnalusisUserMapper.listBySql(analid, fromid);
            return list != null && list.size() > 0 ? (TblProcessAnalusisUser) list.get(0) : null;
        }
    }

    @Override
    public void updateSetting(TblProcessAnalusisUser analysisUser) {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            tblProcessAnalusisUserMapper.updateAnalysisUser(analysisUser);
        } else {
            tblProcessAnalusisUserMapper.updateAnalysisUser(analysisUser);
        }
    }

    @Override
    public void insertSetting(TblProcessAnalusisUser analysisUser) throws Exception {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            this.tblProcessAnalusisUserMapper.insertSetting(analysisUser);
        } else {
            this.tblProcessAnalusisUserMapper.insertSetting(analysisUser);
        }
    }
}
