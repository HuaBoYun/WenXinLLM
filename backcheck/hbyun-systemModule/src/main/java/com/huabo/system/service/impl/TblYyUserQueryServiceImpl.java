package com.huabo.system.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblYyUserQuery;
import com.huabo.system.mapper.TblYyUserQueryMapper;
import com.huabo.system.service.TblYyUserQueryService;

@Service
public class TblYyUserQueryServiceImpl implements TblYyUserQueryService {

    @Resource
    private TblYyUserQueryMapper tblYyUserQueryMapper;


    @Override
    public Map<String, Object> selectUserQueryById(Integer recordId) throws Exception {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            TblYyUserQuery tblYyUserQuery = tblYyUserQueryMapper.selectByrecordId(recordId);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", tblYyUserQuery);
            return resultMap;
    }
}
