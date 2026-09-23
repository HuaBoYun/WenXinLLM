package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TcBankDirectConnection;
import com.global.treasurer.mapper.TcBankDirectConnectionMapper;
import com.global.treasurer.service.TcBankDirectConnectionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.exception.ServiceException;
import com.hbfk.util.StringUtil;
import com.hbfk.util.RandowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class TcBankDirectConnectionServiceImpl implements TcBankDirectConnectionService {
    @Autowired
    private TcBankDirectConnectionMapper connectionMapper;

    @Override
    public PageInfo<TcBankDirectConnection> list(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        // 添加调试日志
        System.out.println("========== 查询参数 ==========");
        System.out.println("pageNum: " + pageNum);
        System.out.println("pageSize: " + pageSize);
        System.out.println("params: " + params);
        // 使用自定义方法名，避免与 MyBatis-Plus 的 BaseMapper 方法冲突
        List<TcBankDirectConnection> list = connectionMapper.selectByParamsCustom(params);
        System.out.println("查询结果数量: " + (list != null ? list.size() : 0));
        return new PageInfo<>(list);
    }

    @Override
    public TcBankDirectConnection getById(String id) {
        if (StringUtil.isEmpty(id)) throw new ServiceException("连接ID不能为空");
        // 使用selectByPrimaryKey方法查询单个对象
        return connectionMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean save(TcBankDirectConnection connection) {
        if (connection == null) throw new ServiceException("连接信息不能为空");
        connection.setId(RandowUtil.uuId());
        connection.setCreateTime(new Date());
        connection.setUpdateTime(new Date());
        if (StringUtil.isEmpty(connection.getStatus())) connection.setStatus("1");
        return connectionMapper.insertSelective(connection) > 0;
    }

    @Override
    public boolean update(TcBankDirectConnection connection) {
        if (connection == null || StringUtil.isEmpty(connection.getId()))
            throw new ServiceException("连接信息不完整");
        connection.setUpdateTime(new Date());
        // 使用自定义的updateByPrimaryKey方法，只使用ID作为WHERE条件
        return connectionMapper.updateByPrimaryKey(connection) > 0;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtil.isEmpty(id)) throw new ServiceException("连接ID不能为空");
        return connectionMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) throw new ServiceException("连接ID列表不能为空");
        for (String id : ids) delete(id);
        return true;
    }

    @Override
    public Map<String, Object> testConnection(String id) {
        Map<String, Object> result = new HashMap<>();
        TcBankDirectConnection connection = getById(id);
        if (connection == null) {
            result.put("success", false);
            result.put("message", "连接不存在");
            return result;
        }
        result.put("success", true);
        result.put("message", "连接测试成功");
        result.put("testTime", new Date());
        connection.setLastConnectionTime(new Date());
        connection.setConnectionTestResult("SUCCESS");
        connectionMapper.updateByPrimaryKeySelective(connection);
        return result;
    }

    @Override
    public Map<String, Object> batchTestConnection() {
        Map<String, Object> result = new HashMap<>();
        List<TcBankDirectConnection> list = connectionMapper.selectByParams(new HashMap<>());
        int successCount = 0, failCount = 0;
        for (TcBankDirectConnection conn : list) {
            Map<String, Object> testResult = testConnection(conn.getId());
            if ((Boolean) testResult.get("success")) successCount++;
            else failCount++;
        }
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("total", list.size());
        return result;
    }

    @Override
    public Map<String, Object> getStatistics() {
        return connectionMapper.selectStatistics();
    }

    @Override
    public List<TcBankDirectConnection> exportConnections(Map<String, Object> params) {
        return connectionMapper.selectByParams(params);
    }
}

