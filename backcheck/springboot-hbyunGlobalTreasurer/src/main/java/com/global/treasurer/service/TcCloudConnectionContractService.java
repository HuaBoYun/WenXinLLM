package com.global.treasurer.service;

import com.global.treasurer.entity.TcCloudConnectionContract;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

public interface TcCloudConnectionContractService {
    PageInfo<TcCloudConnectionContract> list(Integer pageNum, Integer pageSize, Map<String, Object> params);
    TcCloudConnectionContract getById(String id);
    boolean save(TcCloudConnectionContract contract);
    boolean update(TcCloudConnectionContract contract);
    boolean delete(String id);
    boolean batchDelete(List<String> ids);
    Map<String, Object> getStatistics();
    List<TcCloudConnectionContract> getRenewalAlerts();
    List<TcCloudConnectionContract> getRenewalAlerts(Integer days);
    List<Map<String, String>> getServiceProviders();
    List<Map<String, String>> getServiceTypes();
    List<TcCloudConnectionContract> exportContracts(Map<String, Object> params);
}

