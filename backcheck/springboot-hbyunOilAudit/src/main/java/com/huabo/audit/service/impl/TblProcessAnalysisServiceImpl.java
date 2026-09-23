package com.huabo.audit.service.impl;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.oracle.entity.TblProcessAnalysis;
import com.huabo.audit.oracle.mapper.TblProcessAnalysisMapper;
import com.huabo.audit.service.TblProcessAnalysisService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TblProcessAnalysisServiceImpl implements TblProcessAnalysisService {

    @Resource
    private TblProcessAnalysisMapper tblProcessAnalysisMapper;

    @Override
    public List<TblProcessAnalysis> getByModuel(String settingid) {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            return this.tblProcessAnalysisMapper.findSettingId(settingid);
        } else {
            return this.tblProcessAnalysisMapper.findSettingId(settingid);
        }
    }

    @Override
    public TblProcessAnalysis findOndBytakdidstart(String usertaskid, String anid) {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.listBySql(usertaskid, anid);
            return list != null && list.size() > 0 ? (TblProcessAnalysis) list.get(0) : null;
        } else {
            List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.listBySql(usertaskid, anid);
            return list != null && list.size() > 0 ? (TblProcessAnalysis) list.get(0) : null;
        }
    }

    @Override
    public boolean findByModuel(String flownumber, TblStaffUtil user) {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.listBySqlUser(flownumber, user);
            return list != null && list.size() > 0;
        } else {
            List<TblProcessAnalysis> list = this.tblProcessAnalysisMapper.listBySqlUser(flownumber, user);
            return list != null && list.size() > 0;
        }
    }


    @Override
    public TblProcessAnalysis findOnd(String analid){
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            return this.tblProcessAnalysisMapper.findOndAnalysis(analid);
        } else {
            return this.tblProcessAnalysisMapper.findOndAnalysis(analid);
        }
    }

    @Override
    public TblProcessAnalysis findOndBytakdid(String usertaskid){
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            TblProcessAnalysis list = this.tblProcessAnalysisMapper.findOndBytakdid(usertaskid);
            return list;
        } else {
            TblProcessAnalysis list = this.tblProcessAnalysisMapper.findOndBytakdid(usertaskid);
            return list;
        }
    }

    @Override
    public TblProcessAnalysis findOndBytakdidAnId(String usertaskid,String anid){
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
        	TblProcessAnalysis list = this.tblProcessAnalysisMapper.findOndBytakdidAnId(usertaskid, anid);
        	return list;
        } else {
            TblProcessAnalysis list = this.tblProcessAnalysisMapper.findOndBytakdidAnId(usertaskid, anid);
            return list;
        }
    }

}
