package com.global.treasurer.service;

import com.global.treasurer.entity.TcBankDirectConnection;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

public interface TcBankDirectConnectionService {
    PageInfo<TcBankDirectConnection> list(Integer pageNum, Integer pageSize, Map<String, Object> params);
    TcBankDirectConnection getById(String id);
    boolean save(TcBankDirectConnection connection);
    boolean update(TcBankDirectConnection connection);
    boolean delete(String id);
    boolean batchDelete(List<String> ids);
    Map<String, Object> testConnection(String id);
    Map<String, Object> batchTestConnection();
    Map<String, Object> getStatistics();
    List<TcBankDirectConnection> exportConnections(Map<String, Object> params);
}

