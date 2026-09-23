package com.huabo.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblIndicatorthreshold;
import com.huabo.system.mapper.TblIndicatorthresholdMapper;
import com.huabo.system.service.TblIndicatorthresholdService;

@Service
public class TblIndicatorthresholdServiceImpl implements TblIndicatorthresholdService {

    @Resource
    private TblIndicatorthresholdMapper tblIndicatorthresholdMapper;

    @Override
    public List<TblIndicatorthreshold> findByIndicatorId(String thresholdid) {
    	return tblIndicatorthresholdMapper.findByIndicatorId(thresholdid);
    }
}
