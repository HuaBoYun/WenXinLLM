package com.huabo.monitor.service.impl;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblAssesstemple;
import com.huabo.monitor.service.TblAssesstempleService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service("TblAssesstempleService")
public class TblAssesstempleServiceImpl implements TblAssesstempleService {
    @Override
    public List<TblAssesstemple> getTmplByNumber(String templeNumber, BigDecimal orgid) {
        return null;
    }

    @Override
    public Map<String, Object> findAll(String toString, Integer pageNumber, Integer pageSize, TblAssesstemple assesstemple) {
        return null;
    }

    @Override
    public TblAssesstemple findByid(JsonBean tmplId) {
        return null;
    }

    @Override
    public JsonBean add(TblAssesstemple tblAssesstemple) {
        return null;
    }

    @Override
    public void modify(TblAssesstemple tblAssesstemple) {

    }

    @Override
    public JsonBean findByPageBean(Integer pageNumber, Integer pageSize, TblAssesstemple tblAssesstemple) {
        return null;
    }
}
