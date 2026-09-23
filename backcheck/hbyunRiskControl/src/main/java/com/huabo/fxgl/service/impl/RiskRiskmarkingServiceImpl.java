package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.huabo.fxgl.entity.RiskRiskmarking;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.mapper.RiskRiskmarkingMapper;
import com.huabo.fxgl.service.IRiskRiskmarkingService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
@Service
public class RiskRiskmarkingServiceImpl extends ServiceImpl<RiskRiskmarkingMapper, RiskRiskmarking> implements IRiskRiskmarkingService {

    @Autowired
    private  RiskRiskmarkingMapper riskRiskmarkingMapper;

    @Override
    public List<RiskRiskmarking> checkSubmit(BigDecimal planId) {
            return baseMapper.checkSubmit(planId);
    }
    @Override
    public void deleteByMark(Set<RiskRiskmarking> markingSet) {
        for (RiskRiskmarking riskMarking : markingSet) {
            this.deleteRiskMarking(riskMarking);
        }
    }
    public void deleteRiskMarking(RiskRiskmarking riskMarking){
        baseMapper.deleteById(riskMarking.getMarkingid());   /*    */
    }


    /*@Override
    public IPage<RiskRiskmarking> findRiskByRiskAndUserId(RiskAssplan riskAssplan, IPage page, BigDecimal staffid) {
//        return baseMapper.findRiskandRiskAssPlan(riskAssplan.getAssplanid(),page,staffid);
        return null;
    }*/

    @Override
    public List<RiskRiskmarking> findByRiskId(BigDecimal assriskid) {
        return riskRiskmarkingMapper.findByRiskId(assriskid);
    }
	
	  @Override
	    public IPage<RiskRiskmarking> getMarkingList(BigDecimal planId, IPage pageBean) {
	    	IPage<RiskRiskmarking> pageinfo=baseMapper.getMarkingList(planId, pageBean);
	    
	        return pageinfo;
	    }

	    
}
