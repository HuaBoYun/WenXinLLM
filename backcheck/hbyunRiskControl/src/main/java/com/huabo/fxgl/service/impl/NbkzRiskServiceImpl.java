package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.entity.NbkzRisk;
import com.huabo.fxgl.mapper.NbkzRiskMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.fxgl.service.INbkzRiskService;
import com.huabo.fxgl.entity.Criterion;
import com.huabo.fxgl.entity.NbkzRisk;
import com.huabo.fxgl.mapper.NbkzRiskMapper;
import com.huabo.fxgl.service.INbkzRiskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
@Service
public class NbkzRiskServiceImpl extends ServiceImpl<NbkzRiskMapper, NbkzRisk> implements INbkzRiskService {
    @Autowired
    private NbkzRiskMapper nbkzRiskMapper;
    @Override
    public NbkzRisk get(String riskid) {
        List<NbkzRisk> nbkzRisks = nbkzRiskMapper.get(riskid);
        if(nbkzRisks!=null && nbkzRisks.size()>0){
            return nbkzRisks.get(0);
        }
        return null;
    }
    @Override
    public NbkzRisk getBycode(String risknumber, String type, String orgid) {

        List<NbkzRisk> list = nbkzRiskMapper.getBycode(risknumber, type, orgid);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }
    @Override
    public List<Criterion> findAll(String orgid) {
        List<Criterion> all = nbkzRiskMapper.findAll(orgid);
        return all;
    }
}
