package com.huabo.monitor.service.impl;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblTestElement;
import com.huabo.monitor.service.TblTestElementService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TblTestElementServiceImpl implements TblTestElementService {
    @Override
    public Integer findByPlan(String planid) {
        return null;
    }

    @Override
    public Integer findByPlanwStra(String planid) {
        return null;
    }

    @Override
    public boolean checkCode(String code, BigDecimal templId) {
        return false;
    }

    @Override
    public String fingBynode(String node, String templId, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public List<TblTestElement> fingBynode(String node, String templId) {
        return null;
    }

    @Override
    public String fingByTree(String node, String templId, String planid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public String fingByTreeCSRW(String node, String templId, String planid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public String fingByTreeCSRWByUser(String node, String templId, String planid, String userid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public String fingByPlanAllCSRW(String planid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public String fingByPlanAll(String planid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public List<TblTestElement> findByPlanidAll(String planid) {
        return null;
    }

    @Override
    public List<Object[]> fingByTreeVSRWCZ(String node, String templId, String planid, String taskid) {
        return null;
    }

    @Override
    public List<Object[]> fingByTreeCSRWCZ(String templId) {
        return null;
    }

    @Override
    public String fingByTreeCSJGY(String node, String templId, String planid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public String fingByTreeCSHZByUser(String node, String templId, String planid, String userid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public String fingByTreeCSJGW(String node, String templId, String planid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public JsonBean fingByTreeCSJGB(String token, Integer pageNumber, String node, String templId, String planid) {
        return null;
    }

    @Override
    public void deleteByTempId(BigDecimal templId) {

    }

    @Override
    public void csmbImport(String path, BigDecimal tmplId) throws IOException {

    }

    @Override
    public String fingByTreeCSRWByUserReturn(String node, String templId, String planid, String userid, Integer pageNumber, int pageSize) {
        return null;
    }

    @Override
    public List<Object[]> findElementByPlanid(String planid) {
        return null;
    }
}
