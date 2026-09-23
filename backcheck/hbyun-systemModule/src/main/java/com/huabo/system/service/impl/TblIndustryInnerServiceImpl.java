package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblIndustryInner;
import com.huabo.system.mapper.TblIndustryInnerDAO;
import com.huabo.system.service.TblIndustryInnerService;


@Service("TblIndustryInnerService")
public class TblIndustryInnerServiceImpl implements TblIndustryInnerService {

    @Resource
    private TblIndustryInnerDAO tblIndustryInnerDAO;
    
    @Override
    public void deleteByOrgid(BigDecimal orgid) {
    	tblIndustryInnerDAO.deleteByOrgid(orgid);
    }

    @Override
    public void delete(TblIndustryInner ii) {
        tblIndustryInnerDAO.deleteBYii(ii);
    }
    
    @Override
    public void save(TblIndustryInner industryInner) {
        tblIndustryInnerDAO.saveTblIndustryInner(industryInner);
    }
    
    @Override
    public List<TblIndustryInner> findByList(String hyid) {
        return this.tblIndustryInnerDAO.findByList(hyid);
    }

}
