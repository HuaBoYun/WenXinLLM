package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblBiCkEcharts;
import com.huabo.system.mapper.TblBiCkEchartsDao;
import com.huabo.system.mapper.TblBiReportMenuMapper;
import com.huabo.system.service.TblBiCkEchartsService;

@Service
public class TblBiCkEchartsServiceImpl implements TblBiCkEchartsService {

    @Resource
    private TblBiReportMenuMapper tblBiReportMenuMapper;

    @Resource
    private TblBiCkEchartsDao tblBiCkEchartsDao;

//    @Override
//    public List find(BigDecimal pageid) {
//        TblBiReportMenu page = (TblBiReportMenu)tblBiReportMenuMapper.get(pageid);
//        return (List)(page != null ? this.tblBiCkEchartsDao.tblBiCkEchartsDao("tblBiReportMenu", page) : new ArrayList());
//    }

    @Override
    public void delete(BigDecimal pageid) {
            tblBiCkEchartsDao.deletePageId(pageid);

    }

    @Override
    public List<TblBiCkEcharts> getChartsPages(String pageid) {
        return this.tblBiCkEchartsDao.listBySql(pageid);
    }

}
