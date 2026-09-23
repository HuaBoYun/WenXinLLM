package com.huabo.system.service.impl;

import com.huabo.system.entity.TblNbsjSheet;
import com.huabo.system.mapper.TblNbsjSheetMapper;
import com.huabo.system.service.TblNbsjSheetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class TblNbsjSheetServiceImpl implements TblNbsjSheetService {

    @Resource
    private TblNbsjSheetMapper tblNbsjSheetMapper;

    @Override
    public TblNbsjSheet get(BigDecimal spid) {
        return tblNbsjSheetMapper.getSpid(spid);
    }

}
