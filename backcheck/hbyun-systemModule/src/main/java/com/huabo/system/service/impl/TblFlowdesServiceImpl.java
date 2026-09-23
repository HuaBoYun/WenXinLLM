package com.huabo.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblFlowdes;
import com.huabo.system.mapper.TblFlowdesMapper;
import com.huabo.system.service.TblFlowdesService;

@Service
public class TblFlowdesServiceImpl implements TblFlowdesService {

    @Resource
    private TblFlowdesMapper tblFlowdesMapper;


    @Override
    public List<TblFlowdes> returnFlowBysqls(String flowid) {
        List<TblFlowdes> flowdes = this.tblFlowdesMapper.findBysqlFlowds(flowid);
        return flowdes;
    }

    @Override
    public void delete(String id) {
            tblFlowdesMapper.deleteByid(id);
    }

    @Override
    public void add(TblFlowdes tblFlowdes) {
        tblFlowdesMapper.insert(tblFlowdes);
    }

}
