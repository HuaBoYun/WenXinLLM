package com.huabo.system.service.impl;

import com.huabo.system.entity.TblRiskevent;
import com.huabo.system.mapper.TblRiskeventMapper;
import com.huabo.system.service.TblRiskeventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TblRiskeventServiceImpl implements TblRiskeventService {

    @Resource
    private TblRiskeventMapper tblRiskeventMapper;

    @Override
    public void saveTblRiskevent(TblRiskevent tblRiskevent) {
        tblRiskeventMapper.insert(tblRiskevent);
    }

}
