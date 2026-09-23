package com.huabo.system.service.impl;

import com.huabo.system.entity.TblRisk;
import com.huabo.system.mapper.TblRiskMapper;
import com.huabo.system.service.TblRiskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TblRiskServiceImpl implements TblRiskService {

    @Resource
    private TblRiskMapper tblRiskMapper;

    @Override
    public TblRisk findTblRiskByFlowId(BigDecimal flowid) {
        List<TblRisk> list = this.tblRiskMapper.findBysql(flowid);
        TblRisk risk = new TblRisk();
        if (list != null && list.size() > 0) {
            risk = (TblRisk) list.get(0);
        }

        return risk;
    }

    @Override
    public TblRisk findById(String riskid) {
        List<TblRisk> list = this.tblRiskMapper.findByTiskid(riskid);
        TblRisk risk = new TblRisk();
        if (list != null && list.size() > 0) {
            risk = (TblRisk) list.get(0);
        }

        return risk;
    }

    @Override
    public void saveRisk(TblRisk risk) {
        tblRiskMapper.insert(risk);

    }

    @Override
    public void delRisk(BigDecimal riskid) {
            tblRiskMapper.deleteByriskID(riskid);
    }
}
