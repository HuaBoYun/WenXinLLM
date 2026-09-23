package com.huabo.system.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblNbsjProject;
import com.huabo.system.mapper.TblNbsjProjectMapper;
import com.huabo.system.service.TblNbsjProjectService;

@Service
public class TblNbsjProjectServiceImpl implements TblNbsjProjectService {

    @Resource
    private TblNbsjProjectMapper tblNbsjProjectMapper;

    @Override
    public TblNbsjProject findBySheetid(BigDecimal spid) {
        return tblNbsjProjectMapper.findBySheetid(spid);
    }

}
