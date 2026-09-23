package com.financial.sharing.service.impl;

import com.financial.sharing.service.FinanceBuDataService;
import com.financial.sharing.oracle.mapper.FinanceBuDataMapper;
import com.financial.sharing.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BU数据服务实现类
 */
@Service
public class FinanceBuDataServiceImpl implements FinanceBuDataService {

    @Autowired
    private FinanceBuDataMapper financeBuDataMapper;

    @Override
    public List<Map<String, Object>> getBuDataList(Map<String, Object> params) {
        return financeBuDataMapper.selectBuDataList(params);
    }

    @Override
    public Map<String, Object> getBuDataDetail(String buId) {
        return financeBuDataMapper.selectBuDataById(buId);
    }

    @Override
    public int createBuData(Map<String, Object> buData) {
        return financeBuDataMapper.insertBuData(buData);
    }

    @Override
    public int updateBuData(Map<String, Object> buData) {
        return financeBuDataMapper.updateBuData(buData);
    }

    @Override
    public int deleteBuData(String buId) {
        return financeBuDataMapper.deleteBuData(buId);
    }

    @Override
    public Map<String, Object> getBuDataStatistics(Map<String, Object> params) {
        return financeBuDataMapper.selectBuDataStatistics(params);
    }

    @Override
    public int importBuData(List<Map<String, Object>> buDataList) {
        return financeBuDataMapper.batchInsertBuData(buDataList);
    }

    @Override
    public List<Map<String, Object>> exportBuData(Map<String, Object> params) {
        return financeBuDataMapper.selectBuDataForExport(params);
    }

    // ==================== Controller 需要的方法实现 ====================

    @Override
    public PageResult<Map<String, Object>> getYWSJList(Map<String, Object> params) {
        List<Map<String, Object>> list = financeBuDataMapper.selectYWSJList(params);
        int total = financeBuDataMapper.countYWSJList(params);
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public Map<String, Object> getYWSJAdd(Map<String, Object> data) {
        financeBuDataMapper.insertYWSJ(data);
        return data;
    }

    @Override
    public Map<String, Object> getYWSJEdit(Map<String, Object> data) {
        financeBuDataMapper.updateYWSJ(data);
        return data;
    }

    @Override
    public Map<String, Object> getYWSJDetail(Map<String, Object> params) {
        return financeBuDataMapper.selectYWSJDetail(params);
    }

    @Override
    public boolean getYWSJDelete(Map<String, Object> params) {
        return financeBuDataMapper.deleteYWSJ(params) > 0;
    }

    @Override
    public boolean YWSJFabu(Map<String, Object> params) {
        return financeBuDataMapper.publishYWSJ(params) > 0;
    }

    @Override
    public boolean changeStatus(Map<String, Object> params) {
        return financeBuDataMapper.updateYWSJStatus(params) > 0;
    }

    @Override
    public Map<String, Object> startCaiJi(Map<String, Object> params) {
        // 启动采集任务
        financeBuDataMapper.startGatherTask(params);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "started");
        result.put("message", "采集任务已启动");
        return result;
    }

    @Override
    public boolean stopCaiJi(Map<String, Object> params) {
        return financeBuDataMapper.stopGatherTask(params) > 0;
    }

    @Override
    public Map<String, Object> sqlTest(Map<String, Object> data) {
        // 执行SQL测试
        List<Map<String, Object>> testResult = financeBuDataMapper.executeSqlTest(data);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", testResult);
        result.put("message", "SQL测试成功");
        return result;
    }
}