package com.global.treasurer.service.impl;

import com.global.treasurer.entity.DataMappingConfig;
import com.global.treasurer.mapper.DataMappingConfigMapper;
import com.global.treasurer.service.DataMappingConfigService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 数据映射配置服务实现
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@Service
public class DataMappingConfigServiceImpl implements DataMappingConfigService {
    private static final Logger log = LoggerFactory.getLogger(DataMappingConfigServiceImpl.class);

    @Resource
    private DataMappingConfigMapper dataMappingConfigMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String, Object> getMappingPage(Map<String, Object> param) {
        // 使用PageHelper分页，添加默认值处理
        // 支持pageNo/page和pageSize/limit两种参数命名方式
        int pageNo = 1;
        if (param.get("pageNo") != null) {
            pageNo = Integer.parseInt(param.get("pageNo").toString());
        } else if (param.get("page") != null) {
            pageNo = Integer.parseInt(param.get("page").toString());
        }

        int pageSize = 20;
        if (param.get("pageSize") != null) {
            pageSize = Integer.parseInt(param.get("pageSize").toString());
        } else if (param.get("limit") != null) {
            pageSize = Integer.parseInt(param.get("limit").toString());
        }

        com.github.pagehelper.PageHelper.startPage(pageNo, pageSize);

        List<DataMappingConfig> list = dataMappingConfigMapper.selectMappingPage(param);

        // PageInfo格式
        com.github.pagehelper.PageInfo<DataMappingConfig> pageInfo = new com.github.pagehelper.PageInfo<>(list);

        Map<String, Object> result = new HashMap<>();
        result.put("tlist", list);
        result.put("totalRecord", pageInfo.getTotal());
        result.put("pageNo", pageInfo.getPageNum());
        result.put("pageSize", pageInfo.getPageSize());

        return result;
    }

    @Override
    public DataMappingConfig getMappingById(Long id) {
        return dataMappingConfigMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createMapping(DataMappingConfig mapping) {
        // 获取当前用户信息
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        mapping.setCreatedBy(loginStaff.getStaffid().longValue());
        mapping.setCreatedByName(loginStaff.getRealname());
        mapping.setCreatedTime(new Date());
        mapping.setDeleteFlag(0);

        return dataMappingConfigMapper.insert(mapping);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateMapping(DataMappingConfig mapping) {
        TblStaffUtil loginStaff;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("用户信息获取失败", e);
        }

        if (loginStaff == null) {
            throw new RuntimeException("用户信息获取失败");
        }

        mapping.setUpdatedBy(loginStaff.getStaffid().longValue());
        mapping.setUpdatedByName(loginStaff.getRealname());
        mapping.setUpdatedTime(new Date());

        return dataMappingConfigMapper.updateById(mapping);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Long> ids) {
        return dataMappingConfigMapper.batchDelete(ids);
    }

    @Override
    public Map<String, Object> testMapping(Map<String, Object> param) {
        Map<String, Object> result = dataMappingConfigMapper.testMapping(param);
        if (result == null || result.isEmpty()) {
            result = new HashMap<>();
            result.put("success", 0);
            result.put("message", "映射规则不存在");
        }
        return result;
    }
}
