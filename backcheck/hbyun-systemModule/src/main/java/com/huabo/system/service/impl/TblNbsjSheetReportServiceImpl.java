package com.huabo.system.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblNbsjSheetReport;
import com.huabo.system.mapper.TblNbsjSheetReportMapper;
import com.huabo.system.service.TblNbsjSheetReportService;

@Service
public class TblNbsjSheetReportServiceImpl implements TblNbsjSheetReportService {

    @Resource
    private TblNbsjSheetReportMapper tblNbsjSheetReportMapper;

    @Override
    public List<TblNbsjSheetReport> findReportListBySheet(String sheetid) {
        return tblNbsjSheetReportMapper.findReportListBySheet(sheetid);
    }

    @Override
    public void update(TblNbsjSheetReport tnsr) {
        // TODO Auto-generated method stub

    }
}
