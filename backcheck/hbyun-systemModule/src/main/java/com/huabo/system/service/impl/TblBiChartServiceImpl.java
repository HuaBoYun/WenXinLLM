package com.huabo.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblBiChart;
import com.huabo.system.mapper.TblBiChartDao;
import com.huabo.system.service.TblBiChartService;

import lombok.extern.slf4j.Slf4j;

@Service("TblBiChartService")
@Slf4j
public class TblBiChartServiceImpl implements TblBiChartService {

    @Resource
    private TblBiChartDao tblBiChartDao;

    @Override
    public void add(TblBiChart biChart) {
        tblBiChartDao.insertBichart(biChart);
    }

}
