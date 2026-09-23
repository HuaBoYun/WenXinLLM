package com.huabo.system.service.impl;

import com.huabo.system.entity.TblNbsjPlanproject;
import com.huabo.system.mapper.TblNbsjPlanprojectMapper;
import com.huabo.system.service.TblNbsjPlanprojectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TblNbsjPlanprojectServiceImpl implements TblNbsjPlanprojectService {

    @Resource
    private TblNbsjPlanprojectMapper tblNbsjPlanprojectMapper;


    @Override
    public List<TblNbsjPlanproject> findByPlanId(String planid) {
        return tblNbsjPlanprojectMapper.findByPlanId(planid);
    }

}
