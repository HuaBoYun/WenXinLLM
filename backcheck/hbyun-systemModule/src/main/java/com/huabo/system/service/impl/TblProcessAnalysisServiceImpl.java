package com.huabo.system.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.util.RandowUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblProcessAnalysis;
import com.huabo.system.mapper.TblProcessAnalysisMapper;
import com.huabo.system.service.TblProcessAnalysisService;

import cn.hutool.core.util.StrUtil;

@Service
public class TblProcessAnalysisServiceImpl implements TblProcessAnalysisService {

    @Resource
    private TblProcessAnalysisMapper tblProcessAnalysisMapper;

    @Override
    public void updateSetting(TblProcessAnalysis tblAnalysis) {
    	try {
    		
	        	if(StrUtil.isNotBlank(tblAnalysis.getUserid())) {
					tblProcessAnalysisMapper.insertUser(tblAnalysis.getProcessname(), tblAnalysis.getProcessid(), tblAnalysis.getUserid(), tblAnalysis.getRolename(), tblAnalysis.getUsertaskid(),RandomUtil.uuBigDecimalId());
	        	}else {
	        		tblProcessAnalysisMapper.insertRole(tblAnalysis.getProcessname(), tblAnalysis.getProcessid(), tblAnalysis.getUserid(), tblAnalysis.getRolename(), tblAnalysis.getUsertaskid(),RandomUtil.uuBigDecimalId());
	        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}
